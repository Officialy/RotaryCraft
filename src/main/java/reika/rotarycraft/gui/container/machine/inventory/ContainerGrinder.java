/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.registry.RotaryMenus;


public class ContainerGrinder extends IOMachineContainer<BlockEntityGrinder> {
    private final BlockEntityGrinder grinder;
    private int lastGrinderCookTime;

    //Client
    public ContainerGrinder(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityGrinder) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerGrinder(int id, Inventory inv, BlockEntityGrinder te) {
        super(RotaryMenus.GRINDER.get(), id, inv, te);
        lastGrinderCookTime = 0;
        grinder = te;
        // 26.1: slot positions from the original 1.7 ContainerGrinder — input at (76, 35),
        // output at (136, 35), lube-bucket at (35, 60). The port previously used (56, 17),
        // (116, 35), (116, 53) which didn't match the {@code grindergui.png} background.
        this.addSlot(grinder.itemHandler.slot(0, 76, 35));
        this.addSlot(new ResultSlotItemHandler(grinder.itemHandler, 1, 136, 35));
        this.addSlot(new ResultSlotItemHandler(grinder.itemHandler, 2, 35, 60));
        this.addPlayerInventory(inv);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
    @Override
    public void broadcastChanges() {
        super.broadcastChanges();

    /*    for (int i = 0; i < crafters.size(); i++) {
            ICrafting icrafting = (ICrafting) crafters.get(i);

            if (lastGrinderCookTime != grinder.grinderCookTime) {
                icrafting.sendProgressBarUpdate(this, 0, grinder.grinderCookTime);
            }
            icrafting.sendProgressBarUpdate(this, 1, grinder.getLevel());
        }*/

        lastGrinderCookTime = grinder.grinderCookTime;
    }

    @Override
    public void setData(int par1, int par2) {
        switch (par1) {
            case 1 -> grinder.setLevel(par2);
            case 0 -> grinder.grinderCookTime = par2;
        }
    }

}