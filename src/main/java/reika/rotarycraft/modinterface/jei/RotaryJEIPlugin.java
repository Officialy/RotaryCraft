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
import mezz.jei.api.registration.IRecipeCatalystRegistration;
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
import reika.rotarycraft.auxiliary.recipemanagers.CentrifugeRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ExtractorRecipe;
import reika.rotarycraft.blockentities.production.BlockEntityFractionator;
import reika.rotarycraft.auxiliary.recipemanagers.FermenterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FrictionHeaterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.GrinderRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.PulseFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryFluids;
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
                new PulseFurnaceCategory(gui),
                new GrinderCategory(gui),
                new CentrifugeCategory(gui),
                new FrictionHeaterCategory(gui),
                new ExtractorCategory(gui),
                new FermenterCategory(gui),
                new FractionatorCategory(gui)
        );
    }

    @Override
    public void registerRecipeCatalysts(IRecipeCatalystRegistration registration) {
        registration.addRecipeCatalyst(MachineRegistry.BLASTFURNACE.getCraftedProduct(),
                BlastFurnaceShapedCategory.TYPE, BlastFurnaceShapelessCategory.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.PULSEJET.getCraftedProduct(), PulseFurnaceCategory.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.GRINDER.getCraftedProduct(), GrinderCategory.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.CENTRIFUGE.getCraftedProduct(), CentrifugeCategory.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.FRICTION.getCraftedProduct(), FrictionHeaterCategory.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.EXTRACTOR.getCraftedProduct(), ExtractorCategory.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.FERMENTER.getCraftedProduct(), FermenterCategory.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.FRACTIONATOR.getCraftedProduct(), FractionatorCategory.TYPE);
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

            List<GrinderRecipe> grinder = new java.util.ArrayList<>(rm.recipeMap()
                    .byType(RotaryRecipeTypes.GRINDER.get())
                    .stream().map(RecipeHolder::value).toList());
            // Display-only: the grinder also mills canola seeds into lubricant (the BE fills its own
            // tank — this isn't a datapack recipe). An empty item output flags the fluid path.
            grinder.add(new GrinderRecipe(
                    Ingredient.of(reika.rotarycraft.registry.RotaryItems.CANOLA_SEEDS.get()),
                    new net.minecraft.world.item.ItemStackTemplate(net.minecraft.world.item.Items.AIR)));
            registration.addRecipes(GrinderCategory.TYPE, grinder);

            List<CentrifugeRecipe> centrifuge = rm.recipeMap()
                    .byType(RotaryRecipeTypes.CENTRIFUGE.get())
                    .stream().map(RecipeHolder::value).collect(Collectors.toList());
            registration.addRecipes(CentrifugeCategory.TYPE, centrifuge);

            List<FrictionHeaterRecipe> friction = rm.recipeMap()
                    .byType(RotaryRecipeTypes.FRICTION_HEATER.get())
                    .stream().map(RecipeHolder::value).collect(Collectors.toList());
            registration.addRecipes(FrictionHeaterCategory.TYPE, friction);

            List<ExtractorRecipe> extractor = rm.recipeMap()
                    .byType(RotaryRecipeTypes.EXTRACTOR.get())
                    .stream().map(RecipeHolder::value).collect(Collectors.toList());
            registration.addRecipes(ExtractorCategory.TYPE, extractor);

            List<FermenterRecipe> fermenter = rm.recipeMap()
                    .byType(RotaryRecipeTypes.FERMENTER.get())
                    .stream().map(RecipeHolder::value).collect(Collectors.toList());
            registration.addRecipes(FermenterCategory.TYPE, fermenter);

            // The fractionator recipe is hardcoded in the BE, not data-driven — one synthetic entry.
            registration.addRecipes(FractionatorCategory.TYPE, List.of(FractionatorJEIRecipe.INSTANCE));

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

    // =========================================================================
    // Grinder — single input → single output
    // =========================================================================
    public static final class GrinderCategory implements IRecipeCategory<GrinderRecipe> {

        public static final RecipeType<GrinderRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "grinder", GrinderRecipe.class);

        private final IDrawable icon;

        public GrinderCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.GRINDER.getCraftedProduct());
        }

        @Override public RecipeType<GrinderRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.grinder"); }
        @Override public int getWidth()  { return 76; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, GrinderRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                   .addIngredients(recipe.getInput());
            // Seeds mill into lubricant (fluid) rather than an item — an empty item output marks that.
            if (recipe.getOutput().isEmpty()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 54, 5)
                       .addFluidStack(reika.rotarycraft.registry.RotaryFluids.LUBRICANT.get(), 1000);
            } else {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 9)
                       .addItemStack(recipe.getOutput());
            }
        }
    }

    // =========================================================================
    // Centrifuge — single input → chanced outputs (+ optional fluid byproduct)
    // =========================================================================
    public static final class CentrifugeCategory implements IRecipeCategory<CentrifugeRecipe> {

        public static final RecipeType<CentrifugeRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "centrifuge", CentrifugeRecipe.class);

        private final IDrawable icon;

        public CentrifugeCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.CENTRIFUGE.getCraftedProduct());
        }

        @Override public RecipeType<CentrifugeRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.centrifuge"); }
        @Override public int getWidth()  { return 130; }
        @Override public int getHeight() { return 54; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, CentrifugeRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 19)
                   .addIngredients(recipe.getInput());
            int i = 0;
            for (CentrifugeRecipe.ChancedOutput out : recipe.getOutputs()) {
                float chance = Math.min(1F, out.chance());
                builder.addSlot(RecipeIngredientRole.OUTPUT, 58 + (i % 3) * 18, 1 + (i / 3) * 18)
                       .addItemStack(out.stack().create())
                       .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                               Component.literal(String.format("%.4g%% chance", chance * 100))));
                i++;
            }
            recipe.getFluidOutput().ifPresent(f -> builder
                    .addSlot(RecipeIngredientRole.OUTPUT, 112, 19)
                    .addFluidStack(f.fluid().value(), f.amount())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                            Component.literal(String.format("%.4g%% chance", Math.min(1F, f.chance()) * 100)))));
        }
    }

    // =========================================================================
    // Friction Heater — single input → single output (heat-driven)
    // =========================================================================
    public static final class FrictionHeaterCategory implements IRecipeCategory<FrictionHeaterRecipe> {

        public static final RecipeType<FrictionHeaterRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "friction_heater", FrictionHeaterRecipe.class);

        private final IDrawable icon;

        public FrictionHeaterCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.FRICTION.getCraftedProduct());
        }

        @Override public RecipeType<FrictionHeaterRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.friction"); }
        @Override public int getWidth()  { return 76; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, FrictionHeaterRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                   .addIngredients(recipe.getInput());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 9)
                   .addItemStack(recipe.getOutput())
                   .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                           Component.literal((int) recipe.requiredTemperature() + "C")));
        }
    }

    // =========================================================================
    // Extractor — 4-stage ore line (dust → slurry → solution → flakes)
    // =========================================================================
    public static final class ExtractorCategory implements IRecipeCategory<ExtractorRecipe> {

        public static final RecipeType<ExtractorRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "extractor", ExtractorRecipe.class);

        private final IDrawable icon;

        public ExtractorCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.EXTRACTOR.getCraftedProduct());
        }

        @Override public RecipeType<ExtractorRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.extractor"); }
        @Override public int getWidth()  { return 76; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, ExtractorRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                   .addIngredients(recipe.getInput());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 9)
                   .addItemStack(recipe.getOutput())
                   .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                           Component.literal("Stage " + (recipe.getStage() + 1) + "/4")));
        }
    }

    // =========================================================================
    // Fermenter — catalyst + input → output
    // =========================================================================
    public static final class FermenterCategory implements IRecipeCategory<FermenterRecipe> {

        public static final RecipeType<FermenterRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "fermenter", FermenterRecipe.class);

        private final IDrawable icon;

        public FermenterCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.FERMENTER.getCraftedProduct());
        }

        @Override public RecipeType<FermenterRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.fermenter"); }
        @Override public int getWidth()  { return 98; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, FermenterRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                   .addIngredients(recipe.getCatalyst());
            builder.addSlot(RecipeIngredientRole.INPUT, 23, 9)
                   .addIngredients(recipe.getInput());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 80, 9)
                   .addItemStack(recipe.getOutput());
        }
    }

    // =========================================================================
    // Fractionator — hardcoded jet-fuel recipe: ethanol + 6 ingredients + ghast tear
    // =========================================================================
    /** Marker for the single hardcoded fractionator recipe. */
    public static final class FractionatorJEIRecipe {
        public static final FractionatorJEIRecipe INSTANCE = new FractionatorJEIRecipe();
        private FractionatorJEIRecipe() {}
    }

    public static final class FractionatorCategory implements IRecipeCategory<FractionatorJEIRecipe> {

        public static final RecipeType<FractionatorJEIRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "fractionator", FractionatorJEIRecipe.class);

        private final IDrawable icon;

        public FractionatorCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.FRACTIONATOR.getCraftedProduct());
        }

        @Override public RecipeType<FractionatorJEIRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.fractionator"); }
        @Override public int getWidth()  { return 150; }
        @Override public int getHeight() { return 40; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, FractionatorJEIRecipe recipe, IFocusGroup focuses) {
            BlockEntityFractionator.registerIngredients();
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 11)
                   .addFluidStack(RotaryFluids.ETHANOL.get(), BlockEntityFractionator.ETHANOL_PER_OP);
            for (int i = 0; i < 6; i++) {
                var item = BlockEntityFractionator.ingredientForSlot(i);
                if (item == null) continue;
                builder.addSlot(RecipeIngredientRole.INPUT, 23 + (i % 3) * 18, 2 + (i / 3) * 18)
                       .addItemStack(new net.minecraft.world.item.ItemStack(item));
            }
            builder.addSlot(RecipeIngredientRole.INPUT, 81, 11)
                   .addItemStack(new net.minecraft.world.item.ItemStack(net.minecraft.world.item.Items.GHAST_TEAR))
                   .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                           Component.literal("Catalyst — consumed every 4th cycle")));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 11)
                   .addFluidStack(RotaryFluids.JET_FUEL.get(), 1000)
                   .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                           Component.literal("Yield scales with pressure and difficulty")));
        }
    }
}
