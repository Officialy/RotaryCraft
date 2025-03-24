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

public class HydraulicTurbineModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape9;
    private final ModelPart shape10;
    private final ModelPart shape11;
    private final ModelPart root;

    public HydraulicTurbineModel(ModelPart modelPart) {
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
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape10 = modelPart.getChild("shape10");
        this.shape11 = modelPart.getChild("shape11");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 2, 16),
                PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, -1, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 15, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-1, -6, 0, 2, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-1, -6, 0, 2, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-1, -6, 0, 2, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-1, -6, 0, 2, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-1, -6, 0, 2, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-1, -6, 0, 2, 12, 1),
                PartPose.offsetAndRotation(0, 15, -3, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 13, 13, 4),
                PartPose.offsetAndRotation(-6.5F, 8.5F, -2, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 18)
                        .addBox(0, 0, 0, 14, 1, 4),
                PartPose.offsetAndRotation(-7, 21, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 25)
                        .addBox(0, 0, 0, 14, 1, 4),
                PartPose.offsetAndRotation(-7, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(18, 31)
                        .addBox(0, 0, 0, 1, 12, 4),
                PartPose.offsetAndRotation(-7, 9, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 31)
                        .addBox(0, 0, 0, 1, 12, 4),
                PartPose.offsetAndRotation(6, 9, -6, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 19)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(2, 9, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 19)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(-3, 9, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 17, -5.8F, 0, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 12, -5.8F, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 54)
                        .addBox(0, 0, 0, 12, 12, 1),
                PartPose.offsetAndRotation(-6, 9, -5.3F, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(0, 0, 0, 16, 14, 5),
                PartPose.offsetAndRotation(-8, 8, 2, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 89)
                        .addBox(-4, -4, 0, 8, 8, 1),
                PartPose.offsetAndRotation(0, 15, 7, 0, 0, 0));

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
