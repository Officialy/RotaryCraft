package reika.rotarycraft;

import java.util.*;
import java.util.function.Consumer;
import com.mojang.serialization.JsonOps;
import net.minecraft.core.*;
import net.minecraft.core.component.DataComponents;
import net.minecraft.core.registries.Registries;
import net.minecraft.gametest.framework.*;
import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.effect.*;
import net.minecraft.world.entity.*;
import net.minecraft.world.item.*;
import net.minecraft.world.item.crafting.*;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.*;
import net.minecraft.world.level.storage.*;
import net.minecraft.world.phys.*;
import net.neoforged.neoforge.common.NeoForge;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.event.level.block.BreakBlockEvent;
import reika.rotarycraft.api.event.RailgunImpactEvent;
import reika.rotarycraft.auxiliary.RailgunBlockDamage;
import reika.rotarycraft.auxiliary.recipemanagers.RailgunAmmoIngredient;
import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun;
import reika.rotarycraft.entities.*;
import reika.rotarycraft.items.ItemRailGunAmmo;
import reika.rotarycraft.registry.*;

/** Tests the real registered ammunition, reloadable recipes, projectile flight, impacts and persistence. */
final class RotaryRailgunTests {
    private static final BlockPos GUN = new BlockPos(2, 6, 4), IMPACT = new BlockPos(5, 6, 4);
    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env) {
        test(event, env, "variants_and_legacy_stacks", RotaryRailgunTests::variants);
        for (int tier = 0; tier < 16; tier++) {
            final int power = tier;
            test(event, env, "fire_tier_" + tier, h -> fire(h, power));
            test(event, env, "recipe_tier_" + tier, h -> recipe(h, power));
        }
        test(event, env, "insufficient_power_and_torque", RotaryRailgunTests::unpowered);
        test(event, env, "native_shell_factory_and_recipe", RotaryRailgunTests::shell);
        test(event, env, "swept_collision", RotaryRailgunTests::collision);
        test(event, env, "constant_velocity", RotaryRailgunTests::flight);
        test(event, env, "expiry_impacts", RotaryRailgunTests::expiry);
        test(event, env, "disarm_effects_damage_and_knockback", RotaryRailgunTests::mob);
        test(event, env, "player_keeps_equipment", RotaryRailgunTests::player);
        test(event, env, "impact_event_once_before_terrain", RotaryRailgunTests::event);
        test(event, env, "stone_fatigue_roundtrip", RotaryRailgunTests::fatigue);
        test(event, env, "fatigue_replacement", RotaryRailgunTests::replacement);
        test(event, env, "claims_block_damage_and_fatigue", RotaryRailgunTests::protection);
        test(event, env, "tier_thresholds", RotaryRailgunTests::thresholds);
        test(event, env, "unbreakable_liquid_and_gun_immunity", RotaryRailgunTests::immunity);
        test(event, env, "projectile_save_and_spawn_sync", RotaryRailgunTests::save);
        test(event, env, "high_tier_loot", RotaryRailgunTests::loot);
        test(event, env, "extension_veto", h -> extension(h, true));
        test(event, env, "extension_knockback", h -> extension(h, false));
        test(event, env, "shell_native_explosion_contract", RotaryRailgunTests::shellImpact);
        for (int tier : new int[]{3,14,15}) test(event, env, "family_recursive_radius_" + tier, h -> recursive(h,tier));
        test(event, env, "fatigue_same_block_replacement", RotaryRailgunTests::sameBlockReplacement);
        test(event, env, "handbook_shapeless_choices", RotaryRailgunTests::shapelessDisplay);
        test(event, env, "handbook_cooking_input", RotaryRailgunTests::cookingDisplay);
        test(event, env, "difficulty_conditioned_recipe_data", RotaryRailgunTests::difficulty);
        for (var option : new ConfigRegistry[]{ConfigRegistry.ATTACKBLOCKS,ConfigRegistry.RAILGUNDAMAGE})
            test(event, env, "disabled_" + option.name().toLowerCase(Locale.ROOT), h -> damageGate(h,option));
    }
    private static void test(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> env, String name, Consumer<GameTestHelper> body) {
        RotaryGameTests.register(event, env, "railgun_" + name, 80, body);
    }
    private static BlockEntityRailGun gun(GameTestHelper h) {
        h.setBlock(GUN, RotaryBlocks.RAILGUN.get());
        var gun = h.getBlockEntity(GUN, BlockEntityRailGun.class); gun.power = gun.MINPOWER; gun.torque = 4096; return gun;
    }
    private static EntityRailGunShot slug(GameTestHelper h, BlockEntityRailGun gun, int tier) {
        return new EntityRailGunShot(h.getLevel(), Vec3.atCenterOf(h.absolutePos(IMPACT)), new Vec3(4, 0, 0), gun, ItemRailGunAmmo.getType(tier));
    }
    private static void variants(GameTestHelper h) {
        int[] damage={1,2,3,4,5,6,7,9,13,26,75,268,1037,4110,16399,Integer.MAX_VALUE};
        int[] torque={22,32,45,64,90,128,181,256,362,512,724,1024,1448,2048,2896,4096};
        for (int tier = 0; tier < 16; tier++) {
            var type = ItemRailGunAmmo.getType(tier); var stack = type.getItem();
            h.assertTrue(ItemRailGunAmmo.power(stack) == tier && type.getMass() == 1 << tier && stack.getMaxStackSize() == 16,
                    "tier identity, original mass and stack limit");
            h.assertTrue(EntityRailGunShot.damageForTier(tier)==damage[tier] && type.getRequiredTorque()==torque[tier], "original damage and torque table");
            var old = new ItemStack(RotaryItems.RAILGUN_AMMO.get()); old.set(DataComponents.DAMAGE, tier);
            h.assertTrue(new RailgunAmmoIngredient(tier).test(old) && ItemRailGunAmmo.power(old) == tier,
                    "old damage metadata remains a valid exact-tier ingredient");
            old.set(RotaryDataComponents.RAILGUN_POWER.get(), 15 - tier);
            h.assertTrue(ItemRailGunAmmo.power(old) == 15 - tier, "native tier wins over stale metadata");
            if (tier > 0) h.assertTrue(type.compareTo(ItemRailGunAmmo.getType(tier - 1)) > 0, "strongest affordable type orders by tier");
        }
        h.assertTrue(new RailgunAmmoIngredient(0).test(new ItemStack(RotaryItems.RAILGUN_AMMO.get())), "plain old base ammunition still crafts");
        h.assertTrue(ItemRailGunAmmo.getType(15).getRequiredTorque() == 4096 && EntityRailGunShot.damageForTier(15) == Integer.MAX_VALUE,
                "highest source tier torque and damage"); h.succeed();
    }
    private static void fire(GameTestHelper h, int tier) {
        var gun = gun(h); gun.torque = ItemRailGunAmmo.getType(tier).getRequiredTorque();
        var stack = ItemRailGunAmmo.stack(tier); stack.setCount(2); gun.setItem(0, stack);
        if (tier < 15) gun.setItem(1, ItemRailGunAmmo.stack(tier + 1));
        if (tier > 0) gun.setItem(2, ItemRailGunAmmo.stack(tier - 1));
        Vec3 target = Vec3.atLowerCornerOf(gun.getBlockPos()).add(20, 0, 0);
        gun.fire(h.getLevel(), new double[]{target.x, target.y, target.z, 1});
        var shots = h.getEntities(RotaryEntities.RAILGUN.get(), GUN, 3);
        h.assertTrue(shots.size() == 1 && shots.getFirst().getType() == RotaryEntities.RAILGUN.get()
                && shots.getFirst().getPower() == tier && shots.getFirst().getDeltaMovement().equals(new Vec3(4, 0, 0)),
                "actual registered slug uses selected stack, not torque-derived mass, with source launch speed");
        h.assertTrue(gun.getItem(0).getCount() == 1 && (tier == 15 || gun.getItem(1).getCount() == 1), "only one selected round consumed"); shots.forEach(Entity::discard); h.succeed();
    }
    private static void unpowered(GameTestHelper h) {
        var gun = gun(h); gun.setItem(0, ItemRailGunAmmo.stack(15)); gun.torque = 4095;
        h.assertTrue(!gun.hasAmmo(), "unaffordable rounds are not usable");
        gun.fire(h.getLevel(), new double[]{gun.getBlockPos().getX()+20,gun.getBlockPos().getY(),gun.getBlockPos().getZ(),1});
        gun.torque = 4096; gun.power = gun.MINPOWER - 1;
        gun.fire(h.getLevel(), new double[]{gun.getBlockPos().getX()+20,gun.getBlockPos().getY(),gun.getBlockPos().getZ(),1});
        h.assertTrue(gun.getItem(0).getCount() == 1 && h.getEntities(RotaryEntities.RAILGUN.get(), GUN, 3).isEmpty(), "failed shots conserve ammunition"); h.succeed();
    }
    private static void recipe(GameTestHelper h, int tier) {
        var recipe = h.getLevel().getServer().getRecipeManager().recipeMap().byKey(ResourceKey.create(Registries.RECIPE,
                Identifier.fromNamespaceAndPath("rotarycraft", "railgun_ammo_tier_" + tier))).value();
        var inputs = new ArrayList<ItemStack>(Collections.nCopies(9, ItemStack.EMPTY));
        if (tier == 0) {
            for (int index : new int[]{0,1,3}) inputs.set(index, new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get()));
        } else {
            Item material = tier <= 3 ? Items.OAK_PLANKS : tier <= 6 ? Items.STONE : tier <= 9 ? Items.IRON_INGOT
                    : tier <= 12 ? Items.GOLD_INGOT : RotaryItems.BEDROCK_ALLOY_INGOT.get();
            int[] positions = tier % 3 == 1 ? new int[]{0,8} : tier % 3 == 2 ? new int[]{0,2,6,8} : new int[]{0,1,2,3,5,6,7,8};
            for (int index : positions) inputs.set(index, new ItemStack(material));
            inputs.set(4, ItemRailGunAmmo.stack(tier - 1));
        }
        var input = CraftingInput.of(3,3,inputs);
        h.assertTrue(((CraftingRecipe)recipe).matches(input,h.getLevel()), "source pattern and material match actual reloadable recipe");
        ItemStack result = ((CraftingRecipe)recipe).assemble(input);
        h.assertTrue(ItemRailGunAmmo.power(result) == tier && result.getCount() == (tier == 0 ? 3 : DifficultyEffects.RAILGUNCRAFT.getInt()), "source output mass and difficulty count");
        if (tier > 0) {
            var displayed = reika.dragonapi.libraries.ReikaRecipeHelper.getPermutedRecipeArray(recipe);
            h.assertTrue(displayed != null && displayed[4] != null && ItemRailGunAmmo.power(displayed[4]) == tier - 1
                    && displayed[4].is(RotaryItems.RAILGUN_AMMO.get()), "actual handbook helper preserves center slot and previous-tier components");
            inputs.set(4, ItemRailGunAmmo.stack(tier));
            h.assertTrue(!((CraftingRecipe)recipe).matches(CraftingInput.of(3,3,inputs),h.getLevel()), "a different tier cannot shortcut the upgrade chain");
        }
        h.succeed();
    }
    private static void shell(GameTestHelper h) {
        var gun = gun(h); gun.setItem(0, new ItemStack(RotaryItems.SHELL.get()));
        var pos = gun.getBlockPos(); gun.fire(h.getLevel(), new double[]{pos.getX()+20,pos.getY(),pos.getZ(),1});
        var shells = h.getEntities(RotaryEntities.EXPLOSIVE_SHELL.get(), GUN, 3);
        h.assertTrue(shells.size() == 1 && shells.getFirst().getDeltaMovement().length() == 4 && gun.isEmpty(), "shell factory creates a moving native shell"); shells.forEach(Entity::discard);
        var s = new ItemStack(RotaryItems.HSLA_STEEL_INGOT.get());
        var input = CraftingInput.of(3,3,List.of(ItemStack.EMPTY,s,ItemStack.EMPTY,s,new ItemStack(RotaryItems.NITRATE.get()),s,ItemStack.EMPTY,s,ItemStack.EMPTY));
        h.assertTrue(h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream()
                .anyMatch(r -> r.value().matches(input,h.getLevel()) && r.value().assemble(input).is(RotaryItems.SHELL.get()) && r.value().assemble(input).getCount() == 16), "original sixteen-shell steel/nitrate recipe"); h.succeed();
    }
    private static void collision(GameTestHelper h) {
        var shot = slug(h,gun(h),0); shot.setPos(Vec3.atCenterOf(h.absolutePos(IMPACT)).subtract(2,0,0));
        h.setBlock(IMPACT, Blocks.BEDROCK); shot.tick();
        h.assertTrue(shot.isRemoved() && shot.getX() <= h.absolutePos(IMPACT).getX(), "swept four-block flight cannot tunnel through a wall"); h.succeed();
    }
    private static void flight(GameTestHelper h) {
        var shot = slug(h,gun(h),7); shot.setPos(shot.position().subtract(2,0,0)); Vec3 start = shot.position(); shot.tick();
        h.assertTrue(!shot.isRemoved() && shot.position().equals(start.add(4,0,0)) && shot.getDeltaMovement().equals(new Vec3(4,0,0)), "flight has no fireball acceleration or drag: start="+start+", end="+shot.position()+", velocity="+shot.getDeltaMovement()+", removed="+shot.isRemoved()+", cell="+h.getBlockState(IMPACT)); h.succeed();
    }
    private static void expiry(GameTestHelper h) {
        var shot = slug(h,gun(h),0); shot.setDeltaMovement(Vec3.ZERO);
        for (int tick = 0; tick < 80; tick++) shot.tick();
        h.assertTrue(!shot.isRemoved(), "source flight remains alive through eighty ticks"); shot.tick();
        h.assertTrue(shot.isRemoved(), "eighty-first tick impacts"); h.succeed();
    }
    private static void mob(GameTestHelper h) {
        var gun = gun(h); var mob = EntityTypes.COW.create(h.getLevel(),EntitySpawnReason.COMMAND); mob.setNoAi(true);
        mob.setPos(Vec3.atCenterOf(h.absolutePos(IMPACT))); mob.addEffect(new MobEffectInstance(MobEffects.REGENERATION,200));
        mob.setItemSlot(EquipmentSlot.MAINHAND,new ItemStack(Items.DIAMOND)); mob.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.IRON_HELMET));
        h.getLevel().addFreshEntity(mob); slug(h,gun,3).onHit(null);
        h.assertTrue(mob.getHealth() == 6 && mob.getActiveEffects().isEmpty() && mob.getItemBySlot(EquipmentSlot.HEAD).isEmpty()
                && mob.getItemBySlot(EquipmentSlot.MAINHAND).isEmpty(), "source tier-three damage, potion stripping and disarm occur once");
        h.assertTrue(h.getEntities(EntityTypes.ITEM,IMPACT,2).stream().anyMatch(i -> i.getItem().is(Items.DIAMOND)) && mob.getDeltaMovement().equals(new Vec3(.8,0,0)), "equipment drops and momentum scales with mass tier"); h.succeed();
    }
    private static void player(GameTestHelper h) {
        var player = reika.dragonapi.test.GameTestPlayers.joinedCreative(h); player.setGameMode(GameType.SURVIVAL); player.setPos(Vec3.atCenterOf(h.absolutePos(IMPACT)));
        player.setItemSlot(EquipmentSlot.HEAD,new ItemStack(Items.DIAMOND_HELMET));
        slug(h,gun(h),0).onHit(null); h.assertTrue(player.getItemBySlot(EquipmentSlot.HEAD).is(Items.DIAMOND_HELMET), "players keep armor"); h.getLevel().getServer().getPlayerList().remove(player); h.succeed();
    }
    private static void event(GameTestHelper h) {
        var gun = gun(h); h.setBlock(IMPACT,Blocks.OAK_PLANKS); int[] count = {0};
        Consumer<RailgunImpactEvent> listener = e -> { if (e.world == h.getLevel() && e.blockX == h.absolutePos(IMPACT).getX()) { count[0]++; h.assertTrue(e.projectileMass == 8 && h.getBlockState(IMPACT).is(Blocks.OAK_PLANKS), "public event carries mass before block mutation"); } };
        NeoForge.EVENT_BUS.addListener(listener); try { var shot=slug(h,gun,3);shot.onHit(null);shot.onHit(null); }
        finally { NeoForge.EVENT_BUS.unregister(listener); }
        h.assertTrue(count[0] == 1 && h.getBlockState(IMPACT).isAir(), "one event and impact per projectile"); h.succeed();
    }
    private static void fatigue(GameTestHelper h) {
        var gun = gun(h); h.setBlock(IMPACT,Blocks.STONE); var data=RailgunBlockDamage.get(h.getLevel());
        data.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);
        var ops = h.getLevel().registryAccess().createSerializationContext(JsonOps.INSTANCE);
        var saved=RailgunBlockDamage.CODEC.encodeStart(ops,data).getOrThrow();
        var restored=RailgunBlockDamage.CODEC.parse(ops,saved).getOrThrow(); restored.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);
        h.assertTrue(h.getBlockState(IMPACT).is(Blocks.STONE), "two hits preserve stone and persist fatigue");
        restored.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);
        h.assertTrue(h.getBlockState(IMPACT).is(Blocks.COBBLESTONE), "third original-tier hit converts stone without treating granite as fatigue"); h.succeed();
    }
    private static void replacement(GameTestHelper h) {
        var gun=gun(h);var data=RailgunBlockDamage.get(h.getLevel());h.setBlock(IMPACT,Blocks.STONE);
        data.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun); data.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);
        h.setBlock(IMPACT,Blocks.DIRT);data.impact(h.getLevel(),h.absolutePos(IMPACT),3,gun);
        h.assertTrue(h.getBlockState(IMPACT).is(Blocks.DIRT), "replacement starts its own fatigue"); h.succeed();
    }
    private static void extension(GameTestHelper h, boolean veto) {
        var gun=gun(h);var target=new TargetCow(h.getLevel(),veto);target.setNoAi(true);target.setPos(Vec3.atCenterOf(h.absolutePos(IMPACT)));
        target.addEffect(new MobEffectInstance(MobEffects.REGENERATION,200));h.getLevel().addFreshEntity(target);
        slug(h,gun,3).onHit(null);
        h.assertTrue(target.hits==1 && target.mass==8 && target.source==gun, "public extension receives one impact with source and mass");
        h.assertTrue(veto ? target.getHealth()==10 && target.hasEffect(MobEffects.REGENERATION)
                : target.getHealth()==6 && target.getDeltaMovement().equals(new Vec3(1.6,0,0)) && target.syncVelocity,
                "veto prevents processing; accepted impact uses and syncs extension knockback");h.succeed();
    }
    private static void shellImpact(GameTestHelper h) {
        var gun=gun(h);var target=new TargetCow(h.getLevel(),true);target.setNoAi(true);target.setPos(Vec3.atCenterOf(h.absolutePos(IMPACT)));
        h.getLevel().addFreshEntity(target);int[] explosions={0};
        Consumer<net.neoforged.neoforge.event.level.ExplosionEvent.Start> listener=e->{
            if(e.getLevel()==h.getLevel()) {h.assertTrue(e.getExplosion().radius()==8, "native shell requests original eight-block explosion");explosions[0]++;e.setCanceled(true);}
        };
        NeoForge.EVENT_BUS.addListener(listener);try {
            var shell=new EntityExplosiveShell(h.getLevel(),target.position(),new Vec3(4,0,0),gun);shell.onHit(null);
            h.assertTrue(shell.isRemoved() && explosions[0]==1 && target.hits==1 && target.mass==6,
                    "shell goes through native explosion cancellation and original extension callback");
        } finally {NeoForge.EVENT_BUS.unregister(listener);}h.succeed();
    }
    /** A server-only extension consumer; other turret callbacks are outside this fixture's tests. */
    private static final class TargetCow extends net.minecraft.world.entity.animal.cow.Cow implements reika.rotarycraft.api.interfaces.TargetEntity {
        private final boolean veto;private int hits,mass;private net.minecraft.world.level.block.entity.BlockEntity source;
        private TargetCow(net.minecraft.world.level.Level level,boolean veto) {super(EntityTypes.COW,level);this.veto=veto;}
        @Override public boolean onRailgunImpact(net.minecraft.world.level.block.entity.BlockEntity gun,reika.rotarycraft.api.interfaces.RailGunAmmo.RailGunAmmoType ammo) {hits++;source=gun;mass=ammo.getMass();return veto;}
        @Override public double getKnockbackMultiplier(net.minecraft.world.level.block.entity.BlockEntity gun,reika.rotarycraft.api.interfaces.RailGunAmmo.RailGunAmmoType ammo) {return 2;}
        @Override public boolean shouldTarget(net.minecraft.world.level.block.entity.BlockEntity gun,UUID owner) {return true;}
        @Override public void onLaserBeam(net.minecraft.world.level.block.entity.BlockEntity gun) {}
        @Override public void onFreeze(net.minecraft.world.level.block.entity.BlockEntity gun) {}
        @Override public void flakShot(net.minecraft.world.level.block.entity.BlockEntity gun) {}
    }
    private static void protection(GameTestHelper h) {
        var gun=gun(h);h.setBlock(IMPACT,Blocks.STONE);
        Consumer<BreakBlockEvent> listener=e->{if(e.getPos().equals(h.absolutePos(IMPACT)))e.setCanceled(true);};
        NeoForge.EVENT_BUS.addListener(listener);try { for(int i=0;i<4;i++) RailgunBlockDamage.get(h.getLevel()).impact(h.getLevel(),h.absolutePos(IMPACT),5,gun); }
        finally {NeoForge.EVENT_BUS.unregister(listener);}
        RailgunBlockDamage.get(h.getLevel()).impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);
        h.assertTrue(h.getBlockState(IMPACT).is(Blocks.STONE), "canceled break events neither mutate terrain nor advance fatigue"); h.succeed();
    }
    private static void thresholds(GameTestHelper h) {
        var gun=gun(h);var data=RailgunBlockDamage.get(h.getLevel());
        Block[] blocks={Blocks.GLASS,Blocks.OAK_PLANKS,Blocks.GRAVEL};
        for(int i=0;i<blocks.length;i++) {
            h.setBlock(IMPACT,blocks[i]);data.impact(h.getLevel(),h.absolutePos(IMPACT),i+1,gun);
            h.assertTrue(h.getBlockState(IMPACT).is(blocks[i]), "subthreshold hard family remains");
            data.impact(h.getLevel(),h.absolutePos(IMPACT),i+2,gun);h.assertTrue(h.getBlockState(IMPACT).isAir(), "original family threshold breaks block");
        } h.succeed();
    }
    private static void immunity(GameTestHelper h) {
        var gun=gun(h); for(Block block:new Block[]{Blocks.BEDROCK,Blocks.WATER,RotaryBlocks.RAILGUN.get()}) {
            h.setBlock(IMPACT,block);RailgunBlockDamage.get(h.getLevel()).impact(h.getLevel(),h.absolutePos(IMPACT),15,gun);
            h.assertTrue(h.getBlockState(IMPACT).is(block), "even largest slug preserves liquid, bedrock and cannons");
        } h.succeed();
    }
    private static void save(GameTestHelper h) {
        var shot=slug(h,gun(h),12);var output=TagValueOutput.createWithContext(ProblemReporter.DISCARDING,h.getLevel().registryAccess());shot.saveWithoutId(output);
        var restored=new EntityRailGunShot(RotaryEntities.RAILGUN.get(),h.getLevel());restored.load(TagValueInput.create(ProblemReporter.DISCARDING,h.getLevel().registryAccess(),output.buildResult()));
        var buffer=new RegistryFriendlyByteBuf(io.netty.buffer.Unpooled.buffer(),h.getLevel().registryAccess());
        try {
            shot.writeSpawnData(buffer);var tracked=new EntityRailGunShot(RotaryEntities.RAILGUN.get(),h.getLevel());tracked.readSpawnData(buffer);
            h.assertTrue(restored.getPower()==12 && restored.getDeltaMovement().equals(new Vec3(4,0,0)) && tracked.getAmmoType().getMass()==4096,
                    "native entity NBT and complex spawn data preserve ammunition identity and velocity");
        } finally {buffer.release();} h.succeed();
    }
    private static void loot(GameTestHelper h) {
        var gun=gun(h);h.setBlock(IMPACT,Blocks.OBSIDIAN);slug(h,gun,14).onHit(null);
        h.assertTrue(h.getBlockState(IMPACT).isAir() && h.getEntities(EntityTypes.ITEM,IMPACT,2).stream().anyMatch(i->i.getItem().is(Items.OBSIDIAN)), "high tier destroys and drops original arbitrary breakable blocks");h.succeed();
    }
    private static void recursive(GameTestHelper h, int tier) {
        var gun=gun(h);for(int i=0;i<=6;i++) h.setBlock(IMPACT.above(i),Blocks.OAK_PLANKS);
        RailgunBlockDamage.get(h.getLevel()).impact(h.getLevel(),h.absolutePos(IMPACT),tier,gun);
        h.assertTrue(h.getBlockState(IMPACT.above(5)).isAir() && h.getBlockState(IMPACT.above(6)).is(Blocks.OAK_PLANKS),
                "source wood flood reaches five blocks beyond the seed at all eligible tiers, before arbitrary high-tier breaking");
        for(int i=0;i<=6;i++)h.setBlock(IMPACT.above(i),Blocks.AIR);h.succeed();
    }
    private static void sameBlockReplacement(GameTestHelper h) {
        var gun=gun(h);var data=RailgunBlockDamage.get(h.getLevel());h.setBlock(IMPACT,Blocks.STONE);
        data.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);data.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);
        h.setBlock(IMPACT,Blocks.AIR);h.setBlock(IMPACT,Blocks.STONE);data.impact(h.getLevel(),h.absolutePos(IMPACT),5,gun);
        h.assertTrue(h.getBlockState(IMPACT).is(Blocks.STONE), "native replacement notifications clear fatigue even when the same block is placed again");h.succeed();
    }
    private static void shapelessDisplay(GameTestHelper h) {
        var input=CraftingInput.of(2,2,List.of(new ItemStack(Items.PAPER),new ItemStack(Items.PAPER),new ItemStack(Items.PAPER),new ItemStack(Items.LEATHER)));
        var recipe=h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.CRAFTING).stream()
                .filter(r->r.value().matches(input,h.getLevel()) && r.value().assemble(input).is(Items.BOOK)).findFirst().orElseThrow().value();
        var displays=reika.dragonapi.libraries.ReikaRecipeHelper.getPermutedRecipeArray(recipe);
        h.assertTrue(displays!=null && Arrays.stream(displays).filter(Objects::nonNull).filter(s->s.is(Items.PAPER)).count()==3
                && Arrays.stream(displays).filter(Objects::nonNull).filter(s->s.is(Items.LEATHER)).count()==1, "native shapeless ingredients are not indexed by their recipe slot into a one-choice array");h.succeed();
    }
    private static void cookingDisplay(GameTestHelper h) {
        var input=new SingleRecipeInput(new ItemStack(Items.RAW_IRON));
        var recipe=h.getLevel().getServer().getRecipeManager().recipeMap().byType(RecipeType.SMELTING).stream()
                .filter(r->r.value().matches(input,h.getLevel()) && r.value().assemble(input).is(Items.IRON_INGOT)).findFirst().orElseThrow().value();
        var displays=reika.dragonapi.libraries.ReikaRecipeHelper.getPermutedRecipeArray(recipe);
        h.assertTrue(displays!=null && displays[4]!=null && displays[4].is(Items.RAW_IRON), "cooking display shows raw input rather than the result in both slots");h.succeed();
    }
    @SuppressWarnings("unchecked")
    private static void difficulty(GameTestHelper h) {
        try {
            var field=reika.dragonapi.instantiable.io.ControlledConfig.class.getDeclaredField("modernValues");field.setAccessible(true);
            var values=(Map<Integer,net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<?>>)field.get(RotaryCraft.config);
            var value=(net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<Integer>)values.get(ConfigRegistry.DIFFICULTY.ordinal());int before=value.get();
            try {
                var ops=h.getLevel().registryAccess().createSerializationContext(com.mojang.serialization.JsonOps.INSTANCE);
                for(int setting=0;setting<=4;setting++) {
                    value.set(setting);int accepted=0;
                    for(String suffix:new String[]{"_easy","","_hard"}) {
                        var id=Identifier.parse("rotarycraft:recipe/railgun_ammo_tier_1"+suffix+".json");
                        try(var reader=h.getLevel().getServer().getResourceManager().getResource(id).orElseThrow().openAsReader()) {
                            var json=com.google.gson.JsonParser.parseReader(reader);
                            var decoded=net.neoforged.neoforge.common.conditions.ICondition.getConditionally(Recipe.DIRECT_CODEC,ops,json);
                            if(decoded.isPresent()) {
                                accepted++;var output=reika.dragonapi.libraries.ReikaRecipeHelper.getRecipeOutput(decoded.get());
                                h.assertTrue(output.getCount()==DifficultyEffects.RAILGUNCRAFT.getInt(), "actual generated conditional recipe uses server difficulty yield");
                            }
                        }
                    }
                    h.assertTrue(accepted==1, "exactly one original yield is accepted, including NOOB/UNPLAYABLE medium fallback");
                }
            } finally {value.set(before);}
        } catch(ReflectiveOperationException|java.io.IOException error) {throw new IllegalStateException(error);}h.succeed();
    }
    @SuppressWarnings("unchecked")
    private static void damageGate(GameTestHelper h,ConfigRegistry option) {
        try {
            var field=reika.dragonapi.instantiable.io.ControlledConfig.class.getDeclaredField("modernValues");field.setAccessible(true);
            var values=(Map<Integer,net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<?>>)field.get(RotaryCraft.config);
            var value=(net.neoforged.neoforge.common.ModConfigSpec.ConfigValue<Boolean>)values.get(option.ordinal());boolean before=value.get();
            try {
                value.set(false);h.assertTrue(!option.getState(), "native configured damage gate is disabled");
                var gun=gun(h);h.setBlock(IMPACT,Blocks.OAK_PLANKS);slug(h,gun,3).onHit(null);
                h.assertTrue(h.getBlockState(IMPACT).is(Blocks.OAK_PLANKS), "disabled machine or railgun block-damage control preserves terrain");
            } finally {value.set(before);}
        } catch(ReflectiveOperationException error) {throw new IllegalStateException(error);}h.succeed();
    }
}
