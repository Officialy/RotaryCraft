/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.registry;

import net.minecraft.tags.TagKey;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.neoforged.neoforge.common.Tags;
import net.neoforged.neoforge.registries.DeferredItem;

/**
 * The extractor's four-stage ore chain (ore → dust → slurry → solution → flakes),
 * replacing the legacy metadata-based EXTRACTS item and ReikaOreHelper vanilla ore
 * list. COPPER is new relative to 1.7.10 — it didn't exist in vanilla then.
 */
public enum ExtractOres {

    COAL(Tags.Items.ORES_COAL, RotaryItems.COAL_DUST, RotaryItems.COAL_SLURRY, RotaryItems.COAL_SOLUTION, RotaryItems.COAL_FLAKES, false, Items.COAL, 1, 0.1F),
    IRON(Tags.Items.ORES_IRON, RotaryItems.IRON_DUST, RotaryItems.IRON_SLURRY, RotaryItems.IRON_SOLUTION, RotaryItems.IRON_FLAKES, false, Items.IRON_INGOT, 1, 0.7F),
    GOLD(Tags.Items.ORES_GOLD, RotaryItems.GOLD_DUST, RotaryItems.GOLD_SLURRY, RotaryItems.GOLD_SOLUTION, RotaryItems.GOLD_FLAKES, false, Items.GOLD_INGOT, 1, 1F),
    REDSTONE(Tags.Items.ORES_REDSTONE, RotaryItems.REDSTONE_DUST, RotaryItems.REDSTONE_SLURRY, RotaryItems.REDSTONE_SOLUTION, RotaryItems.REDSTONE_FLAKES, false, Items.REDSTONE, 4, 0.5F),
    LAPIS(Tags.Items.ORES_LAPIS, RotaryItems.LAPIS_DUST, RotaryItems.LAPIS_SLURRY, RotaryItems.LAPIS_SOLUTION, RotaryItems.LAPIS_FLAKES, false, Items.LAPIS_LAZULI, 6, 0.6F),
    DIAMOND(Tags.Items.ORES_DIAMOND, RotaryItems.DIAMOND_DUST, RotaryItems.DIAMOND_SLURRY, RotaryItems.DIAMOND_SOLUTION, RotaryItems.DIAMOND_FLAKES, true, Items.DIAMOND, 1, 1F),
    EMERALD(Tags.Items.ORES_EMERALD, RotaryItems.EMERALD_DUST, RotaryItems.EMERALD_SLURRY, RotaryItems.EMERALD_SOLUTION, RotaryItems.EMERALD_FLAKES, true, Items.EMERALD, 1, 1F),
    QUARTZ(Tags.Items.ORES_QUARTZ, RotaryItems.QUARTZ_DUST, RotaryItems.QUARTZ_SLURRY, RotaryItems.QUARTZ_SOLUTION, RotaryItems.QUARTZ_FLAKES, false, Items.QUARTZ, 1, 1F),
    COPPER(Tags.Items.ORES_COPPER, RotaryItems.COPPER_DUST, RotaryItems.COPPER_SLURRY, RotaryItems.COPPER_SOLUTION, RotaryItems.COPPER_FLAKES, false, Items.COPPER_INGOT, 1, 0.7F);

    public static final ExtractOres[] oreList = values();

    private final TagKey<Item> oreTag;
    private final DeferredItem<Item> dust;
    private final DeferredItem<Item> slurry;
    private final DeferredItem<Item> solution;
    private final DeferredItem<Item> flakes;
    private final boolean rare;
    private final Item smeltProduct;
    private final int smeltCount;
    private final float smeltXP;

    ExtractOres(TagKey<Item> oreTag, DeferredItem<Item> dust, DeferredItem<Item> slurry, DeferredItem<Item> solution, DeferredItem<Item> flakes,
                boolean rare, Item smeltProduct, int smeltCount, float smeltXP) {
        this.oreTag = oreTag;
        this.dust = dust;
        this.slurry = slurry;
        this.solution = solution;
        this.flakes = flakes;
        this.rare = rare;
        this.smeltProduct = smeltProduct;
        this.smeltCount = smeltCount;
        this.smeltXP = smeltXP;
    }

    public TagKey<Item> getOreTag() {
        return oreTag;
    }

    public Item getDust() {
        return dust.get();
    }

    public Item getSlurry() {
        return slurry.get();
    }

    public Item getSolution() {
        return solution.get();
    }

    public Item getFlakes() {
        return flakes.get();
    }

    /** Stage counts from zero: 0=dust, 1=slurry, 2=solution, 3=flakes. */
    public Item getStageItem(int stage) {
        return switch (stage) {
            case 0 -> this.getDust();
            case 1 -> this.getSlurry();
            case 2 -> this.getSolution();
            case 3 -> this.getFlakes();
            default -> throw new IllegalArgumentException("Invalid extractor stage " + stage);
        };
    }

    /** Rare ores (diamond, emerald) duplicate at the higher {@code oreCopyRare} rate. */
    public boolean isRare() {
        return rare;
    }

    public ItemStack getSmeltProduct() {
        return new ItemStack(smeltProduct, smeltCount);
    }

    public float getSmeltXP() {
        return smeltXP;
    }

    /** The ore whose chain the given item belongs to, with the stage it sits at, or null. */
    public static ExtractOres getByStageItem(ItemStack is) {
        for (ExtractOres ore : oreList) {
            for (int stage = 0; stage < 4; stage++) {
                if (is.is(ore.getStageItem(stage)))
                    return ore;
            }
        }
        return null;
    }

    public static ExtractOres getByOreBlock(ItemStack is) {
        for (ExtractOres ore : oreList) {
            if (is.is(ore.oreTag))
                return ore;
        }
        return null;
    }

    public static int getStage(ItemStack is) {
        for (ExtractOres ore : oreList) {
            for (int stage = 0; stage < 4; stage++) {
                if (is.is(ore.getStageItem(stage)))
                    return stage;
            }
        }
        return -1;
    }
}
