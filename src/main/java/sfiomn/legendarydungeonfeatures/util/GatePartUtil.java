package sfiomn.legendarydungeonfeatures.util;

import net.minecraft.core.Direction;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class GatePartUtil {
    private final List<IGatePart> gateParts;
    private final List<IGatePart> closedGateParts;
    private final List<IGatePart> openedGateParts;
    private final IGatePart mostBottomRight;
    private final IGatePart mostBottomLeft;
    private final int height;
    private final int width;
    public GatePartUtil(IGatePart[] gateParts) {
        this.gateParts = Arrays.asList(gateParts);
        this.closedGateParts = new ArrayList<>();
        for (IGatePart part: this.gateParts) {
            if (!part.isOpenPart()) {
                this.closedGateParts.add(part);
            }
        }

        this.openedGateParts = new ArrayList<>();
        for (IGatePart part: this.gateParts) {
            if (part.isOpenPart()) {
                this.openedGateParts.add(part);
            }
        }

        this.mostBottomRight = mostBottomRight();
        this.mostBottomLeft = mostBottomLeft();
        this.height = heightCalculation();
        this.width = widthCalculation();
    }

    public List<IGatePart> gateParts() {
        return gateParts;
    }

    public List<IGatePart> getClosedGateParts() {
        return closedGateParts;
    }

    public List<IGatePart> getOpenedGateParts() {
        return openedGateParts;
    }

    // direction unchanged if not gateHinge or gateOpen parts
    public Direction getOpenDirection(IGatePart part, Direction gateFacing) {
        Direction gateDirection = gateFacing;
        if (part.isOpenRight() || part.isGateHingeRight()) {
            gateDirection = gateFacing.getClockWise();
        } else if (part.isOpenLeft() || part.isGateHingeLeft()) {
            gateDirection = gateFacing.getCounterClockWise();
        } else if (part.isOpenTop() || part.isGateHingeTop()) {
            gateDirection = Direction.DOWN;
        } else if (part.isOpenBottom() || part.isGateHingeBottom()) {
            gateDirection = Direction.UP;
        }
        return gateDirection;
    }

    public boolean isMostRight(IGatePart part) {
        return part.offset().getX() == this.mostBottomRight.offset().getX();
    }

    public boolean isMostLeft(IGatePart part) {
        return part.offset().getX() == this.mostBottomLeft.offset().getX();
    }

    public boolean isTop(IGatePart part) {
        for (IGatePart dungeonGatePart: this.gateParts) {
            if (dungeonGatePart.offset().getX() == part.offset().getX() && dungeonGatePart.offset().getZ() == part.offset().getZ()) {
                if (dungeonGatePart.offset().getY() > part.offset().getY())
                    return false;
            }
        }
        return true;
    }

    public List<IGatePart> getNeighbourOffsets(IGatePart part) {
        List<IGatePart> neighbourOffsets = new ArrayList<>();
        for (IGatePart dungeonGatePart: this.gateParts) {
            if (dungeonGatePart.offset().distManhattan(part.offset()) == 1) {
                neighbourOffsets.add(dungeonGatePart);
            }
        }

        return neighbourOffsets;
    }

    public IGatePart getMostBottomRight() {
        return mostBottomRight;
    }

    public IGatePart getMostBottomLeft() {
        return mostBottomLeft;
    }

    public int getHeight() {
        return this.height;
    }

    public int getWidth() {
        return this.width;
    }

    public int widthCalculation() {
        int left = 0;
        int right = 0;
        for (IGatePart part: this.gateParts) {
            if (part.offset().getX() > right) {
                right = part.offset().getX();
            } else if (part.offset().getX() < left) {
                left = part.offset().getX();
            }
        }
        return right - left + 1;
    }

    public int heightCalculation() {
        int height = 1;
        for (IGatePart part: this.gateParts) {
            if (part.offset().getY() + 1 > height) {
                height = part.offset().getY() + 1;
            }
        }
        return height;
    }

    public IGatePart mostBottomRight() {
        IGatePart mostBottomRight = null;
        for (IGatePart part: this.gateParts) {
            if (part.isBottom() && part.offset().getZ() == 0) {
                if (mostBottomRight == null || mostBottomRight.offset().getX() < part.offset().getX()) {
                    mostBottomRight = part;
                }
            }
        }
        return mostBottomRight;
    }

    public IGatePart mostBottomLeft() {
        IGatePart mostBottomLeft = null;
        for (IGatePart part: this.gateParts) {
            if (part.isBottom() && part.offset().getZ() == 0) {
                if (mostBottomLeft == null || mostBottomLeft.offset().getX() > part.offset().getX()) {
                    mostBottomLeft = part;
                }
            }
        }
        return mostBottomLeft;
    }
}
