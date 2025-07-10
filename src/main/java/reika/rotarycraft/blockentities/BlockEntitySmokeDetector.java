/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;


import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
import reika.rotarycraft.registry.*;

public class BlockEntitySmokeDetector extends BlockEntitySpringPowered implements RangedEffect {

    //public static int MINPOWER = 16; //runs off of 4AAA's (max power = 4W) , so 16W (one DC engine can run 64, or 8 at max range)
    //public static int BASESPEED = 0;

    public int soundDelay = -1;
    private boolean isAlarm = false;
    private boolean isLowBatt = false;
    private int unwindtick = 0;

    public BlockEntitySmokeDetector(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SMOKE_DETECTOR.get(), pos, state);

        ///todo make the item handler only accept RotaryItems.HSLA_STEEL_SPRING.get().getDefaultInstance();
    }

    public boolean isAlarming() {
        return isAlarm;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SMOKE_DETECTOR.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        tickcount++;
        unwindtick++;
//        this.getPower4Sided(0, 1, 0);
//        if (this.power < MINPOWER)
//        	return;
        if (!this.checkValidCoil())
            return;
        if (unwindtick >= this.getUnwindTime()) {
            itemHandler.setStackInSlot(0, this.getDecrementedCharged());
            unwindtick = 0;
        }
        //ReikaChatHelper.write(ReikaWorldHelper.findNearBlock(world, pos, 8, Blocks.FIRE.blockID));
        if (ReikaWorldHelper.findNearBlock(world, pos, 8, Blocks.FIRE)) {
            if (!isAlarm) {
                isAlarm = true;
                ReikaWorldHelper.causeAdjacentUpdates(world, pos);
            }
        } else {
            if (isAlarm) {
                isAlarm = false;
                ReikaWorldHelper.causeAdjacentUpdates(world, pos);
            }
        }
        isLowBatt = this.lowBattery();
        if (isAlarm)
            soundDelay = 4;
        else if (isLowBatt)
            soundDelay = 600;
        else
            soundDelay = -1;
        if (tickcount >= soundDelay && soundDelay != -1) {
            tickcount = 0;
            SoundRegistry.SMOKE.playSoundAtBlock(world, pos, 0.1F, 1);
            world.playLocalSound(pos.getX(), pos.getY(), pos.getZ(), SoundEvents.AMETHYST_BLOCK_CHIME, SoundSource.BLOCKS, 1, 1, false);
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected String getTEName() {
        return "SmokeDetector";
    }
	/*
	public int getReactionTime() {
		int time = (int)(10-ReikaMathLibrary.logbase(this.omega, 2));
		if (time < 1)
			return 1;
		return time;
	}
	 */

    public boolean checkValidCoil() {
        return this.hasCoil();
    }

    public int getRange() {
        int overpower;
        //overpower  = (int)(this.power/MINPOWER);
        if (!this.checkValidCoil())
            return 0;
        int dmg = itemHandler.getStackInSlot(0).getTag().getInt("power");
        overpower = (int) ReikaMathLibrary.logbase((long) dmg * dmg, 2);
        return Math.min(overpower, 8);
    }

    public boolean lowBattery() {
        if (!this.checkValidCoil())
            return false;
        return itemHandler.getStackInSlot(0).getTag().getInt("power") <= 8;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SMOKEDETECTOR;
    }

    @Override
    public int getMaxRange() {
        return this.getRange();
    }

    @Override
    public int getBaseDischargeTime() {
        return 1200;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public int getContainerSize() {
        return 1;
    }

}
