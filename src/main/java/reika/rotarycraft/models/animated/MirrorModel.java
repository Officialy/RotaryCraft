package reika.rotarycraft.models.animated;

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

public class MirrorModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/animated");

    private final ModelPart shape1;
    private final ModelPart shape3;
    private final ModelPart shape4;
    private final ModelPart shape5;
    private final ModelPart shape5a;
    private final ModelPart shape5b;
    private final ModelPart shape5c;
    private final ModelPart shape5d;
    private final ModelPart shape3a;
    private final ModelPart shape3b;
    private final ModelPart shape3c;
    private final ModelPart shape3d;
    private final ModelPart shape3as;
    private final ModelPart shape5af;
    private final ModelPart shape5as;
    private final ModelPart root;

    public MirrorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape3 = modelPart.getChild("shape3");
        this.shape4 = modelPart.getChild("shape4");
        this.shape5 = modelPart.getChild("shape5");
        this.shape5a = modelPart.getChild("shape5a");
        this.shape5b = modelPart.getChild("shape5b");
        this.shape5c = modelPart.getChild("shape5c");
        this.shape5d = modelPart.getChild("shape5d");
        this.shape3a = modelPart.getChild("shape3a");
        this.shape3b = modelPart.getChild("shape3b");
        this.shape3c = modelPart.getChild("shape3c");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape3as = modelPart.getChild("shape3as");
        this.shape5af = modelPart.getChild("shape5af");
        this.shape5as = modelPart.getChild("shape5as");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 71)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.5235988F, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 19)
                        .addBox(-1, -4.1F, -4, 2, 9, 1),
                PartPose.offsetAndRotation(0, 23, 0, -1.047198F, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 0)
                        .addBox(-8, 6.1F, 5.3F, 16, 3, 1),
                PartPose.offsetAndRotation(0, 9, 0, -0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(4, 3, 1.5F, 2, 11, 1),
                PartPose.offsetAndRotation(0, 9, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 11)
                        .addBox(-8, 3, 2.1F, 16, 3, 1),
                PartPose.offsetAndRotation(0, 9, 0, -0.2617994F, 0, 0));

        root.addOrReplaceChild("shape5c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 5)
                        .addBox(-8, 4.7F, 3.5F, 16, 3, 1),
                PartPose.offsetAndRotation(0, 9, 0, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape5d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(66, 17)
                        .addBox(-8, 0.6F, 1.3F, 16, 3, 1),
                PartPose.offsetAndRotation(0, 9, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 22, 0, 0, 1.047198F, 0));

        root.addOrReplaceChild("shape3c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 22, 0, 0, 1.308997F, 0));

        root.addOrReplaceChild("shape3as",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 1, 10),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.2617994F, 0));

        root.addOrReplaceChild("shape5af",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-6, 3, 1.5F, 2, 11, 1),
                PartPose.offsetAndRotation(0, 9, 0, 0, 0, 0));

        root.addOrReplaceChild("shape5as",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 44)
                        .addBox(-1, 3, 1.5F, 2, 11, 1),
                PartPose.offsetAndRotation(0, 9, 0, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
