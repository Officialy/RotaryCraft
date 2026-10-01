/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.renders.m;
import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import net.minecraft.client.Minecraft;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.blockentity.BlockEntityRendererProvider;
import net.minecraft.client.renderer.blockentity.state.BlockEntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.resources.Identifier;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.base.RotaryTERenderer;
import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;
import reika.rotarycraft.models.animated.RadarModel;
import reika.rotarycraft.registry.RotaryModelLayers;
import reika.rotarycraft.renders.RotaryRenderPipelines;
public final class RenderMobRadar extends RotaryTERenderer<BlockEntityMobRadar> {
    private final RadarModel model;
    public RenderMobRadar(BlockEntityRendererProvider.Context context) { model = new RadarModel(context.bakeLayer(RotaryModelLayers.MOB_RADAR)); }
    @Override protected Identifier getSubmitTexture(BlockEntity tile) { return RadarModel.TEXTURE_LOCATION; }
    @Override protected boolean useEntityCutout() { return true; }
    @Override public boolean shouldRenderOffScreen() { return true; }
    @Override public int getViewDistance() { return 256; }
    @Override protected void renderModel(PoseStack stack, BlockEntity tile, VertexConsumer vertices, int light) {
        var radar = (BlockEntityMobRadar)tile; stack.pushPose(); stack.translate(.5, 1.5, .5); stack.scale(1, -1, -1);
        model.renderAll(stack, vertices, light, tile, null, -radar.phi, 0); stack.popPose();
    }
    @Override public void submit(BlockEntityRenderState state, PoseStack stack, SubmitNodeCollector collector, CameraRenderState camera) {
        super.submit(state, stack, collector, camera);
        var minecraft = Minecraft.getInstance();
        if (minecraft.level == null || !(minecraft.level.getBlockEntity(state.blockPos) instanceof BlockEntityMobRadar radar) || !radar.canShowHud(minecraft.player)) return;
        var contacts = radar.getContacts(); var pos = radar.getBlockPos(); int viewerId = minecraft.player.getId();
        var snapped = new PoseStack(); snapped.last().set(stack.last());
        collector.submitCustomGeometry(stack, RotaryRenderPipelines.NO_DEPTH_LINES_TYPE, (ignored, vertices) -> {
            for (var contact : contacts) {
                if (contact.entityId() == viewerId) continue;
                float x = (float)(contact.x() - pos.getX()), y = (float)(contact.y() - pos.getY()), z = (float)(contact.z() - pos.getZ());
                int color = 0xff000000 | contact.color();
                vertices.addVertex(snapped.last(), x, y, z).setColor(color).setNormal(snapped.last(), 0, 1, 0).setLineWidth(1);
                vertices.addVertex(snapped.last(), x, y + 2, z).setColor(color).setNormal(snapped.last(), 0, 1, 0).setLineWidth(1);
            }
        });
    }
}
