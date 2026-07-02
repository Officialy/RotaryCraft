/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.engine;

import net.minecraft.client.Minecraft;
import net.minecraft.client.multiplayer.ClientLevel;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.BlockParticleOption;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.ExperienceOrb;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.chicken.Chicken;
import net.minecraft.world.entity.decoration.ItemFrame;
import net.minecraft.world.entity.decoration.painting.Painting;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Silverfish;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.IronBarsBlock;
import net.minecraft.world.level.block.WallBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.shapes.VoxelShape;
import net.neoforged.api.distmarker.Dist;
import net.neoforged.fml.loading.FMLEnvironment;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.RayTracer;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.libraries.ReikaAABBHelper;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.dragonapi.libraries.rendering.ReikaRenderHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.ThermalMachine;
import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.api.event.JetEngineEnterFailureEvent;
import reika.rotarycraft.api.event.JetEngineExplosionEvent;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.gui.container.machine.ContainerJet;
import reika.rotarycraft.items.tools.ItemEngineUpgrade;
import reika.rotarycraft.items.tools.ItemEngineUpgrade.UpgradeType;
import reika.rotarycraft.registry.DifficultyEffects;
import reika.rotarycraft.registry.EngineType;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.PacketRegistry;
import reika.rotarycraft.registry.RotaryAdvancements;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Locale;

/**
 * 26.1 port of the legacy 1.7.10 jet engine. Faithful translation of every behaviour from the
 * 1.7.10 source — ingest mechanics, foreign-object damage (FOD), afterburner, choke detection,
 * heat-jet effect, jet failure / detonation, chicken counter, fluid ingest. APIs translated:
 * <ul>
 *   <li>{@code entity.motionX/Y/Z = ...} → {@link Entity#setDeltaMovement(double, double, double)}</li>
 *   <li>{@code entity.motionX += ...} → {@link Entity#push(double, double, double)}</li>
 *   <li>{@code entity.velocityChanged = true} → {@code entity.hurtMarked = true}</li>
 *   <li>{@code entity.kill()} → {@link Entity#discard()}</li>
 *   <li>{@code ItemEntity.getEntityItem()} → {@link ItemEntity#getItem()}</li>
 *   <li>{@code world.getBlock(x,y,z)} → {@code level.getBlockState(new BlockPos(x,y,z)).getBlock()}</li>
 *   <li>{@code world.addParticle("name", ...)} → {@link Level#addParticle} with {@link ParticleTypes}</li>
 *   <li>{@code world.playLocalSound(x,y,z,"sound",v,p)} → {@link Level#playLocalSound} with {@link SoundEvent}</li>
 *   <li>{@code Block.getBlockBoundsMin/MaxXYZ()} → {@code BlockState.getShape(level,pos).bounds()}</li>
 *   <li>{@code DamageSource.GENERIC / ON_FIRE} → {@code level.damageSources().generic() / onFire()}</li>
 *   <li>{@code world.explode(entity, x, y, z, str, true, true)} → {@code level.explode(entity, x, y, z, str, Level.ExplosionInteraction.MOB)}</li>
 *   <li>Particle entities ({@code EntityFX}, {@code EntityBlockTexFX}, {@code EntityLiquidParticleFX})
 *       → 26.1 particle system with {@link BlockParticleOption} / {@link ParticleTypes#BLOCK}.</li>
 * </ul>
 */
public class BlockEntityJetEngine extends BlockEntityEngine implements NBTMachine, UpgradeableMachine {

    public static final int BASE_CONSUMPTION = 10;
    public static final int AFTERBURNER_CONSUMPTION = 25;
    private final RayTracer tracer = new RayTracer(0, 0, 0, 0, 0, 0);

    private final StepTimer jetstarttimer = new StepTimer(479);
    /** Used in jet engines — Foreign Object Damage counter, 0..8 (8 = engine destroyed). */
    public int FOD = 0;
    private boolean isJetFailing = false;
    private int dumpx;
    private int dumpz;
    private int dumpvx;
    private int dumpvz;
    private int afterburnTick = 2000;
    private int chickenCount = 0;
    private boolean isChoking = false;
    private boolean canAfterBurn;
    private boolean burnerActive;

    public BlockEntityJetEngine(BlockPos pos, BlockState state) {
        // Jet engine wants air, no lubricant, no water, yes fuel (jet fuel via the fuel tank).
        super(RotaryBlockEntities.JET_ENGINE.get(), pos, state, true, false, false, true);
        type = EngineType.JET;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.JET_ENGINE;
    }

    @Override
    public int getFuelLevel() {
        return fuel.getFluidLevel();
    }

    @Override
    protected void consumeFuel() {
        fuel.removeLiquid(this.getConsumedFuel());
    }

    @Override
    protected int getConsumedFuel() {
        return this.isAfterburning() ? AFTERBURNER_CONSUMPTION : BASE_CONSUMPTION;
    }

    @Override
    protected void internalizeFuel() {
    }

    @Override
    protected boolean canBeThrottled() {
        return !this.isAfterburning();
    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        if (FOD >= 8) {
            jetstarttimer.reset();
            return false;
        }
        if (fuel.getFluidLevel() <= 0) {
            jetstarttimer.reset();
            return false;
        }
        if (power > 0)
            RotaryAdvancements.JETENGINE.triggerAchievement(this.getPlacer());
        return true;
    }

    private void checkJetFailure(Level world, BlockPos pos) {
        if (isJetFailing)
            this.jetEngineDetonation(world, pos);
        else if (FOD > 0 && DragonAPI.rand.nextInt(DifficultyEffects.JETFAILURE.getInt() * (9 - FOD)) == 0) {
            this.triggerJetFailing(world, pos);
        }
    }

