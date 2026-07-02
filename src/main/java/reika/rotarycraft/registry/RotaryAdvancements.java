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

import net.minecraft.advancements.AdvancementHolder;
import net.minecraft.advancements.AdvancementProgress;
import net.minecraft.resources.Identifier;
import net.minecraft.server.MinecraftServer;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import reika.rotarycraft.RotaryCraft;

import java.util.Locale;
import java.util.function.Supplier;

public enum RotaryAdvancements {

    RCUSEBOOK(1, 1, RotaryItems.HANDBOOK.get(), null, false),
    DUMBEXTRACTOR(1, -1, RotaryBlocks.DC_ENGINE.get(), null, false),
    MAKESTEEL(0, 0, RotaryItems.HSLA_STEEL_INGOT.get(), null, false),
    FAILSTEEL(1, 2, RotaryBlocks.HSLA_STEEL_BLOCK.get(), MAKESTEEL, false),
    //    WORKTABLE(-2, 1, MachineRegistry.WORKTABLE, MAKESTEEL, false),
    MAKEYEAST(2, -2, RotaryItems.YEAST.get(), MAKESTEEL, false),
    //    EXTRACTOR(2, 0, RotaryItems.GOLDOREFLAKES, MAKESTEEL, false),
//    PCB(0, 4, RotaryItems.PCB, MAKESTEEL, false),
    PUMP(-6, 0, MachineRegistry.PUMP, MAKESTEEL, false),
    //    GPR(-2, 4, MachineRegistry.GPR, PCB, false),
//    BORER(2, 6, MachineRegistry.BORER, PCB, false),
    JETFUEL(4, -4, RotaryItems.JET_FUEL_BUCKET.get(), MAKEYEAST, false), //make
    RECYCLE(4, -8, RotaryItems.HSLA_STEEL_SCRAP.get(), JETFUEL, false),
    JETENGINE(6, -4, RotaryBlocks.JET_ENGINE.get(), JETFUEL, true),
//    MAKERAILGUN(0, 8, MachineRegistry.RAILGUN, PCB, true),
    SUCKEDINTOJET(6, -8, Items.ROTTEN_FLESH, JETENGINE, false),
    BEDROCKBREAKER(-4, 2, RotaryItems.BEDROCK_DUST.get(), MAKESTEEL, false), //break bedrock with
    STEAMENGINE(-8, 0, RotaryBlocks.STEAM_ENGINE.get(), PUMP, false), //turn on
    STEELSHAFT(-2, -2, RotaryItems.HSLA_SHAFT.get(), MAKESTEEL, false), //make
    //    CVT(-2, -4, MachineRegistry.ADVANCEDGEARS.getCraftedMetadataProduct(1), STEELSHAFT, false), //make
    BEDROCKSHAFT(-4, 6, RotaryItems.BEDROCK_ALLOY_SHAFT.get(), BEDROCKBREAKER, false), //make
    //    BEDROCKTOOLS(-6, 2, RotaryItems.BEDPICK, BEDROCKBREAKER, false), //make
    JETCHICKEN(8, -4, Items.FEATHER, JETENGINE, false), //suck 50 chickens into jet engine
    JETFAIL(8, -2, Blocks.FIRE, JETENGINE, false), //cause violent failure
//    LIGHTFALL(8, -6, MachineRegistry.LIGHTBRIDGE, JETENGINE, false), //light bridge turns off, drops you to death
//    SPRINKLER(-6, -2, MachineRegistry.SPRINKLER, PUMP, false), //turn on
    FLOODLIGHT(-1, -1, MachineRegistry.FLOODLIGHT, MAKESTEEL, false), //turn on at Light 15
    //    DAMAGEGEARS(-4, -2, RotaryItems.GEARUNIT, STEELSHAFT, false),
//    DIAMONDGEARS(-4, -4, GearboxTypes.DIAMOND.getGearboxItem(8), DAMAGEGEARS, false), //make
//    MRADS32(2, -6, RotaryItems.ANGULAR_TRANSDUCER.get(), JETFUEL, true), //transmit power at 32Mrad/s
//    GIGAWATT(6, 0, Blocks.REDSTONE_BLOCK, JETENGINE, true), //transmit 1GW of power in one shaft w/o breaking
//    RAILDRAGON(2, 8, Blocks.DRAGON_EGG, MAKERAILGUN, true), //kill dragon with railgun
//    RAILKILLED(0, 10, new ItemStack(Items.SKELETON_SKULL, 1), MAKERAILGUN, false), //kill self with railgun
//    GRAVELGUN(0, -4, RotaryItems.GRAVEL_GUN, MAKESTEEL, false), //one hit kill with
    LANDMINE(2, 3, MachineRegistry.LANDMINE, MAKESTEEL, false), //step on
    //    NETHERHEATRAY(4, -2, MachineRegistry.HEATRAY, JETFUEL, true), //dig 500m with heat ray in nether
//    CUTKNOT(4, 6, RotaryItems.DRILL, BORER, true),
    //RAREEXTRACT(4, 0, ExtractorModOres.getFlakeProduct(ModOreList.PLATINUM), EXTRACTOR, true),
//    MASSIVEHIT(0, -8, Items.FLINT, GRAVELGUN, true),
    OVERPRESSURE(-8, 2, MachineRegistry.COOLINGFIN, STEAMENGINE, false),
//    DOUBLEKILL(-2, -6, Items.ARROW, GRAVELGUN, true),
//    INSANITY(2, 2, MachineRegistry.EXTRACTOR, EXTRACTOR, true),
//    INSTANTBED(-6, 4, MachineRegistry.BEDROCKBREAKER, BEDROCKBREAKER, true),
//    PULSEFIRE(5, -5, MachineRegistry.PULSEJET, JETFUEL, false),
    ;
    public static final RotaryAdvancements[] list = values();

