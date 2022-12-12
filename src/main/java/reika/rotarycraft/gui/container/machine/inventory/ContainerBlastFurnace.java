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
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.inventory.Slot;
//import net.minecraft.world.item.ItemStack;
//import reika.dragonapi.base.CoreMenu;
//import reika.dragonapi.instantiable.gui.Slot.SlotApprovedItems;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.registry.RotaryItems;
//
//public class ContainerBlastFurnace extends CoreMenu {
//    private final BlockEntityBlastFurnace blast;
//
//    public ContainerBlastFurnace(Inventory player, BlockEntityBlastFurnace te) {
//        super(player, te);
//        blast = te;
//        int getY = blast.xCoord;
//        int posY = blast.yCoord;
//        int posZ = blast.zCoord;
//
//        int id = 0;
//        this.addSlot(new Slot(te, id, 26, 35));
//        id++;
//        for (int i = 0; i < 3; i++) {
//            for (int j = 0; j < 3; j++) {
//                this.addSlot(new Slot(te, id, 62 + j * 18, 17 + i * 18));
//                id++;
//            }
//        }
//        this.addSlot(new SlotFurnace(player, te, 10, 148, 35));
//        this.addSlot(new Slot(te, 11, 26, 54));
//        this.addSlot(new SlotFurnace(player, te, 12, 148, 17));
//        this.addSlot(new SlotFurnace(player, te, 13, 148, 53));
//
//        this.addSlot(new Slot(te, 14, 26, 16));
//
//        this.addSlot(new SlotApprovedItems(te, BlockEntityBlastFurnace.PATTERN_SLOT, 123, 53).addItem(RotaryItems.CRAFTPATTERN.get()));
//
//        this.addPlayerInventory(player);
//    }
//
//    @Override
//    public ItemStack slotClick(int ID, int mouse, int action, Player ep) {
//        if (ID >= 0 && ID < blast.getContainerSize()) {
//            if (slots.get(ID).getClass() == Slot.class) {
//                if (mouse == 2) {
//                    blast.lockedSlots[ID] = !blast.lockedSlots[ID];
//                    blast.syncAllData(false);
//                    return null; //prevent creative item dupe
//                }
//            }
//        }
//        ItemStack is = super.slotClick(ID, mouse, action, ep);
//        if (ID >= 0 && ID < blast.getContainerSize()) {
//            if (ID == 10 || ID == 13 || ID == 12) {
//                if (ReikaItemHelper.matchStacks(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), is)) {
//                    RotaryAchievements.MAKESTEEL.triggerAchievement(ep);
//                }
//            }
//        }
//        return is;
//    }
//
//    @Override
//    protected ItemStack onShiftClickSlot(Player ep, int ID, ItemStack is) {
//        if (ID < blast.getContainerSize() && (ID == 10 || ID == 13 || ID == 12)) {
//            if (ReikaItemHelper.matchStacks(RotaryItems.HSLA_STEEL_INGOT.get().getDefaultInstance(), is)) {
//                RotaryAchievements.MAKESTEEL.triggerAchievement(ep);
//            }
//        }
//        return null;
//    }
//}
