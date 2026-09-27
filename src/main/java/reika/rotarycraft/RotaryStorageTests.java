package reika.rotarycraft;

import java.util.List;

import com.mojang.serialization.DynamicOps;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.nbt.NbtOps;
import net.minecraft.nbt.Tag;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntitySpawnReason;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.level.storage.loot.parameters.LootContextParams;
import net.minecraft.world.phys.BlockHitResult;
import net.minecraft.world.phys.Vec3;

import reika.rotarycraft.blockentities.storage.BlockEntityScaleableChest;
import reika.rotarycraft.blockentities.storage.ScaleChestContents;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryDataComponents;

/**
 * Storage machines. The Scaleable Chest never spills its inventory when broken: a player harvest
 * carries it inside the dropped chest item (V33a {@code writeInventoryToItem}, restored on
 * placement by {@code readInventoryFromItem}); any other removal voids it, as V33a did.
 */
final class RotaryStorageTests {

    private RotaryStorageTests() {}

    private static final BlockPos CHEST = new BlockPos(4, 1, 4);

    /** Slots straddling vanilla CONTAINER's 256-slot cap, which is why the chest has its own component. */
    private static final int[] SLOTS = {0, 255, 256, 900, BlockEntityScaleableChest.MAXSIZE - 1};

    private static ItemStack stackFor(int slot) {
        return switch (slot % 3) {
            case 0 -> new ItemStack(Items.COBBLESTONE, 1 + slot % 64);
            case 1 -> {
                // A stack carrying its own components must come back intact, not just its item and count.
                ItemStack is = new ItemStack(Items.DIAMOND_SWORD);
                is.set(DataComponents.CUSTOM_NAME, Component.literal("slot " + slot));
                is.setDamageValue(17);
                yield is;
            }
            default -> new ItemStack(Items.REDSTONE, 7);
        };
    }

    private static BlockEntityScaleableChest filledChest(GameTestHelper helper) {
        helper.setBlock(CHEST, RotaryBlocks.SCALECHEST.get());
        BlockEntityScaleableChest te = helper.getBlockEntity(CHEST, BlockEntityScaleableChest.class);
        for (int slot : SLOTS)
            te.itemHandler.setStackInSlot(slot, stackFor(slot));
        return te;
    }

    private static void assertHoldsExactly(GameTestHelper helper, BlockEntityScaleableChest te, String when) {
        int filled = 0;
        for (int i = 0; i < te.getContainerSize(); i++) {
            if (!te.getStackInSlot(i).isEmpty())
                filled++;
        }
        helper.assertTrue(filled == SLOTS.length, when + ": " + filled + " slots filled, expected " + SLOTS.length);
        for (int slot : SLOTS) {
            ItemStack is = te.getStackInSlot(slot);
            helper.assertTrue(ItemStack.matches(is, stackFor(slot)),
                    when + ": slot " + slot + " holds " + is + " " + is.getComponentsPatch() + ", expected " + stackFor(slot));
        }
    }

