package reika.rotarycraft.models.animated;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import reika.rotarycraft.base.model.GearboxBaseModel;

import static reika.rotarycraft.RotaryCraft.MODID;

public class Gearbox16Model extends GearboxBaseModel {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/transmission/shaft/gear/");

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
    private final ModelPart shape26;
    private final ModelPart shape27;
    private final ModelPart shape28;
    private final ModelPart shape29;
    private final ModelPart shape30;
    private final ModelPart shape31;
    private final ModelPart shape32;
    private final ModelPart shape33;
    private final ModelPart shape34;
    private final ModelPart shape35;
    private final ModelPart shape36;
    private final ModelPart shape37;
    private final ModelPart shape38;
    private final ModelPart shape39;
    private final ModelPart shape40;
    private final ModelPart shape41;
    private final ModelPart shape42;
    private final ModelPart shape43;
    private final ModelPart shape44;
    private final ModelPart shape45;
    private final ModelPart shape46;
    private final ModelPart shape47;
    private final ModelPart shape48;
    private final ModelPart shape49;
    private final ModelPart shape50;
    private final ModelPart shape51;
    private final ModelPart root;

    public Gearbox16Model(ModelPart modelPart) {
        super(modelPart);
        this.root = modelPart;

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
        this.shape26 = modelPart.getChild("shape26");
        this.shape27 = modelPart.getChild("shape27");
        this.shape28 = modelPart.getChild("shape28");
        this.shape29 = modelPart.getChild("shape29");
        this.shape30 = modelPart.getChild("shape30");
        this.shape31 = modelPart.getChild("shape31");
        this.shape32 = modelPart.getChild("shape32");
        this.shape33 = modelPart.getChild("shape33");
        this.shape34 = modelPart.getChild("shape34");
        this.shape35 = modelPart.getChild("shape35");
        this.shape36 = modelPart.getChild("shape36");
        this.shape37 = modelPart.getChild("shape37");
        this.shape38 = modelPart.getChild("shape38");
        this.shape39 = modelPart.getChild("shape39");
        this.shape40 = modelPart.getChild("shape40");
        this.shape41 = modelPart.getChild("shape41");
        this.shape42 = modelPart.getChild("shape42");
        this.shape43 = modelPart.getChild("shape43");
        this.shape44 = modelPart.getChild("shape44");
        this.shape45 = modelPart.getChild("shape45");
        this.shape46 = modelPart.getChild("shape46");
        this.shape47 = modelPart.getChild("shape47");
        this.shape48 = modelPart.getChild("shape48");
        this.shape49 = modelPart.getChild("shape49");
        this.shape50 = modelPart.getChild("shape50");
        this.shape51 = modelPart.getChild("shape51");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();
        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.ZERO);

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.ZERO);

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 12, 16),
                PartPose.ZERO);

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .texOffs(30, 17)
                        .addBox(0, 0, 0, 1, 3, 1),
                PartPose.ZERO);

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.ZERO);

        root.addOrReplaceChild("shape11",
                CubeListBuilder.create()
                        .texOffs(42, 17)
                        .addBox(0, 0, 0, 1, 3, 10),
                PartPose.ZERO);


        //
        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 13, 2, 2),
                PartPose.offsetAndRotation(-6.5F, 18, 3, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 13, 2, 2),
                PartPose.offsetAndRotation(-6.5F, 19, 2.6F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-5, 15.5F, -5.7F, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-5, 17.5F, -6.5F, 0.8028515F, 0, 0));

        root.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-2, 14, -9.3F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-4, 14.5F, -0.5F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape18",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 3, 3),
                PartPose.offsetAndRotation(-5, 14.5F, 2, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape19",
                CubeListBuilder.create()
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 3, 3),
                PartPose.offsetAndRotation(-5, 13, 2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 14, -6.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 10, 2, 2),
                PartPose.offsetAndRotation(-5, 17.5F, -5, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 5, 5),
                PartPose.offsetAndRotation(-1, 16.5F, 1.8F, 0, 0, 0));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 5, 5),
                PartPose.offsetAndRotation(-1, 19, 0.5F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape24",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 3, 2, 2),
                PartPose.offsetAndRotation(5.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape25",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 3, 2, 2),
                PartPose.offsetAndRotation(5.5F, 16, -1.4F, 0.7853982F, 0, 0.0174533F));

        root.addOrReplaceChild("shape26",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(5, 15.5F, -5.5F, 0, 0, 0));

        root.addOrReplaceChild("shape27",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(5, 17.5F, -6.5F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape28",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 19)
                        .addBox(0, 0, 0, 2, 7, 3),
                PartPose.offsetAndRotation(-2, 16, -5, 0, 0, 0));

        root.addOrReplaceChild("shape29",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 19)
                        .addBox(0, 0, 0, 2, 10, 3),
                PartPose.offsetAndRotation(1, 13, 2.4F, 0, 0, 0));

        root.addOrReplaceChild("shape30",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 8, 2, 2),
                PartPose.offsetAndRotation(-3, 14.5F, 2.5F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape31",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 8, 2, 2),
                PartPose.offsetAndRotation(-3, 13.5F, 3, 0, 0, 0));

        root.addOrReplaceChild("shape32",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(5, 14, -2, 0, 0, 0));

        root.addOrReplaceChild("shape33",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(5, 16, -3, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape34",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-5, 11, -3, 0, 0, 0));

        root.addOrReplaceChild("shape35",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-5, 13, -4, 0.8028515F, 0, 0));

        root.addOrReplaceChild("shape36",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-4, 11.4F, 0.8F, 0, 0, 0));

        root.addOrReplaceChild("shape37",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-2, 11, -8, 0, 0, 0));

        root.addOrReplaceChild("shape38",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-5, 14, -2, 0, 0, 0));

        root.addOrReplaceChild("shape39",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-5, 16, -3, 0.8028515F, 0, 0));

        root.addOrReplaceChild("shape40",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 10, 2, 2),
                PartPose.offsetAndRotation(-5, 16.5F, -4.6F, 0, 0, 0));

        root.addOrReplaceChild("shape41",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 13, -6, 0, 0, 0));

        root.addOrReplaceChild("shape42",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 4, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape43",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 4, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape44",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(2, 10, -4, 0, 0, 0));

        root.addOrReplaceChild("shape45",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(2, 13, -5.3F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape46",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 13, -2.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape47",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 12, -2, 0, 0, 0));

        root.addOrReplaceChild("shape48",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(2, 12.4F, 1.8F, 0, 0, 0));

        root.addOrReplaceChild("shape49",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(2, 14.5F, 1, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape50",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(5, 12.4F, 1.8F, 0, 0, 0));

        root.addOrReplaceChild("shape51",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(5, 14.5F, 1, 0.7853982F, 0, 0));

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
