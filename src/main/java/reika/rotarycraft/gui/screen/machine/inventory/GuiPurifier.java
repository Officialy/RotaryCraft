package reika.rotarycraft.gui.screen.machine.inventory;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.processing.BlockEntityPurifier;
import reika.rotarycraft.gui.container.machine.inventory.ContainerPurifier;

public class GuiPurifier extends MachineScreen<BlockEntityPurifier, ContainerPurifier> {
    public GuiPurifier(ContainerPurifier menu, Inventory inventory, Component title) { super(menu, inventory, title, 176, 166); }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int progress = menu.tile.getCookScaled(17);
        if (progress > 0) graphics.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                leftPos + 11, topPos + 34, 4, 167, 82, progress, 256, 256);
        graphics.text(font, Component.translatable("gui.rotarycraft.purifier_temperature", menu.tile.temperature),
                leftPos + 88, topPos + 16, 0xFF404040, false);
    }
    @Override protected void drawPowerTab(GuiGraphicsExtractor graphics, int x, int y) {
        Identifier texture = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x, y + 4, 0, 4, 42, imageHeight - 4, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x + 5, imageHeight + y - 144,
                0, 0, (int) menu.tile.getScaledPower(29), 4, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x + 5, imageHeight + y - 84,
                0, 0, (int) menu.tile.getScaledOmega(29), 4, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x + 5, imageHeight + y - 24,
                0, 0, (int) menu.tile.getScaledTorque(29), 4, 256, 256);
        api.drawCenteredStringNoShadow(graphics, font, "Power:", imageWidth + x + 20, y + 9, 0xFF000000);
        api.drawCenteredStringNoShadow(graphics, font, "Speed:", imageWidth + x + 20, y + 69, 0xFF000000);
        api.drawCenteredStringNoShadow(graphics, font, "Torque:", imageWidth + x + 20, y + 129, 0xFF000000);
    }
    @Override protected String getGuiTexture() { return "purifiergui"; }
}
