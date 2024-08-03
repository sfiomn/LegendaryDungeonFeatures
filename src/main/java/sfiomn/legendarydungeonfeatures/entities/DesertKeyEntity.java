package sfiomn.legendarydungeonfeatures.entities;

import net.minecraft.network.protocol.Packet;
import net.minecraft.network.protocol.game.ClientGamePacketListener;
import net.minecraft.network.protocol.game.ClientboundAddEntityPacket;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.Level;
import sfiomn.legendary_additions.registry.ItemRegistry;

public class DesertKeyEntity extends KeyEntity {
    protected static final int successfulInsertAnimationTicks = 60;
    protected static final int successfulInsertSoundAtTick = 35;
    protected static final int failedInsertAnimationTicks = 80;
    public DesertKeyEntity(EntityType<? extends DesertKeyEntity> entityType, Level level) {
        super(entityType, level);
        this.noPhysics = true;
    }

    @Override
    public Packet<ClientGamePacketListener> getAddEntityPacket() {
        return new ClientboundAddEntityPacket(this);
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
        return ItemRegistry.DESERT_KEY.get();
    }
}
