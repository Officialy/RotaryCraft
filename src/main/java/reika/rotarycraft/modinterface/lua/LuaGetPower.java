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
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;

public class LuaGetPower extends LuaMethod {

	public LuaGetPower() {
		super("getPower", BlockEntityIOMachine.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityIOMachine io = (BlockEntityIOMachine)te;
		Object[] o = new Object[3];
		o[0] = io.power;
		o[1] = io.torque;
		o[2] = io.omega;
		return o;
	}

	@Override
	public String getDocumentation() {
		return "Returns the power data.\nArgs: None\nReturns: [Power, Torque, Speed]";
	}

	@Override
	public String getArgsAsString() {
		return "";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.ARRAY;
	}

}
