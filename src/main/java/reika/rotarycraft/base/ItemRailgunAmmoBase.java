package reika.rotarycraft.base;

import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.api.interfaces.RailGunAmmo;

public abstract class ItemRailgunAmmoBase extends ItemBasic implements RailGunAmmo {

    public ItemRailgunAmmoBase() {
        super(new Properties(), 16);
    }

    public RailGunAmmoType getAmmo(ItemStack is, int pwr) {
        return null;
    }

}
