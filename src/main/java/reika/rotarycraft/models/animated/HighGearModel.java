package reika.rotarycraft.models.animated;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class HighGearModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape10;
    private final ModelPart shape11;
    private final ModelPart shape24;
    private final ModelPart shape25;
    private final ModelPart shape42;
    private final ModelPart shape43;
    private final ModelPart shape12a;
    private final ModelPart shape12;
    private final ModelPart shape12c;
    private final ModelPart shape12b;
    private final ModelPart shape13;
    private final ModelPart shape13a;
    private final ModelPart shape12d;
    private final ModelPart shape12e;
    private final ModelPart shape12f;
    private final ModelPart shape12g;
    private final ModelPart shape13b;
    private final ModelPart shape13c;
    private final ModelPart root;

    public HighGearModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9 = modelPart.getChild("shape9");
        this.shape10 = modelPart.getChild("shape10");
        this.shape11 = modelPart.getChild("shape11");
        this.shape24 = modelPart.getChild("shape24");
        this.shape25 = modelPart.getChild("shape25");
        this.shape42 = modelPart.getChild("shape42");
        this.shape43 = modelPart.getChild("shape43");
        this.shape12a = modelPart.getChild("shape12a");
        this.shape12 = modelPart.getChild("shape12");
        this.shape12c = modelPart.getChild("shape12c");
        this.shape12b = modelPart.getChild("shape12b");
        this.shape13 = modelPart.getChild("shape13");
        this.shape13a = modelPart.getChild("shape13a");
        this.shape12d = modelPart.getChild("shape12d");
        this.shape12e = modelPart.getChild("shape12e");
        this.shape12f = modelPart.getChild("shape12f");
        this.shape12g = modelPart.getChild("shape12g");
        this.shape13b = modelPart.getChild("shape13b");
        this.shape13c = modelPart.getChild("shape13c");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(7, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(-8, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, 7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(6, 16, 7, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(6, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-7, 16, 7, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-7, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.offsetAndRotation(7, 8, -5, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.offsetAndRotation(-8, 8, -5, 0, 0, 0));

        root.addOrReplaceChild("shape24",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(5.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape25",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(5.5F, 16, 0, 0.7853982F, 0, 0.0174533F));

        root.addOrReplaceChild("shape42",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape43",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-6, 15, -2, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-6, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape12c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-5, 15, 2, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape12b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-6, 15, -2, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 32)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-5, 15, -4.5F, 0, 0, 0));

        root.addOrReplaceChild("shape13a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 32)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-6, 15, 4.5F, 0, 0, 0));

        root.addOrReplaceChild("shape12d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-6, 15, -2, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-5, 15, 2, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-5, 15, 2, 0, 0, 0));

        root.addOrReplaceChild("shape12g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-5, 15, 2, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape13b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 32)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-6, 15, 4.5F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape13c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 32)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-5, 15, -4.5F, 0.7853982F, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape8.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape9.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape10.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape11.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape24.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape25.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape42.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape43.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        for (double f = 0; f < 0.75; f += 0.125) {
            stack.translate(f, 0, 0);

            double d = 0.125;
            stack.translate(0, 0.9375, d);
            stack.mulPose(Axis.XP.rotationDegrees(phi));
            stack.translate(0, -0.9375, -d);
            shape12c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape12e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape12f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape12g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            stack.translate(0, 0.9375, d);
            stack.mulPose(Axis.XN.rotationDegrees(phi));
            stack.translate(0, -0.9375, -d);

            stack.translate(0, 0.9375, -d);
            stack.mulPose(Axis.XN.rotationDegrees(phi));
            stack.translate(0, -0.9375, d);
            shape12a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape12.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape12b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape12d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            stack.translate(0, 0.9375, -d);
            stack.mulPose(Axis.XP.rotationDegrees(phi));
            stack.translate(0, -0.9375, d);

            d = 0.2875;
            stack.translate(0, 0.9375, -d);
            stack.mulPose(Axis.XN.rotationDegrees(phi));
            stack.translate(0, -0.9375, d);
            shape13.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape13c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            stack.translate(0, 0.9375, -d);
            stack.mulPose(Axis.XP.rotationDegrees(phi));
            stack.translate(0, -0.9375, d);

            stack.translate(0, 0.9375, d);
            stack.mulPose(Axis.XP.rotationDegrees(phi));
            stack.translate(0, -0.9375, -d);
            shape13a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape13b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            stack.translate(0, 0.9375, d);
            stack.mulPose(Axis.XN.rotationDegrees(phi));
            stack.translate(0, -0.9375, -d);

            stack.translate(-f, 0, 0);
        }
    }
}
