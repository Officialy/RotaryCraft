package reika.rotarycraft.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
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

public class SpillwayModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2a;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape4b;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart root;

    public SpillwayModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
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

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 8, 13, 1),
                PartPose.offsetAndRotation(0, 10, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 16)
                        .addBox(0, 0, 0, 8, 15, 1),
                PartPose.offsetAndRotation(-8, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 36)
                        .addBox(0, 0, 0, 1, 13, 14),
                PartPose.offsetAndRotation(7, 10, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 1, 12, 14),
                PartPose.offsetAndRotation(-2.9F, 11.4F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0.3F, -0.5F, 0, 5, 1, 14),
                PartPose.offsetAndRotation(-6.5F, 8.8F, -7, 0, 0, 0.6108652F));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(0, 0, 0, 2, 1, 14),
                PartPose.offsetAndRotation(-8, 8.5F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 30)
                        .addBox(0, 0, 0, 1, 14, 14),
                PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 16)
                        .addBox(0, 0, 0, 8, 15, 1),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 53)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 8, 13, 1),
                PartPose.offsetAndRotation(0, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 53)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 9, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 64);
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
