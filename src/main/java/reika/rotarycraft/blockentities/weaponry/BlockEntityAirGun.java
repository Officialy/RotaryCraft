///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.weaponry;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.List;
//
//public class BlockEntityAirGun extends BlockEntityPowerReceiver implements RangedEffect, DiscreteFunction {
//
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getIOSides(world, pos);
//        this.getPower(false);
//
//        if (power < MINPOWER || torque < MINTORQUE)
//            return;
//
//        //ReikaJavaLibrary.pConsole(tickcount+"/"+this.getFireRate()+":"+ReikaInventoryHelper.checkForItem(Items.arrow.itemID, inv));
//
//        if (tickcount >= this.getOperationTime() && !world.isClientSide) {
//            AABB box = this.drawAABB(pos);
//            List<LivingEntity> li = world.getEntities(LivingEntity.class, box);
//            if (li.size() > 0 && !ReikaEntityHelper.allAreDead(li, false)) {
//                this.fire(world, pos, meta, li);
//            }
//            tickcount = 0;
//        }
//    }
//
//    public void getIOSides(Level world, BlockPos pos) {
//        switch (metadata) {
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 3:
//                read = Direction.NORTH;
//                break;
//            case 2:
//                read = Direction.SOUTH;
//                break;
//        }
//    }
//
//    private double getFirePower() {
//        return ReikaMathLibrary.logbase(torque + 1, 2);
//    }
//
//    public int getOperationTime() {
//        return Math.max(16 - (int) ReikaMathLibrary.logbase(omega + 1, 2), 4);
//    }
//
//    private void fire(Level world, BlockPos pos, List<LivingEntity> li) {
//        double vx = 0;
//        double vz = 0;
//        double v = this.getFirePower() / 4;
//        switch (meta) {
//            case 1:
//                vx = v;
//                break;
//            case 0:
//                vx = -v;
//                break;
//            case 3:
//                vz = v;
//                break;
//            case 2:
//                vz = -v;
//                break;
//        }
//        boolean flag = false;
//        for (LivingEntity e : li) {
//            int x2 = (int) Math.floor(e.getY);
//            int z2 = (int) Math.floor(e.posZ);
//            int y2 = (int) e.getY() - 1;
//            Block b = world.getBlock(x2, y2, z2);
//            boolean immune = false;
//            if (e instanceof Player) {
//                Player ep = (Player) e;
//                if (this.isPlacer(ep) || ReikaPlayerAPI.isReika(ep))
//                    immune = true;
//            }
//            if (!immune && b != Blocks.AIR) {
//                e.motionX = vx;
//                e.motionZ = vz;
//                e.motionY = 0.5;
//                e.velocityChanged = true;
//                flag = true;
//            }
//        }
//        if (flag)
//            ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.explode", 1, 1); //gravity gun sound?
//    }
//
//    private AABB drawAABB(BlockPos pos) {
//        double d = 0.1;
//        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).contract(d, d, d);
//        switch (meta) {
//            case 1:
//                box.offset(1, 0, 0);
//                box.maxX += this.getRange();
//                break;
//            case 0:
//                box.offset(-1, 0, 0);
//                box.minX -= this.getRange();
//                break;
//            case 3:
//                box.offset(0, 0, 1);
//                box.maxZ += this.getRange();
//                break;
//            case 2:
//                box.offset(0, 0, -1);
//                box.minZ -= this.getRange();
//                break;
//        }
//
//        return box;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.AIRGUN;
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
//    public int getRange() {
//        return this.getMaxRange();
//    }
//
//    @Override
//    public int getMaxRange() {
//        return 10 + 2 * (int) ReikaMathLibrary.logbase(torque + 1, 2);
//    }
//
//}
