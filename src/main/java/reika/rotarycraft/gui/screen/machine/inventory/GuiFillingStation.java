///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.gui.screen.machine.inventory;
//
//import com.mojang.blaze3d.vertex.PoseStack;
//import net.minecraft.network.chat.Component;
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraft.world.entity.player.Player;
//import net.minecraftforge.client.gui.ScreenUtils;
//import org.lwjgl.opengl.GL11;
//
//import reika.dragonapi.libraries.rendering.ReikaColorAPI;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.blockentities.auxiliary.BlockEntityFillingStation;
//import reika.rotarycraft.gui.container.machine.inventory.ContainerFillingStation;
//import reika.rotarycraft.registry.RotaryMenus;
//
//public class GuiFillingStation extends GuiPowerOnlyMachine<BlockEntityFillingStation, ContainerFillingStation> {
//
//    private final BlockEntityFillingStation fillingStation;
//    //private Level level = ModLoader.getMinecraftInstance().theWorld;
//
//    public GuiFillingStation(ContainerFillingStation container, Inventory inv, Component title) {
//        super(container, inv, title);
//        fillingStation = (BlockEntityFillingStation) inventory.player.level.getBlockEntity(container.tile.getBlockPos());;
//        imageWidth = 176;
//        imageHeight = 187;
//        inventory = inv;
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float pPartialTick, int pX, int pY) {
//        super.renderBg(poseStack, pPartialTick, pX, pY);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        if (api.isMouseInBox(j + 81, j + 94, k + 20, k + 87, pX, pY)) {
//            int mx = pX;
//            int my = pY;
//            api.drawTooltipAt(poseStack, font, String.format("%d/%d mB", fillingStation.getLevel(), BlockEntityFillingStation.CAPACITY), mx - j, my - k);
//        }
//
//        if (!fillingStation.isEmpty()) {
//            int i2 = fillingStation.getLiquidScaled(66);
//            int x = 82;
//            int y = 87 - i2;
////       todo     IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(fillingStation.getFluidStack());
////            ReikaLiquidRenderer.bindFluidTexture(fillingStation.getFluid());
//
//            int clr = 0xffffffff;
////   todo         if (fillingStation.getFluidStack().canBePlacedInWorld()) {
////                clr = fillingStation.getFluidStack().getBlock().colorMultiplier(fillingStation.level, fillingStation.xCoord * 2, fillingStation.yCoord * 2, fillingStation.zCoord * 2);
////            }
//            GL11.glColor4f(ReikaColorAPI.HextoColorMultiplier(clr, 0), ReikaColorAPI.HextoColorMultiplier(clr, 1), ReikaColorAPI.HextoColorMultiplier(clr, 2), 1);
//            ScreenUtils.drawTexturedModalRect(poseStack, x, y, 12, i2, 0, 0, 0); //todo width and height
//        }
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "fillingstationgui";
//    }
//
//}
