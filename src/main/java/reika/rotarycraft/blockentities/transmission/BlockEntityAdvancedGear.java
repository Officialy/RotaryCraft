/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.Tag;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.Mth;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Explosion;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import net.minecraftforge.items.IItemHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.data.immutable.WorldLocation;
import reika.dragonapi.interfaces.blockentity.PartialInventory;
import reika.dragonapi.interfaces.blockentity.PartialTank;
import reika.dragonapi.interfaces.blockentity.ToggleTile;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.java.ReikaRandomHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.api.interfaces.CVTController;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.interfaces.PowerSourceTracker;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;

import reika.rotarycraft.registry.*;

public class BlockEntityAdvancedGear extends BlockEntity1DTransmitter implements IItemHandler, PowerGenerator, PartialInventory, PartialTank, PipeConnector, IFluidHandler, ToggleTile, CVTController {

    public static final int WORMRATIO = 64;
    private final HybridTank lubricant = new HybridTank("advgear", 20000);
    private final CVTState[] cvtState = new CVTState[2];
    public boolean torquemode = true;
    private boolean isReleasing = false;
    private int releaseTorque = 0;
    private int releaseOmega = 0;
    /**
     * Stored energy, in joules
     */
    private long energy;
    private boolean isBedrockCoil = false;
    private boolean isCreative;
    private CVTMode cvtMode = CVTMode.MANUAL;
    private CVTController controller;
    private ItemStack[] belts = new ItemStack[31];
    private int targetTorque = 1;

    private boolean enabled = true;

