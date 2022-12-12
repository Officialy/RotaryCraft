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
import reika.rotarycraft.blockentities.BlockEntityBlower;

public class LuaToggleOreDict extends LuaMethod {

	public LuaToggleOreDict() {
		super("toggleOreDict", BlockEntityBlower.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		((BlockEntityBlower)te).useOreDict = !((BlockEntityBlower)te).useOreDict;
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Toggles item pump ore dictionary matching.";
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
