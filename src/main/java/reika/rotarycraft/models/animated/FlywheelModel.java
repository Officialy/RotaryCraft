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

public class FlywheelModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/engine/steamtex.png");

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
    private final ModelPart shape13;
    private final ModelPart shape15;
    private final ModelPart shape16;
    private final ModelPart shape17;
    private final ModelPart shape18;
    private final ModelPart shape12;
    private final ModelPart shape14;
    private final ModelPart shape19;
    private final ModelPart shape20;
    private final ModelPart shape21;
    private final ModelPart shape22;
    private final ModelPart shape23;
    private final ModelPart shape24;
    private final ModelPart shape25;
    private final ModelPart shape26;
    private final ModelPart shape27;
    private final ModelPart shape28;
    private final ModelPart shape29;
    private final ModelPart shape30;
    private final ModelPart shape31;
    private final ModelPart shape32;
    private final ModelPart shape33;
    private final ModelPart shape34;
    private final ModelPart shape35;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public FlywheelModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

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
        this.shape13 = modelPart.getChild("shape13");
        this.shape15 = modelPart.getChild("shape15");
        this.shape16 = modelPart.getChild("shape16");
        this.shape17 = modelPart.getChild("shape17");
        this.shape18 = modelPart.getChild("shape18");
        this.shape12 = modelPart.getChild("shape12");
        this.shape14 = modelPart.getChild("shape14");
        this.shape19 = modelPart.getChild("shape19");
        this.shape20 = modelPart.getChild("shape20");
        this.shape21 = modelPart.getChild("shape21");
        this.shape22 = modelPart.getChild("shape22");
        this.shape23 = modelPart.getChild("shape23");
        this.shape24 = modelPart.getChild("shape24");
        this.shape25 = modelPart.getChild("shape25");
        this.shape26 = modelPart.getChild("shape26");
        this.shape27 = modelPart.getChild("shape27");
        this.shape28 = modelPart.getChild("shape28");
        this.shape29 = modelPart.getChild("shape29");
        this.shape30 = modelPart.getChild("shape30");
        this.shape31 = modelPart.getChild("shape31");
        this.shape32 = modelPart.getChild("shape32");
        this.shape33 = modelPart.getChild("shape33");
        this.shape34 = modelPart.getChild("shape34");
        this.shape35 = modelPart.getChild("shape35");
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

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 1, 10, 10),
                PartPose.offsetAndRotation(1, 13.5F, -6.5F, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 1, 10, 10),
                PartPose.offsetAndRotation(1, 16, -7, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 1, 10, 10),
                PartPose.offsetAndRotation(1, 11, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 1, 10, 10),
                PartPose.offsetAndRotation(1, 9.5F, -2.8F, -0.3926991F, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 2, 3, 10),
                PartPose.offsetAndRotation(-1, 18, -5, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 10)
                        .addBox(0, 0, 0, 1, 6, 2),
                PartPose.offsetAndRotation(-2, 13, -5, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 1, 2, 10),
                PartPose.offsetAndRotation(-2, 19, -5, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 17)
                        .addBox(0, 0, 0, 2, 4, 3),
                PartPose.offsetAndRotation(-1, 14, -5, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 2, 3, 10),
                PartPose.offsetAndRotation(-1, 11, -5, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 17)
                        .addBox(0, 0, 0, 2, 4, 3),
                PartPose.offsetAndRotation(-1, 14, 2, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(0, 0, 0, 1, 2, 10),
                PartPose.offsetAndRotation(-2, 11, -5, 0, 0, 0));

        root.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 3)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-3, 11, -4, 0, 0, 0));

        root.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 10)
                        .addBox(0, 0, 0, 1, 6, 2),
                PartPose.offsetAndRotation(-2, 13, 3, 0, 0, 0));

        root.addOrReplaceChild("shape18",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 0)
                        .addBox(0, 0, 0, 4, 4, 1),
                PartPose.offsetAndRotation(-3, 14, -6, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 5)
                        .addBox(0, 0, 0, 4, 1, 4),
                PartPose.offsetAndRotation(-3, 21, -2, 0, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 3)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-3, 20, -4, 0, 0, 0));

        root.addOrReplaceChild("shape19",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 5)
                        .addBox(0, 0, 0, 4, 1, 4),
                PartPose.offsetAndRotation(-3, 10, -2, 0, 0, 0));

        root.addOrReplaceChild("shape20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-3, 18, -5, 0, 0, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 3)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-3, 20, 2, 0, 0, 0));

        root.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 3)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-3, 11, 2, 0, 0, 0));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-3, 12, -5, 0, 0, 0));

        root.addOrReplaceChild("shape24",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 0)
                        .addBox(0, 0, 0, 4, 4, 1),
                PartPose.offsetAndRotation(-3, 14, 5, 0, 0, 0));

        root.addOrReplaceChild("shape25",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-3, 12, 4, 0, 0, 0));

        root.addOrReplaceChild("shape26",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(114, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-3, 18, 4, 0, 0, 0));

        root.addOrReplaceChild("shape27",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-1, 17, 1, 0, 0, 0));

        root.addOrReplaceChild("shape28",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-1, 17, -2, 0, 0, 0));

        root.addOrReplaceChild("shape29",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-1, 14, 1, 0, 0, 0));

        root.addOrReplaceChild("shape30",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-1, 14, -2, 0, 0, 0));

        root.addOrReplaceChild("shape31",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
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
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, 7, 0, 0, 0));

        root.addOrReplaceChild("shape35",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    /**
     * Mirrors the original 1.7.10 ModelFlywheel.renderAll animation.
     *
     * <p>Original: ALL parts 1–30 rotate around X-axis at pivot y=1 block. Parts 31–35 are static
     * structural walls. The original also checked a {@code failed} boolean (li.get(0)) to skip
     * parts 3–30 when the bearing had failed; that state is not yet surfaced in the port so we
     * always render the full flywheel.
     */
    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        // Rotating assembly: pivot at y = 1 block, rotate around X-axis by phi
        stack.pushPose();
        stack.translate(0.0, 1.0, 0.0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0.0, -1.0, 0.0);
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape8.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape9.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape10.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape11.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape12.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape13.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape14.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape15.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape16.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape17.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape18.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape19.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape20.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape21.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape22.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape23.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape24.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape25.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape26.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape27.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape28.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape29.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape30.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.popPose();

        // Static structural walls: shapes 31–35 (not part of the rotating flywheel disc)
        shape31.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape32.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape33.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape34.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape35.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

