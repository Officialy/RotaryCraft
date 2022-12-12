///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface.conversion;
//
//import net.minecraft.world.entity.player.Player;
//import reika.dragonapi.base.CoreMenu;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
//
//public class ContainerEnergyToPower extends CoreMenu {
//
//	private EnergyToPowerBase engine;
//
//	public ContainerEnergyToPower(Player player, EnergyToPowerBase te) {
//		super();
////		super(player, te);
//		engine = te;
//	}
//
//	/**
//	 * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
//	 */
////	@Override
//	public void detectAndSendChanges()
//	{
////		super.detectAndSendChanges();
//
//		ReikaPacketHelper.sendSyncPacket(RotaryCraft.packetChannel, engine, "storedEnergy");
//		ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "tank");
//	}
//
//}
