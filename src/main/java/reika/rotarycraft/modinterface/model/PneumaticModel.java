package reika.rotarycraft.modinterface.model;

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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class PneumaticModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/converter/pneutex.png");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape5;
    private final ModelPart shape4c;
    private final ModelPart shape4d;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape9;
    private final ModelPart shape8b;
    private final ModelPart shape8c;
    private final ModelPart shape9a;
    private final ModelPart shape8d;
    private final ModelPart shape6e;
    private final ModelPart shape8e;
    private final ModelPart root;

    public PneumaticModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape5 = modelPart.getChild("shape5");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape8b = modelPart.getChild("shape8b");
        this.shape8c = modelPart.getChild("shape8c");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape8d = modelPart.getChild("shape8d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape8e = modelPart.getChild("shape8e");
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(87, 8)
                        .addBox(0, -1, -1, 14, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(87, 8)
                        .addBox(0, -1, -1, 14, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 63)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 15, 1, 14),
                PartPose.offsetAndRotation(-7, 22, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(-5, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(-3, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(-1, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 25)
                        .addBox(0, 0, 0, 11, 9, 10),
                PartPose.offsetAndRotation(-6, 13, -5, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(1, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(3, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(8, 0)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(3, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 12)
                        .addBox(0, 0, 0, 1, 1, 7),
                PartPose.offsetAndRotation(-5, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 82)
                        .addBox(0, 0, 0, 1, 1, 9),
                PartPose.offsetAndRotation(-3, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(0, 12, 3, 0, 0, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 106)
                        .addBox(0, 0, 0, 1, 1, 7),
                PartPose.offsetAndRotation(1, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 0)
                        .addBox(0, 0, 0, 1, 11, 12),
                PartPose.offsetAndRotation(7, 11, -6, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(58, 5)
                        .addBox(0, 0, 0, 2, 10, 11),
                PartPose.offsetAndRotation(5, 12, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(46, 25)
                        .addBox(0, 0, 0, 1, 6, 1),
                PartPose.offsetAndRotation(-3, 16, 5, 0, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(3, 20, 5, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-2, 16, 5, 0, 0, 0));

        root.addOrReplaceChild("shape8b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 82)
                        .addBox(0, 0, 0, 1, 8, 1),
                PartPose.offsetAndRotation(-5, 14, 5, 0, 0, 0));

        root.addOrReplaceChild("shape8c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 0)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(2, 15, 5, 0, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 6)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(0, 18, 5, 0, 0, 0));

        root.addOrReplaceChild("shape8d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(17, 6)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(-1, 18, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 94)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(-1, 12, -6, 0, 0, 0));

        root.addOrReplaceChild("shape8e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 6)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 13, 5, 0, 0, 0));

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
