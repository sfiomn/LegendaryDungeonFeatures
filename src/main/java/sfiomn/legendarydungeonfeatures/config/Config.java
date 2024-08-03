package sfiomn.legendarydungeonfeatures.config;

import net.minecraftforge.common.ForgeConfigSpec;
import net.minecraftforge.fml.ModLoadingContext;
import net.minecraftforge.fml.config.ModConfig;
import org.apache.commons.lang3.tuple.Pair;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;

import java.util.Collections;
import java.util.List;

public class Config
{
	public static final ForgeConfigSpec COMMON_SPEC;
	public static final Common COMMON;
	
	static
	{
		final Pair<Common, ForgeConfigSpec> common = new ForgeConfigSpec.Builder().configure(Common::new);
		COMMON_SPEC = common.getRight();
		COMMON = common.getLeft();
	}
	
	public static void register()
	{
		ModLoadingContext.get().registerConfig(ModConfig.Type.COMMON, COMMON_SPEC, LegendaryDungeonFeatures.MOD_ID + ".toml");
	}
	
	public static class Common
	{
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonGateCanClose;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonGateOpenWhenUnlocked;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonGateBreakable;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonGateDrop;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonGateDropKeys;
		public final ForgeConfigSpec.ConfigValue<List<String>> forestDungeonGateLock1Unlocks;
		public final ForgeConfigSpec.ConfigValue<Integer> forestDungeonGateMobCheckRange;
		public final ForgeConfigSpec.ConfigValue<Integer> forestDungeonGateMobCheckFrequency;

		public final ForgeConfigSpec.ConfigValue<List<String>> dungeonHeartItemsBlocked;
		public final ForgeConfigSpec.ConfigValue<Boolean> dungeonHeartBlockPlaceBlocked;
		public final ForgeConfigSpec.ConfigValue<Boolean> dungeonHeartBlockBreakBlocked;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonHeartActiveBreakable;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonHeartBreakable;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonHeartDrop;
		public final ForgeConfigSpec.ConfigValue<String> forestDungeonHeartDeactivationByItem;
		public final ForgeConfigSpec.ConfigValue<Boolean> forestDungeonHeartDeactivationByRedStone;

		Common(ForgeConfigSpec.Builder builder)
		{
			builder.push("DungeonGates");
			builder.push("ForestDungeonGate");
			forestDungeonGateCanClose = builder.define(" Can Forest Dungeon Gate Be Closed Back", true);
			forestDungeonGateOpenWhenUnlocked = builder.define(" Will Forest Dungeon Gate Open When Unlocked", false);
			forestDungeonGateBreakable = builder.define(" Can Forest Dungeon Gate Be Destroyed", true);
			forestDungeonGateDrop = builder.define(" Can Forest Dungeon Gate Be Dropped", true);
			forestDungeonGateDropKeys = builder.define(" Can Forest Dungeon Gate Drop Keys On Break", true);
			forestDungeonGateLock1Unlocks = builder.define(" Items To Unlock Lock1", Collections.singletonList(LegendaryDungeonFeatures.MOD_ID + ":forest_key"));
			forestDungeonGateMobCheckRange = builder.comment(" To choose a mob for which its presence forces the gate to stay locked, " +
					"use an entity spawn egg and use it on the gate in creative mode.").define(" Mob Check Range In Blocks", 20);
			forestDungeonGateMobCheckFrequency = builder.define(" Mob Check Frequency In Ticks", 20);
			builder.pop();
			builder.push("DesertDungeonGate");
			builder.pop();
			builder.push("IcyDungeonGate");
			builder.pop();
			builder.push("OceanDungeonGate");
			builder.pop();
			builder.push("DesertDungeonGate");
			builder.pop();
			builder.push("DesertDungeonGate");
			builder.pop();
			builder.pop();

			builder.push("DungeonHearts");
			builder.push("Effects");
			// grapplemod:grapplinghook
			dungeonHeartItemsBlocked = builder.define(" Items Disabled While Dungeon Heart Active", Collections.singletonList("minecraft:stick"));
			dungeonHeartBlockPlaceBlocked = builder.define(" Block Placement Disabled While Dungeon Heart Active", true);
			dungeonHeartBlockBreakBlocked = builder.define(" Block Break Disabled While Dungeon Heart Active", true);
			builder.pop();
			builder.push("ForestDungeonHeartBlock");
			forestDungeonHeartActiveBreakable = builder.comment("If Forest Dungeon Heart can't be destroyed, this configuration is de facto set to false").define(" Can Forest Dungeon Heart Be Destroyed While Active", true);
			forestDungeonHeartBreakable = builder.define(" Can Forest Dungeon Heart Be Destroyed", true);
			forestDungeonHeartDrop = builder.define(" Can Forest Dungeon Heart Be Dropped", true);
			forestDungeonHeartDeactivationByItem = builder.comment("No item defined means no item needed to deactivate it").define(" Item Use To Deactivate Forest Dungeon Heart", "minecraft:apple");
			forestDungeonHeartDeactivationByRedStone = builder.define(" Can Forest Dungeon Heart Be Deactivated By Receiving RedStone Signal", true);
			builder.pop();
			builder.pop();
		}
	}
	
