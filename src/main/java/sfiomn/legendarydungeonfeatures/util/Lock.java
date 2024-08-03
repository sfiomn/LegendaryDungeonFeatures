package sfiomn.legendarydungeonfeatures.util;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.phys.Vec3;
import net.minecraftforge.registries.ForgeRegistries;

import java.util.List;

public class Lock {
    public final int id;
    private final Vec3 positionInBlock;
    private final Vec3i offsetOnBase;
    private final double size;
    private boolean unlocked;
    private Vec3i insertedKeyPosition;
    private int insertTimerTick;
    private final List<String> keyNames;

    public Lock(int lockId, Vec3i offsetOnBase, Vec3 positionInBlock, double size, List<String> keyNames) {
        this.id = lockId;
        this.offsetOnBase = offsetOnBase;
        this.positionInBlock = positionInBlock;
        this.size = size;
        this.keyNames = keyNames;
        this.unlocked = false;
        this.insertedKeyPosition = Vec3i.ZERO;
        this.insertTimerTick = -1;
    }

    public Vec3 getPositionInBlock() {
        return this.positionInBlock;
    }
    public Vec3i getOffsetOnBase() {
        return this.offsetOnBase;
    }
    public Vec3 getLockPos(Direction facing, BlockPos basePos) {
        Vec3 positionInBlockFacing = MathUtil.rotateYFromSouth(this.positionInBlock, facing);

        return Vec3.atLowerCornerOf(MathUtil.rotateYFromSouth(this.offsetOnBase, facing)).add(positionInBlockFacing).add(basePos.getX(), basePos.getY(), basePos.getZ());
    }
    public double getSize() {
        return this.size;
    }

    public void unlocked() {
        this.unlocked = true;
    }

    public boolean isUnlocked() {
        return this.unlocked;
    }

    public boolean keyInserting() {
        return this.insertTimerTick > -1;
    }
    public void keyStopInserting() {
        this.insertTimerTick = -1;
        this.insertedKeyPosition = Vec3i.ZERO;
    }

    public void updateTimer() {
        if (this.insertTimerTick >= 0)
            this.insertTimerTick -= 1;
    }

    public void setInsertTimerTick(int insertTimerTick) {
        this.insertTimerTick = insertTimerTick;
    }

    public int getInsertTimerTick() {
        return insertTimerTick;
    }

    public boolean canInsert(Vec3 insertLocation, Direction facing, BlockPos basePos) {
        Vec3 centerInsertLocation = facing.getAxis() == Direction.Axis.Z ?
                        new Vec3(insertLocation.x, insertLocation.y, Mth.floor(insertLocation.z) + 0.5) :
                        new Vec3(Mth.floor(insertLocation.x) + 0.5, insertLocation.y, insertLocation.z);
        return this.getLockPos(facing, basePos).subtract(centerInsertLocation).length() <= this.getSize() && !this.keyInserting() && !this.isUnlocked();
    }

    public boolean canBeUnlocked(Item item) {
        for (String keyName: getKeyNames()) {
            Item keyItem = ForgeRegistries.ITEMS.getValue(new ResourceLocation(keyName));
            if (keyItem != null) {
                if (keyItem == item) {
                    return true;
                }
            }
        }
        return false;
    }

    public List<String> getKeyNames() {
        return keyNames;
    }

    public Vec3i getInsertedKeyPosition() {
        return insertedKeyPosition;
    }

    public void setInsertedKeyPosition(Vec3i insertedKeyPosition) {
        this.insertedKeyPosition = insertedKeyPosition;
    }
}
