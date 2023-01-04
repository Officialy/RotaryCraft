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
import org.jetbrains.annotations.NotNull;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.*;

public class BlockEntityMicroturbine extends BlockEntityEngine {

    public BlockEntityMicroturbine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MICROTURBINE.get(), pos, state);
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
        return fuel.getLevel();
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
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.MICROTURBINE.get();
    }

    @Override
    protected String getTEName() {
        return "Microturbine";
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
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
}