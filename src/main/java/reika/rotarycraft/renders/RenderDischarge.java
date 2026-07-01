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

import java.awt.Color;

import org.joml.Matrix4f;

import net.minecraft.client.renderer.SubmitNodeCollector;
import net.minecraft.client.renderer.entity.EntityRenderer;
import net.minecraft.client.renderer.entity.EntityRendererProvider;
import net.minecraft.client.renderer.rendertype.RenderTypes;
import net.minecraft.client.renderer.state.level.CameraRenderState;
import net.minecraft.util.RandomSource;

import com.mojang.blaze3d.vertex.PoseStack;

import reika.rotarycraft.entities.EntityDischarge;

/**
 * 26.2 port of the legacy {@code RenderDischarge}: the electric arc a van de Graaff generator shoots at
 * a nearby {@code Shockable} block (e.g. the ReactorCraft electrolyzer). The arc is a jagged poly-line
 * from the entity to its target, jittered per-segment, drawn on the {@code lines} render type in the
 * discharge's charge-derived colour. Matches the original: 20 mid points between origin and target,
 * each nudged ~0.1 off the straight path, re-randomised each frame for a live crackle.
 */
public class RenderDischarge extends EntityRenderer<EntityDischarge, DischargeRenderState> {

	private static final int SEGMENTS = 20;
	private static final double JITTER = 0.1;

	public RenderDischarge(EntityRendererProvider.Context context) {
		super(context);
	}

	@Override
	public DischargeRenderState createRenderState() {
		return new DischargeRenderState();
	}

	@Override
	public void extractRenderState(EntityDischarge entity, DischargeRenderState state, float partialTicks) {
		super.extractRenderState(entity, state, partialTicks);
		state.tx = entity.targetX - entity.getX();
		state.ty = entity.targetY - entity.getY();
		state.tz = entity.targetZ - entity.getZ();
		Color c = entity.getColor();
		state.rgb = (c.getRed() << 16) | (c.getGreen() << 8) | c.getBlue();
	}

	// The arc reaches far beyond the entity's tiny bounding box, so never cull it against that box.
	@Override
	protected boolean affectedByCulling(EntityDischarge entity) {
		return false;
	}

	@Override
	public void submit(DischargeRenderState state, PoseStack poseStack, SubmitNodeCollector collector, CameraRenderState camera) {
		final double tx = state.tx, ty = state.ty, tz = state.tz;
		final int rgb = state.rgb;
		// Fresh randomness each frame -> the bolt visibly crackles (legacy used `new Random()` per draw).
		final RandomSource rand = RandomSource.create();

		// Build the poly-line: origin, SEGMENTS jittered mid points, target.
		final float[][] pts = new float[SEGMENTS + 2][3];
		pts[0][0] = pts[0][1] = pts[0][2] = 0F;
		for (int i = 1; i <= SEGMENTS; i++) {
			double f = i / (double) SEGMENTS;
			pts[i][0] = (float) (tx * f + (rand.nextDouble() - 0.5) * JITTER);
			pts[i][1] = (float) (ty * f + (rand.nextDouble() - 0.5) * JITTER);
			pts[i][2] = (float) (tz * f + (rand.nextDouble() - 0.5) * JITTER);
		}
		pts[SEGMENTS + 1][0] = (float) tx;
		pts[SEGMENTS + 1][1] = (float) ty;
		pts[SEGMENTS + 1][2] = (float) tz;

		final float r = ((rgb >> 16) & 0xFF) / 255F;
		final float g = ((rgb >> 8) & 0xFF) / 255F;
		final float b = (rgb & 0xFF) / 255F;

		collector.submitCustomGeometry(poseStack, RenderTypes.lines(), (pose, buffer) -> {
			Matrix4f m = pose.pose();
			for (int i = 0; i < pts.length - 1; i++) {
				float[] p0 = pts[i];
				float[] p1 = pts[i + 1];
				float nx = p1[0] - p0[0], ny = p1[1] - p0[1], nz = p1[2] - p0[2];
				float len = (float) Math.sqrt(nx * nx + ny * ny + nz * nz);
				if (len < 1.0E-4F) { nx = 0F; ny = 1F; nz = 0F; } else { nx /= len; ny /= len; nz /= len; }
				buffer.addVertex(m, p0[0], p0[1], p0[2]).setColor(r, g, b, 1F).setNormal(pose, nx, ny, nz);
				buffer.addVertex(m, p1[0], p1[1], p1[2]).setColor(r, g, b, 1F).setNormal(pose, nx, ny, nz);
			}
		});
	}
}
