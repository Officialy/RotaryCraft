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

public class DefoliatorModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape4e;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape4d;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6c;
    private final ModelPart root;

    public DefoliatorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape4e = modelPart.getChild("shape4e");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6c = modelPart.getChild("shape6c");
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

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 1.308997F, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(-3, 0, -3, 6, 1, 6),
                PartPose.offsetAndRotation(0, 17, 0, 0, 1.308997F, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 16, 0, 0, 1.308997F, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-5, 0, -5, 10, 3, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 1.308997F, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-5, 0, -5, 10, 3, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-5, 0, -5, 10, 3, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0.2617994F, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-5, 0, -5, 10, 3, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-5, 0, -5, 10, 3, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-5, 0, -5, 10, 3, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0.2617994F, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape4e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(-3, 0, -3, 6, 1, 6),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(-3, 0, -3, 6, 1, 6),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0.2617994F, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(-3, 0, -3, 6, 1, 6),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(-3, 0, -3, 6, 1, 6),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(-3, 0, -3, 6, 1, 6),
                PartPose.offsetAndRotation(0, 17, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0.2617994F, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 16, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(1, -7, -1, 2, 4, 2),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(1, -8, -1, 2, 4, 2),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 7)
                        .addBox(0.5F, -6, -0.5F, 1, 3, 1),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

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
