package sfiomn.legendarydungeonfeatures.util;

import net.minecraft.core.Direction;
import net.minecraft.core.Vec3i;
import net.minecraft.world.phys.Vec3;

public class MathUtil {
    public static Vec3i rotateYFromSouth(Vec3i vector, Direction toDirection) {
        return switch (toDirection) {
            case NORTH -> new Vec3i(-vector.getX(), vector.getY(), -vector.getZ());
            case EAST -> new Vec3i(vector.getZ(), vector.getY(), -vector.getX());
            case WEST -> new Vec3i(-vector.getZ(), vector.getY(), vector.getX());
            default -> vector;
        };
    }

    public static Vec3 rotateYFromSouth(Vec3 vector, Direction toDirection) {
        return switch (toDirection) {
            case NORTH -> new Vec3(1 - vector.x, vector.y, 1 - vector.z);
            case EAST -> new Vec3(vector.z, vector.y, 1 - vector.x);
            case WEST -> new Vec3(1 - vector.z, vector.y, vector.x);
            default -> vector;
        };
    }

    public static float getAngleFromWest(Direction toDirection) {
        return switch (toDirection) {
            case NORTH -> 270.0F;
            case EAST -> 180.0f;
            case WEST -> 0.0f;
            default -> 90.0f;
        };
    }

    public static float getAngleFromNorth(Direction toDirection) {
        return switch (toDirection) {
            case NORTH -> 0.0f;
            case EAST -> 90.0f;
            case WEST -> -90.0f;
            default -> 180.0F;
        };
    }

    public static Vec3i oppositeVector3i(Vec3i vector3i) {
        return new Vec3i(-vector3i.getX(), -vector3i.getY(), -vector3i.getZ());
    }
}
