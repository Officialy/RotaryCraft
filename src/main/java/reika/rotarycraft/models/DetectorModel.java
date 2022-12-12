package reika.rotarycraft.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static reika.rotarycraft.RotaryCraft.MODID;

public class DetectorModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape4;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape2p;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape3h;
    private final ModelPart shape3j;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape3i;
    private final ModelPart shape3k;
    private final ModelPart shape2i;
    private final ModelPart shape2j;
    private final ModelPart shape2k;
    private final ModelPart shape2l;
    private final ModelPart shape3l;
    private final ModelPart shape3m;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart root;

    public DetectorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape4 = modelPart.getChild("shape4");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape2p = modelPart.getChild("shape2p");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape3h = modelPart.getChild("shape3h");
        this.shape3j = modelPart.getChild("shape3j");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape3i = modelPart.getChild("shape3i");
        this.shape3k = modelPart.getChild("shape3k");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape2l = modelPart.getChild("shape2l");
        this.shape3l = modelPart.getChild("shape3l");
        this.shape3m = modelPart.getChild("shape3m");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 8, 1, 8),
                PartPose.offsetAndRotation(-4, 22, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 3, 2, 1),
                PartPose.offsetAndRotation(3, 19, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 3, 2, 1),
                PartPose.offsetAndRotation(3, 19, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 2, 2),
                PartPose.offsetAndRotation(-6, 19, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 2, 2),
                PartPose.offsetAndRotation(5, 19, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-5, 21, 2, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(4, 21, 2, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(2, 21, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 1),
                PartPose.offsetAndRotation(-3, 19, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(116, 0)
                        .addBox(-0.5F, 0, -0.5F, 1, 8, 1),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0, -0.2617994F));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 2, 2),
                PartPose.offsetAndRotation(-6, 19, -5, 0, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 2, 2),
                PartPose.offsetAndRotation(5, 19, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 3, 2, 1),
                PartPose.offsetAndRotation(-6, 19, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 3, 2, 1),
                PartPose.offsetAndRotation(-6, 19, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(-5, 21, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(-5, 21, 4, 0, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-5, 21, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 1, 2, 6),
                PartPose.offsetAndRotation(-7, 19, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(4, 21, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 1, 2, 6),
                PartPose.offsetAndRotation(6, 19, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(2, 21, 4, 0, 0, 0));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 1),
                PartPose.offsetAndRotation(-3, 19, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(96, 0)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(-5, 22, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(96, 0)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(4, 22, -2, 0, 0, 0));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(106, 0)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-2, 22, 4, 0, 0, 0));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(106, 0)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-2, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 6, 1, 1),
                PartPose.offsetAndRotation(-3, 21, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 6, 1, 1),
                PartPose.offsetAndRotation(-3, 21, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(5, 21, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(-6, 21, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(116, 0)
                        .addBox(-0.5F, 0, -0.5F, 1, 8, 1),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0, 0.2617994F));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(116, 0)
                        .addBox(-0.5F, 0, -0.5F, 1, 8, 1),
                PartPose.offsetAndRotation(0, 15, 0, 0.2617994F, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(116, 0)
                        .addBox(-0.5F, 0, -0.5F, 1, 8, 1),
                PartPose.offsetAndRotation(0, 15, 0, -0.2617994F, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(107, 2)
                        .addBox(-1, 0, -1, 2, 3, 2),
                PartPose.offsetAndRotation(0, 13, 0, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
