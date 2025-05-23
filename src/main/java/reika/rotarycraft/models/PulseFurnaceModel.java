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

public class PulseFurnaceModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape8;
    private final ModelPart shape9;
    private final ModelPart shape10;
    private final ModelPart shape11;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape14;
    private final ModelPart shape15;
    private final ModelPart shape16;
    private final ModelPart shape17;
    private final ModelPart shape18;
    private final ModelPart shape19;
    private final ModelPart shape20;
    private final ModelPart shape21;
    private final ModelPart shape5;
    private final ModelPart shape23;
    private final ModelPart shape24;
    private final ModelPart shape25;
    private final ModelPart shape26;
    private final ModelPart root;

    public PulseFurnaceModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9 = modelPart.getChild("shape9");
        this.shape10 = modelPart.getChild("shape10");
        this.shape11 = modelPart.getChild("shape11");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape14 = modelPart.getChild("shape14");
        this.shape15 = modelPart.getChild("shape15");
        this.shape16 = modelPart.getChild("shape16");
        this.shape17 = modelPart.getChild("shape17");
        this.shape18 = modelPart.getChild("shape18");
        this.shape19 = modelPart.getChild("shape19");
        this.shape20 = modelPart.getChild("shape20");
        this.shape21 = modelPart.getChild("shape21");
        this.shape5 = modelPart.getChild("shape5");
        this.shape23 = modelPart.getChild("shape23");
        this.shape24 = modelPart.getChild("shape24");
        this.shape25 = modelPart.getChild("shape25");
        this.shape26 = modelPart.getChild("shape26");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 20)
                        .addBox(0, 0, 0, 10, 1, 4),
                PartPose.offsetAndRotation(-6.8F, 16, -6.8F, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-5, 17, 2, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 17)
                        .addBox(0, 0, 0, 3, 4, 6),
                PartPose.offsetAndRotation(-4, 17, 1, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 22)
                        .addBox(0, 0, 0, 1, 2, 6),
                PartPose.offsetAndRotation(6, 21, 1, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 27)
                        .addBox(0, 0, 0, 6, 6, 8),
                PartPose.offsetAndRotation(-1, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 30)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(7, 22, 1, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 13)
                        .addBox(0, 0, 0, 1, 3, 6),
                PartPose.offsetAndRotation(5, 20, 1, 0, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(5, 16, 7, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 22)
                        .addBox(0, 0, 0, 1, 2, 6),
                PartPose.offsetAndRotation(6, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 30)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(7, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 13)
                        .addBox(0, 0, 0, 1, 3, 6),
                PartPose.offsetAndRotation(5, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 17)
                        .addBox(0, 0, 0, 1, 2, 2),
                PartPose.offsetAndRotation(5, 18, 5, 0, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(50, 17)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(6, 17, 6, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(52, 40)
                        .addBox(0, 0, 0, 1, 2, 2),
                PartPose.offsetAndRotation(4.1F, 18, 3, 0, 0, 0));

        root.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(50, 17)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(6, 17, 1, 0, 0, 0));

        root.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(5, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape18",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 8, 6),
                PartPose.offsetAndRotation(-1, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape19",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 17)
                        .addBox(0, 0, 0, 3, 6, 4),
                PartPose.offsetAndRotation(-4, 16, 2, 0, 0, 0));

        root.addOrReplaceChild("shape20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 6)
                        .addBox(0, 0, 0, 3, 2, 2),
                PartPose.offsetAndRotation(-8, 18, 3, 0, 0, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 0)
                        .addBox(0, 0, 0, 2, 2, 4),
                PartPose.offsetAndRotation(-8, 18, -1, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 13)
                        .addBox(0, 0, 0, 12, 1, 6),
                PartPose.offsetAndRotation(-7.5F, 16.5F, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape24",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 13, 6, 7),
                PartPose.offsetAndRotation(-7.8F, 17, -7.8F, 0, 0, 0));

        root.addOrReplaceChild("shape25",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(52, 25)
                        .addBox(0, 0, 0, 4, 8, 7),
                PartPose.offsetAndRotation(4, 15, -8, 0, 0, 0));

        root.addOrReplaceChild("shape26",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 17)
                        .addBox(0, 0, 0, 1, 2, 2),
                PartPose.offsetAndRotation(5, 18, 1, 0, 0, 0));

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
