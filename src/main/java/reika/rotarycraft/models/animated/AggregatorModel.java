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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

public class AggregatorModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape2a;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart root;

    public AggregatorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 0)
                        .addBox(-1, 0, -1, 2, 16, 2),
                PartPose.offsetAndRotation(0, 8.5F, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, -2.356194F, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 2.356194F, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 3.141593F, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(1, 0, -0.5F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, -1.570796F, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, -1.570796F, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, -2.356194F, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 3.141593F, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(2.5F, 0, -3.2F, 3, 12, 1),
                PartPose.offsetAndRotation(0, 10, 0, 0, 2.356194F, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 0)
                        .addBox(-1, 0, -1, 2, 16, 2),
                PartPose.offsetAndRotation(0, 8.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(59, 19)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(59, 0)
                        .addBox(-1, 0, -8, 2, 1, 16),
                PartPose.offsetAndRotation(6, 8, 0, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 16, 14, 1),
                PartPose.offsetAndRotation(-8, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 16, 14, 1),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(0, 0, 0, 1, 14, 14),
                PartPose.offsetAndRotation(7, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(0, 0, 0, 1, 14, 14),
                PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(59, 91)
                        .addBox(-1, 0, -8, 2, 1, 16),
                PartPose.offsetAndRotation(-6, 8, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(59, 37)
                        .addBox(-1, 0, -8, 2, 1, 16),
                PartPose.offsetAndRotation(3, 8, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(59, 109)
                        .addBox(-7.5F, 0, -1, 15, 1, 2),
                PartPose.offsetAndRotation(0, 8.4F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(59, 73)
                        .addBox(-1, 0, -8, 2, 1, 16),
                PartPose.offsetAndRotation(-3, 8, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(59, 55)
                        .addBox(-1, 0, -8, 2, 1, 16),
                PartPose.offsetAndRotation(0, 8, 0, 0, 0, 0));

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
