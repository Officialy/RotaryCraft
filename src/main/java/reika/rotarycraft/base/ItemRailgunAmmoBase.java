package reika.rotarycraft.base;

import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.api.interfaces.RailGunAmmo;

public abstract class ItemRailgunAmmoBase extends ItemBasic implements RailGunAmmo {

    public ItemRailgunAmmoBase() {
        super(reika.rotarycraft.registry.RotaryItems.itemProperties(), 16);
    }

    public RailGunAmmoType getAmmo(ItemStack is, int pwr) {
        return null;
    }

}
