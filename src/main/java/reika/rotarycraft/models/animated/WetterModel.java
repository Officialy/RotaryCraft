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

public class WetterModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart root;

    public WetterModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 13, 1),
                PartPose.offsetAndRotation(-8, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 13, 1),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 13, 1),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 13, 1),
                PartPose.offsetAndRotation(7, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 19)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-8, 22, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 19)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 19)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(7, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 19)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(7, 22, 7, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 1, 12, 14),
                PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(97, 28)
                        .addBox(0, 0, 0, 14, 12, 1),
                PartPose.offsetAndRotation(-7, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 28)
                        .addBox(0, 0, 0, 14, 12, 1),
                PartPose.offsetAndRotation(-7, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(97, 0)
                        .addBox(0, 0, 0, 1, 12, 14),
                PartPose.offsetAndRotation(7, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(-1, 0, -1, 2, 9, 2),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(-1, 0, -1, 2, 9, 2),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(-0.5F, 0, -4, 1, 4, 8),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(-0.5F, 0, -4, 1, 4, 8),
                PartPose.offsetAndRotation(0, 17, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(-0.5F, 0, -4, 1, 4, 8),
                PartPose.offsetAndRotation(0, 17, 0, 0, -1.047198F, 0));

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
