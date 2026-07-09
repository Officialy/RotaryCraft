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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import net.minecraft.world.phys.Vec3;

import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Air Cannon: a directional wind weapon. When powered, it periodically fires a blast of air down its
 * facing that knocks grounded living entities up and away (torque sets the force, omega the fire
 * rate, torque the range). The placer is immune. Port of the legacy {@code TileEntityAirGun}; the 1.7
 * metadata direction switch is replaced by the block's FACING.
 */
public class BlockEntityAirGun extends BlockEntityPowerReceiver implements RangedEffect, DiscreteFunction {

    public BlockEntityAirGun(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.AIRGUN.get(), pos, state);
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
        if (!this.isInWorld()) {
            phi = 0;
            return;
        }
        phi += (float) ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.AIRGUN;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.AIRGUN.get();
    }

    @Override
    protected String getTEName() {
        return "airgun";
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    private Direction facing() {
        return this.getBlockState().getValue(BlockRotaryCraftMachine.FACING);
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        tickcount++;
        this.getPower(false);
        if (power < MINPOWER || torque < MINTORQUE)
            return;
        if (tickcount >= this.getOperationTime() && !world.isClientSide()) {
            AABB box = this.fireBox(pos);
            List<LivingEntity> li = world.getEntitiesOfClass(LivingEntity.class, box, LivingEntity::isAlive);
            if (!li.isEmpty())
                this.fire(world, pos, li);
            tickcount = 0;
        }
    }

    private double getFirePower() {
        return ReikaMathLibrary.logbase(torque + 1, 2);
    }

    @Override
    public int getOperationTime() {
        return Math.max(16 - (int) ReikaMathLibrary.logbase(omega + 1, 2), 4);
    }

    private void fire(Level world, BlockPos pos, List<LivingEntity> li) {
        Direction facing = this.facing();
        double v = this.getFirePower() / 4;
        double vx = facing.getStepX() * v;
        double vz = facing.getStepZ() * v;
        boolean flag = false;
        for (LivingEntity e : li) {
            if (e instanceof Player p && this.isPlacer(p))
                continue;
            // Only knock back entities standing on something solid (grounded), matching the legacy check.
            BlockPos below = BlockPos.containing(e.getX(), e.getY() - 1, e.getZ());
            if (!world.getBlockState(below).isAir()) {
                e.setDeltaMovement(new Vec3(vx, 0.5, vz));
                e.hurtMarked = true;
                flag = true;
            }
        }
        if (flag)
            world.playSound(null, pos, SoundEvents.GENERIC_EXPLODE.value(), SoundSource.BLOCKS, 1F, 1F);
    }

    private AABB fireBox(BlockPos pos) {
        Direction facing = this.facing();
        int r = this.getRange();
        BlockPos start = pos.relative(facing);
        BlockPos end = pos.relative(facing, r);
        return AABB.encapsulatingFullBlocks(start, end).inflate(0.1);
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
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
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }
}
