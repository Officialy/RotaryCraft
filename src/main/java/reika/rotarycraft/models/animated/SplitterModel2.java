package reika.rotarycraft.models.animated;

import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

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

import static reika.rotarycraft.RotaryCraft.MODID;

public class SplitterModel2 extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/bedsplittertex.png");

    private final ModelPart main;

    public SplitterModel2(ModelPart modelPart) {
        super(RenderType::entitySolid);
        this.main = modelPart.getChild("shape1");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        PartDefinition main = root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        main.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 1)
                        .addBox(0, 0, 0, 1, 15, 16),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        main.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 2),
                PartPose.offsetAndRotation(2.5F, 15, -1, 0, 0, 0));

        main.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 2),
                PartPose.offsetAndRotation(2.5F, 16, -1.4F, 0.7853982F, 0, 0));

        main.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 24)
                        .addBox(0, 0, 0, 2, 2, 6),
                PartPose.offsetAndRotation(-1, 15, 2.5F, 0, 0, 0));

        main.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 24)
                        .addBox(0, 0, 0, 2, 2, 6),
                PartPose.offsetAndRotation(0, 14.5F, 2.5F, 0, 0, 0.7853982F));

        main.addOrReplaceChild("shape16",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 15)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(2, 14, -2, 0, 0, 0));

        main.addOrReplaceChild("shape17",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 15)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(2, 16, -2.8F, 0.7853982F, 0, 0));

        main.addOrReplaceChild("shape18",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(83, 26)
                        .addBox(0, 0, 0, 4, 4, 1),
                PartPose.offsetAndRotation(-2, 14, 2, 0, 0, 0));

        main.addOrReplaceChild("shape19",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(83, 26)
                        .addBox(0, 0, 0, 4, 4, 1),
                PartPose.offsetAndRotation(0, 13.2F, 2, 0, 0, 0.7853982F));

        main.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(107, 19)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(3, 16, -4.2F, 0.7853982F, 0, 0));

        main.addOrReplaceChild("shape20",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(107, 19)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(3, 13, -3, 0, 0, 0));

        main.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(83, 17)
                        .addBox(0, 0, 0, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.7F, 3, 0, 0, 0.7853982F));

        main.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(83, 17)
                        .addBox(0, 0, 0, 6, 6, 1),
                PartPose.offsetAndRotation(-3, 13, 3, 0, 0, 0));

        main.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(68, 0)
                        .addBox(0, 0, 0, 14, 15, 1),
                PartPose.offsetAndRotation(-7, 8, 7, 0, 0, 0));

        main.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 20)
                        .addBox(0, 0, 0, 14, 1, 1),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        main.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(48, 1)
                        .addBox(0, 0, 0, 1, 15, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        main.addOrReplaceChild("shape13a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        main.addOrReplaceChild("shape17a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(96, 23)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-3, 16, -2.8F, 0.7853982F, 0, 0));

        main.addOrReplaceChild("shape21a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-4, 16, -4.2F, 0.7853982F, 0, 0));

        main.addOrReplaceChild("shape16a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(96, 23)
                        .addBox(0, 0, 0, 1, 4, 4),
                PartPose.offsetAndRotation(-3, 14, -2, 0, 0, 0));

        main.addOrReplaceChild("shape20a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(101, 0)
                        .addBox(0, 0, 0, 1, 6, 6),
                PartPose.offsetAndRotation(-4, 13, -3, 0, 0, 0));

        main.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 0)
                        .addBox(0, 0, 0, 1, 9, 4),
                PartPose.offsetAndRotation(-6, 14, -2, 0, 0, 0));

        main.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(118, 0)
                        .addBox(0, 0, 0, 1, 9, 4),
                PartPose.offsetAndRotation(5, 14, -2, 0, 0, 0));

        main.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(18, 26)
                        .addBox(0, 0, 0, 10, 1, 4),
                PartPose.offsetAndRotation(-5, 22, -2, 0, 0, 0));

        main.addOrReplaceChild("shape12a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        main.addOrReplaceChild("shape12b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 27)
                        .addBox(0, 0, 0, 6, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 32);
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        main.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 1, 1, 1, 1);
    }

    @Override
    public ResourceLocation getTexture() {
        return TEXTURE_LOCATION;
    }

}
