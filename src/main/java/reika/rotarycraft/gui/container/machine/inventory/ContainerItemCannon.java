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
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraftforge.network.IContainerFactory;
//import reika.rotarycraft.base.IOMachineMenu;
//import reika.rotarycraft.blockentities.BlockEntityItemCannon;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class ContainerItemCannon extends IOMachineMenu {
//    private final BlockEntityItemCannon cannon;
//
//    public ContainerItemCannon(int id, Inventory inv, BlockEntityItemCannon par2BlockEntityItemCannon) {
//        super(RotaryMenuTypes.ITEM_CANNON.get(), id, inv, par2BlockEntityItemCannon);
//        cannon = par2BlockEntityItemCannon;
//        int posX = cannon.getBlockPos().getX();
//        int posY = cannon.getBlockPos().getY();
//        int posZ = cannon.getBlockPos().getZ();
//        int dy = 48;
//        int i = 0;
//        for (int j = 0; j < 9; j++)
//            this.addSlot(new Slot(par2BlockEntityItemCannon, j + 9 * i, 8 + j * 18, dy + i * 18 + 18));
//        dy += 40;
//        for (i = 0; i < 3; i++)
//            for (int k = 0; k < 9; k++)
//                this.addSlot(new Slot(inv, k + i * 9 + 9, 8 + k * 18, dy + i * 18));
//        dy += 58;
//        for (int j = 0; j < 9; j++)
//            this.addSlot(new Slot(inv, j, 8 + j * 18, dy));
//    }
//
//    @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//    }
//
//    public static class Factory implements IContainerFactory<ContainerItemCannon> {
//        @Override
//        public ContainerItemCannon create(final int id, final Inventory inv, final FriendlyByteBuf data) {
//            final BlockPos pos = data.readBlockPos();
//            final Level world = inv.player.getCommandSenderWorld();
//            final BlockEntity blockEntity = world.getBlockEntity(pos);
//
//            if (!(blockEntity instanceof BlockEntityItemCannon)) {
//                throw new IllegalStateException("Invalid block at:" + pos);
//            }
//
//            return new ContainerItemCannon(id, inv, (BlockEntityItemCannon) blockEntity);
//        }
//    }
//}
