/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.world.level.Level;

import java.util.ArrayList;

/**
 * Make your block or BlockEntity implements this to have custom Angular Transducer display.
 */
public interface Transducerable {

    /**
     * Each entry in the list is a new line in chat.
     */
    ArrayList<String> getMessages(Level world, BlockPos pos, Direction side);

}
