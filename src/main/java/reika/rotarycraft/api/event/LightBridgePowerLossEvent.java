/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.event;

import net.minecraft.world.level.block.entity.BlockEntity;
import reika.dragonapi.instantiable.event.TileEntityEvent;

public class LightBridgePowerLossEvent extends TileEntityEvent {
    public LightBridgePowerLossEvent(BlockEntity tile) {
        super(tile);
    }
}
