package reika.rotarycraft.base.fluids;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleOptions;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.tags.FluidTags;
import net.minecraft.util.RandomSource;
import net.minecraft.world.item.Item;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.LevelAccessor;
import net.minecraft.world.level.LevelReader;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.LiquidBlock;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.level.block.state.StateDefinition;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.material.Fluid;
import net.minecraft.world.level.material.FluidState;
import net.minecraftforge.fluids.ForgeFlowingFluid;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Random;

public abstract class HslaFluid extends ForgeFlowingFluid {

    protected HslaFluid(Properties properties) {
        super(properties);
    }

    @Override
    public Fluid getFlowing() {
        return RotaryFluids.HSLA_FLUID_FLOWING.get();
    }

    @Override
    public Fluid getSource() {
        return RotaryFluids.HSLA_FLUID.get();
    }

    @Override
    public Item getBucket() {
        return RotaryItems.MOLTEN_HSLA_BUCKET.get();
    }

    @Override
    public void animateTick(Level worldIn, BlockPos pos, FluidState state, RandomSource random) {
        if (!state.isSource() && !state.getValue(FALLING)) {
            if (random.nextInt(64) == 0) {
                worldIn.playLocalSound(
                        (double) pos.getX() + 0.5D,
                        (double) pos.getY() + 0.5D,
                        (double) pos.getZ() + 0.5D,
                        SoundEvents.LAVA_AMBIENT,
                        SoundSource.BLOCKS,
                        random.nextFloat() * 0.25F + 0.75F,
                        random.nextFloat() + 0.5F, false);
            }
        } else if (random.nextInt(10) == 0) {
            worldIn.addParticle(ParticleTypes.DRIPPING_LAVA,
                    (double) pos.getX() + (double) random.nextFloat(),
                    (double) pos.getY() + (double) random.nextFloat(),
                    (double) pos.getZ() + (double) random.nextFloat(),
                    0.0D,
                    0.0D,
                    0.0D);
        }
    }

    @Override
    public ParticleOptions getDripParticle() {
        return ParticleTypes.LAVA;
    }

    @Override
    protected boolean isRandomlyTicking() {
        return true;
    }

    @Override
    public int getTickDelay(LevelReader world) {
        return 5;
    }

    @Override
    protected float getExplosionResistance() {
        return 100.0F;
    }

    @Override
    protected void beforeDestroyingBlock(LevelAccessor world, BlockPos pos, BlockState state) {
        BlockEntity blockEntity = state.hasBlockEntity() ? world.getBlockEntity(pos) : null;
        Block.dropResources(state, world, pos, blockEntity);
    }

    @Override
    public int getSlopeFindDistance(LevelReader world) {
        return 4;
    }

    @Override
    public int getDropOff(LevelReader world) {
        return 1;
    }

/*    @Override
    public boolean isSame(Fluid fluid) {
        return fluid.is(BzTags.VISUAL_WATER_FLUID);
    }*/

    @Override
    public boolean canBeReplacedWith(FluidState state, BlockGetter world, BlockPos pos, Fluid fluid, Direction direction) {
        return direction == Direction.DOWN && !fluid.is(FluidTags.create(ResourceLocation.parse("molten_metal")));
    }

    @Override
    public BlockState createLegacyBlock(FluidState state) {
        return RotaryFluids.HSLA_FLUID_BLOCK.get().defaultBlockState().setValue(LiquidBlock.LEVEL, getLegacyLevel(state));
    }

    public static class Flowing extends HslaFluid {
        public Flowing(Properties properties) {
            super(properties);
            registerDefaultState(getStateDefinition().any().setValue(LEVEL, 7));
        }

        protected void createFluidStateDefinition(StateDefinition.Builder<Fluid, FluidState> builder) {
            super.createFluidStateDefinition(builder);
            builder.add(BlockStateProperties.LEVEL_FLOWING);
        }

        @Override
        public int getAmount(FluidState state) {
            return state.getValue(BlockStateProperties.LEVEL_FLOWING);
        }

        @Override
        public boolean isSource(FluidState state) {
            return false;
        }

    }

    public static class Source extends HslaFluid {

        public Source(ForgeFlowingFluid.Properties properties) {
            super(properties);
        }

        @Override
        public int getAmount(FluidState state) {
            return 8;
        }

        @Override
        public boolean isSource(FluidState state) {
            return true;
        }

    }
}
