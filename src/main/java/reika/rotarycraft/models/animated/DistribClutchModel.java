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

public class DistribClutchModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/transmission/distribclutchtex.png");

    private final ModelPart shape1a;
    private final ModelPart shape2a;
    private final ModelPart shape2c;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4_r;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5_rb;
    private final ModelPart shape5_r_r;
    private final ModelPart shape5_r_r_r;
    private final ModelPart shape4_r_r_r;
    private final ModelPart shape4_r_r;
    private final ModelPart shape51_r;
    private final ModelPart shape51_r_r;
    private final ModelPart shape51_r_r_r;
    private final ModelPart shape5_r1;
    private final ModelPart shape41_r;
    private final ModelPart shape41_r_r;
    private final ModelPart shape41_r_r_r;
    private final ModelPart shape4_r1;
    private final ModelPart shape7a;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape1a2;
    private final ModelPart shape1a4;
    private final ModelPart shape1a3;
    private final ModelPart shape5_r;
    private final ModelPart shape5_rb2;
    private final ModelPart shapez2;
    private final ModelPart shapez;
    private final ModelPart shapez1;
    private final ModelPart shapebase;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public DistribClutchModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

        this.shape1a = modelPart.getChild("shape1a");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4_r = modelPart.getChild("shape4_r");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5_rb = modelPart.getChild("shape5_rb");
        this.shape5_r_r = modelPart.getChild("shape5_r_r");
        this.shape5_r_r_r = modelPart.getChild("shape5_r_r_r");
        this.shape4_r_r_r = modelPart.getChild("shape4_r_r_r");
        this.shape4_r_r = modelPart.getChild("shape4_r_r");
        this.shape51_r = modelPart.getChild("shape51_r");
        this.shape51_r_r = modelPart.getChild("shape51_r_r");
        this.shape51_r_r_r = modelPart.getChild("shape51_r_r_r");
        this.shape5_r1 = modelPart.getChild("shape5_r1");
        this.shape41_r = modelPart.getChild("shape41_r");
        this.shape41_r_r = modelPart.getChild("shape41_r_r");
        this.shape41_r_r_r = modelPart.getChild("shape41_r_r_r");
        this.shape4_r1 = modelPart.getChild("shape4_r1");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape1a2 = modelPart.getChild("shape1a2");
        this.shape1a4 = modelPart.getChild("shape1a4");
        this.shape1a3 = modelPart.getChild("shape1a3");
        this.shape5_r = modelPart.getChild("shape5_r");
        this.shape5_rb2 = modelPart.getChild("shape5_rb2");
        this.shapez2 = modelPart.getChild("shapez2");
        this.shapez = modelPart.getChild("shapez");
        this.shapez1 = modelPart.getChild("shapez1");
        this.shapebase = modelPart.getChild("shapebase");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 23, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-8, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(7, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4_r",
                CubeListBuilder.create()
                        .texOffs(60, 40)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(3.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_rb",
                CubeListBuilder.create()
                        .texOffs(0, 87)
                        .addBox(0, -0.5F, -0.5F, 7, 1, 1),
                PartPose.offsetAndRotation(-3.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5_r_r",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(3, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_r_r_r",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(3.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4_r_r_r",
                CubeListBuilder.create()
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4_r_r",
                CubeListBuilder.create()
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, 2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape51_r",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape51_r_r",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(3, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape51_r_r_r",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(-4, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5_r1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(-4, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape41_r",
                CubeListBuilder.create()
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 16, 2.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape41_r_r",
                CubeListBuilder.create()
                        .texOffs(60, 40)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape41_r_r_r",
                CubeListBuilder.create()
                        .texOffs(40, 40)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape4_r1",
                CubeListBuilder.create()
                        .texOffs(40, 40)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .texOffs(6, 42)
                        .addBox(0, 0, 0, 1, 8, 14),
                PartPose.offsetAndRotation(7, 13, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 8, 14),
                PartPose.offsetAndRotation(-8, 13, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .texOffs(32, 28)
                        .addBox(0, 0, 0, 14, 8, 1),
                PartPose.offsetAndRotation(-7, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .texOffs(32, 14)
                        .addBox(0, 0, 0, 14, 8, 1),
                PartPose.offsetAndRotation(-7, 13, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a2",
                CubeListBuilder.create()
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(-8, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a4",
                CubeListBuilder.create()
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(7, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a3",
                CubeListBuilder.create()
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(7, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5_r",
                CubeListBuilder.create()
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_rb2",
                CubeListBuilder.create()
                        .texOffs(0, 87)
                        .addBox(0, -0.5F, -0.5F, 7, 1, 1),
                PartPose.offsetAndRotation(-3.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shapez2",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(0, 0, -1.5F, 1, 10, 3),
                PartPose.offsetAndRotation(5, 14, 0, 0, 0, 0));

        root.addOrReplaceChild("shapez",
                CubeListBuilder.create()
                        .texOffs(15, 0)
                        .addBox(-0.5F, 0, -1, 1, 10, 2),
                PartPose.offsetAndRotation(0, 14, 0, 0, 0, 0));

        root.addOrReplaceChild("shapez1",
                CubeListBuilder.create()
                        .texOffs(22, 0)
                        .addBox(0, 0, -1.5F, 1, 10, 3),
                PartPose.offsetAndRotation(-6, 14, 0, 0, 0, 0));

        root.addOrReplaceChild("shapebase",
                CubeListBuilder.create()
                        .texOffs(38, 49)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 23, -7, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

        @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int light, BlockEntity te,
                          ArrayList<?> conditions, float phi, float theta) {
        shape1a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape1a2.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape1a4.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape1a3.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        shape2a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2c.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3b.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3c.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        shape7a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape7c.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape7d.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape7e.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shapebase.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        shape4_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4_r_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape41_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape41_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape41_r_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4_r1.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(-phi));
        stack.translate(0, -1, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(-phi));
        stack.translate(0, -1, 0);

        shape5_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape51_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5_rb.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5_rb2.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape51_r_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5_r1.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape51_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5_r_r_r.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.YP.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.YP.rotationDegrees(-phi));
        stack.translate(0, -1, 0);

        shapez2.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shapez.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shapez1.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

