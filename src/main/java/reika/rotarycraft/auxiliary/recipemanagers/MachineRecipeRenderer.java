package reika.rotarycraft.auxiliary.recipemanagers;

import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.Font;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.core.NonNullList;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraft.world.level.Level;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryRecipeTypes;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class MachineRecipeRenderer {

    public static final MachineRecipeRenderer instance = new MachineRecipeRenderer();

    private final Font font = Minecraft.getInstance().font;
    private final ReikaGuiAPI gui = ReikaGuiAPI.instance;

    private MachineRecipeRenderer() {

    }

    private int getIndex(List<?> li) {
        long time = System.currentTimeMillis();
        int index = (int)((time / 500) % li.size());
        return index;
    }

    public void drawCompressor(GuiGraphics itemRender, int x, int y, ItemStack in, int x2, int y2, ItemStack out) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        if (in != null) {
            for (int ii = 0; ii < 4; ii++)
                gui.drawItemStackWithTooltip(itemRender, font, in, x + j, y + k + ii * 18);
        }
        if (out != null)
            gui.drawItemStackWithTooltip(itemRender, font, out, x2 + j, y2 + k);
    }

    public void drawBlastFurnaceRecipe(GuiGraphics graphics, int x, int y, int x2, int y2, Level level, ItemStack output) {
        List<ShapedBlastFurnaceRecipe> shapedRecipes = level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get()).stream()
                .map(r -> (ShapedBlastFurnaceRecipe) r)
                .filter(r -> r.getResultItem(level.registryAccess()).getItem() == output.getItem())
                .collect(Collectors.toList());

        List<ShapelessBlastFurnaceRecipe> shapelessRecipes = level.getRecipeManager().getAllRecipesFor(RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get()).stream()
                .map(r -> (ShapelessBlastFurnaceRecipe) r)
                .filter(r -> r.getResultItem(level.registryAccess()).getItem() == output.getItem())
                .collect(Collectors.toList());

        List<Recipe<?>> allRecipes = new ArrayList<>();
        allRecipes.addAll(shapedRecipes);
        allRecipes.addAll(shapelessRecipes);

        if (allRecipes.isEmpty()) {
            RotaryCraft.LOGGER.error("Tried drawing a Blast Furnace Crafting of " + output + ", but no recipes for it exist!");
            return;
        }

        int n = getIndex(allRecipes);
        Recipe<?> currentRecipe = allRecipes.get(n);

        if (currentRecipe instanceof ShapedBlastFurnaceRecipe shapedRecipe) {
            drawShapedBlastFurnaceRecipe(graphics, x, y, x2, y2, shapedRecipe);
        } else if (currentRecipe instanceof ShapelessBlastFurnaceRecipe shapelessRecipe) {
            drawShapelessBlastFurnaceRecipe(graphics, x, y, x2, y2, shapelessRecipe);
        }
    }

    private void drawShapedBlastFurnaceRecipe(GuiGraphics graphics, int x, int y, int x2, int y2, ShapedBlastFurnaceRecipe recipe) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int row = 0; row < 3; row++) {
            for (int col = 0; col < 3; col++) {
                int index = row * 3 + col;
                if (index < ingredients.size()) {
                    Ingredient ingredient = ingredients.get(index);
                    if (!ingredient.isEmpty()) {
                        ItemStack[] items = ingredient.getItems();
                        if (items.length > 0) {
                            ItemStack displayItem = items[getIndex(List.of(items))];
                            gui.drawItemStackWithTooltip(graphics, font, displayItem, x + j + col * 18, y + k + row * 18);
                        }
                    }
                }
            }
        }
        gui.drawItemStackWithTooltip(graphics, font, recipe.getResultItem(Minecraft.getInstance().level.registryAccess()), x2 + j, y2 + k);
    }

    private void drawShapelessBlastFurnaceRecipe(GuiGraphics graphics, int x, int y, int x2, int y2, ShapelessBlastFurnaceRecipe recipe) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        NonNullList<Ingredient> ingredients = recipe.getIngredients();
        for (int i = 0; i < ingredients.size(); i++) {
            Ingredient ingredient = ingredients.get(i);
            if (!ingredient.isEmpty()) {
                ItemStack[] items = ingredient.getItems();
                if (items.length > 0) {
                    ItemStack displayItem = items[getIndex(List.of(items))];
                    gui.drawItemStackWithTooltip(graphics, font, displayItem, x + j + (i % 3) * 18, y + k + (i / 3) * 18);
                }
            }
        }

        NonNullList<Ingredient> additives = recipe.getAdditives();
        for (int i = 0; i < additives.size(); i++) {
            Ingredient additive = additives.get(i);
            if (!additive.isEmpty()) {
                ItemStack[] items = additive.getItems();
                if (items.length > 0) {
                    ItemStack displayItem = items[getIndex(List.of(items))];
                    // Position additives below ingredients or in a separate section
                    gui.drawItemStackWithTooltip(graphics, font, displayItem, x + j + (i % 3) * 18, y + k + 54 + (i / 3) * 18);
                }
            }
        }

        gui.drawItemStackWithTooltip(graphics, font, recipe.getResultItem(Minecraft.getInstance().level.registryAccess()), x2 + j, y2 + k);
    }

    public void drawExtractor(GuiGraphics graphics, int x, int y, ItemStack[] in, int x2, int y2, ItemStack[] out) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        for (int ij = 0; ij < 4; ij++)
            gui.drawItemStackWithTooltip(graphics, font, in[ij], x + j + 36 * ij, y + k);
        for (int ij = 0; ij < 4; ij++)
            gui.drawItemStackWithTooltip(graphics, font, out[ij], x2 + j + 36 * ij, y2 + k);
    }

    public void drawFermenter(GuiGraphics graphics, int x, int y, ItemStack[] in, int x2, int y2, ItemStack out) {
        int j = gui.getScreenXInset();
        int k = gui.getScreenYInset();

        gui.drawItemStackWithTooltip(graphics, font, in[0], x + j, y + k);
        gui.drawItemStackWithTooltip(graphics, font, in[1], x + j, y + 36 + k);

        if (out != null)
            gui.drawItemStackWithTooltip(graphics, font, out, x2 + 4 + j, y2 + 4 + k);
    }
}