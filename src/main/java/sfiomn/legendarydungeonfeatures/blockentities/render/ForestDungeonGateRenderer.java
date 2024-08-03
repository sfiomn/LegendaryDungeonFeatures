package sfiomn.legendarydungeonfeatures.blockentities.render;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.renderer.MultiBufferSource;
import net.minecraft.client.renderer.blockentity.BlockEntityRenderer;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.world.phys.AABB;
import org.jetbrains.annotations.NotNull;
import sfiomn.legendary_additions.blockentities.ForestDungeonGateBlockEntity;
import sfiomn.legendary_additions.blockentities.model.ForestDungeonGateModel;

import javax.annotation.Nonnull;

public class ForestDungeonGateRenderer implements BlockEntityRenderer<ForestDungeonGateBlockEntity> {



    public ForestDungeonGateRenderer(BlockEntityRendererProvider.Context context) {
        this.model = new ForestDungeonGateModel(context.bakeLayer(ForestDungeonGateModel.LAYER_LOCATION));
    }

    @Override
    public void render(@Nonnull ForestDungeonGateBlockEntity blockEntity, float partialTicks, @Nonnull PoseStack poseStack, @Nonnull MultiBufferSource buffer, int combinedLight, int combinedOverlay) {
        BlockState state = blockEntity.getBlockState();
        poseStack.pushPose();

        poseStack.translate(0.5D, 1.5D, 0.5D);
        poseStack.mulPose(Axis.YP.rotationDegrees(-state.getValue(BlockStateProperties.HORIZONTAL_FACING).toYRot() + 180));
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));

        this.model.setupAnim(blockEntity, partialTicks);
        this.model.renderToBuffer(poseStack, buffer.getBuffer(this.model.renderType(TEXTURE)), combinedLight, combinedOverlay, 1.0F, 1.0F, 1.0F, 1.0F);

        poseStack.popPose();
    }

    @Override
    public boolean shouldRenderOffScreen(@Nonnull ForestDungeonGateBlockEntity blockEntity) {
        return true;
    }

    @Override
    public @NotNull AABB getRenderBoundingBox(ForestDungeonGateBlockEntity blockEntity) {
        return new AABB(blockEntity.getBlockPos().offset(-5, -5, -5).getCenter(), blockEntity.getBlockPos().offset(5, 5, 5).getCenter());
    }

    @Override
    public void render(ForestDungeonGateBlockEntity blockEntity, float v, PoseStack poseStack, MultiBufferSource multiBufferSource, int i, int i1) {

    }
}
