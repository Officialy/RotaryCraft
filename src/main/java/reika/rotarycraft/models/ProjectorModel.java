package reika.rotarycraft.models;

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

public class ProjectorModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape4;
    private final ModelPart shape4a;
    private final ModelPart shape3b1;
    private final ModelPart shape3b2;
    private final ModelPart shape4a1;
    private final ModelPart shape4a2;
    private final ModelPart shape2;
    private final ModelPart shape3;
    private final ModelPart shape5;
    private final ModelPart root;

    public ProjectorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape4 = modelPart.getChild("shape4");
        this.shape4a = modelPart.getChild("shape4a");
        this.shape3b1 = modelPart.getChild("shape3b1");
        this.shape3b2 = modelPart.getChild("shape3b2");
        this.shape4a1 = modelPart.getChild("shape4a1");
        this.shape4a2 = modelPart.getChild("shape4a2");
        this.shape2 = modelPart.getChild("shape2");
        this.shape3 = modelPart.getChild("shape3");
        this.shape5 = modelPart.getChild("shape5");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(7, 18, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(6, 19, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, -2, 0, 0, 0));

        root.addOrReplaceChild("shape4a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(6, 15, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3b1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 4),
                PartPose.offsetAndRotation(7, 15, -2, 0, 0, 0));

        root.addOrReplaceChild("shape3b2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(6, 14, -3, 0, 0, 0));

        root.addOrReplaceChild("shape4a1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 2, 1),
                PartPose.offsetAndRotation(7, 16, 1, 0, 0, 0));

        root.addOrReplaceChild("shape4a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 1, 4, 1),
                PartPose.offsetAndRotation(6, 15, 2, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(0, 0, 0, 13, 11, 8),
                PartPose.offsetAndRotation(-7, 12, -4, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 37)
                        .addBox(0, 0, 0, 13, 1, 6),
                PartPose.offsetAndRotation(-7, 11, -3, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(16, 0)
                        .addBox(0, 0, 0, 1, 12, 6),
                PartPose.offsetAndRotation(-8, 11, -3, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
