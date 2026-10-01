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
import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;

import net.minecraft.world.InteractionResult;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.neoforge.fluids.FluidStack;

import reika.dragonapi.libraries.ReikaNBTHelper;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.Fillable;
import reika.rotarycraft.auxiliary.interfaces.IntegratedGearboxable;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.registry.RotaryFluids;
import reika.rotarycraft.registry.RotaryItems;

import java.util.List;
import java.util.function.Consumer;

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
        ReikaItemHelper.updateStackTag(is, tag -> tag.putInt("ratio", Math.abs(ratio)));
        if (f != null) {
            ItemIntegratedGearbox i = (ItemIntegratedGearbox) is.getItem();
            i.addFluid(is, f, i.getCapacity(is));
        }
        return is;
    }

    public static int getRatioFromIntegratedGearItem(ItemStack is, boolean requireFill) {
        int ratio = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("ratio", 0);
        if (requireFill && is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() == null)
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

    // 1.21.5: Item.appendHoverText now takes (ItemStack, TooltipContext, TooltipDisplay, Consumer<Component>, TooltipFlag).
    @Override
    public void appendHoverText(ItemStack is, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> pTooltipComponents, TooltipFlag pIsAdvanced) {
        if (is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("ratio", 0) > 0) {
            int ratio = getRatioFromIntegratedGearItem(is, false);
            pTooltipComponents.accept(Component.translatable("tooltip.integratedgearbox.ratio").append(Component.literal(Math.abs(ratio) + "x")));
            ratio = getRatioFromIntegratedGearItem(is, true);
            if (ratio > 0) {
                pTooltipComponents.accept(Component.translatable("tooltip.integratedgearbox.torquemode"));
            } else if (ratio < 0) {
                pTooltipComponents.accept(Component.translatable("tooltip.integratedgearbox.speedmode"));
            } else {
                pTooltipComponents.accept(Component.translatable("tooltip.integratedgearbox.requiresfill"));
                if (this.getCurrentFillLevel(is) > 0) {
                    int amt = this.getCurrentFillLevel(is);
                    Fluid f = this.getCurrentFluid(is);
                    pTooltipComponents.accept(Component.literal("Is " + (amt * 100F / this.getCapacity(is))).append("%" + Component.translatable("tooltip.integratedgearbox.filled") + new FluidStack(f, amt)));
                }
            }
        } else {
            pTooltipComponents.accept(Component.translatable("tooltip.integratedgearbox.gearandfluid"));
        }
    }

    @Override
    public boolean isValidFluid(FluidStack fluid, ItemStack stack) {
        if (fluid == null || fluid.isEmpty()) return false;
        Fluid current = getCurrentFluid(stack);
        return (current == null || current == fluid.getFluid())
                && (fluid.getFluid() == RotaryFluids.LIQUID_NITROGEN.get() || fluid.getFluid() == RotaryFluids.LUBRICANT.get());
    }

    @Override
    public int getCapacity(ItemStack is) {
        return 500;
    }

    @Override
    public int getCurrentFillLevel(ItemStack is) {
        return is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() != null ? is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("lvl", 0) : 0;
    }

    @Override
    public int addFluid(ItemStack stack, FluidStack fluid, int amount) {
        if (amount <= 0 || !isValidFluid(fluid, stack)) return 0;
        int added = Math.min(amount, Math.max(0, getCapacity(stack) - getCurrentFillLevel(stack)));
        if (added > 0) ReikaItemHelper.updateStackTag(stack, tag -> {
            tag.putInt("lvl", getCurrentFillLevel(stack) + added);
            ReikaNBTHelper.writeFluidToNBT(tag, new FluidStack(fluid.getFluid(), 1));
        });
        return added;
    }

    @Override
    public boolean isFull(ItemStack is) {
        return is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag() != null && this.getCurrentFillLevel(is) >= this.getCapacity(is);
    }

    private boolean canFill(ItemStack is) {
        return !this.isFull(is);
    }

    @Override
    public Fluid getCurrentFluid(ItemStack stack) {
        if (getCurrentFillLevel(stack) <= 0) return null;
        FluidStack fluid = ReikaNBTHelper.getFluidFromNBT(stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag());
        return fluid.isEmpty() ? null : fluid.getFluid();
    }

}
