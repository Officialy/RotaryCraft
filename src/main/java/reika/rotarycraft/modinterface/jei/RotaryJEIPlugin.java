/*******************************************************************************
 * @author Reika Kalseki / 26.1 port by OfficialyMax
 *
 * JEI (Just Enough Items) integration for RotaryCraft machine processes.
 ******************************************************************************/
package reika.rotarycraft.modinterface.jei;

import mezz.jei.api.IModPlugin;
import java.util.Optional;
import reika.rotarycraft.blockentities.production.BlockEntityBlastFurnace;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import mezz.jei.api.runtime.IIngredientManager;
import mezz.jei.api.ingredients.ITypedIngredient;
import mezz.jei.api.gui.ingredient.IRecipeSlotsView;
import mezz.jei.api.constants.VanillaTypes;
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
                new BlastFurnaceShapelessCategory(gui, registration.getJeiHelpers().getIngredientManager()),
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
                new Distiller(gui), new FuelEnhancer(gui),
                new TerraformerJEICategory(gui),
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
        registration.addRecipeCatalyst(MachineRegistry.FUELENHANCER.getCraftedProduct(), FuelEnhancer.TYPE);
        registration.addRecipeCatalyst(MachineRegistry.TERRAFORMER.getCraftedProduct(), TerraformerJEICategory.TYPE);
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

        var magnetizer = new java.util.ArrayList<>(recipes.byType(RotaryRecipeTypes.MAGNETIZER.get()).stream().map(RecipeHolder::value).toList());
        magnetizer.addAll(RecipesMagnetizer.getRecipes().getAPIRecipes());
        tracked.accept(Magnetizer.TYPE, magnetizer);

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
        tracked.accept(TerraformerJEICategory.TYPE, recipes.byType(RotaryRecipeTypes.TERRAFORMER.get()).stream().map(RecipeHolder::value).toList());
        tracked.accept(FuelEnhancer.TYPE, recipes.byType(RotaryRecipeTypes.FUEL_ENHANCER.get()).stream().map(RecipeHolder::value)
                .filter(recipe -> net.minecraft.core.registries.BuiltInRegistries.FLUID.listElements().anyMatch(fluid -> fluid.is(recipe.input()))).toList());
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
    // Blast Furnace. Both categories reproduce V33a's NEI BlastFurnaceHandler: the furnace GUI
    // (blastfurngui.png, from 5,11) with the thermometer, the required temperature beneath it, the
    // grid at 57,6, the output at 143,24 and, for alloying, the three additives where the GUI keeps
    // them plus one "name: xN (chance%)" line each and the bonus output.
    // =========================================================================
    private static final Identifier BLAST_GUI =
            Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/blastfurngui.png");
    private static final int BLAST_WIDTH = 166;
    private static final int BLAST_GUI_HEIGHT = 70;

    private static void drawBlastFurnace(GuiGraphicsExtractor graphics, float temperature) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, BLAST_GUI, 0, 0, 5, 11, BLAST_WIDTH, BLAST_GUI_HEIGHT, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, BLAST_GUI, 6, 17, 176, 44, 11, 43, 256, 256);
        var font = Minecraft.getInstance().font;
        String text = String.format("%dC", Math.round(temperature));
        graphics.text(font, text, Math.max(0, 11 - font.width(text) / 2), 61, 0xff000000, false);
    }

    private static ItemStack displayStack(Ingredient ingredient) {
        return ingredient.items().findFirst().map(h -> h.value().getDefaultInstance()).orElse(ItemStack.EMPTY);
    }

    public static final class BlastFurnaceShapedCategory
            implements IRecipeCategory<ShapedBlastFurnaceRecipe> {

        public static final RecipeType<ShapedBlastFurnaceRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "blast_furnace_shaped", ShapedBlastFurnaceRecipe.class);

        private final IDrawable icon;

        public BlastFurnaceShapedCategory(IGuiHelper gui) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.BLASTFURNACE.getCraftedProduct());
        }

        @Override public RecipeType<ShapedBlastFurnaceRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.BLASTFURNACE.getCraftedProduct().getHoverName(); }
        @Override public int getWidth()  { return BLAST_WIDTH; }
        @Override public int getHeight() { return BLAST_GUI_HEIGHT; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder,
                              ShapedBlastFurnaceRecipe recipe,
                              IFocusGroup focuses) {
            // Place each cell where the pattern puts it; the flat ingredient list drops the gaps.
            var pattern = recipe.getPattern();
            var cells = pattern.ingredients();
            for (int row = 0; row < pattern.height(); row++)
                for (int col = 0; col < pattern.width(); col++) {
                    var cell = cells.get(col + row * pattern.width());
                    if (cell.isPresent())
                        builder.addSlot(RecipeIngredientRole.INPUT, 57 + col * 18, 6 + row * 18)
                                .add(cell.get());
                }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 143, 24).add(recipe.getOutput());
        }

        @Override
        public void draw(ShapedBlastFurnaceRecipe recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics,
                         double mouseX, double mouseY) {
            drawBlastFurnace(graphics, recipe.getOperatingTemperature());
        }
    }

    public static final class BlastFurnaceShapelessCategory
            implements IRecipeCategory<ShapelessBlastFurnaceRecipe> {

        public static final RecipeType<ShapelessBlastFurnaceRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "blast_furnace_shapeless", ShapelessBlastFurnaceRecipe.class);
        private static final int LINE = 11;

        private final IDrawable icon;
        private final IIngredientManager ingredients;

        public BlastFurnaceShapelessCategory(IGuiHelper gui, IIngredientManager ingredients) {
            this.icon = gui.createDrawableItemStack(MachineRegistry.BLASTFURNACE.getCraftedProduct());
            this.ingredients = ingredients;
        }

        @Override public RecipeType<ShapelessBlastFurnaceRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.BLASTFURNACE.getCraftedProduct().getHoverName(); }
        @Override public int getWidth()  { return BLAST_WIDTH; }
        // The GUI plus up to three additive lines and the bonus line.
        @Override public int getHeight() { return BLAST_GUI_HEIGHT + 2 + 4 * LINE; }
        @Override public IDrawable getIcon() { return icon; }

        /** V33a BlastRecipe.getValidInputNumbers: the batch sizes the grid accepts. */
        private static List<Integer> batchSizes(ShapelessBlastFurnaceRecipe recipe) {
            List<Integer> sizes = new java.util.ArrayList<>();
            int per = recipe.getMainCount();
            for (int n = per; n <= 9; n += per)
                if (n == per || !recipe.isExactCount())
                    sizes.add(n);
            return sizes;
        }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder,
                              ShapelessBlastFurnaceRecipe recipe,
                              IFocusGroup focuses) {
            // NEI cycled the batch size: n main items in the grid (column-major) and the n / mainCount
            // products they make. Every slot carries one frame per batch size, empty where that batch
            // leaves the cell bare, so JEI's shared cycle keeps the grid and the output in step.
            List<Integer> sizes = batchSizes(recipe);
            ItemStack main = displayStack(recipe.getIngredients().getFirst());
            for (int col = 0; col < 3; col++)
                for (int row = 0; row < 3; row++) {
                    int cell = col * 3 + row;
                    List<Optional<ITypedIngredient<?>>> frames = new java.util.ArrayList<>();
                    boolean used = false;
                    for (int size : sizes) {
                        boolean filled = cell < size;
                        used |= filled;
                        frames.add(filled ? ingredients.createTypedIngredient(VanillaTypes.ITEM_STACK, main)
                                .map(t -> (ITypedIngredient<?>) t) : Optional.empty());
                    }
                    if (used)
                        builder.addSlot(RecipeIngredientRole.INPUT, 57 + col * 18, 6 + row * 18)
                                .addOptionalTypedIngredients(frames);
                }
            List<ItemStack> outputs = new java.util.ArrayList<>();
            for (int size : sizes) {
                ItemStack out = recipe.getOutput();
                out.setCount(out.getCount() * (recipe.getMainCount() > 1 ? size / recipe.getMainCount() : size));
                outputs.add(out);
            }
            builder.addSlot(RecipeIngredientRole.OUTPUT, 143, 24).addItemStacks(outputs);
            for (ShapelessBlastFurnaceRecipe.Additive additive : recipe.getAdditives()) {
                int y = switch (additive.slot()) {
                    case BlockEntityBlastFurnace.UPPER_ADDITIVE -> 5;
                    case BlockEntityBlastFurnace.LOWER_ADDITIVE -> 43;
                    default -> 24;
                };
                builder.addSlot(RecipeIngredientRole.INPUT, 21, y).add(additive.ingredient());
            }
        }

        @Override
        public void draw(ShapelessBlastFurnaceRecipe recipe, IRecipeSlotsView slots, GuiGraphicsExtractor graphics,
                         double mouseX, double mouseY) {
            drawBlastFurnace(graphics, recipe.getOperatingTemperature());
            var font = Minecraft.getInstance().font;
            int y = BLAST_GUI_HEIGHT + 2;
            // NEI order: primary (centre), secondary (lower), tertiary (upper).
            for (int slot : new int[] {BlockEntityBlastFurnace.CENTER_ADDITIVE,
                    BlockEntityBlastFurnace.LOWER_ADDITIVE, BlockEntityBlastFurnace.UPPER_ADDITIVE})
                for (ShapelessBlastFurnaceRecipe.Additive additive : recipe.getAdditives())
                    if (additive.slot() == slot) {
                        String line = String.format("%s: x%d (%.1f%%)",
                                displayStack(additive.ingredient()).getHoverName().getString(),
                                additive.count(), 100 * additive.chance());
                        graphics.text(font, line, 21, y, 0xff000000, false);
                        y += LINE;
                    }
            if (!recipe.getAdditives().isEmpty() || recipe.bonusChance() > 0)
                graphics.text(font, "Bonus output: " + (recipe.bonusChance() > 0
                        ? recipe.bonusChance() / 100F + "x" : "None"), 21, y, 0xff000000, false);
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
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.PULSEJET.getCraftedProduct().getHoverName(); }
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
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.GRINDER.getCraftedProduct().getHoverName(); }
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
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.CENTRIFUGE.getCraftedProduct().getHoverName(); }
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
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.FRICTION.getCraftedProduct().getHoverName(); }
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
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.EXTRACTOR.getCraftedProduct().getHoverName(); }
        @Override public int getWidth()  { return 103; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, ExtractorRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                   .addIngredients(recipe.getInput());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 58, 9)
                   .addItemStack(recipe.getOutput())
                   .addRichTooltipCallback((view, tooltip) -> {
                       tooltip.add(Component.literal("Stage " + (recipe.getStage() + 1) + "/4"));
                       recipe.getDuplicationChance().ifPresent(chance -> tooltip.add(Component.translatable(
                               "tooltip.rotarycraft.extractor.duplication", Math.round(chance * 100))));
                       recipe.getOreDuplicationChance().ifPresent(chance -> tooltip.add(Component.translatable(
                               "tooltip.rotarycraft.extractor.duplication", Math.round(chance * 100))));
                   });
            recipe.getAvailableBonus().ifPresent(bonus -> builder.addSlot(RecipeIngredientRole.OUTPUT, 85, 9)
                    .addItemStack(bonus.output().create()).addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "tooltip.rotarycraft.extractor.bonus", Math.round(bonus.chance() * 100)))));
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
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.FERMENTER.getCraftedProduct().getHoverName(); }
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
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.FRACTIONATOR.getCraftedProduct().getHoverName(); }
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
    public static final class FuelEnhancer implements IRecipeCategory<reika.rotarycraft.auxiliary.recipemanagers.FuelEnhancerRecipe> {
        public static final RecipeType<reika.rotarycraft.auxiliary.recipemanagers.FuelEnhancerRecipe> TYPE = RecipeType.create(RotaryCraft.MODID, "fuel_enhancer", reika.rotarycraft.auxiliary.recipemanagers.FuelEnhancerRecipe.class);
        private final IDrawable icon;
        public FuelEnhancer(IGuiHelper gui) { icon = gui.createDrawableItemStack(MachineRegistry.FUELENHANCER.getCraftedProduct()); }
        @Override public RecipeType<reika.rotarycraft.auxiliary.recipemanagers.FuelEnhancerRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.FUELENHANCER.getCraftedProduct().getHoverName(); }
        @Override public int getWidth() { return 170; }
        @Override public int getHeight() { return 58; }
        @Override public IDrawable getIcon() { return icon; }
        @Override public void setRecipe(IRecipeLayoutBuilder builder, reika.rotarycraft.auxiliary.recipemanagers.FuelEnhancerRecipe recipe, IFocusGroup focuses) {
            var input = builder.addSlot(RecipeIngredientRole.INPUT, 1, 1);
            net.minecraft.core.registries.BuiltInRegistries.FLUID.listElements().filter(fluid -> fluid.is(recipe.input())).forEach(fluid -> input.addFluidStack(fluid.value(), (long) recipe.fluidRatio() * recipe.speedFactor()));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 150, 1).addFluidStack(recipe.output().value(), recipe.speedFactor())
                    .addRichTooltipCallback((view, tooltip) -> {
                        tooltip.add(Component.translatable("jei.rotarycraft.fuel_enhancer_power"));
                        if (!recipe.getCondition().isEmpty()) tooltip.add(Component.literal(recipe.getCondition()));
                    });
            for (int n = 0; n < recipe.ingredients().size(); n++)
                builder.addSlot(RecipeIngredientRole.INPUT, 1 + 18 * n, 35).add(recipe.ingredients().get(n))
                        .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable("jei.rotarycraft.fuel_enhancer_consumption", String.format(java.util.Locale.ROOT, "%.4f", recipe.itemConsumptionChance() * 100))));
        }
    }

    public static final class Distiller implements IRecipeCategory<reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe> {
        public static final RecipeType<reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe> TYPE = RecipeType.create(RotaryCraft.MODID, "distiller", reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe.class);
        private final IDrawable icon;
        public Distiller(IGuiHelper gui) { icon = gui.createDrawableItemStack(MachineRegistry.DISTILLER.getCraftedProduct()); }
        @Override public RecipeType<reika.rotarycraft.auxiliary.recipemanagers.DistilleryRecipe> getRecipeType() { return TYPE; }
        @Override public Component getTitle() { return reika.rotarycraft.registry.MachineRegistry.DISTILLER.getCraftedProduct().getHoverName(); }
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
