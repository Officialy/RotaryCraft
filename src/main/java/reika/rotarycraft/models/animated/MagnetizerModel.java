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

public class MagnetizerModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2c;
    private final ModelPart shape2b;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2j;
    private final ModelPart shape2h;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape2k;
    private final ModelPart shape2l;
    private final ModelPart shape2m;
    private final ModelPart shape2n;
    private final ModelPart shape2o;
    private final ModelPart shape2p;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart root;

    public MagnetizerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape2l = modelPart.getChild("shape2l");
        this.shape2m = modelPart.getChild("shape2m");
        this.shape2n = modelPart.getChild("shape2n");
        this.shape2o = modelPart.getChild("shape2o");
        this.shape2p = modelPart.getChild("shape2p");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 5, 1, 16),
                PartPose.offsetAndRotation(3, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 5, 1, 16),
                PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 94)
                        .addBox(0, 0, 0, 4, 1, 16),
                PartPose.offsetAndRotation(4, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 57)
                        .addBox(0, 0, 0, 3, 2, 16),
                PartPose.offsetAndRotation(5, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 98)
                        .addBox(0, 0, 0, 1, 2, 16),
                PartPose.offsetAndRotation(7, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 19)
                        .addBox(0, 0, 0, 2, 2, 16),
                PartPose.offsetAndRotation(6, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 76)
                        .addBox(0, 0, 0, 4, 1, 16),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 38)
                        .addBox(0, 0, 0, 3, 2, 16),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(79, 0)
                        .addBox(0, 0, 0, 2, 2, 16),
                PartPose.offsetAndRotation(-8, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 79)
                        .addBox(0, 0, 0, 1, 2, 16),
                PartPose.offsetAndRotation(-8, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 111)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(0, 15, -6.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 111)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(0, 15, -6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 4, 10, 2),
                PartPose.offsetAndRotation(-2, 13, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 4, 10, 2),
                PartPose.offsetAndRotation(-2, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 39)
                        .addBox(0, 0, 0, 2, 3, 16),
                PartPose.offsetAndRotation(-8, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 59)
                        .addBox(0, 0, 0, 2, 3, 16),
                PartPose.offsetAndRotation(6, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 92)
                        .addBox(0, 0, 0, 3, 2, 16),
                PartPose.offsetAndRotation(-8, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 20)
                        .addBox(0, 0, 0, 3, 2, 16),
                PartPose.offsetAndRotation(5, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(0, 0, 0, 4, 1, 16),
                PartPose.offsetAndRotation(-8, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(0, 0, 0, 4, 1, 16),
                PartPose.offsetAndRotation(4, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(79, 113)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(79, 113)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(79, 113)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(79, 113)
                        .addBox(-2, -2, 0, 4, 4, 6),
                PartPose.offsetAndRotation(0, 15, -2, 0, 0, 0));

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
