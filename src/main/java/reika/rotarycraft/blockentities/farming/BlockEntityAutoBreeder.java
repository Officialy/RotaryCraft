/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.farming;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.TamableAnimal;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Auto Breeder: feeds nearby animals from its 18-slot feed inventory, setting eligible adults in
 * love when they come within reach (2.4 blocks) and pathing ready animals toward the machine.
 * Range scales with power above the minimum (+1 block per 2 kW, base 8, capped by config).
 *
 * <p>26.2 port notes: {@code Animal.isFood(ItemStack)} replaces the legacy hardcoded class->item
 * feed registry, so any modern animal's real breeding food works automatically. Villager mating
 * is not ported (modern villager breeding is willingness/food driven and self-managing). Tamed-
 * animal gating (sitting pets are skipped, only tamed ones breed) is preserved.</p>
 */
public class BlockEntityAutoBreeder extends InventoriedPowerReceiver implements RangedEffect, ConditionalOperation {

    /** 2 kW per extra meter of range (legacy). */
    public static final int FALLOFF = 2048;

    public BlockEntityAutoBreeder(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.AUTOBREEDER.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getPowerBelow();

        if (power < MINPOWER || world.isClientSide())
            return;

        List<Animal> inrange = world.getEntitiesOfClass(Animal.class,
                new AABB(pos).inflate(this.getRange()));
        this.breed(world, pos, inrange);
    }

    private void breed(Level world, BlockPos pos, List<Animal> inroom) {
        boolean pathing = false;
        if (tickcount >= 20) {
            tickcount = 0;
            pathing = true;
        }
        for (Animal ent : inroom) {
            if (this.getFeedSlot(ent) < 0)
                continue;
            boolean sittingPet = ent instanceof TamableAnimal tam && tam.isOrderedToSit();
            if (!sittingPet && pathing) {
                if (this.canDirectToMate(ent))
                    ent.getNavigation().moveTo(pos.getX() + 0.5, pos.getY(), pos.getZ() + 0.5, 1.0);
                else
                    ent.getNavigation().stop();
            }
            if (!ent.isBaby() && ent.getAge() <= 0 && !ent.isInLove()
                    && ReikaMathLibrary.py3d(pos.getX() - ent.getX(), pos.getY() - ent.getY(), pos.getZ() - ent.getZ()) <= 2.4) {
                if (!(ent instanceof TamableAnimal tam2) || tam2.isTame()) {
                    ent.setInLove(null);
                    this.useFeedItem(ent);
                    if (world instanceof ServerLevel sl) {
                        int n = 1 + rand.nextInt(3);
                        sl.sendParticles(ParticleTypes.HEART, ent.getX(), ent.getY() + 0.7, ent.getZ(), n, 0.4, 0.4, 0.4, 0.02);
                    }
                }
            }
        }
    }

    private boolean canDirectToMate(Animal ent) {
        return !ent.isBaby() && ent.getAge() == 0 && !ent.isInLove();
    }

    /** First inventory slot holding this animal's real breeding food, or -1. */
    private int getFeedSlot(Animal ent) {
        for (int i = 0; i < this.getContainerSize(); i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (!is.isEmpty() && ent.isFood(is))
                return i;
        }
        return -1;
    }

    private void useFeedItem(Animal ent) {
        int slot = this.getFeedSlot(ent);
        if (slot >= 0)
            itemHandler.extractItem(slot, 1, false);
    }

    @Override
    public int getRange() {
        int range = 8 + (int) ((power - MINPOWER) / FALLOFF);
        return Math.min(range, this.getMaxRange());
    }

    @Override
    public int getMaxRange() {
        return Math.max(24, ConfigRegistry.BREEDERRANGE.getValue());
    }

    /** Idle when nothing in the inventory can feed anything (legacy IdleComparator). */
    public boolean isIdle() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return true;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    /** Exposes the feed inventory for the container. */
    public reika.dragonapi.instantiable.storage.ManagedItemHandler getFeedHandler() {
        return itemHandler;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.AUTOBREEDER;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.AUTOBREEDER.get();
    }

    @Override
    protected String getTEName() {
        return "autobreeder";
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
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return this.isIdle() ? 15 : 0;
    }

    @Override
    public boolean areConditionsMet() {
        return !this.isIdle();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Feed Items";
    }

}
