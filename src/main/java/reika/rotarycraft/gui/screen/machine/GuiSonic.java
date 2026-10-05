/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.weaponry.BlockEntitySonicWeapon;
import reika.rotarycraft.gui.container.machine.ContainerSonic;
import reika.rotarycraft.registry.PacketRegistry;
public final class GuiSonic extends GuiPowerOnlyMachine<BlockEntitySonicWeapon, ContainerSonic> {
    private EditBox volume;
    private boolean updating;
    public GuiSonic(ContainerSonic menu, Inventory inventory, Component title) { super(menu, inventory, title, 176, 56); }
    @Override protected void init() {
        super.init();
        volume = new EditBox(font, leftPos + 82, topPos + 31, 38, 16, Component.translatable("gui.rotarycraft.sonic_weapon.volume"));
        volume.setMaxLength(3);
        volume.setValue(Integer.toString(BlockEntitySonicWeapon.decibelsFromVolume(menu.tile.setvolume)));
        volume.setResponder(text -> {
            if (!updating && text.matches("[0-9]{1,3}")) ReikaPacketHelper.sendLongDataPacket(RotaryCraft.packetChannel,
                    PacketRegistry.SONICVOLUME.ordinal(), menu.tile, BlockEntitySonicWeapon.volumeFromDecibels(Integer.parseInt(text)));
        });
        addRenderableWidget(volume);
    }
    @Override protected void containerTick() {
        super.containerTick();
        if (!volume.isFocused()) {
            updating = true;
            volume.setValue(Integer.toString(BlockEntitySonicWeapon.decibelsFromVolume(menu.tile.setvolume)));
            updating = false;
        }
    }
    @Override protected void extractLabels(GuiGraphicsExtractor graphics, int mouseX, int mouseY) {
        super.extractLabels(graphics, mouseX, mouseY);
        graphics.text(font, Component.translatable("gui.rotarycraft.sonic_weapon.volume"), 44, 35, 0xff404040, false);
        graphics.text(font, "dB", 126, 35, 0xff404040, false);
    }
    @Override protected String getGuiTexture() { return "sonicgui3"; }
}
