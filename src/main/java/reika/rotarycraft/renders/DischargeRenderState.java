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

import net.minecraft.client.renderer.entity.state.EntityRenderState;

/**
 * 26.2 render state for {@link reika.rotarycraft.entities.EntityDischarge}. Carries the arc endpoint
 * (target, relative to the entity origin) and its charge-derived colour, extracted from the entity so
 * {@link RenderDischarge#submit} never touches the entity directly (the 26.2 EntityRenderer contract).
 */
public class DischargeRenderState extends EntityRenderState {

	/** Arc target, relative to the entity's own position. */
	public double tx, ty, tz;
	/** Packed 0xRRGGBB colour from the discharge's current (see EntityDischarge.getColor). */
	public int rgb;
}
