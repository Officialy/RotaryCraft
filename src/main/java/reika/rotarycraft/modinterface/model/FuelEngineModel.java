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

public class FuelEngineModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/modinterface/");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape5;
    private final ModelPart shape5b1;
    private final ModelPart shape5c;
    private final ModelPart shape5d2;
    private final ModelPart shape5e1;
    private final ModelPart shape5f3;
    private final ModelPart shape5g2;
    private final ModelPart shape5h3;
    private final ModelPart shape6k;
    private final ModelPart shape6j;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape7;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape10;
    private final ModelPart shape10a;
    private final ModelPart shape10b;
    private final ModelPart shape10c;
    private final ModelPart shape11;
    private final ModelPart shape10d;
    private final ModelPart shape10e;
    private final ModelPart shape10f;
    private final ModelPart shape10g;
    private final ModelPart shape5i;
    private final ModelPart shape5j;
    private final ModelPart root;

    public FuelEngineModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5b1 = modelPart.getChild("shape5b1");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d2 = modelPart.getChild("shape5d2");
        this.shape5e1 = modelPart.getChild("shape5e1");
        this.shape5f3 = modelPart.getChild("shape5f3");
        this.shape5g2 = modelPart.getChild("shape5g2");
        this.shape5h3 = modelPart.getChild("shape5h3");
        this.shape6k = modelPart.getChild("shape6k");
        this.shape6j = modelPart.getChild("shape6j");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape10 = modelPart.getChild("shape10");
        this.shape10a = modelPart.getChild("shape10a");
        this.shape10b = modelPart.getChild("shape10b");
        this.shape10c = modelPart.getChild("shape10c");
        this.shape11 = modelPart.getChild("shape11");
        this.shape10d = modelPart.getChild("shape10d");
        this.shape10e = modelPart.getChild("shape10e");
        this.shape10f = modelPart.getChild("shape10f");
        this.shape10g = modelPart.getChild("shape10g");
        this.shape5i = modelPart.getChild("shape5i");
        this.shape5j = modelPart.getChild("shape5j");
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 26)
                        .addBox(0, 0, -2, 2, 4, 2),
                PartPose.offsetAndRotation(6, 12, -7, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 2, 11, 2),
                PartPose.offsetAndRotation(6, 12, -7, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 2, 11, 2),
                PartPose.offsetAndRotation(-8, 12, -7, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 26)
                        .addBox(0, 0, -2, 2, 4, 2),
                PartPose.offsetAndRotation(-8, 12, -7, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 6, 10, 14),
                PartPose.offsetAndRotation(-3, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(41, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 66)
                        .addBox(-3.5F, 0, 0, 7, 8, 13),
                PartPose.offsetAndRotation(4.9F, 15.4F, -5.5F, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-3.5F, -8, 0, 7, 8, 13),
                PartPose.offsetAndRotation(0, 20, -5.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2, -2, 0, 4, 11, 14),
                PartPose.offsetAndRotation(0, 14, -6.4F, 0, 0, 0));

        root.addOrReplaceChild("shape5b1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 37)
                        .addBox(-1, -1, 0, 2, 2, 1),
                PartPose.offsetAndRotation(0, 14, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 110)
                        .addBox(-2, -2, 0, 4, 4, 14),
                PartPose.offsetAndRotation(0, 14, -6.5F, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape5d2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(46, 31)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 2),
                PartPose.offsetAndRotation(0, 17.2F, -7.5F, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape5e1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 37)
                        .addBox(-1, -1, 0, 2, 2, 1),
                PartPose.offsetAndRotation(0, 14, -7.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5f3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 19)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 2),
                PartPose.offsetAndRotation(0, 20.8F, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5g2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(46, 31)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 2),
                PartPose.offsetAndRotation(0, 17.2F, -7.5F, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape5h3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 19)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 2),
                PartPose.offsetAndRotation(0, 20.8F, -7.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape6k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(107, 0)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 15, 6.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape6j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(107, 0)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 15, 6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-3.5F, -8, 0, 7, 8, 13),
                PartPose.offsetAndRotation(0, 20, -5.5F, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 66)
                        .addBox(-3.5F, 0, 0, 7, 8, 13),
                PartPose.offsetAndRotation(-4.9F, 15.4F, -5.5F, 0, 0, -0.3926991F));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(41, 19)
                        .addBox(0, 0, 0, 12, 3, 1),
                PartPose.offsetAndRotation(-6, 16, -6.25F, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(46, 24)
                        .addBox(0, 0, 0, 10, 4, 2),
                PartPose.offsetAndRotation(-5, 17.5F, -6, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 26)
                        .addBox(-2, 0, 0, 4, 1, 9),
                PartPose.offsetAndRotation(-6, 14, -5, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 26)
                        .addBox(-2, 0, 0, 4, 1, 9),
                PartPose.offsetAndRotation(6, 14, -5, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(6.5F, 13.5F, 2, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-6.5F, 13.5F, 2, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape10b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(6.5F, 13.5F, -2, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(6.5F, 13.5F, 0, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 38)
                        .addBox(0, 0, 0, 14, 2, 2),
                PartPose.offsetAndRotation(-7, 14, 5, 0, 0, 0));

        root.addOrReplaceChild("shape10d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(6.5F, 13.5F, -4, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-6.5F, 13.5F, -4, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape10f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-6.5F, 13.5F, -2, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape10g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(-1, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-6.5F, 13.5F, 0, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape5i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 110)
                        .addBox(-2, -2, 0, 4, 4, 14),
                PartPose.offsetAndRotation(0, 14, -6.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 110)
                        .addBox(-2, -2, 0, 4, 4, 14),
                PartPose.offsetAndRotation(0, 14, -6.5F, 0, 0, 0.3926991F));

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
