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

public class SpawnerModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4r;
    private final ModelPart shape5r;
    private final ModelPart shape6r;
    private final ModelPart shape7;
    private final ModelPart shape6r2;
    private final ModelPart shape4r2;
    private final ModelPart shape5r2;
    private final ModelPart shape4r3;
    private final ModelPart shape5r3;
    private final ModelPart shape6r3;
    private final ModelPart shape6r4;
    private final ModelPart shape5r4;
    private final ModelPart shape4r4;
    private final ModelPart root;

    public SpawnerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4r = modelPart.getChild("shape4r");
        this.shape5r = modelPart.getChild("shape5r");
        this.shape6r = modelPart.getChild("shape6r");
        this.shape7 = modelPart.getChild("shape7");
        this.shape6r2 = modelPart.getChild("shape6r2");
        this.shape4r2 = modelPart.getChild("shape4r2");
        this.shape5r2 = modelPart.getChild("shape5r2");
        this.shape4r3 = modelPart.getChild("shape4r3");
        this.shape5r3 = modelPart.getChild("shape5r3");
        this.shape6r3 = modelPart.getChild("shape6r3");
        this.shape6r4 = modelPart.getChild("shape6r4");
        this.shape5r4 = modelPart.getChild("shape5r4");
        this.shape4r4 = modelPart.getChild("shape4r4");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 0)
                        .addBox(0, 0, 0, 12, 3, 12),
                PartPose.offsetAndRotation(-6, 19, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 14, 2, 14),
                PartPose.offsetAndRotation(-7, 22, -7, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 16)
                        .addBox(0, 0, 0, 10, 1, 10),
                PartPose.offsetAndRotation(-5, 18, -5, 0, 0, 0));

        root.addOrReplaceChild("shape4r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(58, 16)
                        .addBox(0, 0, 0, 1, 3, 8),
                PartPose.offsetAndRotation(-9, 23, -4, 0, 0, 0));

        root.addOrReplaceChild("shape5r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(76, 16)
                        .addBox(0, 0, 0, 1, 5, 5),
                PartPose.offsetAndRotation(-9, 26, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape6r",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 16)
                        .addBox(0, 0, 0, 1, 1, 8),
                PartPose.offsetAndRotation(-8, 23, -4, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(104, 0)
                        .addBox(0, 0, 0, 1, 7, 1),
                PartPose.offsetAndRotation(5, 12, 5, 0, 0, 0));

        root.addOrReplaceChild("shape6r2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 16)
                        .addBox(0, 0, 0, 1, 1, 8),
                PartPose.offsetAndRotation(7, 23, -4, 0, 0, 0));

        root.addOrReplaceChild("shape4r2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(58, 16)
                        .addBox(0, 0, 0, 1, 3, 8),
                PartPose.offsetAndRotation(8, 23, -4, 0, 0, 0));

        root.addOrReplaceChild("shape5r2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(76, 16)
                        .addBox(0, 0, 0, 1, 5, 5),
                PartPose.offsetAndRotation(8, 26, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape4r3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 8, 1, 1),
                PartPose.offsetAndRotation(-4, 23, 7, 0, 0, 0));

        root.addOrReplaceChild("shape5r3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 8, 3, 1),
                PartPose.offsetAndRotation(-4, 23, 8, 0, 0, 0));

        root.addOrReplaceChild("shape6r3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(0, 0, 0, 5, 5, 1),
                PartPose.offsetAndRotation(-2.5F, 26, 8, 0, 0, 0));

        root.addOrReplaceChild("shape6r4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 35)
                        .addBox(0, 0, 0, 5, 5, 1),
                PartPose.offsetAndRotation(-2.5F, 26, -9, 0, 0, 0));

        root.addOrReplaceChild("shape5r4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 8, 3, 1),
                PartPose.offsetAndRotation(-4, 23, -9, 0, 0, 0));

        root.addOrReplaceChild("shape4r4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 8, 1, 1),
                PartPose.offsetAndRotation(-4, 23, -8, 0, 0, 0));

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
