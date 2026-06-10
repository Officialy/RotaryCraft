package reika.rotarycraft.models;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;
import net.minecraft.client.renderer.texture.OverlayTexture;
import java.util.ArrayList;

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
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryModelBase;

import java.util.ArrayList;

import static reika.rotarycraft.RotaryCraft.MODID;

public class ReservoirModel extends RotaryModelBase {

    public static final Identifier TEXTURE_LOCATION = Identifier.fromNamespaceAndPath(MODID, "textures/blockentitytex/reservoirtex.png");

    private final ModelPart mx;
    private final ModelPart mz;
    private final ModelPart pz;
    private final ModelPart px;
    private final ModelPart bottom;
    // 1.21.5: Model already declares a protected `root`; removed shadowing field.

    public ReservoirModel(ModelPart modelPart) {
        super(modelPart, RenderTypes::entityCutout);

        this.mx = modelPart.getChild("mx");
        this.mz = modelPart.getChild("mz");
        this.pz = modelPart.getChild("pz");
        this.px = modelPart.getChild("px");
        this.bottom = modelPart.getChild("bottom");
    }


    public static LayerDefinition createLayer() {
        var definition = new MeshDefinition();
        var root = definition.getRoot();

        root.addOrReplaceChild("mx",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 0)
                        .addBox(0, 0, 0, 1, 16, 16),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("mz",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(56, 0)
                        .addBox(0, 0, 0, 1, 16, 16),
                PartPose.offsetAndRotation(7, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("pz",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 16, 1),
                PartPose.offsetAndRotation(-8, 8, 7, 0, 0, 0));

        root.addOrReplaceChild("px",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 0)
                        .addBox(0, 0, 0, 16, 16, 1),
                PartPose.offsetAndRotation(-8, 8, -8, 0, 0, 0));

        root.addOrReplaceChild("bottom",
                CubeListBuilder.create()
                        .mirror(true)
                        .texOffs(0, 46)
                        .addBox(0, 0, 0, 16, 1, 16),
                PartPose.offsetAndRotation(-8, 23, -8, 0, 0, 0));

        return LayerDefinition.create(definition, 128, 128);
    }

    public void renderSide(PoseStack stack, VertexConsumer tex, int packedLightIn, Direction dir) {
        switch (dir) {
            case DOWN -> bottom.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            case WEST -> px.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            case SOUTH -> mz.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            case EAST -> pz.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            case NORTH -> mx.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
            default -> {
            }
        }
    }

    @Override
    public void renderAll(PoseStack stack, VertexConsumer tex, int packedLightIn, BlockEntity te, ArrayList<?> conditions, float phi, float theta) {
        mx.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        mz.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        pz.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        px.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
        bottom.render(stack, tex, packedLightIn, OverlayTexture.NO_OVERLAY, 0xFFFFFFFF);
    }

    @Override
    public Identifier getTexture() {
        return TEXTURE_LOCATION;
    }
}

