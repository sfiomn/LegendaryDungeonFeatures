package sfiomn.legendarydungeonfeatures.blockentities.model;

import net.minecraft.resources.ResourceLocation;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.blockentities.ForestDungeonHeartBlockEntity;

public class ForestDungeonHeartModel extends AnimatedGeoModel<ForestDungeonHeartBlockEntity> {
    @Override
    public ResourceLocation getModelLocation(ForestDungeonHeartBlockEntity animatable) {
        if (animatable.isActive())
            return new ResourceLocation(LegendaryAdditions.MOD_ID, "geo/forest_dungeon_heart.geo.json");
        else
            return new ResourceLocation(LegendaryAdditions.MOD_ID, "geo/forest_dungeon_heart_deactivated.geo.json");
    }

    @Override
    public ResourceLocation getTextureLocation(ForestDungeonHeartBlockEntity object) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "textures/entity/forest_dungeon_heart.png");
    }

    @Override
    public ResourceLocation getAnimationFileLocation(ForestDungeonHeartBlockEntity animatable) {
        return new ResourceLocation(LegendaryAdditions.MOD_ID, "animations/forest_dungeon_heart.animation.json");
    }
}
