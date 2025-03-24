package reika.rotarycraft.modinterface.model;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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
import org.joml.Vector3f;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class MagneticModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/converter/magneticmotortex.png");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape4;
    private final ModelPart shape3h;
    private final ModelPart shape3i;
    private final ModelPart shape3j;
    private final ModelPart shape3k;
    private final ModelPart shape3l;
    private final ModelPart shape3m;
    private final ModelPart shape3n;
    private final ModelPart shape3o;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape2i;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape6f;
    private final ModelPart shape6g;
    private final ModelPart shape6h;
    private final ModelPart shape6u;
    private final ModelPart shape6i;
    private final ModelPart shape6j;
    private final ModelPart shape6k;
    private final ModelPart shape6l;
    private final ModelPart shape6m;
    private final ModelPart shape6n;
    private final ModelPart shape6o;
    private final ModelPart shape6p;
    private final ModelPart shape6q;
    private final ModelPart shape6r;
    private final ModelPart shape6s;
    private final ModelPart shape6t;
    private final ModelPart shape6v;
    private final ModelPart shape6w;
    private final ModelPart shape6x;
    private final ModelPart shape6y;
    private final ModelPart shape6z;
    private final ModelPart shape6aa;
    private final ModelPart shape6ab;
    private final ModelPart shape6ac;
    private final ModelPart shape6ad;
    private final ModelPart shape6ae;
    private final ModelPart shape6af;
    private final ModelPart shape6ag;
    private final ModelPart shape6ah;
    private final ModelPart shape6ai;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4d;
    private final ModelPart shape4c;
    private final ModelPart root;

    public MagneticModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape4 = modelPart.getChild("shape4");
        this.shape3h = modelPart.getChild("shape3h");
        this.shape3i = modelPart.getChild("shape3i");
        this.shape3j = modelPart.getChild("shape3j");
        this.shape3k = modelPart.getChild("shape3k");
        this.shape3l = modelPart.getChild("shape3l");
        this.shape3m = modelPart.getChild("shape3m");
        this.shape3n = modelPart.getChild("shape3n");
        this.shape3o = modelPart.getChild("shape3o");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape6f = modelPart.getChild("shape6f");
        this.shape6g = modelPart.getChild("shape6g");
        this.shape6h = modelPart.getChild("shape6h");
        this.shape6u = modelPart.getChild("shape6u");
        this.shape6i = modelPart.getChild("shape6i");
        this.shape6j = modelPart.getChild("shape6j");
        this.shape6k = modelPart.getChild("shape6k");
        this.shape6l = modelPart.getChild("shape6l");
        this.shape6m = modelPart.getChild("shape6m");
        this.shape6n = modelPart.getChild("shape6n");
        this.shape6o = modelPart.getChild("shape6o");
        this.shape6p = modelPart.getChild("shape6p");
        this.shape6q = modelPart.getChild("shape6q");
        this.shape6r = modelPart.getChild("shape6r");
        this.shape6s = modelPart.getChild("shape6s");
        this.shape6t = modelPart.getChild("shape6t");
        this.shape6v = modelPart.getChild("shape6v");
        this.shape6w = modelPart.getChild("shape6w");
        this.shape6x = modelPart.getChild("shape6x");
        this.shape6y = modelPart.getChild("shape6y");
        this.shape6z = modelPart.getChild("shape6z");
        this.shape6aa = modelPart.getChild("shape6aa");
        this.shape6ab = modelPart.getChild("shape6ab");
        this.shape6ac = modelPart.getChild("shape6ac");
        this.shape6ad = modelPart.getChild("shape6ad");
        this.shape6ae = modelPart.getChild("shape6ae");
        this.shape6af = modelPart.getChild("shape6af");
        this.shape6ag = modelPart.getChild("shape6ag");
        this.shape6ah = modelPart.getChild("shape6ah");
        this.shape6ai = modelPart.getChild("shape6ai");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape4c = modelPart.getChild("shape4c");
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-1, -1, 0, 2, 2, 14),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, -1.963495F));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, -2.748893F));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 2.356194F));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, -0.3926991F));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, -1.178097F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 26)
                        .addBox(0, 0, 0, 9, 1, 2),
                PartPose.offsetAndRotation(-4.5F, 22, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape3i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, -1.570796F));

        root.addOrReplaceChild("shape3j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, -2.356194F));

        root.addOrReplaceChild("shape3k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape3l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape3m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape3n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 1.963495F));

        root.addOrReplaceChild("shape3o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(-2, -7, 0, 4, 1, 9),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 2.748893F));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-1, -1, 0, 2, 2, 14),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 4),
                PartPose.offsetAndRotation(0, 15, -4.5F, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -0.5F, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 4),
                PartPose.offsetAndRotation(0, 15, -4.5F, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 4),
                PartPose.offsetAndRotation(0, 15, -4.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 59)
                        .addBox(-1.5F, -1.5F, 0, 3, 3, 4),
                PartPose.offsetAndRotation(0, 15, -4.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 0)
                        .addBox(0, 0, 0, 4, 10, 1),
                PartPose.offsetAndRotation(-2, 13, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape6f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape6g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape6h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape6u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape6i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape6j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, -1.047198F));

        root.addOrReplaceChild("shape6k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, -1.570796F));

        root.addOrReplaceChild("shape6l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, -2.094395F));

        root.addOrReplaceChild("shape6m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape6n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape6o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape6p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape6q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape6r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape6s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape6t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape6v",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, -1.047198F));

        root.addOrReplaceChild("shape6w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, -1.570796F));

        root.addOrReplaceChild("shape6x",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 1, 0, 0, -2.094395F));

        root.addOrReplaceChild("shape6y",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0));

        root.addOrReplaceChild("shape6z",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape6aa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape6ab",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape6ac",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape6ad",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape6ae",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape6af",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape6ag",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, -1.047198F));

        root.addOrReplaceChild("shape6ah",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, -1.570796F));

        root.addOrReplaceChild("shape6ai",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-2, -7.3F, 0, 4, 1, 1),
                PartPose.offsetAndRotation(0, 15, 4, 0, 0, -2.094395F));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 78)
                        .addBox(0, -0.5F, 0, 1, 6, 2),
                PartPose.offsetAndRotation(-6.5F, 18, 2, 0, 0, 0.2617994F));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 78)
                        .addBox(0, -0.5F, 0, 1, 6, 2),
                PartPose.offsetAndRotation(5.5F, 18, 2, 0, 0, -0.2617994F));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 78)
                        .addBox(0, -0.5F, 0, 1, 6, 2),
                PartPose.offsetAndRotation(-6.5F, 18, -1, 0, 0, 0.2617994F));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 78)
                        .addBox(0, -0.5F, 0, 1, 6, 2),
                PartPose.offsetAndRotation(5.5F, 18, -1, 0, 0, -0.2617994F));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 15, 9, 2),
                PartPose.offsetAndRotation(-7.5F, 11, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 12)
                        .addBox(0, 0, 0, 13, 2, 2),
                PartPose.offsetAndRotation(-6.5F, 20, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 21)
                        .addBox(0, 0, 0, 13, 2, 2),
                PartPose.offsetAndRotation(-6.5F, 9, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 17)
                        .addBox(0, 0, 0, 9, 1, 2),
                PartPose.offsetAndRotation(-4.5F, 8, 6, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {

        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3o.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        stack.translate(0, 0.9375, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(0, -0.9375, 0);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 0.9375, 0);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(0, -0.9375, 0);

        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6u.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6o.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6p.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6q.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6r.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6s.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6t.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6v.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6w.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6x.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6y.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6z.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6aa.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6ab.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6ac.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6ad.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6ae.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6af.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6ag.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6ah.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6ai.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
