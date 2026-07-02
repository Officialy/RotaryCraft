/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.registry;

import net.minecraft.core.registries.Registries;
import net.minecraft.resources.Identifier;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.Entity;
import net.minecraft.world.entity.EntityType;
import net.minecraft.world.entity.MobCategory;
import net.neoforged.neoforge.registries.DeferredRegister;
import net.minecraft.core.registries.BuiltInRegistries;
import net.neoforged.neoforge.registries.DeferredHolder;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.entities.EntityDischarge;
import reika.rotarycraft.entities.EntityIceBlock;

import java.util.function.Supplier;

public class RotaryEntities {

    public static final DeferredRegister<EntityType<?>> ENTITIES = DeferredRegister.create(BuiltInRegistries.ENTITY_TYPE, RotaryCraft.MODID);

//    public static final DeferredHolder<EntityType<?>, EntityType<EntityRailGunShot>> RAILGUN = registerEntityType("railgun_shot", () -> EntityType.Builder.of((EntityRailGunShot::new), MobCategory.MISC));

//    public static final DeferredHolder<EntityType<?>, EntityType<EntityFreezeGunShot>> FREEZEGUN = registerEntityType("freezegun_shot", () -> EntityType.Builder.of((EntityFreezeGunShot::new), MobCategory.MISC));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityIceBlock>> ICE = registerEntityType("ice_block", () -> EntityType.Builder.of((EntityIceBlock::new), MobCategory.MISC));

//    public static final DeferredHolder<EntityType<?>, EntityType<EntityGasMinecart>> GASCART = registerEntityType("gas_minecart", () -> EntityType.Builder.of((EntityGasMinecart::new), MobCategory.MISC));

    //LIQUIDBLOCK(EntityLiquidBlock, "Liquid Block");
//    public static final DeferredHolder<EntityType<?>, EntityType<EntitySonicShot>> SHOCKWAVE = registerEntityType("shock_wave", () -> EntityType.Builder.of((EntitySonicShot::new), MobCategory.MISC));

    public static final DeferredHolder<EntityType<?>, EntityType<EntityDischarge>> DISCHARGE = registerEntityType("discharge", () -> EntityType.Builder.of((EntityDischarge::new), MobCategory.MISC));

    //FLAMETHROWER(EntityFlameThrowerFire, "Flamethrower Fire");
//    public static final DeferredHolder<EntityType<?>, EntityType<EntityFlakShot>> FLAKSHOT = registerEntityType("flak_shot", () -> EntityType.Builder.of((EntityFlakShot::new), MobCategory.MISC));

//    public static final DeferredHolder<EntityType<?>, EntityType<EntityGatlingShot>> GATLING = registerEntityType("gatling_round", () -> EntityType.Builder.of((EntityGatlingShot::new), MobCategory.MISC));

//    public static final DeferredHolder<EntityType<?>, EntityType<EntityFlameTurretShot>> FLAMESHOT = registerEntityType("burning_liquid", () -> EntityType.Builder.of((EntityFlameTurretShot::new), MobCategory.MISC));

    /**
     * Registers an entity type.
     *
     * @param name    The registry name of the entity type
     * @param factory The factory used to create the entity type builder
     * @return A RegistryObject reference to the entity type
     */
    private static <T extends Entity> DeferredHolder<EntityType<?>, EntityType<T>> registerEntityType(final String name, final Supplier<EntityType.Builder<T>> factory) {
        // 1.21.5: EntityType.Builder.build now takes ResourceKey<EntityType<?>> instead of a String.
        ResourceKey<EntityType<?>> key = ResourceKey.create(
                Registries.ENTITY_TYPE,
                Identifier.fromNamespaceAndPath(RotaryCraft.MODID, name));
        return ENTITIES.register(name, () -> factory.get().build(key));
    }

/*
	public static void auxRegistration() {
		ReikaItemHelper.registerItemMass(RotaryItems.STEELBOOTS.get(), ReikaEngLibrary.rhoiron, 4);
		ReikaItemHelper.registerItemMass(RotaryItems.STEELCHEST.get(), ReikaEngLibrary.rhoiron, 8);
		ReikaItemHelper.registerItemMass(RotaryItems.STEELLEGS.get(), ReikaEngLibrary.rhoiron, 7);
		ReikaItemHelper.registerItemMass(RotaryItems.STEELHELMET.get(), ReikaEngLibrary.rhoiron, 5);

		double packFactor = 1.5;
		double springFactor = 1.0625;

		ReikaItemHelper.registerItemMass(RotaryItems.HSLA_STEEL_PACK.get(), ReikaEngLibrary.rhoiron*packFactor, 8);
		ReikaItemHelper.registerItemMass(RotaryItems.JUMP.get(), ReikaEngLibrary.rhoiron*springFactor, 4);

		double bedFactor = 1.25;
		ReikaItemHelper.registerItemMass(RotaryItems.BEDBOOTS.get(), ReikaEngLibrary.rhoiron*bedFactor, 4);
		ReikaItemHelper.registerItemMass(RotaryItems.BEDCHEST.get(), ReikaEngLibrary.rhoiron*bedFactor, 8);
		ReikaItemHelper.registerItemMass(RotaryItems.BEDLEGS.get(), ReikaEngLibrary.rhoiron*bedFactor, 7);
		ReikaItemHelper.registerItemMass(RotaryItems.BEDHELM.get(), ReikaEngLibrary.rhoiron*bedFactor, 5);

		ReikaItemHelper.registerItemMass(RotaryItems.BEDROCK_ALLOY_CHESTPLATE.get(), ReikaEngLibrary.rhoiron*bedFactor*packFactor, 8);
		ReikaItemHelper.registerItemMass(RotaryItems.BEDJUMP.get(), ReikaEngLibrary.rhoiron*bedFactor*springFactor, 4);
	}
*/

}
