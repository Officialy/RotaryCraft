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

public class DryingBedModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape4d;
    private final ModelPart shape4e;
    private final ModelPart shape4f;
    private final ModelPart shape4g;
    private final ModelPart shape4h;
    private final ModelPart shape4i;
    private final ModelPart shape4k;
    private final ModelPart shape4j;
    private final ModelPart shape4m;
    private final ModelPart shape4n;
    private final ModelPart shape4o;
    private final ModelPart shape4p;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape6f;
    private final ModelPart shape6g;
    private final ModelPart shape6h;
    private final ModelPart shape6i;
    private final ModelPart shape6j;
    private final ModelPart shape6k;
    private final ModelPart root;

    public DryingBedModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape4e = modelPart.getChild("shape4e");
        this.shape4f = modelPart.getChild("shape4f");
        this.shape4g = modelPart.getChild("shape4g");
        this.shape4h = modelPart.getChild("shape4h");
        this.shape4i = modelPart.getChild("shape4i");
        this.shape4k = modelPart.getChild("shape4k");
        this.shape4j = modelPart.getChild("shape4j");
        this.shape4m = modelPart.getChild("shape4m");
        this.shape4n = modelPart.getChild("shape4n");
        this.shape4o = modelPart.getChild("shape4o");
        this.shape4p = modelPart.getChild("shape4p");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape6f = modelPart.getChild("shape6f");
        this.shape6g = modelPart.getChild("shape6g");
        this.shape6h = modelPart.getChild("shape6h");
        this.shape6i = modelPart.getChild("shape6i");
        this.shape6j = modelPart.getChild("shape6j");
        this.shape6k = modelPart.getChild("shape6k");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 61)
                        .addBox(0, 0, 0, 16, 2, 1),
                PartPose.offsetAndRotation(-8, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(0, 0, 0, 16, 2, 1),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 65)
                        .addBox(0, 0, 0, 1, 2, 14),
                PartPose.offsetAndRotation(7, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 2, 14),
                PartPose.offsetAndRotation(-8, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 44)
                        .addBox(0, 0, 0, 16, 2, 1),
                PartPose.offsetAndRotation(-8, 10, 3, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(6, 10, 4, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(1, 10, 4, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-8, 10, 4, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-3, 10, 4, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, -1.5F, 14, 1, 4),
                PartPose.offsetAndRotation(-7, 10.5F, 3, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 16, 2, 2),
                PartPose.offsetAndRotation(-8, 10, -1, 0, 0, 0));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-8, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-8, 10, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-8, 10, 1, 0, 0, 0));

        root.addOrReplaceChild("shape4g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(6, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(6, 10, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(6, 10, 1, 0, 0, 0));

        root.addOrReplaceChild("shape4k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-3, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-3, 10, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-3, 10, 1, 0, 0, 0));

        root.addOrReplaceChild("shape4n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(1, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape4o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(1, 10, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 33)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(1, 10, 1, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 44)
                        .addBox(0, 0, 0, 16, 2, 1),
                PartPose.offsetAndRotation(-8, 10, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 38)
                        .addBox(0, 0, -1.5F, 14, 1, 4),
                PartPose.offsetAndRotation(-7, 10.5F, -4, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(37, 0)
                        .addBox(0, 0, 0, 16, 8, 16),
                PartPose.offsetAndRotation(-8, 12, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(6, 20, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-8, 20, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(6, 20, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(2, 20, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-8, 20, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(6, 20, -4, 0, 0, 0));

        root.addOrReplaceChild("shape6f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(6, 20, 2, 0, 0, 0));

        root.addOrReplaceChild("shape6g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-4, 20, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-4, 20, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(2, 20, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-8, 20, -4, 0, 0, 0));

        root.addOrReplaceChild("shape6k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-8, 20, 2, 0, 0, 0));

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
