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

public class BeltModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shaape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart s1;
    private final ModelPart s2;
    private final ModelPart w;
    private final ModelPart w4;
    private final ModelPart w1;
    private final ModelPart w2;
    private final ModelPart w3;
    private final ModelPart w5;
    private final ModelPart w6;
    private final ModelPart w7;
    private final ModelPart c;
    private final ModelPart w8;
    private final ModelPart w9;
    private final ModelPart w10;
    private final ModelPart w11;
    private final ModelPart w12;
    private final ModelPart w13;
    private final ModelPart w14;
    private final ModelPart w15;
    private final ModelPart c1;
    private final ModelPart c2;
    private final ModelPart c3;
    private final ModelPart root;

    public BeltModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shaape1 = modelPart.getChild("shaape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.s1 = modelPart.getChild("s1");
        this.s2 = modelPart.getChild("s2");
        this.w = modelPart.getChild("w");
        this.w4 = modelPart.getChild("w4");
        this.w1 = modelPart.getChild("w1");
        this.w2 = modelPart.getChild("w2");
        this.w3 = modelPart.getChild("w3");
        this.w5 = modelPart.getChild("w5");
        this.w6 = modelPart.getChild("w6");
        this.w7 = modelPart.getChild("w7");
        this.c = modelPart.getChild("c");
        this.w8 = modelPart.getChild("w8");
        this.w9 = modelPart.getChild("w9");
        this.w10 = modelPart.getChild("w10");
        this.w11 = modelPart.getChild("w11");
        this.w12 = modelPart.getChild("w12");
        this.w13 = modelPart.getChild("w13");
        this.w14 = modelPart.getChild("w14");
        this.w15 = modelPart.getChild("w15");
        this.c1 = modelPart.getChild("c1");
        this.c2 = modelPart.getChild("c2");
        this.c3 = modelPart.getChild("c3");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 8)
                        .addBox(0, 0, 0, 16, 14, 5),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shaape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 8)
                        .addBox(0, 0, 0, 16, 14, 5),
                PartPose.offsetAndRotation(-8, 9, 3, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 1, 5),
                PartPose.offsetAndRotation(-7, 8, 3, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 1, 5),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 1, 5),
                PartPose.offsetAndRotation(-7, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 1, 5),
                PartPose.offsetAndRotation(-7, 23, 3, 0, 0, 0));

        root.addOrReplaceChild("s1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 0)
                        .addBox(-1, -1, 0, 2, 2, 17),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("s2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 0)
                        .addBox(-1, -1, 0, 2, 2, 17),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0));

        root.addOrReplaceChild("w4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0.7853982F));

        root.addOrReplaceChild("w1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 1.178097F));

        root.addOrReplaceChild("w2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 1.374447F));

        root.addOrReplaceChild("w3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0.3926991F));

        root.addOrReplaceChild("w5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0.1963495F));

        root.addOrReplaceChild("w6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0.5890486F));

        root.addOrReplaceChild("w7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, 2, 0, 0, 0.9817477F));

        root.addOrReplaceChild("c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(-4, -4, 0, 8, 8, 4),
                PartPose.offsetAndRotation(0, 16, -2, 0, 0, 0.7853982F));

        root.addOrReplaceChild("w8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 1.374447F));

        root.addOrReplaceChild("w9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 1.178097F));

        root.addOrReplaceChild("w10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.9817477F));

        root.addOrReplaceChild("w11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.7853982F));

        root.addOrReplaceChild("w12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.5890486F));

        root.addOrReplaceChild("w13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("w14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0.1963495F));

        root.addOrReplaceChild("w15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(-6, -6, 0, 12, 12, 1),
                PartPose.offsetAndRotation(0, 16, -3, 0, 0, 0));

        root.addOrReplaceChild("c1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(-4, -4, 0, 8, 8, 4),
                PartPose.offsetAndRotation(0, 16, -2, 0, 0, 0));

        root.addOrReplaceChild("c2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(-4, -4, 0, 8, 8, 4),
                PartPose.offsetAndRotation(0, 16, -2, 0, 0, 0.3926991F));

        root.addOrReplaceChild("c3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(-4, -4, 0, 8, 8, 4),
                PartPose.offsetAndRotation(0, 16, -2, 0, 0, 1.178097F));

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
