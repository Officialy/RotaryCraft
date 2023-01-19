/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.engine;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.damagesource.DamageSource;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.properties.IntegerProperty;
import net.minecraft.world.phys.AABB;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.libraries.ReikaDirectionHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.registry.*;

import java.util.List;

public class BlockEntityWindEngine extends BlockEntityEngine {

    public static final IntegerProperty DAMAGE = IntegerProperty.create("damage", 0, 3);
    private WindClearanceCheck clearance;

    public BlockEntityWindEngine(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.WIND_ENGINE.get(), pos, state);
        type = EngineType.WIND;
    }
    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.WIND_ENGINE;
    }
    private void dealBladeDamage(Level world, BlockPos pos) {
        int c = 0;
        int d = 0;
        int a = 0;
        int b = 0;
        if (getBlockState().getValue(DAMAGE) < 2) //todo might not work
            b = 1;
        else
            a = 1;
        switch (DAMAGE.getPossibleValues().size()) {
            case 0 -> c = 1;
            case 1 -> c = -1;
            case 2 -> d = 1;
            case 3 -> d = -1;
        }
        AABB box = new AABB(pos.getX() + c, pos.getY(), pos.getZ() + d, pos.getX() + 1 + c, pos.getY() + 1, pos.getZ() + 1 + d).expandTowards(a, 1, b);
        List<LivingEntity> in = world.getEntitiesOfClass(LivingEntity.class, box);
        for (LivingEntity ent : in) {
            ent.hurt(DamageSource.GENERIC, 1);
        }
    }

    @Override
    protected void consumeFuel() {

    }

    @Override
    protected void internalizeFuel() {

    }

    private float getWindFactor(Level world, BlockPos pos) {

//        if (AtmosphereHandler.isNoAtmo(world, x - this.getWriteDirection().getStepX(), y, z - this.getWriteDirection().getStepZ(), blockType, false))
//            return 0;

        if (clearance == null)
            clearance = new WindClearanceCheck(this, 32, 3);
        clearance.tick(world);

        float f = 1F - clearance.getPenalty();
//        f *= PlanetDimensionHandler.getWindFactor(world);
        return Math.min(1, f);
    }

    @Override
    protected boolean getRequirements(Level world, BlockPos pos) {
        int c = 0;
        int d = 0;
        int a = 0;
        int b = 0;
        if (getBlockState().getValue(DAMAGE) < 2) //todo might not work
            b = 1;
        else
            a = 1;
        switch (DAMAGE.getPossibleValues().size()) {
            case 0 -> c = 1;
            case 1 -> c = -1;
            case 2 -> d = 1;
            case 3 -> d = -1;
        }
        for (int i = -1; i <= 1; i++) {
            for (int j = -1; j <= 1; j++) {
                if (!ReikaWorldHelper.softBlocks(world, new BlockPos(pos.getX() + a * i + c, pos.getY() + j, pos.getZ() + b * i + d))) {
                    omega = 0;
                    return false;
                }
            }
        }
        return true;
    }

    @Override
    protected void playSounds(Level world, BlockPos pos, float pitchMultiplier, float volume) {
        soundtick++;
        if (this.isMuffled(world, pos)) {
            volume *= 0.3125F;
        }

        if (soundtick < this.getSoundLength(1F / pitchMultiplier) && soundtick < 2000)
            return;
        soundtick = 0;

        SoundRegistry.WIND.playSoundAtBlock(world, pos, 1.1F * volume, 1F * pitchMultiplier);
    }

    @Override
    public int getFuelLevel() {
        return 0;
    }

    @Override
    protected int getMaxSpeed(Level world, BlockPos pos) {
        return (int) (EngineType.WIND.getSpeed() * this.getWindFactor(world, pos));
    }

    @Override
    protected void affectSurroundings(Level world, BlockPos pos) {
        this.dealBladeDamage(world, pos);
    }

    @Override

    public AABB getRenderBoundingBox() {
        return new AABB(worldPosition).expandTowards(1, 1, 1);//ReikaAABBHelper.getBlockAABB(xCoord, yCoord, zCoord).expand(1, 1, 1);
    }

    @Override
    public void onRotate() {
        clearance = null;
    }

    @Override
    public boolean isEmpty() {
        return false;
    }

    @Override
    public ItemStack getItem(int pIndex) {
        return null;
    }

    @Override
    public ItemStack removeItem(int pIndex, int pCount) {
        return null;
    }

    @Override
    public ItemStack removeItemNoUpdate(int pIndex) {
        return null;
    }

    @Override
    public void setItem(int pIndex, ItemStack pStack) {

    }

    @Override
    public boolean stillValid(Player pPlayer) {
        return false;
    }

    @Override
    public void clearContent() {

    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.WIND_ENGINE.get();
    }

    @Override
    protected String getTEName() {
        return "windengine";
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

    @Override
    public int fillPipe(Direction from, FluidStack resource, IFluidHandler.FluidAction action) {
        return 0;
    }

    @Override
    public FluidStack drainPipe(Direction from, int maxDrain, IFluidHandler.FluidAction doDrain) {
        return null;
    }

    private static class WindClearanceCheck {

        private final BlockPos engine;
        private final Direction direction;

        private final int stepRatio;
        private final int searchDistance;
        private final double[] blockFraction;
        private int step = 1;
        private boolean dirty;

        private float penalty;

        private WindClearanceCheck(BlockEntityWindEngine te, int search, int step) {
            engine = te.getBlockPos();
            direction = te.getWriteDirection().getOpposite();

            searchDistance = search;
            stepRatio = step;

            blockFraction = new double[searchDistance];

            for (int i = 1; i <= searchDistance; i++) {
                this.scanRow(te.level, i);
            }
        }

        private void tick(Level world) {
            this.scanRow(world, step);

            step++;
            if (step > searchDistance)
                step = 1;
        }

        private void scanRow(Level world, int step) {
            int r = 1 + ((step - 1) / stepRatio);
            Direction dir = ReikaDirectionHelper.getRightBy90(direction);

            //ReikaJavaLibrary.pConsole("Scanning row "+step+", center coord is "+engine.offset(direction, step));

            int blocked = 0;
            for (int i = -r; i <= r; i++) {
                //lastBlockLocations[step-1][i+r] = blockLocations[step-1][i+r];

                int dx = engine.getX() + direction.getStepX() * step + dir.getStepX() * i;
                int dy = engine.getY();
                int dz = engine.getZ() + direction.getStepZ() * step + dir.getStepZ() * i;
                //blockLocations[step-1][i+r] = !ReikaWorldHelper.softBlocks(world, dx, dy, dz);

                if (!ReikaWorldHelper.softBlocks(world, new BlockPos(dx, dy, dz))) {
                    blocked++;
                }
            }

            double frac = blocked / (r * 2D + 1);
            //ReikaJavaLibrary.pConsole("Row "+step+" is "+(frac*100)+" % blocked.");
            if (blockFraction[step - 1] != frac) {
                blockFraction[step - 1] = frac;
                dirty = true;
            }
        }

        private void calculatePenalty() {
            penalty = 0;
            double max = 0;
            for (int i = 0; i < blockFraction.length; i++) {
                //double rowValue = 1+1D/searchDistance-((i+1)/(double)searchDistance);
                double pow = 1.2D - (i / (double) searchDistance);
                double rowValue = Math.pow(pow, 12);
                penalty += rowValue * blockFraction[i];
                max += rowValue;

                //penalty = (float)Math.max(penalty, blockFraction[i]);
            }
            penalty /= Math.sqrt(max);
            penalty = (float) Math.sqrt(penalty);
            //ReikaJavaLibrary.pConsole(penalty);
            //ReikaJavaLibrary.pConsole(penalty+" of "+max);
        }

        public float getPenalty() {
            if (dirty) {
                this.calculatePenalty();
                dirty = false;
            }
            return penalty;
        }

    }
}
