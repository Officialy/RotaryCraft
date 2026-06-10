package reika.rotarycraft.models.engine;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.model.Model;
import net.minecraft.client.model.geom.ModelPart;
import net.minecraft.client.model.geom.PartPose;
import net.minecraft.client.model.geom.builders.CubeListBuilder;
import net.minecraft.client.model.geom.builders.LayerDefinition;
import net.minecraft.client.model.geom.builders.MeshDefinition;
import net.minecraft.client.model.geom.builders.PartDefinition;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class CombustionModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/engine/combtex.png");

    private final ModelPart shape1;
    private final ModelPart shape12;
    private final ModelPart shape13;
    private final ModelPart shape5;
    private final ModelPart shape6;
    private final ModelPart shape7;
    private final ModelPart shape3;
    private final ModelPart shape8;
    private final ModelPart shape4;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public CombustionModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

        this.shape1 = modelPart.getChild("shape1");
        this.shape12 = modelPart.getChild("shape12");
        this.shape13 = modelPart.getChild("shape13");
        this.shape5 = modelPart.getChild("shape5");
        this.shape6 = modelPart.getChild("shape6");
        this.shape7 = modelPart.getChild("shape7");
        this.shape3 = modelPart.getChild("shape3");
        this.shape8 = modelPart.getChild("shape8");
        this.shape4 = modelPart.getChild("shape4");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(64, 0)
                        .addBox(0, 0, 0, 15, 1, 15),
                PartPose.offsetAndRotation(-7.5F, 22, -7.5F, 0, 0, 0));

        root.addOrReplaceChild("shape12",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, 0, 11, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 15, -1, 0, 0, 0));

        root.addOrReplaceChild("shape13",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 80)
                        .addBox(0, 0, 0, 11, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, -1.4F, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape5",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 12, 11, 6),
                PartPose.offsetAndRotation(-6, 11, 3, -0.5235988F, 0, 0));

        root.addOrReplaceChild("shape6",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 17)
                        .addBox(0, 0, 0, 13, 12, 6),
                PartPose.offsetAndRotation(-6.5F, 11, -3, 0, 0, 0));

        root.addOrReplaceChild("shape7",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 33)
                        .addBox(0, 0, 0, 12, 11, 6),
                PartPose.offsetAndRotation(-6, 14, -8, 0.5235988F, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 17)
                        .addBox(0, 0, 0, 14, 2, 14),
                PartPose.offsetAndRotation(-7, 21, -7, 0, 0, 0));

        root.addOrReplaceChild("shape8",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        root.addOrReplaceChild("shape4",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 53)
                        .addBox(0, 0, 0, 12, 3, 3),
                PartPose.offsetAndRotation(-5.9F, 11, -2, 0.7853982F, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    /**
     * Mirrors original 1.7 {@code ModelCombustion.renderAll}.
     * shape12/13 = crankshaft, rotate around X at pivot y=1 by phi. All other shapes are
     * static housing / intake / exhaust geometry.
     */
    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        final int LM = packedLightIn;
        final int OV = OverlayTexture.NO_OVERLAY;
        final int COL = 0xFFFFFFFF;
        shape1.render(stack, tex, LM, OV, COL);

        // Crank rotation: pivot at y=1, X-axis rotation by phi.
        stack.pushPose();
        stack.translate(0.0, 1.0, 0.0);
        stack.mulPose(com.mojang.math.Axis.XP.rotationDegrees(phi));
        stack.translate(0.0, -1.0, 0.0);
        shape12.render(stack, tex, LM, OV, COL);
        shape13.render(stack, tex, LM, OV, COL);
        stack.popPose();

        shape5.render(stack, tex, LM, OV, COL);
        shape6.render(stack, tex, LM, OV, COL);
        shape7.render(stack, tex, LM, OV, COL);
        shape3.render(stack, tex, LM, OV, COL);
        shape8.render(stack, tex, LM, OV, COL);
        shape4.render(stack, tex, LM, OV, COL);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

