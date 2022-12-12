/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api;


import net.minecraft.core.BlockPos;

/**
 * This is not intended for you to use, but a helper bridge class for PowerTransferHelper
 */
public interface IOMachine {

    BlockPos getWritePos();

    BlockPos getWritePos2();

}
