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

public class MultiClutchModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/transmission/multiclutchtex.png");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4_r;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5_r;
    private final ModelPart shape6;
    private final ModelPart shape6_r;
    private final ModelPart shape6_r_r;
    private final ModelPart shape6_r_r_r;
    private final ModelPart shape5_r_r;
    private final ModelPart shape5_r_r_r;
    private final ModelPart shape4_r_r_r;
    private final ModelPart shape4_r_r;
    private final ModelPart shape5_u_r;
    private final ModelPart shape5_u_r_r;
    private final ModelPart shape5_u_r_r_r;
    private final ModelPart shape5_r_u;
    private final ModelPart shape6_u_r;
    private final ModelPart shape6_u_r_r;
    private final ModelPart shape6_u_r_r_r;
    private final ModelPart shape6_r_u;
    private final ModelPart shape4_u_r;
    private final ModelPart shape4_u_r_r;
    private final ModelPart shape4_u_r_r_r;
    private final ModelPart shape4_r_u;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape7;
    private final ModelPart root;

    public MultiClutchModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4_r = modelPart.getChild("shape4_r");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5_r = modelPart.getChild("shape5_r");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6_r = modelPart.getChild("shape6_r");
        this.shape6_r_r = modelPart.getChild("shape6_r_r");
        this.shape6_r_r_r = modelPart.getChild("shape6_r_r_r");
        this.shape5_r_r = modelPart.getChild("shape5_r_r");
        this.shape5_r_r_r = modelPart.getChild("shape5_r_r_r");
        this.shape4_r_r_r = modelPart.getChild("shape4_r_r_r");
        this.shape4_r_r = modelPart.getChild("shape4_r_r");
        this.shape5_u_r = modelPart.getChild("shape5_u_r");
        this.shape5_u_r_r = modelPart.getChild("shape5_u_r_r");
        this.shape5_u_r_r_r = modelPart.getChild("shape5_u_r_r_r");
        this.shape5_r_u = modelPart.getChild("shape5_r_u");
        this.shape6_u_r = modelPart.getChild("shape6_u_r");
        this.shape6_u_r_r = modelPart.getChild("shape6_u_r_r");
        this.shape6_u_r_r_r = modelPart.getChild("shape6_u_r_r_r");
        this.shape6_r_u = modelPart.getChild("shape6_r_u");
        this.shape4_u_r = modelPart.getChild("shape4_u_r");
        this.shape4_u_r_r = modelPart.getChild("shape4_u_r_r");
        this.shape4_u_r_r_r = modelPart.getChild("shape4_u_r_r_r");
        this.shape4_r_u = modelPart.getChild("shape4_r_u");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape7 = modelPart.getChild("shape7");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 23, 7, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 17)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-8, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(7, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(-8, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(69, 0)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(7, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, -4, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(3.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 10)
                        .addBox(-1, 0, -1, 2, 5, 2),
                PartPose.offsetAndRotation(0, 19.5F, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape6_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 10)
                        .addBox(-1, 0, -1, 2, 5, 2),
                PartPose.offsetAndRotation(0, 7.5F, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape6_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 10)
                        .addBox(-1, 0, -1, 2, 5, 2),
                PartPose.offsetAndRotation(0, 7.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape6_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, 0, -2.5F, 5, 1, 5),
                PartPose.offsetAndRotation(0, 12, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape5_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2.5F, -2.5F, 1, 5, 5),
                PartPose.offsetAndRotation(3, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(3.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape5_u_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 12)
                        .addBox(0, -1, -1, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5_u_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2.5F, -2.5F, 1, 5, 5),
                PartPose.offsetAndRotation(3, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5_u_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2.5F, -2.5F, 1, 5, 5),
                PartPose.offsetAndRotation(-4, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5_r_u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -2.5F, -2.5F, 1, 5, 5),
                PartPose.offsetAndRotation(-4, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape6_u_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 10)
                        .addBox(-1, 0, -1, 2, 5, 2),
                PartPose.offsetAndRotation(0, 19.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape6_u_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, 0, -2.5F, 5, 1, 5),
                PartPose.offsetAndRotation(0, 12, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape6_u_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, 0, -2.5F, 5, 1, 5),
                PartPose.offsetAndRotation(0, 19, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape6_r_u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, 0, -2.5F, 5, 1, 5),
                PartPose.offsetAndRotation(0, 19, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape4_u_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 16, 3.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape4_u_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, -4, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape4_u_r_r_r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, 3, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape4_r_u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-2.5F, -2.5F, 0, 5, 5, 1),
                PartPose.offsetAndRotation(0, 16, 3, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, 0, 1, 4, 14),
                PartPose.offsetAndRotation(7, 14, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 7)
                        .addBox(0, 0, 0, 14, 1, 4),
                PartPose.offsetAndRotation(-7, 23, -2, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 4, 14),
                PartPose.offsetAndRotation(-8, 14, -7, 0, 0, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 21)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 14)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 0)
                        .addBox(0, 0, 0, 14, 1, 4),
                PartPose.offsetAndRotation(-7, 8, -2, 0, 0, 0));

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
