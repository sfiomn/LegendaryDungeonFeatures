package sfiomn.legendarydungeonfeatures.screens;

import com.mojang.blaze3d.systems.RenderSystem;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendarydungeonfeatures.LegendaryDungeonFeatures;
import sfiomn.legendarydungeonfeatures.blockentities.AbstractDungeonHeartBlockEntity;
import sfiomn.legendarydungeonfeatures.network.NetworkHandler;
import sfiomn.legendarydungeonfeatures.network.packets.MessageDungeonHeartRange;

import java.util.Objects;
import java.util.function.Consumer;

public class DungeonHeartScreen extends Screen {
    public static final ResourceLocation DUNGEON_HEART_SCREEN = new ResourceLocation(LegendaryDungeonFeatures.MOD_ID, "textures/screen/dungeon_heart.png");
    public static final int DUNGEON_HEART_SCREEN_WIDTH = 252;
    public static final int DUNGEON_HEART_SCREEN_HEIGHT = 140;
    public static final int TEXT_AREA_WIDTH = 40;
    public static final int TEXT_AREA_HEIGHT = 12;
    private final AbstractDungeonHeartBlockEntity blockEntity;
    private int leftPos;
    private int topPos;
    private EditBox rangeEastBox;
    private EditBox rangeWestBox;
    private EditBox rangeUpBox;
    private EditBox rangeDownBox;
    private EditBox rangeSouthBox;
    private EditBox rangeNorthBox;

    public DungeonHeartScreen(AbstractDungeonHeartBlockEntity blockEntity) {
        super(Component.translatable("screen." + LegendaryDungeonFeatures.MOD_ID + ".dungeon_heart"));
        this.blockEntity = blockEntity;
    }

    @Override
    protected void init() {
        super.init();
        assert this.minecraft != null;
        this.minecraft.keyboardHandler.setSendRepeatsToGui(true);

        this.leftPos = (this.width - DUNGEON_HEART_SCREEN_WIDTH) /2;
        this.topPos = (this.height - DUNGEON_HEART_SCREEN_HEIGHT) /2;

        this.rangeEastBox = addTextBox(50, 29, this.blockEntity.getRangeXPos(), setRangeValue(this.rangeEastBox, Direction.EAST));
        this.rangeWestBox = addTextBox(164, 29, this.blockEntity.getRangeXNeg(), setRangeValue(this.rangeWestBox, Direction.WEST));
        this.rangeUpBox = addTextBox(50, 66, this.blockEntity.getRangeYPos(), setRangeValue(this.rangeUpBox, Direction.UP));
        this.rangeDownBox = addTextBox(164, 66, this.blockEntity.getRangeYNeg(), setRangeValue(this.rangeDownBox, Direction.DOWN));
        this.rangeSouthBox = addTextBox(50, 101, this.blockEntity.getRangeZPos(), setRangeValue(this.rangeSouthBox, Direction.SOUTH));
        this.rangeNorthBox = addTextBox(164, 101, this.blockEntity.getRangeZNeg(), setRangeValue(this.rangeNorthBox, Direction.NORTH));
    }

    private EditBox addTextBox(int posX, int posY, int defaultValue, Consumer<String> c) {
        EditBox textBox = new EditBox(this.font, this.leftPos + posX, this.topPos + posY, TEXT_AREA_WIDTH, TEXT_AREA_HEIGHT, Component.literal("X Y Z"));
        textBox.setCanLoseFocus(true);
        textBox.setTextColor(-1);
        textBox.setTextColorUneditable(-1);
        textBox.setBordered(false);
        textBox.setMaxLength(5);
        textBox.setResponder(c);
        try {
            textBox.setValue(String.valueOf(defaultValue));
        } catch (NumberFormatException e) {
            textBox.setValue("0");
        }
        this.addWidget(textBox);
        return textBox;
    }

    private Consumer<String> setRangeValue(EditBox textBox, Direction direction) {
        return (String s) -> {
            while (!isInt(s)) {
                if (!s.isEmpty())
                    s = s.substring(0, s.length() - 1);
                else
                    s = "0";
            }
            if (textBox != null)
                textBox.setValue(s);
            if (s.isEmpty())
                s = "0";
            this.blockEntity.setRange(direction, Integer.parseInt(s));
            this.sendUpdateRange(direction, Integer.parseInt(s));
        };
    }

    private boolean isInt(String s) {
        if (s.isEmpty())
            return true;
        try {
            int i = Integer.parseInt(s);
            return true;
        } catch (NumberFormatException e) {
            return false;
        }
    }

    public void sendUpdateRange(Direction rangeDirection, int rangeValue) {
        CompoundTag updateRangeNbt = new CompoundTag();
        updateRangeNbt.putInt("posX", this.blockEntity.getBlockPos().getX());
        updateRangeNbt.putInt("posY", this.blockEntity.getBlockPos().getY());
        updateRangeNbt.putInt("posZ", this.blockEntity.getBlockPos().getZ());
        updateRangeNbt.putString("rangeDirection", rangeDirection.getName());
        updateRangeNbt.putInt("rangeValue", rangeValue);
        MessageDungeonHeartRange messageDungeonHeartChange = new MessageDungeonHeartRange(updateRangeNbt);
        NetworkHandler.INSTANCE.sendToServer(messageDungeonHeartChange);
    }

    @Override
    public boolean keyPressed(int id, int p_231046_2_, int p_231046_3_) {
        if (id == 256) {
            assert Objects.requireNonNull(this.minecraft).player != null;
            assert this.minecraft.player != null;
            this.minecraft.player.closeContainer();
        }

        return this.rangeEastBox.keyPressed(id, p_231046_2_, p_231046_3_) || this.rangeWestBox.keyPressed(id, p_231046_2_, p_231046_3_) ||
                this.rangeUpBox.keyPressed(id, p_231046_2_, p_231046_3_) || this.rangeDownBox.keyPressed(id, p_231046_2_, p_231046_3_) ||
                this.rangeSouthBox.keyPressed(id, p_231046_2_, p_231046_3_) || this.rangeNorthBox.keyPressed(id, p_231046_2_, p_231046_3_) ||
                super.keyPressed(id, p_231046_2_, p_231046_3_);
    }

    @Override
    public void render(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        this.renderBackground(gui);
        super.render(gui, mouseX, mouseY, partialTicks);
        this.renderFg(gui, mouseX, mouseY, partialTicks);
    }

    public void renderFg(GuiGraphics gui, int mouseX, int mouseY, float partialTicks) {
        this.rangeEastBox.render(gui, mouseX, mouseY, partialTicks);
        this.rangeWestBox.render(gui, mouseX, mouseY, partialTicks);
        this.rangeUpBox.render(gui, mouseX, mouseY, partialTicks);
        this.rangeDownBox.render(gui, mouseX, mouseY, partialTicks);
        this.rangeSouthBox.render(gui, mouseX, mouseY, partialTicks);
        this.rangeNorthBox.render(gui, mouseX, mouseY, partialTicks);
    }

    @Override
    public void renderBackground(GuiGraphics gui) {
        if (minecraft == null) {
            return;
        }

        RenderSystem.setShaderColor(1, 1, 1, 1);

        gui.blit(DUNGEON_HEART_SCREEN, leftPos, topPos, 0, 0, DUNGEON_HEART_SCREEN_WIDTH, DUNGEON_HEART_SCREEN_HEIGHT);
    }
}
