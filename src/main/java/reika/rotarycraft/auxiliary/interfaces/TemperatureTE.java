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

import reika.dragonapi.interfaces.blockentity.ThermalTile;
import reika.rotarycraft.api.interfaces.TemperatureTile;
import net.minecraft.core.BlockPos;
import net.minecraft.world.level.Level;

public interface TemperatureTE extends ThermalTile, TemperatureTile, HeatConduction {

    void updateTemperature(Level world, BlockPos pos);

    void addTemperature(int temp);

    int getThermalDamage();

    void overheat(Level world, BlockPos pos);

}
