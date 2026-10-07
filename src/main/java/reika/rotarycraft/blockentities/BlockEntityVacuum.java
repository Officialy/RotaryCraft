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
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.transfer.ResourceHandler;
import net.neoforged.neoforge.transfer.TransferPreconditions;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.transaction.SnapshotJournal;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import reika.dragonapi.interfaces.blockentity.HasFluidResourceHandler;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;

import reika.dragonapi.DragonAPI;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.interfaces.blockentity.XPProducer;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.modinteract.ReikaXPFluidHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.event.VacuumItemAbsorbEvent;
import reika.rotarycraft.api.event.VacuumXPAbsorbEvent;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;

import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

import java.util.List;

public class BlockEntityVacuum extends InventoriedPowerReceiver implements RangedEffect, BreakAction, HasItemHandler, HasFluidResourceHandler {

    public static final int FALLOFF = Math.min(524288, ReikaMathLibrary.ceil2exp(Math.max(1024, ConfigRegistry.VACPOWER.getValue())));
    public boolean equidistant = true;
    public boolean suckIfFull = true;
    private int experience = 0;
    private final ResourceHandler<FluidResource> fluidHandler = new XPFluidHandler();

    private static int xpFluidRatio() {
        FluidStack unit = ReikaXPFluidHelper.getFluid();
        return unit == null ? 0 : unit.getAmount();
    }

    @Override
    public ResourceHandler<FluidResource> getFluidHandler(Direction side) {
        return xpFluidRatio() > 0 ? fluidHandler : null;
    }

    private final class XPFluidHandler extends SnapshotJournal<Integer> implements ResourceHandler<FluidResource> {
        @Override public int size() { return 1; }
        @Override public FluidResource getResource(int index) {
            java.util.Objects.checkIndex(index, 1);
            return experience > 0 && xpFluidRatio() > 0
                    ? FluidResource.of(ReikaXPFluidHelper.getFluidType()) : FluidResource.EMPTY;
        }
        @Override public long getAmountAsLong(int index) {
            java.util.Objects.checkIndex(index, 1);
            return (long) experience * xpFluidRatio();
        }
        @Override public long getCapacityAsLong(int index, FluidResource resource) {
            java.util.Objects.checkIndex(index, 1);
            return resource.isEmpty() || isValid(index, resource) ? Integer.MAX_VALUE : 0;
        }
        @Override public boolean isValid(int index, FluidResource resource) {
            java.util.Objects.checkIndex(index, 1);
            return xpFluidRatio() > 0 && !resource.isEmpty()
                    && resource.equals(FluidResource.of(ReikaXPFluidHelper.getFluidType()));
        }
        @Override public int insert(int index, FluidResource resource, int amount, TransactionContext transaction) {
            java.util.Objects.checkIndex(index, 1);
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
            return 0;
        }
        @Override public int extract(int index, FluidResource resource, int amount, TransactionContext transaction) {
            java.util.Objects.checkIndex(index, 1);
            TransferPreconditions.checkNonEmptyNonNegative(resource, amount);
            if (!isValid(index, resource) || amount == 0) return 0;
            int ratio = xpFluidRatio();
            int xp = Math.min(experience, amount / ratio);
            if (xp <= 0) return 0;
            updateSnapshots(transaction);
            experience -= xp;
            return xp * ratio;
        }
        @Override protected Integer createSnapshot() { return experience; }
        @Override protected void revertToSnapshot(Integer snapshot) { experience = snapshot; }
        @Override protected void onRootCommit(Integer originalState) { setChanged(); }
    }
    private boolean isFull = false;

    /**
     * Exposes the collected-items inventory so an adjacent hopper (or chest/pipe) can pull the
     * vacuumed drops out — the whole vacuum inventory is output, so it maps straight to the
     * ManagedItemHandler. This is what lets "vacuum → hopper → grinder/chest" automation work.
     */
    @Override
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getItemHandler() {
        return itemHandler;
    }

