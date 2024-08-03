package sfiomn.legendarydungeonfeatures.network;

import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.network.packets.MessageDungeonGateChange;
import sfiomn.legendarydungeonfeatures.network.packets.MessageDungeonHeartRange;

public class NetworkHandler
{
	private static final String PROTOCOL_VERSION = "1";
	
	public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
			new ResourceLocation(LegendaryDungeonFeatures.MOD_ID, "main"),
			() -> PROTOCOL_VERSION,
			PROTOCOL_VERSION::equals,
			PROTOCOL_VERSION::equals);
	
	public static void register()
	{
		int id = -1;

		INSTANCE.registerMessage(id++, MessageDungeonGateChange.class, MessageDungeonGateChange::encode, MessageDungeonGateChange::decode, MessageDungeonGateChange::handle);
		INSTANCE.registerMessage(id++, MessageDungeonHeartRange.class, MessageDungeonHeartRange::encode, MessageDungeonHeartRange::decode, MessageDungeonHeartRange::handle);
	}
}