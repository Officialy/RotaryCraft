package reika.rotarycraft.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public interface RailGunAmmo {

    /** The ammunition stack, not the turret torque, defines its type. */
    default RailGunAmmoType getAmmo(ItemStack stack) { return getAmmo(stack, stack.getDamageValue()); }

    /** Compatibility with the early port's two-argument interface. */
    RailGunAmmoType getAmmo(ItemStack is, int pwr);

    interface RailGunAmmoType extends Comparable<RailGunAmmoType> {

        int getRequiredTorque();

        ItemStack getItem();

        Entity getProjecBlockEntity(Level world, BlockPos pos, BlockPos vpos, BlockEntity railgun);

        /** Precise muzzle position and the original four-blocks-per-tick velocity. */
        default Entity getProjectileEntity(Level world, Vec3 position, Vec3 velocity, BlockEntity railgun) {
            Entity entity = getProjecBlockEntity(world, BlockPos.containing(position), BlockPos.containing(position.add(velocity)), railgun);
            if (entity != null) { entity.setPos(position); entity.setDeltaMovement(velocity); }
            return entity;
        }

        /**
         * Return > 0 for "more powerful than"; < 1 for "less powerful than", 0 for "equal"
         */
        int compareTo(RailGunAmmoType type);

        boolean isExplosive();

        boolean isMagic();

        int getMass();

    }

}
