package reika.rotarycraft.data;

import java.util.concurrent.CompletableFuture;

import net.minecraft.core.HolderLookup;
import net.minecraft.data.PackOutput;
import net.minecraft.tags.BlockTags;
import net.neoforged.neoforge.common.data.BlockTagsProvider;

import reika.dragonapi.libraries.level.LegacyMotionTags;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * RotaryCraft's block tags. 26.3 made movement blocking, heightmaps, suffocation, fluid blocking and
 * fluid washing tag-driven, and NeoForge tags no modded blocks, so without these every machine would be
 * passable and never washed away. {@link LegacyMotionTags} gives each block its 26.2 behaviour.
 */
public class RoCBlockTagsProvider extends BlockTagsProvider {

    public RoCBlockTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, RotaryCraft.MODID);
    }

    @Override
    protected void addTags(HolderLookup.Provider provider) {
        var motionTag = tag(BlockTags.BLOCKS_MOTION_NO_LEAVES);
        var leafTag = tag(BlockTags.LEAVES);
        var washedTag = tag(BlockTags.WASHED_AWAY_BY_FLUIDS);
        LegacyMotionTags.classifyEntries(RotaryBlocks.BLOCKS.getEntries(), motionTag::add, leafTag::add, washedTag::add);
        // Machines used iron material in V33a. The tool component now reads mining tags;
        // without this even a diamond pickaxe mines them at hand speed.
        for (var entry : RotaryBlocks.BLOCKS.getEntries()) {
            var block = entry.get();
            if (block instanceof reika.rotarycraft.base.blocks.BlockBasicMachine
                    || block instanceof reika.rotarycraft.base.blocks.entity.pipe.BlockPipeShell
                    || block instanceof reika.rotarycraft.base.blocks.entity.BlockPiping)
                tag(BlockTags.MINEABLE_WITH_PICKAXE).add(entry.getKey());
        }
        tag(BlockTags.MINEABLE_WITH_PICKAXE).add(RotaryBlocks.HSLA_STEEL_BLOCK.getKey(),
                RotaryBlocks.BLASTGLASS.getKey(), RotaryBlocks.BLASTPANE.getKey(), RotaryBlocks.DECOTANK.getKey(),
                RotaryBlocks.ANTHRA.getKey(), RotaryBlocks.LONS.getKey(), RotaryBlocks.SHIELD.getKey(), RotaryBlocks.COKE.getKey());
        tag(common("storage_blocks/hsla")).add(RotaryBlocks.HSLA_STEEL_BLOCK.getKey());
        tag(common("glass_blocks/hardened")).add(RotaryBlocks.BLASTGLASS.getKey());
        tag(common("glass_blocks")).addTag(common("glass_blocks/hardened"));
        tag(common("glass_panes/hardened")).add(RotaryBlocks.BLASTPANE.getKey());
        tag(common("glass_panes")).addTag(common("glass_panes/hardened"));
    }

    private static net.minecraft.tags.TagKey<net.minecraft.world.level.block.Block> common(String path) {
        return net.minecraft.tags.TagKey.create(net.minecraft.core.registries.Registries.BLOCK,
                net.minecraft.resources.Identifier.fromNamespaceAndPath("c", path));
    }
}
