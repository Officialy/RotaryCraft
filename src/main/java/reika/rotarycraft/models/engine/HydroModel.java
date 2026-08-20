package reika.rotarycraft.models.engine;

import com.mojang.math.Axis;

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
import net.minecraft.resources.Identifier;

import static reika.rotarycraft.RotaryCraft.MODID;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import java.util.ArrayList;

public class HydroModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/engine/hydrotex.png");
    public static final Identifier BEDROCK_TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/engine/bedhydrotex.png");

    private final ModelPart shape1;
    private final ModelPart shape1a;
    private final ModelPart shape2;
    private final ModelPart shape2a;
    private final ModelPart shape2b;
    private final ModelPart shape2c;
    private final ModelPart shape3;
    private final ModelPart shape3d;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public HydroModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

        this.shape1 = modelPart.getChild("shape1");
        this.shape1a = modelPart.getChild("shape1a");
        this.shape2 = modelPart.getChild("shape2");
        this.shape2a = modelPart.getChild("shape2a");
        this.shape2b = modelPart.getChild("shape2b");
        this.shape2c = modelPart.getChild("shape2c");
        this.shape3 = modelPart.getChild("shape3");
        this.shape3d = modelPart.getChild("shape3d");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("shape1",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -1, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape1a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, -1, -1, 17, 2, 2),
                PartPose.offsetAndRotation(-8.5F, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape2",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, -0.5F, -16, 14, 1, 32),
                PartPose.offsetAndRotation(-7, 16, 0, 1.570796F, 0, 0));

        root.addOrReplaceChild("shape2a",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, -0.5F, -16, 14, 1, 32),
                PartPose.offsetAndRotation(-7, 16, 0, 2.356194F, 0, 0));

        root.addOrReplaceChild("shape2b",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, -0.5F, -16, 14, 1, 32),
                PartPose.offsetAndRotation(-7, 16, 0, 0.7853982F, 0, 0));

        root.addOrReplaceChild("shape2c",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 34)
                        .addBox(0, -0.5F, -16, 14, 1, 32),
                PartPose.offsetAndRotation(-7, 16, 0, 0, 0, 0));

        root.addOrReplaceChild("shape3",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(75, 0)
                        .addBox(0, 0, 0, 1, 16, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("shape3d",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(40, 0)
                        .addBox(0, 0, 0, 1, 16, 16),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

        @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int light, BlockEntity te,
                          ArrayList<?> conditions, float phi, float theta) {
        boolean fail = (Boolean)conditions.get(0);
        boolean bedrock = (Boolean)conditions.get(1);

        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(phi));
        stack.translate(0, -1, 0);
        shape1.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape1a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);

        double d = 0.1875;
        if (fail)
            stack.translate(0, d, 0);
        shape2.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2a.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2b.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape2c.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        if (fail)
            stack.translate(0, -d, 0);
        stack.translate(0, 1, 0);
        stack.mulPose(Axis.XP.rotationDegrees(-phi));
        stack.translate(0, -1, 0);

        shape3.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        shape3d.render(stack, tex, light, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

