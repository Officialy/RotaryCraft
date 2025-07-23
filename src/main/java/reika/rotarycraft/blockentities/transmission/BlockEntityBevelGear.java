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

import com.google.common.collect.HashBiMap;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import org.apache.commons.lang3.tuple.ImmutablePair;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.PowerAcceptor;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.gui.container.machine.BlankContainer;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryMenus;

public class BlockEntityBevelGear extends BlockEntity1DTransmitter implements GuiController {

    private static final HashBiMap<Integer, ImmutablePair<Direction, Direction>> directions = HashBiMap.create();

    static {
        for (int i = 0; i < 24; i++) { //do it this way to preserve ordering
            registerDirectionValue(i);
        }
    }

    public int direction;

    public BlockEntityBevelGear(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BEVEL_GEARS.get(), pos, state);
    }

    private static void registerDirectionValue(int value) {
        Direction read = null;
        Direction write = null;
        switch (value) {
            case 0 -> {//-x -> -z
                read = Direction.WEST;
                write = Direction.NORTH;
            }
            case 1 -> {
                read = Direction.NORTH;
                write = Direction.EAST;
            }
            case 2 -> {
                read = Direction.EAST;
                write = Direction.SOUTH;
            }
            case 3 -> {
                read = Direction.SOUTH;
                write = Direction.WEST;
            }
            case 4 -> {
                read = Direction.WEST;
                write = Direction.SOUTH;
            }
            case 5 -> {
                read = Direction.NORTH;
                write = Direction.WEST;
            }
            case 6 -> {
                write = Direction.NORTH;
                read = Direction.EAST;
            }
            case 7 -> {
                read = Direction.SOUTH;
                write = Direction.EAST;
            }
            case 8 -> {    //VERTICAL POSITIONS - going up from flat
                read = Direction.WEST;
                write = Direction.UP;
            }
            case 9 -> {
                read = Direction.NORTH;
                write = Direction.UP;
            }
            case 10 -> {
                read = Direction.EAST;
                write = Direction.UP;
            }
            case 11 -> {
                read = Direction.SOUTH;
                write = Direction.UP;
            }
            case 12 -> { //VERTICAL POSITIONS - going flat from up
                read = Direction.DOWN;
                write = Direction.WEST;
            }
            case 13 -> {
                read = Direction.DOWN;
                write = Direction.NORTH;
            }
            case 14 -> {
                read = Direction.DOWN;
                write = Direction.EAST;
            }
            case 15 -> {
                read = Direction.DOWN;
                write = Direction.SOUTH;
            }
            case 16 -> { //VERTICAL POSITIONS - going down from flat
                write = Direction.DOWN;
                read = Direction.WEST;
            }
            case 17 -> {
                write = Direction.DOWN;
                read = Direction.NORTH;
            }
            case 18 -> {
                write = Direction.DOWN;
                read = Direction.EAST;
            }
            case 19 -> {
                write = Direction.DOWN;
                read = Direction.SOUTH;
            }
            case 20 -> { //VERTICAL POSITIONS - going flat from down
                read = Direction.UP;
                write = Direction.WEST;
            }
            case 21 -> {
                read = Direction.UP;
                write = Direction.NORTH;
            }
            case 22 -> {
                read = Direction.UP;
                write = Direction.EAST;
            }
            case 23 -> {
                read = Direction.UP;
                write = Direction.SOUTH;
            }
        }
        registerDirection(read, write);
    }

    private static void registerDirection(Direction read, Direction write) {
        int idx = directions.size();
        directions.put(idx, new ImmutablePair<>(read, write));
//        RotaryCraft.LOGGER.info("Registered bevel direction #" + idx + ": " + read + " to " + write);
    }

    public static HashBiMap<Integer, ImmutablePair<Direction, Direction>> getDirectionMap() {
        return HashBiMap.create(directions);
    }

    public static boolean isValid(Direction read, Direction write) {
        return directions.inverse().containsKey(new ImmutablePair<>(read, write));
    }

    public boolean isInverting() {
        return true; // Assume default bevel gear behavior inverts rotation
    }

    @Override
    public void updateBlockEntity() {
        super.updateBlockEntity();

        if (level.getGameTime() % 10 == 0) {
            this.findRoute(level, worldPosition);
        }

        power = (long) omega * (long) torque;
        this.getIOSides();
        this.transferPower(level, worldPosition);

        this.basicPowerReceiver();
    }

    private void findRoute(Level world, BlockPos pos) {
        Direction write = null;
        Direction read = null;
        for (int i = 0; i < 6; i++) {
            BlockEntity te = getAdjacentBlockEntity(Direction.values()[i]);
            if (te instanceof BlockEntityIOMachine io) {
                if (read == null) {
                    if (io.isWritingToCoordinate(pos) || io.isWritingToCoordinate2(pos)) {
                        read = Direction.values()[i];
                    }
                }
                if (write == null) {
                    if (io.isReadingFrom(this) || io.isReadingFrom2(this) || io.isReadingFrom3(this) || io.isReadingFrom4(this)) {
                        write = Direction.values()[i];
                    }
                }
            } else if (te instanceof PowerAcceptor) {
                if (write == null) {
                    if (((PowerAcceptor) te).canReadFrom(Direction.values()[i].getOpposite())) {
                        write = Direction.values()[i];
                    }
                }
            }
        }
        if (write != null && read != null && read != write && read != write.getOpposite()) {
            direction = directions.inverse().get(new ImmutablePair<>(read, write));
        }
    }

    public void getIOSides() {
        ImmutablePair<Direction, Direction> dirs = directions.get(direction);
        if (dirs != null) {
            read = dirs.left;
            write = dirs.right;
        } else {
            // Handle invalid direction value
            read = Direction.NORTH; // Default fallback
            write = Direction.SOUTH; // Default fallback
            // Log an error or handle appropriately
            System.err.println("Invalid bevel gear direction: " + direction);

            // Optionally reset to a valid direction
            direction = 0;
        }
    }

    @Override
    protected void transferPower(Level world, BlockPos pos) {
        if (world.isClientSide && !RotaryAux.getPowerOnClient)
            return;

        this.getIOSides(); // Ensure I/O sides are set before power transfer

        if (read == null || write == null) {
            //RotaryCraft.LOGGER.warn("Bevel gear @ " + pos + " has null I/O sides, skipping power transfer.");
            return;
        }

        omegain = torquein = 0;
        boolean isCentered = pos.equals(worldPosition);
        BlockPos readPos = isCentered ? pos.relative(read) : new BlockPos(pos.getX() + read.getStepX(), pos.getY() + read.getStepY(), pos.getZ() + read.getStepZ());

        MachineRegistry m = MachineRegistry.getMachine(world, readPos);
        BlockEntity te = world.getBlockEntity(readPos);

//        RotaryCraft.LOGGER.info("Bevel @ " + pos + ": Attempting to read power from " + read + " at " + readPos + ". Found TileEntity: " + te);

        if (this.isProvider(te)) {
//            RotaryCraft.LOGGER.info("Bevel @ " + pos + ": " + readPos + " is a valid provider.");
            if (te instanceof BlockEntityShaft devicein) {
                if (devicein.isCross()) {
                    this.readFromCross(devicein);
//                    RotaryCraft.LOGGER.info("Reading from cross-shaft. Power: T=" + torquein + ", S=" + omegain);
                    return;
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
//                    RotaryCraft.LOGGER.info("Reading from shaft. Power: T=" + torquein + ", S=" + omegain);
                }
            }
            if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
//                RotaryCraft.LOGGER.info("Reading from simple provider. Power: T=" + torquein + ", S=" + omegain);
            }
            if (te instanceof ComplexIO pwr) {
                Direction dir = this.getInputDirection().getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
//                RotaryCraft.LOGGER.info("Reading from complex IO. Power: T=" + torquein + ", S=" + omegain);
            }
            if (te instanceof ShaftPowerEmitter sp) {
                if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                    torquein = sp.getTorque();
                    omegain = sp.getOmega();
//                    RotaryCraft.LOGGER.info("Reading from power emitter. Power: T=" + torquein + ", S=" + omegain);
                }
            }
            if (te instanceof BlockEntitySplitter devicein) {
                if (devicein.isSplitting()) {
                    this.readFromSplitter(world, pos, devicein);
                    torquein = torque;
                    omegain = omega;
//                    RotaryCraft.LOGGER.info("Reading from splitter. Power: T=" + torquein + ", S=" + omegain);
                    return;
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
        } else {
//            RotaryCraft.LOGGER.warn("Bevel @ " + pos + ": " + readPos + " is NOT a provider. Resetting power.");
            omega = 0;
            torque = 0;
            power = 0;
            return;
        }

        omega = omegain;
        torque = torquein;
        power = (long) omega * (long) torque;
//        RotaryCraft.LOGGER.info("Bevel @ " + pos + ": Final power values: T=" + torque + ", S=" + omega + ", P=" + power);
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("posn", direction);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        direction = tag.getInt("posn");
        //Try to preserve the directionality after the system was changed:
        if (!tag.contains("posn"))
            direction = directions.inverse().get(new ImmutablePair(read, write));
    }
    @Override
    public void saveAdditional(CompoundTag NBT) {
        super.saveAdditional(NBT);
        NBT.putInt("direction", direction);
    }

    @Override
    public void load(CompoundTag NBT) {
        super.load(NBT);
        direction = NBT.getInt("direction");
    }
    @Override
    protected String getTEName() {
        return "bevelgear";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {
        // This method is required by the abstract base class
        // The actual update logic is handled in updateBlockEntity()
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
           phi = 0;
           return;
        }
        phi += (float) ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BEVELGEARS;
    }

    @Override
    public void onEMP() {
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
    public Component getDisplayName() {
        return Component.literal("Bevel Gear");
    }

    @Override
    public  AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new BlankContainer<>(RotaryMenus.BEVEL.get(), p_39954_, p_39955_, this);
    }
}