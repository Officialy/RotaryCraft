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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class VacuumModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5d;
    private final ModelPart shape5e;
    private final ModelPart shape5f;
    private final ModelPart shape5g;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape6b;
    private final ModelPart shape6c;
    private final ModelPart shape6d;
    private final ModelPart shape6e;
    private final ModelPart shape6f;
    private final ModelPart shape6g;
    private final ModelPart shape6h;
    private final ModelPart shape6i;
    private final ModelPart shape6j;
    private final ModelPart shape6k;
    private final ModelPart shape6l;
    private final ModelPart shape6m;
    private final ModelPart shape6n;
    private final ModelPart shape6o;
    private final ModelPart shape6p;
    private final ModelPart root;

    public VacuumModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape5e = modelPart.getChild("shape5e");
        this.shape5f = modelPart.getChild("shape5f");
        this.shape5g = modelPart.getChild("shape5g");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape6b = modelPart.getChild("shape6b");
        this.shape6c = modelPart.getChild("shape6c");
        this.shape6d = modelPart.getChild("shape6d");
        this.shape6e = modelPart.getChild("shape6e");
        this.shape6f = modelPart.getChild("shape6f");
        this.shape6g = modelPart.getChild("shape6g");
        this.shape6h = modelPart.getChild("shape6h");
        this.shape6i = modelPart.getChild("shape6i");
        this.shape6j = modelPart.getChild("shape6j");
        this.shape6k = modelPart.getChild("shape6k");
        this.shape6l = modelPart.getChild("shape6l");
        this.shape6m = modelPart.getChild("shape6m");
        this.shape6n = modelPart.getChild("shape6n");
        this.shape6o = modelPart.getChild("shape6o");
        this.shape6p = modelPart.getChild("shape6p");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(36, 0)
                        .addBox(0, 0, 0, 12, 12, 12),
                PartPose.offsetAndRotation(-6, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 2, 16),
                PartPose.offsetAndRotation(-8, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 2, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 2, 16),
                PartPose.offsetAndRotation(6, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 2, 2, 16),
                PartPose.offsetAndRotation(6, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 0)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 22, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 0)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 8, 6, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 0)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 22, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(84, 0)
                        .addBox(0, 0, 0, 12, 2, 2),
                PartPose.offsetAndRotation(-6, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 2, 12, 2),
                PartPose.offsetAndRotation(-8, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 2, 12, 2),
                PartPose.offsetAndRotation(6, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 2, 12, 2),
                PartPose.offsetAndRotation(6, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 45)
                        .addBox(0, 0, 0, 2, 12, 2),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 29)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 22, 5, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(5, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 29)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 22, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(-6, 9, -5, 0, 0, 0));

        root.addOrReplaceChild("shape5e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 29)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 9, 5, 0, 0, 0));

        root.addOrReplaceChild("shape5f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 29)
                        .addBox(0, 0, 0, 12, 1, 1),
                PartPose.offsetAndRotation(-6, 9, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(5, 9, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, 0, 0, 10, 1, 1),
                PartPose.offsetAndRotation(-5, 21, -7, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(5, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(-6, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, 0, 0, 10, 1, 1),
                PartPose.offsetAndRotation(-5, 10, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(-6, 10, -7, 0, 0, 0));

        root.addOrReplaceChild("shape6e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, 0, 0, 10, 1, 1),
                PartPose.offsetAndRotation(-5, 21, 6, 0, 0, 0));

        root.addOrReplaceChild("shape6f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, 0, 0, 10, 1, 1),
                PartPose.offsetAndRotation(-5, 10, -7, 0, 0, 0));

        root.addOrReplaceChild("shape6g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(-7, 10, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(5, 10, -7, 0, 0, 0));

        root.addOrReplaceChild("shape6i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(6, 21, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(-7, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(-7, 10, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(6, 10, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(6, 10, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(-6, 22, -5, 0, 0, 0));

        root.addOrReplaceChild("shape6o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 18)
                        .addBox(0, 0, 0, 1, 12, 1),
                PartPose.offsetAndRotation(6, 10, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6p",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 1, 1, 10),
                PartPose.offsetAndRotation(-7, 21, -5, 0, 0, 0));

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
