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

import net.minecraftforge.common.MinecraftForge;
import reika.dragonapi.instantiable.data.maps.PlayerMap;

import java.util.ArrayList;

public class TutorialTracker {

    public static final TutorialTracker instance = new TutorialTracker();

    private final PlayerMap<Hint> data = new PlayerMap();
//    private final EnumMap<MachineRegistry, HintList> machineHints = new EnumMap(MachineRegistry.class);
//    private final EnumMap<EngineType, HintList> engineHints = new EnumMap(EngineType.class);

    private TutorialTracker() {
        MinecraftForge.EVENT_BUS.register(this);
    }
//
//    public void placeMachine(MachineRegistry m, Player ep) {
//
//    }
//
//    public void placeEngine(EngineType e, Player ep) {
//
//    }

    private static class Hint {

        public final String text;

        private Hint(String s) {
            text = s;
        }

    }

    private static class HintList {

        private final ArrayList<Hint> list = new ArrayList<>();

    }

}
