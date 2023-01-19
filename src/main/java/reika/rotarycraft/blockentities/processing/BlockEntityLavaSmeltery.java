/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.processing;


import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.Nullable;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.interfaces.blockentity.XPProducer;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerBigFurnace;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntityLavaSmeltery extends InventoriedPowerLiquidReceiver implements TemperatureTE, XPProducer, DiscreteFunction, ConditionalOperation {

    public static final int HEIGHT = 2;
    public static final int WIDTH = 9;

    public static final int MAXTEMP = 1200;

    public static final int SMELT_TEMP = 400;
    private final StepTimer smelter = new StepTimer(200);
    private final StepTimer tempTimer = new StepTimer(20);
    public int smeltTick;
    private float xp;
    private int temperature;

    public BlockEntityLavaSmeltery(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BIG_FURNACE.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.LAVA_SMELTORY.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getPowerBelow();
        tempTimer.update();
        if (tempTimer.checkCap())
            this.updateTemperature(world, pos);

        smelter.setCap(this.getOperationTime());

        if (!level.isClientSide) {
            if (this.canSmelt()) {
                smelter.update();
                if (smelter.checkCap())
                    if (!level.isClientSide)
                        this.smelt();
            } else
                smelter.reset();
        }
        smeltTick = smelter.getTick();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    public int getNumberInputSlots() {
        return WIDTH * HEIGHT;
    }

    private void smelt() {
        int n = this.getNumberInputSlots();
        for (int i = 0; i < n; i++) {
            ItemStack is = inv[i];
            if (is != null) {
                ItemStack to = new ItemStack(Items.STONE);// = FurnaceRecipes.smelting().getSmeltingResult(is);
                if (to != null) {
                    boolean add = false;
                    if (inv[i + n] == null) {
                        inv[i + n] = to.copy();
                        add = true;
                    } else {
                        if (ReikaItemHelper.areStacksCombinable(to, inv[i + n], this.getInventoryStackLimit())) {
                            add = true;
                            int count = inv[i + n].getCount();
                            inv[i + n].setCount(count + to.getCount());
                        }
                    }
                    if (add)
                        ReikaInventoryHelper.decrStack(i, inv);
                }
            }
        }
    }

    private boolean canSmelt() {
        if (temperature < SMELT_TEMP)
            return false;
        if (power < MINPOWER)
            return false;
        int n = this.getNumberInputSlots();
        for (int i = 0; i < n; i++) {
            ItemStack is = inv[i];
            if (is != null) {
                ItemStack to = new ItemStack(Items.STONE);//FurnaceRecipes.smelting().getSmeltingResult(is);
                if (to != null) {
                    return true;
                }
            }
        }
        return false;
    }

    //    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i >= this.getNumberInputSlots();
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);

        if (!tank.isEmpty()) {
            tank.removeLiquid(15);
            FluidStack f = tank.getActualFluid();
            if (f.equals(Fluids.LAVA))
                Tamb += 600;
//            else if (f.equals(Fluids.getFluid("pyrotheum")))
//                Tamb += 1000;
        }

        if (temperature > Tamb)
            temperature--;
        if (temperature > Tamb * 2)
            temperature--;
        if (temperature < Tamb)
            temperature++;
        if (temperature * 2 < Tamb)
            temperature++;
        if (temperature > MAXTEMP) {
            temperature = MAXTEMP;
            this.overheat(world, pos);
        }
        if (temperature > 100) {
            Direction side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW);
            if (side != null)
                ReikaWorldHelper.changeAdjBlock(world, pos, side, Blocks.AIR.defaultBlockState());
            side = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
            if (side != null)
                ReikaWorldHelper.changeAdjBlock(world, pos, side, Fluids.FLOWING_WATER.getFlowing().defaultFluidState().createLegacyBlock());
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
        return temperature / 200;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {

    }

    @Override
    public boolean isItemValidForSlot(int slot, ItemStack is) {
        return slot < this.getNumberInputSlots();
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BIGFURNACE;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }

    @Override
    public Fluid getInputFluid() {
        return null;
    }

    @Override
    public boolean isValidFluid(Fluid f) {
        if (f == null)
            return false;
        return f == Fluids.LAVA;// || f == Fluids.getFluid("pyrotheum");
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return from.getStepY() == 0;
    }

    @Override
    public int getCapacity() {
        return 16000;
    }

    @Override
    public void clearXP() {
        xp = 0;
    }

    @Override
    public float getXP() {
        return xp;
    }

    public int getCookScaled(int i) {
        return smeltTick * i / smelter.getCap();
    }

    public int getLavaScaled(int i) {
        return tank.getFluidLevel() * i / tank.getCapacity();
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag);

        temperature = tag.getInt("temp");
        xp = tag.getFloat("xp");
    }

    @Override
    protected String getTEName() {
        return "bigfurnace";
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);

        tag.putInt("temp", temperature);
        tag.putFloat("xp", xp);
    }

    @Override
    public int getOperationTime() {
        int base = temperature >= 600 ? 150 : 200;
        return temperature >= 1000 ? base / 2 : base;
    }

    @Override
    public boolean areConditionsMet() {
        return this.canSmelt();
    }

    @Override
    public String getOperationalStatus() {
        if (temperature < SMELT_TEMP)
            return "Insufficient Temperature";
        return this.areConditionsMet() ? "Operational" : "No Smeltable Items";
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public int getContainerSize() {
        return WIDTH * HEIGHT * 2;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Big Furnace");
    }

    @Override
    public @Nullable AbstractContainerMenu createMenu(int p_39954_, Inventory p_39955_, Player p_39956_) {
        return new ContainerBigFurnace(p_39954_, p_39955_, this);
    }
}
