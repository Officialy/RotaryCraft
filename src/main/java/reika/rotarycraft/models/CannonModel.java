package reika.rotarycraft.models;

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

public class CannonModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

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
    private final ModelPart shape2l;
    private final ModelPart shape2m;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape3h;
    private final ModelPart shape3i;
    private final ModelPart shape3j;
    private final ModelPart shape3k;
    private final ModelPart shape3l;
    private final ModelPart shape3m;
    private final ModelPart root;

    public CannonModel(ModelPart modelPart) {
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
        this.shape2l = modelPart.getChild("shape2l");
        this.shape2m = modelPart.getChild("shape2m");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape3h = modelPart.getChild("shape3h");
        this.shape3i = modelPart.getChild("shape3i");
        this.shape3j = modelPart.getChild("shape3j");
        this.shape3k = modelPart.getChild("shape3k");
        this.shape3l = modelPart.getChild("shape3l");
        this.shape3m = modelPart.getChild("shape3m");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 8, 16),
                PartPose.offsetAndRotation(-8, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 24)
                        .addBox(0, 0, 0, 1, 1, 16),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 24)
                        .addBox(0, 0, 0, 1, 1, 16),
                PartPose.offsetAndRotation(7, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 34)
                        .addBox(0, 0, 0, 6, 1, 16),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(86, 51)
                        .addBox(0, 0, 0, 5, 1, 16),
                PartPose.offsetAndRotation(-8, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(88, 68)
                        .addBox(0, 0, 0, 4, 1, 16),
                PartPose.offsetAndRotation(-8, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(90, 85)
                        .addBox(0, 0, 0, 3, 1, 16),
                PartPose.offsetAndRotation(-8, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 102)
                        .addBox(0, 0, 0, 2, 1, 16),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 17)
                        .addBox(0, 0, 0, 7, 1, 16),
                PartPose.offsetAndRotation(-8, 15, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 7, 1, 16),
                PartPose.offsetAndRotation(1, 15, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 109)
                        .addBox(0, 0, 0, 6, 1, 16),
                PartPose.offsetAndRotation(2, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 92)
                        .addBox(0, 0, 0, 5, 1, 16),
                PartPose.offsetAndRotation(3, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 75)
                        .addBox(0, 0, 0, 4, 1, 16),
                PartPose.offsetAndRotation(4, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 58)
                        .addBox(0, 0, 0, 3, 1, 16),
                PartPose.offsetAndRotation(5, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 2, 1, 16),
                PartPose.offsetAndRotation(6, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 41)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(39, 68)
                        .addBox(0, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(-1, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(38, 61)
                        .addBox(0, 0, 0, 4, 1, 6),
                PartPose.offsetAndRotation(-2, 14, 2, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(38, 55)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 13, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 50)
                        .addBox(0, 0, 0, 8, 1, 4),
                PartPose.offsetAndRotation(-4, 12, 4, 0, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 46)
                        .addBox(0, 0, 0, 10, 1, 3),
                PartPose.offsetAndRotation(-5, 11, 5, 0, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 43)
                        .addBox(0, 0, 0, 12, 1, 2),
                PartPose.offsetAndRotation(-6, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 76)
                        .addBox(0, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(-1, 15, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 84)
                        .addBox(0, 0, 0, 4, 1, 6),
                PartPose.offsetAndRotation(-2, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 91)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 97)
                        .addBox(0, 0, 0, 8, 1, 4),
                PartPose.offsetAndRotation(-4, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 102)
                        .addBox(0, 0, 0, 10, 1, 3),
                PartPose.offsetAndRotation(-5, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 106)
                        .addBox(0, 0, 0, 12, 1, 2),
                PartPose.offsetAndRotation(-6, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 109)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 9, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
