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

import net.minecraft.core.registries.Registries;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.level.BlockEntityTerraformer;
import reika.rotarycraft.registry.RotaryMenus;

/** Original 54-slot layout; menu-owned state and server-validated biome selection. */
public class ContainerTerraformer extends IOMachineContainer<BlockEntityTerraformer> {
    public int water;
    public ContainerTerraformer(int id, Inventory inv, FriendlyByteBuf data) { this(id, inv, (BlockEntityTerraformer)inv.player.level().getBlockEntity(data.readBlockPos())); }
    public ContainerTerraformer(int id, Inventory inv, BlockEntityTerraformer tile) {
        super(RotaryMenus.TERRAFORMER.get(), id, inv, tile);
        for (int row = 0; row < 6; row++) for (int col = 0; col < 9; col++) addSlot(tile.getItemHandler().slot(row * 9 + col, 72 + col * 18, 18 + row * 18));
        for (int row = 0; row < 3; row++) for (int col = 0; col < 9; col++) addSlot(new Slot(inv, 9 + row * 9 + col, 72 + col * 18, 140 + row * 18));
        for (int col = 0; col < 9; col++) addSlot(new Slot(inv, col, 72 + col * 18, 198));
        addDataSlots(new ContainerData() {
            @Override public int getCount() { return 12; }
            @Override public int get(int i) {
                if (i == 0) return tile.getLiquidLevel();
                if (i == 1) return tile.operationTicks;
                if (i == 2) return tile.queuedCells;
                if (i == 3) return tile.getTarget() == null ? -1 : tile.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).get(tile.getTarget()).map(b -> tile.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getId(b.value())).orElse(-1);
                if (i < 6) return tile.omega >>> ((i - 4) * 16) & 0xffff;
                if (i < 8) return tile.torque >>> ((i - 6) * 16) & 0xffff;
                return (int)(tile.power >>> ((i - 8) * 16)) & 0xffff;
            }
            @Override public void set(int i, int value) {
                if (i == 0) water = value;
                else if (i == 1) tile.operationTicks = value;
                else if (i == 2) tile.queuedCells = value;
                else if (i == 3) {
                    var biome = value < 0 ? null : tile.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).get(value).orElse(null);
                    tile.setTarget(biome == null ? null : biome.getKey());
                } else if (i < 6) { int shift = (i - 4) * 16; tile.omega = (tile.omega & ~(0xffff << shift)) | ((value & 0xffff) << shift); }
                else if (i < 8) { int shift = (i - 6) * 16; tile.torque = (tile.torque & ~(0xffff << shift)) | ((value & 0xffff) << shift); }
                else { int shift = (i - 8) * 16; tile.power = (tile.power & ~(0xffffL << shift)) | ((value & 0xffffL) << shift); }
            }
        });
    }
    @Override public net.minecraft.world.item.ItemStack quickMoveStack(Player player, int index) {
        var slot = slots.get(index);
        if (!slot.hasItem()) return net.minecraft.world.item.ItemStack.EMPTY;
        var stack = slot.getItem();
        var original = stack.copy();
        if (index < 54 ? !moveItemStackTo(stack, 54, slots.size(), true) : !moveItemStackTo(stack, 0, 54, false)) return net.minecraft.world.item.ItemStack.EMPTY;
        if (stack.isEmpty()) slot.set(net.minecraft.world.item.ItemStack.EMPTY); else slot.setChanged();
        slot.onTake(player, stack);
        return original;
    }
    @Override public boolean clickMenuButton(Player player, int id) {
        if (player.containerMenu != this || !stillValid(player) || player.level().isClientSide()) return false;
        var choice = tile.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).get(id).orElse(null);
        if (choice == null || tile.getValidTargetBiomes(tile.getCentralBiome()).stream().noneMatch(r -> r.target().equals(choice.getKey()))) return false;
        tile.setTarget(choice.getKey());
        return true;
    }
}
