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
//import net.minecraft.entity.player.Player;
//import net.minecraft.inventory.ICrafting;
//import net.minecraft.inventory.Slot;
//import net.minecraft.inventory.SlotFurnace;
//
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.inventory.Slot;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter;
//import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.CraftingMode;
//
//public class ContainerAutoCrafter extends IOMachineMenu {
//    private final BlockEntityAutoCrafter crafter;
//
//    public ContainerAutoCrafter(int id, Inventory player, BlockEntityAutoCrafter te) {
//        super(id, player, te);
//        crafter = te;
//        for (int i = 0; i < 18; i++) {
//            int dx = 8 + (i % 9) * 18;
//            int dy = i < 9 ? 19 : 81;
//            this.addSlot(new Slot(te, i, dx, dy));
//            if (te.getMode() != CraftingMode.SUSTAIN)
//                this.addSlot(new SlotFurnace(player, te, i + 18, dx, dy + 27));
//        }
//
//        this.addPlayerInventoryWithOffset(player, 0, 56);
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//            for (int k = 0; k < 18; k++) {
//                icrafting.sendProgressBarUpdate(this, k, crafter.crafting[k]);
//            }
//        }
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        crafter.crafting[par1] = par2;
//    }
//}
