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
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.network.IContainerFactory;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.engine.BlockEntityGasEngine;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class ContainerEthanol extends IOMachineMenu {
//    private final BlockEntityGasEngine engine;
//
//    public ContainerEthanol(final int id, Inventory player, BlockEntityGasEngine te) {
//        super(RotaryMenuTypes.GAS_ENGINE.get(), id, player, te);
//        engine = te;
//        int getX = engine.getBlockPos().getX();
//        int posY = engine.getBlockPos().getY();
//        int posZ = engine.getBlockPos().getZ();
//        this.addSlot(new Slot(te, 0, 61, 36));
//
//        this.addPlayerInventory(player);
//    }
//
//    /**
//     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
//     */
////    @Override
////    public void broadcastChanges() {
////        super.broadcastChanges();
////
////        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, Engine, "fuel");
////    }
//
//    public static class Factory implements IContainerFactory<ContainerEthanol> {
//        @Override
//        public ContainerEthanol create(final int id, final Inventory inv, final FriendlyByteBuf data) {
//            final BlockPos pos = data.readBlockPos();
//            final Level world = inv.player.getCommandSenderWorld();
//            final BlockEntity blockEntity = world.getBlockEntity(pos);
//
//            if (!(blockEntity instanceof BlockEntityGasEngine)) {
//                throw new IllegalStateException("Invalid block at:" + pos);
//            }
//
//            return new ContainerEthanol(id, inv, (BlockEntityGasEngine) blockEntity);
//        }
//    }
//}
