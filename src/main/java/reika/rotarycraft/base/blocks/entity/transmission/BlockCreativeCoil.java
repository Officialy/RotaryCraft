package reika.rotarycraft.base.blocks.entity.transmission;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;

import reika.rotarycraft.blockentities.transmission.BlockEntityCreativeCoil;

/**
 * Creative Coil -- an "infinite Industrial Coil" for easy testing. A thin {@link BlockCoil} subclass:
 * every visual/behavioural aspect (BER, {@code CoilModel}, GUI, capacitor sounds, ticker) is inherited
 * unchanged, only {@link #newBlockEntity} differs, constructing {@link BlockEntityCreativeCoil} (which
 * starts creative and pre-charged) instead of a normal {@code BlockEntityAdvancedGear}.
 */
public class BlockCreativeCoil extends BlockCoil {

	public BlockCreativeCoil(Properties properties) {
		super(properties);
	}

	@Override
	public BlockEntity newBlockEntity(BlockPos pos, BlockState state) {
		return new BlockEntityCreativeCoil(pos, state);
	}
}
