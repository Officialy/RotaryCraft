package reika.rotarycraft.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface RailGunAmmo {

    RailGunAmmoType getAmmo(ItemStack is, int pwr);

    interface RailGunAmmoType extends Comparable<RailGunAmmoType> {

        int getRequiredTorque();

        ItemStack getItem();

        Entity getProjecBlockEntity(Level world, BlockPos pos, BlockPos vpos, BlockEntity railgun);

        /**
         * Return > 0 for "more powerful than"; < 1 for "less powerful than", 0 for "equal"
         */
        int compareTo(RailGunAmmoType type);

        boolean isExplosive();

        boolean isMagic();

        int getMass();

    }

}
