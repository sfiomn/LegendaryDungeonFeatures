package sfiomn.legendarydungeonfeatures.events;


import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.EntityRenderersEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.blockentities.layer.ModModelLayers;
import sfiomn.legendary_additions.blockentities.model.ForestDungeonGateModel;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;

@Mod.EventBusSubscriber(modid = LegendaryDungeonFeatures.MOD_ID, bus = Mod.EventBusSubscriber.Bus.MOD, value = Dist.CLIENT)
public class ClientModBusEvents {

    @SubscribeEvent
    public static void registerLayers(EntityRenderersEvent.RegisterLayerDefinitions event) {
        event.registerLayerDefinition(ModModelLayers.FOREST_DUNGEON_GATE_LAYER, ForestDungeonGateModel::createBodyLayer);
    }

}
