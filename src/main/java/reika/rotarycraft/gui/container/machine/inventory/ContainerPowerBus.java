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
//import net.minecraft.core.Direction;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//
//public class ContainerPowerBus extends CoreMenu {
//
//    private final BlockEntityPowerBus te;
//
//    public ContainerPowerBus(Player player, BlockEntityPowerBus te) {
//        super(player, te);
//
//        this.te = te;
//
//        int[] x = {102, 102, 66, 138};
//        int[] y = {33, 105, 69, 69};
//
//        for (int i = 0; i < 4; i++) {
//            Direction dir = Direction.values()[i + 2];
//            if (te.canHaveItemInSlot(dir))
//                this.addSlot(i, x[i], y[i]);
//        }
//
//        int dx = 22;
//        int dy = 57;
//        for (int i = 0; i < 3; i++) {
//            for (int k = 0; k < 9; k++) {
//                this.addSlot(new Slot(player.inventory, k + i * 9 + 9, 8 + k * 18 + dx, 84 + i * 18 + dy));
//            }
//        }
//        dy -= 4;
//        for (int j = 0; j < 9; j++) {
//            this.addSlot(new Slot(player.inventory, j, 8 + j * 18 + dx, 142 + dy));
//        }
//    }
//
//}
