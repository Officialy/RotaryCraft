///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.tileentities.Transmission;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.data.immutable.WorldLocation;
//import reika.dragonapi.interfaces.blockentity.ChunkLoadingTile;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.api.Interfaces.ComplexIO;
//import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
//import reika.rotarycraft.auxiliary.ShaftPowerEmitter;
//import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.MaterialRegistry;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import org.stringtemplate.v4.misc.BlockPos;
//
//import java.util.Collection;
//
//public class BlockEntityPortalShaft extends BlockEntity1DTransmitter implements ChunkLoadingTile {
//
//    public MaterialRegistry material;
//    private PortalType type;
//    private WorldLocation otherShaft;
//
//    public int getCurrentDimID() {
//        return level.dimension();
//    }
//
//    public int getTargetDimID() {
//        int id = this.getCurrentDimID();
//        switch (type) {
//            case END:
//                return id == 0 ? 1 : 0;
//            case MYSTCRAFT: //portal has a book slot?
//                return this.getMystCraftTarget();
//            case NETHER:
//                return id == 0 ? -1 : 0;
//            case TWILIGHT:
//                return id == 0 ? TwilightForestHandler.getInstance().dimensionID : 0;
//            default:
//                return id;
//        }
//    }
//
//    private int getMystCraftTarget() {
//        int x = xCoord + write.getStepX();
//        int y = yCoord + write.getStepY();
//        int z = zCoord + write.getStepZ();
//        return ReikaMystcraftHelper.getTargetDimensionIDFromPortalBlock(level, pos);
//    }
//
//    public void setPortalType(Level world, BlockPos pos) {
//        Block id = world.getBlockState(pos).getBlock();
//        if (id == Blocks.NETHER_PORTAL)
//            type = PortalType.NETHER;
//        if (id == Blocks.END_PORTAL)
//            type = PortalType.END;
//        if (ModList.MYSTCRAFT.isLoaded() && id == MystCraftHandler.getInstance().portalID)
//            type = PortalType.MYSTCRAFT;
//        if (ModList.TWILIGHT.isLoaded() && id == TwilightForestHandler.BlockEntry.PORTAL.getBlock())
//            type = PortalType.TWILIGHT;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos,) {
//        super.updateBlockEntity();
//        this.getIOSides(world, pos, meta);
//        if (ReikaBlockHelper.isPortalBlock(world, x + write.getStepX(), y + write.getStepY(), z + write.getStepZ())) {
//            this.transferPower(world, pos, meta);
//            this.emitPower(world, pos);
//        }
//
//        if (!world.isClientSide && otherShaft != null) {
//            if (otherShaft.getBlockEntity() instanceof BlockEntityPortalShaft) {
//
//            } else {
//                power = 0;
//
//                world.setBlock(pos, MachineRegistry.SHAFT.getBlock(), MachineRegistry.SHAFT.getBlockMetadata(), 3);
//                BlockEntityShaft ts = new BlockEntityShaft(this.getShaftType());
//                ts.setBlockMetadata(this.getBlockMetadata());
//                world.setBlockEntity(pos, ts);
//            }
//        }
//    }
//
//    @Override
//    protected void onFirstTick(Level world, BlockPos pos) {
//        super.onFirstTick(world, pos);
//
//        ChunkManager.instance.loadChunks(this);
//    }
//
//    private int getTargetDimensionBy(Level world, BlockPos pos) {
//        Block block = world.getBlock(pos);
//        int id = world.provider.dimensionId;
//        //ReikaJavaLibrary.pConsole(id+":"+block+" @ "+x+", "+y+", "+z, Dist.DEDICATED_SERVER);
//        //ReikaJavaLibrary.pConsole(id+":"+block+" @ "+x+", "+y+", "+z, id == 7);
//        switch (type) {
//            case END:
//                return block == Blocks.end_portal ? id == 0 ? 1 : 0 : Integer.MIN_VALUE;
//            case MYSTCRAFT: //portal has a book slot?
//                return ReikaMystcraftHelper.getTargetDimensionIDFromPortalBlock(world, pos);
//            case NETHER:
//                return block == Blocks.portal ? id == 0 ? -1 : 0 : Integer.MIN_VALUE;
//            case TWILIGHT:
//                return id == 0 ? TwilightForestHandler.getInstance().dimensionID : 0;
//            default:
//                return id;
//        }
//    }
//
//    private BlockPos getScaledCoordinates(BlockPos pos, Level source, Level target) {
//        if (source == null || target == null)
//            return null;
//        int tg = target.provider.dimensionId;
//        int src = source.provider.dimensionId;
//        if (src != -1 && tg == -1) { //to nether
//            x = x / 8;
//            z = z / 8;
//        }
//        if (src == -1 && tg != -1) { //from nether
//            x *= 8;
//            z *= 8;
//        }
//        //ReikaJavaLibrary.pConsole(src+">"+tg+" @ "+x+","+y+","+z, Dist.DEDICATED_SERVER);
//        return new BlockPos(pos);
//    }
//
//    private void emitPower(Level world, BlockPos pos) {
//        Location c = this.getOutputLocation(world, pos);
//        if (c == null)
//            return;
//        int dx = c.getY;
//        int dy = c.getY();
//        int dz = c.posZ;
//        int ax = c.posXA;
//        int ay = c.posYA;
//        int az = c.posZA;
//        Level age = c.world;
//        MachineRegistry m = MachineRegistry.getMachine(age, dx, dy, dz);
//        //ReikaJavaLibrary.pConsole(x+", "+y+", "+z+":"+dx+", "+dy+", "+dz+" >> "+age.getBlock(dx, dy, dz), Dist.DEDICATED_SERVER);
//        //ReikaJavaLibrary.pConsole(x+", "+y+", "+z+":"+dx+", "+dy+", "+dz+" >> "+m, Dist.DEDICATED_SERVER);
//        //ReikaJavaLibrary.pConsole(dx+", "+dy+", "+dz+" >> "+m, Dist.DEDICATED_SERVER);
//        //ReikaJavaLibrary.pConsole(dx+", "+dy+", "+dz+" >> "+m, dim == 7);
//        if (m == MachineRegistry.SHAFT) {
//            BlockEntityShaft te = (BlockEntityShaft) age.getBlockEntity(dx, dy, dz);
//            int terx = te.xCoord + te.getReadDirection().getStepX();
//            int tery = te.yCoord + te.getReadDirection().getStepY();
//            int terz = te.zCoord + te.getReadDirection().getStepZ();
//            //ReikaJavaLibrary.pConsole(terx+","+tery+","+terz, Dist.DEDICATED_SERVER);
//            if (terx == ax && tery == ay && terz == az) {
//                Block tid = MachineRegistry.PORTALSHAFT.getBlock();
//                int tmeta = MachineRegistry.PORTALSHAFT.getBlockMetadata();
//                //ReikaJavaLibrary.pConsole(tid+":"+tmeta);
//                age.setBlockToAir(dx, dy, dz);
//                age.setBlock(dx, dy, dz, tid, tmeta, 3);
//                BlockEntityPortalShaft ps = (BlockEntityPortalShaft) age.getBlockEntity(dx, dy, dz);//new BlockEntityPortalShaft();
//                ps.read = te.getReadDirection();
//                ps.setBlockMetadata(te.getBlockMetadata());
//                ps.setPortalType(age, ax, ay, az);
//                ps.material = material;
//                ps.otherShaft = new WorldLocation(this);
//                otherShaft = new WorldLocation(age, dx, dy, dz);
//                ReikaPacketHelper.sendUpdatePacket(DragonAPI.packetChannel, PacketIDs.TILEDELETE.ordinal(), ps, new PacketTarget.RadiusTarget(this, 32));
//            }
//        } else if (m == MachineRegistry.PORTALSHAFT) {
//            BlockEntityPortalShaft te = (BlockEntityPortalShaft) age.getBlockEntity(dx, dy, dz);
//            int terx = te.xCoord + te.getReadDirection().getStepX();
//            int tery = te.yCoord + te.getReadDirection().getStepY();
//            int terz = te.zCoord + te.getReadDirection().getStepZ();
//            if (terx == ax && tery == ay && terz == az) {
//                te.power = power;
//                te.omega = omega;
//                te.torque = torque;
//            }
//        }
//    }
//
//    private Location getOutputLocation(Level world, BlockPos pos) {
//        //use dimensionmanager to set power
//        int dim = this.getTargetDimID();
//        //ReikaJavaLibrary.pConsole(writex+":"+writey+":"+writez, Dist.DEDICATED_SERVER);
//        //ReikaJavaLibrary.pConsole(dim, Dist.DEDICATED_SERVER);
//        Level age = DimensionManager.getWorld(dim);
//        //ReikaJavaLibrary.pConsole(age);
//        BlockPos loc = this.getScaledCoordinates(x + write.getStepX(), y + write.getStepY(), z + write.getStepZ(), world, age);
//        if (loc == null)
//            return null;
//        int ax = loc.xCoord;
//        int ay = loc.yCoord;
//        int az = loc.zCoord;
//        if (age != null/* && age.hasChunksAt(ax, ay, az, ax, ay, az)*/) {
//            int tg = this.getTargetDimensionBy(age, ax, ay, az);
//            //ReikaJavaLibrary.pConsole(write+": "+tg+": "+ax+","+ay+","+az, Dist.DEDICATED_SERVER);
//            //ReikaJavaLibrary.pConsole(tg, dim == 7);
//            if (tg == world.provider.dimensionId) {
//                //ReikaJavaLibrary.pConsole(writex+", "+writey+", "+writez+" >> "+Blocks.blocksList[id], Dist.DEDICATED_SERVER);
//                BlockPos c2 = this.getScaledCoordinates(pos, world, age);
//                return new Location(age, loc.xCoord + write.getStepX(), loc.yCoord + write.getStepY(), loc.zCoord + write.getStepZ(), ax, ay, az);
//            }
//        }
//        return null;
//    }
//
//    public void getIOSides(Level world, BlockPos pos,) {
//        switch (meta) {
//            case 0:
//                read = Direction.EAST;
//                write = read.getOpposite();
//                break;
//            case 1:
//                read = Direction.WEST;
//                write = read.getOpposite();
//                break;
//            case 2:
//                read = Direction.SOUTH;
//                write = read.getOpposite();
//                break;
//            case 3:
//                read = Direction.NORTH;
//                write = read.getOpposite();
//                break;
//            case 4:    //moving up
//                read = Direction.DOWN;
//                write = read.getOpposite();
//                break;
//            case 5:    //moving down
//                read = Direction.UP;
//                write = read.getOpposite();
//                break;
//        }
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.25);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.PORTALSHAFT;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    protected void transferPower(Level world, BlockPos pos,) {
//        omegain = torquein = 0;
//        MachineRegistry m = this.getMachine(read);
//        BlockEntity te = this.getAdjacentBlockEntity(read);
//        if (this.isProvider(te)) {
//            if (m == MachineRegistry.SHAFT) {
//                BlockEntityShaft devicein = (BlockEntityShaft) te;
//                if (devicein.isCross()) {
//                    this.readFromCross(devicein);
//                    return;
//                } else if (devicein.isWritingTo(this)) {
//                    torquein = devicein.torque;
//                    omegain = devicein.omega;
//                }
//            }
//            if (te instanceof ComplexIO) {
//                ComplexIO pwr = (ComplexIO) te;
//                Direction dir = this.getInputDirection().getOpposite();
//                omegain = pwr.getSpeedToSide(dir);
//                torquein = pwr.getTorqueToSide(dir);
//            }
//            if (te instanceof SimpleProvider) {
//                this.copyStandardPower(te);
//            }
//            if (te instanceof ShaftPowerEmitter) {
//                ShaftPowerEmitter sp = (ShaftPowerEmitter) te;
//                if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
//                    torquein = sp.getTorque();
//                    omegain = sp.getOmega();
//                }
//            }
//            if (m == MachineRegistry.SPLITTER) {
//                BlockEntitySplitter devicein = (BlockEntitySplitter) te;
//                if (devicein.isSplitting()) {
//                    this.readFromSplitter(world, pos, devicein);
//                    torquein = torque;
//                    omegain = omega;
//                    return;
//                } else if (devicein.isWritingTo(this)) {
//                    torquein = devicein.torque;
//                    omegain = devicein.omega;
//                }
//            }
//            omega = omegain;
//            torque = torquein;
//        } else {
//            omega = torque = 0;
//        }
//        power = (long) torque * (long) omega;
//    }
//
//    @Override
//    public void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        type = PortalType.values()[NBT.getInt("portal")];
//
//        MaterialRegistry mat = MaterialRegistry.WOOD;
//        if (NBT.contains("shafttype")) {
//            mat = MaterialRegistry.valueOf(NBT.getString("shafttype"));
//        } else if (NBT.contains("type")) {
//            mat = MaterialRegistry.matList[NBT.getInt("type")];
//        }
//        material = mat;
//    }
//
//    @Override
//    public void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        if (type != null)
//            NBT.putInt("portal", type.ordinal());
//        if (material != null)
//            NBT.setString("mat", material.name());
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//    }
//
//    public MaterialRegistry getShaftType() {
//        if (this.isInWorld() && material != null) {
//            return material;
//        }
//        return MaterialRegistry.STEEL;
//    }
//
//    public boolean isEnteringPortal() {
//        if (write == null)
//            return false;
//        return ReikaBlockHelper.isPortalBlock(level, xCoord + write.getStepX(), yCoord + write.getStepY(), zCoord + write.getStepZ());
//    }
//
//    public boolean isExitingPortal() {
//        if (read == null)
//            return false;
//        return ReikaBlockHelper.isPortalBlock(level, xCoord + read.getStepX(), yCoord + read.getStepY(), zCoord + read.getStepZ());
//    }
//
//    @Override
//    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
//        Location loc = this.getOutputLocation(level, xCoord, yCoord, zCoord);
//        c.add(loc.world.getBlockEntity(loc.getY, loc.getY(), loc.posZ));
//    }
//
//    @Override
//    public void breakBlock() {
//        ChunkManager.instance.unloadChunks(this);
//    }
//
//    @Override
//    public Collection<ChunkCoordIntPair> getChunksToLoad() {
//        return ChunkManager.getChunkSquare(xCoord, zCoord, 1);
//    }
//
//    public enum PortalType {
//        NETHER(),
//        END(),
//        TWILIGHT(),
//        MYSTCRAFT()
//    }
//
//    private static class Location {
//
//        private final Level world;
//        private final int getY;
//        private final int getY();
//        private final int posZ;
//        private final int posXA;
//        private final int posYA;
//        private final int posZA;
//
//        private Location(Level world, BlockPos pos, int xa, int ya, int za) {
//            this.world = world;
//            getY = x;
//            getY() = y;
//            posZ = z;
//            posXA = xa;
//            posYA = ya;
//            posZA = za;
//        }
//
//    }
//
//}
