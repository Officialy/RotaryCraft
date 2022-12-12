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
//import net.minecraft.core.Direction;
//import net.minecraft.entity.EntityList;
//import net.minecraft.entity.LivingEntity;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.BlockEntity.BlockEntityMobSpawner;
//import net.minecraft.BlockEntity.MobSpawnerBaseLogic;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.SpawnerBlockEntity;
//import net.minecraftforge.common.MinecraftForge;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.interfaces.blockentity.GuiController;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.rotarycraft.api.Interfaces.ControllableSpawner;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.Collection;
//
//public class BlockEntitySpawnerController extends BlockEntityPowerReceiver implements GuiController, DiscreteFunction, ConditionalOperation {
//    public static final int BASEDELAY = 800; //40s default max spawner delay
//    public static final String CONTROLLED_SPAWN_TAG = "ControllerSpawned";
//    private final StepTimer timer = new StepTimer(0);
//    public boolean disable;
//    private SpawnerControl control = null;
//    private int setDelay = BASEDELAY;
//
//    private static void flagNoDespawn(Entity e) {
//        CompoundTag tag = e.getEntityData();
//        tag.putBoolean(CONTROLLED_SPAWN_TAG, true);
//    }
//
//    public static boolean isFlaggedNoDespawn(Entity e) {
//        return e.getEntityData().getBoolean(CONTROLLED_SPAWN_TAG);
//    }
//
//    private static void runVanillaSpawnCycle(SpawnerBlockEntity tile, BlockEntitySpawnerController c) {
//        Level world = tile.getLevel();
//        int x = tile.xCoord;
//        int y = tile.yCoord;
//        int z = tile.zCoord;
//        MobSpawnerBaseLogic lgc = tile.func_145881_a();
//        lgc.maxNearbyEntities = Short.MAX_VALUE;
//        lgc.activatingRangeFromPlayer = Short.MAX_VALUE;
//
//        if (world.isClientSide) {
//            double var1 = x + world.DragonAPI.rand.nextFloat();
//            double var3 = y + world.DragonAPI.rand.nextFloat();
//            double var5 = z + world.DragonAPI.rand.nextFloat();
//            world.addParticle("smoke", var1, var3, var5, 0, 0, 0);
//            world.addParticle("flame", var1, var3, var5, 0, 0, 0);
//            lgc.field_98284_d = lgc.field_98287_c;
//            lgc.field_98287_c = (lgc.field_98287_c + 1000D / (lgc.spawnDelay + 200)) % 360;
//        } else {
//            for (int i = 0; i < lgc.spawnCount; i++) {
//                Entity toSpawn = EntityList.createEntityByName(lgc.getEntityNameToSpawn(), world);
//
//                // This is the max-6 code
//                //int var4 = world.getEntities(var13.getClass(), AABB.getBoundingBox(pos, x+1, y+1, z+1).expand(spawnRange*2, 4, spawnRange*2)).size();
//
//                if (toSpawn != null) {
//                    double ex = x + (world.random.nextDouble() - world.random.nextDouble()) * lgc.spawnRange;
//                    double ey = y + world.random.nextInt(3) - 1;
//                    double ez = z + (world.random.nextDouble() - world.random.nextDouble()) * lgc.spawnRange;
//                    LivingEntity livingSpawn = toSpawn instanceof LivingEntity ? (LivingEntity) toSpawn : null;
//                    toSpawn.setLocationAndAngles(ex, ey, ez, world.random.nextFloat() * 360, 0);
//
//                    if (livingSpawn == null || livingSpawn.getCanSpawnHere()) {
//                        lgc.func_98265_a(toSpawn);
//                        flagNoDespawn(toSpawn);
//                        world.playAuxSFX(2004, pos, 0);
//                        if (livingSpawn != null) {
//                            livingSpawn.spawnExplosionParticle();
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    public int getOperationTime() {
//        int time = BASEDELAY - 40 * (int) ReikaMathLibrary.logbase(omega, 2);
//        if (time < 0)
//            time = 0; //0 tick minimum
//        return time;
//    }
//
//    public int getDelay() {
//        if (setDelay >= this.getOperationTime())
//            return setDelay;
//        else
//            return this.getOperationTime();
//    }
//
//    public void setDelay(int delay) {
//        setDelay = delay;
//        timer.setCap(setDelay);
//        timer.setTick(Math.min(setDelay - 1, timer.getTick()));
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        if (!this.isValidLocation(world, pos)) {
//            disable = false;
//            this.setDelay(0);
//            timer.setTick(0);
//            omega = torque = 0;
//            power = 0;
//            this.setPointingOffset(0, -1, 0);
//            control = null;
//            return;
//        }
//        this.getOffsetPower4Sided(0, -1, 0, true); //The spawner itself is the power input
//        if (power >= MINPOWER && setDelay > 0)
//            this.applyToSpawner(world, pos);
//    }
//
//    private void shutdownSpawner(Level world, BlockPos pos) {
//        control.setInactive();
//        for (int i = 0; i < 4; i++) {
//            double var1 = xCoord + level.random.nextFloat();
//            double var3 = (float) yCoord - 1 + level.DragonAPI.rand.nextFloat();
//            double var5 = zCoord + level.random.nextFloat();
//            double var11 = xCoord - 0.25 + 1.5 * level.DragonAPI.rand.nextFloat();
//            double var15 = zCoord - 0.25 + 1.5 * level.DragonAPI.rand.nextFloat();
//            level.addParticle("reddust", var11, var3, var15, 0, 0, 0);
//            level.addParticle("crit", var1, var3, var5, -0.3 + 0.6 * level.random.nextFloat(), 0.4 * level.random.nextFloat(), -0.3 + 0.6 * level.random.nextFloat());
//        }
//    }
//
//    private void applyToSpawner(Level world, BlockPos pos) {
//        control = new SpawnerControl(getAdjacentBlockEntity(Direction.DOWN));
//        if (disable || world.isBlockIndirectlyGettingPowered(x, y - 1, z)) {
//            this.shutdownSpawner(world, pos);
//        } else if (this.canSpawn(world, pos)) {
//            timer.update();
//            control.setDelay(setDelay - timer.getTick());
//            timer.checkCap();
//        } else {
//            //hijackdelay = 255; //"do not affect"
//        }
//        if (control.getDelay() <= 0)
//            control.spawnCycle(this);
//    }
//
//    private int getNumberNearbySpawns(Level world, BlockPos pos, Class ent) {
//        return world.getEntities(ent, ReikaAABBHelper.getBlockAABB(pos).expand(16, 24, 16)).size();
//    }
//
//    private boolean canSpawn(Level world, BlockPos pos) {
//        Class ent = this.getEntityClass();
//        int num = this.getNumberNearbySpawns(world, pos, ent);
//        return num < this.getSpawnLimit();
//    }
//
//    private Class getEntityClass() {
//        return control.getEntity();
//    }
//
//    private int getSpawnLimit() {
//        return 99;//Math.max(24, RotaryConfig.COMMON.SPAWNERLIMIT.get());
//    }
//
//    public boolean isValidLocation(Level world, BlockPos pos) {
//        Block b = world.getBlockState(pos.below()).getBlock();
//        return b == Blocks.SPAWNER || getAdjacentBlockEntity(Direction.DOWN) instanceof ControllableSpawner;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("setdelay", setDelay);
//        NBT.putBoolean("disable", disable);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        this.setDelay(NBT.getInt("setdelay"));
//        disable = NBT.getBoolean("disable");
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.SPAWNERCONTROLLER;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.isValidLocation(level, xCoord, yCoord, zCoord);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Spawner";
//    }
//
//    private static class SpawnerControl {
//
//        private final BlockEntityMobSpawner tile;
//        private final ControllableSpawner proxy;
//        private final boolean vanilla;
//
//        private SpawnerControl(BlockEntity te) {
//            vanilla = te instanceof BlockEntityMobSpawner;
//            tile = vanilla ? (BlockEntityMobSpawner) te : null;
//            proxy = vanilla ? null : (ControllableSpawner) te;
//        }
//
//        private void setInactive() {
//            if (vanilla) {
//                tile.func_145881_a().spawnDelay = 5;
//            } else {
//                proxy.setInactive();
//            }
//        }
//
//        private int getDelay() {
//            return vanilla ? tile.func_145881_a().spawnDelay : proxy.getCurrentSpawnDelay();
//        }
//
//        private void setDelay(int t) {
//            if (vanilla) {
//                tile.func_145881_a().spawnDelay = t;
//            } else {
//                proxy.setCurrentSpawnDelay(t);
//            }
//        }
//
//        private void spawnCycle(BlockEntitySpawnerController te) {
//            if (vanilla) {
//                runVanillaSpawnCycle(tile, te);
//            } else {
//                Collection<Entity> c = proxy.performSpawnCycle();
//                for (Entity e : c)
//                    flagNoDespawn(e);
//            }
//            MinecraftForge.EVENT_BUS.post(new SpawnerControllerSpawnEvent(te, this.getEntity()));
//        }
//
//        private Class<? extends LivingEntity> getEntity() {
//            return vanilla ? (Class) EntityList.stringToClassMapping.get(tile.func_145881_a().getEntityNameToSpawn()) : proxy.getSpawningEntityClass();
//        }
//
//    }
//}
