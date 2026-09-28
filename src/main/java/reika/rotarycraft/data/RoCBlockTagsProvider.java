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
    }
}
