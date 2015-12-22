/*******************************************************************************
 * @author Reika Kalseki
 * 
 * Copyright 2015
 * 
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package Reika.RotaryCraft.Auxiliary.Interfaces;

import Reika.RotaryCraft.API.Interfaces.BasicTemperatureMachine;


public interface FrictionHeatable extends BasicTemperatureMachine {

	public void addTemperature(int add);

	public int getMaxTemperature();

}
