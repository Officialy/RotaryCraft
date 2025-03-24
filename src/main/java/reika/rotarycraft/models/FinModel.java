package reika.rotarycraft.models;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class FinModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/fintex.png");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2o;
    private final ModelPart shape2n;
    private final ModelPart shape2m;
    private final ModelPart shape2l;
    private final ModelPart shape2k;
    private final ModelPart shape2j;
    private final ModelPart shape2i;
    private final ModelPart shape2h;
    private final ModelPart shape2g;
    private final ModelPart shape2f;
    private final ModelPart shape2e;
    private final ModelPart shape2d;
    private final ModelPart shape2c;
    private final ModelPart shape2b;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart root;

    public FinModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2o = modelPart.getChild("shape2o");
        this.shape2n = modelPart.getChild("shape2n");
        this.shape2m = modelPart.getChild("shape2m");
        this.shape2l = modelPart.getChild("shape2l");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(5, 9, 1, 0, 0, 0));

        root.addOrReplaceChild("shape2o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(5, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-3, 9, 1, 0, 0, 0));

        root.addOrReplaceChild("shape2m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(1, 9, 1, 0, 0, 0));

        root.addOrReplaceChild("shape2l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-7, 9, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-3, 9, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(1, 9, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(5, 9, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-7, 9, 1, 0, 0, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(5, 9, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-7, 9, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-3, 9, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(1, 9, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-7, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(-3, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 14, 2),
                PartPose.offsetAndRotation(1, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 17)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7.5F, 24, 7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 17)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8.5F, 24, 7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 17)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8.5F, 24, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 17)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7.5F, 24, -8.5F, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        root.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1,1,1,1);
    }


    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
