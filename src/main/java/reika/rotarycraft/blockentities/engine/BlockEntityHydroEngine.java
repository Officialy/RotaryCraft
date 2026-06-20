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

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraft.world.phys.AABB;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.ReikaAABBHelper;
import reika.dragonapi.libraries.ReikaDirectionHelper;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaPhysicsHelper;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.registry.*;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityHydroEngine extends BlockEntityEngine {

    public boolean failed;
    private boolean bedrock;
    private Fluid fluidType;
    private double fluidFallSpeed;

    public BlockEntityHydroEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.HYDRO_ENGINE.get(), pos, state, false, true, false, false);
        type = EngineType.HYDRO;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.HYDRO_ENGINE;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.HYDRO_ENGINE.get();
    }

    @Override
    protected String getTEName() {
        return "hydroengine";
    }

    @Override
    protected void consumeFuel() {
    }

    @Override
    protected void internalizeFuel() {
    }

    public boolean isBedrock() {
        return bedrock;
    }

    public void setDataFromPlacer(ItemStack is) {
        if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() != null) {
            bedrock = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getBooleanOr("bed", false);
        }
    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        boolean hasLube = !lubricant.isEmpty() && lubricant.getActualFluid().getFluid().equals(RotaryFluids.LUBRICANT.get());
        if (hasLube)
            this.distributeLubricant(world, pos);
        else
            return false;

        if (this.doesBlockObstructBlades(world, pos.above())) {
            omega = 0;
            return false;
        }
        if (this.doesBlockObstructBlades(world, pos.below())) {
            omega = 0;
            return false;
        }

        BlockPos waterCol = this.getWaterColumnPos();
        Direction waterDir = ReikaDirectionHelper.getLeftBy90(write);
        Direction oppositeWater = waterDir.getOpposite();

        for (int i = -1; i <= 1; i++) {
            BlockPos checkPos = new BlockPos(
                    worldPosition.getX() + oppositeWater.getStepX(),
                    worldPosition.getY() + i,
                    worldPosition.getZ() + oppositeWater.getStepZ());
            if (this.doesBlockObstructBlades(world, checkPos)) {
                omega = 0;
                return false;
            }
        }

        if (!isLiquidColumn(world, waterCol))
            return false;

        this.getFluidData(world, waterCol);

        if (fluidType != null) {
            if (fluidType.getFluidType().getTemperature() >= 900) {
                if (ReikaRandomHelper.doWithChance(2)) {
                    world.setBlock(waterCol, Blocks.AIR.defaultBlockState(), 3);
                    boolean lube = !lubricant.isEmpty();
                    world.explode(null, waterCol.getX() + 0.5, waterCol.getY() + 0.5, waterCol.getZ() + 0.5, lube ? 3 : 2, Level.ExplosionInteraction.TNT);
                }
            }
            return !fluidType.getFluidType().isLighterThanAir() && fluidType.getFluidType().getDensity() > 0;
        }
        return true;
    }

    private void distributeLubricant(Level world, BlockPos pos) {
        Direction[] horizontals = {Direction.NORTH, Direction.SOUTH, Direction.EAST, Direction.WEST};
        for (Direction dir : horizontals) {
            if (dir == this.getWriteDirection() || dir.getOpposite() == this.getWriteDirection()) {
                BlockPos neighbor = worldPosition.relative(dir);
                MachineRegistry m = MachineRegistry.getMachine(world, neighbor);
                if (m == MachineRegistry.HYDRO_ENGINE) {
                    BlockEntity adj = getAdjacentBlockEntity(dir);
                    if (adj instanceof BlockEntityHydroEngine hy) {
                        int it = hy.getLube();
                        int dL = this.getLube() - it;
                        if (dL > 3) {
                            hy.addLubricant(dL / 4);
                            this.removeLubricant(dL / 4);
                        }
                    }
                } else if (m == MachineRegistry.RESERVOIR) {
                    BlockEntity adj = getAdjacentBlockEntity(dir);
                    if (adj instanceof BlockEntityReservoir te) {
                        if (!lubricant.isEmpty() && te.canAcceptFluid(RotaryFluids.LUBRICANT.get())) {
                            int amt = Math.min(this.getLube(), BlockEntityReservoir.CAPACITY - te.getFluid().getAmount());
                            if (amt > 0) {
                                te.addLiquid(amt, RotaryFluids.LUBRICANT.get());
                                this.removeLubricant(amt);
                            }
                        }
                    }
                }
            }
        }
        if (!failed && !lubricant.isEmpty() && omega > 0) {
            if (level.getGameTime() % 10 == 0)
                lubricant.removeLiquid(1);
        }
    }

    private boolean doesBlockObstructBlades(Level world, BlockPos pos) {
        return !failed && !ReikaWorldHelper.softBlocks(world, pos);
    }

    private BlockPos getWaterColumnPos() {
        if (write == null)
            return worldPosition;
        return worldPosition.relative(ReikaDirectionHelper.getLeftBy90(write));
    }

    private static boolean isLiquidColumn(Level world, BlockPos pos) {
        FluidState fs = world.getFluidState(pos);
        if (fs.isEmpty())
            return false;
        FluidState above = world.getFluidState(pos.above());
        return !above.isEmpty();
    }

    private static int findFluidSurface(Level world, BlockPos base) {
        int y = base.getY();
        while (y < world.getMaxY()) {
            FluidState fs = world.getFluidState(new BlockPos(base.getX(), y + 1, base.getZ()));
            if (fs.isEmpty())
                break;
            y++;
        }
        return y;
    }

    private void getFluidData(Level world, BlockPos waterCol) {
        FluidState fs = world.getFluidState(waterCol);
        if (fs.isEmpty()) {
            fluidType = null;
            fluidFallSpeed = 0;
            return;
        }
        Fluid f = fs.getType();
        fluidType = f;
        if (f.getFluidType().isLighterThanAir() || f.getFluidType().getDensity() <= 0) {
            fluidFallSpeed = 0;
            return;
        }
        double grav = this.getGravity(world);
        int top = findFluidSurface(world, waterCol);
        double dy = top - worldPosition.getY();
        dy = Math.pow(dy, 1.5) / 32;
        double viscosity = f.getFluidType().getViscosity();
        fluidFallSpeed = 0.92 * Math.sqrt(2 * grav * dy) / Math.max(0.25, Math.pow(viscosity / 1000, 0.375));
    }

    public int getWaterfallHeightForDisplay() {
        BlockPos waterCol = this.getWaterColumnPos();
        FluidState fs = level.getFluidState(waterCol);
        if (fs.isEmpty())
            return 0;
        Fluid f = fs.getType();
        if (f.getFluidType().isLighterThanAir() || f.getFluidType().getDensity() <= 0)
            return 0;
        return findFluidSurface(level, waterCol) - worldPosition.getY();
    }

    private int getEffectiveSpeed(Level world, BlockPos pos) {
        double omg = fluidFallSpeed * 2;
        return Math.min((int) omg, type.getSpeed());
    }

    private int getEffectiveTorque(Level world, BlockPos pos) {
        if (fluidType == null)
            return 0;
        double density = Math.min(12000, fluidType.getFluidType().getDensity());
        double mdot = density * fluidFallSpeed;
        double tau = 0.0625 * mdot * fluidFallSpeed;
        return Math.min((int) tau, type.getTorque());
    }

    private void dealPanelDamage(Level world, BlockPos pos) {
        int a = 0;
        int b = 0;
        Direction facing = write;
        if (facing == Direction.NORTH || facing == Direction.SOUTH)
            a = 1;
        else
            b = 1;
        AABB box = new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).inflate(a, 1, b);
        List<LivingEntity> in = world.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity ent : in) {
            ent.hurt(RotaryCraft.hydrokinetic.get(world), 1);
        }
    }

    private double getGravity(Level world) {
        return ReikaPhysicsHelper.g;
    }

    private boolean isPartOfArray() {
        return this.isBackEndOfArray() || this.isFrontOfArray();
    }

    public boolean isBackEndOfArray() {
        if (write == null) return false;
        BlockEntity te = getAdjacentBlockEntity(write);
        if (te instanceof BlockEntityHydroEngine hydro) {
            return hydro.getEngineType() == EngineType.HYDRO && !hydro.failed;
        }
        return false;
    }

    public boolean isFrontOfArray() {
        if (write == null) return false;
        BlockPos backPos = new BlockPos(backx, worldPosition.getY(), backz);
        MachineRegistry from = MachineRegistry.getMachine(level, backPos);
        BlockEntity writeTe = getAdjacentBlockEntity(write);
        if (from == MachineRegistry.HYDRO_ENGINE && !(writeTe instanceof BlockEntityHydroEngine)) {
            BlockEntity backTe = level.getBlockEntity(backPos);
            if (backTe instanceof BlockEntityEngine eng)
                return eng.getEngineType() == EngineType.HYDRO;
        }
        if (writeTe instanceof BlockEntityHydroEngine hydro) {
            return hydro.failed;
        }
        return false;
    }

    private int getArrayTorqueMultiplier() {
        if (write == null) return 1;
        ArrayList<BlockEntityHydroEngine> li = new ArrayList<>();
        int size = 1;
        BlockEntity te = getAdjacentBlockEntity(write.getOpposite());
        while (te instanceof BlockEntityHydroEngine eng && te != this && !li.contains(te)) {
            li.add(eng);
            if (eng.getRequirements(level, eng.worldPosition)) {
                if (eng.omega == omega && !eng.failed) {
                    size++;
                    te = eng.getAdjacentBlockEntity(eng.write != null ? eng.write.getOpposite() : null);
                } else {
                    ReikaParticleHelper.CRITICAL.spawnAroundBlock(level, eng.worldPosition, 5);
                    if (DragonAPI.rand.nextInt(3) == 0)
                        ReikaSoundHelper.playSoundAtBlock(level, eng.worldPosition, SoundEvents.BLAZE_HURT);
                    break;
                }
            }
        }
        return size;
    }

    @Override
    public boolean shouldPlayEngineSound() {
        return super.shouldPlayEngineSound() && (this.isFrontOfArray() || !this.isPartOfArray());
    }

    @Override
    public int getFuelLevel() {
        return 0;
    }

    @Override
    protected int getMaxSpeed(Level world, BlockPos pos) {
        return Math.max(1, this.getEffectiveSpeed(world, pos));
    }

    @Override
    protected int getGenTorque(Level world, BlockPos pos) {
        if (failed)
            return 1;
        int torque = this.getEffectiveTorque(world, pos) * this.getArrayTorqueMultiplier();
        double ratio = (double) torque / EngineType.HYDRO.getTorque();
        int r = bedrock ? 16 : 4;
        if (ratio > r) {
            this.fail(world, pos);
        }
        return torque;
    }

    private void fail(Level world, BlockPos pos) {
        ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.ITEM_BREAK.value());
        ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.GENERIC_EXPLODE.value(), 0.2F, 0.5F);
        failed = true;
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {
        this.dealPanelDamage(world, pos);
        this.spawnParticles(world, pos);
        if (failed) {
            Direction dir = this.getWriteDirection();
            if (dir == null) return;
            Direction left = ReikaDirectionHelper.getLeftBy90(dir);
            int x = worldPosition.getX();
            int y = worldPosition.getY();
            int z = worldPosition.getZ();
            for (int i = -1; i <= 1; i++) {
                BlockPos sidePos = new BlockPos(x + left.getStepX(), y + i, z + left.getStepZ());
                ReikaWorldHelper.dropAndDestroyBlockAt(world, sidePos, null, false, true);
            }
            ReikaWorldHelper.dropAndDestroyBlockAt(world, worldPosition.above(), null, false, true);
            ReikaWorldHelper.dropAndDestroyBlockAt(world, worldPosition.below(), null, false, true);
        }
    }

    private void spawnParticles(Level world, BlockPos pos) {
        BlockPos waterCol = this.getWaterColumnPos();
        Direction waterDir = ReikaDirectionHelper.getLeftBy90(write);
        Direction oppositeWater = waterDir.getOpposite();

        ReikaParticleHelper.RAIN.spawnAroundBlock(world, pos, 16);
        BlockPos oppositePos = new BlockPos(
                worldPosition.getX() + oppositeWater.getStepX(),
                worldPosition.getY(),
                worldPosition.getZ() + oppositeWater.getStepZ());
        ReikaParticleHelper.RAIN.spawnAroundBlock(world, oppositePos, 16);
        ReikaParticleHelper.RAIN.spawnAroundBlock(world, waterCol, 16);
        if (failed) {
            if (DragonAPI.rand.nextInt(5) == 0)
                ReikaSoundHelper.playSoundAtBlock(world, pos, SoundEvents.BLAZE_HURT);
            ReikaParticleHelper.CRITICAL.spawnAroundBlockWithOutset(world, pos, 3, 0.25);
        }
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        failed = NBT.getBooleanOr("fail", false);
        bedrock = NBT.getBooleanOr("bedrock", false);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putBoolean("fail", failed);
        NBT.putBoolean("bedrock", bedrock);
    }

    public void makeBedrock() {
        if (!bedrock) {
            bedrock = true;
            failed = false;
        }
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        PowerSourceList psl = super.getPowerSources(io, caller);
        if (write == null) return psl;
        ArrayList<BlockEntityHydroEngine> visited = new ArrayList<>();
        BlockEntity te = getAdjacentBlockEntity(write.getOpposite());
        while (te instanceof BlockEntityHydroEngine eng && te != this && !visited.contains(te)) {
            visited.add(eng);
            if (eng.getRequirements(level, eng.worldPosition)) {
                if (eng.omega == omega && !eng.failed) {
                    psl.addSource(eng);
                    te = eng.getAdjacentBlockEntity(eng.write != null ? eng.write.getOpposite() : null);
                } else {
                    break;
                }
            }
        }
        return psl;
    }

    public AABB getRenderBoundingBox() {
        return ReikaAABBHelper.getBlockAABB(worldPosition).inflate(1, 1, 1);
    }

    @Override
    public boolean isBroken() {
        return failed;
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
    public int getAmbientTemperature() {
        return 0;
    }
}
