package sfiomn.legendarydungeonfeatures.screens;

import net.minecraft.client.Minecraft;
import net.minecraft.world.level.block.entity.BlockEntity;
import sfiomn.legendarydungeonfeatures.blockentities.AbstractDungeonHeartBlockEntity;

public class ClientHooks {
    public static void openDungeonHeartScreen(BlockEntity blockEntity) {
        AbstractDungeonHeartBlockEntity be = (AbstractDungeonHeartBlockEntity) blockEntity;
        Minecraft.getInstance().setScreen(new DungeonHeartScreen(be));
    }
}
