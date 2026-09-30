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


public interface SodiumSolarUpgrades {

	boolean isActive();

	interface SodiumSolarReceiver extends SodiumSolarUpgrades {

		/** Cold sodium supplied by the integration, without making RotaryCraft depend on that mod. */
		default net.minecraft.world.level.material.Fluid getSodiumFluid() {
			return reika.rotarycraft.registry.RotaryFluids.SODIUM.get();
		}

		void tick(int mirrorCount, float totalBrightness);

		int getTemperature();

	}

	interface SodiumSolarOutput extends SodiumSolarUpgrades {

		int receiveSodium(int amt);

	}

}
