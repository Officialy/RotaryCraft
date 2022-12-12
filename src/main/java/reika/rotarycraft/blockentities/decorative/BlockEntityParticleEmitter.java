/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.decorative;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityParticleEmitter extends BlockEntitySpringPowered {// todo implements OneSlotMachine, InertIInv {

    public ReikaParticleHelper particleType = ReikaParticleHelper.SMOKE;
    public double pX = 0;
    public double pY = 0;
    public double pZ = 0;
    public int particlesPerTick = 3;

    public boolean useRedstone = false;

    public BlockEntityParticleEmitter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.PARTICLE.get(), pos, state);
    }

    @Override
    public int getBaseDischargeTime() {
        return 600;
    }

    public boolean canEmit(Level world, BlockPos pos) {
        if (!this.hasCoil())
            return false;
        return !useRedstone || this.hasRedstoneSignal();
    }


    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        this.updateCoil();
        if (this.canEmit(world, pos)) {
            for (int i = 0; i < particlesPerTick; i++) {
                particleType.spawnAt(world, worldPosition.getX() + DragonAPI.rand.nextDouble(), worldPosition.getY() + 2 + DragonAPI.rand.nextDouble() * 4, worldPosition.getZ() + DragonAPI.rand.nextDouble(), pX, pY, pZ);
            }
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void updateCoil() {
        if (!this.hasCoil()) {
            return;
        }
        tickcount++;
        if (tickcount > this.getUnwindTime()) {
            ItemStack is = this.getDecrementedCharged();
            inv[0] = is;
            tickcount = 0;
        }
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PARTICLE;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putInt("type", particleType.ordinal());
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        particleType = ReikaParticleHelper.values()[NBT.getInt("type")];
    }

    @Override
    protected String getTEName() {
        return null;
    }

    public boolean isParticleValid(ReikaParticleHelper p) {
        if (p == ReikaParticleHelper.DRIPLAVA)
            return false;
        if (p == ReikaParticleHelper.DRIPWATER)
            return false;
//    todo    if (p == ReikaParticleHelper.SUSPEND)
//            return false;
        return true;// todo p != ReikaParticleHelper.SNOWSHOVEL;
    }

    @Override
    public int getContainerSize() {
        return 0;
    }

    @Override
    public boolean isEmpty() {
        return false;
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

    @Override
    public int getSlots() {
        return 0;
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
        return 0;
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
}
