/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.storage;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.MenuProvider;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.material.MapColor;

import net.minecraft.world.phys.AABB;
import net.neoforged.client.extensions.common.IClientFluidTypeExtensions;
import net.neoforged.common.capabilities.Capability;
import net.neoforged.common.capabilities.ForgeCapabilities;
import net.neoforged.common.util.LazyOptional;
import net.neoforged.fluids.FluidStack;
import net.neoforged.fluids.capability.IFluidHandler;


import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.AdjacentUpdateWatcher;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.interfaces.blockentity.OpenTopTank;
import reika.dragonapi.interfaces.blockentity.PlaceNotification;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.ReservoirAPI;
import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.gui.container.machine.ReservoirContainer;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;

import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;


public class BlockEntityReservoir extends RotaryCraftBlockEntity implements PipeConnector, NBTMachine, BreakAction, AdjacentUpdateWatcher, PlaceNotification, OpenTopTank, MenuProvider {

    public static final int CAPACITY = 64000;

    private static final ArrayList<FluidStack> creativeFluids = new ArrayList<>();

    private static final Collection<ReservoirAPI.TankHandler> tankHandlers = new HashSet<>();
    private static final HashMap<String, FluidEffect> fluidEffects = new HashMap<>();

    private final StepTimer flowTimer = new StepTimer(BlockEntityPiping.getTickDelay());
    private final StepTimer tempTimer = new StepTimer(20).stagger();
    private final HybridTank tank = new HybridTank("reservoir", CAPACITY) {
        @Override
        protected void onContentsChanged() {
            setChanged();
        }

        @Override
        public boolean isFluidValid(FluidStack stack) {
            return true; //todo ban specific fluids? blacklist?
        }
    };

    private LazyOptional<IFluidHandler> lazyFluidHandler = LazyOptional.empty();
    public boolean isCovered = false;
    public boolean isCreative;
    private boolean[] adjacent = new boolean[10];

    public BlockEntityReservoir(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.RESERVOIR.get(), pos, state);
    }

    @Override
    
    public <T> LazyOptional<T> getCapability( Capability<T> capability,  Direction facing) {
        if (capability == ForgeCapabilities.FLUID_HANDLER)
            return lazyFluidHandler.cast();
        return super.getCapability(capability, facing);
    }

    @Override
    public void onLoad() {
        super.onLoad();
        lazyFluidHandler = LazyOptional.of(() -> tank);
    }

    @Override
    public void invalidateCaps() {
        super.invalidateCaps();
        lazyFluidHandler.invalidate();
    }
