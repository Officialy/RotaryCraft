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

public class BreederModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape5g;
    private final ModelPart shape5h;
    private final ModelPart shape5i;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape8b;
    private final ModelPart shape8c;
    private final ModelPart shape4;
    private final ModelPart root;

    public BreederModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape5g = modelPart.getChild("shape5g");
        this.shape5h = modelPart.getChild("shape5h");
        this.shape5i = modelPart.getChild("shape5i");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape8b = modelPart.getChild("shape8b");
        this.shape8c = modelPart.getChild("shape8c");
        this.shape4 = modelPart.getChild("shape4");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 41)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(31, 0)
                        .addBox(0, 0, 0, 1, 7, 16),
                PartPose.offsetAndRotation(-8, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(31, 0)
                        .addBox(0, 0, 0, 1, 7, 16),
                PartPose.offsetAndRotation(7, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 14, 7, 1),
                PartPose.offsetAndRotation(-7, 16, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 14, 7, 1),
                PartPose.offsetAndRotation(-7, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(4, 17, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 3, 1, 3),
                PartPose.offsetAndRotation(-7, 17, 2, -0.1570796F, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-3, 17, -1, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 3, 1, 3),
                PartPose.offsetAndRotation(-1, 17, -6, 0, 0.1396263F, 0.1570796F));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(0, 17, 5, -0.2617994F, 0, 0.1745329F));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(5, 17, -4, -0.1489348F, -0.4363323F, -0.296706F));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(4, 17, 5, 0, 1.204277F, 0.3141593F));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 2, 2),
                PartPose.offsetAndRotation(-5, 17, -5, -0.2268928F, -0.2792527F, -0.1570796F));

        root.addOrReplaceChild("shape5h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-7, 17, -2, 0, 0, 0));

        root.addOrReplaceChild("shape5i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 1, 2),
                PartPose.offsetAndRotation(-1, 17, 0, 0, 0.3141593F, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 4)
                        .addBox(0, 0, 0, 3, 1, 1),
                PartPose.offsetAndRotation(-4, 17, 5, 0, 0.3839724F, 0.1570796F));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 4)
                        .addBox(0, 0, 0, 4, 1, 1),
                PartPose.offsetAndRotation(3, 17, -3, 0, 1.169371F, 0.1919862F));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 4)
                        .addBox(0, 0, 0, 9, 1, 1),
                PartPose.offsetAndRotation(-2, 17.2F, 4, 0, 1.745329F, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 4)
                        .addBox(0, 0, 0, 7, 1, 1),
                PartPose.offsetAndRotation(-2, 17, 1, 0, -0.6108652F, 0));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 4)
                        .addBox(0, 0, 0, 11, 2, 1),
                PartPose.offsetAndRotation(-5, 17, -2, 0.122173F, 0, 0.0349066F));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 6)
                        .addBox(0, 0, 0, 3, 1, 4),
                PartPose.offsetAndRotation(-4, 17.5F, -7, 0, -0.2617994F, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 6)
                        .addBox(0, 0, 0, 3, 1, 4),
                PartPose.offsetAndRotation(-2, 17.5F, 1, 0, -1.343904F, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 6)
                        .addBox(0, 0, 0, 3, 1, 4),
                PartPose.offsetAndRotation(0, 17.5F, 0, 0, 0.4886922F, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 11)
                        .addBox(0, 0, 0, 1, 2, 3),
                PartPose.offsetAndRotation(5, 16.5F, 1, -0.0872665F, -0.7853982F, 0.3316126F));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 11)
                        .addBox(0, 0, 0, 1, 2, 3),
                PartPose.offsetAndRotation(1, 16.5F, -4, -0.0349066F, -1.099557F, -0.2792527F));

        root.addOrReplaceChild("shape8b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 11)
                        .addBox(0, 0, 0, 1, 2, 3),
                PartPose.offsetAndRotation(-5, 16.8F, 6, 0, 1.43117F, 0));

        root.addOrReplaceChild("shape8c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 11)
                        .addBox(0, 0, 0, 1, 2, 3),
                PartPose.offsetAndRotation(-6, 16.5F, -2, 0, 0.3490659F, -0.2094395F));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 14, 1, 14),
                PartPose.offsetAndRotation(-7, 18, -7, 0, 0, 0));

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
