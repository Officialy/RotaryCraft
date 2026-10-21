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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.item.ItemStack;
import net.neoforged.neoforge.transfer.item.ResourceHandlerSlot;
import reika.dragonapi.instantiable.gui.slot.ResultSlotItemHandler;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.production.BlockEntityFermenter;
import reika.rotarycraft.registry.RotaryMenus;

public class ContainerFermenter extends IOMachineContainer<BlockEntityFermenter> {
    private boolean sludgeMode;
    private int operationTime = 1;

    public ContainerFermenter(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityFermenter) inv.player.level().getBlockEntity(data.readBlockPos()));
    }

    public ContainerFermenter(int id, Inventory inv, BlockEntityFermenter te) {
        super(RotaryMenus.FERMENTER.get(), id, inv, te);
        sludgeMode = te.isSludgeMode();
        for (int i = 0; i < 2; i++) {
            final int input = i;
            addSlot(new ResourceHandlerSlot(te.itemHandler, te.itemHandler::set, i, 55, 17 + i * 36) {
                @Override public boolean mayPlace(ItemStack stack) {
                    return te.isItemValidForMode(input, stack, isSludgeMode()) && super.mayPlace(stack);
                }
            });
        }
        addSlot(new ResultSlotItemHandler(te.itemHandler, 2, 116, 35, stack -> {
            if (stack.is(reika.rotarycraft.registry.RotaryItems.YEAST.get()))
                reika.rotarycraft.registry.RotaryAdvancements.MAKEYEAST.triggerAchievement(inv.player);
        }));
        addPlayerInventory(inv);
        // Vanilla menu data carries signed shorts. Split shaft values and power into 16-bit words.
        addDataSlots(new ContainerData() {
            @Override public int getCount() { return 13; }
            @Override public int get(int index) {
                return switch (index) {
                    case 0 -> te.fermenterCookTime;
                    case 1 -> te.temperature;
                    case 2 -> te.getLiquidLevel();
                    case 3 -> te.isSludgeMode() ? 1 : 0;
                    case 4 -> te.getOperationTime();
                    case 5, 6 -> te.omega >>> ((index - 5) * 16) & 0xFFFF;
                    case 7, 8 -> te.torque >>> ((index - 7) * 16) & 0xFFFF;
                    default -> (int)(te.power >>> ((index - 9) * 16) & 0xFFFF);
                };
            }
            @Override public void set(int index, int value) {
                switch (index) {
                    case 0 -> te.fermenterCookTime = value;
                    case 1 -> te.temperature = value;
                    case 2 -> te.setLiquid(value);
                    case 3 -> sludgeMode = value != 0;
                    case 4 -> operationTime = Math.max(1, value & 0xFFFF);
                    case 5, 6 -> {
                        int shift = (index - 5) * 16;
                        te.omega = (te.omega & ~(0xFFFF << shift)) | ((value & 0xFFFF) << shift);
                    }
                    case 7, 8 -> {
                        int shift = (index - 7) * 16;
                        te.torque = (te.torque & ~(0xFFFF << shift)) | ((value & 0xFFFF) << shift);
                    }
                    default -> {
                        int shift = (index - 9) * 16;
                        te.power = (te.power & ~(0xFFFFL << shift)) | ((value & 0xFFFFL) << shift);
                    }
                }
            }
        });
    }

    public boolean isSludgeMode() {
        return tile.getLevel() != null && !tile.getLevel().isClientSide() ? tile.isSludgeMode() : sludgeMode;
    }

    public int getCookProgressScaled(int width) {
        int duration = tile.getLevel() != null && !tile.getLevel().isClientSide() ? tile.getOperationTime() : operationTime;
        return Math.clamp((int)((long)tile.fermenterCookTime * width / 2 / Math.max(1, duration)), 0, width);
    }

    @Override public ItemStack quickMoveStack(Player player, int index) {
        if (index < 0 || index >= slots.size() || !slots.get(index).hasItem()) return ItemStack.EMPTY;
        var slot = slots.get(index);
        ItemStack stack = slot.getItem(), original = stack.copy();
        if (index < 3) {
            if (!moveItemStackTo(stack, 3, slots.size(), true)) return ItemStack.EMPTY;
        } else if (!moveItemStackTo(stack, 0, 2, false)) return ItemStack.EMPTY;
        // ResourceHandlerSlot reads a snapshot; write the remaining stack back explicitly.
        slot.set(stack.isEmpty() ? ItemStack.EMPTY : stack);
        slot.onTake(player, original.copyWithCount(original.getCount() - stack.getCount()));
        return original;
    }

    @Override public boolean stillValid(Player player) {
        return !tile.isRemoved() && tile.isPlayerAccessible(player);
    }
}
