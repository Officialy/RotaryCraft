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
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntitySeparatorPipe extends BlockEntityPiping {

    private Fluid fluid;
    private int level;

    public BlockEntitySeparatorPipe(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SEPARATION.get(), pos, state);
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m, Direction dir) {
        return m.isStandardPipe() || m == MachineRegistry.FUELLINE || m == MachineRegistry.HOSE;
    }

    private Direction getDirectionDir() {
        return this.hasRedstoneSignal() ? Direction.UP : Direction.DOWN;
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
        return MachineRegistry.SEPARATION;
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
        return false;
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
        return side.getStepY() == 0;
    }

    @Override
    public boolean canEmitToPipeOn(Direction side) {
        return side == this.getDirectionDir();
    }

    @Override
    public Block getPipeBlockType() {
        return Blocks.LAPIS_BLOCK;
    }

    @Override
    public boolean isConnectedToNonSelf(Direction dir) {
        return false;
    }

    @Override
    public boolean canIntakeFromIFluidHandler(Direction side) {
        return false;
    }

    @Override
    public boolean canOutputToIFluidHandler(Direction side) {
        return false;
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SEPARATION.get();
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
