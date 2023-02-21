/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************//*

package reika.rotarycraft.blockentities.production;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.common.MinecraftForge;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.instantiable.recipe.CraftingInputMatrix;
import reika.dragonapi.interfaces.blockentity.CraftingTile;
import reika.dragonapi.interfaces.blockentity.TriggerableAction;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.dragonapi.libraries.ReikaRecipeHelper;
import reika.dragonapi.libraries.io.ReikaSoundHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.event.WorktableCraftEvent;
import reika.rotarycraft.api.interfaces.ChargeableTool;
import reika.rotarycraft.auxiliary.interfaces.AlternatingRedstoneUser;
import reika.rotarycraft.auxiliary.recipemanagers.WorktableRecipes;
import reika.rotarycraft.base.ItemChargedArmor;
import reika.rotarycraft.base.ItemChargedTool;
import reika.rotarycraft.base.blockentity.InventoriedRCBlockEntity;
import reika.rotarycraft.gui.container.machine.inventory.ContainerWorktable;
import reika.rotarycraft.items.tools.ItemJetPack;
import reika.rotarycraft.items.tools.ItemJetPack.PackUpgrades;
import reika.rotarycraft.items.tools.bedrock.ItemBedrockArmor.HelmetUpgrades;
import reika.rotarycraft.registry.*;

import java.util.ArrayList;
import java.util.List;

public class BlockEntityWorktable extends InventoriedRCBlockEntity implements CraftingTile<WorktableRecipes.WorktableRecipe>, TriggerableAction, AlternatingRedstoneUser {

    private final CraftingInputMatrix matrix = new CraftingInputMatrix(this);

    private boolean hasUpgrade;
    private WorktableRecipes.WorktableRecipe toCraft;

