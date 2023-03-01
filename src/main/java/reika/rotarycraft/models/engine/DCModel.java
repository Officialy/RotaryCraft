package reika.rotarycraft.models.engine;

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

public class DCModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/engine/dc.png");

    private final ModelPart shape1;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape14;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape8;

    public DCModel(ModelPart modelPart) {
        super(RenderType::entityCutout);

        this.shape1 = modelPart.getChild("shape1");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape14 = modelPart.getChild("shape14");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8 = modelPart.getChild("shape8");
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

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 11, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 14, -1, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 11, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 10, 10, 12),
                PartPose.offsetAndRotation(-3, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 10, 2, 8),
                PartPose.offsetAndRotation(-3, 11, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(60, 17)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(-4, 14, -5, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(60, 17)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(-4, 14, 4, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 24)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-4, 13, 3, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 24)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-4, 13, -4, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 17)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(-4, 12, -3, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 22)
                        .addBox(0, 0, 0, 8, 1, 10),
                PartPose.offsetAndRotation(-2, 12, -5, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape8.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        double d = 0.9375;
        stack.translate(0, d, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -d, 0);
        shape12.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, d, 0);
        stack.mulPose(Axis.XP.rotationDegrees(-phi));
        stack.translate(0, -d, 0);
        shape14.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
