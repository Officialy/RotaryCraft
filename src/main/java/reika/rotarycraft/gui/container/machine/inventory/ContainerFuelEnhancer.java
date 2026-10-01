package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityFuelConverter;
import reika.rotarycraft.registry.RotaryMenus;

/** Original single row of nine machine slots, with 32-bit shaft values and 64-bit power. */
public class ContainerFuelEnhancer extends IOMachineContainer<BlockEntityFuelConverter> {
    public ContainerFuelEnhancer(int id, Inventory inv, FriendlyByteBuf data) { this(id, inv, (BlockEntityFuelConverter)inv.player.level().getBlockEntity(data.readBlockPos())); }
    public ContainerFuelEnhancer(int id, Inventory inv, BlockEntityFuelConverter tile) {
        super(RotaryMenus.FUEL_ENHANCER.get(), id, inv, tile);
        for (int slot = 0; slot < 9; slot++) addSlot(tile.getItemHandler().slot(slot, 8 + slot * 18, 18));
        addPlayerInventoryWithOffset(inv, 0, -35);
        addDataSlots(new ContainerData() {
            @Override public int get(int index) {
                if (index < 2) return tile.omega >>> (index * 16) & 0xFFFF;
                if (index < 4) return tile.torque >>> ((index - 2) * 16) & 0xFFFF;
                return (int)(tile.power >>> ((index - 4) * 16) & 0xFFFF);
            }
            @Override public void set(int index, int value) {
                if (index < 2) { int shift = index * 16; tile.omega = (tile.omega & ~(0xFFFF << shift)) | ((value & 0xFFFF) << shift); }
                else if (index < 4) { int shift = (index - 2) * 16; tile.torque = (tile.torque & ~(0xFFFF << shift)) | ((value & 0xFFFF) << shift); }
                else { int shift = (index - 4) * 16; tile.power = (tile.power & ~(0xFFFFL << shift)) | ((value & 0xFFFFL) << shift); }
            }
            @Override public int getCount() { return 8; }
        });
    }
    @Override public boolean stillValid(net.minecraft.world.entity.player.Player player) { return !tile.isRemoved() && tile.stillValid(player); }
}
