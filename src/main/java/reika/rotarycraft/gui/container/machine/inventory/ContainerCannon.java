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
//import reika.rotarycraft.base.blockentity.BlockEntityLaunchCannon;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class ContainerCannon extends IOMachineMenu {
//    private final BlockEntityLaunchCannon cannon;
//
//    public ContainerCannon(int id, Inventory player, BlockEntityLaunchCannon par2BlockEntityCannon) {
//        super(RotaryMenuTypes.CANNON.get(), id, player, par2BlockEntityCannon);
//        cannon = par2BlockEntityCannon;
//        int posX = cannon.getBlockPos().getX();
//        int posY = cannon.getBlockPos().getY();
//        int posZ = cannon.getBlockPos().getZ();
//        int dy = 114;
//        if (cannon.targetMode)
//            dy = 48;
//        int i = 0;
//        int dx = 0;
//        //for (int i = 0; i < 2; i++)
//        for (int j = 0; j < cannon.getContainerSize(); j++)
//            this.addSlot(new Slot(par2BlockEntityCannon, j, dx + 8 + j * 18, dy + 18));
//        dy += 40;
//        dx = 18;
//        for (i = 0; i < 3; i++)
//            for (int k = 0; k < 9; k++)
//                this.addSlot(new Slot(player, k + i * 9 + 9, dx + 8 + k * 18, dy + i * 18));
//        dy += 58;
//        for (int j = 0; j < 9; j++)
//            this.addSlot(new Slot(player, j, dx + 8 + j * 18, dy));
//    }
//
//    public static class Factory implements IContainerFactory<ContainerCannon> {
//        @Override
//        public ContainerCannon create(final int id, final Inventory inv, final FriendlyByteBuf data) {
//            final BlockPos pos = data.readBlockPos();
//            final Level world = inv.player.getCommandSenderWorld();
//            final BlockEntity blockEntity = world.getBlockEntity(pos);
//
//            if (!(blockEntity instanceof BlockEntityLaunchCannon)) {
//                throw new IllegalStateException("Invalid block at:" + pos);
//            }
//
//            return new ContainerCannon(id, inv, (BlockEntityLaunchCannon) blockEntity);
//        }
//    }
//}
