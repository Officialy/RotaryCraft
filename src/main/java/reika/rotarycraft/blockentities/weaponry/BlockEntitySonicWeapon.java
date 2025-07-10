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
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.sounds.SoundEvents;
//import net.minecraft.sounds.SoundSource;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.effect.MobEffectInstance;
//import net.minecraft.world.effect.MobEffects;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.Mob;
//import net.minecraft.world.entity.ambient.AmbientCreature;
//import net.minecraft.world.entity.animal.Animal;
//import net.minecraft.world.entity.monster.Silverfish;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.alchemy.Potion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.AABB;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.interfaces.blockentity.GuiController;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import java.util.List;
//
//public class BlockEntitySonicWeapon extends BlockEntityPowerReceiver implements GuiController, RangedEffect {
//
//    public static final long MAXBROWNNOTE = 64; //Triggers food poisoning
//    public static final long BATKILL = 80000; //Well within their echolocation pitch range
//    public static final long OMINOUS = 16; //That almost-infrasonic uneasy-feeling-generating tone; triggers blindness effect
//    public static final long DOGWHISTLE = 40000; //dog whistle
//    public static final long LRAD = 2400; //Pitch for most crowd-control audio weapons
//    public static final long LETHALVOLUME = 100000000; //10^8 W/m (~38psi)=200dB for 1-hit kill
//    public static final long BRICKDESTROY = 1000000; //== 20kPa overpressure -> "Brick walls destroyed"
//    public static final long LRADVOLUME = 1260;
//    public static final long SHATTERGLASS = 118680; //1psi overpressure
//    public static final long BREAKWOOD = 475410; //2 psi overpressure
//    public static final long LUNGDAMAGE = 2971000; //5 psi causes pulmonary damage -> cause drowning effect
//    public static final long BRAINDAMAGE = 3906200; //125kPa causes TBIs
//    public static final long EYEDAMAGE = 1807500; //Causes blindness
//    public static final long SILVERFISHKILL = 400000;
//    public static final long REFERENCE = 1000000000000L; // 10^-12 W/m^2 reference
//    public static final int fudge = 100;
//    public static final int FALLOFF = 16384;
//    public static final long INTENSITYPERTORQUE = 262144L * 65536L * 256L * 8L;
//    public static final int HZPEROMEGA = 8192;
//    public static final boolean ENABLEFREQ = false;
//    public static final boolean DECIBELMODE = true;
//    public long setpitch;
//    public long setvolume;
//    public int brownrange;
//    public int batrange;
//    public int ominousrange;
//    public int dogrange;
//    public int lradrange;
//
//    public int killrange;
//    public int brickrange;
//    public int glassrange;
//    public int woodrange;
//    public int lungrange;
//    public int brainrange;
//    public int eyerange;
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getSummativeSidedPower();
//        if (power < MINPOWER)
//            return;
//        if (setpitch > this.getMaxPitch())
//            setpitch = this.getMaxPitch();
//        if (setvolume > this.getMaxVolume())
//            setvolume = this.getMaxVolume();
//        this.applyEffects(world, pos);
//        if (tickcount >= 10) {
//            SoundRegistry.SONIC.playSoundAtBlock(world, pos);
//            tickcount = 0;
//        }
//    }
//
//    public void applyEffects(Level world, BlockPos pos) {
//        this.testKill(world, pos);
//        this.breakBrick(world, pos);
//        this.testLung(world, pos);
//        this.testEye(world, pos);
//        this.testBrain(world, pos);
//        this.killSilverfish(world, pos);
//    }
//
//    private void killSilverfish(Level world, BlockPos pos) {
//        if (this.getVolume() < SILVERFISHKILL)
//            return;
//        int range = (int) (6D * this.getVolume() / SILVERFISHKILL);
//        if (range > 20)
//            range = 20;
//        //ReikaJavaLibrary.pConsole(range);
//        for (int i = 0; i < range; i++) {
//            int bx = x - range + DragonAPI.rand.nextInt(range + 1);
//            int by = y - range + DragonAPI.rand.nextInt(range + 1);
//            int bz = z - range + DragonAPI.rand.nextInt(range + 1);
//            //ReikaJavaLibrary.pConsole("Block "+world.getBlock(bx, by, bz)+" @ "+bx+", "+by+", "+bz);
//            if (world.getBlockState(new BlockPos(bx, by, bz)).getBlock() == Blocks.MONSTER_EGG) {
//                //ReikaJavaLibrary.pConsole("Killed at "+bx+", "+by+", "+bz);
//                int metadata = world.getBlockMetadata(bx, by, bz);
//                switch (metadata) {
//                    case 0:
//                        world.setBlock(bx, by, bz, Blocks.STONE);
//                        break;
//                    case 1:
//                        world.setBlock(bx, by, bz, Blocks.COBBLESTONE);
//                        break;
//                    case 2:
//                        world.setBlock(bx, by, bz, Blocks.STONEBRICK);
//                        break;
//                }
//                world.playLocalSound(bx + 0.5, by + 0.5, bz + 0.5, SoundEvents.SILVERFISH_DEATH, SoundSource.BLOCKS, 1, 1, true);
//                ReikaWorldHelper.splitAndSpawnXP(world, pos.getX() + 0.5F, pos.getY() + 0.5F, pos.getZ() + 0.5F, new Silverfish(world).experienceValue);
//            }
//        }
//    }
//
//    public void testEye(Level world, BlockPos pos) {
//        int range = this.getRange();
//        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(range, range, range);
//        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, box);
//        for (LivingEntity ent : inbox) {
//            boolean vuln = true;
//            if (ent instanceof Player)
//                if (!this.isPlayerVulnerable((Player) ent))
//                    vuln = false;
//            //ReikaChatHelper.write(this.EYEDAMAGE-this.fudge*ReikaPhysicsHelper.inverseSquare(ent.getY-x-0.5, ent.getY()-y-0.5, ent.posZ-z-0.5, this.getMaxVolume()));
//            if (vuln && fudge * ReikaPhysicsHelper.inverseSquare(ent.getY() - pos.getX() - 0.5, ent.getY() - pos.getY() - 0.5, ent.getZ() - pos.getZ() - 0.5, this.getVolume()) >= EYEDAMAGE) {
//                ent.addEffect(new MobEffectInstance(MobEffects.BLINDNESS, 20, 0));
//                //ReikaChatHelper.write(ent.getAITarget());
//                //ent.getNavigator().clearPathEntity();
//                //ent.setAttackTarget(null);
//                //ent.setRevengeTarget(null);
//                //ent.setLastAttackingEntity(null);
//                if (ent instanceof AmbientCreature) {
//                    //((AmbientCreature)ent).setTarget(null);
//                }
//            }
//        }
//    }
//
//    public void testBrain(Level world, BlockPos pos) {
//        int range = this.getRange();
//        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expandTowards(range, range, range);
//        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, box);
//        for (LivingEntity ent : inbox) {
//            boolean vuln = true;
//            if (ent instanceof Player)
//                if (!this.isPlayerVulnerable((Player) ent))
//                    vuln = false;
//            //ReikaChatHelper.write(this.BRAINDAMAGE-this.fudge*ReikaPhysicsHelper.inverseSquare(ent.getY-x-0.5, ent.getY()-y-0.5, ent.posZ-z-0.5, this.getVolume()));
//            if (vuln && fudge * ReikaPhysicsHelper.inverseSquare(ent.getY() - pos.getX() - 0.5, ent.getY() - pos.getY() - 0.5, ent.getZ() - pos.getZ() - 0.5, this.getVolume()) >= BRAINDAMAGE) {
//                ent.addEffect(new MobEffectInstance(MobEffects.CONFUSION, 200, 10));
//                ent.addEffect(new MobEffectInstance(MobEffects.DIG_SLOWDOWN, 20, 3));
//                ent.addEffect(new MobEffectInstance(MobEffects.MOVEMENT_SLOWDOWN, 20, 1));
//                if (ent instanceof Animal) {
//                    Animal ani = (Animal) ent;
//                    ani.getNavigator().clearPathEntity();
//                    if (ani.getNavigator().noPath()) {
//                        double randx = ani.getY - 8 + DragonAPI.rand.nextInt(17);
//                        double randz = ani.posZ - 8 + DragonAPI.rand.nextInt(17);
//                        int randy = world.getTopSolidOrLiquidBlock((int) randx, (int) randz);
//                        PathEntity path = ani.getNavigator().getPathToXYZ(randx, randy, randz);
//                        ani.getNavigator().setPath(path, 0.2F);
//                    }
//                }
//                if (ent instanceof Mob) {
//                    AABB nearmob = AABB.getBoundingBox(ent.getY, ent.getY(), ent.posZ, ent.getY, ent.getY(), ent.posZ).expand(10, 10, 10);
//                    Entity target = world.findNearestEntityWithinAABB(Mob.class, nearmob, ent);
//                    if (target instanceof EntityMob) {
//                        ((Mob) ent).setAttackTarget((LivingEntity) target);
//                        ((Mob) ent).setTarget(target);
//                        ent.setRevengeTarget((LivingEntity) target);
//                        ent.setLastAttacker(target);
//                    }
//                }
//            }
//        }
//    }
//
//    public void testLung(Level world, BlockPos pos) {
//        int range = this.getRange();
//        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(range, range, range);
//        List<LivingEntity> inbox = world.getEntities(LivingEntity.class, box);
//        for (LivingEntity ent : inbox) {
//            boolean vuln = true;
//            if (ent instanceof Player)
//                if (!this.isPlayerVulnerable((Player) ent))
//                    vuln = false;
//            if (vuln && ReikaPhysicsHelper.inverseSquare(ent.getY() - x - 0.5, ent.getY() - y - 0.5, ent.getZ() - z - 0.5, this.getVolume()) >= LUNGDAMAGE)
//                if (DragonAPI.rand.nextInt(40) == 0)
//                    ent.hurt(DamageSource.drown, 1);
//        }
//    }
//
//    public void breakBrick(Level world, BlockPos pos) {
//        //ReikaWorldHelper
//    }
//
//    public void testKill(Level world, BlockPos pos) {
//        //ReikaChatHelper.write(this.getMaxVolume());
//        int range = this.getRange();
//        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expandTowards(range, range, range);
//        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, box);
//        for (LivingEntity ent : inbox) {
//            boolean vuln = true;
//            if (ent instanceof Player)
//                if (!this.isPlayerVulnerable((Player) ent))
//                    vuln = false;
//            if (vuln && ReikaPhysicsHelper.inverseSquare(ent.getY() - pos.getX() - 0.5, ent.getY() - pos.getY() - 0.5, ent.getZ() - pos.getZ() - 0.5, this.getVolume()) >= LETHALVOLUME)
//                ent.hurt(DamageSource.OUT_OF_WORLD, Integer.MAX_VALUE);
//        }
//    }
//
//    public long getMaxPitch() {
//        return (omega * HZPEROMEGA);
//    }
//
//    public long getMaxVolume() {
//        return (INTENSITYPERTORQUE * torque);
//    }
//
//    public long getVolume() {
//        long maxvol = this.getMaxVolume();
//        if (setvolume > maxvol)
//            return maxvol / 1000000;
//        return setvolume / 1000000;
//    }
//
//    public long getPitch() {
//        long maxpitch = this.getMaxPitch();
//        if (setpitch > maxpitch)
//            return maxpitch;
//        return setpitch;
//    }
//
//    public int getRange() {
//        if (this != null)
//            return 16;
//        int overpower = (int) (power - MINPOWER) / FALLOFF;
//        if (overpower > RotaryConfig.COMMON.SONICRANGE.get())
//            return RotaryConfig.COMMON.SONICRANGE.get();
//        return overpower;
//    }
//
//    private boolean isPlayerVulnerable(Player ep) {
//        if (ep.isCreative())
//            return false;
//        //if (ep.inventory.armorInventory[3].getItem == RotaryCraft.earmuff.itemID)
//        return ep.getInventory().armorInventory[3] == null;
//    }
//
//    /**
//     * Writes a tile entity to NBT.
//     */
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putLong("setfrequency", setpitch);
//        NBT.putLong("setvolume", setvolume);
//    }
//
//    /**
//     * Reads a tile entity from NBT.
//     */
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        setpitch = NBT.getLong("setfrequency");
//        setvolume = NBT.getLong("setvolume");
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
//        return MachineRegistry.SONICWEAPON;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return Math.max(64, RotaryConfig.COMMON.SONICRANGE.get());
//    }
//
//    @Override
//    public int getSlots() {
//        return 0;
//    }
//
//    
//    @Override
//    public ItemStack getStackInSlot(int slot) {
//        return null;
//    }
//
//    
//    @Override
//    public ItemStack insertItem(int slot,  ItemStack stack, boolean simulate) {
//        return null;
//    }
//
//    
//    @Override
//    public ItemStack extractItem(int slot, int amount, boolean simulate) {
//        return null;
//    }
//
//    @Override
//    public int getSlotLimit(int slot) {
//        return 0;
//    }
//
//    @Override
//    public boolean isItemValid(int slot,  ItemStack stack) {
//        return false;
//    }
//
//    @Override
//    public String getName() {
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
