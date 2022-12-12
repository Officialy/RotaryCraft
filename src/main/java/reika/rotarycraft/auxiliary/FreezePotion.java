package reika.rotarycraft.auxiliary;

import net.minecraft.network.chat.Component;

import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;
import net.minecraft.world.entity.monster.Slime;

public class FreezePotion extends MobEffect {

    public FreezePotion(MobEffectCategory category, int color) {
        super(category, color); //this had true, 0x289EFF);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, "2532FA5D-7CC8-4440-140E-514A1A162299", -10, AttributeModifier.Operation.fromValue(2));
    }

    @Override
    public void applyEffectTick(LivingEntity e, int pAmplifier) {
        e.addEffect(new MobEffectInstance(MobEffects.JUMP, 20, -30));
        e.fallDistance = 0;
        if (e instanceof Slime) {
            e.setJumping(false);// = Integer.MAX_VALUE;
        }

    }

    @Override
    public Component getDisplayName() {
        return Component.translatable("effect.freeze");
    }

    @Override
    public boolean isBeneficial() {
        return false;
    }

}
