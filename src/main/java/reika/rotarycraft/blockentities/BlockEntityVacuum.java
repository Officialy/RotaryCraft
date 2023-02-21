///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.phys.AABB;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.interfaces.blockentity.BreakAction;
//import reika.dragonapi.interfaces.blockentity.XPProducer;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.List;
//
//public class BlockEntityVacuum extends InventoriedPowerReceiver implements RangedEffect, BreakAction, IFluidHandler {
//
//    public static final int FALLOFF = Math.min(524288, ReikaMathLibrary.ceil2exp(Math.max(1024, RotaryConfig.COMMON.VACPOWER.get())));
//    public boolean equidistant = true;
//    public boolean suckIfFull = true;
//    private int experience = 0;
//    private boolean isFull = false;
//
//    public int getExperience() {
//        return experience;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return true;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getSummativeSidedPower();
//        if (world.isClientSide)
//            return;
//        tickcount++;
//        if (power < MINPOWER)
//            return;
//        power = (long) torque * (long) omega;
//        if (tickcount < 2)
//            return;
//        tickcount = 0;
//        if (suckIfFull || !isFull) {
//            this.suck(world, pos);
//            this.absorb(world, pos);
//            this.transfer(world, pos);
//        }
//    }
//
//    @Override
//    protected void onInventoryChanged(int slot) {
//        isFull = false;
//    }
//
//    private void transfer(Level world, BlockPos pos) {
//        for (int i = 2; i < 6; i++) {
//            Direction dir = Direction.values()[i];
//            BlockEntity te = getAdjacentBlockEntity(dir);
//            if (te instanceof Container && !(te instanceof BlockEntityVacuum)) {
//                Container ii = ((Container) te);
//                int size = ii.getContainerSize();
//                for (int k = 0; k < size; k++) {
//                    ItemStack inslot = ii.getStackInSlot(k);
//                    if (inslot != null) {
//                        boolean cansuck = true;
//                        if (te instanceof IItemHandler)
//                            cansuck = ((IItemHandler) te).canExtractItem(k, inslot, dir.getOpposite().ordinal());
//                        if (cansuck) {
//                            if (this.canSuckStacks() && ReikaInventoryHelper.addToIInv(inslot.copy(), this)) {
//                                ii.setInventorySlotContents(k, null);
//                            } else {
//                                int newsize = inslot.getCount() - 1;
//                                ItemStack is2 = ReikaItemHelper.getSizedItemStack(inslot, 1);
//                                ItemStack is3 = ReikaItemHelper.getSizedItemStack(inslot, newsize);
//                                if (newsize <= 0)
//                                    is3 = null;
//                                if (ReikaInventoryHelper.addToIInv(is2, this)) {
//                                    ii.setInventorySlotContents(k, is3);
//                                }
//                            }
//                        }
//                    }
//                }
//            }
//            if (te instanceof XPProducer) {
//                XPProducer xpm = (XPProducer) te;
//                experience += xpm.getXP();
//                xpm.clearXP();
//            }
//        }
//    }
//
//    private boolean canSuckStacks() {
//        return power / MINPOWER >= 4;
//    }
//
//    public void spawnXP() {
//        ReikaWorldHelper.splitAndSpawnXP(level, xCoord - 1 + 2 * DragonAPI.rand.nextFloat(), yCoord + 2 * DragonAPI.rand.nextFloat(), zCoord - 1 + 2 * DragonAPI.rand.nextFloat(), experience);
//        experience = 0;
//    }
//
//    private void suck(Level world, BlockPos pos) {
//        AABB box = this.getBox(world, pos);
//
//        ///Do not merge these, they have slightly different code!
//        List<Entity> inbox = world.selectEntitiesWithinAABB(Entity.class, box, ReikaEntityHelper.itemOrXPSelector);
//        double v = Math.max(1, power / 1048576D);
//        for (Entity ent : inbox) {
//            if (ent.tickCount > 5) {
//                //Vec3 i2vac = ReikaVectorHelper.getVec2Pt(ent.getY, ent.getY(), ent.posZ, x+0.5, y+0.5, z+0.5);
//                //if (ReikaWorldHelper.canBlockSee(world, pos, ent.getY, ent.getY(), ent.posZ, this.getRange()+2)) {
//                if (true || ReikaWorldHelper.canBlockSee(world, pos, ent.getY, ent.getY(), ent.posZ, this.getRange() + 2)) {
//                    double dx = (x + 0.5 - ent.getY);
//                    double dy = (y + 0.5 - ent.getY());
//                    double dz = (z + 0.5 - ent.posZ);
//                    double ddt = ReikaMathLibrary.py3d(dx, dy, dz);
//                    if (ent.tickCount > 50 && ddt > 1.5 && ent.tickCount % 400 < 80) { //For routing around objects
//                        double t = this.tickcount / 25D;
//                        double r = 2.875;//+1*ReikaMathLibrary.cosInterpolation(0, 1, Math.sin(t/2));
//                        dx += r * Math.cos(t);
//                        dz += r * Math.sin(t);
//                    }
//                    double vx = v * dx / ddt / ddt;
//                    double vy = v * dy / ddt / ddt / 2;
//                    double vz = v * dz / ddt / ddt;
//                    double vmax = 0.125;
//                    vx = Mth.clamp(vx, -vmax, vmax);
//                    vy = Mth.clamp(vy, -vmax, vmax);
//                    vz = Mth.clamp(vz, -vmax, vmax);
//                    ent.motionX += vx;
//                    ent.motionY += vy;
//                    ent.motionZ += vz;
//                    if (ent.getY() < y)
//                        ent.motionY += 0.125;
//                    if (!world.isClientSide)
//                        ent.velocityChanged = true;
//                }
//            }
//        }
//
//    }
//
//    private void absorb(Level world, BlockPos pos) {
//        if (world.isClientSide)
//            return;
//        boolean suck = false;
//        AABB close = AABB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(0.25D, 0.25D, 0.25D);
//        List<ItemEntity> closeitems = world.getEntities(ItemEntity.class, close);
//        for (ItemEntity ent : closeitems) {
//            if (ent.delayBeforeCanPickup <= 0) {
//                ItemStack is = ent.getEntityItem();
//                int targetslot = this.checkForStack(is);
//                if (targetslot != -1) {
//                    if (inv[targetslot] == null)
//                        inv[targetslot] = is.copy();
//                    else
//                        inv[targetslot].getCount() += is.getCount();
//                    suck = true;
//                } else {
//                    return;
//                }
//                ent.kill();
//                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "DragonAPI.rand.pop", 0.1F + 0.5F * DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                MinecraftForge.EVENT_BUS.post(new VacuumItemAbsorbEvent(this, is != null ? is.copy() : null));
//            } else {
//                suck = true;
//            }
//        }
//        isFull = !suck;
//        List<EntityXPOrb> closeorbs = world.getEntities(EntityXPOrb.class, close);
//        for (EntityXPOrb xp : closeorbs) {
//            int val = xp.getXpValue();
//            experience += val;
//            xp.kill();
//            world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "DragonAPI.rand.orb", 0.1F, 0.5F * ((DragonAPI.rand.nextFloat() - DragonAPI.rand.nextFloat()) * 0.7F + 1.8F));
//            MinecraftForge.EVENT_BUS.post(new VacuumXPAbsorbEvent(this, val));
//        }
//    }
//
//    private int checkForStack(ItemStack is) {
//        int target = -1;
//        Item id = is.getItem();
//        //=is.getItemDamage();
//        int size = is.getCount();
//        int firstempty = -1;
//
//        for (int k = 0; k < itemHandler.getSlots(); k++) { //Find first empty slot
//            if (inv[k] == null) {
//                firstempty = k;
//                k = itemHandler.getSlots();
//            }
//        }
//        for (int j = 0; j < itemHandler.getSlots(); j++) {
//            if (inv[j] != null) {
//                if (ReikaItemHelper.areStacksCombinable(is, inv[j], Integer.MAX_VALUE)) {
//                    if (ItemStack.isSameItemSameTags(is, inv[j])) {
//                        if (inv[j].getCount() + size <= this.getInventoryStackLimit()) {
//                            target = j;
//                            j = itemHandler.getSlots();
//                        } else {
//                            int diff = is.getMaxStackSize() - inv[j].getCount();
//                            inv[j].getCount() += diff;
//                            is.getCount() -= diff;
//                        }
//                    }
//                }
//            }
//        }
//
//        if (target == -1)
//            target = firstempty;
//        return target;
//    }
//
//    private AABB getBox(Level world, BlockPos pos) {
//        int expand = this.getRange();
//        AABB base = AABB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
//        return equidistant ? base.expand(expand, expand, expand) : base.expand(expand, 2, expand);
//    }
//
//    public int getRange() {
//        return Math.min(8 + (int) (power / FALLOFF), this.getMaxRange());
//    }
//
//    public int getContainerSize() {
//        return 54;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        experience = NBT.getInt("xp");
//        equidistant = NBT.getBoolean("equi");
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("xp", experience);
//        NBT.putBoolean("equi", equidistant);
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.VACUUM;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return true;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return Math.max(32, RotaryConfig.COMMON.VACUUMRANGE.get());
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return Container.calcRedstoneFromInventory(this);
//    }
//
//    @Override
//    public void onEMP() {
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, boolean doFill) {
//        return 0;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
//        return resource.getFluidID() == ReikaXPFluidHelper.getFluid().getFluidID() ? this.drain(from, resource.amount, doDrain) : null;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
//        FluidStack fs = experience > 0 ? ReikaXPFluidHelper.getFluid(experience) : null;
//        if (fs != null) {
//            if (fs.amount > maxDrain)
//                fs.amount = maxDrain;
//            if (ReikaXPFluidHelper.getXPForAmount(fs.amount) <= 0)
//                return null;
//            if (doDrain)
//                experience -= ReikaXPFluidHelper.getXPForAmount(fs.amount);
//        }
//        return fs;
//    }
//
//    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return ReikaXPFluidHelper.fluidsExist() && experience > 0 && fluid.equals(ReikaXPFluidHelper.getFluidType());
//    }
//
////    @Override
////    public FluidTankInfo[] getTankInfo(Direction from) {
////        return new FluidTankInfo[]{};
////    }
//
//    @Override
//    public void breakBlock() {
//        ReikaWorldHelper.splitAndSpawnXP(level, xCoord + DragonAPI.rand.nextFloat(), yCoord + DragonAPI.rand.nextFloat(), zCoord + DragonAPI.rand.nextFloat(), experience);
//    }
//
//    @Override
//    public int getTanks() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return null;
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 0;
//    }
//
//    @Override
//    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
//        return false;
//    }
//
//    @Override
//    public int fill(FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(FluidStack resource, FluidAction action) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(int maxDrain, FluidAction action) {
//        return null;
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public String getName() {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
