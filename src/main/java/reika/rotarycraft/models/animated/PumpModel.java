package reika.rotarycraft.models.animated;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.blockentities.production.BlockEntityPump;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class PumpModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/pumptex.png");

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
    private final ModelPart shape14;
    private final ModelPart shape15;
    private final ModelPart shape16;
    private final ModelPart shape17;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public PumpModel(ModelPart modelPart) {
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
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape14 = modelPart.getChild("shape14");
        this.shape15 = modelPart.getChild("shape15");
        this.shape16 = modelPart.getChild("shape16");
        this.shape17 = modelPart.getChild("shape17");
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
                        .texOffs(36, 29)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-1, 22.5F, -1, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 10, 8, 8),
                PartPose.offsetAndRotation(-5, 15, -4, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 10, 8, 8),
                PartPose.offsetAndRotation(-5, 17.5F, -5.5F, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 10, 8, 8),
                PartPose.offsetAndRotation(-5, 20.5F, -5.5F, 1.047198F, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 17)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(5, 16, -3, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 16)
                        .addBox(0, 0, 0, 16, 9, 2),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 16)
                        .addBox(0, 0, 0, 16, 9, 2),
                PartPose.offsetAndRotation(-8, 14, 6, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 2, 9, 12),
                PartPose.offsetAndRotation(6, 14, -6, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 2, 9, 12),
                PartPose.offsetAndRotation(-8, 14, -6, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 17)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-6, 16, -3, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 14)
                        .addBox(0, 0, 0, 14, 2, 1),
                PartPose.offsetAndRotation(-7, 12, -7, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 14)
                        .addBox(0, 0, 0, 14, 2, 1),
                PartPose.offsetAndRotation(-7, 12, 6, 0, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 0)
                        .addBox(0, 0, 0, 1, 2, 12),
                PartPose.offsetAndRotation(-7, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 0)
                        .addBox(0, 0, 0, 1, 2, 12),
                PartPose.offsetAndRotation(6, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 12, 1, 12),
                PartPose.offsetAndRotation(-6, 14, -6, 0, 0, 0));

        root.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(0, 0, 0, 12, 1, 12),
                PartPose.offsetAndRotation(-6, 12, -6, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

        /**
     * Mirrors the original 1.7.10 {@code ModelPump.renderAll} animation logic.
     *
     * <p>Original code (GL11 era):
     * <pre>
     *   if (solid) {
     *     Shape1.render; Shape2.render;
     *     if (!broken) {
     *       double d = 1.1875;
     *       GL11.glTranslated(0, d, 0);
     *       GL11.glRotatef(phi, 1, 0, 0);    // rotate crank assembly around X at pivot
     *       GL11.glTranslated(0, -d, 0);
     *       Shape3.render; Shape4.render; Shape5.render;
     *       GL11.glTranslated(0, d, 0);
     *       GL11.glRotatef(-phi, 1, 0, 0);
     *       GL11.glTranslated(0, -d, 0);
     *     }
     *     Shape6–Shape15.render;
     *   }
     *   if (solid) Shape17.render;
     * </pre>
     * Shape16 was never rendered in the original (absent from both branches) — omitted here too.
     *
     * <p>The renderer passes {@code -tile.phi} (negated) as the {@code phi} argument, matching the
     * original call: {@code var14.renderAll(tile, pars, -tile.phi, 0)}.
     */
    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        // Static base parts
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        // Animated crank assembly (Shape3/4/5): rotate around X-axis at pivot y = 1.1875 blocks.
        // Same transform as original GL11 version: translate to pivot, rotate, translate back.
        boolean broken = (te instanceof BlockEntityPump p) && p.isBroken();
        if (!broken) {
            stack.pushPose();
            double d = 1.1875;
            stack.translate(0.0, d, 0.0);
            stack.mulPose(Axis.XP.rotationDegrees(phi));
            stack.translate(0.0, -d, 0.0);
            shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            stack.popPose();
        }

        // Remaining static frame parts (Shape6–15, Shape17; Shape16 not rendered per original)
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
        shape17.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

