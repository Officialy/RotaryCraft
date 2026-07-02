/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items;

import net.minecraft.core.component.DataComponents;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.item.component.CustomData;
import net.minecraft.world.item.component.TooltipDisplay;
import net.minecraft.world.level.Level;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.api.interfaces.TensionStorage;
import reika.rotarycraft.base.ItemBasic;
import reika.rotarycraft.registry.RotaryItems;

import java.util.function.Consumer;

// 1.21.5 NOTE: Item.initCapabilities(ItemStack, CompoundTag) returning ICapabilityProvider was
// removed in favour of the RegisterCapabilitiesEvent registry. We keep the per-stack default
// tension write in onCraftedBy, and have appendHoverText migrated to the 5-arg signature.
public class ItemCoil extends ItemBasic implements TensionStorage {

    private int tension;

    public ItemCoil() {
        super(RotaryItems.itemProperties().stacksTo(1), 1);
    }

    @Override
    public void appendHoverText(ItemStack stack, Item.TooltipContext ctx, TooltipDisplay display, Consumer<Component> lore, TooltipFlag flag) {
        lore.accept(Component.literal("Tension: " + tension));
    }

    // 1.21.5: Item#onCraftedBy signature is now (ItemStack, Player).
    @Override
    public void onCraftedBy(ItemStack stack, Player pPlayer) {
        ReikaItemHelper.updateStackTag(stack, __T__ -> __T__.putInt("stiffness", stack == RotaryItems.BEDROCK_ALLOY_SPRING.get().getDefaultInstance() ? 4 : 1));
    }

    @Override
    public int getStiffness(ItemStack is) {
        if (is.has(DataComponents.CUSTOM_DATA)) {
            CompoundTag tag = is.getOrDefault(DataComponents.CUSTOM_DATA, CustomData.EMPTY).copyTag();
            return tag.getIntOr("stiffness", 1);
        }
        return 1;
    }

    @Override
    public int getPowerScale(ItemStack is) {
        if (is == RotaryItems.BEDROCK_ALLOY_SPRING.get().getDefaultInstance()) {
            return 4;
        }
        return 1;
    }

    @Override
    public boolean isBreakable(ItemStack is) {
        return is == RotaryItems.HSLA_STEEL_SPRING.get().getDefaultInstance();
    }

    @Override
    public int setTension(int i) {
        return tension = i;
    }
}
