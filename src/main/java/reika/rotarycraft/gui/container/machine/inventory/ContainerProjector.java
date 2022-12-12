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
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.decorative.BlockEntityProjector;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//
//public class ContainerProjector extends IOMachineMenu {
//
//    public ContainerProjector(Player player, BlockEntityProjector te) {
//        super(player, te);
//
//        this.addSlot(new Slot(te, 0, 80, 9));
//        this.addSlot(new Slot(te, 1, 80 + 18, 9));
//        this.addSlot(new Slot(te, 2, 80 + 18, 9 + 18));
//        this.addSlot(new Slot(te, 3, 80 + 18 * 2, 9 + 18));
//        this.addSlot(new Slot(te, 4, 80 + 18 * 2, 9 + 18 * 2));
//        this.addSlot(new Slot(te, 5, 80 + 18 * 3, 9 + 18 * 2));
//        this.addSlot(new Slot(te, 6, 80 + 18 * 3, 9 + 18 * 3));
//        this.addSlot(new Slot(te, 7, 80 + 18 * 3, 9 + 18 * 4));
//        this.addSlot(new Slot(te, 8, 80 + 18 * 2, 9 + 18 * 4));
//        this.addSlot(new Slot(te, 9, 80 + 18 * 2, 9 + 18 * 5));
//        this.addSlot(new Slot(te, 10, 80 + 18, 9 + 18 * 5));
//        this.addSlot(new Slot(te, 11, 80 + 18, 9 + 18 * 6));
//        this.addSlot(new Slot(te, 12, 80, 9 + 18 * 6));
//        this.addSlot(new Slot(te, 13, 80 - 18, 9 + 18 * 6));
//        this.addSlot(new Slot(te, 14, 80 - 18, 9 + 18 * 5));
//        this.addSlot(new Slot(te, 15, 80 - 18 * 2, 9 + 18 * 5));
//        this.addSlot(new Slot(te, 16, 80 - 18 * 2, 9 + 18 * 4));
//        this.addSlot(new Slot(te, 17, 80 - 18 * 3, 9 + 18 * 4));
//        this.addSlot(new Slot(te, 18, 80 - 18 * 3, 9 + 18 * 3));
//        this.addSlot(new Slot(te, 19, 80 - 18 * 3, 9 + 18 * 2));
//        this.addSlot(new Slot(te, 20, 80 - 18 * 2, 9 + 18 * 2));
//        this.addSlot(new Slot(te, 21, 80 - 18 * 2, 9 + 18));
//        this.addSlot(new Slot(te, 22, 80 - 18, 9 + 18));
//        this.addSlot(new Slot(te, 23, 80 - 18, 9));
//
//        int dy = 56;
//        for (int i = 0; i < 3; i++)
//            for (int k = 0; k < 9; k++)
//                this.addSlot(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18, 84 + i * 18 + dy));
//        for (int j = 0; j < 9; j++)
//            this.addSlot(new Slot(player.inventory, j, 8 + j * 18, 142 + dy));
//    }
//
//}
