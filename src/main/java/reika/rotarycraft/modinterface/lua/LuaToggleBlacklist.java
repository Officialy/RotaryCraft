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

public class LuaToggleBlacklist extends LuaMethod {

	public LuaToggleBlacklist() {
		super("toggleBlacklist", BlockEntityBlower.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		((BlockEntityBlower)te).isWhitelist = !((BlockEntityBlower)te).isWhitelist;
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Toggles item pump blacklist/whitelist.";
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
