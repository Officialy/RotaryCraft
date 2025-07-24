///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.production;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.server.MinecraftServer;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.neoforged.common.NeoForge;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.DragonOptions;
//import reika.dragonapi.ModList;
//import reika.dragonapi.base.BlockTieredResource;
//import reika.dragonapi.interfaces.block.MachineRegistryBlock;
//import reika.dragonapi.libraries.ReikaEnchantmentHelper;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.java.ReikaJavaLibrary;
//import reika.dragonapi.libraries.level.ReikaBlockHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.api.Interfaces.IgnoredByBorer;
//import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
//import reika.rotarycraft.base.blockentity.BlockEntityBeamMachine;
//import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
//import reika.rotarycraft.base.blocks.ee.BlockMiningPipe;
//import reika.rotarycraft.registry.*;
//
//import java.util.ArrayList;
//import java.util.Collection;
//import java.util.List;
//
//public class BlockEntityBorer extends BlockEntityBeamMachine implements EnchantableMachine, GuiController, DiscreteFunction {
//
//    /**
//     * Power required to break a block, per 0.1F hardness
//     */
//    public static final int DIGPOWER = (int) (64 * RotaryConfig.COMMON.getBorerPowerMult());
//    public static final int OBSIDIANTORQUE = 512;
//    private static final int genRange = RotaryConfig.COMMON.BORERGEN.get();
//    private static int anticipationDistance = -1;
//    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantment.fortune).addFilter(Enchantment.efficiency).addFilter(Enchantment.silkTouch).addFilter(Enchantment.sharpness);
//    public boolean drops = true;
//    public boolean[][] cutShape = new boolean[7][5]; // 7 cols, 5 rows
//    private int pipemeta2 = 0;
//    private int reqpow;
//    private int mintorque;
//    private int step = 1;
//    private boolean jammed = false;
//
//    private boolean isMiningAir = false;
//
//    private boolean hitProtection = false;
//    private int notifiedPlayer = 0;
//
//    private int durability = RotaryConfig.COMMON.BORERMAINTAIN.get() ? 256 : Integer.MAX_VALUE;
//
//    private int soundtick = 0;
//
//    @Override
//    protected void onFirstTick(Level world, BlockPos pos) {
//        if (anticipationDistance < 0)
//            anticipationDistance = Math.max(2, Math.max(genRange, this.getServerViewDistance()));
//    }
//
//    private int getServerViewDistance() {
//        MinecraftServer s = MinecraftServer.getServer();
//        return s != null ? s.getConfigurationManager().getViewDistance() : 0;
//    }
//
//    @Override
//    public int getTextureStateForSide(int s) {
//        switch (this.getBlockMetadata()) {
//            case 0:
//                return s == 4 ? this.getActiveTexture() : 0;
//            case 1:
//                return s == 5 ? this.getActiveTexture() : 0;
//            case 3:
//                return s == 2 ? this.getActiveTexture() : 0;
//            case 2:
//                return s == 3 ? this.getActiveTexture() : 0;
//        }
//        return 0;
//    }
//
//    @Override
//    public void onRedirect() {
//        this.reset();
//    }
//
//    public boolean repair() {
//        if (durability > 0)
//            return false;
//        durability = RotaryConfig.COMMON.BORERMAINTAIN.get() ? 256 : Integer.MAX_VALUE;
//        return true;
//    }
//
//    public boolean isJammed() {
//        return jammed;
//    }
//
//    private void setJammed(boolean jam) {
//        boolean old = jammed;
//        jammed = jam;
//        if (old != jammed) {
//            ReikaWorldHelper.causeAdjacentUpdates(level, xCoord, yCoord, zCoord);
//            level.markBlockForUpdate(xCoord, yCoord, zCoord);
//        }
//    }
//
//    public void reset() {
//        step = 1;
//        this.syncAllData(true);
//    }
//
//    public int getHeadX() {
//        return xCoord + facing.getStepX() * step;
//    }
//
//    public int getHeadZ() {
//        return zCoord + facing.getStepZ() * step;
//    }
//
//    @Override
//    protected int getActiveTexture() {
//        return power > 0 && power >= reqpow && torque >= mintorque ? 1 : 0;
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//
//        tickcount++;
//        this.getIOSides(world, pos, meta);
//        this.getPower(false);
//
//        if (enchantments.hasEnchantments()) {
//            for (int i = 0; i < 6; i++) {
//                world.addParticle("portal", -0.5 + x + 2 * DragonAPI.rand.nextDouble(), y + DragonAPI.rand.nextDouble(), -0.5 + z + 2 * DragonAPI.rand.nextDouble(), 0, 0, 0);
//            }
//        }
//
//        power = (long) omega * (long) torque;
//        if (power <= 0) {
//            this.setJammed(false);
//            return;
//        }
//
//        if (hitProtection && notifiedPlayer < 10) {
//            if (world.getDayTime() % 100 == 0) {
//                Player ep = this.getPlacer();
//                if (ep != null) {
//                    notifiedPlayer++;
//                    int hx = this.getHeadX();
//                    int hz = this.getHeadZ();
//                    String sg = "Your " + this + " has hit a protected area at " + hx + ", " + hz + " and has jammed.";
//                    ReikaChatHelper.sendChatToPlayer(ep, sg);
//                }
//            }
//        }
//
//        if (durability <= 0) {
//            if (tickcount % 5 == 0) {
//                world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "mob.blaze.hit", 0.75F, 0.05F);
//                for (int i = 0; i < 6; i++) {
//                    world.addParticle("smoke", x + DragonAPI.rand.nextDouble(), y + 1 + DragonAPI.rand.nextDouble() * 0.2, z + DragonAPI.rand.nextDouble(), 0, 0, 0);
//                    world.addParticle("crit", x + DragonAPI.rand.nextDouble(), y + 1 + DragonAPI.rand.nextDouble() * 0.2, z + DragonAPI.rand.nextDouble(), 0, 0, 0);
//                }
//            }
//            return;
//        }
//
//        boolean nodig = true;
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (cutShape[i][j]) {
//                    nodig = false;
//                    i = j = 7;
//                }
//            }
//        }
//
//        if (jammed && tickcount % 5 == 0) {
//            world.playLocalSound(pos.getX() + 0.5, pos.getY() + 0.5, pos.getZ() + 0.5, "mob.blaze.hit", 0.75F, 1F);
//            for (int i = 0; i < 6; i++) {
//                world.addParticle("smoke", x + DragonAPI.rand.nextDouble(), y + 1 + DragonAPI.rand.nextDouble() * 0.2, z + DragonAPI.rand.nextDouble(), 0, 0, 0);
//                world.addParticle("crit", x + DragonAPI.rand.nextDouble(), y + 1 + DragonAPI.rand.nextDouble() * 0.2, z + DragonAPI.rand.nextDouble(), 0, 0, 0);
//            }
//        }
//
//        if (nodig)
//            return;
//        if (omega <= 0)
//            return;
//
//        if (tickcount == 1 || step == 1) {
//            isMiningAir = this.checkMiningAir(world, pos, meta);
//        }
//
//        //ReikaJavaLibrary.pConsole(isMiningAir+":"+tickcount+"/"+this.getOperationTime(), Dist.DEDICATED_SERVER);
//
//        if (soundtick > 0)
//            soundtick--;
//
//        if (tickcount >= this.getOperationTime() || (isMiningAir && tickcount % 5 == 0)) {
//            this.skipMiningPipes(world, pos, meta, 0, 16);
//            this.calcReqPowerSafe(world, pos, meta);
//            if (power >= reqpow && reqpow != -1) {
//                this.setJammed(false);
//                if (!world.isClientSide) {
//                    for (int i = 0; i <= anticipationDistance; i++) {
//                        ReikaWorldHelper.forceGenAndPopulate(world, x + (step + 16 * i) * facing.getStepX(), z + (step + 16 * i) * facing.getStepZ(), genRange);
//                    }
//                    this.safeDig(world, pos, meta);
//                    if (!isMiningAir) {
//                        if (soundtick == 0) {
//                            SoundRegistry.RUMBLE.playSoundAtBlock(this);
//                            soundtick = 5;
//                        }
//                        durability--;
//                    }
//                }
//            } else {
//                this.setJammed(true);
//            }
//            tickcount = 0;
//            isMiningAir = false;
//        }
//    }
//
//    public String getCurrentRequiredPower() {
//        if (reqpow < 0)
//            return "Infinity - Blocked";
//        double d1 = ReikaMathLibrary.getThousandBase(reqpow);
//        double d2 = ReikaMathLibrary.getThousandBase(mintorque);
//        String s1 = ReikaEngLibrary.getSIPrefix(reqpow);
//        String s2 = ReikaEngLibrary.getSIPrefix(mintorque);
//        return String.format("Required Power: %.3f%sW; Required Torque: %.3f%sNm", d1, s1, d2, s2);
//    }
//
//    private void safeDig(Level world, BlockPos pos) {
//        try {
//            this.dig(world, pos);
//        } catch (RuntimeException e) {
//            RotaryCraft.LOGGER.error(this + " triggered an exception mining a chunk, probably during worldgen!");
//            e.printStackTrace();
//        }
//    }
//
//    private boolean checkMiningAir(Level world, BlockPos pos) {
//        int a = 0;
//        if (meta > 1)
//            a = 1;
//        int b = 1 - a;
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (cutShape[i][j] || step == 1) {
//                    int xread = x + step * facing.getStepX() + a * (i - 3);
//                    int yread = y + step * facing.getStepY() + (4 - j);
//                    int zread = z + step * facing.getStepZ() + b * (i - 3);
//                    if (world.getBlock(xread, yread, zread) != Blocks.AIR) {
//                        return false;
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    private void skipMiningPipes(Level world, BlockPos pos, int stepped, int max) {
//        if (stepped >= max)
//            return;
//        int a = 0;
//        if (meta > 1)
//            a = 1;
//        int b = 1 - a;
//        boolean allpipe = true;
//        boolean haspipe = false;
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (cutShape[i][j] || step == 1) {
//                    int xread = x + step * facing.getStepX() + a * (i - 3);
//                    int yread = y + step * facing.getStepY() + (4 - j);
//                    int zread = z + step * facing.getStepZ() + b * (i - 3);
//                    //ReikaJavaLibrary.pConsole(xread+","+yread+","+zread);
//                    if (world.getBlock(xread, yread, zread) == RotaryBlocks.MININGPIPE.get()) {
//                        haspipe = true;
//                        int meta2 = world.getBlockMetadata(xread, yread, zread);
//                        Direction dir = BlockMiningPipe.getDirectionFromMeta(meta2);
//                        if (meta2 == 3 || Math.abs(dir.getStepX()) == Math.abs(facing.getStepX()) && Math.abs(dir.getStepZ()) == Math.abs(facing.getStepZ())) {
//
//                        } else {
//                            allpipe = false;
//                        }
//                    }
//                }
//            }
//        }
//        if (haspipe && allpipe) {
//            step++;
//            this.skipMiningPipes(world, pos, meta, stepped + 1, max);
//        }
//    }
//
//    private boolean ignoreBlockExistence(Level world, BlockPos pos) {
//        Block b = world.getBlock(pos);
//        if (b == Blocks.AIR)
//            return true;
//        Material mat = ReikaWorldHelper.defaultBlockState().getMaterial(world, pos);
//        if (mat == Material.water || mat == Material.lava)
//            return true;
//        if (b.isAir(world, pos))
//            return true;
//        if (b instanceof BlockLiquid || b instanceof BlockFluidBase)
//            return true;
//        if (b instanceof IgnoredByBorer)
//            return ((IgnoredByBorer) b).ignoreHardness(world, world.provider.dimensionId, pos, world.getBlockMetadata(pos));
//        return false;
//    }
//
//    private void calcReqPowerSafe(Level world, BlockPos pos, int metadata) {
//        try {
//            this.calcReqPower(world, pos, metadata);
//        } catch (RuntimeException e) {
//            RotaryCraft.LOGGER.error(this + " triggered an exception mining a chunk, probably during worldgen!");
//            e.printStackTrace();
//            reqpow = -1;
//        }
//    }
//
//    private void calcReqPower(Level world, BlockPos pos, int metadata) {
//        reqpow = 0;
//        mintorque = 0;
//        int lowtorque = -1;
//        int a = 0;
//        if (metadata > 1)
//            a = 1;
//        int b = 1 - a;
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (cutShape[i][j] || step == 1) {
//                    int xread = x + step * facing.getStepX() + a * (i - 3);
//                    int yread = y + step * facing.getStepY() + (4 - j);
//                    int zread = z + step * facing.getStepZ() + b * (i - 3);
//                    this.reqPowAdd(world, xread, yread, zread);
//                    if (reqpow == -1)
//                        return;
//                }
//            }
//        }
//
//        lowtorque = mintorque;
//
//        //ReikaJavaLibrary.pConsole(mintorque, Dist.DEDICATED_SERVER);
//
//        if (torque < lowtorque)
//            reqpow = -1;
//    }
//
//
//    private void reqPowAdd(Level world, int xread, int yread, int zread) {
//        if (step > 30000000) {
//            reqpow = -1;
//            return;
//        }
//        if (!this.ignoreBlockExistence(world, xread, yread, zread)) {
//            Block id = world.getBlock(xread, yread, zread);
//            float hard = id.getBlockHardness(world, xread, yread, zread);
//			/*
//			if (this.isMineableBedrock(world, xread, yread, zread)) {
//				mintorque += PowerReceivers.BEDROCKBREAKER.getMinTorque();
//				reqpow += PowerReceivers.BEDROCKBREAKER.getMinPower();
//			}
//			else */
//            if (TwilightForestHandler.getInstance().isToughBlock(id)) {
//                mintorque += 2048;
//                reqpow += 65536;
//            } else if (hard < 0) {
//                reqpow = -1;
//            } else if (id == RotaryBlocks.DECO.get() && meta == RotaryItems.shieldblock.getItemDamage()) {
//                reqpow = -1;
//            } else if (id instanceof SemiUnbreakable && ((SemiUnbreakable) id).isUnbreakable(world, xread, yread, zread, meta)) {
//                reqpow = -1;
//            } else {
//                reqpow += (int) (DIGPOWER * 10 * hard);
//                mintorque += ReikaMathLibrary.ceil2exp((int) (10 * hard));
//            }
//
//            if (DragonOptions.DEBUGMODE.getState()) {
//                RotaryCraft.LOGGER.log(this + " mined block " + id + ":" + meta + " at " + xread + ", " + yread + ", " + zread + "; pow=" + reqpow + ", torq=" + mintorque);
//            }
//        }
//    }
//
//    public int getRequiredTorque() {
//        return mintorque;
//    }
//
//    public long getRequiredPower() {
//        return reqpow;
//    }
//
//    private void support(Level world, BlockPos pos) {
//        int a = 0;
//        if (metadata > 1)
//            a = 1;
//        int b = 1 - a;
//        int xread;
//        int yread;
//        int zread;
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (cutShape[i][j] || step == 1) {
//                    xread = x + step * facing.getStepX() + a * (i - 3);
//                    yread = y + step * facing.getStepY() + (4 - j);
//                    zread = z + step * facing.getStepZ() + b * (i - 3);
//                    Block id = world.getBlock(xread, yread + 1, zread);
//                    if (id == Blocks.SAND || id == Blocks.GRAVEL)
//                        if (this.checkTop(i, j)) {
//                            if (id == Blocks.SAND)
//                                world.setBlock(xread, yread + 1, zread, Blocks.SANDSTONE);
//                            else
//                                world.setBlock(xread, yread + 1, zread, Blocks.STONE);
//                        }
//                }
//            }
//        }
//    }
//
//    private boolean checkTop(int i, int j) {
//        while (j > 0) {
//            j--;
//            if (cutShape[i][j])
//                return false;
//        }
//        return true;
//    }
//
//    private boolean dropBlocks(int xread, int yread, int zread, Level world, BlockPos pos, Block id) {
//        if (ModList.TWILIGHT.isLoaded() && id == TwilightForestHandler.BlockEntry.MAZESTONE.getBlock())
//            RotaryAdvancements.CUTKNOT.triggerAchievement(this.getPlacer());
//        if (id == Blocks.BEDROCK || id == Blocks.END_PORTAL_FRAME)
//            return false;
//        if (!world.isClientSide && !ReikaPlayerAPI.playerCanBreakAt((WorldServer) world, xread, yread, zread, id, meta, this.getServerPlacer())) {
//            hitProtection = true;
//            return false;
//        }
//        BlockEntity tile = this.getBlockEntity(xread, yread, zread);
//        if (tile instanceof RotaryCraftBlockEntity)
//            return false;
//        if (drops && id != Blocks.AIR) {
//			/*
//			if (this.isMineableBedrock(world, xread, yread, zread)) {
//				ItemStack is = ReikaItemHelper.getSizedItemStack(RotaryItems.bedrockdust.copy(), DifficultyEffects.BEDROCKDUST.getInt());
//				if (!this.chestCheck(world, pos, is)) {
//					ReikaItemHelper.dropItem(world, x+0.5, y+1.125, z+0.5, is, 3);
//				}
//				return true;
//			}*/
//            if (id == Blocks.MOB_SPAWNER) {
//                BlockEntityMobSpawner spw = (BlockEntityMobSpawner) tile;
//                if (spw != null) {
//                    ItemStack is = RotaryItems.SPAWNER.get();
//                    ReikaSpawnerHelper.addMobNBTToItem(is, spw);
//                    if (!this.chestCheck(world, pos, is))
//                        ReikaItemHelper.dropItem(world, x + 0.5, y + 1.125, z + 0.5, is, 3);
//                    return true;
//                }
//            }
//            if (tile instanceof Container) {
//                Container ii = (Container) tile;
//                List<ItemStack> contents = ReikaInventoryHelper.getWholeInventory(ii);
//                ReikaInventoryHelper.clearInventory(ii);
//                for (int i = 0; i < contents.size(); i++) {
//                    ItemStack is = contents.get(i);
//                    boolean fits = this.chestCheck(world, pos, is);
//                    if (!fits) {
//                        ReikaItemHelper.dropItem(world, x + 0.5, y + 1.125, z + 0.5, is, 3);
//                    }
//                }
//            }
//            if (enchantments.getEnchantment(Enchantment.silkTouch) > 0 && this.canSilk(world, xread, yread, zread)) {
//                ItemStack is = ReikaBlockHelper.getSilkTouch(world, xread, yread, zread, id, meta, this.getPlacer(), false);//new ItemStack(id, 1, ReikaBlockHelper.getSilkTouchMetaDropped(id, meta));
//                if (!this.chestCheck(world, pos, is)) {
//                    ReikaItemHelper.dropItem(world, x + 0.5, y + 1.125, z + 0.5, is, 3);
//                }
//                return true;
//            }
//            int fortune = enchantments.getEnchantment(Enchantment.fortune);
//            Collection<ItemStack> items = id.getDrops(world, xread, yread, zread, meta, fortune);
//            NeoForge.EVENT_BUS.post(new HarvestDropsEvent(xread, yread, zread, world, id, meta, fortune, 1, (ArrayList<ItemStack>) items, this.getPlacer(), false));
//            if (id instanceof BlockTieredResource) {
//                Player ep = this.getPlacer();
//                BlockTieredResource bt = (BlockTieredResource) id;
//                boolean harvest = ep != null && bt.isPlayerSufficientTier(world, xread, yread, zread, ep);
//                items = harvest ? bt.getHarvestResources(world, xread, yread, zread, fortune, ep) : bt.getNoHarvestResources(world, xread, yread, zread, fortune, ep);
//            } else if (id instanceof MachineRegistryBlock) {
//                items = ReikaJavaLibrary.makeListFrom(((MachineRegistryBlock) id).getMachine(world, xread, yread, zread).getCraftedProduct());
//            }
//            if (items != null) {
//                for (ItemStack is : items) {
//                    if (!this.chestCheck(world, pos, is)) {
//                        ReikaItemHelper.dropItem(world, x + 0.5, y + 1.125, z + 0.5, is, 3);
//                    }
//                }
//            }
//        }
//        return true;
//    }
//
//    private boolean canSilk(Level world, BlockPos pos) {
//        Block id = world.getBlockState(pos).getBlock();
//        if (id == Blocks.AIR)
//            return false;
//        if (id == Blocks.FIRE)
//            return false;
//        if (id == Blocks.CAULDRON)
//            return false;
//        if (id == Blocks.REEDS)
//            return false;
//        if (id == Blocks.POWERED_COMPARATOR || id == Blocks.UNPOWERED_COMPARATOR)
//            return false;
//        if (id == Blocks.POWERED_REPEATER || id == Blocks.UNPOWERED_REPEATER)
//            return false;
//        if (id == Blocks.REDSTONE_WIRE)
//            return false;
//        if (id == Blocks.PISTON_EXTENSION || id == Blocks.PISTON_HEAD)
//            return false;
//        if (id == Blocks.WOODEN_DOOR || id == Blocks.IRON_DOOR)
//            return false;
//        if (RotaryBlocks.isTechnicalBlock(id))
//            return false;
//        if (id.isAir(world, pos))
//            return false;
//        if (id instanceof BlockLiquid || id instanceof BlockFluidBase)
//            return false;
//        if (id instanceof BlockTieredResource)
//            return false;
//        return !id.hasBlockEntity(world.getBlockMetadata(pos));
//    }
//
//    private boolean chestCheck(Level world, BlockPos pos, ItemStack is) {
//        if (is == null)
//            return false;
//        if (world.isClientSide)
//            return false;
//        for (int i = 0; i < 6; i++) {
//            Direction dir = dirs[i];
//            BlockEntity te = getAdjacentBlockEntity(dir);
//            if (te instanceof Container) {
//                boolean flag = true;
//                if (te instanceof PartialInventory) {
//                    if (!((PartialInventory) te).hasInventory())
//                        flag = false;
//                }
//                if (flag) {
//                    if (ReikaInventoryHelper.addToIInv(is, (Container) te))
//                        return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private void dig(Level world, BlockPos pos) {
//        if (step == 1)
//            RotaryAdvancements.BORER.triggerAchievement(this.getPlacer());
//        this.support(world, pos);
//        int a = 0;
//        if (metadata > 1)
//            a = 1;
//        int b = 1 - a;
//        int xread;
//        int yread;
//        int zread;
//
//        if (step == 1) {
//            pipemeta2 = pipemeta;
//            pipemeta = 3;
//        } else if (pipemeta > 2 && pipemeta2 != 3)
//            pipemeta = pipemeta2;
//
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++) {
//                if (cutShape[i][j] || step == 1) {
//                    xread = x + step * facing.getStepX() + a * (i - 3);
//                    yread = y + step * facing.getStepY() + (4 - j);
//                    zread = z + step * facing.getStepZ() + b * (i - 3);
//                    Block bk = world.getBlockState(xread, yread, zread).getBlock();
//                    if (this.dropBlocks(xread, yread, zread, world, pos, bk, world.getBlockMetadata(xread, yread, zread))) {
//                        ReikaSoundHelper.playBreakSound(world, xread, yread, zread, bk);
//                        world.setBlock(xread, yread, zread, RotaryBlocks.MININGPIPE.get(), pipemeta, 3);
//                    } else {
//                        step--;
//                    }
//                }
//            }
//        }
//        NeoForge.EVENT_BUS.post(new BorerDigEvent(this, step, x + step * facing.getStepX(), y + step * facing.getStepY(), z + step * facing.getStepZ(), enchantments.hasEnchantment(Enchantment.silkTouch)));
//        step++;
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("step", step);
//        NBT.putBoolean("jam", jammed);
//        NBT.putInt("dura", durability);
//
//        NBT.putInt("reqpow", reqpow);
//        NBT.putInt("reqtrq", mintorque);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        step = NBT.getInt("step");
//        jammed = NBT.getBoolean("jam");
//        durability = NBT.getInt("dura");
//
//        mintorque = NBT.getInt("reqtrq");
//        reqpow = NBT.getInt("reqpow");
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//        NBT.putBoolean("drops", drops);
//
//        NBT.put("enchants", enchantments.saveAdditional());
//
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++)
//                NBT.putBoolean("cut" + (i * 7 + j), cutShape[i][j]);
//        }
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//        drops = NBT.getBoolean("drops");
//
//        enchantments.load(NBT.getTagList("enchants", NBTTypes.COMPOUND.ID));
//
//        for (int i = 0; i < 7; i++) {
//            for (int j = 0; j < 5; j++)
//                cutShape[i][j] = NBT.getBoolean("cut" + (i * 7 + j));
//        }
//    }
//
//    @Override
//    protected void makeBeam(Level world, BlockPos pos) {
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
//        return MachineRegistry.BORER;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return this.isJammed() ? 15 : 0;
//    }
//
//    @Override
//    public int getOperationTime() {
//        int base = DurationRegistry.BORER.getOperationTime(omega);
//        float ench = ReikaEnchantmentHelper.getEfficiencyMultiplier(enchantments.getEnchantment(Enchantment.efficiency));
//        return (int) (base / ench);
//    }
//
//    @Override
//    public MachineEnchantmentHandler getEnchantmentHandler() {
//        return enchantments;
//    }
//}
