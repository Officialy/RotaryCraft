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
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;

public class LuaFireTurret extends LuaMethod {

	public LuaFireTurret() {
		super("fireTurret", BlockEntityAimedCannon.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityAimedCannon can = (BlockEntityAimedCannon) te;
		double[] xyz = ReikaPhysicsHelper.polarToCartesian(1, can.theta, can.phi);
		can.fire(te.getLevel(), xyz);
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Fires the turret.\nReturns: Nothing";
	}

	@Override
	public String getArgsAsString() {
		return "";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
