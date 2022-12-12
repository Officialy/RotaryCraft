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

public class CentrifugeModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape2i;
    private final ModelPart shape2j;
    private final ModelPart shape2k;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape3a;
    private final ModelPart shape3c;
    private final ModelPart shape3b;
    private final ModelPart shape3c2;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape3h;
    private final ModelPart shape3i;
    private final ModelPart shape3j;
    private final ModelPart shape4c;
    private final ModelPart shape4d;
    private final ModelPart shape4e;
    private final ModelPart root;

    public CentrifugeModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c2 = modelPart.getChild("shape3c2");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape3h = modelPart.getChild("shape3h");
        this.shape3i = modelPart.getChild("shape3i");
        this.shape3j = modelPart.getChild("shape3j");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape4e = modelPart.getChild("shape4e");
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
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, -2.617994F, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, 2.094395F, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, 2.617994F, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, 3.141593F, 0));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, -1.047198F, 0));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, -1.570796F, 0));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-2, 0, 6.5F, 4, 5, 1),
                PartPose.offsetAndRotation(0, 18, 0, 0, -2.094395F, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, -2.617994F, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 44)
                        .addBox(-7, 0, -3, 14, 1, 6),
                PartPose.offsetAndRotation(0, 22, 0, 0, 2.094395F, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-7, 0, -3, 14, 1, 6),
                PartPose.offsetAndRotation(0, 18, 0, 0, 2.094395F, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(-7, 0, -3, 14, 1, 6),
                PartPose.offsetAndRotation(0, 22, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, 0.5235988F, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, 1.047198F, 0));

        root.addOrReplaceChild("shape3c2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, 1.570796F, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, 2.094395F, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, 2.617994F, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, 3.141593F, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, -0.5235988F, 0));

        root.addOrReplaceChild("shape3h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, -1.047198F, 0));

        root.addOrReplaceChild("shape3i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, -1.570796F, 0));

        root.addOrReplaceChild("shape3j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-2, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(0, 23, 0, 0.6108652F, -2.094395F, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(-7, 0, -3, 14, 1, 6),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-7, 0, -3, 14, 1, 6),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-7, 0, -3, 14, 1, 6),
                PartPose.offsetAndRotation(0, 18, 0, 0, 1.047198F, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
