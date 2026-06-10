/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.interfaces.blockentity.InertIInv;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.api.interfaces.SurrogateBedrock;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.DifficultyEffects;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

public class BlockEntityBedrockBreaker extends InventoriedPowerReceiver implements InertIInv, DiscreteFunction {

    private int step = 1;

    public BlockEntityBedrockBreaker(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BEDROCK_BREAKER.get(), pos, state);
    }

    private Direction getFacing() {
        return getBlockState().getValue(BlockRotaryCraftMachine.FACING);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.BEDROCK_BREAKER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;

        Direction facing = this.getFacing();
        read = facing.getOpposite();
        this.getPower(false);

        if (power < MINPOWER || torque < MINTORQUE)
            return;

        if (!world.isClientSide() && tickcount >= this.getOperationTime()) {
            this.process(world, facing);
            tickcount = 0;
        }

        BlockPos head = pos.relative(facing, step);
        if (!world.getBlockState(head).isAir()) {
            for (int i = 0; i < 4; i++) {
                double px = head.getX() + DragonAPI.rand.nextDouble();
                double py = head.getY() + DragonAPI.rand.nextDouble();
                double pz = head.getZ() + DragonAPI.rand.nextDouble();
                ReikaParticleHelper.CRITICAL.spawnAt(world, px, py, pz);
            }
        }
    }

    private void process(Level world, Direction facing) {
        if (!this.hasInventorySpace())
            return;
        this.recomputeStep(world, facing);
        BlockPos head = worldPosition.relative(facing, step);
        if (world.isOutsideBuildHeight(head))
            return;
        this.grind(world, head, facing);
    }

    private void grind(Level world, BlockPos head, Direction facing) {
        BlockState state = world.getBlockState(head);
        Block b = state.getBlock();

        if (this.isBedrock(world, head, state)) {
            world.playLocalSound(head.getX() + 0.5, head.getY() + 0.5, head.getZ() + 0.5,
                    SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.5F, 0.8F + DragonAPI.rand.nextFloat() * 0.4F, false);
            world.setBlock(head, RotaryBlocks.BEDROCKSLICE.get().defaultBlockState(), 3);
            if (world.getBlockEntity(head) instanceof BlockEntityBedrockSlice slice)
                slice.setDirection(facing.getOpposite());
        } else if (b == RotaryBlocks.BEDROCKSLICE.get()) {
            if (world.getBlockEntity(head) instanceof BlockEntityBedrockSlice slice) {
                if (slice.getWear() < BlockEntityBedrockSlice.MAX_WEAR) {
                    slice.incrementWear();
                    world.playLocalSound(head.getX() + 0.5, head.getY() + 0.5, head.getZ() + 0.5,
                            SoundEvents.STONE_HIT, SoundSource.BLOCKS, 0.5F, 0.8F + DragonAPI.rand.nextFloat() * 0.4F, false);
                } else {
                    world.playLocalSound(head.getX() + 0.5, head.getY() + 0.5, head.getZ() + 0.5,
                            SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 0.5F, 0.8F + DragonAPI.rand.nextFloat() * 0.4F, false);
                    ItemStack dust = ReikaItemHelper.getSizedItemStack(RotaryItems.BEDROCK_DUST.get().getDefaultInstance(), this.getNumberDust(slice));
                    world.removeBlock(head, false);
                    this.outputDust(world, dust);
                }
            }
        } else if (b != Blocks.AIR.defaultBlockState().getBlock() && !state.isAir()
                && state.getDestroySpeed(world, head) >= 0) {
            reika.dragonapi.libraries.io.ReikaSoundHelper.playBreakSound(world, head, b);
            world.removeBlock(head, false);
        }
    }

    private int getNumberDust(BlockEntityBedrockSlice slice) {
        float f = Math.min(1, slice.dustYield);
        return Math.max(1, (int) (f * DifficultyEffects.BEDROCKDUST.getInt()));
    }

    private void outputDust(Level world, ItemStack is) {
        if (this.chestCheck(is))
            return;
        if (ReikaInventoryHelper.addToIInv(is, itemHandler))
            return;
        BlockPos drop = worldPosition.above();
        net.minecraft.world.entity.item.ItemEntity ei = new net.minecraft.world.entity.item.ItemEntity(
                world, drop.getX() + 0.5, drop.getY() + 0.25, drop.getZ() + 0.5, is);
        ei.setDeltaMovement(0, 0.15, 0);
        ei.setDefaultPickUpDelay();
        world.addFreshEntity(ei);
    }

    private boolean chestCheck(ItemStack is) {
        if (is.isEmpty() || level.isClientSide())
            return false;
        for (Direction dir : Direction.values()) {
            BlockEntity te = this.getAdjacentBlockEntity(dir);
            if (te instanceof Container c && ReikaInventoryHelper.addToIInv(is.copy(), c))
                return true;
        }
        return false;
    }

    private boolean isBedrock(Level world, BlockPos pos, BlockState state) {
        Block b = state.getBlock();
        if (b == Blocks.BEDROCK)
            return true;
        return b instanceof SurrogateBedrock sb && sb.isBedrock(world, pos);
    }

    private void recomputeStep(Level world, Direction facing) {
        int i = 1;
        while (i < 256) {
            BlockPos p = worldPosition.relative(facing, i);
            if (world.isOutsideBuildHeight(p)) {
                step = i;
                return;
            }
            if (!reika.dragonapi.libraries.level.ReikaWorldHelper.softBlocks(world, p)) {
                step = i;
                return;
            }
            i++;
        }
        step = i;
    }

    private boolean hasInventorySpace() {
        ItemStack in = itemHandler.getStackInSlot(0);
        if (in.isEmpty())
            return true;
        if (!ReikaItemHelper.matchStacks(in, RotaryItems.BEDROCK_DUST.get().getDefaultInstance()))
            return false;
        return in.getCount() + DifficultyEffects.BEDROCKDUST.getInt() <= in.getMaxStackSize();
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BEDROCKBREAKER;
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
    public int getOperationTime() {
        return DurationRegistry.BEDROCK.getOperationTime(omega);
    }

    @Override
    public void onEMP() {
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("step", step);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        step = NBT.getIntOr("step", 1);
    }

    @Override
    protected String getTEName() {
        return "bedrockbreaker";
    }

    public int getStep() {
        return step;
    }

    /* Container — single internal bedrock-dust slot, not exposed to automation (InertIInv). */
    @Override
    public int getContainerSize() {
        return 1;
    }

    @Override
    public boolean isEmpty() {
        return itemHandler.getStackInSlot(0).isEmpty();
    }

    @Override
    public ItemStack getItem(int slot) {
        return itemHandler.getStackInSlot(slot);
    }

    @Override
    public ItemStack removeItem(int slot, int count) {
        return itemHandler.extractItem(slot, count, false);
    }

    @Override
    public ItemStack removeItemNoUpdate(int slot) {
        ItemStack is = itemHandler.getStackInSlot(slot);
        itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
        return is;
    }

    @Override
    public void setItem(int slot, ItemStack stack) {
        itemHandler.setStackInSlot(slot, stack);
    }

    @Override
    public boolean stillValid(Player player) {
        return false;
    }

    @Override
    public void clearContent() {
        itemHandler.setStackInSlot(0, ItemStack.EMPTY);
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
