package reika.rotarycraft.gui.screen.machine.inventory;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityFuelConverter;
import reika.rotarycraft.gui.container.machine.inventory.ContainerFuelEnhancer;

public class GuiFuelEnhancer extends MachineScreen<BlockEntityFuelConverter, ContainerFuelEnhancer> {
    public GuiFuelEnhancer(ContainerFuelEnhancer menu, Inventory inventory, Component title) { super(menu, inventory, title, 176, 132); }
    @Override protected String getGuiTexture() { return "basicstorage"; }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        graphics.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), leftPos, topPos, 0, 0, 176, 35, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), leftPos, topPos + 35, 0, 126, 176, 96, 256, 256);
        drawPowerTab(graphics, leftPos, topPos);
    }
    @Override protected void drawPowerTab(GuiGraphicsExtractor graphics, int x, int y) {
        var texture = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x + imageWidth, y + 4, 211, 4, 42, 145, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x + imageWidth, y + 149, 211, 157, 42, 6, 256, 256);
        for (int n = 0; n < 3; n++) {
            long value = n == 0 ? menu.tile.power : n == 1 ? menu.tile.omega : menu.tile.torque;
            long minimum = n == 0 ? menu.tile.MINPOWER : n == 1 ? menu.tile.MINSPEED : menu.tile.MINTORQUE;
            int fraction = value >= minimum ? 29 : (int)(Math.max(0, value) * 29 / minimum);
            graphics.blit(RenderPipelines.GUI_TEXTURED, texture, x + imageWidth + 5, y + 20 + n * 45, 0, 0, fraction, 4, 256, 256);
            api.drawCenteredStringNoShadow(graphics, font, n == 0 ? "Power:" : n == 1 ? "Speed:" : "Torque:", x + imageWidth + 20, y + 9 + n * 45, 0xFF000000);
        }
    }
}
