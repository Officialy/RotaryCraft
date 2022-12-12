/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

public abstract class BlockEntityTransmissionMachine extends BlockEntityIOMachine {

    public BlockEntityTransmissionMachine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    protected void readFromCross(BlockEntityShaft cross) {
        if (cross.isWritingTo(this)) {
            omega = cross.readomega[0];
            torque = cross.readtorque[0];
        } else if (cross.isWritingTo2(this)) {
            omega = cross.readomega[1];
            torque = cross.readtorque[1];
        } else {
            omega = torque = 0;
        }
    }

    protected void readFromSplitter(Level world, BlockPos pos, BlockEntitySplitter spl) { //Complex enough to deserve its own function
        int sratio = spl.getRatioFromMode();
        if (sratio == 0)
            return;
        boolean favorbent = false;
        if (sratio < 0) {
            favorbent = true;
            sratio = -sratio;
        }
        if (pos == spl.getWritePos2() && pos == spl.getWritePos2()) { //We are the inline
            omega = spl.omega; //omega always constant
            if (sratio == 1) { //Even split, favorbent irrelevant
                torque = spl.torque / 2;
                return;
            }
            if (favorbent) {
                torque = spl.torque / sratio;
            } else {
                torque = (int) (spl.torque * ((sratio - 1D)) / sratio);
            }
        } else if (pos == spl.getWritePos2() && pos == spl.getWritePos2()) { //We are the bend //todo fixmetyvm
            omega = spl.omega; //omega always constant
            if (sratio == 1) { //Even split, favorbent irrelevant
                torque = spl.torque / 2;
                return;
            }
            if (favorbent) {
                torque = (int) (spl.torque * ((sratio - 1D) / (sratio)));
            } else {
                torque = spl.torque / sratio;
            }
        } else { //We are not one of its write-to blocks
            torque = 0;
            omega = 0;
            power = 0;
            return;
        }
    }

}
