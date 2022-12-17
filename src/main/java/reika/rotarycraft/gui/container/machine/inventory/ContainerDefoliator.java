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
//
//public class ContainerDefoliator extends IOMachineMenu {
//    private final BlockEntityDefoliator te;
//
//    public ContainerDefoliator(Inventory player, BlockEntityDefoliator defoliator) {
//        super(player, defoliator);
//        te = defoliator;
//        int getY = te.xCoord;
//        int posY = te.yCoord;
//        int posZ = te.zCoord;
//        this.addSlot(0, 80, 17);
//        this.addSlot(1, 80, 53);
//
//        this.addPlayerInventory(player);
//    }
//
//    /**
//     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
//     */
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//    }
//}