    public BlockEntityWorktable(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        if (!world.isClientSide) {
            matrix.update();
            if (matrix.isEmpty())
                return;
            if (hasUpgrade && this.tickcount % 6 == 0 && !this.hasRedstoneSignal()) {
                this.onPositiveRedstoneEdge();
                if (this.tickcount % 6 == 0) {
//             todo       ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.click", 0.5F, 0.675F);
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
        int armorslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_HELMET.get(), inv);
//  todo      if (armorslot == -1)
//            armorslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_HELMET_REVEALING.get(), inv);
        if (armorslot != -1) {
            for (int i = 0; i < HelmetUpgrades.list.length; i++) {
                HelmetUpgrades g = HelmetUpgrades.list[i];
                if (g.isAvailable && !g.existsOn(inv[armorslot])) {
                    ItemStack[] rec = g.getUpgradeItems();
                    boolean flag = false;
                    int itemslot = -1;
                    if (rec.length == 1) { //shapeless
                        itemslot = ReikaInventoryHelper.locateInInventory(rec[0], inv, false);
                        flag = itemslot != -1;
                    } else if (armorslot == 4) {
                        boolean flag2 = true;
                        for (int k = 0; k < rec.length; k++) {
                            ItemStack is = rec[k];
                            ItemStack in = inv[k >= 4 ? k + 1 : k];
                            if (!ReikaItemHelper.matchStacks(in, is)) {
                                flag2 = false;
                                break;
                            }
                        }
                        flag = flag2;
                    }
                    if (flag && ReikaInventoryHelper.isEmptyFrom(this, 9, 17)) {
                        ItemStack is = inv[armorslot].copy();
                        if (itemslot != -1) {
                            inv[itemslot] = null;
                            inv[armorslot] = null;
                        } else {
                            for (int k = 0; k < 9; k++) {
                                ReikaInventoryHelper.decrStack(k, inv);
                            }
                            inv[armorslot] = null;
                        }
                        g.enable(is, true);
                        inv[9] = is;
                    }
                }
            }
        }
    }

    private void coolJetpacks() {
        ItemStack is = inv[4];
        if (is != null) {
            Item item = is.getItem();
            if (item instanceof ItemJetPack) {
                ItemJetPack pack = (ItemJetPack) item;
                if (!PackUpgrades.COOLING.existsOn(is)) {
                    boolean items = ReikaItemHelper.matchStacks(inv[3], MachineRegistry.COOLINGFIN.getBlockState());
                    items &= ReikaItemHelper.matchStacks(inv[5], MachineRegistry.COOLINGFIN.getBlockState());
                    if (items) {
                        ReikaInventoryHelper.decrStack(3, inv);
                        ReikaInventoryHelper.decrStack(5, inv);
                        PackUpgrades.COOLING.enable(is, true);
                        inv[13] = is.copy();
                        inv[4] = null;
                    }
                }
            }
        }
    }

    private void makeJetPropel() {
        ItemStack is = inv[4];
        if (is != null) {
            Item item = is.getItem();
            if (item instanceof ItemJetPack) {
                ItemJetPack pack = (ItemJetPack) item;
                if (!PackUpgrades.JET.existsOn(is)) {
                    */
/*todo jet engine if (ReikaItemHelper.matchStacks(inv[7], EngineType.JET.getCraftedProduct())) {
                        ReikaInventoryHelper.decrStack(7, inv);
                        PackUpgrades.JET.enable(is, true);
                        inv[13] = is.copy();
                        inv[4] = null;
                    }*//*

                }
            }
        }
    }

    private void wingJetpacks() {
        ItemStack is = inv[4];
        if (is != null) {
            Item item = is.getItem();
            if (item instanceof ItemJetPack) {
                ItemJetPack pack = (ItemJetPack) item;
                if (!PackUpgrades.WING.existsOn(is)) {
                    ItemStack ingot = pack.getDefaultInstance();
                    for (int i = 0; i < 3; i++) {
                        if (!ReikaItemHelper.matchStacks(itemHandler.getStackInSlot(i), ingot))
                            return;
                    }
                    for (int i = 0; i < 3; i++) {
                        ReikaInventoryHelper.decrStack(i, inv);
                    }
                    PackUpgrades.WING.enable(is, true);
                    inv[13] = is.copy();
                    inv[4] = null;
                }
            }
        }
    }

    public CraftingContainer constructContainer() {
        return new ContainerWorktable(this.getPlacer(), this, level, false);
    }

    @Override
    public int getOutputSlot() {
        return 13;
    }

    private boolean craft() {
        WorktableRecipes.WorktableRecipe wr = WorktableRecipes.getInstance().findMatchingRecipe(matrix, level);
        if (wr != null) {
            return this.handleCrafting(wr, this.getPlacer(), false);
        }
        return false;
    }

    public boolean isReadyToCraft() {
        for (int i = 9; i < 18; i++) {
            if (itemHandler.getStackInSlot(i).isEmpty())
                return false;
        }
        return true;
    }

    public boolean handleCrafting(WorktableRecipes.WorktableRecipe wr, Player ep, boolean craftAll) {
        int maxCrafts = craftAll ? this.getSmallestInputStack() : 1;
        int crafts = 0;
        if (wr.isRecycling()) {
            ArrayList<ItemStack> li = wr.getRecycling().getSplitOutput();
            int i = 9;
            for (ItemStack is : li) {
                ReikaInventoryHelper.addOrSetStack(is, inv, i);
                i++;
            }
            RotaryAdvancements.RECYCLE.triggerAchievement(ep);
            crafts = 1;
        } else {
            for (int i = 0; i < maxCrafts; i++) {
                ItemStack is = wr.getOutput();
                if (inv[13] != null && inv[13].getCount() + is.getCount() > Math.min(this.getMaxStackSize(), is.getMaxStackSize()))
                    break;
                is.onCraftedBy(level, ep, is.getCount());
                ReikaInventoryHelper.addOrSetStack(is, inv, 13);
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
                    ReikaInventoryHelper.decrStack(i, this, crafts);
                }
            }
            SoundRegistry.CRAFT.playSoundAtBlock(level, worldPosition, 0.3F, 1.5F);
            return true;
        } else {
            return false;
        }
    }

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
        int armorslot = ReikaInventoryHelper.locateInInventory(RotaryItems.JUMP.get(), inv);
        int jumpslot = ReikaInventoryHelper.locateInInventory(RotaryItems.JUMP.get(), inv);
        if (jumpslot != -1 && armorslot != -1 && ReikaInventoryHelper.hasNEmptyStacks(inv, 17)) {
            CompoundTag tag = inv[armorslot].getTag().copy();
            inv[jumpslot] = null;
            inv[armorslot] = null;
            ItemStack is = RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get().getDefaultInstance();//todo .getEnchantedStack();
            ReikaNBTHelper.combineNBT(is.getTag(), tag);
            inv[9] = is;
        }
    }

    public boolean canUncraft() {
        boolean can = false;
        for (int i = 0; i < 9; i++) {
            ItemStack is = itemHandler.getStackInSlot(i);
            if (i == 4) {
                if (is == null || this.isNotUncraftable(is))
                    return false;
                else {
                    Recipe ir = WorktableRecipes.getInstance().getInputRecipe(is);
                    if (ir == null)
                        return false;
                    else {
                        List<ItemStack>[] in = ReikaRecipeHelper.getRecipeArray(ir);
                        if (in != null) {
                            boolean flag = true;
                            for (int k = 0; k < 9; k++) {
                                if (in[k] != null && !in[k].isEmpty()) {
                                    if (inv[k + 9] != null) {
                                        if (!ReikaItemHelper.collectionContainsItemStack(in[k], inv[k + 9]))
                                            flag = false;
                                        if (inv[k + 9].getCount() >= Math.min(this.getMaxStackSize(), inv[k + 9].getMaxStackSize()))
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
        }
        return can;
    }

    private boolean isNotUncraftable(ItemStack is) {
*/
/*     todo  RotaryItems ir = RotaryItems.getEntry(is);
      if (ir != null && (ir.isTool() || ir.isArmor())) {
            return is.getItemDamage() > 0;
        }*//*

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
      */
/*todo   if (ir == RotaryItems.MACHINE) {
            MachineRegistry r = MachineRegistry.machineList.get(is.getItemDamage());
            return !r.isUncraftable();
        }*//*

        return false;
    }

    private void uncraft() {
        ItemStack is = inv[4];
        Recipe ir = WorktableRecipes.getInstance().getInputRecipe(is);
        List<ItemStack>[] in = ReikaRecipeHelper.getRecipeArray(ir);
        if (in == null)
            return;

        for (int i = 0; i < ir.getResultItem().getCount(); i++)
            ReikaInventoryHelper.decrStack(4, inv);

     */
/*todo   for (int i = 0; i < 9; i++) {
            if (in[i] != null && !in[i].isEmpty()) {
                if (inv[i + 9] == null) {
                    inv[i + 9] = in[i].get(0).copy();
                    if (inv[i + 9].getItemDamage() == OreDictionary.WILDCARD_VALUE)
                        inv[i + 9].setItemDamage(0);
                } else {
                    ++inv[i + 9].getCount();
                }
            }
        }*//*

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
        int coilslot = ReikaInventoryHelper.locateInInventory(RotaryItems.HSLA_STEEL_SPRING.get(), inv);
        if (coilslot == -1)
            coilslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_SPRING.get(), inv);
        Item toolid = this.getTool();
        int toolslot = ReikaInventoryHelper.locateInInventory(toolid, inv);

        if (toolslot != -1 && coilslot != -1 && ReikaInventoryHelper.hasNEmptyStacks(inv, 17)) {
            Item coilid = inv[coilslot].getItem();
            int coilmeta = 5;//todo coil current level inv[coilslot].getItemDamage();
            ItemStack tool = inv[toolslot];
            if (toolid instanceof ChargeableTool) {
                int newcoilcharge = ((ChargeableTool) toolid).setCharged(tool, coilmeta, coilid == RotaryItems.BEDROCK_ALLOY_SPRING.get());
                ItemStack newcoil = new ItemStack(coilid, 1); //todo add coil charge tag
                inv[toolslot] = null;
                inv[coilslot] = null;
                inv[9] = tool.copy();
                inv[10] = newcoil;
            } else {
                ItemStack newtool = new ItemStack(toolid, 1);
                CompoundTag tag = tool.getTag() != null ? tool.getTag().copy() : null;
                newtool.setTag(tag);
                ItemStack newcoil = new ItemStack(coilid, 1); //todo add coil charge tag
                inv[toolslot] = null;
                inv[coilslot] = null;
                inv[9] = newtool;
                inv[10] = newcoil;
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
        int plateslot = ReikaInventoryHelper.locateInInventory(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get(), inv);
        if (plateslot == -1)
            plateslot = ReikaInventoryHelper.locateInInventory(RotaryItems.HSLA_CHESTPLATE.get(), inv);
        else
            bed = true;
        //ReikaJavaLibrary.pConsole(plateslot, Dist.DEDICATED_SERVER);
        int jetslot = ReikaInventoryHelper.locateInInventory(RotaryItems.JETPACK.get(), inv);
        if (jetslot != -1 && plateslot != -1 && plateslot < 9 && jetslot < 9 && ReikaInventoryHelper.hasNEmptyStacks(inv, 17)) {
            ItemStack jet = inv[jetslot];
            ItemStack plate = inv[plateslot];
            CompoundTag tag1 = plate.getTag() != null ? (CompoundTag) plate.getTag().copy() : null;
            CompoundTag tag2 = jet.getTag() != null ? (CompoundTag) jet.getTag().copy() : null;
            inv[jetslot] = null;
            inv[plateslot] = null;
            ItemStack is = (bed ? RotaryItems.BEDROCK_ALLOY_PACK.get().getDefaultInstance() */
/*todo get the enchanted version max*//*
 : RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance());
            if (is.getTag() == null)
                is.setTag(new CompoundTag());
            ReikaNBTHelper.combineNBT(is.getTag(), tag1);
            ReikaNBTHelper.combineNBT(is.getTag(), tag2);
            inv[9] = is;
            for (PackUpgrades u : PackUpgrades.values()) {
                if (u.existsOn(jet)) {
                    u.enable(is, true);
                }
            }
        }
    }

    private void uncraftJetplate() {
        ItemStack combine = inv[4];
        boolean bed = RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance() == combine;
        if (combine != null && (RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance() == combine || RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance() == combine)) {
            ItemJetPack pack = (ItemJetPack) combine.getItem();
            //ReikaJavaLibrary.pConsole(plateslot, Dist.DEDICATED_SERVER);
            if (ReikaInventoryHelper.hasNEmptyStacks(inv, 18)) {
                ItemStack jet = RotaryItems.HSLA_STEEL_PACK.get().getDefaultInstance();
                ((ItemJetPack) jet.getItem()).addFluid(jet, pack.getCurrentFluid(combine).defaultFluidState(), pack.getFuel(combine));
                for (PackUpgrades p : pack.getUpgrades(combine))
                    p.enable(jet, true);
                inv[4] = null;
                ItemStack plate = bed ? RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get().getDefaultInstance() */
/*todo get the enchanted version max*//*
 : RotaryItems.HSLA_CHESTPLATE.get().getDefaultInstance();
                inv[9] = plate;
                inv[10] = jet;
            }
        }
    }

    @Override
    public int getContainerSize() {
        return 19;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int p_18941_) {
        return null;
    }

    @Override
    public ItemStack removeItem(int p_18942_, int p_18943_) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int p_18951_) {
        return null;
    }

    @Override
    public void setItem(int p_18944_, ItemStack p_18945_) {

    }

    @Override
    public int getMaxStackSize() {
        return 64;
    }

    @Override
    public boolean stillValid(Player p_18946_) {
        return false;
    }

    public boolean isItemValidForSlot(int i, ItemStack itemstack) {
        if (i >= 9)
            return false;
        //return hasProgram ? inv[i+18] != null && ReikaItemHelper.matchStacks(inv[i+18], itemstack) : true;
//todo        return !RotaryItems.CRAFT_PATTERN.matchItem(inv[18]) || ItemCraftPattern.checkPatternForMatch(this, RecipeMode.WORKTABLE, i, i, itemstack, inv[18]);
        return false;
    }

    //    @Override
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

    */
/*
    public ItemStack getProgrammedSlot(int i, int k) {
        ItemStack is = inv[18+i*3+k];
        return is != null ? is.copy() : null;
    }

    public void setMapping(int slot, ItemStack is) {
        inv[slot] = is != null ? is.copy() : null;
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
     *//*

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
            ReikaItemHelper.dropItem(level, worldPosition.offset(0.5, 0.5, 0.5), RotaryItems.UPGRADE.get().getDefaultInstance());
            ;//.getStackOfMetadata(Upgrades.REDSTONE.ordinal()));
        }
    }

    @Override
    public WorktableRecipes.WorktableRecipe getToCraft() {
        return toCraft;
    }

    @Override
    public void setToCraft(WorktableRecipes.WorktableRecipe wr) {
        toCraft = wr;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public int getSlots() {
        return 0;
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
        return 0;
    }

    @Override
    public boolean isItemValid(int slot, @NotNull ItemStack stack) {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
*/
