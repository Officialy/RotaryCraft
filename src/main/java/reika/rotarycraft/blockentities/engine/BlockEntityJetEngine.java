///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.engine;
//
//
//import net.minecraft.client.Minecraft;
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.util.Mth;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.EquipmentSlot;
//import net.minecraft.world.entity.ExperienceOrb;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.ambient.Bat;
//import net.minecraft.world.entity.animal.Chicken;
//import net.minecraft.world.entity.decoration.ItemFrame;
//import net.minecraft.world.entity.decoration.Painting;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.monster.Silverfish;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Explosion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraft.world.phys.AABB;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.common.NeoForge;
//import net.neoforged.fluids.FluidStack;
//import net.neoforged.fml.loading.FMLLoader;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.ReikaAABBHelper;
//import reika.dragonapi.libraries.ReikaFluidHelper;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.io.ReikaSoundHelper;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Interfaces.ThermalMachine;
//import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
//import reika.rotarycraft.base.EntityTurretShot;
//import reika.rotarycraft.base.blockentity.BlockEntityEngine;
//import reika.rotarycraft.items.tools.ItemEngineUpgrade.Upgrades;
//import reika.rotarycraft.registry.*;
//
//import java.util.ArrayList;
//import java.util.HashSet;
//import java.util.List;
//import java.util.Locale;
//
//public class BlockEntityJetEngine extends BlockEntityEngine implements NBTMachine, UpgradeableMachine {
//
//    public static final int BASE_CONSUMPTION = 10;
//    public static final int AFTERBURNER_CONSUMPTION = 25;
//    private static final RayTracer tracer;
//
//    static {
//        tracer = new RayTracer(0, 0, 0, 0, 0, 0);
//    }
//
//    private final StepTimer jetstarttimer = new StepTimer(479);
//    /**
//     * Used in jet engines
//     */
//    public int FOD = 0;
//    private boolean isJetFailing = false;
//    private int dumpx;
//    private int dumpz;
//    private int dumpvx;
//    private int dumpvz;
//    private int afterburnTick = 2000;
//    private int chickenCount = 0;
//    private boolean isChoking = false;
//    private boolean canAfterBurn;
//    private boolean burnerActive;
//
//    public BlockEntityJetEngine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    public int getFuelLevel() {
//        return fuel.getLevel();
//    }
//
//    @Override
//    protected void consumeFuel() {
//        fuel.removeLiquid(this.getConsumedFuel());
//    }
//
//    @Override
//    protected int getConsumedFuel() {
//        return this.isAfterburning() ? AFTERBURNER_CONSUMPTION : BASE_CONSUMPTION;
//    }
//
//    @Override
//    protected void internalizeFuel() {
//
//    }
//
//    @Override
//    protected boolean canBeThrottled() {
//        return !this.isAfterburning();
//    }
//
//    @Override
//    protected boolean getRequirements(Level world, BlockPos pos) {
//        if (FOD >= 8) {
//            jetstarttimer.reset();
//            return false;
//        }
//        if (fuel.getLevel() <= 0) {
//            jetstarttimer.reset();
//            return false;
//        }
//        if (power > 0)
//            RotaryAdvancements.JETENGINE.triggerAchievement(this.getPlacer());
//        return true;
//    }
//
//    private void checkJetFailure(Level world, BlockPos pos) {
//        if (isJetFailing)
//            this.jetEngineDetonation(world, pos);
//        else if (FOD > 0 && DragonAPI.rand.nextInt(DifficultyEffects.JETFAILURE.getInt() * (9 - FOD)) == 0) {
//            this.triggerJetFailing(world, pos);
//        }
//    }
//
//    public float getChokedFraction(Level world, BlockPos blockPos) {
//        int[] pos = {blockPos.getX(), blockPos.getZ()};
//        switch (meta) {
//            case 0:
//                pos[0] += 1;
//                break;
//            case 1:
//                pos[0] += -1;
//                break;
//            case 2:
//                pos[1] += 1;
//                break;
//            case 3:
//                pos[1] += -1;
//                break;
//        }
//        Block b = world.getBlock(pos[0], y, pos[1]);
//        int dmg = world.getBlockMetadata(pos[0], y, pos[1]);
//        if (b == Blocks.AIR)
//            return 1;
//        if (b.getCollisionBoundingBoxFromPool(world, pos[0], y, pos[1]) == null)
//            return 1;
//        if (b == Blocks.FENCE || b == Blocks.NETHER_BRICK_FENCE)
//            return 0.75F;
//        if (b == Blocks.IRON_BARS)
//            return 1F;
//        if (b == Blocks.COBBLESTONE_WALL)
//            return 0.25F;
//        if (b == Blocks.GLASS_PANE)
//            return 0.5F;
//        if (b.getBlockBoundsMaxX() > 0.875 && b.getBlockBoundsMaxY() > 0.875 && b.getBlockBoundsMaxZ() > 0.875)
//            if (b.getBlockBoundsMinX() < 0.125 && b.getBlockBoundsMinY() < 0.125 && b.getBlockBoundsMinZ() < 0.125)
//                return 0;
//        double frac;
//        double dx = b.getBlockBoundsMaxX() - b.getBlockBoundsMinX();
//        double dy = b.getBlockBoundsMaxY() - b.getBlockBoundsMinY();
//        double dz = b.getBlockBoundsMaxZ() - b.getBlockBoundsMinZ();
//        if (b.getBlockBoundsMaxX() <= 0.125 || b.getBlockBoundsMinX() >= 0.875)
//            dx = 0;
//        if (b.getBlockBoundsMaxY() <= 0.125 || b.getBlockBoundsMinY() >= 0.875)
//            dy = 0;
//        if (b.getBlockBoundsMaxZ() <= 0.125 || b.getBlockBoundsMinZ() >= 0.875)
//            dz = 0;
//        if (b.getBlockBoundsMaxY() >= 0.75)
//            dy += 0.125;
//        //ReikaJavaLibrary.pConsole(dx+"  "+dy+"  "+dz);
//        frac = 1 - (dx * dy * dz);
//        return (float) frac;
//    }
//
//    private void heatJet(Level world, BlockPos pos) {
//        if (this.isOn() && this.tickcount % 10 == 0) {
//            int max = this.getMaxExhaustTemperature() * omega / type.getSpeed();
//            if (max > temperature)
//                temperature = Math.min(temperature + Math.max(1, (max - temperature) / 16), max);
//            else if (!this.isAfterburning()) {
//                temperature = Math.max(temperature - Math.max(1, (temperature - max) / 32), max);
//            }
//        }
//        int T = temperature;
//        int r = this.isAfterburning() ? 6 : 4;
//        for (int i = 1; i < r; i++) {
//            int dx = x + write.getStepX() * i;
//            int dz = z + write.getStepZ() * i;
//            BlockEntity te = this.getBlockEntity(dx, y, dz);
//            if (te instanceof TemperatureTE) {
//                if (((TemperatureTE) te).allowExternalHeating()) {
//                    int dT = T - ((TemperatureTE) te).getTemperature();
//                    ((TemperatureTE) te).addTemperature(dT);
//                }
//            } else if (te instanceof ThermalMachine) {
//                ((ThermalMachine) te).setTemperature(T);
//            }
//            if (this.isAfterburning()) {
//                ReikaWorldHelper.temperatureEnvironment(world, dx, y, dz, Math.min(1400, T));
//            }
//        }
//        int x1 = write.getStepX() != 0 ? write.getStepX() > 0 ? x : x - 4 : x;
//        int x2 = write.getStepX() != 0 ? write.getStepX() > 0 ? x + 5 : x + 1 : x + 1;
//        int z1 = write.getStepZ() != 0 ? write.getStepZ() > 0 ? z : z - 4 : z;
//        int z2 = write.getStepZ() != 0 ? write.getStepZ() > 0 ? z + 5 : z + 1 : z + 1;
//        AABB box = new AABB(x1, pos.getY(), z1, x2, pos.getY() + 1, z2);
//        List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box);
//        for (LivingEntity e : li) {
//            e.hurt(DamageSource.ON_FIRE, this.isAfterburning() ? 4 : 1);
//        }
//    }
//
//    public int getMaxExhaustTemperature() {
//        return this.isAfterburning() ? 1750 : 1200;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return this.isAfterburning() ? 2000 : 1500;
//    }
//
//    /**
//     * Like BC obsidian pipe - suck in entities in a "funnel" in front of the engine, and deal damage (50 hearts).
//     * Items (including players' inventories and mob drops) will be spat out the back.
//     * Large mobs (Player, creeper, spider, ghast, etc) will cause foreign object damage, necessitating repair.
//     */
//    private void ingest(Level world, BlockPos pos) {
//        if (FOD >= 8)
//            return;
//        Direction dir = this.getWriteDirection().getOpposite();
//        double px = pos.getX() + 0.5 + dir.getStepX() * 0.49;
//        double pz = pos.getZ() + 0.5 + dir.getStepZ() * 0.49;
//        for (int step = 0; step < 8; step++) {
//            AABB zone = this.getSuctionZone(world, pos, step);
//            List<Entity> inzone = world.getEntitiesOfClass(Entity.class, zone);
//            for (Entity caught : inzone) {
//                if (this.canSuckTowards(world, pos, caught, px, pz)) {
//                    float mult = this.getSuctionMultiplier(caught);
//                    if (mult > 0) {
//                        caught.motionX += (pos.getX() + 0.5D - caught.getX()) / 20 * mult;
//                        caught.motionY += (pos.getY() + 0.5D - caught.getY()) / 20 * mult;
//                        caught.motionZ += (pos.getZ() + 0.5D - caught.getZ()) / 20 * mult;
//                        if (!world.isClientSide)
//                            caught.velocityChanged = true;
//                    }
//                    if (ReikaMathLibrary.py3d(caught.getY() - px, caught.getY() - (pos.getY() + 0.5), caught.getZ() - pz) < 1.2) {
//                        this.ingest(world, pos, caught, mult <= 0);
//                    }
//                }
//            }
//        }
//    }
//
//    private boolean canSuckTowards(Level world, BlockPos pos, Entity e, double px, double pz) {
//        int n = 2;
//        for (int i = 0; i <= n; i++) {
//            tracer.setOrigins(px, pos.getX() + 0.5, pz, e.getY(), e.getY() + e.getBbHeight() * i / n, e.getZ());
//            //ReikaJavaLibrary.pConsole(e+":"+tracer.isClearLineOfSight(world)+" of "+e.height, e instanceof EntityChicken);
//            if (tracer.isClearLineOfSight(world))
//                return true;
//        }
//        return false;
//    }
//
//    private float getSuctionMultiplier(Entity e) {
//        if (e instanceof Player) {
//            Player epl = (Player) e;
//            if (epl.isCreative())
//                return 0;
//            ItemStack is = epl.getItemBySlot(EquipmentSlot.FEET);
//            if (is != null) {
//                if (is.getItem() == RotaryItems.BEDROCK_ALLOY_BOOTS.get())
//                    return 0.1F;
//                if (is.getItem() == RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get())
//                    return 0.1F;
//            }
//        }
//        if (e instanceof EntityTurretShot)
//            return 0;
//        /*if (ModList.VOIDMONSTER.isLoaded() && e instanceof EntityVoidMonster) {
//            if (e.getDistanceSq(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5) < 16) {
//                isJetFailing = true;
//                temperature += 2;
//            }
//            return 0;
//        }*/
//        return 1;
//    }
//
//    private void ingest(Level world, BlockPos pos, Entity e, boolean immune) { // Kill the adjacent entities, except items, which are teleported
//        if (e instanceof ItemEntity) {
//            if (!e.isAlive()) {
//                ItemStack is = ((ItemEntity) e).getEntityItem();
//                e.kill();
//                int trycount = 0;
//                while (trycount < 1 && !ReikaWorldHelper.nonSolidBlocks(world, dumpx, y, dumpz)) {
//                    if (dumpvx == 1)
//                        dumpx++;
//                    if (dumpvx == -1)
//                        dumpx--;
//                    if (dumpvz == 1)
//                        dumpz++;
//                    if (dumpvz == -1)
//                        dumpz--;
//                    trycount++;
//                }
//                ItemEntity item = new ItemEntity(world, dumpx + 0.5D, y + 0.375D, dumpz + 0.5D, is);
//                if (!world.isClientSide)
//                    world.addFreshEntity(item);
//                item.motionX = dumpvx * 1.5D;
//                item.motionY = 0.15;
//                item.motionZ = dumpvz * 1.5D;
//                if (!world.isClientSide)
//                    e.velocityChanged = true;
//                if (this.itemDestroysEngine(is)) {
//                    e.kill();
//                    FOD = 2;
//                    this.triggerJetFailing(world, pos);
//                }
//            }
//        } else if (e instanceof ExperienceOrb) {
//            if (!e.isAlive()) {
//                int xp = ((ExperienceOrb) e).getXpValue();
//                e.kill();
//                int trycount = 0;
//                while (trycount < 1 && !ReikaWorldHelper.nonSolidBlocks(world, dumpx, y, dumpz)) {
//                    if (dumpvx == 1)
//                        dumpx++;
//                    if (dumpvx == -1)
//                        dumpx--;
//                    if (dumpvz == 1)
//                        dumpz++;
//                    if (dumpvz == -1)
//                        dumpz--;
//                    trycount++;
//                }
//                ExperienceOrb item = new ExperienceOrb(world, dumpx + 0.5D, y + 0.375D, dumpz + 0.5D, xp);
//                if (!world.isClientSide)
//                    world.addFreshEntity(item);
//                item.motionX = dumpvx * 1.5D;
//                item.motionY = 0.15;
//                item.motionZ = dumpvz * 1.5D;
//                if (!world.isClientSide)
//                    e.velocityChanged = true;
//            }
//        } else if (e instanceof LivingEntity && !(e instanceof Player && immune)) {
//            e.setSecondsOnFire(2);
//            if (!world.isClientSide && ((LivingEntity) e).getHealth() > 0 && this.canDamageEngine(e))
//                this.damageEngine();
//            if (FOD > 8)
//                FOD = 8;
//            if (e instanceof Chicken && !e.isAlive() && ((Chicken) e).getHealth() > 0) {
//                chickenCount++;
//                if (chickenCount >= 50) {
//                    RotaryAdvancements.JETCHICKEN.triggerAchievement(this.getPlacer());
//                }
//            }
//            if (!e.isAlive() && !(e instanceof LivingEntity && ((LivingEntity) e).getHealth() < 0))
//                SoundRegistry.INGESTION.playSoundAtBlock(world, pos, 1, 1.4F);
//            RotaryCraft.jetingest.lastMachine = this;
//            e.hurt(RotaryCraft.jetingest, 10000);
//            if (e instanceof Player && e == this.getPlacer()) {
//                RotaryAdvancements.SUCKEDINTOJET.triggerAchievement((Player) e);
//            }
//        }
//    }
//
//    private void triggerJetFailing(Level world, BlockPos pos) {
//        RotaryCraft.LOGGER.warn(this + " just entered failure mode!");
//        isJetFailing = true;
//
//        int x = pos.getX();
//        int y = pos.getY();
//        int z = pos.getZ();
//
//        RotaryAdvancements.JETFAIL.triggerAchievement(this.getPlacer());
//        NeoForge.EVENT_BUS.post(new JetEngineEnterFailureEvent(this));
//        ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.explode", 2, 0.75F);
//        SoundRegistry.INGESTION.playSoundAtBlock(this, 0.5F, 1);
//
//        AABB box = ReikaAABBHelper.getBlockAABB(pos).move(write.getStepX() * 8, y, write.getStepZ() * 8).expandTowards(3, 3, 3); //todo move was, addCoord check if this is right
//        List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box);
//        HashSet<Integer> idSet = new HashSet<>();
//        for (LivingEntity e : li) {
//            e.hurt(DamageSource.GENERIC, 8);
//            idSet.add(e.getId());
//        }
//        box = ReikaAABBHelper.getBlockAABB(pos).expandTowards(4, 4, 4);
//        li = world.getEntitiesOfClass(LivingEntity.class, box);
//        for (LivingEntity e : li) {
//            if (!idSet.contains(e.getId()))
//                e.hurt(DamageSource.GENERIC, 4);
//        }
//
//        if (world.isClientSide) {
//            for (int i = 0; i < 24; i++) {
//                ItemEntity ei = new ItemEntity(world, x + 0.5 + write.getStepX(), y + 0.5, z + 0.5 + write.getStepZ(), RotaryItems.HSLA_STEEL_SCRAP);
//                double v = ReikaRandomHelper.getRandomBetween(0.8D, 2D);
//                ei.motionX = write.getStepX() * v;
//                ei.motionZ = write.getStepZ() * v;
//                ei.motionX = ReikaRandomHelper.getRandomPlusMinus(ei.motionX, 0.25);
//                ei.motionZ = ReikaRandomHelper.getRandomPlusMinus(ei.motionZ, 0.25);
//                ei.motionY = ReikaRandomHelper.getRandomPlusMinus(0, 0.25);
//                ei.lifespan = ReikaRandomHelper.getRandomBetween(10, 30);
//                world.addFreshEntity(ei);
//            }
//        }
//            int r = 8;
//            for (int i = -r; i <= r; i++) {
//                for (int j = -r; j <= r; j++) {
//                    for (int k = -r; k <= r; k++) {
//                        int dx = x + i;
//                        int dy = y + j;
//                        int dz = z + k;
//                        ReikaRenderHelper.spawnDropParticles(world, pos, world.getBlockState(new BlockPos(dx, dy, dz)));//, world.getBlockMetadata(dx, dy, dz));
//                    }
//                }
//            }
//        }
//
//
//    private boolean itemDestroysEngine(ItemStack is) {
//        return is.getItem() == RotaryItems.SCREWDRIVER.get() || InterfaceCache.IWRENCH.instanceOf(is.getItem());
//    }
//
//    private void damageEngine() {
//        FOD++;
//        if (DifficultyEffects.JETINGESTFAIL.testChance()) {
//            isJetFailing = true;
//            temperature = Math.max(temperature, 800);
//        }
//        //SoundRegistry.JETDAMAGE.playSoundAtBlock(level, xCoord, yCoord, zCoord);
//    }
//
//    private boolean canDamageEngine(Entity caught) {
//        if (caught.isSpectator())
//            return false;
//        //if (caught instanceof EtherealEntity)
//        //    return false;
//        if (caught instanceof Chicken)
//            return false;
//        if (caught instanceof Bat)
//            return false;
//        if (caught instanceof Silverfish)
//            return false;
//        if (caught instanceof ItemEntity)
//            return false;
//        if (caught instanceof ExperienceOrb)
//            return false;
//        //if (ModList.THAUMCRAFT.isLoaded() && caught instanceof Wisp)
//        //    return false;
//        String name = caught.getName().getString().toLowerCase(Locale.ENGLISH);
//        if (name.contains("bird"))
//            return false;
//        if (name.contains("firefly"))
//            return false;
//        if (name.contains("butterfly"))
//            return false;
//        return caught instanceof LivingEntity;
//    }
//
//    private AABB getSuctionZone(Level world, BlockPos pos, int step) {
//        int x = pos.getX();
//        int y = pos.getY();
//        int z = pos.getZ();
//
//        int minx = 0;
//        int miny = 0;
//        int minz = 0;
//        int maxx = 0;
//        int maxy = 0;
//        int maxz = 0;
//
//        switch (meta) {
//            case 0:
//                minx = x + 1 + step;
//                maxx = x + 1 + step + 1;
//                miny = y - step;
//                maxy = y + step + 1;
//                minz = z - step;
//                maxz = z + step + 1;
//                dumpx = x - 1;
//                dumpz = z;
//                dumpvx = -1;
//                dumpvz = 0;
//                break;
//            case 1:
//                minx = x - 1 - step;
//                maxx = x - 1 - step + 1;
//                miny = y - step;
//                maxy = y + step + 1;
//                minz = z - step;
//                maxz = z + step + 1;
//                dumpx = x + 1;
//                dumpz = z;
//                dumpvx = 1;
//                dumpvz = 0;
//                break;
//            case 2:
//                minz = z + 1 + step;
//                maxz = z + 1 + step + 1;
//                miny = y - step;
//                maxy = y + step + 1;
//                minx = x - step;
//                maxx = x + step + 1;
//                dumpx = x;
//                dumpz = z - 1;
//                dumpvx = 0;
//                dumpvz = -1;
//                break;
//            case 3:
//                minz = z - 1 - step;
//                maxz = z - 1 - step + 1;
//                miny = y - step;
//                maxy = y + step + 1;
//                minx = x - step;
//                maxx = x + step + 1;
//                dumpx = x;
//                dumpz = z + 1;
//                dumpvx = 0;
//                dumpvz = 1;
//                break;
//        }
//
//        return new AABB(minx, miny, minz, maxx, maxy, maxz).expandTowards(0.25, 0.25, 0.25);
//    }
//
//    public void repairJet() {
//        FOD = 0;
//        isJetFailing = false;
//        temperature = Math.max(ReikaWorldHelper.getAmbientTemperatureAt(level, getPos()), temperature / 2);
//    }
//
//    public void repairJetPartial() {
//        if (FOD > 0)
//            FOD--;
//    }
//
//    private void jetEngineDetonation(Level world, BlockPos pos) {
//        AABB zone = this.getFlameZone(world, pos);
//        List<LivingEntity> in = world.getEntitiesOfClass(LivingEntity.class, zone);
//        for (LivingEntity e : in) {
//            e.setSecondsOnFire(2);
//        }
//        double vx = (pos.getX() - backx) / 2D;
//        double vz = (pos.getZ() - backz) / 2D;
//        for (int i = 0; i < 16; i++) {
//            String part;
//            if (i % 2 == 0)
//                part = "flame";
//            else
//                part = "smoke";
//            world.addParticle(part, x + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), y + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), z + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), vx - 0.1 + 0.2 * DragonAPI.rand.nextDouble(), -0.1 + 0.2 * DragonAPI.rand.nextDouble(), vz - 0.1 + 0.2 * DragonAPI.rand.nextDouble());
//        }
//        int dx = x - backx;
//        int dz = z - backz;
//        for (int i = 0; i < 16; i++) {
//            ReikaWorldHelper.temperatureEnvironment(world, pos.getX() + dx * i, pos.getY(), pos.getZ() dz * i, 800);
//        }
//        world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "mob.blaze.hit", 1F, 1F);
//        if (FMLLoader.getDist() == Dist.DEDICATED_SERVER) {
//            if (fuel.getLevel() < FUELCAP / 12 && DragonAPI.rand.nextInt(10) == 0) {
//                ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.ENGINEBACKFIRE.ordinal(), this, 64);
//                this.backFire(world, pos);
//            }
//            if (fuel.getLevel() < FUELCAP / 4 && DragonAPI.rand.nextInt(20) == 0) {
//                ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.ENGINEBACKFIRE.ordinal(), this, 64);
//                this.backFire(world, pos);
//            } else if (DragonAPI.rand.nextInt(40) == 0) {
//                ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetchannel, PacketRegistry.ENGINEBACKFIRE.ordinal(), this, 64);
//                this.backFire(world, pos);
//            }
//        }
//        if (DragonAPI.rand.nextInt(2) == 0)
//            temperature++;
//
//        if (temperature >= 800) {
//            RotaryCraft.LOGGER.warn("WARNING: " + this + " is near explosion!");
//        }
//
//        if (temperature > 1000) {
//            this.fail(world, pos);
//        }
//    }
//
//    private void fail(Level world, BlockPos pos) {
//        NeoForge.EVENT_BUS.post(new JetEngineExplosionEvent(this));
//        int r = 6;
//        for (int i = -r; i <= r; i++) {
//            for (int j = -r; j <= r; j++) {
//                for (int k = -r; k <= r; k++) {
//                    if (RotaryConfig.COMMON.BLOCKDAMAGE.getState())
//                        ReikaWorldHelper.temperatureEnvironment(world, x + i, y + j, z + k, 1000);
//                    world.addParticle("lava", pos.getX() + i, pos.getY() + j, pos.getZ() + k, 0, 0, 0);
//                    world.addParticle("lava", pos.getX() + i, pos.getY() + j, pos.getZ() + k, DragonAPI.rand.nextDouble() - 0.5, DragonAPI.rand.nextDouble() - 0.5, DragonAPI.rand.nextDouble() - 0.5);
//                }
//            }
//        }
//        if (!world.isClientSide) {
//            world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12F, true, true);
//            for (int m = 0; m < 6; m++)
//                world.explode(null, x - 4 + DragonAPI.rand.nextInt(11), y - 4 + DragonAPI.rand.nextInt(11), z - 4 + DragonAPI.rand.nextInt(11), 4F + DragonAPI.rand.nextFloat() * 2, true, true);
//        }
//    }
//
//    public void backFire(Level world, BlockPos pos) {
//        double vx = (pos.getX() - backx) / 2D;
//        double vz = (pos.getZ() - backz) / 2D;
//        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 2 * DragonAPI.rand.nextFloat(), Explosion.BlockInteraction.NONE);
//        ReikaSoundHelper.playSoundAtBlock(world, pos.getX(), pos.getY(), pos.getZ(), "DragonAPI.rand.explode", 1, 0.5F);
//        for (int i = 0; i < 32; i++) {
//            String part;
//            if (i % 2 == 0)
//                part = "flame";
//            else
//                part = "smoke";
//            world.addParticle(part, pos.getX() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), pos.getY() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), pos.getZ() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), -vx - 0.1 + 0.2 * DragonAPI.rand.nextDouble(), -0.1 + 0.2 * DragonAPI.rand.nextDouble(), -vz - 0.1 + 0.2 * DragonAPI.rand.nextDouble());
//        }
//    }
//
//    private AABB getFlameZone(Level world, BlockPos pos) {
//        return switch (meta) {
//            case 0 -> //-x
//                    new AABB(pos.getX() - 6, pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
//            case 1 -> //+x
//                    new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 7, pos.getY() + 1, pos.getZ() + 1);
//            case 2 -> //-z
//                    new AABB(pos.getX(), pos.getY(), pos.getZ() - 6, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
//            case 3 -> //+z
//                    new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 7);
//            default -> null;
//        };
//    }
//
//    private void launchEntities(Level world, BlockPos pos) {
//        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expandTowards(8, 8, 8);
//        List<Entity> inbox = world.getEntitiesOfClass(Entity.class, box);
//        for (Entity e : inbox) {
//            double dx = e.getX() - pos.getX() - 0.5;
//            double dy = e.getY() - pos.getY() - 0.5;
//            double dz = e.getZ() - pos.getX() - 0.5;
//            double dd = ReikaMathLibrary.py3d(dx, dy, dz);
//            e.motionX += 2 * dx / dd;
//            e.motionY += 2 * dy / dd;
//            e.motionZ += 2 * dz / dd;
//            if (!world.isClientSide)
//                e.velocityChanged = true;
//            if (e instanceof Painting || e instanceof ItemFrame)
//                e.hurt(DamageSource.GENERIC, 10);
//        }
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putBoolean("choke", isChoking);
//        NBT.putBoolean("jetfail", isJetFailing);
//        NBT.putBoolean("burn", canAfterBurn);
//        NBT.putBoolean("burning", burnerActive);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        isChoking = NBT.getBoolean("choke");
//        isJetFailing = NBT.getBoolean("jetfail");
//        canAfterBurn = NBT.getBoolean("burn");
//        burnerActive = NBT.getBoolean("burning");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        NBT.putInt("FOD", FOD);
//        NBT.putInt("chickens", chickenCount);
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        FOD = NBT.getInt("FOD");
//        chickenCount = NBT.getInt("chickens");
//    }
//
//    @Override
//    protected void playSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
//        soundtick++;
//        afterburnTick++;
//        if (FOD > 0 && DragonAPI.rand.nextInt(2 * (9 - FOD)) == 0) {
//            world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "mob.blaze.hit", 1F + DragonAPI.rand.nextFloat(), 1F);
//            world.addParticle("crit", pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
//        }
//        if (this.isMuffled(world, pos)) {
//            volume *= 0.3125F;
//        }
//
//        if (this.isAfterburning() && afterburnTick >= 50) {
//            afterburnTick = 0;
//            float vol = 0.9F;
//            if (omega < type.getSpeed()) {
//                vol *= Math.pow(0.75, type.getSpeed() / omega);
//            }
//            SoundRegistry.AFTERBURN.playSoundAtBlock(world, pos, vol, 1);
//            SoundRegistry.AFTERBURN.playSoundAtBlock(world, pos, vol, 1);
//        }
//
//        if (soundtick < this.getSoundLength(1F / pitchMultiplier) && soundtick < 2000)
//            return;
//        soundtick = 0;
//
//        float pitch = 1F;
//        pitch = 1F / (0.125F * FOD + 1);
//        if (jetstarttimer.getTick() >= jetstarttimer.getCap())
//            SoundRegistry.JET.playSoundAtBlock(world, pos, volume, pitch * pitchMultiplier);
//        else
//            soundtick = 2000;
//    }
//
//    public boolean isAfterburning() {
//        return canAfterBurn && burnerActive;
//    }
//
//    @Override
//    public boolean hasTemperature() {
//        return true;
//    }
//
//    @Override
//    public int getMaxSpeed(Level world, BlockPos pos) {
//        return (int) (EngineType.JET.getSpeed() * this.getChokedFraction(world, pos, meta));
//    }
//
//    @Override
//    protected boolean canStart() {
//        return true; //JumpStart code goes here
//    }
//
//    @Override
//    protected int getGenTorque(Level world, BlockPos pos) {
//        int amt = EngineType.JET.getTorque();
//        if (this.isAfterburning())
//            amt *= 2;
//        return amt / ReikaMathLibrary.intpow2(2, FOD);
//    }
//
//    @Override
//    protected void affectSurroundings(Level world, BlockPos pos) {
//        this.checkJetFailure(world, pos);
//        this.ingest(world, pos);
//        this.fluidIngest(world, pos);
//        this.heatJet(world, pos);
//        //ReikaJavaLibrary.pConsole(lastpower+":"+power, Dist.DEDICATED_SERVER);
//        if (lastpower == 0) {
//            SoundRegistry.JETSTART.playSoundAtBlock(world, pos);
//        }
//        if (world.isClientSide)
//            this.spawnSmokeParticles(world, pos);
//        jetstarttimer.update();
//        this.doAfterburning(world, pos);
//    }
//
//    private void fluidIngest(Level world, BlockPos pos) {
//        int dx = x + write.getOpposite().getStepX();
//        int dz = z + write.getOpposite().getStepZ();
//        Block b = world.getBlockState(new BlockPos(dx, pos.getY(), dz)).getBlock();
//        Fluid f = ReikaFluidHelper.lookupFluidForBlock(b.defaultBlockState());
//        if (f != null) {
//
//            if (level.isClientSide) {
//                this.fluidIngestParticles(world, pos, f);
//            } else {
//                int temp = f.getAttributes().getTemperature();
//                if (f.getName().toLowerCase(Locale.ENGLISH).contains("fuel")) {
//                    if (!isJetFailing && DragonAPI.rand.nextInt(200) == 0) {
//                        temperature = 900;
//                        isJetFailing = true;
//                        this.jetEngineDetonation(world, pos);
//                    }
//                } else if (temp >= 500) {
//                    if (temp >= 2000 || DragonAPI.rand.nextInt(1 + (2000 - temp) / 500) == 0) {
//                        if (FOD < 8 && world.getDayTime() % 20 == 0 && DragonAPI.rand.nextInt(1 + 2 * FOD) == 0) {
//                            this.damageEngine();
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//
//    private void fluidIngestParticles(Level world, BlockPos pos, Fluid f) {
//        int n = 1 + DragonAPI.rand.nextInt(8);
//        for (int i = 0; i < n; i++) {
//            double vx = (pos.getX() - backx) / 4D;
//            double vy = ReikaRandomHelper.getRandomPlusMinus(0, 0.0625);
//            double vz = (pos.getZ() - backz) / 4D;
//
//            vx = ReikaRandomHelper.getRandomPlusMinus(vx, 0.0625);
//            vz = ReikaRandomHelper.getRandomPlusMinus(vz, 0.0625);
//            EntityFX fx = new EntityLiquidParticleFX(world, x + 0.5 + write.getStepX() * 0.25, y + 0.5, z + 0.5 + write.getStepZ() * 0.25, vx, vy, vz, f);
//            fx.noClip = true;
//            Minecraft.getInstance().effectRenderer.addEffect(fx);
//        }
//    }
//
//    private void doAfterburning(Level world, BlockPos pos) {
//        if (this.isAfterburning()) {
//            this.afterBurnParticles(world, pos);
//            if (this.tickcount % 200 == 0) {
//                temperature += 1;
//                if (temperature > this.getMaxTemperature()) {
//                    temperature = this.getMaxTemperature();
//                    this.fail(world, pos);
//                } else if (temperature >= 600) {
//                    ReikaSoundHelper.playSoundAtBlock(world, pos.getX(), pos.getY(), pos.getZ(), "DragonAPI.rand.fizz");
//                    ReikaParticleHelper.SMOKE.spawnAroundBlock(world, pos, 8);
//                }
//            }
//        }
//    }
//
//    private void afterBurnParticles(Level world, BlockPos pos) {
//        double dx = pos.getX() - backx;
//        double dz = pos.getZ() - backz;
//        dx /= 2;
//        dz /= 2;
//        double vx = (pos.getX() - backx) * 6D;
//        double vz = (pos.getZ() - backz) * 6D;
//        for (int i = 0; i < 16; i++) {
//            int r = 255;
//            int g = 0;
//            int b = 0;
//            double px = dx + pos.getX() + 0.25 + 0.5 * DragonAPI.rand.nextDouble() + vx * DragonAPI.rand.nextDouble();
//            double pz = dz + pos.getZ() + 0.25 + 0.5 * DragonAPI.rand.nextDouble() + vz * DragonAPI.rand.nextDouble();
//            double dd = Math.abs(px - pos.getX()) + Math.abs(pz - pos.getZ());
//            if (dd < 1.5 + DragonAPI.rand.nextDouble()) {
//                r = 0;
//                g = 127;
//                b = 255;
//            } else if (dd < 2.5 + DragonAPI.rand.nextDouble()) {
//                r = 255;
//                g = 255;
//                b = 255;
//            } else if (dd < 3 + DragonAPI.rand.nextDouble() * 2) {
//                g = 255;
//            } else if (dd < 5 + DragonAPI.rand.nextDouble() * 3 && DragonAPI.rand.nextBoolean()) {
//                g = 10;
//            }
//            ReikaParticleHelper.spawnColoredParticleAt(world, px, pos.getY() + 0.75 * DragonAPI.rand.nextDouble(), pz, r, g, b);
//        }
//    }
//
//
//    private void spawnSmokeParticles(Level world, BlockPos pos) {
//        double dx = pos.getX() - backx;
//        double dz = pos.getZ() - backz;
//        dx /= 2;
//        dz /= 2;
//        double vx = -(pos.getX() - backx) / 2D;
//        double vz = -(pos.getZ() - backz) / 2D;
//        ReikaParticleHelper.SMOKE.spawnAt(world, dx + pos.getX() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), pos.getY() + 0.5 * DragonAPI.rand.nextDouble(), dz + pos.getZ() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(), -vx - 0.1 + 0.2 * DragonAPI.rand.nextDouble(), -0.1 + 0.2 * DragonAPI.rand.nextDouble(), -vz - 0.1 + 0.2 * DragonAPI.rand.nextDouble());
//
//        int n = 1 + DragonAPI.rand.nextInt(8);
//        double w = n / 2D;
//        dx = write.getStepX() == 0 ? ReikaRandomHelper.getRandomPlusMinus(pos.getX() + 0.5, w) : pos.getX() + 0.5 - n * write.getStepX();
//        double dy = ReikaRandomHelper.getRandomPlusMinus(pos.getY() + 0.5, w);
//        dz = write.getStepZ() == 0 ? ReikaRandomHelper.getRandomPlusMinus(pos.getZ() + 0.5, w) : pos.getZ() + 0.5 - n * write.getStepZ();
//
//        double v = -0.0625;
//
//        vx = v * (dx - x - 0.5);
//        double vy = v * (dy - y - 0.5);
//        vz = v * (dz - z - 0.5);
//
//        int bx = Mth.floor(dx);
//        int by = Mth.floor(dy);
//        int bz = Mth.floor(dz);
//        Block b = world.getBlockState(new BlockPos(bx, by, bz)).getBlock();
//        int bmeta = world.getBlockMetadata(bx, by, bz);
//        if (b.isAir(world, bx, by, bz)) {
//            if (DragonAPI.rand.nextInt(3) == 0)
//                ReikaParticleHelper.CLOUD.spawnAt(world, dx, dy, dz, vx, vy, vz);
//        } else {
//            EntityBlockTexFX fx = new EntityBlockTexFX(world, dx, dy + 1, dz, vx, vy - 0.03125, vz, b, bmeta).setGravity(0);
//            fx.applyColourMultiplier(bx, by, bz);
//            Minecraft.getInstance().effectRenderer.addEffect(fx);
//        }
//
//    }
//
//    @Override
//    protected void resetPower() {
//        super.resetPower();
//
//        jetstarttimer.reset();
//    }
//
//    @Override
//    protected int getSoundLength(float factor) {
//        return super.getSoundLength(factor) + (int) (Math.min(FOD, 7) * 11 * factor);
//    }
//
//    @Override
//    public void setDataFromPlacer(ItemStack is) {
//        super.setDataFromPlacer(is);
//
//        if (is.getTag() != null)
//            FOD = is.getTag().getInt("damage");
//    }
//
//    @Override
//    public boolean isBroken() {
//        return FOD >= 8;
//    }
//
//    @Override
//    public CompoundTag getTagsToWriteToStack() {
//        if (canAfterBurn) {
//            CompoundTag NBT = new CompoundTag();
//            NBT.putBoolean("burn", canAfterBurn);
//            return NBT;
//        }
//        return null;
//    }
//
//    @Override
//    public void setDataFromItemStackTag(CompoundTag NBT) {
//        canAfterBurn = NBT != null && NBT.getBoolean("burn");
//    }
//
//    @Override
//    public ArrayList<CompoundTag> getCreativeModeVariants() {
//        return new ArrayList<>();
//    }
//
//    @Override
//    public ArrayList<String> getDisplayTags(CompoundTag NBT) {
//        ArrayList<String> li = new ArrayList<>();
//        if (NBT != null && NBT.getBoolean("burn")) {
//            li.add("With Afterburner");
//        }
//        return li;
//    }
//
//    @Override
//    public void upgrade(ItemStack is) {
//        canAfterBurn = true;
//    }
//
//    @Override
//    public boolean canUpgradeWith(ItemStack item) {
//        return !canAfterBurn && RotaryItems.UPGRADE.matchItem(item) && item.getItemDamage() == Upgrades.AFTERBURNER.ordinal();
//    }
//
//    public boolean canAfterBurn() {
//        return canAfterBurn;
//    }
//
//    public boolean burnerActive() {
//        return burnerActive;
//    }
//
//    public void setBurnerActive(boolean burn) {
//        burnerActive = burn;
//		/*
//		if (!burn && !isJetFailing) {
//			temperature = ReikaWorldHelper.getAmbientTemperatureAt(level, xCoord, yCoord, zCoord);
//		}*/
//    }
//
//    @Override
//    public void breakBlock() {
//        super.breakBlock();
//        if (canAfterBurn) {
//            ReikaItemHelper.dropItem(level, getBlockPos().getX() + 0.5, getBlockPos().getY() + 0.5, getBlockPos().getZ() + 0.5, RotaryItems.UPGRADE.getStackOfMetadata(Upgrades.AFTERBURNER.ordinal()));
//        }
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return true;
//    }
//
//    @Override
//    public int getAmbientTemperature() {
//        return 0;
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return false;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int pIndex, int pCount) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int pIndex, ItemStack pStack) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    @Override
//    public int getTanks() {
//        return 0;
//    }
//
//    
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return new FluidStack(RotaryFluids.JET_FUEL.get(), 0);
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 24000;
//    }
//
//    @Override
//    public boolean isFluidValid(int tank,  FluidStack stack) {
//        return false;
//    }
//
//    @Override
//    public int fill(FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    
//    @Override
//    public FluidStack drain(FluidStack resource, FluidAction action) {
//        return null;
//    }
//
//    
//    @Override
//    public FluidStack drain(int maxDrain, FluidAction action) {
//        return null;
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
