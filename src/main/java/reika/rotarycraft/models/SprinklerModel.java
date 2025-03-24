package reika.rotarycraft.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.renderer.RenderType;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class SprinklerModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape1g;
    private final ModelPart shape3;
    private final ModelPart shape3f;
    private final ModelPart shape3e;
    private final ModelPart shape3d;
    private final ModelPart shape1c;
    private final ModelPart shape1b;
    private final ModelPart shape2a;
    private final ModelPart root;

    public SprinklerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3f = modelPart.getChild("shape3f");
        this.shape3e = modelPart.getChild("shape3e");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape2a = modelPart.getChild("shape2a");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(87, 0)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(2, 16, -2, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(25, 0)
                        .addBox(0, 0, 0, 5, 2, 5),
                PartPose.offsetAndRotation(-2.5F, 9, -2.5F, 0, 0, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 6, 1, 6),
                PartPose.offsetAndRotation(-3, 8, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(99, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(1, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(99, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(1, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape3e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(99, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-2, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(99, 0)
                        .addBox(0, 0, 0, 1, 1, 1),
                PartPose.offsetAndRotation(-2, 15, 1, 0, 0, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 4, 1, 6),
                PartPose.offsetAndRotation(-2, 16, -3, 0, 0, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(87, 0)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(-3, 16, -2, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(47, 0)
                        .addBox(0, 0, 0, 4, 4, 4),
                PartPose.offsetAndRotation(-2, 11, -2, 0, 0, 0));

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
