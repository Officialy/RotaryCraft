/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.gui.screen;

import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.gui.components.Button;
import net.minecraft.client.gui.screens.inventory.AbstractContainerScreen;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.client.resources.language.I18n;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;

import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.rendering.ReikaGuiAPI;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.gui.container.ContainerCraftingPattern;
import reika.rotarycraft.items.tools.ItemCraftPattern;
import reika.rotarycraft.items.tools.ItemCraftPattern.RecipeMode;
import reika.rotarycraft.registry.PacketRegistry;
import reika.rotarycraft.registry.RotaryItems;

/**
 * V33a {@code GuiCraftingPattern}: the Craft Pattern programming screen - recipe-mode toggle (top left, showing
 * the mode's icon) and the input stack-limit up/down buttons.
 */
public class GuiCraftingPattern extends AbstractContainerScreen<ContainerCraftingPattern> {

    private static final Identifier TEXTURE = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/patterngui.png");
    private static final Identifier BUTTONS = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/buttons.png");

    private final Player player;

    public GuiCraftingPattern(ContainerCraftingPattern container, Inventory inv, Component title) {
        super(container, inv, title);
        player = inv.player;
    }

    private ItemStack getItem() {
        return player.getMainHandItem();
    }

    @Override
    protected void init() {
        super.init();
        this.clearWidgets();
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        this.addRenderableWidget(Button.builder(Component.empty(), b -> this.toggleMode()).bounds(j + 6, k + 6, 20, 20).build());
        this.addRenderableWidget(new ImagedGuiButton(1, j + 4, k + 42, 24, 8, 18, 110, BUTTONS, b -> this.changeLimit(1)));
        this.addRenderableWidget(new ImagedGuiButton(2, j + 4, k + 61, 24, 8, 42, 110, BUTTONS, b -> this.changeLimit(-1)));
    }

    private void toggleMode() {
        if (this.getItem().is(RotaryItems.CRAFT_PATTERN.get())) {
            RecipeMode next = ItemCraftPattern.getMode(this.getItem()).next();
            ItemCraftPattern.setMode(this.getItem(), next);
            menu.clearRecipe();
            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CRAFTPATTERNMODE.ordinal(), next.ordinal());
        }
    }

    private void changeLimit(int sign) {
        int amt = minecraft.hasShiftDown() ? 64 : minecraft.hasControlDown() ? 16 : 1;
        amt *= sign;
        if (amt > 1 && ItemCraftPattern.getStackInputLimit(this.getItem()) == 1)
            amt--;
        ItemCraftPattern.changeStackLimit(this.getItem(), amt);
        ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.CRAFTPATTERNLIMIT.ordinal(), amt);
    }

    @Override
    protected void extractLabels(GuiGraphicsExtractor stack, int mouseX, int mouseY) {
        String inv = I18n.get("container.inventory");
        stack.text(font, inv, imageWidth - font.width(inv) - 8, imageHeight - 96 + 2, 0xFF404040, false);
        if (this.getItem().is(RotaryItems.CRAFT_PATTERN.get())) {
            RecipeMode mode = ItemCraftPattern.getMode(this.getItem());
            ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, mode.displayName, imageWidth / 2, 6, 0xFF404040);
            int lim = ItemCraftPattern.getStackInputLimit(this.getItem());
            ReikaGuiAPI.instance.drawCenteredStringNoShadow(stack, font, lim == 64 ? "∞" : String.valueOf(lim), 16, 40 + 12, 0xFF404040);
            stack.text(font, "Input Limit", 6, 72, 0xFF404040, false);
            stack.item(mode.getIcon(), 8, 8);
        }
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor stack, int mouseX, int mouseY, float partial) {
        super.extractBackground(stack, mouseX, mouseY, partial);
        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        stack.blit(RenderPipelines.GUI_TEXTURED, TEXTURE, j, k, 0, 0, imageWidth, imageHeight, 256, 256);
    }
}
