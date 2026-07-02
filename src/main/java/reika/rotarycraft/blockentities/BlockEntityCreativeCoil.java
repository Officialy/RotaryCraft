/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.ShaftPowerReceiver;
import reika.rotarycraft.base.blocks.entity.BlockCreativeCoil;
import reika.rotarycraft.registry.RotaryBlockEntities;

/**
 * The Creative Coil's block entity: an infinite shaft-power source that outputs on a single face (its
 * block's {@link BlockCreativeCoil#FACING}). Every server tick it writes a large constant torque/speed
 * to the {@link ShaftPowerReceiver} on that side if it will read from us, and satisfies
 * {@code PowerTransferHelper.checkPowerFrom} via {@link PowerGenerator}.
 *
 * <p>OMEGA is deliberately capped at 4096 -- the value ReactorCraft's own {@code DragonAPI.debugtest}
 * path feeds the solenoid, which maps to the solenoid's max safe render speed. Higher would trip the
 * solenoid's "spinning too fast -> violently fail" guard. TORQUE is huge (far above any machine's
 * minimum), so power = torque*omega is effectively unlimited for the machines that matter.</p>
 */
public class BlockEntityCreativeCoil extends BlockEntity implements PowerGenerator {

	public static final int OMEGA = 4096;
	public static final int TORQUE = 1 << 19; // 524288
	private static final long POWER = (long) OMEGA * TORQUE;

	public BlockEntityCreativeCoil(BlockPos pos, BlockState state) {
		super(RotaryBlockEntities.CREATIVE_COIL.get(), pos, state);
	}

	public void serverTick() {
		if (level == null)
			return;
		Direction out = getBlockState().getValue(BlockCreativeCoil.FACING);
		BlockEntity te = level.getBlockEntity(worldPosition.relative(out));
		// `out` points coil -> receiver, so the receiver reads from the opposite side (back at us).
		if (te instanceof ShaftPowerReceiver sp && sp.isReceiving() && sp.canReadFrom(out.getOpposite())) {
			sp.setOmega(OMEGA);
			sp.setTorque(TORQUE);
			sp.setPower(POWER);
		}
	}

	@Override
	public long getMaxPower() {
		return POWER;
	}

	@Override
	public long getCurrentPower() {
		return POWER;
	}

	@Override
	public BlockPos getEmittingPos(BlockPos pos) {
		// We only power the block on our FACING side; checkPowerFrom does pos.equals(getEmittingPos(pos)).
		return worldPosition.relative(getBlockState().getValue(BlockCreativeCoil.FACING));
	}
}