//    private CompoundReservoir network;

    private static void addCreativeFluid(FluidStack fluid) {
        FluidStack f = fluid;
        if (f != null)
            creativeFluids.add(f);
    }

    public static void initCreativeFluids() {
        creativeFluids.clear();
        addCreativeFluid(new FluidStack(Fluids.WATER, -1));
        addCreativeFluid(new FluidStack(Fluids.LAVA, -1));
        addCreativeFluid(new FluidStack(RotaryFluids.LUBRICANT.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.JET_FUEL.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.ETHANOL.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.LIQUID_NITROGEN.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.LIQUID_AMMONIA.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.SODIUM.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.CHLORINE.get(), -1));
        addCreativeFluid(new FluidStack(RotaryFluids.OXYGEN.get(), -1));
//        addCreativeFluid("co2");
        addCreativeFluid(new FluidStack(RotaryFluids.HEAVY_WATER.get(), -1));
//        addCreativeFluid("fuel");
//        addCreativeFluid("oil");
//        addCreativeFluid("ender");
//        addCreativeFluid("redstone");
//        addCreativeFluid("glowstone");
//        addCreativeFluid("pyrotheum");
//        addCreativeFluid("cryotheum");
//        addCreativeFluid("coal");
//        addCreativeFluid("bop.springwater");
        addCreativeFluid(new FluidStack(RotaryFluids.POISON.get(), -1));
//        addCreativeFluid("sewage");
//        addCreativeFluid("potion crystal");
//        addCreativeFluid("chroma");

        fluidEffects.put("rc jet fuel", new PotionFluidEffect(MobEffects.POISON, 0, 200));
        fluidEffects.put("rc ammonia", new PotionFluidEffect(MobEffects.POISON, 0, 200));
        fluidEffects.put("ammonia", new PotionFluidEffect(MobEffects.POISON, 0, 200));
        fluidEffects.put("rc ethanol", new EthanolEffect());
        fluidEffects.put("ethanol", new EthanolEffect());
    }
	/*
	@Override
	protected void onFirstTick(Level world, BlockPos pos) {
		if (!world.isClientSide)
			this.recalculateCompound();
	}

	public void recalculateCompound() {
		network = new CompoundReservoir(level).addReservoir(this);
		for (int i = 2; i < 6; i++) {
			BlockEntity te = this.getAdjacentBlockEntity(dirs[i]);
			if (te instanceof BlockEntityReservoir) {
				BlockEntityReservoir tr = (BlockEntityReservoir)te;
				if (tr.getFluid() == this.getFluid() || tr.getFluid() == null || this.getFluid() == null) {
					if (tr.network != null) {
						network.merge(tr.network);
					}
				}
			}
		}
	}
	 */

    public static void addFluidEffect(FluidStack f, FluidEffect e) {
        addFluidEffect(f.getDisplayName().getString(), e);
    }

    public static void addFluidEffect(String f, FluidEffect e) {
        FluidEffect g = fluidEffects.get(f);
        if (g != null) {
            RotaryCraft.LOGGER.error("Cannot add effect " + e + " for fluid " + f + "; fluid already mapped to " + g);
        } else {
            fluidEffects.put(f, e);
        }
    }

    public int getLiquidScaled(int par1) {
        return (tank.getFluidLevel() * par1) / CAPACITY;
    }

    @Override
    protected void onFirstTick(Level world, BlockPos pos) {
        this.updateSides(world, pos);
    }

    private void updateSides(Level world, BlockPos pos) {
//        RotaryCraft.LOGGER.info("Updating sides");
        adjacent[8] = MachineRegistry.getMachine(world, pos.getX(), pos.getY(), pos.getZ() - 1) == MachineRegistry.RESERVOIR;
        adjacent[4] = MachineRegistry.getMachine(world, pos.getX() - 1, pos.getY(), pos.getZ()) == MachineRegistry.RESERVOIR;
        adjacent[6] = MachineRegistry.getMachine(world, pos.getX() + 1, pos.getY(), pos.getZ()) == MachineRegistry.RESERVOIR;
        adjacent[2] = MachineRegistry.getMachine(world, pos.getX(), pos.getY(), pos.getZ() + 1) == MachineRegistry.RESERVOIR;

        adjacent[1] = MachineRegistry.getMachine(world, pos.getX() - 1, pos.getY(), pos.getZ() + 1) == MachineRegistry.RESERVOIR;
        adjacent[3] = MachineRegistry.getMachine(world, pos.getX() + 1, pos.getY(), pos.getZ() + 1) == MachineRegistry.RESERVOIR;
        adjacent[7] = MachineRegistry.getMachine(world, pos.getX() - 1, pos.getY(), pos.getZ() - 1) == MachineRegistry.RESERVOIR;
        adjacent[9] = MachineRegistry.getMachine(world, pos.getX() + 1, pos.getY(), pos.getZ() - 1) == MachineRegistry.RESERVOIR;

        this.syncAllData(false);
    }

    @Override
    public void onAdjacentUpdate(Level world, BlockPos pos, Block b) {
        this.updateSides(world, pos);
    }

    @Override
    public void onPlaced() {
        this.updateNeighbors();
    }

    @Override
    public void breakBlock() {
        this.updateNeighbors();
    }

    private void updateNeighbors() {
        for (int i = -1; i <= 1; i++) {
            for (int k = -1; k <= 1; k++) {
                if (i != 0 || k != 0) {
                    int dx = worldPosition.getX() + i;
                    int dz = worldPosition.getZ() + k;
                    if (MachineRegistry.getMachine(level, dx, worldPosition.getY(), dz) == this.getMachine()) {
                        ((BlockEntityReservoir) level.getBlockEntity(new BlockPos(dx, worldPosition.getY(), dz))).updateSides(level, new BlockPos(dx, worldPosition.getY(), dz));
                    }
                }
            }
        }
    }

    public boolean hasNearbyReservoir(int loc) {
        return adjacent[loc];
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.RESERVOIR.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        for (ReservoirAPI.TankHandler th : tankHandlers) {
            int amt = th.onTick(this, tank.getFluid(), this.getPlacer());
            if (amt > 0)
                tank.removeLiquid(amt);
        }

        flowTimer.update();
        if (flowTimer.checkCap())
            this.transferBetween(world, pos);
        if (!isCovered) {
            if (!world.isClientSide) {
                Biome biome = world.getBiomeManager().getBiome(pos).value();
                if (world.isRaining()/* && biome.canSpawnLightningBolt() && world.canLightningStrikeAt(pos.above())*/) {
                    if (this.isEmpty() || (this.getFluid().equals(Fluids.WATER) && this.getFluidLevel() < CAPACITY)) {
                        this.addLiquid(25, Fluids.WATER);
                    }
                }
            }

            if (!tank.isEmpty()) {
                if (tank.getActualFluid().getFluid().getFluidType().getDensity() < 0 && tank.getActualFluid().getFluid().getFluidType().isLighterThanAir()) {
                    tank.removeLiquid(100); //evaporate
                }
            }
        }

        if (tank.getActualFluid() == null || this.getFluidLevel() <= 0)
            tank.empty();
        else if (isCreative)
            tank.addLiquid(CAPACITY, tank.getActualFluid().getFluid());

        tempTimer.setCap(isCovered ? 30 : 20);

        tempTimer.update();
        if (!world.isClientSide && !this.isEmpty() && tempTimer.checkCap()) {
            if (!this.isSurrounded(false)) {
                FluidStack f = tank.getActualFluid();
                int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
                int temp = f.getFluid().getFluidType().getTemperature() - 273;
                int dT = temp - Tamb;
                int r = 2;
                for (int i = -r; i <= r; i++) {
                    for (int j = -r; j <= r; j++) {
                        for (int k = -r; k <= r; k++) {
                            double dd = ReikaMathLibrary.py3d(i, j, k) + 1;
                            int hiT = (int) (Tamb + dT / dd / 2D);
                            //ReikaJavaLibrary.pConsole(dT+" above "+Tamb+" @ "+temp+", h = "+hiT, Dist.DEDICATED_SERVER);
                            ReikaWorldHelper.temperatureEnvironment(world, new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k), hiT);
//           todo                 if (temp > 2500)
//                                ReikaSoundHelper.playSoundAtBlock(world, x + i, y + j, z + k, "DragonAPI.rand.fizz", 0.2F, 1F);
                        }
                    }
                }
                if (temp > 2500) {
                    world.setBlock(pos, Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
                    world.setBlock(new BlockPos(pos.getX() + 1, pos.getY(), pos.getZ()), Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
                    world.setBlock(new BlockPos(pos.getX() - 1, pos.getY(), pos.getZ()), Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
                    world.setBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ() + 1), Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
                    world.setBlock(new BlockPos(pos.getX(), pos.getY(), pos.getZ() - 1), Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
//            todo        ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.fizz", 0.4F, 1F);
                }

                boolean hot = Tamb >= 300;
                hot = hot || ReikaWorldHelper.checkForAdjMaterial(world, pos, MapColor.FIRE) != null;
//                hot = hot || ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.LAVA) != null;
                if (hot) {
                    if (ReikaFluidHelper.isFlammable(f.getFluid().defaultFluidState())) {
                        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
                        world.explode(null, pos.getX() + 0.5d, pos.getY() + 0.5d, pos.getZ() + 0.5d, 4F, true, Level.ExplosionInteraction.BLOCK);
                    }
                }
            }
        }

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private boolean isSurrounded(boolean reservoirOnly) {
        return (adjacent[2] && adjacent[4] && adjacent[6] && adjacent[8]) || (!reservoirOnly && ReikaWorldHelper.isBlockSurroundedBySolid(level, worldPosition, false));
    }

    private void transferBetween(Level world, BlockPos pos) {
        if (tank.getFluidLevel() < CAPACITY) {
            for (int i = 2; i < 6; i++) {
                Direction dir = dirs[i];
                if (this.adjacentOnSide(dir)) {
                    int dx = pos.getX() + dir.getStepX();
                    int dy = pos.getY() + dir.getStepY();
                    int dz = pos.getZ() + dir.getStepZ();
                    if (this.matchMachine(world, new BlockPos(dx, dy, dz))) {
                        BlockEntityReservoir tile = (BlockEntityReservoir) world.getBlockEntity(new BlockPos(dx, dy, dz));
                        if (this.canMixWith(tile)) {
                            int diff = tile.getFluidLevel() - this.getFluidLevel();
                            if (diff > 1) {
                                tile.tank.removeLiquid(diff / 2);
                                tank.addLiquid(diff / 2, tile.getFluid().getFluid());
                            }
                        }
                    }
                }
            }
        }
    }

    public boolean adjacentOnSide(Direction dir) {
        return switch (dir) {
            case EAST -> adjacent[6];
            case NORTH -> adjacent[8];
            case SOUTH -> adjacent[2];
            case WEST -> adjacent[4];
            case DOWN, UP -> false;
        };
    }

    private boolean canMixWith(BlockEntityReservoir tile) {
        if (tile.getFluid() == null)
            return false;
        return tank.isEmpty() || this.getFluid().equals(tile.getFluid());
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        tank.writeToNBT(NBT);

        NBT.putBoolean("cover", isCovered);
        NBT.putBoolean("creative", isCreative);

        NBT.putInt("sides", ReikaArrayHelper.booleanToBitflags(adjacent));
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        tank.readFromNBT(NBT);

        isCovered = NBT.getBoolean("cover");
        isCreative = NBT.getBoolean("creative");

        adjacent = ReikaArrayHelper.booleanFromBitflags(NBT.getInt("sides"), 10);
    }

    @Override
    protected String getTEName() {
        return "reservoir";
    }

    @Override
    public boolean hasModelTransparency() {
        return true;
    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.RESERVOIR;
    }

    @Override
    public int getRedstoneOverride() {
        return (int) (15 * tank.getFraction());
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe() || m == MachineRegistry.HOSE || m == MachineRegistry.FUELLINE || m == MachineRegistry.VALVE;
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canConnectToPipe(p);
    }

    @Override
    public void onEMP() {
    }

    public Collection<AABB> getComplexHitbox() {
        Collection<AABB> li = new ArrayList<>();
        li.add(new AABB(0, 0, 0, 1, 0.0625, 1));
        if (isCovered) {
            li.add(new AABB(0.0625, 0.875, 0.0625, 0.9375, 0.9375, 0.9375));
        }
        if (!this.isConnectedOnSide(Direction.EAST)) {
            li.add(new AABB(0.9375, 0, 0, 1, 1, 1));
        }
        if (!this.isConnectedOnSide(Direction.WEST)) {
            li.add(new AABB(0, 0, 0, 0.0625, 1, 1));
        }
        if (!this.isConnectedOnSide(Direction.SOUTH)) {
            li.add(new AABB(0, 0, 0.9375, 1, 1, 1));
        }
        if (!this.isConnectedOnSide(Direction.NORTH)) {
            li.add(new AABB(0, 0, 0, 1, 1, 0.0625));
        }
        return li;
    }

//    public AABB getHitbox() {
//        if (isCovered || this.isEdgePiece(level, xCoord, yCoord, zCoord))
//            return ReikaAABBHelper.getBlockAABB(xCoord, yCoord, zCoord);
//        return AABB.getBoundingBox(xCoord, yCoord, zCoord, xCoord + 1, yCoord + 0.0625, zCoord + 1);
//    }

    private boolean isEdgePiece(Level world, BlockPos pos) {
        for (int i = 2; i < 6; i++) {
            Direction dir = dirs[i];
            int dx = pos.getX() + dir.getStepX();
            int dy = pos.getY() + dir.getStepY();
            int dz = pos.getZ() + dir.getStepZ();
            MachineRegistry m = MachineRegistry.getMachine(world, dx, dy, dz);
            if (m != MachineRegistry.RESERVOIR)
                return true;
        }
        return false;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction doFill) {
        if (from == Direction.UP)
            return 0;
        return tank.fill(resource, doFill);
    }

//        @Override
    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
        return this.canDrain(from, resource) ? tank.drain(resource.getAmount(), doDrain ? IFluidHandler.FluidAction.EXECUTE : IFluidHandler.FluidAction.SIMULATE) : null;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        if (from == Direction.UP)
            return null;
        return tank.drain(maxDrain, doDrain);
    }

    //    @Override
    public boolean canFill(Direction from, Fluid fluid) {
        return from != Direction.UP;
    }

    //    @Override
    public boolean canDrain(Direction from, FluidStack fluid) {
        return from != Direction.UP && ReikaFluidHelper.isFluidDrainableFromTank(fluid, tank);
    }

    public boolean canAcceptFluid(Fluid f) {
        return tank.isEmpty() || f.equals(tank.getActualFluid());
    }

    public int getFluidLevel() {
        return tank.getFluidLevel();
    }

    public FluidStack getFluid() {
        return tank.getActualFluid();
    }

    public void setLevel(int amt, Fluid f) {
        tank.setContents(amt, f);
    }

    public void removeLiquid(int amt) {
        tank.removeLiquid(amt);
    }

    public void addLiquid(int amt, Fluid f) {
//        RotaryCraft.LOGGER.info("Adding "+amt+" of "+f+" to "+this);
        tank.addLiquid(amt, f);
    }

    public boolean isEmpty() {
        return tank.isEmpty();
    }

    public FluidStack getContents() {
        return tank.getFluid();
    }
	/*
	@Override
	public void breakBlock() {
		if (network != null && !level.isClientSide) {
			network.removeReservoir(this);
		}
	}
	 */

    public boolean isConnectedOnSide(Direction dir) {
        int dx = worldPosition.getX() + dir.getStepX();
        int dy = worldPosition.getY() + dir.getStepY();
        int dz = worldPosition.getZ() + dir.getStepZ();
        if (this.adjacentOnSide(dir)) {
            BlockEntityReservoir te = (BlockEntityReservoir) level.getBlockEntity(new BlockPos(dx, dy, dz));
            if (te == null)
                return false;
            return te.isEmpty() || this.isEmpty() || te.getFluid().equals(this.getFluid());
        }
        return false;
    }

    public void setEmpty() {
        tank.empty();
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side == Direction.DOWN ? Flow.OUTPUT : Flow.INPUT;
    }


    public int getFluidRenderColor() {
        FluidStack fs = tank.getFluid();
        IClientFluidTypeExtensions props = IClientFluidTypeExtensions.of(fs.getFluid());
        if (fs.isEmpty())
            return 0xffffff;
        int clr = fs.getTag() != null && fs.getTag().contains("renderColor") ? fs.getTag().getInt("renderColor") : props.getTintColor();
//        if (this.isInWorld() && fs.getFluid().canBePlacedInWorld()) {
//            clr = fs.getFluid().defaultFluidState().createLegacyBlock().colorMultiplier(level, xCoord, yCoord, zCoord);
//        }
        return clr;
    }

    public void applyFluidEffectsToEntity(LivingEntity e) {
        if (!tank.isEmpty() && !isCovered) {
            FluidStack f = tank.getActualFluid();
            FluidEffect eff = fluidEffects.get(f.getDisplayName());
            if (eff != null) {
                eff.applyEffect(e);
            }
            if (f.equals(Fluids.LAVA) || f.getFluid().getFluidType().getTemperature() > 500) {
                e.hurt(e.damageSources().lava(), 4);
                e.setSecondsOnFire(12);
            }
            if (f.getFluid().getFluidType().getTemperature() < 250)
                e.hurt(e.damageSources().wither(), 1);
            if (e.isOnFire() && ReikaFluidHelper.isFlammable(f.getFluid().defaultFluidState())) {
                this.delete();
                level.explode(e, e.getY(), e.getY(), e.getZ(), 4F, true, Level.ExplosionInteraction.BLOCK);
            }
//            if (f.canBePlacedInWorld()) {
//                Block b = f.getFluid().defaultFluidState().createLegacyBlock().getBlock();
//                b.onEntityCollidedWithBlock(level, worldPosition, e);
//            }
        }
    }

    @Override
    public CompoundTag getTagsToWriteToStack() {
        CompoundTag NBT = new CompoundTag();
        NBT.putBoolean("cover", isCovered);
        if (this.isEmpty())
            return NBT;
        FluidStack f = this.getFluid();
        int level = this.getFluidLevel();
        ReikaNBTHelper.writeFluidToNBT(NBT, f);
        NBT.putInt("lvl", level);
        return NBT;
    }

    @Override
    public void setDataFromItemStackTag(CompoundTag NBT) {
        if (NBT == null) {
            tank.empty();
            isCovered = false;
            return;
        }
        FluidStack f = ReikaNBTHelper.getFluidFromNBT(NBT);
        int level = NBT.getInt("lvl");
        tank.setContents(level, f.getFluid());

        isCovered = NBT.getBoolean("cover");
    }

    public void combineDataFromItemStackTag(CompoundTag NBT) {
        if (NBT == null)
            return;
        FluidStack f = ReikaNBTHelper.getFluidFromNBT(NBT);
        if (f != tank.getActualFluid())
            return;
        int level = NBT.getInt("lvl");
        tank.setContents(level + tank.getFluidLevel(), f.getFluid());

        isCovered = isCovered || NBT.getBoolean("cover");
    }

    public ArrayList<String> getDisplayTags(CompoundTag nbt) {
        ArrayList<String> li = new ArrayList<>();
        FluidStack f = ReikaNBTHelper.getFluidFromNBT(nbt);
        if (f != null) {
            String fluid = f.getDisplayName().getString();
            int amt = nbt.getInt("lvl");
            if (amt > 0) {
                String amount = String.format("%d", amt);
                String contents = "Contents: " + amount + " mB of " + fluid;
                li.add(contents);
            }
        }
        if (nbt.getBoolean("cover"))
            li.add("Covered");
        return li;
    }

    public ArrayList<CompoundTag> getCreativeModeVariants() {
        ArrayList<CompoundTag> li = new ArrayList<>();
        li.add(null);
        for (FluidStack creativeFluid : creativeFluids) {
            CompoundTag nbt = new CompoundTag();
            nbt.putInt("lvl", CAPACITY);
            ReikaNBTHelper.writeFluidToNBT(nbt, creativeFluid);
            li.add(nbt);
        }
        return li;
    }

    @Override
    public int addLiquid(Fluid f, int amt, boolean doAdd) {
        if (!tank.isEmpty() && tank.getActualFluid().getFluid() != f)
            return 0;
        amt = Math.min(amt, tank.getRemainingSpace());
        if (doAdd)
            tank.addLiquid(amt, f);
        return amt;
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
        return Component.literal("Reservoir");
    }

    
    @Override
    public AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ReservoirContainer(p_39954_, p_39955_, this);
    }

    public interface FluidEffect {

        void applyEffect(LivingEntity e);

    }

    public static final class PotionFluidEffect implements FluidEffect {

        public final int duration;
        public final int level;
        public final MobEffect potion;

        public PotionFluidEffect(MobEffect p, int l, int d) {
            potion = p;
            level = l;
            duration = d;
        }

        @Override
        public void applyEffect(LivingEntity e) {
            e.addEffect(new MobEffectInstance(potion, duration, level));
        }

    }

    public static class EthanolEffect implements FluidEffect {

        @Override
        public void applyEffect(LivingEntity e) {
            MobEffectInstance eff = e.getEffect(MobEffects.CONFUSION);
            int dura = 1;
            if (eff != null) {
                dura = eff.getDuration() + 1;
            }
            if (dura > 600)
                dura = 600;
            e.addEffect(new MobEffectInstance(MobEffects.CONFUSION, dura, 3));
        }

    }

    public static final class WaterEffect implements FluidEffect {

        @Override
        public void applyEffect(LivingEntity e) {
            e.clearFire();
        }

    }

}
