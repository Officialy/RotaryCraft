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
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

public interface CachedConnection {

	boolean isConnectionValidForSide(Direction dir);

	void recomputeConnections(Level world, BlockPos pos);

	boolean shouldTryToConnect(Direction dir);

	void deleteFromAdjacentConnections(Level world, BlockPos pos);

	void addToAdjacentConnections(Level world, BlockPos pos);

}
