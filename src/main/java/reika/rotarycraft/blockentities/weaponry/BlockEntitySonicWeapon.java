/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.blockentities.weaponry;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.InfestedBlock;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.levelgen.Heightmap;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.gui.container.machine.ContainerSonic;
import reika.rotarycraft.registry.*;

/** V33a torque-limited acoustic weapon; the original disables frequency effects. */
public final class BlockEntitySonicWeapon extends BlockEntityPowerReceiver implements GuiController, RangedEffect {
    public static final long MAXBROWNNOTE = 64, BATKILL = 80000, OMINOUS = 16, DOGWHISTLE = 40000, LRAD = 2400;
    public static final long LETHALVOLUME = 100000000, BRICKDESTROY = 1000000, LRADVOLUME = 1260;
    public static final long SHATTERGLASS = 118680, BREAKWOOD = 475410, LUNGDAMAGE = 2971000;
    public static final long BRAINDAMAGE = 3906200, EYEDAMAGE = 1807500, SILVERFISHKILL = 400000;
    public static final long REFERENCE = 1000000000000L;
    public static final int fudge = 1, FALLOFF = 16384, HZPEROMEGA = 8192;
    public static final long INTENSITYPERTORQUE = 1000000L * LETHALVOLUME / 262144;
    public static final boolean ENABLEFREQ = false, DECIBELMODE = true;
    public long setpitch, setvolume;

