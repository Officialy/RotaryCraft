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
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import reika.dragonapi.libraries.java.ReikaArrayHelper;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;

import java.util.Arrays;

public class BlockEntityBypass extends BlockEntityPiping {

    private Fluid fluid;
    private int fluidLevel;
    private boolean[] forcedConnection = new boolean[6];
    private boolean[] tryForcedConnection = new boolean[6];

    public BlockEntityBypass(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    @Override
    public void onPlacedAgainst(Direction dir) {
        if (MachineRegistry.getMachine(level, worldPosition.getX() + dir.getStepX(), worldPosition.getY() + dir.getStepY(), worldPosition.getZ() + dir.getStepZ()) == this.getMachine())
            tryForcedConnection[dir.ordinal()] = true;
    }

    @Override
    public void recomputeConnections(Level world, BlockPos pos) {
        forcedConnection = new boolean[6]; //zero out
        for (int i = 0; i < 6; i++) {
            Direction dir = dirs[i];
            if (tryForcedConnection[i]) {
                BlockEntity te = getAdjacentBlockEntity(dir);
                if (te instanceof BlockEntityBypass) {
                    BlockEntityBypass tb = (BlockEntityBypass) te;
                    tb.tryForcedConnection[dir.getOpposite().ordinal()] = true;
                    this.forceConnect(dir);
                    tb.forceConnect(dir.getOpposite());
                    tb.queueConnectionEvaluation(2);
                }
            }
        }
        super.recomputeConnections(world, pos);
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m, Direction side) {
        return m.isStandardPipe() || m == MachineRegistry.FUELLINE || m == MachineRegistry.HOSE || (m == this.getMachine() && forcedConnection[side.ordinal()]);
    }

    public void forceConnect(Direction dir) {
        forcedConnection[dir.ordinal()] = true;
    }

    @Override
    public boolean hasLiquid() {
        return fluidLevel > 0;
    }

    @Override
    public Fluid getAttributes() {
        return fluid;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BYPASS;
    }

    @Override
    public int getFluidLevel() {
        return fluidLevel;
    }

    @Override
    protected void setFluidLevel(int amt) {
        fluidLevel = amt;
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
        return true;
    }

    @Override
    public boolean canEmitToPipeOn(Direction side) {
        return true;
    }

    @Override
    public Block getPipeBlockType() {
        return Blocks.SANDSTONE;
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
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putByte("fconn", ReikaArrayHelper.booleanToByteBitflags(forcedConnection));
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        boolean update = false;
        boolean[] old = new boolean[forcedConnection.length];
        System.arraycopy(forcedConnection, 0, old, 0, old.length);
        forcedConnection = ReikaArrayHelper.booleanFromByteBitflags(NBT.getByte("fconn"), 6);
        update = !Arrays.equals(old, forcedConnection);

//        if (fluidLevel != null && update)
//            fluidLevel.markBlockForUpdate(xCoord, yCoord, zCoord);
    }

    @Override
    protected String getTEName() {
        return "bypass";
    }

    //    @Override
    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
        NBT.putByte("tfconn", ReikaArrayHelper.booleanToByteBitflags(tryForcedConnection));
    }

    //    @Override
    public void load(CompoundTag NBT) {
//        super.load(NBT);
        boolean update = false;
        boolean[] old = new boolean[tryForcedConnection.length];
        System.arraycopy(tryForcedConnection, 0, old, 0, old.length);
        tryForcedConnection = ReikaArrayHelper.booleanFromByteBitflags(NBT.getByte("tfconn"), 6);
        update = !Arrays.equals(old, tryForcedConnection);
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
