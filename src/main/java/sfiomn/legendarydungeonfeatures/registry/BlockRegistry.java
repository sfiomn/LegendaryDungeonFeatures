package sfiomn.legendarydungeonfeatures.registry;

import net.minecraft.world.item.BlockItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.block.Block;
import net.minecraftforge.eventbus.api.IEventBus;
import net.minecraftforge.registries.DeferredRegister;
import net.minecraftforge.registries.ForgeRegistries;
import net.minecraftforge.registries.RegistryObject;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.blocks.ForestDungeonGateBlock;
import sfiomn.legendarydungeonfeatures.blocks.ForestDungeonHeartBlock;

import java.util.function.Supplier;

public class BlockRegistry
{
	public static final DeferredRegister<Block> BLOCKS = DeferredRegister.create(ForgeRegistries.BLOCKS, LegendaryDungeonFeatures.MOD_ID);

	public static final RegistryObject<Block> FOREST_DUNGEON_GATE_BLOCK = registerBlock("forest_dungeon_gate", ForestDungeonGateBlock::new);

	public static final RegistryObject<Block> FOREST_DUNGEON_HEART_BLOCK = registerBlock("forest_dungeon_heart", ForestDungeonHeartBlock::new);

	private static <T extends Block> RegistryObject<Block> registerBlock(String name, Supplier<T> block) {
		RegistryObject<Block> newBlock = BLOCKS.register(name, block);
		registerBlockItem(name, newBlock);
		return newBlock;
	}

	private static <T extends Block> RegistryObject<Item> registerBlockItem(String name, RegistryObject<T> block) {
		return ItemRegistry.ITEMS.register(name, () -> new BlockItem(block.get(), new Item.Properties()));
	}

	public static void register(IEventBus eventBus){
		BLOCKS.register(eventBus);
	}
}
