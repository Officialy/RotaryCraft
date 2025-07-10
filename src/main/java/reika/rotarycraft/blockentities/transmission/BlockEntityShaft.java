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
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.api.event.ShaftFailureEvent;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.*;

import java.util.Collection;


public class BlockEntityShaft extends BlockEntity1DTransmitter {

    public int[] readtorque = new int[2];
    public int[] readomega = new int[2];
    public boolean reading2Dir = false; //Is reading a 2-direction block (splitter, cross)

    public float crossphi1 = 0;
    public float crossphi2 = 0;

    private MaterialRegistry materialType;
    private boolean failed = false;

    private boolean isCrossForRender;

    public boolean inverted = false;

    public BlockEntityShaft(MaterialRegistry materialType, BlockPos pos, BlockState state) {
        super(switch (materialType) {
            case WOOD -> RotaryBlockEntities.WOOD_SHAFT.get();
            case STONE -> RotaryBlockEntities.STONE_SHAFT.get();
            case STEEL -> RotaryBlockEntities.HSLA_STEEL_SHAFT.get();
            case TUNGSTEN -> RotaryBlockEntities.TUNGSTEN_SHAFT.get();
            case DIAMOND -> RotaryBlockEntities.DIAMOND_SHAFT.get();
            case BEDROCK -> RotaryBlockEntities.BEDROCK_SHAFT.get();
        }, pos, state);
        if (materialType == null)
            materialType = MaterialRegistry.WOOD;
        this.materialType = materialType;
    }

    public MaterialRegistry getShaftType() {
        return materialType;
    }

    public void setShaftType(MaterialRegistry type) {
        materialType = type;
    }

    public void setData(MaterialRegistry mat, boolean cross) {
        materialType = mat;
        isCrossForRender = cross;
    }

    public void setMaterialFromItem(ItemStack is) {
        materialType = MaterialRegistry.getMaterialFromShaftItem(is);
        this.syncAllData(true);
    }

    public void fail(Level world, BlockPos pos, boolean speed) {
        MinecraftForge.EVENT_BUS.post(new ShaftFailureEvent(this, speed, materialType.ordinal()));
        failed = true;
        if (speed) {
            world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1F, Level.ExplosionInteraction.BLOCK);
            ItemStack item = switch (materialType) {
                case WOOD -> RotaryItems.SAWDUST.get().getDefaultInstance();
                case STONE -> new ItemStack(Blocks.GRAVEL, 1);
                case STEEL -> RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance();
                case TUNGSTEN -> RotaryItems.HSLA_STEEL_SCRAP.get().getDefaultInstance();
                case DIAMOND -> new ItemStack(Items.DIAMOND, 1);
                case BEDROCK -> RotaryItems.BEDROCK_DUST.get().getDefaultInstance();
            };
            ItemEntity ei = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5, item);

