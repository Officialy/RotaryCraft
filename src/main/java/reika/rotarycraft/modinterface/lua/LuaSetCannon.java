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
import reika.rotarycraft.base.blockentity.BlockEntityLaunchCannon;

public class LuaSetCannon extends LuaMethod {

	public LuaSetCannon() {
		super("setCannon", BlockEntityLaunchCannon.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityLaunchCannon can = (BlockEntityLaunchCannon) te;
		int theta = ((Double)args[0]).intValue();
		int ang = ((Double)args[1]).intValue();
		int v = ((Double)args[2]).intValue();
		int maxth = can.getMaxTheta();
		int maxv = can.getMaxLaunchVelocity();
		can.velocity = Math.min(v, maxv);
		can.theta = Math.min(theta, maxth);
		can.phi = ang;
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Sets the launch cannon trajectory.\nArgs: Inclination, Compass, Velocity\nReturns: Nothing";
	}

	@Override
	public String getArgsAsString() {
		return "int angle, int compass, int velocity";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
