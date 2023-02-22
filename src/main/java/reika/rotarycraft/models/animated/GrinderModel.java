package reika.rotarycraft.models.animated;

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
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class GrinderModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/grindertex.png");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape2i;
    private final ModelPart shape2j;
    private final ModelPart shape2k;
    private final ModelPart shape2l;
    private final ModelPart shape2m;
    private final ModelPart shape2n;
    private final ModelPart shape2o;
    private final ModelPart shape2p;
    private final ModelPart shape2q;
    private final ModelPart shape2r;
    private final ModelPart shape2s;
    private final ModelPart shape2t;
    private final ModelPart shape2u;
    private final ModelPart shape2v;
    private final ModelPart shape2w;
    private final ModelPart shape2x;
    private final ModelPart shape2y;
    private final ModelPart shape2z;
    private final ModelPart shape2_a;
    private final ModelPart shape2_b;
    private final ModelPart shape2_c;
    private final ModelPart shape2_d;
    private final ModelPart shape2_e;
    private final ModelPart shape2_f;
    private final ModelPart shape2_g;
    private final ModelPart shape2_h;
    private final ModelPart shape2_i;
    private final ModelPart shape2_j;
    private final ModelPart shape2_k;
    private final ModelPart shape2_l;
    private final ModelPart shape2_m;
    private final ModelPart shape2_n;
    private final ModelPart shape2_o;
    private final ModelPart shape2_p;
    private final ModelPart shape2_q;
    private final ModelPart shape2_r;
    private final ModelPart shape2_s;
    private final ModelPart shape2_t;
    private final ModelPart shape2_u;
    private final ModelPart shape3;
    private final ModelPart shape3_a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape27;
    private final ModelPart shape21;
    private final ModelPart shape22;
    private final ModelPart shape23;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape211;
    private final ModelPart shape2111;
    private final ModelPart shape21111;
    private final ModelPart shape211111;
    private final ModelPart shape2111111;
    private final ModelPart shape21111111;
    private final ModelPart root;

    public GrinderModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape2l = modelPart.getChild("shape2l");
        this.shape2m = modelPart.getChild("shape2m");
        this.shape2n = modelPart.getChild("shape2n");
        this.shape2o = modelPart.getChild("shape2o");
        this.shape2p = modelPart.getChild("shape2p");
        this.shape2q = modelPart.getChild("shape2q");
        this.shape2r = modelPart.getChild("shape2r");
        this.shape2s = modelPart.getChild("shape2s");
        this.shape2t = modelPart.getChild("shape2t");
        this.shape2u = modelPart.getChild("shape2u");
        this.shape2v = modelPart.getChild("shape2v");
        this.shape2w = modelPart.getChild("shape2w");
        this.shape2x = modelPart.getChild("shape2x");
        this.shape2y = modelPart.getChild("shape2y");
        this.shape2z = modelPart.getChild("shape2z");
        this.shape2_a = modelPart.getChild("shape2_a");
        this.shape2_b = modelPart.getChild("shape2_b");
        this.shape2_c = modelPart.getChild("shape2_c");
        this.shape2_d = modelPart.getChild("shape2_d");
        this.shape2_e = modelPart.getChild("shape2_e");
        this.shape2_f = modelPart.getChild("shape2_f");
        this.shape2_g = modelPart.getChild("shape2_g");
        this.shape2_h = modelPart.getChild("shape2_h");
        this.shape2_i = modelPart.getChild("shape2_i");
        this.shape2_j = modelPart.getChild("shape2_j");
        this.shape2_k = modelPart.getChild("shape2_k");
        this.shape2_l = modelPart.getChild("shape2_l");
        this.shape2_m = modelPart.getChild("shape2_m");
        this.shape2_n = modelPart.getChild("shape2_n");
        this.shape2_o = modelPart.getChild("shape2_o");
        this.shape2_p = modelPart.getChild("shape2_p");
        this.shape2_q = modelPart.getChild("shape2_q");
        this.shape2_r = modelPart.getChild("shape2_r");
        this.shape2_s = modelPart.getChild("shape2_s");
        this.shape2_t = modelPart.getChild("shape2_t");
        this.shape2_u = modelPart.getChild("shape2_u");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3_a = modelPart.getChild("shape3_a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape27 = modelPart.getChild("shape27");
        this.shape21 = modelPart.getChild("shape21");
        this.shape22 = modelPart.getChild("shape22");
        this.shape23 = modelPart.getChild("shape23");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape211 = modelPart.getChild("shape211");
        this.shape2111 = modelPart.getChild("shape2111");
        this.shape21111 = modelPart.getChild("shape21111");
        this.shape211111 = modelPart.getChild("shape211111");
        this.shape2111111 = modelPart.getChild("shape2111111");
        this.shape21111111 = modelPart.getChild("shape21111111");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -5, 0, 0, 0.0872665F));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -3, 0, 0, 0.2617994F));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 2, 0, 0, 0.6981317F));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 1, 0, 0, 0.6108652F));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -1, 0, 0, 0.4363323F));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -4, 0, 0, 0.1745329F));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -2, 0, 0, 0.3490659F));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 0, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 5, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 3, 0, 0, 0.8726646F));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -5, 0, 0, 0.8726646F));

        root.addOrReplaceChild("shape2l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(4, 18, -7.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 5, 0, 0, 0.1745329F));

        root.addOrReplaceChild("shape2n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -4, 0, 0, 0.9599311F));

        root.addOrReplaceChild("shape2p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -3, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape2q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -2, 0, 0, 1.134464F));

        root.addOrReplaceChild("shape2r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -1, 0, 0, 1.22173F));

        root.addOrReplaceChild("shape2s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 0, 0, 0, 1.308997F));

        root.addOrReplaceChild("shape2t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 1, 0, 0, 1.396263F));

        root.addOrReplaceChild("shape2u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 2, 0, 0, 1.48353F));

        root.addOrReplaceChild("shape2v",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 3, 0, 0, 0));

        root.addOrReplaceChild("shape2w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 4, 0, 0, 0.0872665F));

        root.addOrReplaceChild("shape2x",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, 4, 0, 0, 0.9599311F));

        root.addOrReplaceChild("shape2y",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -5, 0, 0, 0.3054326F));

        root.addOrReplaceChild("shape2z",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -3, 0, 0, 0.9162979F));

        root.addOrReplaceChild("shape2_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -4, 0, 0, 1.003564F));

        root.addOrReplaceChild("shape2_b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 1, 0, 0, 0.567232F));

        root.addOrReplaceChild("shape2_c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape2_d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -2, 0, 0, 0.8290314F));

        root.addOrReplaceChild("shape2_e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 0, 0, 0, 0.6544985F));

        root.addOrReplaceChild("shape2_f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -1, 0, 0, 0.7417649F));

        root.addOrReplaceChild("shape2_g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 2, 0, 0, 0.4799655F));

        root.addOrReplaceChild("shape2_h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 5, 0, 0, 1.003564F));

        root.addOrReplaceChild("shape2_i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 5, 0, 0, 0.2181662F));

        root.addOrReplaceChild("shape2_j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -6, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape2_k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 2, 0, 0, -0.3054326F));

        root.addOrReplaceChild("shape2_l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -4, 0, 0, 0.2181662F));

        root.addOrReplaceChild("shape2_m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -2, 0, 0, 0.0436332F));

        root.addOrReplaceChild("shape2_n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 0, 0, 0, -0.1308997F));

        root.addOrReplaceChild("shape2_o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 3, 0, 0, -0.3926991F));

        root.addOrReplaceChild("shape2_p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -3, 0, 0, 0.1308997F));

        root.addOrReplaceChild("shape2_q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -1, 0, 0, -0.0436332F));

        root.addOrReplaceChild("shape2_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 1, 0, 0, -0.2181662F));

        root.addOrReplaceChild("shape2_s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, -6, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape2_t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 4, 0, 0, 0.3054326F));

        root.addOrReplaceChild("shape2_u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(4, 18, 4, 0, 0, 1.090831F));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 17)
                        .addBox(0, 0, 0, 1, 3, 16),
                PartPose.offsetAndRotation(-8, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 17)
                        .addBox(0, 0, 0, 1, 3, 16),
                PartPose.offsetAndRotation(7, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(103, 17)
                        .addBox(0, 0, 0, 1, 8, 2),
                PartPose.offsetAndRotation(7, 15, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(103, 17)
                        .addBox(0, 0, 0, 1, 8, 2),
                PartPose.offsetAndRotation(-8, 15, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(103, 17)
                        .addBox(0, 0, 0, 1, 8, 2),
                PartPose.offsetAndRotation(7, 15, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(103, 17)
                        .addBox(0, 0, 0, 1, 8, 2),
                PartPose.offsetAndRotation(-8, 15, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 1, 1, 12),
                PartPose.offsetAndRotation(7, 22, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 1, 1, 12),
                PartPose.offsetAndRotation(-8, 22, -6, 0, 0, 0));

        root.addOrReplaceChild("shape27",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-4, 18, -6, 0, 0, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(4, 18, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(-4, 18, -7.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(25, 0)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 16, 6.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 37)
                        .addBox(0, 0, 0, 14, 12, 1),
                PartPose.offsetAndRotation(-7, 11, 7, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 14, 10, 1),
                PartPose.offsetAndRotation(-7, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape211",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(-4, 18, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2111",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape21111",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(25, 0)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 16, 6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape211111",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 6, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape2111111",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 6, 0, 0, -0.3926991F));

        root.addOrReplaceChild("shape21111111",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 16, 6, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3_a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape5a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        double xoff = -0.25;
        double yoff = 1.125;

        stack.translate(xoff, yoff, 0);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(-xoff, -yoff, 0);
        shape2a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2o.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2p.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2q.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2r.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2s.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2t.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2u.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2v.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2w.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2x.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape27.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape22.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape211.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(xoff, yoff, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(-xoff, -yoff, 0);

        xoff = 0.25;
        stack.translate(xoff, yoff, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(-xoff, -yoff, 0);
        shape2.zRot = 1.0908F;
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape21.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2y.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2z.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_c.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_d.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_e.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_f.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_g.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_h.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_i.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_j.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_k.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_l.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_m.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_n.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_o.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_p.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_q.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_r.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_s.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_t.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2_u.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(xoff, yoff, 0);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(-xoff, -yoff, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape23.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape21111.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2111.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape211111.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2111111.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape21111111.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(0, -1, 0);
    }
}
