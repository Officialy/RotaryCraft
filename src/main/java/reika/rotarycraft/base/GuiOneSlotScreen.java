package reika.rotarycraft.base;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.PoseStack;
import net.minecraft.client.gui.GuiGraphics;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.Container;
import net.minecraft.world.entity.player.Inventory;
import net.neoforged.client.gui.ScreenUtils;
import reika.dragonapi.base.CoreContainer;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;

public class GuiOneSlotScreen<E extends BlockEntityPowerReceiver, T extends CoreContainer<E>> extends MachineScreen<E, T> {

    public static final ResourceLocation ONE_SLOT = new ResourceLocation(RotaryCraft.MODID, "textures/gui/basic_gui_oneslot.png");
    public static final ResourceLocation POWERTAB = new ResourceLocation(RotaryCraft.MODID, "textures/gui/powertab.png");
    protected Container iinv;

    public GuiOneSlotScreen(Inventory pl, T par1Container, Component title) {
        super(par1Container, pl, title);
        iinv = (Container) tile;
        imageWidth = 176;
        imageHeight = 166;
        inventory = pl;
    }

    @Override
    protected final void drawPowerTab(GuiGraphics stack, int j, int k) {
        if (recv != null) {
            RenderSystem.setShaderColor(1.0F, 1.0F, 1.0F, 1.0F);

            stack.blit(POWERTAB,imageWidth + j, k + 4, 0, 4, 42, imageHeight - 4);

            long frac = (recv.power * 29L) / recv.MINPOWER;
            if (frac > 29)
                frac = 29;
            stack.blit(POWERTAB, imageWidth + j + 5, imageHeight + k - 144, 0, 0, (int) frac, 4);

            frac = (int) (recv.omega * 29L) / recv.MINSPEED;
            if (frac > 29)
                frac = 29;
            stack.blit(POWERTAB, imageWidth + j + 5, imageHeight + k - 84, 0, 0, (int) frac, 4);

            frac = (int) (recv.torque * 29L) / recv.MINTORQUE;
            if (frac > 29)
                frac = 29;
            stack.blit(POWERTAB, imageWidth + j + 5, imageHeight + k - 24, 0, 0, (int) frac, 4);

            api.drawCenteredStringNoShadow(stack, minecraft.font, "Power:", imageWidth + j + 20, k + 9, 0xff000000);
            api.drawCenteredStringNoShadow(stack, minecraft.font, "Speed:", imageWidth + j + 20, k + 69, 0xff000000);
            api.drawCenteredStringNoShadow(stack, minecraft.font, "Torque:", imageWidth + j + 20, k + 129, 0xff000000);
//            this.drawCenteredStringNoShadow(font, String.format("%d/%d", recv.power, recv.MINPOWER), imageWidth+j+16, k+16, 0xff000000);

        }
    }

    @Override
    protected String getGuiTexture() {
        return "basic_gui_oneslot";
    }

}
