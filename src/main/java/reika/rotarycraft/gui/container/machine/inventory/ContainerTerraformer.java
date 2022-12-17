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
//import net.minecraft.world.entity.player.Inventory;
//import reika.rotarycraft.base.IOMachineMenu;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//
//public class ContainerTerraformer extends IOMachineMenu {
//
//    private final BlockEntityTerraformer terra;
//
//    public ContainerTerraformer(Inventory player, BlockEntityTerraformer te) {
//        super(player, te);
//        terra = te;
//
//        for (int i = 0; i < 9; i++) {
//            for (int j = 0; j < 6; j++) {
//                this.addSlot(new Slot(te, j * 9 + i, 72 + i * 18, 18 + j * 18));
//            }
//        }
//
//        int dx = 46 + 18;
//        int dy = 37;
//
//        for (int j = 0; j < 3; ++j) {
//            for (int k = 0; k < 9; ++k) {
//                this.addSlot(new Slot(player, k + j * 9 + 9, dx + 8 + k * 18, 103 + j * 18 + dy));
//            }
//        }
//
//        for (int j = 0; j < 9; ++j) {
//            this.addSlot(new Slot(player, j, dx + 8 + j * 18, 161 + dy));
//        }
//    }
//
//}