    /** Survival player breaks the chest, picks up the one item, places it again: same inventory. */
    static void harvestCarriesContents(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        filledChest(helper);
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.IRON_PICKAXE));

        // The real server break path: playerWillDestroy -> removeBlock (preRemoveSideEffects) -> playerDestroy.
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(CHEST)), "survival player failed to break the chest");
        helper.assertBlockNotPresent(RotaryBlocks.SCALECHEST.get(), CHEST);

        List<ItemEntity> drops = helper.getEntities(EntityTypes.ITEM, CHEST, 2);
        helper.assertTrue(drops.size() == 1, "breaking must drop only the chest (no spilled contents), got " + drops.size()
                + " item entities: " + drops.stream().map(ItemEntity::getItem).toList());
        ItemStack chest = drops.getFirst().getItem().copy();
        drops.getFirst().discard();
        helper.assertTrue(chest.is(RotaryBlocks.SCALECHEST.get().asItem()) && chest.getCount() == 1, "dropped " + chest + ", expected one chest");
        ScaleChestContents contents = chest.get(RotaryDataComponents.SCALE_CHEST_CONTENTS.get());
        helper.assertTrue(contents != null && contents.slots().size() == SLOTS.length,
                "harvested chest item must carry its " + SLOTS.length + " stacks, carried " + contents);

        // The component must survive a save (item NBT) — the persistent codec accepts slot indices above 255.
        DynamicOps<Tag> ops = level.registryAccess().createSerializationContext(NbtOps.INSTANCE);
        Tag saved = ScaleChestContents.CODEC.encodeStart(ops, contents).getOrThrow();
        ScaleChestContents reloaded = ScaleChestContents.CODEC.parse(ops, saved).getOrThrow();
        helper.assertTrue(reloaded.equals(contents), "contents changed across a save/load round trip");

        // Place it back through the item, the way a player does.
        player.setItemInHand(InteractionHand.MAIN_HAND, chest);
        helper.placeAt(player, chest, CHEST.below(), Direction.UP);
        BlockEntityScaleableChest placed = helper.getBlockEntity(CHEST, BlockEntityScaleableChest.class);
        assertHoldsExactly(helper, placed, "after re-placing");
        helper.succeed();
    }

    /** Explosions (with or without a source entity) and the self-destruct drop a bare chest; the loot table never copies. */
    static void unharvestedDropsBareChest(GameTestHelper helper) {
        ServerLevel level = helper.getLevel();
        BlockEntityScaleableChest te = filledChest(helper);
        BlockState state = level.getBlockState(helper.absolutePos(CHEST));
        BlockPos pos = helper.absolutePos(CHEST);
        Entity creeper = EntityTypes.CREEPER.create(level, EntitySpawnReason.TRIGGERED);

        for (Entity source : new Entity[] {null, creeper}) {
            // Mirrors BlockBehaviour.onExplosionHit's loot context.
            LootParams.Builder params = new LootParams.Builder(level)
                    .withParameter(LootContextParams.ORIGIN, Vec3.atCenterOf(pos))
                    .withParameter(LootContextParams.TOOL, ItemStack.EMPTY)
                    .withOptionalParameter(LootContextParams.BLOCK_ENTITY, te)
                    .withOptionalParameter(LootContextParams.THIS_ENTITY, source)
                    .withParameter(LootContextParams.EXPLOSION_RADIUS, 1.0F);
            assertBareChest(helper, state.getDrops(params), "explosion by " + source);
        }
        // The power-flicker self-destruct: Block.dropResources(state, level, pos) carries no block entity.
        assertBareChest(helper, Block.getDrops(state, level, pos, null), "self-destruct");
        // Harvest context, as the control: the same block entity does fill the item for a player.
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        List<ItemStack> harvested = Block.getDrops(state, level, pos, te, player, new ItemStack(Items.IRON_PICKAXE));
        helper.assertTrue(harvested.size() == 1 && harvested.getFirst().has(RotaryDataComponents.SCALE_CHEST_CONTENTS.get()),
                "player harvest must copy the contents, dropped " + harvested);
        helper.succeed();
    }

    private static void assertBareChest(GameTestHelper helper, List<ItemStack> drops, String when) {
        helper.assertTrue(drops.size() == 1 && drops.getFirst().is(RotaryBlocks.SCALECHEST.get().asItem()),
                when + ": expected one chest, dropped " + drops);
        helper.assertTrue(!drops.getFirst().has(RotaryDataComponents.SCALE_CHEST_CONTENTS.get()),
                when + ": the chest must not carry its contents (V33a getDrops returned a bare chest)");
    }

    /** An unpowered chest does not open, and hands the interaction on to the held item (V33a returned false). */
    static void unpoweredChestStaysShut(GameTestHelper helper) {
        helper.setBlock(CHEST, RotaryBlocks.SCALECHEST.get());
        BlockPos pos = helper.absolutePos(CHEST);
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        BlockState state = helper.getLevel().getBlockState(pos);
        var result = state.useItemOn(ItemStack.EMPTY, helper.getLevel(), player, InteractionHand.MAIN_HAND,
                new BlockHitResult(Vec3.atCenterOf(pos), Direction.NORTH, pos, false));
        helper.assertTrue(!result.consumesAction(), "unpowered chest consumed the click: " + result);
        helper.assertTrue(player.containerMenu == player.inventoryMenu, "unpowered chest opened a menu");
        helper.succeed();
    }
}
