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

public class ChunkLoaderModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape21;
    private final ModelPart shape3d;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape32d;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4d;
    private final ModelPart shape2d;
    private final ModelPart shape2c;
    private final ModelPart shape2b;
    private final ModelPart shape21d;
    private final ModelPart shape21c;
    private final ModelPart shape21b;
    private final ModelPart shape3;
    private final ModelPart shape31d;
    private final ModelPart shape32;
    private final ModelPart shape32b;
    private final ModelPart shape32c;
    private final ModelPart shape31;
    private final ModelPart shape31b;
    private final ModelPart shape31c;
    private final ModelPart root;

    public ChunkLoaderModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape21 = modelPart.getChild("shape21");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape32d = modelPart.getChild("shape32d");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape21d = modelPart.getChild("shape21d");
        this.shape21c = modelPart.getChild("shape21c");
        this.shape21b = modelPart.getChild("shape21b");
        this.shape3 = modelPart.getChild("shape3");
        this.shape31d = modelPart.getChild("shape31d");
        this.shape32 = modelPart.getChild("shape32");
        this.shape32b = modelPart.getChild("shape32b");
        this.shape32c = modelPart.getChild("shape32c");
        this.shape31 = modelPart.getChild("shape31");
        this.shape31b = modelPart.getChild("shape31b");
        this.shape31c = modelPart.getChild("shape31c");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 3, 16),
                PartPose.offsetAndRotation(-8, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 19, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 19, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(-3, 0, -1, 6, 1, 2),
                PartPose.offsetAndRotation(0, 10.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 9, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0, 0));

        root.addOrReplaceChild("shape32d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 26)
                        .addBox(1, 0, -2, 2, 1, 1),
                PartPose.offsetAndRotation(0, 10.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(-5, 0, -1, 10, 1, 2),
                PartPose.offsetAndRotation(0, 16.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(-4, 0, -1, 8, 1, 2),
                PartPose.offsetAndRotation(0, 13.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-1.5F, 0, -1.5F, 3, 1, 3),
                PartPose.offsetAndRotation(0, 12, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 13, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape21d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 10, 0, 0, 0, 0));

        root.addOrReplaceChild("shape21c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 13, 0, 0, 0, 0));

        root.addOrReplaceChild("shape21b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(-1, 0, -1, 2, 2, 2),
                PartPose.offsetAndRotation(0, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 40)
                        .addBox(-6, 0, -1, 12, 1, 2),
                PartPose.offsetAndRotation(0, 19.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape31d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(17, 26)
                        .addBox(-3, 0, 1, 2, 1, 1),
                PartPose.offsetAndRotation(0, 10.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape32",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 40)
                        .addBox(4, 0, -5, 2, 1, 4),
                PartPose.offsetAndRotation(0, 19.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape32b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 34)
                        .addBox(3, 0, -4, 2, 1, 3),
                PartPose.offsetAndRotation(0, 16.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape32c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 30)
                        .addBox(2, 0, -3, 2, 1, 2),
                PartPose.offsetAndRotation(0, 13.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape31",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(29, 40)
                        .addBox(-6, 0, 1, 2, 1, 4),
                PartPose.offsetAndRotation(0, 19.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape31b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(25, 34)
                        .addBox(-5, 0, 1, 2, 1, 3),
                PartPose.offsetAndRotation(0, 16.5F, 0, 0, 0, 0));

        root.addOrReplaceChild("shape31c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(21, 30)
                        .addBox(-4, 0, 1, 2, 1, 2),
                PartPose.offsetAndRotation(0, 13.5F, 0, 0, 0, 0));

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
