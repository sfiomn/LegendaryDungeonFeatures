package sfiomn.legendarydungeonfeatures.registry;

import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.blockentities.ForestDungeonGateBlockEntity;
import sfiomn.legendarydungeonfeatures.blockentities.ForestDungeonHeartBlockEntity;

public class BlockEntityRegistry {
    public static DeferredRegister<BlockEntityType<?>> BLOCK_ENTITIES =
            DeferredRegister.create(ForgeRegistries.BLOCK_ENTITY_TYPES, LegendaryDungeonFeatures.MOD_ID);

    public static RegistryObject<BlockEntityType<ForestDungeonGateBlockEntity>> FOREST_DUNGEON_GATE_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendaryDungeonFeatures.MOD_ID + "forest_dungeon_gate_tile_entity", () -> BlockEntityType.Builder
                    .of(ForestDungeonGateBlockEntity::new, BlockRegistry.FOREST_DUNGEON_GATE_BLOCK.get()).build(null));

    public static RegistryObject<BlockEntityType<ForestDungeonHeartBlockEntity>> FOREST_DUNGEON_HEART_BLOCK_ENTITY =
            BLOCK_ENTITIES.register(LegendaryDungeonFeatures.MOD_ID + "forest_dungeon_heart_tile_entity", () -> BlockEntityType.Builder
                    .of(ForestDungeonHeartBlockEntity::new, BlockRegistry.FOREST_DUNGEON_HEART_BLOCK.get()).build(null));


    public static void register(IEventBus eventBus) {
        BLOCK_ENTITIES.register(eventBus);
    }
}
