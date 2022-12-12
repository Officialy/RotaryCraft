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
import reika.dragonapi.instantiable.data.immutable.WorldLocation;

public interface LocationTarget {

    WorldLocation getTarget();

    void setTarget(WorldLocation pos);

}
