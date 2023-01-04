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
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.gui.Button;
//import net.minecraft.entity.player.Player;
//
//import reika.dragonapi.instantiable.io.PacketTarget;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerVacuum;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.BlockEntityVacuum;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.OnlyIn;
//
//
//public class GuiVacuum extends GuiPowerOnlyMachine {
//    private final BlockEntityVacuum vac;
//
//    /**
//     * window height is calculated with this values, the more rows, the heigher
//     */
//    private int inventoryRows = 0;
//
//    public GuiVacuum(Player p5ep, BlockEntityVacuum te) {
//        super(new ContainerVacuum(p5ep, te), te);
//        allowUserInput = false;
//        short var3 = 222;
//        int var4 = var3 - 108;
//        inventoryRows = te.getContainerSize() / 9;
//        imageHeight = var4 + inventoryRows * 18;
//        vac = te;
//        ep = p5ep;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        addRenderableWidget(new Button(0, var5 + imageWidth - 1, var6 + 32, 43, 20, "Get XP"));
//    }
//
//    @Override
//    protected void actionPerformed(Button button) {
//        super.actionPerformed(button);
//        if (button.id == 0)
//            ReikaPacketHelper.sendUpdatePacket(RotaryCraft.packetChannel, PacketRegistry.VACUUMXP.ordinal(), vac, PacketTarget.server);
//    }
//
//    /**
//     * Draw the foreground layer for the GuiContainer (everything in front of the items)
//     */
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);
//
//        font.draw("XP: " + String.format("%d", vac.getExperience()), 150 - font.width(String.format("%d", vac.getExperience())), 6, 4210752);
//    }
//
//    /**
//     * Draw the background layer for the GuiContainer (everything behind the items)
//     */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        String var4 = "/gui/container.png";
//        GL11.glColor4f(1.0F, 1.0F, 1.0F, 1.0F);
//        //mc.renderEngine.bindTexture(GuiContainer.field_110408_a);
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, this.getGuiTexture());
//        int var5 = (width - imageWidth) / 2;
//        int var6 = (height - imageHeight) / 2;
//        this.drawTexturedModalRect(var5, var6, 0, 0, imageWidth, inventoryRows * 18 + 17);
//        this.drawTexturedModalRect(var5, var6 + inventoryRows * 18 + 17, 0, 126, imageWidth, 96);
//
//        this.drawPowerTab(var5, var6);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "/Reika/RotaryCraft/Textures/GUI/basicstorage.png";
//    }
//}
