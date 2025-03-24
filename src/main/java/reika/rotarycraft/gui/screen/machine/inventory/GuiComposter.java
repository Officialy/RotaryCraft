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
//import com.mojang.blaze3d.vertex.PoseStack;
//
//import net.minecraft.world.entity.player.Inventory;
//import net.minecraftforge.client.gui.ScreenUtils;
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.blockentities.farming.BlockEntityComposter;
//
//public class GuiComposter extends GuiNonPoweredMachine {
//    private final BlockEntityComposter comp;
//
//    public GuiComposter(Inventory p5ep, BlockEntityComposter Composter) {
//        super(new ContainerComposter(p5ep, Composter), Composter);
//        comp = Composter;
//        ep = p5ep;
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(poseStack, par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        int i1 = comp.getScaledTimer(48);
//        ScreenUtils.drawTexturedModalRect(poseStack, j + 79, k + 34, 176, 14, 1 * (i1) + 1, 16, 0);
//
//        int i2 = comp.getScaledTemperature(54);
//        if (i2 > 54)
//            i2 = 54;
//        ScreenUtils.drawTexturedModalRect(poseStack, j + 24, k + 70 - i2, 177, 86 - i2, 9, i2, 0);
//    }
//
//    @Override
//    protected ResourceLocation getGuiTexture() {
//        return ResourceLocation.parse("compostergui");
//    }
//}
