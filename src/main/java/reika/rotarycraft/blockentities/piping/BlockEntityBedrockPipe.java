/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.piping;


import net.minecraft.core.BlockPos;
import net.minecraft.world.level.block.state.BlockState;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityBedrockPipe extends BlockEntityPipe {

    public BlockEntityBedrockPipe(BlockPos pos, BlockState state) {
        super(RotaryBlockEntities.BEDROCK_PIPE.get(), pos, state);
    }

    @Override
    public int getMaxPressure() {
        return Integer.MAX_VALUE;
    }

    @Override
    public int getMaxTemperature() {
        return 5000;
    }

    @Override
    public MachineRegistry getMachine() {
        return MachineRegistry.BEDPIPE;
    }

	/*
	@Override
	public IIcon getGlassIcon() {
		return RotaryBlocks.BLASTGLASS.get().getIcon(0, 0);
	}*/

}
