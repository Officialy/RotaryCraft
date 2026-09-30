/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen.machine.inventory;

import java.util.ArrayList;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;

import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.instantiable.io.PacketTarget;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.BlockEntityItemFilter;
import reika.rotarycraft.blockentities.BlockEntityItemFilter.MatchData;
import reika.rotarycraft.blockentities.BlockEntityItemFilter.MatchDisplay;
import reika.rotarycraft.blockentities.BlockEntityItemFilter.MatchType;
import reika.rotarycraft.blockentities.BlockEntityItemFilter.SettingType;
import reika.rotarycraft.gui.container.machine.inventory.ContainerItemFilter;
import reika.rotarycraft.registry.PacketRegistry;

/**
 * V33a {@code GuiItemFilter}: four pages (basic / ore names / NBT / class hierarchy) of match settings, each cycled
 * Match - Mismatch - Ignore; the NBT page has a toggle-all row and scrolls.
 */
public class GuiItemFilter extends GuiPowerOnlyMachine<BlockEntityItemFilter, ContainerItemFilter> {

    private static final Identifier BUTTONS = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/buttons.png");

    private final BlockEntityItemFilter filter;

    private static final int LINES = 5;

    private SettingType page = SettingType.BASIC;
    private ArrayList<MatchDisplay> display;
    private int nbtListPos = 0;

    private MatchData lastData;

    public GuiItemFilter(ContainerItemFilter container, Inventory inv, Component title) {
        super(container, inv, title, 256, 217);
        filter = (BlockEntityItemFilter) tile;
    }

    @Override
    protected void init() {
        super.init();

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;

        MatchData data = filter.getData();
        display = null;
        if (data != null) {
            display = switch (page) {
                case BASIC -> data.getMainDisplay();
                case NBT -> data.getNBTDisplay();
                case ORE -> data.getOreDisplay();
                case CLASS -> data.getClassDisplay();
            };
            int d = page == SettingType.NBT ? 1 : 0;
            int max = Math.min(nbtListPos + display.size(), nbtListPos + LINES - d);
            for (int i = nbtListPos; i < max && i < display.size(); i++) {
                int i2 = i - nbtListPos + d;
                MatchDisplay m = display.get(i);
                int u = m.getSetting() == MatchType.MATCH ? 0 : 9;
                int v = m.getSetting() == MatchType.MISMATCH ? 54 : 63;
                int idx = i;
                this.addRenderableWidget(new ImagedGuiButton(i, j + 30, k + 18 + i2 * 16, 9, 9, u, v, BUTTONS, b -> {
                    display.get(idx).increment();
                    this.sendData();
                    this.rebuild();
                }));
            }
            this.addRenderableWidget(Button.builder(Component.literal("<"), b -> {
                page = page.previous();
                nbtListPos = 0;
                this.rebuild();
            }).bounds(j + 30, k + 100, 20, 20).build());
            this.addRenderableWidget(Button.builder(Component.literal(">"), b -> {
                page = page.next();
                nbtListPos = 0;
                this.rebuild();
            }).bounds(j + 50, k + 100, 20, 20).build());
            if (page == SettingType.NBT) {
                if (!display.isEmpty()) {
                    for (int i = 0; i < 3; i++) {
                        int u = i == 0 ? 0 : 9;
                        int v = i == 1 ? 54 : 63;
                        int target = i;
                        this.addRenderableWidget(new ImagedGuiButton(-5 - i, j + 30 + i * 10, k + 18, 9, 9, u, v, BUTTONS, b -> {
                            for (MatchDisplay m : display) {
                                while (m.getSetting().ordinal() != target)
                                    m.increment();
                            }
                            this.sendData();
                            this.rebuild();
                        }));
                    }
                }
                if (display.size() > LINES) {
                    this.addRenderableWidget(Button.builder(Component.literal("+"), b -> {
                        if (nbtListPos < display.size() - LINES + 1)
                            nbtListPos++;
                        this.rebuild();
                    }).bounds(j + 70, k + 100, 20, 20).build());
                    this.addRenderableWidget(Button.builder(Component.literal("-"), b -> {
                        if (nbtListPos > 0)
                            nbtListPos--;
                        this.rebuild();
                    }).bounds(j + 90, k + 100, 20, 20).build());
                }
            }
        }
    }

    private boolean needsRebuild;

    private void rebuild() {
        this.rebuildWidgets();
    }

    @Override
    protected void containerTick() {
        super.containerTick();
        if (needsRebuild) {
            needsRebuild = false;
            this.rebuild();
        }
    }

    private void sendData() {
        MatchData data = filter.getData();
        if (data == null)
            return;
        CompoundTag nbt = data.writeToNBT();
        nbt.putInt("posX", tile.getBlockPos().getX());
        nbt.putInt("posY", tile.getBlockPos().getY());
        nbt.putInt("posZ", tile.getBlockPos().getZ());
        ReikaPacketHelper.sendNBTPacket(RotaryCraft.packetChannel, PacketRegistry.FILTERSETTING.ordinal(), nbt, PacketTarget.server);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int mouseX, int mouseY) {
        super.extractLabels(stack, mouseX, mouseY);

        int dx = this.inventoryLabelLeft() ? 176 : imageWidth - 50;
        stack.text(font, "Blacklist", dx, (imageHeight - 96) + 3, 0xff404040, false);

        MatchData data = filter.getData();
        if (data != lastData) {
            lastData = data;
            needsRebuild = true; //widgets cannot be rebuilt mid-render
        }

        if (data != null && display != null) {
            if (display.isEmpty()) {
                stack.text(font, "[No Values]", 42, 19, 0xff000000, false);
            }

            int d = page == SettingType.NBT ? 1 : 0;
            int max = Math.min(nbtListPos + display.size(), nbtListPos + LINES - d);
            for (int i = nbtListPos; i < max && i < display.size(); i++) {
                int i2 = i - nbtListPos + d;
                MatchDisplay m = display.get(i);
                int tx = 42;
                int ty = 19 + i2 * 16;
                String s = m.displayName + " (" + m.value + "): ";
                stack.text(font, s, tx, ty, 0xff000000, false);
                stack.text(font, m.getSetting().name, tx + font.width(s), ty, 0xff000000 | m.getSetting().color, false);
            }

            if (page == SettingType.NBT && !display.isEmpty()) {
                stack.text(font, "Toggle All", 42 + 22, 19, 0xff000000, false);
            }
        }
    }

    @Override
    protected boolean inventoryLabelLeft() {
        return true;
    }

    @Override
    protected String getGuiTexture() {
        return "filtergui2";
    }
}
