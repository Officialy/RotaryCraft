package reika.rotarycraft.models;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import net.minecraft.client.renderer.texture.OverlayTexture;
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

public class FractionModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape5d;
    private final ModelPart shape6e;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape6f;
    private final ModelPart shape5g;
    private final ModelPart shape6g;
    private final ModelPart shape7;
    private final ModelPart root;

    public FractionModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape6f = modelPart.getChild("shape6f");
        this.shape5g = modelPart.getChild("shape5g");
        this.shape6g = modelPart.getChild("shape6g");
        this.shape7 = modelPart.getChild("shape7");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 14)
                        .addBox(0, 0, 0, 8, 14, 12),
                PartPose.offsetAndRotation(-4, 9, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 12, 14, 8),
                PartPose.offsetAndRotation(-6, 9, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 3, 14, 3),
                PartPose.offsetAndRotation(1.7F, 9, 3.8F, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 3, 14, 3),
                PartPose.offsetAndRotation(1.7F, 9, -3.8F, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 3, 14, 3),
                PartPose.offsetAndRotation(-5.9F, 9, 3.8F, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 3, 14, 3),
                PartPose.offsetAndRotation(-5.9F, 9, -3.8F, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 13, 1, 13),
                PartPose.offsetAndRotation(-6.5F, 8, -6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 76)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 76)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(2, 61)
                        .addBox(0, 0, 0, 2, 2, 12),
                PartPose.offsetAndRotation(6, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(2, 61)
                        .addBox(0, 0, 0, 2, 2, 12),
                PartPose.offsetAndRotation(-8, 18, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 46)
                        .addBox(0, 0, 0, 2, 1, 12),
                PartPose.offsetAndRotation(6, 22, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 41)
                        .addBox(0, 0, 0, 16, 1, 2),
                PartPose.offsetAndRotation(-8, 22, 6, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 41)
                        .addBox(0, 0, 0, 16, 1, 2),
                PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(2, 61)
                        .addBox(0, 0, 0, 2, 2, 12),
                PartPose.offsetAndRotation(-8, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 46)
                        .addBox(0, 0, 0, 2, 1, 12),
                PartPose.offsetAndRotation(-8, 22, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 76)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 18, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(2, 61)
                        .addBox(0, 0, 1, 2, 2, 12),
                PartPose.offsetAndRotation(6, 18, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 76)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 18, 6, 0, 0, 0));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 76)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 14, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(2, 61)
                        .addBox(0, 0, 0, 2, 2, 12),
                PartPose.offsetAndRotation(6, 14, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 76)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(2, 61)
                        .addBox(0, 0, 0, 2, 2, 12),
                PartPose.offsetAndRotation(-8, 14, -6, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

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
