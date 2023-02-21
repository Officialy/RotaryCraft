///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.production;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.level.material.Fluids;
//import net.minecraft.world.level.material.Material;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.MultiOperational;
//import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
//import reika.rotarycraft.base.blockentity.InventoriedPowerLiquidReceiver;
//
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class BlockEntityFermenter extends InventoriedPowerLiquidReceiver implements TemperatureTE, MultiOperational, ConditionalOperation {
//
//    public static final int MINUSEFULTEMP = 20;
//    public static final int OPTMULTIPLYTEMP = 25;
//    public static final int MAXUSEFULTEMP = 40;
//    public static final int OPTFERMENTTEMP = 35;
//    public static final int MAXTEMP = 60;
//    public static final int CAPACITY = 4000;
//    public static final int CONSUME_WATER = 50;
//    /**
//     * The number of ticks that the current item has been cooking for
//     */
//    public int fermenterCookTime = 0;
//    public int temperature;
//
//    public boolean idle = false;
//
//    private int temperaturetick = 0;
//
//    @Override
//    protected int getActiveTexture() {
//        return power >= MINPOWER && omega >= MINSPEED && this.canMake() ? 1 : 0;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == 2;
//    }
//
//    // Return the itemstack product from the input items.
//    private ItemStack getRecipe() {
//        for (int i = 0; i < 2; i++)
//            if (itemHandler.getStackInSlot(i) == null)
//                return null;
//        if (itemHandler.getStackInSlot(0).getItem() == Items.SUGAR) {
//            if (this.hasWater())
//                if (ReikaItemHelper.matchStackWithBlock(itemHandler.getStackInSlot(1), Blocks.DIRT))
//                    return !RotaryConfig.COMMON.enableFermenterYeast() ? null : new ItemStack(RotaryItems.YEAST.get(), 1, 0);
//        }
//        if (itemHandler.getStackInSlot(0).getItem() == RotaryItems.YEAST.get()) {
//            if (MulchMaterials.instance.isMulchable(itemHandler.getStackInSlot(1)))
//                if (this.hasWater())
//                    return ReikaItemHelper.getSizedItemStack(RotaryItems.SLUDGE, MulchMaterials.instance.getPlantValue(itemHandler.getStackInSlot(1)));
//        }
//        return null;
//    }
//
//    private boolean hasWater() {
//        return !tank.isEmpty();
//    }
//
//    private float getFermentRate() {
//        boolean fermenting = true;
//        if (this.getRecipe() == null)
//            return -1F;
//        if (this.getRecipe().getItem() == RotaryItems.YEAST.get())
//            fermenting = false;
//        if (temperature < MINUSEFULTEMP)
//            return 1F / (MINUSEFULTEMP - temperature);
//        if (temperature > MAXUSEFULTEMP)
//            return 1F / (temperature - MAXUSEFULTEMP);
//        float Tdiff = temperature - OPTMULTIPLYTEMP;
//        if (fermenting)
//            Tdiff = temperature - OPTFERMENTTEMP;
//        if (Tdiff < 0)
//            Tdiff = -Tdiff;
//        return (float) Math.pow(1 - Tdiff / 16F, 0.2);
//    }
//
//    public void testIdle() {
//        idle = (this.getRecipe() == null);
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
//        temperaturetick++;
//        tickcount++;
//        this.getIOSidesDefault(world, pos);
//        this.getPower(false);
//
//        if (temperaturetick >= 20) {
//            temperaturetick = 0;
//            this.updateTemperature(world, pos);
//        }
//
//        if (power < MINPOWER || omega < MINSPEED)
//            return;
//
//        int n = this.getNumberConsecutiveOperations();
//        for (int i = 0; i < n; i++)
//            this.doOperation(n > 1);
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private void doOperation(boolean multiple) {
//        ItemStack product = this.getRecipe();
//
//        if (tickcount >= 2 + DragonAPI.rand.nextInt(18)) {
//            this.testYeastKill();
//            tickcount = 0;
//        }
//
//        if (product == null) {
//            idle = true;
//            fermenterCookTime = 0;
//            return;
//        }
//        if (product.getItem() != RotaryItems.YEAST.get() && !ReikaItemHelper.matchStacks(product, RotaryItems.sludge))
//            return;
//
//        if (!itemHandler.getStackInSlot(2).isPresent()) {
//            if (product.getItem() != inv[2].getItem()) {
//                fermenterCookTime = 0;
//                return;
//            }
//        }
//        idle = false;
//        if (!itemHandler.getStackInSlot(2).isPresent()) {
//            if (inv[2].getCount() + product.getCount() > inv[2].getMaxStackSize()) {
//                fermenterCookTime = 0;
//                return;
//            }
//        }
//        fermenterCookTime++;
//        if (multiple || fermenterCookTime >= this.getOperationTime()) {
//            this.make(product);
//            fermenterCookTime = 0;
//        }
//    }
//
//    private boolean canMake() {
//        ItemStack product = this.getRecipe();
//        if (product == null) {
//            return false;
//        }
//        if (product.getItem() != RotaryItems.YEAST.get() && !ReikaItemHelper.matchStacks(product, RotaryItems.SLUDGE))
//            return false;
//        if (!itemHandler.getStackInSlot(2).isPresent()) {
//            if (product.getItem() != inv[2].getItem()) {
//                return false;
//            }
//            return inv[2].getCount() + product.getCount() <= inv[2].getMaxStackSize();
//        }
//        return true;
//    }
//
//    private void make(ItemStack product) {
//        if (product.getItem() == RotaryItems.YEAST.get()) {
//            if (inv[2] == null)
//                inv[2] = new ItemStack(RotaryItems.YEAST.get(), 1, 0);
//            else if (inv[2].getItem() == RotaryItems.YEAST.get()) {
//                if (inv[2].getCount() < inv[2].getMaxStackSize())
//                    inv[2].getCount()++;
//                else
//                    return;
//            } else {
//                fermenterCookTime = 0;
//                return;
//            }
//            ReikaInventoryHelper.decrStack(0, inv);
//            if (DragonAPI.rand.nextInt(4) == 0)
//                ReikaInventoryHelper.decrStack(1, inv);
//        }
//        if (ReikaItemHelper.matchStacks(product, RotaryItems.SLUDGE)) {
//            if (inv[2] == null)
//                inv[2] = product.copy();
//            else if (ReikaItemHelper.matchStacks(inv[2], RotaryItems.SLUDGE)) {
//                int count = inv[2].getCount();
//                if (inv[2].getCount() < inv[2].getMaxStackSize())
//                    inv[2].setCount(count + product.getCount());
//
//                else
//                    return;
//            } else {
//                fermenterCookTime = 0;
//                return;
//            }
//            ReikaInventoryHelper.decrStack(1, inv);
//            if (DragonAPI.rand.nextInt(2) == 0)
//                ReikaInventoryHelper.decrStack(0, inv);
//        }
////        this.markDirty();
//        tank.removeLiquid(CONSUME_WATER);
//    }
//
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//        Direction waterside = ReikaWorldHelper.checkForAdjSourceBlock(world, pos, Material.WATER);
//        if (waterside != null) {
//            Tamb -= 5;
//        }
//        Direction iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
//        if (iceside != null) {
//            Tamb -= 15;
//        }
//        Direction fireside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.FIRE);
//        if (fireside != null) {
//            Tamb += 50;
//        }
//        Direction lavaside = ReikaWorldHelper.checkForAdjSourceBlock(world, pos, Material.LAVA);
//        if (lavaside != null) {
//            Tamb += 200;
//        }
//        if (temperature > Tamb)
//            temperature--;
//        if (temperature > Tamb * 2)
//            temperature--;
//        if (temperature < Tamb)
//            temperature++;
//        if (temperature * 2 < Tamb)
//            temperature++;
//        if (temperature > MAXTEMP)
//            temperature = MAXTEMP;
//    }
//
//    public void testYeastKill() {
//        if (temperature < MAXTEMP)
//            return;
//        int slot = ReikaInventoryHelper.locateInInventory(RotaryItems.YEAST.get(), inv);
//        if (slot != -1) {
//            ReikaInventoryHelper.decrStack(slot, inv);
////            level.playLocalSound(worldPosition.getX(), worldPosition.getY(), worldPosition.getZ(), "DragonAPI.rand.fizz", 0.8F, 0.8F);
//        }
//    }
//
//    public int getContainerSize() {
//        return 3;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        temperature = NBT.getInt("temperature");
//
//        fermenterCookTime = NBT.getShort("CookTime");
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("temperature", temperature);
//        NBT.putShort("CookTime", (short) fermenterCookTime);
//    }
//
//    public int getCookProgressScaled(int par1) {
//        //ReikaChatHelper.writeInt(this.operationTime(0));
//        return (fermenterCookTime * par1) / 2 / this.getOperationTime();
//    }
//
//    public int getTemperatureScaled(int par1) {
//        //ReikaChatHelper.writeInt(this.operationTime(0));
//        return (temperature * par1) / MAXTEMP;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FERMENTER;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int i, ItemStack is) {
//        if (i >= 2)
//            return false;
//        if (this.hasRedstoneSignal() || !RotaryConfig.COMMON.enableFermenterYeast()) {
//            switch (i) {
//                case 0:
//                    return is.getItem() == RotaryItems.YEAST.get();
//                case 1:
//                    return MulchMaterials.instance.isMulchable(is);//ReikaItemHelper.matchStacks(is, RotaryItems.mulch);
//            }
//        } else {
//            switch (i) {
//                case 0:
//                    return is.getItem() == Items.SUGAR;
//                case 1:
//                    return ReikaItemHelper.matchStackWithBlock(is, Blocks.DIRT);
//            }
//        }
//        return false;
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return 0;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (!this.canMake())
//            return 15;
//        return 0;
//    }
//
//    @Override
//    public void addTemperature(int temp) {
//        temperature += temp;
//    }
//
//    @Override
//    public int getTemperature() {
//        return temperature;
//    }
//
//    public void setTemperature(int temp) {
//        temperature = temp;
//    }
//
//    @Override
//    public void overheat(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return true;
//    }
//
//    @Override
//    public Fluid getInputFluid() {
//        return Fluids.WATER;
//    }
//
//    @Override
//    public int getCapacity() {
//        return CAPACITY;
//    }
//
//    @Override
//    public boolean canReceiveFrom(Direction from) {
//        return true;
//    }
//
//    public void setLiquid(int amt) {
//        tank.setContents(amt, Fluids.WATER);
//    }
//
//    @Override
//    public int getOperationTime() {
//        int base = DurationRegistry.FERMENTER.getOperationTime(omega);
//        return Math.max(1, (int) (base / this.getFermentRate()));
//    }
//
//    @Override
//    public int getNumberConsecutiveOperations() {
//        return (int) Math.max(1, this.getFermentRate() * DurationRegistry.FERMENTER.getNumberOperations(omega));
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.canMake();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return true;
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return false;
//    }
//
//    @Override
//    public int getAmbientTemperature() {
//        return 0;
//    }
//
//    @Override
//    public boolean allowExternalHeating() {
//        return true;
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return MAXTEMP;
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
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
//}
