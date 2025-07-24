///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.items.tools.charged;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.core.Direction;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.level.block.entity.BlockEntity;
//import net.minecraft.world.level.material.Fluid;
//import net.minecraft.world.phys.HitResult;
//import net.neoforged.eventbus.api.Event;
//import net.neoforged.fluids.FluidStack;
//import net.neoforged.fluids.capability.IFluidHandler;
//import reika.dragonapi.libraries.ReikaNBTHelper;
//import reika.dragonapi.libraries.io.ReikaChatHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.rotarycraft.RotaryCraft;
//import reika.rotarycraft.base.ItemChargedTool;
//import reika.rotarycraft.blockentities.Storage.BlockEntityReservoir;
//
//import java.util.List;
//
//public class ItemPump extends ItemChargedTool implements EnchantableItem {
//
//    public ItemPump() {
//    }
//
//    @Override
//    public ItemStack onItemRightClick(ItemStack is, Level world, Player ep) {
//        if (ep.isShiftKeyDown()) {
//            this.incrementMode(is);
//            return is;
//        }
//        if (this.getMode(is) == Modes.DRAIN) {
//            this.warnCharge(is);
//            HitResult mov = ReikaPlayerAPI.getLookedAtBlock(ep, 5, true);
//            if (mov != null) {
//                int x = mov.blockX;
//                int y = mov.blockY;
//                int z = mov.blockZ;
//                Block id = world.getBlockState(pos).getBlock();
//                if (id != Blocks.AIR) {
//                    if (ReikaWorldHelper.isLiquidSourceBlock(world, pos)) {
//                        Fluid f = ReikaFluidHelper.lookupFluidForBlock(id);
//                        if (f != null && !world.isClientSide) {
//                            this.drainLiquid(world, pos, is, f);
//                        } else {
//                            RotaryCraft.LOGGER.debug("Null fluid for block " + id + ", yet was marked as such!");
//                        }
//                    } else {
//                        RotaryCraft.LOGGER.debug("Not a fluid block (" + id + ")");
//                    }
//                }
//            }
//        }
//        return is;
//    }
//
//    private void drainLiquid(Level world, BlockPos pos, ItemStack is, Fluid f) {
//        Fluid f2 = is.getTag() == null ? null : ReikaNBTHelper.getFluidFromNBT(is.getTag());
//        if (f2 == null) {
//            if (is.getTag() == null)
//                is.put(new CompoundTag());
//            this.drainAndFill(world, pos, is, f, 1000);
//        } else {
//            int amt = is.getTag().getInt("lvl");
//            if (f2.equals(f)) {
//                if (amt < BlockEntityReservoir.CAPACITY) {
//                    this.drainAndFill(world, pos, is, f, amt + 1000);
//                } else {
//                    RotaryCraft.LOGGER.debug("Too little space");
//                }
//            } else {
//                RotaryCraft.LOGGER.debug("Fluid mismatch " + f + " != " + f2);
//            }
//        }
//        if (ReikaEnchantmentHelper.hasEnchantment(Enchantment.aquaAffinity, is)) {
//            int r = 2;
//            for (int j = r; j >= -r; j--) {
//                for (int i = r; i >= -r; i--) {
//                    for (int k = r; k >= -r; k--) {
//                        int dx = x + i;
//                        int dy = y + j;
//                        int dz = z + k;
//                        if (ReikaWorldHelper.isLiquidSourceBlock(world, dx, dy, dz)) {
//                            Block id = world.getBlockState(dx, dy, dz).getBlock();
//                            Fluid f3 = ReikaFluidHelper.lookupFluidForBlock(id);
//                            if (f3 == f) {
//                                int amt = is.getTag().getInt("lvl");
//                                this.drainAndFill(world, dx, dy, dz, is, f3, amt + 1000);
//                            }
//                            if (is.getTag().getInt("lvl") >= BlockEntityReservoir.CAPACITY)
//                                return;
//                        }
//                    }
//                }
//            }
//        }
//    }
//
//    private void drainAndFill(Level world, BlockPos pos, ItemStack is, Fluid f, int amt) {
//        ReikaNBTHelper.writeFluidToNBT(is.getTag(), f);
//        is.getTag().putInt("lvl", amt);
//        world.setBlockToAir(pos);
//        is.setItemDamage(is.getItemDamage() - 1);
//    }
//
//    @Override
//    public Event.Result getEnchantValidity(Enchantment e, ItemStack is) {
//        return e == Enchantment.aquaAffinity ? Event.Result.ALLOW : Event.Result.DENY;
//    }
//
//    @Override
//    public int getItemEnchantability(ItemStack is) {
//        return Items.IRON_AXE.getItemEnchantability();
//    }
//
//    @Override
//    public boolean onItemUse(ItemStack is, Player ep, Level world, BlockPos pos, int s, float par8, float par9, float par10) {
//        BlockEntity te = world.getBlockEntity(pos);
//        if (te instanceof IFluidHandler) {
//            IFluidHandler fl = (IFluidHandler) te;
//            int amt = is.getTag() != null ? is.getTag().getInt("lvl") : 0;
//            if (this.getMode(is) == Modes.PLACE) {
//                Fluid f = is.getTag() != null ? ReikaNBTHelper.getFluidFromNBT(is.getTag()) : null;
//                FluidStack f2 = fl.drain(Direction.values()[s], 1, false);
//                if (f2 != null) {
//                    if (f == null || f == f2.getFluid()) {
//                        int space = BlockEntityReservoir.CAPACITY - amt;
//                        FluidStack fs = fl.drain(Direction.values()[s], space, true);
//                        if (fs != null) {
//                            if (is.getTag() == null)
//                                is.put(new CompoundTag());
//                            is.getTag().putInt("lvl", amt + fs.amount);
//                            ReikaNBTHelper.writeFluidToNBT(is.getTag(), fs.getFluid());
//                        }
//                    }
//                }
//            } else {
//                if (amt > 0) {
//                    Fluid f = ReikaNBTHelper.getFluidFromNBT(is.getTag());
//                    for (int i = 0; i < 6; i++) {
//                        int d = fl.fill(Direction.values()[i], new FluidStack(f, amt), true);
//                        amt -= d;
//                    }
//                    is.getTag().putInt("lvl", amt);
//                    if (amt == 0)
//                        ReikaNBTHelper.writeFluidToNBT(is.getTag(), null);
//                }
//            }
//            return true;
//        } else if (this.getMode(is) == Modes.PLACE) {
//            if (is.getDamageValue() > 0) {
//                this.warnCharge(is);
//                int amt = is.getTag().getInt("lvl");
//                Fluid f = ReikaNBTHelper.getFluidFromNBT(is.getTag());
//                if (f != null && amt >= 1000) {
//                    Block b = f.getBlock();
//                    if (b != null) {
//                        Direction dir = Direction.values()[s];
//                        int dx = x + dir.getStepX();
//                        int dy = y + dir.getStepY();
//                        int dz = z + dir.getStepZ();
//                        if (world.getBlockState(new BlockPos(dx, dy, dz)).isAir() || (world.getBlockState(new BlockPos(dx, dy, dz)).getBlock() == b && !ReikaWorldHelper.isLiquidSourceBlock(world, dx, dy, dz))) {
//                            world.setBlock(dx, dy, dz, b);
//                            is.getTag().putInt("lvl", amt - 1000);
//                            if (amt <= 1000)
//                                ReikaNBTHelper.writeFluidToNBT(is.getTag(), null);
//                            is.setDamageValue(is.getDamageValue() - 1);
//                            for (int i = -1; i <= 1; i++) {
//                                for (int k = -1; k <= 1; k++) {
//                                    world.markBlockForUpdate(dx + i, dy, dz + k);
//                                    world.getBlockState(new BlockPos(dx + i, dy, dz + k)).onNeighborChange(world, new BlockPos(dx + i, dy, dz + k));
//                                    ReikaWorldHelper.causeAdjacentUpdates(world, dx + i, dy, dz + k);
//                                }
//                            }
//                        }
//                    }
//                }
//            } else {
//                ReikaChatHelper.clearChat(); //clr
//                this.noCharge();
//            }
//        }
//        return false;
//    }
//
//    private Modes getMode(ItemStack is) {
//        //return Modes.list[is.getItemDamage()];
//        if (is.getTag() == null)
//            return Modes.DRAIN;
//        return Modes.list[is.getTag().getInt("mode")];
//    }
//
//    private void setMode(ItemStack is, Modes m) {
//        if (is.getTag() == null)
//            is.put(new CompoundTag());
//        //is.setItemDamage(m.ordinal());
//        is.getTag().putInt("mode", m.ordinal());
//    }
//
//    private void incrementMode(ItemStack is) {
//        this.setMode(is, this.getMode(is).next());
//    }
//
//    @Override
//    public void addInformation(ItemStack is, Player ep, List li, boolean par4) {
//        CompoundTag nbt = is.getTag();
//        if (nbt != null) {
//            Fluid f = ReikaNBTHelper.getFluidFromNBT(nbt);
//            if (f != null) {
//                String fluid = f.getRegistryName().toString();
//                int amt = nbt.getInt("lvl");
//                String amount = String.format("%d", amt);
//                String s = "Contents: " + amount + " mB of " + fluid;
//                li.add(s);
//            }
//        }
//        li.add("Mode: " + this.getMode(is).desc);
//    }
//
//    private enum Modes {
//        DRAIN("Drain"),
//        PLACE("Place");
//
//        private static final Modes[] list = values();
//        private final String desc;
//
//        Modes(String s) {
//            desc = s;
//        }
//
//        public Modes next() {
//            return list[(this.ordinal() + 1) % list.length];
//        }
//    }
//
//}
