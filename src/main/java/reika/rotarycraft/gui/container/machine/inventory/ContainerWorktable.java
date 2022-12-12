///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.container.machine.inventory;
//
//import net.minecraft.world.inventory.CraftingContainer;
//import net.minecraft.world.inventory.FurnaceResultSlot;
//import net.minecraft.world.item.crafting.Recipe;
//import net.minecraft.world.level.Level;
//import reika.dragonapi.instantiable.gui.Slot.SlotApprovedItems;
//
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import reika.dragonapi.interfaces.ReikaCraftingContainer;
//import reika.rotarycraft.auxiliary.recipemanagers.WorktableRecipes;
//import reika.rotarycraft.blockentities.production.BlockEntityWorktable;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class ContainerWorktable extends ReikaCraftingContainer<WorktableRecipes.WorktableRecipe> {
//
//    private final BlockEntityWorktable table;
//
//    public ContainerWorktable(Player player, BlockEntityWorktable te, Level world, boolean gui) {
//        super(player, te, world, gui);
//        int dx = 0;
//        table = te;
//
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                this.addSlot(new Slot(te, i * 3 + j, dx + 26 + j * 18, 17 + i * 18));
//            }
//        }
//        dx += 96 - 28 + 4;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                this.addSlot(new FurnaceResultSlot(player, te, 9 + i * 3 + j, dx + 26 + j * 18, 17 + i * 18));
//            }
//        }
//
//        this.addSlot(new SlotApprovedItems(te, 18, 6, 53).addItem(RotaryItems.CRAFTPATTERN.get()));
//
//		/*
//		dx = 153;
//		int dy = 84;
//		for (int i = 0; i < 3; i++) {
//			for (int j = 0; j < 3; j++) {
//				//this.addSlot(new GhostSlot(te, 18+i*3+j, dx+26+j*18, dy+i*18));
//				this.addSlot(new SlotXItems(te, 18+i*3+j, dx+26+j*18, dy+i*18, 1));
//			}
//		}
//		 */
//
//        this.updateCraftMatrix();
//
//        this.addPlayerInventory(player);
//        this.onCraftMatrixChanged();
//    }
//
//    @Override
//    protected WorktableRecipes.WorktableRecipe getRecipe(CraftingContainer craftMatrix, Level world) {
//        return WorktableRecipes.getInstance().findMatchingRecipe(craftMatrix, world);
//    }
//
//    @Override
//    protected ItemStack getOutput(WorktableRecipes.WorktableRecipe wr) {
//        return wr.isRecycling() ? wr.getRecycling().getResultItem() : wr.getOutput();
//    }
//
//    /**
//     * Callback for when the crafting matrix is changed.
//     */
//}
