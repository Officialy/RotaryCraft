/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.modinterface.conversion;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;

import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.energy.IEnergyStorage;
import net.neoforged.fluids.FluidStack;

import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.modinteract.power.ReikaRFHelper;
import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.SoundRegistry;

public class BlockEntityMagnetEngine extends EnergyToPowerBase {//todo implements IEnergyStorage //Handler because EnderIO uses it

    public BlockEntityMagnetEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MAGNETOSTATIC_ENGINE.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        this.getIOSides(world, pos, getBlockState().getValue(BlockRotaryCraftMachine.FACING));

        if ((world.getGameTime() & 31) == 0)
            ReikaWorldHelper.causeAdjacentUpdates(world, pos);

        //ReikaJavaLibrary.pConsole(storedEnergy+":"+this.getConsumedUnitsPerTick(), Dist.DEDICATED_SERVER);

        this.updateSpeed();
        if (!world.isClientSide()) {
            tickcount++;
            if (power > 0) {
                if (tickcount >= 85) {
                    tickcount = 0;
                    SoundRegistry.DYNAMO.playSoundAtBlock(world, pos, this.isMuffled() ? 0.1F : 0.5F, 1F);
                }
            }
        }
        this.basicPowerReceiver();
    }

    @Override
    public boolean isValidSupplier(BlockEntity te) {
        return te instanceof IEnergyStorage;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.MAGNETIC;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.MAGNETOSTATIC_ENGINE.get();
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected String getTEName() {
        return null;
    }

    public int receiveEnergy(int maxReceive, boolean simulate) {
        if (/*this.canConnectEnergy(from) && */maxReceive >= this.getMinimumCurrent()) {
            int amt = Math.min(maxReceive, this.getMaxStorage() - storedEnergy);
            if (!simulate)
                storedEnergy += amt;
            return amt;
        }
        return 0;
    }

    private int getMinimumCurrent() {
        return switch (this.getTier()) {
            case 0 -> 1;
            case 1 -> 3;
            case 2 -> 24;
            case 3 -> 187;
            case 4 -> 1491;
            case 5 -> 11925;
            default -> 0;
        };
    }

//    @Override
//  todo  public boolean canConnectEnergy(Direction from) {
//        return from == this.getFacing();// && this.isValidSupplier(this.getAdjacentTileEntity(from));
//    }

    @Override
    public int getMaxStorage() {
        return 1 + this.getMinimumCurrent() * 200;//ReikaMathLibrary.intpow2(10, this.getTier());//TileEntityPneumaticEngine.maxMJ*10;
    }

    @Override
    protected int getIdealConsumedUnitsPerTick() {
        return this.getRFPerTick();
    }

    private int getRFPerTick() {
        return (int) (this.getPowerLevel() / ReikaRFHelper.getWattsPerRF());
    }

    @Override
    public String getUnitDisplay() {
        return "RF";
    }

    @Override
    public int getPowerColor() {
        return 0xff0000;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @Override
    public  FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank,  FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public  FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Override
    public  FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, FluidAction doDrain) {
        return null;
    }


    //@Override
    //public int getMaxSpeedBase(int tier) {
    //	return 5+3*tier;
    //}

}
