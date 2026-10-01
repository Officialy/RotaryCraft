package reika.rotarycraft;

import com.google.gson.JsonObject;
import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.core.registries.Registries;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.rotarycraft.auxiliary.recipemanagers.ExtractorRecipe;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityExtractor;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.registry.*;

/** Raw-iron provenance/rates, legacy ore recipes, and real multi-output furnace conversions. */
final class RotaryExtractorBalanceTests {
    private static final BlockPos POS = new BlockPos(3, 2, 4);
    private RotaryExtractorBalanceTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        test(event, env, "rates_and_fortune_balance", RotaryExtractorBalanceTests::rates);
        test(event, env, "raw_input_and_stage_slots", RotaryExtractorBalanceTests::slots);
        test(event, env, "legacy_ore_rates", RotaryExtractorBalanceTests::legacy);
        test(event, env, "recipe_network_and_json", RotaryExtractorBalanceTests::codec);
        for (int stage = 0; stage < 4; stage++) {
            int index = stage;
            test(event, env, "raw_stage_" + stage, h -> stage(h, index));
        }
        test(event, env, "bedrock_raw_rate", RotaryExtractorBalanceTests::bedrock);
        test(event, env, "raw_save_load_provenance", RotaryExtractorBalanceTests::save);
        test(event, env, "interstage_merge_conservation", RotaryExtractorBalanceTests::transfer);
        test(event, env, "stacked_output_consumption", RotaryExtractorBalanceTests::merge);
        test(event, env, "raw_complete_chain", RotaryExtractorBalanceTests::chain);
        test(event, env, "ore_input_consumption", RotaryExtractorBalanceTests::ore);
        test(event, env, "lapis_recipe_count", h -> recipe(h, true));
        test(event, env, "redstone_recipe_count", h -> recipe(h, false));
        test(event, env, "lapis_furnace", h -> furnace(h, true, false));
        test(event, env, "redstone_furnace", h -> furnace(h, false, false));
        test(event, env, "lapis_furnace_backpressure", h -> furnace(h, true, true));
        test(event, env, "redstone_furnace_backpressure", h -> furnace(h, false, true));
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event, env, "extractor_balance_" + name, 260, body);
    }
    private static ExtractorRecipe raw(GameTestHelper h, int stage) {
        return (ExtractorRecipe)h.getLevel().getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath("rotarycraft", "extractor/raw_iron_stage_" + stage))).orElseThrow().value();
    }
    private static List<Item> inputs() { return List.of(Items.RAW_IRON, RotaryItems.RAW_IRON_DUST.get(), RotaryItems.RAW_IRON_SLURRY.get(), RotaryItems.RAW_IRON_SOLUTION.get()); }
    private static List<Item> outputs() { return List.of(RotaryItems.RAW_IRON_DUST.get(), RotaryItems.RAW_IRON_SLURRY.get(), RotaryItems.RAW_IRON_SOLUTION.get(), RotaryItems.IRON_FLAKES.get()); }
    private static BlockEntityExtractor extractor(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.EXTRACTOR.get()); return h.getBlockEntity(POS, BlockEntityExtractor.class);
    }
    private static BlockEntityExtractor powered(GameTestHelper h) {
        var tile = extractor(h); h.setBlock(POS.below().north(), Blocks.REDSTONE_BLOCK);
        h.setBlock(POS.below(), RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.DOWN));
        var coil = h.getBlockEntity(POS.below(), BlockEntityCreativeCoil.class); coil.setReleaseTorque(512); coil.setReleaseOmega(8192);
        coil.updateEntity(h.getLevel(), coil.getBlockPos()); tile.addLiquid(16000);
        tile.setInventorySlotContents(9, new ItemStack(RotaryItems.HSLA_DRILL.get())); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.power == 4194304 && tile.omega == 8192 && tile.torque == 512, "real bottom coil must power all four stages"); return tile;
    }
    private static void operation(GameTestHelper h, BlockEntityExtractor tile, int stage, Item input) {
        for (int slot = 0; slot < 9; slot++) tile.setInventorySlotContents(slot, ItemStack.EMPTY);
        tile.setInventorySlotContents(stage, new ItemStack(input)); tile.setCookTime(stage, tile.getOperationTime(stage) - 1);
        tile.updateEntity(h.getLevel(), tile.getBlockPos());
    }
    private static void rates(GameTestHelper h) {
        double rawYield = 1;
        for (int stage = 0; stage < 4; stage++) {
            var recipe = raw(h, stage);
            h.assertTrue(recipe.matches(stage, new ItemStack(inputs().get(stage))) && recipe.getOutput().is(outputs().get(stage)), "all four raw stages must preserve the separate chain");
            double chance = recipe.getDuplicationChance().orElseThrow();
            h.assertTrue(chance == .23, "each raw stage must carry the authored 23% duplication rate"); rawYield *= 1 + chance;
        }
        // Actual ore_drops outcomes: max(0, nextInt(fortune + 2) - 1) + 1.
        for (int fortune = 0; fortune <= 3; fortune++) {
            double drops = fortune == 0 ? 1 : java.util.stream.IntStream.range(0, fortune + 2).map(x -> Math.max(0, x - 1) + 1).average().orElseThrow();
            h.assertTrue(drops * rawYield <= Math.pow(1.5, 4) && drops * rawYield <= 2 * Math.pow(1.5, 3), "ore must remain at least as rewarding on average through Fortune " + fortune + " with either drill");
        }
        h.assertTrue(Math.abs(rawYield * 2.2 - 5.035506102) < 1e-9, "Fortune III raw route must average 5.035506102 ingots per original ore"); h.succeed();
    }
    private static void slots(GameTestHelper h) {
        var tile = extractor(h);
        h.assertTrue(tile.isItemValidForSlot(0, new ItemStack(Items.RAW_IRON)) && tile.isItemValidForSlot(0, new ItemStack(Blocks.IRON_ORE))
                && !tile.isItemValidForSlot(0, new ItemStack(Items.RAW_GOLD)), "raw iron must be valid without accidentally enabling raw gold");
        for (int stage = 1; stage < 4; stage++) {
            h.assertTrue(tile.isItemValidForSlot(stage, new ItemStack(inputs().get(stage))), "raw intermediate must enter its correct GUI/automation slot");
            Item oreIntermediate = ExtractOres.IRON.getStageItem(stage - 1);
            h.assertTrue(!raw(h, stage).matches(stage, new ItemStack(oreIntermediate)), "ore intermediate cannot switch into raw recipes");
            var regular = (ExtractorRecipe)h.getLevel().getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE,
                    Identifier.fromNamespaceAndPath("rotarycraft", "extractor/iron_" + new String[]{"", "dust_to_slurry", "slurry_to_solution", "solution_to_flakes"}[stage]))).orElseThrow().value();
            h.assertTrue(!regular.matches(stage, new ItemStack(inputs().get(stage))), "raw intermediate cannot switch into 50% ore recipes");
        } h.succeed();
    }
    private static void legacy(GameTestHelper h) {
        for (var holder : h.getLevel().getServer().getRecipeManager().recipeMap().byType(RotaryRecipeTypes.EXTRACTOR.get()))
            if (!holder.id().identifier().getPath().startsWith("extractor/raw_iron_"))
                h.assertTrue(holder.value().getDuplicationChance().isEmpty(), "existing ore recipes must keep normal/rare/Nether/bedrock behavior");
        h.succeed();
    }
    private static void codec(GameTestHelper h) {
        var recipe = raw(h, 2); var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), h.getLevel().registryAccess());
        try {
            ExtractorRecipe.STREAM_CODEC.encode(buf, recipe); var decoded = ExtractorRecipe.STREAM_CODEC.decode(buf);
            h.assertTrue(decoded.getDuplicationChance().equals(recipe.getDuplicationChance()) && decoded.matches(2, new ItemStack(inputs().get(2))) && decoded.getOutput().is(outputs().get(2)), "JEI network sync must retain input, output, stage and lower rate");
        } finally { buf.release(); }
        var ops = h.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE);
        JsonObject json = ExtractorRecipe.CODEC.codec().encodeStart(ops, recipe).getOrThrow().getAsJsonObject();
        h.assertTrue(ExtractorRecipe.CODEC.codec().parse(ops, json).getOrThrow().getDuplicationChance().orElseThrow() == .23, "recipe JSON must round-trip the lower rate");
        json.addProperty("duplication_chance", 2); h.assertTrue(ExtractorRecipe.CODEC.codec().parse(ops, json).error().isPresent(), "datapack rates above 100% must be rejected");
        json.remove("duplication_chance"); h.assertTrue(ExtractorRecipe.CODEC.codec().parse(ops, json).getOrThrow().getDuplicationChance().isEmpty(), "old datapacks without a rate must remain readable"); h.succeed();
    }
    private static void stage(GameTestHelper h, int stage) {
        var tile = powered(h); int water = tile.getLiquidLevel(); operation(h, tile, stage, inputs().get(stage));
        var out = tile.getStackInSlot(stage + 4);
        h.assertTrue(tile.getStackInSlot(stage).isEmpty() && out.is(outputs().get(stage)) && out.getCount() >= 1 && out.getCount() <= 2, "real raw stage must consume one input and produce one or two correct intermediates: input=" + tile.getStackInSlot(stage) + ", output=" + out + ", cook=" + tile.getCookTime(stage) + ", time=" + tile.getOperationTime(stage) + ", operations=" + tile.getNumberConsecutiveOperations(stage));
        h.assertTrue(tile.getLiquidLevel() == water - (stage == 1 || stage == 2 ? 125 : 0), "raw route must retain original water cost");
        if (stage == 3) h.assertTrue(ExtractorBonus.getBonusForIngredient(new ItemStack(inputs().get(3))) == ExtractorBonus.IRON, "raw solution must retain tungsten bonus eligibility"); h.succeed();
    }
    private static void bedrock(GameTestHelper h) {
        var tile = powered(h); tile.upgrade(); boolean single = false;
        // Seeing no single result in 64 raw operations has probability 0.23^64 (< 1e-40).
        for (int n = 0; n < 64; n++) { operation(h, tile, 0, Items.RAW_IRON); single |= tile.getStackInSlot(4).getCount() == 1; }
        h.assertTrue(single, "bedrock drill must not replace raw iron's 23% rate with guaranteed doubling");
        operation(h, tile, 0, Blocks.IRON_ORE.asItem()); h.assertTrue(tile.getStackInSlot(4).is(RotaryItems.IRON_DUST.get()) && tile.getStackInSlot(4).getCount() == 2, "ore keeps its guaranteed bedrock crushing bonus"); h.succeed();
    }
    private static void save(GameTestHelper h) {
        var tile = powered(h);
        for (int stage = 1; stage < 4; stage++) tile.setInventorySlotContents(stage, new ItemStack(inputs().get(stage), stage + 1));
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess()); var restored = new BlockEntityExtractor(tile.getBlockPos(), tile.getBlockState());
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        for (int stage = 1; stage < 4; stage++) h.assertTrue(ItemStack.matches(restored.getStackInSlot(stage), tile.getStackInSlot(stage)) && raw(h, stage).matches(stage, restored.getStackInSlot(stage)), "saved inventory must preserve lower-yield provenance"); h.succeed();
    }
    private static void transfer(GameTestHelper h) {
        var tile = extractor(h); tile.setInventorySlotContents(1, new ItemStack(RotaryItems.RAW_IRON_DUST.get(), 10));
        tile.setInventorySlotContents(4, new ItemStack(RotaryItems.RAW_IRON_DUST.get(), 5)); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(1).getCount() == 11 && tile.getStackInSlot(4).getCount() == 4, "interstage merge must write destination count and conserve all 15 items");
        tile.setInventorySlotContents(4, new ItemStack(RotaryItems.IRON_DUST.get(), 5)); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(1).getCount() == 11 && tile.getStackInSlot(4).getCount() == 5, "raw and ore dust must not merge into each other"); h.succeed();
    }
    private static void merge(GameTestHelper h) {
        var tile = powered(h); tile.setInventorySlotContents(3, new ItemStack(RotaryItems.RAW_IRON_SOLUTION.get()));
        tile.setInventorySlotContents(7, new ItemStack(RotaryItems.IRON_FLAKES.get(), 10)); tile.setCookTime(3, tile.getOperationTime(3) - 1);
        tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(3).isEmpty() && tile.getStackInSlot(7).getCount() >= 11 && tile.getStackInSlot(7).getCount() <= 12, "existing output stack must gain the real processed result and input must be consumed"); h.succeed();
    }
    private static void chain(GameTestHelper h) {
        var tile = powered(h); tile.setInventorySlotContents(0, new ItemStack(Items.RAW_IRON, 2));
        for (int tick = 0; tick < 64; tick++) {
            for (int stage = 0; stage < 4; stage++) tile.setCookTime(stage, tile.getOperationTime(stage) - 1);
            tile.updateEntity(h.getLevel(), tile.getBlockPos());
        }
        for (int slot = 0; slot < 7; slot++) h.assertTrue(tile.getStackInSlot(slot).isEmpty(), "completed raw chain must have no unconsumed inputs or intermediate stacks in slot " + slot);
        h.assertTrue(tile.getStackInSlot(7).is(RotaryItems.IRON_FLAKES.get()) && tile.getStackInSlot(7).getCount() >= 2 && tile.getStackInSlot(7).getCount() <= 32, "two raw iron must complete into two to 32 flakes without duplication beyond the four recipe rolls"); h.succeed();
    }
    private static void ore(GameTestHelper h) {
        var tile = powered(h); operation(h, tile, 0, Blocks.IRON_ORE.asItem());
        h.assertTrue(tile.getStackInSlot(0).isEmpty() && tile.getStackInSlot(4).is(RotaryItems.IRON_DUST.get()), "legacy ore processing must consume its real source item too"); h.succeed();
    }
    private static Item flake(boolean lapis) { return lapis ? RotaryItems.LAPIS_FLAKES.get() : RotaryItems.REDSTONE_FLAKES.get(); }
    private static Item product(boolean lapis) { return lapis ? Items.LAPIS_LAZULI : Items.REDSTONE; }
    private static void recipe(GameTestHelper h, boolean lapis) {
        var input = new SingleRecipeInput(new ItemStack(flake(lapis)));
        var recipe = h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.SMELTING).stream().filter(r -> r.value().matches(input, h.getLevel())).findFirst().orElseThrow().value();
        var out = recipe.assemble(input);
        h.assertTrue(out.is(product(lapis)) && out.getCount() == (lapis ? 6 : 4) && recipe.experience() == (lapis ? .6F : .5F) && recipe.cookingTime() == 200, "original flake count, XP and 200-tick furnace recipe must be restored"); h.succeed();
    }
    private static void furnace(GameTestHelper h, boolean lapis, boolean blocked) {
        h.setBlock(POS, Blocks.FURNACE); var furnace = h.getBlockEntity(POS, FurnaceBlockEntity.class);
        furnace.setItem(0, new ItemStack(flake(lapis))); furnace.setItem(1, new ItemStack(Items.COAL));
        if (blocked) furnace.setItem(2, new ItemStack(product(lapis), 65 - (lapis ? 6 : 4)));
        h.runAfterDelay(210, () -> {
            if (blocked) h.assertTrue(furnace.getItem(0).is(flake(lapis)) && furnace.getItem(2).getCount() == 65 - (lapis ? 6 : 4), "furnace must wait until the entire multi-item output fits");
            else h.assertTrue(furnace.getItem(0).isEmpty() && furnace.getItem(2).is(product(lapis)) && furnace.getItem(2).getCount() == (lapis ? 6 : 4), "actual furnace must smelt one flake into its full restored output"); h.succeed();
        });
    }
}
