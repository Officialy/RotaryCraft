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

public class PipePumpModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape4;
    private final ModelPart shape4b;
    private final ModelPart root;

    public PipePumpModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4b = modelPart.getChild("shape4b");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 18, 1, 14),
                PartPose.offsetAndRotation(-9, 22, -7, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 18, 1, 14),
                PartPose.offsetAndRotation(-9, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 19)
                        .addBox(0, 0, 0, 18, 12, 1),
                PartPose.offsetAndRotation(-9, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 0)
                        .addBox(0, 0, 0, 18, 12, 1),
                PartPose.offsetAndRotation(-9, 10, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(0.4F, 0, -0.25F, 2, 1, 1),
                PartPose.offsetAndRotation(-9, 8, 5, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0.4F, 0, -0.25F, 20, 1, 1),
                PartPose.offsetAndRotation(-9, 8, -4, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 38)
                        .addBox(0.4F, 0, -0.25F, 14, 1, 1),
                PartPose.offsetAndRotation(-9, 8, -1, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(74, 38)
                        .addBox(0.4F, 0, -0.25F, 8, 1, 1),
                PartPose.offsetAndRotation(-9, 8, 2, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 35)
                        .addBox(18.4F, 0, -0.25F, 2, 1, 1),
                PartPose.offsetAndRotation(-9, 8, -16, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 35)
                        .addBox(0.4F, 0, -0.25F, 20, 1, 1),
                PartPose.offsetAndRotation(-9, 8, -7, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(33, 35)
                        .addBox(6.4F, 0, -0.25F, 14, 1, 1),
                PartPose.offsetAndRotation(-9, 8, -10, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 35)
                        .addBox(12.4F, 0, -0.25F, 8, 1, 1),
                PartPose.offsetAndRotation(-9, 8, -13, 0, -0.5235988F, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 41)
                        .addBox(0, 0, 0, 1, 12, 12),
                PartPose.offsetAndRotation(-9, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 1, 12, 12),
                PartPose.offsetAndRotation(8, 10, -6, 0, 0, 0));

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
