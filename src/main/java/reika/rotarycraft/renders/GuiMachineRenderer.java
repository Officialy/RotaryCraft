package reika.rotarycraft.renders;

import com.mojang.blaze3d.platform.Lighting;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.render.pip.PictureInPictureRenderer;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import reika.rotarycraft.base.RotaryModelBase;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.EnumMap;
import java.util.Map;

/**
 * Renders a machine's {@link RotaryModelBase} into a picture-in-picture texture for
 * GUI display (the handbook). Replaces the legacy direct
 * {@code TileEntityRendererDispatcher.renderTileEntityAt} call, which cannot run
 * inside the 26.1 deferred GUI pipeline.
 */
public class GuiMachineRenderer extends PictureInPictureRenderer<GuiMachineRenderState> {

    private final Map<MachineRegistry, RotaryModelBase> models = new EnumMap<>(MachineRegistry.class);

    // 26.2: PictureInPictureRenderer no longer takes a BufferSource in ctor.
    public GuiMachineRenderer() {
        super();
    }

    @Override
    public Class<GuiMachineRenderState> getRenderStateClass() {
        return GuiMachineRenderState.class;
    }

    @Override
    protected void renderToTexture(GuiMachineRenderState state, PoseStack poseStack, SubmitNodeCollector collector) {
        RotaryModelBase model = models.computeIfAbsent(state.machine(),
                m -> m.getModel().apply(Minecraft.getInstance().getEntityModels()));
        if (model == null)
            return;
        Minecraft.getInstance().gameRenderer.lighting().setupFor(Lighting.Entry.ENTITY_IN_UI);
        poseStack.mulPose(Axis.XP.rotationDegrees(state.pitch()));
        poseStack.mulPose(Axis.YP.rotationDegrees(state.yaw()));
        poseStack.translate(0.0F, 1.0F, 0.0F);
        poseStack.mulPose(Axis.ZP.rotationDegrees(180));
        // 26.2: obtain VertexConsumer from the collector's RenderType (no direct bufferSource access).
        // We still use the RenderType to select the pipeline; submitCustomGeometry gives us the VC.
        RenderType rt = RenderTypes.entitySolid(model.getTexture());
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            model.renderAll(poseStack, vc, LightCoordsUtil.FULL_BRIGHT, state.blockEntity(), state.conditions(), state.phi(), 0);
        });
    }

    @Override
    protected float getTranslateY(int height, int guiScale) {
        return height / 2.0F;
    }

    @Override
    protected String getTextureLabel() {
        return "rotarycraft_machine";
    }
}
