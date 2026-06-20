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
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.CreativeModeTab;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.TooltipFlag;
import net.minecraft.world.level.Level;
import reika.rotarycraft.base.ItemRotaryTool;

import java.util.List;
import java.util.Locale;

public class ItemEngineUpgrade extends ItemRotaryTool {

    private static UpgradeType type;

    public ItemEngineUpgrade() {
        super(reika.rotarycraft.registry.RotaryItems.itemProperties().stacksTo(16));
    }

    // 1.21.5: Item.appendHoverText now takes (ItemStack, TooltipContext, TooltipDisplay, Consumer<Component>, TooltipFlag).
    @Override
    public void appendHoverText(ItemStack is, net.minecraft.world.item.Item.TooltipContext ctx, net.minecraft.world.item.component.TooltipDisplay display, java.util.function.Consumer<Component> li, TooltipFlag flag) {
        if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag() != null) {
            int magnet = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getIntOr("magnet", 0);
            if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().contains("magnet")) {
                li.accept(Component.literal(String.format("Magnetized to %d microTeslas", magnet)));
            } else if (magnet < 720) {
                li.accept(Component.literal("Must be magnetized to 720 microTeslas to be used"));
            }

            if (is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().contains("upgradeType")) {
                li.accept(Component.literal(is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA, net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getStringOr("upgradeType", "").toLowerCase(Locale.ROOT)));
            }
        } else {
            li.accept(Component.literal("Must be magnetized to 720 microTeslas to be used"));
        }
    }

    /**
     * Reads the upgrade type stored on a stack's CUSTOM_DATA under key "upgradeType"
     * (the value matches {@link UpgradeType#desc}). Returns null if the stack is not an
     * engine upgrade or carries no recognised type. This is the single source of truth used by
     * every consumer (magnetizer, gas engine, jet engine) so crafted upgrades dispatch correctly.
     */
    public static UpgradeType getUpgrade(ItemStack is) {
        if (is == null || is.isEmpty() || !(is.getItem() instanceof ItemEngineUpgrade))
            return null;
        String s = is.getOrDefault(net.minecraft.core.component.DataComponents.CUSTOM_DATA,
                net.minecraft.world.item.component.CustomData.EMPTY).copyTag().getStringOr("upgradeType", "");
        for (UpgradeType t : UpgradeType.list)
            if (t.desc.equals(s))
                return t;
        return null;
    }

    public enum UpgradeType {
        PERFORMANCE("upgrade.gasperf"),
        MAGNETOSTATIC1("upgrade.tier1"), //Made with ethanol
        MAGNETOSTATIC2("upgrade.tier2"), //Made in magnetizer
        MAGNETOSTATIC3("upgrade.tier3"), //Made with pulse jet ingot
        MAGNETOSTATIC4("upgrade.tier4"), //Made with 4MW extractor product
        MAGNETOSTATIC5("upgrade.tier5"), //Made with bedrock
        AFTERBURNER("upgrade.afterburn"),//Gas Turbine afterburner
        EFFICIENCY("upgrade.efficiency"),
        FLUX("upgrade.flux"),
        REDSTONE("upgrade.redstone"), //Auto redstone signal
        LODESTONE("upgrade.lodestone"); //Requires ReC lodestone

        public static final UpgradeType[] list = values();

        public final String desc;

        UpgradeType(String d) {
            desc = d;
        }

    }

}
