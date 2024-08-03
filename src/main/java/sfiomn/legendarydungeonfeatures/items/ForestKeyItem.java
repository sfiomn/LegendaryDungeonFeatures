package sfiomn.legendarydungeonfeatures.items;

import net.minecraft.world.entity.EntityType;
import sfiomn.legendarydungeonfeatures.entities.KeyEntity;
import sfiomn.legendarydungeonfeatures.registry.EntityTypeRegistry;

public class ForestKeyItem extends KeyItem {
    public ForestKeyItem(Properties properties) {
        super(properties);
    }

    public EntityType<? extends KeyEntity> getKeyEntity() {
        return EntityTypeRegistry.FOREST_KEY_ENTITY.get();
    }
}
