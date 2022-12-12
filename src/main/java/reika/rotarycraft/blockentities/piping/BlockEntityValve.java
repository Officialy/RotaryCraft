/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.piping;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;

public class BlockEntityValve extends BlockEntityPiping {

    private Fluid fluid;
    private int level;

    public BlockEntityValve(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry p, Direction dir) {
        return p.isStandardPipe() || p == MachineRegistry.HOSE || p == MachineRegistry.FUELLINE || p == MachineRegistry.SEPARATION;
    }

    @Override
    public boolean hasLiquid() {
        return level > 0;
    }

    @Override
    public Fluid getAttributes() {
        return fluid;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.VALVE;
    }

    @Override
    public int getFluidLevel() {
        return level;
    }

    @Override
    protected void setFluidLevel(int amt) {
        level = amt;
    }

    @Override
    protected void setFluid(Fluid f) {
        fluid = f;
    }

    @Override
    protected boolean interactsWithMachines() {
        return this.hasRedstoneSignal();
    }

    @Override
    protected void onIntake(BlockEntity te) {

    }

    @Override
    public boolean isValidFluid(Fluid f) {
        return true;
    }

    @Override
    public boolean canReceiveFromPipeOn(Direction side) {
        return true;
    }

    @Override
    public boolean canEmitToPipeOn(Direction side) {
        return true;
    }

    @Override
    public Block getPipeBlockType() {
        return Blocks.REDSTONE_BLOCK;
    }

    @Override
    public boolean isConnectedToNonSelf(Direction dir) {
        return false;
    }

    @Override
    public boolean canIntakeFromIFluidHandler(Direction side) {
        return true;
    }

    @Override
    public boolean canOutputToIFluidHandler(Direction side) {
        return false;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
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
