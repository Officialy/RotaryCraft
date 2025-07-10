package reika.rotarycraft.api;

import java.util.Locale;

public enum RoCWoodTypes {
    OAK, BIRCH, SPRUCE, JUNGLE, ACACIA, DARK_OAK, WARPED, CRIMSON, LIVINGWOOD;

    public static final RoCWoodTypes[] VALUES = values();

    @Override
    public String toString() {
        return name().toLowerCase(Locale.ROOT);
    }
}