    public BlockEntityAdvancedGear(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.ADVANCED_GEAR.get(), pos, state);
    }

    public static long getMaxStorageCapacity(boolean bedrock) {
        return bedrock ? 240L * ReikaMathLibrary.longpow(10, 12) : 720 * ReikaMathLibrary.intpow2(10, 6);
    }

    public static String getMaxStorageCapacityFormatted(boolean bedrock) {
        long max = getMaxStorageCapacity(bedrock);
        return String.format("%.3f%sJ", ReikaMathLibrary.getThousandBase(max), ReikaEngLibrary.getSIPrefix(max));
    }

    public static String getRequiredInputPower() {
        return "CEIL2((log_2(energy))^4)";
    }

    public static String getRequiredInputTorque() {
        return "CEIL2((log_2(energy))^3)";
    }

    public static int getOutputCap(boolean bedrock) {
        return bedrock ? 4096 : 1024;
    }

    public static String getOutputFunction() {
        return "CEIL2_pseudo(SQRT(energy)/4)";
    }

    public void stepMode() {
        cvtMode = cvtMode.next();
        this.syncAllData(false);
    }

    public int getTargetTorque() {
        return targetTorque;
    }

    public void setTargetTorque(int target) {
        targetTorque = target;
    }

    public void setController(CVTController c) {
        controller = c;
    }

    public long getMaxStorageCapacity() {
        return this.getGearType().storesEnergy() ? getMaxStorageCapacity(isBedrockCoil) : 0;
    }

    public void setBedrock(boolean bedrock) {
        isBedrockCoil = bedrock;
    }

    public int getMaximumEmission() {
        return isCreative ? Integer.MAX_VALUE : getOutputCap(this.isBedrockCoil()); //still the 16x increase in the coil item
    }

    public int getReleaseTorque() {
        return releaseTorque;
    }

    public void setReleaseTorque(int torque) {
        releaseTorque = Math.min(this.getTorqueCap(), Math.min(this.getMaximumEmission(), torque));
    }

    public int getReleaseOmega() {
        return releaseOmega;
    }

    public void setReleaseOmega(int omega) {
        releaseOmega = Math.min(this.getMaximumEmission(), omega);
    }

    public GearType getGearType() {
        return GearType.list[1];//todo [meta / 4];
    }

    public int getLubricant() {
        return lubricant.getLevel();
    }

    public void addLubricant(int amt) {
        lubricant.addLiquid(amt, RotaryFluids.LUBRICANT.get());
    }

    public boolean hasLubricant() {
        return !lubricant.isEmpty();
    }

    public boolean canAcceptAnotherLubricantBucket() {
        return lubricant.getLevel() + 1000 <= lubricant.getCapacity();
    }

    //-ve ratio is torque mode for cvt
    @Override
    protected void readFromSplitter(Level world, BlockPos pos, BlockEntitySplitter spl) { //Complex enough to deserve its own function
        int sratio = spl.getRatioFromMode();
        if (sratio == 0)
            return;
        boolean favorbent = false;
        if (sratio < 0) {
            favorbent = true;
            sratio = -sratio;
        }
        if (this.getGearType() == GearType.WORM || this.getGearType() == GearType.CVT && this.getEffectiveRatio() < 0) {
            if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
                omega = Math.abs((int) (spl.omega / this.getEffectiveRatio() * this.getPowerLossFraction(spl.omega))); //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = Math.abs((int) (spl.torque / 2 * this.getEffectiveRatio()));
                    return;
                }
                if (favorbent) {
                    torque = Math.abs((int) (spl.torque / sratio * this.getEffectiveRatio()));
                } else {
                    torque = Math.abs((int) (this.getEffectiveRatio() * (int) (spl.torque * ((sratio - 1D) / (sratio)))));
                }
            } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
                omega = Math.abs((int) (spl.omega / this.getEffectiveRatio() * this.getPowerLossFraction(spl.omega))); //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = Math.abs((int) (spl.torque / 2 * this.getEffectiveRatio()));
                    return;
                }
                if (favorbent) {
                    torque = Math.abs((int) (this.getEffectiveRatio() * (int) (spl.torque * ((sratio - 1D) / (sratio)))));
                } else {
                    torque = Math.abs((int) (spl.torque / sratio * this.getEffectiveRatio()));
                }
            } else { //We are not one of its write-to blocks
                torque = 0;
                omega = 0;
                power = 0;
                return;
            }
        } else if (this.getGearType() == GearType.HIGH) {
            if (this.hasLubricant()) {
                if (this.getEffectiveRatio() < 0) {
                    if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
                        omega = -(int) (spl.omega / this.getEffectiveRatio()); //omega always constant
                        if (sratio == 1) { //Even split, favorbent irrelevant
                            torque = Math.abs((int) (spl.torque / 2 * this.getEffectiveRatio()));
                        } else if (favorbent) {
                            torque = Math.abs((int) (spl.torque / sratio * this.getEffectiveRatio()));
                        } else {
                            torque = Math.abs((int) (this.getEffectiveRatio() * (int) (spl.torque * ((sratio - 1D) / (sratio)))));
                        }
                    } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
                        omega = -(int) (spl.omega / this.getEffectiveRatio()); //omega always constant
                        if (sratio == 1) { //Even split, favorbent irrelevant
                            torque = Math.abs((int) (spl.torque / 2 * this.getEffectiveRatio()));
                        } else if (favorbent) {
                            torque = Math.abs((int) (this.getEffectiveRatio() * (int) (spl.torque * ((sratio - 1D) / (sratio)))));
                        } else {
                            torque = Math.abs((int) (spl.torque / sratio * this.getEffectiveRatio()));
                        }
                    } else { //We are not one of its write-to blocks
                        torque = 0;
                        omega = 0;
                        power = 0;
                        return;
                    }
                } else {
                    if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
                        omega = (int) (spl.omega * this.getEffectiveRatio()); //omega always constant
                        if (sratio == 1) { //Even split, favorbent irrelevant
                            torque = (int) (spl.torque / 2 / this.getEffectiveRatio());
                        } else if (favorbent) {
                            torque = (int) (spl.torque / sratio / this.getEffectiveRatio());
                        } else {
                            torque = (int) ((spl.torque * ((sratio - 1D)) / sratio) / (this.getEffectiveRatio()));
                        }
                    } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
                        omega = (int) (spl.omega * this.getEffectiveRatio()); //omega always constant
                        if (sratio == 1) { //Even split, favorbent irrelevant
                            torque = (int) (spl.torque / 2 / this.getEffectiveRatio());
                        } else if (favorbent) {
                            torque = (int) (spl.torque * ((sratio - 1D) / (sratio)) / this.getEffectiveRatio());
                        } else {
                            torque = (int) (spl.torque / sratio / this.getEffectiveRatio());
                        }
                    } else { //We are not one of its write-to blocks
                        torque = 0;
                        omega = 0;
                        power = 0;
                        return;
                    }
                }
                if (omega > 0 && (level.getDayTime() & 4) == 4)
                    lubricant.removeLiquid((int) ReikaMathLibrary.logbase(Math.max(omega, torque), 2));
            } else {
                omega = torque = 0;
            }
        } else {
            if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
                omega = (int) (spl.omega * this.getEffectiveRatio() * this.getPowerLossFraction(spl.omega)); //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = (int) (spl.torque / 2 / this.getEffectiveRatio());
                } else if (favorbent) {
                    torque = (int) (spl.torque / sratio / this.getEffectiveRatio());
                } else {
                    torque = (int) ((spl.torque * ((sratio - 1D)) / sratio) / (this.getEffectiveRatio()));
                }
            } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
                omega = (int) (spl.omega * this.getEffectiveRatio() * this.getPowerLossFraction(spl.omega)); //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = (int) (spl.torque / 2 / this.getEffectiveRatio());
                } else if (favorbent) {
                    torque = (int) (spl.torque * ((sratio - 1D) / (sratio)) / this.getEffectiveRatio());
                } else {
                    torque = (int) (spl.torque / sratio / this.getEffectiveRatio());
                }
            } else { //We are not one of its write-to blocks
                torque = 0;
                omega = 0;
                power = 0;
            }
        }
    }

    private double getEffectiveRatio() {
        GearType type = this.getGearType();
        if (type == GearType.COIL)
            return 1;
        if (type == GearType.WORM)
            return WORMRATIO;
        if (type == GearType.HIGH)
            return torquemode ? -256 : 256;
        return this.getCVTRatio();
    }

    public int getCVTRatio() {
        switch (cvtMode) {
            case AUTO, MANUAL -> {
                return ratio;
            }
            case REDSTONE -> {
                int ratio = this.getCVTState(this.hasRedstoneSignal()).gearRatio;
                return (int) Math.signum(ratio) * Math.min(Math.abs(ratio), this.getMaxRatio());
            }
        }
        return 1;
    }

    private double getPowerLossFraction(int speed) {
        if (this.getGearType() == GearType.WORM)
            return (128 - 4 * ReikaMathLibrary.logbase(speed, 2)) / 100;
        return 1;
    }

    public double getCurrentLoss() {
        return this.getPowerLossFraction(omegain);
    }

    //   todo @Override
    public boolean isUseableByPlayer(Player var1) {
        if (this.getGearType() == GearType.WORM)
            return false;
        return this.isPlayerAccessible(var1);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return switch (this.getGearType()) {
            case COIL -> RotaryBlocks.COIL.get();
            case CVT -> RotaryBlocks.CVT.get();
            case WORM -> RotaryBlocks.WORMGEAR.get();
            case HIGH -> RotaryBlocks.HIGHGEAR.get();
        };
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getIOSides(world, pos);
        if (this.getGearType() == GearType.CVT) {
            if (controller != null && controller.isActive() && controller.getCVT().equals(this)) {
                boolean torque = controller.isTorque();
                int r = Mth.clamp(controller.getControlledRatio(), 1, 32);
                ratio = torque ? r : -r;
            }
        }
        if (this.getGearType().storesEnergy())
            this.store(world, pos);
        else
            this.transferPower(world, pos);
        power = (long) omega * (long) torque;
        //ReikaJavaLibrary.pConsole(torque+" @ "+omega);

        this.basicPowerReceiver();
    }

    public boolean isBedrockCoil() {
        return isBedrockCoil;
    }

    private void store(Level world, BlockPos pos) {
        this.transferPower(world, pos);
        isReleasing = enabled && this.hasRedstoneSignal();
        //ReikaJavaLibrary.pConsole(energy/20+"/"+this.getMaxStorageCapacity(), Dist.DEDICATED_SERVER);
        if (!isCreative && !world.isClientSide && energy / 20 >= this.getMaxStorageCapacity()) {
            this.overChargeExplosion(world, pos);
        }
        if (!isReleasing) {
            torque = omega = 0;
            power = 0;
            if (energy + ((long) torquein * (long) omegain) < 0 || energy + ((long) torquein * (long) omegain) > Long.MAX_VALUE) {
                this.destroy(world, pos);
            } else if (!isCreative) {
                long pwr = (long) torquein * (long) omegain;
                if (torquein >= this.getChargingTorque() && pwr >= this.getChargingPower())
                    energy += pwr;
            }
        } else if (energy > 0 && releaseTorque > 0 && releaseOmega > 0) {
            releaseTorque = Math.min(releaseTorque, this.getTorqueCap());
            torque = releaseTorque;
            omega = releaseOmega;
            power = (long) torque * (long) omega;
            if (this.tickcount % 26 == 0)
                SoundRegistry.COIL.playSoundAtBlock(this);
            if (!isCreative)
                energy -= power;
            if (energy < 0)
                energy = 0;
        } else {
            torque = 0;
            omega = 0;
            power = 0;
        }
    }

    public long getChargingPower() {
        return energy >= 20 ? ReikaMathLibrary.ceil2exp(ReikaMathLibrary.intpow2(ReikaMathLibrary.logbase2(energy / 20), 4)) : 1;
    }

    public int getChargingTorque() {
        int base = energy >= 20 ? ReikaMathLibrary.ceil2exp(ReikaMathLibrary.intpow2(ReikaMathLibrary.logbase2(energy / 20), 3)) / 2 : 1;
        if (this.isBedrockCoil()) {
            base = Math.max(base, energy >= 20 ? 16 * ReikaMathLibrary.ceil2exp(ReikaMathLibrary.intpow2(ReikaMathLibrary.logbase2(energy / 80), 3)) / 2 : 16);
        }
        if (base <= (this.isBedrockCoil() ? 16 : 1))
            base = this.isBedrockCoil() ? 16 : 1;
        return base;
    }

    public int getTorqueCap() {
        return ReikaMathLibrary.ceilPseudo2Exp((int) Math.ceil(Math.sqrt(energy / 20) / 4));
    }

    private void overChargeExplosion(Level world, BlockPos pos) {
        world.setBlockAndUpdate(pos, Blocks.AIR.defaultBlockState());
        int num = isBedrockCoil ? 24 : 3;
        int pow = isBedrockCoil ? 12 : 8;
        int r = isBedrockCoil ? 9 : 1;
        for (int i = 0; i < num; i++) {
            double rx = ReikaRandomHelper.getRandomPlusMinus(pos.getX(), r);
            double ry = ReikaRandomHelper.getRandomPlusMinus(pos.getY(), r);
            double rz = ReikaRandomHelper.getRandomPlusMinus(pos.getZ(), r);
            world.explode(null, rx, ry, rz, 8, ConfigRegistry.BLOCKDAMAGE.getState() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE); //todo Explosioninteraction
        }
        world.explode(null, pos.getX(), pos.getY(), pos.getZ(), pow, ConfigRegistry.BLOCKDAMAGE.getState() ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE);
    }

    private void destroy(Level world, BlockPos pos) {
//        for (int i = 0; i < 16; i++)
//     todo       ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.explode", 5, 0.2F);

        ReikaParticleHelper.EXPLODE.spawnAroundBlock(world, pos, 2);
        int r = 20;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                for (int k = -r; k <= r; k++) {
                    double dd = ReikaMathLibrary.py3d(i, j * 2, k);
                    if (dd <= r + 0.5) {
                        if (world.getBlockState(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k)).getBlock() != Blocks.BEDROCK) {
                            world.setBlockAndUpdate(new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k), Blocks.AIR.defaultBlockState());
                        }
                    }
                    if (!world.isClientSide && DragonAPI.rand.nextInt(8) == 0)
                        ReikaWorldHelper.ignite(world, new BlockPos(pos.getX() + i, pos.getY() + j, pos.getZ() + k));
                }
            }
        }
    }

    public void getIOSides(Level world, BlockPos pos) {
//        while (metadata > 3)
//            metadata -= 4;
        super.getIOSides(world, pos, false);
    }

    private void calculateRatio() {
        int sign = 1;
        if (ratio < 0)
            sign = -1;
        if (Math.abs(ratio) > this.getMaxRatio())
            ratio = this.getMaxRatio() * sign;
        if (ratio == 0)
            ratio = 1;
    }

    public void setRatio(int ratio) {
        if (ratio == 0) {
            this.ratio = 1;
        } else {
            int sign = ratio < 0 ? -1 : 1;
            int maxrat = Math.min(Math.abs(ratio), this.getMaxRatio());
            this.ratio = maxrat * sign;
        }
    }

    public int getMaxRatio() {
        if (belts[0] == null)
            return 1;
        /* todo if (belts[0].getItem() != RotaryItems.BELT.get() || belts[0].getItemDamage() != RotaryItems.BELT.get().getItemDamage())
            return 1;
        for (int i = 1; i <= 2; i++) {
            if (belts[i] == null)
                return 2;
            if (belts[i].getItem() != RotaryItems.BELT.get() || belts[i].getItemDamage() != RotaryItems.BELT.get().getItemDamage())
                return 2;
        }
        for (int i = 3; i <= 6; i++) {
            if (belts[i] == null)
                return 4;
            if (belts[i].getItem() != RotaryItems.BELT.get() || belts[i].getItemDamage() != RotaryItems.BELT.get().getItemDamage())
                return 4;
        }
        for (int i = 7; i <= 14; i++) {
            if (belts[i] == null)
                return 8;
            if (belts[i].getItem() != RotaryItems.BELT.get() || belts[i].getItemDamage() != RotaryItems.BELT.get().getItemDamage())
                return 8;
        }
        for (int i = 15; i <= 30; i++) {
            if (belts[i] == null)
                return 16;
            if (belts[i].getItem() != RotaryItems.BELT.get() || belts[i].getItemDamage() != RotaryItems.BELT.get().getItemDamage())
                return 16;
        }*/
        return 32;
    }

    @Override
    protected void readFromCross(BlockEntityShaft cross) {
        if (cross.isWritingTo(this)) {
            omega = cross.readomega[0];
            if (this.getGearType() == GearType.WORM)
                omega = (int) ((((omega / WORMRATIO) * (100 - 4 * ReikaMathLibrary.logbase(omega, 2) + 28))) / 100);
            torque = cross.readtorque[0];
            if (this.getGearType() == GearType.WORM)
                torque = torque * WORMRATIO;
        } else if (cross.isWritingTo2(this)) {
            omega = cross.readomega[1];
            if (this.getGearType() == GearType.WORM)
                omega = (int) ((((omega / WORMRATIO) * (100 - 4 * ReikaMathLibrary.logbase(omega, 2) + 28))) / 100);
            torque = cross.readtorque[1];
            if (this.getGearType() == GearType.WORM)
                torque = torque * WORMRATIO;
        } else {
            omega = torque = 0;
            return; //not its output
        }
    }

    @Override
    protected void transferPower(Level world, BlockPos pos) {
        this.calculateRatio();
        if (level.isClientSide && !RotaryAux.getPowerOnClient)
            return;
        performRatio = true;
        omegain = torquein = 0;
        boolean isCentered = pos.getX() == worldPosition.getX() && pos.getY() == worldPosition.getY() && pos.getZ() == worldPosition.getZ();
        int dx = pos.getX() + read.getStepX();
        int dy = pos.getY() + read.getStepY();
        int dz = pos.getZ() + read.getStepZ();
        MachineRegistry m = isCentered ? this.getMachine(read) : MachineRegistry.getMachine(world, dx, dy, dz);
        BlockEntity te = isCentered ? getAdjacentBlockEntity(read) : world.getBlockEntity(new BlockPos(dx, dy, dz));
        if (this.isProvider(te)) {
            if (m == MachineRegistry.SHAFT) {
                BlockEntityShaft devicein = (BlockEntityShaft) te;
                if (devicein.isCross()) {
                    this.readFromCross(devicein);
                    return;
                } else if (devicein.isWritingToCoordinate(pos)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
            if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
            }
            if (te instanceof ShaftPowerEmitter) {
                ShaftPowerEmitter sp = (ShaftPowerEmitter) te;
                if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                    torquein = sp.getTorque();
                    omegain = sp.getOmega();
                }
            }

            if (te instanceof ComplexIO) {
                ComplexIO pwr = (ComplexIO) te;
                Direction dir = this.getInputDirection().getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
            }
            if (m == MachineRegistry.SPLITTER) {
                BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                if (devicein.isSplitting()) {
                    this.readFromSplitter(world, pos, devicein);
                    performRatio = false;
                    torquein = torque;
                    omegain = omega;
                    //ReikaJavaLibrary.pConsole(torque+" @ "+omega, Dist.DEDICATED_SERVER);
                    return;
                } else if (devicein.isWritingToCoordinate(pos)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
        }/* else if (te instanceof WorldRift) {
            WorldRift sr = (WorldRift) te;
            WorldLocation loc = sr.getLinkTarget();
            if (loc != null)
                this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta);
        }*/ else {
            omega = 0;
            torque = 0;
            power = 0;
            return;
        }

        if (performRatio) {
            switch (this.getGearType()) {
                case WORM:
                    omega = (int) ((omegain / WORMRATIO) * this.getPowerLossFraction(omegain));
                    if (torquein <= RotaryConfig.torquelimit / WORMRATIO)
                        torque = torquein * WORMRATIO;
                    else {
                        torque = RotaryConfig.torquelimit;
                        world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                        world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 0.1F, 1F, false);
                    }
                    break;
                case CVT:
                    if (cvtMode == CVTMode.AUTO) {
                        ratio = this.updateAutoRatio();
                    }
                    int ratio = this.getCVTRatio();
                    if (this.hasLubricant()) {
                        boolean speed = true;
                        if (ratio > 0) {
                            if (omegain <= RotaryConfig.omegalimit / ratio)
                                omega = omegain * ratio;
                            else {
                                omega = RotaryConfig.omegalimit;
                                world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.BLOCKS,0.1F, 1F, false);
                            }
                            torque = torquein / ratio;
                        } else {
                            if (torquein <= RotaryConfig.torquelimit / -ratio)
                                torque = torquein * -ratio;
                            else {
                                torque = RotaryConfig.torquelimit;
                                world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 0.1F, 1F, false);
                            }
                            omega = omegain / -ratio;
                        }
                    } else {
                        omega = torque = 0;
                    }
                    break;
                case COIL:

                    break;
                case HIGH:
                    if (this.hasLubricant()) {
                        if (torquemode) {
                            if (torquein <= RotaryConfig.torquelimit / 256)
                                torque = torquein * 256;
                            else {
                                torque = RotaryConfig.torquelimit;
                                world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 0.1F, 1F, false);
                            }
                            omega = omegain / 256;
                        } else {
                            torque = torquein / 256;
                            if (omegain <= RotaryConfig.omegalimit / 256)
                                omega = omegain * 256;
                            else {
                                omega = RotaryConfig.omegalimit;
                                world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, SoundEvents.BLAZE_HURT, SoundSource.BLOCKS, 0.1F, 1F, false);
                            }
                        }
                        if (omega > 0 && (world.getDayTime() & 4) == 4)
                            lubricant.removeLiquid((int) (DifficultyEffects.LUBEUSAGE.getChance() * ReikaMathLibrary.logbase(Math.max(omega, torque), 2)));
                    } else {
                        omega = torque = 0;
                    }
                    break;
            }
        }
    }

    public int getTorqueIn() {
        return torquein;
    }

    private int updateAutoRatio() {
        if (torquein >= targetTorque && torquein < targetTorque * 2) { //already meeting, and cannot do any more
            return 1;
        } else if (torquein > targetTorque) { //can get extra speed
            int val = 1;
            int has = torquein;
            while (has >= targetTorque && val <= this.getMaxRatio()) {
                val++;
                has = torquein / val;
            }
            return val - 1;
        } else {
            int val = 1;
            int has = torquein;
            while (has < targetTorque && val < this.getMaxRatio()) {
                val++;
                has = torquein * val;
            }
            return -val;
        }
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("ratio", ratio);
        NBT.putLong("e", energy);
        NBT.putInt("relo", releaseOmega);
        NBT.putInt("relt", releaseTorque);
        NBT.putInt("mode", cvtMode.ordinal());
        NBT.putInt("cvton", this.getCVTState(true).ordinal());
        NBT.putInt("cvtoff", this.getCVTState(false).ordinal());
        NBT.putBoolean("bedrock", isBedrockCoil);
        NBT.putBoolean("creative", isCreative);
        NBT.putBoolean("trq", torquemode);
        NBT.putInt("target", targetTorque);
        NBT.putInt("torquein", torquein);

        NBT.putBoolean("t_enable", enabled);

        lubricant.writeToNBT(NBT);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        ratio = NBT.getInt("ratio");
        energy = NBT.getLong("e");
        releaseOmega = NBT.getInt("relo");
        releaseTorque = NBT.getInt("relt");
        cvtMode = CVTMode.list[NBT.getInt("mode")];
        if (NBT.getBoolean("redstone")) //porting pre-placed redstone ones
            cvtMode = CVTMode.REDSTONE;
        cvtState[0] = CVTState.list[NBT.getInt("cvtoff")];
        cvtState[1] = CVTState.list[NBT.getInt("cvton")];
        isBedrockCoil = NBT.getBoolean("bedrock");
        isCreative = NBT.getBoolean("creative");
        torquemode = NBT.getBoolean("trq");
        targetTorque = NBT.getInt("target");
        torquein = NBT.getInt("torquein");

        if (NBT.contains("t_enable"))
            enabled = NBT.getBoolean("t_enable");

        lubricant.readFromNBT(NBT);
    }

    @Override
    public void saveAdditional(CompoundTag NBT) {
        super.saveAdditional(NBT);
        ListTag nbttaglist = new ListTag();

        for (int i = 0; i < belts.length; i++) {
            if (belts[i] != null) {
                CompoundTag CompoundTag = new CompoundTag();
                CompoundTag.putByte("Slot", (byte) i);
                belts[i].save(CompoundTag);
                nbttaglist.add(CompoundTag);
            }
        }

        NBT.put("Items", nbttaglist);
    }

    @Override
    protected String getTEName() {
        return "advancedgear";
    }

    @Override
    public void load(CompoundTag NBT) {
        super.load(NBT);
        ListTag nbttaglist = NBT.getList("Items", Tag.TAG_COMPOUND);
        belts = new ItemStack[this.getSlots()];

        for (int i = 0; i < nbttaglist.size(); i++) {
            CompoundTag CompoundTag = nbttaglist.getCompound(i);
            byte byte0 = CompoundTag.getByte("Slot");

            if (byte0 >= 0 && byte0 < belts.length) {
                belts[byte0] = ItemStack.of(CompoundTag);
            }
        }
    }

    @Override
    public int getSlots() {
        return belts.length;
    }

    @Override
    public ItemStack getStackInSlot(int var1) {
        return belts[var1];
    }

    @Override
    public @NotNull ItemStack insertItem(int slot, @NotNull ItemStack stack, boolean simulate) {
        return null;
    }

    @Override
    public @NotNull ItemStack extractItem(int slot, int amount, boolean simulate) {
        return null;
    }

    @Override
    public int getSlotLimit(int slot) {
        return 1;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return this.getGearType() == GearType.CVT && ReikaItemHelper.matchStacks(stack, RotaryItems.BELT);
    }

