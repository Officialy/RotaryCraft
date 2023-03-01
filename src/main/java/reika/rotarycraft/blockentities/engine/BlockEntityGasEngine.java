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
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.gui.container.machine.inventory.ContainerEthanol;
import reika.rotarycraft.registry.*;

public class BlockEntityGasEngine extends BlockEntityEngine implements UpgradeableMachine {

    public BlockEntityGasEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.GAS_ENGINE.get(), pos, state, false, false, false, true);
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
        if (!itemHandler.getStackInSlot(0).isEmpty() && fuel.getFluidLevel() + FluidType.BUCKET_VOLUME <= FUELCAP) {
            if (itemHandler.getStackInSlot(0).getItem() == RotaryItems.ETHANOL.get()) {
                ReikaInventoryHelper.decrStack(0, itemHandler);
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
        soundTick++;
        if (this.isMuffled(world, pos)) {
            volume *= 0.3125F;
        }
        if (soundTick < this.getSoundLength(1F / pitchMultiplier) && soundTick < 2000)
            return;
        soundTick = 0;

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


    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ContainerEthanol(p_39954_, p_39955_, this);
    }
}