    /**
     * % of the intake area NOT blocked by the block directly in front of the engine. Range [0,1]:
     * 1 = clear, 0 = fully choked. Full-block check uses {@link BlockState#getShape(net.minecraft.world.level.BlockGetter, BlockPos)}
     * since legacy {@code getBlockBoundsMin/MaxXYZ} is gone.
     */
    public float getChokedFraction(Level world, BlockPos blockPos) {
        // Intake is opposite the engine's output (FACING). Derive it straight from FACING rather than
        // the `write` field: write is set by getIOSides, which only maps the 4 horizontal facings, so a
        // null/stale write would default to NORTH and check the wrong block → a spurious full choke
        // (engine reads 0 speed and never spins/consumes fuel).
        Direction intake = this.getBlockState().getValue(BlockRotaryCraftMachine.FACING).getOpposite();
        BlockPos checkPos = blockPos.relative(intake);
        BlockState st = world.getBlockState(checkPos);
        Block b = st.getBlock();
        if (st.isAir())
            return 1;
        VoxelShape collisionShape = st.getCollisionShape(world, checkPos);
        if (collisionShape.isEmpty())
            return 1;
        if (b == Blocks.OAK_FENCE || b == Blocks.NETHER_BRICK_FENCE
                || b == Blocks.SPRUCE_FENCE || b == Blocks.BIRCH_FENCE || b == Blocks.JUNGLE_FENCE
                || b == Blocks.ACACIA_FENCE || b == Blocks.DARK_OAK_FENCE
                || b == Blocks.MANGROVE_FENCE || b == Blocks.CHERRY_FENCE || b == Blocks.CRIMSON_FENCE
                || b == Blocks.WARPED_FENCE || b == Blocks.BAMBOO_FENCE)
            return 0.75F;
        if (b == Blocks.IRON_BARS)
            return 1F;
        if (b instanceof WallBlock)
            return 0.25F;
        if (b == Blocks.GLASS_PANE || st.getBlock() instanceof IronBarsBlock)
            return 0.5F;
        AABB box = collisionShape.bounds();
        // Full block: total occlusion.
        if (box.maxX > 0.875 && box.maxY > 0.875 && box.maxZ > 0.875
                && box.minX < 0.125 && box.minY < 0.125 && box.minZ < 0.125)
            return 0;
        double dx = box.maxX - box.minX;
        double dy = box.maxY - box.minY;
        double dz = box.maxZ - box.minZ;
        if (box.maxX <= 0.125 || box.minX >= 0.875) dx = 0;
        if (box.maxY <= 0.125 || box.minY >= 0.875) dy = 0;
        if (box.maxZ <= 0.125 || box.minZ >= 0.875) dz = 0;
        if (box.maxY >= 0.75) dy += 0.125;
        double frac = 1 - (dx * dy * dz);
        return (float) frac;
    }

