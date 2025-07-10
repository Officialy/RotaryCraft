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
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.util.Mth;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.phys.AABB;
//import net.minecraftforge.fluids.FluidStack;
//
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.instantiable.Interpolation;
//import reika.dragonapi.libraries.ReikaAABBHelper;
//import reika.dragonapi.libraries.ReikaDirectionHelper;
//import reika.dragonapi.libraries.io.ReikaSoundHelper;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.java.ReikaRandomHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Power.ShaftMerger;
//import reika.rotarycraft.auxiliary.PowerSourceList;
//import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
//import reika.rotarycraft.base.blockentity.BlockEntityEngine;
//import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
//import reika.rotarycraft.registry.*;
//
//import java.util.ArrayList;
//import java.util.List;
//
//public class BlockEntityHydroEngine extends BlockEntityEngine {
//
//    private static final Interpolation streamWidthFactor = new Interpolation(false);
//    private static final Interpolation streamDepthFactor = new Interpolation(false);
//    private static final Interpolation streamFallHeights = new Interpolation(false);
//
//    static {
//        streamDepthFactor.addPoint(1, 0.5);
//        streamDepthFactor.addPoint(2, 1 / Math.sqrt(2));
//        streamDepthFactor.addPoint(3, 1);
//        streamDepthFactor.addPoint(4, 1.25);
//        streamDepthFactor.addPoint(5, 1.5);
//        streamDepthFactor.addPoint(6, 2);
//        streamDepthFactor.addPoint(7, 3);
//        streamDepthFactor.addPoint(8, 5);
//
//        streamWidthFactor.addPoint(1, 1 / 3D);
//        streamWidthFactor.addPoint(2, 1 / Math.sqrt(2));
//        streamWidthFactor.addPoint(3, 0.8);
//        streamWidthFactor.addPoint(4, 1);
//        streamWidthFactor.addPoint(5, 1);
//        streamWidthFactor.addPoint(6, 1.2);
//        streamWidthFactor.addPoint(7, 1.5);
//        streamWidthFactor.addPoint(8, 2);
//
//        int h = RotaryConfig.COMMON.HYDROSTREAMFALLMAX.get();
//        streamFallHeights.addPoint(h, 64);
//        for (int i = 1; i <= 5; i++) {
//            streamFallHeights.addPoint(h + i, 64 + i * 2);
//        }
//        int d = 64;
//        int s = 5;
//        while (h > 0 && d > 6) {
//            h--;
//            d -= s;
//            d = Math.max(d, 6);
//            s += 3 + Math.max(0, s / 6 - 1);
//            streamFallHeights.addPoint(h, d);
//        }
//        streamFallHeights.addPoint(0, 6);
//    }
//
//    public boolean failed;
//    private boolean bedrock;
//    private Fluid fluidType;
//    private double fluidFallSpeed;
//    private StreamPowerData streamData = null;
//
//    public BlockEntityHydroEngine(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    @Override
//    protected void consumeFuel() {
//
//    }
//
//    @Override
//    protected void internalizeFuel() {
//
//    }
//
//    public boolean isBedrock() {
//        return bedrock;
//    }
//
//    @Override
//    public void setDataFromPlacer(ItemStack is) {
//        if (is.getTag() != null) {
//            bedrock = is.getTag().getBoolean("bed");
//        }
//    }
//
//    @Override
//    protected boolean getRequirements(Level world, BlockPos pos) {
//        boolean hasLube = !lubricant.isEmpty() && lubricant.getActualFluid().getFluid().equals(RotaryFluids.LUBRICANT.get().getFlowing());
//        if (hasLube)
//            this.distributeLubricant(world, pos);
//        else
//            return false;
//
//        if (this.doesBlockObstructBlades(world, pos.above())) {
//            omega = 0;
//            return false;
//        }
//        if (this.doesBlockObstructBlades(world, pos.below())) {
//            omega = 0;
//            return false;
//        }
//
//        int[] pos = this.getWaterColumnPos();
//
//        for (int i = -1; i <= 1; i++) {
//            if (this.doesBlockObstructBlades(world, 2 * x - pos[0], y + i, 2 * z - pos[1])) {
//                omega = 0;
//                return false;
//            }
//        }
//
//        //ReikaJavaLibrary.pConsole(Block.getIdFromBlock(world.getBlock(x, y-1, z))+":"+world.getBlockMetadata(pos)+" > "+world.getBlock(x, y-1, z));
//        Block b = world.getBlockState(worldPosition.below()).getBlock();
//        if (InterfaceCache.STREAM.instanceOf(b)) {
//            return this.handleStream(world, pos, x, y - 1, z, meta, b, pos);
//        } else {
//            streamData = null;
//
//            if (!ReikaWorldHelper.isLiquidAColumn(world, pos[0], y, pos[1]))
//                return false;
//
//            this.getFluidData(world, pos, pos);
//
//            if (fluidType != null) {
//                if (fluidType.getAttributes().getTemperature() >= 900) {
//                    if (ReikaRandomHelper.doWithChance(2)) {
//                        world.setBlockToAir(pos);
//                        boolean lube = !lubricant.isEmpty();
//                        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, lube ? 3 : 2, lube, true);
//                    }
//                }
//                return !fluidType.getAttributes().isLighterThanAir() && fluidType.getAttributes().getDensity() > 0;
//            }
//            return true;
//        }
//    }
//
//    private boolean isStreamFall(Level world, BlockPos pos) {
//        while (world.getBlockState(pos).getBlock() == Blocks.WATER) {
//            dy--;
//        }
//        return InterfaceCache.STREAM.instanceOf(world.getBlockState(pos).getBlock());
//    }
//
//    private boolean handleStream(Level world, BlockPos pos, int wx, int wy, int wz, Block b, BlockPos pos2) {
//        if (streamData != null) {
//            this.checkForOtherStreamEngines(world, pos);
//            if (streamData.valid(world))
//                return true;
//            else
//                streamData = null;
//        }
//        FixedFlowBlock ff = (FixedFlowBlock) b;
//        double vel = this.getUsefulVelocity(world, ff.dx(), ff.dz());
//        if (vel > 0) {
//            streamData = new StreamPowerData(this, wx, wy, wz, vel);
//            streamData.calculatePower(world);
//            this.checkForOtherStreamEngines(world, pos);
//            return true;
//        }
//        streamData = null;
//        return false;
//    }
//
//    public void invalidateStream() {
//        streamData = null;
//    }
//
//    //    @Override
//    protected void onAdjacentBlockUpdate() {
//        this.invalidateStream();
//    }
//
//    private void checkForOtherStreamEngines(Level world, BlockPos pos) {
//        if (this.getWriteDirection() == null)
//            return;
//        boolean continueForward = true;
//        boolean continueBackward = true;
//        for (int d = 1; d < 4; d++) {
//            if (continueForward) {
//                continueForward = false;
//                int dx = x + this.getWriteDirection().getStepX() * d;
//                int dz = z + this.getWriteDirection().getStepZ() * d;
//                if (MachineRegistry.getMachine(world, dx, y, dz) == MachineRegistry.ENGINE) {
//                    BlockEntity te = world.getBlockEntity(dx, y, dz);
//                    if (te instanceof BlockEntityHydroEngine) {
//                        continueForward = true;
//                        BlockEntityHydroEngine th = (BlockEntityHydroEngine) te;
//                        if (th.streamData != null && th.streamData.omega > streamData.omega) {
//                            streamData = th.streamData;
//                        }
//                    }
//                }
//            }
//            if (continueBackward) {
//                continueBackward = false;
//                int dx = x - this.getWriteDirection().getStepX() * d;
//                int dz = z - this.getWriteDirection().getStepZ() * d;
//                if (MachineRegistry.getMachine(world, dx, y, dz) == MachineRegistry.ENGINE) {
//                    BlockEntity te = world.getBlockEntity(dx, y, dz);
//                    if (te instanceof BlockEntityHydroEngine) {
//                        continueBackward = true;
//                        BlockEntityHydroEngine th = (BlockEntityHydroEngine) te;
//                        if (th.streamData != null && th.streamData.omega > streamData.omega) {
//                            streamData = th.streamData;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private double getUsefulVelocity(Level world, int dx, int dz) {
//        double vx = Math.abs(dx / 2);
//        double vz = Math.abs(dz / 2); // div by 2 since speeds are from 0-2
//        switch (meta) {
//            case 0:
//                return vz;
//            case 1:
//                return vz;
//            case 2:
//                return vx;
//            case 3:
//                return vx;
//            default:
//                return 0;
//        }
//    }
//
//    private void distributeLubricant(Level world, BlockPos pos) {
//        for (int i = 2; i < 6; i++) {
//            Direction dir = dirs[i];
//            if (dir == this.getWriteDirection() || dir.getOpposite() == this.getWriteDirection()) {
//                int dx = x + dir.getStepX();
//                int dy = y + dir.getStepY();
//                int dz = z + dir.getStepZ();
//                MachineRegistry m = MachineRegistry.getMachine(world, dx, dy, dz);
//                if (m == MachineRegistry.ENGINE) {
//                    BlockEntityEngine eng = (BlockEntityEngine) getAdjacentBlockEntity(dir);
//                    if (eng instanceof BlockEntityHydroEngine) {
//                        BlockEntityHydroEngine hy = (BlockEntityHydroEngine) eng;
//                        int it = hy.lubricant.getLevel();
//                        int dL = lubricant.getLevel() - it;
//                        if (dL > 3) {
//                            hy.lubricant.addLiquid(dL / 4, Fluids.getFluid("rc lubricant"));
//                            lubricant.removeLiquid(dL / 4);
//                        }
//                    }
//                } else if (m == MachineRegistry.RESERVOIR) {
//                    BlockEntityReservoir te = (BlockEntityReservoir) getAdjacentBlockEntity(dir);
//                    if (!lubricant.isEmpty() && te.canAcceptFluid(Fluids.getFluid("rc lubricant"))) {
//                        int amt = Math.min(this.getLube(), BlockEntityReservoir.CAPACITY - te.getLevel());
//                        if (amt > 0) {
//                            te.addLiquid(amt, Fluids.getFluid("rc lubricant"));
//                            lubricant.removeLiquid(amt);
//                        }
//                    }
//                }
//            }
//        }
//        if (!failed && !lubricant.isEmpty() && omega > 0) {
//            if (world.getDayTime() % 10 == 0)
//                lubricant.removeLiquid(1);
//        }
//    }
//
//    private boolean doesBlockObstructBlades(Level world, BlockPos pos) {
//        return !failed && !ReikaWorldHelper.softBlocks(world, pos);
//    }
//
//    private BlockPos[] getWaterColumnPos() {
//        BlockPos[] pos = {BlockPos.of(worldPosition.getX()), BlockPos.of(worldPosition.getZ())};
//        switch (this.getBlockMetadata()) {
//            case 0:
//                pos[1] += -1;
//                break;
//            case 1:
//                pos[1] += 1;
//                break;
//            case 2:
//                pos[0] += 1;
//                break;
//            case 3:
//                pos[0] += -1;
//                break;
//        }
//        return pos;
//    }
//
//    private void getFluidData(Level world, BlockPos[] pos, int[] pos2) {
//        Fluid f = ReikaWorldHelper.getFluid(world, pos[0], y, pos[1]);
//        fluidType = f;
//        if (f == null || f.getAttributes().isLighterThanAir() || f.getAttributes().getDensity() <= 0) {
//            fluidFallSpeed = 0;
//            return;
//        }
//        double grav = this.getGravity(world);
//        int top = ReikaWorldHelper.findFluidSurface(world, pos[0], y, pos[1], fluidType);
//        double dy = top - y;
//        if (dy >= 2 && this.isStreamFall(world, pos[0], y, pos[1])) {
//            dy = streamFallHeights.getValue(dy);
//        }
//        dy = Math.pow(dy, 1.5) / 32;
//        fluidFallSpeed = 0.92 * Math.sqrt(2 * grav * dy) / Math.max(0.25, Math.pow(f.getViscosity() / 1000, 0.375));
//    }
//
//    public int getWaterfallHeightForDisplay() {
//        BlockPos[] pos = this.getWaterColumnPos();
//        Fluid f = ReikaWorldHelper.getFluid(level, pos[0], yCoord, pos[1]);
//        if (f == null || f.isGaseous() || f.getDensity() <= 0) {
//            return 0;
//        }
//        return Mth.ceil(ReikaWorldHelper.findFluidSurface(level, pos[0], yCoord, pos[1])) - yCoord;
//    }
//
//    private int getEffectiveSpeed(Level world, BlockPos pos) {
//        if (this.isStreamPowered())
//            return streamData.omega;
//        double omg = fluidFallSpeed * 2;
//        return Math.min((int) omg, type.getSpeed());
//    }
//
//    private int getEffectiveTorque(Level world, BlockPos pos) {
//        if (this.isStreamPowered())
//            return streamData.torque;
//        double mdot = Math.min(12000, fluidType.getDensity()) * fluidFallSpeed; //*1 since area is 1m^2
//        double tau = 0.0625 * mdot * fluidFallSpeed;
//        return Math.min((int) tau, type.getTorque());
//    }
//
//    private void dealPanelDamage(Level world, BlockPos pos) {
//        int a = 0;
//        int b = 0;
//        if (meta < 2)
//            b = 1;
//        else
//            a = 1;
//        AABB box = AABB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(a, 1, b);
//        List<LivingEntity> in = world.getEntities(LivingEntity.class, box);
//        RotaryCraft.hydrokinetic.lastMachine = this;
//        for (LivingEntity ent : in) {
//            ent.hurt(RotaryCraft.hydrokinetic, 1);
//        }
//    }
//
//    private double getGravity(Level world) {
//        double grav = ReikaPhysicsHelper.g;
//        grav += PlanetDimensionHandler.getExtraGravity(world) * 20; //*20 since tick/s
//        return grav;
//    }
//
//    private boolean isPartOfArray() {
//        return this.isBackEndOfArray() || this.isFrontOfArray();
//    }
//
//    public boolean isBackEndOfArray() {
//        MachineRegistry to = this.getMachine(write);
//        if (to == MachineRegistry.ENGINE) {
//            BlockEntityEngine te = (BlockEntityEngine) getAdjacentBlockEntity(write);
//            return te.getEngineType() == EngineType.HYDRO && !((BlockEntityHydroEngine) te).failed;
//        }
//        return false;
//    }
//
//    public boolean isFrontOfArray() {
//        MachineRegistry from = MachineRegistry.getMachine(level, backx, worldPosition.getY(), backz);
//        MachineRegistry to = this.getMachine(write);
//        if (from == MachineRegistry.ENGINE && to != MachineRegistry.ENGINE) {
//            BlockEntityEngine te = (BlockEntityEngine) level.getBlockEntity(backx, yCoord, backz);
//            return te.getEngineType() == EngineType.HYDRO;
//        }
//        BlockEntity te = getAdjacentBlockEntity(write);
//        if (te instanceof BlockEntityHydroEngine) {
//            return ((BlockEntityHydroEngine) te).failed;
//        }
//        return false;
//    }
//
//    private int getArrayTorqueMultiplier() {
//        ArrayList<BlockEntityHydroEngine> li = new ArrayList<>();
//        int size = 1;
//        BlockEntity te = getAdjacentBlockEntity(write.getOpposite());
//        while (te instanceof BlockEntityHydroEngine && te != this && !li.contains(te)) {
//            BlockEntityHydroEngine eng = (BlockEntityHydroEngine) te;
//            li.add(eng);
//            if (eng.getRequirements(level, eng.xCoord, eng.yCoord, eng.zCoord, eng.getBlockMetadata())) {
//                if (eng.omega == omega && !eng.failed) {
//                    //float fac = eng.getHydroFactor(level, xyz[0], xyz[1], xyz[2], true);
//                    size++;
//                    te = getAdjacentBlockEntity(eng.write.getOpposite());
//                } else {
//                    ReikaParticleHelper.CRITICAL.spawnAroundBlock(level, eng.xCoord, eng.yCoord, eng.zCoord, 5);
//                    if (DragonAPI.rand.nextInt(3) == 0)
//                        ReikaSoundHelper.playSoundAtBlock(level, eng.xCoord, eng.yCoord, eng.zCoord, "mob.blaze.hit");
//                    break;
//                }
//            }
//        }
//        return size;
//    }
//
//    @Override
//    protected void playSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
//        soundtick++;
//        if (this.isMuffled(world, pos)) {
//            volume *= 0.3125F;
//        }
//
//        if (soundtick < this.getSoundLength(1F / pitchMultiplier) && soundtick < 2000)
//            return;
//        soundtick = 0;
//
//        if (this.isFrontOfArray() || !this.isPartOfArray())
//            SoundRegistry.HYDRO.playSoundAtBlock(world, pos, 1F * volume, 0.9F * pitchMultiplier);
//    }
//
//    @Override
//    public int getFuelLevel() {
//        return 0;
//    }
//
//    @Override
//    protected int getMaxSpeed(Level world, BlockPos pos) {
//        return Math.max(1, this.getEffectiveSpeed(world, pos));
//    }
//
//    @Override
//    protected int getGenTorque(Level world, BlockPos pos) {
//        if (failed)
//            return 1;
//        int torque = this.getEffectiveTorque(world, pos) * this.getArrayTorqueMultiplier();
//        double ratio = (double) torque / EngineType.HYDRO.getTorque();
//        int r = bedrock ? 16 : 4;
//        if (ratio > r) {
//            this.fail(world, pos);
//        }
//        return torque;
//    }
//
//    private void fail(Level world, BlockPos pos) {
//        ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.break");
//        ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.explode", 0.2F, 0.5F);
//		/*
//		BlockEntity te = this.getAdjacentBlockEntity(write.getOpposite());
//		while (te instanceof BlockEntityHydroEngine) {
//			BlockEntityHydroEngine eng = (BlockEntityHydroEngine)te;
//			eng.getGenTorque(world, eng.xCoord, eng.yCoord, eng.zCoord, eng.getBlockMetadata());
//			te = eng.getAdjacentBlockEntity(write.getOpposite());
//		}*/
//        failed = true;
//    }
//
//    @Override
//    protected void affectSurroundings(Level world, BlockPos pos) {
//        this.dealPanelDamage(world, pos);
//        this.spawnParticles(world, pos);
//        if (failed) {
//            Direction dir = this.getWriteDirection();
//            Direction left = ReikaDirectionHelper.getLeftBy90(dir);
//            for (int i = -1; i <= y; i++) {
//                int dx = x + left.getStepX();
//                int dy = y + i;
//                int dz = z + left.getStepZ();
//                if (!InterfaceCache.STREAM.instanceOf(world.getBlock(dx, dy, dz)))
//                    ReikaWorldHelper.dropAndDestroyBlockAt(world, dx, dy, dz, null, false, true);
//            }
//            ReikaWorldHelper.dropAndDestroyBlockAt(world, x, y + 1, z, null, false, true);
//            if (!InterfaceCache.STREAM.instanceOf(world.getBlock(x, y - 1, z)))
//                ReikaWorldHelper.dropAndDestroyBlockAt(world, x, y - 1, z, null, false, true);
//        }
//    }
//
//    private void spawnParticles(Level world, BlockPos pos) {
//        BlockPos[] xz = this.getWaterColumnPos();
//        ReikaParticleHelper.RAIN.spawnAroundBlock(world, pos, 16);
//        ReikaParticleHelper.RAIN.spawnAroundBlock(world, x - (xz[0] - x), y, z - (xz[1] - z), 16);
//        if (this.isStreamPowered()) {
//
//        } else {
//            ReikaParticleHelper.RAIN.spawnAroundBlock(world, xz[0], y, xz[1], 16);
//        }
//        if (failed) {
//            if (DragonAPI.rand.nextInt(5) == 0)
//                ReikaSoundHelper.playSoundAtBlock(world, pos, "mob.blaze.hit");
//            ReikaParticleHelper.CRITICAL.spawnAroundBlockWithOutset(world, pos, 3, 0.25);
//        }
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        failed = NBT.getBoolean("fail");
//        bedrock = NBT.getBoolean("bedrock");
//
//        if (NBT.contains("stream"))
//            streamData = StreamPowerData.load(NBT.getCompound("stream"));
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
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
//        NBT.putBoolean("fail", failed);
//        NBT.putBoolean("bedrock", bedrock);
//
//        if (streamData != null) {
//            NBT.put("stream", streamData.saveAdditional());
//        }
//    }
//
//    public void makeBedrock() {
//        if (!bedrock) {
//            bedrock = true;
//            failed = false;
//        }
//    }
//
//    @Override
//    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
//        PowerSourceList psl = super.getPowerSources(io, caller);
//        ArrayList<BlockEntityHydroEngine> li = new ArrayList<>();
//        ArrayList<BlockEntityHydroEngine> li2 = new ArrayList<>();
//        BlockEntity te = getAdjacentBlockEntity(write.getOpposite());
//        while (te instanceof BlockEntityHydroEngine && te != this && !li2.contains(te)) {
//            BlockEntityHydroEngine eng = (BlockEntityHydroEngine) te;
//            li2.add(eng);
//            if (eng.getRequirements(level, eng.xCoord, eng.yCoord, eng.zCoord, eng.getBlockMetadata())) {
//                if (eng.omega == omega && !eng.failed) {
//                    li.add(eng);
//                    te = getAdjacentBlockEntity(eng.write.getOpposite());
//                }
//
//            }
//        }
//        for (int i = 0; i < li.size(); i++) {
//            psl.addSource(li.get(i));
//        }
//        return psl;
//    }
//
//    @Override
//
//    public AABB getRenderBoundingBox() {
//        return ReikaAABBHelper.getBlockAABB(worldPosition).expandTowards(1, 1, 1);
//    }
//
//    public boolean isStreamPowered() {
//        return streamData != null;
//    }
//
//    public ArrayList<String> getStreamData() {
//        return streamData.getDataDisplay();
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
//        return 1;
//    }
//
//    
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return null;
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 12000;
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
//
//    @Override
//    public int getAmbientTemperature() {
//        return 0;
//    }
//
//    private static class StreamPowerData {
//
//        private static final long startTime = ReikaJavaLibrary.getLaunchTime();
//        private final BlockPos engineSource;
//        private final BlockPos waterSource;
//        private final double normalVelocity;
//        private final long lastFullCheck = -1;
//        private long gameInstance = startTime;
//        private int localDepth;
//        private int localWidth;
//        private double powerFactor;
//        private int torque;
//        private int omega;
//
//        private StreamPowerData(BlockEntityHydroEngine te, int wx, int wy, int wz, double vel) {
//            this(te.worldPosition, new BlockPos(wx, wy, wz), vel);
//        }
//
//        private StreamPowerData(BlockPos c1, BlockPos c2, double vel) {
//            engineSource = c1;
//            waterSource = c2;
//            normalVelocity = vel;
//        }
//
//        private static StreamPowerData load(CompoundTag tag) {
//            if (tag == null || tag.hasNoTags())
//                return null;
//            BlockPos eng = BlockPos.load("engine", tag);
//            BlockPos water = BlockPos.load("water", tag);
//            double vel = tag.getDouble("velocity");
//            StreamPowerData ret = new StreamPowerData(eng, water, vel);
//            ret.localDepth = tag.getInt("depth");
//            ret.localWidth = tag.getInt("width");
//            ret.powerFactor = tag.getDouble("power");
//            ret.torque = tag.getInt("torque");
//            ret.omega = tag.getInt("omega");
//            ret.gameInstance = tag.getLong("time");
//            return ret;
//        }
//
//        private void calculateGeography(Level world) {
//            localWidth = 8;
//            localDepth = 8;
//            for (int i = 0; i < 6; i++) {
//                Direction dir = Direction.values()[i];
//                if (dir != Direction.UP) {
//                    for (int d = 1; d < (dir.getStepY() == 0 ? localWidth : localDepth); d++) {
//                        int dx = waterSource.getX() + dir.getStepX() * d;
//                        int dy = waterSource.getY() + dir.getStepY() * d;
//                        int dz = waterSource.getZ() + dir.getStepZ() * d;
//                        Block b = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
//                        if (!(b instanceof FixedFlowBlock)) {
//                            if (dir.getStepY() == 0)
//                                localWidth = d - 1;
//                            else
//                                localDepth = d - 1;
//                            break;
//                        }
//                    }
//                }
//            }
//        }
//
//        private void calculatePower(Level world) {
//            this.calculateGeography(world);
//            powerFactor = Math.min(1, this.getStreamSpeedScale());
//            omega = (int) (EngineType.HYDRO.getSpeed() * powerFactor);
//            torque = (int) (EngineType.HYDRO.getTorque() * powerFactor);
//        }
//
//        private boolean valid(Level world) {
//            return gameInstance == startTime && world.getBlockEntity(engineSource) instanceof BlockEntityHydroEngine && world.getBlockState(waterSource).getBlock() instanceof FixedFlowBlock;
//        }
//
//        private double getStreamSpeedScale() {
//            return Math.sqrt(streamDepthFactor.getValue(localDepth + 1) * streamWidthFactor.getValue(localWidth + 1) * normalVelocity);
//        }
//
//        private CompoundTag saveAdditional() {
//            CompoundTag tag = new CompoundTag();
//            tag.putInt("depth", localDepth);
//            tag.putInt("width", localWidth);
//            tag.putDouble("power", powerFactor);
//            tag.putDouble("velocity", normalVelocity);
//            tag.putInt("torque", torque);
//            tag.putInt("omega", omega);
//            engineSource.saveAdditional("engine", tag);
//            waterSource.saveAdditional("water", tag);
//            tag.putLong("time", gameInstance);
//            return tag;
//        }
//
//        public ArrayList<String> getDataDisplay() {
//            ArrayList<String> li = new ArrayList<>();
//            li.add("Width: " + (localWidth + 1) + "m");
//            li.add("Depth: " + (localDepth + 1) + "m");
//            li.add("Local Flow Velocity: " + String.format("%.0f", normalVelocity * 100) + "%%");
//            li.add("Efficiency: " + String.format("%.2f", powerFactor * powerFactor * 100) + "%%");
//            return li;
//        }
//    }
//}
