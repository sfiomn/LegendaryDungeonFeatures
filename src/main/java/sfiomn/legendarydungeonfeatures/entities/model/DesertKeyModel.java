package sfiomn.legendarydungeonfeatures.entities.model;

import net.minecraft.util.ResourceLocation;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.entities.DesertKeyEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class DesertKeyModel extends AnimatedGeoModel<DesertKeyEntity> {
    @Override
    public ResourceLocation getModelLocation(DesertKeyEntity object) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "geo/forest_key.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(DesertKeyEntity object) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "textures/entity/forest_dungeon_gate.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(DesertKeyEntity animatable) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "animations/forest_key.animation.json");
    }
}
