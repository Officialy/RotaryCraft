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
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.Fluids;
import reika.dragonapi.DragonAPI;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.auxiliary.interfaces.PumpablePipe;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryFluids;

public class BlockEntityPipe extends BlockEntityPiping implements TemperatureTE, PumpablePipe {

    public static final int HORIZLOSS = 0;    // all are 1(friction)+g (10m) * delta h (0 or 1m)
    public static final int UPLOSS = 0;
    public static final int DOWNLOSS = 0;
    private Fluid liquid;
    private int liquidLevel = 0;
    private int temperature;

    public BlockEntityPipe(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.FLUID_PIPE.get(), pos, state);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.FLUID_PIPE.get();
    }

    @Override
    public final void updateEntity(Level world, BlockPos pos) {
        super.updateEntity(world, pos);

        //if (ModList.BCFACTORY.isLoaded() && ModList.REACTORCRAFT.isLoaded()) { //Only if, since need a way to pipe it
//        if (this.contains(Fluids.getFluid("uranium hexafluoride")) || this.contains(Fluids.getFluid("hydrofluoric acid"))) {
//            ReikaSoundHelper.playSoundAtBlock(world, pos, "DragonAPI.rand.fizz");
//            for (int i = 0; i < 6; i++) {
//                Direction dir = dirs[i];
//                int dx = x + dir.getStepX();
//                int dy = y + dir.getStepY();
//                int dz = z + dir.getStepZ();
//                MachineRegistry m = MachineRegistry.getMachine(world, dx, dy, dz);
//                if (m.isStandardPipe()) {
//                    BlockEntityPipe p = (BlockEntityPipe) world.getBlockEntity(dx, dy, dz);
//                    p.setFluid(liquid);
//                    p.addFluid(5);
//                    //ReikaParticleHelper.SMOKE.spawnAroundBlock(world, dx, dy, dz, 8);
//                }
//            }
//            world.setBlockToAir(pos);
//            ReikaParticleHelper.SMOKE.spawnAroundBlock(world, pos, 8);
//        }
        //}

        if (DragonAPI.rand.nextInt(60) == 0) {
            ReikaWorldHelper.temperatureEnvironment(world, pos, this.getTemperature());
        }

        if (liquid != null) {
            int temp = liquid.getFluidType().getTemperature();
            temperature = temp > 750 ? temp - 425 : temp - 273;
            if (temperature > this.getMaxTemperature()) {
                this.overheat(level, worldPosition);
            }
        } else {
            temperature = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        }
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    public int getMaxTemperature() {
        return 2500;
    }

    @Override
    protected void onIntake(BlockEntity te) {

    }


    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.PIPE;
    }

    @Override
    public boolean canConnectToPipe(MachineRegistry m, Direction dir) {
        return m == MachineRegistry.BEDPIPE || m.isStandardPipe() || m == MachineRegistry.VALVE || m == MachineRegistry.SPILLER || m == MachineRegistry.SEPARATION || m == MachineRegistry.BYPASS || m == MachineRegistry.SUCTION;
    }

    @Override
    public boolean hasLiquid() {
        return liquid != null && liquidLevel > 0;
    }

    @Override
    public Fluid getAttributes() {
        return liquid;
    }

    public boolean contains(Fluid f) {
        return f != null && f.equals(liquid);
    }

    @Override
    public void setFluid(Fluid f) {
        liquid = f;
    }

    @Override
    public int getFluidLevel() {
        return liquidLevel;
    }

    @Override
    protected void setFluidLevel(int amt) {
        liquidLevel = amt;
    }

    @Override
    public boolean canTransferTo(PumpablePipe p, Direction dir) {
        return false;
    }

    @Override
    protected boolean interactsWithMachines() {
        return true;
    }

    @Override
    public boolean isValidFluid(Fluid f) {
        if (f == null)
            return false;
        // These fluids require dedicated transport (fuel lines / hoses), so the standard pipe rejects them.
        if (f == RotaryFluids.JET_FUEL.get())
            return false;
        if (f == RotaryFluids.LUBRICANT.get())
            return false;
        if (f == RotaryFluids.ETHANOL.get())
            return false;
        return true;
    }

    @Override
    public boolean canEmitToPipeOn(Direction side) {
        return true;
    }

    @Override
    public boolean canReceiveFromPipeOn(Direction side) {
        return true;
    }

    @Override
    public Block getPipeBlockType() {
        return RotaryBlocks.DECO.get();
    }

    @Override
    public boolean canIntakeFromIFluidHandler(Direction side) {
        return side.getStepY() != 0;
    }

    @Override
    public boolean canOutputToIFluidHandler(Direction side) {
        return side.getStepY() == 0;
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    public final void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public int getThermalDamage() {
        return this.getTemperature() >= 100 ? this.getTemperature() / 400 : 0;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {
        // 26.1 simplified port: legacy 1.7 flood-filled the connected pipe network and turned
        // every pipe in it to flowing lava. {@link BlockArray#recursiveAddWithMetadata} hasn't
        // been ported yet, so we just blow up THIS block instead. The standard intake/dump
        // cycle will still pull fluid OUT of an over-heating pipe before this fires for most
        // realistic configurations.
        if (world.isClientSide()) return;
        ReikaParticleHelper.LAVA.spawnAroundBlock(world, pos, 36);
        world.setBlock(pos, Fluids.FLOWING_LAVA.defaultFluidState().createLegacyBlock(), 1);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        temperature = NBT.getIntOr("temp", 0);
    }

    @Override
    protected String getTEName() {
        return null;
    }
	/*
	@Override
	public boolean canTransferTo(PumpablePipe p, Direction dir) {
		if (p instanceof BlockEntityPipe) {
			Fluid f = ((BlockEntityPipe)p).liquid;
			return f != null ? f.equals(liquid) : true;
		}
		return false;
	}

	@Override
	public void transferFrom(PumpablePipe from, int amt) {
		((BlockEntityPipe)from).liquidLevel -= amt;
		liquid = ((BlockEntityPipe)from).liquid;
		liquidLevel += amt;
	}
	 */

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("temp", temperature);
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

}
