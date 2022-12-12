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
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;

public class LuaGetName extends LuaMethod {

	public LuaGetName() {
		super("getName", RotaryCraftBlockEntity.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		return new Object[]{((RotaryCraftBlockEntity)te).getName()};
	}

	@Override
	public String getDocumentation() {
		return "Returns the machine name.\nArgs: None\nReturns: Display name";
	}

	@Override
	public String getArgsAsString() {
		return "";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.STRING;
	}

}
