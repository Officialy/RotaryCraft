/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.bedrock;

import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.ItemLike;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.ShovelItem;
import net.minecraft.world.item.ToolMaterial;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.MapColor;
import net.neoforged.bus.api.SubscribeEvent;
import net.neoforged.fml.common.EventBusSubscriber;
import net.neoforged.neoforge.event.level.BlockDropsEvent;
import reika.dragonapi.instantiable.data.collections.ChancedOutputList;
import reika.dragonapi.instantiable.data.maps.BlockMap;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;
import java.util.List;

/**
 * 26.1 bedrock shovel.
 * <p>
 * Functional behaviour:
 * <ul>
 *   <li>Massive destroy-speed (24x) on grass / dirt / sand-mapped blocks.</li>
 *   <li>Extra random drops per source block — the same per-block chance table the 1.7.10
 *       original carried (grass→seeds/clay/mycelium, dirt→glowstone/diamond rolls, sand→gunpowder,
 *       clay→bone/soul-sand/gold-nugget, soul-sand→blaze powder/nether wart/quartz).
 *       Drops are added via a {@link BlockDropsEvent} listener — the 1.7.10 hook
 *       ({@code onBlockStartBreak} / {@code Item#breakBlock}) was removed in 26.1.</li>
 * </ul>
 */
@EventBusSubscriber(modid = RotaryCraft.MODID)
public class ItemBedrockShovel extends ShovelItem {

    /**
     * Drop registrations queued at class-init time. We can't build {@link ItemStack}s during the
     * static initializer in 26.1 — Item components aren't bound until later in mod loading and
     * {@code new ItemStack(item)} dereferences {@code Holder.Reference#components} which throws
     * "Components not bound yet". So we record the raw (source, extra, chance) tuples and
     * materialise the actual {@link ChancedOutputList} table lazily on first event dispatch.
     */
    private record PendingDrop(Block source, ItemLike extra, float chance) {}

    private static final List<PendingDrop> pendingDrops = new ArrayList<>();
    private static volatile BlockMap<ChancedOutputList> extraDrops; // built lazily

    static {
        queue(Blocks.GRASS_BLOCK, Items.WHEAT_SEEDS, 10);
        queue(Blocks.GRASS_BLOCK, Items.CLAY_BALL, 5);
        queue(Blocks.GRASS_BLOCK, Blocks.MYCELIUM, 0.5F);
        queue(Blocks.GRASS_BLOCK, Items.PUMPKIN_SEEDS, 5);
        queue(Blocks.GRASS_BLOCK, Items.MELON_SEEDS, 5);

        queue(Blocks.DIRT, Items.WHEAT_SEEDS, 10);
        queue(Blocks.DIRT, Items.GLOWSTONE_DUST, 2);
        queue(Blocks.DIRT, Items.NETHER_WART, 0.5F);
        queue(Blocks.DIRT, Items.EMERALD, 0.05F);
        queue(Blocks.DIRT, Items.DIAMOND, 0.05F);

        queue(Blocks.SAND, Items.GUNPOWDER, 2);

        queue(Blocks.CLAY, Items.BONE, 5);
        queue(Blocks.CLAY, Blocks.SOUL_SAND, 2);
        queue(Blocks.CLAY, Items.GOLD_NUGGET, 4);

        queue(Blocks.SOUL_SAND, Items.BLAZE_POWDER, 4);
        queue(Blocks.SOUL_SAND, Items.NETHER_WART, 5);
        queue(Blocks.SOUL_SAND, Items.QUARTZ, 2);
    }

    public ItemBedrockShovel() {
        super(ToolMaterial.NETHERITE, 4F, -2.8F, RotaryItems.itemProperties().stacksTo(1));
    }

    private static void queue(Block source, ItemLike extra, float chance) {
        pendingDrops.add(new PendingDrop(source, extra, chance));
    }

    /**
     * Public addDrop hook for external callers — same legacy signature, except it queues into
     * the same lazy buffer if the component map isn't built yet, otherwise inserts directly.
     */
    public static void addDrop(Block source, ItemStack extra, float chance) {
        BlockMap<ChancedOutputList> table = extraDrops;
        if (table == null) {
            // Still in component-not-bound window; keep deferring. ItemLike is captured raw and
            // a single-stack list is created lazily.
            pendingDrops.add(new PendingDrop(source, extra.getItem(), chance));
            return;
        }
        appendDrop(table, source, extra, chance);
    }

    private static void appendDrop(BlockMap<ChancedOutputList> table, Block source, ItemStack extra, float chance) {
        ChancedOutputList co = table.get(source);
        if (co == null) {
            co = new ChancedOutputList(false);
            table.put(source, co);
        }
        co.addItem(extra, chance);
    }

    /** Materialises the {@link ChancedOutputList} table the first time it's needed. */
    private static BlockMap<ChancedOutputList> ensureTable() {
        BlockMap<ChancedOutputList> table = extraDrops;
        if (table != null) return table;
        synchronized (pendingDrops) {
            table = extraDrops;
            if (table != null) return table;
            table = new BlockMap<>();
            for (PendingDrop pd : pendingDrops) {
                appendDrop(table, pd.source(), new ItemStack(pd.extra()), pd.chance());
            }
            extraDrops = table;
            return table;
        }
    }

    @SubscribeEvent
    public static void onBlockDrops(BlockDropsEvent event) {
        ItemStack tool = event.getTool();
        if (!(tool.getItem() instanceof ItemBedrockShovel)) return;
        ChancedOutputList table = ensureTable().get(event.getState().getBlock());
        if (table == null) return;
        var bonus = table.calculate();
        if (bonus.isEmpty()) return;
        var level = event.getLevel();
        var pos = event.getPos();
        for (ItemStack drop : bonus) {
            event.getDrops().add(new ItemEntity(level, pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, drop));
        }
    }

    @Override
    public float getDestroySpeed(ItemStack i, BlockState b) {
        if (b == null) return 0;
        var color = b.getBlock().defaultMapColor();
        if (color == MapColor.GRASS || color == MapColor.DIRT || color == MapColor.SAND) {
            return 24F;
        }
        return 1F;
    }

    public boolean isAcceleratedOn(BlockState b) {
        var color = b.getBlock().defaultMapColor();
        return color == MapColor.GRASS || color == MapColor.DIRT || color == MapColor.SAND;
    }
}
