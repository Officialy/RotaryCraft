///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.transmission;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//public class BlockEntityChainDrive extends BlockEntityBeltHub {
//
//    @Override
//    public int getTorque(int input) {
//        input = super.getTorque(input);
//        if (input > this.getMaxTorque()) {
//            ReikaSoundHelper.playSoundAtBlock(level, xCoord, yCoord, zCoord, "DragonAPI.rand.break");
//            this.reset();
//            this.resetOther();
//        }
//        return this.isSplitting() ? input / 2 : input;
//    }
//
//    @Override
//    public int getOmega(int input) {
//        input = super.getOmega(input);
//        if (input > this.getMaxSmoothSpeed()) {
//            level.setBlockToAir(xCoord, yCoord, zCoord);
//            this.resetOther();
//            level.explode(null, xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, 2, true);
//        }
//        return input;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.CHAIN;
//    }
//
//    @Override
//    public int[] getBeltColor() {
//        return new int[]{80, 80, 80};
//    }
//
//    @Override
//    public ItemStack getBeltItem() {
//        return RotaryItems.chain.copy();
//    }
//
//    @Override
//    public int getMaxTorque() {
//        return 16384;
//    }
//
//    @Override
//    public int getMaxSmoothSpeed() {
//        return 65536;
//    }
//
//}
