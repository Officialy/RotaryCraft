package reika.rotarycraft.auxiliary;

import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerLevel;
import net.minecraft.world.entity.monster.cubemob.Slime;
import net.minecraft.resources.Identifier;
import net.minecraft.world.effect.MobEffect;
import net.minecraft.world.effect.MobEffectCategory;
import net.minecraft.world.effect.MobEffectInstance;
import net.minecraft.world.effect.MobEffects;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.ai.attributes.AttributeModifier;
import net.minecraft.world.entity.ai.attributes.Attributes;

// 1.21.5: MobEffect#addAttributeModifier now takes a ResourceLocation id and an
// AttributeModifier.Operation enum constant (the int form was removed). MobEffects.JUMP
// was renamed to JUMP_BOOST.
public class FreezePotion extends MobEffect {

    private static final Identifier FREEZE_SLOWDOWN_ID = Identifier.fromNamespaceAndPath("rotarycraft", "effect.freeze.slowdown");

    public FreezePotion(MobEffectCategory category, int color) {
        super(category, color);
        this.addAttributeModifier(Attributes.MOVEMENT_SPEED, FREEZE_SLOWDOWN_ID, -10.0D, AttributeModifier.Operation.ADD_MULTIPLIED_TOTAL);
    }

    @Override
    public boolean applyEffectTick(ServerLevel level, LivingEntity e, int pAmplifier) {
        e.addEffect(new MobEffectInstance(MobEffects.JUMP_BOOST, 20, -30));
        e.fallDistance = 0;
        if (e instanceof Slime) {
            e.setJumping(false);
        }
        return true;
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
