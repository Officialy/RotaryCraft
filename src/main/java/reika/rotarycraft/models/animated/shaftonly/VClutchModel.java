package reika.rotarycraft.models.animated.shaftonly;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.resources.ResourceLocation;

import static reika.rotarycraft.RotaryCraft.MODID;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

public class VClutchModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/transmission/shaft/");

    private final ModelPart shape14b;
    private final ModelPart shape15b;
    private final ModelPart shape15c;
    private final ModelPart shape14;
    private final ModelPart shape1;
    private final ModelPart shape15d;
    private final ModelPart shape15;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1g;
    private final ModelPart shape1f;
    private final ModelPart shape1e;
    private final ModelPart shape2;
    private final ModelPart shape2b;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart root;

    public VClutchModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape14b = modelPart.getChild("shape14b");
        this.shape15b = modelPart.getChild("shape15b");
        this.shape15c = modelPart.getChild("shape15c");
        this.shape14 = modelPart.getChild("shape14");
        this.shape1 = modelPart.getChild("shape1");
        this.shape15d = modelPart.getChild("shape15d");
        this.shape15 = modelPart.getChild("shape15");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape14b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 3)
                        .addBox(0, 0, 0, 14, 1, 10),
                PartPose.offsetAndRotation(-7, 8, -5, 0, 0, 0));

        root.addOrReplaceChild("shape15b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(15, 1)
                        .addBox(0, 0, 0, 14, 1, 2),
                PartPose.offsetAndRotation(-7, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape15c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(15, 13)
                        .addBox(0, 0, 0, 14, 1, 2),
                PartPose.offsetAndRotation(-7, 8, -7, 0, 0, 0));

        root.addOrReplaceChild("shape14",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 3)
                        .addBox(0, 0, 0, 14, 1, 10),
                PartPose.offsetAndRotation(-7, 23, -5, 0, 0, 0));

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-6, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape15d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(15, 13)
                        .addBox(0, 0, 0, 14, 1, 2),
                PartPose.offsetAndRotation(-7, 23, -7, 0, 0, 0));

        root.addOrReplaceChild("shape15",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(15, 1)
                        .addBox(0, 0, 0, 14, 1, 2),
                PartPose.offsetAndRotation(-7, 23, 5, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(5, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-6, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(5, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(7, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(62, 0)
                        .addBox(0, 0, 0, 1, 16, 1),
                PartPose.offsetAndRotation(-8, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(115, 0)
                        .addBox(0, 0, 0, 2, 17, 2),
                PartPose.offsetAndRotation(-1.4F, 7.5F, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(115, 0)
                        .addBox(0, 0, 0, 2, 17, 2),
                PartPose.offsetAndRotation(-1, 7.5F, -1, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(102, 0)
                        .addBox(0, 0, 0, 6, 10, 6),
                PartPose.offsetAndRotation(-3, 11, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 28)
                        .addBox(0, 0, -1, 10, 2, 2),
                PartPose.offsetAndRotation(0, 15, 0, 0, -2.356194F, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 28)
                        .addBox(0, 0, -1, 10, 2, 2),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 28)
                        .addBox(0, 0, -1, 10, 2, 2),
                PartPose.offsetAndRotation(0, 15, 0, 0, -0.7853982F, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 28)
                        .addBox(0, 0, -1, 10, 2, 2),
                PartPose.offsetAndRotation(0, 15, 0, 0, 2.356194F, 0));

        return LayerDefinition.create(definition, 128, 32);
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