            ei.setDeltaMovement(0.4F + 0.6F * DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat() / 5, DragonAPI.rand.nextFloat() / 5);
            if (world.isClientSide)
                return;
            ei.hurtMarked = true;
            if (DragonAPI.rand.nextInt(24) == 0)
                world.addFreshEntity(ei);
        } else {
//            world.playSound(player, pos, "DragonAPI.rand.break", 1F, 1F);
            ItemStack item = switch (materialType) {
                case WOOD -> new ItemStack(Items.STICK, 1);
                case STONE -> new ItemStack(Blocks.COBBLESTONE, 1);
                case STEEL -> new ItemStack(RotaryItems.HSLA_SHAFT.get());
                case TUNGSTEN -> new ItemStack(RotaryItems.TUNGSTEN_ALLOY_SHAFT.get());
                case DIAMOND -> new ItemStack(Items.DIAMOND, 1);
                case BEDROCK -> new ItemStack(RotaryItems.BEDROCK_DUST.get());
            };

            ItemEntity ei = new ItemEntity(world, pos.getX() + 0.5, pos.getY() + 1, pos.getZ() + 0.5, item);
            ei.setDeltaMovement(0.4F + 0.6F * DragonAPI.rand.nextFloat() / 5, DragonAPI.rand.nextFloat() / 5, DragonAPI.rand.nextFloat() / 5);
            if (world.isClientSide)
                return;
            ei.hurtMarked = true;
            if (DragonAPI.rand.nextInt(24) == 0)
                world.addFreshEntity(ei);
        }
    }

    public void repair() {
        failed = false;
    }

    public boolean failed() {
        return failed;
    }

    public void testFailure() {
        if (ReikaEngLibrary.mat_rotfailure(materialType.getDensity(), 0.0625, ReikaMathLibrary.doubpow(omega, materialType.getSpeedForceExponent()), materialType.getTensileStrength())) {
            this.fail(level, worldPosition, true);
        } else if (ReikaEngLibrary.mat_twistfailure(torque, 0.0625, materialType.getShearStrength() / 16D)) {
            this.fail(level, worldPosition, false);
        }
    }

    //No read/write y2 since vertical shafts will not have cross equivalent
    //(no way to make them look structurally sound)

    private void crossReadFromCross(BlockEntityShaft cross, int dir) {
        reading2Dir = true;
        if (cross.isWritingTo(this)) {
            readomega[dir] = cross.readomega[0];
            readtorque[dir] = cross.readtorque[0];
        } else if (cross.isWritingTo2(this)) {
            readomega[dir] = cross.readomega[1];
            readtorque[dir] = cross.readtorque[1];
        } else {
        } //not its output
    }

    @Override
    protected void readFromCross(BlockEntityShaft cross) {
        reading2Dir = true;
        super.readFromCross(cross);
    }

    private void crossReadFromSplitter(Level world, BlockPos pos, BlockEntitySplitter spl, int dir) {
        reading2Dir = true;
        int sratio = spl.getRatioFromMode();
        if (sratio == 0)
            return;
        boolean favorbent = false;
        if (sratio < 0) {
            favorbent = true;
            sratio = -sratio;
        }
        if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
            readomega[dir] = spl.omega; //omega always constant
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("INLINE!  %d  %d  FOR %d", spl.omega, spl.torque, sratio));
            if (sratio == 1) { //Even split, favorbent irrelevant
                readtorque[dir] = spl.torque / 2;
                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", this.readtorque[dir], this.readomega[dir]));
                return;
            }
            if (favorbent) {
                readtorque[dir] = spl.torque / sratio;
            } else {
                readtorque[dir] = (int) (spl.torque * ((sratio - 1D) / (sratio)));
            }
        } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
            readomega[dir] = spl.omega; //omega always constant
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("BEND!");
            if (sratio == 1) { //Even split, favorbent irrelevant
                readtorque[dir] = spl.torque / 2;
                return;
            }
            if (favorbent) {
                readtorque[dir] = (int) (spl.torque * ((sratio - 1D) / (sratio)));
            } else {
                readtorque[dir] = spl.torque / sratio;
            }
        } else { //We are not one of its write-to blocks
            readtorque[dir] = 0;
            readomega[dir] = 0;
        }
    }

    @Override
    protected void readFromSplitter(Level world, BlockPos pos, BlockEntitySplitter spl) { //Complex enough to deserve its own function
        reading2Dir = true;
        int sratio = spl.getRatioFromMode();
        if (sratio == 0)
            return;
        boolean favorbent = false;
        if (sratio < 0) {
            favorbent = true;
            sratio = -sratio;
        }
        if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
            omega = spl.omega; //omega always constant
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("INLINE!  %d  %d  FOR %d", spl.omega, spl.torque, sratio));
            if (sratio == 1) { //Even split, favorbent irrelevant
                torque = spl.torque / 2;
                //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", this.torque, this.omega));
            } else if (favorbent) {
                torque = spl.torque / sratio;
            } else {
                torque = (int) (spl.torque * ((sratio - 1D) / (sratio)));
            }
        } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
            omega = spl.omega; //omega always constant
            //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("BEND!");
            if (sratio == 1) { //Even split, favorbent irrelevant
                torque = spl.torque / 2;
            } else if (favorbent) {
                torque = (int) (spl.torque * ((sratio - 1D) / (sratio)));
            } else {
                torque = spl.torque / sratio;
            }
        } else { //We are not one of its write-to blocks
            torque = 0;
            omega = 0;
            power = 0;
        }
    }

    @Override
    public Block getBlockEntityBlockID() {
        return switch (materialType) {
            case WOOD -> RotaryBlocks.WOOD_SHAFT.get();
            case STONE -> RotaryBlocks.STONE_SHAFT.get();
            case STEEL -> RotaryBlocks.HSLA_SHAFT.get();
            case TUNGSTEN -> RotaryBlocks.TUNGSTEN_SHAFT.get();
            case DIAMOND -> RotaryBlocks.DIAMOND_SHAFT.get();
            case BEDROCK -> RotaryBlocks.BEDROCK_SHAFT.get();
        };
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateEntity();
        super.updateBlockEntity();
        ratio = 1;
        if (failed) {
            torque = 0;
            omega = 0;
            power = 0;

            this.basicPowerReceiver();
            return;
        }
//        this.testFailure();
        this.getIOSides(world, pos, getBlockState().getValue(BlockRotaryCraftMachine.FACING));
        this.transferPower(world, pos);

        //ReikaJavaLibrary.pConsole(Arrays.toString(readtorque)+":"+Arrays.toString(readomega), Dist.DEDICATED_SERVER);
    }

    public boolean isVertical() {
        if (this.getBlockState().hasProperty(BlockRotaryCraftMachine.FACING))
            return getBlockState().getValue(BlockRotaryCraftMachine.FACING) == Direction.UP || getBlockState().getValue(BlockRotaryCraftMachine.FACING) == Direction.DOWN;
        return false;
    }

    public boolean isCross() {
        return isCrossForRender;// || this.getBlockMetadata() >= 6;
    }

    public void getIOSides(Level world, BlockPos pos, Direction dir) {
        read2 = null;
        write2 = null;
        if (this.isCross()) {
            // This logic is based on the 4 cross meta values from the 1.7.10 code.
            // The mapping from the FACING direction to a specific cross configuration is a best guess.
            switch (dir) {
                case NORTH: // Guessing this corresponds to old meta 9 (W/S -> E/N)
                    read = Direction.WEST;
                    read2 = Direction.SOUTH;
                    write = Direction.EAST;
                    write2 = Direction.NORTH;
                    break;
                case SOUTH: // Guessing this corresponds to old meta 8 (W/N -> E/S)
                    read = Direction.WEST;
                    read2 = Direction.NORTH;
                    write = Direction.EAST;
                    write2 = Direction.SOUTH;
                    break;
                case WEST: // Guessing this corresponds to old meta 7 (E/N -> W/S)
                    read = Direction.EAST;
                    read2 = Direction.NORTH;
                    write = Direction.WEST;
                    write2 = Direction.SOUTH;
                    break;
                case EAST: // Guessing this corresponds to old meta 6 (E/S -> W/N)
                    read = Direction.EAST;
                    read2 = Direction.SOUTH;
                    write = Direction.WEST;
                    write2 = Direction.NORTH;
                    break;
                default: // Vertical and other directions are not valid for cross shafts as per old code comments
                    // Fallback to a simple configuration
                    read = dir.getOpposite();
                    write = dir;
                    break;
            }
        } else {
            // This is the existing logic for simple shafts, which seems correct.
            switch (dir) {
                case WEST -> {
                    read = Direction.EAST;
                    write = Direction.WEST;
                }
                case EAST -> {
                    read = Direction.WEST;
                    write = Direction.EAST;
                }
                case NORTH -> {
                    read = Direction.SOUTH;
                    write = Direction.NORTH;
                }
                case SOUTH -> {
                    read = Direction.NORTH;
                    write = Direction.SOUTH;
                }
                case UP -> {    //moving up
                    read = Direction.DOWN;
                    write = Direction.UP;
                }
                case DOWN -> {    //moving down
                    read = Direction.UP;
                    write = Direction.DOWN;
                }
            }
        }
    }

    private void crossTransfer(Level world, BlockPos pos, boolean check1, boolean check2) {
        if (check1 && check2) {
            readomega[0] = 0;
            readomega[1] = 0;
            readtorque[0] = 0;
            readtorque[1] = 0;
        }
        boolean isCentered = pos.getX() == pos.getX() && pos.getY() == pos.getY() && pos.getZ() == pos.getZ();
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();
        int dx2 = pos.getX() + read2.getStepX();
        int dy2 = pos.getY() + read2.getStepY();
        int dz2 = pos.getZ() + read2.getStepZ();
        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, dx, dy, dz);
        BlockEntity te1 = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
        MachineRegistry m2 = isCentered ? this.getMachine(read2) : MachineRegistry.getMachine(world, dx2, dy2, dz2);
        BlockEntity te2 = isCentered ? getAdjacentBlockEntity(read2) : world.getBlockEntity(new BlockPos(dx2, dy2, dz2));

        //ReikaJavaLibrary.pConsole(read.name()+":"+read2.name(), Dist.DEDICATED_SERVER);

        //ReikaJavaLibrary.pConsole(te1, Dist.DEDICATED_SERVER);

        if (check1) {
            if (this.isProvider(te1)) {
                if (m == MachineRegistry.WOOD_SHAFT || m ==
                        MachineRegistry.STONE_SHAFT || m ==
                        MachineRegistry.HSLA_SHAFT || m ==
                        MachineRegistry.TUNGSTEN_SHAFT || m ==
                        MachineRegistry.DIAMOND_SHAFT || m ==
                        MachineRegistry.BEDROCK_SHAFT) {
                    BlockEntityShaft devicein = (BlockEntityShaft) te1;
                    if (devicein.isCross()) {
                        this.crossReadFromCross(devicein, 0);
                        return;
                    } else if (devicein.isWritingTo(this)) {
                        readomega[0] = devicein.omega;
                        readtorque[0] = devicein.torque;
                    }
                }
                if (te1 instanceof SimpleProvider) {
                    if (((BlockEntityIOMachine) te1).isWritingTo(this)) {
                        readtorque[0] = ((BlockEntityIOMachine) te1).torque;
                        readomega[0] = ((BlockEntityIOMachine) te1).omega;
                    }
                }
                if (te1 instanceof ShaftPowerEmitter sp) {
                    if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                        readtorque[0] = sp.getTorque();
                        readomega[0] = sp.getOmega();
                    }
                }
                if (m == MachineRegistry.SPLITTER) {
                    BlockEntitySplitter devicein = (BlockEntitySplitter) te1;
                    if (devicein.isSplitting()) {
                        this.crossReadFromSplitter(world, pos, devicein, 0);
                        return;
                    } else if (devicein.isWritingTo(this)) {
                        readtorque[0] = devicein.torque;
                        readomega[0] = devicein.omega;
                    }
                }
//            } else if (te1 instanceof WorldRift) {
//                WorldRift sr = (WorldRift) te1;
//                WorldLocation loc = sr.getLinkTarget();
//                if (loc != null)
//                    this.crossTransfer(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, true, false);
            } else {
                readtorque[0] = 0;
                readomega[0] = 0;
            }
        }

        if (check2) {
            if (this.isProvider(te2)) {
                if (m2 == MachineRegistry.WOOD_SHAFT || m2 ==
                        MachineRegistry.STONE_SHAFT || m2 ==
                        MachineRegistry.HSLA_SHAFT || m2 ==
                        MachineRegistry.TUNGSTEN_SHAFT || m2 ==
                        MachineRegistry.DIAMOND_SHAFT || m2 ==
                        MachineRegistry.BEDROCK_SHAFT) {
                    BlockEntityShaft devicein2 = (BlockEntityShaft) te2;
                    if (devicein2.isCross()) {
                        this.crossReadFromCross(devicein2, 1);
                        return;
                    } else if (devicein2.isWritingTo(this)) {
                        if (((BlockEntityIOMachine) te2).isWritingTo(this)) {
                            readomega[1] = devicein2.omega;
                            readtorque[1] = devicein2.torque;
                        }
                    }
                }
                if (te2 instanceof SimpleProvider) {
                    readtorque[1] = ((BlockEntityIOMachine) te2).torque;
                    readomega[1] = ((BlockEntityIOMachine) te2).omega;
                }
                if (te2 instanceof ShaftPowerEmitter sp) {
                    if (sp.isEmitting() && sp.canWriteTo(read2.getOpposite())) {
                        readtorque[1] = sp.getTorque();
                        readomega[1] = sp.getOmega();
                    }
                }
                if (m2 == MachineRegistry.SPLITTER) {
                    BlockEntitySplitter devicein2 = (BlockEntitySplitter) te2;
                    if (devicein2.isSplitting()) {
                        this.crossReadFromSplitter(world, pos, devicein2, 1);
                        return;
                    } else if (devicein2.isWritingTo(this)) {
                        readtorque[1] = devicein2.torque;
                        readomega[1] = devicein2.omega;
                    }
                }
            } /*else if (te2 instanceof WorldRift) {
                WorldRift sr = (WorldRift) te2;
                WorldLocation loc = sr.getLinkTarget();
                if (loc != null)
                    this.crossTransfer(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, false, true);
            }*/ else {
                readtorque[1] = 0;
                readomega[1] = 0;
            }
        }

        if (!check1 || !check2)
            return;

        //ReikaJavaLibrary.pConsole(Arrays.toString(readtorque)+":"+Arrays.toString(readomega), Dist.DEDICATED_SERVER);

        this.writeToPowerReceiver(write, readomega[0], readtorque[0]);
        this.writeToPowerReceiver(write2, readomega[1], readtorque[1]);

    }

    @Override
    protected void transferPower(Level world, BlockPos pos) {
        if (world.isClientSide && !RotaryAux.getPowerOnClient)
            return;
        reading2Dir = false;
        if (this.isCross()) {
            this.crossTransfer(world, pos, true, true);
            return;
        }
        omegain = torquein = 0;
        boolean isCentered = pos.getX() == worldPosition.getX() && pos.getY() == worldPosition.getY() && pos.getZ() == worldPosition.getZ();
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();

        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, dx, dy, dz);
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));

        this.inverted = false; // Reset before checking
        if (this.isProvider(te)) {
            if (te instanceof BlockEntityBevelGear) {
                BlockEntityBevelGear gear = (BlockEntityBevelGear) te;
                if (gear.getWriteDirection() == read.getOpposite()) {
                    this.copyStandardPower(te);
                    if (gear.isInverting()) {
                        this.inverted = true;
                    }
                }
            } else if (te instanceof BlockEntityShaft) {
                BlockEntityShaft devicein = (BlockEntityShaft) te;
                if (devicein.isCross()) {
                    this.readFromCross(devicein);
                    return;
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                    if (this.isVertical() && devicein.isVertical()) {
                        this.inverted = !devicein.inverted;
                    } else {
                        this.inverted = devicein.inverted;
                    }
                }
            }
            if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
            }
            if (te instanceof ComplexIO pwr) {
                Direction dir = this.getInputDirection().getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
            }
            if (te instanceof ShaftPowerEmitter sp) {
                if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                    torquein = sp.getTorque();
                    omegain = sp.getOmega();
                }
            }
            if (m == MachineRegistry.SPLITTER) {
                BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                if (devicein.isSplitting()) {
                    this.readFromSplitter(world, pos, devicein);
                    omegain = omega;
                    torquein = torque;
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
        } /*else if (te instanceof WorldRift) {
            WorldRift sr = (WorldRift) te;
            WorldLocation loc = sr.getLinkTarget();
            if (loc != null)
                this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta);
        }*/ else {
            omega = 0;
            torque = 0;
            power = 0;
            return;
        }

        omega = omegain / ratio;
        torque = torquein * ratio;
        power = (long) omega * torque;

        this.basicPowerReceiver();

        if (!materialType.isInfiniteStrength())
            this.testFailure();

        if (omega >= 32000000 && !failed) {
//            RotaryAdvancements.MRADS32.triggerAchievement(this.getPlacer());
        }
        if (power >= 1000000000 && !failed) {
//            RotaryAdvancements.GIGAWATT.triggerAchievement(this.getPlacer());
        }
    }

    /*
        private void convertToPortal(Level world, BlockPos pos, int dx, int dy, int dz) {
            world.setBlock(pos, MachineRegistry.PORTALSHAFT.getBlock(), MachineRegistry.PORTALSHAFT, 3);
            BlockEntityPortalShaft ps = new BlockEntityPortalShaft();
            ps.setPortalType(world, dx, dy, dz);
            ps.material = this.getShaftType();
            world.setBlockEntity(pos, ps);
        }
    */
    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putBoolean("failed", failed);

        tag.putIntArray("readtorque", readtorque);
        tag.putIntArray("readomega", readomega);

        tag.putFloat("cphi1", crossphi1);
        tag.putFloat("cphi2", crossphi2);
        tag.putBoolean("inverted", inverted);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        failed = tag.getBoolean("failed");

        readtorque = tag.getIntArray("readtorque");
        readomega = tag.getIntArray("readomega");
        if (readtorque.length != 2)
            readtorque = new int[2];
        if (readomega.length != 2)
            readomega = new int[2];

        crossphi1 = tag.getFloat("cphi1");
        crossphi2 = tag.getFloat("cphi2");
        inverted = tag.getBoolean("inverted");
    }

    @Override
    protected String getTEName() {
        return "shaft";
    }

    @Override
    public void saveAdditional(CompoundTag NBT) {
        super.saveAdditional(NBT);
        NBT.putString("shafttype", materialType.name());
        NBT.putBoolean("inverted", inverted);
    }

    @Override
    public void load(CompoundTag NBT) {
        super.load(NBT);
        MaterialRegistry mat = MaterialRegistry.WOOD;
        if (NBT.contains("shafttype")) {
            mat = MaterialRegistry.valueOf(NBT.getString("shafttype"));
        } else if (NBT.contains("type")) {
            int idx = NBT.getInt("type");
            if (idx >= MaterialRegistry.TUNGSTEN.ordinal())
                idx++;
            mat = MaterialRegistry.matList[idx];
        }
        phi = NBT.getFloat("phi");
        materialType = mat;
        inverted = NBT.getBoolean("inverted");
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.hasLevel()) {
            phi = 0;
            return;
        }
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.25);

        crossphi1 += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(readomega[0] + 1, 2), 1.25);
        crossphi2 += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(readomega[1] + 1, 2), 1.25);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public MachineRegistry getMachine() {
        return switch (materialType){
            case WOOD -> MachineRegistry.WOOD_SHAFT;
            case STONE -> MachineRegistry.STONE_SHAFT;
            case STEEL -> MachineRegistry.HSLA_SHAFT;
            case TUNGSTEN -> MachineRegistry.TUNGSTEN_SHAFT;
            case DIAMOND -> MachineRegistry.DIAMOND_SHAFT;
            case BEDROCK -> MachineRegistry.BEDROCK_SHAFT;
        };
    }

    @Override
    public void onEMP() {
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        if (this.isCross()) {
            boolean read1 = this.isWritingTo(io);
            if (read1) {
                return PowerSourceList.getAllFrom(level, read, new BlockPos(worldPosition.getX() + read.getStepX(), worldPosition.getY() + read.getStepY(), worldPosition.getZ() + read.getStepZ()), this, caller);
            } else {
                return PowerSourceList.getAllFrom(level, read2, new BlockPos(worldPosition.getX() + read2.getStepX(), worldPosition.getY() + read2.getStepY(), worldPosition.getZ() + read2.getStepZ()), this, caller);
            }
        } else
            return super.getPowerSources(io, caller);
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (this.isCross()) {
            if (dir == read) {
                c.add(getAdjacentBlockEntity(write));
            } else if (dir == read2) {
                c.add(getAdjacentBlockEntity(write2));
            }
        } else {
            super.getAllOutputs(c, dir);
        }
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
