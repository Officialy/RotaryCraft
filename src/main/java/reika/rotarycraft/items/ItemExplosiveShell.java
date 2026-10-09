package reika.rotarycraft.items;

import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.base.ItemRailgunAmmoBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.phys.Vec3;
import reika.rotarycraft.entities.EntityExplosiveShell;
import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;

public class ItemExplosiveShell extends ItemRailgunAmmoBase {

    public ItemExplosiveShell() {
        super();
    }

    @Override
    public RailGunAmmoType getAmmo(ItemStack is, int pwr) {
        return ExplosiveRailGunAmmo.instance;
    }

    public static class ExplosiveRailGunAmmo implements RailGunAmmoType {

        public static final ExplosiveRailGunAmmo instance = new ExplosiveRailGunAmmo();

        private ExplosiveRailGunAmmo() {

        }

        @Override
        public Entity getProjecBlockEntity(Level world, BlockPos pos, BlockPos vpos, BlockEntity railgun) {
            return getProjectileEntity(world, Vec3.atCenterOf(pos), Vec3.atCenterOf(vpos).subtract(Vec3.atCenterOf(pos)).normalize().scale(4), railgun);
        }

        @Override
        public Entity getProjectileEntity(Level world, Vec3 position, Vec3 velocity, BlockEntity railgun) {
            if (!(railgun instanceof BlockEntityRailGun gun)) throw new IllegalArgumentException("An explosive shell needs its firing cannon");
            return new EntityExplosiveShell(world, position, velocity, gun);
        }

        @Override
        public ItemStack getItem() {
            return RotaryItems.SHELL.get().getDefaultInstance();
        }

        @Override
        public int getRequiredTorque() {
            return 0;
        }

        @Override
        public int compareTo(RailGunAmmoType type) {
            return -1;
        }

        @Override
        public boolean isExplosive() {
            return true;
        }

        @Override
        public boolean isMagic() {
            return false;
        }

        @Override
        public int getMass() {
            return 6;
        }

    }

}
