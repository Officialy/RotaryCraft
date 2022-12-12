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
import org.checkerframework.checker.units.qual.C;
import org.jetbrains.annotations.Nullable;
import reika.rotarycraft.base.ItemRotaryTool;

import java.util.List;
import java.util.Locale;

public class ItemEngineUpgrade extends ItemRotaryTool {

    private static UpgradeType type;

    public ItemEngineUpgrade() {
        super(new Properties().stacksTo(16));
    }

    @Override
    public void appendHoverText(ItemStack is, @Nullable Level p_41422_, List<Component> li, TooltipFlag p_41424_) {
        if (is.getTag() != null) {
            int magnet = is.getTag().getInt("magnet");
            if (is.getTag().contains("magnet")) {
                li.add(Component.literal(String.format("Magnetized to %d microTeslas", magnet)));
            } else if (magnet < 720) {
                li.add(Component.literal("Must be magnetized to 720 microTeslas to be used"));
            }

            if (is.getTag().contains("upgradeType")) {
                li.add(Component.literal(is.getTag().getString("upgradeType").toLowerCase(Locale.ROOT)));
            }
        } else {
            li.add(Component.literal("Must be magnetized to 720 microTeslas to be used"));
        }
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

        private final String desc;

        UpgradeType(String d) {
            desc = d;
        }

    }

}
