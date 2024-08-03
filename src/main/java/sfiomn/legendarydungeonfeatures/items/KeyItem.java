package sfiomn.legendarydungeonfeatures.items;

import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import sfiomn.legendarydungeonfeatures.entities.KeyEntity;

public abstract class KeyItem extends Item {
    public KeyItem(Properties properties) {
        super(properties);
    }

    public abstract EntityType<? extends KeyEntity> getKeyEntity();
}
