package sfiomn.legendarydungeonfeatures.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendary_additions.config.Config;
import sfiomn.legendary_additions.registry.BlockEntityRegistry;

import javax.annotation.Nullable;

public class ForestDungeonHeartBlock extends AbstractDungeonHeartBlock {

    public static Properties properties = getProperties();

    public static Properties getProperties()
    {
        Properties properties =
                Properties
                        .of()
                        .mapColor(MapColor.WOOD)
                        .sound(SoundType.WOOD)
                        .strength(12f, 1200f)
                        .noOcclusion();

        if (!Config.Baked.forestDungeonHeartBreakable)
            properties.strength(-1.0f, 3600000.0F);

        return properties;
    }

    public ForestDungeonHeartBlock() {
        super(properties);
    }

    @SuppressWarnings("deprecation")
    @Override
    public void onPlace(BlockState state, Level world, BlockPos pos, BlockState oldState, boolean isMoving)
    {
        if (world.isEmptyBlock(pos.above()) && isBase(state))
            world.setBlockAndUpdate(pos.above(), state.setValue(AbstractDungeonHeartBlock.BASE, false));
    }

    @Nullable
    @Override
    public BlockState getStateForPlacement(BlockItemUseContext context) {
        if (!canPlace(context))
            return null;
        return super.getStateForPlacement(context);
    }

    private boolean canPlace(BlockItemUseContext context) {
        World world = context.getLevel();
        BlockPos pos = context.getClickedPos();

        if (pos.getY() > world.getHeight() - 2) {
            return false;
        }

        if (world.getBlockState(pos.above()).isSolidRender(world, pos.above())) {
            return false;
        }

        return true;
    }

    @Override
    public boolean canDrop() {
        return Config.Baked.forestDungeonHeartDrop;
    }

    @Override
    public boolean isBreakable() {
        return Config.Baked.forestDungeonHeartBreakable;
    }

    @Override
    public boolean canDeactivateBySignal() {
        return Config.Baked.forestDungeonHeartDeactivationByRedStone;
    }

    @Override
    public ResourceLocation getItemForDeactivation() {
        return new ResourceLocation(Config.Baked.forestDungeonHeartDeactivationByItem);
    }

    @Nullable
    @Override
    public BlockEntity newBlockEntity(@NotNull BlockPos blockPos, @NotNull BlockState blockState) {
        return BlockEntityRegistry.FOREST_DUNGEON_HEART_BLOCK_ENTITY.get().create(blockPos, blockState);
    }
}
