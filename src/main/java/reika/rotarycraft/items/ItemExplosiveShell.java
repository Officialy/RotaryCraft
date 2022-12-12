package reika.rotarycraft.items;

import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.base.ItemRailgunAmmoBase;
import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.item.PrimedTnt;
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
            //return new EntityExplosiveShell(world, pos, vpos, (BlockEntityRailGun) railgun);
            return new PrimedTnt(world, pos.getX(), pos.getY(), pos.getZ(), null);
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
