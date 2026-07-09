/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks.entity;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.util.StringRepresentable;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.SoundType;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import net.minecraft.world.level.material.MapColor;
import net.minecraft.world.level.storage.loot.LootParams;
import net.minecraft.world.phys.shapes.CollisionContext;
import net.minecraft.world.phys.shapes.VoxelShape;

import reika.rotarycraft.registry.RotaryBlocks;

import java.util.ArrayList;
import java.util.List;

/**
 * The tunnel-lining block the Boring Machine leaves behind. Not a placeable item — the borer writes
 * it as it advances and reads it back to know how far it has already tunnelled.
 *
 * <p>The 1.7.10 block used raw metadata 0/1/2 for a pipe running along the X/Y/Z axis and 3 for a
 * full-cube "junction" cap (written at the borer face on the first step; {@code skipMiningPipes}
 * treats a junction as skippable regardless of axis). This port encodes that as the {@link Shape}
 * enum property so the borer's axis-vs-bore-direction and junction-sentinel checks survive.</p>
 */
public class BlockMiningPipe extends Block {

    public enum Shape implements StringRepresentable {
        AXIS_X("axis_x", Direction.Axis.X),
        AXIS_Y("axis_y", Direction.Axis.Y),
        AXIS_Z("axis_z", Direction.Axis.Z),
        JUNCTION("junction", null);

        private final String id;
        private final Direction.Axis axis; // null for the junction cap

        Shape(String id, Direction.Axis axis) {
            this.id = id;
            this.axis = axis;
        }

        public Direction.Axis axis() {
            return axis;
        }

        public boolean isJunction() {
            return this == JUNCTION;
        }

        public static Shape forAxis(Direction.Axis a) {
            return switch (a) {
                case X -> AXIS_X;
                case Y -> AXIS_Y;
                case Z -> AXIS_Z;
            };
        }

        @Override
        public String getSerializedName() {
            return id;
        }
    }

    public static final EnumProperty<Shape> SHAPE = EnumProperty.create("shape", Shape.class);

    // Axis bars: full extent along their axis, inset 0.33..0.67 on the other two. Junction: full cube.
    private static final VoxelShape SHAPE_X = Block.box(0, 5.28, 5.28, 16, 10.72, 10.72);
    private static final VoxelShape SHAPE_Y = Block.box(5.28, 0, 5.28, 10.72, 16, 10.72);
    private static final VoxelShape SHAPE_Z = Block.box(5.28, 5.28, 0, 10.72, 10.72, 16);
    private static final VoxelShape SHAPE_CUBE = Block.box(0, 0, 0, 16, 16, 16);

    public BlockMiningPipe() {
        super(RotaryBlocks.blockProperties().mapColor(MapColor.METAL).strength(5, 6).sound(SoundType.METAL));
        this.registerDefaultState(this.stateDefinition.any().setValue(SHAPE, Shape.JUNCTION));
    }

    /** Blockstate for a pipe running along {@code axis}. */
    public BlockState stateForAxis(Direction.Axis axis) {
        return this.defaultBlockState().setValue(SHAPE, Shape.forAxis(axis));
    }

    /** Blockstate for the full-cube junction cap. */
    public BlockState junctionState() {
        return this.defaultBlockState().setValue(SHAPE, Shape.JUNCTION);
    }

    public static Shape shapeOf(BlockState state) {
        return state.getValue(SHAPE);
    }

    @Override
    protected void createBlockStateDefinition(StateDefinition.Builder<Block, BlockState> builder) {
        builder.add(SHAPE);
    }

    @Override
    protected VoxelShape getShape(BlockState state, BlockGetter level, BlockPos pos, CollisionContext ctx) {
        return switch (state.getValue(SHAPE)) {
            case AXIS_X -> SHAPE_X;
            case AXIS_Y -> SHAPE_Y;
            case AXIS_Z -> SHAPE_Z;
            case JUNCTION -> SHAPE_CUBE;
        };
    }

    @Override
    public SoundType getSoundType(BlockState state, LevelReader world, BlockPos pos, Entity entity) {
        return SoundType.METAL;
    }

    // Never drops an item — it's tunnel lining, cleared by hand or by the borer.
    @Override
    public List<ItemStack> getDrops(BlockState state, LootParams.Builder builder) {
        return new ArrayList<>();
    }

    @Override
    public void playerDestroy(Level level, Player player, BlockPos pos, BlockState state, BlockEntity blockEntity, ItemStack stack) {
        level.playSound(player, pos, SoundEvents.STONE_BREAK, SoundSource.BLOCKS, 1, 1);
        level.setBlock(pos, Blocks.AIR.defaultBlockState(), 0);
    }
}
