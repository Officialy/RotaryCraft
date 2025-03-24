package reika.rotarycraft.models.engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class JetModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/engine/");

    private final ModelPart shape1;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3a1;
    private final ModelPart shape3a2;
    private final ModelPart shape3d3;
    private final ModelPart shape3c4;
    private final ModelPart shape3b5;
    private final ModelPart shape36;
    private final ModelPart shape3a7;
    private final ModelPart shape3a8;
    private final ModelPart shape3a9;
    private final ModelPart shape3a0;
    private final ModelPart shape3a20;
    private final ModelPart shape3a11;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape2;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2h;
    private final ModelPart shape2i;
    private final ModelPart shape4;
    private final ModelPart shape41;
    private final ModelPart shape42;
    private final ModelPart shape43;
    private final ModelPart shape44;
    private final ModelPart shape45;
    private final ModelPart shape46;
    private final ModelPart shape47;
    private final ModelPart shape48;
    private final ModelPart shape49;
    private final ModelPart shape410;
    private final ModelPart shape411;
    private final ModelPart shape412;
    private final ModelPart shape413;
    private final ModelPart shape414;
    private final ModelPart shape415;
    private final ModelPart shape416;
    private final ModelPart shape417;
    private final ModelPart shape418;
    private final ModelPart shape419;
    private final ModelPart shape420;
    private final ModelPart shape421;
    private final ModelPart shape422;
    private final ModelPart shape423;
    private final ModelPart shape424;
    private final ModelPart shape425;
    private final ModelPart shape426;
    private final ModelPart shape2a;
    private final ModelPart shape2a2;
    private final ModelPart root;

    public JetModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3a1 = modelPart.getChild("shape3a1");
        this.shape3a2 = modelPart.getChild("shape3a2");
        this.shape3d3 = modelPart.getChild("shape3d3");
        this.shape3c4 = modelPart.getChild("shape3c4");
        this.shape3b5 = modelPart.getChild("shape3b5");
        this.shape36 = modelPart.getChild("shape36");
        this.shape3a7 = modelPart.getChild("shape3a7");
        this.shape3a8 = modelPart.getChild("shape3a8");
        this.shape3a9 = modelPart.getChild("shape3a9");
        this.shape3a0 = modelPart.getChild("shape3a0");
        this.shape3a20 = modelPart.getChild("shape3a20");
        this.shape3a11 = modelPart.getChild("shape3a11");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2h = modelPart.getChild("shape2h");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape4 = modelPart.getChild("shape4");
        this.shape41 = modelPart.getChild("shape41");
        this.shape42 = modelPart.getChild("shape42");
        this.shape43 = modelPart.getChild("shape43");
        this.shape44 = modelPart.getChild("shape44");
        this.shape45 = modelPart.getChild("shape45");
        this.shape46 = modelPart.getChild("shape46");
        this.shape47 = modelPart.getChild("shape47");
        this.shape48 = modelPart.getChild("shape48");
        this.shape49 = modelPart.getChild("shape49");
        this.shape410 = modelPart.getChild("shape410");
        this.shape411 = modelPart.getChild("shape411");
        this.shape412 = modelPart.getChild("shape412");
        this.shape413 = modelPart.getChild("shape413");
        this.shape414 = modelPart.getChild("shape414");
        this.shape415 = modelPart.getChild("shape415");
        this.shape416 = modelPart.getChild("shape416");
        this.shape417 = modelPart.getChild("shape417");
        this.shape418 = modelPart.getChild("shape418");
        this.shape419 = modelPart.getChild("shape419");
        this.shape420 = modelPart.getChild("shape420");
        this.shape421 = modelPart.getChild("shape421");
        this.shape422 = modelPart.getChild("shape422");
        this.shape423 = modelPart.getChild("shape423");
        this.shape424 = modelPart.getChild("shape424");
        this.shape425 = modelPart.getChild("shape425");
        this.shape426 = modelPart.getChild("shape426");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2a2 = modelPart.getChild("shape2a2");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 56)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, -1, 0, 7, 1, 5),
                PartPose.offsetAndRotation(6.1F, 16.6F, -4, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, -1, 0, 6, 1, 4),
                PartPose.offsetAndRotation(-6, 17.2F, -8, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, -1, 0, 7, 1, 5),
                PartPose.offsetAndRotation(-6.9F, 17, -4, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, 0, 0, 7, 1, 5),
                PartPose.offsetAndRotation(3.5F, 11, -4, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, -1, 0, 7, 1, 5),
                PartPose.offsetAndRotation(-3.5F, 11, -4, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape3a1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, -1, 0, 8, 1, 7),
                PartPose.offsetAndRotation(-4, 11, 0.9F, 0, 0, 0));

        root.addOrReplaceChild("shape3a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, -1, 0, 7, 1, 5),
                PartPose.offsetAndRotation(-3.5F, 23, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3d3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, -1, 0, 8, 1, 7),
                PartPose.offsetAndRotation(-4, 10, 0.9F, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape3c4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 8, 1, 7),
                PartPose.offsetAndRotation(4, 10, 0.9F, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape3b5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, -1, 0, 8, 1, 7),
                PartPose.offsetAndRotation(-8, 17, 0.9F, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape36",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 8, 1, 7),
                PartPose.offsetAndRotation(8, 17, 0.9F, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape3a7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 6, 1, 4),
                PartPose.offsetAndRotation(6, 17.2F, -8, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape3a8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, -1, 0, 6, 1, 4),
                PartPose.offsetAndRotation(-3, 22.4F, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3a9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, 0, 0, 6, 1, 4),
                PartPose.offsetAndRotation(3, 12, -8, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape3a0",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, -1, 0, 6, 1, 4),
                PartPose.offsetAndRotation(-3, 12, -8, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape3a20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, -1, 0, 7, 1, 5),
                PartPose.offsetAndRotation(-3.5F, 12, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3a11",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 49)
                        .addBox(0, -1, 0, 6, 1, 4),
                PartPose.offsetAndRotation(-3, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(83, 0)
                        .addBox(0, 0, 0, 7, 9, 1),
                PartPose.offsetAndRotation(-3.5F, 12.5F, -3, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(100, 0)
                        .addBox(0, 0, 0, 10, 3, 1),
                PartPose.offsetAndRotation(-5, 16, -3, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 8)
                        .addBox(0, 0, 0, 6, 10, 1),
                PartPose.offsetAndRotation(-3, 12, -3, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 8, 7, 1),
                PartPose.offsetAndRotation(-4, 14, -3, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 14, 3, 1),
                PartPose.offsetAndRotation(-7, 15.5F, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 8, 12, 1),
                PartPose.offsetAndRotation(-4, 11, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 10, 10, 1),
                PartPose.offsetAndRotation(-5, 12, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 12, 6, 1),
                PartPose.offsetAndRotation(-6, 14, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 11, 7, 1),
                PartPose.offsetAndRotation(-5.5F, 13.5F, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 2.443461F));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 1.745329F));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 0.6981317F));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 0.3490659F));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 1.396263F));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape2h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 2.792527F));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 0)
                        .addBox(-0.5F, -5.5F, 0, 1, 11, 1),
                PartPose.offsetAndRotation(0, 17, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 1.745329F));

        root.addOrReplaceChild("shape41",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 0.6981317F));

        root.addOrReplaceChild("shape42",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 0.3490659F));

        root.addOrReplaceChild("shape43",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 1.396263F));

        root.addOrReplaceChild("shape44",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 2.443461F));

        root.addOrReplaceChild("shape45",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape46",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape47",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape48",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 2.792527F));

        root.addOrReplaceChild("shape49",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 2.443461F));

        root.addOrReplaceChild("shape410",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 1.22173F));

        root.addOrReplaceChild("shape411",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 1.919862F));

        root.addOrReplaceChild("shape412",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 0.8726646F));

        root.addOrReplaceChild("shape413",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 2.268928F));

        root.addOrReplaceChild("shape414",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 2.96706F));

        root.addOrReplaceChild("shape415",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape416",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape417",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4, 0, 1, 8, 1),
                PartPose.offsetAndRotation(0, 17, -6, 0, 0, 0.1745329F));

        root.addOrReplaceChild("shape418",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -3.5F, 0, 1, 7, 1),
                PartPose.offsetAndRotation(0, 17, -7, 0, 0, 0));

        root.addOrReplaceChild("shape419",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 0.6981317F));

        root.addOrReplaceChild("shape420",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 1.396263F));

        root.addOrReplaceChild("shape421",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 1.745329F));

        root.addOrReplaceChild("shape422",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 0.3490659F));

        root.addOrReplaceChild("shape423",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape424",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape425",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 2.792527F));

        root.addOrReplaceChild("shape426",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-0.5F, -4.5F, 0, 1, 9, 1),
                PartPose.offsetAndRotation(0, 17, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-1, -1, 0, 2, 2, 16),
                PartPose.offsetAndRotation(0, 17, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 74)
                        .addBox(-1, -1, 0, 2, 2, 16),
                PartPose.offsetAndRotation(0, 17, -8.5F, 0, 0, 0));

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
