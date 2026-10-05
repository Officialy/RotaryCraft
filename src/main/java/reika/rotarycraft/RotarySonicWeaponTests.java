package reika.rotarycraft;

import java.util.List;
import java.util.ArrayList;
import java.util.function.Consumer;
import net.minecraft.core.*;
import net.minecraft.gametest.framework.*;
import net.minecraft.util.ProblemReporter;
import net.minecraft.util.RandomSource;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.Vec3;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.weaponry.BlockEntitySonicWeapon;
import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;
import reika.rotarycraft.gui.container.machine.ContainerSonic;
import reika.rotarycraft.registry.*;

/** Live shaft input, original intensity thresholds, player immunity, persistence and survival access. */
final class RotarySonicWeaponTests {
    private static final BlockPos POS = new BlockPos(4, 3, 4);
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        for (Direction side : Direction.values()) test(event, env, "input_" + side.getName(), h -> input(h, side));
        test(event, env, "summative_torque", h -> summative(h, false));
        test(event, env, "unequal_speeds", h -> summative(h, true));
        test(event, env, "power_gate", RotarySonicWeaponTests::powerGate);
        test(event, env, "torque_clamp", RotarySonicWeaponTests::clamp);
        test(event, env, "power_loss", RotarySonicWeaponTests::powerLoss);
        test(event, env, "long_pitch_and_volume", RotarySonicWeaponTests::longValues);
        for (long threshold : new long[]{BlockEntitySonicWeapon.EYEDAMAGE, BlockEntitySonicWeapon.BRAINDAMAGE, BlockEntitySonicWeapon.LETHALVOLUME}) {
            test(event, env, "threshold_" + threshold, h -> threshold(h, threshold, true));
            test(event, env, "below_" + threshold, h -> threshold(h, threshold, false));
        }
        test(event, env, "inverse_square", RotarySonicWeaponTests::falloff);
        test(event, env, "symmetric_range", RotarySonicWeaponTests::range);
        test(event, env, "helmet_protection", h -> protection(h, false));
        test(event, env, "creative_protection", h -> protection(h, true));
        test(event, env, "hostile_confusion", RotarySonicWeaponTests::confusion);
        test(event, env, "lung_damage", RotarySonicWeaponTests::lung);
        for (Block host : new Block[]{Blocks.STONE, Blocks.COBBLESTONE, Blocks.STONE_BRICKS, Blocks.CRACKED_STONE_BRICKS, Blocks.MOSSY_STONE_BRICKS, Blocks.CHISELED_STONE_BRICKS})
            test(event, env, "silverfish_" + net.minecraft.core.registries.BuiltInRegistries.BLOCK.getKey(host).getPath(), h -> silverfish(h, host));
        test(event, env, "packet_long_round_trip", RotarySonicWeaponTests::packet);
        test(event, env, "detached_save_and_sync", RotarySonicWeaponTests::save);
        test(event, env, "legacy_defaults", RotarySonicWeaponTests::legacy);
        test(event, env, "validated_controls", RotarySonicWeaponTests::controls);
        test(event, env, "original_recipe", RotarySonicWeaponTests::recipe);
        test(event, env, "survival_harvest", RotarySonicWeaponTests::harvest);
        test(event, env, "decibel_conversion", RotarySonicWeaponTests::decibels);
    }
    private static void test(RegisterGameTestsEvent e, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(e, env, "sonic_weapon_" + name, 100, body);
    }
    private static BlockEntitySonicWeapon machine(GameTestHelper h) {
        h.setBlock(POS, RotaryBlocks.SONIC_WEAPON.get()); return h.getBlockEntity(POS, BlockEntitySonicWeapon.class);
    }
    private static BlockEntityCreativeCoil coil(GameTestHelper h, Direction side, int torque, int speed) {
        BlockPos pos = POS.relative(side);
        h.setBlock(pos.relative(side), Blocks.REDSTONE_BLOCK);
        h.setBlock(pos, RotaryBlocks.CREATIVE_COIL.get().defaultBlockState().setValue(BlockRotaryCraftMachine.FACING, side.getAxis().isVertical() ? side : side.getOpposite()));
        var coil = h.getBlockEntity(pos, BlockEntityCreativeCoil.class);
        coil.setReleaseTorque(torque); coil.setReleaseOmega(speed); coil.updateEntity(h.getLevel(), coil.getBlockPos()); return coil;
    }
    private static BlockEntitySonicWeapon powered(GameTestHelper h) {
        var machine = machine(h); coil(h, Direction.DOWN, 1 << 24, 1); tick(h, machine); return machine;
    }
    private static void tick(GameTestHelper h, BlockEntitySonicWeapon machine) { machine.updateEntity(h.getLevel(), machine.getBlockPos()); }
    private static <T extends LivingEntity> T spawn(GameTestHelper h, EntityType<T> type, double dx, double dy, double dz) {
        T entity = type.create(h.getLevel(), EntitySpawnReason.COMMAND);
        Vec3 center = Vec3.atCenterOf(h.absolutePos(POS)); entity.setPos(center.add(dx, dy, dz));
        if (entity instanceof Mob mob) mob.setNoAi(true);
        h.getLevel().addFreshEntity(entity); return entity;
    }
    private static void input(GameTestHelper h, Direction side) {
        var m = machine(h); coil(h, side, 512, 512); tick(h, m);
        h.assertTrue(m.MINPOWER == 262144 && m.power == 262144 && m.torque == 512 && m.omega == 512, "all six full-cube faces must accept real original shaft power: " + side); h.succeed();
    }
    private static void summative(GameTestHelper h, boolean unequal) {
        var m = machine(h); coil(h, Direction.DOWN, 512, 512); coil(h, Direction.UP, 512, unequal ? 1024 : 512); tick(h, m);
        h.assertTrue(unequal ? m.power == 0 : m.torque == 1024 && m.omega == 512 && m.power == 524288,
                "equal-speed inputs add torque; unequal speeds reject the combined supply"); h.succeed();
    }
    private static void powerGate(GameTestHelper h) {
        var m = machine(h); var coil = coil(h, Direction.DOWN, 262143, 1); m.setvolume = Long.MAX_VALUE;
        var cow = spawn(h, EntityTypes.COW, 1, 0, 0); tick(h, m);
        h.assertTrue(cow.isAlive() && !cow.hasEffect(MobEffects.BLINDNESS), "below 262144 W no effects may apply");
        coil.setReleaseTorque(262144); coil.updateEntity(h.getLevel(), coil.getBlockPos()); tick(h, m);
        h.assertTrue(cow.isAlive() && cow.hasEffect(MobEffects.NAUSEA), "V33a integer rounding makes 262144 Nm slightly below lethal intensity");
        m.setvolume = Long.MAX_VALUE; coil.setReleaseTorque(262145); coil.updateEntity(h.getLevel(), coil.getBlockPos()); tick(h, m);
        h.assertTrue(!cow.isAlive(), "one more Nm crosses the rounded lethal threshold"); h.succeed();
    }
    private static void clamp(GameTestHelper h) {
        var m = machine(h); coil(h, Direction.DOWN, 1024, 256); m.setvolume = Long.MAX_VALUE; m.setpitch = Long.MAX_VALUE; tick(h, m);
        h.assertTrue(m.setvolume == 1024 * BlockEntitySonicWeapon.INTENSITYPERTORQUE && m.setpitch == 256L * 8192,
                "requests must clamp to actual torque and angular speed at the powered tick"); h.succeed();
    }
    private static void powerLoss(GameTestHelper h) {
        var m = powered(h); m.setvolume = Long.MAX_VALUE; tick(h, m);
        h.setBlock(POS.below(), Blocks.AIR); var cow = spawn(h, EntityTypes.COW, 1, 0, 0); tick(h, m);
        h.assertTrue(m.power == 0 && m.getVolume() == 0 && cow.isAlive(), "shaft removal clears mechanical input and effects despite a stored request"); h.succeed();
    }
    private static void longValues(GameTestHelper h) {
        var m = machine(h); var source = coil(h, Direction.DOWN, Integer.MAX_VALUE, Integer.MAX_VALUE); m.setvolume = Long.MAX_VALUE; m.setpitch = Long.MAX_VALUE; tick(h, m);
        h.assertTrue(m.power == (long)source.getReleaseTorque() * Integer.MAX_VALUE && m.getMaxPitch() == (long)Integer.MAX_VALUE * 8192
                && m.setvolume == BlockEntitySonicWeapon.INTENSITYPERTORQUE * source.getReleaseTorque() && m.getVolume() > Integer.MAX_VALUE,
                "maximum coil values must preserve long power, pitch and intensity without overflow"); h.succeed();
    }
    private static void threshold(GameTestHelper h, long threshold, boolean exact) {
        var m = powered(h); var cow = spawn(h, EntityTypes.COW, 1, 0, 0); m.setvolume = (threshold - (exact ? 0 : 1)) * 1000000; tick(h, m);
        boolean effect = threshold == BlockEntitySonicWeapon.EYEDAMAGE ? cow.hasEffect(MobEffects.BLINDNESS)
                : threshold == BlockEntitySonicWeapon.BRAINDAMAGE ? cow.hasEffect(MobEffects.NAUSEA) : !cow.isAlive();
        h.assertTrue(effect == exact, "threshold is inclusive with integer volume conversion: " + threshold); h.succeed();
    }
    private static void falloff(GameTestHelper h) {
        var m = powered(h); m.setvolume = 8000000L * 1000000;
        var near = spawn(h, EntityTypes.COW, 1, 0, 0); var far = spawn(h, EntityTypes.COW, 3, 0, 0); tick(h, m);
        h.assertTrue(near.hasEffect(MobEffects.NAUSEA) && !far.hasEffect(MobEffects.BLINDNESS)
                && m.getIntensityAt(near.position()) == 9 * m.getIntensityAt(far.position()), "inverse-square effects must use X/Y/Z relative to machine center"); h.succeed();
    }
    private static void range(GameTestHelper h) {
        var m = powered(h); m.setvolume = Long.MAX_VALUE;
        var minus = spawn(h, EntityTypes.COW, -15, 0, 0); var plus = spawn(h, EntityTypes.COW, 15, 0, 0); var outside = spawn(h, EntityTypes.COW, 18, 0, 0); tick(h, m);
        h.assertTrue(m.getRange() == 16 && m.getMaxRange() == 16 && minus.hasEffect(MobEffects.BLINDNESS)
                && plus.hasEffect(MobEffects.BLINDNESS) && !outside.hasEffect(MobEffects.BLINDNESS), "scan must expand symmetrically by the original fixed 16 blocks"); h.succeed();
    }
    private static void protection(GameTestHelper h, boolean creative) {
        var m = powered(h); m.setvolume = Long.MAX_VALUE;
        var player = h.makeMockPlayer(creative ? GameType.CREATIVE : GameType.SURVIVAL);
        if (!creative) player.setItemSlot(EquipmentSlot.HEAD, new ItemStack(Items.LEATHER_HELMET));
        h.assertTrue(!m.isPlayerVulnerable(player), "creative or any equipped helmet must protect the player");
        if (!creative) { player.setItemSlot(EquipmentSlot.HEAD, ItemStack.EMPTY); h.assertTrue(m.isPlayerVulnerable(player), "helmet removal restores susceptibility"); }
        h.succeed();
    }
    private static void confusion(GameTestHelper h) {
        var m = powered(h); m.setvolume = 4000000L * 1000000;
        var a = spawn(h, EntityTypes.ZOMBIE, 1, 0, 0); var b = spawn(h, EntityTypes.SKELETON, 2, 0, 0); tick(h, m);
        h.assertTrue(a.getTarget() == b && a.getLastHurtByMob() == b && a.hasEffect(MobEffects.NAUSEA)
                && a.getEffect(MobEffects.NAUSEA).getAmplifier() == 10 && a.getEffect(MobEffects.MINING_FATIGUE).getAmplifier() == 3
                && a.getEffect(MobEffects.SLOWNESS).getAmplifier() == 1, "brain damage restores original potion amplitudes and nearest-hostile confusion"); h.succeed();
    }
    private static void lung(GameTestHelper h) {
        var m = powered(h); m.setvolume = BlockEntitySonicWeapon.LUNGDAMAGE * 1000000;
        var cow = spawn(h, EntityTypes.COW, 1, 0, 0); float health = cow.getHealth();
        long seed = 0; while (RandomSource.create(seed).nextInt(40) != 0) seed++;
        h.getLevel().getRandom().setSeed(seed); tick(h, m);
        h.assertTrue(cow.getHealth() == health - 1, "original one-in-forty pulmonary damage must deal one drowning damage"); h.succeed();
    }
    private static void silverfish(GameTestHelper h, Block host) {
        var m = powered(h); m.setvolume = BlockEntitySonicWeapon.SILVERFISHKILL * 1000000;
        long seed = 54321; var random = RandomSource.create(seed);
        var target = POS.offset(random.nextInt(13) - 6, random.nextInt(13) - 6, random.nextInt(13) - 6);
        h.setBlock(target, InfestedBlock.infestedStateByHost(host.defaultBlockState()));
        h.getLevel().getRandom().setSeed(seed); tick(h, m);
        h.assertTrue(h.getBlockState(target).is(host), "sampled infestation must retain its host rather than spawn a silverfish");
        h.assertTrue(h.getLevel().getEntitiesOfClass(ExperienceOrb.class, new net.minecraft.world.phys.AABB(m.getBlockPos()).inflate(2)).stream().mapToInt(ExperienceOrb::getValue).sum() == 5,
                "a killed hidden silverfish must grant its original five XP"); h.succeed();
    }
    private static void packet(GameTestHelper h) {
        var m = machine(h); var player = h.makeMockServerPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(m.getBlockPos()));
        player.containerMenu = new ContainerSonic(1, player.getInventory(), m);
        long value = 123456789012345L;
        try {
            var bytes = new java.io.ByteArrayOutputStream(); var out = new java.io.DataOutputStream(bytes);
            out.writeInt(PacketRegistry.SONICVOLUME.ordinal()); out.writeLong(value);
            out.writeInt(m.getBlockPos().getX()); out.writeInt(m.getBlockPos().getY()); out.writeInt(m.getBlockPos().getZ());
            var packet = new reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket(bytes.toByteArray());
            packet.init(reika.dragonapi.auxiliary.PacketTypes.DATA, reika.dragonapi.libraries.io.ReikaPacketHelper.getPipeline(RotaryCraft.packetChannel));
            var buffer = new net.minecraft.network.FriendlyByteBuf(io.netty.buffer.Unpooled.buffer());
            try {
                packet.encode(buffer); var decoded = reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket.decode(buffer);
                new PacketHandlerCore().handleData(decoded, h.getLevel(), player);
            } finally { buffer.release(); }
            h.assertTrue(m.setvolume == value, "actual channel encode/decode and server handler must deliver all 64 volume bits");
            player.containerMenu = player.inventoryMenu;
            new PacketHandlerCore().handleData(packet, h.getLevel(), player);
            h.assertTrue(m.setvolume == value, "a stale request must not bypass the menu guard");
        } catch (java.io.IOException error) { throw new IllegalStateException(error); }
        h.succeed();
    }
    private static void save(GameTestHelper h) {
        var m = powered(h); m.setvolume = 987654321098765L; m.setpitch = 1234567890123L;
        var saved = m.saveWithoutMetadata(h.getLevel().registryAccess()); var restored = new BlockEntitySonicWeapon(m.getBlockPos(), m.getBlockState());
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(restored.getLevel() == null && restored.setvolume == m.setvolume && restored.setpitch == m.setpitch && restored.power == m.power, "detached save must retain original long keys and power");
        var client = new BlockEntitySonicWeapon(m.getBlockPos(), m.getBlockState()); client.applySyncTag(saved);
        h.assertTrue(client.setvolume == m.setvolume && client.setpitch == m.setpitch && client.torque == m.torque, "block sync must preserve full-width requests and mechanics"); h.succeed();
    }
    private static void legacy(GameTestHelper h) {
        var m = machine(h); var saved = m.saveWithoutMetadata(h.getLevel().registryAccess()); saved.remove("setfrequency"); saved.remove("setvolume");
        m.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(m.setvolume == 0 && m.setpitch == 0, "missing legacy settings use original silent defaults"); h.succeed();
    }
    private static void controls(GameTestHelper h) {
        var m = machine(h); var player = h.makeMockServerPlayer(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(m.getBlockPos()));
        var menu = new ContainerSonic(1, player.getInventory(), m);
        h.assertTrue(menu.slots.isEmpty() && !menu.setValue(player, false, 123), "controls require the actual active menu");
        player.containerMenu = menu;
        h.assertTrue(menu.setValue(player, false, 123456789012345L) && m.setvolume == 123456789012345L
                && menu.setValue(player, true, 9876543210L) && m.setpitch == 9876543210L && !menu.setValue(player, false, -1), "controls accept full-width nonnegative values and reject negative values");
        player.setPos(player.getX() + 20, player.getY(), player.getZ());
        h.assertTrue(!menu.setValue(player, false, 4), "distant player cannot change sonic settings");
        player.setPos(Vec3.atCenterOf(m.getBlockPos())); h.setBlock(POS, Blocks.AIR);
        h.assertTrue(!menu.setValue(player, false, 4), "removed or replaced machine cannot accept stale controls"); h.succeed();
    }
    private static void recipe(GameTestHelper h) {
        var plate = new ItemStack(RotaryItems.HSLA_PLATE.get()); var sonar = new ItemStack(RotaryItems.SONAR_UNIT.get());
        var input = CraftingInput.of(3, 3, List.of(plate, sonar, plate, sonar, new ItemStack(RotaryItems.TURBINE.get()), sonar, plate, sonar, plate));
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream()
                .anyMatch(r -> r.value().matches(input, h.getLevel()) && r.value().assemble(input).is(RotaryBlocks.SONIC_WEAPON.get().asItem())), "V33a psp/sts/psp recipe must craft the weapon"); h.succeed();
    }
    private static void harvest(GameTestHelper h) {
        machine(h); var player = (net.minecraft.server.level.ServerPlayer)h.makeMockServerPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));
        h.assertTrue(player.gameMode.destroyBlock(h.absolutePos(POS)), "survival pickaxe must harvest the weapon");
        h.assertTrue(h.getEntities(EntityTypes.ITEM, POS, 3).stream().filter(e -> e.getItem().is(RotaryBlocks.SONIC_WEAPON.get().asItem())).mapToInt(e -> e.getItem().getCount()).sum() == 1, "harvest drops exactly one weapon"); h.succeed();
    }
    private static void decibels(GameTestHelper h) {
        h.assertTrue(BlockEntitySonicWeapon.volumeFromDecibels(0) == 1 && BlockEntitySonicWeapon.volumeFromDecibels(140) == 100000000000000L
                && BlockEntitySonicWeapon.volumeFromDecibels(999) == Long.MAX_VALUE && BlockEntitySonicWeapon.decibelsFromVolume(0) == 0
                && BlockEntitySonicWeapon.decibelsFromVolume(100000000000000L) == 140, "original dB conversion must remain bounded and use full-width volume"); h.succeed();
    }
}
