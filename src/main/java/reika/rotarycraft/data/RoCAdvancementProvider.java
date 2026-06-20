package reika.rotarycraft.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.ImpossibleTrigger;
import net.minecraft.advancements.triggers.InventoryChangeTrigger;
import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import reika.rotarycraft.registry.RotaryAdvancements;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;
import java.util.concurrent.CompletableFuture;
import java.util.function.Consumer;

/**
 * Generates {@code data/rotarycraft/advancement/<name>.json} for each {@link RotaryAdvancements}
 * entry. "Obtain X" advancements use an inventory_changed (has-item) criterion so they grant
 * naturally; "do X" advancements (the {@link #CODE_TRIGGERED} set) use an impossible criterion and
 * are granted from code via {@link RotaryAdvancements#triggerAchievement}. The single root is
 * MAKESTEEL; dependency-less entries hang off it so the whole set forms one tab.
 */
public class RoCAdvancementProvider extends AdvancementProvider {

    public RoCAdvancementProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, registries, List.of(new Generator()));
    }

    private static final class Generator implements AdvancementSubProvider {

        private static final Set<RotaryAdvancements> CODE_TRIGGERED = EnumSet.of(
                RotaryAdvancements.RECYCLE, RotaryAdvancements.JETENGINE, RotaryAdvancements.SUCKEDINTOJET,
                RotaryAdvancements.JETCHICKEN, RotaryAdvancements.JETFAIL, RotaryAdvancements.FLOODLIGHT,
                RotaryAdvancements.LANDMINE, RotaryAdvancements.OVERPRESSURE, RotaryAdvancements.STEAMENGINE);

        private static final Identifier ROOT_BACKGROUND =
                Identifier.fromNamespaceAndPath("minecraft", "textures/block/iron_block.png");

        private static final RotaryAdvancements ROOT = RotaryAdvancements.MAKESTEEL;

        @Override
        public void generate(HolderLookup.Provider registries, Consumer<AdvancementHolder> consumer) {
            Map<RotaryAdvancements, AdvancementHolder> built = new EnumMap<>(RotaryAdvancements.class);
            List<RotaryAdvancements> remaining = new ArrayList<>(List.of(RotaryAdvancements.list));

            // Build parents before children; dependency-less entries (except the root) hang off ROOT.
            while (!remaining.isEmpty()) {
                boolean progressed = false;
                Iterator<RotaryAdvancements> it = remaining.iterator();
                while (it.hasNext()) {
                    RotaryAdvancements a = it.next();
                    AdvancementHolder parent;
                    if (a == ROOT) {
                        parent = null;
                    } else if (a.dependency != null) {
                        if (!built.containsKey(a.dependency)) continue;
                        parent = built.get(a.dependency);
                    } else {
                        if (!built.containsKey(ROOT)) continue;
                        parent = built.get(ROOT);
                    }

                    String key = a.name().toLowerCase(Locale.ENGLISH);
                    // A few machine blocks resolve asItem() to AIR at datagen; fall back to a
                    // non-empty icon so the DisplayInfo / has-item criterion stays valid.
                    net.minecraft.world.item.Item icon = a.getIconItem();
                    boolean iconEmpty = icon == null || icon == net.minecraft.world.item.Items.AIR;
                    net.minecraft.world.level.ItemLike displayIcon = iconEmpty ? net.minecraft.world.item.Items.IRON_INGOT : icon;

                    Advancement.Builder b = Advancement.Builder.advancement();
                    if (parent != null)
                        b.parent(parent);
                    b.display(
                            displayIcon,
                            Component.translatable("advancements.rotarycraft." + key + ".title"),
                            Component.translatable("advancements.rotarycraft." + key + ".description"),
                            parent == null ? ROOT_BACKGROUND : null,
                            a.isSpecial ? AdvancementType.CHALLENGE : AdvancementType.TASK,
                            true, true, false);
                    // Code-triggered (or empty-icon) advancements use the impossible criterion;
                    // the rest grant by obtaining the icon item.
                    Criterion<?> crit = (CODE_TRIGGERED.contains(a) || iconEmpty)
                            ? new Criterion<>(CriteriaTriggers.IMPOSSIBLE, new ImpossibleTrigger.TriggerInstance())
                            : InventoryChangeTrigger.TriggerInstance.hasItems(displayIcon);
                    b.addCriterion("trigger", crit);
                    built.put(a, b.save(consumer, "rotarycraft:" + key));
                    it.remove();
                    progressed = true;
                }
                if (!progressed) break; // guard against a broken dependency cycle
            }
        }
    }
}
