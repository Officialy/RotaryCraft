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

public class PileDriverModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape9;
    private final ModelPart shape9a;
    private final ModelPart shape9b;
    private final ModelPart shape9c;
    private final ModelPart shape9d;
    private final ModelPart shape9e;
    private final ModelPart shape9f;
    private final ModelPart shape9g;
    private final ModelPart shape10;
    private final ModelPart shape1o;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape9h;
    private final ModelPart shape10af;
    private final ModelPart shape10z;
    private final ModelPart shape10y;
    private final ModelPart shape10x;
    private final ModelPart shape10w;
    private final ModelPart shape10v;
    private final ModelPart shape10u;
    private final ModelPart shape10t;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart root;

    public PileDriverModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape9 = modelPart.getChild("shape9");
        this.shape9a = modelPart.getChild("shape9a");
        this.shape9b = modelPart.getChild("shape9b");
        this.shape9c = modelPart.getChild("shape9c");
        this.shape9d = modelPart.getChild("shape9d");
        this.shape9e = modelPart.getChild("shape9e");
        this.shape9f = modelPart.getChild("shape9f");
        this.shape9g = modelPart.getChild("shape9g");
        this.shape10 = modelPart.getChild("shape10");
        this.shape1o = modelPart.getChild("shape1o");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape9h = modelPart.getChild("shape9h");
        this.shape10af = modelPart.getChild("shape10af");
        this.shape10z = modelPart.getChild("shape10z");
        this.shape10y = modelPart.getChild("shape10y");
        this.shape10x = modelPart.getChild("shape10x");
        this.shape10w = modelPart.getChild("shape10w");
        this.shape10v = modelPart.getChild("shape10v");
        this.shape10u = modelPart.getChild("shape10u");
        this.shape10t = modelPart.getChild("shape10t");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0.1745329F, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 0.1745329F, 0, 0));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 0.3490659F, 0, 0));

        root.addOrReplaceChild("shape9b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 0.6981317F, 0, 0));

        root.addOrReplaceChild("shape9c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 1.047198F, 0, 0));

        root.addOrReplaceChild("shape9d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 0.8726646F, 0, 0));

        root.addOrReplaceChild("shape9e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape9f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 1.22173F, 0, 0));

        root.addOrReplaceChild("shape9g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 1.396263F, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 0.1745329F, 0, 0));

        root.addOrReplaceChild("shape1o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 1.22173F, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0.6981317F, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0.8726646F, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0.3490659F, 0, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 1.047198F, 0, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 0)
                        .addBox(0, -5, -5, 2, 10, 10),
                PartPose.offsetAndRotation(-1, 16, 0, 1.396263F, 0, 0));

        root.addOrReplaceChild("shape9h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(-2, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape10af",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 1.396263F, 0, 0));

        root.addOrReplaceChild("shape10z",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 0.8726646F, 0, 0));

        root.addOrReplaceChild("shape10y",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 1.047198F, 0, 0));

        root.addOrReplaceChild("shape10x",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 0.6981317F, 0, 0));

        root.addOrReplaceChild("shape10w",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 0.3490659F, 0, 0));

        root.addOrReplaceChild("shape10v",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape10u",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 1.22173F, 0, 0));

        root.addOrReplaceChild("shape10t",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -6, -6, 1, 12, 12),
                PartPose.offsetAndRotation(1, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 25)
                        .addBox(0, -1, -1, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 25)
                        .addBox(0, -1, -1, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(51, 0)
                        .addBox(0, 0, 0, 1, 16, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(51, 0)
                        .addBox(0, 0, 0, 1, 16, 16),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 14, 3, 1),
                PartPose.offsetAndRotation(-7, 21, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 14, 3, 1),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 14, 3, 1),
                PartPose.offsetAndRotation(-7, 21, 7, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 14, 3, 1),
                PartPose.offsetAndRotation(-7, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, -0.5F, 2, 8, 1),
                PartPose.offsetAndRotation(-1, 16.4F, 8, -2.687807F, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, -0.5F, 2, 8, 1),
                PartPose.offsetAndRotation(-1, 16.4F, -8, 2.687807F, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, -0.5F, 2, 4, 13),
                PartPose.offsetAndRotation(-1, 21, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(106, 0)
                        .addBox(0, 0, -0.5F, 2, 7, 1),
                PartPose.offsetAndRotation(-1, 16, 8, -0.296706F, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(106, 0)
                        .addBox(0, 0, -0.5F, 2, 7, 1),
                PartPose.offsetAndRotation(-1, 16, -8, 0.296706F, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(86, 0)
                        .addBox(0, 0, 0, 2, 1, 5),
                PartPose.offsetAndRotation(-1, 9, 0.2F, -2.94088F, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(86, 0)
                        .addBox(0, 0, 0, 2, 1, 5),
                PartPose.offsetAndRotation(-1, 8, 0, -0.2181662F, 0, 0));

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
