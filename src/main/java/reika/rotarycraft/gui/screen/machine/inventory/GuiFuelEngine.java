package reika.rotarycraft.gui.screen.machine.inventory;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.NonPoweredMachineScreen;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFuelEngine;
import reika.rotarycraft.modinterface.TileEntityFuelEngine;

public final class GuiFuelEngine extends NonPoweredMachineScreen<TileEntityFuelEngine, ContainerFuelEngine> {
    public GuiFuelEngine(ContainerFuelEngine menu, Inventory inv, Component title) { super(menu, inv, title, 176, 166); }
    @Override protected String getGuiTexture() { return "fuelenggui"; }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int x, int y, float partial) {
        super.extractBackground(graphics, x, y, partial);
        for (int i = 0; i < 4; i++) {
            int height = Math.clamp((long)menu.getGauge(i) * 54 / (i == 3 ? TileEntityFuelEngine.MAXTEMP : TileEntityFuelEngine.CAPACITY), 0, 54);
            int gx = i == 0 ? 85 : i == 1 ? 31 : i == 2 ? 58 : 138;
            int u = i == 0 ? 207 : i == 1 ? 214 : i == 2 ? 221 : 177;
            graphics.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), leftPos + gx, topPos + 71 - height,
                    u, (i == 3 ? 99 : 55) - height, i == 3 ? 9 : 5, height, 256, 256);
        }
    }
    @Override protected void extractTooltip(GuiGraphicsExtractor graphics, int x, int y) {
        super.extractTooltip(graphics, x, y);
        for (int i = 0; i < 4; i++) {
            int gx = i == 0 ? 84 : i == 1 ? 30 : i == 2 ? 57 : 137;
            if (x < leftPos + gx || x > leftPos + gx + (i == 3 ? 11 : 7) || y < topPos + 16 || y > topPos + 71) continue;
            String name = i == 0 ? "fuel" : i == 1 ? "water" : i == 2 ? "lubricant" : "temperature";
            Component tooltip = Component.translatable("gui.rotarycraft.fuel_engine." + name, menu.getGauge(i));
            if (i == 0) {
                int seconds = menu.getGauge(0) * menu.getGauge(4) / 4 / 20;
                graphics.setTooltipForNextFrame(font, java.util.List.of(tooltip.getVisualOrderText(), Component.translatable("gui.rotarycraft.fuel_engine.duration", seconds / 60, seconds % 60).getVisualOrderText()), x, y);
            } else graphics.setTooltipForNextFrame(font, tooltip, x, y);
        }
    }
}
