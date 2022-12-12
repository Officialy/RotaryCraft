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

public class HarvesterModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/");

    private final ModelPart shape1;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape3;
    private final ModelPart shape3a;
    private final ModelPart shape4;
    private final ModelPart shape2an;
    private final ModelPart shape2ab1;
    private final ModelPart shape3ab1;
    private final ModelPart shape3ab;
    private final ModelPart shape3a1;
    private final ModelPart shape3a2;
    private final ModelPart shape2ab;
    private final ModelPart shape2aba;
    private final ModelPart shape5;
    private final ModelPart root;

    public HarvesterModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape4 = modelPart.getChild("shape4");
        this.shape2an = modelPart.getChild("shape2an");
        this.shape2ab1 = modelPart.getChild("shape2ab1");
        this.shape3ab1 = modelPart.getChild("shape3ab1");
        this.shape3ab = modelPart.getChild("shape3ab");
        this.shape3a1 = modelPart.getChild("shape3a1");
        this.shape3a2 = modelPart.getChild("shape3a2");
        this.shape2ab = modelPart.getChild("shape2ab");
        this.shape2aba = modelPart.getChild("shape2aba");
        this.shape5 = modelPart.getChild("shape5");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 14, 16),
                PartPose.offsetAndRotation(-8, 10, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 2, 16),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 1, 1, 12),
                PartPose.offsetAndRotation(5, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 14, 2, 1),
                PartPose.offsetAndRotation(-7, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 13)
                        .addBox(0, 0, 0, 10, 1, 1),
                PartPose.offsetAndRotation(-5, 8, 5, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 42)
                        .addBox(0, 0, 0, 12, 1, 12),
                PartPose.offsetAndRotation(-6, 9, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2an",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 1, 2, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2ab1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(3, 8, -3, 0, 0, 0));

        root.addOrReplaceChild("shape3ab1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 30)
                        .addBox(0, 0, 0, 14, 2, 1),
                PartPose.offsetAndRotation(-7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3ab",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 40)
                        .addBox(0, 0, 0, 8, 1, 1),
                PartPose.offsetAndRotation(-4, 8, 3, 0, 0, 0));

        root.addOrReplaceChild("shape3a1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 13)
                        .addBox(0, 0, 0, 10, 1, 1),
                PartPose.offsetAndRotation(-5, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape3a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 40)
                        .addBox(0, 0, 0, 8, 1, 1),
                PartPose.offsetAndRotation(-4, 8, -4, 0, 0, 0));

        root.addOrReplaceChild("shape2ab",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(98, 0)
                        .addBox(0, 0, 0, 1, 1, 12),
                PartPose.offsetAndRotation(-6, 8, -6, 0, 0, 0));

        root.addOrReplaceChild("shape2aba",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 1, 1, 6),
                PartPose.offsetAndRotation(-4, 8, -3, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(14, 33)
                        .addBox(0, 0, 0, 4, 1, 4),
                PartPose.offsetAndRotation(-2, 8, -2, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
