package reika.rotarycraft.base.blocks.entity.pipe;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.context.BlockPlaceContext;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.ScheduledTickAccess;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BooleanProperty;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import reika.rotarycraft.auxiliary.interfaces.PipeConnector;
import reika.rotarycraft.base.blockentity.BlockEntityPiping;
import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.registry.MachineRegistry;

/**
 * Shared base for every RotaryCraft fluid-pipe block (fluid pipe, hose, fuel line, separation pipe,
 * suction pipe, bedrock pipe). 1.7.10 drew the whole pipe (frame + fluid window) via a single custom
 * renderer reading {@code BlockBasicMultiTE} metadata; the 26.1 port instead renders the frame as a
 * static multipart blockstate (hand-authored per type under {@code assets/rotarycraft/blockstates/} +
 * {@code models/block/pipe/<type>/}, since each pipe type now has its own Block class instead of one
 * metadata-indexed block) and the fluid as a {@link reika.rotarycraft.renders.PipeRenderer} BER drawn
 * through the frame's open core. The six {@code CONN_*} properties expose the BE's connectivity to the
 * blockstate multipart conditions and drive the per-state collision shape; both flow from the same
 * {@link #canConnect} predicate that {@code BlockEntityPiping#recomputeConnections} uses, so the visual
 * shell and the fluid-flow connections never disagree.
 */
public abstract class BlockPipeShell extends BlockBasicMachine {

    public static final BooleanProperty CONN_DOWN = BooleanProperty.create("conn_down");
    public static final BooleanProperty CONN_UP = BooleanProperty.create("conn_up");
    public static final BooleanProperty CONN_NORTH = BooleanProperty.create("conn_north");
    public static final BooleanProperty CONN_SOUTH = BooleanProperty.create("conn_south");
    public static final BooleanProperty CONN_WEST = BooleanProperty.create("conn_west");
    public static final BooleanProperty CONN_EAST = BooleanProperty.create("conn_east");

    private static final BooleanProperty[] CONN = {CONN_DOWN, CONN_UP, CONN_NORTH, CONN_SOUTH, CONN_WEST, CONN_EAST};

    private static final VoxelShape CORE_SHAPE = Block.box(2, 2, 2, 14, 14, 14);
    private static final VoxelShape[] ARM_SHAPES = {
            Block.box(2, 0, 2, 14, 2, 14),    // DOWN
            Block.box(2, 14, 2, 14, 16, 14),  // UP
            Block.box(2, 2, 0, 14, 14, 2),    // NORTH
            Block.box(2, 2, 14, 14, 14, 16),  // SOUTH
            Block.box(0, 2, 2, 2, 14, 14),    // WEST
            Block.box(14, 2, 2, 16, 14, 14)   // EAST
    };

    protected BlockPipeShell(Properties properties) {
        super(properties.noOcclusion());
        BlockState s = this.stateDefinition.any();
        for (BooleanProperty p : CONN) s = s.setValue(p, false);
        this.registerDefaultState(s);
    }

    /** The {@link MachineRegistry} entry for this pipe type, used for the {@link PipeConnector} handshake. */
    protected abstract MachineRegistry self();

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        super.createBlockStateDefinition(builder);
        builder.add(CONN);
    }

    @Override
    public BlockState getStateForPlacement(BlockPlaceContext ctx) {
        BlockState base = super.getStateForPlacement(ctx);
        if (base == null) base = this.defaultBlockState();
        Level level = ctx.getLevel();
        BlockPos pos = ctx.getClickedPos();
        for (Direction d : Direction.values()) {
            base = base.setValue(CONN[d.ordinal()], this.canConnect(level, pos, d));
        }
        return base;
    }

    @Override
    protected BlockState updateShape(BlockState state, LevelReader level, ScheduledTickAccess ticks, BlockPos pos,
                                     Direction dir, BlockPos neighborPos, BlockState neighborState, RandomSource random) {
        return state.setValue(CONN[dir.ordinal()], this.canConnect(level, pos, dir));
    }

    /**
     * Mirror of {@link BlockEntityPiping#shouldTryToConnect} but operating purely on the neighbour's
     * block + BE state, so it works inside {@link #updateShape} before the BE has had a chance to
     * {@code recomputeConnections}. Any {@link BlockEntityPiping} (regardless of pipe TYPE — a hose can
     * connect to a fluid pipe, matching 1.7.10's {@code instanceof TileEntityPiping} check) connects,
     * as does any {@link PipeConnector} that accepts this pipe's {@link #self()} registry entry.
     */
    private boolean canConnect(LevelReader level, BlockPos pos, Direction dir) {
        BlockPos npos = pos.relative(dir);
        BlockState ns = level.getBlockState(npos);
        if (ns.is(this)) return true;
        BlockEntity nbe = level.getBlockEntity(npos);
        if (nbe instanceof BlockEntityPiping) return true;
        if (nbe instanceof PipeConnector pc) {
            MachineRegistry self = this.self();
            return pc.canConnectToPipe(self) && pc.canConnectToPipeOnSide(self, dir.getOpposite());
        }
        return false;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return shapeForState(state);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return shapeForState(state);
    }

    private static VoxelShape shapeForState(BlockState state) {
        VoxelShape s = CORE_SHAPE;
        for (int i = 0; i < CONN.length; i++) {
            if (state.getValue(CONN[i]))
                s = Shapes.joinUnoptimized(s, ARM_SHAPES[i], BooleanOp.OR);
        }
        return s.optimize();
    }
}
