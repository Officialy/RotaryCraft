/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

/**
 * The Creative Coil: an "infinite Industrial Coil" for easy testing. Reuses
 * {@link BlockEntityAdvancedGear}'s {@code GearType.COIL} behaviour wholesale (the real coil's
 * texture, {@code CoilModel} renderer, GUI, capacitor sounds) -- the only differences are the concrete
 * {@link net.minecraft.world.level.block.entity.BlockEntityType} it validates against (its own
 * {@code CREATIVE_COIL}, not {@code COIL} -- reusing COIL's type would crash on placement) and that it
 * starts creative and pre-charged.
 *
 * <p>{@code isCreative} alone is not enough to make a coil output power: legacy
 * {@code BlockEntityAdvancedGear.store()} only enters its release branch when {@code energy > 0}, and
 * the {@code !isCreative} guard on the charging branch means a creative coil's energy is NEVER
 * incremented from input power -- so a freshly-flagged creative coil with {@code energy == 0} would
 * sit inert forever. In 1.7.10 this was presumably worked around per-instance with an NBT edit setting
 * both flags together; the fixed point (a placeable, creative-from-spawn coil) needs both set at
 * construction, so this seeds {@code energy} to a value that is functionally infinite (never spent,
 * since {@code !isCreative} also guards the decrement) without risking the overflow checks elsewhere
 * in {@code store()} ({@code energy + torquein*omegain} arithmetic).</p>
 */
public class BlockEntityCreativeCoil extends BlockEntityAdvancedGear {

	/** Comfortably below Long.MAX_VALUE so `energy + torquein*omegain` can't overflow into negative. */
	private static final long INFINITE_ENERGY = Long.MAX_VALUE / 4;

	public BlockEntityCreativeCoil(BlockPos pos, BlockState state) {
		super(GearType.COIL, RotaryBlockEntities.CREATIVE_COIL.get(), pos, state);
		this.setCreative(true);
		this.energy = INFINITE_ENERGY;
	}

	@Override
	public MachineRegistry getMachine() {
		return MachineRegistry.CREATIVE_COIL;
	}
}
