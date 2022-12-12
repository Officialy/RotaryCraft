/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import java.util.Locale;

public enum Variables {

    PRESSURE(),
    TEMPERATURE(),
    SPEED(),
    TORQUE(),
    POWER(),
    RANGE(),
    DAMAGED(),
    FUEL(),
    OPERATIONTIME();

    private String getText() {
        return "label." + this.name().toLowerCase(Locale.ROOT);
    }

    @Override
    public String toString() {
        return this.getText();
    }
}
