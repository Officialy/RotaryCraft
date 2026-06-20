package reika.rotarycraft.data;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.data.loot.BlockLootSubProvider;
import net.minecraft.data.loot.LootTableProvider;
import net.minecraft.world.flag.FeatureFlags;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.storage.loot.LootTable;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.predicates.LootItemBlockStatePropertyCondition;
import net.minecraft.world.level.storage.loot.predicates.LootItemCondition;
import net.minecraft.advancements.predicates.StatePropertiesPredicate;
import reika.rotarycraft.base.blocks.CanolaBlock;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CompletableFuture;

/**
 * 26.1 block loot-table data provider for RotaryCraft.
 * <p>
 * Every block registered through {@link RotaryBlocks#BLOCKS} claims a loot table by default
 * (the block properties never call {@code .noLootTable()}), so the vanilla generator throws
 * "Missing loottable" unless a map entry exists for each known block. This provider iterates
 * the RotaryCraft block registry and produces an entry for every block:
 * <ul>
 *   <li>Blocks that have an associated {@code BlockItem} (registered via the machine / item
 *       helpers) simply {@link #dropSelf}, matching the legacy {@code MachineRegistry} behaviour
 *       where breaking a machine dropped its own item.</li>
 *   <li>{@link RotaryBlocks#CANOLA} uses {@link #createCropDrops}; faithful to legacy
 *       {@code BlockCanola}, which dropped only canola seeds (more when fully grown).</li>
 *   <li>Item-less blocks ({@code BEDROCK}, {@code BEDROCKSLICE}, the fluid blocks registered
 *       through {@code registerBlockOnly}) drop nothing — they have no item form.</li>
 * </ul>
 */
public final class RoCLootProvider extends LootTableProvider {

    public RoCLootProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> registries) {
        super(output, Set.of(), List.of(
                new SubProviderEntry(Blocks::new, LootContextParamSets.BLOCK)
        ), registries);
    }

    private static final class Blocks extends BlockLootSubProvider {

        Blocks(HolderLookup.Provider registries) {
            super(Set.of(), FeatureFlags.REGISTRY.allFlags(), registries);
        }

        @Override
        protected void generate() {
            for (var holder : RotaryBlocks.BLOCKS.getEntries()) {
                Block block = holder.get();
                if (block instanceof CanolaBlock) {
                    // Legacy BlockCanola dropped only canola seeds; mature plants drop several.
                    LootItemCondition.Builder isMaxAge = LootItemBlockStatePropertyCondition
                            .hasBlockStateProperties(block)
                            .setProperties(StatePropertiesPredicate.Builder.properties()
                                    .hasProperty(CanolaBlock.AGE, CanolaBlock.MAX_AGE));
                    this.add(block, this.createCropDrops(block,
                            RotaryItems.CANOLA_SEEDS.get(),
                            RotaryItems.CANOLA_SEEDS.get(),
                            isMaxAge));
                } else if (block.asItem() == Items.AIR) {
                    // Item-less blocks (bedrock, bedrock slice, fluid blocks) drop nothing.
                    this.add(block, noDrop());
                } else {
                    this.dropSelf(block);
                }
            }
        }

        @Override
        protected Iterable<Block> getKnownBlocks() {
            List<Block> blocks = new ArrayList<>();
            for (var holder : RotaryBlocks.BLOCKS.getEntries()) {
                blocks.add(holder.get());
            }
            return blocks;
        }
    }
}
