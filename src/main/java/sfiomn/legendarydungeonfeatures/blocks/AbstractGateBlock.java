package sfiomn.legendarydungeonfeatures.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.network.chat.Component;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.SpawnEggItem;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.PushReaction;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.common.ForgeMod;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendarydungeonfeatures.blockentities.AbstractGateBlockEntity;
import sfiomn.legendarydungeonfeatures.util.GatePartUtil;
import sfiomn.legendarydungeonfeatures.util.IGatePart;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class AbstractGateBlock extends BaseEntityBlock implements SimpleWaterloggedBlock {
    public static final BooleanProperty OPENED = BooleanProperty.create("opened");
    public static final BooleanProperty WATERLOGGED = BlockStateProperties.WATERLOGGED;
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;
    public final GatePartUtil gatePartUtil;

    public AbstractGateBlock(Properties properties, IGatePart[] gateParts) {
        super(properties);

        this.gatePartUtil = new GatePartUtil(gateParts);

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(OPENED, false)
                .setValue(WATERLOGGED, Boolean.FALSE)
                .setValue(FACING, Direction.SOUTH));
    }

    @Override
    public RenderShape getRenderShape(BlockState state) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(OPENED, WATERLOGGED, FACING);
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockPlaceContext context)
    {
        if (!canPlace(context))
            return null;

        BlockPos blockpos = context.getClickedPos();
        Level level = context.getLevel();
        FluidState fluidState = level.getFluidState(blockpos);
        int gateHeight = this.gatePartUtil.getHeight();
        return (blockpos.getY() + gateHeight) < 255 ?
                this.defaultBlockState()
                        .setValue(FACING, context.getHorizontalDirection().getOpposite())
                        .setValue(WATERLOGGED, fluidState.getType() == Fluids.WATER) : null;
    }

    @Override
    public PushReaction getPistonPushReaction(BlockState p_149656_1_) {
        return PushReaction.BLOCK;
    }

    @Override
    public InteractionResult use(BlockState blockstate, Level level, BlockPos pos, Player player, InteractionHand hand,
                                 BlockHitResult hit) {
        super.use(blockstate, level, pos, player, hand, hit);

        if (new Vec3(pos.getX(), pos.getY(), pos.getZ()).distanceTo(player.position()) > player.getAttributeValue(ForgeMod.BLOCK_REACH.get()))
            return InteractionResult.FAIL;

        IGatePart gatePart = this.getGatePart(blockstate);
        if (gatePart == null)
            return InteractionResult.FAIL;

        // retrieve the tile entity that controls the DungeonGate
        BlockEntity tileEntity = level.getBlockEntity(getBasePos(blockstate, pos));
        if (tileEntity instanceof AbstractGateBlockEntity dungeonGateTE) {

            if (player.isCreative() && player.getMainHandItem().getItem() instanceof SpawnEggItem eggInHand) {
                dungeonGateTE.setEntityLock(eggInHand.getType(player.getMainHandItem().getTag()));
                player.getMainHandItem().shrink(1);

                if (level.isClientSide && dungeonGateTE.getEntityLock().isPresent())
                    player.displayClientMessage(Component.translatable("block.legendary_additions.gate.entityLockApplied", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(dungeonGateTE.getEntityLock().get())).getPath()), true);

                return InteractionResult.SUCCESS;
            }
            Direction gateFacing = blockstate.getValue(FACING);
            Direction insertFacing = gateFacing.getAxis() == Direction.Axis.X ?
                    (hit.getLocation().x - Math.floor(hit.getLocation().x)) >= 0.5 ? Direction.WEST : Direction.EAST:
                    (hit.getLocation().z - Math.floor(hit.getLocation().z)) >= 0.5 ? Direction.NORTH : Direction.SOUTH;
            boolean keyInserted = dungeonGateTE.insertKey(player, hit.getLocation(), insertFacing);
            if (dungeonGateTE.isUnlocked() && !dungeonGateTE.isOpened()) {
                dungeonGateTE.openGate();
            } else if (dungeonGateTE.isOpened() && canClose()) {
                dungeonGateTE.closeGate();
            } else if (!level.isClientSide && !keyInserted) {
                dungeonGateTE.playFailedToOpenSound();
                if (!dungeonGateTE.checkEntityUnlocked() && dungeonGateTE.getEntityLock().isPresent())
                    player.displayClientMessage(Component.translatable("block.legendary_additions.gate.entityLocked", Objects.requireNonNull(ForgeRegistries.ENTITY_TYPES.getKey(dungeonGateTE.getEntityLock().get())).getPath()), true);
            }
        }

        return InteractionResult.SUCCESS;
    }

    @Override
    public BlockState updateShape(BlockState blockState, Direction facing, BlockState facingState, LevelAccessor level, BlockPos pos, BlockPos facingPos) {
        if (blockState.getValue(WATERLOGGED)) {
            level.scheduleTick(pos, Fluids.WATER, Fluids.WATER.getTickDelay(level));
        }

        if (!this.canSurvive(blockState, level, pos)) {
            this.removeGate(level, pos, blockState);
            return Blocks.AIR.defaultBlockState();
        }

        return blockState;
    }

    @Override
    public void onRemove(BlockState blockState, Level level, BlockPos pos, BlockState newBlockState, boolean isMoving) {
        if (!blockState.is(newBlockState.getBlock())) {
            BlockPos basePos = getBasePos(blockState, pos);
            BlockEntity blockEntity = level.getBlockEntity(basePos);
            if (blockEntity instanceof AbstractGateBlockEntity gateBlockEntity) {
                if (gateBlockEntity.isOpened() || !isOpenPart(blockState)) {
                    this.removeGate(level, pos, blockState);
                    gateBlockEntity.dropKeys();
                }
                super.onRemove(blockState, level, pos, newBlockState, isMoving);
            }
        }
    }

    @Override
    public @NotNull List<ItemStack> getDrops(BlockState state, LootParams.Builder context) {
        List<ItemStack> lootList = super.getDrops(state, context);
        if (!getGatePart(state).isBase() || !canDropGate())
            lootList.removeIf(e -> (e.getItem() instanceof BlockItem && ((BlockItem) e.getItem()).getBlock() instanceof AbstractGateBlock));

        return lootList;
    }

    public boolean canPlace(BlockPlaceContext context) {
        Level level = context.getLevel();
        BlockPos basePos = context.getClickedPos();
        Direction gateFacing = context.getHorizontalDirection().getOpposite();
        for (IGatePart part: this.gatePartUtil.getClosedGateParts()) {
            try {
                BlockPos partPos = basePos.offset(part.offset(gateFacing));
                BlockState partState = level.getBlockState(partPos);
                if (!partState.canBeReplaced(context)) {
                    return false;
                }
            } catch (NullPointerException e) {
                return false;
            }
        }

        return true;
    }

    public void removeGate(Level level, BlockPos pos, BlockState state) {
        if (level.isClientSide()) {
            return;
        }

        BlockPos basePos = getBasePos(state, pos);
        List<IGatePart> partsToRemove = this.gatePartUtil.getClosedGateParts();
        if (state.getValue(OPENED))
            partsToRemove = this.gatePartUtil.gateParts();

        for (IGatePart part: partsToRemove) {
            BlockPos partPos = basePos.offset(part.offset(state.getValue(FACING)));
            BlockState partState = level.getBlockState(partPos);
            if (partState.getBlock() ==this) {
                if (part.isBase()) {
                    level.destroyBlock(partPos, true);
                } else {
                    level.setBlock(partPos, partState.getValue(WATERLOGGED) ? Blocks.WATER.defaultBlockState() : Blocks.AIR.defaultBlockState(), 35);
                    level.levelEvent(2001, partPos, Block.getId(partState));
                }
            }
        }
    }

    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        IGatePart part = getGatePart(state);
        if (part.isBase())
            return pos;
        else
            return pos.offset(getGatePart(state).reverseOffset(state.getValue(FACING)));
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState);

    abstract IGatePart getGatePart(BlockState blockState);

    abstract public boolean canClose();

    abstract public boolean isBreakable();
    abstract public boolean canDropGate();

    abstract public boolean isOpenPart(BlockState state);

    public BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState)pState.setValue(FACING, pRot.rotate((Direction)pState.getValue(FACING)));
    }

    public BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation((Direction)pState.getValue(FACING)));
    }
}