    public BlockEntitySonicWeapon(BlockPos pos, BlockState state) { super(RotaryBlockEntities.SONIC_WEAPON.get(), pos, state); }
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (!(world instanceof ServerLevel)) return;
        tickcount++;
        getSummativeSidedPower();
        if (power < MINPOWER) return;
        long pitch = Math.clamp(setpitch, 0, getMaxPitch());
        long volume = Math.clamp(setvolume, 0, getMaxVolume());
        if (pitch != setpitch || volume != setvolume) {
            setpitch = pitch; setvolume = volume; setChanged();
        }
        applyEffects(world, pos);
        if (tickcount >= 10) { SoundRegistry.SONIC.playSoundAtBlock(world, pos); tickcount = 0; }
    }
    public void applyEffects(Level world, BlockPos pos) {
        if (!(world instanceof ServerLevel server)) return;
        for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(pos).inflate(getRange()))) {
            if (!entity.isAlive() || entity instanceof Player player && !isPlayerVulnerable(player)) continue;
            double intensity = getIntensityAt(entity.position());
            if (intensity >= EYEDAMAGE) entity.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
            if (intensity >= BRAINDAMAGE) affectBrain(server, entity);
            if (intensity >= LUNGDAMAGE && world.getRandom().nextInt(40) == 0)
                entity.hurtServer(server, entity.damageSources().drown(), 1);
            if (intensity >= LETHALVOLUME)
                entity.hurtServer(server, entity.damageSources().fellOutOfWorld(), Integer.MAX_VALUE);
        }
        // V33a declares brick/wood/glass thresholds but its breakBrick method has no behavior.
        killSilverfish(server, pos);
    }
    public double getIntensityAt(Vec3 position) {
        double volume = getVolume();
        return volume == 0 ? 0 : volume / position.distanceToSqr(Vec3.atCenterOf(getBlockPos()));
    }
    private void affectBrain(ServerLevel world, LivingEntity entity) {
        entity.addEffect(new MobEffectInstance(MobEffects.NAUSEA, 200, 10));
        entity.addEffect(new MobEffectInstance(MobEffects.MINING_FATIGUE, 20, 3));
        entity.addEffect(new MobEffectInstance(MobEffects.SLOWNESS, 20, 1));
        if (entity instanceof Animal animal) {
            var navigation = animal.getNavigation(); navigation.stop();
            if (navigation.isDone()) {
                double x = animal.getX() - 8 + world.getRandom().nextInt(17), z = animal.getZ() - 8 + world.getRandom().nextInt(17);
                int y = world.getHeight(Heightmap.Types.MOTION_BLOCKING, (int)x, (int)z);
                navigation.moveTo(navigation.createPath(x, y, z, 0), .2);
            }
        }
        if (entity instanceof Monster monster) {
            Monster target = world.getEntitiesOfClass(Monster.class, new AABB(entity.position(), entity.position()).inflate(10),
                    candidate -> candidate != entity && candidate.isAlive()).stream()
                    .min(java.util.Comparator.comparingDouble(entity::distanceToSqr)).orElse(null);
            if (target != null) { monster.setTarget(target); monster.setLastHurtByMob(target); monster.setLastHurtMob(target); }
        }
    }
    private void killSilverfish(ServerLevel world, BlockPos pos) {
        if (getVolume() < SILVERFISHKILL) return;
        int range = (int)Math.min(20, 6D * getVolume() / SILVERFISHKILL);
        for (int i = 0; i < range; i++) {
            BlockPos target = pos.offset(world.getRandom().nextInt(2 * range + 1) - range,
                    world.getRandom().nextInt(2 * range + 1) - range, world.getRandom().nextInt(2 * range + 1) - range);
            if (!world.hasChunkAt(target)) continue;
            BlockState state = world.getBlockState(target);
            if (state.getBlock() instanceof InfestedBlock infested) {
                world.setBlock(target, infested.hostStateByInfested(state), 3);
                world.playSound(null, target, SoundEvents.SILVERFISH_DEATH, SoundSource.BLOCKS, 1, 1);
                var silverfish = EntityTypes.SILVERFISH.create(world, EntitySpawnReason.COMMAND);
                if (silverfish != null) ExperienceOrb.award(world, Vec3.atCenterOf(pos), silverfish.getExperienceReward(world, null));
            }
        }
    }
    public long getMaxPitch() { return Math.max(0L, (long)omega * HZPEROMEGA); }
    public long getMaxVolume() { return Math.max(0L, INTENSITYPERTORQUE * torque); }
    public long getVolume() { return Math.clamp(setvolume, 0, getMaxVolume()) / 1000000; }
    public long getPitch() { return Math.clamp(setpitch, 0, getMaxPitch()); }
    @Override public int getRange() { return 16; }
    @Override public int getMaxRange() { return getRange(); }
    public boolean isPlayerVulnerable(Player player) { return !player.isCreative() && player.getItemBySlot(EquipmentSlot.HEAD).isEmpty(); }
    public void setRequestedValue(boolean pitch, long value) {
        if (value < 0) return;
        if (pitch) setpitch = value; else setvolume = value;
        setChanged(); syncAllData(false);
    }
    public static long volumeFromDecibels(int decibels) { return decibels < 0 ? 0 : (long)Math.pow(10, decibels / 10D); }
    public static int decibelsFromVolume(long volume) { return volume <= 0 ? 0 : Math.max(0, (int)(10 * Math.log10(volume))); }
    @Override protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag); tag.putLong("setfrequency", setpitch); tag.putLong("setvolume", setvolume);
    }
    @Override protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag); setpitch = Math.max(0, tag.getLongOr("setfrequency", 0L)); setvolume = Math.max(0, tag.getLongOr("setvolume", 0L));
    }
    @Override protected void animateWithTick(Level world, BlockPos pos) {}
    @Override public MachineRegistry getMachine() { return MachineRegistry.SONICWEAPON; }
    @Override protected String getTEName() { return "sonicweapon"; }
    @Override public Block getBlockEntityBlockID() { return RotaryBlocks.SONIC_WEAPON.get(); }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public boolean hasAnInventory() { return false; }
    @Override public boolean hasATank() { return false; }
    @Override public int getRedstoneOverride() { return 0; }
    @Override public Component getDisplayName() { return Component.translatable("block.rotarycraft.sonic_weapon"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inventory, Player player) { return new ContainerSonic(id, inventory, this); }
}
