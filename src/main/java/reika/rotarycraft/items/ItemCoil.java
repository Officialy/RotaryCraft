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

import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.chat.Component;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import net.neoforged.common.capabilities.ICapabilityProvider;

import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.TensionStorage;
import reika.rotarycraft.base.ItemBasic;
import reika.rotarycraft.registry.RotaryItems;

import java.util.List;

public class ItemCoil extends ItemBasic implements TensionStorage {

    private int tension;

    public ItemCoil() {
        super(new Properties().stacksTo(1), 1);
    }

    
    @Override
    public ICapabilityProvider initCapabilities(ItemStack stack,  CompoundTag nbt) {
        if (!stack.getOrCreateTag().contains("energy")) {
            stack.getOrCreateTag().putDouble("energy", tension);
        }
        return null;
    }

    @Override
    public void appendHoverText(ItemStack stack,  Level pLevel, List<Component> lore, TooltipFlag pIsAdvanced) {
        lore.add(Component.literal("Tension: " + tension));
    }

    @Override
    public void onCraftedBy(ItemStack stack, Level pLevel, Player pPlayer) {
        stack.getOrCreateTag().putInt("stiffness", stack == RotaryItems.BEDROCK_ALLOY_SPRING.get().getDefaultInstance() ? 4 : 1);
    }

    @Override
    public int getStiffness(ItemStack is) {
        if (is.hasTag()) {
            return is.getTag().getInt("stiffness");
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
