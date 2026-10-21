package reika.rotarycraft;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.component.DataComponents;
import net.minecraft.gametest.framework.GameTestHelper;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.entity.EntityTypes;
import net.minecraft.world.entity.animal.pig.Pig;
import net.minecraft.world.entity.item.ItemEntity;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.phys.Vec3;
import reika.rotarycraft.auxiliary.PowerSourceList;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.blockentities.transmission.BlockEntityShaft;
import reika.rotarycraft.blockentities.transmission.BlockEntitySplitter;
import reika.rotarycraft.gui.container.machine.inventory.ContainerGrinder;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryMenus;

/**
 * Player-facing machine behaviour ported from 1.7.10's block/inventory plumbing: the grinder's
 * contact damage ({@code onEntityCollidedWithBlock}) and shift-click insertion (ISidedInventory),
 * and an NBTMachine's item tag surviving a harvest (gearbox lubricant, {@code harvestBlock}).
 */
final class RotaryMachineInteractionTests {

    private RotaryMachineInteractionTests() {}

    /** A powered grinder grinds a mob standing on its 13/16-high wheels. */
    static void grinderDamagesStandingEntity(GameTestHelper helper) {
        RotaryPowerTests.coil(helper, 2, 4, 128, 1 << 20);
        RotaryPowerTests.place(helper, 3, 4, RotaryBlocks.GRINDER.get(), Direction.EAST);
        BlockPos grinder = RotaryPowerTests.at(3, 4);
        // On the wheels themselves (NoAI mobs don't fall, so it can't be dropped from the block above).
        Pig pig = helper.spawn(EntityTypes.PIG, new Vec3(grinder.getX() + 0.5, grinder.getY() + 13 / 16D, grinder.getZ() + 0.5));
        pig.setNoAi(true);
        float max = pig.getMaxHealth();
        helper.runAfterDelay(30, () -> {
            var machine = helper.getBlockEntity(grinder, BlockEntityGrinder.class);
            helper.assertTrue(machine.canDealDamage(), "grinder must be powered past its damage threshold, power=" + machine.power);
            helper.assertTrue(!pig.isAlive() || pig.getHealth() < max,
                    "pig standing on a powered grinder took no damage (health " + pig.getHealth() + "/" + max + ", y=" + pig.getY() + ")");
            helper.succeed();
        });
    }

    /** Shift-clicking a grindable item out of the player inventory lands it in the input slot. */
    static void grinderShiftClickInsert(GameTestHelper helper) {
        RotaryPowerTests.place(helper, 3, 4, RotaryBlocks.GRINDER.get(), Direction.EAST);
        var machine = helper.getBlockEntity(RotaryPowerTests.at(3, 4), BlockEntityGrinder.class);
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.getInventory().setItem(9, new ItemStack(Items.BLAZE_ROD, 5));
        player.getInventory().setItem(10, new ItemStack(Items.DIRT, 5));
        ContainerGrinder menu = new ContainerGrinder(0, player.getInventory(), machine);

        menu.quickMoveStack(player, playerSlot(helper, menu, player, 9));
        helper.assertTrue(machine.getItem(0).is(Items.BLAZE_ROD) && machine.getItem(0).getCount() == 5,
                "shift-clicked blaze rods must enter the input slot, slot 0 holds " + machine.getItem(0));
        helper.assertTrue(player.getInventory().getItem(9).isEmpty(), "the moved stack must leave the player inventory");

        menu.quickMoveStack(player, playerSlot(helper, menu, player, 10));
        helper.assertTrue(player.getInventory().getItem(10).is(Items.DIRT) && player.getInventory().getItem(10).getCount() == 5,
                "an ungrindable item must stay in the player inventory");
        helper.assertTrue(machine.getItem(2).isEmpty() && machine.getItem(1).isEmpty(), "dirt must not reach the bucket or output slots");
        helper.succeed();
    }

