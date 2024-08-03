package sfiomn.legendarydungeonfeatures.blockentities;

import net.minecraft.client.animation.AnimationChannel;
import net.minecraft.client.animation.AnimationDefinition;
import net.minecraft.client.animation.Keyframe;
import net.minecraft.client.animation.KeyframeAnimations;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BaseContainerBlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendary_additions.blocks.AbstractGateBlock;
import sfiomn.legendary_additions.entities.KeyEntity;
import sfiomn.legendary_additions.items.KeyItem;
import sfiomn.legendary_additions.network.NetworkHandler;
import sfiomn.legendary_additions.network.packets.MessageDungeonGateChange;
import sfiomn.legendary_additions.util.GatePartUtil;
import sfiomn.legendary_additions.util.IGatePart;
import sfiomn.legendary_additions.util.Lock;
import sfiomn.legendary_additions.util.MathUtil;
import sfiomn.legendarydungeonfeatures.util.GatePartUtil;
import sfiomn.legendarydungeonfeatures.util.Lock;

import java.util.*;

public abstract class AbstractGateBlockEntity extends BaseContainerBlockEntity {
    protected NonNullList<ItemStack> insertedKeys;
    protected final GatePartUtil gatePartUtil;
    protected List<Lock> locks;
    private String entityLock;
    private boolean opened;
    private boolean unlocked;
    private int animation;
    private int updateUnlockedCounter;
    private static float openingTime;
    private static final int NO_ANIMATION = 0;
    private static final int OPENING = 1;
    private static final int CLOSING = 2;

