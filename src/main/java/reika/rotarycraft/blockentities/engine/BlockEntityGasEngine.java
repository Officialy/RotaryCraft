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
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.FluidType;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.items.tools.ItemEngineUpgrade;
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
        // Gas -> Sport is a distinct block in the port (engines were split from one metadata block
        // into a block per type), so the upgrade replaces GAS_ENGINE with PERFORMANCE_ENGINE and
        // carries the engine state across via NBT. Orientation lives in NBT (read directions),
        // so it survives the swap; no FACING blockstate to preserve (engine blocks are plain Blocks).
        type = EngineType.SPORT;
        CompoundTag NBT = new CompoundTag();
        this.saveAdditional(NBT);
        // Clear our inventory first so removing the gas block does not drop the carried items;
        // they are restored from NBT into the performance engine below.
        for (int i = 0; i < itemHandler.getSlots(); i++)
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
        level.setBlock(worldPosition, Blocks.AIR.defaultBlockState(), 1);
        level.setBlock(worldPosition, RotaryBlocks.PERFORMANCE_ENGINE.get().defaultBlockState(), 3);
        if (level.getBlockEntity(worldPosition) instanceof BlockEntityEngine te) {
            te.load(NBT);
            te.syncAllData(true);
        }
        level.updateNeighborsAt(worldPosition, getBlockState().getBlock());
    }

    public boolean canUpgradeWith(ItemStack item) {
        return ItemEngineUpgrade.getUpgrade(item) == ItemEngineUpgrade.UpgradeType.PERFORMANCE;
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
    public  AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ContainerEthanol(p_39954_, p_39955_, this);
    }
}
