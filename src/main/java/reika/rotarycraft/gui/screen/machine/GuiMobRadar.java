/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.util.Mth;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;
import reika.rotarycraft.gui.container.machine.ContainerMobRadar;
public final class GuiMobRadar extends GuiPowerOnlyMachine<BlockEntityMobRadar, ContainerMobRadar> {
    public static final int UNIT = 4;
    private static final Identifier ICONS = Identifier.fromNamespaceAndPath("rotarycraft", "textures/screen/mobicons.png");
    public GuiMobRadar(ContainerMobRadar menu, Inventory inventory, Component title) { super(menu, inventory, title, 214, 223); }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partialTick) {
        super.extractBackground(graphics, mouseX, mouseY, partialTick);
        var radar = (BlockEntityMobRadar)tile;
        int x = (width - imageWidth) / 2 + 7, y = (height - imageHeight) / 2 + 17;
        int range = radar.getRange();
        graphics.enableScissor(x, y, x + 200, y + 200);
        for (var contact : radar.getContacts()) {
            int dx = range == 0 ? 100 : 100 + Mth.floor(100 * (contact.x() - tile.getBlockPos().getX() - .5) / range);
            int dz = range == 0 ? 100 : 100 + Mth.floor(100 * (contact.z() - tile.getBlockPos().getZ() - .5) / range);
            int icon = Math.clamp(contact.icon(), 0, 255);
            graphics.blit(RenderPipelines.GUI_TEXTURED, ICONS, x + dx - UNIT / 2, y + dz - UNIT / 2, 8 * (icon % 16), 8 * (icon / 16), 8, 8, 256, 256);
        }
        graphics.disableScissor();
        graphics.text(font, Component.translatable("gui.rotarycraft.mob_radar.range", range), x + 102, y, 0xffaaffaa, false);
        graphics.text(font, Component.translatable("gui.rotarycraft.mob_radar.range", (int)(.63 * range)), x + 102, y + 37, 0xffaaffaa, false);
        graphics.text(font, Component.translatable("gui.rotarycraft.mob_radar.range", Mth.ceil(.31 * range)), x + 102, y + 69, 0xffaaffaa, false);
        if (radar.isJammed()) api.renderStatic(graphics, x, y, x + 200, y + 200);
    }
    @Override protected String getGuiTexture() { return "mobradargui"; }
}
