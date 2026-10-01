package reika.rotarycraft;

import com.mojang.serialization.JsonOps;
import io.netty.buffer.Unpooled;
import java.util.*;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.core.registries.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.*;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.FurnaceBlockEntity;
import net.minecraft.world.level.storage.TagValueInput;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.rotarycraft.auxiliary.recipemanagers.*;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityExtractor;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.registry.*;

/** Full powered extraction + real furnace conversion for every original modded family and custom datapack families. */
final class RotaryModExtractorTests {
    private static final BlockPos POS = new BlockPos(3, 2, 4), FURNACE = new BlockPos(6, 2, 4);
    private RotaryModExtractorTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        for (var ore : ModExtractOres.values()) test(event, env, "family_" + ore.path, h -> family(h, ore));
        for (var ore : List.of(ModExtractOres.FORCE, ModExtractOres.MIMICHITE, ModExtractOres.ESSENCE))
            test(event, env, "nether_variant_" + ore.path, h -> netherVariant(h, ore));
        test(event, env, "custom_alpha", h -> custom(h, "alpha", Blocks.TUFF.asItem(), Items.EMERALD, 2));
        test(event, env, "custom_beta", h -> custom(h, "beta", Blocks.DIORITE.asItem(), Items.IRON_NUGGET, 3));
        test(event, env, "custom_identity_and_save", RotaryModExtractorTests::identity);
        test(event, env, "richer_overlap_priority", RotaryModExtractorTests::priority);
        test(event, env, "recipe_codecs_and_bonuses", RotaryModExtractorTests::codec);
        test(event, env, "tagged_smelting_external", RotaryModExtractorTests::taggedExternal);
        test(event, env, "tagged_smelting_fallback", RotaryModExtractorTests::taggedFallback);
        test(event, env, "bonus_space_and_consumption", RotaryModExtractorTests::bonusSpace);
        test(event, env, "primary_count_space", RotaryModExtractorTests::primarySpace);
        test(event, env, "water_requirement", RotaryModExtractorTests::water);
        test(event, env, "bedrock_ore_rates", RotaryModExtractorTests::bedrock);
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event, env, "extractor_mod_" + name, 300, body);
    }
    private static BlockEntityExtractor powered(GameTestHelper h) {
        return powered(h, 16000);
    }
    private static BlockEntityExtractor powered(GameTestHelper h, int water) {
        h.setBlock(POS, RotaryBlocks.EXTRACTOR.get());
        h.setBlock(POS.below().north(), Blocks.REDSTONE_BLOCK);
        h.setBlock(POS.below(), RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.DOWN));
        var coil = h.getBlockEntity(POS.below(), BlockEntityCreativeCoil.class);
        coil.setReleaseTorque(512); coil.setReleaseOmega(8192); coil.updateEntity(h.getLevel(), coil.getBlockPos());
        var tile = h.getBlockEntity(POS, BlockEntityExtractor.class);
        tile.addLiquid(water); tile.setInventorySlotContents(9, new ItemStack(RotaryItems.HSLA_DRILL.get()));
        tile.updateEntity(h.getLevel(), tile.getBlockPos());
        return tile;
    }
    private static void chain(GameTestHelper h, BlockEntityExtractor tile, Item input) {
        h.assertTrue(tile.isItemValidForSlot(0, new ItemStack(input)), "tagged source must enter the real extractor slot");
        tile.setInventorySlotContents(0, new ItemStack(input));
        for (int tick = 0; tick < 64; tick++) {
            for (int stage = 0; stage < 4; stage++) tile.setCookTime(stage, tile.getOperationTime(stage) - 1);
            tile.updateEntity(h.getLevel(), tile.getBlockPos());
        }
        for (int slot = 0; slot < 7; slot++) h.assertTrue(tile.getStackInSlot(slot).isEmpty(), "complete chain must consume every input/intermediate: slot " + slot);
    }
    private static void furnace(GameTestHelper h, ItemStack flakes, ItemStack expected) {
        h.setBlock(FURNACE, Blocks.FURNACE); var furnace = h.getBlockEntity(FURNACE, FurnaceBlockEntity.class);
        furnace.setItem(0, flakes.copyWithCount(1)); furnace.setItem(1, new ItemStack(Items.COAL));
        h.runAfterDelay(210, () -> {
            h.assertTrue(furnace.getItem(0).isEmpty() && ItemStack.matches(furnace.getItem(2), expected), "real furnace must produce the full tagged/fallback output count");
            h.succeed();
        });
    }
    private static void family(GameTestHelper h, ModExtractOres ore) {
        var tile = powered(h);
        Item source = ore == ModExtractOres.COPPER ? Blocks.PURPUR_BLOCK.asItem()
                : BuiltInRegistries.ITEM.getTagOrEmpty(ore.inputTag()).iterator().next().value();
        for (int stage = 0; stage < 4; stage++) {
            var input = new ItemStack(stage == 0 ? source : ore.stage(stage - 1));
            var recipe = tile.getExtractionRecipe(stage, input);
            h.assertTrue(recipe != null && recipe.getOutput().is(ore.stage(stage)), "all four loaded recipes must preserve the family " + ore + " at " + stage);
            if (ore != ModExtractOres.COPPER)
                h.assertTrue(recipe.getOreDuplicationChance().orElseThrow() == ore.duplicationChance(), "rare/Nether chance must persist at every stage");
            h.assertTrue(tile.isItemValidForSlot(stage, input), "GUI and automation must accept the correct family stage");
        }
        chain(h, tile, source);
        var flakes = tile.getStackInSlot(7);
        h.assertTrue(flakes.is(ore.stage(3)) && flakes.getCount() >= 1 && flakes.getCount() <= 16, "one ore must produce flakes after exactly four multiplication stages");
        var smelt = h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.SMELTING).stream()
                .filter(r -> r.value().matches(new SingleRecipeInput(flakes), h.getLevel())).findFirst().orElseThrow().value();
        var expected = smelt.assemble(new SingleRecipeInput(flakes));
        h.assertTrue(expected.getCount() == ore.dropCount, "smelting must keep V33a's per-family drop count");
        if (ore != ModExtractOres.COPPER)
            h.assertTrue(smelt.experience() == ore.experience() && smelt.cookingTime() == 200, "V33a rarity XP and real furnace timing");
        furnace(h, flakes, expected);
    }
    private static ExtractorRecipe customRecipe(GameTestHelper h, String family, int stage) {
        return (ExtractorRecipe)h.getLevel().getServer().getRecipeManager().byKey(ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath("rotarycraft", "extractor/test_" + family + "_stage_" + stage))).orElseThrow().value();
    }
    private static void custom(GameTestHelper h, String family, Item source, Item product, int count) {
        var tile = powered(h); chain(h, tile, source); var flakes = tile.getStackInSlot(7);
        h.assertTrue(flakes.is(RotaryItems.CUSTOM_ORE_FLAKES.get()) && flakes.getCount() == 1
                && flakes.get(RotaryDataComponents.EXTRACT_FAMILY.get()).equals(Identifier.fromNamespaceAndPath("rotarycraft", family)), "custom identity must survive the entire zero-duplication chain");
        h.assertTrue(tile.getStackInSlot(8).is(Items.GUNPOWDER) && tile.getStackInSlot(8).getCount() == 2, "unavailable tagged bonus must fall through to the first available alternative");
        furnace(h, flakes, new ItemStack(product, count));
    }
    private static void identity(GameTestHelper h) {
        var tile = powered(h); var alpha = customRecipe(h, "alpha", 0).getOutput(); var beta = customRecipe(h, "beta", 0).getOutput();
        h.assertTrue(tile.isItemValidForSlot(1, alpha) && tile.isItemValidForSlot(1, beta) && !tile.isItemValidForSlot(2, alpha), "custom stage validation must use synced recipes");
        h.assertTrue(!customRecipe(h, "alpha", 1).matches(1, beta) && !ItemStack.isSameItemSameComponents(alpha, beta), "custom families must not mix");
        tile.setInventorySlotContents(1, alpha.copyWithCount(10)); tile.setInventorySlotContents(4, beta.copyWithCount(5));
        tile.setCookTime(1, 0); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(1).getCount() == 10 && tile.getStackInSlot(4).getCount() == 5, "throughput must not merge different component families");
        var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess());
        var restored = new BlockEntityExtractor(tile.getBlockPos(), tile.getBlockState());
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(ItemStack.matches(restored.getStackInSlot(1), alpha.copyWithCount(10))
                && ItemStack.matches(restored.getStackInSlot(4), beta.copyWithCount(5)), "save/load must preserve names, tint and family components"); h.succeed();
    }
    private static void priority(GameTestHelper h) {
        var tile = powered(h); var ore = ModExtractOres.NETHERIRON;
        var source = new ItemStack(BuiltInRegistries.ITEM.getTagOrEmpty(ore.inputTag()).iterator().next());
        h.assertTrue(source.is(net.neoforged.neoforge.common.Tags.Items.ORES_IRON), "fixture must overlap vanilla iron and Nether iron tags");
        var recipe = tile.getExtractionRecipe(0, source);
        h.assertTrue(recipe.getPriority() == 20 && recipe.getOutput().is(ore.stage(0)) && recipe.getOreDuplicationChance().orElseThrow() == .8,
                "specific Nether recipe must win over generic iron regardless of registry ordering"); h.succeed();
    }
    private static void codec(GameTestHelper h) {
        var recipe = customRecipe(h, "alpha", 3); var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), h.getLevel().registryAccess());
        try {
            ExtractorRecipe.STREAM_CODEC.encode(buf, recipe); var decoded = ExtractorRecipe.STREAM_CODEC.decode(buf);
            h.assertTrue(decoded.getPriority() == 100 && decoded.getBonuses().size() == 2 && decoded.getDuplicationChance().orElseThrow() == 0
                    && decoded.getAvailableBonus().orElseThrow().output().create().is(Items.GUNPOWDER)
                    && ItemStack.matches(decoded.getOutput(), recipe.getOutput()), "network sync must preserve custom components and ordered gated bonuses");
            var ops = h.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE);
            var json = ExtractorRecipe.CODEC.codec().encodeStart(ops, recipe).getOrThrow();
            var fromJson = ExtractorRecipe.CODEC.codec().parse(ops, json).getOrThrow();
            h.assertTrue(fromJson.getBonuses().equals(recipe.getBonuses()) && ItemStack.matches(fromJson.getOutput(), recipe.getOutput()), "JSON round-trip must retain custom recipe data");
        } finally { buf.release(); }
        h.succeed();
    }
    private static TaggedSmeltingRecipe tagged(ModExtractOres ore) {
        return new TaggedSmeltingRecipe(new Recipe.CommonInfo(true), new AbstractCookingRecipe.CookingBookInfo(CookingBookCategory.MISC, ""),
                Ingredient.of(ore.stage(3)), new ItemStackTemplate(ore.product(), ore.dropCount), ore.experience(), 200, ModExtractOres.common(ore.productTag));
    }
    private static void taggedExternal(GameTestHelper h) {
        var recipe = tagged(ModExtractOres.TIN); var input = new SingleRecipeInput(new ItemStack(ModExtractOres.TIN.stage(3)));
        h.assertTrue(recipe.assemble(input).is(Items.IRON_NUGGET), "furnace output must use installed tagged products rather than creating a second incompatible material");
        var buf = new RegistryFriendlyByteBuf(Unpooled.buffer(), h.getLevel().registryAccess());
        try {
            TaggedSmeltingRecipe.STREAM_CODEC.encode(buf, recipe);
            var copy = (TaggedSmeltingRecipe)TaggedSmeltingRecipe.STREAM_CODEC.decode(buf);
            h.assertTrue(copy.fallback().item().equals(recipe.fallback().item()) && copy.resultTag().equals(recipe.resultTag()) && copy.assemble(input).is(Items.IRON_NUGGET), "tagged smelting sync must retain fallback and resolve tagged output");
        } finally { buf.release(); }
        furnace(h, input.item(), new ItemStack(Items.IRON_NUGGET));
    }
    private static void taggedFallback(GameTestHelper h) {
        var ore = ModExtractOres.DILITHIUM; var recipe = tagged(ore);
        var output = recipe.assemble(new SingleRecipeInput(new ItemStack(ore.stage(3))));
        h.assertTrue(output.is(ore.product()) && output.getCount() == ore.dropCount, "when no other mod publishes a product, the original RotaryCraft tagged fallback remains usable");
        furnace(h, new ItemStack(ore.stage(3)), output);
    }
    private static void bonusSpace(GameTestHelper h) {
        var tile = powered(h); var recipe = customRecipe(h, "alpha", 3); var solution = customRecipe(h, "alpha", 2).getOutput();
        tile.setInventorySlotContents(3, solution); tile.setInventorySlotContents(8, new ItemStack(Items.GUNPOWDER, 63));
        tile.setCookTime(3, tile.getOperationTime(3) - 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(3).getCount() == 1 && tile.getStackInSlot(7).isEmpty() && tile.getStackInSlot(8).getCount() == 63,
                "must wait when the entire two-item secondary output cannot fit");
        tile.setInventorySlotContents(8, new ItemStack(Items.GUNPOWDER, 62)); tile.setCookTime(3, tile.getOperationTime(3) - 1);
        tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(3).isEmpty() && tile.getStackInSlot(8).getCount() == 64 && ItemStack.matches(tile.getStackInSlot(7), recipe.getOutput()), "exact-fit secondary products must persist without lost input or output"); h.succeed();
    }
    private static void water(GameTestHelper h) {
        var tile = powered(h, 124);
        tile.setInventorySlotContents(1, new ItemStack(ModExtractOres.TIN.stage(0))); tile.setCookTime(1, tile.getOperationTime(1) - 1);
        tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(1).getCount() == 1 && tile.getStackInSlot(5).isEmpty() && tile.getLiquidLevel() == 124, "modded washing requires the full 125 mB"); h.succeed();
    }
    private static void primarySpace(GameTestHelper h) {
        var tile = powered(h); tile.upgrade();
        tile.setInventorySlotContents(0, new ItemStack(Blocks.PRISMARINE));
        tile.setInventorySlotContents(4, new ItemStack(ModExtractOres.TIN.stage(0), 63));
        // Occupy the next stage so throughPut cannot free room in the first-stage output.
        tile.setInventorySlotContents(1, new ItemStack(ModExtractOres.LEAD.stage(0), 64));
        tile.setCookTime(0, tile.getOperationTime(0) - 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(0).getCount() == 1 && tile.getStackInSlot(4).getCount() == 63, "counted primary output must wait when two items do not fit");
        tile.setInventorySlotContents(4, new ItemStack(ModExtractOres.TIN.stage(0), 62));
        tile.setCookTime(0, tile.getOperationTime(0) - 1); tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(0).isEmpty() && tile.getStackInSlot(4).getCount() == 64,
                "explicit zero duplication must allow exact-fit counted outputs even with the bedrock drill"); h.succeed();
    }
    private static void bedrock(GameTestHelper h) {
        var tile = powered(h); tile.upgrade(); var ore = ModExtractOres.PLATINUM;
        var source = BuiltInRegistries.ITEM.getTagOrEmpty(ore.inputTag()).iterator().next().value();
        tile.setInventorySlotContents(0, new ItemStack(source)); tile.setCookTime(0, tile.getOperationTime(0) - 1);
        tile.updateEntity(h.getLevel(), tile.getBlockPos());
        h.assertTrue(tile.getStackInSlot(0).isEmpty() && tile.getStackInSlot(4).is(ore.stage(0)) && tile.getStackInSlot(4).getCount() == 2,
                "modded ore retains guaranteed first-stage bedrock doubling");
        h.assertTrue(tile.getExtractionRecipe(3, new ItemStack(ore.stage(2))).getOreDuplicationChance().orElseThrow() == .9,
                "rare ore's later stages keep original ninety-percent duplication"); h.succeed();
    }
    private static void netherVariant(GameTestHelper h, ModExtractOres ore) {
        var tile = powered(h); var source = BuiltInRegistries.ITEM.getTagOrEmpty(ModExtractOres.common("ores/nether_" + ore.path)).iterator().next().value();
        var first = tile.getExtractionRecipe(0, new ItemStack(source));
        h.assertTrue(first.getOutput().is(ore.stage(0)) && first.getOreDuplicationChance().orElseThrow() == .8, "legacy Nether variant must have richer crushing");
        for (int stage = 1; stage < 4; stage++)
            h.assertTrue(tile.getExtractionRecipe(stage, new ItemStack(ore.stage(stage - 1))).getOreDuplicationChance().orElseThrow() == .5, "legacy variant must rejoin ordinary stages after crushing");
        chain(h, tile, source); h.assertTrue(tile.getStackInSlot(7).is(ore.stage(3)), "Nether variant must finish into its ordinary family flakes"); h.succeed();
    }
}
