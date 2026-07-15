/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.decorative;

import java.util.List;

import it.unimi.dsi.fastutil.ints.IntList;

import net.minecraft.core.BlockPos;
import net.minecraft.core.component.DataComponents;
import net.minecraft.world.entity.projectile.FireworkRocketEntity;
import net.minecraft.world.item.DyeColor;
import net.minecraft.world.item.DyeItem;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.component.FireworkExplosion;
import net.minecraft.world.item.component.Fireworks;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;

import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Firework Machine automatically assembles and launches a random firework rocket each operation
 * from the reagents in its inventory: a dye sets the burst colour, gunpowder is the mandatory
 * propellant, an optional shape ingredient (fire charge → large ball, gold nugget → star, feather →
 * burst, skull → creeper) sets the shape, a diamond adds a trail and glowstone dust a twinkle. The
 * 1.7.10 NBT assembly is re-expressed on the 1.21 {@link Fireworks}/{@link FireworkExplosion} data
 * components. Faster operation with more speed; the Infinity enchant sometimes crafts for free.
 */
public class BlockEntityFireworkMachine extends InventoriedPowerReceiver implements EnchantableMachine, DiscreteFunction, ConditionalOperation {

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantments.INFINITY);

    public boolean idle = false;

    public BlockEntityFireworkMachine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FIREWORK.get(), pos, state);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getSummativeSidedPower();
        if (world.isClientSide())
            return;
        if (power < MINPOWER || omega < MINSPEED)
            return;
        if (tickcount < this.getOperationTime())
            return;
        tickcount = 0;

        ItemStack rocket = this.assembleRocket();
        if (rocket == null) {
            idle = true;
            return;
        }
        idle = false;
        world.addFreshEntity(new FireworkRocketEntity(world, pos.getX() + 0.5, pos.getY() + 1.25, pos.getZ() + 0.5, rocket));
        // API-PORT: legacy also posted a FireworkLaunchEvent for other mods (not in this build).
    }

    private ItemStack assembleRocket() {
        int dye = this.findDye();
        int powder = ReikaInventoryHelper.locateInInventory(Items.GUNPOWDER, itemHandler);
        if (dye < 0 || powder < 0)
            return null;

        // 26.x DyeItem no longer exposes its colour directly; derive it from the item id (e.g. "red_dye").
        String path = net.minecraft.core.registries.BuiltInRegistries.ITEM.getKey(itemHandler.getStackInSlot(dye).getItem()).getPath();
        DyeColor color = DyeColor.byName(path.endsWith("_dye") ? path.substring(0, path.length() - 4) : path, DyeColor.WHITE);

        FireworkExplosion.Shape shape = FireworkExplosion.Shape.SMALL_BALL;
        int shapeSlot = -1;
        int fc = ReikaInventoryHelper.locateInInventory(Items.FIRE_CHARGE, itemHandler);
        int gn = ReikaInventoryHelper.locateInInventory(Items.GOLD_NUGGET, itemHandler);
        int fe = ReikaInventoryHelper.locateInInventory(Items.FEATHER, itemHandler);
        int sk = ReikaInventoryHelper.locateInInventory(Items.WITHER_SKELETON_SKULL, itemHandler);
        if (fc >= 0) {
            shape = FireworkExplosion.Shape.LARGE_BALL;
            shapeSlot = fc;
        } else if (gn >= 0) {
            shape = FireworkExplosion.Shape.STAR;
            shapeSlot = gn;
        } else if (fe >= 0) {
            shape = FireworkExplosion.Shape.BURST;
            shapeSlot = fe;
        } else if (sk >= 0) {
            shape = FireworkExplosion.Shape.CREEPER;
            shapeSlot = sk;
        }

        int diamond = level.getRandom().nextBoolean() ? ReikaInventoryHelper.locateInInventory(Items.DIAMOND, itemHandler) : -1;
        int glow = level.getRandom().nextBoolean() ? ReikaInventoryHelper.locateInInventory(Items.GLOWSTONE_DUST, itemHandler) : -1;

        FireworkExplosion explosion = new FireworkExplosion(shape,
                IntList.of(color.getFireworkColor()), IntList.of(), diamond >= 0, glow >= 0);
        ItemStack rocket = new ItemStack(Items.FIREWORK_ROCKET);
        rocket.set(DataComponents.FIREWORKS, new Fireworks(1, List.of(explosion)));

        // Consume the reagents (Infinity sometimes crafts for free).
        if (!this.freeCraft()) {
            this.consume(dye);
            this.consume(powder);
            if (shapeSlot >= 0)
                this.consume(shapeSlot);
            if (diamond >= 0)
                this.consume(diamond);
            if (glow >= 0)
                this.consume(glow);
        }
        return rocket;
    }

    private boolean freeCraft() {
        return enchantments.hasEnchantment(Enchantments.INFINITY) && level.getRandom().nextInt(3) == 0;
    }

    private void consume(int slot) {
        ReikaInventoryHelper.decrStack(slot, itemHandler);
    }

    private int findDye() {
        for (int i = 0; i < this.getContainerSize(); i++) {
            if (itemHandler.getStackInSlot(i).getItem() instanceof DyeItem)
                return i;
        }
        return -1;
    }

    public boolean isItemValidForSlot(int slot, ItemStack is) {
        Item it = is.getItem();
        return it instanceof DyeItem || it == Items.GUNPOWDER || it == Items.FIRE_CHARGE || it == Items.GOLD_NUGGET
                || it == Items.FEATHER || it == Items.WITHER_SKELETON_SKULL || it == Items.DIAMOND || it == Items.GLOWSTONE_DUST;
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
    }

    @Override
    public int getContainerSize() {
        return 18;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.FIREWORK;
    }

    @Override
    protected String getTEName() {
        return "firework";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FIREWORK.get();
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public int getOperationTime() {
        return Math.max(4, 300 - (int) (16 * reika.dragonapi.libraries.mathsci.ReikaMathLibrary.logbase2(Math.max(1, omega))));
    }

    @Override
    public boolean areConditionsMet() {
        return this.findDye() >= 0 && ReikaInventoryHelper.locateInInventory(Items.GUNPOWDER, itemHandler) >= 0;
    }

    @Override
    public String getOperationalStatus() {
        return this.areConditionsMet() ? "Operational" : "No Reagents";
    }
}
