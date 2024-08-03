package sfiomn.legendarydungeonfeatures.entities;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import net.minecraftforge.network.NetworkHooks;
import sfiomn.legendary_additions.registry.ItemRegistry;

public class ForestKeyEntity extends KeyEntity {
    protected static final int successfulInsertAnimationTicks = 60;
    protected static final int successfulInsertSoundAtTick = 35;
    protected static final int failedInsertAnimationTicks = 80;
    public ForestKeyEntity(EntityType<? extends ForestKeyEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return NetworkHooks.getEntitySpawningPacket(this);
    }

    @Override
    public int getSuccessfulInsertAnimationTicks() {
        return successfulInsertAnimationTicks;
    }

    @Override
    public int getSuccessfulInsertSoundAtTick() {
        return successfulInsertSoundAtTick;
    }

    @Override
    public int getFailedInsertAnimationTicks() {
        return failedInsertAnimationTicks;
    }

    @Override
    public Item getKeyItem() {
        return ItemRegistry.FOREST_KEY.get();
    }
}
