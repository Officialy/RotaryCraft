/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.auxiliary.interfaces.PressureTE;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.entities.EntitySonicShot;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

/**
 * Pressure-cannon boring machine (companion to the Item Vacuum). Spins up an internal pressure from
 * incoming shaft power, and once it clears {@link #FIRE_PRESSURE} it fires an {@link EntitySonicShot}
 * down its facing axis. The shot flies to the first solid surface and flattens a {@code (2*FOV+1)²}
 * cross-section there. Overpressure ({@link #MAXPRESSURE}) detonates the machine. Port of the legacy
 * {@code TileEntitySonicBorer}; the 1.7 metadata switch is replaced by the block's {@code FACING}:
 * the borer fires along {@code facing} and reads power from {@code facing.getOpposite()}.
 */
public class BlockEntitySonicBorer extends BlockEntityPowerReceiver implements PressureTE {

    public static final int FIRE_PRESSURE = 400; //4 atm
    public static final int MAXPRESSURE = 1000;
    public static final int FOV = 3;

    public int xstep;
    public int ystep;
    public int zstep;
    private int pressure;

    public BlockEntitySonicBorer(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SONICBORER.get(), pos, state);
    }

    public static boolean canDrop(Level world, BlockPos pos) {
        BlockState bs = world.getBlockState(pos);
        Block b = bs.getBlock();
        if (bs.isAir())
            return true;
        if (bs.getDestroySpeed(world, pos) < 0) // unbreakable (bedrock, etc.)
            return false;
        if (b instanceof LiquidBlock)
            return false;
        return true;
    }

    @Override
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        this.getIOSides(this.getBlockState().getValue(BlockRotaryCraftMachine.FACING));
        this.getPower(false);
        this.updatePressure(world, pos);
        if (this.canFire(world, pos)) {
            this.fire(world, pos);
            pressure -= FIRE_PRESSURE;
        }
        if (pressure > MAXPRESSURE) {
            this.overpressure(world, pos);
        }
    }

    /** Fires along {@code facing}; power comes in from the opposite side. */
    public final void getIOSides(Direction facing) {
        read = facing.getOpposite();
        xstep = facing.getStepX();
        ystep = facing.getStepY();
        zstep = facing.getStepZ();
    }

    private void fire(Level world, BlockPos pos) {
        int r = this.getDistanceToSurface(world, pos);
        if (r < 0)
            return;
        if (!world.isClientSide()) {
            EntitySonicShot e = new EntitySonicShot(world, this);
            world.addFreshEntity(e);
        }
    }

    /**
     * Distance (in blocks along the axis) to the first cross-section that contains a non-air block,
     * or {@code -1} if any cell along the way is undroppable (bedrock/liquid/protected), matching the
     * legacy scan. {@link #getMaxRange()} if nothing solid is hit within range.
     */
    private int getDistanceToSurface(Level world, BlockPos pos) {
        int k = FOV;
        for (int m = 1; m < this.getMaxRange(); m++) {
            int dx = pos.getX() + m * xstep;
            int dy = pos.getY() + m * ystep;
            int dz = pos.getZ() + m * zstep;
            boolean nonair = false;
            for (int a = -k; a <= k; a++) {
                for (int b = -k; b <= k; b++) {
                    BlockPos p;
                    if (xstep != 0) p = new BlockPos(dx, pos.getY() + a, pos.getZ() + b);
                    else if (zstep != 0) p = new BlockPos(pos.getX() + a, pos.getY() + b, dz);
                    else p = new BlockPos(pos.getX() + a, dy, pos.getZ() + b);
                    if (!canDrop(world, p))
                        return -1;
                    if (!world.getBlockState(p).isAir())
                        nonair = true;
                }
            }
            if (nonair)
                return m;
        }
        return this.getMaxRange();
    }

    private int getMaxRange() {
        return Math.max(ConfigRegistry.SONICBORERRANGE.getValue(), 64);
    }

    private boolean canFire(Level world, BlockPos pos) {
        if (pressure < FIRE_PRESSURE)
            return false;
        if (power < MINPOWER || torque < MINTORQUE)
            return false;
        return pos.getY() - this.getDistanceToSurface(world, pos) > world.getMinY() || ystep != -1;
    }

    private int getPressureIncrement() {
        return (int) (power / 65536);
    }

    public int[] getTargetPosn() {
        int r = this.getDistanceToSurface(level, worldPosition);
        if (r < 0)
            r = 0;
        return new int[]{
                worldPosition.getX() + xstep * r,
                worldPosition.getY() + ystep * r,
                worldPosition.getZ() + zstep * r
        };
    }

    @Override
    protected void animateWithTick(Level world, BlockPos pos) {
    }

    @Override
    protected String getTEName() {
        return "sonicborer";
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SONICBORER.get();
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SONICBORER;
    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    public void updatePressure(Level world, BlockPos pos) {
        int Pamb = 101;
        if (world.dimension() == Level.NETHER)
            Pamb = 2000;
        int dP = pressure - Pamb;
        int pd = dP / 384 + 1;
        if (dP > 0)
            pressure -= pd;
        else
            pressure++;
        if (power >= MINPOWER && torque >= MINTORQUE) {
            pressure += this.getPressureIncrement();
        }
    }

    @Override
    public void addPressure(int press) {
        pressure += press;
    }

    @Override
    public int getPressure() {
        return pressure;
    }

    @Override
    public void overpressure(Level world, BlockPos pos) {
        float f = 4;
        boolean grief = ConfigRegistry.BLOCKDAMAGE.getState();
        Level.ExplosionInteraction mode = grief ? Level.ExplosionInteraction.BLOCK : Level.ExplosionInteraction.NONE;
        double x = pos.getX() + 0.5, y = pos.getY() + 0.5, z = pos.getZ() + 0.5;
        world.explode(null, x, y, z, f, mode);
        world.explode(null, x, y + 1, z, f, mode);
        world.explode(null, x, y - 1, z, f, mode);
        world.explode(null, x + 1, y, z, f, mode);
        world.explode(null, x - 1, y, z, f, mode);
        world.explode(null, x, y, z + 1, f, mode);
        world.explode(null, x, y, z - 1, f, mode);
    }

    @Override
    protected void writeSyncTag(CompoundTag NBT) {
        super.writeSyncTag(NBT);
        NBT.putInt("press", pressure);
    }

    @Override
    protected void readSyncTag(CompoundTag NBT) {
        super.readSyncTag(NBT);
        pressure = NBT.getIntOr("press", 0);
    }

    public int getDistanceToSurface() {
        return this.getDistanceToSurface(level, worldPosition);
    }

    @Override
    public int getMaxPressure() {
        return MAXPRESSURE;
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
