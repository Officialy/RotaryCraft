package reika.rotarycraft.data;

import java.util.List;
import java.util.Optional;
import net.minecraft.world.item.*;
import reika.rotarycraft.auxiliary.recipemanagers.ExtractorBonusOutput;
import reika.rotarycraft.registry.*;

/** V33a secondary-product table, used only to emit reloadable recipe data. */
final class ModExtractorRecipeData {
    private ModExtractorRecipeData() {}
    static List<ExtractorBonusOutput> bonuses(String ore) {
        return switch (ore) {
            case "GOLD", "NETHERGOLD" -> List.of(bonus(RotaryItems.SILVER_FLAKES.get(), .125));
            case "IRON" -> List.of(bonus(RotaryItems.TUNGSTEN_FLAKES.get(), .025));
            case "NETHERIRON" -> List.of(bonus(RotaryItems.TUNGSTEN_FLAKES.get(), .05));
            case "COAL" -> List.of(gated(ModExtractOres.PITCHBLENDE, .0625, ModExtractOres.PITCHBLENDE),
                    gated(ModExtractOres.URANIUM, .0625, ModExtractOres.URANIUM), bonus(Items.GUNPOWDER, .0625));
            case "COPPER" -> List.of(bonus(RotaryItems.GOLD_FLAKES.get(), .25));
            case "LEAD" -> List.of(bonus(ModExtractOres.NICKEL.stage(3), .25));
            case "SILVER" -> List.of(gated(ModExtractOres.IRIDIUM, .01, ModExtractOres.IRIDIUM));
            case "PLATINUM" -> List.of(gated(ModExtractOres.IRIDIUM, .0625, ModExtractOres.IRIDIUM));
            case "NETHERPLATINUM" -> List.of(gated(ModExtractOres.IRIDIUM, .125, ModExtractOres.IRIDIUM));
            case "NICKEL", "NETHERNICKEL", "IRIDIUM" -> List.of(bonus(ModExtractOres.PLATINUM.stage(3), .5));
            case "SODALITE" -> List.of(bonus(ModExtractOres.ALUMINUM.stage(3), 1));
            case "PYRITE" -> List.of(bonus(ModExtractOres.SULFUR.stage(3), .4));
            case "BAUXITE" -> List.of(bonus(ModExtractOres.ALUMINUM.stage(3), .25));
            case "TUNGSTEN" -> List.of(bonus(RotaryItems.IRON_FLAKES.get(), .75));
            case "OSMIUM" -> List.of(bonus(RotaryItems.IRON_FLAKES.get(), .125));
            case "LAPIS" -> List.of(bonus(RotaryItems.ALUMINUM_ALLOY_POWDER.get(), .125));
            case "RUBY", "SAPPHIRE" -> List.of(bonus(ModExtractOres.ALUMINUM.stage(3), .0625));
            case "QUARTZ" -> List.of(bonus(ModExtractOres.CERTUSQUARTZ.stage(3), .0625));
            case "CERTUSQUARTZ" -> List.of(bonus(RotaryItems.QUARTZ_FLAKES.get(), .5));
            case "COBALT" -> List.of(bonus(ModExtractOres.NICKEL.stage(3), .125));
            case "REDSTONE" -> List.of(bonus(RotaryItems.ALUMINUM_ALLOY_POWDER.get(), .25));
            case "MAGNETITE" -> List.of(bonus(RotaryItems.IRON_FLAKES.get(), .2));
            case "MONAZIT" -> List.of(gated(ModExtractOres.THORIUM, .15, ModExtractOres.THORIUM));
            case "EMERALD" -> List.of(bonus(ModExtractOres.RUBY.stage(3), .1));
            default -> List.of();
        };
    }
    private static ExtractorBonusOutput bonus(Item item, double declaredChance) {
        // Original doBonus() used rand.nextInt((int)(1F / probability)) == 0.
        double actualChance = 1D / (int)(1F / (float)declaredChance);
        return new ExtractorBonusOutput(new ItemStackTemplate(item), actualChance, Optional.empty());
    }
    private static ExtractorBonusOutput gated(ModExtractOres output, double chance, ModExtractOres required) {
        var bonus = bonus(output.stage(3), chance);
        return new ExtractorBonusOutput(bonus.output(), bonus.chance(), Optional.of(required.inputTag()));
    }
}
