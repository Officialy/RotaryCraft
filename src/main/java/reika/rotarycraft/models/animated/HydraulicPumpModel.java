package reika.rotarycraft.models.animated;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;

import static reika.rotarycraft.RotaryCraft.MODID;

public class HydraulicPumpModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape1a;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape4d;
    private final ModelPart shape4e;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape10;
    private final ModelPart shape11;
    private final ModelPart shape11a;
    private final ModelPart shape11b;
    private final ModelPart shape11c;
    private final ModelPart shape11d;
    private final ModelPart shape12;
    private final ModelPart root;

    public HydraulicPumpModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape4e = modelPart.getChild("shape4e");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape10 = modelPart.getChild("shape10");
        this.shape11 = modelPart.getChild("shape11");
        this.shape11a = modelPart.getChild("shape11a");
        this.shape11b = modelPart.getChild("shape11b");
        this.shape11c = modelPart.getChild("shape11c");
        this.shape11d = modelPart.getChild("shape11d");
        this.shape12 = modelPart.getChild("shape12");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 62)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 15, -6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 69)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-2.5F, 0, -2.5F, 5, 5, 5),
                PartPose.offsetAndRotation(-3, 16, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(-1, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-2.5F, 0, -2.5F, 5, 5, 5),
                PartPose.offsetAndRotation(-3, 16, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 99)
                        .addBox(-1, 0, -1, 2, 1, 2),
                PartPose.offsetAndRotation(-3, 15.5F, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-2.5F, 0, -2.5F, 5, 5, 5),
                PartPose.offsetAndRotation(3, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 12)
                        .addBox(-1, 0, -3, 1, 6, 3),
                PartPose.offsetAndRotation(-7.5F, 15, 2, 0, -2.356194F, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 1, 6, 4),
                PartPose.offsetAndRotation(-7.5F, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 12)
                        .addBox(-1, 0, 0, 1, 6, 3),
                PartPose.offsetAndRotation(-7.5F, 15, -2, 0, 2.356194F, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(80, 0)
                        .addBox(0, 0, 0, 1, 6, 4),
                PartPose.offsetAndRotation(6.5F, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(80, 12)
                        .addBox(0, 0, 0, 1, 6, 3),
                PartPose.offsetAndRotation(7.5F, 15, -2, 0, -2.356194F, 0));

        root.addOrReplaceChild("shape4e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(80, 12)
                        .addBox(0, 0, -3, 1, 6, 3),
                PartPose.offsetAndRotation(7.5F, 15, 2, 0, 2.356194F, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 43)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(2.3F, 15, 3.1F, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(20, 43)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(-5.3F, 15, 3.1F, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 51)
                        .addBox(0, 0, 0, 8, 8, 1),
                PartPose.offsetAndRotation(-4, 13, -5, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(-1, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(11, 62)
                        .addBox(-0.5F, -0.5F, 0, 1, 1, 1),
                PartPose.offsetAndRotation(0, 20, -6.5F, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 75)
                        .addBox(0, 0, 0, 16, 2, 13),
                PartPose.offsetAndRotation(-8, 21, -5, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 95)
                        .addBox(0, 0, 0, 4, 2, 1),
                PartPose.offsetAndRotation(4, 21, -6, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 91)
                        .addBox(0, 0, 0, 4, 2, 1),
                PartPose.offsetAndRotation(-8, 21, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 62)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 15, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 62)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 20, -6, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(2.3F, 15, -4.1F, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(29, 43)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(-5.3F, 15, -4.1F, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(-5.3F, 15, 3.1F, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-2.5F, 0, -2.5F, 5, 5, 5),
                PartPose.offsetAndRotation(3, 16, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 99)
                        .addBox(-1, 0, -1, 2, 1, 2),
                PartPose.offsetAndRotation(3, 15.5F, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 99)
                        .addBox(-1, 0, -1, 2, 1, 2),
                PartPose.offsetAndRotation(3, 15.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 99)
                        .addBox(-1, 0, -1, 2, 1, 2),
                PartPose.offsetAndRotation(-3, 15.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 62)
                        .addBox(-2, -2, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 20, -6, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(11, 62)
                        .addBox(-0.5F, -0.5F, 0, 1, 1, 1),
                PartPose.offsetAndRotation(0, 20, -6.5F, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 114)
                        .addBox(-3, -3, 0, 6, 4, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 103)
                        .addBox(-3, -3, 0, 6, 9, 1),
                PartPose.offsetAndRotation(0, 15, 7, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 18)
                        .addBox(0, 0, 0, 1, 9, 4),
                PartPose.offsetAndRotation(3, 12, 4, 0, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 18)
                        .addBox(0, 0, 0, 1, 9, 4),
                PartPose.offsetAndRotation(-4, 12, 4, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(25, 18)
                        .addBox(0, 0, 0, 6, 1, 2),
                PartPose.offsetAndRotation(-3, 12, 5, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(68, 120)
                        .addBox(0, 0, 0, 11, 1, 1),
                PartPose.offsetAndRotation(-5.5F, 14, 3, 0, 0, 0));

        root.addOrReplaceChild("shape11a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 120)
                        .addBox(0, 0, 0, 15, 1, 4),
                PartPose.offsetAndRotation(-7.5F, 14, -2, 0, 0, 0));

        root.addOrReplaceChild("shape11b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(39, 120)
                        .addBox(0, 0, 0, 13, 1, 1),
                PartPose.offsetAndRotation(-6.5F, 14, 2, 0, 0, 0));

        root.addOrReplaceChild("shape11c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(39, 123)
                        .addBox(0, 0, 0, 13, 1, 1),
                PartPose.offsetAndRotation(-6.5F, 14, -3, 0, 0, 0));

        root.addOrReplaceChild("shape11d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(68, 123)
                        .addBox(0, 0, 0, 11, 1, 1),
                PartPose.offsetAndRotation(-5.5F, 14, -4, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(93, 0)
                        .addBox(0, 0, 0, 3, 8, 1),
                PartPose.offsetAndRotation(-1.5F, 13, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
