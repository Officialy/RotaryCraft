package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.EntityBlock;
import net.minecraft.world.level.block.RenderShape;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.Shapes;
import net.minecraft.world.phys.shapes.VoxelShape;
import reika.rotarycraft.blockentities.production.BlockEntityBedrockSlice;

public class BlockBedrockSlice extends Block implements EntityBlock {

    public BlockBedrockSlice(Properties properties) {
        super(properties.noOcclusion());
    }

    @Override
    public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
        return new BlockEntityBedrockSlice(pos, state);
    }

    @Override
    protected RenderShape getRenderShape(BlockState state) {
        return RenderShape.MODEL;
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        if (level.getBlockEntity(pos) instanceof BlockEntityBedrockSlice te) {
            return shapeFor(te.getMachineDirection(), te.getWear());
        }
        return Shapes.block();
    }

    @Override
    protected VoxelShape getCollisionShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return this.getShape(state, level, pos, ctx);
    }

    private static VoxelShape shapeFor(Direction dir, int wear) {
        double f = wear / 16D; // fraction ground away, cut from the face the drill bites into
        double x1 = 0, y1 = 0, z1 = 0, x2 = 1, y2 = 1, z2 = 1;
        switch (dir) {
            case EAST -> x2 = 1 - f;
            case WEST -> x1 = f;
            case UP -> y2 = 1 - f;
            case DOWN -> y1 = f;
            case SOUTH -> z2 = 1 - f;
            case NORTH -> z1 = f;
        }
        if (x2 <= x1 || y2 <= y1 || z2 <= z1)
            return Shapes.empty();
        return Shapes.box(x1, y1, z1, x2, y2, z2);
    }

    // Indestructible by players and entities — only the bedrock breaker removes it.
    @Override
    protected float getDestroyProgress(BlockState state, Player player, BlockGetter level, BlockPos pos) {
        return 0;
    }

    @Override
    public boolean canEntityDestroy(BlockState state, BlockGetter level, BlockPos pos, Entity entity) {
        return false;
    }
}
