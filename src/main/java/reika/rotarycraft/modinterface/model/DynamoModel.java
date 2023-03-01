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

public class DynamoModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/modinterface/");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape2u;
    private final ModelPart shape2j;
    private final ModelPart shape2i;
    private final ModelPart shape2k;
    private final ModelPart shape2l;
    private final ModelPart shape2m;
    private final ModelPart shape2n;
    private final ModelPart shape2o;
    private final ModelPart shape2p;
    private final ModelPart shape3;
    private final ModelPart shape4;
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
    private final ModelPart shape4a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape8;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape8a;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape10;
    private final ModelPart shape10a;
    private final ModelPart shape11;
    private final ModelPart shape11a;
    private final ModelPart shape12;
    private final ModelPart shape12a;
    private final ModelPart shape13;
    private final ModelPart shape13a;
    private final ModelPart shape14;
    private final ModelPart shape14a;
    private final ModelPart root;

    public DynamoModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2u = modelPart.getChild("shape2u");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape2l = modelPart.getChild("shape2l");
        this.shape2m = modelPart.getChild("shape2m");
        this.shape2n = modelPart.getChild("shape2n");
        this.shape2o = modelPart.getChild("shape2o");
        this.shape2p = modelPart.getChild("shape2p");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
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
        this.shape4a = modelPart.getChild("shape4a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape8 = modelPart.getChild("shape8");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape10 = modelPart.getChild("shape10");
        this.shape10a = modelPart.getChild("shape10a");
        this.shape11 = modelPart.getChild("shape11");
        this.shape11a = modelPart.getChild("shape11a");
        this.shape12 = modelPart.getChild("shape12");
        this.shape12a = modelPart.getChild("shape12a");
        this.shape13 = modelPart.getChild("shape13");
        this.shape13a = modelPart.getChild("shape13a");
        this.shape14 = modelPart.getChild("shape14");
        this.shape14a = modelPart.getChild("shape14a");
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(-1, -1, 0, 2, 2, 17),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(-1, -1, 0, 2, 2, 17),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -4.9F, 0, 0, 0.3490659F));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, -2.792527F));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5.1F, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, 2.792527F));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5.1F, 0, 0, 2.443461F));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5.1F, 0, 0, 1.745329F));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, 1.396263F));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5.1F, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape2u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, 0.6981317F));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -4.9F, 0, 0, -0.3490659F));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, -0.6981317F));

        root.addOrReplaceChild("shape2l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5.1F, 0, 0, -1.047198F));

        root.addOrReplaceChild("shape2m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, -1.396263F));

        root.addOrReplaceChild("shape2n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5.1F, 0, 0, -1.745329F));

        root.addOrReplaceChild("shape2o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5, 0, 0, -2.094395F));

        root.addOrReplaceChild("shape2p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-2, -8, 0, 4, 5, 1),
                PartPose.offsetAndRotation(0, 15, -5.1F, 0, 0, -2.443461F));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 4, 10, 1),
                PartPose.offsetAndRotation(-2, 13, 6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.766667F, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape5h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape5i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, -1.047198F));

        root.addOrReplaceChild("shape5j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, -1.570796F));

        root.addOrReplaceChild("shape5k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(-0.5F, -7.5F, 0, 1, 4, 1),
                PartPose.offsetAndRotation(0, 15, -4.7F, 0, 0, -2.094395F));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 4, 10, 1),
                PartPose.offsetAndRotation(-2, 13, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(122, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, -5.5F, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(122, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(122, 0)
                        .addBox(-0.5F, -6, 0, 1, 12, 1),
                PartPose.offsetAndRotation(0, 15, -5.5F, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(89, 0)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(5.5F, 14, -4.2F, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(89, 0)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(3.5F, 14, -4.2F, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(77, 0)
                        .addBox(0, 0, 0, 4, 8, 1),
                PartPose.offsetAndRotation(3, 15, -4, 0, 0, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(89, 0)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-4.5F, 14, -4.2F, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(89, 0)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-6.5F, 14, -4.2F, 0, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(77, 0)
                        .addBox(0, 0, 0, 4, 8, 1),
                PartPose.offsetAndRotation(-7, 15, -4, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 9)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(-8, 15, 0, 0, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 0)
                        .addBox(0, -2, -2, 1, 4, 4),
                PartPose.offsetAndRotation(7, 15, 0, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 52)
                        .addBox(0, 0, 0, 4, 1, 2),
                PartPose.offsetAndRotation(-2, 17, -1, 0, 0, 0));

        root.addOrReplaceChild("shape10a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 48)
                        .addBox(0, 0, 0, 4, 1, 2),
                PartPose.offsetAndRotation(-2, 12, -1, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(115, 7)
                        .addBox(0, 0, 0, 1, 4, 2),
                PartPose.offsetAndRotation(-3, 13, -1, 0, 0, 0));

        root.addOrReplaceChild("shape11a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(115, 0)
                        .addBox(0, 0, 0, 1, 4, 2),
                PartPose.offsetAndRotation(2, 13, -1, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, 0, 0, 4, 2, 2),
                PartPose.offsetAndRotation(-7, 14, -1, 0, 0, 0));

        root.addOrReplaceChild("shape12a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, 0, 0, 4, 2, 2),
                PartPose.offsetAndRotation(3, 14, -1, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(94, 0)
                        .addBox(0, 0, 0, 1, 10, 3),
                PartPose.offsetAndRotation(-6, 13, -1.5F, 0, 0, 0));

        root.addOrReplaceChild("shape13a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(94, 0)
                        .addBox(0, 0, 0, 1, 10, 3),
                PartPose.offsetAndRotation(5, 13, -1.5F, 0, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 72)
                        .addBox(0, 0, 0, 1, 3, 4),
                PartPose.offsetAndRotation(-5.5F, 17, -3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape14a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 63)
                        .addBox(0, 0, 0, 1, 3, 4),
                PartPose.offsetAndRotation(4.5F, 17, -3.5F, 0, 0, 0));

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
