//package reika.rotarycraft.modinterface;
//
//import net.minecraft.entity.player.Player;
//import net.minecraft.item.EnumRarity;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.util.IIcon;
//import net.minecraft.util.BlockHitResult;
//import net.minecraft.world.World;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.common.util.Direction;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent;
//import net.minecraftforge.event.entity.player.PlayerInteractEvent.Action;
//
//import reika.dragonapi.ModList;
//import reika.dragonapi.asm.DependentMethodStripper.ModDependent;
//import reika.dragonapi.Base.BlockEntityBase;
//import reika.dragonapi.libraries.io.ReikaSoundHelper;
//import reika.dragonapi.modinteract.DeepInteract.ItemCustomFocus;
//import reika.dragonapi.modinteract.DeepInteract.ReikaThaumHelper;
//import reika.dragonapi.modinteract.ItemHandlers.AppEngHandler;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.registry.ItemRegistry;
//
//import cpw.mods.fml.relauncher.Side;
//import cpw.mods.fml.relauncher.SideOnly;
//import thaumcraft.api.aspects.Aspect;
//import thaumcraft.api.aspects.AspectList;
//import thaumcraft.api.wands.FocusUpgradeType;
//
//public class ItemScrewdriverFocus extends ItemCustomFocus {
//
//	public ItemScrewdriverFocus() {
//		super(RotaryCraft.tabRotaryTools);
//	}
//
//	@Override
//	@SideOnly(Dist.CLIENT)
//	protected String getIconString() {
//		return "rotarycraft:screw-focus";
//	}
//
//	@Override
//	@ModDependent(ModList.THAUMCRAFT)
//	public AspectList getVisCost(ItemStack focusStack) {
//		return new AspectList().add(Aspect.ORDER, 1);
//	}
//
//	@Override
//	public boolean onLeftClick(Level world, int x, int y, int z, Player ep, ItemStack wand, Direction side) {
//		ep.setCurrentItemOrArmor(0, ItemRegistry.SCREWDRIVER.get());
//		if (!MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(ep, Action.LEFT_CLICK_BLOCK, x, y, z, side.ordinal(), world)))
//			world.getBlock(x, y, z).onBlockClicked(world, x, y, z, ep);
//		ep.setCurrentItemOrArmor(0, wand);
//		this.playSound(world, x, y, z);
//		return true;
//	}
//
//	@Override
//	public ItemStack onFocusRightClick(ItemStack wand, Level world, Player player, BlockHitResult mov) {
//		if (mov == null)
//			return null;
//		ItemStack is = ItemRegistry.SCREWDRIVER.get();
//		boolean flag = false;
//		int x = mov.blockX;
//		int y = mov.blockY;
//		int z = mov.blockZ;
//		float a = (float)mov.hitVec.xCoord;
//		float b = (float)mov.hitVec.yCoord;
//		float c = (float)mov.hitVec.zCoord;
//		boolean nativeTile = world.getBlockEntity(x, y, z) instanceof BlockEntityBase;
//		if (ItemRegistry.SCREWDRIVER.get().onItemUse(is, player, world, x, y, z, mov.sideHit, a, b, c))
//			flag = nativeTile;
//		if ((nativeTile || this.canMultiTool(wand)) && this.fakeScrewclick(world, x, y, z, player, mov, a, b, c, is))
//			flag = true;
//		if (this.canMultiTool(wand) && AppEngHandler.getInstance().tryRightClick(is, x, y, z, mov.sideHit, player, world, 0))
//			flag = true;
//		if (flag) {
//			this.playSound(world, x, y, z);
//		}
//		return null;
//	}
//
//	private boolean canMultiTool(ItemStack wand) {
//		ItemStack focus = ReikaThaumHelper.getWandFocusStack(wand);
//		return focus != null && this.getUpgradeLevel(focus, FocusUpgradeType.enlarge) > 0;
//	}
//
//	private void playSound(Level world, int x, int y, int z) {
//		ReikaSoundHelper.playSoundFromServer(world, x+0.5, y+0.5, z+0.5, "thaumcraft:wand", 0.4F, 0.7F+world.rand.nextFloat()*0.6F, true);
//		ReikaSoundHelper.playSoundFromServer(world, x+0.5, y+0.5, z+0.5, "mob.blaze.hit", 0.5F, 0.75F, true);
//	}
//
//	private boolean fakeScrewclick(Level world, int x, int y, int z, Player player, BlockHitResult mov, float a, float b, float c, ItemStack is) {
//		ItemStack hold = player.getCurrentEquippedItem();
//		if (hold != null)
//			hold = hold.copy();
//		player.setCurrentItemOrArmor(0, is);
//		boolean ret = world.getBlock(x, y, z).onBlockActivated(world, x, y, z, player, mov.sideHit, a, b, c);
//		//MinecraftForge.EVENT_BUS.post(new PlayerInteractEvent(player, Action.RIGHT_CLICK_BLOCK, x, y, z, mov.sideHit, world));
//		player.setCurrentItemOrArmor(0, hold);
//		return ret;
//	}
//
//	@Override
//	public EnumRarity getRarity(ItemStack focusstack) {
//		return EnumRarity.common;
//	}
//
//	@SideOnly(Dist.CLIENT)
//	@Override
//	public IIcon getIconFromDamage(int par1) {
//		return itemIcon;
//	}
//
//	/**
//	 * What color will the focus orb be rendered on the held wand
//	 */
//	@Override
//	@SideOnly(Dist.CLIENT)
//	public int getFocusColor(ItemStack focusstack) {
//		return 0xCACBF2;
//	}
//
//	/**
//	 * Does the focus have ornamentation like the focus of the nine hells. Ornamentation is a standard icon rendered in a cross around the focus
//	 */
//	@Override
//	@SideOnly(Dist.CLIENT)
//	public IIcon getOrnament(ItemStack focusstack) {
//		return null;
//	}
//
//	/**
//	 * An icon to be rendered inside the focus itself
//	 */
//	@Override
//	@SideOnly(Dist.CLIENT)
//	public IIcon getFocusDepthLayerIcon(ItemStack focusstack) {
//		return null;
//	}
//
//	@Override
//	public int getActivationCooldown(ItemStack focusstack) {
//		return 100;
//	}
//
//	@Override
//	protected String getID() {
//		return "screwdriver";
//	}
//
//	@Override
//	public FocusUpgradeType[] getPossibleUpgradesByRank(ItemStack focusstack, int rank) {
//		return new FocusUpgradeType[] {FocusUpgradeType.enlarge};
//	}
//
//}
