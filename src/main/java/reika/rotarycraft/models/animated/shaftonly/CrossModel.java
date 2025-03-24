package reika.rotarycraft.models.animated.shaftonly;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import org.joml.Vector3f;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

public class CrossModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/transmission/shaft/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape13;
    private final ModelPart shape12;
    private final ModelPart shape14;
    private final ModelPart shape14a;
    private final ModelPart shape14b;
    private final ModelPart shape14c;
    private final ModelPart shape14b1;
    private final ModelPart shape14b2;
    private final ModelPart shape14b3;
    private final ModelPart shape14b4;
    private final ModelPart shape14b5;
    private final ModelPart shape14b6;
    private final ModelPart shape14b7;
    private final ModelPart shape14b8;
    private final ModelPart shape14b9;
    private final ModelPart shape14b10;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart root;

    public CrossModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape13 = modelPart.getChild("shape13");
        this.shape12 = modelPart.getChild("shape12");
        this.shape14 = modelPart.getChild("shape14");
        this.shape14a = modelPart.getChild("shape14a");
        this.shape14b = modelPart.getChild("shape14b");
        this.shape14c = modelPart.getChild("shape14c");
        this.shape14b1 = modelPart.getChild("shape14b1");
        this.shape14b2 = modelPart.getChild("shape14b2");
        this.shape14b3 = modelPart.getChild("shape14b3");
        this.shape14b4 = modelPart.getChild("shape14b4");
        this.shape14b5 = modelPart.getChild("shape14b5");
        this.shape14b6 = modelPart.getChild("shape14b6");
        this.shape14b7 = modelPart.getChild("shape14b7");
        this.shape14b8 = modelPart.getChild("shape14b8");
        this.shape14b9 = modelPart.getChild("shape14b9");
        this.shape14b10 = modelPart.getChild("shape14b10");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
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
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(7, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(-8, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 15)
                        .addBox(0, 0, 0, 14, 12, 1),
                PartPose.offsetAndRotation(-7, 11, 7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 15)
                        .addBox(0, 0, 0, 14, 12, 1),
                PartPose.offsetAndRotation(-7, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape14a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, 3.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape14b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 20, -4, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape14c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape14b1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-1, -1, 0, 2, 2, 7),
                PartPose.offsetAndRotation(0, 20, -3.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape14b2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 20, -4, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape14b3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 16, -4, 0, 0, 0));

        root.addOrReplaceChild("shape14b4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 16, -4, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape14b5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 16, 3, 0, 0, 0));

        root.addOrReplaceChild("shape14b6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 16, 3, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape14b7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 20, 3, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape14b8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 20, 3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape14b9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape14b10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-1, -1, 0, 2, 2, 7),
                PartPose.offsetAndRotation(0, 20, -3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 4, 9, 1),
                PartPose.offsetAndRotation(-2, 14, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 4, 5, 1),
                PartPose.offsetAndRotation(-2, 18, -0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 4, 9, 1),
                PartPose.offsetAndRotation(-2, 14, -6, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 32);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float phi2) {  //using "theta" param as phi2
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        double d = 0.25;
        stack.translate(0, 1 + d, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(0, -1 - d, 0);
        shape14b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b10.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b8.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1 + d, 0);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(0, -1 - d, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape14.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b9.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi2));
        stack.translate(0, -1, 0);
        shape13.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(phi2));
        stack.translate(0, -1, 0);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