/*    @Override
todo    public ItemStack decrStackSize(int var1, int var2) {
        return ReikaInventoryHelper.decrStackSize(this, var1, var2);
    }

    @Override
    public ItemStack getStackInSlotOnClosing(int var1) {
        return ReikaInventoryHelper.getStackInSlotOnClosing(this, var1);
    }*/

// todo   @Override
    public void setInventorySlotContents(int var1, ItemStack var2) {
        belts[var1] = var2;
    }

    public void openInventory() {
    }

    public void closeInventory() {
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
        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.ADVANCEDGEARS;
    }

    @Override
    public int getRedstoneOverride() {
        if (this.getGearType().storesEnergy()) {
            int level = (int) (15L * (energy + power) / 20L / this.getMaxStorageCapacity()); //+power to "anticipate" next tick
            return level;
        }
        return 0;
    }

    @Override
    public void onEMP() {
//        super.onEMP();
    }

/*  todo  public int[] getAccessibleSlotsFromSide(int var1) {
        if (this instanceof InertIInv)
            return new int[0];
        return ReikaInventoryHelper.getWholeInventoryForISided(this);
    }

    public boolean canInsertItem(int i, ItemStack is, int side) {
        if (this instanceof InertIInv)
            return false;
        return ((Container) this).isItemValidForSlot(i, is);
    }*/

    public long getEnergy() {
        return energy;
    }

    public void setEnergyFromNBT(CompoundTag NBT) {
        energy = NBT.getLong("energy");
    }

    @Override
    public long getMaxPower() {
        if (this.getGearType() != GearType.COIL)
            return 0;
        return power > 0 ? releaseOmega * releaseTorque : 0;
    }

    @Override
    public long getCurrentPower() {
        return power;
    }

    @Override
    public PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        if (this.getGearType() == GearType.COIL)
            return new PowerSourceList().addSource(this);
        else
            return super.getPowerSources(io, caller);
    }

    public void incrementCVTState(boolean on) {
        int i = on ? 1 : 0;
        cvtState[i] = this.getCVTState(on).next();
        while (!this.getCVTState(on).isValid(this)) {
            cvtState[i] = this.getCVTState(on).next();
        }
    }

    private CVTState getCVTState(boolean on) {
        int i = on ? 1 : 0;
        return cvtState[i] != null ? cvtState[i] : CVTState.S1;
    }

    public String getCVTString(boolean on) {
        return this.getCVTState(on).toString();
    }

    public CVTMode getMode() {
        return cvtMode;
    }

    @Override
    public final boolean hasInventory() {
        return this.getGearType().hasInventory();
    }

    @Override
    public final boolean hasTank() {
        return this.getGearType().isLubricated();
    }

    public boolean isCreative() {
        return isCreative;
    }

    public void setCreative(boolean flag) {
        isCreative = flag;
    }

    @Override
    public BlockPos getEmittingPos(BlockPos pos) {
        return new BlockPos(pos.getX() + write.getStepX(), pos.getY() + write.getStepY(), pos.getZ() + write.getStepZ());
    }

    //  todo  @Override
    public boolean canFill(Direction from, Fluid fluid) {
        return this.getGearType().consumesLubricant() && RotaryFluids.LUBRICANT.get().equals(fluid);
    }

