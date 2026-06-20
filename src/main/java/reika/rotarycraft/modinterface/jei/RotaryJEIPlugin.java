/*******************************************************************************
 * @author Reika Kalseki / 26.1 port by OfficialyMax
 *
 * JEI (Just Enough Items) integration for RotaryCraft. Provides recipe categories
 * for the Blast Furnace (shaped + shapeless) and Pulse Jet Furnace so that players
 * can look up what those machines can produce.
 ******************************************************************************/
package reika.rotarycraft.modinterface.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import mezz.jei.api.registration.IRecipeCategoryRegistration;
import mezz.jei.api.registration.IRecipeRegistration;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.client.Minecraft;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;

import java.util.List;
import java.util.stream.Collectors;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.PulseFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryRecipeTypes;

@JeiPlugin
public class RotaryJEIPlugin implements IModPlugin {

    public static final Identifier UID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "jei_plugin");

    @Override
    public Identifier getPluginUid() { return UID; }

    // -------------------------------------------------------------------------
    @Override
    public void registerCategories(IRecipeCategoryRegistration registration) {
        IGuiHelper gui = registration.getJeiHelpers().getGuiHelper();
        registration.addRecipeCategories(
                new BlastFurnaceShapedCategory(gui),
                new BlastFurnaceShapelessCategory(gui),
                new PulseFurnaceCategory(gui)
        );
    }

    // -------------------------------------------------------------------------
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        try {
            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            if (server == null) return; // dedicated client — skip
            var rm = server.getRecipeManager();

            List<ShapedBlastFurnaceRecipe> shaped = rm.recipeMap()
                    .byType(RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get())
                    .stream().map(RecipeHolder::value).collect(Collectors.toList());
            registration.addRecipes(BlastFurnaceShapedCategory.TYPE, shaped);

            List<ShapelessBlastFurnaceRecipe> shapeless = rm.recipeMap()
                    .byType(RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get())
                    .stream().map(RecipeHolder::value).collect(Collectors.toList());
            registration.addRecipes(BlastFurnaceShapelessCategory.TYPE, shapeless);

            List<PulseFurnaceRecipe> pulse = rm.recipeMap()
                    .byType(RotaryRecipeTypes.PULSE_FURNACE.get())
                    .stream().map(RecipeHolder::value).collect(Collectors.toList());
            registration.addRecipes(PulseFurnaceCategory.TYPE, pulse);

        } catch (Throwable t) {
            RotaryCraft.LOGGER.error("Failed to register RotaryCraft JEI recipes", t);
        }
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {}

    // =========================================================================
    // Blast Furnace — shaped (3×3 grid of ingredients, like crafting)
    // =========================================================================
    public static final class BlastFurnaceShapedCategory
            implements IRecipeCategory<ShapedBlastFurnaceRecipe> {

        public static final RecipeType<ShapedBlastFurnaceRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "blast_furnace_shaped", ShapedBlastFurnaceRecipe.class);

        private final IDrawable icon;

        public BlastFurnaceShapedCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.BLASTFURNACE.getCraftedProduct());
        }

        @Override public RecipeType<ShapedBlastFurnaceRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.blastfurnace"); }
        // JEI 29.x: getBackground() removed — size is declared by getWidth()/getHeight() instead
        @Override public int getWidth()  { return 116; }
        @Override public int getHeight() { return 60; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder,
                              ShapedBlastFurnaceRecipe recipe,
                              IFocusGroup focuses) {
            List<Ingredient> ings = recipe.getIngredients();
            for (int i = 0; i < Math.min(ings.size(), 9); i++) {
                int col = i % 3, row = i / 3;
                builder.addSlot(RecipeIngredientRole.INPUT, 1 + col * 18, 1 + row * 18)
                       .addIngredients(ings.get(i));
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 21)
                   .addItemStack(recipe.getOutput());
        }
    }

    // =========================================================================
    // Blast Furnace — shapeless (up to 9 unordered ingredients)
    // =========================================================================
    public static final class BlastFurnaceShapelessCategory
            implements IRecipeCategory<ShapelessBlastFurnaceRecipe> {

        public static final RecipeType<ShapelessBlastFurnaceRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "blast_furnace_shapeless", ShapelessBlastFurnaceRecipe.class);

        private final IDrawable icon;

        public BlastFurnaceShapelessCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.BLASTFURNACE.getCraftedProduct());
        }

        @Override public RecipeType<ShapelessBlastFurnaceRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.blastfurnace"); }
        @Override public int getWidth()  { return 116; }
        @Override public int getHeight() { return 60; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder,
                              ShapelessBlastFurnaceRecipe recipe,
                              IFocusGroup focuses) {
            List<Ingredient> ings = recipe.getIngredients();
            for (int i = 0; i < Math.min(ings.size(), 9); i++) {
                int col = i % 3, row = i / 3;
                builder.addSlot(RecipeIngredientRole.INPUT, 1 + col * 18, 1 + row * 18)
                       .addIngredients(ings.get(i));
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 98, 21)
                   .addItemStack(recipe.getOutput());
        }
    }

    // =========================================================================
    // Pulse Jet Furnace — single input → single output
    // =========================================================================
    public static final class PulseFurnaceCategory
            implements IRecipeCategory<PulseFurnaceRecipe> {

        public static final RecipeType<PulseFurnaceRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "pulse_furnace", PulseFurnaceRecipe.class);

        private final IDrawable icon;

        public PulseFurnaceCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.PULSEJET.getCraftedProduct());
        }

        @Override public RecipeType<PulseFurnaceRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.pulsejet"); }
        @Override public int getWidth()  { return 76; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder,
                              PulseFurnaceRecipe recipe,
                              IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                   .addIngredients(recipe.getInput());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 9)
                   .addItemStack(recipe.getOutput());
        }
    }
}
