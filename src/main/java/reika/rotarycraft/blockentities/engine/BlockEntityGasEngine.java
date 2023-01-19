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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.*;

public class BlockEntityGasEngine extends BlockEntityEngine implements UpgradeableMachine {

    public BlockEntityGasEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.GAS_ENGINE.get(), pos, state);
        type = EngineType.GAS;
    }
    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.GAS_ENGINE;
    }
    @Override
    public void upgrade(ItemStack is) {
        CompoundTag NBT = new CompoundTag();
        type = EngineType.SPORT;
        this.saveAdditional(NBT);
        level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 1);
        level.setBlock(worldPosition, this.getBlockEntityBlockID().defaultBlockState(), type.ordinal(), 3);
        BlockEntityEngine te = (BlockEntityEngine) level.getBlockEntity(worldPosition);
        te.load(NBT);
        this.syncAllData(true);
        te.syncAllData(true);
        level.blockUpdated(worldPosition, getBlockState().getBlock());
    }

    public boolean canUpgradeWith(ItemStack item) {
        return item.getItem() == RotaryItems.UPGRADE.get();
    }

    @Override
    protected void consumeFuel() {
        fuel.removeLiquid(this.getConsumedFuel());
    }

    @Override
    protected void internalizeFuel() {
        if (inv[0] != null && fuel.getFluidLevel() + FluidType.BUCKET_VOLUME <= FUELCAP) {
            if (inv[0].getItem() == RotaryItems.ETHANOL.get()) {
                ReikaInventoryHelper.decrStack(0, inv);
                fuel.addLiquid(1000, RotaryFluids.ETHANOL.get());
            }
        }
    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        return !fuel.isEmpty();
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

        SoundRegistry.CAR.playSoundAtBlock(world, pos, 0.33F * volume, 0.9F * pitchMultiplier);
    }

    @Override
    public int getFuelLevel() {
        return fuel.getFluidLevel();
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
        return RotaryBlocks.GAS_ENGINE.get();
    }

    @Override
    protected String getTEName() {
        return "gasengine";
    }


    @Override
    public boolean hasAnInventory() {
        return true;
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
}
