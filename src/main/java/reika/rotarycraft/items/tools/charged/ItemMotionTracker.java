/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.items.tools.charged;

import java.util.List;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.UUID;
import java.util.function.Consumer;
import net.minecraft.ChatFormatting;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.monster.Monster;
import net.minecraft.world.entity.ambient.Bat;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.animal.squid.Squid;
import net.minecraft.world.entity.boss.enderdragon.EnderDragon;
import net.minecraft.world.entity.boss.wither.WitherBoss;
import net.minecraft.world.entity.monster.Enderman;
import net.minecraft.world.entity.monster.zombie.ZombifiedPiglin;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.rotarycraft.base.ItemChargedTool;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryItems;

/** V33a half-block ray samples, through-wall contacts, boss range and one charge per scan. */
public final class ItemMotionTracker extends ItemChargedTool {
    public ItemMotionTracker() {
        super(RotaryItems.itemProperties().component(DataComponents.MAX_DAMAGE, 32001).component(DataComponents.DAMAGE, 0));
    }
    @Override public InteractionResult use(Level world, Player player, InteractionHand hand) {
        ItemStack stack = player.getItemInHand(hand); int charge = stack.getDamageValue();
        if (world.isClientSide()) return charge > 0 ? InteractionResult.SUCCESS : InteractionResult.FAIL;
        if (ConfigRegistry.CLEARCHAT.getState() && player instanceof ServerPlayer server) ReikaChatHelper.clearChat(server);
        if (charge <= 0) {
            player.sendSystemMessage(Component.translatable("message.rotarycraft.tool_charge_depleted")); return InteractionResult.FAIL;
        }
        if (charge == 2 || charge == 4 || charge == 16 || charge == 32)
            player.sendSystemMessage(Component.translatable(charge == 2 ? "message.rotarycraft.motion_tracker.very_low" : "message.rotarycraft.motion_tracker.low", charge));
        for (Contact contact : scan(world, player)) {
            player.sendSystemMessage(Component.translatable("message.rotarycraft.motion_tracker.contact",
                    contact.name().copy().withStyle(contact.color()), String.format(java.util.Locale.ROOT, "%.2f", contact.distance() - 1)));
            if (contact.attacking()) player.sendSystemMessage(Component.translatable(contact.dragon()
                    ? "message.rotarycraft.motion_tracker.dragon_attacking" : "message.rotarycraft.motion_tracker.mob_attacking").withStyle(ChatFormatting.RED));
        }
        stack.setDamageValue(charge - 1);
        return InteractionResult.SUCCESS;
    }
    public List<Contact> scan(Level world, Player viewer) {
        Vec3 origin = viewer.getEyePosition(), look = viewer.getLookAngle();
        var contacts = new ArrayList<Contact>(); var seen = new HashSet<UUID>();
        for (double distance = 1; distance <= 128; distance += .5) {
            Vec3 sample = origin.add(look.scale(distance));
            for (LivingEntity entity : world.getEntitiesOfClass(LivingEntity.class, new AABB(sample, sample).inflate(.5))) {
                if (entity instanceof Player) continue;
                double actual = origin.distanceTo(entity.getEyePosition());
                boolean dragon = entity instanceof EnderDragon;
                if (actual > 32 && !dragon && !(entity instanceof WitherBoss) || !seen.add(entity.getUUID())) continue;
                contacts.add(new Contact(entity.getUUID(), entity.getDisplayName(), actual, getContactColor(entity), isAttacking(entity, viewer), dragon));
            }
        }
        return List.copyOf(contacts);
    }
    public static ChatFormatting getContactColor(LivingEntity entity) {
        if (entity instanceof EnderDragon) return ChatFormatting.DARK_PURPLE;
        if (entity instanceof WitherBoss) return ChatFormatting.DARK_GRAY;
        if (entity instanceof Enderman || entity instanceof ZombifiedPiglin) return ChatFormatting.YELLOW;
        if (ReikaEntityHelper.isHostile(entity)) return ChatFormatting.RED;
        if (entity instanceof Animal || entity instanceof Bat || entity instanceof Squid) return ChatFormatting.GREEN;
        return ChatFormatting.WHITE;
    }
    private static boolean isAttacking(LivingEntity entity, Player viewer) {
        if (entity instanceof EnderDragon dragon) {
            Vec3 target = dragon.getPhaseManager().getCurrentPhase().getFlyTargetLocation();
            return target != null && Math.abs(target.x - viewer.getX()) <= 4 && Math.abs(target.y - viewer.getY()) <= 4 && Math.abs(target.z - viewer.getZ()) <= 4;
        }
        return entity instanceof Monster mob && (mob.getTarget() == viewer || mob.getLastHurtByMob() == viewer);
    }
    @Override public boolean isBarVisible(ItemStack stack) { return false; }
    @Override public void appendHoverText(ItemStack stack, Item.TooltipContext context, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        super.appendHoverText(stack, context, display, lines, flag);
        lines.accept(Component.translatable("tooltip.rotarycraft.motion_tracker.charge", stack.getDamageValue()));
    }
    public record Contact(UUID entityId, Component name, double distance, ChatFormatting color, boolean attacking, boolean dragon) {}
}
