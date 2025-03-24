package reika.rotarycraft.models.animated.shaftonly;

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
import org.joml.Vector3f;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class ShaftVModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/transmission/shaft/");

    private final ModelPart shape15b;
    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1g;
    private final ModelPart shape1f;
    private final ModelPart shape1e;
    private final ModelPart shape2;
    private final ModelPart shape2b;
    private final ModelPart shape15ba;
    private final ModelPart root;

    public ShaftVModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape15b = modelPart.getChild("shape15b");
        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape15ba = modelPart.getChild("shape15ba");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape15b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-6, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(5, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-6, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(5, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(120, 0)
                        .addBox(0, 0, 0, 2, 17, 2),
                PartPose.offsetAndRotation(-1.4F, 7.5F, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(120, 0)
                        .addBox(0, 0, 0, 2, 17, 2),
                PartPose.offsetAndRotation(-1, 7.5F, -1, 0, 0, 0));

        root.addOrReplaceChild("shape15ba",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 8, -7, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 32);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        boolean fail = (Boolean) conditions.get(0);
        this.renderMount(stack, tex, packedLightIn);
        if (!fail)
            this.renderShaft(stack, tex, packedLightIn, phi);
    }

    public void renderMount(PoseStack stack, VertexConsumer tex, int packedLightIn) {
        shape15b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape1e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape15ba.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    public void renderShaft(PoseStack stack, VertexConsumer tex, int packedLightIn, float phi) {
        stack.mulPose(Axis.YP.rotationDegrees(phi));
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.mulPose(Axis.YN.rotationDegrees(phi));
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
