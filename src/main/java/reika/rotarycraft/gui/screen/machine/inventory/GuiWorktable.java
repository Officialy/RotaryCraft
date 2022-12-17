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
//import net.minecraft.entity.player.Player;
//import net.minecraft.world.Level;
//
//import reika.rotarycraft.base.GuiNonPoweredMachine;
//import reika.rotarycraft.gui.container.Machine.Inventory.ContainerWorktable;
//import reika.rotarycraft.blockentities.production.BlockEntityWorktable;
//
//public class GuiWorktable extends GuiNonPoweredMachine {
//
//    private final BlockEntityWorktable table;
//    //private int rollout = 8;
//    //private int rstep = 0;
//
//    public GuiWorktable(Player pl, BlockEntityWorktable te, Level world) {
//        super(new ContainerWorktable(pl, te, world, true), te);
//        ep = pl;
//        table = te;
//    }
//
//    /*
//    public int getRollout() {
//        return rollout;
//    }
//     */
//    @Override
//    protected void drawGuiContainerForegroundLayer(int a, int b) {
//        super.drawGuiContainerForegroundLayer(a, b);/*
//		int x = 179;
//		int y = 84;
//		for (int i = 0; i < 3; i++) {
//			for (int k = 0; k < 3; k++) {
//				ItemStack is = table.getProgrammedSlot(i, k);
//				if (is != null) {
//					api.drawItemStack(itemRender, font, is, x+k*18, y+i*18);
//				}
//			}
//		}*//*
//		if (rollout <= 8) {
//			int j = (width - imageWidth) / 2;
//			int k = (height - imageHeight) / 2;
//			int x1 = 176;
//			int x2 = 183;
//			if (api.isMouseInBox(j+x1, j+x2, k+78, k+141)) {
//				api.drawTooltipAt(font, "Set Pattern", a-40, b-10);
//			}
//		}*/
//    }
//
//    /*
//    @Override
//    protected void mouseClicked(int x, int y, int button) {
//        super.mouseClicked(x, y, button);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//        int x1 = rollout > 8 ? 232 : 176;
//        int x2 = rollout > 8 ? 239 : 183;
//        if (api.isMouseInBox(j+x1, j+x2, k+78, k+141)) {
//            Minecraft.getInstance().player.playSound("DragonAPI.rand.click", 0.5F, 0.5F);
//            rstep = rollout > 8 ? -1 : 1;
//        }
//    }
//     *//*
//	@Override
//	public boolean isMouseOverSlot(Slot slot, int w, int h)
//	{
//		return this.renderSlot(slot) && super.isMouseOverSlot(slot, w, h);
//	}
//
//	@Override
//	protected boolean renderSlot(Slot slot) {
//		return (slot.slotNumber < 18 || slot.slotNumber >= table.getContainerSize()) || rollout == 64;
//	}
//	  */
//    @Override
//    protected void renderBg(PoseStack poseStack, float par1, int par2, int par3) {
//        super.renderBg(PoseStack poseStack, par1, par2, par3);
//        int j = (width - imageWidth) / 2;
//        int k = (height - imageHeight) / 2;
//		/*
//		rollout += rstep;
//
//		if (rollout > 64) {
//			rollout = 64;
//			rstep = 0;
//			Minecraft.getInstance().player.playSound("DragonAPI.rand.click", 0.5F, 1.5F);
//		}
//		if (rollout < 8) {
//			rollout = 8;
//			rstep = 0;
//			Minecraft.getInstance().player.playSound("DragonAPI.rand.click", 0.5F, 1.5F);
//		}
//
//		int u = rollout <= 8 ? 240 : 176;
//		this.drawTexturedModalRect(j+176, k+78, u, 78, rollout, 64);
//		 */
//        if (!table.isReadyToCraft())
//            return;
//        this.drawTexturedModalRect(j + 79, k + 35, 176, 35, 18, 15);
//        WorktableRecipe wr = table.getToCraft();
//        if (wr != null)
//            api.drawItemStackWithTooltip(itemRender, font, wr.isRecycling() ? wr.getRecycling().getResultItem() : wr.getOutput(), j + 116, k + 35);
//    }
//
//    @Override
//    protected String getGuiTexture() {
//        return "worktablegui2";
//    }
//}
