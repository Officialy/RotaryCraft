/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.items;
import java.util.function.Consumer;
import net.minecraft.core.component.DataComponents;
import net.minecraft.network.chat.Component;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import reika.rotarycraft.api.interfaces.TensionStorage;
import reika.rotarycraft.base.ItemBasic;
import reika.rotarycraft.registry.RotaryItems;
public final class ItemCoil extends ItemBasic implements TensionStorage {
    public static final int MAX_CHARGE = 32000;
    private int tension;
    public ItemCoil() { super(RotaryItems.itemProperties().component(DataComponents.MAX_DAMAGE, MAX_CHARGE + 1).component(DataComponents.DAMAGE, 0), 1); }
    public static int getCharge(ItemStack stack) {
        int charge = stack.getDamageValue();
        if (charge == 0) charge = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag().getIntOr("energy", 0);
        return Math.clamp(charge, 0, MAX_CHARGE);
    }
    public static void setCharge(ItemStack stack, int charge) {
        stack.setDamageValue(Math.clamp(charge, 0, MAX_CHARGE));
        var tag = stack.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
        if (tag.contains("energy")) { tag.remove("energy"); stack.set(DataComponents.CUSTOM_DATA, CustomData.of(tag)); }
    }
    @Override public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> lines, TooltipFlag flag) {
        lines.accept(Component.translatable("tooltip.rotarycraft.spring.charge", getCharge(stack)));
    }
    @Override public boolean isBarVisible(ItemStack stack) { return false; }
    @Override public int getStiffness(ItemStack stack) { return stack.is(RotaryItems.BEDROCK_ALLOY_SPRING.get()) ? 16 : 1; }
    @Override public int getPowerScale(ItemStack stack) { return stack.is(RotaryItems.BEDROCK_ALLOY_SPRING.get()) ? 4 : 1; }
    @Override public boolean isBreakable(ItemStack stack) { return !stack.is(RotaryItems.BEDROCK_ALLOY_SPRING.get()); }
    @Override public int setTension(int value) { return tension = value; }
}
