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

import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import reika.dragonapi.DragonAPI;

import java.util.function.Supplier;

/**
 * Bonus items produced alongside the final extractor stage (solution → flakes).
 * Probabilities are the legacy values. Entries whose outputs were items from
 * un-ported mods (QUARTZ→certus quartz, EMERALD→ruby, and the IC2/ReactorCraft
 * gated ones) return when those interfaces are.
 */
public enum ExtractorBonus {

    GOLD(ExtractOres.GOLD, () -> new ItemStack(RotaryItems.SILVER_FLAKES.get()), 0.125F),
    IRON(ExtractOres.IRON, () -> new ItemStack(RotaryItems.TUNGSTEN_FLAKES.get()), 0.025F),
    COAL(ExtractOres.COAL, () -> new ItemStack(Items.GUNPOWDER), 0.0625F),
    COPPER(ExtractOres.COPPER, () -> new ItemStack(RotaryItems.GOLD_FLAKES.get()), 0.25F),
    LAPIS(ExtractOres.LAPIS, () -> new ItemStack(RotaryItems.ALUMINUM_ALLOY_POWDER.get()), 0.125F),
    REDSTONE(ExtractOres.REDSTONE, () -> new ItemStack(RotaryItems.ALUMINUM_ALLOY_POWDER.get()), 0.25F);

    private static final ExtractorBonus[] bonusList = values();

    private final ExtractOres ore;
    private final Supplier<ItemStack> bonusItem;
    private final float probability;

    ExtractorBonus(ExtractOres ore, Supplier<ItemStack> bonus, float chance) {
        this.ore = ore;
        bonusItem = bonus;
        probability = chance;
    }

    public ItemStack getBonusItem() {
        return bonusItem.get();
    }

    public boolean doBonus() {
        return DragonAPI.rand.nextFloat() < probability;
    }

    /** Looks up the bonus for a solution-stage item (the input of the final stage). */
    public static ExtractorBonus getBonusForIngredient(ItemStack is) {
        for (ExtractorBonus b : bonusList) {
            if (is.is(b.ore.getSolution()))
                return b;
        }
        return null;
    }
}
