package reika.rotarycraft.models;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import net.minecraft.client.renderer.texture.OverlayTexture;
import java.util.ArrayList;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
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

public class CVTModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape14;
    private final ModelPart shape14a;
    private final ModelPart shape14b;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart root;

    public CVTModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape14 = modelPart.getChild("shape14");
        this.shape14a = modelPart.getChild("shape14a");
        this.shape14b = modelPart.getChild("shape14b");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
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
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 10, 16),
                PartPose.offsetAndRotation(7, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 10, 16),
                PartPose.offsetAndRotation(-8, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 14, 10, 1),
                PartPose.offsetAndRotation(-7, 13, 7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, 0, 0, 14, 10, 1),
                PartPose.offsetAndRotation(-7, 13, -8, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 48)
                        .addBox(0, 0, 0, 15, 5, 7),
                PartPose.offsetAndRotation(-7.5F, 8.1F, 3, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape14a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(0, 0, 0, 16, 5, 6),
                PartPose.offsetAndRotation(-8, 8.1F, -3, 0, 0, 0));

        root.addOrReplaceChild("shape14b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 48)
                        .addBox(0, 0, 0, 15, 5, 7),
                PartPose.offsetAndRotation(-7.5F, 13, -8, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, -1, 8, 5, 1),
                PartPose.offsetAndRotation(-4, 8, -5, -0.3490659F, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(3, 4)
                        .addBox(0, 0, 0, 2, 1, 3),
                PartPose.offsetAndRotation(-1, 8.2F, -6, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape12.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        shape14.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14a.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape14b.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape7.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }
}
