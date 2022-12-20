package reika.rotarycraft.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.player.Inventory;
import net.minecraftforge.client.gui.ScreenUtils;
import reika.dragonapi.base.CoreContainer;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;

public abstract class GuiPowerOnlyMachine<E extends RotaryCraftBlockEntity, T extends CoreContainer<E>> extends MachineScreen<E, T> {

    private static final ResourceLocation POWER_ONLY = new ResourceLocation(RotaryCraft.MODID + "textures/gui/power_only.png");
    BlockEntityPowerReceiver pwr;

    public GuiPowerOnlyMachine(T inv, Inventory par1Container, Component component) {
        super(inv, par1Container, component);
        pwr = getBlockEntity();
    }

    public BlockEntityPowerReceiver getBlockEntity() {
        return (BlockEntityPowerReceiver) tile;
    }

    @Override
    protected final void drawPowerTab(PoseStack poseStack, int var5, int var6) {
        RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);
        RenderSystem.setShaderTexture(0, POWER_ONLY);
        ScreenUtils.drawTexturedModalRect(poseStack, imageWidth + var5, var6 + 4, 0, 4, 42, 24, 0);
        ScreenUtils.drawTexturedModalRect(poseStack, imageWidth + var5, var6 + 4 + 23, 0, 157, 42, 6, 0);

        long frac = (pwr.power * 29L) / pwr.MINPOWER;
        if (frac > 29)
            frac = 29;
        ScreenUtils.drawTexturedModalRect(poseStack, imageWidth + var5 + 5, imageHeight + var6 - 53 - (imageHeight - 75), 0, 0, (int) frac, 4, 0);

        ReikaGuiAPI.instance.drawCenteredStringNoShadow(poseStack, minecraft.font, "Power:", imageWidth + var5 + 20, var6 + 9, 0xff000000);
        //this.drawCenteredStringNoShadow(fontRenderer, String.format("%d/%d", spawnercontroller.power, spawnercontroller.MINPOWER), imageWidth+var5+16, var6+16, 0xff000000);
    }

}