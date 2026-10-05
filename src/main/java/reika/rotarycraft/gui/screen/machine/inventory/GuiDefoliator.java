package reika.rotarycraft.gui.screen.machine.inventory;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.level.BlockEntityDefoliator;
import reika.rotarycraft.gui.container.machine.inventory.ContainerDefoliator;
public final class GuiDefoliator extends GuiPowerOnlyMachine<BlockEntityDefoliator, ContainerDefoliator> {
    public GuiDefoliator(ContainerDefoliator menu, Inventory inventory, Component title) { super(menu, inventory, title, 176, 166); }
    @Override protected String getGuiTexture() { return "defoliatorgui"; }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial); int size = menu.getFluidLevel() * 52 / BlockEntityDefoliator.CAPACITY;
        graphics.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(), leftPos + 134, topPos + 69 - size, 177, 69 - size, 16, size, 256, 256);
    }
    @Override protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        if (mouseX >= leftPos + 133 && mouseX <= leftPos + 150 && mouseY >= topPos + 16 && mouseY <= topPos + 69)
            graphics.setTooltipForNextFrame(font, Component.translatable("gui.rotarycraft.defoliator.poison", menu.getFluidLevel(), BlockEntityDefoliator.CAPACITY), mouseX, mouseY);
    }
}
