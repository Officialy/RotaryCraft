///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.auxiliary;
//
//import reika.rotarycraft.blockentities.transmission.BlockEntityBusController;
//import reika.rotarycraft.blockentities.transmission.BlockEntityPowerBus;
//import net.minecraft.core.Direction;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.Collections;
//
//public class ShaftPowerBus {
//
//    private final BlockEntityBusController hub;
//    private final ArrayList<BlockEntityPowerBus> blocks = new ArrayList<>();
//
//    private int sides = 0;
//
//    public ShaftPowerBus(BlockEntityBusController hub) {
//        this.hub = hub;
//    }
//
//    @Override
//    public String toString() {
//        StringBuilder sb = new StringBuilder();
//        sb.append("Power Bus Receiving " + this.getInputTorque() + " Nm @ " + this.getSpeed() + " rad/s\n");
//        sb.append(this.getInputPower() + "W is being split to " + this.getTotalOutputSides() + " devices\n");
//        sb.append("(Power per side is " + this.getInputPower() / this.getTotalOutputSides() + "W)");
//        return sb.toString();
//    }
//
//    public void removeBlock(BlockEntityPowerBus bus) {
//        blocks.remove(bus);
//    }
//
//    public boolean addBlock(BlockEntityPowerBus bus) {
//        if (blocks.contains(bus))
//            return false;
//        else {
//            blocks.add(bus);
//            this.update();
//            return true;
//        }
//    }
//
//    public BlockEntityBusController getController() {
//        return hub;
//    }
//
//    public long getInputPower() {
//        return hub.power;
//    }
//
//    public int getSpeed() {
//        return hub.omega;
//    }
//
//    public int getInputTorque() {
//        return hub.torque;
//    }
//
//    public void recalcTotalOutputSides() {
//        sides = 0;
//        for (int i = 0; i < blocks.size(); i++) {
//            BlockEntityPowerBus te = blocks.get(i);
//            for (int k = 2; k < 6; k++) {
//                Direction dir = Direction.values()[k];
//                if (te.canOutputToSide(dir))
//                    sides++;
//            }
//        }
//    }
//
//    public int getTotalOutputSides() {
//        return Math.max(sides, 1);
//    }
//
//    public void update() {
//        this.recalcTotalOutputSides();
//    }
//
//    public void clear() {
//        for (int i = 0; i < blocks.size(); i++) {
//            BlockEntityPowerBus te = blocks.get(i);
//            te.clearBus();
//        }
//        blocks.clear();
//    }
//
//    public Collection<BlockEntityPowerBus> getBlocks() {
//        return Collections.unmodifiableCollection(blocks);
//    }
//
//    public int getSize() {
//        return blocks.size();
//    }
//
//}
