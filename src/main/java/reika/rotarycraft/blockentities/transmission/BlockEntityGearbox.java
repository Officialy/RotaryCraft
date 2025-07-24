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
import net.minecraft.network.chat.Component;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fluids.FluidStack;
import net.neoforged.fluids.capability.IFluidHandler;


import reika.dragonapi.DragonAPI;
import reika.dragonapi.ModList;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.ComplexIO;
import reika.rotarycraft.api.power.ShaftPowerEmitter;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.NBTMachine;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.interfaces.SimpleProvider;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.BlockEntity1DTransmitter;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.gui.container.machine.GearboxContainer;
import reika.rotarycraft.registry.*;

import java.util.ArrayList;

//@Strippable(value = {"vazkii.botania.api.mana.IManaReceiver", "reika.chromaticraft.API.Interfaces.Repairable"})
public class BlockEntityGearbox extends BlockEntity1DTransmitter implements PipeConnector, IFluidHandler, TemperatureTE, NBTMachine {//, IManaReceiver, Repairable {

    public static final double BEARINGREDUCTION = 0.25;
    public static final double BEARINGINCREASE = 1.0;
    private static final int MAX_DAMAGE = 480;
    private final HybridTank tank = new HybridTank("gear", 24000);
    private final StepTimer tempTimer = new StepTimer(20);
    public boolean reduction = true; // Reduction gear if true, accelerator if false
    private int damage = 0;
    private GearboxTypes type;
    private boolean failed;
    private int temperature;
    private GearboxTypes bearingTier;

