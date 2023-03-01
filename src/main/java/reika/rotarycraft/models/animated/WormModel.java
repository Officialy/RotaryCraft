package reika.rotarycraft.models.animated;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelLayerLocation;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class WormModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape10;
    private final ModelPart shape11;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape12a;
    private final ModelPart shape12b;
    private final ModelPart shape12c;
    private final ModelPart shape12d;
    private final ModelPart shape12e;
    private final ModelPart shape12f;
    private final ModelPart shape12g;
    private final ModelPart shape12h;
    private final ModelPart shape13a;
    private final ModelPart shape13b;
    private final ModelPart shape13c;
    private final ModelPart shape13d;
    private final ModelPart shape13e;
    private final ModelPart shape13f;
    private final ModelPart shape13g;
    private final ModelPart shape13h;
    private final ModelPart shape14;
    private final ModelPart shape13aa;
    private final ModelPart shape12aa;
    private final ModelPart shape121;
    private final ModelPart shape122;
    private final ModelPart shape123;
    private final ModelPart shape124;
    private final ModelPart shape12a1;
    private final ModelPart shape12a2;
    private final ModelPart shape12a3;
    private final ModelPart shape12a4;
    private final ModelPart shape12a6;
    private final ModelPart shape12a5;
    private final ModelPart shape12a7;
    private final ModelPart shape12a8;
    private final ModelPart shape12a9;
    private final ModelPart shape12a0;
    private final ModelPart shape122a;
    private final ModelPart shape122b;
    private final ModelPart shape15a;
    private final ModelPart shape15;
    private final ModelPart root;

    public WormModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9 = modelPart.getChild("shape9");
        this.shape10 = modelPart.getChild("shape10");
        this.shape11 = modelPart.getChild("shape11");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape12a = modelPart.getChild("shape12a");
        this.shape12b = modelPart.getChild("shape12b");
        this.shape12c = modelPart.getChild("shape12c");
        this.shape12d = modelPart.getChild("shape12d");
        this.shape12e = modelPart.getChild("shape12e");
        this.shape12f = modelPart.getChild("shape12f");
        this.shape12g = modelPart.getChild("shape12g");
        this.shape12h = modelPart.getChild("shape12h");
        this.shape13a = modelPart.getChild("shape13a");
        this.shape13b = modelPart.getChild("shape13b");
        this.shape13c = modelPart.getChild("shape13c");
        this.shape13d = modelPart.getChild("shape13d");
        this.shape13e = modelPart.getChild("shape13e");
        this.shape13f = modelPart.getChild("shape13f");
        this.shape13g = modelPart.getChild("shape13g");
        this.shape13h = modelPart.getChild("shape13h");
        this.shape14 = modelPart.getChild("shape14");
        this.shape13aa = modelPart.getChild("shape13aa");
        this.shape12aa = modelPart.getChild("shape12aa");
        this.shape121 = modelPart.getChild("shape121");
        this.shape122 = modelPart.getChild("shape122");
        this.shape123 = modelPart.getChild("shape123");
        this.shape124 = modelPart.getChild("shape124");
        this.shape12a1 = modelPart.getChild("shape12a1");
        this.shape12a2 = modelPart.getChild("shape12a2");
        this.shape12a3 = modelPart.getChild("shape12a3");
        this.shape12a4 = modelPart.getChild("shape12a4");
        this.shape12a6 = modelPart.getChild("shape12a6");
        this.shape12a5 = modelPart.getChild("shape12a5");
        this.shape12a7 = modelPart.getChild("shape12a7");
        this.shape12a8 = modelPart.getChild("shape12a8");
        this.shape12a9 = modelPart.getChild("shape12a9");
        this.shape12a0 = modelPart.getChild("shape12a0");
        this.shape122a = modelPart.getChild("shape122a");
        this.shape122b = modelPart.getChild("shape122b");
        this.shape15a = modelPart.getChild("shape15a");
        this.shape15 = modelPart.getChild("shape15");
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
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(7, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offsetAndRotation(-8, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 6, 1),
                PartPose.offsetAndRotation(-7, 17, 7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 6, 1),
                PartPose.offsetAndRotation(-7, 17, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(6, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(6, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-7, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-7, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.offsetAndRotation(7, 8, -5, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.offsetAndRotation(-8, 8, -5, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-0.5F, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(0, 0, -3, -0.3926991F, 1.570796F, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(1.5F, 16, 0, 2.181662F, 0, 0));

        root.addOrReplaceChild("shape12a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(5.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-5.5F, 16, 0, 0.1745329F, 0, 0));

        root.addOrReplaceChild("shape12c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-3.5F, 16, 0, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape12d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-4.5F, 16, 0, 0.3490659F, 0, 0));

        root.addOrReplaceChild("shape12e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(1.5F, 16, 0, 1.396263F, 0, 0));

        root.addOrReplaceChild("shape12f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-0.5F, 16, 0, 1.047198F, 0, 0));

        root.addOrReplaceChild("shape12g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-2.5F, 16, 0, 0.6981317F, 0, 0));

        root.addOrReplaceChild("shape12h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(0.5F, 16, 0, 1.22173F, 0, 0));

        root.addOrReplaceChild("shape13a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 2, 2, 2),
                PartPose.offsetAndRotation(2.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape13b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-5.5F, 16, 0, 0.9599311F, 0, 0));

        root.addOrReplaceChild("shape13c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-4.5F, 16, 0, 1.134464F, 0, 0));

        root.addOrReplaceChild("shape13d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-3.5F, 16, 0, 1.308997F, 0, 0));

        root.addOrReplaceChild("shape13e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-2.5F, 16, 0, 1.48353F, 0, 0));

        root.addOrReplaceChild("shape13f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-1.5F, 16, 0, 1.658063F, 0, 0));

        root.addOrReplaceChild("shape13g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-0.5F, 16, 0, 1.832596F, 0, 0));

        root.addOrReplaceChild("shape13h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(0.5F, 16, 0, 2.007129F, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 0)
                        .addBox(0, 0, 0, 1, 9, 6),
                PartPose.offsetAndRotation(3, 14, -4, 0, 0, 0));

        root.addOrReplaceChild("shape13aa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12aa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape121",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 1, 2, 2),
                PartPose.offsetAndRotation(-1.5F, 16, 0, 0.8726646F, 0, 0));

        root.addOrReplaceChild("shape122",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-0.5F, -1, -1, 10, 2, 2),
                PartPose.offsetAndRotation(0, 0, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape123",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-0.5F, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(0, 0, -3, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape124",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-0.5F, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(0, 0, -3, 0.3926991F, 1.570796F, 0));

        root.addOrReplaceChild("shape12a1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 4, 2, 2),
                PartPose.offsetAndRotation(0, 0, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(0, 0, 0, 1.178097F, 0, 0));

        root.addOrReplaceChild("shape12a3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 2, 2, 2),
                PartPose.offsetAndRotation(2.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape12a4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(0, 0, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12a6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 3, 2, 2),
                PartPose.offsetAndRotation(5.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape12a5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(0, 0, 0, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape12a7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(5.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape12a8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(5.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape12a9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, -1, -1, 4, 2, 2),
                PartPose.ZERO);

        root.addOrReplaceChild("shape12a0",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.ZERO);

        root.addOrReplaceChild("shape122a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-0.5F, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(0, 0, -3, 0.7853982F, 1.570796F, 0));

        root.addOrReplaceChild("shape122b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(-0.5F, -1, -1, 10, 2, 2),
                PartPose.offsetAndRotation(0, 0, 0, 0.7853982F, 1.570796F, 0));

        root.addOrReplaceChild("shape15a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(67, 0)
                        .addBox(0, 0, 0, 5, 5, 1),
                PartPose.offsetAndRotation(-3, 18, -6, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(67, 0)
                        .addBox(0, 0, 0, 5, 5, 1),
                PartPose.offsetAndRotation(-3, 18, 2, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 32);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {

        double xoff;
        double yoff;

        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape8.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape9.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape10.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape11.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi*2));
        stack.translate(0, -1, 0);
        shape13.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13aa.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12aa.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape121.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(-phi*2));
        stack.translate(0, -1, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape12a7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a8.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        xoff = 0;
        yoff = 0;
        double a = 0.35;
        double b = 1.25;
        double c = -0.125;
        stack.translate(a, b, c);
        stack.mulPose(Axis.XN.rotationDegrees(phi));
//        shape12a2.setRotationPoint(0, 0, 0);
//        shape12a5.setRotationPoint(0, 0, 0);
        shape12a2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(-a, -b, -c);


        a = 0.1; b = 1.25; c = -0.125;
        stack.translate(a, b, c);
        stack.mulPose(Axis.XN.rotationDegrees(phi));
//        shape12a9.setRotationPoint(0, 0, 0);
//        shape12a4.setRotationPoint(0, 0, 0);
//        shape12a1.setRotationPoint(0, 0, 0);
//        shape12a0.setRotationPoint(0, 0, 0);
        shape12a9.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape12a0.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(-a, -b, -c);

        a = -0.03125; b = 1.25; c = 0.1875;
        stack.translate(a, b, c);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
//        shape12.setRotationPoint(0, 0, -3F);
//        shape123.setRotationPoint(0, 0, -3F);
//        shape124.setRotationPoint(0, 0, -3F);
//        shape122a.setRotationPoint(0, 0, -3F);
//        shape122b.setRotationPoint(0, 0, 0);
//        shape122.setRotationPoint(0, 0, 0);
        shape12.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape123.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape124.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape122a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape122b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape122.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(-a, -b, -c);

        shape14.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape15a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape15.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
