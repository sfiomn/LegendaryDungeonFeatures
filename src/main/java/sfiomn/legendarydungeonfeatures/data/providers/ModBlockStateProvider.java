package sfiomn.legendarydungeonfeatures.data.providers;

import net.minecraft.core.Direction;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.DoublePlantBlock;
import net.minecraft.world.level.block.PipeBlock;
import net.minecraft.world.level.block.state.properties.DoubleBlockHalf;
import net.minecraft.world.level.block.state.properties.Property;
import net.minecraftforge.client.model.generators.BlockStateProvider;
import net.minecraftforge.client.model.generators.ModelFile;
import net.minecraftforge.client.model.generators.MultiPartBlockStateBuilder;
import net.minecraftforge.common.data.ExistingFileHelper;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.registry.BlockRegistry;


public class ModBlockStateProvider extends BlockStateProvider {

    public static final ResourceLocation GLOWING_BULB_BOTTOM = new ResourceLocation(LegendaryAdditions.MOD_ID, "block/glowing_bulb_bottom");
    public static final ResourceLocation GLOWING_BULB_TOP = new ResourceLocation(LegendaryAdditions.MOD_ID, "block/glowing_bulb_top");

    public ModBlockStateProvider(PackOutput output, ExistingFileHelper exFileHelper) {
        super(output, LegendaryAdditions.MOD_ID, exFileHelper);
    }

    @Override
    protected void registerStatesAndModels() {

        simpleBlock(BlockRegistry.CARVED_ACACIA_LOG_BLOCK.get());

        this.getVariantBuilder(BlockRegistry.GLOWING_BULB_BLOCK.get())
                .partialState()
                    .with(DoublePlantBlock.HALF, DoubleBlockHalf.LOWER)
                    .modelForState()
                    .modelFile(models().cross("glowing_bulb_bottom", GLOWING_BULB_BOTTOM).texture("particle", GLOWING_BULB_BOTTOM).renderType("cutout"))
                    .addModel()
                .partialState()
                    .with(DoublePlantBlock.HALF, DoubleBlockHalf.UPPER)
                    .modelForState()
                    .modelFile(models().cross("glowing_bulb", GLOWING_BULB_TOP).texture("particle", GLOWING_BULB_TOP).renderType("cutout"))
                    .addModel();

    }

    public void fourWayMultipart(MultiPartBlockStateBuilder builder, ModelFile side) {
        PipeBlock.PROPERTY_BY_DIRECTION.entrySet().forEach((e) -> {
            Direction dir = (Direction)e.getKey();
            if (dir.getAxis().isHorizontal()) {
                ((MultiPartBlockStateBuilder.PartBuilder)builder.part().modelFile(side).rotationY(((int)dir.toYRot() + 180) % 360).uvLock(true).addModel()).condition((Property)e.getValue(), new Boolean[]{true});
            }

        });
    }
}