    /**
     * Heat the row of blocks/entities directly in front of the exhaust. Engine raises its own
     * temperature toward the max exhaust temp based on its omega/torque ratio; afterburner pushes
     * temperatures up to 1400 in nearby blocks and damages living entities in the path.
     */
    private void heatJet(Level world, BlockPos pos) {
        if (this.isOn() && this.tickcount % 10 == 0) {
            int max = this.getMaxExhaustTemperature() * omega / type.getSpeed();
            if (max > temperature)
                temperature = Math.min(temperature + Math.max(1, (max - temperature) / 16), max);
            else if (!this.isAfterburning()) {
                temperature = Math.max(temperature - Math.max(1, (temperature - max) / 32), max);
            }
        }
        int T = temperature;
        int r = this.isAfterburning() ? 6 : 4;
        if (write == null) return;
        for (int i = 1; i < r; i++) {
            BlockPos dp = pos.offset(write.getStepX() * i, 0, write.getStepZ() * i);
            BlockEntity te = world.getBlockEntity(dp);
            if (te instanceof TemperatureTE tte) {
                if (tte.allowExternalHeating()) {
                    int dT = T - tte.getTemperature();
                    tte.addTemperature(dT);
                }
            } else if (te instanceof ThermalMachine tm) {
                tm.setTemperature(T);
            }
            if (this.isAfterburning()) {
                ReikaWorldHelper.temperatureEnvironment(world, dp, Math.min(1400, T));
            }
        }
        // Damage any living entities in the exhaust column.
        int x1, x2, z1, z2;
        if (write.getStepX() != 0) {
            x1 = write.getStepX() > 0 ? pos.getX() : pos.getX() - 4;
            x2 = write.getStepX() > 0 ? pos.getX() + 5 : pos.getX() + 1;
            z1 = pos.getZ();
            z2 = pos.getZ() + 1;
        } else {
            x1 = pos.getX();
            x2 = pos.getX() + 1;
            z1 = write.getStepZ() > 0 ? pos.getZ() : pos.getZ() - 4;
            z2 = write.getStepZ() > 0 ? pos.getZ() + 5 : pos.getZ() + 1;
        }
        AABB box = new AABB(x1, pos.getY(), z1, x2, pos.getY() + 1, z2);
        List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity e : li) {
            e.hurt(world.damageSources().onFire(), this.isAfterburning() ? 4 : 1);
        }
    }

    public int getMaxExhaustTemperature() {
        return this.isAfterburning() ? 1750 : 1200;
    }

    @Override
    public int getMaxTemperature() {
        return this.isAfterburning() ? 2000 : 1500;
    }

    /**
     * Like a BC obsidian pipe — suck entities into a "funnel" in front of the engine and deal
     * damage. Items (including players' inventories and mob drops) get spat out the back. Large
     * mobs (Player, creeper, spider, ghast, etc) cause foreign object damage which can ratchet
     * the engine toward catastrophic failure.
     */
    private void ingest(Level world, BlockPos pos) {
        if (FOD >= 8 || write == null)
            return;
        Direction dir = write.getOpposite();
        double px = pos.getX() + 0.5 + dir.getStepX() * 0.49;
        double pz = pos.getZ() + 0.5 + dir.getStepZ() * 0.49;
        for (int step = 0; step < 8; step++) {
            AABB zone = this.getSuctionZone(world, pos, step);
            if (zone == null) continue;
            List<Entity> inzone = world.getEntitiesOfClass(Entity.class, zone);
            for (Entity caught : inzone) {
                if (this.canSuckTowards(world, pos, caught, px, pz)) {
                    float mult = this.getSuctionMultiplier(caught);
                    if (mult > 0) {
                        double pullX = (pos.getX() + 0.5D - caught.getX()) / 20 * mult;
                        double pullY = (pos.getY() + 0.5D - caught.getY()) / 20 * mult;
                        double pullZ = (pos.getZ() + 0.5D - caught.getZ()) / 20 * mult;
                        caught.push(pullX, pullY, pullZ);
                        if (!world.isClientSide())
                            caught.hurtMarked = true;
                    }
                    if (ReikaMathLibrary.py3d(caught.getX() - px, caught.getY() - (pos.getY() + 0.5), caught.getZ() - pz) < 1.2) {
                        this.ingestEntity(world, pos, caught, mult <= 0);
                    }
                }
            }
        }
    }

    private boolean canSuckTowards(Level world, BlockPos pos, Entity e, double px, double pz) {
        int n = 2;
        for (int i = 0; i <= n; i++) {
            tracer.setOrigins(px, pos.getY() + 0.5, pz, e.getX(), e.getY() + e.getBbHeight() * i / n, e.getZ());
            if (tracer.isClearLineOfSight(world))
                return true;
        }
        return false;
    }

    private float getSuctionMultiplier(Entity e) {
        if (e instanceof Player epl) {
            if (epl.isCreative() || epl.isSpectator())
                return 0;
            ItemStack is = epl.getItemBySlot(EquipmentSlot.FEET);
            if (!is.isEmpty()) {
                if (is.getItem() == RotaryItems.BEDROCK_ALLOY_BOOTS.get())
                    return 0.1F;
                if (is.getItem() == RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get())
                    return 0.1F;
            }
        }
        // EntityTurretShot (legacy projectile) → not yet ported; immune to suction by inheritance.
        return 1;
    }

    /**
     * Kill / process an entity that's been pulled all the way into the intake. Items teleport out
     * the back; experience orbs likewise. Living entities take catastrophic damage and increment
     * FOD if they can damage the engine.
     */
    private void ingestEntity(Level world, BlockPos pos, Entity e, boolean immune) {
        if (e instanceof ItemEntity ie) {
            if (ie.isAlive()) {
                ItemStack is = ie.getItem();
                e.discard();
                int trycount = 0;
                while (trycount < 1 && !ReikaWorldHelper.nonSolidBlocks(world, new BlockPos(dumpx, pos.getY(), dumpz))) {
                    if (dumpvx == 1) dumpx++;
                    if (dumpvx == -1) dumpx--;
                    if (dumpvz == 1) dumpz++;
                    if (dumpvz == -1) dumpz--;
                    trycount++;
                }
                ItemEntity out = new ItemEntity(world, dumpx + 0.5D, pos.getY() + 0.375D, dumpz + 0.5D, is);
                out.setDeltaMovement(dumpvx * 1.5D, 0.15, dumpvz * 1.5D);
                if (!world.isClientSide()) {
                    world.addFreshEntity(out);
                    out.hurtMarked = true;
                }
                if (this.itemDestroysEngine(is)) {
                    out.discard();
                    FOD = 2;
                    this.triggerJetFailing(world, pos);
                }
            }
        } else if (e instanceof ExperienceOrb orb) {
            if (orb.isAlive()) {
                int xp = orb.getValue();
                e.discard();
                int trycount = 0;
                while (trycount < 1 && !ReikaWorldHelper.nonSolidBlocks(world, new BlockPos(dumpx, pos.getY(), dumpz))) {
                    if (dumpvx == 1) dumpx++;
                    if (dumpvx == -1) dumpx--;
                    if (dumpvz == 1) dumpz++;
                    if (dumpvz == -1) dumpz--;
                    trycount++;
                }
                ExperienceOrb out = new ExperienceOrb(world, dumpx + 0.5D, pos.getY() + 0.375D, dumpz + 0.5D, xp);
                out.setDeltaMovement(dumpvx * 1.5D, 0.15, dumpvz * 1.5D);
                if (!world.isClientSide()) {
                    world.addFreshEntity(out);
                    out.hurtMarked = true;
                }
            }
        } else if (e instanceof LivingEntity le && !(e instanceof Player && immune)) {
            e.igniteForSeconds(2);
            if (!world.isClientSide() && le.getHealth() > 0 && this.canDamageEngine(e))
                this.damageEngine();
            if (FOD > 8)
                FOD = 8;
            if (e instanceof Chicken && e.isAlive() && le.getHealth() > 0) {
                chickenCount++;
                if (chickenCount >= 50) {
                    RotaryAdvancements.JETCHICKEN.triggerAchievement(this.getPlacer());
                }
            }
            if (!e.isAlive() && le.getHealth() < 0)
                SoundRegistry.INGESTION.playSoundAtBlock(world, pos, 1, 1.4F);
            e.hurt(RotaryCraft.jetingest.get(world), 10000);
            if (e instanceof Player ep && ep == this.getPlacer()) {
                RotaryAdvancements.SUCKEDINTOJET.triggerAchievement(ep);
            }
        }
    }

    private void triggerJetFailing(Level world, BlockPos pos) {
        RotaryCraft.LOGGER.warn(this + " just entered failure mode!");
        isJetFailing = true;

        RotaryAdvancements.JETFAIL.triggerAchievement(this.getPlacer());
        NeoForge.EVENT_BUS.post(new JetEngineEnterFailureEvent(this));
        // Legacy custom DragonAPI random-explode sound; closest vanilla equivalent is the generic
        // explosion sound at half pitch.
        ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.GENERIC_EXPLODE.value(), 2F, 0.75F);
        SoundRegistry.INGESTION.playSoundAtBlock(this, 0.5F, 1);

        if (write != null) {
            AABB box = ReikaAABBHelper.getBlockAABB(pos)
                    .move(write.getStepX() * 8, 0, write.getStepZ() * 8)
                    .inflate(3, 3, 3);
            List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box);
            HashSet<Integer> idSet = new HashSet<>();
            for (LivingEntity e : li) {
                e.hurt(world.damageSources().generic(), 8);
                idSet.add(e.getId());
            }
            box = ReikaAABBHelper.getBlockAABB(pos).inflate(4, 4, 4);
            li = world.getEntitiesOfClass(LivingEntity.class, box);
            for (LivingEntity e : li) {
                if (!idSet.contains(e.getId()))
                    e.hurt(world.damageSources().generic(), 4);
            }
        }

        if (world.isClientSide()) {
            for (int i = 0; i < 24; i++) {
                if (write == null) break;
                ItemEntity ei = new ItemEntity(world,
                        pos.getX() + 0.5 + write.getStepX(),
                        pos.getY() + 0.5,
                        pos.getZ() + 0.5 + write.getStepZ(),
                        new ItemStack(RotaryItems.HSLA_STEEL_SCRAP.get()));
                double v = ReikaRandomHelper.getRandomBetween(0.8D, 2D);
                double vx = ReikaRandomHelper.getRandomPlusMinus(write.getStepX() * v, 0.25);
                double vz = ReikaRandomHelper.getRandomPlusMinus(write.getStepZ() * v, 0.25);
                double vy = ReikaRandomHelper.getRandomPlusMinus(0, 0.25);
                ei.setDeltaMovement(vx, vy, vz);
                // 26.1: ItemEntity has setUnlimitedLifetime / setDefaultPickUpDelay; lifespan is
                // computed from age + getLifespan(). The legacy directly set `e.lifespan = X`;
                // there's no public setter but lifespan defaults to 6000. Setting age advances
                // the entity toward despawn — 5970 means it dies in 30 ticks.
                ei.setExtendedLifetime();
                world.addFreshEntity(ei);
            }
        }
        // Drop particles around the engine to simulate the violent failure.
        int r = 8;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    BlockPos dp = pos.offset(i, j, k);
                    // spawnDropParticles requires a ClientLevel; this code path only runs in
                    // jetEngineDetonation client-side guards above. Cast defensively.
                    if (world instanceof ClientLevel cl)
                        ReikaRenderHelper.spawnDropParticles(cl, dp, world.getBlockState(dp).getBlock());
                }
            }
        }
    }

    private boolean itemDestroysEngine(ItemStack is) {
        return is.getItem() == RotaryItems.SCREWDRIVER.get();
        // IWRENCH InterfaceCache check omitted — Buildcraft IWrench API isn't ported in 26.1.
    }

    private void damageEngine() {
        FOD++;
        if (DifficultyEffects.JETINGESTFAIL.testChance()) {
            isJetFailing = true;
            temperature = Math.max(temperature, 800);
        }
    }

    private boolean canDamageEngine(Entity caught) {
        if (caught.isSpectator())
            return false;
        if (caught instanceof Chicken) return false;
        if (caught instanceof Bat) return false;
        if (caught instanceof Silverfish) return false;
        if (caught instanceof ItemEntity) return false;
        if (caught instanceof ExperienceOrb) return false;
        String name = caught.getName().getString().toLowerCase(Locale.ROOT);
        if (name.contains("bird")) return false;
        if (name.contains("firefly")) return false;
        if (name.contains("butterfly")) return false;
        return caught instanceof LivingEntity;
    }

    /**
     * Conical AABB extending out from the intake. {@code step} = 0 is closest to the engine,
     * widening outward to {@code step} = 7. Returns null if the engine isn't oriented.
     */
    private AABB getSuctionZone(Level world, BlockPos pos, int step) {
        if (write == null) return null;
        int x = pos.getX();
        int y = pos.getY();
        int z = pos.getZ();

        int minx, miny, minz, maxx, maxy, maxz;
        // The intake side is opposite `write`. Project the funnel outward from there.
        switch (write) {
            case WEST -> { // intake on east (+x)
                minx = x + 1 + step; maxx = x + 1 + step + 1;
                miny = y - step;     maxy = y + step + 1;
                minz = z - step;     maxz = z + step + 1;
                dumpx = x - 1; dumpz = z;
                dumpvx = -1;   dumpvz = 0;
            }
            case EAST -> { // intake on west (-x)
                minx = x - 1 - step; maxx = x - 1 - step + 1;
                miny = y - step;     maxy = y + step + 1;
                minz = z - step;     maxz = z + step + 1;
                dumpx = x + 1; dumpz = z;
                dumpvx = 1;    dumpvz = 0;
            }
            case NORTH -> { // intake on south (+z)
                minz = z + 1 + step; maxz = z + 1 + step + 1;
                miny = y - step;     maxy = y + step + 1;
                minx = x - step;     maxx = x + step + 1;
                dumpx = x; dumpz = z - 1;
                dumpvx = 0; dumpvz = -1;
            }
            case SOUTH -> { // intake on north (-z)
                minz = z - 1 - step; maxz = z - 1 - step + 1;
                miny = y - step;     maxy = y + step + 1;
                minx = x - step;     maxx = x + step + 1;
                dumpx = x; dumpz = z + 1;
                dumpvx = 0; dumpvz = 1;
            }
            default -> { return null; }
        }
        return new AABB(minx, miny, minz, maxx, maxy, maxz).inflate(0.25, 0.25, 0.25);
    }

    /** Manually restore a damaged engine — clears FOD, snaps temperature toward ambient. */
    public void repairJet() {
        FOD = 0;
        isJetFailing = false;
        temperature = Math.max(ReikaWorldHelper.getAmbientTemperatureAt(this.getLevel(), getBlockPos()), temperature / 2);
    }

    public void repairJetPartial() {
        if (FOD > 0)
            FOD--;
    }

    /**
     * The catastrophic chain reaction once {@link #isJetFailing} = true. Spews flames out the
     * back of the engine, scorches the column, ignites entities, and rolls toward an actual
     * explosion via {@link #fail(Level, BlockPos)} once temperature climbs over 1000.
     */
    private void jetEngineDetonation(Level world, BlockPos pos) {
        AABB zone = this.getFlameZone(world, pos);
        if (zone != null) {
            List<LivingEntity> in = world.getEntitiesOfClass(LivingEntity.class, zone);
            for (LivingEntity e : in) {
                e.igniteForSeconds(2);
            }
        }
        if (write != null) {
            double vx = (-write.getStepX()) / 2D;
            double vz = (-write.getStepZ()) / 2D;
            for (int i = 0; i < 16; i++) {
                ParticleOptions p = (i % 2 == 0) ? ParticleTypes.FLAME : ParticleTypes.SMOKE;
                world.addParticle(p,
                        pos.getX() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                        pos.getY() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                        pos.getZ() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                        vx - 0.1 + 0.2 * DragonAPI.rand.nextDouble(),
                        -0.1 + 0.2 * DragonAPI.rand.nextDouble(),
                        vz - 0.1 + 0.2 * DragonAPI.rand.nextDouble());
            }
            for (int i = 0; i < 16; i++) {
                ReikaWorldHelper.temperatureEnvironment(world,
                        pos.offset(write.getStepX() * i, 0, write.getStepZ() * i),
                        800);
            }
        }
        world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 1F, 1F, false);

        if (FMLEnvironment.getDist() == Dist.DEDICATED_SERVER) {
            int chance1 = fuel.getFluidLevel() < FUELCAP / 12 ? 10 : (fuel.getFluidLevel() < FUELCAP / 4 ? 20 : 40);
            if (DragonAPI.rand.nextInt(chance1) == 0) {
                ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.MODID, PacketRegistry.ENGINEBACKFIRE.ordinal(), this, 64);
                this.backFire(world, pos);
            }
        }
        if (DragonAPI.rand.nextInt(2) == 0)
            temperature++;

        if (temperature >= 800) {
            RotaryCraft.LOGGER.warn("WARNING: " + this + " is near explosion!");
        }

        if (temperature > 1000) {
            this.fail(world, pos);
        }
    }

    private void fail(Level world, BlockPos pos) {
        NeoForge.EVENT_BUS.post(new JetEngineExplosionEvent(this));
        int r = 6;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    // ConfigRegistry.BLOCKDAMAGE legacy gate — config not ported, default to active.
                    ReikaWorldHelper.temperatureEnvironment(world, pos.offset(i, j, k), 1000);
                    world.addParticle(ParticleTypes.LAVA,
                            pos.getX() + i, pos.getY() + j, pos.getZ() + k, 0, 0, 0);
                    world.addParticle(ParticleTypes.LAVA,
                            pos.getX() + i, pos.getY() + j, pos.getZ() + k,
                            DragonAPI.rand.nextDouble() - 0.5,
                            DragonAPI.rand.nextDouble() - 0.5,
                            DragonAPI.rand.nextDouble() - 0.5);
                }
            }
        }
        if (!world.isClientSide()) {
            world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 12F, Level.ExplosionInteraction.MOB);
            for (int m = 0; m < 6; m++) {
                world.explode(null,
                        pos.getX() - 4 + DragonAPI.rand.nextInt(11),
                        pos.getY() - 4 + DragonAPI.rand.nextInt(11),
                        pos.getZ() - 4 + DragonAPI.rand.nextInt(11),
                        4F + DragonAPI.rand.nextFloat() * 2,
                        Level.ExplosionInteraction.MOB);
            }
        }
    }

    public void backFire(Level world, BlockPos pos) {
        if (write == null) return;
        double vx = (-write.getStepX()) / 2D;
        double vz = (-write.getStepZ()) / 2D;
        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5,
                2 * DragonAPI.rand.nextFloat(), Level.ExplosionInteraction.NONE);
        ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.GENERIC_EXPLODE.value(), 1F, 0.5F);
        for (int i = 0; i < 32; i++) {
            ParticleOptions p = (i % 2 == 0) ? ParticleTypes.FLAME : ParticleTypes.SMOKE;
            world.addParticle(p,
                    pos.getX() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                    pos.getY() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                    pos.getZ() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                    -vx - 0.1 + 0.2 * DragonAPI.rand.nextDouble(),
                    -0.1 + 0.2 * DragonAPI.rand.nextDouble(),
                    -vz - 0.1 + 0.2 * DragonAPI.rand.nextDouble());
        }
    }

    private AABB getFlameZone(Level world, BlockPos pos) {
        if (write == null) return null;
        return switch (write) {
            case WEST -> // intake east; flame zone runs WEST out the back. Legacy meta 0.
                    new AABB(pos.getX() - 6, pos.getY(), pos.getZ(),
                             pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            case EAST -> // legacy meta 1
                    new AABB(pos.getX(), pos.getY(), pos.getZ(),
                             pos.getX() + 7, pos.getY() + 1, pos.getZ() + 1);
            case NORTH -> // legacy meta 2
                    new AABB(pos.getX(), pos.getY(), pos.getZ() - 6,
                             pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            case SOUTH -> // legacy meta 3
                    new AABB(pos.getX(), pos.getY(), pos.getZ(),
                             pos.getX() + 1, pos.getY() + 1, pos.getZ() + 7);
            default -> null;
        };
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.JET_ENGINE.get();
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putBoolean("choke", isChoking);
        tag.putBoolean("jetfail", isJetFailing);
        tag.putBoolean("burn", canAfterBurn);
        tag.putBoolean("burning", burnerActive);
        // the client drives the looping jet drone: it needs the FOD level (pitch)
        // and spool-up progress (sound gate)
        tag.putInt("FOD", FOD);
        tag.putInt("jetstart", jetstarttimer.getTick());
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        isChoking = tag.getBooleanOr("choke", false);
        isJetFailing = tag.getBooleanOr("jetfail", false);
        canAfterBurn = tag.getBooleanOr("burn", false);
        burnerActive = tag.getBooleanOr("burning", false);
        FOD = tag.getIntOr("FOD", 0);
        jetstarttimer.setTick(tag.getIntOr("jetstart", 0));
    }

    @Override
    protected String getTEName() {
        return "jetengine";
    }

    @Override
    public void saveAdditional(CompoundTag tag) {
        super.saveAdditional(tag);
        tag.putInt("FOD", FOD);
        tag.putInt("chickens", chickenCount);
        tag.putInt("jetstart", jetstarttimer.getTick());
    }

    @Override
    public void load(CompoundTag tag) {
        super.load(tag);
        FOD = tag.getIntOr("FOD", 0);
        chickenCount = tag.getIntOr("chickens", 0);
        jetstarttimer.setTick(tag.getIntOr("jetstart", 0));
    }

    @Override
    protected void playServerSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
        afterburnTick++;
        if (FOD > 0 && DragonAPI.rand.nextInt(2 * (9 - FOD)) == 0) {
            // playLocalSound/addParticle are client-only no-ops; this runs on the server,
            // so broadcast the rattle and debris properly
            ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.BLAZE_HURT, 1F + DragonAPI.rand.nextFloat(), 1F);
            if (world instanceof ServerLevel sl) {
                sl.sendParticles(ParticleTypes.CRIT,
                        pos.getX() + DragonAPI.rand.nextFloat(),
                        pos.getY() + DragonAPI.rand.nextFloat(),
                        pos.getZ() + DragonAPI.rand.nextFloat(),
                        0,
                        -0.5 + DragonAPI.rand.nextFloat(),
                        DragonAPI.rand.nextFloat(),
                        -0.5 + DragonAPI.rand.nextFloat(),
                        1);
            }
        }
        if (this.isMuffled(world, pos)) {
            volume *= 0.3125F;
        }
        if (this.isAfterburning() && afterburnTick >= 50) {
            afterburnTick = 0;
            float vol = 0.9F * volume;
            if (omega < type.getSpeed()) {
                vol *= (float) Math.pow(0.75, type.getSpeed() / (double) omega);
            }
            SoundRegistry.AFTERBURN.playSoundAtBlock(world, pos, vol, 1);
        }
    }

    @Override
    public boolean shouldPlayEngineSound() {
        // no jet drone until the spool-up (jetstart sound) has finished
        return super.shouldPlayEngineSound() && jetstarttimer.getTick() >= jetstarttimer.getCap();
    }

    /**
     * Client-visible spool state: true while the engine is running but the jetstart spool-up sound
     * has not yet finished. The client {@code EngineSoundManager} uses this to drive the spool sound
     * as a tickable instance so it stops the moment the block is removed (previously the spool was a
     * fire-and-forget server packet that kept playing after the engine was broken).
     */
    public boolean isSpoolingUp() {
        return omega > 0 && jetstarttimer.getTick() < jetstarttimer.getCap();
    }

    /** Current spool-up progress in ticks (synced); used to avoid restarting the spool sound late. */
    public int getSpoolTick() {
        return jetstarttimer.getTick();
    }

    @Override
    public float getEngineSoundPitch() {
        return 1F / (0.125F * FOD + 1);
    }

    public boolean isAfterburning() {
        return canAfterBurn && burnerActive;
    }

    @Override
    public boolean hasTemperature() {
        return true;
    }

    @Override
    protected int getMaxSpeed(Level world, BlockPos pos) {
        float choke = this.getChokedFraction(world, pos);
        isChoking = choke < 0.5F;
        return (int) (EngineType.JET.getSpeed() * choke);
    }

    @Override
    protected boolean canStart() {
        return true;
    }

    @Override
    protected int getGenTorque(Level world, BlockPos pos) {
        int amt = EngineType.JET.getTorque();
        if (this.isAfterburning())
            amt *= 2;
        return amt / ReikaMathLibrary.intpow2(2, FOD);
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {
        this.checkJetFailure(world, pos);
        this.ingest(world, pos);
        this.fluidIngest(world, pos);
        this.heatJet(world, pos);
        // The spool-up (jetstart) sound is now a client-managed tickable instance driven by
        // isSpoolingUp() in EngineSoundManager, so it stops immediately when the engine is broken.
        // It is no longer fired as a fire-and-forget server packet here (which kept playing after
        // the block was removed).
        if (world.isClientSide())
            this.spawnSmokeParticles(world, pos);
        jetstarttimer.update();
        this.doAfterburning(world, pos);
    }

    private void fluidIngest(Level world, BlockPos pos) {
        if (write == null) return;
        Direction intake = write.getOpposite();
        BlockPos in = pos.relative(intake);
        BlockState st = world.getBlockState(in);
        Block b = st.getBlock();
        Fluid f = ReikaFluidHelper.lookupFluidForBlock(st);
        if (f == null) return;

        if (world.isClientSide()) {
            this.fluidIngestParticles(world, pos, f);
        } else {
            // 26.1: FluidType.getTemperature() returns the temperature in kelvins (default 300K).
            int temp = f.getFluidType().getTemperature();
            // Fluid-name lookup: legacy used Fluid.getName().contains("fuel") — 26.1 uses the
            // FluidType registry name. Check for jet/fuel/ethanol via registry-key path.
            var key = BuiltInRegistries.FLUID.getKey(f);
            String fname = key != null ? key.getPath() : "";
            if (fname.toLowerCase(Locale.ROOT).contains("fuel")) {
                if (!isJetFailing && DragonAPI.rand.nextInt(200) == 0) {
                    temperature = 900;
                    isJetFailing = true;
                    this.jetEngineDetonation(world, pos);
                }
            } else if (temp >= 500) {
                if (temp >= 2000 || DragonAPI.rand.nextInt(1 + (2000 - temp) / 500) == 0) {
                    // Legacy used getOverworldClockTime(); 26.1's Level only exposes getGameTime()
                    // (absolute server ticks) on the base class — equivalent for the modulo check.
                    if (FOD < 8 && world.getGameTime() % 20 == 0 && DragonAPI.rand.nextInt(1 + 2 * FOD) == 0) {
                        this.damageEngine();
                    }
                }
            }
        }
    }

    private void fluidIngestParticles(Level world, BlockPos pos, Fluid f) {
        if (write == null) return;
        int n = 1 + DragonAPI.rand.nextInt(8);
        for (int i = 0; i < n; i++) {
            double vx = (-write.getStepX()) / 4D;
            double vy = ReikaRandomHelper.getRandomPlusMinus(0, 0.0625);
            double vz = (-write.getStepZ()) / 4D;
            vx = ReikaRandomHelper.getRandomPlusMinus(vx, 0.0625);
            vz = ReikaRandomHelper.getRandomPlusMinus(vz, 0.0625);
            // 26.1: legacy EntityLiquidParticleFX has no direct equivalent. Use the fluid's
            // default flowing-block particle as the closest visual stand-in.
            BlockState fluidState = f.defaultFluidState().createLegacyBlock();
            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, fluidState),
                    pos.getX() + 0.5 + write.getStepX() * 0.25,
                    pos.getY() + 0.5,
                    pos.getZ() + 0.5 + write.getStepZ() * 0.25,
                    vx, vy, vz);
        }
    }

    private void doAfterburning(Level world, BlockPos pos) {
        if (this.isAfterburning()) {
            this.afterBurnParticles(world, pos);
            if (this.tickcount % 200 == 0) {
                temperature += 1;
                if (temperature > this.getMaxTemperature()) {
                    temperature = this.getMaxTemperature();
                    this.fail(world, pos);
                } else if (temperature >= 600) {
                    ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.FIRE_EXTINGUISH);
                    ReikaParticleHelper.SMOKE.spawnAroundBlock(world, pos, 8);
                }
            }
        }
    }

    private void afterBurnParticles(Level world, BlockPos pos) {
        if (write == null) return;
        double dx = -write.getStepX() / 2D;
        double dz = -write.getStepZ() / 2D;
        double vx = -write.getStepX() * 6D;
        double vz = -write.getStepZ() * 6D;
        for (int i = 0; i < 16; i++) {
            double px = dx + pos.getX() + 0.25 + 0.5 * DragonAPI.rand.nextDouble() + vx * DragonAPI.rand.nextDouble();
            double pz = dz + pos.getZ() + 0.25 + 0.5 * DragonAPI.rand.nextDouble() + vz * DragonAPI.rand.nextDouble();
            double dd = Math.abs(px - pos.getX()) + Math.abs(pz - pos.getZ());
            ParticleOptions option;
            if (dd < 1.5 + DragonAPI.rand.nextDouble())
                option = ParticleTypes.SOUL_FIRE_FLAME; // bright blue
            else if (dd < 2.5 + DragonAPI.rand.nextDouble())
                option = ParticleTypes.WHITE_ASH;       // white
            else if (dd < 3 + DragonAPI.rand.nextDouble() * 2)
                option = ParticleTypes.FLAME;           // yellow/green
            else if (dd < 5 + DragonAPI.rand.nextDouble() * 3 && DragonAPI.rand.nextBoolean())
                option = ParticleTypes.SMOKE;           // dim green smoke
            else
                option = ParticleTypes.LARGE_SMOKE;
            world.addParticle(option, px, pos.getY() + 0.75 * DragonAPI.rand.nextDouble(), pz, 0, 0, 0);
        }
    }

    private void spawnSmokeParticles(Level world, BlockPos pos) {
        if (write == null) return;
        double dx = -write.getStepX() / 2D;
        double dz = -write.getStepZ() / 2D;
        double vx = write.getStepX() / 2D;
        double vz = write.getStepZ() / 2D;
        ReikaParticleHelper.SMOKE.spawnAt(world,
                dx + pos.getX() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                pos.getY() + 0.5 * DragonAPI.rand.nextDouble(),
                dz + pos.getZ() + 0.25 + 0.5 * DragonAPI.rand.nextDouble(),
                -vx - 0.1 + 0.2 * DragonAPI.rand.nextDouble(),
                -0.1 + 0.2 * DragonAPI.rand.nextDouble(),
                -vz - 0.1 + 0.2 * DragonAPI.rand.nextDouble());

        int n = 1 + DragonAPI.rand.nextInt(8);
        double w = n / 2D;
        double px = write.getStepX() == 0 ? ReikaRandomHelper.getRandomPlusMinus(pos.getX() + 0.5, w) : pos.getX() + 0.5 - n * write.getStepX();
        double py = ReikaRandomHelper.getRandomPlusMinus(pos.getY() + 0.5, w);
        double pz = write.getStepZ() == 0 ? ReikaRandomHelper.getRandomPlusMinus(pos.getZ() + 0.5, w) : pos.getZ() + 0.5 - n * write.getStepZ();

        double v = -0.0625;
        double pvx = v * (px - pos.getX() - 0.5);
        double pvy = v * (py - pos.getY() - 0.5);
        double pvz = v * (pz - pos.getZ() - 0.5);

        BlockPos bp = new BlockPos(Mth.floor(px), Mth.floor(py), Mth.floor(pz));
        BlockState bstate = world.getBlockState(bp);
        if (bstate.isAir()) {
            if (DragonAPI.rand.nextInt(3) == 0)
                ReikaParticleHelper.CLOUD.spawnAt(world, px, py, pz, pvx, pvy, pvz);
        } else {
            // 26.1: legacy EntityBlockTexFX (block-texture particle with gravity 0) →
            // BlockParticleOption with no per-tick gravity adjust. Closest 26.1 stand-in.
            world.addParticle(new BlockParticleOption(ParticleTypes.BLOCK, bstate),
                    px, py + 1, pz, pvx, pvy - 0.03125, pvz);
        }
    }

    @Override
    protected void resetPower() {
        super.resetPower();
        jetstarttimer.reset();
    }

    /**
     * Reads engine-instance state from the BlockItem the player used to place the engine.
     * Legacy used a custom {@code setDataFromPlacer} on a parent class that no longer exists in
     * 26.1 — the data flow is now: BlockItem → place hook → BE constructor, with persisted state
     * stored on the BlockItem via CustomData. This method is called from {@code BlockJetEngine}'s
     * onPlace (TODO: wire when block-place hook is added in the parent BlockBasicMachine port).
     */
    public void setDataFromPlacer(ItemStack is) {
        var data = is.getOrDefault(DataComponents.CUSTOM_DATA,
                CustomData.EMPTY).copyTag();
        if (data != null) {
            FOD = data.getIntOr("damage", 0);
        }
    }

    @Override
    public boolean isBroken() {
        return FOD >= 8;
    }

    @Override
    public CompoundTag getTagsToWriteToStack() {
        if (canAfterBurn) {
            CompoundTag NBT = new CompoundTag();
            NBT.putBoolean("burn", canAfterBurn);
            return NBT;
        }
        return null;
    }

    @Override
    public void setDataFromItemStackTag(CompoundTag NBT) {
        canAfterBurn = NBT != null && NBT.getBooleanOr("burn", false);
    }

    @Override
    public ArrayList<CompoundTag> getCreativeModeVariants() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> getDisplayTags(CompoundTag NBT) {
        ArrayList<String> li = new ArrayList<>();
        if (NBT != null && NBT.getBooleanOr("burn", false)) {
            li.add("With Afterburner");
        }
        return li;
    }

    @Override
    public void upgrade(ItemStack is) {
        canAfterBurn = true;
    }

    @Override
    public boolean canUpgradeWith(ItemStack item) {
        // The crafted upgrade stores its type as a string ("upgradeType" = UpgradeType.desc),
        // not the legacy int ordinal — read it through the shared helper so afterburner upgrades
        // actually apply.
        return !canAfterBurn && ItemEngineUpgrade.getUpgrade(item) == UpgradeType.AFTERBURNER;
    }

    public boolean canAfterBurn() {
        return canAfterBurn;
    }

    public boolean burnerActive() {
        return burnerActive;
    }

    public void setBurnerActive(boolean burn) {
        burnerActive = burn;
    }

    @Override
    public void breakBlock() {
        super.breakBlock();
        if (canAfterBurn) {
            // Drop the afterburner upgrade item back. Legacy used getStackOfMetadata(int) — port
            // attaches the upgrade meta via CustomData on a fresh ItemStack.
            ItemStack upgrade = new ItemStack(RotaryItems.UPGRADE.get());
            CompoundTag tag = new CompoundTag();
            tag.putInt("upgrade", UpgradeType.AFTERBURNER.ordinal());
            upgrade.set(DataComponents.CUSTOM_DATA,
                    CustomData.of(tag));
            ReikaItemHelper.dropItem(getLevel(),
                    getBlockPos().getX() + 0.5,
                    getBlockPos().getY() + 0.5,
                    getBlockPos().getZ() + 0.5,
                    upgrade);
        }
    }

    public boolean allowHeatExtraction() {
        return true;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Jet Engine");
    }

    @Override
    public AbstractContainerMenu createMenu(int containerId, Inventory inv, Player player) {
        // 26.1: routes through the existing {@code ContainerJet} (was created earlier for the
        // mod-interface fuel engine but works just as well here — both share BlockEntityEngine
        // as the BE base). Previously returned null which meant right-clicking a jet engine
        // produced no GUI at all.
        return new ContainerJet(containerId, inv, this);
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        // Jet fuel only; only accept from the back-inflow side.
        if (resource == null || resource.isEmpty()) return 0;
        if (!resource.getFluid().equals(RotaryFluids.JET_FUEL.get())) return 0;
        return fuel.fill(resource, action);
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return FluidStack.EMPTY;
    }
}