    public BlockEntityVacuum(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.VACUUM.get(), pos, state);
    }

    public int getExperience() {
        return experience;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.VACUUM.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getSummativeSidedPower();
        if (world.isClientSide())
            return;
        tickcount++;
        if (power < MINPOWER)
            return;
        power = (long) torque * (long) omega;
        if (tickcount < 2)
            return;
        tickcount = 0;
        if (suckIfFull || !isFull) {
            this.suck(world, pos);
            this.absorb(world, pos);
            this.transfer(world, pos);
        }
    }

    @Override
    protected void onInventoryChanged(int slot) {
        isFull = false;
    }

    private void transfer(Level world, BlockPos pos) {
        for (int i = 2; i < 6; i++) {
            Direction dir = Direction.values()[i];
            BlockEntity te = getAdjacentBlockEntity(dir);
            // 1.21.5: BlockEntity.getCapability(ForgeCapabilities.ITEM_HANDLER) replaced by
            // Capabilities.ItemHandler.BLOCK lookup; treat any non-vacuum BE with adjacent
            // access as eligible until the new capability lookup is wired through.
            if (te != null && !(te instanceof BlockEntityVacuum)) {
                int size = itemHandler.getSlots();
                for (int k = 0; k < size; k++) {
                    ItemStack inslot = itemHandler.getStackInSlot(k);
                    if (!inslot.isEmpty()) {
                        boolean cansuck = true;
                        if (te instanceof HasItemHandler)
                            cansuck = true;//todo ((IItemHandler) te).canExtractItem(k, inslot, dir.getOpposite().ordinal());
                        if (cansuck) {
                            if (this.canSuckStacks() && ReikaInventoryHelper.addToIInv(inslot.copy(), itemHandler)) {
                                itemHandler.setStackInSlot(k, ItemStack.EMPTY);
                            } else {
                                int newsize = inslot.getCount() - 1;
                                ItemStack is2 = ReikaItemHelper.getSizedItemStack(inslot, 1);
                                ItemStack is3 = ReikaItemHelper.getSizedItemStack(inslot, newsize);
                                if (newsize <= 0)
                                    is3 = ItemStack.EMPTY;
                                if (ReikaInventoryHelper.addToIInv(is2, itemHandler)) {
                                    itemHandler.setStackInSlot(k, is3);
                                }
                            }
                        }
                    }
                }
            }
            if (te instanceof XPProducer xpm) {
                experience += xpm.getXP();
                xpm.clearXP();
            }
        }
    }

    private boolean canSuckStacks() {
        return power / MINPOWER >= 4;
    }

    public void spawnXP() {
        ReikaWorldHelper.splitAndSpawnXP(level, worldPosition.getX() - 1 + 2 * DragonAPI.rand.nextFloat(), worldPosition.getY() + 2 * DragonAPI.rand.nextFloat(), worldPosition.getZ() - 1 + 2 * DragonAPI.rand.nextFloat(), experience);
        experience = 0;
    }

    private void suck(Level world, BlockPos pos) {
        AABB box = this.getBox(world, pos);

        ///Do not merge these, they have slightly different code!
        List<Entity> inbox = world.getEntitiesOfClass(Entity.class, box, e -> e instanceof ItemEntity || e instanceof ExperienceOrb);
        double v = Math.max(1, power / 1048576D);
        for (Entity ent : inbox) {
            if (ent.tickCount > 5) {
                //Vec3 i2vac = ReikaVectorHelper.getVec2Pt(ent.getY, ent.getY(), ent.posZ, x+0.5, y+0.5, z+0.5);
                //if (ReikaWorldHelper.canBlockSee(world, pos, ent.getY, ent.getY(), ent.posZ, this.getRange()+2)) {
                if (true) {
                    double dx = (worldPosition.getX() + 0.5 - ent.getX());
                    double dy = (worldPosition.getY() + 0.5 - ent.getY());
                    double dz = (worldPosition.getZ() + 0.5 - ent.getZ());
                    double ddt = ReikaMathLibrary.py3d(dx, dy, dz);
                    if (ent.tickCount > 50 && ddt > 1.5 && ent.tickCount % 400 < 80) { //For routing around objects
                        double t = this.tickcount / 25D;
                        double r = 2.875;//+1*ReikaMathLibrary.cosInterpolation(0, 1, Math.sin(t/2));
                        dx += r * Math.cos(t);
                        dz += r * Math.sin(t);
                    }
                    double vx = v * dx / ddt / ddt;
                    double vy = v * dy / ddt / ddt / 2;
                    double vz = v * dz / ddt / ddt;
                    double vmax = 0.125;
                    vx = Mth.clamp(vx, -vmax, vmax);
                    vy = Mth.clamp(vy, -vmax, vmax);
                    vz = Mth.clamp(vz, -vmax, vmax);
//               todo ent.motionX += vx;
//                    ent.motionY += vy;
//                    ent.motionZ += vz;
                    var ee = ent.getDeltaMovement().y;
                    if (ent.getY() < worldPosition.getY())
                        ent.setDeltaMovement(ent.getDeltaMovement().x, ee += 0.125, ent.getDeltaMovement().z);
//                    if (!world.isClientSide())
//                        ent.velocityChanged = true;
                }
            }
        }

    }

    private void absorb(Level world, BlockPos pos) {
        if (world.isClientSide())
            return;
        boolean suck = false;
        AABB close = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).inflate(0.25D, 0.25D, 0.25D);
        List<ItemEntity> closeitems = world.getEntitiesOfClass(ItemEntity.class, close);
        for (ItemEntity ent : closeitems) {
            if (!ent.hasPickUpDelay()) {
                ItemStack original = ent.getItem().copy();
                ItemStack remainder = original.copy();
                // Merge occupied slots first, then use empty slots. Each insertion commits its actual amount.
                for (int pass = 0; pass < 2 && !remainder.isEmpty(); pass++) {
                    for (int slot = 0; slot < itemHandler.getSlots() && !remainder.isEmpty(); slot++) {
                        if (itemHandler.getStackInSlot(slot).isEmpty() != (pass == 1)) continue;
                        remainder = itemHandler.insertItem(slot, remainder, false);
                    }
                }
                int absorbed = original.getCount() - remainder.getCount();
                if (absorbed == 0) continue;
                suck = true;
                if (remainder.isEmpty()) ent.discard();
                else ent.setItem(remainder);
                setChanged();
                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.LAVA_POP, SoundSource.BLOCKS, 0.1F + 0.5F * DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), false);
                NeoForge.EVENT_BUS.post(new VacuumItemAbsorbEvent(this, original.copyWithCount(absorbed)));
            } else {
                suck = true;
            }
        }
        isFull = !suck;
        List<ExperienceOrb> closeorbs = world.getEntitiesOfClass(ExperienceOrb.class, close);
        for (ExperienceOrb xp : closeorbs) {
            int val = xp.getValue();
            experience += val;
            if (world instanceof ServerLevel sl) xp.kill(sl);
            world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.EXPERIENCE_ORB_PICKUP, SoundSource.BLOCKS, 0.1F, 0.5F * ((DragonAPI.rand.nextFloat() - DragonAPI.rand.nextFloat()) * 0.7F + 1.8F), false);
            NeoForge.EVENT_BUS.post(new VacuumXPAbsorbEvent(this, val));
        }
    }

    private AABB getBox(Level world, BlockPos pos) {
        int expand = this.getRange();
        AABB base = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
        return equidistant ? base.inflate(expand, expand, expand) : base.inflate(expand, 2, expand);
    }

    public int getRange() {
        return Math.min(8 + (int) (power / FALLOFF), this.getMaxRange());
    }

    public int getContainerSize() {
        return 54;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        experience = NBT.getIntOr("xp", 0);
        equidistant = NBT.getBooleanOr("equi", false);
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("xp", experience);
        NBT.putBoolean("equi", equidistant);
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }


    @Override
    protected void animateWithTick(Level world, BlockPos pos) {

    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.VACUUM;
    }

    @Override
    public int getMaxRange() {
        return Math.max(32, ConfigRegistry.VACUUMRANGE.getValue());
    }

    @Override
    public int getRedstoneOverride() {
        return 1;//Container.calcRedstoneFromInventory(this);
    }

    @Override
    public void onEMP() {
    }

/*    @Override
    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
        return resource.getFluidID() == ReikaXPFluidHelper.getFluid().getFluidID() ? this.drain(from, resource.amount, doDrain) : null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
        FluidStack fs = experience > 0 ? ReikaXPFluidHelper.getFluid(experience) : null;
        if (fs != null) {
            if (fs.amount > maxDrain)
                fs.amount = maxDrain;
            if (ReikaXPFluidHelper.getXPForAmount(fs.amount) <= 0)
                return null;
            if (doDrain)
                experience -= ReikaXPFluidHelper.getXPForAmount(fs.amount);
        }
        return fs;
    }

    @Override
    public boolean canDrain(Direction from, Fluid fluid) {
        return ReikaXPFluidHelper.fluidsExist() && experience > 0 && fluid.equals(ReikaXPFluidHelper.getFluidType());
    }*/

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{};
//    }

    @Override
    public void breakBlock() {
        ReikaWorldHelper.splitAndSpawnXP(level, worldPosition.getX() + DragonAPI.rand.nextFloat(), worldPosition.getY() + DragonAPI.rand.nextFloat(), worldPosition.getZ() + DragonAPI.rand.nextFloat(), experience);
    }


    




    

    

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