    public BlockEntityGearbox(GearboxTypes type, BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.GEARBOX.get(), pos, state);
        if (type == null)
            type = GearboxTypes.WOOD;
        this.type = type;
        bearingTier = type;
    }

    public static int getDamagePercent(int val) {
        return (int) (100 * (1 - Math.pow(0.99, val)));
    }

    public GearboxTypes getGearboxType() {
        return type != null ? type : GearboxTypes.WOOD;
    }

    public GearboxTypes getBearingTier() {
        return bearingTier != null ? bearingTier : type;
    }

    public void setBearingTier(GearboxTypes tier) {
        bearingTier = tier;
        this.syncAllData(true);
    }


    public void setData(GearboxTypes type, int ratio) {
        this.type = type;
        this.ratio = ratio;
    }

    public void setMaterialFromItem(ItemStack is) {
        type = GearboxTypes.getMaterialFromGearboxItem(is);
        bearingTier = type;
        this.syncAllData(true);
    }

    private int getBearingTierOffset() {
        return this.getBearingTier().material.ordinal() - type.material.ordinal();
    }

    public int getMaxLubricant() {
        return type.getMaxLubricant();
    }

    public int getDamage() {
        return damage;
    }


    public void setDamage(int dmg) {
        damage = dmg;
    }

    public double getDamagedPowerFactor() {
        return Math.pow(0.99, damage);
    }

    public int getDamagePercent() {
        return getDamagePercent(damage);
    }

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
        if (reduction) {
            if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
                omega = spl.omega / ratio; //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = spl.torque / 2 * ratio;
                } else if (favorbent) {
                    torque = spl.torque / sratio * ratio;
                } else {
                    torque = ratio * (int) (spl.torque * ((sratio - 1D) / (sratio)));
                }
            } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
                omega = spl.omega / ratio; //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = spl.torque / 2 * ratio;
                } else if (favorbent) {
                    torque = ratio * (int) (spl.torque * ((sratio - 1D) / (sratio)));
                } else {
                    torque = spl.torque / sratio * ratio;
                }
            } else { //We are not one of its write-to blocks
                torque = 0;
                omega = 0;
                power = 0;
            }
        } else {
            if (pos.getX() == spl.getWritePos().getX() && pos.getZ() == spl.getWritePos().getZ()) { //We are the inline
                omega = spl.omega * ratio; //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = spl.torque / 2 / ratio;
                } else if (favorbent) {
                    torque = spl.torque / sratio / ratio;
                } else {
                    torque = (int) (spl.torque * ((sratio - 1D)) / sratio) / (ratio);
                }
            } else if (pos.getX() == spl.getWritePos2().getX() && pos.getZ() == spl.getWritePos2().getZ()) { //We are the bend
                omega = spl.omega * ratio; //omega always constant
                if (sratio == 1) { //Even split, favorbent irrelevant
                    torque = spl.torque / 2 / ratio;
                } else if (favorbent) {
                    torque = (int) (spl.torque * ((sratio - 1D) / (sratio))) / ratio;
                } else {
                    torque = spl.torque / sratio / ratio;
                }
            } else { //We are not one of its write-to blocks
                torque = 0;
                omega = 0;
                power = 0;
            }
        }
    }

    @Override
    public Block getBlockEntityBlockID() {
        return switch (type) {
            case WOOD -> switch (ratio) {
                case 4 -> RotaryBlocks.WOOD_GEARBOX_4x.get();
                case 8 -> RotaryBlocks.WOOD_GEARBOX_8x.get();
                case 16 -> RotaryBlocks.WOOD_GEARBOX_16x.get();
                default -> RotaryBlocks.WOOD_GEARBOX_2x.get();
            };
            case STONE -> switch (ratio) {
                case 4 -> RotaryBlocks.STONE_GEARBOX_4x.get();
                case 8 -> RotaryBlocks.STONE_GEARBOX_8x.get();
                case 16 -> RotaryBlocks.STONE_GEARBOX_16x.get();
                default -> RotaryBlocks.STONE_GEARBOX_2x.get();
            };
            case STEEL -> switch (ratio) {
                case 4 -> RotaryBlocks.HSLA_GEARBOX_4x.get();
                case 8 -> RotaryBlocks.HSLA_GEARBOX_8x.get();
                case 16 -> RotaryBlocks.HSLA_GEARBOX_16x.get();
                default -> RotaryBlocks.HSLA_GEARBOX_2x.get();
            };
            case TUNGSTEN -> switch (ratio) {
                case 4 -> RotaryBlocks.TUNGSTEN_GEARBOX_4x.get();
                case 8 -> RotaryBlocks.TUNGSTEN_GEARBOX_8x.get();
                case 16 -> RotaryBlocks.TUNGSTEN_GEARBOX_16x.get();
                default -> RotaryBlocks.TUNGSTEN_GEARBOX_2x.get();
            };
            case DIAMOND -> switch (ratio) {
                case 4 -> RotaryBlocks.DIAMOND_GEARBOX_4x.get();
                case 8 -> RotaryBlocks.DIAMOND_GEARBOX_8x.get();
                case 16 -> RotaryBlocks.DIAMOND_GEARBOX_16x.get();
                default -> RotaryBlocks.DIAMOND_GEARBOX_2x.get();
            };
            case BEDROCK -> switch (ratio) {
                case 4 -> RotaryBlocks.BEDROCK_GEARBOX_4x.get();
                case 8 -> RotaryBlocks.BEDROCK_GEARBOX_8x.get();
                case 16 -> RotaryBlocks.BEDROCK_GEARBOX_16x.get();
                default -> RotaryBlocks.BEDROCK_GEARBOX_2x.get();
            };
            case LIVINGWOOD -> null; //todo botania compat
            case LIVINGROCK -> null;
        };
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getIOSides(world, pos, false);

        if ((world.getDayTime() & 31) == 0)
            ReikaWorldHelper.causeAdjacentUpdates(world, pos);

        this.transferPower(world, pos);
        power = (long) omega * (long) torque;
        this.getLubeAndApplyDamage(world, pos);
        tempTimer.update();
        if (tempTimer.checkCap()) {
            this.updateTemperature(world, pos);
        }

        if (!world.isClientSide && power == 0 && this.isLiving() && DragonAPI.rand.nextInt(20) == 0) {
            if (damage > 0 && (!type.needsLubricant() || tank.getFluidLevel() >= 25)) {
                this.repair(1);
                if (type.needsLubricant()) {
                    tank.removeLiquid(25);
                }
            }
        }

        this.basicPowerReceiver();
    }

    private void getLubeAndApplyDamage(Level world, BlockPos pos) {
        int oldlube = 0;
        if (type.needsLubricant() && omega > 0 && this.getBearingTier().material.ordinal() < MaterialRegistry.BEDROCK.ordinal()) {
            if (tank.isEmpty()) {
                if (!world.isClientSide && damage < MAX_DAMAGE && DragonAPI.rand.nextInt(40) == 0 && this.tickcount >= 100) {
                    damage++;
//                    RotaryAdvancements.DAMAGEGEARS.triggerAchievement(this.getPlacer());
                }
                if (DragonAPI.rand.nextDouble() * DragonAPI.rand.nextDouble() > this.getDamagedPowerFactor()) {
                    if (type.material.isFlammable() && !world.isClientSide)
                        ReikaWorldHelper.ignite(world, pos);
                    world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                    if (DragonAPI.rand.nextInt(5) == 0) {
                        world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, type.material.getDamageNoise(), SoundSource.BLOCKS, 1F, 1F, false);
                    }
                }
            } else if (!world.isClientSide && type.consumesLubricant() && this.getBearingTier().material.ordinal() < MaterialRegistry.DIAMOND.ordinal()) {
                if (tickcount >= 80) {
                    tank.removeLiquid(Math.max(1, (int) (DifficultyEffects.LUBEUSAGE.getChance() * this.getLubricantConsumptionFactor())));
                    tickcount = 0;
                }
            }
        }
    }

    private double getLubricantConsumptionFactor() {
        double base = type.getLubricantConsumeRate(omegain) * ReikaMathLibrary.logbase(omegain, 2) / 4;
        if (type != this.getBearingTier()) {
            base *= this.getBearingLubricantFactor();
        }
        return base;
    }

    public double getBearingLubricantFactor() {
        int offset = this.getBearingTierOffset();
//        double pow = Math.pow(0.667, offset);
//        base *= pow;

        double add = offset > 0 ? -BEARINGREDUCTION * offset : -offset * BEARINGINCREASE;
        return Math.max(0.1, 1 + add);
    }

    private void calculateRatio() {
        int tratio = 1;// + this.getBlockMetadata() / 4;
        ratio = (int) ReikaMathLibrary.intpow(2, tratio);
    }

    @Override
    protected void readFromCross(BlockEntityShaft cross) {
        if (cross.isWritingTo(this)) {
            if (reduction) {
                omegain = cross.readomega[0] / ratio;
                torquein = cross.readtorque[0] * ratio;
            } else {
                omegain = cross.readomega[0] * ratio;
                torquein = cross.readtorque[0] / ratio;
            }
        } else if (cross.isWritingTo2(this)) {
            if (reduction) {
                omegain = cross.readomega[1] / ratio;
                torquein = cross.readtorque[1] * ratio;
            } else {
                omegain = cross.readomega[1] * ratio;
                torquein = cross.readtorque[1] / ratio;
            }
        } else {
            omegain = torquein = 0;
            //not its output
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
            if (m == MachineRegistry.WOOD_SHAFT || m ==
                    MachineRegistry.STONE_SHAFT || m ==
                    MachineRegistry.HSLA_SHAFT || m ==
                    MachineRegistry.TUNGSTEN_SHAFT || m ==
                    MachineRegistry.DIAMOND_SHAFT || m ==
                    MachineRegistry.BEDROCK_SHAFT) {
                BlockEntityShaft devicein = (BlockEntityShaft) te;
                if (devicein.isCross()) {
                    this.readFromCross(devicein);
                    performRatio = false;
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            } else if (te instanceof SimpleProvider) {
                this.copyStandardPower(te);
            } else if (te instanceof ComplexIO pwr) {
                Direction dir = this.getInputDirection().getOpposite();
                omegain = pwr.getSpeedToSide(dir);
                torquein = pwr.getTorqueToSide(dir);
            } else if (te instanceof ShaftPowerEmitter sp) {
                if (sp.isEmitting() && sp.canWriteTo(read.getOpposite())) {
                    torquein = sp.getTorque();
                    omegain = sp.getOmega();
                }
            } else if (m == MachineRegistry.SPLITTER) {
                BlockEntitySplitter devicein = (BlockEntitySplitter) te;
                if (devicein.isSplitting()) {
                    this.readFromSplitter(world, pos, devicein);
                    //omegain = reduction ? omega*ratio : omega/ratio;
                    //torquein = reduction ? torque/ratio : torque*ratio;
                    performRatio = false;
                } else if (devicein.isWritingTo(this)) {
                    torquein = devicein.torque;
                    omegain = devicein.omega;
                }
            }
        } //else if (te instanceof WorldRift) {
        //  WorldRift sr = (WorldRift) te;
        //  WorldLocation loc = sr.getLinkTarget();
        //  if (loc != null)
        //     this.transferPower(loc.getWorld(), loc.xCoord, loc.yCoord, loc.zCoord, meta);
//        } else {
//            omega = 0;
//            torque = 0;
//            power = 0;
//            return;
//        }

        if (performRatio) {
            if (reduction) {
                omega = omegain / ratio;
                if (torquein <= (Integer.MAX_VALUE - 1) / 2 /*RotaryConfig.torquelimit*/ / ratio)
                    torque = torquein * ratio;
                else {
                    torque = (Integer.MAX_VALUE - 1) / 2;//RotaryConfig.torquelimit;
                    world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                    world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, type.material.getDamageNoise(), SoundSource.BLOCKS, 0.1F, 1F, true);
                }
            } else {
                if (omegain <= (Integer.MAX_VALUE - 1) / 2 /*RotaryConfig.omegalimit*/ / ratio)
                    omega = omegain * ratio;
                else {
                    omega = (Integer.MAX_VALUE - 1) / 2; //RotaryConfig.omegalimit;
                    world.addParticle(ParticleTypes.CRIT, pos.getX() + DragonAPI.rand.nextFloat(), pos.getY() + DragonAPI.rand.nextFloat(), pos.getZ() + DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), -0.5 + DragonAPI.rand.nextFloat());
                    world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, type.material.getDamageNoise(), SoundSource.BLOCKS, 0.1F, 1F, true);
                }
                torque = torquein / ratio;
            }
        }
        torque *= this.getDamagedPowerFactor();
        if (torque <= 0)
            omega = 0;

        if (!type.material.isInfiniteStrength())
            this.testFailure();
    }

    public void fail(Level world, BlockPos pos) {
        failed = true;
        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 1F, Level.ExplosionInteraction.BLOCK);
        ItemStack item = switch (type) {
            case WOOD, LIVINGWOOD -> new ItemStack(RotaryItems.SAWDUST.get());
            case STONE, LIVINGROCK -> new ItemStack(Blocks.GRAVEL, 1);
            case STEEL -> new ItemStack(RotaryItems.HSLA_STEEL_SCRAP.get());
            case TUNGSTEN -> new ItemStack(RotaryItems.HSLA_STEEL_SCRAP.get());
//            item = RotaryItems.tungstenflakes.copy();
            case DIAMOND -> new ItemStack(Items.DIAMOND, 1);
            case BEDROCK -> new ItemStack(RotaryItems.BEDROCK_DUST.get());
        };
        for (int i = 0; i < this.getRatio(); i++) {
            ReikaItemHelper.dropItem(world, worldPosition.getX() + 0.5, worldPosition.getY() + 1.25, worldPosition.getZ() + 0.5, item);
        }
        world.setBlock(pos, Blocks.AIR.defaultBlockState(), 1);
    }

    public boolean repair(int dmg) {
        if (damage <= 0)
            return false;
        damage -= dmg;
        if (damage < 0)
            damage = 0;
        failed = false;
        return true;
    }

    public void testFailure() {
        if (ReikaEngLibrary.mat_rotfailure(type.material.getDensity(), 0.0625, type.getOmegaForRotFailure(omega, omegain), type.material.getTensileStrength())) {
            this.fail(level, worldPosition);
        } else if (ReikaEngLibrary.mat_twistfailure(Math.max(torque, torquein), 0.0625, type.material.getShearStrength() / 16D)) {
            this.fail(level, worldPosition);
        }
    }

    public int getLubricantScaled(int par1) {
        if (this.getMaxLubricant() == 0)
            return 0;
        return tank.getFluidLevel() * par1 / this.getMaxLubricant();
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putBoolean("reduction", reduction);
        tag.putInt("damage", damage);
        tag.putBoolean("fail", failed);
        tag.putInt("temp", temperature);
        tag.putString("bearing", bearingTier.name());

        tank.writeToNBT(tag);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);
        reduction = tag.getBoolean("reduction");
        damage = tag.getInt("damage");
        failed = tag.getBoolean("fail");
        temperature = tag.getInt("temp");
        if (tag.contains("bearing"))
            bearingTier = GearboxTypes.valueOf(tag.getString("bearing"));

        tank.readFromNBT(tag);
    }

    @Override
    protected String getTEName() {
        return "gearbox";
    }

    //    @Override
    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
    }

    //    @Override

    @Override
    public void load(CompoundTag nbt) {
        GearboxTypes gear = GearboxTypes.WOOD;
        if (nbt.contains("geartype")) {
            gear = GearboxTypes.valueOf(nbt.getString("geartype"));
        } else if (nbt.contains("type")) {
            int idx = nbt.getInt("type");
            if (idx >= MaterialRegistry.TUNGSTEN.ordinal())
                idx++;
            MaterialRegistry mat = MaterialRegistry.matList[idx];
            gear = GearboxTypes.getFromMaterial(mat);
        }
        type = gear;
//        super.load(NBT);
    }

    @Override
    public CompoundTag serializeNBT() {
        CompoundTag nbt = new CompoundTag();
        nbt.putString("geartype", type.name());
        return nbt;
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


    public int getRedstoneOverride() {
        return this.getMaxLubricant() > 0 ? 15 * tank.getFluidLevel() / this.getMaxLubricant() : 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m == MachineRegistry.HOSE || m == MachineRegistry.BEDPIPE;
//        return true;
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side != (isFlipped ? Direction.DOWN : Direction.UP);
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, FluidAction action) {
        if (this.canFill(from, resource.getFluid())) {
            int space = this.getMaxLubricant() - this.getLubricant();
            if (space > 0) {
                if (resource.getAmount() > space)
                    resource = new FluidStack(resource.getFluid(), space);
                return tank.fill(resource, action);
            }
        }
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, FluidAction doDrain) {
        return null;
    }

    @Override
    public void onEMP() {
    }

    public boolean canFill(Direction from, Fluid fluid) {
        return from != (isFlipped ? Direction.DOWN : Direction.UP) && fluid.equals(RotaryFluids.LUBRICANT.get()) && !this.isLiving();
    }

    public int getLubricant() {
        return tank.getFluidLevel();
    }

    public void setLubricant(int amt) {
        tank.setContents(amt, RotaryFluids.LUBRICANT.get());
    }

    public void fillWithLubricant() {
        this.setLubricant(this.getMaxLubricant());
    }

    public boolean canTakeLubricant(int amt) {
        return tank.getFluidLevel() + amt <= this.getMaxLubricant();
    }

    public void addLubricant(int amt) {
        tank.addLiquid(amt, RotaryFluids.LUBRICANT.get());
    }

    public void clearLubricant() {
        tank.empty();
    }

    @Override
    public Flow getFlowForSide(Direction side) {
        return side != (isFlipped ? Direction.DOWN : Direction.UP) ? Flow.INPUT : Flow.NONE;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.GEARBOX;
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (omega > 0 && type.generatesHeat(omega, Tamb)) {
            temperature++;
            ReikaSoundHelper.playSoundAtBlock(world, pos.getX(), pos.getY(), pos.getZ(), type.material.getDamageNoise(), 0.67F, 1);
        } else {
            temperature = Tamb;
        }
        if (temperature > 90 && DragonAPI.rand.nextBoolean() && type.takesTemperatureDamage()) {
            damage++;
            ReikaSoundHelper.playSoundAtBlock(world, pos.getX(), pos.getY(), pos.getZ(), type.material.getDamageNoise(), 1, 1);
        }
        if (omega == 0 && temperature > Tamb) {
            temperature--;
        }
        if (temperature > 120) {
            this.overheat(world, pos);
        }
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public int getThermalDamage() {
        return 0;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        if (type.material.isFlammable() && !world.isClientSide)
            ReikaWorldHelper.ignite(world, pos);
    }

    @Override
    public boolean canBeCooledWithFins() {
        return true;
    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 20;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public int getMaxTemperature() {
        return 1000;
    }

    @Override
    public CompoundTag getTagsToWriteToStack() {
        CompoundTag NBT = new CompoundTag();
        //if (this.getGearboxType().isDamageableGear())
        NBT.putInt("damage", this.getDamage());
        NBT.putInt("lube", this.getLubricant());
        NBT.putString("bearing", bearingTier.name());
        return NBT;
    }

    @Override
    public void setDataFromItemStackTag(CompoundTag tag) {
        if (tag != null) {
            damage = tag.getInt("damage");
            this.setLubricant(tag.getInt("lube"));
            if (tag.contains("bearing")) {
                try {
                    bearingTier = GearboxTypes.valueOf(tag.getString("bearing"));
                } catch (Exception e) {
                    RotaryCraft.LOGGER.error("Invalid gearbox item with data " + tag);
                }
            }
        }
    }

    @Override
    public ArrayList<CompoundTag> getCreativeModeVariants() {
        return new ArrayList<>();
    }

    @Override
    public ArrayList<String> getDisplayTags(CompoundTag NBT) {
        return new ArrayList<>();
    }

    public boolean isLiving() {
        return ModList.BOTANIA.isLoaded() && (type == GearboxTypes.LIVINGROCK || type == GearboxTypes.LIVINGWOOD);
    }

    //    @Override
    public int getCurrentMana() {
        return tank.getFluidLevel();
    }

    //    @Override
    public boolean isFull() {
        return this.getLubricant() + 150 >= this.getMaxLubricant(); //+150 to not have bursts sent and waste 95%
    }

    //    @Override
    public void recieveMana(int mana) {
        tank.addLiquid(Math.min(mana, this.getMaxLubricant() - this.getLubricant()), RotaryFluids.LUBRICANT.get());
    }

    //    @Override
    public boolean canRecieveManaFromBursts() {
        return this.getGearboxType() == GearboxTypes.LIVINGROCK && !this.isFull();
    }

    //    @Override
    public void repair(Level world, BlockPos pos, int tier) {
//        damage = 60;
        int mod = Math.max(1, 64 / ReikaMathLibrary.intpow2(2, tier));
        if (!world.isClientSide && this.tickcount % mod == 0) {
            int amt = Math.max(1, Math.min(damage / 8, (int) (Math.sqrt(tier) / 20D)));
            this.repair(amt);
        }
    }

    @Override
    public int getTanks() {
        return 1;
    }

    
    @Override
    public FluidStack getFluidInTank(int tank) {
        return new FluidStack(RotaryFluids.LUBRICANT.get(), 0);
    }

    @Override
    public int getTankCapacity(int tank) {
        return 24000;
    }

    @Override
    public boolean isFluidValid(int tank,  FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
        return null;
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
        return Component.literal("Gearbox");
    }

    @Override
    public  AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new GearboxContainer(p_39954_, p_39955_, this);
    }
}
