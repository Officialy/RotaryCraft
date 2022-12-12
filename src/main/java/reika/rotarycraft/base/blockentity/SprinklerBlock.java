/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import reika.dragonapi.instantiable.StepTimer;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.SoundRegistry;

public abstract class SprinklerBlock extends RotaryCraftBlockEntity implements PipeConnector, RangedEffect {

    private final StepTimer soundTimer = new StepTimer(40);
    private int liquid;
    private int pressure;

    public SprinklerBlock(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    private void getLiq(Level world, BlockPos pos) {
        int oldLevel = 0;
        Direction dir = this.getPipeDirection();
        int dx = pos.getX() + dir.getStepX();
        int dy = pos.getY() + dir.getStepY();
        int dz = pos.getZ() + dir.getStepZ();
        MachineRegistry m = MachineRegistry.getMachine(world, new BlockPos(dx, dy, dz));
        if (m != null && m.isStandardPipe()) {
            BlockEntityPipe tile = (BlockEntityPipe) world.getBlockEntity(new BlockPos(dx, dy, dz));
            if (tile != null && tile.contains(Fluids.WATER) && tile.getFluidLevel() > 0) {
                if (liquid < this.getCapacity()) {
                    oldLevel = tile.getFluidLevel();
                    int toremove = tile.getFluidLevel() / 4 + 1;
                    int toadd = Math.min(toremove, this.getCapacity() - liquid);
                    tile.removeLiquid(toadd);
                    liquid = Math.max(liquid + toadd, 0);
                }
                pressure = tile.getPressure();
            }
        }
        if (liquid > this.getCapacity())
            liquid = this.getCapacity();
    }

    public abstract int getCapacity();

    public abstract int getWaterConsumption();

    public abstract Direction getPipeDirection();

    //@Override
    public final void tick(Level world, BlockPos pos) {
        this.getLiq(world, pos);

        if (this.canPerformEffects()) { //&& !AtmosphereHandler.isNoAtmo(world, new BlockPos(pos.getX(), pos.getY() + 1, pos.getZ()), getType(), false)) { todo atmosphere checking
            this.performEffects(world, pos);
            soundTimer.update();
            if (soundTimer.checkCap()) {
                SoundRegistry.SPRINKLER.playSoundAtBlock(world, pos, 1, 1);
            }
            liquid -= this.getWaterConsumption();
        }

        this.doAnimations();
    }

    protected void doAnimations() {

    }

    public final boolean canPerformEffects() {
        return this.getRange() > 0 && liquid >= this.getWaterConsumption();
    }

    protected abstract void performEffects(Level world, BlockPos pos);

    public final int getWater() {
        return liquid;
    }

    public final int getPressure() {
        return pressure;
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);

        NBT.putInt("press", pressure);
        NBT.putInt("lvl", liquid);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);

        pressure = NBT.getInt("press");
        liquid = NBT.getInt("lvl");

        if (liquid < 0)
            liquid = 0;
        if (liquid > this.getCapacity())
            liquid = this.getCapacity();
        if (pressure < 0)
            pressure = 0;
    }

    @Override
    public final int getRange() {
        int val = 0;
        if (pressure <= 0)
            return 0;
        val = pressure / 80;
        if (val > this.getMaxRange())
            val = this.getMaxRange();
        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", val));
        return val;
    }

    @Override
    public final int getMaxRange() {
        return 8;
    }

    @Override
    public final boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    @Override
    public final boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return side == this.getPipeDirection() && p.isStandardPipe();
    }

    @Override
    public int fill(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        if (!resource.getFluid().equals(Fluids.WATER))
            return 0;
        int toadd = Math.min(resource.getAmount(), this.getCapacity() - liquid);
        liquid += toadd;
        return toadd;
    }

    public boolean canFill(Direction side, Fluid f) {
        return f.equals(Fluids.WATER) && side == this.getPipeDirection();
    }

    @Override
    public final FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
        return null;
    }

    @Override
    public final Flow getFlowForSide(Direction side) {
        return side == this.getPipeDirection() ? Flow.INPUT : Flow.NONE;
    }

}
