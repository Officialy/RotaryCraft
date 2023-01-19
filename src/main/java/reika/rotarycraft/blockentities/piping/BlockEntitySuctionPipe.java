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
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import net.minecraft.core.Direction;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntitySuctionPipe extends BlockEntityPiping {

    private Fluid fluid;
    private int level;

    public BlockEntitySuctionPipe(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SUCTION.get(), pos, state);
    }

    @Override
    public Block getPipeBlockType() {
        return Blocks.NETHER_BRICKS;
    }

    @Override
    public boolean isConnectedToNonSelf(Direction dir) {
        return false;
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
    public boolean canConnectToPipe(MachineRegistry p, Direction dir) {
        return p == MachineRegistry.HOSE || p == MachineRegistry.FUELLINE || p.isStandardPipe();
    }

    @Override
    protected void setFluid(Fluid f) {
        fluid = f;
    }

    @Override
    protected boolean interactsWithMachines() {
        return true;
    }

    @Override
    protected void onIntake(BlockEntity te) {

    }

    @Override
    public boolean canReceiveFromPipeOn(Direction side) {
        return false;
    }

    @Override
    public boolean canEmitToPipeOn(Direction side) {
        return true;
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
    public boolean isValidFluid(Fluid f) {
        return true;
    }

    @Override
    public boolean hasLiquid() {
        return fluid != null && level > 0;
    }

    @Override
    public Fluid getAttributes() {
        return fluid;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SUCTION;
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
