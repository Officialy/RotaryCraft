package reika.rotarycraft.models;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import net.minecraft.client.renderer.texture.OverlayTexture;
import java.util.ArrayList;

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

public class ExtractorModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape36;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape35;
    private final ModelPart shape34;
    private final ModelPart shape33;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape32;
    private final ModelPart shape8;
    private final ModelPart shape39;
    private final ModelPart shape9;
    private final ModelPart shape31;
    private final ModelPart shape30;
    private final ModelPart shape10;
    private final ModelPart shape11;
    private final ModelPart shape29;
    private final ModelPart shape28;
    private final ModelPart shape27;
    private final ModelPart shape26;
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
    private final ModelPart shape22;
    private final ModelPart shape23;
    private final ModelPart shape24;
    private final ModelPart shape25;
    private final ModelPart shape38;
    private final ModelPart shape37;
    private final ModelPart root;

    public ExtractorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape36 = modelPart.getChild("shape36");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape35 = modelPart.getChild("shape35");
        this.shape34 = modelPart.getChild("shape34");
        this.shape33 = modelPart.getChild("shape33");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape32 = modelPart.getChild("shape32");
        this.shape8 = modelPart.getChild("shape8");
        this.shape39 = modelPart.getChild("shape39");
        this.shape9 = modelPart.getChild("shape9");
        this.shape31 = modelPart.getChild("shape31");
        this.shape30 = modelPart.getChild("shape30");
        this.shape10 = modelPart.getChild("shape10");
        this.shape11 = modelPart.getChild("shape11");
        this.shape29 = modelPart.getChild("shape29");
        this.shape28 = modelPart.getChild("shape28");
        this.shape27 = modelPart.getChild("shape27");
        this.shape26 = modelPart.getChild("shape26");
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
        this.shape22 = modelPart.getChild("shape22");
        this.shape23 = modelPart.getChild("shape23");
        this.shape24 = modelPart.getChild("shape24");
        this.shape25 = modelPart.getChild("shape25");
        this.shape38 = modelPart.getChild("shape38");
        this.shape37 = modelPart.getChild("shape37");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 5, 16),
                PartPose.offsetAndRotation(-8, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, 0, 0, 4, 2, 12),
                PartPose.offsetAndRotation(-7.9F, 11.9F, -6.3F, -0.3839724F, 0, 0));

        root.addOrReplaceChild("shape36",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 2, 4, 12),
                PartPose.offsetAndRotation(-6.9F, 11, -6, -0.3839724F, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(112, 11)
                        .addBox(0, 0, 0, 1, 4, 3),
                PartPose.offsetAndRotation(-0.5F, 16, 3.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(76, 18)
                        .addBox(0, 0, 0, 5, 2, 1),
                PartPose.offsetAndRotation(-4, 17.5F, -3, 0, 0, 0));

        root.addOrReplaceChild("shape35",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 3)
                        .addBox(0, 0, 0, 1, 2, 3),
                PartPose.offsetAndRotation(0, 17.5F, -2, 0, 0, 0));

        root.addOrReplaceChild("shape34",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(76, 18)
                        .addBox(0, 0, 0, 5, 2, 1),
                PartPose.offsetAndRotation(-4, 17.5F, 1, 0, 0, 0));

        root.addOrReplaceChild("shape33",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 3)
                        .addBox(0, 0, 0, 1, 2, 3),
                PartPose.offsetAndRotation(-4, 17.5F, -2, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 32)
                        .addBox(0, 0, 0, 3, 1, 3),
                PartPose.offsetAndRotation(-3, 18, -2, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(117, 0)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(-2, 16, -5, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(85, 15)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-2, 17.5F, -1, 0, 0, 0));

        root.addOrReplaceChild("shape32",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(85, 15)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-2.2F, 17.5F, -0.5F, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 70)
                        .addBox(0, 0, 0, 9, 1, 2),
                PartPose.offsetAndRotation(-2, 15.5F, -6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape39",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 6)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(-2, 17.5F, 1, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 87)
                        .addBox(0, 0, 0, 1, 1, 7),
                PartPose.offsetAndRotation(3, 18, -4, 0, 0, 0));

        root.addOrReplaceChild("shape31",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 95)
                        .addBox(0, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(4, 18.8F, -4, 0, 0, 0));

        root.addOrReplaceChild("shape30",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 87)
                        .addBox(0, 0, 0, 1, 1, 7),
                PartPose.offsetAndRotation(6, 18, -4, 0, 0, 0));

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 77)
                        .addBox(0, 0, 0, 5, 3, 7),
                PartPose.offsetAndRotation(2.5F, 16.5F, 1, -0.3839724F, 0, 0));

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 73)
                        .addBox(0, 0, 0, 9, 3, 1),
                PartPose.offsetAndRotation(-2, 16.5F, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape29",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(0, 0, 0, 10, 3, 3),
                PartPose.offsetAndRotation(-2.5F, 16, -7, 0, 0, 0));

        root.addOrReplaceChild("shape28",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(28, 28)
                        .addBox(0, 0, 0, 6, 3, 1),
                PartPose.offsetAndRotation(-7, 16, 6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape27",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(50, 22)
                        .addBox(0, 0, 0, 7, 2, 2),
                PartPose.offsetAndRotation(-7.5F, 15.1F, 3.6F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape26",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(0, 0, 0, 8, 4, 4),
                PartPose.offsetAndRotation(-8.1F, 15, 3, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 14)
                        .addBox(0, 0, 0, 7, 1, 3),
                PartPose.offsetAndRotation(-7, 10, -4, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 4, 8, 4),
                PartPose.offsetAndRotation(-8, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 18)
                        .addBox(0, 0, 0, 3, 1, 3),
                PartPose.offsetAndRotation(-4, 10, -7, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(30, 21)
                        .addBox(0, 0, 0, 4, 1, 6),
                PartPose.offsetAndRotation(-1, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 11, 1, 4),
                PartPose.offsetAndRotation(-8, 9, -1, 0, 0, 0));

        root.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(80, 2)
                        .addBox(0, 0, 0, 1, 1, 3),
                PartPose.offsetAndRotation(-8, 10, -7, 0, 0, 0));

        root.addOrReplaceChild("shape18",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(80, 0)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape19",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(112, 0)
                        .addBox(0, 0, 0, 1, 10, 1),
                PartPose.offsetAndRotation(3, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(90, 9)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(3, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(90, 0)
                        .addBox(0, 0, 0, 8, 1, 1),
                PartPose.offsetAndRotation(-8, 9, -8, 0, 0, 0));

        root.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(90, 2)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(-8, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 12)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape24",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(90, 9)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(-8, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape25",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 12)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-8, 8, 3, 0, 0, 0));

        root.addOrReplaceChild("shape38",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(112, 0)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(-8, 9, 3, 0, 0, 0));

        root.addOrReplaceChild("shape37",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(112, 0)
                        .addBox(0, 0, 0, 1, 9, 1),
                PartPose.offsetAndRotation(3, 9, 3, 0, 0, 0));

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
