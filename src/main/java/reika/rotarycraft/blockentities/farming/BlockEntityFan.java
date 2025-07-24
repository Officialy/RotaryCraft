/*******************************************************************************
* @author Reika Kalseki
*
* Copyright 2017
*
* All rights reserved.
* Distribution of the software in any form is only allowed with
* explicit, prior permission from the owner.
******************************************************************************/
package reika.rotarycraft.blockentities.farming;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.common.NeoForge;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.ReikaAABBHelper;
import reika.dragonapi.libraries.ReikaDirectionHelper;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.interfaces.CustomFanEntity;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
import reika.rotarycraft.base.blocks.entity.BlockFan;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.SoundRegistry;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityFan extends BlockEntityBeamMachine implements RangedEffect, UpgradeableMachine, BreakAction {

   public static final long MAXPOWER = 2097152;
   public static final int FALLOFF = 1024;
   public static final int FALLOFF_WIDE = 2048;
   public static final double AXISSPEEDCAP = 1; //40 m/s
   public static final double BASESPEED = 0.000125;
   /**
    * Minimum speeds required to rip up blocks
    */
   public static final int WEBSPEED = 256;
   public static final int LEAFSPEED = 4096;
   public static final int GRASSSPEED = 1024;
   public static final int FIRESPEED = 64;
   public static final int FIRESPREADSPEED = 16;
   public static final int HARVESTSPEED = 512;
   private final StepTimer sound = new StepTimer(27);
   public int distancelimit = Math.max(32, ConfigRegistry.FANRANGE.getValue());
   public boolean wideAreaHarvest = true;
   public boolean wideAreaBlow = false;

   public BlockEntityFan(BlockPos pos, BlockState state) {
       super(RotaryBlockEntities.FAN.get(), pos, state);
   }

   @Override
   public Block getBlockEntityBlockID() {
       return RotaryBlocks.FAN.get();
   }

   @Override
   public void updateEntity(Level world, BlockPos pos) {
       super.updateBlockEntity();
       this.getPower(false);
       power = (long) omega * (long) torque;
               this.makeBeam(world, pos);
       sound.update();
       if (omega > 0) {
           if (sound.checkCap())
               SoundRegistry.FAN.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.05F : 0.5F, 1F);
       }
   }

   private void spreadFire(Level world, BlockPos pos, int range) {
       if (ReikaWorldHelper.checkForAdjMaterial(world, pos, MapColor.FIRE) != null) {
           Direction facing = this.getFacing();
           int a = 0;
           if (facing.getAxis() == Direction.Axis.Y) {
               a = 1;
           }
           int b = 1 - a;
           int editx;
           int edity;
           int editz;
           for (int i = 1; i <= range; i++) {
               editx = pos.getX() + i * facing.getStepX();
               edity = pos.getY() + i * facing.getStepY();
               editz = pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
               editx = -1 * a + pos.getX() + i * facing.getStepX();
               edity = pos.getY() + i * facing.getStepY();
               editz = -1 * b + pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
               editx = -1 * a + pos.getX() + i * facing.getStepX();
               edity = 1 + pos.getY() + i * facing.getStepY();
               editz = -1 * b + pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);

               editx = -1 * a + pos.getX() + i * facing.getStepX();
               edity = 2 + pos.getY() + i * facing.getStepY();
               editz = -1 * b + pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
               editx = pos.getX() + i * facing.getStepX();
               edity = pos.getY() + i * facing.getStepY();
               editz = pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);

               editx = pos.getX() + i * facing.getStepX();
               edity = 1 + pos.getY() + i * facing.getStepY();
               editz = pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);

               editx = pos.getX() + i * facing.getStepX();
               edity = 2 + pos.getY() + i * facing.getStepY();
               editz = pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);

               editx = 1 * a + pos.getX() + i * facing.getStepX();
               edity = pos.getY() + i * facing.getStepY();
               editz = 1 * b + pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);

               editx = 1 * a + pos.getX() + i * facing.getStepX();
               edity = 2 + pos.getY() + i * facing.getStepY();
               editz = 1 * b + pos.getZ() + i * facing.getStepZ();
               if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
                   world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
           }
       }
   }

   public int getRange() {
       if (power < MINPOWER)
           return 0;
       int power2 = (int) Math.min(power - MINPOWER, MAXPOWER);
       int range = 8 + power2 / (wideAreaBlow ? FALLOFF_WIDE : FALLOFF);
       if (range > this.getMaxRange())
           range = this.getMaxRange();
       return range;
   }

   private boolean isStoppedBy(Level world, BlockPos pos) {
       Block b = world.getBlockState(pos).getBlock();
       BlockState state = world.getBlockState(pos);
       if (b == Blocks.AIR || state.isAir())
           return false;
       if (state.canOcclude() || state.isCollisionShapeFullBlock(world, pos))
           return true;
       MachineRegistry m = MachineRegistry.getMachine(world, pos);
       return state.isSolid();
   }

       
    
    @Override
    protected void makeBeam(Level world, BlockPos pos) {
       if (power < MINPOWER)
           return;
       long power2 = Math.min(power, MAXPOWER);
       int range = this.getRange();
       Direction facing = this.getFacing();
       boolean blocked = false;
       for (int i = 1; i <= range && !blocked; i++) {
           if (this.isStoppedBy(world, new BlockPos(pos.getX() + i * facing.getStepX(), pos.getY() + i * facing.getStepY(), pos.getZ() + i * facing.getStepZ()))) {
               blocked = true;
               range = i;
           }
       }
       AABB zone = wideAreaBlow ? this.getWideBlowZone(range) : this.getBlowZone(range);
       List<Entity> inzone = world.getEntitiesOfClass(Entity.class, zone);
       for (Entity caught : inzone) {
           if (this.canBlowEntity(caught)) {
               double mass = ReikaEntityHelper.getEntityMass(caught);
               Vec3 motion = caught.getDeltaMovement();
               double motionX = motion.x;
               double motionY = motion.y;
               double motionZ = motion.z;
               
               if (motionX < AXISSPEEDCAP && facing.getStepX() != 0) {
                   double d = caught.getY() - pos.getX();
                   if (d == 0)
                       d = 1;
                   double multiplier = 1 / (d - this.getMaxRange());
                   if (d - this.getMaxRange() > 12)
                       multiplier = 0;
                   if (multiplier > 1 || multiplier < 0)
                       multiplier = 1;
                   double base = multiplier * power2 * BASESPEED * (wideAreaBlow ? 0.125 : 1);
                   double speedstep = Math.max(Math.abs(Math.abs(motionX) + base / (mass * Math.abs(d))), AXISSPEEDCAP);
                   double a = facing.getStepX() > 0 ? 0.004 : 0;
                   motionX = facing.getStepX() * speedstep + a;
               }
               if (motionY < AXISSPEEDCAP && facing.getStepY() != 0) {
                   double d = caught.getY() - pos.getY();
                   if (d == 0)
                       d = 1;
                   double multiplier = 1 / (d - this.getMaxRange());
                   if (d - this.getMaxRange() > 12)
                       multiplier = 0;
                   if (multiplier > 1 || multiplier < 0)
                       multiplier = 1;
                   double base = multiplier * power2 * BASESPEED * (wideAreaBlow ? 0.125 : 1);
                   motionY = facing.getStepY() * Math.max(Math.abs(Math.abs(motionY) + base / (mass * Math.abs(d))), AXISSPEEDCAP);
               }
               if (motionZ < AXISSPEEDCAP && facing.getStepZ() != 0) {
                   double d = caught.getZ() - pos.getZ();
                   if (d == 0)
                       d = 1;
                   double multiplier = 1 / (d - this.getMaxRange());
                   if (d - this.getMaxRange() > 12)
                       multiplier = 0;
                   if (multiplier > 1 || multiplier < 0)
                       multiplier = 1;
                   double base = multiplier * power2 * BASESPEED * (wideAreaBlow ? 0.125 : 1);
                   double speedstep = Math.max(Math.abs(Math.abs(motionZ) + base / (mass * Math.abs(d))), AXISSPEEDCAP);
                   double a = facing.getStepZ() > 0 ? 0.004 : 0;
                   motionZ = facing.getStepZ() * speedstep + a;
               }
               
               caught.setDeltaMovement(motionX, motionY, motionZ);
           }
       }
       this.clearBlocks(world, pos, range);
       this.spreadFire(world, pos, range);
   }

   private boolean canBlowEntity(Entity e) {
       if (e instanceof CustomFanEntity) {
           CustomFanEntity c = (CustomFanEntity) e;
           if (c.getBlowPower() > power)
               return false;
           Vec3 motion = e.getDeltaMovement();
           Direction facing = this.getFacing();
           double ang = ReikaMathLibrary.py3d(Math.signum(motion.x) - facing.getStepX(), Math.signum(motion.y) - facing.getStepY(), Math.signum(motion.z) - facing.getStepZ());
           return !(ang > c.getMaxDeflection());
       }
       return true;
   }

   private void clearBlocks(Level world, BlockPos pos, int range) {
       Direction facing = this.getFacing();
       int a = 0;
       if (facing.getAxis() == Direction.Axis.Y) {
           a = 1;
       }
       int b = 1 - a;
       int editx;
       int edity;
       int editz;
       for (int i = 1; i <= range; i++) {
           editx = pos.getX() + i * facing.getStepX();
           edity = pos.getY() + i * facing.getStepY();
           editz = pos.getZ() + i * facing.getStepZ();
           this.rip2(world, new BlockPos(editx, edity, editz));
           this.enhanceFinPower(world, new BlockPos(editx, edity, editz));

           if (wideAreaHarvest) {
               if (facing.getStepY() != 0) {
                   for (int k = -1; k <= 1; k++) {
                       for (int j = -1; j <= 1; j++) {
                           editx = pos.getX() + k;
                           edity = pos.getY() + i * facing.getStepY();
                           editz = pos.getZ() + j;
                           this.rip2(world, new BlockPos(editx, edity, editz));
                       }
                   }
               } else {
                   Direction left = ReikaDirectionHelper.getLeftBy90(facing);
                   for (int k = -1; k <= 1; k++) {
                       for (int j = 0; j <= 2; j++) {
                           editx = pos.getX() + i * facing.getStepX() + left.getStepX() * k;
                           edity = pos.getY() + i * facing.getStepY() + j;
                           editz = pos.getZ() + i * facing.getStepZ() + left.getStepZ() * k;
                           this.rip2(world, new BlockPos(editx, edity, editz));
                       }
                   }
               }
           }
       }
   }

   private void enhanceFinPower(Level world, BlockPos pos) {
       MachineRegistry m = MachineRegistry.getMachine(world, pos);
       if (m == MachineRegistry.COOLINGFIN) {
           BlockEntityCoolingFin te = (BlockEntityCoolingFin) world.getBlockEntity(pos);
           int[] tg = te.getTarget();
           ReikaParticleHelper.CLOUD.spawnAroundBlock(world, pos, 1);
           if (world.getDayTime() % 20 == 0) {
               int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
               if (te.getTemperature() > Tamb)
                   te.addTemperature(-(int) Math.min(10, 1 + power / 32768));
           }
       }
   }

   public void rip2(Level world, BlockPos pos) {
       Block id = world.getBlockState(pos).getBlock();
       BlockState state = world.getBlockState(pos);
       boolean crop = false;
       if (id != Blocks.POWDER_SNOW && id != Blocks.COBWEB && 
           id != Blocks.OAK_LEAVES && id != Blocks.BIRCH_LEAVES && 
           id != Blocks.SPRUCE_LEAVES && id != Blocks.JUNGLE_LEAVES &&
           id != Blocks.ACACIA_LEAVES && id != Blocks.DARK_OAK_LEAVES &&
                       id != Blocks.GRASS && id != Blocks.FIRE && !crop)
           return;
       
       int c = this.getHarvestingRand();
               if (id == Blocks.GRASS)
            c /= 3;
       c = Math.max(1, c);
       if (DragonAPI.rand.nextInt(c) > 0)
           return;
       if (id == Blocks.COBWEB && omega < WEBSPEED)
           return;
       if ((id == Blocks.OAK_LEAVES || id == Blocks.BIRCH_LEAVES || 
            id == Blocks.SPRUCE_LEAVES || id == Blocks.JUNGLE_LEAVES ||
            id == Blocks.ACACIA_LEAVES || id == Blocks.DARK_OAK_LEAVES || id == Blocks.CHERRY_LEAVES || id == Blocks.AZALEA_LEAVES || id == Blocks.FLOWERING_AZALEA_LEAVES) && omega < LEAFSPEED)
           return;
               if (id == Blocks.GRASS && omega < GRASSSPEED)
           return;
       if (id == Blocks.FIRE && omega < FIRESPEED)
           return;
       if (id == Blocks.POWDER_SNOW && omega < FIRESPEED)
           return;
       if (crop && omega < HARVESTSPEED)
           return;
       if (crop) {
           this.harvest(world, pos, id);
           return;
       }
       this.dropBlocks(world, pos, id);
       world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
   }

   private int getHarvestingRand() {
       return Math.max(50, 600 - 25 * ReikaMathLibrary.logbase2(omega));
   }

   private void harvest(Level world, BlockPos pos, Block id) {
   }

   private void dropBlocks(Level world, BlockPos pos, Block id) {
       if (id != Blocks.AIR) {
           BlockState state = world.getBlockState(pos);
           List<ItemStack> drops = Block.getDrops(state, (net.minecraft.server.level.ServerLevel) world, pos, null);
                       for (ItemStack drop : drops) {
                ReikaItemHelper.dropItem(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop);
            }
       }
       world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
   }

   public AABB getBlowZone(int range) {
       Direction facing = this.getFacing();
       return ReikaAABBHelper.getBeamBox(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), facing, range);
   }

   public AABB getWideBlowZone(int range) {
       AABB box = this.getBlowZone(range);
       Direction facing = this.getFacing();
       int ex = (facing.getAxis() == Direction.Axis.X) ? 0 : 1;
       int ey = (facing.getAxis() == Direction.Axis.Y) ? 0 : 1;
       int ez = (facing.getAxis() == Direction.Axis.Z) ? 0 : 1;
       return box.expandTowards(ex, ey, ez);
   }

   @Override
   public boolean hasModelTransparency() {
       return false;
   }

   @Override
   protected void animateWithTick(Level world, BlockPos pos) {
       if (!this.isInWorld()) {
           phi = 0;
           return;
       }
       if (power < MINPOWER)
           return;
       phi += 3 * ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
   }

   @Override
   public MachineRegistry getMachine() {
       return MachineRegistry.FAN;
   }

   @Override
   public int getMaxRange() {
       return distancelimit;
   }

   @Override
   public int getRedstoneOverride() {
       return 0;
   }

   @Override
   public void onEMP() {
   }

   @Override
   public void upgrade(ItemStack item) {
       if (!wideAreaBlow && ReikaItemHelper.matchStacks(item, RotaryItems.DIFFUSER.get())) {
           wideAreaBlow = true;
       }
   }

   @Override
   public boolean canUpgradeWith(ItemStack item) {
       return ReikaItemHelper.matchStacks(item, RotaryItems.DIFFUSER.get());
   }

   @Override
   protected void writeSyncTag(CompoundTag NBT) {
       super.writeSyncTag(NBT);

       NBT.putBoolean("wideh", wideAreaHarvest);
       NBT.putBoolean("wideb", wideAreaBlow);
   }

   @Override
   protected void readSyncTag(CompoundTag NBT) {
       super.readSyncTag(NBT);

       wideAreaBlow = NBT.getBoolean("wideb");
       wideAreaHarvest = NBT.getBoolean("wideh");
   }

   @Override
   protected String getTEName() {
       return null;
   }

   @Override
   public void breakBlock() {
       if (wideAreaBlow) {
           ReikaItemHelper.dropItem(level, worldPosition, RotaryItems.DIFFUSER.get().getDefaultInstance());
       }
   }

   @Override
   public boolean hasAnInventory() {
       return false;
   }

   @Override
   public boolean hasATank() {
       return false;
   }
}
