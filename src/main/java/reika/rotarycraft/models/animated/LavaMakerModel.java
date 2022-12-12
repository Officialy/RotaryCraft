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

public class LavaMakerModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape6;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape9b;
    private final ModelPart shape9c;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape5g;
    private final ModelPart shape5h;
    private final ModelPart shape5i;
    private final ModelPart shape5j;
    private final ModelPart shape5k;
    private final ModelPart shape5l;
    private final ModelPart shape5m;
    private final ModelPart shape5n;
    private final ModelPart shape5o;
    private final ModelPart shape5p;
    private final ModelPart shape5q;
    private final ModelPart root;

    public LavaMakerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape6 = modelPart.getChild("shape6");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape9b = modelPart.getChild("shape9b");
        this.shape9c = modelPart.getChild("shape9c");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape5g = modelPart.getChild("shape5g");
        this.shape5h = modelPart.getChild("shape5h");
        this.shape5i = modelPart.getChild("shape5i");
        this.shape5j = modelPart.getChild("shape5j");
        this.shape5k = modelPart.getChild("shape5k");
        this.shape5l = modelPart.getChild("shape5l");
        this.shape5m = modelPart.getChild("shape5m");
        this.shape5n = modelPart.getChild("shape5n");
        this.shape5o = modelPart.getChild("shape5o");
        this.shape5p = modelPart.getChild("shape5p");
        this.shape5q = modelPart.getChild("shape5q");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 0)
                        .addBox(0, 0, 0, 1, 14, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 0)
                        .addBox(0, 0, 0, 1, 14, 16),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 14, 1),
                PartPose.offsetAndRotation(-7, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 14, 1),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 21, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 61)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(4, 13, -4, -0.2443461F, -0.9773844F, 0.6632251F));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 62)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(4, 13, 0, -0.5759587F, 0.3839724F, 0.7679449F));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 67)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(0, 13, -4, -1.134464F, -0.296706F, 0.4712389F));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-8, 22, 6, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(6, 22, 6, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(6, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 0)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 0)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-0.5F, 0, -6, 1, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 2.356194F, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-0.5F, 0, -6, 1, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0, 0));

        root.addOrReplaceChild("shape9b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-0.5F, 0, -6, 1, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape9c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-0.5F, 0, -6, 1, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 63)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(-4, 13, -4, -0.5759587F, -1.134464F, 0.296706F));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(4, 13, 4, -0.6457718F, 0.3839724F, 0.1745329F));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(0, 13, 0, -0.5061455F, 0.9948377F, 0.2094395F));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(-4, 13, 0, -0.837758F, -0.2268928F, 0.7679449F));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(-4, 13, 4, 0.4014257F, 0.5759587F, -0.1919862F));

        root.addOrReplaceChild("shape5h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(0, 13, 4, 0.4014257F, -0.296706F, 0.3839724F));

        root.addOrReplaceChild("shape5i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(-4, 19, -4, 0.5759587F, 1.134464F, 0.296706F));

        root.addOrReplaceChild("shape5j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(0, 19, -4, 1.134464F, 0.296706F, 0.4712389F));

        root.addOrReplaceChild("shape5k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(4, 19, -4, 0.2443461F, 0.9773844F, 0.6632251F));

        root.addOrReplaceChild("shape5l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(-4, 19, 0, 0.837758F, 0.2268928F, 0.7679449F));

        root.addOrReplaceChild("shape5m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(0, 19, 0, 0.5061455F, -0.9948377F, 0.2094395F));

        root.addOrReplaceChild("shape5n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(4, 19, 0, 0.5759587F, -0.3839724F, 0.7679449F));

        root.addOrReplaceChild("shape5o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(-4, 19, 4, -0.4014257F, -0.5759587F, -0.1919862F));

        root.addOrReplaceChild("shape5p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(0, 19, 4, -0.4014257F, 0.296706F, 0.3839724F));

        root.addOrReplaceChild("shape5q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-2, -2, -2, 4, 4, 4),
                PartPose.offsetAndRotation(4, 19, 4, 0.6457718F, -0.3839724F, 0.1745329F));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
