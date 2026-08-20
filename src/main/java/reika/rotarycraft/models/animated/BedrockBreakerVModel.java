package reika.rotarycraft.models.animated;

import com.mojang.math.Axis;
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
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;

import static reika.rotarycraft.RotaryCraft.MODID;

public class BedrockBreakerVModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/bedrockvtex.png");

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
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public BedrockBreakerVModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

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
                        .texOffs(38, 0)
                        .addBox(0, 0, 0, 12, 16, 16),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 14, 14),
                PartPose.offsetAndRotation(6, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .texOffs(0, 8)
                        .addBox(0, 0, 0, 1, 15, 15),
                PartPose.offsetAndRotation(5, 8.5F, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3h",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3i",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3j",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3k",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3cc",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3l",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3m",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3n",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3o",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3, -1, 1, 4, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape3p",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3q",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3r",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3s",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3t",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3u",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3v",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 5, -2, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape3w",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape3x",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape3y",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape3z",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape3aa",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3bb",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape3cca",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, -1.570796F, 0, 0));

        root.addOrReplaceChild("shape3dd",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 3.5F, 1, 1, 3, 1),
                PartPose.offsetAndRotation(7, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 88)
                        .addBox(0, 0, 0, 1, 6, 16),
                PartPose.offsetAndRotation(-8, 18, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .texOffs(0, 110)
                        .addBox(0, 0, 0, 1, 4, 6),
                PartPose.offsetAndRotation(-8, 14, 2, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .texOffs(0, 66)
                        .addBox(0, 0, 0, 1, 6, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .texOffs(14, 110)
                        .addBox(0, 0, 0, 1, 4, 6),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, -2.356194F, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, 3.141593F, 0, 0));

        root.addOrReplaceChild("shape7f",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape7g",
                CubeListBuilder.create()
                        .texOffs(40, 70)
                        .addBox(0, -7, -1, 16, 1, 3),
                PartPose.offsetAndRotation(8, 16, 0, -1.570796F, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

        @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int light, BlockEntity te,
                          ArrayList<?> conditions, float phi, float theta) {
        int step = (Integer)conditions.get(0);
        shape1.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape3.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3b.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3c.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3d.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3e.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3f.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3g.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3h.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3i.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3j.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3k.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3cc.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3l.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3m.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3n.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3o.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3p.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3q.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3s.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3t.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3u.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3v.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3w.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3x.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3y.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3z.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3aa.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3bb.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3cca.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3dd.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        float f = (Float)conditions.get(1);
        for (int i = 1; i < step; i++) {
            int a = i-1;
            stack.pushPose();
            stack.translate(a, 0, 0);
            if (i == step-1) {
                stack.translate(-f/2, 0, 0);
                stack.scale(1+f, 1, 1);
            }
            shape7.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape7a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape7b.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape7c.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape7d.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape7e.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape7f.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape7g.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            stack.popPose();
        }

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(-phi));
        stack.translate(0, -1, 0);
        shape4.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4b.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4c.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

