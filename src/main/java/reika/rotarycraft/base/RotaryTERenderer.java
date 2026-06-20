package reika.rotarycraft.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.*;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.LevelRenderer;
import net.minecraft.util.LightCoordsUtil;
import net.minecraft.client.renderer.SubmitNodeCollector; // SubmitNodeCollector + VertexConsumer replace MultiBufferSource in 26.2 feature pipeline.
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.feature.ModelFeatureRenderer;
import net.minecraft.client.renderer.rendertype.RenderType;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import reika.dragonapi.auxiliary.trackers.SpecialDayTracker;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.base.BlockEntityRenderBase;
import reika.dragonapi.base.DragonAPIMod;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.IORenderer;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;

import java.awt.*;

public abstract class RotaryTERenderer<TE extends BlockEntity> extends BlockEntityRenderBase<TE> {

    @Override
    protected Class<?> getModClass() {
        return RotaryCraft.class;
    }

    @Override
    protected String getModID() {
        return RotaryCraft.MODID;
    }

    @Override
    protected boolean loadXmasTextures() {
        return SpecialDayTracker.instance.loadXmasTextures();
    }

    /**
     * Builds a texture {@link net.minecraft.resources.Identifier} by appending a file-name
     * {@code suffix} (e.g. {@code "shafttexw.png"}) to a directory-prefix Identifier whose
     * path already ends in {@code "/"}. Used by renderers whose models share a folder but pick
     * a per-instance file name (shafts by material, gearboxes by gear material, etc.) — avoids
     * the {@code Identifier.parse(prefix + suffix)} idiom, which goes through
     * {@code Identifier.toString()} and re-parses the namespace prefix.
     */
    protected static net.minecraft.resources.Identifier textureWithSuffix(net.minecraft.resources.Identifier prefix, String suffix) {
        return net.minecraft.resources.Identifier.fromNamespaceAndPath(prefix.getNamespace(), prefix.getPath() + suffix);
    }

/*    protected void renderFaceColors(BlockEntityIOMachine te, double p2, double p4, double p6) {
        double offset = 0.0625;
        int alpha = te.iotick;
        Color[] colors = RotaryAux.sideColors;
        ReikaRenderHelper.prepareGeoDraw(true);

        Tesselator tesselator = Tesselator.getInstance();
        BufferBuilder v5 = tesselator.getBuilder();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[0].getRed(), colors[0].getGreen(), colors[0].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[1].getRed(), colors[1].getGreen(), colors[1].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[2].getRed(), colors[2].getGreen(), colors[2].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[3].getRed(), colors[3].getGreen(), colors[3].getBlue(), (int) (alpha / 2.4)).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 - offset, p4 - offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 - offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 - offset, p4 + 1 + offset, p6 - offset).color(colors[4].getRed(), colors[4].getGreen(), colors[4].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha).endVertex();
        tesselator.end();
        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_COLOR);
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 + 1 + offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 + 1 + offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        v5.vertex(p2 + 1 + offset, p4 - offset, p6 - offset).color(colors[5].getRed(), colors[5].getGreen(), colors[5].getBlue(), alpha / 3).endVertex();
        tesselator.end();

        for (int i = 0; i < 6; i++) {
            int a = 0;
            int b = 0;
            int c = 0;
            switch (i) {
                case 0 -> b = -3;
                case 1 -> b = 3;
                case 2 -> c = -3;
                case 3 -> c = 3;
                case 4 -> a = -3;
                case 5 -> a = 3;
            }
            v5.begin(VertexFormat.Mode.LINES, DefaultVertexFormat.POSITION_COLOR);
            v5.vertex(p2 + 0.5, p4 + 0.5, p6 + 0.5).color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), alpha).endVertex();
            v5.vertex(p2 + 0.5 + a, p4 + 0.5 + b, p6 + 0.5 + c).color(colors[i].getRed(), colors[i].getGreen(), colors[i].getBlue(), alpha).endVertex();
            tesselator.end();
        }

        ReikaRenderHelper.exitGeoDraw();
    }*/

    @Override
    protected final DragonAPIMod getOwnerMod() {
        return RotaryCraft.getInstance();
    }

    @Override
    protected final boolean doRenderModel(PoseStack stack, BlockEntityBase te) {
        return this.isValidMachineRenderPass(te);
    }

    protected Identifier getSubmitTexture(BlockEntity be) {
        return null;
    }

    protected boolean useEntityCutout() {
        return false;
    }

    /**
     * 26.2 port: renderModel now receives a VertexConsumer directly (from submitCustomGeometry lambda),
     * not a MultiBufferSource. Implementations should obtain a buffer via the consumer or just draw.
     */
    @SuppressWarnings("unchecked")
    protected void renderModel(PoseStack stack, BlockEntity be, VertexConsumer vc, int light) {
    }

    // The machine blocks are full/opaque, so the light at their own position (what extractBase
    // samples) is 0 and the BER model renders pure black. Sample the brightest neighbour instead
    // so the model is lit like its surroundings.
    @Override
    public void extractRenderState(TE be, BlockEntityRenderState state, float partialTicks, Vec3 cameraPosition, ModelFeatureRenderer.CrumblingOverlay breakProgress) {
        super.extractRenderState(be, state, partialTicks, cameraPosition, breakProgress);
        Level level = be.getLevel();
        if (level == null)
            return;
        BlockPos pos = be.getBlockPos();
        int best = state.lightCoords;
        for (Direction d : Direction.values()) {
            int l = LightCoordsUtil.getLightCoords(level, pos.relative(d));
            if (l > best)
                best = l;
        }
        state.lightCoords = best;
    }

    @Override
    public void submit(BlockEntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
        Level level = Minecraft.getInstance().level;
        if (level == null) return;
        BlockEntity be = level.getBlockEntity(state.blockPos);
        if (be == null) return;
        if (!(be instanceof BlockEntityBase bbe)) return;
        if (!this.doRenderModel(poseStack, bbe)) return;

        Identifier tex = this.getSubmitTexture(be);
        if (tex == null) return;

        RenderType rt = this.useEntityCutout() ? RenderTypes.entityCutout(tex) : RenderTypes.entitySolid(tex);
        PoseStack snapped = new PoseStack();
        snapped.last().set(poseStack.last());
        int light = state.lightCoords;
        collector.submitCustomGeometry(poseStack, rt, (pose, vc) -> {
            // 26.2: pass the VertexConsumer straight through; no MultiBufferSource adapter.
            this.renderModel(snapped, be, vc, light);
        });
        if (be instanceof BlockEntityIOMachine ioMachine && ioMachine.isInWorld()) {
            IORenderer.renderIO(poseStack, collector, ioMachine, ioMachine.getBlockPos());
        }
    }
}
