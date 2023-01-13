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
import net.minecraft.core.Direction;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
import net.minecraft.world.item.SwordItem;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.SoundRegistry;

public class BlockEntityGrindstone extends InventoriedPowerLiquidReceiver implements MultiOperational, ConditionalOperation {

    private static final String NBT_TAG = "repairs";
    private int soundtick;

    public BlockEntityGrindstone(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.GRINDSTONE.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    //@Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        //this.getIOSides(world, pos);
        this.getPower(true);
        tickcount++;
        if (power < MINPOWER || torque < MINTORQUE) {
            return;
        }

        if (inv[0] != null) {
            soundtick++;
            if (soundtick > 49) {
                SoundRegistry.FRICTION.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.1F : 0.5F, 1);
                soundtick = 0;
            }
        }

        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;

        if (world.isClientSide)
            return;

        int n = this.getNumberConsecutiveOperations();
        for (int i = 0; i < n; i++)
            this.doOperation(n > 1);
    }

    private void doOperation(boolean multiple) {
        if (tank.isEmpty())
            return;

        if (this.hasValidItem()) {
            this.createUsesTag();
            this.repair();
            tank.removeLiquid(100);
        }
    }

    private void createUsesTag() {
        if (!inv[0].getOrCreateTag().contains(NBT_TAG))
            inv[0].getOrCreateTag().putInt(NBT_TAG, inv[0].getMaxDamage() * 2);
    }

    public void getIOSides(Level world, BlockPos pos, int metadata) {
        switch (metadata) {
            case 1 -> {
                read = Direction.EAST;
                read2 = Direction.WEST;
            }
            case 0 -> {
                read = Direction.SOUTH;
                read2 = Direction.NORTH;
            }
        }
    }

    private void repair() {
        int dmg = inv[0].getDamageValue();
        int newdmg = dmg - 1;
        inv[0].setDamageValue(newdmg);
        int repair = inv[0].getTag().getInt(NBT_TAG);
        inv[0].getTag().putInt(NBT_TAG, repair - 1);
        //ReikaJavaLibrary.pConsole(inv[0].getTag());
    }

    public int getMinimumDamageForItem(ItemStack is) {
        return is.getTag() != null && is.getTag().contains(NBT_TAG) ? is.getMaxDamage() - Mth.ceil(is.getTag().getInt(NBT_TAG) / 2F) : 0;
    }

    public boolean hasValidItem() {
        if (inv[0] == null)
            return false;
        if (!this.isItemValidForSlot(0, inv[0]))
            return false;
        return inv[0].getDamageValue() > this.getMinimumDamageForItem(inv[0]);
    }

    //    @Override
    public boolean canExtractItem(int i, ItemStack is, int j) {
        return is.getDamageValue() <= this.getMinimumDamageForItem(is);
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return is.isDamageableItem() && (is.getItem() instanceof ShearsItem || is.getItem() instanceof SwordItem);// || is.getItem() instanceof ItemTool);
    }

    @Override
    public Fluid getInputFluid() {
        return Fluids.WATER;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return true;
    }

    @Override
    public int getCapacity() {
        return 1000;
    }

    //    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
        if (power < MINPOWER)
            return;
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected String getTEName() {
        return "grindstone";
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.GRINDSTONE;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public boolean areConditionsMet() {
        return this.hasValidItem();
    }

    @Override
    public String getOperationalStatus() {
        return inv[0] == null ? "No Tool" : this.areConditionsMet() ? "Operational" : "Invalid Item";
    }

    @Override
    public int getOperationTime() {
        return DurationRegistry.GRINDSTONE.getOperationTime(omega);
    }

    @Override
    public int getNumberConsecutiveOperations() {
        return DurationRegistry.GRINDSTONE.getNumberOperations(omega);
    }

    @Override
    public int getSlots() {
        return 1;
    }

    @NotNull
    @Override
    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @NotNull
    @Override
    public ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 64;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
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
    public int getContainerSize() {
        return 1;
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
}
