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
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.phys.Vec3;
import reika.rotarycraft.base.ItemRailgunAmmoBase;
import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun;
import reika.rotarycraft.entities.EntityRailGunShot;
import reika.rotarycraft.registry.RotaryDataComponents;
import reika.rotarycraft.registry.RotaryItems;

/** All sixteen V33a masses; torque only decides which existing stack the cannon can fire. */
public final class ItemRailGunAmmo extends ItemRailgunAmmoBase implements reika.dragonapi.interfaces.LegacyItemData {
    private static final BasicRailGunAmmo[] TYPES = java.util.stream.IntStream.range(0, 16)
            .mapToObj(BasicRailGunAmmo::new).toArray(BasicRailGunAmmo[]::new);

    public ItemRailGunAmmo() { super(); }

    public static int power(ItemStack stack) {
        Integer tier = stack.get(RotaryDataComponents.RAILGUN_POWER.get());
        // Old port stacks used vanilla DAMAGE as metadata, not durability. Typed data wins when present.
        return Math.clamp(tier != null ? tier : stack.getOrDefault(DataComponents.DAMAGE, 0), 0, 15);
    }
    @Override public void migrateLegacyComponents(ItemStack stack) {
        if (stack.has(DataComponents.DAMAGE)) {
            if (!stack.has(RotaryDataComponents.RAILGUN_POWER.get()))
                stack.set(RotaryDataComponents.RAILGUN_POWER.get(), power(stack));
            stack.remove(DataComponents.DAMAGE);
        }
    }
    public static ItemStack stack(int power) {
        ItemStack stack = RotaryItems.RAILGUN_AMMO.get().getDefaultInstance();
        stack.set(RotaryDataComponents.RAILGUN_POWER.get(), checked(power));
        return stack;
    }
    private static int checked(int power) {
        if (power < 0 || power > 15) throw new IllegalArgumentException("Railgun tier must be 0..15: " + power);
        return power;
    }
    public static BasicRailGunAmmo getType(int power) { return TYPES[checked(power)]; }
    @Override public RailGunAmmoType getAmmo(ItemStack stack) { return getType(power(stack)); }
    @Override public RailGunAmmoType getAmmo(ItemStack stack, int ignoredTorqueTier) { return getAmmo(stack); }
    @Override public Component getName(ItemStack stack) {
        return Component.translatable("item.rotarycraft.railgun_ammo.mass", getType(power(stack)).getMass());
    }
    public static final class BasicRailGunAmmo implements RailGunAmmoType {
        private final int power;
        private BasicRailGunAmmo(int power) { this.power = power; }
        public int power() { return power; }
        @Override public Entity getProjecBlockEntity(Level world, BlockPos pos, BlockPos target, BlockEntity railgun) {
            return getProjectileEntity(world, Vec3.atCenterOf(pos), Vec3.atCenterOf(target).subtract(Vec3.atCenterOf(pos)).normalize().scale(4), railgun);
        }
        @Override public Entity getProjectileEntity(Level world, Vec3 position, Vec3 velocity, BlockEntity railgun) {
            if (!(railgun instanceof BlockEntityRailGun gun)) throw new IllegalArgumentException("A railgun projectile needs its firing cannon");
            return new EntityRailGunShot(world, position, velocity, gun, this);
        }
        @Override public ItemStack getItem() { return stack(power); }
        @Override public int getRequiredTorque() { return (int)Math.sqrt(512 * (1 << power)); }
        @Override public int compareTo(RailGunAmmoType other) {
            return other instanceof BasicRailGunAmmo basic ? Integer.compare(power, basic.power) : 1 + power;
        }
        @Override public boolean isExplosive() { return false; }
        @Override public boolean isMagic() { return false; }
        @Override public int getMass() { return 1 << power; }
    }
}
