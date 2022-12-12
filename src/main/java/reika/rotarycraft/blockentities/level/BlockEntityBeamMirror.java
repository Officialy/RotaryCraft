/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.interfaces.block.SemiTransparent;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;

import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

import java.util.List;

public class BlockEntityBeamMirror extends RotaryCraftBlockEntity implements RangedEffect, BreakAction {

    private final BlockArray light = new BlockArray();
    public float theta;
    private int lastRange = 0;
    private Direction facingDir;

    public BlockEntityBeamMirror(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BEAM_MIRROR.get(), pos, state);
    }

    @Override
    public void onEMP() {
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BEAMMIRROR;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    //@Override
    public void updateBlockEntity(Level world, BlockPos pos) {
        this.getDirection(getBlockState().getValue(BlockRotaryCraftMachine.FACING));

        this.adjustAim(world, pos);

        this.setLight(world, pos);

        this.burnMobs(world, pos);
    }

    private void getDirection(Direction dir) {
        switch (dir) {
            case EAST -> facingDir = Direction.EAST;
            case WEST -> facingDir = Direction.WEST;
            case SOUTH -> facingDir = Direction.SOUTH;
            case NORTH -> facingDir = Direction.NORTH;
        }
    }

    private void burnMobs(Level world, BlockPos pos) {
        AABB box = this.getBurningBox(world, pos);
        List<LivingEntity> inbox = world.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity e : inbox) {
            if (ReikaEntityHelper.burnsInSun(e)) {
                e.setSecondsOnFire(10);
            }
        }
    }

    private AABB getBurningBox(Level world, BlockPos pos) {
        int r = this.getRange();
        AABB box = new AABB(pos, new BlockPos(pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1));
        return switch (facingDir) {
            case EAST -> new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1 + r, pos.getY() + 1, pos.getZ() + 1);
            case NORTH -> new AABB(pos.getX(), pos.getY(), pos.getZ() - r, pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            case SOUTH -> new AABB(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1 + r);
            case WEST -> new AABB(pos.getX() - r, pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1);
            default -> box;
        };
    }

    private void setLight(Level world, BlockPos pos) {
        //ReikaJavaLibrary.pConsole(lastRange+":"+r, Dist.DEDICATED_SERVER);
        int r = this.getRange();
        if (lastRange != r) {
            //ReikaJavaLibrary.pConsole(light);
            for (int i = 0; i < light.getSize(); i++) {
                BlockPos c = light.getNthBlock(i);
                Block b = world.getBlockState(c).getBlock();
                if (b == Blocks.LIGHT) {
                    //ReikaJavaLibrary.pConsole(Arrays.toString(xyz));
                    world.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
                    world.blockUpdated(c, this.getBlockState().getBlock());
                }
            }
            light.clear();
            if (r > 0 && world.canSeeSky(pos.above()))
                light.addLineOfClear(world, new BlockPos(pos.getX() + facingDir.getStepX(), pos.getY(), pos.getZ() + facingDir.getStepZ()), r, facingDir.getStepX(), 0, facingDir.getStepZ());
            lastRange = r;
        }

        for (int i = 0; i < light.getSize(); i++) {
            BlockPos c = light.getNthBlock(i);
            if (world.getBlockState(c).getBlock() == Blocks.AIR)
                world.setBlock(c, Blocks.LIGHT.defaultBlockState(), 0);
            world.blockUpdated(c, this.getBlockState().getBlock());
        }
    }

    private void adjustAim(Level world, BlockPos pos) {
        float suntheta = ReikaWorldHelper.getSunAngle(world) / 2 + 12.5F;
        float movespeed = 0.5F;

        if (theta < suntheta)
            theta += movespeed;
        if (theta > suntheta)
            theta -= movespeed;
    }

    @Override
    public int getRange() {
        if (!level.canSeeSky(worldPosition.above()))
            return 0;
        int time = (int) (level.dayTime() % 24000);
        if (time > 13500 && time < 22500)
            return 0;
        double r = ReikaMathLibrary.doubpow(2, 7 * ReikaWorldHelper.getSunIntensity(level, true, 0));
        //ReikaJavaLibrary.pConsole(r);
        int ir = (int) r;
        if (ir > this.getMaxRange())
            ir = this.getMaxRange();
        for (int i = 1; i < ir; i++) {
            int dx = worldPosition.getX() + i * facingDir.getStepX();
            int dy = worldPosition.getY() + i * facingDir.getStepY();
            int dz = worldPosition.getZ() + i * facingDir.getStepZ();
            Block b = level.getBlockState(new BlockPos(dx, dy, dz)).getBlock();
            if (b != Blocks.AIR) {
                if (b instanceof SemiTransparent) {
                    if (((SemiTransparent) b).isOpaque())
                        return i;
                } else if (b.hasDynamicShape()) //isOpaqueCube())
                    return i;
            }
        }
        return ir;
    }

    @Override
    public int getMaxRange() {
        return Math.max(ConfigRegistry.FLOODLIGHTRANGE.getValue(), 64);
    }

    private void lightsOut() {
        Level world = level;
        for (int i = 0; i < light.getSize(); i++) {
            BlockPos c = light.getNthBlock(i);
            Block b = world.getBlockState(c).getBlock();
            if (b == Blocks.LIGHT) {
                world.setBlock(c, Blocks.AIR.defaultBlockState(), 0);
                world.blockUpdated(c, this.getBlockState().getBlock());
            }
        }
    }

    @Override
    public void breakBlock() {
        this.lightsOut();
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.BEAM_MIRROR.get();
    }

    @Override
    public void updateEntity(Level level, BlockPos blockPos) {

    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected String getTEName() {
        return null;
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
