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

public class FertilizerModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape1j;
    private final ModelPart shape1h;
    private final ModelPart shape1r;
    private final ModelPart shape1k;
    private final ModelPart shape1l;
    private final ModelPart shape1m;
    private final ModelPart shape1n;
    private final ModelPart shape1o;
    private final ModelPart shape1p;
    private final ModelPart shape1q;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape2a;
    private final ModelPart shape5;
    private final ModelPart root;

    public FertilizerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1j = modelPart.getChild("shape1j");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape1r = modelPart.getChild("shape1r");
        this.shape1k = modelPart.getChild("shape1k");
        this.shape1l = modelPart.getChild("shape1l");
        this.shape1m = modelPart.getChild("shape1m");
        this.shape1n = modelPart.getChild("shape1n");
        this.shape1o = modelPart.getChild("shape1o");
        this.shape1p = modelPart.getChild("shape1p");
        this.shape1q = modelPart.getChild("shape1q");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape5 = modelPart.getChild("shape5");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 13, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 0)
                        .addBox(1, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 17)
                        .addBox(-2, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 0)
                        .addBox(1, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 10, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 0)
                        .addBox(1, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape1j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 17)
                        .addBox(-2, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape1h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 13, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape1r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 17)
                        .addBox(-2, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape1k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 0)
                        .addBox(1, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape1l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 13, 0, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape1m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 10, 0, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape1n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0, 0));

        root.addOrReplaceChild("shape1o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 17)
                        .addBox(-2, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, 0, 0));

        root.addOrReplaceChild("shape1p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 0)
                        .addBox(1, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11, 0, 0, 0, 0));

        root.addOrReplaceChild("shape1q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, 0, -7, 2, 1, 14),
                PartPose.offsetAndRotation(0, 13, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(0, 0, 0, 16, 3, 16),
                PartPose.offsetAndRotation(-8, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, 0, 3, 2, 3, 1),
                PartPose.offsetAndRotation(0, 14, 0, 0, -1.570796F, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, 0, 3, 2, 3, 1),
                PartPose.offsetAndRotation(0, 14, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, 0, 3, 2, 3, 1),
                PartPose.offsetAndRotation(0, 14, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, 0, 3, 2, 3, 1),
                PartPose.offsetAndRotation(0, 14, 0, 0, 3.141593F, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 17, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 35)
                        .addBox(0, 0, 0, 14, 3, 14),
                PartPose.offsetAndRotation(-7, 18, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-1, 0, -1, 2, 3, 2),
                PartPose.offsetAndRotation(0, 14, 0, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
