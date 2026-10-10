package reika.rotarycraft;

import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.gametest.framework.TestEnvironmentDefinition;
import net.minecraft.network.chat.Component;
import net.minecraft.util.ProblemReporter;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.storage.TagValueInput;
import net.minecraft.world.phys.AABB;
import net.neoforged.neoforge.capabilities.Capabilities;
import net.neoforged.neoforge.event.RegisterGameTestsEvent;
import net.neoforged.neoforge.transfer.item.ItemResource;
import net.neoforged.neoforge.transfer.transaction.Transaction;
import reika.dragonapi.instantiable.storage.ManagedItemHandler;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.blockentities.BlockEntityItemCannon;
import reika.rotarycraft.registry.RotaryBlocks;

/** Real registry-backed inventories: component identity, conservation, rollback and native EMPTY slots. */
final class DragonAPIInventoryAuditTests {
    private static final BlockPos POS = new BlockPos(4, 1, 4);

    static void register(RegisterGameTestsEvent event, Holder<TestEnvironmentDefinition<?>> environment) {
        RotaryGameTests.register(event, environment, "dragonapi_inventory_arrays", 40, DragonAPIInventoryAuditTests::arrays);
        RotaryGameTests.register(event, environment, "dragonapi_inventory_transactions", 40, DragonAPIInventoryAuditTests::transactions);
        RotaryGameTests.register(event, environment, "dragonapi_inventory_player", 40, DragonAPIInventoryAuditTests::player);
        RotaryGameTests.register(event, environment, "dragonapi_inventory_spill", 40, DragonAPIInventoryAuditTests::spill);
        RotaryGameTests.register(event, environment, "dragonapi_inventory_last_block", 40, DragonAPIInventoryAuditTests::lastBlock);
        RotaryGameTests.register(event, environment, "dragonapi_inventory_cannon", 40, DragonAPIInventoryAuditTests::cannon);
    }

    private static ItemStack named(int count) {
        ItemStack stack = new ItemStack(Items.DIAMOND, count);
        stack.set(DataComponents.CUSTOM_NAME, Component.literal("Conserved components"));
        return stack;
    }

    private static void arrays(GameTestHelper h) {
        ItemStack zero = named(0);
        ItemStack[] empty = {null, ItemStack.EMPTY, zero};
        h.assertTrue(ReikaInventoryHelper.isEmpty(empty) && ReikaInventoryHelper.countEmptySlots(empty) == 3
                && ReikaInventoryHelper.hasNEmptyStacks(empty, 3), "null, EMPTY and zero-count stacks are all empty");
        h.assertTrue(ReikaInventoryHelper.findEmptySlot(empty) == 0
                && ReikaInventoryHelper.findEmptySlots(empty).equals(List.of(0, 1, 2)) && empty[2] == zero,
                "empty-slot queries must preserve legacy array entries");
        h.assertTrue(!ReikaInventoryHelper.checkForItem(Items.AIR, empty)
                && !ReikaInventoryHelper.checkForItemStack(ItemStack.EMPTY, empty, true), "empty slots are not actual items");
        ItemStack[] inventory = {named(60), named(61), ItemStack.EMPTY};
        ItemStack input = named(10);
        h.assertTrue(ReikaInventoryHelper.addToInventoryWithLeftover(input, inventory) == 0
                && inventory[0].getCount() == 64 && inventory[1].getCount() == 64 && inventory[2].getCount() == 3,
                "partial stacks and empty slots accept exactly the offered count");
        h.assertTrue(input.getCount() == 10 && ItemStack.isSameItemSameComponents(input, inventory[2]), "array insertion retains components without consuming its argument");
        ItemStack[] split = {ItemStack.EMPTY, null};
        h.assertTrue(ReikaInventoryHelper.addToInventoryWithLeftover(named(130), split) == 2
                && split[0].getCount() == 64 && split[1].getCount() == 64, "oversized input splits across bounded slots");
        h.assertTrue(ReikaInventoryHelper.getTotalUniqueStacks(new ItemStack[]{null, named(1), named(5), new ItemStack(Items.DIAMOND)}) == 2,
                "unique stack types retain component identity and ignore counts/empty entries");
        h.assertTrue(ReikaInventoryHelper.getSmallestStack(inventory) == inventory[2], "smallest nonempty stack must actually be smallest");
        ReikaInventoryHelper.decrStack(0, empty, 1);
        ReikaInventoryHelper.decrStack(0, inventory, -3);
        h.assertTrue(inventory[0].getCount() == 64, "nonpositive decrements cannot create items");
        h.succeed();
    }

