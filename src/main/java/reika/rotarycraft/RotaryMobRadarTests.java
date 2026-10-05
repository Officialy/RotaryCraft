package reika.rotarycraft;

import java.util.List;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.*;
import net.minecraft.world.entity.animal.cow.Cow;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.*;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.rotarycraft.api.interfaces.RadarJammer;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.gui.container.machine.ContainerMobRadar;
import reika.rotarycraft.registry.*;

/** Real bottom power, world scans, jammer API, server contacts and survival access. */
final class RotaryMobRadarTests {
    private static final BlockPos POS = new BlockPos(3, 2, 4);
    private RotaryMobRadarTests() {}
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        test(event, env, "bottom_coil", RotaryMobRadarTests::power);
        test(event, env, "wrong_power_face", RotaryMobRadarTests::side);
        for (long watts : new long[]{0, 8191, 8192, 9215, 9216, 262144, Long.MAX_VALUE})
            test(event, env, "range_" + watts, h -> range(h, watts));
        test(event, env, "unpowered_column", RotaryMobRadarTests::column);
        test(event, env, "square_and_outside", RotaryMobRadarTests::square);
        test(event, env, "full_world_height", RotaryMobRadarTests::height);
        test(event, env, "filters", RotaryMobRadarTests::filters);
        test(event, env, "player_filter", RotaryMobRadarTests::player);
        test(event, env, "neutral_entities", RotaryMobRadarTests::neutral);
        test(event, env, "hostile_categories", RotaryMobRadarTests::hostiles);
        test(event, env, "jammer_removal", h -> jammer(h, 0));
        test(event, env, "jammer_filter", h -> jammer(h, 1));
        test(event, env, "jammer_false", h -> jammer(h, 2));
        test(event, env, "moving_contacts", RotaryMobRadarTests::moving);
        test(event, env, "immutable_snapshots", RotaryMobRadarTests::immutable);
        test(event, env, "detached_save_sync", RotaryMobRadarTests::save);
        test(event, env, "legacy_save_defaults", RotaryMobRadarTests::legacy);
        test(event, env, "owner_hud_inventory", RotaryMobRadarTests::hud);
        test(event, env, "menu_access", RotaryMobRadarTests::menu);
        test(event, env, "survival_recipe", RotaryMobRadarTests::crafting);
        test(event, env, "survival_harvest", RotaryMobRadarTests::harvest);
        test(event, env, "collision_and_registry", RotaryMobRadarTests::shape);
        test(event, env, "subclass_hud_colors", RotaryMobRadarTests::colors);
        test(event, env, "partial_contact_sync", RotaryMobRadarTests::sync);
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event, env, "mob_radar_" + name, 100, body);
    }
    private static BlockEntityMobRadar radar(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.MOB_RADAR.get());
        return h.getBlockEntity(POS, BlockEntityMobRadar.class);
    }
    private static BlockEntityCreativeCoil coil(GameTestHelper h, BlockPos pos, Direction facing, int torque, int speed) {
        h.setBlock(pos.north(), Blocks.REDSTONE_BLOCK);
        h.setBlock(pos, RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, facing));
        var coil = h.getBlockEntity(pos, BlockEntityCreativeCoil.class);
        coil.setReleaseTorque(torque); coil.setReleaseOmega(speed);
        coil.updateEntity(h.getLevel(), coil.getBlockPos());
        return coil;
    }
    private static BlockEntityMobRadar powered(GameTestHelper h) {
        var radar = radar(h); coil(h, POS.below(), Direction.DOWN, 64, 128); tick(h, radar); return radar;
    }
    private static void tick(GameTestHelper h, BlockEntityMobRadar radar) { radar.updateEntity(h.getLevel(), radar.getBlockPos()); }
    private static <T extends LivingEntity> T spawn(GameTestHelper h, net.minecraft.world.entity.EntityType<T> type, double x, double y, double z) {
        T mob = type.create(h.getLevel(), EntitySpawnReason.COMMAND);
        var pos = h.absolutePos(POS); mob.setPos(pos.getX() + x, pos.getY() + y, pos.getZ() + z);
        if (mob instanceof Mob ai) ai.setNoAi(true); h.getLevel().addFreshEntity(mob); return mob;
    }
    private static boolean has(BlockEntityMobRadar radar, LivingEntity mob) { return radar.getEntities().contains(mob); }
    private static void power(GameTestHelper h) {
        var radar = powered(h);
        h.assertTrue(radar.MINPOWER == 8192 && radar.power == 8192 && radar.getRange() == 8, "64 Nm x 128 rad/s bottom coil must provide original 8192 W base range");
        h.succeed();
    }
    private static void side(GameTestHelper h) {
        var radar = radar(h); coil(h, POS.west(), Direction.EAST, 64, 128); tick(h, radar);
        h.assertTrue(radar.power == 0 && radar.getRange() == 0, "horizontal coil cannot feed bottom-only radar input"); h.succeed();
    }
    private static void range(GameTestHelper h, long watts) {
        var radar = radar(h); radar.power = watts;
        int expected = watts == 0 ? 0 : watts == 8191 ? 8 : watts < 9216 ? 8 : watts == 9216 ? 9 : 256;
        h.assertTrue(radar.getRange() == expected, "range must retain integer falloff and cap without overflow at " + watts); h.succeed();
    }
    private static void column(GameTestHelper h) {
        var radar = radar(h); var inside = spawn(h, EntityTypes.COW, .5, 2, .5); var outside = spawn(h, EntityTypes.COW, 2, 2, .5); tick(h, radar);
        h.assertTrue(has(radar, inside) && !has(radar, outside), "V33a scans its own column even without power"); h.succeed();
    }
    private static void square(GameTestHelper h) {
        var radar = powered(h); var corner = spawn(h, EntityTypes.COW, 7.5, 0, 7.5); var outside = spawn(h, EntityTypes.COW, 10, 0, .5); tick(h, radar);
        h.assertTrue(has(radar, corner) && !has(radar, outside), "range is an axis-aligned square, with diagonal contacts and no out-of-range entities"); h.succeed();
    }
    private static void height(GameTestHelper h) {
        var radar = powered(h); var low = spawn(h, EntityTypes.COW, .5, 0, .5); var high = spawn(h, EntityTypes.COW, .5, 0, .5);
        low.setPos(low.getX(), h.getLevel().getMinY() + 1, low.getZ()); high.setPos(high.getX(), h.getLevel().getMaxY() - 2, high.getZ());
        tick(h, radar); h.assertTrue(has(radar, low) && has(radar, high), "scan must include modern negative Y and contacts above legacy Y=255"); h.succeed();
    }
    private static void filters(GameTestHelper h) {
        var radar = powered(h); var cow = spawn(h, EntityTypes.COW, 1, 0, 1); var zombie = spawn(h, EntityTypes.ZOMBIE, 2, 0, 1);
        radar.animal = false; tick(h, radar); h.assertTrue(!has(radar, cow) && has(radar, zombie), "animal filter must not affect hostiles");
        radar.hostile = false; radar.animal = true; tick(h, radar); h.assertTrue(has(radar, cow) && !has(radar, zombie), "hostile filter must not affect animals");
        radar.animal = false; tick(h, radar); h.assertTrue(!has(radar, cow) && !has(radar, zombie), "disabled categories must disappear on next scan"); h.succeed();
    }
    private static void player(GameTestHelper h) {
        var radar = radar(h); var player = h.makeMockPlayer(GameType.SURVIVAL);
        h.assertTrue(radar.acceptsEntity(player), "players enabled by default"); radar.player = false;
        h.assertTrue(!radar.acceptsEntity(player), "player filter must be independent of animal/hostile filters"); h.succeed();
    }
    private static void neutral(GameTestHelper h) {
        var radar = powered(h); var villager = spawn(h, EntityTypes.VILLAGER, 1, 0, 1); var bat = spawn(h, EntityTypes.BAT, 2, 0, 1); tick(h, radar);
        h.assertTrue(!has(radar, villager) && !has(radar, bat), "modern Mob base class must not classify villagers and bats as hostiles"); h.succeed();
    }
    private static void hostiles(GameTestHelper h) {
        var radar = radar(h);
        for (var type : List.of(EntityTypes.SLIME, EntityTypes.GHAST, EntityTypes.WITCH, EntityTypes.WITHER, EntityTypes.ENDER_DRAGON)) {
            var entity = type.create(h.getLevel(), EntitySpawnReason.COMMAND);
            h.assertTrue(radar.acceptsEntity(entity), "non-Monster hostile category must remain visible: " + type);
            radar.hostile = false; h.assertTrue(!radar.acceptsEntity(entity), "hostile filter must also apply to bosses and slimes"); radar.hostile = true;
        } h.succeed();
    }
    private static final class JammingCow extends Cow implements RadarJammer {
        boolean enabled = true;
        BlockPos scannedAt;
        JammingCow(Level level) { super(EntityTypes.COW, level); }
        @Override public boolean jamRadar(Level world, BlockPos pos) { scannedAt = pos; return enabled; }
    }
    private static void jammer(GameTestHelper h, int mode) {
        var radar = powered(h); var jammer = new JammingCow(h.getLevel()); var pos = radar.getBlockPos();
        jammer.setPos(pos.getX() + .5, pos.getY() + 2, pos.getZ() + .5); jammer.setNoAi(true); h.getLevel().addFreshEntity(jammer); tick(h, radar);
        h.assertTrue(radar.isJammed() && pos.equals(jammer.scannedAt), "selected jammer must receive actual radar position and activate static");
        if (mode == 0) jammer.discard(); else if (mode == 1) radar.animal = false; else jammer.enabled = false;
        tick(h, radar); h.assertTrue(!radar.isJammed(), "jamming must reset after removal, filtering or API false"); h.succeed();
    }
    private static void moving(GameTestHelper h) {
        var radar = powered(h); var cow = spawn(h, EntityTypes.COW, 1, 0, 1); tick(h, radar);
        var contact = radar.getContacts().stream().filter(c -> c.entityId() == cow.getId()).findFirst().orElseThrow();
        h.assertTrue(contact.x() == cow.getX() && contact.y() == cow.getY() && contact.z() == cow.getZ() && contact.icon() == 92 && contact.color() == 0x433525, "synced contact must carry real coordinates and original cow icon/color");
        cow.setPos(cow.getX() + 11, cow.getY(), cow.getZ()); tick(h, radar);
        h.assertTrue(!has(radar, cow) && radar.getContacts().stream().noneMatch(c -> c.entityId() == cow.getId()), "entities that leave range must lose both live and synced contacts"); h.succeed();
    }
    private static void immutable(GameTestHelper h) {
        var radar = powered(h); spawn(h, EntityTypes.COW, 1, 0, 1); tick(h, radar);
        boolean immutable = false; try { radar.getEntities().clear(); } catch (UnsupportedOperationException expected) { immutable = true; }
        h.assertTrue(immutable, "API scan list must be immutable"); immutable = false;
        try { radar.getContacts().clear(); } catch (UnsupportedOperationException expected) { immutable = true; }
        h.assertTrue(immutable, "render snapshot must be immutable"); h.succeed();
    }
    private static void save(GameTestHelper h) {
        var radar = powered(h); spawn(h, EntityTypes.COW, 1, 0, 1); radar.owner = "Radar owner"; radar.player = false; radar.setPlacer(h.makeMockPlayer(GameType.SURVIVAL)); tick(h, radar);
        var saved = radar.saveWithoutMetadata(h.getLevel().registryAccess());
        var restored = new BlockEntityMobRadar(radar.getBlockPos(), radar.getBlockState());
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(restored.getLevel() == null && restored.owner.equals(radar.owner) && !restored.player && restored.animal && restored.hostile
                && restored.getPlacerID().equals(radar.getPlacerID()) && restored.power == radar.power && restored.getContacts().equals(radar.getContacts()), "detached save/sync must preserve ownership, filters, mechanical state and contacts");
        h.assertTrue(restored.getEntities().isEmpty(), "saved contact IDs must not create stale live entity references"); h.succeed();
    }
    private static void legacy(GameTestHelper h) {
        var radar = radar(h); var saved = radar.saveWithoutMetadata(h.getLevel().registryAccess());
        saved.remove("hostile"); saved.remove("animal"); saved.remove("player"); saved.remove("radarContacts");
        radar.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(radar.hostile && radar.animal && radar.player && radar.getContacts().isEmpty(), "older saves must retain default categories without contact list"); h.succeed();
    }
    private static void hud(GameTestHelper h) {
        var radar = radar(h); var owner = h.makeMockPlayer(GameType.SURVIVAL); radar.setPlacer(owner);
        h.assertTrue(!radar.canShowHud(owner), "HUD requires tracker");
        owner.getInventory().setItem(9, new ItemStack(RotaryItems.MOTION.get()));
        h.assertTrue(radar.canShowHud(owner), "Registered Motion Tracker unlocks the owner HUD through the actual item tag");
        var other = h.makeMockPlayer(GameType.SURVIVAL); other.getInventory().setItem(9, new ItemStack(RotaryItems.MOTION.get()));
        h.assertTrue(!radar.canShowHud(other), "tracker must not reveal another player's radar HUD");
        owner.getInventory().setItem(9, ItemStack.EMPTY); owner.setItemInHand(InteractionHand.OFF_HAND, new ItemStack(RotaryItems.MOTION.get()));
        h.assertTrue(!radar.canShowHud(owner), "V33a HUD requires main inventory rather than offhand"); h.succeed();
    }
    private static void menu(GameTestHelper h) {
        var radar = radar(h); var player = h.makeMockPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(radar.getBlockPos()));
        var menu = new ContainerMobRadar(1, player.getInventory(), radar);
        h.assertTrue(menu.slots.isEmpty() && menu.stillValid(player), "radar menu must expose no inventory slots and allow nearby access");
        player.setPos(player.getX() + 20, player.getY(), player.getZ()); h.assertTrue(!menu.stillValid(player), "distant player loses access"); h.succeed();
    }
    private static void crafting(GameTestHelper h) {
        var input = CraftingInput.of(3, 3, List.of(ItemStack.EMPTY, new ItemStack(RotaryItems.RADAR_UNIT.get()), new ItemStack(RotaryItems.SCREEN.get()),
                ItemStack.EMPTY, new ItemStack(RotaryItems.HSLA_STEEL_GEAR_2x.get()), ItemStack.EMPTY,
                new ItemStack(RotaryItems.HSLA_PLATE.get()), new ItemStack(RotaryItems.CIRCUIT_BOARD.get()), new ItemStack(RotaryItems.HSLA_PLATE.get())));
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream().anyMatch(r -> r.value().matches(input, h.getLevel()) && r.value().assemble(input).is(RotaryBlocks.MOB_RADAR.get().asItem())), "faithful V33a shaped recipe must craft a survival radar"); h.succeed();
    }
    private static void harvest(GameTestHelper h) {
        radar(h); var player = (net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL); player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(POS)), "iron pickaxe must harvest radar");
        h.assertTrue(h.getEntities(EntityTypes.ITEM, POS, 3).stream().filter(e -> e.getItem().is(RotaryBlocks.MOB_RADAR.get().asItem())).mapToInt(e -> e.getItem().getCount()).sum() == 1, "loot table and mining tags must drop exactly one radar"); h.succeed();
    }
    private static void shape(GameTestHelper h) {
        var radar = radar(h);
        h.assertTrue(h.getBlockState(POS).getShape(h.getLevel(), radar.getBlockPos()).max(Direction.Axis.Y) == .75
                && radar.getMachine() == MachineRegistry.MOBRADAR && radar.getRedstoneOverride() == 0, "radar registry must expose original three-quarter collision height and comparator behavior"); h.succeed();
    }
    private static void colors(GameTestHelper h) {
        var mob = new JammingCow(h.getLevel());
        int color = reika.dragonapi.libraries.ReikaEntityHelper.mobToColor(mob);
        h.assertTrue(color != 0xffffff && color == reika.dragonapi.libraries.ReikaEntityHelper.mobToColor(new JammingCow(h.getLevel())), "modded cow subclass must inherit a stable cached shade rather than white");
        var ocelot = EntityTypes.OCELOT.create(h.getLevel(), EntitySpawnReason.COMMAND);
        h.assertTrue(reika.dragonapi.libraries.ReikaEntityHelper.mobToColor(ocelot) == 0xf2c56e, "original ocelot color must survive the port"); h.succeed();
    }
    private static void sync(GameTestHelper h) {
        var radar = powered(h); var jammer = new JammingCow(h.getLevel()); jammer.setPos(Vec3.atCenterOf(radar.getBlockPos()));
        h.getLevel().addFreshEntity(jammer); tick(h, radar);
        var client = new BlockEntityMobRadar(radar.getBlockPos(), radar.getBlockState());
        client.applySyncTag(radar.saveWithoutMetadata(h.getLevel().registryAccess()));
        h.assertTrue(client.getContacts().equals(radar.getContacts()) && client.isJammed() && client.power == radar.power, "periodic sync must deliver contacts, power and static without client entity tracking");
        jammer.discard(); tick(h, radar); client.applySyncTag(radar.saveWithoutMetadata(h.getLevel().registryAccess()));
        h.assertTrue(!client.isJammed() && client.getContacts().stream().noneMatch(c -> c.entityId() == jammer.getId()), "sync must remove stale contacts and jam state"); h.succeed();
    }
}
