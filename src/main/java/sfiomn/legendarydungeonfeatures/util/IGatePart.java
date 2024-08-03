package sfiomn.legendarydungeonfeatures.util;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.util.StringRepresentable;
import org.jetbrains.annotations.NotNull;

public interface IGatePart extends StringRepresentable {

    @NotNull
    String getSerializedName();
    Vec3i offset();
    boolean isBase();
    DungeonGatePartTypeEnum partType();

    // By default, offset x+1 = right block, offset x-1 = left block
    // Facing south (z+) => offset x+1 = right block, offset x-1 = left block
    // Facing north (z-) => offset x-1 = right block, offset x+1 = left block
    // Facing west (x-) => offset z+1 = right block, offset z-1 = left block
    // Facing east (x+) => offset z-1 = right block, offset z+1 = left block
    default Vec3i offset(Direction facing) {
        return MathUtil.rotateYFromSouth(this.offset(), facing);
    }

    default Vec3i reverseOffset(Direction facing) {
        return MathUtil.oppositeVector3i(MathUtil.rotateYFromSouth(this.offset(), facing));
    }

    default boolean isGateway() {
        return this.partType() == DungeonGatePartTypeEnum.GATEWAY;
    }

    default boolean isGateHinge() {
        return this.partType().isHinge();
    }
    default boolean isGateHingeRight() {
        return this.partType() == DungeonGatePartTypeEnum.HINGE_RIGHT;
    }

    default boolean isGateHingeLeft() {
        return this.partType() == DungeonGatePartTypeEnum.HINGE_LEFT;
    }

    default boolean isGateHingeTop() {
        return this.partType() == DungeonGatePartTypeEnum.HINGE_TOP;
    }

    default boolean isGateHingeBottom() {
        return this.partType() == DungeonGatePartTypeEnum.HINGE_BOTTOM;
    }
    default boolean isOpenRight() {
        return this.partType() == DungeonGatePartTypeEnum.OPEN_GATEWAY_RIGHT;
    }

    default boolean isOpenLeft() {
        return this.partType() == DungeonGatePartTypeEnum.OPEN_GATEWAY_LEFT;
    }

    default boolean isOpenTop() {
        return this.partType() == DungeonGatePartTypeEnum.OPEN_GATEWAY_TOP;
    }

    default boolean isOpenBottom() {
        return this.partType() == DungeonGatePartTypeEnum.OPEN_GATEWAY_BOTTOM;
    }

    default boolean isBottom() {
        return this.offset().getY() == 0;
    }

    default boolean isOpenPart() {
        return this.partType().isOpen();
    }
}
