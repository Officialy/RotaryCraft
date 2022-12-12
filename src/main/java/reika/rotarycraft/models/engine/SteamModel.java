package reika.rotarycraft.models.engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static reika.rotarycraft.RotaryCraft.MODID;

public class SteamModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/engine/steamtex.png");

    private final ModelPart shape1;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape14;
    private final ModelPart shape15;
    private final ModelPart shape16;
    private final ModelPart shape17;
    private final ModelPart shape2;
    private final ModelPart shape18;
    private final ModelPart shape19;
    private final ModelPart shape20;
    private final ModelPart shape11;
    private final ModelPart shape10;
    private final ModelPart shape9;
    private final ModelPart shape8;
    private final ModelPart shape7;
    private final ModelPart shape6;
    private final ModelPart shape5;
    private final ModelPart shape4;
    private final ModelPart shape21;
    private final ModelPart shape22;
    private final ModelPart shape23;
    private final ModelPart shape3;
    private final ModelPart root;

    public SteamModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape14 = modelPart.getChild("shape14");
        this.shape15 = modelPart.getChild("shape15");
        this.shape16 = modelPart.getChild("shape16");
        this.shape17 = modelPart.getChild("shape17");
        this.shape2 = modelPart.getChild("shape2");
        this.shape18 = modelPart.getChild("shape18");
        this.shape19 = modelPart.getChild("shape19");
        this.shape20 = modelPart.getChild("shape20");
        this.shape11 = modelPart.getChild("shape11");
        this.shape10 = modelPart.getChild("shape10");
        this.shape9 = modelPart.getChild("shape9");
        this.shape8 = modelPart.getChild("shape8");
        this.shape7 = modelPart.getChild("shape7");
        this.shape6 = modelPart.getChild("shape6");
        this.shape5 = modelPart.getChild("shape5");
        this.shape4 = modelPart.getChild("shape4");
        this.shape21 = modelPart.getChild("shape21");
        this.shape22 = modelPart.getChild("shape22");
        this.shape23 = modelPart.getChild("shape23");
        this.shape3 = modelPart.getChild("shape3");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 1, 8, 8),
                PartPose.offsetAndRotation(-6.1F, 13.8F, -5.1F, 0.3926991F, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 1, 8, 8),
                PartPose.offsetAndRotation(-6, 16, -5.5F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 5, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 1, 8, 8),
                PartPose.offsetAndRotation(-6, 10.8F, -2.2F, -0.3926991F, 0, 0));

        root.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 1, 8, 8),
                PartPose.offsetAndRotation(-5.9F, 12, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 16)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-1, 0, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape18",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -0.5F, -0.5F, 3, 1, 1),
                PartPose.offsetAndRotation(0, 0, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape19",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 16)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-1, 0, 0, 0, 0, 0));

        root.addOrReplaceChild("shape20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 16)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-1, 0, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 16)
                        .addBox(0, -1.5F, -1.5F, 1, 3, 3),
                PartPose.offsetAndRotation(-1, 0, 0, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 17)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-2, 20.5F, 1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 9, 6, 8),
                PartPose.offsetAndRotation(-3, 17, -4, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 17)
                        .addBox(0, 0, 0, 1, 1, 2),
                PartPose.offsetAndRotation(-4, 14.5F, -1, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -0.5F, -0.5F, 7, 1, 1),
                PartPose.offsetAndRotation(0, 0, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -0.5F, -0.5F, 7, 1, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 17)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(2, 20.5F, -6.7F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, -0.5F, -0.5F, 3, 1, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(34, 17)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(7, 17, -2, 0, 0, 0));

        root.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 3, 1, 5),
                PartPose.offsetAndRotation(-4, 15.5F, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 17)
                        .addBox(0, 0, 0, 2, 1, 4),
                PartPose.offsetAndRotation(-4, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 14)
                        .addBox(0, 0, 0, 11, 7, 6),
                PartPose.offsetAndRotation(-4, 16, -3, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {
        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
