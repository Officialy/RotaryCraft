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
//import reika.rotarycraft.blockentities.level.BlockEntityAerosolizer;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class ContainerAerosolizer extends IOMachineMenu {
//    private final BlockEntityAerosolizer aerosolizer;
//
//    public ContainerAerosolizer(int id, Inventory player, BlockEntityAerosolizer par2BlockEntityAerosolizer) {
//        super(RotaryMenuTypes.AEROSOLIZER.get(), id, player, par2BlockEntityAerosolizer);
//        aerosolizer = par2BlockEntityAerosolizer;
//        int var3;
//        int var4;
//
//        for (var3 = 0; var3 < 3; ++var3) {
//            for (var4 = 0; var4 < 3; ++var4) {
//                this.addSlot(new Slot(par2BlockEntityAerosolizer, var4 + var3 * 3, 62 + var4 * 18, 17 + var3 * 18));
//            }
//        }
//        //addSlot(new SlotFurnace(par1InventoryPlayer.player, par2BlockEntityAerosolizer, 2, 116, 35));
//
//        this.addPlayerInventory(player);
//    }
//
//    public static class Factory implements IContainerFactory<ContainerAerosolizer> {
//        @Override
//        public ContainerAerosolizer create(final int id, final Inventory inv, final FriendlyByteBuf data) {
//            final BlockPos pos = data.readBlockPos();
//            final Level world = inv.player.getCommandSenderWorld();
//            final BlockEntity blockEntity = world.getBlockEntity(pos);
//
//            if (!(blockEntity instanceof BlockEntityAerosolizer)) {
//                throw new IllegalStateException("Invalid block at:" + pos);
//            }
//
//            return new ContainerAerosolizer(id, inv, (BlockEntityAerosolizer) blockEntity);
//        }
//    }
//}
