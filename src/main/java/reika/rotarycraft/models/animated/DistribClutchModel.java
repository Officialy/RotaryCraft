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

public class DistribClutchModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1a;
    private final ModelPart shape2a;
    private final ModelPart shape2c;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4_r;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5_rb;
    private final ModelPart shape5_r_r;
    private final ModelPart shape5_r_r_r;
    private final ModelPart shape4_r_r_r;
    private final ModelPart shape4_r_r;
    private final ModelPart shape51_r;
    private final ModelPart shape51_r_r;
    private final ModelPart shape51_r_r_r;
    private final ModelPart shape5_r1;
    private final ModelPart shape41_r;
    private final ModelPart shape41_r_r;
    private final ModelPart shape41_r_r_r;
    private final ModelPart shape4_r1;
    private final ModelPart shape7a;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape1a2;
    private final ModelPart shape1a4;
    private final ModelPart shape1a3;
    private final ModelPart shape5_r;
    private final ModelPart shape5_rb2;
    private final ModelPart shapez2;
    private final ModelPart shapez;
    private final ModelPart shapez1;
    private final ModelPart shapebase;
    private final ModelPart root;

    public DistribClutchModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1a = modelPart.getChild("shape1a");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4_r = modelPart.getChild("shape4_r");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5_rb = modelPart.getChild("shape5_rb");
        this.shape5_r_r = modelPart.getChild("shape5_r_r");
        this.shape5_r_r_r = modelPart.getChild("shape5_r_r_r");
        this.shape4_r_r_r = modelPart.getChild("shape4_r_r_r");
        this.shape4_r_r = modelPart.getChild("shape4_r_r");
        this.shape51_r = modelPart.getChild("shape51_r");
        this.shape51_r_r = modelPart.getChild("shape51_r_r");
        this.shape51_r_r_r = modelPart.getChild("shape51_r_r_r");
        this.shape5_r1 = modelPart.getChild("shape5_r1");
        this.shape41_r = modelPart.getChild("shape41_r");
        this.shape41_r_r = modelPart.getChild("shape41_r_r");
        this.shape41_r_r_r = modelPart.getChild("shape41_r_r_r");
        this.shape4_r1 = modelPart.getChild("shape4_r1");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape1a2 = modelPart.getChild("shape1a2");
        this.shape1a4 = modelPart.getChild("shape1a4");
        this.shape1a3 = modelPart.getChild("shape1a3");
        this.shape5_r = modelPart.getChild("shape5_r");
        this.shape5_rb2 = modelPart.getChild("shape5_rb2");
        this.shapez2 = modelPart.getChild("shapez2");
        this.shapez = modelPart.getChild("shapez");
        this.shapez1 = modelPart.getChild("shapez1");
        this.shapebase = modelPart.getChild("shapebase");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 23, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-8, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(7, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(60, 40)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(3.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_rb",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 87)
                        .addBox(0, -0.5F, -0.5F, 7, 1, 1),
                PartPose.offsetAndRotation(-3.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(3, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(3.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, 2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape51_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape51_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(3, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape51_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(-4, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5_r1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(-4, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape41_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, 2.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape41_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(60, 40)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape41_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 40)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape4_r1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 40)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 42)
                        .addBox(0, 0, 0, 1, 8, 14),
                PartPose.offsetAndRotation(7, 13, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 8, 14),
                PartPose.offsetAndRotation(-8, 13, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 28)
                        .addBox(0, 0, 0, 14, 8, 1),
                PartPose.offsetAndRotation(-7, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 14)
                        .addBox(0, 0, 0, 14, 8, 1),
                PartPose.offsetAndRotation(-7, 13, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(-8, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(7, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(7, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_rb2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 87)
                        .addBox(0, -0.5F, -0.5F, 7, 1, 1),
                PartPose.offsetAndRotation(-3.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shapez2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(22, 0)
                        .addBox(0, 0, -1.5F, 1, 10, 3),
                PartPose.offsetAndRotation(5, 14, 0, 0, 0, 0));

        root.addOrReplaceChild("shapez",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(15, 0)
                        .addBox(-0.5F, 0, -1, 1, 10, 2),
                PartPose.offsetAndRotation(0, 14, 0, 0, 0, 0));

        root.addOrReplaceChild("shapez1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(22, 0)
                        .addBox(0, 0, -1.5F, 1, 10, 3),
                PartPose.offsetAndRotation(-6, 14, 0, 0, 0, 0));

        root.addOrReplaceChild("shapebase",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(38, 49)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 23, -7, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
