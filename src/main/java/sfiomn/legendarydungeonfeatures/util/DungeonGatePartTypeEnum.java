package sfiomn.legendarydungeonfeatures.util;

public enum DungeonGatePartTypeEnum {

    GATEWAY("gateway"),
    HINGE_LEFT("hinge_left"),
    HINGE_RIGHT("hinge_right"),
    HINGE_TOP("hinge_top"),
    HINGE_BOTTOM("hinge_bottom"),
    OPEN_GATEWAY("open_gateway"),
    OPEN_GATEWAY_RIGHT("open_gateway_right"),
    OPEN_GATEWAY_LEFT("open_gateway_left"),
    OPEN_GATEWAY_TOP("open_gateway_top"),
    OPEN_GATEWAY_BOTTOM("open_gateway_bottom");

    private final String name;

    // By default: offset x+1 = right block, offset x-1 = left block
    DungeonGatePartTypeEnum(String name) {
        this.name = name;
    }

    public boolean isHinge() {
        return this.name.startsWith("hinge");
    }

    public boolean isOpen() {
        return this.name.startsWith("open_gateway");
    }
}
