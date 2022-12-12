///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.auxiliary;
//
//import com.mojang.blaze3d.vertex.Tesselator;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.items.tools.ItemJetPack;
//import reika.rotarycraft.registry.RotaryItems;
//import net.minecraft.client.Minecraft;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraftforge.client.event.RenderGameOverlayEvent;
//import net.minecraftforge.client.event.RenderGameOverlayEvent.ElementType;
//import net.minecraftforge.eventbus.api.SubscribeEvent;
//import org.lwjgl.opengl.GL11;
//
//
//public class JetpackFuelOverlay {
//
//    public static final JetpackFuelOverlay instance = new JetpackFuelOverlay();
//
//    private JetpackFuelOverlay() {
//
//    }
//
//    @SubscribeEvent
//    public void eventHandler(RenderGameOverlayEvent event) {
//        if (event.type == ElementType.HELMET) {
//            Player ep = Minecraft.getInstance().player;
//            ItemStack is = ep.getCurrentArmor(2);
//            if (is != null) {
//                RotaryItems ir = RotaryItems.getEntry(is);
//                if (ir != null) {
//                    if (ir.isJetpack()) {
//                        ItemJetPack i = (ItemJetPack) is.getItem();
//                        int fuel = i.getCurrentFillLevel(is);
//                        float frac = fuel / (float) i.getCapacity(is);
//                        Fluid fluid = fuel > 0 ? i.getCurrentFluid(is) : null;
//                        ReikaTextureHelper.bindTexture(RotaryCraft.class, "Textures/GUI/overlays.png");
//                        Tesselator tess = Tesselator.getInstance();
//			BufferBuilder v5 = tess.getBuilder();
//                        int height = event.resolution.getScaledHeight();
//                        int width = event.resolution.getScaledWidth();
//                        float w = 4 / 128F;
//                        float h = 32 / 128F;
//                        float f = 1 - frac;
//                        float dy = h * f;
//                        //ReikaJavaLibrary.pConsole(1-frac);
//                        float u = w;
//                        if (fluid != null && fluid.equals(RotaryFluids.JET_FUEL))
//                            u += w;
//                        RenderSystem.enableBlend();
//                        v5.begin(VertexFormat.Mode.QUADS, DefaultVertexFormat.POSITION_TEX);
//                        v5.setColorOpaque_I(0xffffff);
//                        v5.addVertexWithUV(4, height / 2 + 32, 0, 0, h);
//                        v5.addVertexWithUV(12, height / 2 + 32, 0, w, h);
//                        v5.addVertexWithUV(12, height / 2 - 32, 0, w, 0);
//                        v5.addVertexWithUV(4, height / 2 - 32, 0, 0, 0);
//
//                        v5.setColorRGBA_I(0xffffff, 192);
//                        v5.addVertexWithUV(4, height / 2 + 32, 0, u, h);
//                        v5.addVertexWithUV(12, height / 2 + 32, 0, u + w, h);
//                        v5.addVertexWithUV(12, height / 2 - 32 + f * 64, 0, u + w, f * h);
//                        v5.addVertexWithUV(4, height / 2 - 32 + f * 64, 0, u, f * h);
//                        v5.end();
//                        Minecraft.getInstance().fontRenderer.draw(String.format("%d%s", Math.round(frac * 100), "%"), 1, height / 2 - 40, 0xffffff);
//                        Minecraft.getInstance().fontRenderer.draw(String.format("%dmB", fuel), 1, height / 2 + 33, 0xffffff);
//                        ReikaTextureHelper.bindHUDTexture();
//                        //RenderSystem.disableBlend();
//                    }
//                }
//            }
//        }
//    }
//
//}
