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
//import reika.rotarycraft.blockentities.BlockEntityVacuum;
//import net.minecraft.world.entity.player.Player;
//
//public class ContainerVacuum extends ContainerStackingStorage {
//    private final Container lowerVacuumInventory;
//    private final BlockEntityVacuum vac;
//    private int numRows;
//
//    public ContainerVacuum(Player player, BlockEntityVacuum te) {
//        super(player, te);
//        vac = te;
//        lowerVacuumInventory = te;
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//            //icrafting.sendProgressBarUpdate(this, 1, vac.experience);
//
//
//            if (icrafting instanceof Player)
//                PacketHandlerCore.sendPowerSyncPacket(vac, (Player) icrafting);
//        }
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            //case 1: vac.experience = par2; break;
//        }
//    }
//
//    /**
//     * Return this vacuum container's lower vacuum inventory.
//     */
//    public Container getLowerVacuumInventory() {
//        return lowerVacuumInventory;
//    }
//}
