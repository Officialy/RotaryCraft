package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.entity.BlockEntityTicker;
import net.minecraft.world.level.block.entity.BlockEntityType;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.BooleanOp;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;

import reika.rotarycraft.base.blocks.BlockBasicMachine;
import reika.rotarycraft.blockentities.storage.BlockEntityReservoir;

public class BlockReservoir extends BlockBasicMachine {

    /**
     * 26.1 port: legacy 1.7 reservoir collision was a hollow tray — 1px floor + four 1px walls
     * with the top open so players could walk in / pour buckets in from above. The port had
     * been falling back on the default solid cube, so the user couldn't enter the bowl AND
     * vanilla's bucket-on-block logic couldn't reach the top face. Build the U-shape lazily
     * from five primitive boxes and union them so {@code getShape} and {@code getCollisionShape}
     * both return the hollow tray.
     */
    private static final VoxelShape FLOOR      = Block.box(0, 0, 0, 16, 1, 16);
    private static final VoxelShape WALL_NORTH = Block.box(0, 0, 0, 16, 16, 1);
    private static final VoxelShape WALL_SOUTH = Block.box(0, 0, 15, 16, 16, 16);
    private static final VoxelShape WALL_WEST  = Block.box(0, 0, 0, 1, 16, 16);
    private static final VoxelShape WALL_EAST  = Block.box(15, 0, 0, 16, 16, 16);

    /** Full hollow tray — used as a fallback when there's no level / BE present. */
    private static final VoxelShape HOLLOW_SHAPE;

    static {
        VoxelShape s = Shapes.or(FLOOR, WALL_NORTH);
        s = Shapes.joinUnoptimized(s, WALL_SOUTH, BooleanOp.OR);
        s = Shapes.joinUnoptimized(s, WALL_WEST,  BooleanOp.OR);
        s = Shapes.joinUnoptimized(s, WALL_EAST,  BooleanOp.OR);
        HOLLOW_SHAPE = s.optimize();
    }

    /**
     * Build the collision shape lazily based on which sides this reservoir is connected to —
     * a connected side has no wall (visually skipped by the BE renderer), so the collision
     * should match. Without this the user could walk into one bowl but hit an invisible wall
     * between adjacent reservoirs ("reservoirs connected still have some sides that are
     * collidable, yet invisible").
     */
    private static VoxelShape shapeFor(BlockGetter level, BlockPos pos) {
        if (!(level.getBlockEntity(pos) instanceof BlockEntityReservoir tile)) return HOLLOW_SHAPE;
        VoxelShape s = FLOOR;
        if (!tile.isConnectedOnSide(Direction.NORTH)) s = Shapes.joinUnoptimized(s, WALL_NORTH, BooleanOp.OR);
        if (!tile.isConnectedOnSide(Direction.SOUTH)) s = Shapes.joinUnoptimized(s, WALL_SOUTH, BooleanOp.OR);
        if (!tile.isConnectedOnSide(Direction.WEST))  s = Shapes.joinUnoptimized(s, WALL_WEST,  BooleanOp.OR);
        if (!tile.isConnectedOnSide(Direction.EAST))  s = Shapes.joinUnoptimized(s, WALL_EAST,  BooleanOp.OR);
        return s.optimize();
    }

    public BlockReservoir(Properties properties) {
        super(properties.noOcclusion());
    }


    @Override
    public BlockEntity newBlockEntity(BlockPos pPos, BlockState pState) {
        return new BlockEntityReservoir(pPos, pState);
    }


    @Override
    public <T extends BlockEntity> BlockEntityTicker<T> getTicker(Level pLevel, BlockState pState, BlockEntityType<T> pBlockEntityType) {
        return pLevel.isClientSide() ? null : ((pLevel1, pPos, pState1, pBlockEntity) -> {
            ((BlockEntityReservoir) pBlockEntity).updateEntity(pLevel1, pPos);
        });
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return shapeFor(level, pos);
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return shapeFor(level, pos);
    }

    @Override
    protected VoxelShape getInteractionShape(BlockState state, BlockGetter level, BlockPos pos) {
        // Use the full cube so right-click probes the whole footprint (lets the player aim the
        // bucket at the top opening from above without needing to hit a 1px-thick wall edge).
        return Shapes.block();
    }

    @Override
    protected boolean isCustomRendered() { return true; }
}
