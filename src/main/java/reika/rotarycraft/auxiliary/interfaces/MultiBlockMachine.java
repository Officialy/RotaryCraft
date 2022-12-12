/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2017
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;


public interface MultiBlockMachine {

	boolean isMultiBlock(Level world, BlockPos pos);

	int[] getMultiBlockPosition(Level world, BlockPos pos);

	int[] getMultiBlockSize(Level world, BlockPos pos);

}
