package reika.rotarycraft.base.blockentity;

import net.minecraft.core.BlockPos;
import net.minecraft.sounds.SoundEvent;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import reika.dragonapi.instantiable.Comparators;
import reika.dragonapi.instantiable.data.blockstruct.BlockArray;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.dragonapi.libraries.level.ReikaBlockHelper;
import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
import reika.rotarycraft.registry.ConfigRegistry;
import reika.rotarycraft.registry.DurationRegistry;

import java.util.Comparator;

public abstract class BlockEntityAreaFiller extends BlockEntityPowerReceiver implements DiscreteFunction {

    private final BlockArray blocks = new BlockArray();

    private Comparator<BlockPos> distanceComparator;

    public BlockEntityAreaFiller(BlockEntityType<?> type, BlockPos pos, BlockState state) {
        super(type, pos, state);
    }

    //@Override
    public void updateEntity(Level world, BlockPos pos) {
        if (world.isClientSide())
            return;

        this.getSummativeSidedPower();

        this.legacyFunction(world, pos);
    }

    @Override
    protected void onFirstTick(Level world, BlockPos pos) {
        super.onFirstTick(world, pos);

        distanceComparator = new Comparators.CoordinateDistanceComparator(worldPosition);
    }

    private void legacyFunction(Level world, BlockPos pos) {
        tickcount++;
        if (power >= MINPOWER && this.hasRemainingBlocks()) {
            BlockKey bk = this.getNextPlacedBlock();
            if (bk != null && power >= this.getRequiredPower()) {
                if (blocks.isEmpty()) {
                    int r = ConfigRegistry.SPILLERRANGE.getValue();
                    if (r > 0) {
                        blocks.recursiveAddWithBounds(world, pos.getX(), pos.getY() - 1, pos.getZ(), Blocks.AIR, pos.getX() - r, 1, pos.getZ() - r, pos.getX() + r, pos.getY() - 1, pos.getZ() + r);
                        if (this.allowFluidOverwrite())
                            blocks.recursiveAddLiquidWithBounds(world, pos.getX(), pos.getY() - 1, pos.getZ(), pos.getX() - r, 0, pos.getZ() - r, pos.getX() + r, pos.getY() - 1, pos.getZ() + r, null);
                        if (ReikaBlockHelper.isLiquid(bk.blockID))
                            blocks.recursiveAddWithBoundsNoFluidSource(world, pos.getX(), pos.getY() - 1, pos.getZ(), bk.blockID.getBlock(), pos.getX() - r, 0, pos.getZ() - r, pos.getX() + r, pos.getY() - 1, pos.getZ() + r);
                    }
                    blocks.sort(distanceComparator);
                    blocks.sortBlocksByHeight(false);
                }
                if (tickcount >= this.getOperationTime() && !blocks.isEmpty()) {
                    tickcount = 0;
                    BlockPos c = blocks.getNextAndMoveOn();
                    world.setBlock(c, bk.blockID, 0);
                    //bk.blockID.onBlockAdded(world, c.getX(), c.getY(), c.getZ());
                    //ReikaJavaLibrary.pConsole(c.xCoord+" "+c.yCoord+" "+c.zCoord);
                    //world.sendBlockUpdated(pos, this.getBlockState(), this.getBlockState(), 3);
                    SoundEvent snd = bk.blockID.getSoundType().getBreakSound();
                    float f = 1;
                    if (ReikaBlockHelper.isLiquid(bk.blockID)) {
                        f = 0.5F + rand.nextFloat();
                        snd = SoundEvents.GENERIC_SWIM;
                    }
                    world.playLocalSound(c.getX(), c.getY(), c.getZ(), snd, SoundSource.BLOCKS, 1, f, true);
                    this.onBlockPlaced();
                }
            }
        } else {
            //blocks.clear();
        }
    }

    protected abstract boolean allowFluidOverwrite();

    protected abstract long getRequiredPower();

    protected abstract void onBlockPlaced();

    protected abstract boolean hasRemainingBlocks();

    protected abstract BlockKey getNextPlacedBlock();

    public int getOperationTime() {
        return DurationRegistry.FILLER.getOperationTime(omega);
    }
}
