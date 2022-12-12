/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;

import java.util.EnumSet;
import java.util.HashMap;
import java.util.Set;


public class TemperatureHandler {

    public static final TemperatureHandler instance = new TemperatureHandler();

    private final HashMap<Block, Integer> blockTemps = new HashMap<>();

    private TemperatureHandler() {
        blockTemps.put(Blocks.LAVA, 600);
        blockTemps.put(Blocks.FIRE, 200);
        blockTemps.put(Blocks.WATER, 12);
        blockTemps.put(Blocks.ICE, -3);
        blockTemps.put(Blocks.PACKED_ICE, -10);
        blockTemps.put(Blocks.BLUE_ICE, -3);
        blockTemps.put(Blocks.FROSTED_ICE, -3);
        blockTemps.put(Blocks.SNOW, 0);
        blockTemps.put(Blocks.SNOW_BLOCK, -2);
    }

    public int getAmbientTemperature(Level world, BlockPos pos, int minAmb, int maxAmb) {
        //return Mth.clamp(ReikaWorldHelper.getAmbientTemperatureAt(world, pos), minAmb, maxAmb); todo ambient temperature
        return 10;
    }

    public int getAmbientTemperatureOffset(Level world, BlockPos pos, int temp, boolean allowHeating, boolean allowCooling) {
        return this.getAmbientTemperatureOffset(world, pos, temp, allowHeating ? EnumSet.allOf(Direction.class) : null, allowCooling ? EnumSet.allOf(Direction.class) : null);
    }

    public int getAmbientTemperatureOffset(TemperatureTE te, Set<Direction> heatSides, Set<Direction> coolSides) {
        BlockEntity tile = (BlockEntity) te;
        return this.getAmbientTemperatureOffset(tile.getLevel(), tile.getBlockPos(), te.getTemperature(), heatSides, coolSides);
    }

    public int getAmbientTemperatureOffset(Level world, BlockPos pos, int temp, Set<Direction> heatSides, Set<Direction> coolSides) {
        int ret = 0;
        if (heatSides != null) {
            for (Direction dir : heatSides) {
                int get = this.getBlockTemperature(world, new BlockPos(pos.getX() + dir.getStepX(), pos.getY() + dir.getStepY(), pos.getZ() + dir.getStepZ()));
                if (get != Integer.MIN_VALUE && get > temp)
                    ret += get - temp;
            }
        }
        if (coolSides != null) {
            for (Direction dir : coolSides) {
                int get = this.getBlockTemperature(world, new BlockPos(pos.getX() + dir.getStepX(), pos.getY() + dir.getStepY(), pos.getZ() + dir.getStepZ()));
                if (get != Integer.MIN_VALUE && get < temp)
                    ret -= temp - get;
            }
        }
        return ret;
    }

    private int getBlockTemperature(Level world, BlockPos pos) {
        Block b = world.getBlockState(pos).getBlock();
        Integer base = blockTemps.get(b);
        if (base != null) {
            return base;
        }
        Fluid f = world.getFluidState(pos).getType();// ReikaFluidHelper.lookupFluidForBlock(b);
        if (f != null) {
            return f.getFluidType().getTemperature();
        }
        return Integer.MIN_VALUE;
    }

    public void applyTemperature(TemperatureTE te) {
        BlockEntity tile = (BlockEntity) te;
        this.applyTemperature(tile.getLevel(), tile.getBlockPos(), te.getTemperature());
    }

    public void applyTemperature(Level world, BlockPos pos, int temp) {

    }

}
