package sfiomn.legendarydungeonfeatures.items;

import net.minecraft.world.entity.EntityType;
import sfiomn.legendarydungeonfeatures.entities.KeyEntity;
import sfiomn.legendarydungeonfeatures.registry.EntityTypeRegistry;

public class DesertKeyItem extends KeyItem {
    public DesertKeyItem(Properties properties) {
        super(properties);
    }

    public EntityType<? extends KeyEntity> getKeyEntity() {
        return EntityTypeRegistry.DESERT_KEY_ENTITY.get();
    }
}
