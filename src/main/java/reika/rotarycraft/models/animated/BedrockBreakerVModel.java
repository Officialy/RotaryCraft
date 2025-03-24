package reika.rotarycraft.models.animated;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

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

public class BedrockBreakerVModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3h;
    private final ModelPart shape3i;
    private final ModelPart shape3j;
    private final ModelPart shape3k;
    private final ModelPart shape3cc;
    private final ModelPart shape3l;
    private final ModelPart shape3m;
    private final ModelPart shape3n;
    private final ModelPart shape3o;
    private final ModelPart shape3p;
    private final ModelPart shape3q;
    private final ModelPart shape3r;
    private final ModelPart shape3s;
    private final ModelPart shape3t;
    private final ModelPart shape3u;
    private final ModelPart shape3v;
    private final ModelPart shape3w;
    private final ModelPart shape3x;
    private final ModelPart shape3y;
    private final ModelPart shape3z;
    private final ModelPart shape3aa;
    private final ModelPart shape3bb;
    private final ModelPart shape3cca;
    private final ModelPart shape3dd;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape7f;
    private final ModelPart shape7g;
    private final ModelPart root;

    public BedrockBreakerVModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3h = modelPart.getChild("shape3h");
        this.shape3i = modelPart.getChild("shape3i");
        this.shape3j = modelPart.getChild("shape3j");
        this.shape3k = modelPart.getChild("shape3k");
        this.shape3cc = modelPart.getChild("shape3cc");
        this.shape3l = modelPart.getChild("shape3l");
        this.shape3m = modelPart.getChild("shape3m");
        this.shape3n = modelPart.getChild("shape3n");
        this.shape3o = modelPart.getChild("shape3o");
        this.shape3p = modelPart.getChild("shape3p");
        this.shape3q = modelPart.getChild("shape3q");
        this.shape3r = modelPart.getChild("shape3r");
        this.shape3s = modelPart.getChild("shape3s");
        this.shape3t = modelPart.getChild("shape3t");
        this.shape3u = modelPart.getChild("shape3u");
        this.shape3v = modelPart.getChild("shape3v");
        this.shape3w = modelPart.getChild("shape3w");
        this.shape3x = modelPart.getChild("shape3x");
        this.shape3y = modelPart.getChild("shape3y");
        this.shape3z = modelPart.getChild("shape3z");
        this.shape3aa = modelPart.getChild("shape3aa");
        this.shape3bb = modelPart.getChild("shape3bb");
        this.shape3cca = modelPart.getChild("shape3cca");
        this.shape3dd = modelPart.getChild("shape3dd");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape7f = modelPart.getChild("shape7f");
        this.shape7g = modelPart.getChild("shape7g");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(38, 0)
                        .addBox(0, 0, 0, 12, 16, 16),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 14, 14),
                PartPose.offsetAndRotation(6, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 8)
                        .addBox(0, 0, 0, 1, 15, 15),
                PartPose.offsetAndRotation(5, 8.5F, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3cc",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape3p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3v",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape3w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape3x",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3y",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3z",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3aa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3bb",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3cca",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3dd",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(0, 0, 0, 1, 6, 16),
                PartPose.offsetAndRotation(-8, 18, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 110)
                        .addBox(0, 0, 0, 1, 4, 6),
                PartPose.offsetAndRotation(-8, 14, 2, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 66)
                        .addBox(0, 0, 0, 1, 6, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 110)
                        .addBox(0, 0, 0, 1, 4, 6),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape7f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape7g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, -1.570796F, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

        @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        root.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
