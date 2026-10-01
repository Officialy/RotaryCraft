package reika.rotarycraft;

import java.util.Map;
import java.util.Set;
import java.util.HashSet;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.ChunkPos;
import net.minecraft.world.level.TicketStorage;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.level.GameType;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.common.world.chunk.TicketController;
import net.neoforged.neoforge.common.world.chunk.TicketHelper;
import net.neoforged.neoforge.common.world.chunk.TicketSet;
import reika.dragonapi.auxiliary.ChunkManager;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.BlockEntityChunkLoader;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

/** Assert the actual NeoForge tickets, independently of DragonAPI's in-memory ownership cache. */
final class RotaryChunkLoaderTests {
    private static final BlockPos MACHINE = new BlockPos(3,2,4);
    private static final int SPEED = 2097152;
    private RotaryChunkLoaderTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event,env,"chunk_loader_speed_gate",80,RotaryChunkLoaderTests::gate);
        RotaryGameTests.register(event,env,"chunk_loader_radius_expands_and_contracts",100,RotaryChunkLoaderTests::radius);
        RotaryGameTests.register(event,env,"chunk_loader_power_loss_releases_tickets",80,RotaryChunkLoaderTests::powerLoss);
        RotaryGameTests.register(event,env,"chunk_loader_command_removal_releases_tickets",60,h->removal(h,false));
        RotaryGameTests.register(event,env,"chunk_loader_survival_harvest_releases_tickets",60,h->removal(h,true));
        RotaryGameTests.register(event,env,"chunk_loader_overlapping_owners",80,RotaryChunkLoaderTests::overlap);
        RotaryGameTests.register(event,env,"chunk_loader_restored_tickets_release",80,RotaryChunkLoaderTests::restored);
        RotaryGameTests.register(event,env,"chunk_loader_long_power_radius",40,RotaryChunkLoaderTests::longPower);
        RotaryGameTests.register(event,env,"chunk_loader_bottom_power_only",60,RotaryChunkLoaderTests::bottomOnly);
        RotaryGameTests.register(event,env,"chunk_loader_original_recipe",40,RotaryChunkLoaderTests::recipe);
    }
    private static BlockEntityCreativeCoil coil(GameTestHelper h,BlockPos pos,int omega) {
        h.setBlock(pos.below().north(),Blocks.REDSTONE_BLOCK);
        h.setBlock(pos.below(),RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING,Direction.DOWN));
        var coil=h.getBlockEntity(pos.below(),BlockEntityCreativeCoil.class);
        coil.setReleaseTorque(1); coil.setReleaseOmega(omega); return coil;
    }
    private static BlockEntityChunkLoader machine(GameTestHelper h,BlockPos pos) {
        h.setBlock(pos,RotaryBlocks.CHUNK_LOADER.get()); return h.getBlockEntity(pos,BlockEntityChunkLoader.class);
    }
    private static void gate(GameTestHelper h) {
        var coil=coil(h,MACHINE,SPEED-1); var tile=machine(h,MACHINE);
        h.startSequence().thenIdle(5).thenExecute(()-> {
            h.assertTrue(!tile.isActive()&&tickets(h,MACHINE).isEmpty(),"one rad/s below the threshold must not force chunks"); coil.setReleaseOmega(SPEED);
        }).thenIdle(5).thenExecute(()-> {
            h.assertTrue(tile.MINSPEED==SPEED&&tile.power==SPEED,"registry and bottom power must agree with V33a");
            h.assertTrue(tickets(h,MACHINE).equals(Set.of(new ChunkPos(h.absolutePos(MACHINE).getX() >> 4,h.absolutePos(MACHINE).getZ() >> 4).pack())),"minimum speed must force exactly the machine chunk");
            tile.breakBlock();
        }).thenSucceed();
    }
    private static void radius(GameTestHelper h) {
        var coil=coil(h,MACHINE,SPEED+BlockEntityChunkLoader.FALLOFF); var tile=machine(h,MACHINE);
        h.startSequence().thenIdle(5).thenExecute(()-> {
            h.assertTrue(tile.getRange()==16&&tickets(h,MACHINE).size()==9,"one falloff step must force a 3x3 chunk square");
            coil.setReleaseOmega(SPEED+2*BlockEntityChunkLoader.FALLOFF);
        }).thenIdle(5).thenExecute(()-> {
            h.assertTrue(tile.getRange()==32&&tickets(h,MACHINE).size()==25,"increased power must update the live ticket set");
            coil.setReleaseOmega(SPEED);
        }).thenIdle(5).thenExecute(()-> {
            h.assertTrue(tile.getRange()==0&&tickets(h,MACHINE).size()==1,"contraction must release every outer ticket"); tile.breakBlock();
        }).thenSucceed();
    }
    private static void powerLoss(GameTestHelper h) {
        coil(h,MACHINE,SPEED+BlockEntityChunkLoader.FALLOFF); var tile=machine(h,MACHINE);
        h.startSequence().thenIdle(5).thenExecute(()-> {
            h.assertTrue(tickets(h,MACHINE).size()==9,"powered loader must own its square"); h.setBlock(MACHINE.below().north(),Blocks.AIR);
        }).thenIdle(5).thenExecute(()-> {
            h.assertTrue(tile.omega==0&&!ChunkManager.instance.isLoaded(tile)&&tickets(h,MACHINE).isEmpty(),"power loss must clear real tickets and cached ownership");
        }).thenSucceed();
    }
    private static void removal(GameTestHelper h,boolean survival) {
        coil(h,MACHINE,SPEED); machine(h,MACHINE);
        h.runAfterDelay(5,()-> {
            h.assertTrue(tickets(h,MACHINE).size()==1,"loader must acquire its ticket");
            if(survival) {
                var player=(net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);
                player.setItemInHand(InteractionHand.MAIN_HAND,new ItemStack(Items.IRON_PICKAXE));
                h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(MACHINE)),"survival harvest must succeed");
                h.assertTrue(h.getEntities(net.minecraft.world.entity.EntityTypes.ITEM,MACHINE,2).stream()
                        .filter(e->e.getItem().is(RotaryBlocks.CHUNK_LOADER.get().asItem())).mapToInt(e->e.getItem().getCount()).sum()==1,"harvest must return exactly one loader");
            } else h.setBlock(MACHINE,Blocks.AIR);
            h.assertTrue(tickets(h,MACHINE).isEmpty(),"any-removal hook must release owned tickets immediately"); h.succeed();
        });
    }
    private static void overlap(GameTestHelper h) {
        var other=MACHINE.east(3); coil(h,MACHINE,SPEED); coil(h,other,SPEED);
        machine(h,MACHINE); var second=machine(h,other);
        h.runAfterDelay(5,()-> {
            h.assertTrue(tickets(h,MACHINE).size()==1&&tickets(h,other).size()==1,"both positions must own independent tickets");
            h.setBlock(MACHINE,Blocks.AIR);
            h.assertTrue(tickets(h,MACHINE).isEmpty()&&tickets(h,other).size()==1,"removing one owner must preserve the other's tickets"); second.breakBlock(); h.succeed();
        });
    }
    private static void restored(GameTestHelper h) {
        coil(h,MACHINE,SPEED+BlockEntityChunkLoader.FALLOFF); var tile=machine(h,MACHINE);
        h.startSequence().thenIdle(5).thenExecute(()-> {
            Set<Long> saved=tickets(h,MACHINE); h.assertTrue(saved.size()==9,"fixture must have a full saved radius");
            try {
                // Emulate the restart boundary: live cache lost, NeoForge restores saved ticket ownership.
                var live=ChunkManager.class.getDeclaredField("liveTickets"); live.setAccessible(true);
                ((Map<?,?>)live.get(ChunkManager.instance)).remove(new reika.dragonapi.instantiable.data.immutable.WorldLocation(tile));
                var field=ChunkManager.class.getDeclaredField("CONTROLLER"); field.setAccessible(true);
                var controller=(TicketController)field.get(null);
                var constructor=TicketHelper.class.getDeclaredConstructor(TicketStorage.class,net.minecraft.resources.Identifier.class,Map.class,Map.class); constructor.setAccessible(true);
                var helper=constructor.newInstance(storage(h),controller.id(),Map.of(h.absolutePos(MACHINE),new TicketSet(new it.unimi.dsi.fastutil.longs.LongOpenHashSet(saved),new it.unimi.dsi.fastutil.longs.LongOpenHashSet())),Map.of());
                controller.callback().validateTickets(h.getLevel(),helper);
                h.assertTrue(ChunkManager.instance.isLoaded(tile),"restore validation must rebuild the ownership cache");
            } catch(ReflectiveOperationException ex) { throw new IllegalStateException(ex); }
            h.setBlock(MACHINE.below().north(),Blocks.AIR);
        }).thenIdle(5).thenExecute(()-> {
            h.assertTrue(tickets(h,MACHINE).isEmpty(),"first-tick power loss after restore must release the entire saved radius");
        }).thenSucceed();
    }
    private static void longPower(GameTestHelper h) {
        var tile=machine(h,MACHINE); tile.omega=SPEED; tile.torque=Integer.MAX_VALUE; tile.power=(long)tile.omega*tile.torque;
        h.assertTrue(tile.getRange()==tile.getMaxRange()&&tile.getLoadingRadius()>=0,"long power must saturate the configured radius without overflow");
        tile.power=0; tile.omega=0;
        h.assertTrue(tile.getLoadingRadius()==0&&tile.getChunksToLoad().isEmpty(),"inactive loaders must request no chunks and report no negative range"); h.succeed();
    }
    private static void bottomOnly(GameTestHelper h) {
        RotaryPowerTests.coil(h,2,4,1,SPEED); var pos=new BlockPos(3,1,4); var tile=machine(h,pos);
        h.runAfterDelay(6,()-> { h.assertTrue(tile.power==0&&tickets(h,pos).isEmpty(),"horizontal power must not activate a bottom-powered loader"); h.succeed(); });
    }
    private static void recipe(GameTestHelper h) {
        var inputs=java.util.List.of(new ItemStack(Items.NETHER_STAR),new ItemStack(RotaryBlocks.BEDROCK_SHAFT.get()),new ItemStack(Items.NETHER_STAR),
                new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get()),new ItemStack(RotaryBlocks.BEDROCK_SHAFT.get()),new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get()),
                new ItemStack(RotaryItems.HSLA_PLATE.get()),new ItemStack(RotaryItems.BEDROCK_ALLOY_GEAR_16x.get()),new ItemStack(RotaryItems.HSLA_PLATE.get()));
        var input=net.minecraft.world.item.crafting.CraftingInput.of(3,3,inputs);
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(net.minecraft.world.item.crafting.RecipeType.CRAFTING).stream()
                .anyMatch(holder->holder.value().matches(input,h.getLevel())&&holder.value().assemble(input).is(RotaryBlocks.CHUNK_LOADER.get().asItem())),"original bedrock-gear recipe must be craftable"); h.succeed();
    }
    private static TicketStorage storage(GameTestHelper h) { return h.getLevel().getDataStorage().computeIfAbsent(TicketStorage.TYPE); }
    private static Set<Long> tickets(GameTestHelper h,BlockPos relative) {
        try {
            var tracker=storage(h).getBlockForcedChunks();
            var source=tracker.getClass().getDeclaredField("sourcesLoading"); source.setAccessible(true);
            var map=(it.unimi.dsi.fastutil.longs.Long2ObjectMap<?>)source.get(tracker);
            var result=new HashSet<Long>();
            for(var entry:map.long2ObjectEntrySet()) for(var owner:(Set<?>)entry.getValue()) {
                var pos=owner.getClass().getDeclaredField("owner"); pos.setAccessible(true);
                var id=owner.getClass().getDeclaredField("id"); id.setAccessible(true);
                if(h.absolutePos(relative).equals(pos.get(owner))&&id.get(owner).toString().equals("dragonapi:block_entity_chunk_loading")) result.add(entry.getLongKey());
            }
            return result;
        } catch(ReflectiveOperationException ex) { throw new IllegalStateException(ex); }
    }
}
