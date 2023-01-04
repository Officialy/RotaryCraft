///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.guis.Machine.Inventory;
//
//import java.util.ArrayList;
//
//import net.minecraft.client.gui.Button;
//import net.minecraft.entity.player.Player;
//
//import reika.dragonapi.base.OneSlotContainer;
//import reika.dragonapi.instantiable.gui.ImageButton;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.registry.ReikaParticleHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.decorative.BlockEntityParticleEmitter;
//
//public class GuiParticle extends GuiNonPoweredMachine {
//
//    private final BlockEntityParticleEmitter tile;
//
//    private final ArrayList<ReikaParticleHelper> particles = new ArrayList<>();
//
//    public GuiParticle(Player ep, BlockEntityParticleEmitter te) {
//        super(new OneSlotContainer(ep, te, 28), te);
//        tile = te;
//        this.ep = ep;
//        imageHeight = 194;
//
//        for (int i = 0; i < ReikaParticleHelper.values().length; i++) {
//            ReikaParticleHelper p = ReikaParticleHelper.values()[i];
//            if (tile.isParticleValid(p)) {
//                particles.add(p);
//            }
//        }
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        String = "/Reika/RotaryCraft/Textures/GUI/buttons.png";
//        for (int i = 0; i < particles.size(); i++) {
//            ReikaParticleHelper p = particles.get(i);
//            int dx = (i % 6);
//            int dy = (i / 6);
//            int x = j + 8 + dx * 20;
//            if (dx >= 3 && dy < 2)
//                x += 41;
//            if (dy >= 2) {
//                dx = (i - 12) % 8;
//                x = j + 8 + dx * 20;
//                dy = 2 + (i - 12) / 8;
//            }
//            addRenderableWidget(new ImageButton(i, x, k + 19 + dy * 20, 18, 18, 0, 36, , RotaryCraft.class));
//        }
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        this.bindParticleTexture();
//        for (int i = 0; i < particles.size(); i++) {
//            ReikaParticleHelper p = particles.get(i);
//            int dx = (i % 6);
//            int dy = (i / 6);
//            int x = 10 + dx * 20;
//            int y = 21 + dy * 20;
//            if (dx >= 3 && dy < 2)
//                x += 41;
//            if (dy >= 2) {
//                dx = (i - 12) % 8;
//                dy = 2 + (i - 12) / 8;
//                x = 10 + dx * 20;
//                y = 21 + dy * 20;
//            }
//            int u = 16 * (p.ordinal() % 16);
//            int v = 16 * (p.ordinal() / 16);
//            ScreenUtils.drawTexturedModalRect(x, y, u, v, 16, 16);
//        }
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack,float par1, int par2, int par3) {
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        super.renderBg(PoseStack poseStack,par1, par2, par3);
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        this.initGui();
//        if (button.id < 24000) {
//            ReikaParticleHelper p = particles.get(button.id);
//            int particle = p.ordinal();
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.PARTICLES.ordinal(), tile, particle);
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "particlegui";
//    }
//
//    private void bindParticleTexture() {
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, "/Reika/RotaryCraft/Textures/GUI/particles.png");
//    }
//
//
//}
