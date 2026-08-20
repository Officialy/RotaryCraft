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
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class WinderModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/windertex.png");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape6f;
    private final ModelPart shape6g;
    private final ModelPart shape6h;
    private final ModelPart shape6i;
    private final ModelPart shape6j;
    private final ModelPart shape6k;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public WinderModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape6f = modelPart.getChild("shape6f");
        this.shape6g = modelPart.getChild("shape6g");
        this.shape6h = modelPart.getChild("shape6h");
        this.shape6i = modelPart.getChild("shape6i");
        this.shape6j = modelPart.getChild("shape6j");
        this.shape6k = modelPart.getChild("shape6k");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .texOffs(48, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .texOffs(0, 37)
                        .addBox(0, 0, 0, 12, 1, 7),
                PartPose.offsetAndRotation(-6, 11, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .texOffs(48, 18)
                        .addBox(0, 0, 0, 12, 1, 12),
                PartPose.offsetAndRotation(-6, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 1, 10, 7),
                PartPose.offsetAndRotation(5, 12, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 1, 10, 7),
                PartPose.offsetAndRotation(-6, 12, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 10, 10, 5),
                PartPose.offsetAndRotation(-5, 12, 2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .texOffs(0, 64)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(0, 17, 0.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .texOffs(0, 64)
                        .addBox(-1, -1, 0, 2, 2, 8),
                PartPose.offsetAndRotation(0, 17, 0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -2, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-2.9F, 18.2F, 1, 0, 0, 2.303835F));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(1.2F, 20, 1, 0, 0, 0.8726646F));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(2.5F, 16.5F, 1, 0, 0, -0.6806784F));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -4, 0, 1, 4, 1),
                PartPose.offsetAndRotation(-2.2F, 14.4F, 1, 0, 0, -3.01942F));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 1, 1, 1),
                PartPose.offsetAndRotation(1, 17, 1, 0, 0, 0));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0.7F, 14.2F, 1, 0, 0, -1.692969F));

        root.addOrReplaceChild("shape6f",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -2, 0, 1, 2, 1),
                PartPose.offsetAndRotation(1, 16, 1, 0, 0, -1.22173F));

        root.addOrReplaceChild("shape6g",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-0.8F, 15.2F, 1, 0, 0, -2.86234F));

        root.addOrReplaceChild("shape6h",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-1.6F, 19.4F, 1, 0, 0, 1.745329F));

        root.addOrReplaceChild("shape6i",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-1.7F, 18, 1, 0, 0, 1.867502F));

        root.addOrReplaceChild("shape6j",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(3.4F, 18.2F, 1, 0, 0, -0.0698132F));

        root.addOrReplaceChild("shape6k",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, -3, 0, 1, 3, 1),
                PartPose.offsetAndRotation(1, 19, 1, 0, 0, 0.5061455F));

        return LayerDefinition.create(definition, 128, 128);
    }

        /**
     * Faithful to {@code ModelWinder.renderAll}: frame, then the spool turning on Z, then - only
     * when a spring is loaded - the coil.
     *
     * <p>The coil is drawn mirrored: 1.7.10 paired its {@code glScaled(-1, 1, 1)} with
     * {@code glFrontFace(GL_CW)} to keep the reversed winding facing outwards. 26.2 has no
     * per-draw front-face switch, so {@link reika.rotarycraft.renders.dmi.RenderWinder} routes the
     * model through the no-cull entityCutout pipeline instead.
     */
    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int light, BlockEntity te,
                          ArrayList<?> conditions, float phi, float theta) {
        shape1.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape4.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        stack.pushPose();
        stack.translate(0, 1.0625, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(0, -1.0625, 0);
        shape5.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape5a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.popPose();

        if (hasSpring(conditions))
            this.renderSpring(stack, tex, light);
    }

    /** True when the caller passed a loaded spring, as {@code li.get(0)} did in 1.7.10. */
    public static boolean hasSpring(ArrayList<?> conditions) {
        return conditions != null && !conditions.isEmpty() && Boolean.TRUE.equals(conditions.get(0));
    }

    /** The mirrored coil - see {@link #renderAll} for why it needs a no-cull pipeline. */
    public void renderSpring(PoseStack stack, VertexConsumer tex, int light) {
        stack.pushPose();
        stack.scale(-1, 1, 1);
        for (ModelPart p : new ModelPart[]{shape6, shape6a, shape6b, shape6c, shape6d, shape6e,
                shape6f, shape6g, shape6h, shape6i, shape6j, shape6k})
            p.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.popPose();
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

