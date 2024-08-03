package sfiomn.legendarydungeonfeatures.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.NonNullList;
import net.minecraft.core.Vec3i;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.phys.Vec3;
import sfiomn.legendary_additions.blocks.AbstractGateBlock;
import sfiomn.legendary_additions.blocks.ForestDungeonGateBlock;
import sfiomn.legendary_additions.config.Config;
import sfiomn.legendary_additions.registry.BlockEntityRegistry;
import sfiomn.legendary_additions.registry.BlockRegistry;
import sfiomn.legendary_additions.registry.SoundRegistry;
import sfiomn.legendary_additions.util.IGatePart;
import sfiomn.legendary_additions.util.Lock;

public class ForestDungeonGateBlockEntity extends AbstractGateBlockEntity {
    public ForestDungeonGateBlockEntity(BlockPos blockPos, BlockState blockState) {
        super(BlockEntityRegistry.FOREST_DUNGEON_GATE_BLOCK_ENTITY.get(), blockPos, blockState, ForestDungeonGateBlock.ForestDungeonGatePart.values());

        int SLOT_INDEX = 0;
        locks.add(new Lock(SLOT_INDEX++, new Vec3i(0, 1, 0), new Vec3(0.5, 0.5, 0.5), 0.2, Config.Baked.forestDungeonGateLock1Unlocks));

        this.insertedKeys = NonNullList.withSize(SLOT_INDEX, ItemStack.EMPTY);
    }

    public BlockState getOpenPartBlockState(IGatePart part, BlockPos partPos, Direction gateFacing) {
        if (part instanceof ForestDungeonGateBlock.ForestDungeonGatePart && this.level != null) {
            FluidState fluidState = this.level.getFluidState(partPos);
            return BlockRegistry.FOREST_DUNGEON_GATE_BLOCK.get().defaultBlockState()
                            .setValue(AbstractGateBlock.OPENED, true)
                            .setValue(AbstractGateBlock.WATERLOGGED, fluidState.getType() == Fluids.WATER)
                            .setValue(AbstractGateBlock.FACING, gateFacing)
                            .setValue(ForestDungeonGateBlock.PART, (ForestDungeonGateBlock.ForestDungeonGatePart) part);
        }
        return null;
    }

    @Override
    public void playSuccessfulOpenSound() {
        if (this.level != null)
            this.level.playSound(null, this.worldPosition, SoundRegistry.OPEN_GATE_SUCCESSFUL.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    @Override
    public void playFailedToOpenSound() {
        if (this.level != null)
            this.level.playSound(null, this.worldPosition, SoundRegistry.OPEN_GATE_FAILED.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    @Override
    public void playUnlockSound(Vec3 lockPos) {
        if (this.level != null)
            this.level.playSound(null, lockPos.x, lockPos.y, lockPos.z, SoundRegistry.LOCK_UNLOCKED.get(), SoundSource.NEUTRAL, 1.0f, 1.0f);
    }

    @Override
    public int getMobCheckRangeInBlocks() {
        return Config.Baked.forestDungeonGateMobCheckRange;
    }

    @Override
    public int getMobCheckFrequencyInTicks() {
        return Config.Baked.forestDungeonGateMobCheckFrequency;
    }

    @Override
    public boolean openWhenUnlocked() {
        return Config.Baked.forestDungeonGateOpenWhenUnlocked;
    }

    @Override
    public boolean canDropKeys() {
        return Config.Baked.forestDungeonGateDropKeys;
    }
}
