package reika.rotarycraft.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import reika.rotarycraft.RotaryCraft;

/** Empty common steel tag permits the integration recipe to load without a steel-supplying mod. */
public class RoCItemTagsProvider extends ItemTagsProvider {
    public static final TagKey<Item> STEEL_INGOTS = TagKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath("c", "ingots/steel"));

    public RoCItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, RotaryCraft.MODID);
    }
    @Override protected void addTags(HolderLookup.Provider lookup) { tag(STEEL_INGOTS); }
}
