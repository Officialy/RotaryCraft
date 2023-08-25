///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.processing;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.biome.Biome;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.auxiliary.RotaryAux;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.auxiliary.interfaces.*;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class BlockEntityCompactor extends InventoriedPowerReceiver implements TemperatureTE, PressureTE, FrictionHeatable,
//        MultiOperational, ConditionalOperation {
//
//    public static final int MAXTEMP = 1000;
//    public static final int MAXPRESSURE = 600000; //All pressures in kPa, steel yield strength = 250Mpa
//    // public static final int MAXTIME = 2000;
//    public static final int REQTEMP = 800;        //real temp/2
//    public static final int REQPRESS = 550000; //real pressure
//    /**
//     * The number of ticks that the current item has been cooking for
//     */
//    public int compactorCookTime;
//    public int temperature;
//    public boolean idle = false;
//    private int pressure;
//    private boolean animdir = false;
//
//    private int envirotick = 0;
//    private int tempTick;
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return i == 4;
//    }
//
//    public void testIdle() {
//        boolean ingred = false;
//        boolean invalid = false;
//        for (int i = 0; i < 4; i++) {
//            if (itemHandler.getStackInSlot(i) == null)
//                invalid = true;
//        }
//        if (!invalid) {
//            Item id = itemHandler.getStackInSlot(0).getItem();
//            int dmg = itemHandler.getStackInSlot(0).getItemDamage();
//            for (int i = 1; i < 4; i++) {
//                if (itemHandler.getStackInSlot(i).getItem() != id || itemHandler.getStackInSlot(i).getItemDamage() != dmg)
//                    invalid = true;
//            }
//        }
//        if (!invalid) {
//            if (RecipesCompactor.getRecipes().isCompactable(itemHandler.getStackInSlot(0)))
//                ingred = true;
//        }
//        boolean full = true;
//        if (inv[4] == null)
//            full = false;
//        else if (inv[4].getCount() < inv[4].getMaxStackSize())
//            full = false;
//        idle = (!ingred || full);
//    }
//
//    public boolean getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 0:
//                read = Direction.EAST;
//                break;
//            case 1:
//                read = Direction.WEST;
//                break;
//            case 2:
//                read = Direction.SOUTH;
//                break;
//            case 3:
//                read = Direction.NORTH;
//                break;
//        }
//        //ReikaWorldHelper.legacySetBlockWithNotify(world, powinx, y, powinz, 4);
//        return true;
//    }
//
//    public void readPower() {
//        if (!this.getIOSides(level, xCoord, yCoord, zCoord, this.getBlockMetadata()))
//            return;
//        super.getPower(false);
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", Math.max(2, 1200-this.omega, "max")));
//        return;
//    }
//
//    /**
//     * Returns the number of slots in the inventory.
//     */
//    public int getContainerSize() {
//        return 5;
//    }
//
//    /**
//     * Reads a tile entity from NBT.
//     */
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        compactorCookTime = NBT.getShort("CookTime");
//        temperature = NBT.getInt("temperature");
//        pressure = NBT.getInt("pressure");
//    }
//
//    /**
//     * Writes a tile entity to NBT.
//     */
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putShort("CookTime", (short) compactorCookTime);
//        NBT.putInt("temperature", temperature);
//        NBT.putInt("pressure", pressure);
//    }
//
//    /**
//     * Returns an integer between 0 and the passed value representing how close the current item is to being completely
//     * cooked
//     */
//    public int getCookProgressScaled(int par1) {
//        int time = this.getOperationTime();
//        return time > 0 ? (compactorCookTime * par1) / time : 0;
//    }
//
//    public int getPressureScaled(int par1) {
//        return (pressure * par1) / MAXPRESSURE;
//    }
//
//    public int getTemperatureScaled(int par1) {
//        return (temperature * par1) / MAXTEMP;
//    }
//
//    public int compressTime() {
//        ItemStack is1 = itemHandler.getStackInSlot(0);
//        ItemStack is2 = itemHandler.getStackInSlot(1);
//        ItemStack is3 = inv[2];
//        ItemStack is4 = inv[3];
//
//        if (is1 == null || is2 == null || is3 == null || is4 == null)
//            return -1;/*
//    	if (!is1.equals(is2))
//    		return -1;
//    	if (!is1.equals(is3))
//    		return -1;
//    	if (!is1.equals(is4))
//    		return -1;*/
//
//        Item item = is1.getItem();
//        if (item != RotaryItems.COMPACTS.get() && item != Items.coal)
//            return -1;
//
//        if (item == Items.coal)
//            return 80;
//        switch (meta) {
//            case 0:
//                return 160;
//            case 1:
//                return 320;
//            case 2:
//                return 640;
//            default:
//                return -1;
//        }
//    }
//
//    public void updatePressure(Level world, BlockPos pos) {
//        int Pamb = (int) ReikaWorldHelper.getAmbientPressureAt(world, pos, true);
//
//        if (pressure > Pamb && world.getBiomeGenForCoords(x, z) != Biome.hell)
//            pressure -= Math.max((pressure - Pamb) / 200, 1);
//        if (pressure > Pamb && world.getBiomeGenForCoords(x, z) == Biome.hell)
//            pressure -= Math.max((pressure - Pamb) / 600, 1);
//        if (pressure < Pamb)
//            pressure += Math.max((Pamb - pressure) / 40, 1);
//
//        if (omega > 0) {
//            pressure += 128 * ReikaMathLibrary.logbase(torque, 2);
//        }
//
//        if (pressure >= 0.8 * MAXPRESSURE) {
//            RotaryCraft.LOGGER.warn("WARNING: " + this + " is reaching very high pressure!");
//        }
//
//        if (pressure > MAXPRESSURE) {
//            this.overpressure(world, pos);
//        }
//    }
//
//    public void updateTemperature(Level world, BlockPos pos) {
//        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
//        if (temperature > Tamb)
//            temperature -= Math.max((temperature - Tamb) / 200, 1);
//        if (temperature < Tamb)
//            temperature += Math.max((Tamb - temperature) / 40, 1);
//
//        //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", 0));
//
//        if (RotaryAux.isNextToLava(world, pos))
//            temperature += 4;
//        if (RotaryAux.isNextToFire(world, pos))
//            temperature += 2;
//        if (Tamb == 300)    //Fire is 50% hotter in the nether
//            temperature++;
//
//        Direction a = ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.water);
//        if (a != null && temperature > 600) {
//            temperature--;
//            if (DragonAPI.rand.nextInt(4000) == 0)
//                ReikaWorldHelper.changeAdjBlock(world, pos, a, Blocks.AIR, 0);
//        }
//        Direction iceside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.ICE);
//        if (iceside != null && temperature > 0) {
//            temperature -= 2;
//            if (DragonAPI.rand.nextInt(200) == 0)
//                ReikaWorldHelper.changeAdjBlock(world, pos, iceside, Blocks.flowing_water, 0);
//        }
//        Direction snowside = ReikaWorldHelper.checkForAdjBlock(world, pos, Blocks.SNOW);
//        if (snowside != null && temperature > -5) {
//            temperature -= 2;
//            if (DragonAPI.rand.nextInt(100) == 0)
//                ReikaWorldHelper.changeAdjBlock(world, pos, iceside, Blocks.flowing_water, 0);
//        }
//        ReikaWorldHelper.temperatureEnvironment(world, pos, temperature);
//
//        if (temperature >= 0.9 * MAXTEMP) {
//            RotaryCraft.LOGGER.warn("WARNING: " + this + " is reaching very high temperature!");
//        }
//
//        if (temperature > MAXTEMP) {
//            this.overheat(world, pos);
//            temperature = MAXTEMP;
//        }
//    }
//
//    public void overheat(Level world, BlockPos pos) {
//        temperature = MAXTEMP;
//        ReikaWorldHelper.overheat(world, pos, RotaryItems.HSLA_STEEL_SCRAP, 0, 17, true, 1F, false, RotaryConfig.COMMON.BLOCKDAMAGE.get(), 2F);
//        world.setBlockToAir(pos);
//    }
//
//    public int getStage() {
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return 1;
//        if (!RecipesCompactor.getRecipes().isCompactable(itemHandler.getStackInSlot(0)))
//            return 1;
//        if (itemHandler.getStackInSlot(0).getItem() == Items.coal)
//            return 1;
//        if (ReikaItemHelper.matchStacks(RotaryItems.anthracite, itemHandler.getStackInSlot(0)))
//            return 2;
//        if (ReikaItemHelper.matchStacks(RotaryItems.prismane, itemHandler.getStackInSlot(0)))
//            return 3;
//        if (ReikaItemHelper.matchStacks(RotaryItems.lonsda, itemHandler.getStackInSlot(0)))
//            return 4;
//        return 1;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getIOSides(world, pos, meta);
//        this.getPower(false);
//        if (envirotick >= 20) {
//            this.updatePressure(world, pos, meta);
//            if (tempTick == 0)
//                this.updateTemperature(world, pos, meta);
//            envirotick = 0;
//        }
//        this.testIdle();
//        boolean flag1 = false;
//        envirotick++;
//        if (tempTick > 0)
//            tempTick--;
//        tickcount++;
//        //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d  %d  %d", this.power, this.omega, this.torque));
//        if (!world.isClientSide) {
//            int n = this.getNumberConsecutiveOperations();
//            for (int i = 0; i < n; i++)
//                flag1 |= this.doOperation(n > 1);
//        }
//        if (flag1)
//            this.markDirty();
//    }
//
//    private boolean doOperation(boolean multiple) {
//        if (this.canSmelt()) {
//            compactorCookTime++;
//            //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", Math.max(2, 600-this.omega, "max")));
//            if (compactorCookTime >= this.getOperationTime()) {
//                compactorCookTime = 0;
//                this.smeltItem();
//            }
//            return true;
//        } else {
//            compactorCookTime = 0;
//            return false;
//        }
//    }
//
//    /**
//     * Returns true if the furnace can smelt an item, i.e. has a source item, destination stack isn't full, etc.
//     */
//    private boolean canSmelt() {
//        this.readPower();
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", power));
//        if (power < MINPOWER || torque < MINTORQUE)
//            return false;
//
//        for (int i = 0; i < 4; i++)
//            if (itemHandler.getStackInSlot(i) == null)
//                return false;
//
//        if (itemHandler.getStackInSlot(0).getItem() != itemHandler.getStackInSlot(1).getItem())
//            return false;
//        if (itemHandler.getStackInSlot(0).getItem() != inv[2].getItem())
//            return false;
//        if (itemHandler.getStackInSlot(0).getItem() != inv[3].getItem())
//            return false;
//        if (itemHandler.getStackInSlot(0).getItemDamage() != itemHandler.getStackInSlot(1).getItemDamage())
//            return false;
//        if (itemHandler.getStackInSlot(0).getItemDamage() != inv[2].getItemDamage())
//            return false;
//        if (itemHandler.getStackInSlot(0).getItemDamage() != inv[3].getItemDamage())
//            return false;
//
//        if (pressure < RecipesCompactor.getRecipes().getReqPressure(itemHandler.getStackInSlot(0)) || temperature < RecipesCompactor.getRecipes().getReqTemperature(itemHandler.getStackInSlot(0)))
//            return false;
//
//        ItemStack itemstack = RecipesCompactor.getRecipes().getCompactingResult(itemHandler.getStackInSlot(0));
//        if (itemstack == null)
//            return false;
//        if (!itemHandler.getStackInSlot(4).isPresent()) {
//            if (!inv[4].isItemEqual(itemstack))
//                return false;
//            if (inv[4].getCount() >= itemstack.getMaxStackSize())
//                return false;
//        }
//        if (inv[4] == null)
//            return true;
//        return inv[4].getCount() < this.getInventoryStackLimit() && inv[4].getCount() < inv[4].getMaxStackSize();
//    }
//
//    /**
//     * Turn one item from the furnace source stack into the appropriate smelted item in the furnace result stack
//     */
//    public void smeltItem() {
//        if (!this.canSmelt())
//            return;
//        ItemStack itemstack = RecipesCompactor.getRecipes().getCompactingResult(itemHandler.getStackInSlot(0));
//        if (inv[4] == null)
//            inv[4] = itemstack.copy();
//        else if (inv[4].getItem() == itemstack.getItem())
//            inv[4].getCount() += itemstack.getCount();
//
//        for (int i = 0; i < 4; i++) {
//            //if (itemHandler.getStackInSlot(i).getItem().func_46056_k())
//            //    itemHandler.getStackInSlot(i) = new ItemStack(itemHandler.getStackInSlot(i).getItem().setFull3D());
//            // else
//            itemHandler.getStackInSlot(i).getCount()--;
//
//            if (itemHandler.getStackInSlot(i).getCount() <= 0)
//                itemHandler.getStackInSlot(i) = null;
//        }
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (phi < 0.5F)
//            phi = 1F;
//        if (!this.isInWorld()) {
//            //this.phi = 1;
//            return;
//        }
//        if (power < MINPOWER || torque < MINTORQUE)
//            return;
//        if (phi >= 1.5F || phi <= 0.5F)
//            if (DragonAPI.rand.nextInt(40) > 0)
//                return;
//        if (animdir)
//            phi += 0.03125F;
//        else
//            phi -= 0.03125F;
//        if (phi >= 1.5F || phi <= 0.5F) {
//            animdir = !animdir;
//        }
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.COMPACTOR;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        if (slot == 4)
//            return false;
//        if (is.getItem() == Items.coal || (RotaryItems.COMPACTS.matchItem(is) && is.getItemDamage() <= RotaryItems.lonsda.getItemDamage()))
//            return true;
//        return RecipesCompactor.getRecipes().isCompactable(is);
//    }
//
//    @Override
//    public int getThermalDamage() {
//        return (temperature) / 100;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (!this.canSmelt())
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
//    @Override
//    public void setTemperature(int T) {
//        temperature = T;
//    }
//
//    @Override
//    public void addPressure(int press) {
//        pressure += press;
//    }
//
//    @Override
//    public int getPressure() {
//        return pressure;
//    }
//
//    @Override
//    public void overpressure(Level world, BlockPos pos) {
//        world.explode(null, pos, 4, RotaryConfig.COMMON.BLOCKDAMAGE.getState());
//        pressure = MAXPRESSURE;
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.COMPACTOR.getOperationTime(omega, this.getStage() - 1);
//    }
//
//    @Override
//    public int getNumberConsecutiveOperations() {
//        return DurationRegistry.COMPACTOR.getNumberOperations(omega, this.getStage() - 1);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.canSmelt();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        if (itemHandler.getStackInSlot(0).isEmpty())
//            return "Missing Items";
//        if (temperature < RecipesCompactor.getRecipes().getReqTemperature(itemHandler.getStackInSlot(0)))
//            return "Insufficient Temperature";
//        if (pressure < RecipesCompactor.getRecipes().getReqPressure(itemHandler.getStackInSlot(0)))
//            return "Insufficient Pressure";
//        return this.areConditionsMet() ? "Operational" : "Invalid or Missing Items";
//    }
//
//    @Override
//    public int getMaxTemperature() {
//        return MAXTEMP;
//    }
//
//    @Override
//    public boolean canBeCooledWithFins() {
//        return true;
//    }
//
//    @Override
//    public int getMaxPressure() {
//        return MAXPRESSURE;
//    }
//
//    @Override
//    public void resetAmbientTemperatureTimer() {
//        tempTick = 5;
//    }
//
//    @Override
//    public float getMultiplier() {
//        return 0.75F;
//    }
//
//    @Override
//    public void onOverheat(Level world, BlockPos pos) {
//
//    }
//
//    @Override
//    public boolean canBeFrictionHeated() {
//        return true;
//    }
//
//    @Override
//    public boolean allowExternalHeating() {
//        return true;
//    }
//
//    @Override
//    public boolean allowHeatExtraction() {
//        return false;
//    }
//}
