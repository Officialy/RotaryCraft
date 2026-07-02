/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.production;

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
import net.minecraft.world.level.material.MapColor;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;

import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.auxiliary.recipemanagers.FermenterRecipe;
import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFermenter;
import reika.rotarycraft.registry.DurationRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryRecipeTypes;

public class BlockEntityFermenter extends InventoriedPowerLiquidReceiver implements TemperatureTE, MultiOperational, ConditionalOperation {

    public static final int MINUSEFULTEMP = 20;
    public static final int OPTMULTIPLYTEMP = 25;
    public static final int MAXUSEFULTEMP = 40;
    public static final int OPTFERMENTTEMP = 35;
    public static final int MAXTEMP = 60;
    public static final int CAPACITY = 4000;
    public static final int CONSUME_WATER = 50;

    /**
     * The number of ticks that the current item has been cooking for
     */
    public int fermenterCookTime = 0;
    public int temperature;

    public boolean idle = false;

    private int temperaturetick = 0;
    private int tickcount = 0;

    public BlockEntityFermenter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FERMENTER.get(), pos, state);
    }

    // Return the itemstack product from the input items.
    private ItemStack getRecipe() {
        ItemStack catalyst = itemHandler.getStackInSlot(0);
        ItemStack input = itemHandler.getStackInSlot(1);
        if (catalyst.isEmpty() || input.isEmpty() || !this.hasWater())
            return ItemStack.EMPTY;
        if (level == null || level.getServer() == null)
            return ItemStack.EMPTY;
        FermenterRecipe.FermenterInput in = new FermenterRecipe.FermenterInput(catalyst, input);
        return level.getServer().getRecipeManager()
                .getRecipeFor(RotaryRecipeTypes.FERMENTER.get(), in, level)
                .map(h -> h.value().assemble(in).copy())
                .orElse(ItemStack.EMPTY);
    }

    private boolean hasWater() {
        return !tank.isEmpty();
    }

    private float getFermentRate() {
        ItemStack product = this.getRecipe();
        if (product.isEmpty())
            return -1F;
        // yeast production is more temperature-forgiving than the sludge fermentation step
        boolean fermenting = !product.is(RotaryItems.YEAST.get());
        if (temperature < MINUSEFULTEMP)
            return 1F / (MINUSEFULTEMP - temperature);
        if (temperature > MAXUSEFULTEMP)
            return 1F / (temperature - MAXUSEFULTEMP);
        float Tdiff = temperature - OPTMULTIPLYTEMP;
        if (fermenting)
            Tdiff = temperature - OPTFERMENTTEMP;
        if (Tdiff < 0)
            Tdiff = -Tdiff;
        return (float) Math.pow(1 - Tdiff / 16F, 0.2);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FERMENTER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        temperaturetick++;
        tickcount++;
        this.getPower(false);

        if (temperaturetick >= 20) {
            temperaturetick = 0;
            this.updateTemperature(world, pos);
        }

        if (world.isClientSide())
            return;

        if (power < MINPOWER || omega < MINSPEED)
            return;

        int n = this.getNumberConsecutiveOperations();
        for (int i = 0; i < n; i++)
            this.doOperation(n > 1);
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private void doOperation(boolean multiple) {
        ItemStack product = this.getRecipe();

        if (tickcount >= 2 + DragonAPI.rand.nextInt(18)) {
            this.testYeastKill();
            tickcount = 0;
        }

        if (product.isEmpty()) {
            idle = true;
            fermenterCookTime = 0;
            return;
        }

        ItemStack out = itemHandler.getStackInSlot(2);
        if (!out.isEmpty()) {
            if (!ItemStack.isSameItemSameComponents(product, out)) {
                fermenterCookTime = 0;
                return;
            }
            if (out.getCount() + product.getCount() > out.getMaxStackSize()) {
                fermenterCookTime = 0;
                return;
            }
        }
        idle = false;
        fermenterCookTime++;
        if (multiple || fermenterCookTime >= this.getOperationTime()) {
            this.make(product);
            fermenterCookTime = 0;
        }
    }

    private boolean canMake() {
        ItemStack product = this.getRecipe();
        if (product.isEmpty())
            return false;
        ItemStack out = itemHandler.getStackInSlot(2);
        if (out.isEmpty())
            return true;
        if (!ItemStack.isSameItemSameComponents(product, out))
            return false;
        return out.getCount() + product.getCount() <= out.getMaxStackSize();
    }

    private void make(ItemStack product) {
        boolean yeast = product.is(RotaryItems.YEAST.get());

        ItemStack out = itemHandler.getStackInSlot(2);
        if (out.isEmpty())
            itemHandler.setStackInSlot(2, product.copy());
        else if (ItemStack.isSameItemSameComponents(out, product))
            out.setCount(Math.min(out.getMaxStackSize(), out.getCount() + product.getCount()));
        else {
            fermenterCookTime = 0;
            return;
        }

        // both recipes consume the input every cycle and the catalyst only sometimes,
        // so a small amount of catalyst processes a large amount of feedstock
        if (yeast) {
            ReikaInventoryHelper.decrStack(0, itemHandler);
            if (DragonAPI.rand.nextInt(4) == 0)
                ReikaInventoryHelper.decrStack(1, itemHandler);
        } else {
            ReikaInventoryHelper.decrStack(1, itemHandler);
            if (DragonAPI.rand.nextInt(2) == 0)
                ReikaInventoryHelper.decrStack(0, itemHandler);
        }
        tank.removeLiquid(CONSUME_WATER);
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, MapColor.WATER) != null)
            Tamb -= 5;
        if (ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE) != null)
            Tamb -= 15;
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, MapColor.FIRE) != null)
            Tamb += 50;
        if (ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.LAVA) != null)
            Tamb += 200;
        if (temperature > Tamb)
            temperature--;
        if (temperature > Tamb * 2)
            temperature--;
        if (temperature < Tamb)
            temperature++;
        if (temperature * 2 < Tamb)
            temperature++;
        if (temperature > MAXTEMP)
            temperature = MAXTEMP;
    }

    public void testYeastKill() {
        if (temperature < MAXTEMP)
            return;
        int slot = ReikaInventoryHelper.locateInInventory(RotaryItems.YEAST.get(), itemHandler);
        if (slot != -1) {
            ReikaInventoryHelper.decrStack(slot, itemHandler);
        }
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temperature", 0);
        fermenterCookTime = NBT.getShortOr("CookTime", (short) 0);
    }

    @Override
    protected String getTEName() {
        return "fermenter";
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temperature", temperature);
        NBT.putShort("CookTime", (short) fermenterCookTime);
    }

    public int getCookProgressScaled(int par1) {
        return (fermenterCookTime * par1) / 2 / Math.max(1, this.getOperationTime());
    }

    public int getTemperatureScaled(int par1) {
        return (temperature * par1) / MAXTEMP;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FERMENTER;
    }

    @Override
    public boolean isItemValidForSlot(int i, ItemStack is) {
        if (i >= 2)
            return false;
        // slot 0 = catalyst (sugar or yeast), slot 1 = feedstock (dirt or plant matter);
        // the recipe pairing is validated at process time, this just keeps the slots sane
        if (i == 0)
            return is.is(Items.SUGAR) || is.is(RotaryItems.YEAST.get());
        return true;
    }

    @Override
    public int getThermalDamage() {
        return 0;
    }

    @Override
    public int getRedstoneOverride() {
        return this.canMake() ? 0 : 15;
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
    public void overheat(Level world, BlockPos pos) {

    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return true;
    }

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        if (this.canFill(from, resource.getFluid()))
            return tank.fill(resource, action);
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return FluidStack.EMPTY;
    }

    @Override
    public Fluid getInputFluid() {
        return Fluids.WATER;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return true;
    }

    public void setLiquid(int amt) {
        tank.setContents(amt, Fluids.WATER);
    }

    public int getWaterScaled(int par1) {
        return tank.getFluidLevel() * par1 / CAPACITY;
    }

    @Override
    public int getOperationTime() {
        int base = DurationRegistry.FERMENTER.getOperationTime(omega);
        float rate = this.getFermentRate();
        if (rate <= 0)
            return base;
        return Math.max(1, (int) (base / rate));
    }

    @Override
    public int getNumberConsecutiveOperations() {
        float rate = this.getFermentRate();
        if (rate <= 0)
            return 1;
        return (int) Math.max(1, rate * DurationRegistry.FERMENTER.getNumberOperations(omega));
    }

    @Override
    public boolean areConditionsMet() {
        return this.canMake();
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
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
        return 0;
    }

    @Override
    public boolean allowExternalHeating() {
        return true;
    }

    @Override
    public int getMaxTemperature() {
        return MAXTEMP;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return true;
    }

    @Override
    public Component getDisplayName() {
        return Component.literal("Fermenter");
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerFermenter(id, inv, this);
    }
}
