package reika.rotarycraft.data;

import net.minecraft.advancements.Advancement;
import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementType;
import net.minecraft.advancements.triggers.CriteriaTriggers;
import net.minecraft.advancements.triggers.Criterion;
import net.minecraft.advancements.triggers.ImpossibleTrigger;
import net.minecraft.data.worldgen.BootstrapContext;
import net.minecraft.data.advancements.AdvancementProvider;
import net.minecraft.data.advancements.AdvancementSubProvider;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.ItemLike;
import reika.rotarycraft.registry.RotaryAdvancements;

import java.util.ArrayList;
import java.util.EnumMap;
import java.util.EnumSet;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import java.util.Set;

/**
 * Generates {@code data/rotarycraft/advancement/<name>.json} for each {@link RotaryAdvancements}
 * entry. "Obtain X" advancements use an inventory_changed (has-item) criterion so they grant
 * naturally; "do X" advancements (the gameplay-triggered set) use an impossible criterion and
 * are granted from code via {@link RotaryAdvancements#triggerAchievement}. The single root is
 * MAKESTEEL; dependency-less entries hang off it so the whole set forms one tab.
 */
public class RoCAdvancementProvider extends AdvancementProvider {

    public RoCAdvancementProvider() {
        super(List.of(Generator::new));
    }

    private static final class Generator extends AdvancementSubProvider {

        Generator(BootstrapContext<Advancement> output) {
            super(output);
        }

        // Only actual item milestones use inventory criteria. Actions keep their gameplay hooks;
        // possessing a handbook, engine, spawner or end-portal icon never proves the action happened.
        private static final Set<RotaryAdvancements> ITEM_MILESTONES = EnumSet.of(
                RotaryAdvancements.FAILSTEEL, RotaryAdvancements.WORKTABLE, RotaryAdvancements.PCB,
                RotaryAdvancements.MAKERAILGUN, RotaryAdvancements.STEELSHAFT, RotaryAdvancements.CVT,
                RotaryAdvancements.BEDROCKSHAFT, RotaryAdvancements.DIAMONDGEARS);

        private static final Identifier ROOT_BACKGROUND =
                Identifier.fromNamespaceAndPath("minecraft", "textures/block/iron_block.png");

        private static final RotaryAdvancements ROOT = RotaryAdvancements.MAKESTEEL;

        @Override
        public void generate() {
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
                    Item icon = a.getIconItem();
                    if (icon == null || icon == Items.AIR)
                        throw new IllegalStateException("Missing RotaryCraft advancement icon: " + a);
                    ItemLike displayIcon = icon;

                    Advancement.Builder b = Advancement.Builder.advancement();
                    if (parent != null)
                        b.parent(parent);
                    Component title = Component.translatable("advancements.rotarycraft." + key + ".title");
                    Component description = Component.translatable("advancements.rotarycraft." + key + ".description");
                    AdvancementType type = a.isSpecial ? AdvancementType.CHALLENGE : AdvancementType.TASK;
                    if (parent == null)
                        b.rootDisplay(displayIcon.asItem(), title, description, ROOT_BACKGROUND, type, true, true, false);
                    else
                        b.display(displayIcon.asItem(), title, description, type, true, true, false);
                    Criterion<?> crit = !ITEM_MILESTONES.contains(a)
                            ? new Criterion<>(CriteriaTriggers.IMPOSSIBLE, new ImpossibleTrigger.TriggerInstance())
                            : RoCAdvancementsEnabled.hasItem(displayIcon);
                    b.addCriterion("trigger", crit);
                    built.put(a, b.save(this.output, "rotarycraft:" + key));
                    it.remove();
                    progressed = true;
                }
                if (!progressed) throw new IllegalStateException("Cyclic RotaryCraft advancement parents: " + remaining);
            }
        }
    }
}
