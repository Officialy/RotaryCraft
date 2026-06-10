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
import net.minecraft.client.renderer.RenderPipelines;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Inventory;
import reika.rotarycraft.base.EngineScreen;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.gui.container.machine.ContainerJet;

/**
 * 26.1 port. Pairs with {@link ContainerJet}. Mirrors {@link GuiMicroTurbine}'s fuel-bar
 * layout against the shared {@code ethanolgui} background. Wired in
 * {@code RotaryCraft.registerScreens} so right-clicking a jet engine actually opens a GUI.
 *
 * <p>Parameterised over {@link BlockEntityEngine} (not {@code BlockEntityJetEngine}) because
 * {@link ContainerJet} is itself {@code IOMachineContainer<BlockEntityEngine>} — the
 * EngineScreen generic bound requires T to be CoreContainer&lt;E&gt;, so E must match.
 */
public class GuiJetEngine extends EngineScreen<BlockEntityEngine, ContainerJet> {
    private final BlockEntityEngine engine;

    public GuiJetEngine(ContainerJet container, Inventory inv, Component title) {
        super(container, inv, title);
        engine = (BlockEntityEngine) inv.player.level().getBlockEntity(container.tile.getBlockPos());
    }

    @Override
    public void extractBackground(GuiGraphicsExtractor poseStack, int pX, int pY, float pPartialTick) {
        super.extractBackground(poseStack, pX, pY, pPartialTick);
        if (engine == null) return;

        int j = (width - imageWidth) / 2;
        int k = (height - imageHeight) / 2;
        int i1 = engine.getFuelScaled(54);
        poseStack.blit(RenderPipelines.GUI_TEXTURED, getTextureIdentifier(),
                j + 85, k + 71 - i1, 200, 55 - i1, 5, i1, 256, 256);
    }

    // Reuse the ethanol GUI background until a jet-specific texture is ported.
    @Override
    protected String getGuiTexture() {
        return "ethanolgui";
    }

    @Override protected int getFuelBarXPos()  { return 84; }
    @Override protected int getFuelBarYPos()  { return 16; }
    @Override protected int getFuelBarXSize() { return 6; }
    @Override protected int getFuelBarYSize() { return 55; }
}
