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
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class WindModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/engine/windtex.png");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape5;
    private final ModelPart shape1a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape8c;
    private final ModelPart shape8b;
    private final ModelPart shape8a;
    private final ModelPart shape8;
    private final ModelPart shape9h;
    private final ModelPart shape9g;
    private final ModelPart shape9f;
    private final ModelPart shape9e;
    private final ModelPart shape9d;
    private final ModelPart shape9c;
    private final ModelPart shape9b;
    private final ModelPart shape9a;
    private final ModelPart root;

    public WindModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape8c = modelPart.getChild("shape8c");
        this.shape8b = modelPart.getChild("shape8b");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape8 = modelPart.getChild("shape8");
        this.shape9h = modelPart.getChild("shape9h");
        this.shape9g = modelPart.getChild("shape9g");
        this.shape9f = modelPart.getChild("shape9f");
        this.shape9e = modelPart.getChild("shape9e");
        this.shape9d = modelPart.getChild("shape9d");
        this.shape9c = modelPart.getChild("shape9c");
        this.shape9b = modelPart.getChild("shape9b");
        this.shape9a = modelPart.getChild("shape9a");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(95, 0)
                        .addBox(0, 0, 0, 14, 14, 2),
                PartPose.offsetAndRotation(-7, 9, -7, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(37, 0)
                        .addBox(0, 0, 0, 16, 16, 12),
                PartPose.offsetAndRotation(-8, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(0, 0, 0, 16, 1, 2),
                PartPose.offsetAndRotation(-8, 8, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 16, 1, 2),
                PartPose.offsetAndRotation(-8, 23, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(38, 29)
                        .addBox(0, 0, 0, 1, 14, 2),
                PartPose.offsetAndRotation(7, 9, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(46, 29)
                        .addBox(0, 0, 0, 1, 14, 2),
                PartPose.offsetAndRotation(-8, 9, 6, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 20)
                        .addBox(0, 0, 0, 12, 12, 1),
                PartPose.offsetAndRotation(-6, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 5)
                        .addBox(0, 0, 0, 12, 12, 1),
                PartPose.offsetAndRotation(-6, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 30)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 15, 6.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 30)
                        .addBox(-1, -1, 0, 2, 2, 2),
                PartPose.offsetAndRotation(0, 15, 6.5F, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 36)
                        .addBox(-1, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(0, 16, -10.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(54, 36)
                        .addBox(-1, -1, 0, 2, 2, 3),
                PartPose.offsetAndRotation(0, 16, -10.5F, 0, 0, 0));

        root.addOrReplaceChild("shape8c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, -16, 0, 2, 32, 1),
                PartPose.offsetAndRotation(0, 16, -10, 0, 0, 2.748893F));

        root.addOrReplaceChild("shape8b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, -16, 0, 2, 32, 1),
                PartPose.offsetAndRotation(0, 16, -10, 0, 0, 1.963495F));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, -16, 0, 2, 32, 1),
                PartPose.offsetAndRotation(0, 16, -10, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-1, -16, 0, 2, 32, 1),
                PartPose.offsetAndRotation(0, 16, -10, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape9h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape9g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, -1.19555F));

        root.addOrReplaceChild("shape9f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, 1.963495F));

        root.addOrReplaceChild("shape9e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, 2.748893F));

        root.addOrReplaceChild("shape9d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape9c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, -1.980949F));

        root.addOrReplaceChild("shape9b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, -2.766347F));

        root.addOrReplaceChild("shape9a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(0, 16, -11, 0, 0, -0.4101524F));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {

    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
