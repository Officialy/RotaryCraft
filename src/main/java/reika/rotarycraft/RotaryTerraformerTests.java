package reika.rotarycraft;

import java.util.*;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.QuartPos;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.server.commands.FillBiomeCommand;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.biome.Biome;
import net.minecraft.world.level.biome.Biomes;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.rotarycraft.auxiliary.TerraformerTerrain;
import reika.rotarycraft.auxiliary.recipemanagers.TerraformingRecipe;
import reika.rotarycraft.blockentities.level.BlockEntityTerraformer;
import reika.rotarycraft.gui.container.machine.inventory.ContainerTerraformer;
import reika.rotarycraft.registry.*;

/** Exercises biome storage, real coil power, resources, survival interactions and the original transformation graph. */
final class RotaryTerraformerTests {
    private static final BlockPos MACHINE = RotaryPowerTests.at(3, 4);
    private static final BlockPos SELECTED = new BlockPos(5, 1, 5);
    private static final String[] STEPS = {
        "desert_to_savanna",
        "savanna_to_plains",
        "plains_to_forest",
        "forest_to_jungle",
        "plains_to_swamp",
        "swamp_to_ocean",
        "ocean_to_frozen_ocean",
        "plains_to_windswept_hills",
        "plains_to_snowy_plains",
        "snowy_plains_to_plains",
        "ocean_to_mushroom_fields",
        "mushroom_fields_to_windswept_hills",
        "forest_to_taiga",
        "forest_to_snowy_taiga",
        "forest_to_dark_forest",
        "forest_to_birch_forest",
        "taiga_to_snowy_taiga",
        "taiga_to_snowy_plains",
        "taiga_to_forest",
        "taiga_to_old_growth_pine_taiga",
        "snowy_plains_to_frozen_ocean",
        "plains_to_savanna",
        "savanna_to_desert",
        "forest_to_plains",
        "jungle_to_forest",
        "swamp_to_plains",
        "ocean_to_swamp",
        "frozen_ocean_to_ocean",
        "windswept_hills_to_plains",
        "snowy_plains_to_taiga",
        "frozen_ocean_to_snowy_plains",
        "desert_to_badlands",
        "ocean_to_deep_ocean",
        "badlands_to_desert",
        "dark_forest_to_forest",
        "birch_forest_to_forest"
    };
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        for (String step : STEPS) RotaryGameTests.register(event, environment, "terraformer_" + step, 100, h -> conversion(h, step));
        RotaryGameTests.register(event, environment, "terraformer_redstone_power_water_gates", 120, RotaryTerraformerTests::gates);
        RotaryGameTests.register(event, environment, "terraformer_item_batch_atomicity", 100, RotaryTerraformerTests::itemGate);
        RotaryGameTests.register(event, environment, "terraformer_selector_and_deduplication", 60, RotaryTerraformerTests::selector);
        RotaryGameTests.register(event, environment, "terraformer_save_reload", 60, RotaryTerraformerTests::saveReload);
        RotaryGameTests.register(event, environment, "terraformer_inventory_and_fluid_transactions", 60, RotaryTerraformerTests::transactions);
        RotaryGameTests.register(event, environment, "terraformer_menu_validation_and_shift_click", 60, RotaryTerraformerTests::menu);
        RotaryGameTests.register(event, environment, "terraformer_soil_with_diamond", 100, h -> soil(h, true));
        RotaryGameTests.register(event, environment, "terraformer_soil_without_diamond", 100, h -> soil(h, false));
        RotaryGameTests.register(event, environment, "terraformer_feature_generation_bounds", 60, RotaryTerraformerTests::features);
        RotaryGameTests.register(event, environment, "terraformer_unloaded_and_invalid_target", 100, RotaryTerraformerTests::unloaded);
        RotaryGameTests.register(event, environment, "terraformer_survival_harvest", 60, RotaryTerraformerTests::harvest);
        RotaryGameTests.register(event, environment, "terraformer_recipe_disk_and_network_roundtrip", 60, RotaryTerraformerTests::recipeRoundTrip);
        RotaryGameTests.register(event, environment, "terraformer_child_biomes", 100, RotaryTerraformerTests::children);
        RotaryGameTests.register(event, environment, "terraformer_area_marker_import", 60, RotaryTerraformerTests::areaMarker);
        RotaryGameTests.register(event, environment, "terraformer_climate_without_diamond", 100, RotaryTerraformerTests::climate);
    }
    private static TerraformingRecipe recipe(GameTestHelper h, String step) {
        return BlockEntityTerraformer.getTransformList(h.getLevel()).stream()
                .filter(r -> (r.source().location().getPath().substring("terraformer/".length()) + "_to_" + r.target().identifier().getPath()).equals(step)).findFirst().orElseThrow();
    }
    private static Holder<Biome> from(GameTestHelper h, TerraformingRecipe recipe) {
        return h.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).listElements().filter(b -> b.is(recipe.source())).findFirst().orElseThrow();
    }
    private static void fillBiome(GameTestHelper h, BlockPos absolute, Holder<Biome> biome) {
        var cell = BlockEntityTerraformer.cell(absolute);
        FillBiomeCommand.fill(h.getLevel(), new BlockPos(cell.getX(), h.getLevel().getMinY(), cell.getZ()),
                new BlockPos(cell.getX()+3, h.getLevel().getMaxY()-1, cell.getZ()+3), biome);
    }
    private static Holder<Biome> biome(GameTestHelper h, BlockPos pos) {
        return h.getLevel().getNoiseBiome(QuartPos.fromBlock(pos.getX()), QuartPos.fromBlock(h.absolutePos(MACHINE).getY()), QuartPos.fromBlock(pos.getZ()));
    }
    private static BlockEntityTerraformer machine(GameTestHelper h, TerraformingRecipe recipe, boolean resources) {
        var source = from(h, recipe);
        fillBiome(h, h.absolutePos(MACHINE), source);
        fillBiome(h, h.absolutePos(SELECTED), source);
        RotaryPowerTests.coil(h, 2, 4, 1, 1 << 20);
        h.setBlock(MACHINE, RotaryBlocks.TERRAFORMER.get());
        var tile = h.getBlockEntity(MACHINE, BlockEntityTerraformer.class);
        tile.setTarget(recipe.target());
        tile.addTile(h.absolutePos(SELECTED));
        if (resources) supplies(tile, recipe);
        return tile;
    }
    private static void supplies(BlockEntityTerraformer tile, TerraformingRecipe recipe) {
        if (recipe.water() > 0) tile.addLiquid(recipe.water() * 16);
        int slot = 0;
        for (var cost : recipe.items()) tile.setItem(slot++, new ItemStack(cost.ingredient().items().findFirst().orElseThrow().value(), 64));
    }
    private static void activate(GameTestHelper h, BlockEntityTerraformer tile) { h.setBlock(MACHINE.above(), Blocks.REDSTONE_BLOCK); tile.onBlockUpdate(); }
    private static void assertConverted(GameTestHelper h, BlockEntityTerraformer tile, TerraformingRecipe recipe) {
        var cell = BlockEntityTerraformer.cell(h.absolutePos(SELECTED));
        h.assertTrue(tile.power >= recipe.minPower(), "real creative coil must supply the conversion power");
        h.assertTrue(tile.getCoordinates().isEmpty(), "completed cell must leave the work queue");
        h.assertTrue(tile.getLiquidLevel() == 0, "all sixteen original column water costs must be paid exactly");
        for (int y = h.getLevel().getMinY(); y < h.getLevel().getMaxY(); y += 4)
            h.assertTrue(h.getLevel().getNoiseBiome(QuartPos.fromBlock(cell.getX()), QuartPos.fromBlock(y), QuartPos.fromBlock(cell.getZ())).is(recipe.target()), "entire modern biome column must change, Y=" + y);
    }
    private static void conversion(GameTestHelper h, String step) {
        var recipe = recipe(h, step);
        var tile = machine(h, recipe, true);
        activate(h, tile);
        h.runAfterDelay(15, () -> {
            assertConverted(h, tile, recipe);
            for (int n = 0; n < recipe.items().size(); n++) h.assertTrue(tile.getItem(n).getCount() >= 32, "a cell cannot debit more than its sixteen per-column costs (including repeated ingredients)");
            if (step.equals("ocean_to_frozen_ocean")) h.assertTrue(tile.getItem(0).getCount() == 48, "ice is consumed once per converted column");
            if (step.equals("ocean_to_mushroom_fields")) for (int n = 0; n < 4; n++) h.assertTrue(tile.getItem(n).getCount() == 48, "legacy 1.0 and 0.9 ItemReq chances both consume every column");
            h.succeed();
        });
    }
    private static void gates(GameTestHelper h) {
        var recipe = recipe(h, "swamp_to_ocean");
        var tile = machine(h, recipe, false);
        tile.addLiquid(recipe.water()*16);
        h.startSequence().thenIdle(8).thenExecute(() -> {
            h.assertTrue(tile.queuedCells == 1 && tile.getLiquidLevel() == 8000, "redstone-off selection must not run despite coil power");
            activate(h, tile);
            h.getBlockEntity(RotaryPowerTests.at(2,4), reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil.class).setReleaseTorque(0);
            tile.operationTicks = 800;
        }).thenIdle(8).thenExecute(() -> {
            h.assertTrue(tile.queuedCells == 1 && tile.getLiquidLevel() == 8000, "missing power cannot debit water");
            var data = tile.saveWithoutMetadata(h.getLevel().registryAccess());
            var tank = new reika.dragonapi.instantiable.HybridTank("terraformer", 24000);
            tank.setContents(7999, Fluids.WATER); tank.writeToNBT(data);
            tile.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), data));
            h.getBlockEntity(RotaryPowerTests.at(2,4), reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil.class).setReleaseTorque(1);
        }).thenIdle(8).thenExecute(() -> {
            h.assertTrue(tile.queuedCells == 1 && tile.getLiquidLevel() == 7999, "incomplete full-cell water batch must wait without partial spending");
            tile.addLiquid(1);
        }).thenIdle(8).thenExecute(() -> assertConverted(h,tile,recipe)).thenSucceed();
    }
    private static void itemGate(GameTestHelper h) {
        var recipe = recipe(h, "plains_to_snowy_plains"); var tile = machine(h,recipe,false);
        tile.setItem(0,new ItemStack(Items.SNOW_BLOCK,15)); tile.setItem(1,new ItemStack(Items.OAK_SAPLING,64));
        activate(h,tile);
        h.startSequence().thenIdle(12).thenExecute(() -> {
            h.assertTrue(tile.queuedCells==1 && tile.getItem(0).getCount()==15 && tile.getItem(1).getCount()==64, "partial item batch must not change biome or spend catalysts");
            tile.setItem(0,new ItemStack(Items.SNOW_BLOCK,16));
        }).thenIdle(12).thenExecute(() -> { assertConverted(h,tile,recipe); h.assertTrue(tile.getItem(0).isEmpty(), "sixteen snow blocks pay the original per-column requirement"); }).thenSucceed();
    }
    private static void selector(GameTestHelper h) {
        var tile = machine(h,recipe(h,"plains_to_forest"),false);
        var player = h.makeMockServerPlayerInLevel();
        var stack = new ItemStack(RotaryItems.TILE_SELECTOR.get()); player.setItemInHand(InteractionHand.MAIN_HAND,stack);
        var item = stack.getItem();
        java.util.function.Consumer<BlockPos> click = p -> item.useOn(new UseOnContext(player,InteractionHand.MAIN_HAND,new BlockHitResult(Vec3.atCenterOf(p),Direction.UP,p,false)));
        click.accept(h.absolutePos(MACHINE));
        h.assertTrue(stack.has(net.minecraft.core.component.DataComponents.CUSTOM_DATA), "machine click must bind the real selector item");
        click.accept(h.absolutePos(SELECTED)); click.accept(h.absolutePos(SELECTED).above());
        h.assertTrue(tile.queuedCells==1, "same XZ biome cell and different heights must not duplicate work");
        activate(h,tile); int count=tile.queuedCells;
        click.accept(h.absolutePos(SELECTED).offset(4,0,0));
        h.assertTrue(tile.queuedCells==count, "redstone-on machine must refuse selection changes");
        h.setBlock(MACHINE,Blocks.AIR); click.accept(h.absolutePos(SELECTED));
        h.succeed();
    }
    private static void saveReload(GameTestHelper h) {
        var recipe=recipe(h,"plains_to_forest"); var tile=machine(h,recipe,true); tile.operationTicks=37;
        tile.setItem(53,new ItemStack(Items.DIAMOND));
        var data=tile.saveWithoutMetadata(h.getLevel().registryAccess());
        var copy=new BlockEntityTerraformer(h.absolutePos(MACHINE),RotaryBlocks.TERRAFORMER.get().defaultBlockState());
        copy.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING,h.getLevel().registryAccess(),data)); copy.setLevel(h.getLevel());
        h.assertTrue(copy.getCoordinates().equals(tile.getCoordinates()) && copy.getTarget().equals(recipe.target()) && copy.operationTicks==37 && copy.getLiquidLevel()==160 && copy.getItem(53).is(Items.DIAMOND), "queue, target, cadence, water and all 54 slots must survive load before setLevel");
        h.succeed();
    }
    private static void transactions(GameTestHelper h) {
        var tile=machine(h,recipe(h,"plains_to_forest"),false);
        var items=h.getLevel().getCapability(Capabilities.Item.BLOCK,h.absolutePos(MACHINE),Direction.UP);
        var water=h.getLevel().getCapability(Capabilities.Fluid.BLOCK,h.absolutePos(MACHINE),Direction.DOWN);
        h.assertTrue(items!=null && items.size()==54 && water!=null,"both real block capabilities must exist");
        try(var tx=Transaction.openRoot()) { h.assertTrue(items.insert(ItemResource.of(Items.DIAMOND),2,tx)==2 && water.insert(FluidResource.of(Fluids.WATER),24000,tx)==24000,"automation must accept items and 24 buckets"); }
        h.assertTrue(tile.getItem(0).isEmpty() && tile.getLiquidLevel()==0,"aborted transactions must roll back both inventories");
        try(var tx=Transaction.openRoot()) { items.insert(ItemResource.of(Items.DIAMOND),2,tx); water.insert(FluidResource.of(Fluids.WATER),24000,tx); tx.commit(); }
        try(var tx=Transaction.openRoot()) {
            h.assertTrue(items.extract(ItemResource.of(Items.DIAMOND),1,tx)==0 && items.extract(0,ItemResource.of(Items.DIAMOND),1,tx)==0,"original automated item extraction must be blocked");
            h.assertTrue(water.extract(FluidResource.of(Fluids.WATER),1,tx)==0 && water.insert(FluidResource.of(Fluids.LAVA),1,tx)==0,"tank is water-only input, never automated output");
        }
        h.assertTrue(tile.getItem(0).getCount()==2 && tile.getLiquidLevel()==24000,"blocked automation cannot change contents"); h.succeed();
    }
    private static void menu(GameTestHelper h) {
        var tile=machine(h,recipe(h,"plains_to_forest"),false); var player=h.makeMockServerPlayerInLevel(); player.setPos(Vec3.atCenterOf(h.absolutePos(MACHINE)));
        var menu=new ContainerTerraformer(7,player.getInventory(),tile); player.containerMenu=menu;
        h.assertTrue(menu.slots.size()==90 && MachineRegistry.TERRAFORMER.hasGui(),"machine must expose the original 54+36 inventory screen");
        var biomes=h.getLevel().registryAccess().lookupOrThrow(Registries.BIOME);
        int target=biomes.getId(biomes.getOrThrow(Biomes.FOREST).value());
        h.assertTrue(menu.clickMenuButton(player,target) && tile.getTarget().equals(Biomes.FOREST),"valid central-biome choices must set target");
        h.assertTrue(!menu.clickMenuButton(player,Integer.MAX_VALUE) && !menu.clickMenuButton(player,biomes.getId(biomes.getOrThrow(Biomes.NETHER_WASTES).value())),"invalid/unsupported target packets must be rejected");
        player.getInventory().setItem(9,new ItemStack(Items.OAK_SAPLING,64)); menu.quickMoveStack(player,54);
        h.assertTrue(tile.getItem(0).getCount()==64 && player.getInventory().getItem(9).isEmpty(),"player shift-click must fill machine inventory");
        menu.quickMoveStack(player,0); h.assertTrue(tile.getItem(0).isEmpty(),"manual shift-click extraction must work despite automation restrictions");
        player.containerMenu=player.inventoryMenu;
        h.assertTrue(!menu.clickMenuButton(player,target),"closed menus cannot change target"); h.succeed();
    }
    private static void soil(GameTestHelper h,boolean diamond) {
        var recipe=recipe(h,"savanna_to_desert"); var tile=machine(h,recipe,true);
        var cell=BlockEntityTerraformer.cell(h.absolutePos(SELECTED));
        var pos=new BlockPos(cell.getX()+1,Math.max(40,h.absolutePos(new BlockPos(0,0,0)).getY()),cell.getZ()+1);
        h.getLevel().setBlockAndUpdate(pos.below(2),Blocks.STONE.defaultBlockState());
        h.getLevel().setBlockAndUpdate(pos,Blocks.GRASS_BLOCK.defaultBlockState()); h.getLevel().setBlockAndUpdate(pos.below(),Blocks.DIRT.defaultBlockState());
        if(diamond) tile.setItem(53,new ItemStack(Items.DIAMOND));
        activate(h,tile);
        h.runAfterDelay(20,()-> {
            assertConverted(h,tile,recipe);
            h.assertTrue(h.getLevel().getBlockState(pos).is(diamond?Blocks.SAND:Blocks.GRASS_BLOCK),"diamond/config must gate target topsoil replacement");
            h.assertTrue(h.getLevel().getBlockState(pos.below()).is(diamond?Blocks.SAND:Blocks.DIRT),"diamond/config must gate target filler replacement");
            if(diamond) h.assertTrue(tile.getItem(53).getCount()==1,"terrain-enabling diamond is a reusable catalyst"); h.succeed();
        });
    }
    private static void features(GameTestHelper h) {
        var origin=h.absolutePos(new BlockPos(4,1,4)); var cell=BlockEntityTerraformer.cell(origin);
        var area=new HashSet<BlockPos>();
        for(int x=1;x<=7;x++) for(int z=1;z<=7;z++) { area.add(BlockEntityTerraformer.cell(h.absolutePos(new BlockPos(x,0,z)))); h.setBlock(new BlockPos(x,0,z),Blocks.GRASS_BLOCK); }
        var world=TerraformerTerrain.boundedLevel(h.getLevel(),null,area);
        h.setBlock(new BlockPos(2,1,2),Blocks.CHEST);
        h.assertTrue(!world.setBlock(h.absolutePos(new BlockPos(2,1,2)),Blocks.AIR.defaultBlockState(),3),"biome generation must preserve block entities");
        var outside=cell.offset(40,origin.getY(),40);
        h.assertTrue(!world.setBlock(outside,Blocks.DIAMOND_BLOCK.defaultBlockState(),3),"worldgen writes must remain in selected cells");
        var oak=h.getLevel().registryAccess().lookupOrThrow(Registries.FEATURE).getOrThrow(ResourceKey.create(Registries.FEATURE,Identifier.withDefaultNamespace("oak")));
        h.assertTrue(oak.value().place(world,h.getLevel().getChunkSource().getGenerator(),net.minecraft.util.RandomSource.create(2),origin),"registered 26.3 oak feature must generate through bounded world");
        h.assertTrue(h.getLevel().getBlockState(origin).is(Blocks.OAK_LOG),"terrain decoration must create actual tree blocks"); h.succeed();
    }
    private static void unloaded(GameTestHelper h) {
        var recipe=recipe(h,"swamp_to_ocean"); var tile=machine(h,recipe,true);
        tile.setTarget(Biomes.NETHER_WASTES); activate(h,tile);
        h.startSequence().thenIdle(12).thenExecute(()-> {
            h.assertTrue(tile.queuedCells==1 && tile.getLiquidLevel()==8000,"unsupported target must not debit water or retire selection");
            h.setBlock(MACHINE.above(),Blocks.AIR); tile.onBlockUpdate();
            var far=h.absolutePos(SELECTED).offset(100000,0,100000);
            h.assertTrue(!h.getLevel().hasChunkAt(far),"unloaded fixture must start unloaded"); tile.addTile(far); tile.setTarget(recipe.target()); activate(h,tile);
        }).thenIdle(12).thenExecute(()-> {
            h.assertTrue(tile.queuedCells==1 && tile.getLiquidLevel()==0,"loaded cell should complete while unloaded work remains queued");
            h.assertTrue(!h.getLevel().hasChunkAt(h.absolutePos(SELECTED).offset(100000,0,100000)),"Terraformer must not force-load pending distant chunks");
        }).thenSucceed();
    }
    private static void harvest(GameTestHelper h) {
        var tile=machine(h,recipe(h,"plains_to_forest"),false); tile.setItem(53,new ItemStack(Items.DIAMOND,2));
        var player=(net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(MACHINE)),"survival pickaxes must harvest Terraformer");
        var drops=h.getEntities(net.minecraft.world.entity.EntityTypes.ITEM,MACHINE,3);
        h.assertTrue(drops.stream().filter(e->e.getItem().is(RotaryBlocks.TERRAFORMER.get().asItem())).mapToInt(e->e.getItem().getCount()).sum()==1,"exactly one machine must drop");
        h.assertTrue(drops.stream().filter(e->e.getItem().is(Items.DIAMOND)).mapToInt(e->e.getItem().getCount()).sum()==2,"all 54 inventory slots must drop contents"); h.succeed();
    }
    private static void recipeRoundTrip(GameTestHelper h) {
        var recipes=BlockEntityTerraformer.getTransformList(h.getLevel()); h.assertTrue(recipes.size()==36,"all original directed steps must be registered");
        var buffer=new RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(),h.getLevel().registryAccess());
        var ops=h.getLevel().registryAccess().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
        try { for(var recipe:recipes) {
            buffer.clear(); TerraformingRecipe.STREAM_CODEC.encode(buffer,recipe); var copy=TerraformingRecipe.STREAM_CODEC.decode(buffer);
            h.assertTrue(recipe.source().equals(copy.source()) && recipe.target().equals(copy.target()) && recipe.water()==copy.water() && recipe.minPower()==copy.minPower() && recipe.items().size()==copy.items().size() && recipe.targetTop()==copy.targetTop() && recipe.targetFiller()==copy.targetFiller() && !buffer.isReadable(),"recipe network sync must preserve biome, power, costs and terrain");
            var json=TerraformingRecipe.CODEC.codec().encodeStart(ops,recipe).getOrThrow(); var disk=TerraformingRecipe.CODEC.codec().parse(ops,json).getOrThrow();
            h.assertTrue(disk.target().equals(recipe.target()) && disk.items().size()==recipe.items().size(),"datapack codec must round-trip each original transformation");
        }} finally { buffer.release(); }
        h.succeed();
    }
    private static void children(GameTestHelper h) {
        var recipe=recipe(h,"birch_forest_to_forest"); var tile=machine(h,recipe,true);
        var oldGrowth=h.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(Biomes.OLD_GROWTH_BIRCH_FOREST);
        fillBiome(h,h.absolutePos(SELECTED),oldGrowth); activate(h,tile);
        h.runAfterDelay(12,()-> { assertConverted(h,tile,recipe); h.succeed(); });
    }
    private static void areaMarker(GameTestHelper h) {
        var min = h.absolutePos(new BlockPos(1, 1, 1));
        var max = h.absolutePos(new BlockPos(7, 1, 6));
        var markerPos = MACHINE.north();
        h.setBlock(markerPos, Blocks.OAK_SIGN);
        var marker = new AreaMarker(h.absolutePos(markerPos), min, max);
        h.getLevel().setBlockEntity(marker);
        h.setBlock(MACHINE, RotaryBlocks.TERRAFORMER.get());
        var tile = h.getBlockEntity(MACHINE, BlockEntityTerraformer.class);
        var expected = new HashSet<BlockPos>();
        for (int x = min.getX(); x <= max.getX(); x++) for (int z = min.getZ(); z <= max.getZ(); z++)
            expected.add(BlockEntityTerraformer.cell(new BlockPos(x, min.getY(), z)));
        h.runAfterDelay(5, () -> {
            h.assertTrue(tile.getCoordinates().equals(expected), "inclusive area markers must import every cell, including unaligned boundary columns");
            h.assertTrue(marker.removed && h.getBlockState(markerPos).isAir(), "successful import removes the area marker exactly once");
            h.succeed();
        });
    }
    private static final class AreaMarker extends net.minecraft.world.level.block.entity.SignBlockEntity implements reika.rotarycraft.api.interfaces.TerraformerAreaProvider {
        private final BlockPos min, max;
        private boolean removed;
        AreaMarker(BlockPos pos, BlockPos min, BlockPos max) {
            super(pos, Blocks.OAK_SIGN.defaultBlockState());
            this.min = min; this.max = max;
        }
        @Override public BlockPos minimum() { return min; }
        @Override public BlockPos maximum() { return max; }
        @Override public void removeAreaMarker() { removed = true; getLevel().removeBlock(getBlockPos(), false); }
    }
    private static void climate(GameTestHelper h) {
        var recipe = recipe(h, "ocean_to_frozen_ocean");
        var tile = machine(h, recipe, true);
        var water = h.absolutePos(SELECTED);
        h.getLevel().setBlockAndUpdate(water, Blocks.WATER.defaultBlockState());
        activate(h, tile);
        h.runAfterDelay(15, () -> {
            assertConverted(h, tile, recipe);
            h.assertTrue(!tile.modifyBlocks() && h.getLevel().getBlockState(water).is(Blocks.ICE), "original environmental freezing applies without the terrain diamond");
            h.succeed();
        });
    }

}
