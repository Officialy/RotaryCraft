package reika.rotarycraft.base;

import net.minecraft.client.renderer.RenderPipelines;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.base.CoreContainer;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;

public abstract class GuiPowerOnlyMachine<E extends RotaryCraftBlockEntity, T extends CoreContainer<E>> extends MachineScreen<E, T> {

    private static final Identifier POWER_ONLY = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/gui/power_only.png");
    BlockEntityPowerReceiver pwr;

    public GuiPowerOnlyMachine(T inv, Inventory par1Container, Component component) {
        super(inv, par1Container, component);
        pwr = getBlockEntity();
    }

    public GuiPowerOnlyMachine(T inv, Inventory par1Container, Component component, int imageWidth, int imageHeight) {
        super(inv, par1Container, component, imageWidth, imageHeight);
        pwr = getBlockEntity();
    }

    public BlockEntityPowerReceiver getBlockEntity() {
        return (BlockEntityPowerReceiver) tile;
    }

    @Override
    protected final void drawPowerTab(GuiGraphicsExtractor stack, int var5, int var6) {
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_ONLY, imageWidth + var5, var6 + 4, 0, 4, 42, 24, 256, 256);
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_ONLY, imageWidth + var5, var6 + 4 + 23, 0, 157, 42, 6, 256, 256);

        long frac = (pwr.power * 29L) / pwr.MINPOWER;
        if (frac > 29)
            frac = 29;
        stack.blit(RenderPipelines.GUI_TEXTURED, POWER_ONLY, imageWidth + var5 + 5, imageHeight + var6 - 53 - (imageHeight - 75), 0, 0, (int) frac, 4, 256, 256);

        ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, minecraft.font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
        //this.drawCenteredStringNoShadow(fontRenderer, String.format("%d/%d", spawnercontroller.power, spawnercontroller.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
    }

}