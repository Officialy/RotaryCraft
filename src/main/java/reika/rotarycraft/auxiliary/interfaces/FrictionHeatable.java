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

import reika.rotarycraft.api.interfaces.BasicTemperatureMachine;
import reika.rotarycraft.api.interfaces.ThermalMachine;

public interface FrictionHeatable extends BasicTemperatureMachine, ThermalMachine {

    void addTemperature(int add);

    int getMaxTemperature();

}
