/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.rotarycraft.api.power.PowerAcceptor;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blockentity.BlockEntityTransmissionMachine;
import reika.rotarycraft.blockentities.transmission.*;

import java.util.HashSet;

public class TorqueUsage {

    private static final HashSet<WorldLocation> pathCache = new HashSet();
    private static double requiredTorque;

    public static int getTorque(BlockEntityFlywheel te) {
        requiredTorque = 0;
        recursiveFind(te.getWriteBlockEntity(), te, 1);
        if (requiredTorque < 0)
            requiredTorque = 0;
        pathCache.clear();
        return (int) Math.ceil(requiredTorque);
    }

    private static void recursiveFind(BlockEntity tile, BlockEntityFlywheel reader, double activeRatio) {
        if (tile == null)
            return;
        WorldLocation loc = new WorldLocation(tile);
        if (pathCache.contains(loc))
            return;
        pathCache.add(loc);

        if (tile instanceof BlockEntityTransmissionMachine) { //true if the considered tile is a Transmission tile and is getting power from an already examined block
            BlockEntityIOMachine io = (BlockEntityIOMachine) tile;
            if (tile instanceof BlockEntitySplitter) { //check if splitter
                BlockEntitySplitter spl = (BlockEntitySplitter) tile;
                if (!spl.isSplitting()) { //check if merge mode or split mode (true if in merge mode)
                    BlockEntity write = spl.getWriteBlockEntity();
                    if (isPoweredFrom(write, spl)) {
                        recursiveFind(write, reader, activeRatio); //records the following tile inside the list
                    }
                } else {
                    BlockEntity di = spl.getWriteBlockEntity(); //records both outputs
                    BlockEntity di2 = spl.getWriteBlockEntity2();
                    if (isPoweredFrom(di, spl)) { //calls the recursion first with one output, then with the other
                        recursiveFind(di, reader, activeRatio);
                    }
                    if (isPoweredFrom(di2, spl)) {
                        recursiveFind(di2, reader, activeRatio);
                    }
                }
            } else if (tile instanceof BlockEntityFlywheel) {
                requiredTorque += activeRatio * ((BlockEntityFlywheel) tile).getOppTorque(reader);
            } else if (tile instanceof BlockEntityClutch) {
                BlockEntityClutch clu = (BlockEntityClutch) tile;
                if (clu.isOutputEnabled()) {
                    if (isPoweredFrom(clu.getWriteBlockEntity(), clu)) {
                        recursiveFind(clu.getWriteBlockEntity(), reader, activeRatio);
                    }
                }
            } else if (tile instanceof BlockEntityGearbox) {
                BlockEntityGearbox gbx = (BlockEntityGearbox) tile;
                if (isPoweredFrom(gbx.getWriteBlockEntity(), gbx)) {
                    if (gbx.reduction) {
                        recursiveFind(gbx.getWriteBlockEntity(), reader, activeRatio / gbx.getRatio());
                    } else {
                        recursiveFind(gbx.getWriteBlockEntity(), reader, activeRatio * gbx.getRatio());
                    }
                }
            } /*else if (tile instanceof BlockEntityAdvancedGear) {
                BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) tile;
                switch (adv.getGearType()) {
                    case WORM:
                        if (!isPoweredFrom(adv.getWriteBlockEntity(), adv)) {
                            recursiveFind(adv.getWriteBlockEntity(), reader, activeRatio / 16);
                        }
                        break;
                    case CVT:
                        if (isPoweredFrom(adv.getWriteBlockEntity(), adv)) {
                            if (adv.getRatio() > 0) {
                                recursiveFind(adv.getWriteBlockEntity(), reader, activeRatio * adv.getRatio());
                            } else {
                                recursiveFind(adv.getWriteBlockEntity(), reader, -activeRatio / adv.getRatio());
                            }
                        }
                        break;
                    case COIL:
                        double amt = Math.sqrt(2 * adv.getEnergy());
                        if (adv.isBedrockCoil())
                            amt *= 16;
                        requiredTorque += amt * activeRatio;
                        break;
                    case HIGH:
                        if (isPoweredFrom(adv.getWriteBlockEntity(), adv)) {
                            double d = adv.torquemode ? 256D : 1 / 256D;
                            recursiveFind(adv.getWriteBlockEntity(), reader, activeRatio / d);
                        }
                        break;
                }
            }*/ else if (tile instanceof BlockEntityShaft) {
                BlockEntityShaft sha = (BlockEntityShaft) tile;
                if (sha.isCross()) {
                    BlockEntity write = sha.getWriteBlockEntity();
                    BlockEntity write2 = sha.getWriteBlockEntity2();
                    if (isPoweredFrom(write, sha)) {
                        recursiveFind(write, reader, activeRatio);
                    }
                    if (isPoweredFrom(write2, sha)) {
                        recursiveFind(write2, reader, activeRatio);
                    }
                } else {
                    if (isPoweredFrom(io.getWriteBlockEntity(), io)) { //if it's anything else, it just gets its current output and then calls the recursion
                        recursiveFind(io.getWriteBlockEntity(), reader, activeRatio);
                    }
                }
            } else {
                if (isPoweredFrom(io.getWriteBlockEntity(), io)) { //if it's anything else, it just gets its current output and then calls the recursion
                    recursiveFind(io.getWriteBlockEntity(), reader, activeRatio);
                }
            }
        } else if (tile instanceof BlockEntityPowerReceiver) { //if the tile is a power Receiver, it stores its minimum torque requirement
            BlockEntityPowerReceiver pwr = (BlockEntityPowerReceiver) tile;
            /*if (tile instanceof BlockEntityBeltHub) {
                BlockEntityBeltHub hub = (BlockEntityBeltHub) tile;
                if (!hub.isEmitting) {
                    BlockPos tgt = hub.getConnection();
                    BlockEntity di = tgt != null ? tgt.getBlockEntity(hub.getLevel()) : null;
                    if (di instanceof BlockEntityBeltHub) {
                        BlockEntityBeltHub h2 = (BlockEntityBeltHub) di;
                        BlockEntity write = h2.getWriteBlockEntity();
                        BlockEntity write2 = h2.getWriteBlockEntity2();
                        if (isPoweredFrom(write, h2)) {
                            recursiveFind(write, reader, activeRatio);
                        }
                        if (isPoweredFrom(write2, h2)) {
                            recursiveFind(write2, reader, activeRatio);
                        }
                    }
                }
            } else if (tile instanceof BlockEntityBusController) {
                manageBus((BlockEntityBusController) tile, reader, activeRatio);
            } else {*/
            double req = pwr.MINTORQUE;
            double min = 0;
            /*if (tile instanceof BlockEntityExtractor) { //if it's an extractor, it stores the torque requirement of the most demanding WORKING stage
                BlockEntityExtractor ex = (BlockEntityExtractor) tile;
                int rtorque = ex.torque;
                int mintorque = 0;
                for (int i = 0; i < 4; i++) {
                    if (ex.machine.getMinTorque(i) <= rtorque) {
                        mintorque = (Math.max(mintorque, ex.machine.getMinTorque(i)));
                    }
                }
                req = mintorque;
            } else if (pwr.getMachine().isModConversionEngine()) {
                    min = Math.max(1, Math.min(Integer.MAX_VALUE, pwr.power / 256D));
                } else if (pwr.getMachine() == MachineRegistry.FRICTION) {
                    BlockEntityFurnaceHeater te = (BlockEntityFurnaceHeater) tile;
                    if (te.hasFurnace())
                        req *= 4;
                    else
                        req = 0.01;
                }*/
            requiredTorque += Math.max(activeRatio * req, min);
            //}
        } else if (tile instanceof PowerAcceptor) {
            requiredTorque += Math.max(((PowerAcceptor) tile).getMinTorque(reader.torque), 1);
        }
    }

