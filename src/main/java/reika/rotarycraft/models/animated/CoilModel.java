package reika.rotarycraft.models.animated;

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
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class CoilModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/transmission/shaft/coiltex.png");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape31;
    private final ModelPart shape32;
    private final ModelPart shape33;
    private final ModelPart shape34;
    private final ModelPart shape35;
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
    private final ModelPart shape3_a;
    private final ModelPart shape3_b;
    private final ModelPart shape3_c;
    private final ModelPart shape3_d;
    private final ModelPart shape3_e;
    private final ModelPart shape3_f;
    private final ModelPart shape3_g;
    private final ModelPart shape3_h;
    private final ModelPart shape3_i;
    private final ModelPart shape3_j;
    private final ModelPart shape3_k;
    private final ModelPart shape3_l;
    private final ModelPart shape3_m;
    private final ModelPart shape3_n;
    private final ModelPart root;

    public CoilModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape31 = modelPart.getChild("shape31");
        this.shape32 = modelPart.getChild("shape32");
        this.shape33 = modelPart.getChild("shape33");
        this.shape34 = modelPart.getChild("shape34");
        this.shape35 = modelPart.getChild("shape35");
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
        this.shape3_a = modelPart.getChild("shape3_a");
        this.shape3_b = modelPart.getChild("shape3_b");
        this.shape3_c = modelPart.getChild("shape3_c");
        this.shape3_d = modelPart.getChild("shape3_d");
        this.shape3_e = modelPart.getChild("shape3_e");
        this.shape3_f = modelPart.getChild("shape3_f");
        this.shape3_g = modelPart.getChild("shape3_g");
        this.shape3_h = modelPart.getChild("shape3_h");
        this.shape3_i = modelPart.getChild("shape3_i");
        this.shape3_j = modelPart.getChild("shape3_j");
        this.shape3_k = modelPart.getChild("shape3_k");
        this.shape3_l = modelPart.getChild("shape3_l");
        this.shape3_m = modelPart.getChild("shape3_m");
        this.shape3_n = modelPart.getChild("shape3_n");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape31",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 29)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape32",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(-8, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape33",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(7, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape34",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, 7, 0, 0, 0));

        root.addOrReplaceChild("shape35",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-5, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-5, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(3, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(3, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-5, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(3, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(3, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-4, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-3, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-3, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-3, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-3, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(1, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(1, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(1, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(1, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -5, -5, 1, 10, 10),
                PartPose.offsetAndRotation(-5, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-4, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3v",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(0, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-4, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3x",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-4, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3y",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(2, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3z",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-2, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(4, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3_b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-2, 16, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape3_c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-2, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3_d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-2, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3_e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(-2, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3_f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(0, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3_g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(0, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3_h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3_i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(2, 16, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape3_j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(2, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3_k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(2, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3_l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(4, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3_m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(4, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3_n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, -4, -4, 1, 8, 8),
                PartPose.offsetAndRotation(4, 16, 0, 0.3926991F, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {

        shape31.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape32.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape33.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape34.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape35.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3o.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3p.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3q.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3r.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3s.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3t.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3u.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3v.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3w.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3x.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3y.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3z.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        float off = 12.5F;
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(off));
        stack.translate(0, -1, 0);
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3o.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3p.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3q.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3r.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3s.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3t.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3u.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3v.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3w.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3x.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3y.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3z.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(off));
        stack.translate(0, -1, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(phi));
        stack.translate(0, -1, 0);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
