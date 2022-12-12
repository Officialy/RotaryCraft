///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.containers.machine.inventory;
//
//
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.storage.BlockEntityScaleableChest;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//
//public class ContainerScaleChest extends IOMachineMenu {
//    private final Container lowerScaleChestInventory;
//    private final BlockEntityScaleableChest chest;
//    private final int size;
//    private final int page;
//
//    public ContainerScaleChest(Player player, BlockEntityScaleableChest te, int page) {
//        super(player, te);
//        lowerScaleChestInventory = te;
//        chest = te;
//        size = te.getNumberSlots();
//        te.openInventory();
//        this.page = page;
//        this.setSlots(player, te, page);
//    }
//
//    private int getPageOffset() {
//        return page * 9 * BlockEntityScaleableChest.MAXROWS;
//    }
//
//    private void setSlots(Player player, BlockEntityScaleableChest te, int page) {
//        int offset = this.getPageOffset();
//        for (int i = 0; i < offset; i++) {
//            this.addSlot(new Slot(te, i, -200, -200));
//        }
//        int var3 = 0;
//        int var4 = 8;
//        int var5 = 18;
//        int rowsize = size;
//        if (rowsize > BlockEntityScaleableChest.MAXROWS * 9)
//            rowsize = BlockEntityScaleableChest.MAXROWS * 9;
//        if (page == chest.getMaxPage()) {
//            rowsize = size - offset;
//        }
//        int rows = (int) Math.ceil(rowsize / 9D);
//        int x = 0;
//        int y = 0;
//        //ReikaJavaLibrary.pConsole(size);
//        //ReikaJavaLibrary.pConsole(rowsize);
//        //ReikaJavaLibrary.pConsole(page);
//
//        for (var3 = 0; var3 < rowsize; var3++) {
//            y = var5 + var3 / 9 * 18;
//            x = var4 + 18 * (var3 % 9);
//            //ReikaJavaLibrary.pConsole(var3+"  ->   "+x+", "+y);
//            int id = var3 + offset;
//            //ReikaJavaLibrary.pConsole(id+":"+chest.getStackInSlot(id));
//            this.addSlot(new Slot(te, id, x, y));
//        }
//        var5 = BlockEntityScaleableChest.MAXROWS * 18 + 31;//rows*18+31;
//        //var4 = 44+18*(rows-1);
//        int k;
//        for (k = 0; k < 3; ++k) {
//            for (int m = 0; m < 9; ++m) {
//                var3 = m + 9 * k;
//                y = var5 + k * 18;
//                x = var4 + 18 * m;
//                this.addSlot(new Slot(player.inventory, m + k * 9 + 9, x, y));
//            }
//        }
//        var5 += 58;
//        for (k = 0; k < 9; ++k) {
//            y = var5;
//            x = var4 + 18 * k;
//            this.addSlot(new Slot(player.inventory, k, x, y));
//        }
//    }
//
//    @Override
//    public boolean canInteractWith(Player player) {
//        if (player != null)
//            return true;
//        boolean b = chest.isUseableByPlayer(player);
//        if (!b) {
//            chest.closeInventory();
//            chest.lidAngle = 1;
//        }
//        return b;
//    }
//
//    /**
//     * Callback for when the crafting gui is closed.
//     */
//    @Override
//    public void onContainerClosed(Player par1Player) {
//        super.onContainerClosed(par1Player);
//        lowerScaleChestInventory.closeInventory();
//    }
//
//    public Container getLowerScaleChestInventory() {
//        return lowerScaleChestInventory;
//    }
//}
