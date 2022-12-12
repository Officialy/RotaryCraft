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
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.Entity;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Material;
//import net.minecraft.world.phys.AABB;
//import net.minecraftforge.common.MinecraftForge;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.interfaces.blockentity.BreakAction;
//import reika.dragonapi.libraries.ReikaAABBHelper;
//import reika.dragonapi.libraries.ReikaDirectionHelper;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.rotarycraft.api.Interfaces.CustomFanEntity;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
//import reika.rotarycraft.auxiliary.interfaces.UpgradeableMachine;
//import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockEntityFan extends BlockEntityBeamMachine implements RangedEffect, UpgradeableMachine, BreakAction {
//
//    public static final long MAXPOWER = 2097152;
//    public static final int FALLOFF = 1024;
//    public static final int FALLOFF_WIDE = 2048;
//    public static final double AXISSPEEDCAP = 1; //40 m/s
//    public static final double BASESPEED = 0.000125;
//    /**
//     * Minimum speeds required to rip up blocks
//     */
//    public static final int WEBSPEED = 256;
//    public static final int LEAFSPEED = 4096;
//    public static final int GRASSSPEED = 1024;
//    public static final int FIRESPEED = 64;
//    public static final int FIRESPREADSPEED = 16;
//    public static final int HARVESTSPEED = 512;
//    private final StepTimer sound = new StepTimer(27);
//    public int distancelimit = Math.max(32, RotaryConfig.COMMON.FANRANGE.get());
//    public boolean wideAreaHarvest = true;
//    public boolean wideAreaBlow = false;
//
//    public BlockEntityFan(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
////        this.getIOSides(world, pos);
//        this.getPower(false);
//        power = (long) omega * (long) torque;
////        if (AtmosphereHandler.isNoAtmo(world, pos.getX() - this.getReadDirection().getStepX(), pos.getY(), pos.getZ() - this.getReadDirection().getStepZ(), blockType, false))
////            return;
//        this.makeBeam(world, pos);
//        sound.update();
//        if (omega > 0) {
//            if (sound.checkCap())
//                SoundRegistry.FAN.playSoundAtBlock(world, pos, RotaryAux.isMuffled(this) ? 0.05F : 0.5F, 1F);
//        }
//    }
//
//    private void spreadFire(Level world, BlockPos pos, int range) {
//        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.FIRE) != null || ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.LAVA) != null) {
//            int a = 0;
////            todo if (meta > 1)
//                a = 1;
//            int b = 1 - a;
//            int editx;
//            int edity;
//            int editz;
//            for (int i = 1; i <= range; i++) {
//                editx = pos.getX() + i * facing.getStepX();
//                edity = pos.getY() + i * facing.getStepY();
//                editz = pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//                editx = -1 * a + pos.getX() + i * facing.getStepX();
//                edity = pos.getY() + i * facing.getStepY();
//                editz = -1 * b + pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//                editx = -1 * a + pos.getX() + i * facing.getStepX();
//                edity = 1 + pos.getY() + i * facing.getStepY();
//                editz = -1 * b + pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//
//                editx = -1 * a + pos.getX() + i * facing.getStepX();
//                edity = 2 + pos.getY() + i * facing.getStepY();
//                editz = -1 * b + pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//                editx = pos.getX() + i * facing.getStepX();
//                edity = pos.getY() + i * facing.getStepY();
//                editz = pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//
//                editx = pos.getX() + i * facing.getStepX();
//                edity = 1 + pos.getY() + i * facing.getStepY();
//                editz = pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//
//                editx = pos.getX() + i * facing.getStepX();
//                edity = 2 + pos.getY() + i * facing.getStepY();
//                editz = pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//
//                editx = 1 * a + pos.getX() + i * facing.getStepX();
//                edity = pos.getY() + i * facing.getStepY();
//                editz = 1 * b + pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//
//                editx = 1 * a + pos.getX() + i * facing.getStepX();
//                edity = 2 + pos.getY() + i * facing.getStepY();
//                editz = 1 * b + pos.getZ() + i * facing.getStepZ();
//                if (DragonAPI.rand.nextInt(60) == 0 && ReikaWorldHelper.softBlocks(world, new BlockPos(editx, edity, editz)))
//                    world.setBlock(new BlockPos(editx, edity, editz), Blocks.FIRE.defaultBlockState(), 3);
//            }
//        }
//    }
//
//    public int getRange() {
//        if (power < MINPOWER)
//            return 0;
//        int power2 = (int) Math.min(power - MINPOWER, MAXPOWER);
//        int range = 8 + power2 / (wideAreaBlow ? FALLOFF_WIDE : FALLOFF);
//        if (range > this.getMaxRange())
//            range = this.getMaxRange();
//        return range;
//    }
//
//    private boolean isStoppedBy(Level world, BlockPos pos) {
//        Block b = world.getBlockState(pos).getBlock();
//        if (b == Blocks.AIR || b.defaultBlockState().isAir())
//            return false;
//        if (ReikaCropHelper.isCrop(b) || ModCropList.isModCrop(b, meta))
//            return false;
//        if (b.isOpaqueCube() || b.renderAsNormalBlock())
//            return true;
//        MachineRegistry m = MachineRegistry.getMachine(world, pos);
//        if (m == MachineRegistry.LAWNSPRINKLER || m == MachineRegistry.SPRINKLER)
//            return false;
//        return b.defaultBlockState().getMaterial().isSolid();
//    }
//
//    @Override
//    protected void makeBeam(Level world, BlockPos pos) {
//        if (power < MINPOWER)
//            return;
//        long power2 = Math.min(power, MAXPOWER);
//        int range = this.getRange();
//        boolean blocked = false;
//        for (int i = 1; i <= range && !blocked; i++) { //Limit range to line-of-sight
//            if (this.isStoppedBy(world, new BlockPos(pos.getX() + i * facing.getStepX(), pos.getY() + i * facing.getStepY(), pos.getZ() + i * facing.getStepZ()))) {
//                blocked = true;
//                range = i;
//            }
//        }
//        AABB zone = wideAreaBlow ? this.getWideBlowZone(range) : this.getBlowZone(range);
//        List<Entity> inzone = world.getEntitiesOfClass(Entity.class, zone);
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", inzone.size()));
//        for (Entity caught : inzone) {
//            if (this.canBlowEntity(caught)) {
//                double mass = ReikaEntityHelper.getEntityMass(caught);
//                if (caught instanceof LivingEntity)
//                    mass += ReikaEntityHelper.getCarriedMass((LivingEntity) caught);
//                if (caught.motionX < AXISSPEEDCAP && facing.getStepX() != 0) {
//                    double d = caught.getY() - pos.getX();
//                    if (d == 0)
//                        d = 1;
//                    double multiplier = 1 / (d - this.getMaxRange());
//                    if (d - this.getMaxRange() > 12)
//                        multiplier = 0;
//                    if (multiplier > 1 || multiplier < 0)
//                        multiplier = 1;
//                    double base = multiplier * power2 * BASESPEED * (wideAreaBlow ? 0.125 : 1);
//                    double speedstep = Math.max(Math.abs(Math.abs(caught.motionX) + base / (mass * Math.abs(d))), AXISSPEEDCAP);
//                    double a = facing.getStepX() > 0 ? 0.004 : 0;
//                    caught.motionX = facing.getStepX() * speedstep + a;
//                }
//                if (caught.motionY < AXISSPEEDCAP && facing.getStepY() != 0) {
//                    double d = caught.getY() - pos.getY();
//                    if (d == 0)
//                        d = 1;
//                    double multiplier = 1 / (d - this.getMaxRange());
//                    if (d - this.getMaxRange() > 12)
//                        multiplier = 0;
//                    if (multiplier > 1 || multiplier < 0)
//                        multiplier = 1;
//                    double base = multiplier * power2 * BASESPEED * (wideAreaBlow ? 0.125 : 1);
//                    caught.motionY = facing.getStepY() * Math.max(Math.abs(Math.abs(caught.motionY) + base / (mass * Math.abs(d))), AXISSPEEDCAP);
//                }
//                if (caught.motionZ < AXISSPEEDCAP && facing.getStepZ() != 0) {
//                    double d = caught.getZ() - pos.getZ();
//                    if (d == 0)
//                        d = 1;
//                    double multiplier = 1 / (d - this.getMaxRange());
//                    if (d - this.getMaxRange() > 12)
//                        multiplier = 0;
//                    if (multiplier > 1 || multiplier < 0)
//                        multiplier = 1;
//                    double base = multiplier * power2 * BASESPEED * (wideAreaBlow ? 0.125 : 1);
//                    double speedstep = Math.max(Math.abs(Math.abs(caught.motionZ) + base / (mass * Math.abs(d))), AXISSPEEDCAP);
//                    double a = facing.getStepZ() > 0 ? 0.004 : 0;
//                    caught.motionZ = facing.getStepZ() * speedstep + a;
//                }
//            }
//        }
//        this.clearBlocks(world, pos, range);
//        this.spreadFire(world, pos, range);
//    }
//
//    private boolean canBlowEntity(Entity e) {
//        if (e instanceof CustomFanEntity) {
//            CustomFanEntity c = (CustomFanEntity) e;
//            if (c.getBlowPower() > power)
//                return false;
//            double ang = ReikaMathLibrary.py3d(Math.signum(e.motionX) - facing.getStepX(), Math.signum(e.motionY) - facing.getStepY(), Math.signum(e.motionZ) - facing.getStepZ());
//            return !(ang > c.getMaxDeflection());
//        }
//        return true;
//    }
//
//    private void clearBlocks(Level world, BlockPos pos, int range) {
//        int a = 0;
////        if (meta > 1)
////            a = 1;
//        int b = 1 - a;
//        int editx;
//        int edity;
//        int editz;
//        for (int i = 1; i <= range; i++) {
//            editx = pos.getX() + i * facing.getStepX();
//            edity = pos.getY() + i * facing.getStepY();
//            editz = pos.getZ() + i * facing.getStepZ();
//            this.rip2(world, new BlockPos(editx, edity, editz));
//            this.enhanceFinPower(world, new BlockPos(editx, edity, editz));
//
//            if (wideAreaHarvest) {
//                if (facing.getStepY() != 0) {
//                    for (int k = -1; k <= 1; k++) {
//                        for (int j = -1; j <= 1; j++) {
//                            editx = pos.getX() + k;
//                            edity = pos.getY() + i * facing.getStepY();
//                            editx = pos.getZ() + j;
//                            this.rip2(world, new BlockPos(editx, edity, editz));
//                        }
//                    }
//                } else {
//                    Direction left = ReikaDirectionHelper.getLeftBy90(facing);
//                    for (int k = -1; k <= 1; k++) {
//                        for (int j = 0; j <= 2; j++) {
//                            editx = pos.getX() + i * facing.getStepX() + left.getStepX() * k;
//                            edity = pos.getY() + i * facing.getStepY() + j;
//                            editz = pos.getZ() + i * facing.getStepZ() + left.getStepZ() * k;
//                            this.rip2(world, new BlockPos(editx, edity, editz));
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void enhanceFinPower(Level world, BlockPos pos) {
//        MachineRegistry m = MachineRegistry.getMachine(world, pos);
//        if (m == MachineRegistry.COOLINGFIN) {
//            BlockEntityCoolingFin te = (BlockEntityCoolingFin) world.getBlockEntity(pos);
//            int[] tg = te.getTarget();
//            //BlockEntity te2 = world.getBlockEntity(tg[0], tg[1], tg[2]);
//            //if (te2 instanceof TemperatureTE && world.getDayTime()%20 == 0) {
//            ReikaParticleHelper.CLOUD.spawnAroundBlock(world, pos, 1);
//            if (world.getDayTime() % 20 == 0) {
//                int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//                if (te.getTemperature() > Tamb)
//                    te.addTemperature(-(int) Math.min(10, 1 + power / 32768));
//                //if (((TemperatureTE) te2).getTemperature() > Tamb)
//                //	((TemperatureTE) te2).addTemperature(-1);
//            }
//        }
//    }
//
//    public void rip2(Level world, BlockPos pos) {
//        Block id = world.getBlockState(pos).getBlock();
//		/*
//		if (id instanceof BlowableCrop && omega >= HARVESTSPEED) {
//			float sp = ((BlowableCrop)id).getHarvestingSpeed();
//			if (ReikaRandomHelper.doWithChance(0.015*sp))
//				this.harvest(world, pos, (BlowableCrop)id);
//			return;
//		}
//		 */
//        //ReikaJavaLibrary.pConsole(id+":"+ModCropList.getModCrop(id, meta), id != Blocks.AIR);
//        boolean crop = ReikaCropHelper.isCrop(id) || ModCropList.isModCrop(id, meta);
//        if (id != Blocks.SNOW && id != Blocks.COBWEB && id != Blocks.LEAVES && id != Blocks.LEAVES2 && id != Blocks.TALLGRASS && id != Blocks.FIRE && !crop)
//            return;
//        int c = this.getHarvestingRand();
//        if (id == Blocks.TALL_GRASS)
//            c /= 3;
//        c = Math.max(1, c);
//        if (DragonAPI.rand.nextInt(c) > 0)
//            return;
//        if (id == Blocks.COBWEB && omega < WEBSPEED)
//            return;
//        if ((id == Blocks.LEAVES || id == Blocks.LEAVES2) && omega < LEAFSPEED)
//            return;
//        if (id == Blocks.TALL_GRASS && omega < GRASSSPEED)
//            return;
//        if (id == Blocks.FIRE && omega < FIRESPEED)
//            return;
//        if (id == Blocks.SNOW && omega < FIRESPEED)
//            return;
//        if (crop && omega < HARVESTSPEED)
//            return;
//        if (crop) {
//            this.harvest(world, pos, id);
//            return;
//        }
//        this.dropBlocks(world, pos, id);
//        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
//    }
//
//    private int getHarvestingRand() {
//        return Math.max(50, 600 - 25 * ReikaMathLibrary.logbase2(omega));
//    }
//
//    /*
//    private void harvest(Level world, BlockPos pos, BlowableCrop b) {
//        if (b.isReadyToHarvest(world, pos)) {
//            ArrayList<ItemStack> li = b.getHarvestProducts(world, pos);
//            if (li != null)
//                ReikaItemHelper.dropItems(world, pos.getX()+0.5, pos.getY()+0.5, pos.getZ()+0.5, li);
//            b.setPostHarvest(world, pos);
//            MinecraftForge.EVENT_BUS.post(new FanHarvestEvent(this, pos));
//        }
//    }
//     */
//    //Meta is block meta, not fan meta
//    private void harvest(Level world, BlockPos pos, Block id) {
//        CropType crop = ReikaCropHelper.getCrop(id);
//        if (crop == null)
//            crop = ModCropList.getModCrop(id, meta);
//        if (crop != null && crop.isRipe(world, pos)/* && (omega > HARVESTSPEED*4 || !crop.getShape().isSolid())*/) {
//            if (crop.destroyOnHarvest()) {
//                ArrayList<ItemStack> li = id.getDrops(world, pos, meta, 0);
//                ReikaItemHelper.dropItems(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, li);
//                world.setBlockToAir(pos);
//            } else {
//                ArrayList<ItemStack> li = crop.getDrops(world, pos, 0);
//                CropMethods.removeOneSeed(crop, li);
//                ReikaItemHelper.dropItems(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, li);
//                crop.setHarvested(world, pos);
//            }
//        }
//        MinecraftForge.EVENT_BUS.post(new FanHarvestEvent(this, pos));
//    }
//
//    private void dropBlocks(Level world, BlockPos pos, Block id) {
//        if (id != Blocks.AIR)
//            ReikaItemHelper.dropItems(world, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, id.getDrops(world, pos, meta, 0));
//        world.setBlockAndUpdate(pos, id.defaultBlockState());
//    }
//
//    public AABB getBlowZone(int range) {
//        return ReikaAABBHelper.getBeamBox(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), facing, range);//.expand(0.0, 0.0, 0.0);
//    }
//
//    public AABB getWideBlowZone(int range) {
//        AABB box = this.getBlowZone(range);
//        int ex = ReikaMathLibrary.isValueInsideBoundsIncl(0, 1, meta) ? 0 : 1;
//        int ey = ReikaMathLibrary.isValueInsideBoundsIncl(4, 5, meta) ? 0 : 1;
//        int ez = ReikaMathLibrary.isValueInsideBoundsIncl(2, 3, meta) ? 0 : 1;
//        return box.expandTowards(ex, ey, ez);
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        if (power < MINPOWER)
//            return;
//        phi += 3 * ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FAN;
//    }
//
//    @Override
//    public int getMaxRange() {
//        return distancelimit;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    public void onEMP() {
//    }
//
//    @Override
//    public void upgrade(ItemStack item) {
//        if (!wideAreaBlow && ReikaItemHelper.matchStacks(item, RotaryItems.diffuser)) {
//            wideAreaBlow = true;
//        }
//    }
//
//    @Override
//    public boolean canUpgradeWith(ItemStack item) {
//        return ReikaItemHelper.matchStacks(item, RotaryItems.diffuser);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        NBT.putBoolean("wideh", wideAreaHarvest);
//        NBT.putBoolean("wideb", wideAreaBlow);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        wideAreaBlow = NBT.getBoolean("wideb");
//        wideAreaHarvest = NBT.getBoolean("wideh");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public void breakBlock() {
//        if (wideAreaBlow) {
//            ReikaItemHelper.dropItem(level, worldPosition, RotaryItems.diffuser);
//        }
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