    private static void transactions(GameTestHelper h) {
        ManagedItemHandler handler = new ManagedItemHandler(2);
        handler.setStackInSlot(0, named(60));
        handler.setStackInSlot(1, named(60));
        h.assertTrue(!ReikaInventoryHelper.addToIInv(named(9), handler)
                && handler.getStackInSlot(0).getCount() == 60 && handler.getStackInSlot(1).getCount() == 60,
                "a failed full-fit transaction must roll back every slot");
        h.assertTrue(ReikaInventoryHelper.hasSpaceFor(named(8), handler, false)
                && handler.getStackInSlot(0).getCount() == 60, "capacity simulation must not commit");
        h.assertTrue(ReikaInventoryHelper.addToIInv(named(8), handler)
                && handler.getStackInSlot(0).getCount() == 64 && handler.getStackInSlot(1).getCount() == 64,
                "full-fit insertion must persist merges instead of mutating detached snapshots");
        handler.setStackInSlot(0, named(60)); handler.setStackInSlot(1, ItemStack.EMPTY);
        h.assertTrue(ReikaInventoryHelper.addToInventoryWithLeftover(named(10), handler) == 0
                && handler.getStackInSlot(0).getCount() == 64 && handler.getStackInSlot(1).getCount() == 6
                && ItemStack.isSameItemSameComponents(named(1), handler.getStackInSlot(1)), "handler leftovers conserve components and counts");
        ManagedItemHandler filtered = new ManagedItemHandler(1) {
            @Override public boolean isValid(int slot, ItemResource resource) { return false; }
        };
        h.assertTrue(!ReikaInventoryHelper.addToIInv(named(1), filtered)
                && ReikaInventoryHelper.addToIInv(named(2), filtered, true)
                && filtered.getStackInSlot(0).getCount() == 2, "original administrative override remains available");
        h.assertTrue(!ReikaInventoryHelper.addOrSetStack(named(65), handler, 1)
                && handler.getStackInSlot(1).getCount() == 6, "single-slot output helpers reject oversized stacks without mutation");
        ManagedItemHandler limited = new ManagedItemHandler(2) {
            @Override protected int getCapacity(int slot, ItemResource resource) { return resource.isEmpty() ? 64 : 3; }
        };
        h.assertTrue(!ReikaInventoryHelper.addToIInv(named(7), limited, true)
                && ReikaInventoryHelper.addToIInv(named(6), limited, true)
                && limited.getStackInSlot(0).getCount() == 3 && limited.getStackInSlot(1).getCount() == 3,
                "administrative overrides retain actual item-dependent capacity limits");
        h.assertTrue(ReikaInventoryHelper.getLocatedTransferrables(filtered).get(0).getCount() == 2
                && filtered.getStackInSlot(0).getCount() == 2, "transferrables follow extraction policy, not insertion filters, and simulation preserves source");
        var detached = new net.minecraft.world.SimpleContainer(2) {
            @Override public ItemStack getItem(int slot) { return super.getItem(slot).copy(); }
        };
        detached.setItem(0, named(63));
        h.assertTrue(ReikaInventoryHelper.addToIInv(named(4), detached, false, -10, 100)
                && detached.getItem(0).getCount() == 64 && detached.getItem(1).getCount() == 3,
                "container merges write back even through snapshot adapters and clamp requested slot bounds");
        ReikaInventoryHelper.decrStack(1, detached, 2);
        h.assertTrue(detached.getItem(1).getCount() == 1, "container decrements persist through snapshot adapters");
        h.succeed();
    }

