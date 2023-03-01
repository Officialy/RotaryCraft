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

public class CaveModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3e;
    private final ModelPart shape3f;
    private final ModelPart shape3g;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape7a;
    private final ModelPart shape7;
    private final ModelPart root;

    public CaveModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3g = modelPart.getChild("shape3g");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7 = modelPart.getChild("shape7");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 96)
                        .addBox(0, 0, 0, 12, 16, 12),
                PartPose.offsetAndRotation(-6, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 21)
                        .addBox(0, 0, 0, 16, 5, 2),
                PartPose.offsetAndRotation(-8, 19, 6, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 7)
                        .addBox(0, 0, 0, 16, 5, 2),
                PartPose.offsetAndRotation(-8, 8, 6, 0, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 0)
                        .addBox(0, 0, 0, 16, 5, 2),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(44, 14)
                        .addBox(0, 0, 0, 16, 5, 2),
                PartPose.offsetAndRotation(-8, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 25)
                        .addBox(0, 0, -1, 1, 6, 1),
                PartPose.offsetAndRotation(6, 13, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 25)
                        .addBox(0, 0, 0, 2, 6, 1),
                PartPose.offsetAndRotation(-8, 13, 6, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 96)
                        .addBox(-5.5F, 0, -5.5F, 11, 14, 11),
                PartPose.offsetAndRotation(0, 9, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(-8, 13, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(9, 17)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(5, 13, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 25)
                        .addBox(0, 0, 0, 2, 6, 1),
                PartPose.offsetAndRotation(-8, 13, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(19, 25)
                        .addBox(0, 0, 0, 1, 6, 1),
                PartPose.offsetAndRotation(6, 13, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 17)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(5, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(18, 17)
                        .addBox(0, 0, 0, 3, 6, 1),
                PartPose.offsetAndRotation(-8, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 18)
                        .addBox(0, 0, 0, 2, 5, 12),
                PartPose.offsetAndRotation(-8, 19, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 0)
                        .addBox(0, 0, 0, 2, 5, 12),
                PartPose.offsetAndRotation(-8, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 6, 2),
                PartPose.offsetAndRotation(7, 13, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 0)
                        .addBox(0, 0, 0, 1, 6, 2),
                PartPose.offsetAndRotation(7, 13, -7, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 36)
                        .addBox(0, 0, 0, 2, 5, 12),
                PartPose.offsetAndRotation(6, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(81, 54)
                        .addBox(0, 0, 0, 2, 5, 12),
                PartPose.offsetAndRotation(6, 19, -6, 0, 0, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(29, 25)
                        .addBox(0, 0, 0, 1, 6, 1),
                PartPose.offsetAndRotation(-8, 13, 5, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 25)
                        .addBox(0, 0, 0, 1, 6, 1),
                PartPose.offsetAndRotation(-8, 13, -6, 0, 0, 0));

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
