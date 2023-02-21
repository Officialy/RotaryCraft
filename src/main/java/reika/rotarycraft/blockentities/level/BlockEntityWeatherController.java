///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.level;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.entity.effect.EntityLightningBolt;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.storage.WorldInfo;
//import net.minecraftforge.common.MinecraftForge;
//import net.minecraftforge.oredict.OreDictionary;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaEntityHelper;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.RotaryItems;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryItems;
//
//import java.util.ArrayList;
//
//public class BlockEntityWeatherController extends InventoriedPowerReceiver implements ConditionalOperation {
//
//    private int cooldown = 0;
//
//    private RainMode rainmode = RainMode.NONE;
//
//    @Override
//    public Block getBlockEntityBlockID() {
//        return null;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getPowerBelow();
//        if (cooldown > 0)
//            cooldown--;
//        if (power < MINPOWER)
//            return;
//        if (!world.canBlockSeeTheSky(x, y + 1, z))
//            return;
//
//        WorldInfo wi = world.getWorldInfo();
//        //ReikaJavaLibrary.pConsole(rainmode, Dist.DEDICATED_SERVER);
//        if (rainmode == RainMode.SUPERSTORM) {
//            wi.setRaining(true);
//            wi.setThundering(true);
//            if (DragonAPI.rand.nextInt(20) == 0) {
//                int xl = x - 64 + DragonAPI.rand.nextInt(129);
//                int zl = z - 64 + DragonAPI.rand.nextInt(129);
//                int yl = world.getTopSolidOrLiquidBlock(xl, zl);
//                world.addWeatherEffect(new EntityLightningBolt(world, xl, yl, zl));
//            }
//        }
//
//        if (cooldown > 0)
//            return;
//
//        rainmode = this.getRainMode();
//        //ReikaJavaLibrary.pConsole(rainmode);
//        if (rainmode.isRain() && RotaryConfig.COMMON.BANRAIN.getState())
//            rainmode = RainMode.NONE;
//
//        if (this.isAlready(world, rainmode))
//            return;
//
//        if (!rainmode.hasAction())
//            return;
//
//        boolean isThunder = world.isThundering();
//        boolean rain = rainmode.isRain();
//        boolean thunder = rainmode.isThunder();
//        boolean storm = rainmode == RainMode.SUPERSTORM;
//        wi.setRaining(rain);
//        wi.setThundering(thunder);
//        MinecraftForge.EVENT_BUS.post(new WeatherControlEvent(this, rain, thunder, storm));
//    }
//
//    @Override
//    protected void animateWithTick(Level level, BlockPos blockPos) {
//
//    }
//
//    private boolean isAlready(Level world, RainMode m) {
//        boolean rain = m.isRain();
//        boolean thunder = m.isThunder();
//        boolean rain2 = world.isRaining();
//        boolean thunder2 = world.isThundering();
//        return rain == rain2 && thunder == thunder2;
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    private void fire(ItemStack is, ItemStack is2) {
//        level.playLocalSound(xCoord + 0.5, yCoord + 0.5, zCoord + 0.5, "DragonAPI.rand.explode", 1F, 1F);
//        if (is != null) {
//            ItemEntity ei = new ItemEntity(level, xCoord + 0.5, yCoord + 1.0625, zCoord + 0.5, new ItemStack(is.getItem(), 1, is.getItemDamage()));
//            ReikaEntityHelper.addRandomDirVelocity(ei, 0.2);
//            ei.delayBeforeCanPickup = 5000;
//            ei.getAge() = 5900;
//            ei.motionY = 3;
//            if (!level.isClientSide)
//                level.addFreshEntity(ei);
//        }
//        if (is2 != null) {
//            ItemEntity ei = new ItemEntity(level, xCoord + 0.5, yCoord + 1.0625, zCoord + 0.5, ReikaItemHelper.getSizedItemStack(is2, 1));
//            ReikaEntityHelper.addRandomDirVelocity(ei, 0.2);
//            ei.delayBeforeCanPickup = 5000;
//            ei.getAge() = 5900;
//            ei.motionY = 3;
//            if (!level.isClientSide)
//                level.addFreshEntity(ei);
//        }
//    }
//
//    private int hasSawdust() {
//        int sawdust = ReikaInventoryHelper.locateInInventory(RotaryItems.SAWDUST, inv, false);
//        if (sawdust >= 0)
//            return sawdust;
//        ArrayList<ItemStack> li = OreDictionary.getOres("dustWood");
//        for (int i = 0; i < itemHandler.getSlots(); i++) {
//            ItemStack is = itemHandler.getStackInSlot(i);
//            if (is != null) {
//                if (ReikaItemHelper.collectionContainsItemStack(li, is))
//                    return i;
//            }
//        }
//        return -1;
//    }
//
//    private RainMode getRainMode() {
//        RainMode rainmode;
//        ItemStack is = null;
//        ItemStack is2 = null;
//        int sawdust = this.hasSawdust();
//        boolean silverio = ReikaInventoryHelper.checkForItemStack(RotaryItems.SILVERIODIDE, inv, false);
//        boolean redstone = ReikaInventoryHelper.checkForItem(Items.REDSTONE, inv);
//        boolean glowdust = ReikaInventoryHelper.checkForItem(Items.GLOWSTONE_DUST, inv);
//        if (sawdust >= 0) {
//            rainmode = RainMode.SUN;
//            is = RotaryItems.SAWDUST;
//        } else if (silverio) {
//            rainmode = RainMode.RAIN;
//            is = RotaryItems.SILVERIODIDE;
//            if (redstone) {
//                rainmode = RainMode.THUNDER;
//                is2 = new ItemStack(Items.REDSTONE, 1, 0);
//            } else if (glowdust) {
//                rainmode = RainMode.SUPERSTORM;
//                is2 = new ItemStack(Items.GLOWSTONE_DUST, 1, 0);
//            }
//        } else
//            rainmode = RainMode.NONE;
//        //ReikaJavaLibrary.pConsole(rainmode, Dist.DEDICATED_SERVER);
//        if (this.isAlready(level, rainmode))
//            return this.rainmode;
//        cooldown = 200 + DragonAPI.rand.nextInt(400);
//        if (rainmode.hasAction())
//            this.fire(is, is2);
//        int slot = -1;
//        switch (rainmode) {
//            case NONE:
//                break;
//            case SUN:
//                slot = sawdust;
//                ReikaInventoryHelper.decrStack(slot, inv);
//                break;
//            case RAIN:
//                slot = ReikaInventoryHelper.locateInInventory(RotaryItems.SILVERIODIDE, inv, false);
//                ReikaInventoryHelper.decrStack(slot, inv);
//                break;
//            case THUNDER:
//                slot = ReikaInventoryHelper.locateInInventory(RotaryItems.SILVERIODIDE, inv, false);
//                ReikaInventoryHelper.decrStack(slot, inv);
//                slot = ReikaInventoryHelper.locateInInventory(Items.REDSTONE, inv);
//                ReikaInventoryHelper.decrStack(slot, inv);
//                break;
//            case SUPERSTORM:
//                slot = ReikaInventoryHelper.locateInInventory(RotaryItems.SILVERIODIDE, inv, false);
//                ReikaInventoryHelper.decrStack(slot, inv);
//                slot = ReikaInventoryHelper.locateInInventory(Items.GLOWSTONE_DUST, inv);
//                ReikaInventoryHelper.decrStack(slot, inv);
//                break;
//        }
//        return rainmode;
//    }
//
//    private boolean isValidWeatherItem(ItemStack is) {
//        if (ReikaItemHelper.matchStacks(is, RotaryItems.SAWDUST))
//            return true;
//        if (ReikaItemHelper.matchStacks(is, RotaryItems.SILVERIODIDE))
//            return true;
//        if (is.getItem() == Items.REDSTONE)
//            return true;
//        if (is.getItem() == Items.GLOWSTONE_DUST)
//            return true;
//        ArrayList<ItemStack> li = OreDictionary.getOres("dustWood");
//        return ReikaItemHelper.collectionContainsItemStack(li, is);
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 18;
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.WEATHERCONTROLLER;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return this.isValidWeatherItem(is);
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    @Override
//    protected String getTEName() {
//        return null;
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return this.getRainMode().hasAction();
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return cooldown <= 0 ? this.areConditionsMet() ? "Operational" : "Empty Inventory" : "Idle";
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 0;
//    }
//
//    @Override
//    public boolean isEmpty() {
//        return false;
//    }
//
//    @Override
//    public ItemStack getItem(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItem(int pIndex, int pCount) {
//        return null;
//    }
//
//    @Override
//    public ItemStack removeItemNoUpdate(int pIndex) {
//        return null;
//    }
//
//    @Override
//    public void setItem(int pIndex, ItemStack pStack) {
//
//    }
//
//    @Override
//    public boolean stillValid(Player pPlayer) {
//        return false;
//    }
//
//    @Override
//    public void clearContent() {
//
//    }
//
//    @Override
//    public boolean hasAnInventory() {
//        return false;
//    }
//
//    @Override
//    public boolean hasATank() {
//        return false;
//    }
//
//    private enum RainMode {
//        NONE(),
//        SUN(),
//        RAIN(),
//        THUNDER(),
//        SUPERSTORM();
//
//        public boolean isRain() {
//            return this.ordinal() > SUN.ordinal();
//        }
//
//        public boolean isThunder() {
//            return this.ordinal() > RAIN.ordinal();
//        }
//
//        public boolean hasAction() {
//            return this != NONE;
//        }
//    }
//}
