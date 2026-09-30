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

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.EditBox;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;

import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.rendering.ReikaColorAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.base.GuiPowerOnlyMachine;
import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter;
import reika.rotarycraft.blockentities.processing.BlockEntityAutoCrafter.CraftingMode;
import reika.rotarycraft.gui.container.machine.inventory.ContainerAutoCrafter;
import reika.rotarycraft.registry.PacketRegistry;

/**
 * V33a {@code GuiAutoCrafter}: pattern and output slots; in Request mode a strip button above each pattern runs one
 * cycle, in Sustain mode a button below each pattern selects it and the text box sets how many of its output the ME
 * network should be kept stocked with. Freshly-crafted slots flash; the mode swatch sits in the top left.
 */
public class GuiAutoCrafter extends GuiPowerOnlyMachine<BlockEntityAutoCrafter, ContainerAutoCrafter> {

    private final BlockEntityAutoCrafter crafter;

    private int selectedSlot = -1;
    private EditBox text;

    public GuiAutoCrafter(ContainerAutoCrafter container, Inventory inv, Component title) {
        super(container, inv, title, 176, 222);
        crafter = (BlockEntityAutoCrafter) tile;
    }

    @Override
    protected void init() {
        super.init();
        int var5 = (width - imageWidth) / 2;
        int var6 = (height - imageHeight) / 2;
        if (crafter.getMode() == CraftingMode.REQUEST) {
            for (int i = 0; i < BlockEntityAutoCrafter.SIZE; i++) {
                int dx = var5 + (i % 9) * BlockEntityAutoCrafter.SIZE + 7;
                int dy = i < 9 ? var6 + 13 : var6 + 75;
                int slot = i;
                this.addRenderableWidget(new ImagedGuiButton(i, dx, dy, 18, 4, 176, 6, this.getTextureIdentifier(),
                        b -> ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CRAFTERCRAFT.ordinal(), crafter, slot)));
            }
        }

        if (crafter.getMode() == CraftingMode.SUSTAIN) {
            text = new EditBox(font, var5 + 8, var6 + 119, 52, 15, Component.literal("threshold"));
            text.setFocused(false);
            text.setMaxLength(7);
            text.setResponder(this::onTyped);
            this.addRenderableWidget(text);
            for (int i = 0; i < BlockEntityAutoCrafter.SIZE; i++) {
                int dx = var5 + 8 + 18 * (i % 9);
                int dy = var6 + 37 + 62 * (i / 9);
                int slot = i;
                this.addRenderableWidget(new ImagedGuiButton(40 + i, dx, dy, 16, 16, 195, 41, this.getTextureIdentifier(), b -> {
                    selectedSlot = slot;
                    text.setFocused(false);
                    text.setValue("");
                }));
            }
        }
        else {
            text = null;
        }
    }

    private void onTyped(String s) {
        if (selectedSlot >= 0 && text != null && text.isFocused()) {
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CRAFTERTHRESH.ordinal(), crafter, selectedSlot, Math.max(0, ReikaJavaLibrary.safeIntParse(s)));
        }
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int mouseX, int mouseY) {
        super.extractLabels(stack, mouseX, mouseY);

        int x = 7;
        int y = 36;
        for (int i = 0; i < BlockEntityAutoCrafter.SIZE; i++) {
            int dx = x + (i % 9) * BlockEntityAutoCrafter.SIZE;
            int dy = i >= 9 ? y + 62 : y;
            if (crafter.crafting[i] > 0) {
                float alpha = Math.min(1, crafter.crafting[i] / 2F);
                stack.blit(RenderPipelines.GUI_TEXTURED, this.getTextureIdentifier(), dx, dy, 176, 11, 18, 9, 256, 256, ((int)(alpha * 255) << 24) | 0xffffff);
            }

            if (i == selectedSlot) {
                stack.fill(dx + 1, dy - 17, dx + 17, dy - 1, 0x663388ff);
            }
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        int var5 = (width - imageWidth) / 2;
        int var6 = (height - imageHeight) / 2;
        if (text != null && selectedSlot >= 0) {
            ItemStack out = crafter.getSlotRecipeOutput(selectedSlot);
            if (out != null) {
                if (!text.isFocused()) {
                    graphics.text(font, String.valueOf(crafter.getThreshold(selectedSlot)), var5 + 12, var6 + 122, 0xffffffff, false);
                }
                graphics.text(font, "of", var5 + 65, var6 + 122, 0xff404040, false);
                graphics.item(out.copyWithCount(1), var5 + 80, var6 + 118);
            }
        }

        int c = crafter.getMode().color;
        graphics.fill(var5 + 5, var6 + 5, var5 + 16, var6 + 16, 0xff000000 | c);
        graphics.fill(var5 + 5, var6 + 5, var5 + 6, var6 + 15, 0xff000000 | ReikaColorAPI.mixColors(c, 0xffffff, 0.5F));
        graphics.fill(var5 + 5, var6 + 5, var5 + 15, var6 + 6, 0xff000000 | ReikaColorAPI.mixColors(c, 0xffffff, 0.5F));
        graphics.fill(var5 + 6, var6 + 15, var5 + 16, var6 + 16, 0xff000000 | ReikaColorAPI.mixColors(c, 0x000000, 0.5F));
        graphics.fill(var5 + 15, var6 + 6, var5 + 16, var6 + 16, 0xff000000 | ReikaColorAPI.mixColors(c, 0x000000, 0.5F));
        if (mouseX >= var5 + 5 && mouseX <= var5 + 16 && mouseY >= var6 + 5 && mouseY <= var6 + 16) {
            graphics.setTooltipForNextFrame(font, Component.literal(crafter.getMode().label), mouseX, mouseY);
        }
    }

    @Override
    protected String getGuiTexture() {
        return "craftergui" + crafter.getMode().imageSuffix;
    }
}
