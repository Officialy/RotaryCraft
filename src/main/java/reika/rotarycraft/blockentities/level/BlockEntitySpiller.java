package reika.rotarycraft.blockentities.level;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.util.Mth;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.fluids.FluidStack;
import net.minecraftforge.fluids.capability.IFluidHandler;
import org.jetbrains.annotations.NotNull;
import reika.dragonapi.instantiable.HybridTank;
import reika.dragonapi.instantiable.data.immutable.BlockKey;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityAreaFiller;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;
import reika.rotarycraft.registry.RotaryBlocks;

public class BlockEntitySpiller extends BlockEntityAreaFiller implements IFluidHandler, PipeConnector {

    private final HybridTank tank = new HybridTank("flooder", 4000);

    public BlockEntitySpiller(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.SPILLER.get(), pos, state);
    }

    private Block getFluidID() {
        return !tank.isEmpty() /* && tank.getActualFluid().getAttributes().canBePlacedInWorld()*/ ? tank.getActualFluid().getFluid().defaultFluidState().createLegacyBlock().getBlock() : null;
    }

    private boolean canTakeLiquid(Fluid f) {
//        if (!f.getAttributes().canBePlacedInWorld())
//            return false;
        if (tank.isEmpty())
            return true;
        return tank.getActualFluid().equals(f);
    }

    @Override
    public Block getBlockEntityBlockID() {
        return RotaryBlocks.SPILLER.get();
    }

    @Override
    protected void animateWithTick(Level level, BlockPos blockPos) {

    }

    @Override
    public int getRedstoneOverride() {
        return 0;
    }

    @Override
    protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag);
        tank.writeToNBT(tag);
    }

    @Override
    protected void readSyncTag(CompoundTag tag) {
        tank.readFromNBT(tag);
        super.readSyncTag(tag);
    }

    @Override
    protected String getTEName() {
        return "spiller";
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.SPILLER;
    }

    //@Override
    public boolean canConnectToPipe(MachineRegistry m) {
        return m.isStandardPipe();
    }

    //@Override
    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
        return this.canConnectToPipe(p) && side != Direction.DOWN;
    }

    @Override
    public int fill(Direction from, FluidStack resource, FluidAction action) {
        return this.canFill(from, resource.getFluid()) ? tank.fill(resource, action) : 0;
    }

    @Override
    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
        return null;
    }

    //@Override
    public boolean canFill(Direction from, Fluid fluid) {
        return from != Direction.DOWN;//todo  && fluid.canBePlacedInWorld();
    }

//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }

    @Override
    public boolean hasModelTransparency() {
        return false;
    }

    //@Override
    public BlockEntityPiping.Flow getFlowForSide(Direction side) {
        return side != Direction.DOWN ? BlockEntityPiping.Flow.INPUT : BlockEntityPiping.Flow.NONE;
    }

    @Override
    protected boolean hasRemainingBlocks() {
        return tank.getLevel() >= 1000;
    }

    @Override
    protected void onBlockPlaced() {
        tank.drain(1000, FluidAction.EXECUTE);
    }

    @Override
    protected BlockKey getNextPlacedBlock() {
        Block b = this.getFluidID();
        return b != null ? new BlockKey(b) : null;
    }

    @Override
    protected long getRequiredPower() {
        int visc = tank.getActualFluid().getFluid().getFluidType().getViscosity();
        return Math.max(128, 512 * visc / 1000);
    }

    @Override
    public int getOperationTime() {
        int base = super.getOperationTime();
        if (tank.isEmpty())
            return base;
        return Mth.clamp(base * tank.getActualFluid().getFluid().getFluidType().getViscosity() / 1000, base / 4, base * 4);
    }

    @Override
    protected boolean allowFluidOverwrite() {
        return false;
    }

    @Override
    public int getTanks() {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack getFluidInTank(int tank) {
        return null;
    }

    @Override
    public int getTankCapacity(int tank) {
        return 0;
    }

    @Override
    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
        return false;
    }

    @Override
    public int fill(FluidStack resource, FluidAction action) {
        return 0;
    }

    @NotNull
    @Override
    public FluidStack drain(FluidStack resource, FluidAction action) {
        return null;
    }

    @NotNull
    @Override
    public FluidStack drain(int maxDrain, FluidAction action) {
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
