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

import reika.rotarycraft.api.interfaces.PressureTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface PressureTE extends PressureTile {

    void updatePressure(Level world, BlockPos pos);

    void addPressure(int press);

    void overpressure(Level world, BlockPos pos);

}
