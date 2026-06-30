/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.power;

import reika.rotarycraft.api.IOMachine;
import reika.rotarycraft.api.interfaces.ComplexIO;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * This class has some functions to aid in ensuring you are not the source of a power exploit by remaining powered even if the supplying machine
 * is broken. Call this every tick you are powered, and if the return value is false, your power supply is gone.
 */
public class PowerTransferHelper {

    public static boolean checkPowerFrom(BlockEntity tile, Direction dir) {
        if (tile == null)
            return false;
        BlockPos pos = tile.getBlockPos();
        int dx = pos.getX() + dir.getStepX();
        int dy = pos.getY() + dir.getStepY();
        int dz = pos.getZ() + dir.getStepZ();
        BlockEntity toCheck = tile.getLevel().getBlockEntity(new BlockPos(dx, dy, dz));
		/*if (toCheck instanceof WorldRift) {
			WorldRift sr = (WorldRift)toCheck;
			WorldLocation loc = sr.getLinkTarget();
			if (loc != null) {
				return checkPowerFrom(loc.getBlockEntity(), dir);
			}
		}*/
        if (toCheck instanceof PowerGenerator || toCheck instanceof IOMachine) {
            if (toCheck instanceof ComplexIO cio) {
                int torque = cio.getTorqueToSide(dir.getOpposite());
                int omega = cio.getSpeedToSide(dir.getOpposite());
                return (torque * omega) != 0;
            } else if (toCheck instanceof IOMachine) {
                BlockPos wx = ((IOMachine) toCheck).getWritePos();
                BlockPos wx2 = ((IOMachine) toCheck).getWritePos2();
                // BlockPos must be compared by value: getWritePos() returns a fresh BlockPos, so the
                // old `wx == pos` reference check was ALWAYS false — every machine that gates on this
                // anti-exploit helper (e.g. the reactor CPU) saw "no power" every tick and zeroed out,
                // which made the CPU SCRAM constantly and the control rods un-retractable.
                return pos.equals(wx) || pos.equals(wx2);
            } else {
                BlockPos wx = ((PowerGenerator) toCheck).getEmittingPos(pos);
                return pos.equals(wx);
            }
        } else {
            return false;
        }
    }

    public static boolean checkPowerFromAllSides(BlockEntity tile, boolean vertical) {
        for (int i = vertical ? 0 : 2; i < 6; i++) {
            Direction dir = Direction.values()[i];
            if (checkPowerFrom(tile, dir)) {
                return true;
            }
        }
        return false;
    }

}
