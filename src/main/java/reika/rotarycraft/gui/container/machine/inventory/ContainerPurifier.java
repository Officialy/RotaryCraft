package reika.rotarycraft.gui.container.machine.inventory;

import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.ContainerData;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.base.IOMachineContainer;
import reika.rotarycraft.blockentities.processing.BlockEntityPurifier;
import reika.rotarycraft.registry.RotaryMenus;

/** Original layout with vanilla menu data for networked progress, temperature and power. */
public class ContainerPurifier extends IOMachineContainer<BlockEntityPurifier> {
    public ContainerPurifier(int id, Inventory inv, FriendlyByteBuf data) {
        this(id, inv, (BlockEntityPurifier) inv.player.level().getBlockEntity(data.readBlockPos()));
    }
    public ContainerPurifier(int id, Inventory inv, BlockEntityPurifier tile) {
        super(RotaryMenus.PURIFIER.get(), id, inv, tile);
        addInputSlot(tile, 0, 35, 16);
        addInputSlot(tile, 7, 53, 16);
        for (int i = 0; i < 5; i++) addInputSlot(tile, i + 1, 8 + i * 18, 52);
        addSlot(new Slot(tile, 6, 134, 34) {
            @Override public boolean mayPlace(ItemStack stack) { return false; }
        });
        addPlayerInventory(inv);
        addDataSlots(new ContainerData() {
            @Override public int get(int index) {
                if (index == 0) return tile.cookTime;
                if (index == 1) return tile.temperature;
                if (index < 4) return tile.omega >>> ((index - 2) * 16) & 0xFFFF;
                if (index < 6) return tile.torque >>> ((index - 4) * 16) & 0xFFFF;
                return (int) (tile.power >>> ((index - 6) * 16) & 0xFFFF);
            }
            @Override public void set(int index, int value) {
                if (index == 0) tile.cookTime = value;
                else if (index == 1) tile.temperature = value;
                else if (index < 4) {
                    int shift = (index - 2) * 16;
                    tile.omega = (tile.omega & ~(0xFFFF << shift)) | ((value & 0xFFFF) << shift);
                } else if (index < 6) {
                    int shift = (index - 4) * 16;
                    tile.torque = (tile.torque & ~(0xFFFF << shift)) | ((value & 0xFFFF) << shift);
                } else {
                    int shift = (index - 6) * 16;
                    tile.power = (tile.power & ~(0xFFFFL << shift)) | ((value & 0xFFFFL) << shift);
                }
            }
            @Override public int getCount() { return 10; }
        });
    }
    private void addInputSlot(BlockEntityPurifier tile, int slot, int x, int y) {
        addSlot(new Slot(tile, slot, x, y) {
            @Override public boolean mayPlace(ItemStack stack) { return tile.isItemValidForSlot(slot, stack); }
        });
    }
}
