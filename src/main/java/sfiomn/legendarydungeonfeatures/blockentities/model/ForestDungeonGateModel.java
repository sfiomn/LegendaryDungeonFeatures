package sfiomn.legendarydungeonfeatures.blockentities.model;// Made with Blockbench 4.10.4
// Exported for Minecraft version 1.17 or later with Mojang mappings
// Paste this class into your mod and generate all required imports


import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.*;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import sfiomn.legendary_additions.LegendaryAdditions;
import sfiomn.legendary_additions.blockentities.ForestDungeonGateBlockEntity;

public class ForestDungeonGateModel extends Model {
	// This layer location should be baked with EntityRendererProvider.Context in the entity renderer and passed into this model's constructor
	public static final ModelLayerLocation LAYER_LOCATION = new ModelLayerLocation(new ResourceLocation(LegendaryAdditions.MOD_ID, "forest_dungeon_gate"), "main");
	private final ModelPart gate;
	private final ModelPart door1;
	private final ModelPart door2;

	public ForestDungeonGateModel(ModelPart root) {
		super(RenderType::entityCutout);
		this.gate = root.getChild("gate");
		this.door1 = root.getChild("door1");
		this.door2 = root.getChild("door2");
	}

	public static LayerDefinition createBodyLayer() {
		MeshDefinition meshdefinition = new MeshDefinition();
		PartDefinition partdefinition = meshdefinition.getRoot();

		PartDefinition gate = partdefinition.addOrReplaceChild("gate", CubeListBuilder.create(), PartPose.offset(20.0F, -18.0F, 0.0F));

		PartDefinition door1 = gate.addOrReplaceChild("door1", CubeListBuilder.create().texOffs(94, 65).addBox(-2.1F, -7.0F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(48, 42).addBox(-1.1F, -41.0F, -4.0F, 6.0F, 34.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(0, 42).addBox(-19.0F, -42.0F, -3.0F, 18.0F, 36.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(104, 6).addBox(-5.1F, -14.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(70, 101).addBox(-5.1F, -22.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(60, 101).addBox(-5.1F, -30.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(96, 88).addBox(-5.1F, -36.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(86, 91).addBox(-2.1F, -48.0F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(96, 85).addBox(-5.1F, -14.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(96, 82).addBox(-5.1F, -22.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(96, 3).addBox(-5.1F, -30.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(96, 0).addBox(-5.1F, -36.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offset(-1.0F, 42.0F, 0.0F));

		PartDefinition door1_r1 = door1.addOrReplaceChild("door1_r1", CubeListBuilder.create().texOffs(102, 23).addBox(8.0F, -28.0F, -5.0F, 13.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(68, 0).addBox(21.0F, -25.0F, -4.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(76, 50).addBox(-6.0F, -37.0F, -4.0F, 6.0F, 17.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(68, 76).addBox(36.0F, -37.0F, -4.0F, 6.0F, 17.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-39.0F, -42.0F, 0.0F, 0.0F, 0.0F, 1.5708F));

		PartDefinition door1_r2 = door1.addOrReplaceChild("door1_r2", CubeListBuilder.create().texOffs(102, 48).addBox(-6.5F, -4.0F, -9.0F, 13.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-15.0F, -27.5F, -4.0F, 3.1416F, 0.0F, 1.5708F));

		PartDefinition door1_r3 = door1.addOrReplaceChild("door1_r3", CubeListBuilder.create().texOffs(24, 84).addBox(-3.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(-16.5F, -17.5F, 3.5F, 3.1416F, 0.0F, 1.5708F));

		PartDefinition door2 = gate.addOrReplaceChild("door2", CubeListBuilder.create().texOffs(0, 0).addBox(1.0F, -42.0F, -3.0F, 18.0F, 36.0F, 6.0F, new CubeDeformation(0.0F))
		.texOffs(34, 84).addBox(-4.9F, -48.0F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(-4.9F, -7.0F, -5.0F, 7.0F, 7.0F, 10.0F, new CubeDeformation(0.0F))
		.texOffs(58, 90).addBox(1.1F, -36.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(34, 90).addBox(1.1F, -30.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(24, 90).addBox(1.1F, -22.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 90).addBox(1.1F, -14.0F, -4.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 87).addBox(1.1F, -36.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 87).addBox(1.1F, -30.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(58, 84).addBox(1.1F, -22.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(0, 84).addBox(1.1F, -14.0F, 3.0F, 4.0F, 2.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(48, 0).addBox(-4.9F, -41.0F, -4.0F, 6.0F, 34.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offset(-39.0F, 42.0F, 0.0F));

		PartDefinition door2_r1 = door2.addOrReplaceChild("door2_r1", CubeListBuilder.create().texOffs(42, 0).addBox(-1.5F, -2.5F, -0.5F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(16.5F, -17.5F, 3.5F, 3.1416F, 0.0F, -1.5708F));

		PartDefinition door2_r2 = door2.addOrReplaceChild("door2_r2", CubeListBuilder.create().texOffs(42, 42).addBox(-26.0F, -25.0F, -4.0F, 5.0F, 5.0F, 1.0F, new CubeDeformation(0.0F))
		.texOffs(30, 101).addBox(-21.0F, -28.0F, -5.0F, 13.0F, 8.0F, 2.0F, new CubeDeformation(0.0F))
		.texOffs(76, 0).addBox(-42.0F, -37.0F, -4.0F, 6.0F, 17.0F, 8.0F, new CubeDeformation(0.0F))
		.texOffs(76, 25).addBox(0.0F, -37.0F, -4.0F, 6.0F, 17.0F, 8.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(39.0F, -42.0F, 0.0F, 0.0F, 0.0F, -1.5708F));

		PartDefinition door2_r3 = door2.addOrReplaceChild("door2_r3", CubeListBuilder.create().texOffs(0, 101).addBox(-6.5F, -4.0F, -9.0F, 13.0F, 8.0F, 2.0F, new CubeDeformation(0.0F)), PartPose.offsetAndRotation(15.0F, -27.5F, -4.0F, 3.1416F, 0.0F, -1.5708F));

		return LayerDefinition.create(meshdefinition, 256, 256);
	}

	public void setupAnim(ForestDungeonGateBlockEntity blockEntity, float partialTicks) {
		float hingeAngle = blockEntity.getOpenNess(partialTicks);

		this.door1.yRot= -(hingeAngle * ((float) Math.PI / 2F));
		this.door2.yRot = (hingeAngle * ((float) Math.PI / 2F));
	}

	@Override
	public void renderToBuffer(PoseStack poseStack, VertexConsumer vertexConsumer, int packedLight, int packedOverlay, float red, float green, float blue, float alpha) {
		gate.render(poseStack, vertexConsumer, packedLight, packedOverlay, red, green, blue, alpha);
	}
}