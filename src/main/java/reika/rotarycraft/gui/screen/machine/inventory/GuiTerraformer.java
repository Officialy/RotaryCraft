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

import java.util.List;
import net.minecraft.client.gui.GuiGraphicsExtractor;
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.core.registries.Registries;
import net.minecraft.network.chat.Component;
import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.item.ItemStack;
import reika.dragonapi.instantiable.gui.ImagedGuiButton;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.auxiliary.recipemanagers.TerraformingRecipe;
import reika.rotarycraft.base.MachineScreen;
import reika.rotarycraft.blockentities.level.BlockEntityTerraformer;
import reika.rotarycraft.gui.container.machine.inventory.ContainerTerraformer;

public class GuiTerraformer extends MachineScreen<BlockEntityTerraformer, ContainerTerraformer> {
    private List<TerraformingRecipe> targets = List.of();
    private int offset;
    public GuiTerraformer(ContainerTerraformer menu, Inventory inventory, Component title) { super(menu, inventory, title, 240, 222); }
    @Override protected void init() {
        super.init();
        targets = menu.tile.getValidTargetBiomes(menu.tile.getCentralBiome());
        offset = Math.clamp(offset, 0, Math.max(0, targets.size() - 5));
        Identifier icons = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/biomes.png");
        for (int row = 0; row < Math.min(5, targets.size() - offset); row++) {
            var recipe = targets.get(offset + row);
            String name = Component.translatable("biome." + recipe.target().identifier().getNamespace() + "." + recipe.target().identifier().getPath()).getString();
            String tip = name + " - " + recipe.minPower() + " W; " + recipe.water() + " mB per column (16 columns/cell)";
            addRenderableWidget(new ImagedGuiButton(row, leftPos + 8, topPos + 17 + 39 * row, 32, 32,
                    32 * (recipe.icon() % 8), 32 * (recipe.icon() / 8), icons, tip, 0xffffff, false, button -> {
                var biome = menu.tile.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getOrThrow(recipe.target());
                minecraft.gameMode.handleInventoryButtonClick(menu.containerId, menu.tile.getLevel().registryAccess().lookupOrThrow(Registries.BIOME).getId(biome.value()));
            }));
        }
        Identifier buttons = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/buttons.png");
        var up = addRenderableWidget(new ImagedGuiButton(100, leftPos + 11, topPos + 6, 24, 12, 18, 110, buttons, b -> { offset--; init(); }));
        var down = addRenderableWidget(new ImagedGuiButton(101, leftPos + 11, topPos + imageHeight - 14, 24, 12, 42, 110, buttons, b -> { offset++; init(); }));
        up.active = offset > 0; down.active = offset + 5 < targets.size();
    }
    @Override protected void containerTick() {
        super.containerTick();
        if (!targets.equals(menu.tile.getValidTargetBiomes(menu.tile.getCentralBiome()))) init();
    }
    @Override public void extractBackground(GuiGraphicsExtractor graphics, int mouseX, int mouseY, float partial) {
        super.extractBackground(graphics, mouseX, mouseY, partial);
        for (int row = 0; row < Math.min(5, targets.size() - offset); row++) {
            var recipe = targets.get(offset + row);
            int x = leftPos + 48, y = topPos + 17 + row * 39;
            graphics.text(font, recipe.water() == 0 ? "-" : Integer.toString(recipe.water() * 16), x, y + 4, 0xff404040, false);
            if (!recipe.items().isEmpty()) {
                int index = (int)((System.currentTimeMillis() / 500) % recipe.items().size());
                var item = recipe.items().get(index).ingredient().items().findFirst();
                item.ifPresent(holder -> graphics.item(new ItemStack(holder.value()), x, y + 18));
            }
            if (recipe.target().equals(menu.tile.getTarget())) graphics.fill(leftPos + 5, y, leftPos + 7, y + 32, 0xff33cc33);
        }
        graphics.text(font, Component.translatable("gui.rotarycraft.terraformer_status", menu.tile.queuedCells, menu.water), leftPos + 72, topPos + 128, 0xff404040, false);
    }
    @Override protected void drawPowerTab(GuiGraphicsExtractor graphics, int x, int y) {
        Identifier texture = Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "textures/screen/powertab.png");
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x, y + 4, 0, 4, 42, imageHeight - 4, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x + 5, y + 28, 0, 0, (int)menu.tile.getScaledPower(29), 4, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x + 5, y + 88, 0, 0, (int)menu.tile.getScaledOmega(29), 4, 256, 256);
        graphics.blit(RenderPipelines.GUI_TEXTURED, texture, imageWidth + x + 5, y + 148, 0, 0, (int)menu.tile.getScaledTorque(29), 4, 256, 256);
        api.drawCenteredStringNoShadow(graphics, font, "Power:", imageWidth + x + 20, y + 9, 0xff000000);
        api.drawCenteredStringNoShadow(graphics, font, "Speed:", imageWidth + x + 20, y + 69, 0xff000000);
        api.drawCenteredStringNoShadow(graphics, font, "Torque:", imageWidth + x + 20, y + 129, 0xff000000);
    }
    @Override protected String getGuiTexture() { return "terraformergui"; }
}
