/*******************************************************************************
 * @author Reika Kalseki / 26.1 port by OfficialyMax
 *
 * JEI (Just Enough Items) integration for RotaryCraft machine processes.
 ******************************************************************************/
package reika.rotarycraft.modinterface.jei;

import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.RecipeTypes;
import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.IRecipeManager;
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
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.Ingredient;
import net.minecraft.world.item.crafting.RecipeHolder;
import net.minecraft.world.item.crafting.RecipeMap;

import java.util.List;
import java.util.Map;
import java.util.HashMap;
import java.util.stream.Collectors;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.CentrifugeRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ExtractorRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FractionatorRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FermenterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.FrictionHeaterRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.GrinderRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.PulseFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapedBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.ShapelessBlastFurnaceRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.RecipesMagnetizer;
import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
import reika.rotarycraft.modinterface.jei.RotaryAdditionalJEICategories.*;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryRecipeTypes;

@JeiPlugin
public class RotaryJEIPlugin implements IModPlugin {

    public static final Identifier UID = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "jei_plugin");
    private static IJeiRuntime runtime;
    private static RecipeMap registeredMap;
    private static Map<RecipeType<?>, List<?>> registeredDataRecipes = Map.of();

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
                new FractionatorCategory(gui),
                new LavaMaker(gui),
                new Compactor(gui),
                new Purifier(gui),
                new Distiller(gui),
                new Wetter(gui),
                new DryingBed(gui),
                new Crystallizer(gui),
                new Magnetizer(gui),
                new Composter(gui),
                new Refrigerator(gui),
                new ObsidianMaker(gui)
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
        registration.addRecipeCatalyst(MachineRegistry.LAVAMAKER.getCraftedProduct(), LavaMaker.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.COMPACTOR.getCraftedProduct(), Compactor.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.PURIFIER.getCraftedProduct(), Purifier.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.DISTILLER.getCraftedProduct(), Distiller.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.WETTER.getCraftedProduct(), Wetter.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.DRYING.getCraftedProduct(), DryingBed.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.CRYSTALLIZER.getCraftedProduct(), Crystallizer.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.MAGNETIZER.getCraftedProduct(), Magnetizer.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.COMPOSTER.getCraftedProduct(), Composter.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.REFRIGERATOR.getCraftedProduct(), Refrigerator.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.OBSIDIAN.getCraftedProduct(), ObsidianMaker.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.WORKTABLE.getCraftedProduct(), RecipeTypes.CRAFTING);
    }

    // -------------------------------------------------------------------------
    @Override
    public void registerRecipes(IRecipeRegistration registration) {
        try {
            registration.addRecipes(Magnetizer.TYPE,
                    List.copyOf(RecipesMagnetizer.getRecipes().getAllRecipes()));
            registration.addRecipes(Composter.TYPE, BlockEntityComposter.getAllCompostables().stream()
                    .filter(stack -> BlockEntityComposter.getCompostValue(stack) > 0)
                    .map(stack -> new Composting(stack.copy(), BlockEntityComposter.getCompostValue(stack)))
                    .toList());
            registration.addRecipes(Refrigerator.TYPE, List.of(new Cooling()));
            registration.addRecipes(ObsidianMaker.TYPE,
                    List.of(new ObsidianMix(true), new ObsidianMix(false)));

            MinecraftServer server = Minecraft.getInstance().getSingleplayerServer();
            RecipeMap recipes = RotaryRecipeSync.Client.getCurrentRecipes();
            if (recipes == null && server != null)
                recipes = server.getRecipeManager().recipeMap();
            if (recipes != null) {
                addDataRecipes(recipes, registration::addRecipes);
                registeredMap = recipes;
            }

        } catch (Throwable t) {
            RotaryCraft.LOGGER.error("Failed to register RotaryCraft JEI recipes", t);
        }
    }

    @FunctionalInterface
    private interface RecipeSink {
        <T> void accept(RecipeType<T> type, List<T> recipes);
    }

    private static void addDataRecipes(RecipeMap recipes, RecipeSink sink) {
        Map<RecipeType<?>, List<?>> added = new HashMap<>();
        RecipeSink tracked = new RecipeSink() {
            @Override
            public <T> void accept(RecipeType<T> type, List<T> values) {
                sink.accept(type, values);
                added.put(type, values);
            }
        };

        List<ShapedBlastFurnaceRecipe> shaped = recipes
                .byType(RotaryRecipeTypes.BLAST_FURNACE_SHAPED.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(BlastFurnaceShapedCategory.TYPE, shaped);

        List<ShapelessBlastFurnaceRecipe> shapeless = recipes
                .byType(RotaryRecipeTypes.BLAST_FURNACE_SHAPELESS.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(BlastFurnaceShapelessCategory.TYPE, shapeless);

        List<PulseFurnaceRecipe> pulse = recipes
                .byType(RotaryRecipeTypes.PULSE_FURNACE.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(PulseFurnaceCategory.TYPE, pulse);

        List<GrinderJEIRecipe> grinder = new java.util.ArrayList<>(recipes
                .byType(RotaryRecipeTypes.GRINDER.get())
                .stream().map(RecipeHolder::value)
                .map(recipe -> new GrinderJEIRecipe(recipe.getInput(), recipe.getOutput(), false))
                .toList());
        // Display-only: the grinder also mills canola seeds into lubricant (the BE fills its own
        // tank — this isn't a datapack recipe). Keep that presentation separate from the
        // serialized item-output recipe instead of constructing an illegal AIR template.
        grinder.add(new GrinderJEIRecipe(
                Ingredient.of(reika.rotarycraft.registry.RotaryItems.CANOLA_SEEDS.get()),
                ItemStack.EMPTY, true));
        tracked.accept(GrinderCategory.TYPE, grinder);

        List<CentrifugeRecipe> centrifuge = recipes
                .byType(RotaryRecipeTypes.CENTRIFUGE.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(CentrifugeCategory.TYPE, centrifuge);

        List<FrictionHeaterRecipe> friction = recipes
                .byType(RotaryRecipeTypes.FRICTION_HEATER.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(FrictionHeaterCategory.TYPE, friction);

        List<ExtractorRecipe> extractor = recipes
                .byType(RotaryRecipeTypes.EXTRACTOR.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(ExtractorCategory.TYPE, extractor);

        List<FermenterRecipe> fermenter = recipes
                .byType(RotaryRecipeTypes.FERMENTER.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(FermenterCategory.TYPE, fermenter);

        List<FractionatorRecipe> fractionator = recipes
                .byType(RotaryRecipeTypes.FRACTIONATOR.get())
                .stream().map(RecipeHolder::value).collect(Collectors.toList());
        tracked.accept(FractionatorCategory.TYPE, fractionator);

        tracked.accept(LavaMaker.TYPE, recipes
                .byType(RotaryRecipeTypes.LAVA_MAKER.get()).stream()
                .map(RecipeHolder::value).toList());
        tracked.accept(Purifier.TYPE, recipes.byType(RotaryRecipeTypes.PURIFIER.get()).stream()
                .map(RecipeHolder::value).toList());
        tracked.accept(Distiller.TYPE, recipes.byType(RotaryRecipeTypes.DISTILLER.get()).stream().map(RecipeHolder::value)
                .filter(recipe -> net.minecraft.core.registries.BuiltInRegistries.FLUID.listElements().anyMatch(fluid -> fluid.is(recipe.input()))).toList());
        tracked.accept(Compactor.TYPE, recipes
                .byType(RotaryRecipeTypes.COMPACTOR.get()).stream()
                .map(RecipeHolder::value).toList());
        tracked.accept(Wetter.TYPE, recipes
                .byType(RotaryRecipeTypes.WETTER.get()).stream()
                .map(RecipeHolder::value).toList());
        tracked.accept(DryingBed.TYPE, recipes
                .byType(RotaryRecipeTypes.DRYING_BED.get()).stream()
                .map(RecipeHolder::value).toList());
        tracked.accept(Crystallizer.TYPE, recipes
                .byType(RotaryRecipeTypes.CRYSTALLIZER.get()).stream()
                .map(RecipeHolder::value).toList());

        registeredDataRecipes = Map.copyOf(added);
    }

    @Override
    public void onRuntimeAvailable(IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        RecipeMap synced = RotaryRecipeSync.Client.getCurrentRecipes();
        if (synced != null && synced != registeredMap)
            onRecipeMapReceived(synced);
    }

    @Override
    public void onRuntimeUnavailable() {
        runtime = null;
        registeredMap = null;
        registeredDataRecipes = Map.of();
    }

    static void onRecipeMapReceived(RecipeMap recipes) {
        if (runtime == null || recipes == registeredMap)
            return;
        try {
            hideRegisteredDataRecipes(runtime.getRecipeManager());
            addDataRecipes(recipes, runtime.getRecipeManager()::addRecipes);
            registeredMap = recipes;
        } catch (Throwable t) {
            RotaryCraft.LOGGER.error("Failed to update RotaryCraft JEI recipes after datapack sync", t);
        }
    }

    @SuppressWarnings({"rawtypes", "unchecked"})
    private static void hideRegisteredDataRecipes(IRecipeManager manager) {
        for (var entry : registeredDataRecipes.entrySet())
            manager.hideRecipes((RecipeType) entry.getKey(), entry.getValue());
    }

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
    public record GrinderJEIRecipe(Ingredient input, ItemStack output, boolean lubricant) {}

    public static final class GrinderCategory implements IRecipeCategory<GrinderJEIRecipe> {

        public static final RecipeType<GrinderJEIRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "grinder", GrinderJEIRecipe.class);

        private final IDrawable icon;

        public GrinderCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.GRINDER.getCraftedProduct());
        }

        @Override public RecipeType<GrinderJEIRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.grinder"); }
        @Override public int getWidth()  { return 76; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, GrinderJEIRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                   .addIngredients(recipe.input());
            // Seeds mill into lubricant (fluid) rather than an item — an empty item output marks that.
            if (recipe.lubricant()) {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 54, 5)
                       .addFluidStack(reika.rotarycraft.registry.RotaryFluids.LUBRICANT.get(), 1000);
            } else {
                builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 9)
                       .addItemStack(recipe.output());
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
    public static final class FractionatorCategory implements IRecipeCategory<FractionatorRecipe> {

        public static final RecipeType<FractionatorRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "fractionator", FractionatorRecipe.class);

        private final IDrawable icon;

        public FractionatorCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.FRACTIONATOR.getCraftedProduct());
        }

        @Override public RecipeType<FractionatorRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.fractionator"); }
        @Override public int getWidth()  { return 150; }
        @Override public int getHeight() { return 40; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, FractionatorRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 11)
                   .addFluidStack(recipe.getInputFluid(), recipe.getInputAmount());
            for (int i = 0; i < recipe.getIngredients().size(); i++) {
                var entry = recipe.getIngredients().get(i);
                builder.addSlot(RecipeIngredientRole.INPUT, 23 + (i % 3) * 18, 2 + (i / 3) * 18)
                       .addIngredients(entry.ingredient())
                       .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                               Component.literal("Consumption weight: " + entry.weight())));
            }
            builder.addSlot(RecipeIngredientRole.INPUT, 81, 11)
                   .addIngredients(recipe.getSolvent())
                   .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                           Component.literal("Solvent — required but not consumed")));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 128, 11)
                   .addFluidStack(recipe.getOutputFluid(), recipe.getNominalOutput())
                   .addRichTooltipCallback((view, tooltip) -> tooltip.add(
                           Component.literal("Yield scales with pressure and difficulty")));
        }
    }
    public static final class Distiller implements IRecipeCategory<reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe> {
        public static final RecipeType<reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe> TYPE = RecipeType.create(RotaryCraft.MODID, "distiller", reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe.class);
        private final IDrawable icon;
        public Distiller(IGuiHelper gui) { icon = gui.createDrawableItemStack(MachineRegistry.DISTILLER.getCraftedProduct()); }
        @Override public RecipeType<reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return Component.translatable("machine.distiller"); }
        @Override public int getWidth() { return 140; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }
        @Override public void setRecipe(IRecipeLayoutBuilder builder, reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe recipe, IFocusGroup focuses) {
            var input = builder.addSlot(RecipeIngredientRole.INPUT, 1, 9);
            net.minecraft.core.registries.BuiltInRegistries.FLUID.listElements().filter(fluid -> fluid.is(recipe.input())).forEach(fluid ->
                    input.addFluidStack(fluid.value(), recipe.inputAmount()));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 118, 9).addFluidStack(recipe.output().value(), recipe.outputAmount())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.literal(recipe.minTorque() + " Nm, " + recipe.minPower() + " W; one conversion per 6 ticks")));
        }
    }

}
