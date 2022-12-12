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
import reika.rotarycraft.base.blockentity.BlockEntityPiping;

public class LuaGetPipe extends LuaMethod {

	public LuaGetPipe() {
		super("getPipe", BlockEntityPiping.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityPiping p = (BlockEntityPiping) te;
		return new Object[]{p.getAttributes().getFluidType().toString(), p.getFluidLevel()};
	}

	@Override
	public String getDocumentation() {
		return "Returns the pipe contents.\nArgs: None\nReturns: [Fluid name, amount]";
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
