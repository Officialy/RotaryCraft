/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.screens.Screen;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.dragonapi.interfaces.blockentity.ToggleTile;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaEngLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.power.PowerGenerator;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.auxiliary.interfaces.*;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.items.tools.ItemEngineUpgrade;
import reika.rotarycraft.items.tools.ItemIntegratedGearbox;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;
import java.util.Collection;

public abstract class EnergyToPowerBase extends BlockEntityIOMachine implements SimpleProvider, PowerGenerator, GuiController, UpgradeableMachine,
        IFluidHandler, PipeConnector, TemperatureTE, ToggleTile, NBTMachine, IntegratedGearboxable {

    public static final int MAXTEMP = 500;
    public static final int TIERS = 6;
    private static final int MINBASE = -1;
    private static final double[][] efficiencyTable = new double[2][TIERS];

    static {
        for (int i = 0; i < TIERS; i++) {
            efficiencyTable[0][i] = 0.9 - i * 0.08;
            efficiencyTable[1][i] = 1 - Math.pow(i, 1.4) * 0.04;
        }
    }

    private final StepTimer tempTimer = new StepTimer(20);
    private final HybridTank tank = new HybridTank("energytopower", 24000);
    protected int storedEnergy;
    protected int baseomega = -1;
    private Direction facingDir;
    private int temperature;
    private int tier;
    private RedstoneState rsState = RedstoneState.IGNORE;
    private boolean enabled = true;
    private boolean efficient = false;
    private int integratedGear = 0;

    public EnergyToPowerBase(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private static double getEfficiency(int tier, boolean eff) {
        return efficiencyTable[eff ? 1 : 0][tier];
    }

    private static long getTierPower(int tier) {
        return (long) getGenTorque(tier) * ReikaMathLibrary.intpow2(2, getMaxSpeedBase(tier));
    }

    private static int getGenTorque(int tier) {
        return 8 * ReikaMathLibrary.intpow2(4, tier);
    }

    public static int getMaxSpeedBase(int tier) {
        return 8 + tier;
    }

    public static String getTiersAsString() {
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < TIERS; i++) {
            int om = ReikaMathLibrary.intpow2(2, getMaxSpeedBase(i));
            int tq = getGenTorque(i);
            long pwr = getTierPower(i);
            double base = ReikaMathLibrary.getThousandBase(pwr);
            String eng = ReikaEngLibrary.getSIPrefix(pwr);
            String s = String.format("Tier %d - Torque: %dNm; Max Speed: %d rad/s; Power: %.3f%sW", i, tq, om, base, eng);
            sb.append(s);
            sb.append("\n");
        }
        return sb.toString();
    }

    private RedstoneState getRedstoneState() {
        return rsState != null ? rsState : RedstoneState.IGNORE;
    }

    protected double getRelativeEfficiency() {
        return 1;
    }

    private final double getConsumption() {
        return 1D / this.getEfficiency();
    }

    public final double getEfficiency() {
        return getEfficiency(tier, efficient) * this.getRelativeEfficiency();
    }

    public final long getTierPower() {
        return getTierPower(tier);
    }

    public final int getGenTorque() {
        return BlockEntityEngine.getIntegratedGearTorque(getGenTorque(tier), integratedGear);
    }

    public final int getMaxSpeedBase() {
        return getMaxSpeedBase(tier);
    }

    @Override
    public void updateBlockEntity() {
        super.updateBlockEntity();
        if (DragonAPI.debugtest) {
            storedEnergy = this.getMaxStorage();
            tank.setContents(tank.getCapacity(), RotaryFluids.LIQUID_NITROGEN.get());
        }
        if (storedEnergy < 0) {
            storedEnergy = 0;
        }
        tempTimer.update();
        if (tempTimer.checkCap()) {
            this.updateTemperature(level, worldPosition);
        }
    }

    public final int getTier() {
        return tier;
    }

    @Override
    public final void upgrade(ItemStack item) {
        if (item.getTag().getInt("upgradeType") == ItemEngineUpgrade.UpgradeType.EFFICIENCY.ordinal()) {
            efficient = true;
        } else {
            tier++;
        }
        this.syncAllData(true);
    }

    public final boolean canUpgradeWith(ItemStack item) {
        if (!efficient && item.getTag().getInt("upgradeType") == ItemEngineUpgrade.UpgradeType.EFFICIENCY.ordinal())
            return true;
        if (tier >= 5)
            return false;
        if (item.getTag().getInt("upgradeType") == 2) {
            if (item.getTag() == null)
                return false;
            if (item.getTag().getInt("magnet") < 720)
                return false;
        }
        return RotaryItems.UPGRADE.get() == item.getItem() && (item.getTag().getInt("upgradeType") == tier + 1);
    }

    protected final boolean isMuffled() {
        Level world = level;
        int x = worldPosition.getX();
        int y = worldPosition.getY();
        int z = worldPosition.getZ();

        if (world.getBlockState(new BlockPos(x, y + 1, z)).getBlock() == Blocks.BLACK_WOOL && world.getBlockState(new BlockPos(x, y - 1, z)).getBlock() == Blocks.BLACK_WOOL) {
            return true;
        }
        for (int i = 0; i < 6; i++) {
            Direction dir = Direction.values()[i];
            if (dir != Direction.DOWN) {
                int dx = x + dir.getStepX();
                int dy = y + dir.getStepY();
                int dz = z + dir.getStepZ();
                if ((dir != write.getOpposite() && dir != write) || dir == Direction.UP) {
                    Block b = world.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
                    if (b != Blocks.WHITE_WOOL)
                        return false;
                }
            }
        }
        return true;
    }

    public final int getTierFromPowerOutput(long power) {
        for (int i = 0; i < TIERS; i++) {
            long tier = getTierPower(i);
            if (tier >= power)
                return i;
        }
        return 0;
    }

    public abstract boolean isValidSupplier(BlockEntity te);

    public boolean isRedstoneControlEnabled() {
        return this.getRedstoneState() != RedstoneState.IGNORE;
    }

    public ItemStack getRedstoneStateIcon() {
        return this.getRedstoneState().getDisplayIcon();
    }

    public boolean isEmitting() {
        if (!enabled)
            return false;
        if (this.isRedstoneControlEnabled()) {
            RedstoneState rs = this.getRedstoneState();
            return level.hasNeighborSignal(worldPosition) ? rs == RedstoneState.HI : rs == RedstoneState.LOW;
        } else
            return true;
    }

    public void incrementRedstoneState() {
        rsState = this.getRedstoneState().next();
    }

    public final int getStoredPower() {
        return storedEnergy;
    }

    //public final void setStoredPower(int e) {
    //	storedEnergy = e;
    //}

    public abstract int getMaxStorage();

    public final long getPowerLevel() {
        return this.isEmitting() ? this.getMaxSpeed() * (long) this.getActualTorque() : 0;
    }

    public final int getSpeed() {
        return omega;
    }

    public final int getMaxSpeed() {
        if (baseomega < 0)
            return 0;
        return BlockEntityEngine.getIntegratedGearSpeed(ReikaMathLibrary.intpow2(2, baseomega), integratedGear);
    }

    protected final void updateSpeed() {
        int maxspeed = this.isEmitting() ? this.getMaxSpeed() : 0;
        float mult = 1;
        boolean accel = omega <= maxspeed && this.hasEnoughEnergy() && !this.isShutdown();
        if (accel) {
            if (omega < maxspeed)
                mult = 1.5F;
            omega += 4 * ReikaMathLibrary.logbase(maxspeed + 1, 2);
            if (omega > maxspeed)
                omega = maxspeed;
        } else {
            if (omega > 0) {
                omega -= omega / 256 + 1;
            }
        }
        torque = this.getActualTorque();
        power = (long) torque * (long) omega;
        if (power > 0 && !level.isClientSide) {
            this.usePower(mult);
            //if (level.getDayTime()%(21-4*tier) == 0) {
            //	tank.removeLiquid(1);
            //}
        }
    }

    protected void usePower(float mult) {
        storedEnergy -= this.getConsumedUnitsPerTick() * mult;
        if (storedEnergy < 0)
            storedEnergy = 0;
    }

    public final int getActualTorque() {
        return omega > 0 ? this.getGenTorque() : 0;
    }

    public final boolean hasEnoughEnergy() {
        float energy = this.getStoredPower();
        return energy > this.getConsumedUnitsPerTick();
    }

    protected abstract int getIdealConsumedUnitsPerTick();

    public final int getConsumedUnitsPerTick() {
        return Mth.ceil(this.getIdealConsumedUnitsPerTick() * this.getConsumption());
    }

    public final void setDataFromItemStackTag(CompoundTag nbt) {
        if (nbt != null) {
            tier = nbt.getInt("tier");
            efficient = nbt.getBoolean("efficient");
            storedEnergy = nbt.getInt("energy");
            int c = nbt.getInt("coolant");
            if (c > 0)
                tank.setContents(c, RotaryFluids.LIQUID_NITROGEN.get());
        }
    }

    @Override
    public final ArrayList<CompoundTag> getCreativeModeVariants() {
        ArrayList<CompoundTag> li = new ArrayList<>();
        li.add(new CompoundTag());
        CompoundTag tag = new CompoundTag();
        tag.putInt("tier", TIERS - 1);
        li.add(tag);
        return li;
    }

    @Override
    public final ArrayList<String> getDisplayTags(CompoundTag NBT) {
        ArrayList<String> li = new ArrayList<>();
        this.setDataFromItemStackTag(NBT);
        li.add(String.format("Tier %d", tier));
        if (efficient)
            li.add(ChatFormatting.GOLD + "Efficiency Boost");
        if (Screen.hasShiftDown()) {
            int torque = this.getGenTorque();
            int speed = ReikaMathLibrary.intpow2(2, getMaxSpeedBase(tier));
            long power = (long) torque * (long) speed;
            double val = ReikaMathLibrary.getThousandBase(power);
            String exp = ReikaEngLibrary.getSIPrefix(power);
            li.add(String.format("Torque: %d Nm", torque));
            li.add(String.format("Max Speed: %d rad/s", speed));
            li.add(String.format("Max Power: %.3f%sW", val, exp));
        } else {
            StringBuilder sb = new StringBuilder();
            sb.append("Hold ");
            sb.append(ChatFormatting.GREEN);
            sb.append("Shift");
            sb.append(ChatFormatting.GRAY);
            sb.append(" for power data");
            li.add(sb.toString());
        }
        return li;
    }

    public final CompoundTag getTagsToWriteToStack() {
        CompoundTag nbt = new CompoundTag();
        nbt.putInt("tier", tier);
        nbt.putBoolean("efficient", efficient);
        nbt.putInt("energy", storedEnergy);
        nbt.putInt("coolant", tank.getFluidLevel());
        return nbt;
    }

    public final void incrementOmega() {
        if (baseomega < this.getMaxSpeedBase()) {
            baseomega++;
            while (baseomega < this.getMaxSpeedBase() && this.getMaxSpeed() == 0) {
                baseomega++;
            }
        }
    }

    public final void decrementOmega() {
        if (baseomega > MINBASE)
            baseomega--;
    }

    public final boolean setOmega(int base) {
        if (base >= MINBASE && base <= this.getMaxSpeedBase()) {
            baseomega = base;
            return true;
        } else {
            return false;
        }
    }

    public final int getEnergyScaled(int h) {
        return (int) (this.getPercentStorage() * h);
    }

    public final float getPercentStorage() {
        return this.getStoredPower() / (float) this.getMaxStorage();
    }

    protected final void getIOSides(Level world, BlockPos pos, Direction dir) {
        switch (dir) {
            case NORTH -> facingDir = Direction.NORTH;
            case WEST -> facingDir = Direction.WEST;
            case SOUTH -> facingDir = Direction.SOUTH;
            case EAST -> facingDir = Direction.EAST;
        }
        read = facingDir;
        write = read.getOpposite();
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tag.putInt("storage", storedEnergy);

        tag.putInt("tiero", baseomega);

        tag.putInt("rs", this.getRedstoneState().ordinal());

        if (baseomega > getMaxSpeedBase(tier)) {
            baseomega = MINBASE;
        }
        tag.putInt("level", tier);

        tank.writeToNBT(tag);

        tag.putInt("temp", temperature);

        tag.putBoolean("t_enable", enabled);

        tag.putBoolean("efficient", efficient);

        tag.putInt("gear", integratedGear);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        storedEnergy = tag.getInt("storage");

        rsState = RedstoneState.list[tag.getInt("rs")];

        tier = tag.getInt("level");

        if (baseomega > getMaxSpeedBase(tier)) {
            baseomega = MINBASE;
        }

        baseomega = tag.getInt("tiero");

        tank.readFromNBT(tag);

        temperature = tag.getInt("temp");

        if (tag.contains("t_enable"))
            enabled = tag.getBoolean("t_enable");

        efficient = tag.getBoolean("efficient");

        integratedGear = tag.getInt("gear");
    }

    @Override
    public final long getCurrentPower() {
        return power;
    }

    @Override
    public final long getMaxPower() {
        return this.getTierPower();
    }

    @Override
    public final PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller) {
        return new PowerSourceList().addSource(this);
    }

    @Override
    public final boolean canProvidePower() {
        return true;
    }

    public abstract String getUnitDisplay();

    public abstract int getPowerColor();

    public final Direction getFacing() {
        return facingDir != null ? facingDir : Direction.EAST;
    }

    public final BlockEntity getProvidingBlockEntity() {
        int x = worldPosition.getX() + this.getFacing().getStepX();
        int y = worldPosition.getY() + this.getFacing().getStepY();
        int z = worldPosition.getZ() + this.getFacing().getStepZ();
        BlockEntity te = level.getBlockEntity(worldPosition);
        return te;
    }

    @Override
    public BlockPos getEmittingPos(BlockPos pos) {
        return new BlockPos(pos.getX() + write.getStepX(), pos.getY() + write.getStepY(), pos.getZ() + write.getStepZ());
    }

    @Override
    public int fill(Direction from, FluidStack resource, FluidAction fluidaction) {
        return this.canFill(from, resource.getFluid()) ? tank.fill(resource, fluidaction) : 0;
    }

    //@Override
    public boolean canFill(Direction from, Fluid fluid) {
        return fluid.equals(RotaryFluids.LIQUID_NITROGEN);
    }

    //    @Override
    public boolean canDrain(Direction from, Fluid fluid) {
        return false;
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return p.isStandardPipe();
    }

    //@Override
    public Flow getFlowForSide(Direction side) {
        return Flow.INPUT;
    }

    public final int getLubricant() {
        return tank.getFluidLevel();
    }

    public final int getMaxLubricant() {
        return tank.getCapacity();
    }

    public final int getLubricantScaled(int a) {
        return tank.getFluidLevel() * a / tank.getCapacity();
    }

    @Override
    public final void updateTemperature(Level world, BlockPos pos) {
        int Tamb = tank.isEmpty() ? ReikaWorldHelper.getAmbientTemperatureAt(world, pos) : 25;
        if (power > 0) {
            double d = tank.getFluidLevel() >= 50 ? 0.00275 : 0.14;
            double inc = d * Math.sqrt(power) + ReikaMathLibrary.logbase(tier + 1, 2);
            //ReikaJavaLibrary.pConsole(inc);
            temperature += inc;
            if (temperature > Tamb && !tank.isEmpty()) {
                int drain = Math.max(2, 50 * temperature / 500);
                tank.removeLiquid(drain);
            }
        }
        if (temperature > Tamb) {
            temperature -= (temperature - Tamb) / 16;
        } else {
            temperature += (temperature - Tamb) / 16;
        }
        if (temperature - Tamb <= 16 && temperature > Tamb)
            temperature--;
        if (temperature > MAXTEMP) {
            temperature = MAXTEMP;
            if (!world.isClientSide)
                this.overheat(world, pos);
        }
        if (temperature < Tamb)
            temperature = Tamb;
    }

    @Override
    public final void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public final int getTemperature() {
        return temperature;
    }

    public final void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public final int getThermalDamage() {
        return 0;
    }

    @Override
    public final void overheat(Level world, BlockPos pos) {
        //this.delete();
        world.explode(null, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, 3, true, Level.ExplosionInteraction.BLOCK);
    }

    @Override
    public final boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public final boolean allowExternalHeating() {
        return false;
    }

    @Override
    public final boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public void getAllOutputs(Collection<BlockEntity> c, Direction dir) {
        if (dir == read)
            c.add(getAdjacentBlockEntity(write));
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public final boolean isEnabled() {
        return enabled;
    }

    @Override
    public final void setEnabled(boolean enable) {
        enabled = enable;
        //this.syncAllData(false);
    }

    public final void breakBlock() {
        if (integratedGear != 0) {
            ItemStack is = ItemIntegratedGearbox.getIntegratedGearItem(integratedGear, null);
            ReikaItemHelper.dropItem(level, new BlockPos(
                    worldPosition.getX() + DragonAPI.rand.nextDouble(), worldPosition.getY() + DragonAPI.rand.nextDouble(), worldPosition.getZ() + DragonAPI.rand.nextDouble()), is);
        }
    }

    public final boolean applyIntegratedGear(ItemStack is) {
        if (is == null || is == RotaryItems.INTEGRATED_GEARBOX.get().getDefaultInstance())//matchItem(is))
            return false;
        if (integratedGear != 0)
            return false;
        if (omega > 0 || power > 0)
            return false;
        integratedGear = ItemIntegratedGearbox.getRatioFromIntegratedGearItem(is, true);
        //this.syncAllData(true);
        return integratedGear != 0;
    }

    public final int getIntegratedGear() {
        return integratedGear;
    }

    private enum RedstoneState {
        IGNORE(Items.GUNPOWDER),
        LOW(Blocks.REDSTONE_TORCH),
        HI(Blocks.REDSTONE_TORCH);

        public static final RedstoneState[] list = values();
        private final ItemStack iconItem;

        RedstoneState(Item i) {
            this(new ItemStack(i));
        }

        RedstoneState(Block i) {
            this(new ItemStack(i));
        }

        RedstoneState(ItemStack is) {
            iconItem = is.copy();
        }

        public ItemStack getDisplayIcon() {
            return iconItem.copy();
        }

        public RedstoneState next() {
            return this.ordinal() < list.length - 1 ? list[this.ordinal() + 1] : list[0];
        }
    }

}
