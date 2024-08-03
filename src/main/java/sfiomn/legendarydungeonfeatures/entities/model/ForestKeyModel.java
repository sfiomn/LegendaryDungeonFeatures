package sfiomn.legendarydungeonfeatures.entities.model;

import net.minecraft.util.ResourceLocation;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.entities.ForestKeyEntity;
import software.bernie.geckolib3.model.AnimatedGeoModel;

public class ForestKeyModel extends AnimatedGeoModel<ForestKeyEntity> {
    @Override
    public ResourceLocation getModelLocation(ForestKeyEntity object) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "geo/forest_key.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ForestKeyEntity object) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "textures/entity/forest_dungeon_gate.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ForestKeyEntity animatable) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "animations/forest_key.animation.json");
    }
}
