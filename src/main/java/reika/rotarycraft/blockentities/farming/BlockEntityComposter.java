/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.farming;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.StepTimer;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.InventoriedRCBlockEntity;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class BlockEntityComposter extends InventoriedRCBlockEntity implements TemperatureTE {

    public static final int MINTEMP = 40;
    public static final int KILLTEMP = 70;
    public static final int MAXTEMP = 100;
    private final StepTimer tempTimer = new StepTimer(20);
    private final StepTimer timer = new StepTimer(100);
    public int composterCookTime;
    private int temperature;

    public BlockEntityComposter(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.COMPOSTER.get(), pos, state);
    }

    public static final ArrayList<ItemStack> getAllCompostables() {
        ArrayList<ItemStack> items = new ArrayList<>();
        for (int i = 0; i < CompostMatter.list.length; i++) {
            items.addAll(CompostMatter.list[i].getAllItems());
        }
        return items;
    }

    public static int getCompostValue(ItemStack is) {
        CompostMatter c = CompostMatter.getMatterType(is);
        return c != null ? c.value : 0;
    }

    public int getScaledTimer(int a) {
        return a * composterCookTime / timer.getCap();
    }

    public int getScaledTemperature(int a) {
        return a * temperature / MAXTEMP;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.COMPOSTER.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        tempTimer.update();
        if (tempTimer.checkCap()) {
            this.updateTemperature(world, pos);
        }

        int value = this.getCompostValue();
        if (value <= 0)
            return;

        int time = 1 + (temperature - 40) / 4;
        timer.update(time);
        if (timer.checkCap()) {
            ReikaInventoryHelper.decrStack(0, itemHandler);
            ReikaInventoryHelper.addOrSetStack(ReikaItemHelper.getSizedItemStack(RotaryItems.COMPOST.get().getDefaultInstance(), value), itemHandler, 2);
            if (DragonAPI.rand.nextInt(75 - temperature) == 0)
                ReikaInventoryHelper.decrStack(1, itemHandler);
            composterCookTime = 0;
        }
        composterCookTime = timer.getTick();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    private int getCompostValue() {
        if (temperature < MINTEMP || temperature > KILLTEMP)
            return 0;
        if (itemHandler.getStackInSlot(0).isEmpty() || !itemHandler.getStackInSlot(1).isEmpty())
            return 0;
        if (itemHandler.getStackInSlot(1).getItem() != RotaryItems.YEAST.get())
            return 0;
        CompostMatter c = CompostMatter.getMatterType(itemHandler.getStackInSlot(0));
        if (c == null)
            return 0;
        if (itemHandler.getStackInSlot(2).isEmpty())
            return c.value;
        if (!ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(2), RotaryItems.COMPOST))
            return 0;
        return itemHandler.getStackInSlot(2).getCount() + c.value <= itemHandler.getStackInSlot(2).getMaxStackSize() ? c.value : 0;
    }

    //    @Override
    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i == 2)
            return false;
        if (i == 1)
            return itemstack.getItem() == RotaryItems.YEAST.get();
        return CompostMatter.getMatterType(itemstack) != null;
    }

    //    @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return ReikaItemHelper.matchStacks(itemstack, RotaryItems.COMPOST);
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);

        if (RotaryAux.isNextToWater(world, pos)) {
            Tamb -= 5;
        }

        if (RotaryAux.isNextToIce(world, pos)) {
            Tamb -= 15;
        }

        if (RotaryAux.isNextToFire(world, pos)) {
            Tamb += 50;
        }

        if (RotaryAux.isNextToLava(world, pos)) {
            Tamb += 200;
        }
        if (temperature > Tamb)
            temperature--;
        if (temperature > Tamb * 2)
            temperature--;
        if (temperature < Tamb)
            temperature++;
        if (temperature * 2 < Tamb)
            temperature++;
        //if (temperature > MAXTEMP)
        //	temperature = MAXTEMP;
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

    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.COMPOSTER;
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
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getInt("temperature");

        composterCookTime = NBT.getInt("timer");
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temperature", temperature);

        NBT.putInt("timer", composterCookTime);
    }

    @Override
    public boolean canBeCooledWithFins() {
        return true;
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
    public boolean allowHeatExtraction() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public int getContainerSize() {
        return 3;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    private enum CompostMatter {

        CRAP(1, Items.EGG, Items.COOKIE, Items.WHEAT, RotaryItems.CANOLA_SEEDS, RotaryItems.CANOLA_HUSKS),
        SUGARCANE(2, Items.SUGAR_CANE),
        PLANT(1, Blocks.OAK_SAPLING, Blocks.ACACIA_SAPLING, Blocks.BAMBOO_SAPLING, Blocks.SPRUCE_SAPLING, Blocks.DARK_OAK_SAPLING, Blocks.JUNGLE_SAPLING, Blocks.BIRCH_SAPLING, Blocks.LILY_PAD, Blocks.POPPY, Blocks.DANDELION, Blocks.BROWN_MUSHROOM, Blocks.RED_MUSHROOM, Blocks.CORNFLOWER),
        LEAF(2, Blocks.AZALEA_LEAVES, Blocks.AZALEA, Blocks.ACACIA_LEAVES, Blocks.BIRCH_LEAVES, Blocks.OAK_LEAVES, Blocks.DARK_OAK_LEAVES, Blocks.SPRUCE_LEAVES, Blocks.JUNGLE_LEAVES, Blocks.FLOWERING_AZALEA_LEAVES, Blocks.GRASS, Blocks.VINE, Blocks.TALL_GRASS, Blocks.SUNFLOWER, Blocks.ROSE_BUSH, Blocks.SWEET_BERRY_BUSH),
        MEAT(4, Items.BEEF, Items.COOKED_BEEF, Items.COOKED_PORKCHOP, Items.PORKCHOP, Items.COOKED_CHICKEN, Items.CHICKEN),
        FISH(3, Items.COOKED_COD, Items.COOKED_SALMON, Items.SALMON, Items.COD),
        VEGGIE(2, Items.POTATO, Items.CARROT, Items.BAKED_POTATO, Items.POISONOUS_POTATO, Items.BREAD, Items.APPLE, Items.MELON),
        MOBS(3, Items.ROTTEN_FLESH, Items.SPIDER_EYE);

        public static final CompostMatter[] list = values();
        public final int value;
        private final ArrayList<ItemStack> items = new ArrayList<>();

        CompostMatter(int value, Object... items) {
            this.value = value;
            for (int i = 0; i < items.length; i++) {
                ItemStack is = null;
                if (items[i] instanceof ItemStack)
                    is = (ItemStack) items[i];
//                else if (items[i] instanceof Block)
//                    is = new ItemStack((Block) items[i], 1, OreDictionary.WILDCARD_VALUE);
//                else if (items[i] instanceof Item)
//                    is = new ItemStack((Item) items[i], 1, OreDictionary.WILDCARD_VALUE);
//                else if (items[i] instanceof String) {
//                    ArrayList<ItemStack> li = OreDictionary.getOres((String) items[i]);
//                    this.items.addAll(li);
//                    continue;
//                }
                if (is != null)
                    this.items.add(is);
            }
        }

        public static CompostMatter getMatterType(ItemStack is) {
            for (int i = 0; i < list.length; i++) {
                CompostMatter c = list[i];
                if (ReikaItemHelper.collectionContainsItemStack(c.items, is))
                    return c;
            }
            return null;
        }

        public List<ItemStack> getAllItems() {
            return Collections.unmodifiableList(items);
        }
    }

}
