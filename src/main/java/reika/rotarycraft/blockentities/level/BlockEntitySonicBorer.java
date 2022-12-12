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
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.resources.ResourceKey;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.LiquidBlock;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.interfaces.PressureTE;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class BlockEntitySonicBorer extends BlockEntityPowerReceiver implements PressureTE {
//
//    public static final int FIRE_PRESSURE = 400; //4 atm
//    public static final int MAXPRESSURE = 1000;
//    public static final int FOV = 3;
//    public int xstep;
//    public int ystep;
//    public int zstep;
//    private int pressure;
//
//    public static boolean canDrop(Level world, BlockPos pos) {
//        Block b = world.getBlock(pos);
//        if (b == Blocks.AIR)
//            return true;
//        if (b.getBlockHardness(world, pos) < 0)
//            return false;
//        if (b instanceof LiquidBlock)
//            return false;
//        if (b instanceof BlockFluidBase)
//            return false;
//
//        return !ReikaItemHelper.matchStackWithBlock(RotaryItems.shieldblock, b);
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getIOSides(world, pos);
//        this.getPower(false);
//        this.updatePressure(world, pos);
//        if (this.canFire(world, pos)) {
//            this.fire(world, pos);
//            pressure -= FIRE_PRESSURE;
//        }
//        if (pressure > MAXPRESSURE) {
//            this.overpressure(world, pos);
//        }
//    }
//
//    public final void getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 1:
//                read = Direction.WEST;
//                xstep = 1;
//                ystep = 0;
//                zstep = 0;
//                break;
//            case 0:
//                read = Direction.EAST;
//                xstep = -1;
//                ystep = 0;
//                zstep = 0;
//                break;
//            case 3:
//                read = Direction.NORTH;
//                xstep = 0;
//                ystep = 0;
//                zstep = 1;
//                break;
//            case 2:
//                read = Direction.SOUTH;
//                xstep = 0;
//                ystep = 0;
//                zstep = -1;
//                break;
//            case 5:    //moving up
//                read = Direction.UP;
//                xstep = 0;
//                ystep = -1;
//                zstep = 0;
//                break;
//            case 4:    //moving down
//                read = Direction.DOWN;
//                xstep = 0;
//                ystep = 1;
//                zstep = 0;
//                break;
//        }
//    }
//
//    private void fire(Level world, BlockPos pos) {
//        int r = this.getDistanceToSurface(world, pos);
//        if (r < 0)
//            return;
//
//        EntitySonicShot e = new EntitySonicShot(world, this, placer);
//        if (!world.isClientSide) {
//            world.addFreshEntity(e);
//        }
////        ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.explode");
//    }
//
//    private int getDistanceToSurface(Level world, BlockPos pos) {
//        for (int m = 1; m < this.getMaxRange(); m++) {
//            int dx = pos.getX() + m * xstep;
//            int dy = pos.getY() + m * ystep;
//            int dz = pos.getZ() + m * zstep;
//            boolean nonair = false;
//            int k = FOV;
//            if (xstep != 0) {
//                for (int i = pos.getZ() - k; i <= pos.getZ() + k; i++) {
//                    for (int j = pos.getY() - k; j <= pos.getY() + k; j++) {
//                        if (!canDrop(world, new BlockPos(dx, j, i)))
//                            return -1;
//                        Block b = world.getBlockState(new BlockPos(dx, j, i)).getBlock();
//                        if (b != Blocks.AIR)
//                            nonair = true;
//                    }
//                }
//            } else if (zstep != 0) {
//                for (int i = pos.getX() - k; i <= pos.getX() + k; i++) {
//                    for (int j = pos.getY() - k; j <= pos.getY() + k; j++) {
//                        if (!canDrop(world, new BlockPos(i, j, dz)))
//                            return -1;
//                        Block b = world.getBlockState(new BlockPos(i, j, dz)).getBlock();
//                        if (b != Blocks.AIR)
//                            nonair = true;
//                    }
//                }
//            } else if (ystep != 0) {
//                for (int i = pos.getX() - k; i <= pos.getX() + k; i++) {
//                    for (int j = pos.getZ() - k; j <= pos.getZ() + k; j++) {
//                        if (!canDrop(world, new BlockPos(i, dy, j)))
//                            return -1;
//                        Block b = world.getBlockState(new BlockPos(i, dy, j)).getBlock();
//                        if (b != Blocks.AIR)
//                            nonair = true;
//                    }
//                }
//            }
//            if (nonair)
//                return m;
//        }
//        return this.getMaxRange();
//    }
//
//    private int getMaxRange() {
//        return Math.max(RotaryConfig.COMMON.SONICBORERRANGE.get(), 64);
//    }
//
//    private boolean canFire(Level world, BlockPos pos) {
//        if (pressure < FIRE_PRESSURE)
//            return false;
//        if (power < MINPOWER || torque < MINTORQUE)
//            return false;
//        return pos.getY() - this.getDistanceToSurface(world, pos) > 0 || ystep != -1;
//    }
//
//    private int getPressureIncrement() {
//        int amt3 = (int) (power / 65536);
//        return amt3;
//    }
//
//    public int[] getTargetPosn() {
//        Level world = level;
//        int x = worldPosition.getX();
//        int y = worldPosition.getY();
//        int z = worldPosition.getZ();
//        int[] arr = new int[3];
//        int r = this.getDistanceToSurface(world, worldPosition);
//        if (r < 0)
//            r = 0;
//        arr[0] = x + xstep * r;
//        arr[1] = y + ystep * r;
//        arr[2] = z + zstep * r;
//        return arr;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.SONICBORER;
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
//    public void updatePressure(Level world, BlockPos pos) {
//        int Pamb = 101;
////        if (world == Level.NETHER)
////            Pamb = 2000;
//        int dP = pressure - Pamb;
//        int pd = dP / 384 + 1;
//        //ReikaJavaLibrary.pConsole(dP+":"+pd+":"+(pressure-pd), Dist.DEDICATED_SERVER);
//        if (dP > 0)
//            pressure -= pd;
//        else
//            pressure++;
//        if (power >= MINPOWER && torque >= MINTORQUE) {
//            pressure += this.getPressureIncrement();
//        }
//        //ReikaJavaLibrary.pConsole(pressure, Dist.DEDICATED_SERVER);
//    }
//
//    @Override
//    public void addPressure(int press) {
//        pressure += press;
//    }
//
//    @Override
//    public int getPressure() {
//        return pressure;
//    }
//
//    @Override
//    public void overpressure(Level world, BlockPos pos) {
//        float f = 4;
//        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, f, RotaryConfig.COMMON.BLOCKDAMAGE.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.BLOCK);
//
//        world.explode(null, pos.getX() + 0.5, pos.getY() + 1.5, pos.getZ() + 0.5, f, RotaryConfig.COMMON.BLOCKDAMAGE.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.BLOCK);
//        world.explode(null, pos.getX() + 0.5, pos.getY() - 0.5, pos.getZ() + 0.5, f, RotaryConfig.COMMON.BLOCKDAMAGE.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.BLOCK);
//
//        world.explode(null, pos.getX() + 1.5, pos.getY() + 0.5, pos.getZ() + 0.5, f, RotaryConfig.COMMON.BLOCKDAMAGE.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.BLOCK);
//        world.explode(null, pos.getX() - 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, f, RotaryConfig.COMMON.BLOCKDAMAGE.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.BLOCK);
//
//        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 1.5, f, RotaryConfig.COMMON.BLOCKDAMAGE.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.BLOCK);
//        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() - 0.5, f, RotaryConfig.COMMON.BLOCKDAMAGE.get() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.BLOCK);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putInt("press", pressure);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        pressure = NBT.getInt("press");
//    }
//
//    public int getDistanceToSurface() {
//        return this.getDistanceToSurface(level, worldPosition);
//    }
//
//    @Override
//    public int getMaxPressure() {
//        return MAXPRESSURE;
//    }
//
//}
