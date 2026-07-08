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

import reika.rotarycraft.entities.EntitySonicShot;

/**
 * The sonic shot is an invisible pressure wave — its feedback is the block-flattening and the
 * explosion sound when it lands, not a visible model. MC still requires a renderer per entity type,
 * so this one just supplies a render state and submits nothing.
 */
public class RenderSonicShot extends EntityRenderer<EntitySonicShot, EntityRenderState> {

    public RenderSonicShot(EntityRendererProvider.Context context) {
        super(context);
    }

    @Override
    public EntityRenderState createRenderState() {
        return new EntityRenderState();
    }

    @Override
    protected boolean affectedByCulling(EntitySonicShot entity) {
        return false;
    }

    @Override
    public void submit(EntityRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
    }
}
