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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.WorldlyContainer;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.LocationTarget;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntityItemCannon extends InventoriedPowerReceiver implements DiscreteFunction, ConditionalOperation, LocationTarget, HasItemHandler, WorldlyContainer {

    public static final int STACKPOWER = 524288;
    private WorldLocation target;

    public BlockEntityItemCannon(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.ITEM_CANNON.get(), pos, state);
    }

    //@Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return j == 0;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.ITEMCANNON;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.ITEM_CANNON.get();
    }

    //@Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getPowerBelow();
        if (power < MINPOWER)
            return;
        if (tickcount < this.getOperationTime())
            return;
        ItemStack is = this.getFirstStack();
        if (is.isEmpty())
            return;
        if (world.isClientSide())
            return;
        BlockEntityItemCannon te = this.getTargetTE();
        if (te == null) {
            //ReikaChatHelper.write("Item Cannon At "+xCoord+", "+yCoord+", "+zCoord+" has an invalid target!");
            //ReikaChatHelper.writeBlockAtCoords(world, target[0], target[1], target[2])
            //ReikaJavaLibrary.pConsole(this);
            return;
        }
        // The original cannon waits until the entire first stack fits, even in single-item mode.
        if (!ReikaInventoryHelper.hasSpaceFor(is, te.itemHandler, true))
            return;
        tickcount = 0;
        //ReikaJavaLibrary.pConsole(target[0]+"   "+target[1]+"   "+target[2]);
        this.fire(world, pos, te);
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void fire(Level world, BlockPos pos, BlockEntityItemCannon te) {
        if (world.isClientSide() || te == null || te == this || target == null)
            return;
        double v = 4;
        ItemStack is = this.getFirstStack();
        if (is.isEmpty() || !ReikaInventoryHelper.hasSpaceFor(is, te.itemHandler, true))
            return;
        int num = power >= STACKPOWER ? is.getCount() : 1;
        int slot = ReikaInventoryHelper.locateInInventory(is, itemHandler, false);
        if (slot == -1)
            return;
        int left = ReikaInventoryHelper.addToInventoryWithLeftover(is.copyWithCount(num), te.itemHandler);
        int transferred = num - left;
        if (transferred <= 0)
            return;
        ReikaInventoryHelper.decrStack(slot, itemHandler, transferred);
        ItemEntity ei = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.125, pos.getZ() + 0.5, is);
        double dx = target.pos.getX() - pos.getX();
        double dy = target.pos.getY() - pos.getY();
        double dz = target.pos.getZ() - pos.getZ();
        double dd = ReikaMathLibrary.py3d(dx, dy, dz);
        ei.setDeltaMovement(dd > 0 ? new Vec3(dx / dd * v, dy / dd * v, dz / dd * v) : Vec3.ZERO);
        ei.setPickUpDelay(10);
        ei.lifespan = 5;
        world.addFreshEntity(ei);
        ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.GENERIC_EXPLODE.value(), 1, 1);
    }

    private BlockEntityItemCannon getTargetTE() {
        BlockEntity te = target != null && level != null ? target.getBlockEntity(level) : null;
        return te instanceof BlockEntityItemCannon cannon && cannon != this ? cannon : null;
    }

    private ItemStack getFirstStack() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return itemHandler.getStackInSlot(i).copy();
            }
        }
        return ItemStack.EMPTY;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        target = WorldLocation.load("target", tag);
    }

    @Override
    protected String getTEName() {
        return "itemcannon";
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        if (target != null)
            target.saveAdditional("target", tag);
    }

    @Override
    public int getRedstoneOverride() {
        if (ReikaInventoryHelper.isEmpty(itemHandler))
            return 15;
        return 0;
    }

    @Override
    public int getOperationTime() {
        return 8;
    }

    @Override
    public boolean areConditionsMet() {
        return !ReikaInventoryHelper.isEmpty(itemHandler);
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Items";
    }

    public void selectNewTarget(Level dim, BlockPos pos) {
        this.setTarget(new WorldLocation(dim, pos));
    }

    public WorldLocation getTarget() {
        return target;
    }


    @Override
    public void setTarget(WorldLocation loc) {
        target = loc;
        setChanged();
        this.syncAllData(true);
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
        return 9;
    }

    @Override public ManagedItemHandler getItemHandler() { return itemHandler; }
    @Override public boolean isEmpty() { return ReikaInventoryHelper.isEmpty(itemHandler); }
    @Override public boolean isItemValidForSlot(int slot, ItemStack is) { return true; }
    @Override public ItemStack getItem(int slot) { return itemHandler.getLiveStack(slot); }
    @Override public ItemStack removeItem(int slot, int amount) { return itemHandler.extractItem(slot, amount, false); }
    @Override public ItemStack removeItemNoUpdate(int slot) { return removeItem(slot, getItem(slot).getCount()); }
    @Override public void setItem(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack); }
    @Override public boolean stillValid(Player player) { return isPlayerAccessible(player); }
    @Override public void clearContent() {
        for (int slot = 0; slot < getContainerSize(); slot++) setItem(slot, ItemStack.EMPTY);
    }
    @Override public int[] getSlotsForFace(Direction side) {
        return java.util.stream.IntStream.range(0, getContainerSize()).toArray();
    }
    @Override public boolean canPlaceItemThroughFace(int slot, ItemStack stack, Direction side) { return true; }
    @Override public boolean canTakeItemThroughFace(int slot, ItemStack stack, Direction side) { return side == Direction.DOWN; }
}
