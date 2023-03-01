package reika.rotarycraft.models.engine;

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

public class MicroTurbineModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/engine/microtex.png");

    private final ModelPart shape2;
    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape1h;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape5g;
    private final ModelPart shape5h;
    private final ModelPart shape5i;
    private final ModelPart shape5j;
    private final ModelPart shape5k;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape7;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape6f;
    private final ModelPart shape7a;
    private final ModelPart shape6g;
    private final ModelPart shape6h;
    private final ModelPart shape3c;
    private final ModelPart shape8;
    private final ModelPart shape9a;
    private final ModelPart shape9b;
    private final ModelPart shape9c;
    private final ModelPart shape9d;
    private final ModelPart shape10;
    private final ModelPart shape10a;
    private final ModelPart shape10b;
    private final ModelPart shape10c;
    private final ModelPart shape10d;
    private final ModelPart shape11;
    private final ModelPart shape9e;
    private final ModelPart shape9f;
    private final ModelPart shape9g;
    private final ModelPart shape9h;
    private final ModelPart shape9h1;
    private final ModelPart shape9h2;
    private final ModelPart shape8a4;
    private final ModelPart shape8a1b;
    private final ModelPart shape8a1;
    private final ModelPart shape8a3;
    private final ModelPart shape8a2;
    private final ModelPart shape8a1b1;
    private final ModelPart shape8a1b2;
    private final ModelPart shape8a1b3;
    private final ModelPart root;

    public MicroTurbineModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape2 = modelPart.getChild("shape2");
        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape5g = modelPart.getChild("shape5g");
        this.shape5h = modelPart.getChild("shape5h");
        this.shape5i = modelPart.getChild("shape5i");
        this.shape5j = modelPart.getChild("shape5j");
        this.shape5k = modelPart.getChild("shape5k");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape7 = modelPart.getChild("shape7");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape6f = modelPart.getChild("shape6f");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape6g = modelPart.getChild("shape6g");
        this.shape6h = modelPart.getChild("shape6h");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape9b = modelPart.getChild("shape9b");
        this.shape9c = modelPart.getChild("shape9c");
        this.shape9d = modelPart.getChild("shape9d");
        this.shape10 = modelPart.getChild("shape10");
        this.shape10a = modelPart.getChild("shape10a");
        this.shape10b = modelPart.getChild("shape10b");
        this.shape10c = modelPart.getChild("shape10c");
        this.shape10d = modelPart.getChild("shape10d");
        this.shape11 = modelPart.getChild("shape11");
        this.shape9e = modelPart.getChild("shape9e");
        this.shape9f = modelPart.getChild("shape9f");
        this.shape9g = modelPart.getChild("shape9g");
        this.shape9h = modelPart.getChild("shape9h");
        this.shape9h1 = modelPart.getChild("shape9h1");
        this.shape9h2 = modelPart.getChild("shape9h2");
        this.shape8a4 = modelPart.getChild("shape8a4");
        this.shape8a1b = modelPart.getChild("shape8a1b");
        this.shape8a1 = modelPart.getChild("shape8a1");
        this.shape8a3 = modelPart.getChild("shape8a3");
        this.shape8a2 = modelPart.getChild("shape8a2");
        this.shape8a1b1 = modelPart.getChild("shape8a1b1");
        this.shape8a1b2 = modelPart.getChild("shape8a1b2");
        this.shape8a1b3 = modelPart.getChild("shape8a1b3");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 0.3490659F));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 0.6981317F));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 1.396263F));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 1.745329F));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 2.443461F));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 2.792527F));

        root.addOrReplaceChild("shape1h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(-4, -4, 0, 8, 8, 11),
                PartPose.offsetAndRotation(0, 16, -5, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 0)
                        .addBox(-2, -2, 0, 4, 4, 3),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 0)
                        .addBox(-2, -2, 0, 4, 4, 3),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 0)
                        .addBox(-2, -2, 0, 4, 4, 3),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 8)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 8)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 10)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-0.5F, 20, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 0)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(-0.5F, 18, -7.9F, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 5)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-0.5F, 19, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 10)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(4, 15.5F, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 5)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-0.5F, 12, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 10)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-0.5F, 11, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 0)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(-0.5F, 13, -7.9F, 0, 0, 0));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 0)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(2, 15.5F, -7.9F, 0, 0, 0));

        root.addOrReplaceChild("shape5h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 5)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(3, 15.5F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 0)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(-3, 15.5F, -7.9F, 0, 0, 0));

        root.addOrReplaceChild("shape5j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 5)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-4, 15.5F, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 10)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-5, 15.5F, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 22, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 22, 0, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 18)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-7, 22, 4.9F, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 18)
                        .addBox(-4, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(7, 22, 4.9F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 1, 1, 11),
                PartPose.offsetAndRotation(6, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 18)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-7, 22, -4.9F, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 18)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-7, 22, 0, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape6f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 1, 1, 11),
                PartPose.offsetAndRotation(-7, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 18)
                        .addBox(-4, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(7, 22, 0, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape6h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 18)
                        .addBox(-4, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(7, 22, -4.9F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 0)
                        .addBox(-2, -2, 0, 4, 4, 3),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, -0.3926991F));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 23)
                        .addBox(0, 0, 0, 5, 2, 1),
                PartPose.offsetAndRotation(-2.5F, 18.5F, -5.2F, 0, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(39, 18)
                        .addBox(0, 0, 0, 4, 1, 2),
                PartPose.offsetAndRotation(-2, 18, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 14)
                        .addBox(0, 0, 0, 1, 5, 2),
                PartPose.offsetAndRotation(-3, 13.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(39, 18)
                        .addBox(0, 0, 0, 4, 1, 2),
                PartPose.offsetAndRotation(-2, 13, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 1, 5, 1),
                PartPose.offsetAndRotation(-4, 13.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(46, 22)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(0, 16, 6, 0, 0, 0));

        root.addOrReplaceChild("shape10a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 18)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-0.5F, 15.5F, 5.3F, 0, 0, 0));

        root.addOrReplaceChild("shape10b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(38, 23)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(0, 15, 6, 0, 0, 0));

        root.addOrReplaceChild("shape10c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(46, 22)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(-1, 14, 6, 0, 0, 0));

        root.addOrReplaceChild("shape10d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(38, 23)
                        .addBox(0, 0, 0, 2, 1, 1),
                PartPose.offsetAndRotation(-2, 16, 6, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(26, 21)
                        .addBox(0, 0, 0, 4, 4, 1),
                PartPose.offsetAndRotation(-2, 14, 5.1F, 0, 0, 0));

        root.addOrReplaceChild("shape9e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 14)
                        .addBox(0, 0, 0, 1, 5, 2),
                PartPose.offsetAndRotation(2, 13.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 34)
                        .addBox(0, 0, 0, 6, 1, 1),
                PartPose.offsetAndRotation(-3, 18.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 1, 5, 1),
                PartPose.offsetAndRotation(3, 13.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(26, 28)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-2, 19.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9h1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 34)
                        .addBox(0, 0, 0, 6, 1, 1),
                PartPose.offsetAndRotation(-3, 12.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape9h2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(26, 28)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-2, 11.5F, 5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape8a4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 15)
                        .addBox(0, 0, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-3, 13, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape8a1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 35)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-3.5F, 18.5F, -5.1F, 0, 0, 0));

        root.addOrReplaceChild("shape8a1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 23)
                        .addBox(0, 0, 0, 5, 2, 1),
                PartPose.offsetAndRotation(-2.5F, 11.5F, -5.2F, 0, 0, 0));

        root.addOrReplaceChild("shape8a3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 27)
                        .addBox(0, 0, 0, 2, 5, 1),
                PartPose.offsetAndRotation(-4.5F, 13.5F, -5.2F, 0, 0, 0));

        root.addOrReplaceChild("shape8a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 27)
                        .addBox(0, 0, 0, 2, 5, 1),
                PartPose.offsetAndRotation(2.5F, 13.5F, -5.2F, 0, 0, 0));

        root.addOrReplaceChild("shape8a1b1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 35)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(2.5F, 12.5F, -5.1F, 0, 0, 0));

        root.addOrReplaceChild("shape8a1b2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 35)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(2.5F, 18.5F, -5.1F, 0, 0, 0));

        root.addOrReplaceChild("shape8a1b3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 35)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-3.5F, 12.5F, -5.1F, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        root.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);

    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
