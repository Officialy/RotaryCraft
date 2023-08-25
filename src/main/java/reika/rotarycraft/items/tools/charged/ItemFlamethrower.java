/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.charged;

import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import reika.rotarycraft.base.ItemChargedTool;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.phys.Vec3;

public class ItemFlamethrower extends ItemChargedTool {

    public ItemFlamethrower() {
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player ep, InteractionHand hand) {
        //ep.setItemInUse(is, this.getMaxItemUseDuration(ep.getMainHandItem()));
        return InteractionResultHolder.pass(this.getDefaultInstance());
    }

    /**
     * Called as the item is being used by an entity.
     *
     * @param pLevel
     * @param pLivingEntity
     * @param pStack
     * @param pRemainingUseDuration
     */
    @Override
    public void onUseTick(Level pLevel, LivingEntity pLivingEntity, ItemStack pStack, int pRemainingUseDuration) {
        Vec3 vec = pLivingEntity.getLookAngle();
        pLivingEntity.level().addParticle(ParticleTypes.FLAME, pLivingEntity.blockPosition().getX(), pLivingEntity.blockPosition().getY(), pLivingEntity.blockPosition().getZ(), vec.x() / 4, vec.y() / 4, vec.z() / 4);
    }

    /**
     * How long it takes to use or consume an item
     *
     * @param pStack
     */
    @Override
    public int getUseDuration(ItemStack pStack) {
        return 7200;
    }

}
