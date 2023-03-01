package reika.rotarycraft.models.turret;

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

public class RailGunModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/turret/railguntex.png");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape7f;
    private final ModelPart shape7g;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape9;
    private final ModelPart shape2c;
    private final ModelPart shape2b;
    private final ModelPart shape2a1;
    private final ModelPart shape2a2;
    private final ModelPart shape3d;
    private final ModelPart root;

    public RailGunModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape7f = modelPart.getChild("shape7f");
        this.shape7g = modelPart.getChild("shape7g");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2a1 = modelPart.getChild("shape2a1");
        this.shape2a2 = modelPart.getChild("shape2a2");
        this.shape3d = modelPart.getChild("shape3d");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 81)
                        .addBox(-0.5F, 1.5F, -14, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-0.5F, -3.5F, -14, 1, 2, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 48)
                        .addBox(-4, -0.5F, -14, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 64)
                        .addBox(2, -0.5F, -14, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(-4, -2.5F, 2, 8, 5, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(25, 47)
                        .addBox(-1, -1, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 11.5F, 5, 0.3490659F, 0.7853982F, 0.3490659F));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(25, 52)
                        .addBox(-1, -1, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 11.5F, 5, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 16)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(-5, 0, -5, 10, 2, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.1963495F, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, -0.1963495F, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.9817477F, 0));

        root.addOrReplaceChild("shape7f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.5890486F, 0));

        root.addOrReplaceChild("shape7g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(-4, 0, 0, 2, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(2, 0, 0, 2, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 109)
                        .addBox(-2, 0, 0, 4, 1, 7),
                PartPose.offsetAndRotation(0, 21, 4, 1.186824F, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(49, 0)
                        .addBox(-4, -0.5F, -14, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 16)
                        .addBox(-4, -0.5F, -14, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2a1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 32)
                        .addBox(2, -0.5F, -14, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -0.7853982F));

        root.addOrReplaceChild("shape2a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(82, 0)
                        .addBox(2, -0.5F, -14, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(-5, -3.5F, 0, 10, 7, 2),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

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
