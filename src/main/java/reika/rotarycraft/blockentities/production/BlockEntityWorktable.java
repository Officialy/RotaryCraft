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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.interfaces.blockentity.TriggerableAction;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.ChargeableTool;
import reika.rotarycraft.auxiliary.interfaces.AlternatingRedstoneUser;
import reika.rotarycraft.base.ItemChargedArmor;
import reika.rotarycraft.base.ItemChargedTool;
import reika.rotarycraft.base.blockentity.InventoriedRCBlockEntity;
import reika.rotarycraft.items.tools.ItemCraftPattern;
import reika.rotarycraft.items.tools.ItemJetPack;
import reika.rotarycraft.items.tools.ItemJetPack.PackUpgrades;
import reika.rotarycraft.items.tools.bedrock.ItemBedrockArmor.HelmetUpgrades;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

public class BlockEntityWorktable extends InventoriedRCBlockEntity implements TriggerableAction, AlternatingRedstoneUser {

    private boolean hasUpgrade;
//    private WorktableRecipes.WorktableRecipe toCraft;

    public BlockEntityWorktable(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.WORKTABLE.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.WORKTABLE.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        if (!world.isClientSide) {
//            matrix.update();
//            if (matrix.isEmpty())
//                return;
            if (hasUpgrade && this.tickcount % 6 == 0 && !this.hasRedstoneSignal()) {
                this.onPositiveRedstoneEdge();
                if (this.tickcount % 6 == 0) {
//todo                    ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.click", 0.5F, 0.675F);
                }
            }
            if (this.isReadyToCraft()) {
                if (this.tickcount % 4 == 0) {
                    this.chargeTools();
                    this.makeJetplate();
                    this.makeJetPropel();
                    this.coolJetpacks();
                    this.wingJetpacks();
                    this.makeBedjump();
                    this.makeHelmetUpgrades();
                }
            }
        }
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {

    }

    @Override
    protected void onPositiveRedstoneEdge() {
        if (!level.isClientSide) {
            if (!this.craft()) {
                if (this.canUncraft())
                    this.uncraft();
            }
            this.uncraftJetplate();
        }
    }

    private void makeHelmetUpgrades() {
        int armorslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_HELMET.get(), itemHandler);
//  todo      if (armorslot == -1)
//            armorslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_HELMET_REVEALING.get(), itemHandler);
        if (armorslot != -1) {
            for (int i = 0; i < HelmetUpgrades.list.length; i++) {
                HelmetUpgrades g = HelmetUpgrades.list[i];
                if (g.isAvailable && !g.existsOn(itemHandler.getStackInSlot(armorslot))) {
                    ItemStack[] rec = g.getUpgradeItems();
                    boolean flag = false;
                    int itemslot = -1;
                    if (rec.length == 1) { //shapeless
                        itemslot = ReikaInventoryHelper.locateInInventory(rec[0], itemHandler, false);
                        flag = itemslot != -1;
                    } else if (armorslot == 4) {
                        boolean flag2 = true;
                        for (int k = 0; k < rec.length; k++) {
                            ItemStack is = rec[k];
                            ItemStack in = itemHandler.getStackInSlot(k >= 4 ? k + 1 : k);
                            if (!ReikaItemHelper.matchStacks(in, is)) {
                                flag2 = false;
                                break;
                            }
                        }
                        flag = flag2;
                    }
                    if (flag && ReikaInventoryHelper.isEmptyFrom(itemHandler, 9, 17)) {
                        ItemStack is = itemHandler.getStackInSlot(armorslot).copy();
                        if (itemslot != -1) {
                            itemHandler.setStackInSlot(itemslot, ItemStack.EMPTY);
                            itemHandler.setStackInSlot(armorslot, ItemStack.EMPTY);
                        } else {
                            for (int k = 0; k < 9; k++) {
                                ReikaInventoryHelper.decrStack(k, itemHandler);
                            }
                            itemHandler.setStackInSlot(armorslot, ItemStack.EMPTY);
                        }
                        g.enable(is, true);
                        itemHandler.setStackInSlot(9, is);
                    }
                }
            }
        }
    }

