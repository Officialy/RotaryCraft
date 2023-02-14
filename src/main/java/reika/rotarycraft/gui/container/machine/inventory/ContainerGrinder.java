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
import net.minecraftforge.common.capabilities.ForgeCapabilities;
import net.minecraftforge.items.IItemHandlerModifiable;
import net.minecraftforge.items.SlotItemHandler;
import org.jetbrains.annotations.NotNull;
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
        this(id, inv, (BlockEntityGrinder) inv.player.level.getBlockEntity(data.readBlockPos()));
    }

    public ContainerGrinder(int id, Inventory inv, BlockEntityGrinder te) {
        super(RotaryMenus.GRINDER.get(), id, inv, te);
        lastGrinderCookTime = 0;
        grinder = te;
        te.getCapability(ForgeCapabilities.ITEM_HANDLER).ifPresent(itemHandler -> {
            this.addSlot(new SlotItemHandler(itemHandler, 0, 76, 35) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return grinder.isItemValidForSlot(0, stack);
                }
            });
            this.addSlot(new ResultSlotItemHandler(itemHandler, 1, 136, 35));
            this.addSlot(new SlotItemHandler(itemHandler, 2, 35, 60) {
                @Override
                public boolean mayPlace(ItemStack stack) {
                    return grinder.isItemValidForSlot(2, stack);
                }
            });
        });

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