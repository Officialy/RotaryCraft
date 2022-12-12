///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.farming;
//
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntityType;
//import net.minecraft.world.level.block.state.BlockState;
//import net.minecraft.world.phys.AABB;
//import net.minecraftforge.fluids.FluidStack;
//import net.minecraftforge.fluids.capability.IFluidHandler;
//import org.jetbrains.annotations.NotNull;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.HybridTank;
//import reika.dragonapi.instantiable.data.WeightedRandom;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
//import reika.rotarycraft.base.blockentity.BlockEntityPiping.Flow;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.registry.MachineRegistry;
//
//public class BlockEntityGroundHydrator extends RotaryCraftBlockEntity implements PipeConnector, IFluidHandler {
//
//    public static final int FLUID_PER_BLOCK = 25;
//    private static final double[][] AREA = {
//            {1, 1, 1, 1, 2, 2, 3, 2, 2, 1, 1, 1, 1},
//            {1, 1, 1, 2, 2, 3, 4, 3, 2, 2, 1, 1, 1},
//            {1, 1, 2, 3, 5, 6, 6, 6, 5, 3, 2, 1, 1},
//            {1, 2, 3, 4, 6, 7, 7, 7, 6, 4, 3, 2, 1},
//            {2, 2, 4, 6, 7, 8, 8, 8, 7, 6, 4, 2, 2},
//            {2, 3, 6, 7, 8, 9, 9, 9, 8, 7, 6, 3, 2},
//            {3, 4, 6, 7, 8, 9, 0, 9, 8, 7, 6, 4, 3},
//            {2, 3, 6, 7, 8, 9, 9, 9, 8, 7, 6, 3, 2},
//            {2, 2, 4, 6, 7, 8, 8, 8, 7, 6, 4, 2, 2},
//            {1, 2, 3, 5, 6, 7, 7, 7, 6, 5, 3, 2, 1},
//            {1, 1, 2, 3, 4, 6, 6, 6, 4, 3, 2, 1, 1},
//            {1, 1, 1, 2, 2, 3, 4, 3, 2, 2, 1, 1, 1},
//            {1, 1, 1, 1, 2, 2, 3, 2, 2, 1, 1, 1, 1},
//    };
//    private static final WeightedRandom<BlockPos> coordinateRand = WeightedRandom.fromArray(AREA);
//    private final HybridTank tank = new HybridTank("hydrator", 1000);
//
//    public BlockEntityGroundHydrator(BlockEntityType<?> type, BlockPos pos, BlockState state) {
//        super(type, pos, state);
//    }
//
//    public static int getRange() {
//        return (AREA.length - 1) / 2;
//    }
//
//    public static boolean affectsBlock(Block b) {
//        return b == Blocks.FARMLAND || b == ForestryHandler.BlockEntry.SOIL.getBlock();
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.HYDRATOR;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return true;
//    }
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        if (!world.isClientSide && tank.getLevel() >= FLUID_PER_BLOCK && DragonAPI.rand.nextInt(this.getTickRate(world)) == 0) {
//            BlockPos c = coordinateRand.getRandomEntry().offset(pos);
//            Block b = world.getBlockState(c).getBlock();
//            boolean flag = false;
//            if (b == Blocks.FARMLAND) {
//                flag = ReikaWorldHelper.hydrateFarmland(world, c.getX(), c.getY(), c.getZ(), false);
//            } else if (b == ForestryHandler.BlockEntry.SOIL.getBlock()) {
//                int gmeta = c.getBlockMetadata(world);
//                SoilType type = ForestryHandler.SoilType.getTypeFromMeta(gmeta);
//                if (type == SoilType.HUMUS) {
//                    flag = this.refreshHumus(world, c.xCoord, c.yCoord, c.zCoord, gmeta);
//                } else if (type == SoilType.BOG_EARTH) {
//                    if (DragonAPI.rand.nextInt(4) == 0)
//                        flag = this.matureBog(world, c.xCoord, c.yCoord, c.zCoord, gmeta);
//                }
//            }
//            if (flag) {
//                tank.removeLiquid(FLUID_PER_BLOCK);
//            }
//        }
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private int getTickRate(Level world) {
//        return ModList.MYSTCRAFT.isLoaded() && ReikaMystcraftHelper.isMystAge(world) && ReikaMystcraftHelper.isSymbolPresent(world, "EnvAccel") ? 1 : 2;
//    }
//
//    private boolean refreshHumus(Level world, BlockPos pos) {
//        int type = meta & 0x3;
//        int grade = meta >> 2;
//
//        if (grade == 0)
//            return false;
//
//        grade--;
//        meta = grade << 2 | type;
//        world.setBlockMetadataWithNotify(pos, meta, 3);
//        return true;
//    }
//
//    private boolean matureBog(Level world, BlockPos pos) {
//        int type = meta & 0x3;
//        int maturity = meta >> 2;
//
//        if (maturity >= 3)
//            return false;
//
//        maturity++;
//        meta = maturity << 2 | type;
//        world.setBlockMetadataWithNotify(pos, meta, 3);
//        return true;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    public int getLevel() {
//        return tank.getLevel();
//    }
//
//    public Fluid getFluid() {
//        return tank.getActualFluid();
//    }
//
//    @Override
//    public boolean canConnectToPipe(MachineRegistry m) {
//        return m.isStandardPipe();
//    }
//
//    @Override
//    public boolean canConnectToPipeOnSide(MachineRegistry p, Direction side) {
//        return this.canConnectToPipe(p);
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    @Override
//    public int fill(Direction from, FluidStack resource, boolean doFill) {
//        return this.canFill(from, resource.getFluid()) ? tank.fill(resource, doFill) : 0;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, FluidStack resource, boolean doDrain) {
//        return null;
//    }
//
//    @Override
//    public FluidStack drain(Direction from, int maxDrain, boolean doDrain) {
//        return null;
//    }
//
//    @Override
//    public boolean canFill(Direction from, Fluid fluid) {
//        return /*from != Direction.UP && */fluid == Fluids.WATER;
//    }
//
//    @Override
//    public boolean canDrain(Direction from, Fluid fluid) {
//        return false;
//    }
//
//    @Override
//    public FluidTankInfo[] getTankInfo(Direction from) {
//        return new FluidTankInfo[]{tank.getInfo()};
//    }
//
//    @Override
//    public Flow getFlowForSide(Direction side) {
//        return Flow.INPUT;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//
//        tank.saveAdditional(NBT);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//
//        tank.load(NBT);
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public AABB getRenderBoundingBox() {
//        return ReikaAABBHelper.getBlockAABB(this).expand(getRange(), 0.5, getRange());
//    }
//
//    @Override
//    public int getTanks() {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack getFluidInTank(int tank) {
//        return null;
//    }
//
//    @Override
//    public int getTankCapacity(int tank) {
//        return 0;
//    }
//
//    @Override
//    public boolean isFluidValid(int tank, @NotNull FluidStack stack) {
//        return false;
//    }
//
//    @Override
//    public int fill(FluidStack resource, FluidAction action) {
//        return 0;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(FluidStack resource, FluidAction action) {
//        return null;
//    }
//
//    @NotNull
//    @Override
//    public FluidStack drain(int maxDrain, FluidAction action) {
//        return null;
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//}