    private static int playerSlot(GameTestHelper helper, AbstractContainerMenu menu, ServerPlayer player, int inventorySlot) {
        for (Slot s : menu.slots)
            if (s.container == player.getInventory() && s.getContainerSlot() == inventorySlot)
                return s.index;
        helper.fail("menu has no slot for player inventory slot " + inventorySlot);
        return -1;
    }

    /**
     * Every InventoriedPowerReceiver is a WorldlyContainer now, so menus shift-click by each machine's
     * 1.7.10 isItemValidForSlot: igniter fuel and aerosolizer potions go in, dirt stays put.
     */
    static void machineShiftClickFollowsSlotRules(GameTestHelper helper) {
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        assertShiftClick(helper, player, RotaryBlocks.IGNITER.get(), 2, new ItemStack(Items.COAL, 8));
        assertShiftClick(helper, player, RotaryBlocks.AEROSOLIZER.get(), 4, new ItemStack(Items.POTION));
        helper.succeed();
    }

    private static void assertShiftClick(GameTestHelper helper, ServerPlayer player, Block block, int x, ItemStack accepted) {
        RotaryPowerTests.place(helper, x, 4, block, Direction.EAST);
        var machine = helper.getBlockEntity(RotaryPowerTests.at(x, 4), InventoriedPowerReceiver.class);
        AbstractContainerMenu menu = machine.createMenu(0, player.getInventory(), player);
        helper.assertTrue(menu != null, block + " has no menu");
        player.getInventory().setItem(9, accepted.copy());
        player.getInventory().setItem(10, new ItemStack(Items.DIRT, 5));
        menu.quickMoveStack(player, playerSlot(helper, menu, player, 9));
        menu.quickMoveStack(player, playerSlot(helper, menu, player, 10));
        int moved = 0;
        for (int i = 0; i < machine.getContainerSize(); i++) {
            ItemStack in = machine.getItem(i);
            helper.assertTrue(in.isEmpty() || in.is(accepted.getItem()), block + " accepted " + in + " into slot " + i);
            moved += in.getCount();
        }
        helper.assertTrue(moved == accepted.getCount(), block + " took " + moved + " of " + accepted);
        helper.assertTrue(player.getInventory().getItem(10).getCount() == 5, block + " must leave the dirt in the player inventory");
        player.getInventory().clearContent();
    }

