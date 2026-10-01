package reika.rotarycraft;

import java.util.List;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.*;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.util.ProblemReporter;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.transfer.*;
import net.neoforged.neoforge.transfer.fluid.FluidResource;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.dragonapi.instantiable.HybridTank;
import reika.rotarycraft.auxiliary.recipemanagers.FuelEnhancerRecipe;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityFuelConverter;
import reika.rotarycraft.blockentities.piping.BlockEntityFuelLine;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFuelEnhancer;
import reika.rotarycraft.registry.*;

/** Real coils, reloadable recipes, transactional capabilities and survival interactions. */
final class RotaryFuelEnhancerTests {
    private static final BlockPos MACHINE = new BlockPos(3, 2, 4);
    private RotaryFuelEnhancerTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        test(event,env,"fuel_ratio",h -> conversion(h, false));
        test(event,env,"kerosene_ratio",h -> conversion(h, true));
        for (int n = 0; n < 5; n++) { final int missing = n; test(event,env,"requires_catalyst_"+n,h -> missing(h,missing)); }
        for (int speed : new int[]{16384,32768,65536,262144,1048576,Integer.MAX_VALUE}) test(event,env,"speed_boost_"+speed,h -> speed(h,speed));
        test(event,env,"speed_and_power_gates",RotaryFuelEnhancerTests::gates);
        test(event,env,"requires_bottom_power",RotaryFuelEnhancerTests::bottomOnly);
        test(event,env,"output_capacity",h -> backpressure(h,false));
        test(event,env,"incompatible_output",h -> backpressure(h,true));
        test(event,env,"fluid_transactions",RotaryFuelEnhancerTests::fluids);
        test(event,env,"item_transactions",RotaryFuelEnhancerTests::items);
        test(event,env,"hopper_insertion",RotaryFuelEnhancerTests::hopper);
        test(event,env,"hopper_extraction_denied",RotaryFuelEnhancerTests::hopperExtraction);
        test(event,env,"detached_save_load",RotaryFuelEnhancerTests::save);
        test(event,env,"menu_transfer_and_sync",RotaryFuelEnhancerTests::menu);
        test(event,env,"comparator",RotaryFuelEnhancerTests::comparator);
        test(event,env,"fuel_line_output",RotaryFuelEnhancerTests::pipes);
        test(event,env,"survival_harvest",h -> removal(h,true));
        test(event,env,"command_removal",h -> removal(h,false));
        test(event,env,"recipe_codecs",RotaryFuelEnhancerTests::codecs);
        test(event,env,"overlapping_catalysts",RotaryFuelEnhancerTests::allocation);
        test(event,env,"conditional_consumption",RotaryFuelEnhancerTests::conditional);
        test(event,env,"zero_consumption",RotaryFuelEnhancerTests::zeroConsumption);
        test(event,env,"one_conversion_per_tick",RotaryFuelEnhancerTests::cadence);
        test(event,env,"crafting_recipe",RotaryFuelEnhancerTests::crafting);
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, java.util.function.Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event,env,"fuel_enhancer_"+name,100,body);
    }
    private static BlockEntityFuelConverter machine(GameTestHelper h) {
        h.setBlock(MACHINE,RotaryBlocks.FUEL_ENHANCER.get());
        return h.getBlockEntity(MACHINE,BlockEntityFuelConverter.class);
    }
    private static BlockEntityCreativeCoil coil(GameTestHelper h, int torque, int speed) {
        h.setBlock(MACHINE.below().north(),Blocks.REDSTONE_BLOCK);
        h.setBlock(MACHINE.below(),RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING,Direction.DOWN));
        var coil=h.getBlockEntity(MACHINE.below(),BlockEntityCreativeCoil.class); coil.setReleaseTorque(torque); coil.setReleaseOmega(speed); return coil;
    }
    private static List<Item> catalysts() { return List.of(Items.BLAZE_POWDER,RotaryItems.NETHERRACK_DUST.get(),RotaryItems.TAR.get(),Items.MAGMA_CREAM,Items.DYE.pink()); }
    private static void supply(BlockEntityFuelConverter tile) { for(int n=0;n<5;n++) tile.setItem(n,new ItemStack(catalysts().get(n),64)); }
    private static ResourceHandler<FluidResource> fluid(GameTestHelper h, Direction side) {
        return h.getLevel().getCapability(Capabilities.Fluid.BLOCK,h.absolutePos(MACHINE),side);
    }
    private static void fill(GameTestHelper h, Fluid fluid, int amount) {
        try(var tx=Transaction.openRoot()) { h.assertTrue(fluid(h,Direction.UP).insert(FluidResource.of(fluid),amount,tx)==amount,"top fluid intake must accept complete batch"); tx.commit(); }
    }
    private static void output(GameTestHelper h, BlockEntityFuelConverter tile, Fluid fluid, int amount) {
        var saved=tile.saveWithoutMetadata(h.getLevel().registryAccess());
        var tank=new HybridTank("fuelenhancerout",5000); tank.setContents(amount,fluid); tank.writeToNBT(saved);
        tile.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING,h.getLevel().registryAccess(),saved));
    }
    private static void conversion(GameTestHelper h, boolean kerosene) {
        coil(h,1,16384); var tile=machine(h); supply(tile); fill(h,kerosene?Fluids.LAVA:Fluids.WATER,40);
        h.runAfterDelay(24,()-> { h.assertTrue(tile.omega==16384&&tile.power==16384,"coil must reach real bottom power input");
            h.assertTrue(tile.getInputLevel()==0&&tile.getOutputLevel()==10&&tile.getFluidInOutput().getFluid()==RotaryFluids.JET_FUEL.get(),"both original fuels convert at exactly 4:1"); h.succeed(); });
    }
    private static void missing(GameTestHelper h, int missing) {
        coil(h,1,16384); var tile=machine(h); supply(tile); tile.setItem(missing,ItemStack.EMPTY); fill(h,Fluids.WATER,4);
        h.startSequence().thenIdle(6).thenExecute(()-> { h.assertTrue(tile.getInputLevel()==4&&tile.getOutputLevel()==0,"each V33a catalyst, including pink dye, is required"); tile.setItem(missing,new ItemStack(catalysts().get(missing))); })
            .thenIdle(6).thenExecute(()->h.assertTrue(tile.getInputLevel()==0&&tile.getOutputLevel()==1,"restoring the missing catalyst must enable conversion")).thenSucceed();
    }
    private static void speed(GameTestHelper h, int speed) {
        int boost=1+(int)(Math.log(speed/16384)/Math.log(4));
        coil(h,1,speed); var tile=machine(h); supply(tile); fill(h,Fluids.WATER,4*boost+3);
        h.runAfterDelay(6,()-> { h.assertTrue(tile.getSpeedBoost()==boost&&tile.getOutputLevel()==boost&&tile.getInputLevel()==3,"V33a logarithmic whole-batch speed scaling must be exact"); h.succeed(); });
    }
    private static void gates(GameTestHelper h) {
        var coil=coil(h,2,8192); var tile=machine(h); supply(tile); fill(h,Fluids.WATER,4);
        h.startSequence().thenIdle(6).thenExecute(()-> { h.assertTrue(tile.power==16384&&tile.getInputLevel()==4,"sufficient power must not bypass minimum speed"); coil.setReleaseTorque(0); coil.setReleaseOmega(16384); })
            .thenIdle(6).thenExecute(()-> { h.assertTrue(tile.getInputLevel()==4,"speed without power must not convert fuel"); coil.setReleaseTorque(1); })
            .thenIdle(6).thenExecute(()->h.assertTrue(tile.getOutputLevel()==1,"restoring both requirements must enable conversion")).thenSucceed();
    }
    private static void bottomOnly(GameTestHelper h) {
        RotaryPowerTests.coil(h,2,4,1,16384); var pos=new BlockPos(3,1,4); h.setBlock(pos,RotaryBlocks.FUEL_ENHANCER.get());
        var tile=h.getBlockEntity(pos,BlockEntityFuelConverter.class); supply(tile);
        try(var tx=Transaction.openRoot()) { tile.getFluidHandler(Direction.UP).insert(FluidResource.of(Fluids.WATER),4,tx); tx.commit(); }
        h.runAfterDelay(6,()-> { h.assertTrue(tile.power==0&&tile.getInputLevel()==4,"horizontal shaft output must not run this machine"); h.succeed(); });
    }
    private static void backpressure(GameTestHelper h, boolean incompatible) {
        coil(h,1,65536); var tile=machine(h); supply(tile); fill(h,Fluids.WATER,8);
        output(h,tile,incompatible?RotaryFluids.ETHANOL.get():RotaryFluids.JET_FUEL.get(),incompatible?1:4999);
        h.startSequence().thenIdle(6).thenExecute(()-> { h.assertTrue(tile.getInputLevel()==8&&tile.getOutputLevel()==(incompatible?1:4999),"blocked output must preserve all fluid and catalysts");
            for(int n=0;n<5;n++) h.assertTrue(tile.getItem(n).getCount()==64,"blocked conversions must not roll catalyst consumption"); output(h,tile,RotaryFluids.JET_FUEL.get(),4998); })
            .thenIdle(6).thenExecute(()->h.assertTrue(tile.getInputLevel()==0&&tile.getOutputLevel()==5000,"one complete speed-scaled batch must fit the exact capacity boundary")).thenSucceed();
    }
    private static void fluids(GameTestHelper h) {
        var tile=machine(h); h.assertTrue(fluid(h,Direction.DOWN)==null,"bottom has no fluid port");
        for(Direction side:Direction.values()) {
            if(side==Direction.DOWN) continue;
            try(var tx=Transaction.openRoot()) { var handler=fluid(h,side); h.assertTrue(handler!=null,"fluid capability missing");
                h.assertTrue(handler.insert(FluidResource.of(Fluids.WATER),17,tx)==(side==Direction.UP?17:0),"only top permits fluid input");
                h.assertTrue(handler.insert(FluidResource.of(RotaryFluids.JET_FUEL.get()),1,tx)==0,"unregistered input recipes must be rejected");
                h.assertTrue(handler.extract(FluidResource.of(Fluids.WATER),17,tx)==0,"input extraction must be denied"); }
            h.assertTrue(tile.getInputLevel()==0,"aborted transaction must restore fluid input");
        }
        fill(h,Fluids.WATER,5000); output(h,tile,RotaryFluids.JET_FUEL.get(),17);
        for(Direction side:Direction.values()) {
            if(side==Direction.DOWN) continue;
            try(var tx=Transaction.openRoot()) { h.assertTrue(fluid(h,side).extract(FluidResource.of(RotaryFluids.JET_FUEL.get()),6,tx)==(side==Direction.UP?0:6),"only horizontal ports extract output"); }
            h.assertTrue(tile.getOutputLevel()==17,"aborted extraction must roll back");
        }
        try(var tx=Transaction.openRoot()) { h.assertTrue(fluid(h,Direction.UP).insert(FluidResource.of(Fluids.WATER),1,tx)==0,"5000 mB capacity must be enforced");
            fluid(h,Direction.NORTH).extract(FluidResource.of(RotaryFluids.JET_FUEL.get()),6,tx); tx.commit(); }
        h.assertTrue(tile.getOutputLevel()==11&&tile.getInputLevel()==5000,"committed output transfer must touch only output tank"); h.succeed();
    }
    private static void items(GameTestHelper h) {
        var tile=machine(h); var catalyst=ItemResource.of(new ItemStack(Items.BLAZE_POWDER));
        for(Direction side:Direction.values()) {
            var handler=h.getLevel().getCapability(Capabilities.Item.BLOCK,h.absolutePos(MACHINE),side);
            h.assertTrue(handler!=null&&handler.size()==9,"all item ports must expose nine slots");
            try(var tx=Transaction.openRoot()) { h.assertTrue(handler.insert(catalyst,5,tx)==5,"automation must insert catalysts"); h.assertTrue(handler.insert(ItemResource.of(new ItemStack(Items.DIAMOND)),1,tx)==0,"unrelated items must be rejected"); }
            h.assertTrue(tile.isEmpty(),"aborted item insertion must restore inventory");
            try(var tx=Transaction.openRoot()) { handler.insert(8,catalyst,5,tx); tx.commit(); }
            try(var tx=Transaction.openRoot()) { h.assertTrue(handler.extract(8,catalyst,5,tx)==0&&handler.extract(catalyst,5,tx)==0,"both slot and bulk automated extraction must be denied"); }
            h.assertTrue(tile.removeItem(8,5).getCount()==5,"manual extraction remains available");
        }
        h.succeed();
    }
    private static void hopper(GameTestHelper h) {
        var tile=machine(h); h.setBlock(MACHINE.above(),Blocks.HOPPER);
        var hopper=h.getBlockEntity(MACHINE.above(),net.minecraft.world.level.block.entity.HopperBlockEntity.class); hopper.setItem(0,new ItemStack(Items.BLAZE_POWDER,3));
        h.runAfterDelay(32,()-> { h.assertTrue(tile.getItem(0).getCount()==3&&hopper.getItem(0).isEmpty(),"real hopper must insert allowed catalysts through top: machine="+tile.getItem(0)+", hopper="+hopper.getItem(0)); h.succeed(); });
    }
    private static void save(GameTestHelper h) {
        var tile=machine(h); supply(tile); fill(h,Fluids.WATER,123); output(h,tile,RotaryFluids.JET_FUEL.get(),37);
        var saved=tile.saveWithoutMetadata(h.getLevel().registryAccess()); var restored=new BlockEntityFuelConverter(tile.getBlockPos(),tile.getBlockState());
        var handler=restored.getItemHandler(); var fluid=restored.getFluidHandler(Direction.NORTH);
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING,h.getLevel().registryAccess(),saved));
        h.assertTrue(restored.getLevel()==null&&restored.getInputLevel()==123&&restored.getOutputLevel()==37,"detached loads must restore both named tanks");
        for(int n=0;n<5;n++) h.assertTrue(restored.getItem(n).is(catalysts().get(n))&&handler.getAmountAsInt(n)==64,"detached inventory must retain registry context and handler identity");
        try(var tx=Transaction.openRoot()) { h.assertTrue(fluid.extract(FluidResource.of(RotaryFluids.JET_FUEL.get()),7,tx)==7,"pre-load fluid view must bind restored tank"); tx.commit(); }
        h.assertTrue(restored.getOutputLevel()==30&&handler==restored.getItemHandler(),"capability identities must survive loading"); h.succeed();
    }
    private static void hopperExtraction(GameTestHelper h) {
        var tile=machine(h); supply(tile); h.setBlock(MACHINE.below(),Blocks.HOPPER);
        var hopper=h.getBlockEntity(MACHINE.below(),net.minecraft.world.level.block.entity.HopperBlockEntity.class);
        h.runAfterDelay(24,()-> { h.assertTrue(hopper.isEmpty(),"real hopper must not extract catalysts through bottom");
            for(int n=0;n<5;n++) h.assertTrue(tile.getItem(n).getCount()==64,"blocked extraction must preserve every catalyst"); h.succeed(); });
    }
    private static void menu(GameTestHelper h) {
        var tile=machine(h); var player=h.makeMockServerPlayer(GameType.SURVIVAL); player.setPos(net.minecraft.world.phys.Vec3.atCenterOf(h.absolutePos(MACHINE)));
        var menu=new ContainerFuelEnhancer(1,player.getInventory(),tile);
        h.assertTrue(menu.slots.size()==45&&menu.stillValid(player),"menu must expose all slots at usable range");
        player.getInventory().setItem(9,new ItemStack(Items.BLAZE_POWDER,12)); menu.quickMoveStack(player,9);
        h.assertTrue(tile.getItem(0).getCount()==12&&player.getInventory().getItem(9).isEmpty(),"shift-click must insert accepted catalysts");
        menu.quickMoveStack(player,0); h.assertTrue(tile.isEmpty()&&player.getInventory().getItem(9).getCount()==12,"manual shift-click must return catalysts to player");
        player.getInventory().setItem(9,new ItemStack(Items.DIAMOND)); menu.quickMoveStack(player,9); h.assertTrue(tile.isEmpty(),"shift-click must reject invalid items");
        menu.setData(0,0xFFFF); menu.setData(1,0x10); menu.setData(2,0xFFFF); menu.setData(3,0x20);
        for(int n=4;n<8;n++) menu.setData(n,n==6?1:n==7?0:0xFFFF);
        h.assertTrue(tile.omega==0x10FFFF&&tile.torque==0x20FFFF&&tile.power==0x1FFFFFFFFL,"menu must preserve high speed, torque and power bits");
        player.setPos(player.getX()+20,player.getY(),player.getZ()); h.assertTrue(!menu.stillValid(player),"distant players must lose menu access"); h.succeed();
    }
    private static void comparator(GameTestHelper h) {
        var tile=machine(h); var state=h.getBlockState(MACHINE);
        h.assertTrue(state.hasAnalogOutputSignal()&&state.getAnalogOutputSignal(h.getLevel(),h.absolutePos(MACHINE),Direction.NORTH)==15,"empty input must expose comparator 15 through actual block state");
        fill(h,Fluids.WATER,4); h.assertTrue(state.getAnalogOutputSignal(h.getLevel(),h.absolutePos(MACHINE),Direction.NORTH)==0,"ready machine comparator must be zero");
        output(h,tile,RotaryFluids.JET_FUEL.get(),5000); h.assertTrue(state.getAnalogOutputSignal(h.getLevel(),h.absolutePos(MACHINE),Direction.NORTH)==15,"full output must expose comparator 15"); h.succeed();
    }
    private static void pipes(GameTestHelper h) {
        var tile=machine(h); output(h,tile,RotaryFluids.JET_FUEL.get(),1000);
        h.setBlock(MACHINE.east(),RotaryBlocks.FUEL_LINE.get()); var pipe=h.getBlockEntity(MACHINE.east(),BlockEntityFuelLine.class);
        h.assertTrue(tile.canConnectToPipeOnSide(MachineRegistry.PIPE,Direction.UP)&&tile.canConnectToPipeOnSide(MachineRegistry.FUELLINE,Direction.UP)
                &&tile.canConnectToPipeOnSide(MachineRegistry.FUELLINE,Direction.EAST)&&!tile.canConnectToPipeOnSide(MachineRegistry.PIPE,Direction.EAST)
                &&!tile.canConnectToPipeOnSide(MachineRegistry.HOSE,Direction.UP),"V33a pipe intake and output routes must be exact");
        h.runAfterDelay(24,()-> { h.assertTrue(pipe.getFluidLevel()>0&&pipe.getAttributes()==RotaryFluids.JET_FUEL.get()&&tile.getOutputLevel()<1000,"real fuel line must extract horizontal jet-fuel output"); h.succeed(); });
    }
    private static void removal(GameTestHelper h, boolean survival) {
        var tile=machine(h); supply(tile);
        if(survival) { var player=(net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.IRON_PICKAXE));
            h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(MACHINE)),"survival pickaxe must harvest machine"); }
        else h.setBlock(MACHINE,Blocks.AIR);
        var drops=h.getEntities(EntityTypes.ITEM,MACHINE,3);
        for(var item:catalysts()) h.assertTrue(drops.stream().filter(e->e.getItem().is(item)).mapToInt(e->e.getItem().getCount()).sum()==64,"every catalyst must drop exactly once on any removal");
        if(survival) h.assertTrue(drops.stream().filter(e->e.getItem().is(RotaryBlocks.FUEL_ENHANCER.get().asItem())).mapToInt(e->e.getItem().getCount()).sum()==1,"survival harvest must return exactly one machine"); h.succeed();
    }
    private static void codecs(GameTestHelper h) {
        var recipes=BlockEntityFuelConverter.getAllRecipes(h.getLevel()); var registry=h.getLevel().registryAccess();
        var ops=registry.createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
        var buffer=new RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(),registry);
        try { for(var recipe:recipes) {
            var json=FuelEnhancerRecipe.CODEC.codec().encodeStart(ops,recipe).getOrThrow(); var jsonCopy=FuelEnhancerRecipe.CODEC.codec().parse(ops,json).getOrThrow();
            buffer.clear(); FuelEnhancerRecipe.STREAM_CODEC.encode(buffer,recipe); var copy=FuelEnhancerRecipe.STREAM_CODEC.decode(buffer);
            h.assertTrue(copy.input().equals(recipe.input())&&copy.output().equals(recipe.output())&&copy.speedFactor()==recipe.speedFactor()&&copy.fluidRatio()==recipe.fluidRatio()
                    &&copy.consumptionMultiplier()==recipe.consumptionMultiplier()&&copy.ingredients().size()==recipe.ingredients().size()&&copy.getCondition().equals(recipe.getCondition())&&!buffer.isReadable(),"network codec must preserve all conversion fields");
            h.assertTrue(FuelEnhancerRecipe.CODEC.codec().encodeStart(ops,jsonCopy).getOrThrow().equals(json),"datapack recipe must roundtrip");
        }} finally { buffer.release(); }
        var fuel=BlockEntityFuelConverter.getByInput(h.getLevel(),Fluids.WATER).getFirst(); var kerosene=BlockEntityFuelConverter.getByInput(h.getLevel(),Fluids.LAVA).getFirst();
        h.assertTrue(fuel.ingredients().size()==5&&fuel.fluidRatio()==4&&fuel.speedFactor()==1&&fuel.consumptionMultiplier()==.015&&kerosene.consumptionMultiplier()==.01,"V33a built-in requirements and difficulty-scaled consumption multipliers must load exactly"); h.succeed();
    }
    private static void allocation(GameTestHelper h) {
        var original=BlockEntityFuelConverter.getByInput(h.getLevel(),Fluids.WATER).getFirst();
        var recipe=new FuelEnhancerRecipe(original.input(),original.output(),1,4,0,List.of(Ingredient.of(Items.BLAZE_POWDER,Items.MAGMA_CREAM),Ingredient.of(Items.BLAZE_POWDER)),"");
        int[] slots=recipe.findIngredients(List.of(new ItemStack(Items.BLAZE_POWDER),new ItemStack(Items.MAGMA_CREAM)));
        h.assertTrue(slots!=null&&slots[0]==1&&slots[1]==0,"overlapping catalysts must reassign a general match to reserve the specific one");
        h.assertTrue(recipe.findIngredients(List.of(new ItemStack(Items.BLAZE_POWDER)))==null,"one item must not pay two catalyst costs");
        h.assertTrue(recipe.findIngredients(List.of(new ItemStack(Items.BLAZE_POWDER,2)))!=null,"two units in one stack can pay both costs"); h.succeed();
    }
    private static void conditional(GameTestHelper h) {
        for(var recipe:BlockEntityFuelConverter.getByInput(h.getLevel(),RotaryFluids.LUBRICANT.get()))
            if(recipe.consumptionMultiplier()==0) recipe.setUsability(new FuelEnhancerRecipe.UsabilityCondition() {
                public boolean isUsable(BlockEntityFuelConverter tile) { return false; }
                public String getDescription() { return "Test-only conditional recipe"; }
            });
        coil(h,1,65536); var tile=machine(h); tile.setItem(0,new ItemStack(RotaryItems.TAR.get(),4)); fill(h,RotaryFluids.LUBRICANT.get(),36);
        h.runAfterDelay(12,()-> { h.assertTrue(tile.getInputLevel()==12&&tile.getOutputLevel()==8&&tile.isEmpty(),"conditional alternatives must skip unusable recipes, consume two catalysts per operation regardless of speed boost and stop when catalysts run out"); h.succeed(); });
    }
    private static void zeroConsumption(GameTestHelper h) {
        coil(h,1,16384); var tile=machine(h); tile.setItem(0,new ItemStack(Items.BLAZE_POWDER,3)); fill(h,RotaryFluids.ETHANOL.get(),48);
        h.runAfterDelay(24,()-> { h.assertTrue(tile.getInputLevel()==0&&tile.getOutputLevel()==12&&tile.getItem(0).getCount()==3,"zero-chance recipe must require catalysts but never consume them"); h.succeed(); });
    }
    private static void cadence(GameTestHelper h) {
        coil(h,1,16384); var tile=machine(h); tile.setItem(0,new ItemStack(Items.BLAZE_POWDER)); fill(h,RotaryFluids.ETHANOL.get(),400);
        int[] previous=new int[2];
        h.startSequence().thenIdle(6).thenExecute(()-> { previous[0]=tile.getInputLevel(); previous[1]=tile.getOutputLevel(); })
            .thenIdle(1).thenExecute(()-> { h.assertTrue(tile.getInputLevel()==previous[0]-4&&tile.getOutputLevel()==previous[1]+1,"first consecutive tick must convert exactly one batch"); previous[0]=tile.getInputLevel(); previous[1]=tile.getOutputLevel(); })
            .thenIdle(1).thenExecute(()->h.assertTrue(tile.getInputLevel()==previous[0]-4&&tile.getOutputLevel()==previous[1]+1,"next tick must immediately convert another complete batch")).thenSucceed();
    }
    private static void crafting(GameTestHelper h) {
        var inputs=List.of(new ItemStack(RotaryItems.HSLA_PLATE.get()),new ItemStack(Blocks.GLASS_PANE),new ItemStack(RotaryItems.HSLA_PLATE.get()),
                new ItemStack(Items.GOLD_INGOT),new ItemStack(RotaryItems.MIXER.get()),new ItemStack(Items.GOLD_INGOT),
                new ItemStack(RotaryItems.HSLA_PLATE.get()),new ItemStack(Blocks.GLASS_PANE),new ItemStack(RotaryItems.HSLA_PLATE.get()));
        var input=CraftingInput.of(3,3,inputs);
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream().anyMatch(holder->holder.value().matches(input,h.getLevel())&&holder.value().assemble(input).is(RotaryBlocks.FUEL_ENHANCER.get().asItem())),"original gold-panel-mixer recipe must craft the machine"); h.succeed();
    }
}
