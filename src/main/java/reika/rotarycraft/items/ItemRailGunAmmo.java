/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.base.ItemRailgunAmmoBase;
import reika.rotarycraft.registry.RotaryItems;

import java.util.HashMap;

public class ItemRailGunAmmo extends ItemRailgunAmmoBase {

    public ItemRailGunAmmo() {
        super();
    }

    public static RailGunAmmoType getType(int pwr) {
        BasicRailGunAmmo ret = BasicRailGunAmmo.cache.get(pwr);
        if (ret == null) {
            ret = new BasicRailGunAmmo(pwr);
            BasicRailGunAmmo.cache.put(pwr, ret);
        }
        return ret;
    }

    @Override
    public RailGunAmmoType getAmmo(ItemStack is, int pwr) {
        return getType(pwr);
    }

    public static class BasicRailGunAmmo implements RailGunAmmoType {

        private static final HashMap<Integer, BasicRailGunAmmo> cache = new HashMap();

        private final int power;

        private BasicRailGunAmmo(int p) {
            power = p;
        }

        @Override
        public Entity getProjecBlockEntity(Level world, BlockPos pos, BlockPos vpos, BlockEntity railgun) {
            //return new EntityRailGunShot(world, pos, vpos, power, (BlockEntityRailGun) railgun);
            return null;
        }

        @Override
        public ItemStack getItem() {
            return RotaryItems.RAILGUN_AMMO.get().getDefaultInstance();
        }

        public int getRequiredTorque() {
            return (int) Math.sqrt(512 * ReikaMathLibrary.intpow2(2, power));
        }

        @Override
        public int compareTo(RailGunAmmoType type) {
            return 1 + power;
        }

        @Override
        public boolean isExplosive() {
            return false;
        }

        @Override
        public boolean isMagic() {
            return false;
        }

        @Override
        public int getMass() {
            return ReikaMathLibrary.intpow2(2, power);
        }

    }

}
