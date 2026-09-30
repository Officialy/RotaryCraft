package reika.rotarycraft.modinterface.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.client.Minecraft;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.world.level.material.Fluids;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.TerraformingRecipe;
import reika.rotarycraft.registry.MachineRegistry;

/** JEI displays the same reloaded biome steps as the machine, including original probabilistic costs. */
public class TerraformerJEICategory implements IRecipeCategory<TerraformingRecipe> {
    public static final RecipeType<TerraformingRecipe> TYPE = RecipeType.create(RotaryCraft.MODID, "terraformer", TerraformingRecipe.class);
    private final IDrawable icon;
    public TerraformerJEICategory(IGuiHelper gui) { icon = gui.createDrawableItemStack(MachineRegistry.TERRAFORMER.getCraftedProduct()); }
    @Override public RecipeType<TerraformingRecipe> getRecipeType() { return TYPE; }
    @Override public Component getTitle() { return Component.translatable("machine.terraformer"); }
    @Override public int getWidth() { return 210; }
    @Override public int getHeight() { return 64; }
    @Override public IDrawable getIcon() { return icon; }
    @Override public void setRecipe(IRecipeLayoutBuilder builder, TerraformingRecipe recipe, IFocusGroup focuses) {
        for (int n = 0; n < recipe.items().size(); n++) {
            var cost = recipe.items().get(n);
            builder.addSlot(RecipeIngredientRole.INPUT, 2 + n * 20, 42).addIngredients(cost.ingredient())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.literal(cost.chance() == 0 ? "Required catalyst; never consumed" : "Required catalyst; consumed once in " + Math.max(1, (int)(1F / cost.chance())) + " per-column rolls")));
        }
        if (recipe.water() > 0) builder.addSlot(RecipeIngredientRole.INPUT, 190, 42).addFluidStack(Fluids.WATER, recipe.water() * 16);
    }
    @Override public void draw(TerraformingRecipe recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics, double mouseX, double mouseY) {
        var font = Minecraft.getInstance().font;
        String from = recipe.source().location().getPath().replace("terraformer/", "");
        graphics.text(font, from + " -> " + recipe.target().identifier().getPath(), 2, 2, 0xff404040, false);
        graphics.text(font, recipe.minPower() + " W; " + recipe.water() + " mB/column", 2, 14, 0xff404040, false);
        graphics.text(font, "One 4x4 cell pays 16 column costs", 2, 26, 0xff404040, false);
    }
}
