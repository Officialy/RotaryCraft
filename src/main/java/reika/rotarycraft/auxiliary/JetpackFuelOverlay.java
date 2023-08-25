/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import com.mojang.blaze3d.systems.RenderSystem;
import com.mojang.blaze3d.vertex.BufferBuilder;
import com.mojang.blaze3d.vertex.DefaultVertexFormat;
import com.mojang.blaze3d.vertex.Tesselator;
import com.mojang.blaze3d.vertex.VertexFormat;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.client.event.RenderGuiOverlayEvent;
import net.minecraftforge.client.gui.overlay.VanillaGuiOverlay;
import net.minecraftforge.fml.common.Mod;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.items.tools.ItemJetPack;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.material.Fluid;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import org.lwjgl.opengl.GL11;

@Mod.EventBusSubscriber(modid = RotaryCraft.MODID, value = Dist.CLIENT, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class JetpackFuelOverlay {

    public static final JetpackFuelOverlay instance = new JetpackFuelOverlay();

    private JetpackFuelOverlay() {

    }

    @SubscribeEvent
    public void eventHandler(RenderGuiOverlayEvent event) {
        if (event.getOverlay() == VanillaGuiOverlay.HELMET.type()) {
            Player ep = Minecraft.getInstance().player;
            ItemStack is = ep.getItemBySlot(EquipmentSlot.CHEST);
            if (is != null) {
                if (is != null) {
                    if (is.getItem() == RotaryItems.JETPACK.get() || is.getItem() == RotaryItems.BEDROCK_ALLOY_PACK.get() || is.getItem() == RotaryItems.HSLA_STEEL_PACK.get()) {
                        ItemJetPack i = (ItemJetPack) is.getItem();
                        int fuel = i.getCurrentFillLevel(is);
                        float frac = fuel / (float) i.getCapacity(is);
                        Fluid fluid = fuel > 0 ? i.getCurrentFluid(is) : null;
                        Minecraft.getInstance().textureManager.bindForSetup(new ResourceLocation(RotaryCraft.MODID, "textures/gui/overlays.png"));
                        Tesselator tess = Tesselator.getInstance();
                        BufferBuilder v5 = tess.getBuilder();
                        int height = event.getWindow().getGuiScaledHeight();
                        int width = event.getWindow().getGuiScaledWidth();
                        float w = 4 / 128F;
                        float h = 32 / 128F;
                        float f = 1 - frac;
                        float dy = h * f;
                        //ReikaJavaLibrary.pConsole(1-frac);
                        float u = w;
                        if (fluid != null && fluid.equals(RotaryFluids.JET_FUEL))
                            u += w;
                        RenderSystem.enableBlend();
                        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX_COLOR);
                        v5.vertex(4, height / 2 + 32, 0).uv(0, h).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                        v5.vertex(12, height / 2 + 32, 0).uv(w, h).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                        v5.vertex(12, height / 2 - 32, 0).uv(w, 0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();
                        v5.vertex(4, height / 2 - 32, 0).uv(0, 0).color(1.0f, 1.0f, 1.0f, 1.0f).endVertex();

                        v5.vertex(4, height / 2 + 32, 0).uv(u, h).color(1.0f, 1.0f, 1.0f, 0.75f).endVertex();
                        v5.vertex(12, height / 2 + 32, 0).uv(u + w, h).color(1.0f, 1.0f, 1.0f, 0.75f).endVertex();
                        v5.vertex(12, height / 2 - 32 + f * 64, 0).uv(u + w, f * h).color(1.0f, 1.0f, 1.0f, 0.75f).endVertex();
                        v5.vertex(4, height / 2 - 32 + f * 64, 0).uv(u, f * h).color(1.0f, 1.0f, 1.0f, 0.75f).endVertex();
                        tess.end();
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, String.format("%d%s", Math.round(frac * 100), "%"), 1, height / 2 - 40, 0xffffff);
                        event.getGuiGraphics().drawString(Minecraft.getInstance().font, String.format("%dmB", fuel), 1, height / 2 + 33, 0xffffff);
//                        ReikaTextureHelper.bindHUDTexture();
                        //RenderSystem.disableBlend();
                    }
                }
            }
        }
    }

}
