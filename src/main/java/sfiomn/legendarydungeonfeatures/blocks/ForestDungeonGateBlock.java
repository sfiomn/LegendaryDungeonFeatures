package sfiomn.legendarydungeonfeatures.blocks;

import net.minecraft.core.Vec3i;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.state.BlockBehaviour;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.shapes.VoxelShape;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendary_additions.config.Config;
import sfiomn.legendary_additions.registry.BlockEntityRegistry;
import sfiomn.legendary_additions.util.DungeonGatePartTypeEnum;
import sfiomn.legendary_additions.util.IGatePart;
import sfiomn.legendary_additions.util.VoxelShapeUtil;

import javax.annotation.Nullable;
import java.util.Objects;

public class ForestDungeonGateBlock extends AbstractGateBlock {

    public static final BlockBehaviour.Properties properties = getProperties();
    public static final EnumProperty<ForestDungeonGatePart> PART = EnumProperty.create("part", ForestDungeonGatePart.class);
    private static final double gateShapeThickness = 8.0D;
    public static BlockBehaviour.Properties getProperties()
    {
        BlockBehaviour.Properties properties = BlockBehaviour.Properties
                .of()
                .mapColor(MapColor.WOOD)
                .sound(SoundType.STONE)
                .strength(12f, 100f)
                .noOcclusion();

        if (!Config.Baked.forestDungeonGateBreakable)
            properties.strength(-1.0f, 3600000.0F);

        return properties;
    }

    public ForestDungeonGateBlock() {
        super(properties, ForestDungeonGatePart.values());

        this.defaultBlockState().setValue(PART, ForestDungeonGatePart.GATEWAY_0);
    }

    @Override
    public VoxelShape getShape(BlockState state, IBlockReader world, BlockPos pos, ISelectionContext context) {
        if (!state.getValue(OPENED))
            return state.getValue(FACING).getAxis() == Direction.Axis.X ? VoxelShapeUtil.xCentered(gateShapeThickness) : VoxelShapeUtil.zCentered(gateShapeThickness);
        else {
            IGatePart part = getGatePart(state);
            Direction gateFacing = state.getValue(FACING);
            if (part.isGateHinge()) {
                VoxelShape shape = VoxelShapeUtil.oppositeFacingBox(gateShapeThickness + 1, this.gatePartUtil.getOpenDirection(part, gateFacing));
                return VoxelShapeUtil.moveTo(shape, gateFacing.getOpposite(), (16 - gateShapeThickness) * 0.5 - 1);
            } else if (part.isOpenPart()) {
                VoxelShape shape = VoxelShapeUtil.oppositeFacingBox(gateShapeThickness + 1, this.gatePartUtil.getOpenDirection(part, gateFacing));
                if (part.offset().getZ() == -1) {
                    shape = VoxelShapeUtil.moveTo(shape, gateFacing, gateShapeThickness * 0.5 + 1);
                }
                return shape;
            } else
                return VoxelShapes.empty();
        }
    }

    @Override
    public void setPlacedBy(World world, BlockPos pos, BlockState state, @Nullable LivingEntity player, ItemStack itemStack) {
        super.setPlacedBy(world, pos, state, player, itemStack);
        if (!world.isClientSide) {
            Direction gateFacing = state.getValue(FACING);
            for (IGatePart part : this.gatePartUtil.getClosedGateParts()) {
                if (!part.isBase() && part instanceof ForestDungeonGatePart) {
                    ForestDungeonGatePart forestPart = (ForestDungeonGatePart) part;
                    world.setBlockAndUpdate(pos.offset(part.offset(gateFacing)), state.setValue(PART, forestPart));
                }
            }
        }
    }

    @Nullable
    @Override
    public TileEntity createTileEntity(BlockState state, IBlockReader world) {
        return BlockEntityRegistry.FOREST_DUNGEON_GATE_BLOCK_ENTITY.get().create();
    }

    @Override
    IGatePart getGatePart(BlockState blockState) {
        return blockState.hasProperty(PART) ? blockState.getValue(PART) : null;
    }

    @Override
    public boolean canClose() {
        return Config.Baked.forestDungeonGateCanClose;
    }

    @Override
    public boolean isBreakable() {
        return Config.Baked.forestDungeonGateBreakable;
    }

    @Override
    public boolean canDropGate() {
        return Config.Baked.forestDungeonGateDrop;
    }

    @Override
    public boolean isOpenPart(BlockState state) {
        return state.getValue(PART).isOpenPart();
    }

    @Override
    protected void createBlockStateDefinition(StateContainer.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(PART);
    }

    public enum ForestDungeonGatePart implements IGatePart {
        GATEWAY_0("gateway_0", DungeonGatePartTypeEnum.GATEWAY, new Vec3i(0, 0, 0)),
        GATEWAY_1("gateway_1", DungeonGatePartTypeEnum.GATEWAY, new Vec3i(0, 1, 0)),
        GATEWAY_2("gateway_2", DungeonGatePartTypeEnum.GATEWAY, new Vec3i(0, 2, 0)),
        LEFT_0("hinge_left_0", DungeonGatePartTypeEnum.HINGE_LEFT, new Vec3i(-1, 0, 0)),
        LEFT_1("hinge_left_1", DungeonGatePartTypeEnum.HINGE_LEFT, new Vec3i(-1, 1, 0)),
        LEFT_2("hinge_left_2", DungeonGatePartTypeEnum.HINGE_LEFT, new Vec3i(-1, 2, 0)),
        RIGHT_0("hinge_right_0", DungeonGatePartTypeEnum.HINGE_RIGHT, new Vec3i(1, 0, 0)),
        RIGHT_1("hinge_right_1", DungeonGatePartTypeEnum.HINGE_RIGHT, new Vec3i(1, 1, 0)),
        RIGHT_2("hinge_right_2", DungeonGatePartTypeEnum.HINGE_RIGHT, new Vec3i(1, 2, 0)),
        OPEN_LEFT_0("open_left_0", DungeonGatePartTypeEnum.OPEN_GATEWAY_LEFT, new Vec3i(-1, 0, -1)),
        OPEN_LEFT_1("open_left_1", DungeonGatePartTypeEnum.OPEN_GATEWAY_LEFT, new Vec3i(-1, 1, -1)),
        OPEN_LEFT_2("open_left_2", DungeonGatePartTypeEnum.OPEN_GATEWAY_LEFT, new Vec3i(-1, 2, -1)),
        OPEN_RIGHT_0("open_right_0", DungeonGatePartTypeEnum.OPEN_GATEWAY_RIGHT, new Vec3i(1, 0, -1)),
        OPEN_RIGHT_1("open_right_1", DungeonGatePartTypeEnum.OPEN_GATEWAY_RIGHT, new Vec3i(1, 1, -1)),
        OPEN_RIGHT_2("open_right_2", DungeonGatePartTypeEnum.OPEN_GATEWAY_RIGHT, new Vec3i(1, 2, -1));

        private final String name;
        private final DungeonGatePartTypeEnum partType;
        private final Vec3i offset;

        // By default: offset x+1 = right block, offset x-1 = left block
        ForestDungeonGatePart(String name, DungeonGatePartTypeEnum partType, Vec3i offset) {
            this.name = name;
            this.partType = partType;
            this.offset = offset;
        }

        @Override
        public @NotNull String getSerializedName() {
            return this.name;
        }

        @Override
        public Vec3i offset() {
            return this.offset;
        }

        @Override
        public DungeonGatePartTypeEnum partType() {
            return this.partType;
        }

        @Override
        public boolean isBase() {
            return Objects.equals(this.getSerializedName(), "gateway_0");
        }
    }
}
