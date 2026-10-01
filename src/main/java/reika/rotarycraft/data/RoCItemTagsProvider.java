package reika.rotarycraft.data;

import java.util.concurrent.CompletableFuture;
import net.minecraft.core.HolderLookup;
import net.minecraft.core.registries.Registries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.tags.TagKey;
import net.minecraft.tags.ItemTags;
import net.minecraft.world.item.Item;
import net.neoforged.neoforge.common.data.ItemTagsProvider;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.registry.RotaryBlocks;

/** Vanilla equipment categories and the original material integration entries. */
public class RoCItemTagsProvider extends ItemTagsProvider {
    public static final TagKey<Item> STEEL_INGOTS = TagKey.create(Registries.ITEM,
            Identifier.fromNamespaceAndPath("c", "ingots/steel"));

    public RoCItemTagsProvider(PackOutput output, CompletableFuture<HolderLookup.Provider> lookup) {
        super(output, lookup, RotaryCraft.MODID);
    }
    @Override protected void addTags(HolderLookup.Provider lookup) {
        // HSLA is deliberately not interchangeable with ordinary steel (HSLADICT defaults off).
        // A datapack may opt in; keeping this tag empty also lets the purifier load without other mods.
        tag(STEEL_INGOTS);
        add(ItemTags.PICKAXES, RotaryItems.HSLA_STEEL_PICKAXE.get(), RotaryItems.BEDROCK_ALLOY_PICK.get());
        add(ItemTags.AXES, RotaryItems.HSLA_STEEL_AXE.get(), RotaryItems.BEDROCK_ALLOY_AXE.get());
        add(ItemTags.SHOVELS, RotaryItems.HSLA_STEEL_SHOVEL.get(), RotaryItems.BEDROCK_ALLOY_SHOVEL.get());
        add(ItemTags.HOES, RotaryItems.HSLA_STEEL_HOE.get(), RotaryItems.BEDROCK_ALLOY_HOE.get());
        add(ItemTags.SWORDS, RotaryItems.HSLA_STEEL_SWORD.get(), RotaryItems.BEDROCK_ALLOY_SWORD.get());
        add(common("tools/shear"), RotaryItems.HSLA_STEEL_SHEARS.get(), RotaryItems.BEDROCK_ALLOY_SHEARS.get());
        tag(common("tools")).addTag(common("tools/shear"));
        // Vanilla's equipment categories feed the enchantable tags. Shears are explicit vanilla entries.
        add(ItemTags.MINING_ENCHANTABLE, RotaryItems.HSLA_STEEL_SHEARS.get(), RotaryItems.BEDROCK_ALLOY_SHEARS.get());
        add(ItemTags.DURABILITY_ENCHANTABLE, RotaryItems.HSLA_STEEL_SHEARS.get(), RotaryItems.BEDROCK_ALLOY_SHEARS.get());
        add(ItemTags.HEAD_ARMOR, RotaryItems.HSLA_HELMET.get(), RotaryItems.BEDROCK_ALLOY_HELMET.get(),
                RotaryItems.NVG.get(), RotaryItems.IO_GOGGLES.get());
        add(ItemTags.CHEST_ARMOR, RotaryItems.HSLA_CHESTPLATE.get(), RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get(),
                RotaryItems.JETPACK.get(), RotaryItems.HSLA_STEEL_PACK.get(), RotaryItems.BEDROCK_ALLOY_PACK.get());
        add(ItemTags.LEG_ARMOR, RotaryItems.HSLA_LEGGINGS.get(), RotaryItems.BEDROCK_ALLOY_LEGGINGS.get());
        add(ItemTags.FOOT_ARMOR, RotaryItems.HSLA_BOOTS.get(), RotaryItems.BEDROCK_ALLOY_BOOTS.get(),
                RotaryItems.JUMP.get(), RotaryItems.BEDROCK_ALLOY_JUMP_BOOTS.get());
        // V33a deliberately published its aluminum alloy compact as ingotAluminum.
        add(common("ingots/aluminum"), RotaryItems.ALUMINUM_ALLOY_INGOT.get());
        add(common("ingots/silver"), RotaryItems.SILVER_INGOT.get());
        add(common("ingots/tungsten"), RotaryItems.TUNGSTEN_INGOT.get());
        tag(common("ingots")).addTag(common("ingots/aluminum")).addTag(common("ingots/silver")).addTag(common("ingots/tungsten"));
        add(common("ingots/hsla"), RotaryItems.HSLA_STEEL_INGOT.get());
        add(own("ingots/bedrock_alloy"), RotaryItems.BEDROCK_ALLOY_INGOT.get());
        add(own("ingots/tungsten_alloy"), RotaryItems.TUNGSTEN_ALLOY_INGOT.get());
        add(own("dusts/bedrock"), RotaryItems.BEDROCK_DUST.get());
        material("dusts/aluminum", RotaryItems.ALUMINUM_ALLOY_POWDER.get());
        material("dusts/netherrack", RotaryItems.NETHERRACK_DUST.get());
        material("dusts/soul_sand", RotaryItems.TAR.get());
        material("dusts/coal", RotaryItems.COAL_DUST.get());
        material("dusts/salt", RotaryItems.SALT.get());
        material("dusts/wood", RotaryItems.SAWDUST.get());
        tag(common("dusts")).addTag(common("dusts/aluminum")).addTag(common("dusts/netherrack")).addTag(common("dusts/soul_sand"))
                .addTag(common("dusts/coal")).addTag(common("dusts/salt")).addTag(common("dusts/wood"));
        material("silicon", RotaryItems.SILICON.get());
        material("fertilizers", RotaryItems.COMPOST.get());
        material("coke", RotaryItems.COKE.get());
        material("foods/salt", RotaryItems.SALT.get());
        material("seeds/canola", RotaryItems.CANOLA_SEEDS.get());
        tag(common("seeds")).addTag(common("seeds/canola"));
        material("storage_blocks/hsla", RotaryBlocks.HSLA_STEEL_BLOCK.get().asItem());
        material("glass_blocks/hardened", RotaryBlocks.BLASTGLASS.get().asItem());
        tag(common("glass_blocks")).addTag(common("glass_blocks/hardened"));
        material("glass_panes/hardened", RotaryBlocks.BLASTPANE.get().asItem());
        tag(common("glass_panes")).addTag(common("glass_panes/hardened"));
    }
    private void add(TagKey<Item> key, Item... items) {
        for (Item item : items) tag(key).add(net.minecraft.core.registries.BuiltInRegistries.ITEM.getResourceKey(item).orElseThrow());
    }
    private void material(String path, Item item) { add(common(path), item); }
    public static TagKey<Item> common(String path) { return key("c", path); }
    private static TagKey<Item> own(String path) { return key(RotaryCraft.MODID, path); }
    private static TagKey<Item> key(String namespace, String path) {
        return TagKey.create(Registries.ITEM, Identifier.fromNamespaceAndPath(namespace, path));
    }
}
