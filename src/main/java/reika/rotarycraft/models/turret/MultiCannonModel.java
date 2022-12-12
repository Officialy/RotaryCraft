package reika.rotarycraft.models.turret;

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

public class MultiCannonModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/turret");

    private final ModelPart shape10;
    private final ModelPart shape3;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape7a;
    private final ModelPart shape7b;
    private final ModelPart shape7c;
    private final ModelPart shape7d;
    private final ModelPart shape7e;
    private final ModelPart shape7f;
    private final ModelPart shape7g;
    private final ModelPart shape8;
    private final ModelPart shape8a;
    private final ModelPart shape9;
    private final ModelPart shape3d;
    private final ModelPart shape1a;
    private final ModelPart shape10a;
    private final ModelPart shape10b;
    private final ModelPart shape10c;
    private final ModelPart shape1a0;
    private final ModelPart shape1a1;
    private final ModelPart shape1a2;
    private final ModelPart shape1a3;
    private final ModelPart shape1a4;
    private final ModelPart shape10d;
    private final ModelPart shape10e;
    private final ModelPart shape10f;
    private final ModelPart shape10g;
    private final ModelPart shape10h;
    private final ModelPart shape10i;
    private final ModelPart shape10j;
    private final ModelPart shape10k;
    private final ModelPart root;

    public MultiCannonModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape10 = modelPart.getChild("shape10");
        this.shape3 = modelPart.getChild("shape3");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape7a = modelPart.getChild("shape7a");
        this.shape7b = modelPart.getChild("shape7b");
        this.shape7c = modelPart.getChild("shape7c");
        this.shape7d = modelPart.getChild("shape7d");
        this.shape7e = modelPart.getChild("shape7e");
        this.shape7f = modelPart.getChild("shape7f");
        this.shape7g = modelPart.getChild("shape7g");
        this.shape8 = modelPart.getChild("shape8");
        this.shape8a = modelPart.getChild("shape8a");
        this.shape9 = modelPart.getChild("shape9");
        this.shape3d = modelPart.getChild("shape3d");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape10a = modelPart.getChild("shape10a");
        this.shape10b = modelPart.getChild("shape10b");
        this.shape10c = modelPart.getChild("shape10c");
        this.shape1a0 = modelPart.getChild("shape1a0");
        this.shape1a1 = modelPart.getChild("shape1a1");
        this.shape1a2 = modelPart.getChild("shape1a2");
        this.shape1a3 = modelPart.getChild("shape1a3");
        this.shape1a4 = modelPart.getChild("shape1a4");
        this.shape10d = modelPart.getChild("shape10d");
        this.shape10e = modelPart.getChild("shape10e");
        this.shape10f = modelPart.getChild("shape10f");
        this.shape10g = modelPart.getChild("shape10g");
        this.shape10h = modelPart.getChild("shape10h");
        this.shape10i = modelPart.getChild("shape10i");
        this.shape10j = modelPart.getChild("shape10j");
        this.shape10k = modelPart.getChild("shape10k");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape10",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 57)
                        .addBox(-4, -2.5F, 2, 8, 5, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 16)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(-5, 0, -5, 10, 2, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape7a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape7b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape7c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.1963495F, 0));

        root.addOrReplaceChild("shape7d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, -0.1963495F, 0));

        root.addOrReplaceChild("shape7e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.9817477F, 0));

        root.addOrReplaceChild("shape7f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0.5890486F, 0));

        root.addOrReplaceChild("shape7g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-6, 0, -6, 12, 1, 12),
                PartPose.offsetAndRotation(0, 22, 0, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(-4, 0, 0, 2, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(2, 0, 0, 2, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 109)
                        .addBox(-2, 0, 0, 4, 1, 7),
                PartPose.offsetAndRotation(0, 21, 4, 1.186824F, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 47)
                        .addBox(-5, -3.5F, 0, 10, 7, 2),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-0.5F, -3, -14, 1, 1, 13),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -2.094395F));

        root.addOrReplaceChild("shape10a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape10b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 72)
                        .addBox(-2.5F, -2.5F, -1, 5, 5, 1),
                PartPose.offsetAndRotation(0, 11.5F, -5, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape10c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 2, 0, 0, 1.178097F));

        root.addOrReplaceChild("shape1a0",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-0.5F, -3, -14, 1, 1, 13),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1a1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-0.5F, -3, -14, 1, 1, 13),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 1.047198F));

        root.addOrReplaceChild("shape1a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-0.5F, -3, -14, 1, 1, 13),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 2.094395F));

        root.addOrReplaceChild("shape1a3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-0.5F, -3, -14, 1, 1, 13),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape1a4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 64)
                        .addBox(-0.5F, -3, -14, 1, 1, 13),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -1.047198F));

        root.addOrReplaceChild("shape10d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 72)
                        .addBox(-2.5F, -2.5F, -1, 5, 5, 1),
                PartPose.offsetAndRotation(0, 11.5F, -5, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 72)
                        .addBox(-2.5F, -2.5F, -1, 5, 5, 1),
                PartPose.offsetAndRotation(0, 11.5F, -5, 0, 0, 0));

        root.addOrReplaceChild("shape10g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 72)
                        .addBox(-2.5F, -2.5F, -1, 5, 5, 1),
                PartPose.offsetAndRotation(0, 11.5F, -5, 0, 0, 0.3926991F));

        root.addOrReplaceChild("shape10h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape10i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 2, 0, 0, 0));

        root.addOrReplaceChild("shape10j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 2, 0, 0, 0.7853982F));

        root.addOrReplaceChild("shape10k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 64)
                        .addBox(-3, -3, -1, 6, 6, 1),
                PartPose.offsetAndRotation(0, 11.5F, 2, 0, 0, 0.3926991F));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
