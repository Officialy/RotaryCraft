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
import net.minecraft.world.level.block.entity.BlockEntity;
import reika.rotarycraft.api.power.ShaftMerger;
import reika.rotarycraft.auxiliary.PowerSourceList;

import java.util.Collection;

public interface PowerSourceTracker {

    PowerSourceList getPowerSources(PowerSourceTracker io, ShaftMerger caller);

    /**
     * c may contain nulls.
     */
    void getAllOutputs(Collection<BlockEntity> c, Direction dir);

    BlockPos getIoOffsetPos();

}
