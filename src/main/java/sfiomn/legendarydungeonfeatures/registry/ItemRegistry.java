package sfiomn.legendarydungeonfeatures.registry;

import net.minecraft.world.item.Item;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.items.DesertKeyItem;
import sfiomn.legendarydungeonfeatures.items.ForestKeyItem;

public class ItemRegistry {

    public static final DeferredRegister<Item> ITEMS = DeferredRegister.create(ForgeRegistries.ITEMS, LegendaryDungeonFeatures.MOD_ID);

    public static final RegistryObject<Item> FOREST_KEY = ITEMS.register("forest_key", () -> new ForestKeyItem(new Item.Properties()));
    public static final RegistryObject<Item> DESERT_KEY = ITEMS.register("desert_key", () -> new DesertKeyItem(new Item.Properties()));

    public static void register(IEventBus eventBus) {
        ITEMS.register(eventBus);
    }
}
