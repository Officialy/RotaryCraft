/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.weaponry;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.entity.projectile.arrow.AbstractArrow;
import net.minecraft.world.entity.projectile.arrow.Arrow;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.item.enchantment.Enchantments;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.gui.container.machine.inventory.ContainerMachineGun;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Arrow Cannon: a rapid-fire arrow turret. Fed arrows through its 27-slot magazine, when powered it
 * fires a stream of arrows down its facing at any living entity in line-of-sight range (torque = power
 * + range, omega = fire rate). {@code Power} boosts arrow speed, {@code Infinity} makes it consume no
 * ammo. Port of the legacy {@code TileEntityMachineGun} (registry name ARROWGUN); FACING replaces the
 * 1.7 metadata direction.
 */
public class BlockEntityMachineGun extends InventoriedPowerReceiver implements RangedEffect, EnchantableMachine, DiscreteFunction {

    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler()
            .addFilter(Enchantments.INFINITY).addFilter(Enchantments.POWER);

    public BlockEntityMachineGun(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.MACHINEGUN.get(), pos, state);
    }

    @Override
    public int getContainerSize() {
        return 27;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.ARROWGUN;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.MACHINEGUN.get();
    }

    @Override
    protected String getTEName() {
        return "machinegun";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    private Direction facing() {
        return this.getBlockState().getValue(BlockRotaryCraftMachine.FACING);
    }

    public boolean isItemValidForSlot(int i, ItemStack is) {
        return is.is(Items.ARROW);
    }

    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
        return false;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getPower(false);
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        if (tickcount >= this.getOperationTime() && !world.isClientSide()) {
            if (this.getArrowSlot() < 0)
                return;
            AABB box = this.fireBox(pos);
            List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box, e -> e.isAlive() && !this.isOwner(e));
            if (!li.isEmpty())
                this.fire(world, pos);
            tickcount = 0;
        }
    }

    private boolean isOwner(LivingEntity e) {
        return e instanceof Player p && this.isPlacer(p);
    }

    private int getArrowSlot() {
        for (int i = 0; i < this.getContainerSize(); i++)
            if (itemHandler.getStackInSlot(i).is(Items.ARROW))
                return i;
        return -1;
    }

    private double getFirePower() {
        return enchantments.getEnchantment(Enchantments.POWER) * 0.5 + ReikaMathLibrary.logbase(torque + 1, 2);
    }

    @Override
    public int getOperationTime() {
        return Math.max(16 - (int) ReikaMathLibrary.logbase(omega + 1, 2), 4);
    }

    private void fire(Level world, BlockPos pos) {
        Direction facing = this.facing();
        double v = this.getFirePower();
        double vx = facing.getStepX() * v;
        double vz = facing.getStepZ() * v;
        BlockPos spawn = pos.relative(facing);
        Arrow ar = new Arrow(world, spawn.getX() + 0.5, pos.getY() + 0.8, spawn.getZ() + 0.5, new ItemStack(Items.ARROW), null);
        ar.setDeltaMovement(new Vec3(vx, 0, vz));
        ar.pickup = AbstractArrow.Pickup.DISALLOWED;
        world.addFreshEntity(ar);
        if (enchantments.getEnchantment(Enchantments.INFINITY) == 0)
            itemHandler.extractItem(this.getArrowSlot(), 1, false);
        world.playSound(null, pos, SoundEvents.ARROW_SHOOT, SoundSource.BLOCKS, 1F, 1F);
    }

    private AABB fireBox(BlockPos pos) {
        Direction facing = this.facing();
        BlockPos start = pos.relative(facing);
        BlockPos end = pos.relative(facing, this.getRange());
        return AABB.encapsulatingFullBlocks(start, end).inflate(0.1);
    }

    @Override
    public int getRange() {
        return this.getMaxRange();
    }

    @Override
    public int getMaxRange() {
        return 10 + 2 * (int) ReikaMathLibrary.logbase(torque + 1, 2);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public MachineEnchantmentHandler getEnchantmentHandler() {
        return enchantments;
    }

    @Override
    public boolean hasAnInventory() {
        return true;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) {
        return new ContainerMachineGun(id, inv, this);
    }
}
