/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.auxiliary;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Material;
import reika.dragonapi.instantiable.Interpolation;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.auxiliary.interfaces.TemperatureTE;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityCoolingFin extends RotaryCraftBlockEntity implements TemperatureTE {

    private static final Interpolation reactorTemperatureEfficiency = new Interpolation(false);

    static {
        reactorTemperatureEfficiency.addPoint(-273, 4);
        reactorTemperatureEfficiency.addPoint(-100, 3);
        reactorTemperatureEfficiency.addPoint(-25, 2.5);
        reactorTemperatureEfficiency.addPoint(0, 2);
        reactorTemperatureEfficiency.addPoint(15, 1.5);
        reactorTemperatureEfficiency.addPoint(25, 1.25);
        reactorTemperatureEfficiency.addPoint(50, 1);
        reactorTemperatureEfficiency.addPoint(80, 0.75);
        reactorTemperatureEfficiency.addPoint(100, 0.5);
        reactorTemperatureEfficiency.addPoint(250, 0.25);
        reactorTemperatureEfficiency.addPoint(500, 0.125);
    }

    public int ticks = 512;
    public FinSettings setting = FinSettings.FULL;
    private int targetx;
    private int targety;
    private int targetz;
    private int temperature;

    public BlockEntityCoolingFin(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.COOLING_FIN.get(), pos, state);
    }

    @Override
    public void updateTemperature(Level world, BlockPos pos) {
        int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.WATER) != null)
            Tamb -= 5;
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.LAVA) != null)
            Tamb = 2600;
        if (ReikaWorldHelper.checkForAdjMaterial(world, pos, Material.ICE) != null)
            Tamb -= 15;
        if (Tamb > temperature) {
            temperature++;
        } else {
            //temperature -= Math.max(1, (temperature-Tamb)/16);
            temperature = Math.max(Tamb, temperature - 2);
        }
    }

    public int[] getTarget() {
        return new int[]{targetx, targety, targetz};
    }

    @Override
    public int getThermalDamage() {
        return temperature / 200;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.COOLINGFIN;
    }

    //@Override
    public void updateEntity(Level world, BlockPos pos, Direction dir) {
        tickcount++;
        if (ticks > 0)
            ticks -= 8;
        this.getTargetSide(world, pos, dir);
        BlockEntity te = world.getBlockEntity(new BlockPos(targetx, targety, targetz));
        /*if (!world.isClientSide) {
            if (ModList.IC2.isLoaded() && (te instanceof IReactor || te instanceof IReactorChamber)) {
                this.coolIC2Reactor(world, pos, te);
            }
            if (ModList.BCENERGY.isLoaded() && te instanceof IEngine) {
                //this.coolBCEngine(world, pos, te);
            }
        }*/
        if (tickcount < setting.tickRate)
            return;
        tickcount = 0;
        this.updateTemperature(world, pos);
        if (te instanceof TemperatureTE tr) {
            if (tr.canBeCooledWithFins()) {
                int temp = tr.getTemperature();
                if (temp > temperature) {
                    temperature++;
                    tr.addTemperature(-1);
                }
            }
        }
    }

    /* does not work
    @ModDependent(ModList.BCENERGY)
    private void coolBCEngine(Level world, BlockPos pos, BlockEntity te) {
        TileEngineBase eng = (TileEngineBase)te;
        if (eng instanceof TileEngineStone) {
            float f = 0;
            int temp = (int)eng.heat;
            int dT = temp-temperature;
            if (dT > 0) {
                float dfT = dT/(eng.MAX_HEAT-temperature);
                switch(eng.getEnergyStage()) {
                    case BLUE: //<25%
                        f = 1/128F;
                        break;
                    case GREEN: //25-50%
                        f = 1/32F;
                        break;
                    case YELLOW: //50-75%
                        f = 0.25F;
                        break;
                    case RED: //75-99%
                        f = 0.75F;
                        break;
                    case OVERHEAT: //>=100%
                        f = 0;
                        break;
                }
                if (f > 0) {
                    float rem = eng.heat*f*dfT*5;
                    eng.heat -= rem;
                    int Tamb = ReikaWorldHelper.getAmbientTemperatureAt(world, pos);
                    temperature = (int)(Tamb*0.2+0.8*eng.heat);
                }
            }
        }
    }
     */
    /*@ModDependent(ModList.IC2)
    private void coolIC2Reactor(Level world, BlockPos pos, Object te) {
        if (this.tickcount % 20 != 0)
            return;
        if (te instanceof IReactorChamber) {
            te = ((IReactorChamber) te).getReactor();
        }
        IReactor r = (IReactor) te;
        int h = r.getHeat();
        if (h > 0) {
            int rem = Math.max(1, (int) Math.min(20 * setting.rawMultiplier * reactorTemperatureEfficiency.getValue(temperature), h));
            //ReikaJavaLibrary.pConsole(temperature+" > "+reactorTemperatureEfficiency.getValue(temperature));
            r.addHeat(-rem); //20 == 4x 1 reactor heat vent
            int net = 500 * h / r.getMaxHeat();
            //ReikaJavaLibrary.pConsole(h+" of "+r.getMaxHeat()+" > "+net);
            temperature += Math.max(1, Math.min(net - temperature, rem));
        }
    }*/

    private void getTargetSide(Level world, BlockPos pos, Direction dir) {
        switch (dir) {
            case DOWN -> {
                targetx = pos.getX();
                targety = pos.getY() - 1;
                targetz = pos.getZ();
            }
            case UP -> {
                targetx = pos.getX();
                targety = pos.getY() + 1;
                targetz = pos.getZ();
            }
            case NORTH -> {
                targetx = pos.getX();
                targety = pos.getY();
                targetz = pos.getZ() - 1;
            }
            case EAST -> {
                targetx = pos.getX() - 1;
                targety = pos.getY();
                targetz = pos.getZ();
            }
            case SOUTH -> {
                targetx = pos.getX();
                targety = pos.getY();
                targetz = pos.getZ() + 1;
            }
            case WEST -> {
                targetx = pos.getX() + 1;
                targety = pos.getY();
                targetz = pos.getZ();
            }
        }
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public void addTemperature(int temp) {
        temperature += temp;
    }

    @Override
    public int getTemperature() {
        return temperature;
    }

    public void setTemperature(int temp) {
        temperature = temp;
    }

    @Override
    public void overheat(Level world, BlockPos pos) {

    }

    @Override
    public Block getBlockEntityBlockID() {
        return null;
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
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("tick", ticks);
        NBT.putInt("setting", setting.ordinal());
        NBT.putInt("temp", temperature);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        ticks = NBT.getInt("tick");
        setting = FinSettings.list[NBT.getInt("setting")];
        temperature = NBT.getInt("temp");
    }

    @Override
    protected String getTEName() {
        return null;
    }

    @Override
    public void onEMP() {
    }

    @Override
    public boolean canBeCooledWithFins() {
        return false;
    }

    @Override
    public boolean allowExternalHeating() {
        return false;
    }

    @Override
    public boolean allowHeatExtraction() {
        return true;
    }

    @Override
    public int getAmbientTemperature() {
        return 0;
    }

    @Override
    public int getMaxTemperature() {
        return 2500;
    }

    @Override
    public boolean hasAnInventory() {
        return false;
    }

    @Override
    public boolean hasATank() {
        return false;
    }

    public enum FinSettings {
        FULL(20),
        HALF(40),
        QUARTER(80);

        private static final FinSettings[] list = values();
        public final int tickRate;
        public final float rawMultiplier;

        FinSettings(int n) {
            tickRate = n;
            rawMultiplier = 20F / tickRate;
        }

        public FinSettings next() {
            return this.ordinal() == list.length - 1 ? list[0] : list[this.ordinal() + 1];
        }
    }
}
