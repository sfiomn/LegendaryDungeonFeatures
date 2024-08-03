package sfiomn.legendarydungeonfeatures.registry;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.entities.DesertKeyEntity;
import sfiomn.legendarydungeonfeatures.entities.ForestKeyEntity;

public class EntityTypeRegistry {
    public static final DeferredRegister<EntityType<?>> ENTITY_TYPES = DeferredRegister.create(ForgeRegistries.ENTITY_TYPES, LegendaryDungeonFeatures.MOD_ID);

    public static final RegistryObject<EntityType<ForestKeyEntity>> FOREST_KEY_ENTITY = ENTITY_TYPES.register("forest_key",
            () -> EntityType.Builder.of(ForestKeyEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(32)
                    .setUpdateInterval(3)
                    .build(new ResourceLocation(LegendaryDungeonFeatures.MOD_ID, "forest_key").toString()));

    public static final RegistryObject<EntityType<DesertKeyEntity>> DESERT_KEY_ENTITY = ENTITY_TYPES.register("desert_key",
            () -> EntityType.Builder.of(DesertKeyEntity::new, MobCategory.MISC)
                    .sized(0.5F, 0.5F)
                    .clientTrackingRange(32)
                    .setUpdateInterval(3)
                    .build(new ResourceLocation(LegendaryDungeonFeatures.MOD_ID, "desert_key").toString()));


    public static void register(IEventBus eventBus) {
        ENTITY_TYPES.register(eventBus);
    }
}
