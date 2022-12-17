///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.container;
//
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.AbstractContainerMenu;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.network.IContainerFactory;
//import reika.dragonapi.instantiable.BasicInventory;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class ContainerCraftingPattern extends AbstractContainerMenu {
//
//    private static final int width = 3;
//    private static final int height = 3;
//
//    private final CraftingMenu craftMatrix = new CraftingMenu(this, width, height);
//    private final BasicInventory craftResult = new InventoryResult();
//    private final Level level;
//    private final Player player;
//
//    public ContainerCraftingPattern(int id, Inventory player, Level par2World) {
//        super(RotaryMenuTypes.CRAFTING_PATTERN.get(), id);
//        level = par2World;
//        this.player = player;
//        int var6;
//        int var7;
//
//        for (int i = 0; i < height; i++) {
//            for (int k = 0; k < width; k++) {
//                this.addSlot(new Slot(craftMatrix, i * width + k, 30 + k * 18, 17 + i * 18));
//            }
//        }
//
//        this.addSlot(new SlotFurnace(player, craftResult, 0, 124, 35));
//
//        for (var6 = 0; var6 < 3; ++var6)
//            for (var7 = 0; var7 < 9; ++var7)
//                this.addSlot(new Slot(player.inventory, var7 + var6 * 9 + 9, 8 + var7 * 18, 84 + var6 * 18));
//        for (var6 = 0; var6 < 9; ++var6)
//            this.addSlot(new Slot(player.inventory, var6, 8 + var6 * 18, 142));
//
//        ItemStack tool = player.getCurrentEquippedItem();
//        ItemStack[] items = ItemCraftPattern.getItems(tool);
//        for (int i = 0; i < 9; i++) {
//            craftMatrix.setInventorySlotContents(i, items[i] != null ? items[i] : null);
//        }
//
//        this.onCraftMatrixChanged(craftMatrix);
//    }
//
//    public void clearRecipe() {
//        for (int i = 0; i < 9; i++) {
//            craftMatrix.setInventorySlotContents(i, null);
//        }
//        craftResult.setInventorySlotContents(0, null);
//    }
//
//    @Override
//    public void onCraftMatrixChanged(Container ii) {
//        super.onCraftMatrixChanged(ii);
//
//        ItemStack is = player.getCurrentEquippedItem();
//        ItemStack out = RotaryItems.CRAFTPATTERN.matchItem(is) ? ItemCraftPattern.getMode(is).getRecipe(craftMatrix, player.level) : null;
//        craftResult.setInventorySlotContents(0, out);
//    }
//
//    @Override
//    public ItemStack slotClick(int slot, int par2, int par3, Player ep) {
//        boolean inGUI = slot < width * height && slot >= 0;
//        if (inGUI) {
//            ItemStack held = ep.inventory.getItemStack();
//            ItemStack is = held != null ? ReikaItemHelper.getSizedItemStack(held, 1) : null;
//            craftMatrix.setInventorySlotContents(slot, is);
//            return held;
//        } else if (slot == 9) {
//            return null;
//        } else {
//            return super.slotClick(slot, par2, par3, ep);
//        }
//    }
//
//    @Override
//    public void onContainerClosed(Player ep) {
//        super.onContainerClosed(ep);
//
//        ItemStack is = ep.getCurrentEquippedItem();
//        if (RotaryItems.CRAFTPATTERN.matchItem(is))
//            ItemCraftPattern.setRecipe(is, craftMatrix, ep.level);
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return true;
//    }
//
//    @Override
//    public ItemStack quickMoveStack(Player pPlayer, int pIndex) {
//        return this.getSlot(0).getItem();
//    }
//
//    private static class InventoryResult extends BasicInventory {
//
//        public InventoryResult() {
//            super("Result", 1);
//        }
//
//        @Override
//        public boolean isItemValidForSlot(int slot, ItemStack is) {
//            return true;
//        }
//
//    }
//
//    public static class Factory implements IContainerFactory<ContainerCraftingPattern> {
//        @Override
//        public ContainerCraftingPattern create(final int id, final Inventory inv, final FriendlyByteBuf data) {
//            final Level world = inv.player.getCommandSenderWorld();
//
//            return new ContainerCraftingPattern(id, inv, world);
//        }
//    }
//}
