/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.renders;

import com.mojang.blaze3d.vertex.PoseStack;
import com.mojang.blaze3d.vertex.VertexConsumer;
import com.mojang.math.Axis;
import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.texture.OverlayTexture;
import net.minecraft.resources.Identifier;
import reika.rotarycraft.base.EntityRailgunShotBase;

/** V33a's eight intersecting camera-facing textured planes, using deferred 26.3 geometry. */
public final class RenderRailGunShot<T extends EntityRailgunShotBase> extends EntityRenderer<T, EntityRenderState> {
    public static final Identifier TEXTURE = Identifier.fromNamespaceAndPath("rotarycraft", "textures/entity/railgun.png");
    public RenderRailGunShot(EntityRendererProvider.Context context) {
        super(context); shadowRadius = .15F; shadowStrength = .75F;
    }
    @Override public EntityRenderState createRenderState() { return new EntityRenderState(); }
    @Override protected boolean affectedByCulling(T entity) { return false; }
    @Override public void submit(EntityRenderState state, PoseStack poses, SubmitNodeCollector collector, CameraRenderState camera) {
        final int light = state.lightCoords;
        poses.pushPose();
        poses.rotate(camera.orientation);
        poses.scale(.3F, .3F, .3F);
        // The original loop cumulatively rotated by 0, 45, ... 315 degrees.
        for (int angle = 0; angle < 360; angle += 45) {
            poses.rotateDegrees(Axis.YP, angle);
            collector.submitCustomGeometry(poses, RenderTypes.entityCutout(TEXTURE), (pose, buffer) -> {
                vertex(buffer, pose, -.5F, -.25F, 0, 0, light);
                vertex(buffer, pose, .5F, -.25F, 0, 1, light);
                vertex(buffer, pose, .5F, .75F, 1, 1, light);
                vertex(buffer, pose, -.5F, .75F, 1, 0, light);
            });
        }
        poses.popPose();
        super.submit(state, poses, collector, camera);
    }
    private static void vertex(VertexConsumer buffer, PoseStack.Pose pose, float x, float y, float u, float v, int light) {
        buffer.addVertex(pose, x, y, 0).setColor(-1).setUv(u, v).setOverlay(OverlayTexture.NO_OVERLAY)
                .setLight(light).setNormal(pose, 0, 1, 0);
    }
}
