package reika.rotarycraft.models;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.client.renderer.RenderType;

import static reika.rotarycraft.RotaryCraft.MODID;

public class SolarTowerModel extends Model {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/solartex.png");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape1b;
    private final ModelPart shape1c;
    private final ModelPart shape1d;
    private final ModelPart shape1e;
    private final ModelPart shape1f;
    private final ModelPart shape1g;
    private final ModelPart shape1h;
    private final ModelPart shape1i;
    private final ModelPart shape1j;
    private final ModelPart shape1k;
    private final ModelPart shape1l;
    private final ModelPart shape1m;
    private final ModelPart shape1n;
    private final ModelPart shape1o;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart root;

    public SolarTowerModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1c = modelPart.getChild("shape1c");
        this.shape1d = modelPart.getChild("shape1d");
        this.shape1e = modelPart.getChild("shape1e");
        this.shape1f = modelPart.getChild("shape1f");
        this.shape1g = modelPart.getChild("shape1g");
        this.shape1h = modelPart.getChild("shape1h");
        this.shape1i = modelPart.getChild("shape1i");
        this.shape1j = modelPart.getChild("shape1j");
        this.shape1k = modelPart.getChild("shape1k");
        this.shape1l = modelPart.getChild("shape1l");
        this.shape1m = modelPart.getChild("shape1m");
        this.shape1n = modelPart.getChild("shape1n");
        this.shape1o = modelPart.getChild("shape1o");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, -4, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, 4, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, 4, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, -4, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape1d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, 4, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape1e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, 4, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape1f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, -4, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape1g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, -4, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape1h",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, -4, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape1i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, -4, 0, 0, 0));

        root.addOrReplaceChild("shape1j",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, -4, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape1k",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, -4, 0, 0, 0));

        root.addOrReplaceChild("shape1l",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, 4, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape1m",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(65, 0)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(4, 9, 4, 0, 0, 0));

        root.addOrReplaceChild("shape1n",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, 4, 0, 0, 0));

        root.addOrReplaceChild("shape1o",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(32, 32)
                        .addBox(-3, 0, -3, 6, 14, 6),
                PartPose.offsetAndRotation(-4, 9, 4, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 55)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 55)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    @Override
    public void renderToBuffer(PoseStack stack, VertexConsumer bufferIn, int packedLightIn, int packedOverlayIn, float red, float green, float blue, float alpha) {

        root.render(stack, bufferIn, packedLightIn, packedOverlayIn, red, green, blue, alpha);
    }
}
