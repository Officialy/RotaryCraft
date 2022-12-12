package reika.rotarycraft.modinterface.lua;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

import reika.dragonapi.modinteract.lua.LuaMethod;
import reika.rotarycraft.blockentities.transmission.BlockEntityDistributionClutch;

public class LuaSetDistributionClutchOutput extends LuaMethod {

	public LuaSetDistributionClutchOutput() {
		super("setTorqueToSide", BlockEntityDistributionClutch.class);
	}

	@Override
	protected Object[] invoke(BlockEntity te, Object[] args) throws LuaMethodException, InterruptedException {
		BlockEntityDistributionClutch tile = (BlockEntityDistributionClutch)te;
		int side = ((Double)args[0]).intValue();
		boolean enable = ((Boolean)args[1]).booleanValue();
		int amt = ((Double)args[2]).intValue();
		Direction dir = Direction.values()[side];
		tile.setSideEnabled(dir, enable);
		if (enable)
			tile.setTorqueRequest(dir, amt);
		return null;
	}

	@Override
	public String getDocumentation() {
		return "Sets the requested torque amount out a given side. Remainder is passed directly through.";
	}

	@Override
	public String getArgsAsString() {
		return "int side, int torque";
	}

	@Override
	public ReturnType getReturnType() {
		return ReturnType.VOID;
	}

}
