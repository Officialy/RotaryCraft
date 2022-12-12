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

import net.minecraft.core.Direction;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.material.Fluid;

/** Make your TE implement this to be able to use the RotaryCraft pipe renderer classes on your own pipe. */
public interface RenderableDuct {

	/** The block type used for most of the solid texture; usually also the main crafting ingredient */
	Block getPipeBlockType();

	/** Is the pipe able to connect to the given side (relative to the pipe) */
	boolean isConnectionValidForSide(Direction dir);

	/** Is the pipe connected to something that is not another pipe of this type, on the given side (relative to the pipe) */
	boolean isConnectedToNonSelf(Direction dir);

	/** The contained liquid type. Null if empty. */
	Fluid getAttributes();

	boolean isFluidPipe();

}
