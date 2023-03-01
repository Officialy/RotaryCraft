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

public class SteamTurbineModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/converter/steamturbtex.png");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart blade;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart root;

    public SteamTurbineModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.blade = modelPart.getChild("blade");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, -1, 0, 2, 2, 16),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, -1, 0, 2, 2, 16),
                PartPose.offsetAndRotation(0, 16, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("blade",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-0.5F, -6, -0.5F, 1, 6, 1),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 19)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 28)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(7, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(18, 28)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(-8, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(6, 28)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(7, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 28)
                        .addBox(0, 0, 0, 1, 14, 1),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 1, 14, 14),
                PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 1, 14, 14),
                PartPose.offsetAndRotation(7, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 102)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 18, 7, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 109)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 9, 7, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 116)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 75)
                        .addBox(0, 0, 0, 14, 5, 1),
                PartPose.offsetAndRotation(-7, 18, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 96)
                        .addBox(0, 0, 0, 5, 4, 1),
                PartPose.offsetAndRotation(2, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 96)
                        .addBox(0, 0, 0, 5, 4, 1),
                PartPose.offsetAndRotation(-7, 14, 7, 0, 0, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 90)
                        .addBox(0, 0, 0, 5, 4, 1),
                PartPose.offsetAndRotation(2, 14, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 90)
                        .addBox(0, 0, 0, 5, 4, 1),
                PartPose.offsetAndRotation(-7, 14, -8, 0, 0, 0));

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
