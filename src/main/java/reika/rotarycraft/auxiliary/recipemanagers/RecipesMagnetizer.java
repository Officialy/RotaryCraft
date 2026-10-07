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

    private RecipesMagnetizer() {}

    public static RecipesMagnetizer getRecipes() {
        return INSTANCE;
    }

    public void addAPIRecipe(ItemStack in, int minSpeed, int reqSpeedPerMicroTesla, int timeFactor, boolean allowStacking) {
        recipes.put(in, new MagnetizerRecipe(in, timeFactor, minSpeed, reqSpeedPerMicroTesla, allowStacking));
    }

    public MagnetizerRecipe getRecipe(net.minecraft.world.level.Level level, ItemStack stack) {
        MagnetizerRecipe extension = recipes.get(stack);
        if (extension != null) return extension;
        var map = reika.rotarycraft.modinterface.jei.RotaryRecipeSync.getRecipes(level);
        if (map == null) return null;
        var input = new net.minecraft.world.item.crafting.SingleRecipeInput(stack);
        return map.byType(reika.rotarycraft.registry.RotaryRecipeTypes.MAGNETIZER.get()).stream()
                .map(net.minecraft.world.item.crafting.RecipeHolder::value).filter(r -> r.matches(input, level)).findFirst().orElse(null);
    }

    /** Compatibility lookup uses the current logical-side level; new callers should pass their level. */
    public MagnetizerRecipe getRecipe(ItemStack stack) {
        return getRecipe(currentLevel(), stack);
    }

    private static net.minecraft.world.level.Level currentLevel() {
        var server = net.neoforged.neoforge.server.ServerLifecycleHooks.getCurrentServer();
        return server != null && server.isSameThread() ? server.overworld()
                : net.neoforged.fml.loading.FMLEnvironment.getDist().isClient() ? reika.dragonapi.client.ClientEnvironment.level() : null;
    }

    public Collection<MagnetizerRecipe> getAllRecipes(net.minecraft.world.level.Level level) {
        var all = new java.util.ArrayList<MagnetizerRecipe>();
        var map = reika.rotarycraft.modinterface.jei.RotaryRecipeSync.getRecipes(level);
        if (map != null) map.byType(reika.rotarycraft.registry.RotaryRecipeTypes.MAGNETIZER.get()).forEach(h -> all.add(h.value()));
        all.addAll(recipes.values());
        return java.util.List.copyOf(all);
    }

    public Collection<MagnetizerRecipe> getAllRecipes() { return getAllRecipes(currentLevel()); }

    public Collection<MagnetizerRecipe> getAPIRecipes() { return java.util.List.copyOf(recipes.values()); }

    // -------------------------------------------------------------------------

    public static final class MagnetizerRecipe implements net.minecraft.world.item.crafting.Recipe<net.minecraft.world.item.crafting.SingleRecipeInput> {
        public final net.minecraft.world.item.crafting.Ingredient input;
        public final int timeFactor;
        public final int minSpeed;
        public final int speedPerMicroTesla;
        public final boolean allowStacking;

        public MagnetizerRecipe(ItemStack input, int timeFactor, int minSpeed, int speedPerMicroTesla, boolean allowStacking) {
            this(net.minecraft.world.item.crafting.Ingredient.of(input.getItem()), timeFactor, minSpeed, speedPerMicroTesla, allowStacking);
        }

        public MagnetizerRecipe(net.minecraft.world.item.crafting.Ingredient input, int timeFactor, int minSpeed, int speedPerMicroTesla, boolean allowStacking) {
            if (timeFactor <= 0 || minSpeed < 0 || speedPerMicroTesla <= 0) throw new IllegalArgumentException("Invalid magnetizer speed/time factors");
            this.input           = input;
            this.timeFactor      = timeFactor;
            this.minSpeed        = minSpeed;
            this.speedPerMicroTesla = speedPerMicroTesla;
            this.allowStacking   = allowStacking;
        }

        @Override public boolean matches(net.minecraft.world.item.crafting.SingleRecipeInput input, net.minecraft.world.level.Level level) { return this.input.test(input.item()); }
        @Override public ItemStack assemble(net.minecraft.world.item.crafting.SingleRecipeInput input) { return input.item().copy(); }
        @Override public boolean showNotification() { return false; }
        @Override public String group() { return ""; }
        @Override public net.minecraft.world.item.crafting.PlacementInfo placementInfo() { return net.minecraft.world.item.crafting.PlacementInfo.create(input); }
        @Override public net.minecraft.world.item.crafting.RecipeBookCategory recipeBookCategory() { return net.minecraft.world.item.crafting.RecipeBookCategories.FURNACE_MISC; }
        @Override public net.minecraft.world.item.crafting.RecipeSerializer<MagnetizerRecipe> getSerializer() { return reika.rotarycraft.registry.RotaryRecipeSerializers.MAGNETIZER.get(); }
        @Override public net.minecraft.world.item.crafting.RecipeType<MagnetizerRecipe> getType() { return reika.rotarycraft.registry.RotaryRecipeTypes.MAGNETIZER.get(); }

        public static final com.mojang.serialization.MapCodec<MagnetizerRecipe> CODEC = com.mojang.serialization.codecs.RecordCodecBuilder.mapCodec(i -> i.group(
                net.minecraft.world.item.crafting.Ingredient.CODEC.fieldOf("input").forGetter(r -> r.input),
                com.mojang.serialization.Codec.intRange(1, Integer.MAX_VALUE).fieldOf("time_factor").forGetter(r -> r.timeFactor),
                com.mojang.serialization.Codec.intRange(0, Integer.MAX_VALUE).optionalFieldOf("min_speed", 0).forGetter(r -> r.minSpeed),
                com.mojang.serialization.Codec.intRange(1, Integer.MAX_VALUE).fieldOf("speed_per_microtesla").forGetter(r -> r.speedPerMicroTesla),
                com.mojang.serialization.Codec.BOOL.optionalFieldOf("allow_stacking", false).forGetter(r -> r.allowStacking)
        ).apply(i, MagnetizerRecipe::new));

        public static final net.minecraft.network.codec.StreamCodec<net.minecraft.network.RegistryFriendlyByteBuf, MagnetizerRecipe> STREAM_CODEC = net.minecraft.network.codec.StreamCodec.of(
                (buf, recipe) -> {
                    net.minecraft.world.item.crafting.Ingredient.CONTENTS_STREAM_CODEC.encode(buf, recipe.input);
                    buf.writeVarInt(recipe.timeFactor); buf.writeVarInt(recipe.minSpeed); buf.writeVarInt(recipe.speedPerMicroTesla); buf.writeBoolean(recipe.allowStacking);
                }, buf -> new MagnetizerRecipe(net.minecraft.world.item.crafting.Ingredient.CONTENTS_STREAM_CODEC.decode(buf), buf.readVarInt(), buf.readVarInt(), buf.readVarInt(), buf.readBoolean()));

        public int getMaxCharge(int omega) {
            return speedPerMicroTesla > 0 ? omega / speedPerMicroTesla : 0;
        }
    }
}
