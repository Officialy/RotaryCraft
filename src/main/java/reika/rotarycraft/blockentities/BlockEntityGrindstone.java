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
import net.minecraft.tags.ItemTags;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ShearsItem;
// 1.21.5: SwordItem removed; swords are now plain Items via Properties.sword.
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
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

        if (!itemHandler.getStackInSlot(0).isEmpty()) {
            soundtick++;
            if (soundtick > 49) {
                SoundRegistry.FRICTION.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.1F : 0.5F, 1);
                soundtick = 0;
            }
        }

        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;

        if (world.isClientSide())
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
        ItemStack is = itemHandler.getStackInSlot(0);
        if (!ReikaItemHelper.getOrCreateStackTag(is).contains(NBT_TAG))
            ReikaItemHelper.updateStackTag(is, __T__ -> __T__.putInt(NBT_TAG, is.getMaxDamage() * 2));
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
        ItemStack stack = itemHandler.getStackInSlot(0);
        int dmg = stack.getDamageValue();
        int newdmg = dmg - 1;
        stack.setDamageValue(newdmg);
        int repair = ReikaItemHelper.getOrCreateStackTag(stack).getIntOr(NBT_TAG, 0);
        ReikaItemHelper.updateStackTag(stack, __T__ -> __T__.putInt(NBT_TAG, repair - 1));
    }

    public int getMinimumDamageForItem(ItemStack is) {
        return ReikaItemHelper.hasStackTag(is) && ReikaItemHelper.getStackTag(is).contains(NBT_TAG) ? is.getMaxDamage() - Mth.ceil(ReikaItemHelper.getStackTag(is).getIntOr(NBT_TAG, 0) / 2F) : 0;
    }

    public boolean hasValidItem() {
        if (itemHandler.getStackInSlot(0).isEmpty())
            return false;
        if (!this.isItemValidForSlot(0, itemHandler.getStackInSlot(0)))
            return false;
        return itemHandler.getStackInSlot(0).getDamageValue() > this.getMinimumDamageForItem(itemHandler.getStackInSlot(0));
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
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return FluidStack.EMPTY;
    }
    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        // 1.21.5: SwordItem was collapsed into plain Item + Item.Properties.sword(...). The
        // replacement detection is the swords tag (matches every modded sword that opts in too).
        return is.isDamageableItem() && (is.getItem() instanceof ShearsItem || is.is(ItemTags.SWORDS));
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
        return itemHandler.getStackInSlot(0).isEmpty() ? "No Tool" : this.areConditionsMet() ? "Operational" : "Invalid Item";
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

}
