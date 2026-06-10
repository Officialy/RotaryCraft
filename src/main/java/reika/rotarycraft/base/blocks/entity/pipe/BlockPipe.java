package reika.rotarycraft.base.blocks.entity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.piping.BlockEntityPipe;
import reika.rotarycraft.registry.MachineRegistry;

public class BlockPipe extends BlockBasicMachine {

    /**
     * 26.1 port: legacy 1.7 drew the pipe via a custom BE renderer that read the BE's
     * {@code connections[]} array. The 26.1 renderer pipeline (BatchedRenderType /
     * SubmitNodeCollector) makes JSON-driven blockstate rendering the cleaner path —
     * vanilla handles culling, lighting, AO, and batching for us. So we expose the BE's
     * connectivity as six BooleanProperty values on the block state and a multipart
     * blockstate JSON renders {@code pipe_core} + {@code pipe_arm_<dir>} as appropriate.
     * The BE keeps its own {@code connections[]} as the authoritative source for the
     * fluid-flow logic; both flow from the same {@link #canConnect} predicate, so they
     * stay in sync via {@link #updateShape} and {@code BlockEntityPiping#recomputeConnections}.
     */
    public static final BooleanProperty CONN_DOWN = BooleanProperty.create("conn_down");
    public static final BooleanProperty CONN_UP = BooleanProperty.create("conn_up");
    public static final BooleanProperty CONN_NORTH = BooleanProperty.create("conn_north");
    public static final BooleanProperty CONN_SOUTH = BooleanProperty.create("conn_south");
    public static final BooleanProperty CONN_WEST = BooleanProperty.create("conn_west");
    public static final BooleanProperty CONN_EAST = BooleanProperty.create("conn_east");

    private static final BooleanProperty[] CONN = {CONN_DOWN, CONN_UP, CONN_NORTH, CONN_SOUTH, CONN_WEST, CONN_EAST};

    // VoxelShape pieces matching the visible JSON model (12-px iron cross + 2-px arms).
    // The user reported the pipe "doesn't have the right bounding box" — the inherited
    // BlockBasicMachine collision was a full cube, which made placement aim feel wrong
    // and tools like the screwdriver hit a 16-px target where they should hit the visible
    // 12-px cross.
    private static final net.minecraft.world.phys.shapes.VoxelShape CORE_SHAPE  = net.minecraft.world.level.block.Block.box(2, 2, 2, 14, 14, 14);
    private static final net.minecraft.world.phys.shapes.VoxelShape ARM_DOWN_S  = net.minecraft.world.level.block.Block.box(2, 0,  2, 14, 2,  14);
    private static final net.minecraft.world.phys.shapes.VoxelShape ARM_UP_S    = net.minecraft.world.level.block.Block.box(2, 14, 2, 14, 16, 14);
    private static final net.minecraft.world.phys.shapes.VoxelShape ARM_NORTH_S = net.minecraft.world.level.block.Block.box(2, 2,  0, 14, 14, 2);
    private static final net.minecraft.world.phys.shapes.VoxelShape ARM_SOUTH_S = net.minecraft.world.level.block.Block.box(2, 2, 14, 14, 14, 16);
    private static final net.minecraft.world.phys.shapes.VoxelShape ARM_WEST_S  = net.minecraft.world.level.block.Block.box(0, 2,  2, 2,  14, 14);
    private static final net.minecraft.world.phys.shapes.VoxelShape ARM_EAST_S  = net.minecraft.world.level.block.Block.box(14, 2, 2, 16, 14, 14);

    private static final net.minecraft.world.phys.shapes.VoxelShape[] ARM_SHAPES = {
            ARM_DOWN_S, ARM_UP_S, ARM_NORTH_S, ARM_SOUTH_S, ARM_WEST_S, ARM_EAST_S
    };

    public BlockPipe(Properties properties) {
        super(properties.noOcclusion());
        BlockState s = this.stateDefinition.any();
        for (BooleanProperty p : CONN) s = s.setValue(p, false);
        this.registerDefaultState(s);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        long _t0 = System.nanoTime();
        reika.rotarycraft.auxiliary.PipeDebugLog.event("BlockPipe.getStateForPlacement");
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null) base = this.defaultBlockState();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        for (Direction d : Direction.values()) {
            base = base.setValue(CONN[d.ordinal()], canConnect(level, pos, d));
        }
        long _dt = System.nanoTime() - _t0;
        if (_dt > 5_000_000L) reika.rotarycraft.auxiliary.PipeDebugLog.event("BlockPipe.getStateForPlacement.slow_ms_" + (_dt / 1_000_000L));
        return base;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction dir, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        reika.rotarycraft.auxiliary.PipeDebugLog.event("BlockPipe.updateShape");
        return state.setValue(CONN[dir.ordinal()], canConnect(level, pos, dir));
    }

    /**
     * Mirror of {@link BlockEntityPiping#shouldTryToConnect} but operating purely on the
     * neighbour's block + BE state, so it works inside {@link #updateShape} before the BE
     * has had a chance to {@code recomputeConnections}.
     */
    private boolean canConnect(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos npos = pos.relative(dir);
        BlockState ns = level.getBlockState(npos);
        if (ns.is(this)) return true;                              // pipe-to-pipe
        BlockEntity nbe = level.getBlockEntity(npos);
        if (nbe instanceof BlockEntityPiping) return true;          // pipe-to-other-piping (hose/fuelline)
        if (nbe instanceof PipeConnector pc) {
            MachineRegistry self = MachineRegistry.PIPE;
            return pc.canConnectToPipe(self) && pc.canConnectToPipeOnSide(self, dir.getOpposite());
        }
        return false;
    }

    @Override
    protected net.minecraft.world.phys.shapes.VoxelShape getShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext ctx) {
        return shapeForState(state);
    }

    @Override
    protected net.minecraft.world.phys.shapes.VoxelShape getCollisionShape(BlockState state, net.minecraft.world.level.BlockGetter level, BlockPos pos, net.minecraft.world.phys.shapes.CollisionContext ctx) {
        return shapeForState(state);
    }

    private static net.minecraft.world.phys.shapes.VoxelShape shapeForState(BlockState state) {
        net.minecraft.world.phys.shapes.VoxelShape s = CORE_SHAPE;
        for (int i = 0; i < CONN.length; i++) {
            if (state.getValue(CONN[i])) {
                s = net.minecraft.world.phys.shapes.Shapes.joinUnoptimized(
                        s, ARM_SHAPES[i],
                        net.minecraft.world.phys.shapes.BooleanOp.OR);
            }
        }
        return s.optimize();
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityPipe(pPos, pState);
    }

    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityPipe) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }
}

