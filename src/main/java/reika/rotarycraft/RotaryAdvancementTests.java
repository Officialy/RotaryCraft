package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Holder;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.storage.loot.BuiltInLootTables;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParamSets;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.dragonapi.auxiliary.trackers.PlayerFirstTimeTracker;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.registry.RotaryAdvancements;
import reika.rotarycraft.registry.RotaryItems;

/** Login and data-driven advancement regressions exercised on an actual server. */
final class RotaryAdvancementTests {
    private RotaryAdvancementTests() {}

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        RotaryGameTests.register(event, env, "advancement_catalog", 40, RotaryAdvancementTests::catalog);
        RotaryGameTests.register(event, env, "advancement_login_once", 40, RotaryAdvancementTests::loginOnce);
        RotaryGameTests.register(event, env, "advancement_login_after_death", 40, RotaryAdvancementTests::afterDeath);
        RotaryGameTests.register(event, env, "advancement_full_inventory", 40, RotaryAdvancementTests::fullInventory);
        RotaryGameTests.register(event, env, "advancement_handbook_use", 40, RotaryAdvancementTests::book);
        RotaryGameTests.register(event, env, "advancement_bonus_chest_injection", 40, RotaryAdvancementTests::bonusChest);
        RotaryGameTests.register(event, env, "advancement_gravel_gun_charge", 40, RotaryAdvancementTests::gunCharge);
        RotaryGameTests.register(event, env, "advancement_gravel_gun_hit", 40, RotaryAdvancementTests::gunHit);
        RotaryGameTests.register(event, env, "advancement_blast_result", 40, RotaryAdvancementTests::blastResult);
        RotaryGameTests.register(event, env, "advancement_light_bridge_fall", 40, RotaryAdvancementTests::bridgeFall);
        RotaryGameTests.register(event, env, "advancement_light_bridge_still_active", 40, RotaryAdvancementTests::activeBridge);
        RotaryGameTests.register(event, env, "advancement_light_bridge_unrelated_death", 40, RotaryAdvancementTests::otherDeath);
        RotaryGameTests.register(event, env, "advancement_light_bridge_safe_landing", 40, RotaryAdvancementTests::safeLanding);
    }

    private static ServerPlayer player(GameTestHelper h) {
        return h.makeMockServerPlayerInLevel(); // Includes the real PlayerLoggedInEvent and a packet connection.
    }

    static boolean done(ServerPlayer player, RotaryAdvancements entry) {
        var advancement = player.level().getServer().getAdvancements().get(entry.getId());
        return advancement != null && player.getAdvancements().getOrStartProgress(advancement).isDone();
    }

    private static int count(ServerPlayer player, Item item) {
        int total = 0;
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++) {
            var stack = player.getInventory().getItem(slot);
            if (stack.is(item)) total += stack.getCount();
        }
        return total;
    }

    private static void catalog(GameTestHelper h) {
        h.assertTrue(RotaryAdvancements.list.length == 46, "all 46 original milestones must be present");
        for (var entry : RotaryAdvancements.list) {
            var advancement = h.getLevel().getServer().getAdvancements().get(entry.getId());
            h.assertTrue(advancement != null && advancement.value().display().isPresent(), "missing display/advancement " + entry);
            if (entry.dependency != null)
                h.assertTrue(advancement.value().parent().orElseThrow().equals(entry.dependency.getId()), "original parent " + entry);
        }
        h.succeed();
    }

    private static void loginOnce(GameTestHelper h) {
        var player = player(h);
        h.assertTrue(count(player, RotaryItems.HANDBOOK.get()) == 1, "real login must deliver the handbook");
        PlayerFirstTimeTracker.checkPlayer(player);
        h.assertTrue(count(player, RotaryItems.HANDBOOK.get()) == 1, "repeated login must not duplicate it");
        player.getInventory().clearContent();
        PlayerFirstTimeTracker.checkPlayer(player);
        h.assertTrue(count(player, RotaryItems.HANDBOOK.get()) == 0, "throwing it away must not reset the tracker");
        h.assertTrue(ReikaPlayerAPI.getDeathPersistentNBT(player).getBooleanOr("DragonAPI_PlayerTracker_RotaryCraft_Handbook", false), "persist the original tracker ID");
        h.succeed();
    }

    private static void afterDeath(GameTestHelper h) {
        var original = player(h);
        var clone = player(h);
        clone.getInventory().clearContent();
        clone.restoreFrom(original, false);
        PlayerFirstTimeTracker.checkPlayer(clone);
        h.assertTrue(count(clone, RotaryItems.HANDBOOK.get()) == 0, "NeoForge death clone must retain the delivery flag");
        h.succeed();
    }

    private static void fullInventory(GameTestHelper h) {
        var player = player(h);
        var center = Vec3.atCenterOf(h.absolutePos(new BlockPos(3, 2, 3)));
        player.setPos(center);
        for (int slot = 0; slot < player.getInventory().getContainerSize(); slot++)
            player.getInventory().setItem(slot, new ItemStack(Items.COBBLESTONE, 64));
        ReikaPlayerAPI.getDeathPersistentNBT(player).remove("DragonAPI_PlayerTracker_RotaryCraft_Handbook");
        int before = droppedBooks(h, center);
        PlayerFirstTimeTracker.checkPlayer(player);
        h.assertTrue(droppedBooks(h, center) == before + 1, "full inventory must drop exactly one undelivered book");
        PlayerFirstTimeTracker.checkPlayer(player);
        h.assertTrue(droppedBooks(h, center) == before + 1, "dropped delivery also completes the tracker");
        h.succeed();
    }

    private static int droppedBooks(GameTestHelper h, Vec3 center) {
        return h.getLevel().getEntitiesOfClass(ItemEntity.class, AABB.ofSize(center, 8, 8, 8)).stream()
                .filter(entity -> entity.getItem().is(RotaryItems.HANDBOOK.get())).mapToInt(entity -> entity.getItem().getCount()).sum();
    }

    private static void book(GameTestHelper h) {
        var player = player(h);
        h.assertTrue(!done(player, RotaryAdvancements.RCUSEBOOK), "receiving the handbook is not reading it");
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(RotaryItems.HANDBOOK.get()));
        RotaryItems.HANDBOOK.get().use(h.getLevel(), player, InteractionHand.MAIN_HAND);
        h.assertTrue(done(player, RotaryAdvancements.RCUSEBOOK), "server-side handbook use awards the original milestone");
        h.assertTrue(!done(player, RotaryAdvancements.GPRSPAWNER), "unrelated icon/action must remain locked");
        h.succeed();
    }

    private static void bonusChest(GameTestHelper h) {
        var table = h.getLevel().getServer().reloadableRegistries().getLootTable(BuiltInLootTables.SPAWN_BONUS_CHEST);
        var params = new LootParams.Builder(h.getLevel()).withParameter(LootContextParams.ORIGIN,
                Vec3.atCenterOf(h.absolutePos(BlockPos.ZERO))).create(LootContextParamSets.CHEST);
        int steel = 0;
        for (int seed = 1; seed <= 512; seed++) {
            for (var stack : table.getRandomItems(params, seed)) {
                if (stack.is(RotaryItems.HSLA_STEEL_INGOT.get())) {
                    h.assertTrue(stack.getCount() >= 1 && stack.getCount() <= 5, "original bonus steel stack range");
                    steel++;
                }
            }
        }
        h.assertTrue(steel > 0 && steel < 100, "the modifier must add occasional steel to the actual vanilla bonus table");
        h.succeed();
    }

    private static void gunCharge(GameTestHelper h) {
        var player = player(h);
        var gun = RotaryItems.GRAVELGUN.get();
        var stack = new ItemStack(gun);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        h.assertTrue(gun.use(h.getLevel(), player, InteractionHand.MAIN_HAND) == net.minecraft.world.InteractionResult.FAIL, "uncharged gun cannot fire");
        stack.setDamageValue(4096);
        gun.use(h.getLevel(), player, InteractionHand.MAIN_HAND);
        h.assertTrue(stack.getDamageValue() == 4096, "a miss cannot consume charge");
        h.assertTrue(reika.rotarycraft.items.tools.charged.ItemGravelGun.getAttackDamage(4096) > 20, "preserve the original lethal charge threshold");
        h.succeed();
    }

    private static void gunHit(GameTestHelper h) {
        var player = player(h);
        Vec3 eyeBase = Vec3.atCenterOf(h.absolutePos(new BlockPos(3, 2, 2)));
        player.setPos(eyeBase);
        player.setYRot(0);
        player.setXRot(0);
        var zombie = h.spawn(net.minecraft.world.entity.EntityTypes.ZOMBIE, new BlockPos(3, 2, 6));
        zombie.setNoAi(true);
        var gun = RotaryItems.GRAVELGUN.get();
        var stack = new ItemStack(gun);
        stack.setDamageValue(32000);
        player.setItemInHand(InteractionHand.MAIN_HAND, stack);
        gun.use(h.getLevel(), player, InteractionHand.MAIN_HAND);
        h.assertTrue(zombie.getHealth() <= 0, "the restored gun must actually damage its ray target");
        h.assertTrue(stack.getDamageValue() == 32000 - reika.rotarycraft.items.tools.charged.ItemGravelGun.getChargeConsumed(32000), "one hit spends logarithmic charge");
        h.assertTrue(done(player, RotaryAdvancements.MASSIVEHIT), "500+ damage awards Massive Hit");
        h.succeed();
    }

    private static void blastResult(GameTestHelper h) {
        var player = player(h);
        h.setBlock(new BlockPos(3, 2, 3), reika.rotarycraft.registry.RotaryBlocks.BLAST_FURNACE.get());
        var tile = h.getBlockEntity(new BlockPos(3, 2, 3), reika.rotarycraft.blockentities.production.BlockEntityBlastFurnace.class);
        tile.getOutputInventory().setStackInSlot(0, new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get()));
        var menu = new reika.rotarycraft.gui.container.machine.inventory.ContainerBlastFurnace(1, player.getInventory(), tile);
        menu.quickMoveStack(player, 10);
        h.assertTrue(done(player, RotaryAdvancements.MAKESTEEL), "taking blast-furnace steel awards Steelmaker");
        h.succeed();
    }

    private static ServerPlayer bridgePlayer(GameTestHelper h, boolean deactivate) {
        var cookie = net.minecraft.server.network.CommonListenerCookie.createInitial(
                new com.mojang.authlib.GameProfile(java.util.UUID.randomUUID(), "test-mock-player"), false);
        var player = new ServerPlayer(h.getLevel().getServer(), h.getLevel(), cookie.gameProfile(), cookie.clientInformation()) {
            @Override public net.minecraft.world.level.GameType gameMode() { return net.minecraft.world.level.GameType.SURVIVAL; }
        };
        var connection = new net.minecraft.network.Connection(net.minecraft.network.protocol.PacketFlow.SERVERBOUND);
        new io.netty.channel.embedded.EmbeddedChannel(connection);
        h.getLevel().getServer().getPlayerList().placeNewPlayer(connection, player, cookie);
        net.minecraft.world.level.GameType.SURVIVAL.updatePlayerAbilities(player.getAbilities());
        player.connection.markClientLoaded();
        var emitterPos = new BlockPos(3, 6, 3);
        h.setBlock(emitterPos, reika.rotarycraft.registry.RotaryBlocks.LIGHT_BRIDGE.get());
        var emitter = h.getBlockEntity(emitterPos, reika.rotarycraft.blockentities.level.BlockEntityLightBridge.class);
        emitter.updateEntity(h.getLevel(), emitter.getBlockPos());
        var support = emitter.getBlockPos().relative(emitter.getFacing());
        h.getLevel().setBlockAndUpdate(support, reika.rotarycraft.registry.RotaryBlocks.BRIDGE.get().defaultBlockState());
        player.setPos(Vec3.atBottomCenterOf(support.above()));
        player.setOnGround(true);
        if (deactivate) {
            emitter.updateEntity(h.getLevel(), emitter.getBlockPos());
            h.assertTrue(h.getLevel().getBlockState(support).isAir(), "power loss must actually retract the supporting block");
        }
        return player;
    }

    private static void fatalFall(GameTestHelper h, ServerPlayer player) {
        player.setPos(player.position().add(0, -4, 0));
        player.hurtServer(h.getLevel(), player.damageSources().fall(), 1000);
        h.assertTrue(!player.isAlive(), "exercise a real server-side fall death");
    }

    private static void bridgeFall(GameTestHelper h) {
        var player = bridgePlayer(h, true);
        // The client can still report onGround while the server has removed its support.
        net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.event.tick.PlayerTickEvent.Post(player));
        fatalFall(h, player);
        h.assertTrue(done(player, RotaryAdvancements.LIGHTFALL), "fall death after support deactivation awards Long Way Down");
        h.succeed();
    }

    private static void activeBridge(GameTestHelper h) {
        var player = bridgePlayer(h, false);
        fatalFall(h, player);
        h.assertTrue(!done(player, RotaryAdvancements.LIGHTFALL), "an ordinary fall beside an active bridge is not deactivation");
        h.succeed();
    }

    private static void otherDeath(GameTestHelper h) {
        var player = bridgePlayer(h, true);
        player.setPos(player.position().add(0, -4, 0));
        player.hurtServer(h.getLevel(), player.damageSources().generic(), 1000);
        h.assertTrue(!done(player, RotaryAdvancements.LIGHTFALL), "a different cause of death cannot award the fall milestone");
        h.succeed();
    }

    private static void safeLanding(GameTestHelper h) {
        var player = bridgePlayer(h, true);
        h.runAfterDelay(3, () -> {
            h.getLevel().setBlockAndUpdate(player.blockPosition().below(), net.minecraft.world.level.block.Blocks.STONE.defaultBlockState());
            player.setOnGround(true);
            net.neoforged.neoforge.common.NeoForge.EVENT_BUS.post(new net.neoforged.neoforge.event.tick.PlayerTickEvent.Post(player));
            fatalFall(h, player);
            h.assertTrue(!done(player, RotaryAdvancements.LIGHTFALL), "landing safely clears the previous bridge fall");
            h.succeed();
        });
    }
}
