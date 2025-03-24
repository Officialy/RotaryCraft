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

public class DomeEmitterModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape2i;
    private final ModelPart shape2j;
    private final ModelPart shape2k;
    private final ModelPart shape2l;
    private final ModelPart shape2m;
    private final ModelPart shape2n;
    private final ModelPart shape2o;
    private final ModelPart shape2p;
    private final ModelPart shape2q;
    private final ModelPart shape2r;
    private final ModelPart shape2s;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart root;

    public DomeEmitterModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape2j = modelPart.getChild("shape2j");
        this.shape2k = modelPart.getChild("shape2k");
        this.shape2l = modelPart.getChild("shape2l");
        this.shape2m = modelPart.getChild("shape2m");
        this.shape2n = modelPart.getChild("shape2n");
        this.shape2o = modelPart.getChild("shape2o");
        this.shape2p = modelPart.getChild("shape2p");
        this.shape2q = modelPart.getChild("shape2q");
        this.shape2r = modelPart.getChild("shape2r");
        this.shape2s = modelPart.getChild("shape2s");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
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

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 8, 1, 8),
                PartPose.offsetAndRotation(-4, 18, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(105, 0)
                        .addBox(0, 0, 0, 1, 2, 6),
                PartPose.offsetAndRotation(-7, 18, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 1, 12, 2),
                PartPose.offsetAndRotation(-3, 8, -1, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(72, 0)
                        .addBox(0, 0, 0, 1, 9, 3),
                PartPose.offsetAndRotation(-4, 11, -1.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 0)
                        .addBox(0, 0, 0, 1, 6, 4),
                PartPose.offsetAndRotation(-5, 14, -2, 0, 0, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 0)
                        .addBox(0, 0, 0, 1, 4, 5),
                PartPose.offsetAndRotation(-6, 16, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(105, 18)
                        .addBox(0, 0, 0, 6, 2, 1),
                PartPose.offsetAndRotation(-3, 18, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(72, 13)
                        .addBox(0, 0, 0, 1, 9, 3),
                PartPose.offsetAndRotation(3, 11, -1.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 11)
                        .addBox(0, 0, 0, 1, 6, 4),
                PartPose.offsetAndRotation(4, 14, -2, 0, 0, 0));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 10)
                        .addBox(0, 0, 0, 1, 4, 5),
                PartPose.offsetAndRotation(5, 16, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(105, 9)
                        .addBox(0, 0, 0, 1, 2, 6),
                PartPose.offsetAndRotation(6, 18, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 15)
                        .addBox(0, 0, 0, 1, 12, 2),
                PartPose.offsetAndRotation(2, 8, -1, 0, 0, 0));

        root.addOrReplaceChild("shape2k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 30)
                        .addBox(0, 0, 0, 2, 12, 1),
                PartPose.offsetAndRotation(-1, 8, -3, 0, 0, 0));

        root.addOrReplaceChild("shape2l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(72, 26)
                        .addBox(0, 0, 0, 3, 9, 1),
                PartPose.offsetAndRotation(-1.5F, 11, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 22)
                        .addBox(0, 0, 0, 4, 6, 1),
                PartPose.offsetAndRotation(-2, 14, -5, 0, 0, 0));

        root.addOrReplaceChild("shape2n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 20)
                        .addBox(0, 0, 0, 5, 4, 1),
                PartPose.offsetAndRotation(-2.5F, 16, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 44)
                        .addBox(0, 0, 0, 2, 12, 1),
                PartPose.offsetAndRotation(-1, 8, 2, 0, 0, 0));

        root.addOrReplaceChild("shape2p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(72, 37)
                        .addBox(0, 0, 0, 3, 9, 1),
                PartPose.offsetAndRotation(-1.5F, 11, 3, 0, 0, 0));

        root.addOrReplaceChild("shape2q",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 30)
                        .addBox(0, 0, 0, 4, 6, 1),
                PartPose.offsetAndRotation(-2, 14, 4, 0, 0, 0));

        root.addOrReplaceChild("shape2r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(92, 26)
                        .addBox(0, 0, 0, 5, 4, 1),
                PartPose.offsetAndRotation(-2.5F, 16, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2s",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(105, 22)
                        .addBox(0, 0, 0, 6, 2, 1),
                PartPose.offsetAndRotation(-3, 18, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 20, -7, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(0, 0, 0, 12, 1, 12),
                PartPose.offsetAndRotation(-6, 19, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(110, 26)
                        .addBox(0, 0, 0, 2, 4, 2),
                PartPose.offsetAndRotation(-1, 14, -1, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(105, 26)
                        .addBox(0, 0, 0, 1, 5, 1),
                PartPose.offsetAndRotation(-0.5F, 9, -0.5F, 0, 0, 0));

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
