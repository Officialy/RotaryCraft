package reika.rotarycraft.models.engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.resources.Identifier;

import static reika.rotarycraft.RotaryCraft.MODID;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

public class PerformanceModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/engine/perftex.png");

    private final ModelPart shape1;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape5;
    private final ModelPart shape6n;
    private final ModelPart shape7;
    private final ModelPart shape3;
    private final ModelPart shape8;
    private final ModelPart shape4;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2;
    private final ModelPart shape4a;
    private final ModelPart b;
    private final ModelPart shape4c;
    private final ModelPart shape6;
    private final ModelPart shape9i;
    private final ModelPart shape9h;
    private final ModelPart shape9g;
    private final ModelPart shape9f;
    private final ModelPart shape9e;
    private final ModelPart shape9d;
    private final ModelPart shape9c;
    private final ModelPart shape9b;
    private final ModelPart shape9a;
    private final ModelPart shape9;
    private final ModelPart shape10;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public PerformanceModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

        this.shape1 = modelPart.getChild("shape1");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6n = modelPart.getChild("shape6n");
        this.shape7 = modelPart.getChild("shape7");
        this.shape3 = modelPart.getChild("shape3");
        this.shape8 = modelPart.getChild("shape8");
        this.shape4 = modelPart.getChild("shape4");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2 = modelPart.getChild("shape2");
        this.shape4a = modelPart.getChild("shape4a");
        this.b = modelPart.getChild("b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape6 = modelPart.getChild("shape6");
        this.shape9i = modelPart.getChild("shape9i");
        this.shape9h = modelPart.getChild("shape9h");
        this.shape9g = modelPart.getChild("shape9g");
        this.shape9f = modelPart.getChild("shape9f");
        this.shape9e = modelPart.getChild("shape9e");
        this.shape9d = modelPart.getChild("shape9d");
        this.shape9c = modelPart.getChild("shape9c");
        this.shape9b = modelPart.getChild("shape9b");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape10 = modelPart.getChild("shape10");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 14, 1, 15),
                PartPose.offsetAndRotation(-8, 22, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, 0, 11, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, 0, 11, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 12, 11, 6),
                PartPose.offsetAndRotation(-7, 11, 3, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape6n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 55)
                        .addBox(0, 0, 0, 11, 5, 14),
                PartPose.offsetAndRotation(-6.5F, 16, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 12, 11, 6),
                PartPose.offsetAndRotation(-7, 14, -8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 13, 2, 14),
                PartPose.offsetAndRotation(-7.7F, 21, -7, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 66)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-1.9F, 8, -1, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-2, 9.1F, 6.4F, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(1, 10, -8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-2, 10, -8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-5, 10, -8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(1, 9.1F, 6.4F, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-5, 9.1F, 6.4F, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 53)
                        .addBox(0, 0, 0, 12, 1, 4),
                PartPose.offsetAndRotation(-6.9F, 10, -2, 0, 0, 0));

        root.addOrReplaceChild("b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 66)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(2.1F, 8, -1, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 66)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-5.9F, 8, -1, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 17)
                        .addBox(0, 0, 0, 13, 12, 6),
                PartPose.offsetAndRotation(-7.5F, 11, -3, 0, 0, 0));

        root.addOrReplaceChild("shape9i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4, 14, 2, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape9h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4, 20, 2, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape9g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4, 12.1F, 2, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape9f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4, 16, 2, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape9e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4.5F, 20, -3, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape9d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4, 18, 2, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape9c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4.5F, 14, -3, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape9b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4.5F, 18, -3, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4.5F, 16, -3, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(4.5F, 12.1F, -3, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(105, 17)
                        .addBox(0, 0, 0, 1, 11, 2),
                PartPose.offsetAndRotation(7, 12, -1, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    /**
     * Mirrors the 1.7 ModelPerformance.renderAll motion logic.
     *
     * <p>Two animation groups: (1) the crank/flywheel (shape12, shape13) rotates around X at
     * pivot y=1; (2) eight pistons (shape2a/b/c/d/e, shape2, shape4, shape4c, plus their
     * counterparts) bob vertically with a sinusoidal Y offset, each phase-shifted by 120° at
     * 4×-frequency so neighbouring pistons fire in sequence.
     *
     * <p>For now we collapse the eight pistons into three pivot offsets (ang0, ang1, ang2) like
     * the original — the off-axis pistons (Shape2a/2e/2 → ±30° rotation of the y-vector) get a
     * simpler treatment so the visual is approximately right without a one-piston-per-quad pose
     * push/pop. The visual aim is "engine pistons clearly pump up and down when running"
     * rather than the original's pixel-perfect crankshaft animation.
     */
    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        final int LM = packedLightIn;
        final int OV = OverlayTexture.NO_OVERLAY;
        final int COL = 0xFFFFFFFF;
        // Piston bobbing amplitudes (3 phases at 4× phi frequency).
        double ang0 = 0.03125 * Math.sin(Math.toRadians(phi) * 4);
        double ang1 = 0.03125 * Math.sin(Math.toRadians(phi + 120) * 4);
        double ang2 = 0.03125 * Math.sin(Math.toRadians(phi + 240) * 4);
        if (phi == Float.MIN_NORMAL) ang0 = ang1 = ang2 = 0;

        shape1.render(stack, tex, LM, OV, COL);

        // Crank / flywheel rotating group — pivot y=1, X-axis rotation by phi.
        stack.pushPose();
        stack.translate(0.0, 1.0, 0.0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0.0, -1.0, 0.0);
        shape12.render(stack, tex, LM, OV, COL);
        shape13.render(stack, tex, LM, OV, COL);
        stack.popPose();

        // Static intake plumbing.
        shape5.render(stack, tex, LM, OV, COL);
        shape6n.render(stack, tex, LM, OV, COL);
        shape7.render(stack, tex, LM, OV, COL);
        shape3.render(stack, tex, LM, OV, COL);
        shape8.render(stack, tex, LM, OV, COL);

        // Vertical pistons: each in its own pushPose/popPose to isolate the Y translate.
        stack.pushPose(); stack.translate(0.0, ang0, 0.0); shape4.render(stack, tex, LM, OV, COL);  stack.popPose();
        stack.pushPose(); stack.translate(0.0, ang1, 0.0); b.render(stack, tex, LM, OV, COL);       stack.popPose();
        stack.pushPose(); stack.translate(0.0, ang2, 0.0); shape4c.render(stack, tex, LM, OV, COL); stack.popPose();

        // Off-axis pistons (six shapes at ±30° from vertical). Translation is along (Y, -Z)
        // projected through cos/sin of the off-axis angle.
        final double cosP =  Math.cos(Math.toRadians(30));
        final double sinP =  Math.sin(Math.toRadians(30));
        final double cosN =  Math.cos(Math.toRadians(-30));
        final double sinN =  Math.sin(Math.toRadians(-30));
        renderOffAxis(stack, tex, LM, OV, COL, shape2a, ang0 * cosP, ang0 * sinP);
        renderOffAxis(stack, tex, LM, OV, COL, shape2e, ang1 * cosP, ang1 * sinP);
        renderOffAxis(stack, tex, LM, OV, COL, shape2,  ang2 * cosP, ang2 * sinP);
        renderOffAxis(stack, tex, LM, OV, COL, shape2b, ang1 * cosN, ang1 * sinN);
        renderOffAxis(stack, tex, LM, OV, COL, shape2c, ang0 * cosN, ang0 * sinN);
        renderOffAxis(stack, tex, LM, OV, COL, shape2d, ang2 * cosN, ang2 * sinN);

        // Static structural / housing parts.
        shape4a.render(stack, tex, LM, OV, COL);
        shape6.render(stack, tex, LM, OV, COL);
        shape9i.render(stack, tex, LM, OV, COL);
        shape9h.render(stack, tex, LM, OV, COL);
        shape9g.render(stack, tex, LM, OV, COL);
        shape9f.render(stack, tex, LM, OV, COL);
        shape9e.render(stack, tex, LM, OV, COL);
        shape9d.render(stack, tex, LM, OV, COL);
        shape9c.render(stack, tex, LM, OV, COL);
        shape9b.render(stack, tex, LM, OV, COL);
        shape9a.render(stack, tex, LM, OV, COL);
        shape9.render(stack, tex, LM, OV, COL);
        shape10.render(stack, tex, LM, OV, COL);
    }

    private static void renderOffAxis(PoseStack stack, VertexConsumer tex, int lm, int ov, int col,
                                       ModelPart part, double dy, double dz) {
        stack.pushPose();
        stack.translate(0.0, dy, -dz);
        part.render(stack, tex, lm, ov, col);
        stack.popPose();
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