    private static void player(GameTestHelper h) {
        var inventory = DragonAPIAuditTests.player(h).getInventory();
        for (int slot = 0; slot < inventory.getContainerSize(); slot++) inventory.setItem(slot, new ItemStack(Items.COBBLESTONE, 64));
        inventory.setItem(0, named(60)); inventory.setItem(1, named(61)); inventory.setItem(2, ItemStack.EMPTY);
        ItemStack input = named(6);
        h.assertTrue(ReikaInventoryHelper.addToInventoryWithLeftover(input, inventory, true) == 0
                && inventory.getItem(0).getCount() == 60 && inventory.getItem(1).getCount() == 61 && inventory.getItem(2).isEmpty(),
                "player inventory simulation cannot mutate slots");
        h.assertTrue(ReikaInventoryHelper.addToInventoryWithLeftover(input, inventory, false) == 0
                && inventory.getItem(0).getCount() == 64 && inventory.getItem(1).getCount() == 63 && inventory.getItem(2).isEmpty(),
                "merges must use remaining counts rather than inserting the input count twice");
        h.assertTrue(ReikaInventoryHelper.addToInventoryWithLeftover(named(10), inventory, false) == 0
                && inventory.getItem(1).getCount() == 64 && inventory.getItem(2).getCount() == 9,
                "native EMPTY player slots accept component-bearing items");
        h.assertTrue(input.getCount() == 6 && ReikaInventoryHelper.getItemOnClosing(inventory, 2).getCount() == 9
                && inventory.getItem(2).isEmpty(), "closing removes using EMPTY and preserves the returned stack");
        inventory.setItem(2, named(1));
        h.assertTrue(ReikaInventoryHelper.drawFromInventory(named(1), 130, inventory) == 129 && inventory.getItem(0).isEmpty()
                && inventory.getItem(1).isEmpty() && inventory.getItem(2).isEmpty(), "draining native player slots must never store null");
        h.succeed();
    }

    private static void spill(GameTestHelper h) {
        var level = h.getLevel(); var pos = h.absolutePos(POS);
        ManagedItemHandler handler = new ManagedItemHandler(2); handler.setStackInSlot(0, named(5));
        ReikaInventoryHelper.spillAndEmptyInventory(level, pos.getX(), pos.getY(), pos.getZ(), handler);
        ReikaInventoryHelper.spillAndEmptyInventory(level, pos.getX(), pos.getY(), pos.getZ(), handler);
        ItemStack[] array = {null, ItemStack.EMPTY, named(3)};
        ReikaInventoryHelper.spillAndEmptyInventory(level, pos.getX(), pos.getY(), pos.getZ(), array);
        var entities = level.getEntitiesOfClass(ItemEntity.class, new AABB(pos).inflate(2));
        h.assertTrue(ReikaInventoryHelper.isEmpty(handler) && ReikaInventoryHelper.isEmpty(array)
                && entities.size() == 2 && entities.stream().mapToInt(entity -> entity.getItem().getCount()).sum() == 8,
                "spilling clears storage once, skips empty entities and cannot duplicate on repeated calls");
        h.assertTrue(entities.stream().allMatch(entity -> ItemStack.isSameItemSameComponents(named(1), entity.getItem())),
                "spilled entities retain every source component");
        h.succeed();
    }

    private static void lastBlock(GameTestHelper h) {
        ItemStack block = new ItemStack(Items.OAK_STAIRS); block.set(DataComponents.CUSTOM_NAME, Component.literal("last block"));
        ManagedItemHandler handler = new ManagedItemHandler(1); handler.setStackInSlot(0, block);
        ItemStack consumed = ReikaInventoryHelper.getNextBlockInInventory(handler, true);
        h.assertTrue(consumed.getCount() == 1 && ItemStack.isSameItemSameComponents(consumed, block) && handler.getStackInSlot(0).isEmpty(),
                "Line Builder's last consumed block retains identity even after its source slot empties");
        ItemStack[] array = {null, block.copy()};
        h.assertTrue(ReikaInventoryHelper.getNextBlockInInventory(array, false).is(Items.OAK_STAIRS) && array[1].getCount() == 1
                && ReikaInventoryHelper.getNextBlockInInventory(array, true).is(Items.OAK_STAIRS) && array[1].isEmpty(),
                "legacy arrays support null entries and both peek/consume modes");
        h.succeed();
    }

