package reika.rotarycraft.base;

import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.inventory.MenuType;
import net.minecraft.world.inventory.Slot;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.base.CoreContainer;

public class OneSlotContainer<T extends BlockEntityBase> extends CoreContainer<T> {

    private final Container inv;

    public OneSlotContainer(MenuType<?> type, int id, Inventory player, T te) {
        this(type, id, player, te, 0);
    }

    public OneSlotContainer(MenuType<?> type, int id, Inventory inventory, T te, int offsetY) {
        super(type, id, inventory, te);
        epInv = inventory;
        inv = (Container)te;
        this.addSlot(new Slot(inv, 0, 80, 35));

        this.addPlayerInventoryWithOffset(inventory, 0, offsetY);
    }

    /**
     * Updates crafting matrix; called from onCraftMatrixChanged. Args: none
     */
/*    @Override
    public void detectAndSendChanges()
    {
        super.detectAndSendChanges();

        for (int i = 0; i < crafters.size(); i++)
        {
            ICrafting icrafting = (ICrafting)crafters.get(i);
        }
    }*/

}
