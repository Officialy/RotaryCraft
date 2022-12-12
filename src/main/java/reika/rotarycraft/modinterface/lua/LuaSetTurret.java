/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface.lua;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;

public class LuaSetTurret extends LuaMethod {

	public LuaSetTurret() {
		super("setTurretAim", BlockEntityAimedCannon.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityAimedCannon can = (BlockEntityAimedCannon) te;
		can.isCustomAim = true;
		double tphi = ((Double)args[0]).doubleValue();
		double ttheta = ((Double)args[1]).doubleValue();
		if (!can.isValidTheta(ttheta))
			throw new LuaMethodException("Invalid angle; out of gimbal limits ("+can.MAXLOWANGLE+" degrees below horizontal)");
		boolean ret = can.adjustAim(tphi, ttheta);
		return new Object[] {ret};
	}

	@Override
	public String getDocumentation() {
		return "Sets the turret aim direction.\nArgs: Phi, Theta\nReturns: isAimingAt";
	}

	@Override
	public String getArgsAsString() {
		return "int phi, int theta";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.BOOLEAN;
	}

}
