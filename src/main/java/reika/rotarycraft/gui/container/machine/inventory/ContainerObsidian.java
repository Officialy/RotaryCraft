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
//import reika.rotarycraft.blockentities.production.BlockEntityObsidianMaker;
//import reika.rotarycraft.registry.RotaryMenuTypes;
//
//public class ContainerObsidian extends IOMachineMenu {
//    private final BlockEntityObsidianMaker obsidian;
//    private int lastObsidianCookTime;
//
//    public ContainerObsidian(int id, Inventory playerInv, BlockEntityObsidianMaker par2BlockEntityObsidian) {
//        super(RotaryMenuTypes.OBSIDIAN_MAKER.get(), id, playerInv, par2BlockEntityObsidian);
//        lastObsidianCookTime = 0;
//        obsidian = par2BlockEntityObsidian;
//
//        int var3;
//        int var4;
//        for (var3 = 0; var3 < 3; ++var3) {
//            for (var4 = 0; var4 < 3; ++var4) {
//                this.addSlot(new Slot(par2BlockEntityObsidian, var4 + var3 * 3, 62 + var4 * 18, 18 + var3 * 18));
//            }
//        }
//
//        this.addPlayerInventory(playerInv);
//    }
//
//    /**
//     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
//     */
// /*   @Override
//    public void broadcastChanges() {
//        super.broadcastChanges();
//
//        for (int i = 0; i < crafters.size(); i++) {
//            ICrafting icrafting = (ICrafting) crafters.get(i);
//
//            if (lastObsidianCookTime != obsidian.mixTime) {
//                icrafting.sendProgressBarUpdate(this, 0, obsidian.mixTime);
//            }
//            //icrafting.sendProgressBarUpdate(this, 1, obsidian.getWater());
//            //icrafting.sendProgressBarUpdate(this, 2, obsidian.getLava());
//        }
//
//        lastObsidianCookTime = obsidian.mixTime;
//
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, obsidian, "lava");
//        ReikaPacketHelper.sendTankSyncPacket(RotaryCraft.packetChannel, obsidian, "water");
//    }*/
//    @Override
//    public void setData(int par1, int par2) {
//        switch (par1) {
//            case 0:
//                obsidian.mixTime = par2;
//                break;
//            //case 1: obsidian.setWater(par2); break;
//            //case 2: obsidian.setLava(par2); break;
//        }
//    }
//
//    public static class Factory implements IContainerFactory<ContainerObsidian> {
//        @Override
//        public ContainerObsidian create(final int id, final Inventory inv, final FriendlyByteBuf data) {
//            final BlockPos pos = data.readBlockPos();
//            final Level world = inv.player.getCommandSenderWorld();
//            final BlockEntity blockEntity = world.getBlockEntity(pos);
//
//            if (!(blockEntity instanceof BlockEntityObsidianMaker)) {
//                throw new IllegalStateException("Invalid block at:" + pos);
//            }
//
//            return new ContainerObsidian(id, inv, (BlockEntityObsidianMaker) blockEntity);
//        }
//    }
//}
