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
//import java.util.List;
//
//import org.lwjgl.opengl.GL11;
//
//import net.minecraft.client.gui.Button;
//import net.minecraft.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//
//import net.minecraft.world.biome.Biome;
//import net.minecraftforge.fluids.FluidStack;
//
//import reika.dragonapi.instantiable.gui.ImageButton;
//import reika.dragonapi.libraries.io.ReikaPacketHelper;
//import reika.dragonapi.libraries.io.ReikaTextureHelper;
//import reika.dragonapi.libraries.rendering.ReikaLiquidRenderer;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.GuiPowerOnlyMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerTerraformer;
//import reika.rotarycraft.registry.PacketRegistry;
//import reika.rotarycraft.blockentities.level.BlockEntityTerraformer;
//
//public class GuiTerraformer extends GuiPowerOnlyMachine {
//
//    BlockEntityTerraformer terra;
//    private List<Biome> targets;
//    private int offset = 0;
//
//    public GuiTerraformer(Player pl, BlockEntityTerraformer te) {
//        super(new ContainerTerraformer(pl, te), te);
//        ep = pl;
//        terra = te;
//        imageWidth = 240;
//        imageHeight = 222;
//    }
//
//    @Override
//    public void initGui() {
//        super.initGui();
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        String = "/Reika/RotaryCraft/Textures/GUI/biomes.png";
//
//        targets = new ArrayList(terra.getValidTargetBiomes(terra.getCentralBiome()));
//
//        for (int i = 0; i < this.getNumberBiomesOnPage(); i++) {
//            Biome b = targets.get(i + offset);
//            addRenderableWidget(new ImageButton(i, j + 8, k + 17 + 39 * i, 32, 32, 32 * (b.biomeID % 8), 32 * (b.biomeID / 8), , b.biomeName, 0xffffff, false, RotaryCraft.class));
//        }
//
//		 ="/Reika/RotaryCraft/Textures/GUI/buttons.png";
//        addRenderableWidget(new ImageButton(100, j + 11, k + 6, 24, 12, 18, 110, , RotaryCraft.class));
//        addRenderableWidget(new ImageButton(101, j + 11, k + imageHeight - 14, 24, 12, 42, 110, , RotaryCraft.class));
//    }
//
//    private int getNumberBiomesOnPage() {
//        return Math.min(targets.size(), 5);
//    }
//
//    @Override
//    protected void actionPerformed(Button b) {
//        super.actionPerformed(b);
//
//        if (b.id == 100 && offset > 0) {
//            offset--;
//        } else if (b.id == 101 && offset < targets.size() - 5) {
//            offset++;
//        } else if (b.id < targets.size()) {
//            Biome biome = targets.get(b.id);
//            ReikaPacketHelper.sendPacketToServer(RotaryCraft.packetChannel, PacketRegistry.TERRAFORMER.ordinal(), terra, biome.biomeID);
//        }
//
//        this.initGui();
//    }
//
//    @Override
//    protected void drawGuiContainerForegroundLayer(int par2, int par3) {
//        super.drawGuiContainerForegroundLayer(par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//
//        Biome from = terra.getCentralBiome();
//
//        for (int i = 0; i < this.getNumberBiomesOnPage(); i++) {
//            Biome to = targets.get(i + offset);
//            FluidStack liq = terra.getReqLiquidForTransform(from, to);
//            if (liq != null) {
//                GL11.glColor4f(1, 1, 1, 1);
//                ReikaLiquidRenderer.bindFluidTexture(liq.getFluid());
//                IIcon ico = ReikaLiquidRenderer.getFluidIconSafe(liq.getFluid());
//                this.drawTexturedModelRectFromIcon(48, 17 + i * 39, ico, 16, 16);
//                api.drawCenteredStringNoShadow(font, String.format("%d", liq.amount), 56, 21 + i * 39, 0);
//            } else {
//                api.drawLine(48, 17 + i * 39, 16 + 48, 16 + 17 + i * 39, 0);
//                api.drawLine(16 + 48, 17 + i * 39, 48, 16 + 17 + i * 39, 0);
//            }
//            ArrayList<ItemStack> items = terra.getItemsForTransform(from, to);
//            if (items != null && !items.isEmpty()) {
//                int step = (int) ((System.nanoTime() / 500000000) % items.size());
//                ItemStack is = items.get(step);
//                api.drawItemStack(itemRender, font, is, 48, 19 + 16 + i * 39);
//            } else {
//                api.drawLine(48, 18 + 17 + i * 39, 16 + 48, 18 + 16 + 17 + i * 39, 0);
//                api.drawLine(16 + 48, 18 + 17 + i * 39, 48, 18 + 16 + 17 + i * 39, 0);
//            }
//        }
//        String = "/Reika/RotaryCraft/Textures/GUI/" + this.getGuiTexture() + ".png";
//        ReikaTextureHelper.bindTexture(RotaryCraft.class, );
//    }
//
//    @Override
//    protected void renderBg(PoseStack poseStack,float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack,par1, par2, par3);
//
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "terraformergui";
//    }
//
//}
