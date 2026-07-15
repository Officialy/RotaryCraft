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

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.entity.state.EntityRenderState;
import net.minecraft.client.renderer.state.level.CameraRenderState;

import reika.rotarycraft.base.EntityTurretShot;

/**
 * Shared no-op renderer for the fast turret projectiles (freeze / flak / gatling shots) — their
 * feedback is the impact, not a visible model, but MC still requires a renderer per entity type.
 */
public class RenderTurretShot<E extends EntityTurretShot> extends EntityRenderer<E, EntityRenderState> {

    public RenderTurretShot(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    protected boolean affectedByCulling(E entity) {
        return false;
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
    }
}
