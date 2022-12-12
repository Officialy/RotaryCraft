/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api.interfaces;

import net.minecraft.world.level.block.entity.BlockEntity;

/**
 * If you have an object that wishes to control a CVT, use this interface. You are responsible for getting the BlockEntity instance yourself.
 * The BlockEntityAdvancedGear/CVTControllable has a setController method. Only one controller per CVT, or you may get strange behavior.
 */
public interface CVTController {

    /**
     * Fetch the CVT instance
     */
    BlockEntity getCVT();

    /**
     * Whether the controls should be applied; if this returns false, the CVT behaves as normal
     */
    boolean isActive();

    /**
     * The ratio chosen. It is clamped to [1,32].
     */
    int getControlledRatio();

    /**
     * True for torque mode, false for speed mode
     */
    boolean isTorque();


    /**
     * The BlockEntityAdvancedGear will implement this.
     */
    interface CVTControllable {

        void setController(CVTController c);

    }

}
