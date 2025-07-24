/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;

import net.minecraft.core.NonNullList;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fluids.FluidStack;

import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.Fillable;
import reika.rotarycraft.auxiliary.interfaces.IntegratedGearboxable;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

import java.util.List;

public class ItemIntegratedGearbox extends ItemRotaryTool implements Fillable {

    public ItemIntegratedGearbox(Properties properties) {
        super(properties);
    }

    public static ItemStack getIntegratedGearItem(int ratio, FluidStack f) {
        if (ratio == 0)
            return null;
        //if (ratio > 0)
        //	meta += 4;
        //return RotaryItems.GEARUPGRADE.getStackOfMetadata(meta);
        ItemStack is = RotaryItems.INTEGRATED_GEARBOX.get().getDefaultInstance();
        if (f != null) {
            ItemIntegratedGearbox i = (ItemIntegratedGearbox) is.getItem();
            i.addFluid(is, f, i.getCapacity(is));
        }
        return is;
    }

    public static int getRatioFromIntegratedGearItem(ItemStack is, boolean requireFill) {
        int ratio = is.getTag().getInt("ratio");
        if (requireFill && is.getTag() == null)
            return 0;
        ItemIntegratedGearbox i = (ItemIntegratedGearbox) is.getItem();
        if (requireFill && !i.isFull(is))
            return 0;
        boolean pos = !requireFill || i.getCurrentFluid(is) == RotaryFluids.LUBRICANT.get();//meta >= 4;
        return pos ? ratio : -ratio;

    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack is, UseOnContext context) {
        BlockEntity te = context.getLevel().getBlockEntity(context.getClickedPos());
        if (te instanceof IntegratedGearboxable) {
            if (((IntegratedGearboxable) te).applyIntegratedGear(is)) {
                int count = is.getCount();
                if (!context.getPlayer().isCreative())
                    is.setCount(count--);
                if (is.getCount() == 0)
                    is = null;
                context.getPlayer().setItemInHand(context.getHand(), is); //todo check if this works
                return InteractionResult.CONSUME;
            }
        }
        return InteractionResult.FAIL;
    }

    @Override
    public void appendHoverText(ItemStack is,  Level pLevel, List<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (is.getOrCreateTag().getInt("ratio") > 0) {
            int ratio = getRatioFromIntegratedGearItem(is, false);
            pTooltipComponents.add(Component.translatable("tooltip.integratedgearbox.ratio").append(Component.literal(Math.abs(ratio) + "x")));
            ratio = getRatioFromIntegratedGearItem(is, true);
            if (ratio > 0) {
                pTooltipComponents.add(Component.translatable("tooltip.integratedgearbox.torquemode"));
            } else if (ratio < 0) {
                pTooltipComponents.add(Component.translatable("tooltip.integratedgearbox.speedmode"));
            } else {
                pTooltipComponents.add(Component.translatable("tooltip.integratedgearbox.requiresfill"));
                if (is.getTag() != null) {
                    int amt = this.getCurrentFillLevel(is);
                    Fluid f = this.getCurrentFluid(is);
                    pTooltipComponents.add(Component.literal("Is " + (amt * 100F / this.getCapacity(is))).append("%" + Component.translatable("tooltip.integratedgearbox.filled") + new FluidStack(f, amt)));
                }
            }
        } else {
            pTooltipComponents.add(Component.translatable("tooltip.integratedgearbox.gearandfluid"));
        }
    }

    @Override
    public boolean isValidFluid(FluidStack f, ItemStack is) {
        return is.getTag() != null ? f.equals(ReikaNBTHelper.getFluidFromNBT(is.getTag())) : this.isValidFluid(f);
    }

    private boolean isValidFluid(FluidStack f) {
        if (f.equals(RotaryFluids.LIQUID_NITROGEN.get()))
            return true;
        return f.equals(RotaryFluids.LUBRICANT.get());
    }

    @Override
    public int getCapacity(ItemStack is) {
        return 500;
    }

    @Override
    public int getCurrentFillLevel(ItemStack is) {
        return is.getTag() != null ? is.getTag().getInt("lvl") : 0;
    }

    @Override
    public int addFluid(ItemStack is) {
        return 0;
    }

    //@Override
    public int addFluid(ItemStack is, FluidStack f, int amt) {
        int liq = 0;
        if (!this.isValidFluid(f)) {
            return 0;
        }
        ReikaNBTHelper.writeFluidToNBT(is.getOrCreateTag(), f);
        liq = is.getTag().getInt("lvl");
        if (!f.equals(ReikaNBTHelper.getFluidFromNBT(is.getTag()))) {
            return 0;
        }

        int toadd = Math.min(amt, this.getCapacity(is) - liq);
        is.getTag().putInt("lvl", liq + toadd);
        return toadd;
    }

    @Override
    public boolean isFull(ItemStack is) {
        return is.getTag() != null && this.getCurrentFillLevel(is) >= this.getCapacity(is);
    }

    private boolean canFill(ItemStack is) {
        return !this.isFull(is);
    }

    @Override
    public Fluid getCurrentFluid(ItemStack is) {
        return is.getTag() != null ? ReikaNBTHelper.getFluidFromNBT(is.getTag()).getFluid() : null;
    }

}
