/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2018
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary.interfaces;

import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;
import reika.dragonapi.interfaces.blockentity.BreakAction;
import reika.rotarycraft.auxiliary.SolarPlant;

public interface SolarPlantBlock extends BreakAction {

    SolarPlant getPlant();

    void setPlant(SolarPlant p);

    void searchForPlant(Level world, BlockPos pos);

}
