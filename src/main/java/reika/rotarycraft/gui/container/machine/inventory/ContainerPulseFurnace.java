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
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.processing.BlockEntityPulseFurnace;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//
//public class ContainerPulseFurnace extends IOMachineMenu {
//    private final BlockEntityPulseFurnace pulseFurnace;
//
//    public ContainerPulseFurnace(Player player, BlockEntityPulseFurnace par2BlockEntityPulseFurnace) {
//        super(player, par2BlockEntityPulseFurnace);
//        pulseFurnace = par2BlockEntityPulseFurnace;
//
//        this.addSlot(new Slot(par2BlockEntityPulseFurnace, 0, 125, 16));
//        //addSlot(new Slot(par2BlockEntityPulseFurnace, 1, 70, 34));
//        this.addSlot(new SlotFurnace(player, par2BlockEntityPulseFurnace, 2, 125, 52));
//
//        this.addPlayerInventory(player);
//    }
//
//    @Override
//    public void addCraftingToCrafters(ICrafting par1ICrafting) {
//        super.addCraftingToCrafters(par1ICrafting);
//        par1ICrafting.sendProgressBarUpdate(this, 0, pulseFurnace.pulseFurnaceCookTime);
//        par1ICrafting.sendProgressBarUpdate(this, 2, pulseFurnace.smelttick);
//    }
//
//    /**
//     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
//     */
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//            icrafting.sendProgressBarUpdate(this, 0, pulseFurnace.pulseFurnaceCookTime);
//            icrafting.sendProgressBarUpdate(this, 1, pulseFurnace.temperature);
//            icrafting.sendProgressBarUpdate(this, 2, pulseFurnace.smelttick);
//            //icrafting.sendProgressBarUpdate(this, 3, pulseFurnace.getFuel());
//            icrafting.sendProgressBarUpdate(this, 4, pulseFurnace.omega);
//            //icrafting.sendProgressBarUpdate(this, 5, pulseFurnace.getWater());
//        }
//
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, pulseFurnace, "water");
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, pulseFurnace, "fuel");
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, pulseFurnace, "accel");
//    }
//
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            case 0:
//                pulseFurnace.pulseFurnaceCookTime = par2;
//                break;
//            case 1:
//                pulseFurnace.temperature = par2;
//                break;
//            case 2:
//                pulseFurnace.smelttick = par2;
//                break;
//            //case 3: pulseFurnace.setFuel(par2); break;
//            case 4:
//                pulseFurnace.omega = par2;
//                break;
//            //case 5: pulseFurnace.setWater(par2); break;
//        }
//    }
//}
