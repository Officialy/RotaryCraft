package reika.rotarycraft.auxiliary;

import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.entity.player.AttackEntityEvent;
import net.neoforged.neoforge.event.entity.player.PlayerInteractEvent;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import reika.rotarycraft.registry.RotaryAdvancements;

/** V33a bedrock tools awarded their milestone on use, never just on possession. */
@EventBusSubscriber(modid = "rotarycraft")
public final class RotaryAdvancementEvents {
    private RotaryAdvancementEvents() {}

    private static void toolUsed(Player player, ItemStack tool) {
        var id = BuiltInRegistries.ITEM.getKey(tool.getItem());
        if (id.getNamespace().equals("rotarycraft") && id.getPath().startsWith("bedrock_alloy_")) {
            String type = id.getPath().substring("bedrock_alloy_".length());
            if (java.util.Set.of("pickaxe", "axe", "shovel", "sword", "hoe", "shears", "sickle", "knife", "grafter", "saw").contains(type))
                RotaryAdvancements.BEDROCKTOOLS.triggerAchievement(player);
        }
    }

    @SubscribeEvent
    public static void usedOnBlock(PlayerInteractEvent.RightClickBlock event) {
        toolUsed(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void usedItem(PlayerInteractEvent.RightClickItem event) {
        toolUsed(event.getEntity(), event.getItemStack());
    }

    @SubscribeEvent
    public static void attacked(AttackEntityEvent event) {
        toolUsed(event.getEntity(), event.getEntity().getMainHandItem());
    }

    @SubscribeEvent
    public static void harvested(BlockDropsEvent event) {
        if (event.getBreaker() instanceof Player player) toolUsed(player, event.getTool());
    }
}
