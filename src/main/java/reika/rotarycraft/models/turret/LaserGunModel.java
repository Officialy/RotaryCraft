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

public class LaserGunModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/turret");

    private final ModelPart shape3;
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
    private final ModelPart shape3d;
    private final ModelPart shape3da;
    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape3da1;
    private final ModelPart root;

    public LaserGunModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape3 = modelPart.getChild("shape3");
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
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3da = modelPart.getChild("shape3da");
        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape3da1 = modelPart.getChild("shape3da1");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 67)
                        .addBox(-2, -2, 2, 4, 4, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

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
                        .addBox(-2.7F, 0, 0, 1, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(1.7F, 0, 0, 1, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 109)
                        .addBox(-1, 0, 0, 2, 1, 8),
                PartPose.offsetAndRotation(0, 21, 4, 1.186824F, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(41, 47)
                        .addBox(-2, -2, -13, 4, 4, 3),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3da",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 63)
                        .addBox(-2.5F, -2.5F, -10, 5, 5, 4),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 79)
                        .addBox(-1, 1, -14, 2, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 76)
                        .addBox(2, -2, -14, 1, 2, 1),
                PartPose.offsetAndRotation(-1, 12.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 76)
                        .addBox(-1, -2, -14, 2, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 76)
                        .addBox(-1, -2, -14, 1, 2, 1),
                PartPose.offsetAndRotation(-1, 12.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3da1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(-3, -3, -6, 6, 6, 8),
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