    private static void cannon(GameTestHelper h) {
        BlockPos destination = POS.offset(2, 0, 0);
        h.setBlock(POS, RotaryBlocks.ITEM_CANNON.get()); h.setBlock(destination, RotaryBlocks.ITEM_CANNON.get());
        var source = h.getBlockEntity(POS, BlockEntityItemCannon.class); var target = h.getBlockEntity(destination, BlockEntityItemCannon.class);
        source.selectNewTarget(h.getLevel(), target.getBlockPos()); source.itemHandler.setStackInSlot(0, named(5));
        source.power = BlockEntityItemCannon.STACKPOWER;
        fire(h, source, target);
        h.assertTrue(source.isEmpty() && target.getItem(0).getCount() == 5
                && ItemStack.isSameItemSameComponents(named(1), target.getItem(0)), "stack-mode cannon transfers consume source items and retain components");
        source.itemHandler.setStackInSlot(0, named(5)); source.power = 1;
        fire(h, source, target);
        h.assertTrue(source.getItem(0).getCount() == 4 && target.getItem(0).getCount() == 6, "single-item mode conserves the inventory total");
        for (int slot = 0; slot < 9; slot++) target.setItem(slot, named(64));
        fire(h, source, target);
        h.assertTrue(source.getItem(0).getCount() == 4 && target.getItem(0).getCount() == 64, "full target cannot consume or duplicate items");
        var saved = source.saveWithoutMetadata(h.getLevel().registryAccess());
        var restored = new BlockEntityItemCannon(source.getBlockPos(), source.getBlockState());
        restored.loadWithComponents(TagValueInput.create(ProblemReporter.DISCARDING, h.getLevel().registryAccess(), saved));
        h.assertTrue(source.getTarget().equals(restored.getTarget()) && restored.getItem(0).getCount() == 4,
                "detached cannon load restores its target and registry-backed inventory");
        var bottom = h.getLevel().getCapability(Capabilities.Item.BLOCK, source.getBlockPos(), Direction.DOWN);
        var top = h.getLevel().getCapability(Capabilities.Item.BLOCK, source.getBlockPos(), Direction.UP);
        h.assertTrue(bottom != null && top != null && source.hasAnInventory(), "native item capability exposes the cannon inventory");
        try (Transaction tx = Transaction.openRoot()) {
            h.assertTrue(top.extract(0, ItemResource.of(named(1)), 1, tx) == 0
                    && bottom.extract(0, ItemResource.of(named(1)), 1, tx) == 1, "original bottom-only extraction survives the resource-handler migration");
        }
        source.setTarget(null); fire(h, source, target);
        h.assertTrue(source.getItem(0).getCount() == 4, "unlinked cannons must not transfer or crash");
        source.selectNewTarget(h.getLevel(), source.getBlockPos()); fire(h, source, source);
        h.assertTrue(source.getItem(0).getCount() == 4, "a self-target cannot transfer or produce nonfinite velocity");
        h.succeed();
    }

    private static void fire(GameTestHelper h, BlockEntityItemCannon source, BlockEntityItemCannon target) {
        // Isolate transfer from power acquisition; dedicated power-chain acceptance is a separate fixture.
        try {
            var method = BlockEntityItemCannon.class.getDeclaredMethod("fire", Level.class, BlockPos.class, BlockEntityItemCannon.class);
            method.setAccessible(true); method.invoke(source, h.getLevel(), source.getBlockPos(), target);
        } catch (ReflectiveOperationException error) { throw new IllegalStateException(error); }
    }
}
