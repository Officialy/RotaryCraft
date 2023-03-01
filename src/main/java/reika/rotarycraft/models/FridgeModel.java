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

public class FridgeModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1g;
    private final ModelPart shape1h;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape3b;
    private final ModelPart shape4c;
    private final ModelPart shape3d;
    private final ModelPart root;

    public FridgeModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape3d = modelPart.getChild("shape3d");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 12)
                        .addBox(-1, -1, 0, 2, 2, 5),
                PartPose.offsetAndRotation(0, 13, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 7, 16),
                PartPose.offsetAndRotation(-8, 17, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(109, 15)
                        .addBox(-3, -3, 0, 6, 6, 2),
                PartPose.offsetAndRotation(0, 13, -4, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 5)
                        .addBox(-2, -2, 0, 4, 4, 2),
                PartPose.offsetAndRotation(0, 13, -2, 0, 0, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(109, 6)
                        .addBox(-3, -3, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 13, 5, 0, 0, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(83, 0)
                        .addBox(-5, -5, 0, 10, 9, 2),
                PartPose.offsetAndRotation(0, 13, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(87, 24)
                        .addBox(-5, -5, 0, 10, 9, 2),
                PartPose.offsetAndRotation(0, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(87, 12)
                        .addBox(-4, -4, 0, 8, 8, 2),
                PartPose.offsetAndRotation(0, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 24)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0.5F, 11.5F, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(11, 24)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(-1.5F, 11.5F, 2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(-1.5F, 13.5F, 2, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(-1.5F, 11.5F, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 24)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(-1.5F, 11.5F, 0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(-1.5F, 11.5F, 1, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 24)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.offsetAndRotation(0.5F, 11.5F, 1.5F, 0, 0, 0));

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
