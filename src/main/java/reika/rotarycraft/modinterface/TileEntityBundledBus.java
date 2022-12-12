///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.modinterface;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.inventory.Container;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.level.block.entity.BlockEntity;//import net.minecraft.world.Container;
//import net.minecraft.world.World;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.api.distmarker.Dist;
//import net.minecraftforge.common.util.Direction;
//
//import net.minecraftforge.fml.loading.FMLLoader;
//import reika.dragonapi.ModList;
//import reika.dragonapi.ASM.APIStripper.Strippable;
//import reika.dragonapi.ASM.DependentMethodStripper.ModDependent;
//import reika.dragonapi.instantiable.StepTimer;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.registry.ReikaDyeHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.dragonapi.libraries.registry.ReikaDyeHelper;
//import reika.dragonapi.modinteract.DeepInteract.MESystemReader;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//@Strippable(value={"mrtjp.projectred.api.IBundledTile", "appeng.api.networking.security.IActionHost"})
//public class BlockEntityBundledBus extends BlockEntityPowerReceiver implements IBundledTile, IActionHost {
//
//	@ModDependent(ModList.APPENG)
//	private MESystemReader network;
//	private Object aeGridBlock;
//	private Object aeGridNode;
//	private int AEPowerCost = 1;
//
//	private final InventoryCache output = new InventoryCache();
//
//	private StepTimer checkTimer = new StepTimer(10);
//	private StepTimer cacheTimer = new StepTimer(40);
//
//	public static final int NSLOTS = ReikaDyeHelper.dyes.length;
//
//	private ItemStack[] filter = new ItemStack[NSLOTS];
//
//	private MEWorkTracker hasWork = new MEWorkTracker();
//
//	public BlockEntityBundledBus() {
//		if (ModList.APPENG.isLoaded()) {
//			aeGridBlock = new BasicAEInterface(this, this.getMachine().getCraftedProduct());
//			aeGridNode = FMLLoader.getDist() == Dist.DEDICATED_SERVER ? AEApi.instance().createGridNode((IGridBlock)aeGridBlock) : null;
//		}
//	}
//
//	@Override
//	protected void onInvalidateOrUnload(Level world, int x, int y, int z, boolean invalid) {
//		super.onInvalidateOrUnload(world, x, y, z, invalid);
//		if (ModList.APPENG.isLoaded() && aeGridNode != null)
//			((IGridNode)aeGridNode).destroy();
//	}
//
//	private int[] getBundledInput(Level world, int x, int y, int z) {
//		int[] ret = new int[16];
//		for (int i = 0; i < 6; i++) {
//			byte[] data = ProjectRedAPI.transmissionAPI.getBundledInput(world, x, y, z, i);
//			if (data != null) {
//				for (int k = 0; k < 16; k++)
//					ret[k] = Math.max(ret[k], data[k] & 255);
//			}
//		}
//		return ret;
//	}
//
//	private int getBundledInput(Level world, int x, int y, int z, Direction dir, ReikaDyeHelper color) {
//		byte[] data = ProjectRedAPI.transmissionAPI.getBundledInput(world, x, y, z, dir.ordinal());
//		return data != null ? data[color.ordinal()] & 255 : 0;
//	}
//
//	@Override
//	public void updateEntity(Level world, BlockPos pos) {
//		super.updateBlockEntity();
//		this.getIOSidesDefault(world, pos);
//		this.getPower(false);
//
//		if (world.isClientSide()) {
//
//		}
//		else {
//			cacheTimer.update();
//			if (cacheTimer.checkCap()) {
//				this.buildCache();
//			}
//
//			if (aeGridBlock != null && !world.isClientSide()) {
//				((BasicAEInterface)aeGridBlock).setPowerCost(AEPowerCost);
//			}
//			if (AEPowerCost > 1)
//				AEPowerCost -= Math.max(1, AEPowerCost/40);
//
//			//ReikaJavaLibrary.pConsole(hasWork);
//			if (network != null && power >= MINPOWER) {
//				checkTimer.update();
//				hasWork.tick();
//				if (hasWork.hasWork()) {
//					if (checkTimer.checkCap()) {
//						checkTimer.setCap(Math.min(40, checkTimer.getCap()+2));
//						output.clear();
//						BlockEntity te = this.getAdjacentBlockEntity(this.getFacing());
//						if (te instanceof Container) {
//							output.addInventory((Container)te);
//						}
//						int[] input = this.getBundledInput(world, x, y, z);
//						for (int i = 0; i < Math.min(this.getActiveChannels(), NSLOTS); i++) {
//							ItemStack f1 = filter[i];
//							if (f1 != null && input[i] > 0) {
//								int fit = f1.getMaxStackSize()-output.addItemsToUnderlyingInventories(ReikaItemHelper.getSizedItemStack(f1, f1.getMaxStackSize()), true);
//								if (fit > 0) {
//									hasWork.reset();
//									this.transferItem(ReikaItemHelper.getSizedItemStack(f1, Math.min(fit, f1.getMaxStackSize())), (Container)te);
//								}
//							}
//						}
//					}
//				}
//			}
//		}
//	}
//
//	private int getActiveChannels() {
//		return (int)Math.min(16, power/MINPOWER);
//	}
//
//	private void buildCache() {
//		if (ModList.APPENG.isLoaded()) {
//			Object oldNode = aeGridNode;
//			if (aeGridNode == null) {
//				aeGridNode = FMLLoader.getDist() == Dist.DEDICATED_SERVER ? AEApi.instance().createGridNode((IGridBlock)aeGridBlock) : null;
//			}
//			if (aeGridNode != null)
//				((IGridNode)aeGridNode).updateState();
//
//			if (oldNode != aeGridNode || network == null) {
//				if (aeGridNode == null)
//					network = null;
//				else if (network == null)
//					network = new MESystemReader((IGridNode)aeGridNode, this);
//				else
//					network = new MESystemReader((IGridNode)aeGridNode, network);
//
//				this.buildCallbacks();
//			}
//		}
//	}
//
//	private void buildCallbacks() {
//		if (network != null) {
//			network.clearCallbacks();
//			for (int i = 0; i < filter.length; i++) {
//				ItemStack pattern = filter[i];
//				if (pattern != null) {
//					network.addCallback(pattern, hasWork);
//				}
//			}
//		}
//		hasWork.markDirty();
//	}
//
//	private void transferItem(ItemStack is, Container ii) {
//		long rem = network.removeItem(is, true, true);
//		if (rem > 0) {
//			is.setCount((int)Math.min(is.getCount(), rem));
//			if (ReikaInventoryHelper.addToIInv(is, ii)) {
//				network.removeItem(is, false, true);
//			}
//			AEPowerCost = Math.min(500, AEPowerCost+Math.max(1, is.getCount()/4));
//			checkTimer.setCap(Math.max(4, checkTimer.getCap()-4));
//		}
//	}
//
//	@Override
//	public void saveAdditional(CompoundTag NBT) {
//		super.saveAdditional(NBT);
//
//		CompoundTag fil = new CompoundTag();
//
//		for (int i = 0; i < filter.length; i++) {
//			ItemStack is = filter[i];
//			if (is != null) {
//				CompoundTag tag = new CompoundTag();
//				is.save(tag);
//				fil.put("filter_"+i, tag);
//			}
//		}
//
//		NBT.put("filter", fil);
//	}
//
//	@Override
//	public void load(CompoundTag NBT) {
//		super.load(NBT);
//
//		filter = new ItemStack[filter.length];
//		CompoundTag fil = NBT.getCompound("filter");
//		for (int i = 0; i < filter.length; i++) {
//			String name = "filter_"+i;
//			if (fil.contains(name)) {
//				CompoundTag tag = fil.getCompound(name);
//				ItemStack is = ItemStack.of(tag);
//				filter[i] = is;
//			}
//		}
//	}
//
//	public void setMapping(int slot, ItemStack is) {
//		filter[slot] = is;
//		if (ModList.APPENG.isLoaded())
//			this.buildCallbacks();
//		this.syncAllData(true);
//	}
//
//	public ItemStack getMapping(int slot) {
//		return filter[slot] != null ? filter[slot].copy() : null;
//	}
//
//	@Override
//	protected void animateWithTick(Level world, BlockPos pos) {
//
//	}
//
//	@Override
//	@ModDependent(ModList.APPENG)
//	public IGridNode getGridNode(Direction dir) {
//		return (IGridNode)aeGridNode;
//	}
//
//	@Override
//	@ModDependent(ModList.APPENG)
//	public AECableType getCableConnectionType(Direction dir) {
//		return AECableType.COVERED;
//	}
//
//	private Direction getFacing() {
//		return read != null ? read.getOpposite() : Direction.UP;
//	}
//
//	@Override
//	@ModDependent(ModList.APPENG)
//	public void securityBreak() {
//
//	}
//
//	@Override
//	@ModDependent(ModList.APPENG)
//	public IGridNode getActionableNode() {
//		return (IGridNode)aeGridNode;
//	}
//
//	@Override
//	public byte[] getBundledSignal(int dir) {
//		return null;
//	}
//
//	@Override
//	public boolean canConnectBundled(int side) {
//		return true;
//	}
//
//	@Override
//	public MachineRegistry getMachine() {
//		return MachineRegistry.BUNDLEDBUS;
//	}
//
//	@Override
//	public boolean hasModelTransparency() {
//		return false;
//	}
//
//	@Override
//	public int getRedstoneOverride() {
//		return 0;
//	}
//
//}
//
