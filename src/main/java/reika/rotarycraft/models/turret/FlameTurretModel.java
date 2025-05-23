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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

public class FlameTurretModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/turret");

    private final ModelPart shape12b2;
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
    private final ModelPart shape1a2b2;
    private final ModelPart shape1b2;
    private final ModelPart shape3da;
    private final ModelPart shape1b23;
    private final ModelPart shape1bb2;
    private final ModelPart shape1ab2;
    private final ModelPart shape1b;
    private final ModelPart shape1b22;
    private final ModelPart shape1a2;
    private final ModelPart shape1a2b;
    private final ModelPart shape12;
    private final ModelPart shape12b;
    private final ModelPart shape1;
    private final ModelPart shape1bb;
    private final ModelPart shape1a;
    private final ModelPart shape1ab;
    private final ModelPart shape6b;
    private final ModelPart root;

    public FlameTurretModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape12b2 = modelPart.getChild("shape12b2");
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
        this.shape1a2b2 = modelPart.getChild("shape1a2b2");
        this.shape1b2 = modelPart.getChild("shape1b2");
        this.shape3da = modelPart.getChild("shape3da");
        this.shape1b23 = modelPart.getChild("shape1b23");
        this.shape1bb2 = modelPart.getChild("shape1bb2");
        this.shape1ab2 = modelPart.getChild("shape1ab2");
        this.shape1b = modelPart.getChild("shape1b");
        this.shape1b22 = modelPart.getChild("shape1b22");
        this.shape1a2 = modelPart.getChild("shape1a2");
        this.shape1a2b = modelPart.getChild("shape1a2b");
        this.shape12 = modelPart.getChild("shape12");
        this.shape12b = modelPart.getChild("shape12b");
        this.shape1 = modelPart.getChild("shape1");
        this.shape1bb = modelPart.getChild("shape1bb");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape1ab = modelPart.getChild("shape1ab");
        this.shape6b = modelPart.getChild("shape6b");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape12b2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 96)
                        .addBox(-2.8F, 2, -12, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 16)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(43, 34)
                        .addBox(-5.5F, 0, -5.5F, 11, 2, 11),
                PartPose.offsetAndRotation(0, 20.96667F, 0, 0, 0, 0));

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
                        .addBox(-3, 0, 0, 1, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape8a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 98)
                        .addBox(2, 0, 0, 1, 1, 9),
                PartPose.offsetAndRotation(0, 20, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape9",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 109)
                        .addBox(-1, 0, 0, 2, 1, 7),
                PartPose.offsetAndRotation(0, 21, 4, 1.186824F, 0, 0));

        root.addOrReplaceChild("shape1a2b2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 96)
                        .addBox(-0.1F, 2, -12, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape1b2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 81)
                        .addBox(-1.5F, 1.65F, -13.1F, 3, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 3.141593F));

        root.addOrReplaceChild("shape3da",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 58)
                        .addBox(-4, -3.5F, 0, 8, 7, 3),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1b23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(57, 49)
                        .addBox(-1, 3, -13.1F, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -1.570796F));

        root.addOrReplaceChild("shape1bb2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 96)
                        .addBox(-2.8F, 2, -12, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape1ab2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 96)
                        .addBox(-0.1F, 2, -12, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape1b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(23, 99)
                        .addBox(-1.5F, 1.65F, -13.1F, 3, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0));

        root.addOrReplaceChild("shape1b22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 49)
                        .addBox(-1, 3, -13.1F, 2, 1, 14),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 1.570796F));

        root.addOrReplaceChild("shape1a2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 81)
                        .addBox(-0.1F, 2, -8, 3, 1, 8),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape1a2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 96)
                        .addBox(-0.1F, 2, -10, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 2.617994F));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 81)
                        .addBox(-2.8F, 2, -8, 3, 1, 8),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape12b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 96)
                        .addBox(-2.8F, 2, -10, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -2.617994F));

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 81)
                        .addBox(-2.8F, 2, -8, 3, 1, 8),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape1bb",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(35, 96)
                        .addBox(-2.8F, 2, -10, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, 0.5235988F));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 81)
                        .addBox(-0.1F, 2, -8, 3, 1, 8),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape1ab",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(70, 96)
                        .addBox(-0.1F, 2, -10, 3, 1, 1),
                PartPose.offsetAndRotation(0, 11.5F, 6, 0, 0, -0.5235988F));

        root.addOrReplaceChild("shape6b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(-5, 0, -5, 10, 2, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

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
