/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.charged;

import java.util.List;
import java.util.Locale;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.enderdragon.EndCrystal;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.ClipContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.HitResult;
import net.minecraft.world.phys.Vec3;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.auxiliary.GravelGunDamage;
import reika.rotarycraft.base.ItemChargedTool;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryAdvancements;
import reika.rotarycraft.registry.RotaryItems;

/** V33a's hitscan Gravel Gun, including its charge formula, PvP protections and three milestones. */
public class ItemGravelGun extends ItemChargedTool {
    public ItemGravelGun() {
        super(RotaryItems.itemProperties().component(DataComponents.MAX_DAMAGE, 32001)
                .component(DataComponents.DAMAGE, 0));
    }

    @Override
    public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand);
        int charge = stack.getDamageValue();
        if (charge <= 0) {
            if (!world.isClientSide())
                player.sendOverlayMessage(Component.translatable("message.rotarycraft.tool_charge_depleted"));
            return InteractionResult.FAIL;
        }
        int ammoSlot = findAmmo(player);
        if (!player.isCreative() && ammoSlot < 0) {
            if (!world.isClientSide())
                world.playSound(null, player.blockPosition(), SoundEvents.DISPENSER_FAIL, SoundSource.PLAYERS, 1, 1);
            return InteractionResult.FAIL;
        }
        if (!(world instanceof ServerLevel server)) return InteractionResult.SUCCESS;
        Vec3 look = player.getLookAngle();
        Vec3 eye = player.getEyePosition();
        for (double range = 1; range <= 128; range += .5) {
            Vec3 point = eye.add(look.scale(range));
            List<Entity> targets = world.getEntities(player, new AABB(point.x - .5, point.y - .5, point.z - .5,
                    point.x + .5, point.y + .5, point.z + .5), entity -> attackable(entity, player)
                    && world.clip(new ClipContext(eye, entity.getBoundingBox().getCenter(),
                            ClipContext.Block.COLLIDER, ClipContext.Fluid.NONE, player)).getType() == HitResult.Type.MISS);
            if (targets.isEmpty()) continue;
            for (Entity target : targets) {
                Vec3 delta = target.position().subtract(player.position());
                var flint = new ItemEntity(world, player.getX() + look.x, player.getY() + look.y,
                        player.getZ() + look.z, new ItemStack(Items.FLINT));
                flint.setPickUpDelay(100);
                flint.lifespan = 5;
                flint.setDeltaMovement(delta.x, delta.y + 1, delta.z);
                world.addFreshEntity(flint);
                world.playSound(null, player.blockPosition(), SoundEvents.GRAVEL_BREAK, SoundSource.PLAYERS, 1.5F, 2F);
                float damage = getAttackDamage(charge);
                if (target instanceof EnderDragon dragon) {
                    // V33a attacked the body part; modern dragons otherwise reject a direct player hit.
                    var body = java.util.Arrays.stream(dragon.getSubEntities())
                            .filter(part -> part.name.equals("body")).findFirst().orElseThrow();
                    dragon.hurt(server, body, player.damageSources().playerAttack(player), damage);
                } else if (target instanceof EndCrystal) {
                    target.hurtServer(server, player.damageSources().playerAttack(player), damage);
                } else if (target instanceof LivingEntity living) {
                    if (target instanceof Player) {
                        for (ItemStack armor : List.of(living.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.HEAD),
                                living.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.CHEST),
                                living.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.LEGS),
                                living.getItemBySlot(net.minecraft.world.entity.EquipmentSlot.FEET))) {
                            var id = BuiltInRegistries.ITEM.getKey(armor.getItem());
                            if (id.getNamespace().equals("rotarycraft") && id.getPath().startsWith("bedrock_alloy_")) damage *= .75F;
                        }
                    }
                    float before = living.getHealth();
                    living.hurtServer(server, new GravelGunDamage(player), damage);
                    if (living.getHealth() > 0) {
                        float health = Math.min(living.getHealth(), before - Math.min(10, damage));
                        if (health <= 0) {
                            living.setHealth(.01F);
                            living.hurtServer(server, new GravelGunDamage(player), damage);
                        } else living.setHealth(health);
                    }
                    if (damage >= 500) RotaryAdvancements.MASSIVEHIT.triggerAchievement(player);
                    if (target instanceof Monster && living.getHealth() <= 0 && delta.length() >= 80)
                        RotaryAdvancements.GRAVELGUN.triggerAchievement(player);
                }
                Vec3 direction = delta.normalize();
                for (float t = 0; t < 2; t += .05F)
                    server.sendParticles(ParticleTypes.CRIT, eye.x + look.x, eye.y + look.y, eye.z + look.z,
                            0, direction.x * t, direction.y * t, direction.z * t, 1);
            }
            // Upstream awards this for two targets in the same ray slice, not a fabricated kill count.
            if (targets.size() > 1) RotaryAdvancements.DOUBLEKILL.triggerAchievement(player);
            if (!player.isCreative()) {
                ItemStack ammo = player.getInventory().getItem(ammoSlot).copy();
                ammo.shrink(1);
                player.getInventory().setItem(ammoSlot, ammo);
            }
            stack.setDamageValue(charge - getChargeConsumed(charge));
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

    private static int findAmmo(Player player) {
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++)
            if (player.getInventory().getItem(slot).is(Items.GRAVEL)) return slot;
        return -1;
    }

    private static boolean attackable(Entity target, Player player) {
        if (target instanceof Player other)
            return !player.getUUID().equals(other.getUUID()) && !ReikaPlayerAPI.isReika(other) && ConfigRegistry.GRAVELPLAYER.getState();
        return target instanceof LivingEntity || target instanceof EndCrystal;
    }

    public static int getChargeConsumed(int charge) {
        return Math.max(1, (int)(Math.log(1D + charge) / Math.log(2)));
    }

    public static float getAttackDamage(int charge) {
        if (charge <= 0) return 0;
        if (charge == 1) return 1;
        long half = charge / 2;
        long power = half * half * half;
        boolean hard = ConfigRegistry.HARDGRAVELGUN.getState();
        double base = (hard ? 1.00005 : 1.0001) + Math.pow(charge, hard ? .15 : .1875) / 150000D;
        return (float)(1 + Math.log(power) / Math.log(2) / 2 * Math.pow(base, charge));
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display,
                                Consumer<Component> tooltip, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, tooltip, flag);
        float damage = getAttackDamage(stack.getDamageValue());
        tooltip.accept(damage > 0 ? Component.translatable("tooltip.rotarycraft.gravel_gun.damage",
                String.format(Locale.ROOT, "%.1f", damage / 2)) : Component.translatable("tooltip.rotarycraft.gravel_gun.uncharged"));
    }
}
