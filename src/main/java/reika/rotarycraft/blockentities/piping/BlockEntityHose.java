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
import reika.dragonapi.DragonAPI;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.interfaces.PumpablePipe;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;

public class BlockEntityHose extends BlockEntityPiping implements PumpablePipe {

    private int lubricant = 0;
    private int burnIn = 0;

    public BlockEntityHose(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.HOSE.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.HOSE.get();
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateEntity(world, pos);

        if (burnIn > 0) {
            burnIn--;
            if (burnIn == 0) {
                this.doBurn();
            }
        }
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
    public MachineRegistry getMachine() {
        return MachineRegistry.HOSE;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m, Direction dir) {
        return m == MachineRegistry.HOSE || m == MachineRegistry.VALVE || m == MachineRegistry.SEPARATION || m == MachineRegistry.SUCTION;
    }

    @Override
    public boolean hasLiquid() {
        return lubricant > 0;
    }

    @Override
    public Fluid getAttributes() {
        return this.hasLiquid() ? RotaryFluids.LUBRICANT.get() : null;
    }

    @Override
    public int getFluidLevel() {
        return lubricant;
    }

    @Override
    protected void setFluidLevel(int amt) {
        lubricant = amt;
    }

    @Override
    protected void setFluid(Fluid f) {
    }

    @Override
    protected boolean interactsWithMachines() {
        return true;
    }

    @Override
    protected void onIntake(BlockEntity te) {

    }

    @Override
    public boolean isValidFluid(Fluid f) {
        return f.equals(RotaryFluids.LUBRICANT.get());
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
        return Blocks.OAK_PLANKS;
    }

    @Override
    public boolean isConnectedToNonSelf(Direction dir) {
        return false;
    }

    @Override
    public boolean canIntakeFromIFluidHandler(Direction side) {
        return side.getStepY() != 0;
    }

    @Override
    public boolean canOutputToIFluidHandler(Direction side) {
        return side.getStepY() == 0;
    }

    /*
    @Override
    public boolean canTransferTo(PumpablePipe p, Direction dir) {
        return p instanceof BlockEntityHose;
    }

    @Override
    public void transferFrom(PumpablePipe from, int amt) {
        ((BlockEntityHose)from).lubricant -= amt;
        lubricant += amt;
    }
     */
    public void burn() {
        this.burn(true);
    }

    private void burn(boolean immediate) {
        ReikaWorldHelper.ignite(level, worldPosition);
        if (immediate) {
            this.doBurn();
        } else {
            int time = this.hasLiquid() ? 5 + DragonAPI.rand.nextInt(15) : 10 + DragonAPI.rand.nextInt(30);
            if (burnIn <= 0)
                burnIn = time;
        }
    }

    private void doBurn() {
        for (int i = 0; i < 6; i++) {
            if (DragonAPI.rand.nextInt(3) > 0) {
                BlockEntity te = getAdjacentBlockEntity(dirs[i]);
                if (te instanceof BlockEntityHose) {
                    ((BlockEntityHose) te).burn(false);
                }
            }
        }
        level.setBlock(worldPosition, Blocks.FIRE.defaultBlockState(), 1);
        if (this.hasLiquid() && DragonAPI.rand.nextInt(4) == 0)
            level.explode(null, worldPosition.getX() + 0.5, worldPosition.getY() + 0.5, worldPosition.getZ() + 0.5, 2, true, Level.ExplosionInteraction.BLOCK);
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
