package reika.rotarycraft.modinterface.jei;

import mezz.jei.api.gui.builder.IRecipeLayoutBuilder;
import mezz.jei.api.gui.drawable.IDrawable;
import mezz.jei.api.helpers.IGuiHelper;
import mezz.jei.api.recipe.IFocusGroup;
import mezz.jei.api.recipe.RecipeIngredientRole;
import mezz.jei.api.recipe.RecipeType;
import mezz.jei.api.recipe.category.IRecipeCategory;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.CompactorRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.CrystallizerRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.DryingBedRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.LavaMakerRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.RecipesMagnetizer.MagnetizerRecipe;
import reika.rotarycraft.auxiliary.recipemanagers.WetterRecipe;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

/** JEI views for the RotaryCraft process types that share the machine recipe registry. */
public final class RotaryAdditionalJEICategories {
    private RotaryAdditionalJEICategories() {}

    private abstract static class MachineCategory<T> implements IRecipeCategory<T> {
        private final RecipeType<T> type;
        private final Component title;
        private final IDrawable icon;

        MachineCategory(IGuiHelper gui, RecipeType<T> type, MachineRegistry machine, String titleKey) {
            this.type = type;
            this.title = Component.translatable(titleKey);
            this.icon = gui.createDrawableItemStack(machine.getCraftedProduct());
        }

        @Override public RecipeType<T> getRecipeType() { return type; }
        @Override public Component getTitle() { return title; }
        @Override public int getWidth() { return 108; }
        @Override public int getHeight() { return 36; }
        @Override public IDrawable getIcon() { return icon; }
    }

    public static final class LavaMaker extends MachineCategory<LavaMakerRecipe> {
        public static final RecipeType<LavaMakerRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "lava_maker", LavaMakerRecipe.class);

        public LavaMaker(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.LAVAMAKER, "machine.lavamaker"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, LavaMakerRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getInput())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.lava_requirements", recipe.getMeltTemperature(), recipe.getMeltEnergy())));
            var output = recipe.getFluid();
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9)
                    .addFluidStack(output.getFluid(), output.getAmount());
        }
    }

    public static final class Compactor extends MachineCategory<CompactorRecipe> {
        public static final RecipeType<CompactorRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "compactor", CompactorRecipe.class);

        public Compactor(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.COMPACTOR, "machine.compactor"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, CompactorRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getInput())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.compactor_input")));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9).addItemStack(recipe.getResult())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.compactor_requirements",
                            recipe.getReqPressure(), recipe.getReqTemperature())));
        }
    }

    public static final class Wetter extends MachineCategory<WetterRecipe> {
        public static final RecipeType<WetterRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "wetter", WetterRecipe.class);

        public Wetter(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.WETTER, "machine.wetter"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, WetterRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addIngredients(recipe.getInput());
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 9)
                    .addFluidStack(recipe.getFluid(), recipe.getAmount())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.wetter_duration", recipe.getDuration())));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9).addItemStack(recipe.getResult());
        }
    }

    public static final class DryingBed extends MachineCategory<DryingBedRecipe> {
        public static final RecipeType<DryingBedRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "drying_bed", DryingBedRecipe.class);

        public DryingBed(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.DRYING, "machine.drying"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, DryingBedRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                    .addFluidStack(recipe.getFluid(), recipe.getConsumption());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9).addItemStack(recipe.getResult());
        }
    }

    public static final class Crystallizer extends MachineCategory<CrystallizerRecipe> {
        public static final RecipeType<CrystallizerRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "crystallizer", CrystallizerRecipe.class);

        public Crystallizer(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.CRYSTALLIZER, "machine.crystal"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, CrystallizerRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                    .addFluidStack(recipe.getFluid(), recipe.getConsumption());
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9).addItemStack(recipe.getResult());
        }
    }

    public static final class Magnetizer extends MachineCategory<MagnetizerRecipe> {
        public static final RecipeType<MagnetizerRecipe> TYPE =
                RecipeType.create(RotaryCraft.MODID, "magnetizer", MagnetizerRecipe.class);

        public Magnetizer(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.MAGNETIZER, "machine.magnetizer"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, MagnetizerRecipe recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addItemStack(recipe.input.copy())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.magnetizer_input", recipe.minSpeed)));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9).addItemStack(recipe.input.copy())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.magnetizer_output", recipe.speedPerMicroTesla)));
        }
    }

    public record Composting(ItemStack input, int outputCount) {}

    public static final class Composter extends MachineCategory<Composting> {
        public static final RecipeType<Composting> TYPE =
                RecipeType.create(RotaryCraft.MODID, "composter", Composting.class);

        public Composter(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.COMPOSTER, "machine.composter"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, Composting recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addItemStack(recipe.input());
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 9)
                    .addItemStack(RotaryItems.YEAST.get().getDefaultInstance())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.composter_yeast")));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9)
                    .addItemStack(new ItemStack(RotaryItems.COMPOST.get(), recipe.outputCount()))
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.composter_temperature")));
        }
    }

    public record Cooling() {}

    public static final class Refrigerator extends MachineCategory<Cooling> {
        public static final RecipeType<Cooling> TYPE =
                RecipeType.create(RotaryCraft.MODID, "refrigerator", Cooling.class);

        public Refrigerator(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.REFRIGERATOR, "machine.refrigerator"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, Cooling recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9).addItemStack(new ItemStack(Blocks.ICE));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 57, 1)
                    .addFluidStack(RotaryFluids.LIQUID_NITROGEN.get(), 100)
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.refrigerator_yield")));
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 19)
                    .addItemStack(RotaryItems.DRY_ICE.get().getDefaultInstance())
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            "jei.rotarycraft.refrigerator_byproduct")));
        }
    }

    public record ObsidianMix(boolean obsidian) {}

    public static final class ObsidianMaker extends MachineCategory<ObsidianMix> {
        public static final RecipeType<ObsidianMix> TYPE =
                RecipeType.create(RotaryCraft.MODID, "obsidian_maker", ObsidianMix.class);

        public ObsidianMaker(IGuiHelper gui) { super(gui, TYPE, MachineRegistry.OBSIDIAN, "machine.obsidian"); }

        @Override
        public void setRecipe(IRecipeLayoutBuilder builder, ObsidianMix recipe, IFocusGroup focuses) {
            builder.addSlot(RecipeIngredientRole.INPUT, 1, 9)
                    .addFluidStack(Fluids.WATER, recipe.obsidian() ? 2500 : 1000);
            builder.addSlot(RecipeIngredientRole.INPUT, 28, 9)
                    .addFluidStack(Fluids.LAVA, recipe.obsidian() ? 1000 : 50)
                    .addRichTooltipCallback((view, tooltip) -> {
                        if (!recipe.obsidian())
                            tooltip.add(Component.translatable("jei.rotarycraft.cobblestone_lava"));
                    });
            builder.addSlot(RecipeIngredientRole.OUTPUT, 77, 9)
                    .addItemStack(new ItemStack(recipe.obsidian() ? Blocks.OBSIDIAN : Blocks.COBBLESTONE))
                    .addRichTooltipCallback((view, tooltip) -> tooltip.add(Component.translatable(
                            recipe.obsidian() ? "jei.rotarycraft.obsidian_temperature" : "jei.rotarycraft.cobblestone_temperature")));
        }
    }
}
