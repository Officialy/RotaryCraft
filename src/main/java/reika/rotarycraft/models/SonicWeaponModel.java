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

public class SonicWeaponModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape2a_a;
    private final ModelPart shape3c_a;
    private final ModelPart shape3c_b;
    private final ModelPart shape3a_a;
    private final ModelPart shape3_a;
    private final ModelPart shape2_a;
    private final ModelPart shape2b_a;
    private final ModelPart shape4;
    private final ModelPart shape4e;
    private final ModelPart shape4d;
    private final ModelPart shape4c;
    private final ModelPart shape4b;
    private final ModelPart shape4a;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5d;
    private final ModelPart shape6;
    private final ModelPart shape6c;
    private final ModelPart shape6b;
    private final ModelPart shape6a;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape5df;
    private final ModelPart shape5d_a;
    private final ModelPart shape5bf;
    private final ModelPart shape5b_a;
    private final ModelPart shape6b_a;
    private final ModelPart shape6b_b;
    private final ModelPart shape6a_c;
    private final ModelPart shape6a_d;
    private final ModelPart shape6b_e;
    private final ModelPart shape6b_f;
    private final ModelPart shape6b_g;
    private final ModelPart shape6b_h;
    private final ModelPart shape5d_i;
    private final ModelPart shape5d_j;
    private final ModelPart shape5d_k;
    private final ModelPart shape5d_l;
    private final ModelPart shape5d_m;
    private final ModelPart shape5d_n;
    private final ModelPart shape5d_o;
    private final ModelPart shape5d_p;
    private final ModelPart shape6b_q;
    private final ModelPart shape6b_r;
    private final ModelPart shape6b_s;
    private final ModelPart shape6b_t;
    private final ModelPart shape8aaa;
    private final ModelPart shape8aa;
    private final ModelPart root;

    public SonicWeaponModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape2a_a = modelPart.getChild("shape2a_a");
        this.shape3c_a = modelPart.getChild("shape3c_a");
        this.shape3c_b = modelPart.getChild("shape3c_b");
        this.shape3a_a = modelPart.getChild("shape3a_a");
        this.shape3_a = modelPart.getChild("shape3_a");
        this.shape2_a = modelPart.getChild("shape2_a");
        this.shape2b_a = modelPart.getChild("shape2b_a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4e = modelPart.getChild("shape4e");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape5df = modelPart.getChild("shape5df");
        this.shape5d_a = modelPart.getChild("shape5d_a");
        this.shape5bf = modelPart.getChild("shape5bf");
        this.shape5b_a = modelPart.getChild("shape5b_a");
        this.shape6b_a = modelPart.getChild("shape6b_a");
        this.shape6b_b = modelPart.getChild("shape6b_b");
        this.shape6a_c = modelPart.getChild("shape6a_c");
        this.shape6a_d = modelPart.getChild("shape6a_d");
        this.shape6b_e = modelPart.getChild("shape6b_e");
        this.shape6b_f = modelPart.getChild("shape6b_f");
        this.shape6b_g = modelPart.getChild("shape6b_g");
        this.shape6b_h = modelPart.getChild("shape6b_h");
        this.shape5d_i = modelPart.getChild("shape5d_i");
        this.shape5d_j = modelPart.getChild("shape5d_j");
        this.shape5d_k = modelPart.getChild("shape5d_k");
        this.shape5d_l = modelPart.getChild("shape5d_l");
        this.shape5d_m = modelPart.getChild("shape5d_m");
        this.shape5d_n = modelPart.getChild("shape5d_n");
        this.shape5d_o = modelPart.getChild("shape5d_o");
        this.shape5d_p = modelPart.getChild("shape5d_p");
        this.shape6b_q = modelPart.getChild("shape6b_q");
        this.shape6b_r = modelPart.getChild("shape6b_r");
        this.shape6b_s = modelPart.getChild("shape6b_s");
        this.shape6b_t = modelPart.getChild("shape6b_t");
        this.shape8aaa = modelPart.getChild("shape8aaa");
        this.shape8aa = modelPart.getChild("shape8aa");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 0)
                        .addBox(0, 0, 0, 8, 16, 8),
                PartPose.offsetAndRotation(-4, 8, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 106)
                        .addBox(0, 0, 0, 16, 2, 4),
                PartPose.offsetAndRotation(-8, 15, 4, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(72, 5)
                        .addBox(0, 0, 0, 16, 1, 4),
                PartPose.offsetAndRotation(-8, 8, 4, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 78)
                        .addBox(0, 0, 0, 16, 1, 4),
                PartPose.offsetAndRotation(-8, 23, 4, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(-8, 17, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(3, 17, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(-8, 9, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(3, 9, 3, 0, 0, 0));

        root.addOrReplaceChild("shape2a_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(72, 0)
                        .addBox(0, 0, 0, 16, 1, 4),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3c_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 14)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3c_b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 25)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(3, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3a_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 25)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(3, 17, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 14)
                        .addBox(0, 0, 0, 5, 6, 5),
                PartPose.offsetAndRotation(-8, 17, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 112)
                        .addBox(0, 0, 0, 16, 2, 4),
                PartPose.offsetAndRotation(-8, 15, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 83)
                        .addBox(0, 0, 0, 16, 1, 4),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 88)
                        .addBox(0, 0, 0, 4, 2, 8),
                PartPose.offsetAndRotation(-8, 15, -4, 0, 0, 0));

        root.addOrReplaceChild("shape4e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(96, 10)
                        .addBox(0, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(-8, 8, -4, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(0, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(-8, 23, -4, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(72, 10)
                        .addBox(0, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(4, 8, -4, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 88)
                        .addBox(0, 0, 0, 4, 2, 8),
                PartPose.offsetAndRotation(4, 15, -4, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 97)
                        .addBox(0, 0, 0, 4, 1, 8),
                PartPose.offsetAndRotation(4, 23, -4, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 1, 6),
                PartPose.offsetAndRotation(-8, 23, -3, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 5, 1, 6),
                PartPose.offsetAndRotation(-8, 17, -3, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 5, 1, 6),
                PartPose.offsetAndRotation(8, 23, -3, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 1, 6),
                PartPose.offsetAndRotation(8, 17, -3, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(-8, 17, 3, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(-8, 17, -3, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(8, 17, 3, 0, 2.617994F, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(8, 17, -3, 0, -2.617994F, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 19, -1, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 60)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 11, -1, 0, 0, 0));

        root.addOrReplaceChild("shape5df",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 17, -8, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape5d_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 1, 6),
                PartPose.offsetAndRotation(8, 9, -3, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape5bf",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 1, 6),
                PartPose.offsetAndRotation(-8, 15, -3, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape5b_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 5, 1, 6),
                PartPose.offsetAndRotation(8, 15, -3, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape6b_a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(-3, 17, -8, 0, -1.047198F, 0));

        root.addOrReplaceChild("shape6b_b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(8, 9, 3, 0, 2.617994F, 0));

        root.addOrReplaceChild("shape6a_c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(-8, 9, -3, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape6a_d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(8, 9, -3, 0, -2.617994F, 0));

        root.addOrReplaceChild("shape6b_e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(-8, 9, 3, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape6b_f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(3, 17, -8, 0, -2.094395F, 0));

        root.addOrReplaceChild("shape6b_g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(-3, 17, 8, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape6b_h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(3, 17, 8, 0, 2.094395F, 0));

        root.addOrReplaceChild("shape5d_i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 5, 1, 6),
                PartPose.offsetAndRotation(-8, 9, -3, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape5d_j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 23, -8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape5d_k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 17, 8, -2.617994F, 0, 0));

        root.addOrReplaceChild("shape5d_l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 23, 8, 2.617994F, 0, 0));

        root.addOrReplaceChild("shape5d_m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 9, -8, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape5d_n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 9, 8, -2.617994F, 0, 0));

        root.addOrReplaceChild("shape5d_o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 15, -8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape5d_p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, 0, 6, 1, 5),
                PartPose.offsetAndRotation(-3, 15, 8, 2.617994F, 0, 0));

        root.addOrReplaceChild("shape6b_q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(3, 9, -8, 0, -2.094395F, 0));

        root.addOrReplaceChild("shape6b_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(3, 9, 8, 0, 2.094395F, 0));

        root.addOrReplaceChild("shape6b_s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 5, 6, 1),
                PartPose.offsetAndRotation(-3, 9, -8, 0, -1.047198F, 0));

        root.addOrReplaceChild("shape6b_t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, -1, 5, 6, 1),
                PartPose.offsetAndRotation(-3, 9, 8, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape8aaa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(0, 0, 0, 2, 2, 12),
                PartPose.offsetAndRotation(-1, 19, -6, 0, 0, 0));

        root.addOrReplaceChild("shape8aa",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(0, 0, 0, 2, 2, 12),
                PartPose.offsetAndRotation(-1, 11, -6, 0, 0, 0));

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
