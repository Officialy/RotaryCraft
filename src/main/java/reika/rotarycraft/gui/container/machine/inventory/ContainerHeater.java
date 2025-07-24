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
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.Container;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.neoforged.network.IContainerFactory;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class ContainerHeater extends IOMachineMenu {
//
//    Container lowerInv;
//
//    public ContainerHeater(final int id, Inventory player, BlockEntityHeater te) {
//        super(RotaryMenuTypes.HEATER.get(), id, player, te);
//        lowerInv = te;
//        int numRows = lowerInv.getContainerSize() / 9;
//        lowerInv.startOpen(player.player);
//        int var3 = (numRows - 4) * 18;
//        int var4;
//        int var5;
//        int py = 18;
//        for (var4 = 0; var4 < numRows; ++var4) {
//            for (var5 = 0; var5 < 9; ++var5) {
//                this.addSlot(new Slot(lowerInv, var5 + var4 * 9, 8 + var5 * 18, 18 + var4 * 18));
//            }
//        }
//        for (var4 = 0; var4 < 3; ++var4) {
//            for (var5 = 0; var5 < 9; ++var5) {
//                this.addSlot(new Slot(player, var5 + var4 * 9 + 9, 8 + var5 * 18, 103 + var4 * 18 + var3 + py));
//            }
//        }
//
//        for (var4 = 0; var4 < 9; ++var4) {
//            this.addSlot(new Slot(player, var4, 8 + var4 * 18, 161 + var3 + py));
//        }
//    }
//
//    /**
//     * Callback for when the crafting gui is closed.
//     */
//    @Override
//    public final void removed(Player player) {
//        super.removed(player);
//        lowerInv.stopOpen(player);
//    }
//
//    public final Container getLowerInventory() {
//        return lowerInv;
//    }
//
//    public static class Factory implements IContainerFactory<ContainerHeater> {
//        @Override
//        public ContainerHeater create(final int id, final Inventory inv, final FriendlyByteBuf data) {
//            final BlockPos pos = data.readBlockPos();
//            final Level world = inv.player.getCommandSenderWorld();
//            final BlockEntity blockEntity = world.getBlockEntity(pos);
//
//            if (!(blockEntity instanceof BlockEntityHeater)) {
//                throw new IllegalStateException("Invalid block at:" + pos);
//            }
//
//            return new ContainerHeater(id, inv, (BlockEntityHeater) blockEntity);
//        }
//    }
//}
