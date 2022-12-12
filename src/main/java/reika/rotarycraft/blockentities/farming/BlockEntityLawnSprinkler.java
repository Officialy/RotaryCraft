///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.farming;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.phys.AABB;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.data.WeightedRandom;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.auxiliary.interfaces.Cleanable;
//import reika.rotarycraft.base.blockentity.SprinklerBlock;
//
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.List;
//
//public class BlockEntityLawnSprinkler extends SprinklerBlock {
//
//    //Pipes cap at 2.4MPa = 24atm, so we need a modifier
//    private static final int PRESSURE_MODIFIER = 10;
//    public static final int PRESSURE_TO_HURT = 8000000 / PRESSURE_MODIFIER; //typical pressure washers range from 1.5 to 4ksi = 10-30MPa
//    public static final int PRESSURE_TO_KILL = 20000000 / PRESSURE_MODIFIER; //high enough to cause fluid injection injuries
//
//    private static final int[] GROWTH_PATTERN = {8, 5, 3, 1, 1}; //tick distribution
//
//    private static final WeightedRandom<Integer> radiusRandom = new WeightedRandom<>();
//
//    static {
//        for (int i = 0; i < GROWTH_PATTERN.length - 1; i++) {
//            radiusRandom.addEntry(i + 1, GROWTH_PATTERN[i]);
//        }
//        radiusRandom.addEntry(-1, GROWTH_PATTERN[GROWTH_PATTERN.length - 1]);
//    }
//
//    private int speed;
//
//
//    @Override
//    protected void doAnimations() {
//        if (level.isClientSide) {
//            if (this.canPerformEffects()) {
//                if (speed < 24)
//                    speed += 1;
//            } else {
//                if (speed > 0)
//                    speed--;
//            }
//        }
//        phi += speed;
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.LAWNSPRINKLER;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level level, BlockPos blockPos) {
//
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public void performEffects(Level world, BlockPos pos) {
//        RotaryAdvancements.SPRINKLER.triggerAchievement(this.getPlacer());
//        if (!world.isClientSide) {
//            for (int k = 0; k < 3; k++) {
//                this.accelerateGrowth(world, pos);
//                this.extinguishFire(world, pos);
//            }
//            if (this.getPressure() > 3000)
//                this.washMachines(world, pos);
//            if (ModList.REACTORCRAFT.isLoaded() && DragonAPI.rand.nextInt(2400) == 0)
//                this.clearRadiation(world, pos);
//            if (this.getPressure() > PRESSURE_TO_HURT)
//                this.damageMobs(world, pos, this.getPressure() >= PRESSURE_TO_KILL ? 4 : 1);
//        }
//        this.spreadWater(world, pos);
//    }
//
////    @ModDependent(ModList.REACTORCRAFT)
////    private void clearRadiation(Level world, BlockPos pos) {
////        int r = this.getRange();
////        AABB box = ReikaAABBHelper.getBlockAABB(pos).offset(0, 2, 0).expand(r, 2, r);
////        List<EntityRadiation> li = world.getEntities(EntityRadiation.class, box);
////        for (EntityRadiation e : li) {
////            e.clean();
////            if (DragonAPI.rand.nextBoolean())
////                break;
////        }
////    }
//
//    private void washMachines(Level world, BlockPos pos) {
//        int r = this.getRange();
//        int n = 3;
//        for (int c = 0; c < n; c++) {
//            int rx = ReikaRandomHelper.getRandomPlusMinus(x, r);
//            int rz = ReikaRandomHelper.getRandomPlusMinus(z, r);
//            for (int i = y; i > y - 4; i--) {
//                Block id = world.getBlockState(rx, i, rz).getBlock();
//                MachineRegistry m = MachineRegistry.getMachineFromIDandMetadata(id);
//                if (m != null) {
//                    BlockEntity te = world.getBlockEntity(rx, i, rz);
//                    if (te instanceof Cleanable) {
//                        ((Cleanable) te).clean();
//                    }
//                } else if (id != Blocks.AIR && id.isOpaqueCube())
//                    i = -999;
//            }
//        }
//    }
//
//    private void extinguishFire(Level world, BlockPos pos) {
//        int r = this.getRange();
//        int rx = ReikaRandomHelper.getRandomPlusMinus(x, r);
//        int rz = ReikaRandomHelper.getRandomPlusMinus(z, r);
//        for (int i = y; i > y - 4; i--) {
//            Block id = world.getBlockState(new BlockPos(rx, i, rz)).getBlock();
//            if (id == Blocks.FIRE) {
//                Block id2 = world.getBlockState(new BlockPos(rx, i - 1, rz)).getBlock();
//                if (id2 != Blocks.NETHERRACK) {
//                    world.setBlockToAir(rx, i, rz);
//                    ReikaSoundHelper.playSoundAtBlock(world, rx, i, rz, "DragonAPI.rand.fizz");
//                }
//            } else if (id != Blocks.AIR && id.isOpaqueCube())
//                i = -999;
//        }
//    }
//
//    private void accelerateGrowth(Level world, BlockPos pos) {
//        int r = this.calcRange();
//        int rx = ReikaRandomHelper.getRandomPlusMinus(x, r);
//        int rz = ReikaRandomHelper.getRandomPlusMinus(z, r);
//        for (int i = y; i > y - 4; i--) {
//            Block id = world.getBlockState(new BlockPos(rx, i, rz)).getBlock();
//            if (id == Blocks.FARMLAND) {
//                ReikaWorldHelper.hydrateFarmland(world, rx, i, rz, false);
//                i = -999;
//            } else if (id != Blocks.AIR && id.isOpaqueCube()) {
//                i = -999;
//            } else if (DragonAPI.rand.nextInt(8) == 0) {
//                ReikaCropHelper crop = ReikaCropHelper.getCrop(id);
//                ModCrop modcrop = ModCropList.getModCrop(id, meta);
//                if (crop != null && !crop.isRipe(meta)) {
//                    world.setBlockMetadataWithNotify(rx, i, rz, meta + 1, 3);
//                } else if (modcrop != null && !modcrop.isRipe(world, rx, i, rz)) {
//                    //world.setBlockMetadataWithNotify(rx, i, rz, meta+1, 3);
//                    id.updateTick(world, rx, i, rz, DragonAPI.rand);
//                    BlockTickEvent.fire(world, rx, i, rz, id, UpdateFlags.FORCED.flag);
//                    world.markBlockForUpdate(rx, i, rz);
//                } else if (this.shouldTick(world, pos, id)) {
//                    id.updateTick(world, rx, i, rz, DragonAPI.rand);
//                }
//            }
//        }
//    }
//
//    private boolean shouldTick(Level world, BlockPos pos, Block id) {
//        ReikaPlantHelper p = ReikaPlantHelper.getPlant(id);
//        return p != null && p.grows();
//    }
//
//    private int calcRange() {
//        int r = radiusRandom.getRandomEntry();
//        if (r == -1)
//            r = this.getMaxRange();
//        return r;
//    }
//
//    private void damageMobs(Level world, BlockPos pos, int dmg) {
//        int r = this.getRange();
//        AABB box = ReikaAABBHelper.getBlockAABB(pos).offset(0, 1, 0).expand(r, 1, r);
//        List<LivingEntity> li = world.getEntities(LivingEntity.class, box);
//        for (LivingEntity e : li) {
//            RayTracer rt = new RayTracer(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, e.getY, e.getY() + 0.5, e.posZ);
//            rt.airOnly = true;
//            if (rt.isClearLineOfSight(world)) {
//                e.hurt(DamageSource.drown, 0.5F);
//            }
//        }
//    }
//
//    private void spreadWater(Level world, BlockPos pos) {
//        int d = Math.max(0, RotaryConfig.COMMON.SPRINKLER.get());
//
//        double ypos = y + 0.125;
//        double vel;
//        double r = this.getRange() / 10D;
//
//        double py = y - 0.1875D + 0.5;
//        for (int i = 0; i < DragonAPI.rand.nextInt(1 + d); i++) {
//            double px = x - 1 + 2 * DragonAPI.rand.nextFloat();
//            double pz = z - 1 + 2 * DragonAPI.rand.nextFloat();
//            world.addParticle("splash", px + 0.5, py, pz + 0.5, 0, 0, 0);
//        }
//        for (int i = 0; i < 3; i++)
//            this.createWaterStream(world, pos, i * 120 + 60);
//    }
//
//    private void createWaterStream(Level world, BlockPos pos, float offset) {
//        int d = Math.max(0, RotaryConfig.COMMON.SPRINKLER.get());
//        int r = this.getRange();
//        double dx = 0.6 * Math.sin(Math.toRadians(phi + offset));
//        double dz = 0.6 * Math.cos(Math.toRadians(phi + offset));
//        for (int i = 0; i < 6 * d; i++) {
//            double v = DragonAPI.rand.nextInt((1 + r) * 10) / 72D;
//            world.addParticle("splash", x + 0.5 + dx, y + 0.75, z + 0.5 + dz, dx * v - 0.025 + 0.05 * DragonAPI.rand.nextDouble(), 0, dz * v - 0.025 + 0.05 * DragonAPI.rand.nextDouble());
//        }
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        //NBT.putInt("spd", speed);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        //speed = NBT.getInt("speed");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public int getCapacity() {
//        return 5;
//    }
//
//    @Override
//    public int getWaterConsumption() {
//        return 1;
//    }
//
//    @Override
//    public Direction getPipeDirection() {
//        return Direction.DOWN;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
