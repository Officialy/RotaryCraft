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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.LocationTarget;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntityItemCannon extends InventoriedPowerReceiver implements DiscreteFunction, ConditionalOperation, LocationTarget {

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
        if (is == null)
            return;
        if (world.isClientSide)
            return;
        BlockEntityItemCannon te = this.getTargetTE();
        if (te == null) {
            //ReikaChatHelper.write("Item Cannon At "+xCoord+", "+yCoord+", "+zCoord+" has an invalid target!");
            //ReikaChatHelper.writeBlockAtCoords(world, target[0], target[1], target[2])
            //ReikaJavaLibrary.pConsole(this);
            return;
        }
//        if (!ReikaInventoryHelper.hasSpaceFor(is, te, true))
//            return;
        tickcount = 0;
        //ReikaJavaLibrary.pConsole(target[0]+"   "+target[1]+"   "+target[2]);
        this.fire(world, pos, te);
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void fire(Level world, BlockPos pos, BlockEntityItemCannon te) {
        double v = 4;
        ItemStack is = this.getFirstStack();
        if (is == null)
            return;
        ItemEntity ei = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.125, pos.getZ() + 0.5, is);
        double dx = target.pos.getX() - pos.getX();
        double dy = target.pos.getY() - pos.getY();
        double dz = target.pos.getZ() - pos.getZ();
        double dd = ReikaMathLibrary.py3d(dx, dy, dz);
        ei.xo = dx / dd * v;
        ei.yo = dy / dd * v;
        ei.zo = dz / dd * v;
        ei.setPickUpDelay(10);
        ei.lifespan = 5;
        if (!world.isClientSide)
            world.addFreshEntity(ei);
        //world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "DragonAPI.rand.explode", 1, 1);
        int num = power >= STACKPOWER ? is.getCount() : 1;
        int slot = ReikaInventoryHelper.locateInInventory(is, itemHandler, false);
        if (slot == -1)
            return;
        int left = ReikaInventoryHelper.addToInventoryWithLeftover(ReikaItemHelper.getSizedItemStack(is, num), te.itemHandler);
//         ReikaInventoryHelper.decrStack(slot, this, num - left);
    }

    private BlockEntityItemCannon getTargetTE() {
        BlockEntity te = target.getBlockEntity();
        return te instanceof BlockEntityItemCannon ? (BlockEntityItemCannon) te : null;
    }

    private ItemStack getFirstStack() {
        for (int i = 0; i < itemHandler.getSlots(); i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                return itemHandler.getStackInSlot(i).copy();
            }
        }
        return null;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
//        if (NBT.contains("target"))
        // target = WorldLocation.load("target", NBT);
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
        this.syncAllData(true);
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
        return 9;
    }
}
