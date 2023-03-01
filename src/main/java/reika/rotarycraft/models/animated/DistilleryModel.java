package reika.rotarycraft.models.animated;
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
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;

import static reika.rotarycraft.RotaryCraft.MODID;

public class DistilleryModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1a;
    private final ModelPart shape1;
    private final ModelPart shape1b;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart root;

    public DistilleryModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1a = modelPart.getChild("shape1a");
        this.shape1 = modelPart.getChild("shape1");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 16, 4, 16),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 16, 5, 1),
                PartPose.offsetAndRotation(-8, 18, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 16, 5, 1),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 16, 5, 1),
                PartPose.offsetAndRotation(-8, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 16, 5, 1),
                PartPose.offsetAndRotation(-8, 18, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(-8, 18, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(7, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(7, 18, -7, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

        @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        root.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
