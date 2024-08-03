package sfiomn.legendarydungeonfeatures.blocks;


import net.minecraft.client.Minecraft;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.level.block.state.properties.DirectionProperty;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.registries.ForgeRegistries;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.blockentities.AbstractDungeonHeartTileEntity;
import sfiomn.legendary_additions.screens.ClientHooks;
import sfiomn.legendarydungeonfeatures.blockentities.AbstractGateBlockEntity;

import javax.annotation.Nullable;
import java.util.List;
import java.util.Objects;

public abstract class AbstractDungeonHeartBlock extends BaseEntityBlock {
    public static final BooleanProperty BASE = BooleanProperty.create("base");
    public static final DirectionProperty FACING = BlockStateProperties.HORIZONTAL_FACING;

    public AbstractDungeonHeartBlock(Properties properties) {
        super(properties);

        this.registerDefaultState(this.getStateDefinition().any()
                .setValue(BASE, true));
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder)
    {
        builder.add(BASE);
    }

    @Override
    public RenderShape getRenderShape(BlockState p_149645_1_) {
        return RenderShape.ENTITYBLOCK_ANIMATED;
    }

    @SuppressWarnings("deprecation")
    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player player, InteractionHand hand, BlockHitResult rayTrace) {
        BlockPos basePos = getBasePos(state, pos);

        if (Objects.equals(ForgeRegistries.ITEMS.getKey(player.getMainHandItem().getItem()), this.getItemForDeactivation())) {
            BlockEntity blockEntity = world.getBlockEntity(basePos);
            if (blockEntity instanceof AbstractGateBlockEntity be) {
                if (be.isActive()) {
                    be.setActive(false);
                    player.getMainHandItem().shrink(1);
                    return InteractionResult.SUCCESS;
                }
            }
        }

        if (world.isClientSide && player.isCreative() && Minecraft.getInstance().screen == null) {
            BlockEntity blockEntity = world.getBlockEntity(basePos);

            if (blockEntity instanceof AbstractDungeonHeartTileEntity) {
                DistExecutor.runWhenOn(Dist.CLIENT, () -> () -> ClientHooks.openDungeonHeartScreen(blockEntity));
            } else {
                LegendaryAdditions.LOGGER.debug("Tile entity container is not correct at pos {}", basePos);
            }
            return InteractionResult.SUCCESS;
        }

        return InteractionResult.FAIL;
    }

    @Override
    public void onRemove(BlockState state, Level world, BlockPos pos, BlockState newState, boolean p_196243_5_) {
        super.onRemove(state, world, pos, newState, p_196243_5_);
        if (isBase(state)) {
            world.removeBlock(pos.above(), false);
        } else if (!isBase(state)) {
            world.destroyBlock(pos.below(), false);
        }
    }

    @Override
    public void neighborChanged(BlockState state, Level world, BlockPos pos, Block block, BlockPos neighbor, boolean p_220069_6_) {
        super.neighborChanged(state, world, pos, block, neighbor, p_220069_6_);
        BlockPos basePos = getBasePos(state, pos);

        BlockEntity blockEntity = world.getBlockEntity(basePos);
        if(!world.isClientSide() && blockEntity instanceof AbstractDungeonHeartTileEntity && canDeactivateBySignal()) {
            if (world.hasNeighborSignal(basePos))
                ((AbstractDungeonHeartTileEntity) blockEntity).setActive(false);
            else
                ((AbstractDungeonHeartTileEntity) blockEntity).setActive(true);
        }
    }

    @Nullable
    @Override
    public abstract BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState);


    public boolean isBase(BlockState state) {
        return state.getValue(BASE);
    }
    public BlockPos getBasePos(BlockState state, BlockPos pos) {
        return isBase(state) ? pos : pos.below();
    }
    abstract public boolean canDrop();
    abstract public boolean isBreakable();
    abstract public boolean canDeactivateBySignal();
    abstract public ResourceLocation getItemForDeactivation();

    @Override
    public List<ItemStack> getDrops(BlockState pState, LootParams.Builder pParams) {
        List<ItemStack> lootList = this.getDrops(pState, pParams);
        if (!canDrop())
            lootList.removeIf(e -> (e.getItem() instanceof BlockItem && ((BlockItem) e.getItem()).getBlock() instanceof AbstractDungeonHeartBlock));

        return lootList;
    }

    @Override
    public @NotNull BlockState rotate(BlockState pState, Rotation pRot) {
        return (BlockState)pState.setValue(FACING, pRot.rotate((Direction)pState.getValue(FACING)));
    }

    @Override
    public @NotNull BlockState mirror(BlockState pState, Mirror pMirror) {
        return pState.rotate(pMirror.getRotation((Direction)pState.getValue(FACING)));
    }
}