    private void coolJetpacks() {
        ItemStack is = itemHandler.getStackInSlot(4);
        if (is != null) {
            Item item = is.getItem();
            if (item instanceof ItemJetPack pack) {
                if (!PackUpgrades.COOLING.existsOn(is)) {
                    boolean items = ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(3), MachineRegistry.COOLINGFIN.getBlockState());
                    items &= ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(5), MachineRegistry.COOLINGFIN.getBlockState());
                    if (items) {
                        ReikaInventoryHelper.decrStack(3, itemHandler);
                        ReikaInventoryHelper.decrStack(5, itemHandler);
                        PackUpgrades.COOLING.enable(is, true);
                        itemHandler.setStackInSlot(13, is.copy());
                        itemHandler.setStackInSlot(4, ItemStack.EMPTY);
                    }
                }
            }
        }
    }

    private void makeJetPropel() {
        ItemStack is = itemHandler.getStackInSlot(4);
        if (is != null) {
            Item item = is.getItem();
            if (item instanceof ItemJetPack pack) {
                if (!PackUpgrades.JET.existsOn(is)) {

/*todo jet engine if (ReikaItemHelper.matchStacks(itemHandler[7], EngineType.JET.getCraftedProduct())) {
                        ReikaInventoryHelper.decrStack(7, itemHandler);
                        PackUpgrades.JET.enable(is, true);
                        itemHandler[13] = is.copy();
                        itemHandler[4] = null;
                    }*/

                }
            }
        }
    }

    private void wingJetpacks() {
        ItemStack is = itemHandler.getStackInSlot(4);
        if (is != null) {
            Item item = is.getItem();
            if (item instanceof ItemJetPack pack) {
                if (!PackUpgrades.WING.existsOn(is)) {
                    ItemStack ingot = pack.getDefaultInstance();
                    for (int i = 0; i < 3; i++) {
                        if (!ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(i), ingot))
                            return;
                    }
                    for (int i = 0; i < 3; i++) {
                        ReikaInventoryHelper.decrStack(i, itemHandler);
                    }
                    PackUpgrades.WING.enable(is, true);
                    itemHandler.setStackInSlot(13, is.copy());
                    itemHandler.setStackInSlot(4, ItemStack.EMPTY);
                }
            }
        }
    }

    private boolean craft() {
//        WorktableRecipes.WorktableRecipe wr = WorktableRecipes.getInstance().findMatchingRecipe(matrix, level);
//        if (wr != null) {
//            return this.handleCrafting(wr, this.getPlacer(), false);
//        }
        return false;
    }

    public boolean isReadyToCraft() {
        for (int i = 9; i < 18; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

/*
    public boolean handleCrafting(WorktableRecipes.WorktableRecipe wr, Player ep, boolean craftAll) {
        int maxCrafts = craftAll ? this.getSmallestInputStack() : 1;
        int crafts = 0;
        if (wr.isRecycling()) {
            ArrayList<ItemStack> li = wr.getRecycling().getSplitOutput();
            int i = 9;
            for (ItemStack is : li) {
                ReikaInventoryHelper.addOrSetStack(is, itemHandler, i);
                i++;
            }
            RotaryAdvancements.RECYCLE.triggerAchievement(ep);
            crafts = 1;
        } else {
            for (int i = 0; i < maxCrafts; i++) {
                ItemStack is = wr.getOutput();
                if (itemHandler.getStackInSlot(13) != null && itemHandler.getStackInSlot(13).getCount() + is.getCount() > Math.min(this.getMaxStackSize(), is.getMaxStackSize()))
                    break;
                is.onCraftedBy(level, ep, is.getCount());
                ReikaInventoryHelper.addOrSetStack(is, itemHandler, 13);
                crafts++;
            }
            ItemStack out = wr.getOutput().copy();
            out.setCount(out.getCount() * crafts);
            MinecraftForge.EVENT_BUS.post(new WorktableCraftEvent(this, ep, true, out));
        }
        if (crafts > 0) {
            for (int i = 0; i < 9; ++i) {
                ItemStack item = this.getStackInSlot(i);
                if (item != null) {
                    //noUpdate = true;
                    ReikaInventoryHelper.decrStack(i, itemHandler, crafts);
                }
            }
            SoundRegistry.CRAFT.playSoundAtBlock(level, worldPosition, 0.3F, 1.5F);
            return true;
        } else {
            return false;
        }
    }
*/

    private int getSmallestInputStack() {
        int smallest = Integer.MAX_VALUE;
        for (int i = 0; i < 9; ++i) {
            ItemStack item = this.getStackInSlot(i);
            if (item != null && item.getCount() < smallest) {
                smallest = item.getCount();
            }
        }
        return smallest < Integer.MAX_VALUE ? smallest : -1;
    }

    private void makeBedjump() {
        int armorslot = ReikaInventoryHelper.locateInInventory(RotaryItems.JUMP.get(), itemHandler);
        int jumpslot = ReikaInventoryHelper.locateInInventory(RotaryItems.JUMP.get(), itemHandler);
        if (jumpslot != -1 && armorslot != -1 && ReikaInventoryHelper.hasNEmptyStacks(itemHandler, 17)) {
            CompoundTag tag = itemHandler.getStackInSlot(armorslot).getTag().copy();
            itemHandler.setStackInSlot(jumpslot, ItemStack.EMPTY);
            itemHandler.setStackInSlot(armorslot, ItemStack.EMPTY);
            ItemStack is = RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get().getDefaultInstance();//todo .getEnchantedStack();
            ReikaNBTHelper.combineNBT(is.getTag(), tag);
            itemHandler.setStackInSlot(9, is);
        }
    }

    public boolean canUncraft() {
        boolean can = false;
       /* for (int i = 0; i < 9; i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (i == 4) {
                if (is == null || this.isNotUncraftable(is))
                    return false;
                else {
                    IReikaRecipe ir = WorktableRecipes.getInstance().getInputRecipe(is);
                    if (ir == null)
                        return false;
                    else {
                        List<ItemStack>[] in = ReikaRecipeHelper.getRecipeArray(ir);
                        if (in != null) {
                            boolean flag = true;
                            for (int k = 0; k < 9; k++) {
                                if (in[k] != null && !in[k].isEmpty()) {
                                    if (itemHandler.getStackInSlot(k + 9) != null) {
                                        if (!ReikaItemHelper.collectionContainsItemStack(in[k], itemHandler.getStackInSlot(k + 9)))
                                            flag = false;
                                        if (itemHandler.getStackInSlot(k + 9).getCount() >= Math.min(this.getMaxStackSize(), itemHandler.getStackInSlot(k + 9).getMaxStackSize()))
                                            flag = false;
                                    }
                                }
                            }
                            can = flag;
                        }
                    }
                }
            } else {
                if (is != null)
                    return false;
            }
        }*/
        return can;
    }

    private boolean isNotUncraftable(ItemStack is) {

//        RotaryItems ir = RotaryItems.getEntry(is);
//        if (ir != null && (ir.isTool() || ir.isArmor())) {
//            return is.getItemDamage() > 0;
//        }

        if (is.getTag() == null)
            return false;
        if (is.getTag().getInt("dmg") > 0)
            return true;
        if (is.getTag().getInt("damage") > 0)
            return true;
        if (is.getTag().getInt("lube") > 0)
            return true;
        if (is.getTag().getInt("lvl") > 0)
            return true;
        if (is.getTag().contains("ench"))
            return true;
//        if (ir == RotaryItems.MACHINE) {
//            MachineRegistry r = MachineRegistry.machineList.get(is.getItemDamage());
//            return !r.isUncraftable();
//        }

        return false;
    }

    private void uncraft() {
        ItemStack is = itemHandler.getStackInSlot(4);
//        IReikaRecipe ir = WorktableRecipes.getInstance().getInputRecipe(is);
//        List<ItemStack>[] in = ReikaRecipeHelper.getRecipeArray(ir);
//        if (in == null)
//            return;
//
//        for (int i = 0; i < ir.getResult().getCount(); i++)
//            ReikaInventoryHelper.decrStack(4, itemHandler);
//
//
//        for (int i = 0; i < 9; i++) {
//            if (in[i] != null && !in[i].isEmpty()) {
//                if (itemHandler.getStackInSlot(i + 9) == ItemStack.EMPTY) {
//                    itemHandler.setStackInSlot(i + 9, in[i].get(0).copy());
//                    if (itemHandler.getStackInSlot(i + 9).getItemDamage() == OreDictionary.WILDCARD_VALUE)
//                        itemHandler.getStackInSlot(i + 9).setItemDamage(0);
//                } else {
//                 todo   ++itemHandler.getStackInSlot(i + 9).getCount();
//                }
//            }
//        }

    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.WORKTABLE;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    private void chargeTools() {
        int coilslot = ReikaInventoryHelper.locateInInventory(RotaryItems.HSLA_STEEL_SPRING.get(), itemHandler);
        if (coilslot == -1)
            coilslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_SPRING.get(), itemHandler);
        Item toolid = this.getTool();
        int toolslot = ReikaInventoryHelper.locateInInventory(toolid, itemHandler);

        if (toolslot != -1 && coilslot != -1 && ReikaInventoryHelper.hasNEmptyStacks(itemHandler, 17)) {
            Item coilid = itemHandler.getStackInSlot(coilslot).getItem();
            int coilmeta = 5;//todo coil current level itemHandler[coilslot].getItemDamage();
            ItemStack tool = itemHandler.getStackInSlot(toolslot);
            if (toolid instanceof ChargeableTool) {
                int newcoilcharge = ((ChargeableTool) toolid).setCharged(tool, coilmeta, coilid == RotaryItems.BEDROCK_ALLOY_SPRING.get());
                ItemStack newcoil = new ItemStack(coilid, 1); //todo add coil charge tag
                itemHandler.setStackInSlot(toolslot, ItemStack.EMPTY);
                itemHandler.setStackInSlot(coilslot, ItemStack.EMPTY);
                itemHandler.setStackInSlot(9, tool.copy());
                itemHandler.setStackInSlot(10, newcoil);
            } else {
                ItemStack newtool = new ItemStack(toolid, 1);
                CompoundTag tag = tool.getTag() != null ? tool.getTag().copy() : null;
                newtool.setTag(tag);
                ItemStack newcoil = new ItemStack(coilid, 1); //todo add coil charge tag
                itemHandler.setStackInSlot(toolslot, ItemStack.EMPTY);
                itemHandler.setStackInSlot(coilslot, ItemStack.EMPTY);
                itemHandler.setStackInSlot(9, newtool);
                itemHandler.setStackInSlot(10, newcoil);
            }
        }
    }

    private Item getTool() {
        for (int i = 0; i < 9; i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (is != null) {
                if (is.getItem() instanceof ItemChargedTool || is.getItem() instanceof ItemChargedArmor || is.getItem() instanceof ChargeableTool)
                    return itemHandler.getStackInSlot(i).getItem();
            }
        }
        return null;
    }

    private void makeJetplate() {
        boolean bed = false;
        int plateslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get(), itemHandler);
        if (plateslot == -1)
            plateslot = ReikaInventoryHelper.locateInInventory(RotaryItems.HSLA_CHESTPLATE.get(), itemHandler);
        else
            bed = true;
        //ReikaJavaLibrary.pConsole(plateslot, Dist.DEDICATED_SERVER);
        int jetslot = ReikaInventoryHelper.locateInInventory(RotaryItems.JETPACK.get(), itemHandler);
        if (jetslot != -1 && plateslot != -1 && plateslot < 9 && jetslot < 9 && ReikaInventoryHelper.hasNEmptyStacks(itemHandler, 17)) {
            ItemStack jet = itemHandler.getStackInSlot(jetslot);
            ItemStack plate = itemHandler.getStackInSlot(plateslot);
            CompoundTag tag1 = plate.getTag() != null ? (CompoundTag) plate.getTag().copy() : null;
            CompoundTag tag2 = jet.getTag() != null ? (CompoundTag) jet.getTag().copy() : null;
            itemHandler.setStackInSlot(jetslot, ItemStack.EMPTY);
            itemHandler.setStackInSlot(plateslot, ItemStack.EMPTY);
            ItemStack is = (bed ? RotaryItems.BEDROCK_ALLOY_PACK.get().getDefaultInstance() : RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance());
            /*todo get the enchanted version max*/

            if (is.getTag() == null)
                is.setTag(new CompoundTag());
            ReikaNBTHelper.combineNBT(is.getTag(), tag1);
            ReikaNBTHelper.combineNBT(is.getTag(), tag2);
            itemHandler.setStackInSlot(9, is);
            for (PackUpgrades u : PackUpgrades.values()) {
                if (u.existsOn(jet)) {
                    u.enable(is, true);
                }
            }
        }
    }

    private void uncraftJetplate() {
        ItemStack combine = itemHandler.getStackInSlot(4);
        boolean bed = RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance() == combine;
        if (combine != null && (RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance() == combine || RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() == combine)) {
            ItemJetPack pack = (ItemJetPack) combine.getItem();
            //ReikaJavaLibrary.pConsole(plateslot, Dist.DEDICATED_SERVER);
            if (ReikaInventoryHelper.hasNEmptyStacks(itemHandler, 18)) {
                ItemStack jet = RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance();
                ((ItemJetPack) jet.getItem()).addFluid(jet, pack.getCurrentFluid(combine).defaultFluidState(), pack.getFuel(combine));
                for (PackUpgrades p : pack.getUpgrades(combine))
                    p.enable(jet, true);
                itemHandler.setStackInSlot(4, ItemStack.EMPTY);
                ItemStack plate = bed ? RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance() : RotaryItems.HSLA_CHESTPLATE.get().getDefaultInstance();
                /*todo get the enchanted version max*/
                itemHandler.setStackInSlot(9, plate);
                itemHandler.setStackInSlot(10, jet);
            }
        }
    }

    @Override
    public int getContainerSize() {
        return 19;
    }

    @Override
    public boolean isEmpty() {
        for (int i = 0; i < 19; i++) {
            if (!itemHandler.getStackInSlot(i).isEmpty()) {
                return false;
            }
        }
        return true;
    }

    @Override
    public ItemStack getItem(int i) {
        if (i >= 0 && i < itemHandler.getSlots()) {
            return itemHandler.getStackInSlot(i);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItem(int i, int i1) {
        if (i >= 0 && i < itemHandler.getSlots()) {
            return itemHandler.extractItem(i, i1, false);
        }
        return ItemStack.EMPTY;
    }

    @Override
    public ItemStack removeItemNoUpdate(int i) {
        if (i >= 0 && i < itemHandler.getSlots()) {
            ItemStack stack = itemHandler.getStackInSlot(i);
            itemHandler.setStackInSlot(i, ItemStack.EMPTY);
            return stack;
        }
        return ItemStack.EMPTY;
    }

    @Override
    public void setItem(int i, ItemStack itemStack) {
        if (i >= 0 && i < itemHandler.getSlots()) {
            itemHandler.setStackInSlot(i, itemStack);
        }
    }

    @Override
    public boolean stillValid(Player player) {
        return !isRemoved() && player.distanceToSqr(worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5) <= 64;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i >= 9)
            return false;
        //return hasProgram ? itemHandler[i+18] != null && ReikaItemHelper.matchStacks(itemHandler[i+18], itemstack) : true;
        return !RotaryItems.CRAFT_PATTERN.get().getDefaultInstance().is(itemHandler.getStackInSlot(18).getItem()) || ItemCraftPattern.checkPatternForMatch(this, ItemCraftPattern.RecipeMode.WORKTABLE, i, i, itemstack, itemHandler.getStackInSlot(18));
    }

    //        @Override
    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return i >= 9 && i != 18;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putBoolean("redstoneUpgrade", hasUpgrade);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        hasUpgrade = NBT.getBoolean("redstoneUpgrade");
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    public void onEMP() {

    }

/*
    public ItemStack getProgrammedSlot(int i, int k) {
        ItemStack is = itemHandler[18+i*3+k];
        return is != null ? is.copy() : null;
    }

    public void setMapping(int slot, ItemStack is) {
        itemHandler[slot] = is != null ? is.copy() : null;
    }

    @Override
    public void markDirty() {
        super.markDirty();

        hasProgram = false;
        for (int i = 18; i < 27; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty()) {
                hasProgram = true;
            }
        }
    }
     */

    @Override
    public boolean trigger() {
        return this.craft();
    }

    @Override
    public void upgrade(ItemStack is) {
        this.addRedstoneUpgrade();
    }

    @Override
    public boolean canUpgradeWith(ItemStack item) {
        return !this.hasRedstoneUpgrade() && RotaryItems.UPGRADE.get().getDefaultInstance() == item;//todo upgrade type && item.getItemDamage() == Upgrades.REDSTONE.ordinal();
    }

    @Override
    public void addRedstoneUpgrade() {
        hasUpgrade = true;
    }

    @Override
    public boolean hasRedstoneUpgrade() {
        return hasUpgrade;
    }

    @Override
    public void breakBlock() {
        if (this.hasRedstoneUpgrade()) {
            ReikaItemHelper.dropItem(level, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, RotaryItems.UPGRADE.get().getDefaultInstance());
            ;//.getStackOfMetadata(Upgrades.REDSTONE.ordinal()));
        }
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
    public void clearContent() {

    }
}

