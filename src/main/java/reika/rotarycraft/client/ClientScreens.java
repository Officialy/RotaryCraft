package reika.rotarycraft.client;

import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.Level;

import reika.rotarycraft.gui.screen.GuiCalculator;
import reika.rotarycraft.gui.screen.GuiHandbook;

/**
 * Opens RotaryCraft's client screens.
 *
 * <p>Kept out of the item classes because those are loaded during item registration on a dedicated
 * server, and naming a {@code Screen} type there makes the class unloadable server-side — which
 * failed RotaryCraft's mod construction and skipped every mod that depends on it.
 */
public final class ClientScreens {

    private ClientScreens() {}

    public static void openCalculator(Player player, Level level) {
        Minecraft.getInstance().gui.setScreen(new GuiCalculator(player, level));
    }

    public static void openHandbook(Player player, Level level) {
        Minecraft.getInstance().gui.setScreen(new GuiHandbook(player, level, 0, 0));
    }
}
