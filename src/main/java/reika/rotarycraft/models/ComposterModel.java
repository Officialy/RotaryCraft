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

public class ComposterModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape6;
    private final ModelPart root;

    public ComposterModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
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

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 1, 4, 16),
                PartPose.offsetAndRotation(7, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 1, 4, 16),
                PartPose.offsetAndRotation(-8, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 40)
                        .addBox(0, 0, 0, 14, 4, 1),
                PartPose.offsetAndRotation(-7, 19, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 63)
                        .addBox(0, -8, -1, 16, 8, 1),
                PartPose.offsetAndRotation(-8, 19, 8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 53)
                        .addBox(0, -8, 0, 16, 8, 1),
                PartPose.offsetAndRotation(-8, 19, -8, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(-1, -8, 0, 1, 8, 16),
                PartPose.offsetAndRotation(8, 19, -8, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 73)
                        .addBox(0, -8, 0, 1, 8, 16),
                PartPose.offsetAndRotation(-8, 19, -8, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(63, 22)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 15, 0, 0, 0, 0));

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
