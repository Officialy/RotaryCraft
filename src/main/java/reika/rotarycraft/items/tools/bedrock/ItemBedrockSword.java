/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.bedrock;

import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ToolMaterial;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.registry.ConfigRegistry;

// 1.21.5 NOTE: SwordItem has been removed; swords are now plain Items configured via
// Item.Properties.sword(...). onBlockStartBreak / getEnchantmentValue / isRepairable have
// also been removed from Item. hurtEnemy now returns void.
public class ItemBedrockSword extends Item {

    public ItemBedrockSword() {
        super(reika.rotarycraft.registry.RotaryItems.itemProperties().stacksTo(1).sword(ToolMaterial.NETHERITE, 5, 12));
    }

    @Override
    public void hurtEnemy(ItemStack is, LivingEntity target, LivingEntity player) {
        if (player instanceof Player && (ConfigRegistry.FAKEBEDROCK.getState() || !ReikaPlayerAPI.isFake((Player) player))) {
            if (target.isAlive() || target.getHealth() <= 0) {
                // headDrop / xp-split bonuses pending DragonAPI port
            }
        }
        super.hurtEnemy(is, target, player);
    }
}
