/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.farming;

import java.util.ArrayList;
import java.util.List;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.FarmlandBlock;
import net.neoforged.neoforge.fluids.FluidStack;
import net.neoforged.neoforge.fluids.capability.IFluidHandler;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;

import reika.rotarycraft.base.blockentity.RCFluidReceiver;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Sprinkler-free farmland irrigator: fed water through a pipe, it periodically re-moistens a random
 * tilled block within a 13x13 circular footprint (weighted toward the centre), keeping crops watered
 * without an adjacent water source. Consumes {@link #FLUID_PER_BLOCK} mB per block hydrated. Port of
 * the legacy {@code TileEntityGroundHydrator}; the Forestry-soil branches are dropped (mod interop).
 */
public class BlockEntityGroundHydrator extends RCFluidReceiver {

    public static final int FLUID_PER_BLOCK = 25;
    public static final int CAPACITY = 1000;

    // Legacy per-cell weighting over the 13x13 footprint — heavier toward the centre, 0 at the middle
    // (the hydrator's own column). Built once into a flat weighted list of offsets.
    private static final int[][] AREA = {
            {1, 1, 1, 1, 2, 2, 3, 2, 2, 1, 1, 1, 1},
            {1, 1, 1, 2, 2, 3, 4, 3, 2, 2, 1, 1, 1},
            {1, 1, 2, 3, 5, 6, 6, 6, 5, 3, 2, 1, 1},
            {1, 2, 3, 4, 6, 7, 7, 7, 6, 4, 3, 2, 1},
            {2, 2, 4, 6, 7, 8, 8, 8, 7, 6, 4, 2, 2},
            {2, 3, 6, 7, 8, 9, 9, 9, 8, 7, 6, 3, 2},
            {3, 4, 6, 7, 8, 9, 0, 9, 8, 7, 6, 4, 3},
            {2, 3, 6, 7, 8, 9, 9, 9, 8, 7, 6, 3, 2},
            {2, 2, 4, 6, 7, 8, 8, 8, 7, 6, 4, 2, 2},
            {1, 2, 3, 5, 6, 7, 7, 7, 6, 5, 3, 2, 1},
            {1, 1, 2, 3, 4, 6, 6, 6, 4, 3, 2, 1, 1},
            {1, 1, 1, 2, 2, 3, 4, 3, 2, 2, 1, 1, 1},
            {1, 1, 1, 1, 2, 2, 3, 2, 2, 1, 1, 1, 1},
    };
    private static final int[] WEIGHTED_DX;
    private static final int[] WEIGHTED_DZ;

    static {
        List<int[]> cells = new ArrayList<>();
        int r = (AREA.length - 1) / 2;
        for (int i = 0; i < AREA.length; i++)
            for (int j = 0; j < AREA[i].length; j++)
                for (int w = 0; w < AREA[i][j]; w++)
                    cells.add(new int[]{j - r, i - r});
        WEIGHTED_DX = new int[cells.size()];
        WEIGHTED_DZ = new int[cells.size()];
        for (int k = 0; k < cells.size(); k++) {
            WEIGHTED_DX[k] = cells.get(k)[0];
            WEIGHTED_DZ[k] = cells.get(k)[1];
        }
    }

    public BlockEntityGroundHydrator(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.HYDRATOR.get(), pos, state);
    }

    public static int getRange() {
        return (AREA.length - 1) / 2;
    }

    @Override
    public int getCapacity() {
        return CAPACITY;
    }

    @Override
    public Fluid getInputFluid() {
        return Fluids.WATER;
    }

    @Override
    public boolean canReceiveFrom(Direction from) {
        return true;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.HYDRATOR;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.HYDRATOR.get();
    }

    @Override
    protected String getTEName() {
        return "hydrator";
    }

    @Override
    public boolean hasModelTransparency() {
        return true;
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        if (world.isClientSide() || tank.getFluidLevel() < FLUID_PER_BLOCK)
            return;
        if (world.getRandom().nextInt(2) != 0)
            return;
        int k = world.getRandom().nextInt(WEIGHTED_DX.length);
        BlockPos target = pos.offset(WEIGHTED_DX[k], 0, WEIGHTED_DZ[k]);
        if (this.hydrate(world, target))
            tank.removeLiquid(FLUID_PER_BLOCK);
    }

    private boolean hydrate(Level world, BlockPos p) {
        BlockState bs = world.getBlockState(p);
        if (!(bs.getBlock() instanceof FarmlandBlock))
            return false;
        int moisture = bs.getValue(FarmlandBlock.MOISTURE);
        if (moisture >= 7)
            return false;
        world.setBlock(p, bs.setValue(FarmlandBlock.MOISTURE, 7), 2);
        return true;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    // Receiver only — never gives fluid back to the pipe network.
    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction action) {
        return FluidStack.EMPTY;
    }
}
