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


import net.minecraft.core.Direction;

public interface PumpablePipe {

	int getFluidLevel();

	boolean canTransferTo(PumpablePipe p, Direction dir);

	void transferFrom(PumpablePipe from, int amt);

}
