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
import reika.rotarycraft.items.ItemCoil;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.TensionStorage;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.DifficultyEffects;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryItems;

public class BlockEntityWinder extends InventoriedPowerReceiver implements SimpleProvider, DiscreteFunction, ConditionalOperation, net.minecraft.world.WorldlyContainer, reika.dragonapi.interfaces.blockentity.GuiController {

    //Whether in wind or unwind mode
    public boolean winding = true;

    public BlockEntityWinder(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.WINDER.get(), pos, state);
    }

    public final int getUnwindTorque() {
        if (itemHandler.getStackInSlot(0).isEmpty())
            return 0;
        return 8 * ((TensionStorage) itemHandler.getStackInSlot(0).getItem()).getPowerScale(itemHandler.getStackInSlot(0));
    }

    public final int getUnwindSpeed() {
        if (itemHandler.getStackInSlot(0).isEmpty())
            return 0;
        return 1024 * ((TensionStorage) itemHandler.getStackInSlot(0).getItem()).getPowerScale(itemHandler.getStackInSlot(0));
    }

    //    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j == 0;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.WINDER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide()) return;
        read = getBlockState().getValue(BlockRotaryCraftMachine.FACING).getOpposite();
        write = null;
        getPower(false);
        if (!winding) { write = read; read = null; }
        tickcount++;
        ItemStack spring = itemHandler.getStackInSlot(0);
        if (spring.isEmpty() || !(spring.getItem() instanceof TensionStorage)) {
            if (!winding) { torque = omega = 0; power = 0; }
            return;
        }
        int charge = spring.getItem() instanceof ItemCoil ? ItemCoil.getCharge(spring) : spring.getDamageValue();
        if (winding) {
            if (omega <= 0 || torque <= 0 || tickcount < getOperationTime()) return;
            tickcount = 0;
            if (charge >= getMaxWind()) return;
            spring = spring.copy();
            if (spring.getItem() instanceof ItemCoil) ItemCoil.setCharge(spring, charge + 1); else spring.setDamageValue(charge + 1);
            itemHandler.setStackInSlot(0, spring);
            if (breakCoil()) {
                itemHandler.setStackInSlot(0, ItemStack.EMPTY);
                world.playSound(null, pos, SoundEvents.ITEM_BREAK.value(), SoundSource.BLOCKS, 1, 1);
            }
        } else {
            if (charge <= 0) { omega = torque = 0; power = 0; return; }
            omega = getUnwindSpeed(); torque = getUnwindTorque(); power = (long)omega * torque;
            if (tickcount < getUnwindTime()) return;
            tickcount = 0;
            spring = spring.copy();
            if (spring.getItem() instanceof ItemCoil) ItemCoil.setCharge(spring, charge - 1); else spring.setDamageValue(charge - 1);
            itemHandler.setStackInSlot(0, spring);
        }
        setChanged();
    }

    protected final int getUnwindTime() {
        ItemStack is = itemHandler.getStackInSlot(0);
        int base = 20;
        return base * ((TensionStorage) is.getItem()).getStiffness(is);
    }

    private boolean breakCoil() {
        ItemStack is = itemHandler.getStackInSlot(0);
        if (is.isEmpty())
            return false;
        if (!(is.getItem() instanceof TensionStorage ts))
            return false;
        if (!ts.isBreakable(is))
            return false;
        int dmg = ItemCoil.getCharge(itemHandler.getStackInSlot(0));
        double diff = dmg / 65536D * DifficultyEffects.BREAKCOIL.getDouble();
        boolean rand = ReikaRandomHelper.doWithChance(diff);
        return rand;
    }

    public int getOperationTime() {
        ItemStack spring = itemHandler.getStackInSlot(0);
        if (spring.isEmpty()) return 1;
        if (omega <= 0) return Integer.MAX_VALUE;
        int base = (int)ReikaMathLibrary.logbase(Math.max(1, ItemCoil.getCharge(spring)), 2);
        int speed = (int)ReikaMathLibrary.logbase((long)omega + 1, 2);
        return speed <= 0 ? Integer.MAX_VALUE : (int)(base / (double)speed);
    }
    public int getMaxWind() {
        ItemStack spring = itemHandler.getStackInSlot(0);
        if (spring.isEmpty() || !(spring.getItem() instanceof TensionStorage tension)) return 0;
        return Math.clamp(torque / Math.max(1, tension.getStiffness(spring)), 0, ItemCoil.MAX_CHARGE);
    }

    public int getContainerSize() {
        return 1;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        winding = NBT.getBooleanOr("winding", true);
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putBoolean("winding", winding);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
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
    public boolean canProvidePower() {
        return !winding;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.WINDER;
    }

    //    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        if (is.getItem() == RotaryItems.HSLA_STEEL_SPRING.get())
            return true;
        return is.getItem() == RotaryItems.BEDROCK_ALLOY_SPRING.get();
    }

    @Override
    public int getRedstoneOverride() {
        if (itemHandler.getStackInSlot(0).isEmpty())
            return 15;
        if (itemHandler.getStackInSlot(0).getItem() != RotaryItems.HSLA_STEEL_SPRING.get())
            return 15;
        if (itemHandler.getStackInSlot(0).getDamageValue() >= torque && winding)
            return 15;
        return 0;
    }

    @Override
    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public void onEMP() {
    }

    @Override
    public boolean areConditionsMet() {
        return !itemHandler.getStackInSlot(0).isEmpty() && itemHandler.getStackInSlot(0).getItem() instanceof TensionStorage;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Coil";
    }


    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
    @Override public net.minecraft.network.chat.Component getDisplayName() { return net.minecraft.network.chat.Component.translatable("block.rotarycraft.winder"); }
    @Override public net.minecraft.world.inventory.AbstractContainerMenu createMenu(int id, net.minecraft.world.entity.player.Inventory inventory, Player player) {
        return new reika.rotarycraft.gui.container.machine.inventory.WinderContainer(id, inventory, this);
    }
    @Override public ItemStack getItem(int slot) { return itemHandler.getStackInSlot(slot); }
    @Override public ItemStack removeItem(int slot, int amount) { return itemHandler.extractItem(slot, amount, false); }
    @Override public ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, 1); }
    @Override public void setItem(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack.isEmpty() ? ItemStack.EMPTY : stack.copyWithCount(1)); }
    @Override public boolean isEmpty() { return itemHandler.getStackInSlot(0).isEmpty(); }
    @Override public void clearContent() { itemHandler.setStackInSlot(0, ItemStack.EMPTY); }
    @Override public boolean stillValid(Player player) { return !isRemoved() && isPlayerAccessible(player); }
    @Override public boolean canPlaceItem(int slot, ItemStack stack) { return isItemValidForSlot(slot, stack); }
    @Override public int[] getSlotsForFace(net.minecraft.core.Direction side) { return new int[]{0}; }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, net.minecraft.core.Direction side) { return isItemValidForSlot(slot, stack); }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, net.minecraft.core.Direction side) { return side == net.minecraft.core.Direction.DOWN; }
}
