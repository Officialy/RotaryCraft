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

public class FuelConverterModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/modinterface/");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape1h;
    private final ModelPart shape1i;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart p;
    private final ModelPart p1;
    private final ModelPart p5;
    private final ModelPart p3;
    private final ModelPart p2;
    private final ModelPart p8;
    private final ModelPart p7;
    private final ModelPart p6;
    private final ModelPart root;

    public FuelConverterModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape1i = modelPart.getChild("shape1i");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.p = modelPart.getChild("p");
        this.p1 = modelPart.getChild("p1");
        this.p5 = modelPart.getChild("p5");
        this.p3 = modelPart.getChild("p3");
        this.p2 = modelPart.getChild("p2");
        this.p8 = modelPart.getChild("p8");
        this.p7 = modelPart.getChild("p7");
        this.p6 = modelPart.getChild("p6");
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 17, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-0.5F, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 15, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-0.5F, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-0.5F, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 15, 0, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-0.5F, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 15, 0, 0, 1.570796F, 0));

        root.addOrReplaceChild("shape1h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-0.5F, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 15, 0, 0, -1.047198F, 0));

        root.addOrReplaceChild("shape1i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-0.5F, 0, -7, 1, 2, 14),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(96, 0)
                        .addBox(-1, 0, -1, 2, 5, 2),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(96, 0)
                        .addBox(-1, 0, -1, 2, 5, 2),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 75)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(7, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 75)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(-8, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 75)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(7, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 75)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 96)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 18, 7, 0, 0, 0));

        root.addOrReplaceChild("p1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 96)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("p5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 75)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(7, 18, -7, 0, 0, 0));

        root.addOrReplaceChild("p3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 96)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 18, -8, 0, 0, 0));

        root.addOrReplaceChild("p2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 96)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("p8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 75)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(7, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("p7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 75)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("p6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 75)
                        .addBox(0, 0, 0, 1, 5, 14),
                PartPose.offsetAndRotation(-8, 18, -7, 0, 0, 0));

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
