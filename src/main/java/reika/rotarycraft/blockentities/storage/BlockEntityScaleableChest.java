/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.storage;

import java.util.ArrayList;
import java.util.Iterator;

import net.minecraft.core.BlockPos;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.interfaces.blockentity.MultiPageInventory;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerScaleChest;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Scaleable Chest is a shaft-powered mass-storage block: the number of usable slots grows with
 * the input power (up to {@value #MAXSIZE}, {@value #ROWS_PER_PAGE} rows per GUI page). It is only
 * accessible while powered, and — the signature anti-exploit — if its power supply flickers on and
 * off too often (someone trying to abuse it as free bottomless storage that dumps when unpowered)
 * it destabilises, venting smoke and eventually exploding, dropping itself. 1.7.10-faithful; the
 * animated chest lid / open-close sounds (a client-only BER flourish) are not ported.
 */
public class BlockEntityScaleableChest extends InventoriedPowerReceiver implements MultiPageInventory, MenuProvider {

    public static final int FALLOFF = 128;
    public static final int ROWS_PER_PAGE = 6;
    public static final int SLOTS_PER_PAGE = 9 * ROWS_PER_PAGE; // 54
    public static final int MAXSIZE = 972; // 18 pages
    public static final int POWERCHANGEAGE = 20; // 1s

    private final ArrayList<Integer> powerchanges = new ArrayList<>();
    private int numchanges;
    private boolean lastpower;

    public int page;

    public BlockEntityScaleableChest(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SCALECHEST.get(), pos, state);
    }

    @Override
    public boolean dropsInventoryOnBroken() {
        return false;
    }

    @Override
    public int getContainerSize() {
        return MAXSIZE;
    }

    /** The number of slots currently usable, scaled by the input power. */
    public int getNumberSlots() {
        int size = power < MINPOWER ? 9 : 9 + (int) ((power - MINPOWER) / FALLOFF);
        return Math.min(size, MAXSIZE);
    }

    public static int getMaxPages() {
        return MAXSIZE / SLOTS_PER_PAGE;
    }

    /** The number of pages currently populated (1-based count). */
    public int getMaxPage() {
        return Math.min(getMaxPages(), (int) Math.ceil(this.getNumberSlots() / (double) SLOTS_PER_PAGE));
    }

    @Override
    public int getNumberPages() {
        return this.getMaxPage();
    }

    @Override
    public int getSlotsOnPage(int page) {
        int max = this.getMaxPage();
        if (page == max - 1)
            return this.getNumberSlots() - (max - 1) * SLOTS_PER_PAGE;
        else if (page < max)
            return SLOTS_PER_PAGE;
        return 0;
    }

    @Override
    public int getCurrentPage() {
        return page;
    }

    /** Only the first {@link #getNumberSlots()} backing slots accept items; the rest are inert. */
    public boolean isItemValidForSlot(int slot, ItemStack stack) {
        return slot < this.getNumberSlots();
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return true;
    }

    public boolean isUseableByPlayer(Player ep) {
        return numchanges == 0 && power >= MINPOWER;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        this.testInconsistentPower(world, pos);
    }

    /**
     * Tracks how often the power crosses the on/off threshold; sustained flicker builds instability
     * that vents smoke, then minor pops, then a self-destructing explosion.
     */
    private void testInconsistentPower(Level world, BlockPos pos) {
        for (int i = 0; i < powerchanges.size(); i++)
            powerchanges.set(i, powerchanges.get(i) + 1);
        for (Iterator<Integer> itr = powerchanges.iterator(); itr.hasNext(); ) {
            if (itr.next() > POWERCHANGEAGE)
                itr.remove();
        }
        boolean pw = power >= MINPOWER;
        if (pw != lastpower)
            powerchanges.add(0);
        numchanges = powerchanges.size();
        lastpower = pw;

        if (world.isClientSide())
            return;

        if (numchanges > 10) {
            Block.dropResources(this.getBlockState(), world, pos);
            world.removeBlock(pos, false);
            world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 4F, Level.ExplosionInteraction.BLOCK);
        } else if (numchanges > 8) {
            this.vent(world, pos);
            if (world.getRandom().nextInt(19 - numchanges) == 0)
                world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 0F, Level.ExplosionInteraction.NONE);
        } else if (numchanges > 3) {
            this.vent(world, pos);
            if (world.getRandom().nextInt(11 - numchanges) == 0)
                world.playSound(null, pos, net.minecraft.sounds.SoundEvents.FIRE_EXTINGUISH,
                        net.minecraft.sounds.SoundSource.BLOCKS, 1F, 1F);
        }
    }

    private void vent(Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel sl))
            return;
        for (int i = 0; i < numchanges / 3; i++)
            sl.sendParticles(ParticleTypes.SMOKE,
                    pos.getX() + sl.getRandom().nextFloat(), pos.getY() + sl.getRandom().nextFloat(), pos.getZ() + sl.getRandom().nextFloat(),
                    1, 0, 0, 0, 0);
    }

    public int getNumPowerChanges() {
        return numchanges;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SCALECHEST;
    }

    @Override
    protected String getTEName() {
        return "chest";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SCALECHEST.get();
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
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        page = NBT.getIntOr("pg", 0);
        numchanges = NBT.getIntOr("chng", 0);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("pg", page);
        NBT.putInt("chng", numchanges);
    }

    // ==== MenuProvider: the paged GUI (page carried through the open packet) ====

    @Override
    public Component getDisplayName() {
        return Component.literal("Scaleable Chest");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerScaleChest(id, inv, this, page);
    }
}
