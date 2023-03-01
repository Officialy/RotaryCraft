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

public class DisplayModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape4b;
    private final ModelPart shape4c;
    private final ModelPart shape5;
    private final ModelPart root;

    public DisplayModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape4b = modelPart.getChild("shape4b");
        this.shape4c = modelPart.getChild("shape4c");
        this.shape5 = modelPart.getChild("shape5");
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
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 6, 1, 6),
                PartPose.offsetAndRotation(-3, 22, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 18)
                        .addBox(0, 0, 0, 6, 1, 2),
                PartPose.offsetAndRotation(-3, 10, -1, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 53)
                        .addBox(0, 0, 0, 16, 1, 1),
                PartPose.offsetAndRotation(-8, 8, -0.5F, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(0, 0, 0, 12, 1, 2),
                PartPose.offsetAndRotation(-6, 9, -1, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(7, 18)
                        .addBox(0, 0, 0, 1, 11, 2),
                PartPose.offsetAndRotation(2, 11, -1, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(5, 36)
                        .addBox(0, 0, 0, 1, 15, 1),
                PartPose.offsetAndRotation(7, 8.6F, -0.5F, 0, 0, 0.3490659F));

        root.addOrReplaceChild("shape4b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 18)
                        .addBox(0, 0, 0, 1, 11, 2),
                PartPose.offsetAndRotation(-3, 11, -1, 0, 0, 0));

        root.addOrReplaceChild("shape4c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 36)
                        .addBox(0, 0, 0, 1, 15, 1),
                PartPose.offsetAndRotation(-8, 9, -0.5F, 0, 0, -0.3490659F));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(10, 36)
                        .addBox(0, 0, 0, 4, 11, 1),
                PartPose.offsetAndRotation(-2, 11, -0.5F, 0, 0, 0));

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