    public static final AnimationDefinition OPENING_ANIM = AnimationDefinition.Builder.withLength(2.5f)
            .addAnimation("door1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(0f, -90f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("door2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(0f, 90f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();


    public static final AnimationDefinition CLOSING_ANIM = AnimationDefinition.Builder.withLength(2.5f)
            .addAnimation("door1",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, -90f, 0f),
                                    AnimationChannel.Interpolations.LINEAR),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.LINEAR)))
            .addAnimation("door2",
                    new AnimationChannel(AnimationChannel.Targets.ROTATION,
                            new Keyframe(0f, KeyframeAnimations.degreeVec(0f, 90f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM),
                            new Keyframe(2.5f, KeyframeAnimations.degreeVec(0f, 0f, 0f),
                                    AnimationChannel.Interpolations.CATMULLROM))).build();

    public AbstractGateBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState, IGatePart[] gateParts) {
        super(entityType, blockPos, blockState);
        this.locks = new ArrayList<>();
        this.opened = false;
        this.entityLock = "";
        this.animation = NO_ANIMATION;
        this.unlocked = false;

        this.gatePartUtil = new GatePartUtil(gateParts);
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "gateAnimController", 5, this::gateAnimController));
    }

    protected <E extends AbstractGateBlockEntity> PlayState gateAnimController(final AnimationEvent<E> event) {
        if (this.animation == OPENING) {
            event.getController().setAnimation(OPENING_ANIM);
        } else if (this.animation == CLOSING) {
            event.getController().setAnimation(CLOSING_ANIM);
        }
        if (this.animation == CLOSING || this.animation == OPENING) {
            if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
                this.animation = NO_ANIMATION;
                this.sendResetAnimation();
            }
        }
        return PlayState.CONTINUE;
    }

    public void sendResetAnimation() {
        CompoundTag posNbt = new CompoundTag();
        posNbt.putInt("posX", this.worldPosition.getX());
        posNbt.putInt("posY", this.worldPosition.getY());
        posNbt.putInt("posZ", this.worldPosition.getZ());
        MessageDungeonGateChange messageDungeonGateChange = new MessageDungeonGateChange(posNbt);
        NetworkHandler.INSTANCE.sendToServer(messageDungeonGateChange);
    }

    public static void servertick(Level level, BlockPos pos, BlockState state, AbstractGateBlockEntity entity) {
        if (entity.updateUnlockedCounter++ >= entity.getMobCheckFrequencyInTicks()) {
            entity.updateUnlockedCounter = 0;
            entity.updateUnlocked();
        }

        for (Lock lock: entity.locks) {
            lock.updateTimer();

            // Unlock process + drop keys if failed
            if (lock.getInsertTimerTick() == 0 && !entity.insertedKeys.get(lock.id).isEmpty()) {
                if (lock.canBeUnlocked(entity.insertedKeys.get(lock.id).getItem())) {
                    entity.unlock(lock);
                } else {
                    if (level != null) {
                        Vec3 dropPos = Vec3.atCenterOf(pos);
                        if (lock.getInsertedKeyPosition() != Vec3i.ZERO)
                            dropPos = Vec3.atCenterOf(lock.getInsertedKeyPosition());
                        Entity keyItemEntity = new ItemEntity(level, dropPos.x, dropPos.y, dropPos.z, entity.insertedKeys.get(lock.id));
                        entity.insertedKeys.set(lock.id, ItemStack.EMPTY);
                        level.addFreshEntity(keyItemEntity);
                    }
                }
                lock.keyStopInserting();
            }
        }
    }

    protected boolean checkEntityUnlocked(EntityType<?> entityTypeLock) {
        assert this.level != null;
        int range = getMobCheckRangeInBlocks();
        AABB box = new AABB(this.worldPosition.getX()-range,
                this.worldPosition.getY()-(range*0.5),
                this.worldPosition.getZ()-range,
                this.worldPosition.getX()+range,
                this.worldPosition.getY()+(range*0.5),
                this.worldPosition.getZ()+range);
        Set<LivingEntity> matchedEntities = new HashSet<>(this.level.getEntitiesOfClass(
                LivingEntity.class, box, entity -> Objects.nonNull(entity) && entity.getType() == entityTypeLock));
        return matchedEntities.isEmpty();
    }

    abstract BlockState getOpenPartBlockState(IGatePart part, BlockPos partPos, Direction gateFacing);

    public abstract void playSuccessfulOpenSound();
    public abstract void playFailedToOpenSound();
    public abstract void playUnlockSound(Vec3 lockPos);
    public abstract boolean openWhenUnlocked();
    public abstract int getMobCheckRangeInBlocks();
    public abstract int getMobCheckFrequencyInTicks();

    abstract public boolean canDropKeys();

    public void resetAnimation() {
        this.animation = NO_ANIMATION;
    }

    public boolean openGate() {
        if (this.level != null && this.animation == NO_ANIMATION) {
            if (isOpenFree()) {
                this.animation = OPENING;
                OPENING_ANIM.looping()
                this.opened = true;
                this.setChanged();

                this.playSuccessfulOpenSound();

                Direction gateFacing = getGateFacing();
                // Update closed gate parts (gateway + hinges) as opened
                for (IGatePart part : this.gatePartUtil.getClosedGateParts()) {
                    BlockState partState = this.level.getBlockState(this.worldPosition.offset(part.offset(gateFacing)));
                    this.level.setBlockAndUpdate(this.worldPosition.offset(part.offset(gateFacing)), partState.setValue(AbstractGateBlock.OPENED, true));
                }
                // Create new blocks in the opened gate part locations
                for (IGatePart part : this.gatePartUtil.getOpenedGateParts()) {
                    BlockPos partPos = this.worldPosition.offset(part.offset(gateFacing));
                    this.level.setBlockAndUpdate(partPos, this.getOpenPartBlockState(part, partPos, gateFacing));
                }
                return true;
            } else {
                this.playFailedToOpenSound();
            }
        }
        return false;
    }

    public void closeGate() {
        if (this.level != null && this.animation == NO_ANIMATION) {
            this.animation = CLOSING;
            this.opened = false;
            this.setChanged();

            Direction gateFacing = getGateFacing();
            for (IGatePart part : this.gatePartUtil.getClosedGateParts()) {
                BlockState partState = this.level.getBlockState(this.worldPosition.offset(part.offset(gateFacing)));
                this.level.setBlockAndUpdate(this.worldPosition.offset(part.offset(gateFacing)), partState.setValue(AbstractGateBlock.OPENED, false));
            }
            // Remove the open gate part blocks
            for (IGatePart part : this.gatePartUtil.getOpenedGateParts()) {
                BlockPos partPos = this.worldPosition.offset(part.offset(gateFacing));
                BlockState partState = this.level.getBlockState(partPos);
                this.level.setBlockAndUpdate(partPos, partState.getValue(AbstractGateBlock.WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState());
            }
        }
    }

    public boolean isOpened() {
        return this.opened;
    }

    public boolean isOpenFree() {
        if (this.level == null)
            return false;
        Direction gateFacing = getGateFacing();

        for (IGatePart part: this.gatePartUtil.getOpenedGateParts()) {
            BlockPos partPos = this.worldPosition.offset(part.offset(gateFacing));
            BlockState partState = this.level.getBlockState(partPos);
            if (!partState.canBeReplaced()) {
                return false;
            }
        }
        return true;
    }

    public Direction getGateFacing() {
        return getBlockState().getValue(AbstractGateBlock.FACING);
    }

    public void setEntityLock(EntityType<?> entityType) {
        this.entityLock = Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(entityType)).toString();
        this.setChanged();
    }

    public Optional<EntityType<?>> getEntityLock() {
        return EntityType.byString(this.entityLock);
    }

    public boolean checkLocksUnlocked() {
        for (Lock lock: this.locks) {
            if (!lock.isUnlocked()) {
                return false;
            }
        }

        return true;
    }

    public boolean checkEntityUnlocked() {
        if (this.getEntityLock().isPresent()) {
            EntityType<?> entityTypeLock = this.getEntityLock().get();
            return this.checkEntityUnlocked(entityTypeLock);
        }
        return true;
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public void updateUnlocked() {
        if (this.level == null || this.level.isClientSide)
            return;

        boolean wasUnlocked = this.unlocked;
        this.unlocked = checkLocksUnlocked() && checkEntityUnlocked();
        if (wasUnlocked == this.unlocked)
            return;

        this.setChanged();
        if (this.unlocked && this.openWhenUnlocked() && !this.isOpened()) {
            if (!this.openGate()) {
                // Force the client to update the gate texture to unlocked if it can't be opened
                this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
            }
        } else {
            // Force the client to update the gate texture
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
        }
    }

    public void dropKeys() {
        if (this.level != null && canDropKeys()) {
            ContainerHelper.dropContents(this.level, this.worldPosition, this.insertedKeys);
        }
    }

    public boolean insertKey(Player player, Vec3 insertLocation, Direction insertDirection) {
        Item insertKey = player.getMainHandItem().getItem();

        if (this.level == null || this.level.isClientSide)
            return false;

        Direction gateFacing = getGateFacing();
        for (Lock lock : this.locks) {
            if (lock.canInsert(insertLocation, gateFacing, this.worldPosition) && this.insertedKeys.get(lock.id).isEmpty()) {
                Vec3 lockPos = lock.getLockPos(gateFacing, this.worldPosition);
                Vec3 insertKeyPos = gateFacing.getAxis() == Direction.Axis.Z ?
                        new Vec3(lockPos.x, lockPos.y, insertLocation.z) :
                        new Vec3(insertLocation.x, lockPos.y, lockPos.z);

                boolean keyInserted = false;
                if (insertKey instanceof KeyItem) {
                    insertKeyItem(lock, (KeyItem) insertKey, insertKeyPos, insertDirection);

                    player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
                    this.insertedKeys.set(lock.id, new ItemStack(insertKey));
                    keyInserted = true;
                } else {
                    if (lock.canBeUnlocked(insertKey)) {
                        unlock(lock);
                        this.playUnlockSound(lockPos);

                        player.getMainHandItem().setCount(player.getMainHandItem().getCount() - 1);
                        this.insertedKeys.set(lock.id, new ItemStack(insertKey));
                        keyInserted = true;
                    }
                }
                this.setChanged();
                return keyInserted;
            }
        }
        return false;
    }

    private void insertKeyItem(Lock lock, KeyItem insertKey, Vec3 insertKeyPos, Direction insertDirection) {
        assert this.level != null;
        KeyEntity newKeyEntity = insertKey.getKeyEntity().create(this.level);
        if (newKeyEntity == null)
            return;

        newKeyEntity.setPos(insertKeyPos.x, insertKeyPos.y, insertKeyPos.z);
        newKeyEntity.setYRot(MathUtil.getAngleFromWest(insertDirection));

        if (lock.canBeUnlocked(insertKey)) {
            lock.setInsertedKeyPosition(Vec3i.ZERO);
            lock.setInsertTimerTick(newKeyEntity.getSuccessfulInsertAnimationTicks());
            newKeyEntity.successfulInsertInLock(this.worldPosition);
        } else {
            lock.setInsertedKeyPosition(new Vec3i(insertKeyPos.x, insertKeyPos.y, insertKeyPos.z).relative(insertDirection.getOpposite(), 1));
            lock.setInsertTimerTick(newKeyEntity.getFailedInsertAnimationTicks());
            newKeyEntity.failedInsertInLock(this.worldPosition);
        }

        this.level.addFreshEntity(newKeyEntity);
    }

    protected void unlock(Lock lock) {
        lock.unlocked();
        this.updateUnlocked();
    }

    @Override
    public AABB getRenderBoundingBox() {
        return new AABB(this.worldPosition.offset(-this.gatePartUtil.getWidth() - 1, 0, -this.gatePartUtil.getWidth() - 1), this.worldPosition.offset(this.gatePartUtil.getWidth() + 1, this.gatePartUtil.getHeight() + 1, this.gatePartUtil.getWidth() + 1));
    }

    @Override
    public boolean canPlaceItem(int slot, ItemStack itemStack) {
        return isItemValid(itemStack.getItem());
    }

    @Override
    public int getContainerSize() {
        return this.insertedKeys.size();
    }

    @Override
    public boolean isEmpty() {
        for(ItemStack itemstack : this.insertedKeys) {
            if (!itemstack.isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int slot) {
        return this.insertedKeys.get(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int amount) {
        return ContainerHelper.removeItem(this.insertedKeys, slot, amount);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        return ContainerHelper.takeItem(this.insertedKeys, slot);
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        this.insertedKeys.set(slot, stack);
        if (stack.getCount() > this.getMaxStackSize()) {
            stack.setCount(this.getMaxStackSize());
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return this.level != null
                && this.level.getBlockEntity(this.worldPosition) == this
                && player.distanceToSqr(this.worldPosition.getX() + 0.5, this.worldPosition.getY() + 0.5, this.worldPosition.getZ() + 0.5) <= 64;
    }

    @Override
    public void clearContent() {
        this.insertedKeys.clear();
    }

    private void syncData() {
        this.setChanged();
        if (this.level != null)
            this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    @Override
    protected Component getDefaultName() {
        return null;
    }

    @Override
    protected AbstractContainerMenu createMenu(int i, Inventory inventory) {
        return null;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.insertedKeys = NonNullList.withSize(this.getContainerSize(), ItemStack.EMPTY);
        ContainerHelper.loadAllItems(nbt, this.insertedKeys);
        this.opened = nbt.getBoolean("opened");
        this.unlocked = nbt.getBoolean("unlocked");
        this.animation = nbt.getInt("animation");
        this.entityLock = nbt.getString("entityLock");
        for (Lock lock: this.locks) {
            int[] lockInfo = nbt.getIntArray("lock" + lock.id);
            if (lockInfo[0] == 1)
                lock.unlocked();
            lock.setInsertTimerTick(lockInfo[1]);
            Vec3i insertPos = new Vec3i(lockInfo[2], lockInfo[3], lockInfo[4]);
            lock.setInsertedKeyPosition(insertPos);
        }
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        ContainerHelper.saveAllItems(nbt, this.insertedKeys);
        nbt.putBoolean("opened", this.opened);
        nbt.putBoolean("unlocked", this.unlocked);
        nbt.putInt("animation", this.animation);
        nbt.putString("entityLock", this.entityLock);
        for (Lock lock: this.locks) {
            List<Integer> lockInfo = new ArrayList<>();
            if (lock.isUnlocked())
                lockInfo.add(1);
            else
                lockInfo.add(0);
            lockInfo.add(lock.getInsertTimerTick());
            lockInfo.add(lock.getInsertedKeyPosition().getX());
            lockInfo.add(lock.getInsertedKeyPosition().getY());
            lockInfo.add(lock.getInsertedKeyPosition().getZ());
            nbt.putIntArray("lock" + lock.id, lockInfo);
        }
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        CompoundTag nbt = this.getUpdateTag();
        ContainerHelper.saveAllItems(nbt, this.insertedKeys);
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.putBoolean("opened", this.opened);
        nbt.putBoolean("unlocked", this.unlocked);
        nbt.putInt("animation", this.animation);
        nbt.putString("entityLock", this.entityLock);
        for (Lock lock: this.locks) {
            List<Integer> lockInfo = new ArrayList<>();
            if (lock.isUnlocked())
                lockInfo.add(1);
            else
                lockInfo.add(0);
            lockInfo.add(lock.getInsertTimerTick());
            lockInfo.add(lock.getInsertedKeyPosition().getX());
            lockInfo.add(lock.getInsertedKeyPosition().getY());
            lockInfo.add(lock.getInsertedKeyPosition().getZ());
            nbt.putIntArray("lock" + lock.id, lockInfo);
        }
        return nbt;
    }
}
