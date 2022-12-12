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

import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;

public enum BlastGate {

    ALUMITE("ingotAlumite"),
    OBSIDINGOT("ingotObsidian"),
    OBSIDIAN(Blocks.OBSIDIAN),
    STEEL("ingotSteel"),
    COPPER("ingotCopper"),
    GOLD(Items.GOLD_INGOT),
    SILVER("ingotSilver"),
    BRASS("ingotBrass"),
    BRONZE("ingotBronze"),
    INVAR("ingotInvar"),
    TITANIUM("ingotTitanium"),
    DARKSTEEL("ingotDarkSteel"),
    MANYULLYN("ingotManyullyn"),
    GOLDGEAR("goldGear"),
    TERRASTEEL("ingotTerrasteel"),
    MANASTEEL("ingotManasteel"),
    VOIDMETAL("ingotVoid"),
    THAUMIUM("ingotThaumium"),
    OSMIUM("ingotOsmium"),
    SIGNALUM("ingotSignalum"),
    ENDERIUM("ingotEnderium");

    private static final BlastGate[] matList = values();
    private final Object item;

    BlastGate(Object o) {
        item = o;
    }

    public Object getItem() {
        return item;
    }
}