    /** Breaking a machine spills its inventory beside its own item, as BlockTEBase.breakBlock did. */
    static void machineSpillsInventoryOnBreak(GameTestHelper helper) {
        BlockPos pos = RotaryPowerTests.at(3, 4);
        RotaryPowerTests.place(helper, 3, 4, RotaryBlocks.IGNITER.get(), Direction.EAST);
        helper.getBlockEntity(pos, InventoriedPowerReceiver.class).setItem(0, new ItemStack(Items.COAL, 10));
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_PICKAXE));
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(pos)), "survival player failed to break the igniter");
        int coal = 0;
        boolean machine = false;
        for (ItemEntity e : helper.getEntities(EntityTypes.ITEM, pos, 2)) {
            if (e.getItem().is(Items.COAL))
                coal += e.getItem().getCount();
            machine |= e.getItem().is(RotaryBlocks.IGNITER.get().asItem());
        }
        helper.assertTrue(coal == 10, "broken igniter spilled " + coal + " coal, expected 10");
        helper.assertTrue(machine, "broken igniter must still drop itself");
        helper.succeed();
    }

    /** Automation sees the 1.7.10 sided rules: InertIInv hides every slot, insertion follows isItemValidForSlot. */
    static void automationFollowsSidedRules(GameTestHelper helper) {
        RotaryPowerTests.place(helper, 2, 4, RotaryBlocks.WOODCUTTER.get(), Direction.EAST);
        RotaryPowerTests.place(helper, 4, 4, RotaryBlocks.HEATER.get(), Direction.EAST);
        RotaryPowerTests.place(helper, 6, 4, RotaryBlocks.AUTOBREEDER.get(), Direction.EAST);
        var cutter = helper.getBlockEntity(RotaryPowerTests.at(2, 4), InventoriedPowerReceiver.class);
        var heater = helper.getBlockEntity(RotaryPowerTests.at(4, 4), InventoriedPowerReceiver.class);
        var breeder = helper.getBlockEntity(RotaryPowerTests.at(6, 4), InventoriedPowerReceiver.class);
        helper.assertTrue(cutter.getSlotsForFace(Direction.UP).length == 0, "woodcutter (InertIInv) must expose no slots");
        helper.assertTrue(!cutter.canPlaceItemThroughFace(0, new ItemStack(Items.OAK_SAPLING), Direction.UP), "woodcutter must refuse automation");
        helper.assertTrue(heater.canPlaceItemThroughFace(0, new ItemStack(Items.COAL), Direction.UP), "heater must take furnace fuel");
        helper.assertTrue(!heater.canPlaceItemThroughFace(0, new ItemStack(Items.DIRT), Direction.UP), "heater must refuse non-fuel");
        helper.assertTrue(!heater.canTakeItemThroughFace(0, new ItemStack(Items.COAL), Direction.DOWN), "heater fuel is not extractable");
        helper.assertTrue(breeder.canPlaceItem(0, new ItemStack(Items.WHEAT)), "auto breeder must take wheat");
        helper.assertTrue(!breeder.canPlaceItem(0, new ItemStack(Items.DIRT)), "auto breeder must refuse dirt");
        helper.succeed();
    }

    /**
     * A merging splitter whose input faces a shaft that also reads from the splitter (neither feeds
     * the other). 1.7.10 only walks a neighbour that writes into the asker; the port walked it anyway,
     * splitter -> shaft -> splitter, until the stack overflowed and took the server down.
     */
    static void splitterFacingShaftInputTerminates(GameTestHelper helper) {
        BlockPos sp = RotaryPowerTests.at(3, 4);
        BlockPos sh = RotaryPowerTests.at(4, 4);
        helper.setBlock(sp, RotaryBlocks.SPLITTER.get());
        RotaryPowerTests.place(helper, 4, 4, RotaryBlocks.HSLA_SHAFT.get(), Direction.EAST);
        var splitter = helper.getBlockEntity(sp, BlockEntitySplitter.class);
        var shaft = helper.getBlockEntity(sh, BlockEntityShaft.class);
        splitter.setIoside(0); // merge: reads EAST (the shaft) + NORTH, writes WEST
        boolean aimed = false;
        for (Direction d : Direction.Plane.HORIZONTAL) {
            shaft.getIOSides(helper.getLevel(), helper.absolutePos(sh), d);
            if (shaft.isReadingFrom(splitter)) {
                aimed = true;
                break;
            }
        }
        helper.assertTrue(aimed, "no shaft facing reads from the splitter");
        // The walk reached this splitter from another merger upstream, so neither the splitter nor
        // the shaft is the caller and the self-check never stops it; only the IO direction check does.
        helper.setBlock(RotaryPowerTests.at(1, 1), RotaryBlocks.SPLITTER.get());
        var upstream = helper.getBlockEntity(RotaryPowerTests.at(1, 1), BlockEntitySplitter.class);
        PowerSourceList sources = splitter.getPowerSources(null, upstream);
        helper.assertTrue(!sources.isErrored(), "splitter <-> shaft source walk overflowed the stack");
        helper.assertTrue(sources.isEmpty(), "a shaft reading from the splitter is not a power source, got " + sources);
        helper.succeed();
    }

    /**
     * Machines whose menus and screens were registered but whose block entities never created a menu,
     * so right-clicking opened nothing. Each must now hand out its own menu type with its 1.7.10 slots
     * (machine slots + the 36 player slots), and quick-move must be safe on every slot.
     */
    static void registeredMenusOpen(GameTestHelper helper) {
        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        Object[][] cases = {
                {RotaryBlocks.BLOWER.get(), RotaryMenus.BLOWER.get(), 18},
                {RotaryBlocks.DROPS.get(), RotaryMenus.DROPS.get(), 2},
                {RotaryBlocks.COMPACTOR.get(), RotaryMenus.COMPACTOR.get(), 5},
                {RotaryBlocks.WETTER.get(), RotaryMenus.WETTER.get(), 1},
                {RotaryBlocks.DRYING.get(), RotaryMenus.DRYING.get(), 1},
                {RotaryBlocks.SPAWNERCONTROLLER.get(), RotaryMenus.SPAWNERCONTROLLER.get(), -1},
                {RotaryBlocks.POWERBUS.get(), RotaryMenus.POWERBUS.get(), 4},
                {RotaryBlocks.CRYSTALLIZER.get(), RotaryMenus.CRYSTALLIZER.get(), 2},
                {RotaryBlocks.AUTOBREEDER.get(), RotaryMenus.AUTOBREEDER.get(), 18},
                {RotaryBlocks.HEATER.get(), RotaryMenus.HEATER.get(), 18},
        };
        int x = 1;
        for (Object[] c : cases) {
            Block block = (Block) c[0];
            BlockPos pos = new BlockPos(x, 1, 1);
            x += 2;
            helper.setBlock(pos, block);
            var be = helper.getBlockEntity(pos, RotaryCraftBlockEntity.class);
            AbstractContainerMenu menu = be.createMenu(0, player.getInventory(), player);
            helper.assertTrue(menu != null, block + " still creates no menu");
            helper.assertTrue(menu.getType() == c[1], block + " opened " + menu.getType() + ", not its registered menu");
            int machineSlots = (Integer) c[2];
            if (machineSlots >= 0)
                helper.assertTrue(menu.slots.size() == machineSlots + 36,
                        block + " has " + menu.slots.size() + " slots, expected " + machineSlots + " + 36 player slots");
            for (int i = 0; i < menu.slots.size(); i++)
                helper.assertTrue(menu.quickMoveStack(player, i) != null, block + " quick-move returned null for slot " + i);
        }
        helper.succeed();
    }

    /** Breaking a lubricated gearbox and placing it back keeps its lubricant and wear. */
    static void gearboxHarvestKeepsLubricant(GameTestHelper helper) {
        BlockPos pos = RotaryPowerTests.at(3, 4);
        RotaryPowerTests.place(helper, 3, 4, RotaryBlocks.HSLA_GEARBOX_16x.get(), Direction.EAST);
        var gbx = helper.getBlockEntity(pos, BlockEntityGearbox.class);
        gbx.setLubricant(12000);
        gbx.setDamage(7);

        ServerPlayer player = (ServerPlayer) helper.makeMockServerPlayer(GameType.SURVIVAL);
        player.setItemInHand(InteractionHand.MAIN_HAND, new ItemStack(Items.DIAMOND_PICKAXE));
        helper.assertTrue(player.gameMode.destroyBlock(helper.absolutePos(pos)), "survival player failed to break the gearbox");

        List<ItemEntity> drops = helper.getEntities(EntityTypes.ITEM, pos, 2);
        helper.assertTrue(drops.size() == 1, "expected one gearbox drop, got " + drops.stream().map(ItemEntity::getItem).toList());
        ItemStack item = drops.getFirst().getItem().copy();
        drops.getFirst().discard();
        CustomData data = item.get(DataComponents.CUSTOM_DATA);
        helper.assertTrue(data != null && data.copyTag().getIntOr("lube", 0) == 12000,
                "harvested gearbox must carry its lubricant, carried " + data);

        player.setItemInHand(InteractionHand.MAIN_HAND, item);
        helper.placeAt(player, item, pos.below(), Direction.UP);
        var placed = helper.getBlockEntity(pos, BlockEntityGearbox.class);
        helper.assertTrue(placed.getLubricant() == 12000, "re-placed gearbox holds " + placed.getLubricant() + " mB lubricant, expected 12000");
        helper.assertTrue(placed.getDamage() == 7, "re-placed gearbox wear " + placed.getDamage() + ", expected 7");
        helper.succeed();
    }
}
