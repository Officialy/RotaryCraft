/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.engine;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.*;

public class BlockEntityMicroturbine extends BlockEntityEngine {

    public BlockEntityMicroturbine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MICROTURBINE.get(), pos, state, false, false, false, true);
        type = EngineType.MICRO;
    }

    @Override
    protected void consumeFuel() {
        fuel.removeLiquid(this.getConsumedFuel());
    }

    @Override
    protected void internalizeFuel() {

    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        return this.getFuelLevel() > 0;
    }

    @Override
    public int getFuelLevel() {
        return fuel.getFluidLevel();
    }

    @Override
    protected void playSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
        soundtick++;
        if (this.isMuffled(world, pos)) {
            volume *= 0.3125F;
        }

        if (soundtick < this.getSoundLength(1F / pitchMultiplier) && soundtick < 2000)
            return;
        soundtick = 0;

        float pitch = 1F;
        volume *= 0.125F;
        SoundRegistry.MICRO.playSoundAtBlock(world, pos, volume, pitch * pitchMultiplier);
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {

    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.MICRO_TURBINE.get();
    }

    @Override
    protected String getTEName() {
        return "Microturbine";
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.MICRO_TURBINE;
    }
}
