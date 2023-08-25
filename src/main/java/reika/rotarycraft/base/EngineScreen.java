package reika.rotarycraft.base;

import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.ChatFormatting;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.base.CoreContainer;
import reika.dragonapi.libraries.mathsci.ReikaDateHelper;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;

public abstract class EngineScreen<E extends BlockEntityEngine, T extends CoreContainer<E>> extends NonPoweredMachineScreen<E, T> {

    protected final BlockEntityEngine engine;

    protected EngineScreen(T container, Inventory inv, Component c) {
        super(container, inv, c);
        inventory = inv;
        engine = (BlockEntityEngine) inv.player.level().getBlockEntity(container.tile.getBlockPos());
    }

    @Override
    public void mouseMoved(double pMouseX, double pMouseY) {
        super.mouseMoved(pMouseX, pMouseY);
    }

    @Override
    protected void renderLabels(GuiGraphics stack, int pMouseX, int pMouseY) {
        if (engine.getEngineType().burnsFuel()) {
            int j = (width - imageWidth) / 2;
            int k = (height - imageHeight) / 2;
            super.renderTooltip(stack, pMouseX, pMouseY);
            int dx = this.getFuelBarXPos();
            int dy = this.getFuelBarYPos();
            if (api.isMouseInBox(j + dx, j + dx + this.getFuelBarXSize(), k + dy, k + dy + this.getFuelBarYSize(), 0, 0)) {
                int time = engine.getFuelDuration();
                String color = "";
                if (time == 0)
                    color = ChatFormatting.GRAY.toString();
                else if (time < 30)
                    color = ChatFormatting.RED.toString();
                else if (time < 45)
                    color = ChatFormatting.GOLD.toString();
                else if (time < 60)
                    color = ChatFormatting.YELLOW.toString();
                String sg = String.format("%sFuel: %s", color, ReikaDateHelper.getSecondsAsClock(time));
                api.drawTooltipAt(stack, minecraft.font, sg, pMouseX - j, pMouseY - k);
            }
        }
    }

    protected abstract int getFuelBarXPos();

    protected abstract int getFuelBarYPos();

    protected abstract int getFuelBarXSize();

    protected abstract int getFuelBarYSize();

}
