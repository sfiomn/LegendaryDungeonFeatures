package sfiomn.legendarydungeonfeatures.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendary_additions.registry.EffectRegistry;

public abstract class AbstractDungeonHeartBlockEntity extends BlockEntity {

    protected static final AnimationBuilder IDLE = new AnimationBuilder().addAnimation("idle", ILoopType.EDefaultLoopTypes.LOOP);
    protected static final AnimationBuilder DEACTIVATED = new AnimationBuilder().addAnimation("deactivated", ILoopType.EDefaultLoopTypes.PLAY_ONCE);
    private int rangeXPos;
    private int rangeXNeg;
    private int rangeYPos;
    private int rangeYNeg;
    private int rangeZPos;
    private int rangeZNeg;
    private boolean active;
    private int updateCounter;
    public AbstractDungeonHeartBlockEntity(BlockEntityType<?> entityType, BlockPos blockPos, BlockState blockState) {
        super(entityType, blockPos, blockState);
        this.active = true;
        this.rangeXPos = this.rangeXNeg = this.rangeYPos = this.rangeYNeg = this.rangeZPos = this.rangeZNeg = 0;
    }

    @Override
    public void tick() {
        if (this.level == null)
            return;

        if(isActive() && !this.level.isClientSide){
            if(this.updateCounter++ > 60){
                AABB box = new AABB(this.worldPosition.getX()-rangeXNeg,
                        this.worldPosition.getY()-rangeYNeg,
                        this.worldPosition.getZ()-rangeZNeg,
                        this.worldPosition.getX()+rangeXPos+1,
                        this.worldPosition.getY()+rangeYPos+1,
                        this.worldPosition.getZ()+rangeZPos+1);

                for (Player player: this.level.players())
                    if (box.contains(player.position()))
                        player.addEffect(new MobEffectInstance(EffectRegistry.DUNGEON_HEART.get(), 120, 0, false, false, true));
            }
        }
    }

    protected <E extends AbstractDungeonHeartBlockEntity> PlayState animController(final AnimationEvent<E> event) {
        if (this.isActive()) {
            event.getController().setAnimation(IDLE);
        } else {
            event.getController().setAnimation(DEACTIVATED);
            if (event.getController().getAnimationState().equals(AnimationState.Stopped)) {
                return PlayState.STOP;
            }
        }
        return PlayState.CONTINUE;
    }

    @Override
    public void registerControllers(AnimationData data) {
        data.addAnimationController(new AnimationController<>(this, "animController", 10, this::animController));
    }

    public void setRange(Direction direction, int rangeValue) {
        switch (direction) {
            case EAST:
                this.setRangeXPos(rangeValue);
                break;
            case WEST:
                this.setRangeXNeg(rangeValue);
                break;
            case UP:
                this.setRangeYPos(rangeValue);
                break;
            case DOWN:
                this.setRangeYNeg(rangeValue);
                break;
            case SOUTH:
                this.setRangeZPos(rangeValue);
                break;
            case NORTH:
                this.setRangeZNeg(rangeValue);
                break;
        }
    }

    public void setRangeXPos(int rangeValue) {
        this.rangeXPos = rangeValue;
        this.updateClient();
    }

    public void setRangeXNeg(int rangeValue) {
        this.rangeXNeg = rangeValue;
        this.updateClient();
    }

    public void setRangeYPos(int rangeValue) {
        this.rangeYPos = rangeValue;
        this.updateClient();
    }

    public void setRangeYNeg(int rangeValue) {
        this.rangeYNeg = rangeValue;
        updateClient();
    }

    public void setRangeZPos(int rangeValue) {
        this.rangeZPos = rangeValue;
        updateClient();
    }

    public void setRangeZNeg(int rangeValue) {
        this.rangeZNeg = rangeValue;
        updateClient();
    }

    public void setActive(boolean active) {
        this.active = active;
        updateClient();
    }

    private void updateClient() {
        if (this.level == null)
            return;
        this.setChanged();
        this.level.sendBlockUpdated(this.getBlockPos(), this.getBlockState(), this.getBlockState(), 3);
    }

    public int getRangeXPos() {
        return rangeXPos;
    }

    public int getRangeXNeg() {
        return rangeXNeg;
    }

    public int getRangeYPos() {
        return rangeYPos;
    }

    public int getRangeYNeg() {
        return rangeYNeg;
    }

    public int getRangeZPos() {
        return rangeZPos;
    }

    public int getRangeZNeg() {
        return rangeZNeg;
    }

    public boolean isActive() {
        return active;
    }

    @Override
    public void load(@NotNull CompoundTag nbt) {
        super.load(nbt);
        this.setRangeXPos(nbt.getInt("rangeXPos"));
        this.setRangeXNeg(nbt.getInt("rangeXNeg"));
        this.setRangeYPos(nbt.getInt("rangeYPos"));
        this.setRangeYNeg(nbt.getInt("rangeYNeg"));
        this.setRangeZPos(nbt.getInt("rangeZPos"));
        this.setRangeZNeg(nbt.getInt("rangeZNeg"));
        this.setActive(nbt.getBoolean("active"));
    }

    @Override
    protected void saveAdditional(@NotNull CompoundTag nbt) {
        super.saveAdditional(nbt);
        nbt.putInt("rangeXPos", this.getRangeXPos());
        nbt.putInt("rangeXNeg", this.getRangeXNeg());
        nbt.putInt("rangeYPos", this.getRangeYPos());
        nbt.putInt("rangeYNeg", this.getRangeYNeg());
        nbt.putInt("rangeZPos", this.getRangeZPos());
        nbt.putInt("rangeZNeg", this.getRangeZNeg());
        nbt.putBoolean("active", this.isActive());
    }

    @Override
    public ClientboundBlockEntityDataPacket getUpdatePacket() {
        return ClientboundBlockEntityDataPacket.create(this);
    }

    @Override
    public @NotNull CompoundTag getUpdateTag() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("rangeXPos", this.getRangeXPos());
        nbt.putInt("rangeXNeg", this.getRangeXNeg());
        nbt.putInt("rangeYPos", this.getRangeYPos());
        nbt.putInt("rangeYNeg", this.getRangeYNeg());
        nbt.putInt("rangeZPos", this.getRangeZPos());
        nbt.putInt("rangeZNeg", this.getRangeZNeg());
        nbt.putBoolean("active", this.isActive());
        return nbt;
    }
}
