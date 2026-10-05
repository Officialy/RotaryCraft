/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import java.util.UUID;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.DustParticleOptions;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.alchemy.PotionContents;
import net.minecraft.world.item.alchemy.Potions;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.storage.ValueInput;
import net.minecraft.world.level.storage.ValueOutput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.transfer.*;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.TransactionContext;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.dragonapi.interfaces.blockentity.HasItemHandler;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.PoweredLiquidReceiver;
import reika.rotarycraft.data.RoCBlockTagsProvider;
import reika.rotarycraft.gui.container.machine.inventory.ContainerDefoliator;
import reika.rotarycraft.registry.*;

/** V33a bottom-powered, potion/pipe-fed vegetation decay, using native loot and protection. */
public final class BlockEntityDefoliator extends PoweredLiquidReceiver implements RangedEffect, HasItemHandler, GuiController {
    public static final int CAPACITY = 4000;
    // A ResourceHandler inventory avoids the legacy base's collision between tank isEmpty()
    // and vanilla Container.isEmpty(); native hoppers and pipes use its standard capability.
    public final ManagedItemHandler itemHandler = new ManagedItemHandler(2) {
        @Override public boolean isValid(int slot, ItemResource item) { return isItemValidForSlot(slot, item.toStack()); }
        @Override protected void onContentsChanged(int slot) { setChanged(); }
        @Override public void deserialize(ValueInput input) {
            var loaded = input.read(VALUE_IO_KEY, codec);
            for (int slot = 0; slot < 2; slot++) stacks.set(slot, loaded.isPresent() && slot < loaded.get().size() ? loaded.get().get(slot) : ItemStack.EMPTY);
        }
    };
    private final ResourceHandler<ItemResource> automation = new DelegatingResourceHandler<>(() -> itemHandler) {
        @Override public int extract(int slot, ItemResource item, int amount, TransactionContext tx) {
            TransferPreconditions.checkNonEmptyNonNegative(item, amount); java.util.Objects.checkIndex(slot, size());
            return canExtractItem(slot, item.toStack(), 0) ? super.extract(slot, item, amount, tx) : 0;
        }
        @Override public int extract(ItemResource item, int amount, TransactionContext tx) {
            TransferPreconditions.checkNonEmptyNonNegative(item, amount);
            int extracted = 0;
            for (int slot = 0; slot < size() && extracted < amount; slot++) extracted += extract(slot, item, amount - extracted, tx);
            return extracted;
        }
    };
    public BlockEntityDefoliator(BlockPos pos, BlockState state) { super(RotaryBlockEntities.DEFOLIATOR.get(), pos, state); }
    @Override public void preRemoveSideEffects(BlockPos pos, BlockState state) {
        if (level != null && !level.isClientSide()) {
            for (int slot = 0; slot < 2; slot++) {
                net.minecraft.world.Containers.dropItemStack(level, pos.getX(), pos.getY(), pos.getZ(), getStackInSlot(slot));
                itemHandler.setStackInSlot(slot, ItemStack.EMPTY);
            }
        }
        super.preRemoveSideEffects(pos, state);
    }
    @Override public ManagedItemHandler getItemHandler() { return itemHandler; }
    @Override public ResourceHandler<ItemResource> getAutomationItemHandler() { return automation; }
    public int getContainerSize() { return 2; }
    public ItemStack getStackInSlot(int slot) { return itemHandler.getStackInSlot(slot); }
    public void setInventorySlotContents(int slot, ItemStack stack) { itemHandler.setStackInSlot(slot, stack); }
    public int getInventoryStackLimit() { return 64; }
    public ItemStack decrStackSize(int slot, int count) { return itemHandler.extractItem(slot, count, false); }
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        // V33a checks the base potion metadata, including stacks with custom effects.
        return stack.is(Items.POTION) && stack.getOrDefault(DataComponents.POTION_CONTENTS, PotionContents.EMPTY)
                .potion().filter(potion -> potion.is(Potions.POISON)).isPresent();
    }
    public boolean canExtractItem(int slot, ItemStack stack, int side) { return stack.is(Items.GLASS_BOTTLE); }
    @Override protected void saveAdditional(ValueOutput output) { super.saveAdditional(output); itemHandler.serialize(output.child("ItemsRaw")); }
    @Override protected void loadAdditional(ValueInput input) { super.loadAdditional(input); itemHandler.deserialize(input.childOrEmpty("ItemsRaw")); }
    @Override public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (!(world instanceof ServerLevel)) return;
        getPowerBelow();
        consumePotions();
        if (tank.isEmpty()) return;
        int range = getRange();
        for (int pass = 0, count = getNumberPasses(); pass < count && !tank.isEmpty(); pass++) {
            var random = world.getRandom();
            decay(pos.offset(random.nextInt(2 * range + 1) - range, random.nextInt(2 * range + 1) - range, random.nextInt(2 * range + 1) - range));
        }
    }
    public int getNumberPasses() { return power < MINPOWER || omega <= 0 ? 0 : 2 * (int)Math.sqrt(omega); }
    public boolean consumePotions() {
        ItemStack potion = getStackInSlot(0), bottles = getStackInSlot(1);
        if (level == null || level.isClientSide() || !isItemValidForSlot(0, potion) || !tank.canTakeIn(RotaryFluids.POISON.get(), 1000)
                || !bottles.isEmpty() && (!bottles.is(Items.GLASS_BOTTLE) || bottles.getCount() >= bottles.getMaxStackSize())) return false;
        // Reserve both chemical capacity and the returned bottle before consuming anything.
        tank.addLiquid(1000, RotaryFluids.POISON.get()); potion.shrink(1); itemHandler.setStackInSlot(0, potion);
        if (bottles.isEmpty()) bottles = new ItemStack(Items.GLASS_BOTTLE); else bottles.grow(1);
        itemHandler.setStackInSlot(1, bottles); setChanged(); return true;
    }
    public boolean decay(BlockPos target) {
        if (!(level instanceof ServerLevel server) || tank.isEmpty() || !server.hasChunkAt(target)) return false;
        BlockState state = server.getBlockState(target);
        if (!state.is(RoCBlockTagsProvider.DEFOLIATOR_TARGETS)) return false;
        ServerPlayer owner = getServerPlacer();
        if (owner == null) owner = ReikaPlayerAPI.getFakePlayerByNameAndUUID(server, "[RotaryCraft]", UUID.nameUUIDFromBytes("[RotaryCraft]".getBytes(java.nio.charset.StandardCharsets.UTF_8)));
        if (!ReikaPlayerAPI.playerCanBreakAt(server, target, state, owner) || !server.getBlockState(target).equals(state)) return false;
        var drops = Block.getDrops(state, server, target, server.getBlockEntity(target), owner, ItemStack.EMPTY);
        if (!server.setBlock(target, Blocks.AIR.defaultBlockState(), 3)) return false;
        server.levelEvent(null, 2001, target, Block.getId(state));
        for (ItemStack drop : drops) Block.popResource(server, target, drop);
        for (LivingEntity entity : server.getEntitiesOfClass(LivingEntity.class, new AABB(target).inflate(3))) {
            entity.addEffect(new MobEffectInstance(MobEffects.POISON, 50, 3)); entity.hurtServer(server, entity.damageSources().generic(), .5F);
        }
        // Effects are centered on the target and do not require a loaded machine block entity.
        ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.DEFOLIATOR.ordinal(), server, target, 64, target.getX(), target.getY(), target.getZ());
        tank.removeLiquid(1); setChanged(); return true;
    }
    public static void onBlockBreak(Level world, BlockPos target) {
        if (!world.isClientSide()) return;
        // Legacy redstone RGB (0,20,0): green saturates the modern RGB24 channel.
        var dust = new DustParticleOptions(0x00FF00, 1);
        for (int x = -3; x <= 3; x++) for (int y = -3; y <= 3; y++) for (int z = -3; z <= 3; z++)
            world.addParticle(dust, target.getX() + x + .5 + world.getRandom().nextDouble() * 5 - 2.5,
                    target.getY() + y + .5 + world.getRandom().nextDouble() * 5 - 2.5, target.getZ() + z + .5 + world.getRandom().nextDouble() * 5 - 2.5, 0, 0, 0);
    }
    public int getPoisonScaled(int size) { return (int)((long)getLiquidLevel() * size / CAPACITY); }
    @Override public int getRange() { return torque <= 0 ? 0 : Math.min(getMaxRange(), (int)(8 * (Math.log(torque) / Math.log(2)))); }
    @Override public int getMaxRange() { return 128; }
    @Override public Fluid getInputFluid() { return null; } // Original accepts two different chemical types.
    @Override public boolean isValidFluid(Fluid fluid) { return fluid == RotaryFluids.POISON.get() || fluid == RotaryFluids.CHLORINE.get(); }
    @Override public boolean canReceiveFrom(Direction side) { return side.getAxis().isHorizontal(); }
    @Override public int getCapacity() { return CAPACITY; }
    @Override public boolean canConnectToPipe(MachineRegistry pipe) { return pipe.isStandardPipe(); }
    @Override public boolean hasAnInventory() { return true; }
    @Override public boolean hasATank() { return true; }
    @Override public MachineRegistry getMachine() { return MachineRegistry.DEFOLIATOR; }
    @Override protected String getTEName() { return "defoliator"; }
    @Override public Block getBlockEntityBlockID() { return RotaryBlocks.DEFOLIATOR.get(); }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public int getRedstoneOverride() { return 0; }
    @Override protected void animateWithTick(Level world, BlockPos pos) { if (!isInWorld()) phi = 0; else phi += Math.pow(Math.log(omega + 1D) / Math.log(2), 1.05); }
    @Override public Component getDisplayName() { return Component.translatable("block.rotarycraft.defoliator"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) { return new ContainerDefoliator(id, inventory, this); }
}
