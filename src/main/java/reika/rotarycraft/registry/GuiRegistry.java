/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.registry;

public enum GuiRegistry {

    MACHINE(),
    HANDCRAFT(),
    HANDBOOK(),
    HANDBOOKPAGE(),
    WORLDEDIT(),
    LOADEDHANDBOOK(),
    SAFEPLAYERS(),
    SPYCAM(),
    SLIDE(),
    PATTERN(),
    FILTER();

    private static final GuiRegistry[] guiList = GuiRegistry.values();

    public static GuiRegistry getEntry(int id) {
        return guiList[id];
    }

    public boolean hasContainer() {
        if (this == MACHINE)
            return true;
        if (this == HANDCRAFT)
            return true;
        if (this == WORLDEDIT)
            return true;
        if (this == PATTERN)
            return true;
        return this == FILTER;
    }
    
}
