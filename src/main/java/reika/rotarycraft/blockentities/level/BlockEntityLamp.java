///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.level;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
//import reika.dragonapi.interfaces.blockentity.BreakAction;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntitySpringPowered;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryBlocks;
//
//public class BlockEntityLamp extends BlockEntitySpringPowered implements InertIInv, RangedEffect, OneSlotMachine, BreakAction {
//
//    public static final int MAXRANGE = 12;
//    private final BlockArray light = new BlockArray();
//    private boolean canlight;
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.LAMP;
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
//    public void updateEntity(Level world, BlockPos pos) {
//        boolean red = this.hasRedstoneSignal();
//
//        if (!red)
//            this.updateCoil();
//
//        if (world.isClientSide)
//            return;
//
//        if (red)
//            canlight = false;
//
//        if (!canlight) {
//            this.goDark();
//            return;
//        }
//
//        if (light.isEmpty()) {
//            for (int i = 1; i <= this.getRange(); i++) {
//                if (this.canEditAt(world, x + i, y, z))
//                    light.addBlockCoordinate(x + i, y, z);
//                if (this.canEditAt(world, x, y + i, z))
//                    light.addBlockCoordinate(x, y + i, z);
//                if (this.canEditAt(world, pos + i))
//                    light.addBlockCoordinate(pos + i);
//                if (this.canEditAt(world, x - i, y, z))
//                    light.addBlockCoordinate(x - i, y, z);
//                if (this.canEditAt(world, x, y - i, z))
//                    light.addBlockCoordinate(x, y - i, z);
//                if (this.canEditAt(world, pos - i))
//                    light.addBlockCoordinate(pos - i);
//            }
//            for (int r = 2; r <= this.getRange() * 0.8; r += 2) {
//                if (this.canEditAt(world, x + r, y, z + r))
//                    light.addBlockCoordinate(x + r, y, z + r);
//                if (this.canEditAt(world, x - r, y, z + r))
//                    light.addBlockCoordinate(x - r, y, z + r);
//                if (this.canEditAt(world, x + r, y, z - r))
//                    light.addBlockCoordinate(x + r, y, z - r);
//                if (this.canEditAt(world, x - r, y, z - r))
//                    light.addBlockCoordinate(x - r, y, z - r);
//
//                if (this.canEditAt(world, x + r, y + r, z + r))
//                    light.addBlockCoordinate(x + r, y + r, z + r);
//                if (this.canEditAt(world, x - r, y + r, z + r))
//                    light.addBlockCoordinate(x - r, y + r, z + r);
//                if (this.canEditAt(world, x + r, y + r, z - r))
//                    light.addBlockCoordinate(x + r, y + r, z - r);
//                if (this.canEditAt(world, x - r, y + r, z - r))
//                    light.addBlockCoordinate(x - r, y + r, z - r);
//
//                if (this.canEditAt(world, x + r, y - r, z + r))
//                    light.addBlockCoordinate(x + r, y - r, z + r);
//                if (this.canEditAt(world, x - r, y - r, z + r))
//                    light.addBlockCoordinate(x - r, y - r, z + r);
//                if (this.canEditAt(world, x + r, y - r, z - r))
//                    light.addBlockCoordinate(x + r, y - r, z - r);
//                if (this.canEditAt(world, x - r, y - r, z - r))
//                    light.addBlockCoordinate(x - r, y - r, z - r);
//            }
//            return;
//        }
//        //int[] xyz = light.getNextAndMoveOn();
//        for (int n = 0; n < light.getSize(); n++) {
//            BlockPos c = light.getNthBlock(n);
//            if (c.getBlock(world) == Blocks.AIR)
//                c.setBlock(world, Blocks.LIGHT.get(), 15);
//            level.func_147451_t(c.xCoord, c.yCoord, c.zCoord);
//        }
//    }
//
//    public boolean canEditAt(Level world, BlockPos pos) {
//        Block id = world.getBlock(pos);
//        return id == Blocks.AIR || id.isAir(world, pos);
//    }
//
//    private void goDark() {
//        for (int n = 0; n < light.getSize(); n++) {
//            BlockPos c = light.getNthBlock(n);
//            if (c.getBlock(level) == Blocks.LIGHT.get())
//                c.setBlock(level, Blocks.AIR);
//            level.func_147451_t(c.xCoord, c.yCoord, c.zCoord);
//        }
//    }
//
//    private void updateCoil() {
//        if (!this.hasCoil()) {
//            canlight = false;
//            return;
//        }
//        tickcount++;
//        if (tickcount > this.getUnwindTime()) {
//            ItemStack is = this.getDecrementedCharged();
//            inv[0] = is;
//            tickcount = 0;
//        }
//        canlight = true;
//    }
//
//    @Override
//    public int getRange() {
//        return this.getMaxRange();
//    }
//
//    @Override
//    public int getMaxRange() {
//        return MAXRANGE;
//    }
//
//    private void clearAll() {
//        for (int k = 0; k < light.getSize(); k++) {
//            BlockPos c = light.getNthBlock(k);
//            c.setBlock(level, Blocks.AIR);
//        }
//    }
//
//    @Override
//    public int getBaseDischargeTime() {
//        return 120;
//    }
//
//    @Override
//    public void breakBlock() {
//        this.clearAll();
//    }
//
//}
