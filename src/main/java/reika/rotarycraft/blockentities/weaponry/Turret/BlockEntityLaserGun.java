/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.weaponry.Turret;

import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;

import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.api.interfaces.TargetEntity;
import reika.rotarycraft.auxiliary.TurretDamage;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * The Laser Gun is an auto-turret that fires a continuous hitscan heat beam at the nearest valid
 * target: living things caught in the beam take fire damage and ignite, and blocks along the beam
 * are melted/transmuted (sand→glass, stone/cobble→lava, dirt→sand, grass→dirt, plants burned away,
 * ice/snow→water, flammables set alight, netherrack detonated, TNT primed). The beam stops at the
 * first opaque block. Built on the shared aimed-turret base.
 */
public class BlockEntityLaserGun extends BlockEntityAimedCannon {

    private int range;

    public BlockEntityLaserGun(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.LASER_GUN.get(), pos, state);
    }

    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity(); // aims via the base
        this.getPowerBelow();
        if (power < MINPOWER) {
            range = 0;
            return;
        }
        range = this.getMaxRange();
        tickcount = 0;
        this.fire(world, null);
    }

    @Override
    public int getRange() {
        return range;
    }

    @Override
    public int getMaxRange() {
        return 256;
    }

    @Override
    public boolean hasAmmo() {
        return true;
    }

    @Override
    protected double randomOffset() {
        return 0;
    }

    @Override
    protected boolean isValidTarget(Entity ent) {
        return ent instanceof LivingEntity le && this.isMobOrUnlistedPlayer(le);
    }

    @Override
    protected double[] getTarget(Level world, BlockPos pos) {
        double[] xyzb = new double[4];
        int r = this.getRange();
        AABB box = new AABB(pos.getX() - r, pos.getY() - r, pos.getZ() - r, pos.getX() + 1 + r, pos.getY() + 1 + r, pos.getZ() + 1 + r);
        double mindist = r + 2;
        Entity best = null;
        for (Entity ent : world.getEntitiesOfClass(Entity.class, box)) {
            if (!this.isValidTarget(ent))
                continue;
            double dist = ReikaMathLibrary.py3d(ent.getX() - pos.getX() - 0.5, ent.getY() - pos.getY() - 0.5, ent.getZ() - pos.getZ() - 0.5);
            if (dist >= mindist)
                continue;
            if (!ReikaWorldHelper.canBlockSee(world, pos.getX(), pos.getY(), pos.getZ(), ent.getX(), ent.getY(), ent.getZ(), r))
                continue;
            double dy = -(ent.getY() - pos.getY());
            double reqtheta = -90 + Math.toDegrees(Math.abs(Math.acos(dy / dist)));
            if ((reqtheta <= dir * MAXLOWANGLE && dir == -1) || (reqtheta >= dir * MAXLOWANGLE && dir == 1)) {
                mindist = dist;
                best = ent;
            }
        }
        if (best == null)
            return xyzb;
        closestMob = best;
        xyzb[0] = best.getX() + this.randomOffset();
        xyzb[1] = best.getY() + best.getEyeHeight() * 0.25 + this.randomOffset();
        xyzb[2] = best.getZ() + this.randomOffset();
        xyzb[3] = 1;
        return xyzb;
    }

    @Override
    public void fire(Level world, double[] xyz) {
        if (world.isClientSide())
            return;
        double bx = worldPosition.getX() + 0.5, by = worldPosition.getY() + 0.5, bz = worldPosition.getZ() + 0.5;
        for (float i = 0; i <= this.getMaxRange(); i += 0.5F) {
            double dx = i * Math.cos(Math.toRadians(theta)) * Math.cos(Math.toRadians(-phi + 90));
            double dy = i * Math.sin(Math.toRadians(theta));
            double dz = i * Math.cos(Math.toRadians(theta)) * Math.sin(Math.toRadians(-phi + 90));
            AABB light = new AABB(bx + dx, by + dy, bz + dz, bx + dx, by + dy, bz + dz).inflate(1);
            for (Entity e : world.getEntitiesOfClass(Entity.class, light)) {
                if (e instanceof TargetEntity te)
                    te.onLaserBeam(this);
                if (e instanceof LivingEntity le) {
                    le.hurt(new TurretDamage(this), 4);
                    le.igniteForSeconds(7);
                }
            }
            BlockPos p = BlockPos.containing(bx + dx, by + dy, bz + dz);
            BlockState state = world.getBlockState(p);
            Block id = state.getBlock();
            Block id2 = this.getAffectedID(world, p, id);
            if (id2 != id) {
                world.setBlock(p, id2.defaultBlockState(), 3);
                range = (int) i + 1;
                return;
            }
            if (id == Blocks.NETHERRACK) {
                world.explode(null, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5, 3F, Level.ExplosionInteraction.MOB);
                range = (int) i + 1;
                return;
            }
            if (id == Blocks.TNT) {
                world.removeBlock(p, false);
                net.minecraft.world.entity.item.PrimedTnt tnt = new net.minecraft.world.entity.item.PrimedTnt(world, p.getX() + 0.5, p.getY() + 0.5, p.getZ() + 0.5, null);
                world.addFreshEntity(tnt);
                range = (int) i + 1;
                return;
            }
            if (id != Blocks.AIR && state.isSolidRender()) {
                range = (int) i + 1;
                return;
            }
        }
        range = this.getMaxRange();
    }

    private Block getAffectedID(Level world, BlockPos p, Block id) {
        if (id == Blocks.AIR)
            return Blocks.AIR;
        if (id == Blocks.SAND)
            return Blocks.GLASS;
        if (id == Blocks.STONE || id == Blocks.STONE_BRICKS || id == Blocks.SANDSTONE || id == Blocks.COBBLESTONE)
            return Blocks.LAVA;
        if (id == Blocks.GRASS_BLOCK || id == Blocks.MYCELIUM)
            return Blocks.DIRT;
        if (id == Blocks.DIRT || id == Blocks.FARMLAND)
            return Blocks.SAND;
        if (id == Blocks.GRAVEL)
            return Blocks.COBBLESTONE;
        if (id == Blocks.SHORT_GRASS || id == Blocks.COBWEB || id == Blocks.DANDELION || id == Blocks.SNOW
                || id == Blocks.POPPY || id == Blocks.RED_MUSHROOM || id == Blocks.BROWN_MUSHROOM || id == Blocks.DEAD_BUSH
                || id == Blocks.WHEAT || id == Blocks.CARROTS || id == Blocks.POTATOES || id == Blocks.VINE
                || id == Blocks.MELON_STEM || id == Blocks.PUMPKIN_STEM || id == Blocks.LILY_PAD)
            return Blocks.AIR;
        if (ReikaWorldHelper.flammable(world, p))
            return Blocks.FIRE;
        if (id == Blocks.ICE || id == Blocks.SNOW_BLOCK)
            return Blocks.WATER;
        return id;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.LASERGUN;
    }

    @Override
    protected String getTEName() {
        return "lasergun";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.LASER_GUN.get();
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
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }
}
