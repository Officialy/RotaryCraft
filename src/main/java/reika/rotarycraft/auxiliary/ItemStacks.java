/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.world.item.ItemStack;
import reika.rotarycraft.registry.MaterialRegistry;
import reika.rotarycraft.registry.RotaryBlocks;

public final class ItemStacks {

    public static final ItemStack STONEROD = MaterialRegistry.STONE.getShaftUnitItem();
//
//    public static final ItemStack WOODGEAR = GearboxTypes.WOOD.getPart(GearboxTypes.GearPart.GEAR);
//    public static final ItemStack STONEGEAR = GearboxTypes.STONE.getPart(GearboxTypes.GearPart.GEAR);
//    public static final ItemStack STEELGEAR = GearboxTypes.STEEL.getPart(GearboxTypes.GearPart.GEAR);
//    public static final ItemStack TUNGSTENGEAR = GearboxTypes.TUNGSTEN.getPart(GearboxTypes.GearPart.GEAR);
//    public static final ItemStack DIAMONDGEAR = GearboxTypes.DIAMOND.getPart(GearboxTypes.GearPart.GEAR);
//    public static final ItemStack BEDROCKGEAR = GearboxTypes.BEDROCK.getPart(GearboxTypes.GearPart.GEAR);
//    public static final ItemStack BEARING = GearboxTypes.STEEL.getPart(GearboxTypes.GearPart.BEARING);
//    public static final ItemStack SHAFTCORE = GearboxTypes.STEEL.getPart(GearboxTypes.GearPart.SHAFTCORE);
//    public static final ItemStack TUNGSTENSHAFTCORE = GearboxTypes.TUNGSTEN.getPart(GearboxTypes.GearPart.SHAFTCORE);
//    public static final ItemStack DIAMONDSHAFTCORE = GearboxTypes.DIAMOND.getPart(GearboxTypes.GearPart.SHAFTCORE);

    public static final ItemStack STEELBLOCK = RotaryBlocks.DECO.get().asItem().getDefaultInstance();
    public static final ItemStack ANTHRABLOCK = RotaryBlocks.DECO.get().asItem().getDefaultInstance();
    public static final ItemStack LONSBLOCK = RotaryBlocks.DECO.get().asItem().getDefaultInstance();
    public static final ItemStack SHIELDBLOCK = RotaryBlocks.DECO.get().asItem().getDefaultInstance();
    public static final ItemStack BEDINGOTBLOCK = RotaryBlocks.DECO.get().asItem().getDefaultInstance();
    public static final ItemStack COKEBLOCK = RotaryBlocks.DECO.get().asItem().getDefaultInstance();

}
