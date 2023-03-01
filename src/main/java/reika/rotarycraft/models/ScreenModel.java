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

public class ScreenModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart shape6a;
    private final ModelPart shape7;
    private final ModelPart shape8a;
    private final ModelPart shape8b;
    private final ModelPart shape8c;
    private final ModelPart shape8d;
    private final ModelPart shape8;
    private final ModelPart root;

    public ScreenModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape6 = modelPart.getChild("shape6");
        this.shape6a = modelPart.getChild("shape6a");
        this.shape7 = modelPart.getChild("shape7");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape8b = modelPart.getChild("shape8b");
        this.shape8c = modelPart.getChild("shape8c");
        this.shape8d = modelPart.getChild("shape8d");
        this.shape8 = modelPart.getChild("shape8");
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

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(91, 14)
                        .addBox(0, 0, 0, 8, 1, 8),
                PartPose.offsetAndRotation(-4, 20, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 14)
                        .addBox(0, 0, 0, 6, 1, 6),
                PartPose.offsetAndRotation(-3, 21, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 12, 1, 12),
                PartPose.offsetAndRotation(-6, 22, -6, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 26)
                        .addBox(0, 0, 0, 2, 12, 1),
                PartPose.offsetAndRotation(6, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 26)
                        .addBox(0, 0, 0, 2, 12, 1),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 22)
                        .addBox(0, 0, 0, 12, 2, 1),
                PartPose.offsetAndRotation(-6, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 12, 2, 1),
                PartPose.offsetAndRotation(-6, 18, -8, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 40)
                        .addBox(0, 0, 0, 12, 8, 1),
                PartPose.offsetAndRotation(-6, 10, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 110)
                        .addBox(0, 0, 0, 12, 7, 2),
                PartPose.offsetAndRotation(-6, 12, 6, 0, 0, 0));

        root.addOrReplaceChild("shape8b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 50)
                        .addBox(0, 0, 0, 16, 12, 5),
                PartPose.offsetAndRotation(-8, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape8c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 68)
                        .addBox(0, 0, 0, 16, 11, 3),
                PartPose.offsetAndRotation(-8, 9, -2, 0, 0, 0));

        root.addOrReplaceChild("shape8d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 83)
                        .addBox(0, 0, 0, 15, 10, 3),
                PartPose.offsetAndRotation(-7.5F, 10, 1, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(0, 0, 0, 14, 9, 2),
                PartPose.offsetAndRotation(-7, 11, 4, 0, 0, 0));

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
