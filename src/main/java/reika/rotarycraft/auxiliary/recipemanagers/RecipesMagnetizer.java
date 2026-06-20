/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary.recipemanagers;

import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.data.maps.ItemHashMap;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Collection;

/**
 * Holds the recipes for the Magnetizer machine.
 * Each recipe describes how an input item becomes magnetized/charged over time.
 */
public final class RecipesMagnetizer {

    private static final RecipesMagnetizer INSTANCE = new RecipesMagnetizer();

    private final ItemHashMap<MagnetizerRecipe> recipes = new ItemHashMap<>();

    private RecipesMagnetizer() {
        // shaft core: no min speed, 2 µT per speed unit, timeFactor 2, no stacking
        this.addRecipe(RotaryItems.HSLA_SHAFT_CORE.get().getDefaultInstance(),     0,  2, 2, false);
        // tungsten shaft core: same speed profile but slower timeFactor
        this.addRecipe(RotaryItems.TUNGSTEN_ALLOY_SHAFT_CORE.get().getDefaultInstance(), 0, 2, 1, false);
    }

    public static RecipesMagnetizer getRecipes() {
        return INSTANCE;
    }

    private void addRecipe(ItemStack in, int minSpeed, int reqSpeedPerMicroTesla, int timeFactor, boolean allowStacking) {
        recipes.put(in, new MagnetizerRecipe(in, timeFactor, minSpeed, reqSpeedPerMicroTesla, allowStacking));
    }

    public void addAPIRecipe(ItemStack in, int minSpeed, int reqSpeedPerMicroTesla, int timeFactor, boolean allowStacking) {
        recipes.put(in, new MagnetizerRecipe(in, timeFactor, minSpeed, reqSpeedPerMicroTesla, allowStacking));
    }

    public MagnetizerRecipe getRecipe(ItemStack is) {
        return recipes.get(is);
    }

    public Collection<MagnetizerRecipe> getAllRecipes() {
        return recipes.values();
    }

    // -------------------------------------------------------------------------

    public static final class MagnetizerRecipe {
        public final ItemStack input;
        public final int timeFactor;
        public final int minSpeed;
        public final int speedPerMicroTesla;
        public final boolean allowStacking;

        MagnetizerRecipe(ItemStack input, int timeFactor, int minSpeed, int speedPerMicroTesla, boolean allowStacking) {
            this.input           = input;
            this.timeFactor      = timeFactor;
            this.minSpeed        = minSpeed;
            this.speedPerMicroTesla = speedPerMicroTesla;
            this.allowStacking   = allowStacking;
        }

        public int getMaxCharge(int omega) {
            return speedPerMicroTesla > 0 ? omega / speedPerMicroTesla : 0;
        }
    }
}
