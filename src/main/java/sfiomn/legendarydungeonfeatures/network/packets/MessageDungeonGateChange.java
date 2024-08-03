package sfiomn.legendarydungeonfeatures.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarydungeonfeatures.blockentities.AbstractGateBlockEntity;

import java.util.function.Supplier;

public class MessageDungeonGateChange
{
    // SERVER side message
    CompoundTag compound;

    public MessageDungeonGateChange()
    {
    }

    public MessageDungeonGateChange(Tag nbt)
    {
        this.compound = (CompoundTag) nbt;
    }

    public static MessageDungeonGateChange decode(FriendlyByteBuf buffer)
    {
        return new MessageDungeonGateChange(buffer.readNbt());
    }

    public static void encode(MessageDungeonGateChange message, FriendlyByteBuf buffer)
    {
        buffer.writeNbt(message.compound);
    }

    public static void handle(MessageDungeonGateChange message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();

        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER && context.getSender() != null) {

            context.enqueueWork(() -> changeDungeonGateState(context.getSender().serverLevel(), message.compound));
        }
        supplier.get().setPacketHandled(true);
    }

    public static void changeDungeonGateState(ServerLevel level, CompoundTag nbt) {
        int x = nbt.getInt("posX");
        int y = nbt.getInt("posY");
        int z = nbt.getInt("posZ");

        BlockEntity blockEntity = level.getBlockEntity(new BlockPos(x, y, z));
        if (blockEntity instanceof AbstractGateBlockEntity gateTileEntity) {
            gateTileEntity.resetAnimation();
        }
    }
}
