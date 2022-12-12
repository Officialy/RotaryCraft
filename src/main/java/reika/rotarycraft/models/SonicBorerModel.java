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

public class SonicBorerModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape1h;
    private final ModelPart shape1i;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape1j;
    private final ModelPart shape1k;
    private final ModelPart shape1l;
    private final ModelPart shape1m;
    private final ModelPart shape1n;
    private final ModelPart shape1o;
    private final ModelPart shape1p;
    private final ModelPart shape1q;
    private final ModelPart shape1r;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape1s;
    private final ModelPart shape1t;
    private final ModelPart shape4a;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape2i;
    private final ModelPart shape2j;
    private final ModelPart shape2k;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape8b;
    private final ModelPart shape8c;
    private final ModelPart shape8d;
    private final ModelPart shape8e;
    private final ModelPart shape8f;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart root;

    public SonicBorerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape1i = modelPart.getChild("shape1i");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape1j = modelPart.getChild("shape1j");
        this.shape1k = modelPart.getChild("shape1k");
        this.shape1l = modelPart.getChild("shape1l");
        this.shape1m = modelPart.getChild("shape1m");
        this.shape1n = modelPart.getChild("shape1n");
        this.shape1o = modelPart.getChild("shape1o");
        this.shape1p = modelPart.getChild("shape1p");
        this.shape1q = modelPart.getChild("shape1q");
        this.shape1r = modelPart.getChild("shape1r");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape1s = modelPart.getChild("shape1s");
        this.shape1t = modelPart.getChild("shape1t");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape8b = modelPart.getChild("shape8b");
        this.shape8c = modelPart.getChild("shape8c");
        this.shape8d = modelPart.getChild("shape8d");
        this.shape8e = modelPart.getChild("shape8e");
        this.shape8f = modelPart.getChild("shape8f");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 122)
                        .addBox(0, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(2, 17.5F, -6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 122)
                        .addBox(-1, 0, 0, 2, 2, 3),
                PartPose.offsetAndRotation(-2, 17.5F, -6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(0, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(0, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 122)
                        .addBox(-2, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(-2, 14.5F, -6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(-4, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 55)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(0, 15, 4.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 10)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(-6, 16, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 114)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 55)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(0, 15, 4.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 61)
                        .addBox(-0.5F, -6.5F, 0, 1, 13, 1),
                PartPose.offsetAndRotation(0, 15, 5.9F, 0, 0, 2.373648F));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, -2.356194F));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 61)
                        .addBox(-0.5F, -6.5F, 0, 1, 13, 1),
                PartPose.offsetAndRotation(0, 15, 5.9F, 0, 0, 0.8028515F));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 61)
                        .addBox(-0.5F, -6.5F, 0, 1, 13, 1),
                PartPose.offsetAndRotation(0, 15, 5.9F, 0, 0, 1.58825F));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.9686577F));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.2094395F));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.5846853F));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 1.387537F));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-4, -4, 0, 8, 8, 6),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape1j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(4, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 67)
                        .addBox(-1, -1, 0, 2, 1, 2),
                PartPose.offsetAndRotation(0, 21, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 10)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(6, 16, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 103)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(0, 22, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 103)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(0, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 67)
                        .addBox(-1, -1, 0, 2, 1, 2),
                PartPose.offsetAndRotation(0, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(-2, 14.5F, -8, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(2, 17.5F, -8, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(-2, 17.5F, -8, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(73, 0)
                        .addBox(0, 0, 0, 12, 2, 5),
                PartPose.offsetAndRotation(-6, 8, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(-1, -1, 0, 2, 2, 4),
                PartPose.offsetAndRotation(2, 14.5F, -8, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 122)
                        .addBox(-1, -2, 0, 2, 2, 3),
                PartPose.offsetAndRotation(2, 14.5F, -6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 0)
                        .addBox(0, 0, 0, 16, 13, 5),
                PartPose.offsetAndRotation(-8, 10, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 61)
                        .addBox(-0.5F, -6.5F, 0, 1, 13, 1),
                PartPose.offsetAndRotation(0, 15, 5.9F, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, 2.356194F));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0.5F, -6, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0, 15, 5.5F, 0, 0, -1.570796F));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 40)
                        .addBox(0, 0, 0, 12, 1, 2),
                PartPose.offsetAndRotation(-6, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 44)
                        .addBox(0, 0, 0, 1, 13, 3),
                PartPose.offsetAndRotation(-8, 10, 5, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 44)
                        .addBox(-1, 0, 0, 1, 3, 7),
                PartPose.offsetAndRotation(-5, 9, 0.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 44)
                        .addBox(0, 0, 0, 1, 13, 3),
                PartPose.offsetAndRotation(7, 10, 5, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 44)
                        .addBox(-1, 0, 0, 1, 3, 7),
                PartPose.offsetAndRotation(7, 11, 0.5F, 0, 0, 2.356194F));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 7)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 21, 7, 0, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 82)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(3, 14.5F, 7, 0, 0, 0));

        root.addOrReplaceChild("shape8b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 7)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 19, 7, 0, 0, 0));

        root.addOrReplaceChild("shape8c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 7)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 12, 7, 0, 0, 0));

        root.addOrReplaceChild("shape8d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 7)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 17, 7, 0, 0, 0));

        root.addOrReplaceChild("shape8e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 7)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 10, 7, 0, 0, 0));

        root.addOrReplaceChild("shape8f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 82)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-7, 14.5F, 7, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 61)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(-3, 13, 7, 0, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 61)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(2, 13, 7, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
