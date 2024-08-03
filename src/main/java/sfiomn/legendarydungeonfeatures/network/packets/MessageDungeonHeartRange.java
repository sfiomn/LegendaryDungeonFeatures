package sfiomn.legendarydungeonfeatures.network.packets;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.network.NetworkDirection;
import net.minecraftforge.network.NetworkEvent;
import sfiomn.legendarydungeonfeatures.blockentities.AbstractDungeonHeartBlockEntity;

import java.util.Objects;
import java.util.function.Supplier;

public class MessageDungeonHeartRange
{
    // SERVER side message
    CompoundTag compound;

    public MessageDungeonHeartRange()
    {
    }

    public MessageDungeonHeartRange(Tag nbt)
    {
        this.compound = (CompoundTag) nbt;
    }

    public static MessageDungeonHeartRange decode(FriendlyByteBuf buffer)
    {
        return new MessageDungeonHeartRange(buffer.readNbt());
    }

    public static void encode(MessageDungeonHeartRange message, FriendlyByteBuf buffer)
    {
        buffer.writeNbt(message.compound);
    }

    public static void handle(MessageDungeonHeartRange message, Supplier<NetworkEvent.Context> supplier)
    {
        final NetworkEvent.Context context = supplier.get();

        if (context.getDirection() == NetworkDirection.PLAY_TO_SERVER && context.getSender() != null) {
            context.enqueueWork(() -> updateDungeonHeartRange(context.getSender().serverLevel(), message.compound));
        }
        supplier.get().setPacketHandled(true);
    }

    public static void updateDungeonHeartRange(ServerLevel level, CompoundTag nbt) {
        int x = nbt.getInt("posX");
        int y = nbt.getInt("posY");
        int z = nbt.getInt("posZ");

        BlockEntity tileEntity = level.getBlockEntity(new BlockPos(x, y, z));
        if (tileEntity instanceof AbstractDungeonHeartBlockEntity dungeonHeartTileEntity) {
            String rangeDirection = nbt.getString("rangeDirection");
            int rangeValue = nbt.getInt("rangeValue");

            dungeonHeartTileEntity.setRange(Objects.requireNonNull(Direction.byName(rangeDirection)), rangeValue);
        }
    }
}
