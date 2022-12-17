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
//
//import net.minecraft.world.entity.player.Inventory;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityMultiCannon;
//
//
//public class ContainerMultiCannon extends IOMachineMenu {
//
//    private final BlockEntityMultiCannon tile;
//
//    public ContainerMultiCannon(int id, Inventory player, BlockEntityMultiCannon te) {
//        super(id, player, te);
//        tile = te;
//
//        this.addSlot(BlockEntityMultiCannon.CLIP_SLOT, 12, 13);
//
//        int id = BlockEntityMultiCannon.LOAD_SLOT;
//        int i;
//        int x = 46;
//        int y = 56;
//        for (i = 0; i <= 5; i++) {
//            this.addSlot(id + i, x, y);
//            if (i != 5)
//                x += 17;
//        }
//        y += 17;
//        for (int c = 0; c <= 6; c++) {
//            this.addSlot(id + i, x, y);
//            if (c != 6)
//                x -= 17;
//            i++;
//        }
//        y -= 17;
//        this.addSlot(id + i, x, y);
//        i++;
//        y -= 17;
//        for (int c = 0; c <= 7; c++) {
//            this.addSlot(id + i, x, y);
//            if (c != 7)
//                x += 17;
//            i++;
//        }
//        y += 17;
//        this.addSlot(id + i, x, y);
//        i++;
//        y += 17;
//        this.addSlot(id + i, x, y);
//        i++;
//        y += 17;
//        for (int c = 0; c <= 8; c++) {
//            this.addSlot(id + i, x, y);
//            if (c != 8)
//                x -= 17;
//            i++;
//        }
//        for (int c = 1; c <= 3; c++) {
//            y -= 17;
//            this.addSlot(id + i, x, y);
//            i++;
//        }
//
//        this.addPlayerInventoryWithOffset(ep, 0, 38);
//    }
//
//}
