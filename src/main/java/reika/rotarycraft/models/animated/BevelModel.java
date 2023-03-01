package reika.rotarycraft.models.animated;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
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

public class BevelModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/transmission/beveltex.png");

    private final ModelPart shape1;
    private final ModelPart shape3;
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
    private final ModelPart shape2;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart root;

    public BevelModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape3 = modelPart.getChild("shape3");
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
        this.shape2 = modelPart.getChild("shape2");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
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

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .texOffs(48, 1)
                        .addBox(0, 0, 0, 1, 15, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 24)
                        .addBox(0, 0, 0, 2, 2, 6),
                PartPose.offsetAndRotation(-1, 15, -8.5F, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 24)
                        .addBox(0, 0, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 14.5F, -8.5F, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-3, 14, -2, 0, 0, 0));

        root.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-3, 16, -2.8F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape18",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 4, 4, 1),
                PartPose.offsetAndRotation(-2, 14, -3, 0, 0, 0));

        root.addOrReplaceChild("shape19",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 13.2F, -3, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-4, 13, -3, 0, 0, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-4, 16, -4.2F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-3, 13, -4, 0, 0, 0));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 7)
                        .addBox(0, 0, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.7F, -4, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(0, 0, 0, 15, 15, 1),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(90, 16)
                        .addBox(0, 0, 0, 1, 15, 1),
                PartPose.offsetAndRotation(7, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(17, 17)
                        .addBox(0, 0, 0, 1, 1, 14),
                PartPose.offsetAndRotation(7, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 8, 7, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 32);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
//        if (LODModelPart.allowCompiling) {
//            this.renderList(te);
//        }
//        else {
            shape1.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape2.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape3.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape4.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape5.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
            shape6.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
//        }

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape14.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape15.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape18.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape19.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape22.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape23.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.ZN.rotationDegrees(phi));
        stack.translate(0, -1, 0);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XN.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape12.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape13.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape16.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape17.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape20.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        shape21.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
    }
    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }
}