    /*private static void manageBus(BlockEntityBusController tile, BlockEntityFlywheel reader, double activeRatio) {
        ShaftPowerBus bus = tile.getBus();
        Collection<BlockEntityPowerBus> blocks = bus.getBlocks();
        for (BlockEntityPowerBus te : blocks) {
            for (int k = 2; k < 6; k++) {
                Direction dir = Direction.values()[k];
                if (te.canOutputToSide(dir)) {
                    BlockEntity out = RotaryCraftBlockEntity.getAdjacentBlockEntity(level, pos, dir);
                    if (out instanceof BlockEntityIOMachine) {
                        BlockEntityIOMachine io = (BlockEntityIOMachine) out;
                        BlockEntity read = io.getReadBlockEntity();
                        BlockEntity read2 = io.getReadBlockEntity2();
                        BlockEntity read3 = io.getReadBlockEntity3();
                        BlockEntity read4 = io.getReadBlockEntity4();
                        if (io.getReadBlockEntity() == te || read == te || read2 == te || read3 == te || read4 == te) {
                            double ratio = te.getAbsRatio(dir);
                            if (!te.isSideSpeedMode(dir))
                                ratio = 1D / ratio;
                            recursiveFind(out, reader, ratio * activeRatio);
                        }
                    }
                }
            }
        }
    }*/

    private static boolean isPoweredFrom(BlockEntity tile, BlockEntityIOMachine caller) {
        WorldLocation loc = new WorldLocation(caller);
        if (tile instanceof BlockEntityIOMachine) {
            BlockEntityIOMachine io = (BlockEntityIOMachine) tile;
            WorldLocation read = io.getReadLocation();
            WorldLocation read2 = io.getReadLocation2();
            WorldLocation read3 = io.getReadLocation3();
            WorldLocation read4 = io.getReadLocation4();
            return loc.equals(read) || loc.equals(read2) || loc.equals(read3) || loc.equals(read4);
        } else if (tile instanceof PowerAcceptor) {
            PowerAcceptor acc = (PowerAcceptor) tile;
            if (acc.isReceiving()) {
                for (int i = 0; i < 6; i++) {
                    Direction dir = Direction.values()[i];
                    if (acc.canReadFrom(dir)) {
                        WorldLocation loc2 = new WorldLocation(tile).move(dir, 1);
                        if (loc.equals(loc2))
                            return true;
                    }
                }
            }
        }
        return false;
    }

    private static boolean canReadFrom(BlockEntityIOMachine te, BlockEntityIOMachine input) {
        return te.getReadBlockEntity() == input || te.getReadBlockEntity2() == input || te.getReadBlockEntity3() == input || te.getReadBlockEntity4() == input;
    }
}
