/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.ShaftPowerBus;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.base.blockentity.BlockEntityInventoryIOMachine;
import reika.rotarycraft.registry.GearboxTypes;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Collection;

public class BlockEntityPowerBus extends BlockEntityInventoryIOMachine implements BreakAction, ComplexIO {

    public BlockEntityPowerBus(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.POWERBUS.get(), pos, state);
    }

    private static final boolean ALLOW_VERTICAL_CHAINS = true;

    private boolean[] modes = new boolean[4];

    private Direction inputSide;

    private ShaftPowerBus bus;

    private int hubX = Integer.MIN_VALUE;
    private int hubY = Integer.MIN_VALUE;
    private int hubZ = Integer.MIN_VALUE;


    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    protected String getTEName() {
        return "powerbus";
    }

    @Override
    public net.minecraft.world.level.block.Block getBlockEntityBlockID() {
        return reika.rotarycraft.registry.RotaryBlocks.POWERBUS.get();
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
    public MachineRegistry getMachine() {
        return MachineRegistry.POWERBUS;
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
    public void updateEntity(Level world, BlockPos pos) {
        if (bus == null && this.hasHubCoordinates()) {
            MachineRegistry m = MachineRegistry.getMachine(world, new BlockPos(hubX, hubY, hubZ));
            if (m == MachineRegistry.BUSCONTROLLER) {
                BlockEntityBusController hub = world.getBlockEntity(new BlockPos(hubX, hubY, hubZ)) instanceof BlockEntityBusController h ? h : null;
                if (hub != null) {
                    ShaftPowerBus bus = hub.getBus();
                    this.configureBusData(bus);
                }
            }
        }
        for (int i = 2; i < 6; i++) {
            Direction dir = dirs[i];
            if (this.canHaveItemInSlot(dir) && getAdjacentBlockEntity(dir) != null) {
                int speed = this.getSpeedToSide(dir);
                int torque = this.getTorqueToSide(dir);
                this.writeToPowerReceiver(dir, speed, torque);
            }
        }
    }

    public boolean canHaveItemInSlot(Direction dir) {
        MachineRegistry m = MachineRegistry.getMachine(level, worldPosition.relative(dir));
        return m != MachineRegistry.BUSCONTROLLER && m != MachineRegistry.POWERBUS;
    }

    private boolean hasHubCoordinates() {
        return hubX != Integer.MIN_VALUE && hubY != Integer.MIN_VALUE && hubZ != Integer.MIN_VALUE;
    }

    public int getAbsRatio(Direction dir) {
        if (dir.getStepY() != 0)
            return 0;
        return GearboxTypes.getRatioFromPartItem(itemHandler.getStackInSlot(dir.ordinal() - 2));
    }

    public boolean insertItem(ItemStack is, Direction side) {
        if (GearboxTypes.getRatioFromPartItem(is) > 0 && itemHandler.getStackInSlot(side.ordinal() - 2).isEmpty()) {
            itemHandler.setStackInSlot(side.ordinal() - 2, is.copyWithCount(1));
            return true;
        }
        return false;
    }

    public boolean isSideSpeedMode(Direction dir) {
        return modes[dir.ordinal() - 2];
    }

    public void setMode(Direction dir, boolean speed) {
        modes[dir.ordinal() - 2] = speed;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    public boolean canOutputToSide(Direction dir) {
        return !this.isReceivingFromSide(dir) && this.hasValidItem(dir);
    }

    public boolean isReceivingFromSide(Direction dir) {
        return dir == this.getInputSide();
    }

    private boolean hasValidItem(Direction dir) {
        return this.getAbsRatio(dir) != 0;
    }

    public long getPowerPerSide() {
        return this.getInputPower() / bus.getTotalOutputSides();
    }

    /** Output-side count for the GUI (1 if unlinked). */
    public int getTotalOutputSidesDisplay() {
        return bus != null ? Math.max(1, bus.getTotalOutputSides()) : 1;
    }

    public long getInputPower() {
        return bus != null ? bus.getInputPower() : 0;
    }

    public int getInputTorque() {
        return bus != null ? bus.getInputTorque() : 0;
    }

    public int getTorquePerSide() {
        return bus != null ? this.getInputTorque() / bus.getTotalOutputSides() : 0;
    }

    public int getTorqueToSide(Direction dir) {
        if (dir.getAxis().isVertical())
            return 0;
        int tbase = this.getTorquePerSide();
        int ratio = this.getAbsRatio(dir);
        if (ratio == 0)
            return 0;
        else {
            int trq = this.isSideSpeedMode(dir) ? tbase / ratio : tbase * ratio;
            if (this.verifyTorque(trq, dir))
                return trq;
            else {
                this.breakItem(dir);
                return 0;
            }
        }
    }

    public int getSpeedToSide(Direction dir) {
        if (bus == null)
            return 0;
        if (dir.getAxis().isVertical())
            return 0;
        int sbase = bus.getSpeed();
        int ratio = this.getAbsRatio(dir);
        if (ratio == 0)
            return 0;
        else {
            int spd = this.isSideSpeedMode(dir) ? sbase * ratio : sbase / ratio;
            if (this.verifySpeed(spd, dir))
                return spd;
            else {
                this.breakItem(dir);
                return 0;
            }
        }
    }

    private boolean verifySpeed(int speed, Direction dir) {
        ItemStack is = itemHandler.getStackInSlot(dir.ordinal() - 2);
        GearboxTypes mat = GearboxTypes.getGearUnitMaterial(is);
        return mat != null && mat.material.getLimits().maxSpeed >= speed;
    }

    private boolean verifyTorque(int torque, Direction dir) {
        ItemStack is = itemHandler.getStackInSlot(dir.ordinal() - 2);
        GearboxTypes mat = GearboxTypes.getGearUnitMaterial(is);
        return mat != null && mat.material.getLimits().maxTorque >= torque;
    }

    private void breakItem(Direction dir) {
        itemHandler.setStackInSlot(dir.ordinal() - 2, ItemStack.EMPTY);
        if (level != null)
            level.playSound(null, worldPosition, net.minecraft.sounds.SoundEvents.ITEM_BREAK.value(),
                    net.minecraft.sounds.SoundSource.BLOCKS, 2F, 1F);
    }

    @Override
    public int getContainerSize() {
        return 4;
    }

    public int getInventoryStackLimit() {
        return 1;
    }

    @Override
    public boolean canPlaceItem(int a, ItemStack b) {
        // Gear parts are placed through the GUI slots directly.
        return GearboxTypes.getRatioFromPartItem(b) > 0;
    }

    public Direction getInputSide() {
        return inputSide != null ? inputSide : Direction.EAST;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        inputSide = dirs[NBT.getIntOr("in", 0)];

        modes = ReikaArrayHelper.booleanFromByteBitflags(NBT.getByteOr("mode", (byte)0), 4);

        hubX = NBT.getIntOr("hx", 0);
        hubY = NBT.getIntOr("hy", 0);
        hubZ = NBT.getIntOr("hz", 0);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putInt("in", this.getInputSide().ordinal());

        NBT.putByte("mode", ReikaArrayHelper.booleanToByteBitflags(modes));

        NBT.putInt("hx", hubX);
        NBT.putInt("hy", hubY);
        NBT.putInt("hz", hubZ);
    }

    public void findNetwork(Level world, BlockPos pos) {
        for (int i = ALLOW_VERTICAL_CHAINS ? 0 : 2; i < 6; i++) {
            Direction dir = dirs[i];
            BlockPos dp = pos.relative(dir);
            MachineRegistry m = MachineRegistry.getMachine(world, dp);
            if (m == MachineRegistry.BUSCONTROLLER) {
                BlockEntityBusController te = (BlockEntityBusController) world.getBlockEntity(dp);
                ShaftPowerBus bus = te.getBus();
                if (bus != null) {
                    this.configureBusData(bus);
                    inputSide = dir;
                    return;
                }
            }
            if (m == MachineRegistry.POWERBUS) {
                BlockEntityPowerBus te = (BlockEntityPowerBus) world.getBlockEntity(dp);
                ShaftPowerBus bus = te.getBus();
                if (bus != null) {
                    this.configureBusData(bus);
                    inputSide = dir;
                    return;
                }
            }
        }
    }

    private void configureBusData(ShaftPowerBus bus) {
        bus.addBlock(this);
        this.bus = bus;
        BlockEntityBusController hub = bus.getController();
        hubX = hub.getBlockPos().getX();
        hubY = hub.getBlockPos().getY();
        hubZ = hub.getBlockPos().getZ();
    }

    private void removeFromBus() {
        if (bus != null)
            bus.removeBlock(this);
    }

    public ShaftPowerBus getBus() {
        return bus;
    }

    public void clearBus() {
        bus = null;
        hubX = Integer.MIN_VALUE;
        hubY = Integer.MIN_VALUE;
        hubZ = Integer.MIN_VALUE;
    }

    @Override
    public boolean isWritingTo(PowerSourceTracker te) {
        for (int i = 2; i < 6; i++) {
            if (this.canOutputToSide(dirs[i])) {
                //BlockEntity tile = this.getAdjacentBlockEntity(dirs[i]);
                if (this.matchTile(te, dirs[i]))
                    return true;
            }
        }
        return false;
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        return bus != null && bus.getController() != null ? bus.getController().getPowerSources(io, caller) : new PowerSourceList();
    }

    @Override
    public boolean canProvidePower() {
        return true;
    }

    @Override
    public void breakBlock() {
        this.removeFromBus();
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (dir == read)
            c.add(getAdjacentBlockEntity(write));
    }
}