	public static class Baked
	{
		public static boolean forestDungeonGateCanClose;
		public static boolean forestDungeonGateOpenWhenUnlocked;
		public static boolean forestDungeonGateBreakable;
		public static boolean forestDungeonGateDrop;
		public static boolean forestDungeonGateDropKeys;
		public static List<String> forestDungeonGateLock1Unlocks;
		public static int forestDungeonGateMobCheckRange;
		public static int forestDungeonGateMobCheckFrequency;

		public static List<String> dungeonHeartItemsBlocked;
		public static boolean dungeonHeartBlockPlaceBlocked;
		public static boolean dungeonHeartBlockBreakBlocked;
		public static boolean forestDungeonHeartActiveBreakable;
		public static boolean forestDungeonHeartBreakable;
		public static boolean forestDungeonHeartDrop;
		public static String forestDungeonHeartDeactivationByItem;
		public static boolean forestDungeonHeartDeactivationByRedStone;

		public static void bakeCommon()
		{
			try
			{
				forestDungeonGateCanClose = COMMON.forestDungeonGateCanClose.get();
				forestDungeonGateOpenWhenUnlocked = COMMON.forestDungeonGateOpenWhenUnlocked.get();
				forestDungeonGateBreakable = COMMON.forestDungeonGateBreakable.get();
				forestDungeonGateDrop = COMMON.forestDungeonGateDrop.get();
				forestDungeonGateDropKeys = COMMON.forestDungeonGateDropKeys.get();
				forestDungeonGateLock1Unlocks = COMMON.forestDungeonGateLock1Unlocks.get();
				forestDungeonGateMobCheckRange = COMMON.forestDungeonGateMobCheckRange.get();
				forestDungeonGateMobCheckFrequency = COMMON.forestDungeonGateMobCheckFrequency.get();

				dungeonHeartItemsBlocked = COMMON.dungeonHeartItemsBlocked.get();
				dungeonHeartBlockPlaceBlocked = COMMON.dungeonHeartBlockPlaceBlocked.get();
				dungeonHeartBlockBreakBlocked = COMMON.dungeonHeartBlockBreakBlocked.get();

				forestDungeonHeartActiveBreakable = COMMON.forestDungeonHeartActiveBreakable.get();
				forestDungeonHeartBreakable = COMMON.forestDungeonHeartBreakable.get();
				forestDungeonHeartDrop = COMMON.forestDungeonHeartDrop.get();
				forestDungeonHeartDeactivationByItem = COMMON.forestDungeonHeartDeactivationByItem.get();
				forestDungeonHeartDeactivationByRedStone = COMMON.forestDungeonHeartDeactivationByRedStone.get();
			}
			catch (Exception e)
			{
				LegendaryDungeonFeatures.LOGGER.warn("An exception was caused trying to load the common config for Survival Overhaul");
				e.printStackTrace();
			}
		}
	}
}