    public final RotaryAdvancements dependency;
    public final int xPosition;
    public final int yPosition;
    public final boolean isSpecial;
    // Deferred so the enum can be class-loaded during datagen before item components are bound
    // (a {@code new ItemStack(...)} at static-init time throws "Components not bound yet").
    private final Supplier<Item> iconSupplier;

    RotaryAdvancements(int x, int y, Item icon, RotaryAdvancements preReq, boolean special) {
        this(x, y, () -> icon, preReq, special);
    }

    RotaryAdvancements(int x, int y, Block icon, RotaryAdvancements preReq, boolean special) {
        this(x, y, () -> icon.asItem(), preReq, special);
    }

    RotaryAdvancements(int x, int y, MachineRegistry icon, RotaryAdvancements preReq, boolean special) {
        this(x, y, () -> icon.getBlockState().getBlock().asItem(), preReq, special);
    }

    RotaryAdvancements(int x, int y, Supplier<Item> icon, RotaryAdvancements preReq, boolean special) {
        xPosition = x;
        yPosition = y;
        dependency = preReq;
        iconSupplier = icon;
        isSpecial = special;
    }

    /** The advancement id this enum maps to (datagen emits {@code data/rotarycraft/advancement/<id>}). */
    public Identifier getId() {
        return Identifier.fromNamespaceAndPath(RotaryCraft.MODID, this.name().toLowerCase(Locale.ENGLISH));
    }

    /** Icon item, used by the datagen advancement provider for the display + has-item criterion. */
    public Item getIconItem() {
        return iconSupplier.get();
    }

    /**
     * 26.1: achievements are data-driven advancements (StatList / AchievementPage are gone). Code
     * "do X" advancements are emitted with an impossible criterion, so this grants them by awarding
     * every remaining criterion of the matching advancement to the player on the server.
     */
    public void triggerAchievement(Player ep) {
        if (!ConfigRegistry.ACHIEVEMENTS.getState())
            return;
        if (!(ep instanceof ServerPlayer sp))
            return;
        MinecraftServer server = sp.level().getServer();
        if (server == null)
            return;
        AdvancementHolder adv = server.getAdvancements().get(this.getId());
        if (adv == null)
            return;
        AdvancementProgress progress = sp.getAdvancements().getOrStartProgress(adv);
        if (progress.isDone())
            return;
        for (String criterion : progress.getRemainingCriteria())
            sp.getAdvancements().award(adv, criterion);
    }

    public boolean hasDependency() {
        return dependency != null;
    }

}


