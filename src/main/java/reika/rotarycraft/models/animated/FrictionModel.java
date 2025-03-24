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

public class FrictionModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape4d;
    private final ModelPart shape4e;
    private final ModelPart shape4f;
    private final ModelPart shape4g;
    private final ModelPart shape4h;
    private final ModelPart shape4i;
    private final ModelPart shape4k;
    private final ModelPart shape4l;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart root;

    public FrictionModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape4d = modelPart.getChild("shape4d");
        this.shape4e = modelPart.getChild("shape4e");
        this.shape4f = modelPart.getChild("shape4f");
        this.shape4g = modelPart.getChild("shape4g");
        this.shape4h = modelPart.getChild("shape4h");
        this.shape4i = modelPart.getChild("shape4i");
        this.shape4k = modelPart.getChild("shape4k");
        this.shape4l = modelPart.getChild("shape4l");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 10, 1, 10),
                PartPose.offsetAndRotation(-5, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(0, 16, -6.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-1, -1, 0, 2, 2, 15),
                PartPose.offsetAndRotation(0, 16, -6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-1, -7, 0, 2, 14, 1),
                PartPose.offsetAndRotation(0, 16, -7, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -7, 0, 1, 14, 1),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-1, -7, 0, 2, 14, 1),
                PartPose.offsetAndRotation(0, 16, -7, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-1, -7, 0, 2, 14, 1),
                PartPose.offsetAndRotation(0, 16, -7, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape4d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-1, -7, 0, 2, 14, 1),
                PartPose.offsetAndRotation(0, 16, -7, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape4e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-1, -7, 0, 2, 14, 1),
                PartPose.offsetAndRotation(0, 16, -7, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape4f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 88)
                        .addBox(-1, -7, 0, 2, 14, 1),
                PartPose.offsetAndRotation(0, 16, -7, 0, 0, 0));

        root.addOrReplaceChild("shape4g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -7, 0, 1, 14, 1),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape4h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -7, 0, 1, 14, 1),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape4i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -7, 0, 1, 14, 1),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape4k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -7, 0, 1, 14, 1),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape4l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -7, 0, 1, 14, 1),
                PartPose.offsetAndRotation(0, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(12, 33)
                        .addBox(0, 0, 0, 4, 9, 1),
                PartPose.offsetAndRotation(-2, 14, 3, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 4, 9, 1),
                PartPose.offsetAndRotation(-2, 14, -4, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 16, 12, 1),
                PartPose.offsetAndRotation(-8, 11, 7, 0, 0, 0));

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
