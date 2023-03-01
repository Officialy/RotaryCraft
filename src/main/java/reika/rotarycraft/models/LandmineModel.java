package reika.rotarycraft.models;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import net.minecraft.client.renderer.texture.OverlayTexture;
import java.util.ArrayList;

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

public class LandmineModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = new ResourceLocation(MODID, "textures/blockentitytex/landminetex.png");

    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape2d;
    private final ModelPart shape2e;
    private final ModelPart shape2f;
    private final ModelPart shape2g;
    private final ModelPart shape2i;
    private final ModelPart shape21;
    private final ModelPart shape22;
    private final ModelPart shape23;
    private final ModelPart root;

    public LandmineModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape2d = modelPart.getChild("shape2d");
        this.shape2e = modelPart.getChild("shape2e");
        this.shape2f = modelPart.getChild("shape2f");
        this.shape2g = modelPart.getChild("shape2g");
        this.shape2i = modelPart.getChild("shape2i");
        this.shape21 = modelPart.getChild("shape21");
        this.shape22 = modelPart.getChild("shape22");
        this.shape23 = modelPart.getChild("shape23");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 4, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 4, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-2, 0, -2, 4, 1, 4),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 4, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(-5, 0, -5, 10, 4, 10),
                PartPose.offsetAndRotation(0, 20, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2e",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 15)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2f",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 15)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0.3926991F, 0));

        root.addOrReplaceChild("shape2g",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 15)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape2i",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 15)
                        .addBox(-4, 0, -4, 8, 2, 8),
                PartPose.offsetAndRotation(0, 18, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape21",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-2, 0, -2, 4, 1, 4),
                PartPose.offsetAndRotation(0, 17, 0, 0, 1.178097F, 0));

        root.addOrReplaceChild("shape22",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-2, 0, -2, 4, 1, 4),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0.7853982F, 0));

        root.addOrReplaceChild("shape23",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 31)
                        .addBox(-2, 0, -2, 4, 1, 4),
                PartPose.offsetAndRotation(0, 17, 0, 0, 0.3926991F, 0));

        return LayerDefinition.create(definition, 64, 64);
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
