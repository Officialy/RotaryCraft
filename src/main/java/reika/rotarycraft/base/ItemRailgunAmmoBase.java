package reika.rotarycraft.base;

import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.api.interfaces.RailGunAmmo;
import reika.rotarycraft.registry.RotaryItems;

public abstract class ItemRailgunAmmoBase extends ItemBasic implements RailGunAmmo {

    public ItemRailgunAmmoBase() {
        super(RotaryItems.itemProperties(), 16);
    }

    public RailGunAmmoType getAmmo(ItemStack is, int pwr) {
        return null;
    }

}
