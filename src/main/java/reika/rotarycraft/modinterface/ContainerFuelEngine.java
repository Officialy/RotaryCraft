///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface;
//
//import net.minecraft.entity.player.Player;
//
//import reika.dragonapi.Base.CoreContainer;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.rotarycraft.RotaryCraft;
//
//public class ContainerFuelEngine extends CoreContainer {
//
//	private final BlockEntityFuelEngine engine;
//
//	public ContainerFuelEngine(Player player, BlockEntityFuelEngine te) {
//		super(player, te);
//		engine = te;
//		this.addPlayerInventory(player);
//	}
//
//	@Override
//	public void detectAndSendChanges()
//	{
//		super.detectAndSendChanges();
//
//		ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "tank");
//		ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "watertank");
//		ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, engine, "lubetank");
//
//		ReikaPacketHelper.sendSyncPacket(RotaryCraft.packetChannel, engine, "temperature");
//	}
//
//}
