package sfiomn.legendarydungeonfeatures.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;

import java.util.List;

public class CreativeTabRegistry {
    public static final DeferredRegister<CreativeModeTab> ITEM_GROUPS = DeferredRegister.create(Registries.CREATIVE_MODE_TAB, LegendaryDungeonFeatures.MOD_ID);

    public static final RegistryObject<CreativeModeTab> LEGENDARY_CREATURES_TAB = ITEM_GROUPS.register("legendary_additions", () -> CreativeModeTab.builder()
            .icon(() -> ItemRegistry.FOREST_KEY.get().getDefaultInstance())
            .displayItems((parameters, list) ->
            {
                list.acceptAll(List.of(
                        BlockRegistry.FOREST_DUNGEON_GATE_BLOCK.get().asItem().getDefaultInstance(),
                        BlockRegistry.FOREST_DUNGEON_HEART_BLOCK.get().asItem().getDefaultInstance()
                ));
            })
            .title(Component.translatable("itemGroup." + LegendaryDungeonFeatures.MOD_ID))
            .build());

    public static void register(IEventBus eventBus) {
        ITEM_GROUPS.register(eventBus);
    }
}
