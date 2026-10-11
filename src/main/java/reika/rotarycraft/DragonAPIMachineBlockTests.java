package reika.rotarycraft;

import io.netty.channel.embedded.EmbeddedChannel;
import io.netty.util.ReferenceCountUtil;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetDataPacket;
import net.minecraft.network.protocol.game.ClientboundContainerSetContentPacket;
import net.minecraft.resources.Identifier;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.ComparatorBlockEntity;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluids;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import net.minecraft.world.phys.BlockHitResult;
import net.neoforged.neoforge.common.ModConfigSpec;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.base.BlockEntityBase;
import reika.dragonapi.base.BlockTEBase;
import reika.dragonapi.instantiable.io.ControlledConfig;
import reika.dragonapi.libraries.level.ReikaBlockHelper;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;
import reika.rotarycraft.blockentities.production.BlockEntityBlastFurnace;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.RotaryBlocks;

/** Registered machine hooks exercised through native states, tickers and player connections. */
final class DragonAPIMachineBlockTests {
    private static final BlockPos POS = new BlockPos(3, 1, 3);

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_machine_mining", 40, DragonAPIMachineBlockTests::mining);
        RotaryGameTests.register(event, environment, "dragonapi_machine_comparators", 40, DragonAPIMachineBlockTests::comparators);
        RotaryGameTests.register(event, environment, "dragonapi_machine_family", 40, DragonAPIMachineBlockTests::family);
        RotaryGameTests.register(event, environment, "dragonapi_machine_flag_sync", 40, DragonAPIMachineBlockTests::flagSync);
        RotaryGameTests.register(event, environment, "dragonapi_machine_pipe_hardness", 40, DragonAPIMachineBlockTests::pipeHardness);
        RotaryGameTests.register(event, environment, "dragonapi_machine_menu_sync", 40, DragonAPIMachineBlockTests::menuSync);
    }

    private static BlockEntityBase place(GameTestHelper h, Identifier id) {
        var block = BuiltInRegistries.BLOCK.getValue(id);
        h.assertTrue(block instanceof BlockTEBase, "registered machine uses the shared base: " + id);
        h.setBlock(POS, block);
        return h.getBlockEntity(POS, BlockEntityBase.class);
    }

    private static void mining(GameTestHelper h) {
        var player = h.makeMockPlayer(GameType.SURVIVAL);
        player.setItemInHand(net.minecraft.world.InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_PICKAXE));
        for (var block : List.of(RotaryBlocks.DC_ENGINE.get(), RotaryBlocks.FLUID_PIPE.get())) {
            h.setBlock(POS, block);
            var tile = h.getBlockEntity(POS, BlockEntityBase.class);
            var pos = h.absolutePos(POS); var state = h.getBlockState(POS);
            float baseline = state.getDestroyProgress(player, h.getLevel(), pos);
            h.assertTrue(baseline > 0, "ordinary registered machines remain mineable");
            tile.setUnmineable(true);
            h.assertTrue(state.getDestroyProgress(player, h.getLevel(), pos) == 0
                    && ReikaBlockHelper.isUnbreakable(h.getLevel(), pos, block, player),
                    "mapmaker flag reaches both native mining and shared machine-breaker queries");
            var saved = tile.saveWithoutMetadata(h.getLevel().registryAccess());
            var copy = (BlockEntityBase)((BlockTEBase)block).newBlockEntity(pos, state);
            copy.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
            h.assertTrue(copy.isUnMineable(), "native saved data retains no_mine");
            tile.setUnmineable(false);
            h.assertTrue(state.getDestroyProgress(player, h.getLevel(), pos) == baseline
                    && !ReikaBlockHelper.isUnbreakable(h.getLevel(), pos, block, player),
                    "clearing the flag restores exactly the configured mining behavior");
        }
        h.getLevel().removeBlockEntity(h.absolutePos(POS));
        h.assertTrue(h.getBlockState(POS).getDestroyProgress(player, h.getLevel(), h.absolutePos(POS)) > 0
                && h.getBlockState(POS).getAnalogOutputSignal(h.getLevel(), h.absolutePos(POS), Direction.NORTH) == 0,
                "missing tile retains ordinary mining and zero comparator output");
        h.succeed();
    }

    private static void comparators(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.RESERVOIR.get());
        var tile = h.getBlockEntity(POS, BlockEntityReservoir.class);
        BlockPos pos = h.absolutePos(POS), direct = POS.east(), indirect = POS.south(2);
        tile.addLiquid(BlockEntityReservoir.CAPACITY, Fluids.WATER);
        h.assertTrue(tile.getRedstoneOverride() == 15 && tile.getComparatorOverride() == 0,
                "computed fullness and the initially unsampled comparator cache remain distinct");
        h.setBlock(direct.below(), Blocks.STONE);
        h.setBlock(direct, Blocks.COMPARATOR.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.WEST));
        h.setBlock(POS.south(), Blocks.STONE);
        h.setBlock(indirect.below(), Blocks.STONE);
        h.setBlock(indirect, Blocks.COMPARATOR.defaultBlockState().setValue(BlockStateProperties.HORIZONTAL_FACING, Direction.NORTH));
        h.runAfterDelay(6, () -> {
            for (Direction face : Direction.values())
                h.assertTrue(h.getBlockState(POS).hasAnalogOutputSignal()
                        && h.getBlockState(POS).getAnalogOutputSignal(h.getLevel(), pos, face) == 15,
                        "native machine states expose the sampled full-tank signal on every face");
            h.assertTrue(h.getBlockEntity(direct, ComparatorBlockEntity.class).getOutputSignal() == 15
                    && h.getBlockEntity(indirect, ComparatorBlockEntity.class).getOutputSignal() == 15,
                    "native comparators sample both adjacent machines and machines behind a conductor");
            tile.removeLiquid(BlockEntityReservoir.CAPACITY);
        });
        h.runAfterDelay(18, () -> {
            h.assertTrue(tile.getComparatorOverride() == 0
                    && h.getBlockEntity(direct, ComparatorBlockEntity.class).getOutputSignal() == 0
                    && h.getBlockEntity(indirect, ComparatorBlockEntity.class).getOutputSignal() == 0,
                    "eight-tick sampling notifies both comparators when a tank empties");
            h.succeed();
        });
    }

    private static void family(GameTestHelper h) {
        var player = h.makeMockPlayer(GameType.SURVIVAL);
        for (String name : List.of("rotarycraft:dc_engine", "reactorcraft:coolant_cell",
                "electricraft:redstone_battery", "chromaticraft:crystal_brewer")) {
            Identifier id = Identifier.parse(name);
            if (!net.neoforged.fml.ModList.get().isLoaded(id.getNamespace())) continue;
            BlockEntityBase tile = place(h, id);
            var state = h.getBlockState(POS); var pos = h.absolutePos(POS);
            h.assertTrue(state.hasAnalogOutputSignal(), "shared comparator contract is present in " + id);
            tile.updateEntity();
            h.assertTrue(state.getAnalogOutputSignal(h.getLevel(), pos, Direction.NORTH)
                    == Math.clamp(tile.getRedstoneOverride(), 0, 15), "shared cache exposes the authored machine signal in " + id);
            tile.setUnmineable(true);
            h.assertTrue(state.getDestroyProgress(player, h.getLevel(), pos) == 0,
                    "shared mining protection applies in " + id);
            tile.setUnmineable(false);
        }
        h.succeed();
    }

    private static <P> List<P> packets(ServerPlayer player, Class<P> type) {
        var channel = (EmbeddedChannel)player.connection.getConnection().channel();
        channel.runPendingTasks();
        List<P> result = new ArrayList<>();
        Object outbound;
        while ((outbound = channel.readOutbound()) != null) {
            if (type.isInstance(outbound)) result.add(type.cast(outbound));
            ReferenceCountUtil.release(outbound);
        }
        channel.checkException(); return result;
    }

    private static void flagSync(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.DC_ENGINE.get());
        var tile = h.getBlockEntity(POS, BlockEntityBase.class); var pos = h.absolutePos(POS);
        var player = DragonAPIAuditTests.player(h); player.setPos(Vec3.atCenterOf(pos));
        packets(player, ClientboundBlockEntityDataPacket.class);
        tile.setUnmineable(true);
        var sent = packets(player, ClientboundBlockEntityDataPacket.class).stream().filter(p -> p.getPos().equals(pos)).toList();
        h.assertTrue(sent.size() == 1 && sent.getFirst().getTag().getBooleanOr("no_mine", false),
                "changing protection delivers one native tile packet to the nearby player");
        tile.setUnmineable(true);
        h.assertTrue(packets(player, ClientboundBlockEntityDataPacket.class).isEmpty(), "setting an unchanged flag does not send another packet");
        h.assertTrue(tile.getUpdateTag(h.getLevel().registryAccess()).getBooleanOr("no_mine", false),
                "initial chunk synchronization also contains protection");
        tile.setUnmineable(false);
        sent = packets(player, ClientboundBlockEntityDataPacket.class).stream().filter(p -> p.getPos().equals(pos)).toList();
        h.assertTrue(sent.size() == 1 && !sent.getFirst().getTag().getBooleanOr("no_mine", true),
                "clearing protection reaches the client instead of retaining stale mining state");
        Throwable workerFailure = java.util.concurrent.CompletableFuture.supplyAsync(() -> {
            try { tile.setUnmineable(true); return (Throwable)null; }
            catch (IllegalStateException error) { return error; }
        }).join();
        h.assertTrue(workerFailure instanceof IllegalStateException && !tile.isUnMineable()
                && packets(player, ClientboundBlockEntityDataPacket.class).isEmpty(),
                "worker changes fail before mutating attached state or dispatching packets");
        h.succeed();
    }

    private static void menuSync(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.BLAST_FURNACE.get());
        var tile = h.getBlockEntity(POS, BlockEntityBlastFurnace.class); var pos = h.absolutePos(POS);
        var player = (ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(player.getGameProfile(), false);
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new EmbeddedChannel(connection);
        net.neoforged.neoforge.network.registration.NetworkRegistry.configureMockConnection(connection);
        h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        player.setPos(Vec3.atCenterOf(pos));
        tile.getContainerData().set(0, 42); tile.setTemperature(875);
        tile.setItem(0, new ItemStack(Items.GUNPOWDER, 3));
        packets(player, Object.class);
        var result = h.getBlockState(POS).useWithoutItem(h.getLevel(), player,
                new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false));
        h.assertTrue(result == net.minecraft.world.InteractionResult.SUCCESS
                && player.containerMenu instanceof reika.rotarycraft.gui.container.machine.inventory.ContainerBlastFurnace,
                "empty-hand interaction opens the actual registered machine menu");
        var opening = packets(player, Object.class);
        h.assertTrue(opening.stream().anyMatch(p -> p instanceof ClientboundContainerSetContentPacket contents
                && contents.items().getFirst().is(Items.GUNPOWDER) && contents.items().getFirst().getCount() == 3),
                "opening receives native machine inventory contents");
        h.assertTrue(hasMenuData(opening, 0, 42) && hasMenuData(opening, 1, 875),
                "opening receives current progress and temperature through native menu data");
        h.assertTrue(opening.stream().noneMatch(ClientboundBlockEntityDataPacket.class::isInstance),
                "opening does not broadcast redundant full block entity NBT");
        tile.setTemperature(900); tile.getContainerData().set(0, 43);
        player.containerMenu.broadcastChanges();
        var update = packets(player, Object.class);
        h.assertTrue(hasMenuData(update, 0, 43) && hasMenuData(update, 1, 900),
                "subsequent menu changes reach the same connection without a click-triggered tile sync");
        player.closeContainer();
        h.setBlock(POS, RotaryBlocks.FLUID_PIPE.get());
        packets(player, Object.class);
        h.assertTrue(h.getBlockState(POS).useWithoutItem(h.getLevel(), player,
                new BlockHitResult(Vec3.atCenterOf(pos), Direction.UP, pos, false)) == net.minecraft.world.InteractionResult.PASS
                && packets(player, ClientboundBlockEntityDataPacket.class).isEmpty(),
                "passive empty-hand pipe clicks pass through without broadcasting tile NBT");
        h.succeed();
    }

    private static boolean hasMenuData(List<Object> packets, int id, int value) {
        return packets.stream().anyMatch(packet ->
                packet instanceof ClientboundContainerSetDataPacket data && data.getId() == id && data.getValue() == value
                || packet instanceof net.minecraft.network.protocol.common.ClientboundCustomPayloadPacket wrapper
                && wrapper.payload() instanceof net.neoforged.neoforge.network.payload.AdvancedContainerSetDataPayload advanced
                && advanced.dataId() == id && advanced.value() == value);
    }

    @SuppressWarnings("unchecked")
    private static void pipeHardness(GameTestHelper h) {
        // Exercise the live authoritative value without writing the disposable TOML file.
        try {
            var field = ControlledConfig.class.getDeclaredField("modernValues"); field.setAccessible(true);
            var values = (Map<Integer, ModConfigSpec.ConfigValue<?>>)field.get(RotaryCraft.config);
            var setting = (ModConfigSpec.ConfigValue<Object>)values.get(ConfigRegistry.PIPEHARDNESS.ordinal());
            Object saved = setting.get();
            try {
                h.setBlock(POS, RotaryBlocks.FLUID_PIPE.get());
                var tile = h.getBlockEntity(POS, BlockEntityBase.class); var pos = h.absolutePos(POS);
                var player = h.makeMockPlayer(GameType.SURVIVAL); var state = h.getBlockState(POS);
                setting.set(0.0);
                h.assertTrue(state.getDestroyProgress(player, h.getLevel(), pos) == 1, "zero pipe hardness mines instantly");
                tile.setUnmineable(true);
                h.assertTrue(state.getDestroyProgress(player, h.getLevel(), pos) == 0,
                        "instant mining cannot bypass mapmaker protection");
                tile.setUnmineable(false);
                setting.set(1.0); float atOne = state.getDestroyProgress(player, h.getLevel(), pos);
                setting.set(0.5); float atHalf = state.getDestroyProgress(player, h.getLevel(), pos);
                h.assertTrue(atOne > 0 && Math.abs(atHalf - 2 * atOne) < 0.000001,
                        "live nonzero hardness retains native player/tool mining speed");
                setting.set(2.0);
                h.assertTrue(state.getDestroyProgress(player, h.getLevel(), pos) == atOne, "high hardness clamps to one");
                setting.set(-0.5);
                h.assertTrue(state.getDestroyProgress(player, h.getLevel(), pos) == 1, "negative hardness clamps to zero");
            } finally { setting.set(saved); }
        } catch (ReflectiveOperationException error) { throw new IllegalStateException(error); }
        h.succeed();
    }
}
