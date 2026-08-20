package reika.rotarycraft.base.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;
import java.util.Collection;

import static reika.rotarycraft.RotaryCraft.MODID;

/**
 * Shared gearbox housing (the casing, the end plates and the four corner feet), plus the
 * support-column plumbing.
 *
 * <p>Faithful to {@code Reika.RotaryCraft.Base.ModelGearboxBase}: shapes 1-11 are the static
 * housing every gearbox ratio shares, and each ratio model adds its own gear train on top. The
 * support columns are drawn <em>last</em> and with the <em>bearing tier</em>'s texture rather than
 * the gearbox material's, which is why they are split out of the main pass.
 */
public abstract class GearboxBaseModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/transmission/gear/geartex.png");

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
    // 1.21.5: Model already declares a protected `root`; expose a wider-access alias for subclasses.
    protected final ModelPart gearboxRoot;

    private final Collection<ModelPart> base = new ArrayList<>();
    private final Collection<ModelPart> supportColumns = new ArrayList<>();

    /**
     * Translation left on the stack by {@link #renderGears} that the original never undid, and
     * which therefore also applies to the support columns. Only the 2x gearbox has one.
     */
    private float supportOffsetY;
    private float supportOffsetZ;

    public GearboxBaseModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);
        this.gearboxRoot = modelPart;

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

        base.add(shape1);
        base.add(shape2);
        base.add(shape3);
        base.add(shape4);
        base.add(shape5);
        base.add(shape6);
        base.add(shape7);
        base.add(shape8);
        base.add(shape9);
        base.add(shape10);
        base.add(shape11);
    }

    /**
     * The 11 housing parts, shared by every ratio. Each ratio's {@code createLayer} calls this
     * before declaring its own gear train, so the housing has exactly one definition.
     *
     * <p>1.7.10 note: the Techne export sets {@code mirror = true} <em>after</em> {@code addBox},
     * and {@code ModelRenderer} reads {@code mirror} at {@code addBox} time — so none of those
     * flags ever took effect. They are deliberately not reproduced with
     * {@link CubeListBuilder#mirror(boolean)}, which <em>would</em> flip the UVs.
     */
    protected static void addBaseParts(PartDefinition root) {
        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offset(-8, 23, -8));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offset(7, 11, -8));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.offset(-8, 11, -8));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offset(-7, 19, 7));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offset(-7, 19, -8));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offset(6, 16, 7));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offset(6, 16, -8));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offset(-7, 16, 7));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offset(-7, 16, -8));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.offset(7, 8, -5));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.offset(-8, 8, -5));
    }

    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        addBaseParts(definition.getRoot());
        return LayerDefinition.create(definition, 128, 32);
    }

    protected final void addSupport(ModelPart p) {
        supportColumns.add(p);
    }

    /** See {@link #supportOffsetY}; called by the ratio model whose gear train leaves an offset. */
    protected final void setSupportOffset(float y, float z) {
        supportOffsetY = y;
        supportOffsetZ = z;
    }

    protected final void render(PoseStack stack, VertexConsumer tex, int light, ModelPart... parts) {
        for (ModelPart p : parts)
            p.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    /** The static housing. */
    protected final void renderBase(PoseStack stack, VertexConsumer tex, int light) {
        for (ModelPart p : base)
            p.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    /**
     * The spinning gear train, per ratio. Implementations must leave the pose as they found it.
     */
    protected abstract void renderGears(PoseStack stack, VertexConsumer tex, int light, float phi);

    /** Housing + gear train; drawn with the gearbox material's texture. */
    public final void renderMain(PoseStack stack, VertexConsumer tex, int light, float phi) {
        this.renderBase(stack, tex, light);
        this.renderGears(stack, tex, light, phi);
    }

    /**
     * The support columns, drawn with the <em>bearing tier</em>'s texture in-world (the original
     * rebound the texture from {@code li.get(0)} before this pass).
     */
    public final void renderSupports(PoseStack stack, VertexConsumer tex, int light) {
        stack.pushPose();
        stack.translate(0, supportOffsetY, supportOffsetZ);
        for (ModelPart p : supportColumns)
            p.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        stack.popPose();
    }

    /**
     * Single-texture path (item/handbook renders), where the original passed {@code li == null} and
     * so never rebound a texture for the supports.
     */
    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int light, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        this.renderMain(stack, tex, light, phi);
        this.renderSupports(stack, tex, light);
    }
}
