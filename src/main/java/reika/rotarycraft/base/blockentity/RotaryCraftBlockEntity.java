/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.registry.TileEnum;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.level.ReikaBlockHelper;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaThermoHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.BasicMachine;
import reika.rotarycraft.registry.MachineRegistry;

public abstract class RotaryCraftBlockEntity extends BlockEntityBase implements BasicMachine, MenuProvider {

    /**
     * Rotational position.
     */
    public float phi = 0;

    public boolean isFlipped = false;
    protected int tickcount = 0;
    protected StepTimer second = new StepTimer(20);
    /**
     * For emp
     */
    private boolean disabled;

    public RotaryCraftBlockEntity(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    public int getTick() {
        return tickcount;
    }


    @Override
    protected abstract void animateWithTick(Level world, BlockPos pos);

    public final int getMachineIndex() {
        return this.getMachine().ordinal();
    }

    public abstract MachineRegistry getMachine();

    public final MachineRegistry getMachine(Direction dir) {
        BlockEntity te = getAdjacentBlockEntity(dir);
        return te instanceof RotaryCraftBlockEntity ? ((RotaryCraftBlockEntity) te).getMachine() : null;
    }

    public final void giveNoSuperWarning() {
        RotaryCraft.LOGGER.error("BlockEntity " + this.getName() + " does not call super()!");
        ReikaChatHelper.write("BlockEntity " + this.getName() + " does not call super()!");
    }

    public abstract boolean hasModelTransparency();

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        if (isFlipped)
            NBT.putBoolean("flip", isFlipped);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        isFlipped = NBT.getBoolean("flip");
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("tick", tickcount);
        tag.putBoolean("emp", disabled);
    }

    @Override
    public void load(CompoundTag nbt) {
        super.load(nbt);
        tickcount = nbt.getInt("tick");
        disabled = nbt.getBoolean("emp");
    }


    public boolean isShutdown() {
        return disabled;
    }


    public void onEMP() {
        disabled = true;
        if (this instanceof BlockEntityIOMachine) {
            BlockEntityIOMachine io = (BlockEntityIOMachine) this;
            io.power = 0;
            io.torque = 0;
            io.omega = 0;
        }
    }

    public void onRedirect() {

    }

    public double heatEnergyPerDegree() {
        return ReikaThermoHelper.STEEL_HEAT * ReikaBlockHelper.getBlockVolume(level, getBlockPos()) * ReikaEngLibrary.rhoiron;
    }

    public boolean matchMachine(BlockGetter world, BlockPos pos) {
        Block id = world.getBlockState(pos).getBlock();
        Block id2 = this.getBlockEntityBlockID();
        return id2 == id;
    }

    public final TileEnum getMachine(Level world, BlockPos pos) {
        return MachineRegistry.getMachine(world, pos);
    }

    @Override
    public Component getDisplayName() {
        return Component.literal(getTEName());
    }

    @Nullable
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return null;
    }
}
