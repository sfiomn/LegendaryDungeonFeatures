package sfiomn.legendarydungeonfeatures.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import sfiomn.legendary_additions.registry.BlockEntityRegistry;

public class ForestDungeonHeartBlockEntity extends AbstractDungeonHeartBlockEntity {
    public ForestDungeonHeartBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.FOREST_DUNGEON_HEART_BLOCK_ENTITY.get(), blockPos, blockState);
    }
}