/*    @Override
    public FluidTankInfo[] getTankInfo(Direction from) {
        return this.getGearType().consumesLubricant() ? new FluidTankInfo[]{lubricant.getInfo()} : null;
    }*/

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return this.getGearType().consumesLubricant() && (m == MachineRegistry.HOSE || m == MachineRegistry.BEDPIPE);
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.getGearType().consumesLubricant() && (p == MachineRegistry.HOSE || p == MachineRegistry.BEDPIPE);
    }

    @Override
    public int fill(Direction from, FluidStack resource, FluidAction doFill) {
        return this.canFill(from, resource.getFluid()) ? lubricant.fill(resource, doFill) : 0;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @Override
    public @NotNull FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @Override
    public @NotNull FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @Override
    public @NotNull FluidStack drain(int maxDrain, FluidAction action) {
        return null;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return this.getGearType().isLubricated() ? Flow.INPUT : Flow.NONE;
    }

    public void setLubricantFromNBT(CompoundTag NBT) {
        lubricant.setContents(NBT.getInt("lube"), RotaryFluids.LUBRICANT.get());
    }

    @Override
    public boolean isEnabled() {
        return enabled;
    }

    @Override
    public void setEnabled(boolean enable) {
        enabled = enable;
        this.syncAllData(false);
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public BlockEntity getCVT() {
        return null;
    }

    @Override
    public boolean isActive() {
        return false;
    }

    @Override
    public int getControlledRatio() {
        return 0;
    }

    @Override
    public boolean isTorque() {
        return false;
    }

    public enum GearType {
        WORM(),
        CVT(),
        COIL(),
        HIGH();

        public static final GearType[] list = values();

        public boolean isLubricated() {
            return this == CVT || this.consumesLubricant();
        }

        public boolean consumesLubricant() {
            return this == HIGH;
        }

        public boolean hasLosses() {
            return this == WORM;
        }

        public boolean storesEnergy() {
            return this == COIL;
        }

        public boolean hasInventory() {
            return this == CVT;
        }
    }

    private enum CVTState {
        S1(1),
        S2(2),
        S4(4),
        S8(8),
        S16(16),
        S32(32),
        T1(-1),
        T2(-2),
        T4(-4),
        T8(-8),
        T16(-16),
        T32(-32);

        private static final CVTState[] list = values();
        public final int gearRatio;

        CVTState(int ratio) {
            gearRatio = ratio;
        }

        public CVTState next() {
            if (this.ordinal() == list.length - 1)
                return list[0];
            else
                return list[this.ordinal() + 1];
        }

        public boolean isValid(BlockEntityAdvancedGear te) {
            int abs = Math.abs(gearRatio);
            int max = Math.abs(te.getMaxRatio());
            return max >= abs;
        }

        @Override
        public String toString() {
            return Math.abs(gearRatio) + "x " + (gearRatio > 0 ? "Speed" : "Torque");
        }
    }

    public enum CVTMode {
        MANUAL(),
        REDSTONE(),
        AUTO();

        private static final CVTMode[] list = values();

        public CVTMode next() {
            if (this.ordinal() == list.length - 1)
                return list[0];
            else
                return list[this.ordinal() + 1];
        }
    }
}
