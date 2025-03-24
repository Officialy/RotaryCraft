package reika.rotarycraft.modinterface.model;

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

public class CompressorModel extends RotaryModelBase {

    public static final ResourceLocation TEXTURE_LOCATION = ResourceLocation.fromNamespaceAndPath(MODID, "textures/blockentitytex/modinterface/");

    private final ModelPart ring1;
    private final ModelPart base;
    private final ModelPart ring2;
    private final ModelPart ring3;
    private final ModelPart ring;
    private final ModelPart core;
    private final ModelPart jacket;
    private final ModelPart jacket2;
    private final ModelPart jacket1;
    private final ModelPart jacket3;
    private final ModelPart root;

    public CompressorModel(ModelPart modelPart) {
        super(RenderType::entityCutout);
        this.root = modelPart;

        this.ring1 = modelPart.getChild("ring1");
        this.base = modelPart.getChild("base");
        this.ring2 = modelPart.getChild("ring2");
        this.ring3 = modelPart.getChild("ring3");
        this.ring = modelPart.getChild("ring");
        this.core = modelPart.getChild("core");
        this.jacket = modelPart.getChild("jacket");
        this.jacket2 = modelPart.getChild("jacket2");
        this.jacket1 = modelPart.getChild("jacket1");
        this.jacket3 = modelPart.getChild("jacket3");
    }

    // Grab the parts in the constructor if you need them

    public static LayerDefinition createLayer() {
        MeshDefinition definition = new MeshDefinition();
        PartDefinition root = definition.getRoot();

        root.addOrReplaceChild("ring1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(75, 21)
                        .addBox(0, 0, 0, 4, 4, 16),
                PartPose.offsetAndRotation(4, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("base",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 21)
                        .addBox(0, 0, 0, 16, 4, 16),
                PartPose.offsetAndRotation(-8, 20, -8, 0, 0, 0));

        root.addOrReplaceChild("ring2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 53)
                        .addBox(0, 0, 0, 8, 4, 4),
                PartPose.offsetAndRotation(-4, 16, 4, 0, 0, 0));

        root.addOrReplaceChild("ring3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(75, 0)
                        .addBox(0, 0, 0, 4, 4, 16),
                PartPose.offsetAndRotation(-8, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("ring",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 43)
                        .addBox(0, 0, 0, 8, 4, 4),
                PartPose.offsetAndRotation(-4, 16, -8, 0, 0, 0));

        root.addOrReplaceChild("core",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(41, 0)
                        .addBox(0, 0, 0, 8, 12, 8),
                PartPose.offsetAndRotation(-4, 8, -4, 0, 0, 0));

        root.addOrReplaceChild("jacket",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 72)
                        .addBox(0, 0, 0, 10, 8, 1),
                PartPose.offsetAndRotation(-5, 12, 4, 0, 0, 0));

        root.addOrReplaceChild("jacket2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(24, 70)
                        .addBox(0, 0, 0, 1, 8, 8),
                PartPose.offsetAndRotation(4, 12, -4, 0, 0, 0));

        root.addOrReplaceChild("jacket1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 62)
                        .addBox(0, 0, 0, 10, 8, 1),
                PartPose.offsetAndRotation(-5, 12, -5, 0, 0, 0));

        root.addOrReplaceChild("jacket3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(27, 47)
                        .addBox(0, 0, 0, 1, 8, 8),
                PartPose.offsetAndRotation(-5, 12, -4, 0, 0, 0));

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
