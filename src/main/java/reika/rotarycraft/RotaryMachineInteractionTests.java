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
import net.minecraft.world.inventory.Slot;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.level.GameType;
import net.minecraft.world.phys.Vec3;
import reika.rotarycraft.blockentities.processing.BlockEntityGrinder;
import reika.rotarycraft.blockentities.transmission.BlockEntityGearbox;
import reika.rotarycraft.gui.container.machine.inventory.ContainerGrinder;
import reika.rotarycraft.registry.RotaryBlocks;

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

    private static int playerSlot(GameTestHelper helper, ContainerGrinder menu, ServerPlayer player, int inventorySlot) {
        for (Slot s : menu.slots)
            if (s.container == player.getInventory() && s.getContainerSlot() == inventorySlot)
                return s.index;
        helper.fail("menu has no slot for player inventory slot " + inventorySlot);
        return -1;
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
