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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityCreativeCoil extends BlockEntityAdvancedGear {

	/** Comfortably below Long.MAX_VALUE so `energy + torquein*omegain` can't overflow into negative. */
	private static final long INFINITE_ENERGY = Long.MAX_VALUE / 4;

	public BlockEntityCreativeCoil(BlockPos pos, BlockState state) {
		super(GearType.COIL, RotaryBlockEntities.CREATIVE_COIL.get(), pos, state);
		this.setCreative(true);
		this.energy = INFINITE_ENERGY;
	}

	@Override
	public void getIOSides(Level world, BlockPos pos) {
		super.getIOSides(world, pos, true);
	}
	@Override
	public MachineRegistry getMachine() {
		return MachineRegistry.CREATIVE_COIL;
	}
}
