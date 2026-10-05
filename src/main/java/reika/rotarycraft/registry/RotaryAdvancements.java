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

import net.minecraft.resources.Identifier;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import reika.rotarycraft.RotaryCraft;

import java.util.Locale;
import java.util.function.Supplier;

public enum RotaryAdvancements {

    RCUSEBOOK(1, 1, () -> RotaryItems.HANDBOOK.get(), null, false),
    DUMBEXTRACTOR(1, -1, () -> RotaryBlocks.DC_ENGINE.get().asItem(), null, false),
    MAKESTEEL(0, 0, () -> RotaryItems.HSLA_STEEL_INGOT.get(), null, false),
    FAILSTEEL(1, 2, () -> RotaryBlocks.HSLA_STEEL_BLOCK.get().asItem(), MAKESTEEL, false),
    WORKTABLE(-2, 1, () -> MachineRegistry.WORKTABLE.getBlockState().getBlock().asItem(), MAKESTEEL, false),
    MAKEYEAST(2, -2, () -> RotaryItems.YEAST.get(), MAKESTEEL, false),
    EXTRACTOR(2, 0, () -> RotaryItems.GOLD_FLAKES.get(), MAKESTEEL, false),
    PCB(0, 4, () -> RotaryItems.CIRCUIT_BOARD.get(), MAKESTEEL, false),
    PUMP(-6, 0, () -> MachineRegistry.PUMP.getBlockState().getBlock().asItem(), MAKESTEEL, false),
    GPR(-2, 4, () -> MachineRegistry.GPR.getBlockState().getBlock().asItem(), PCB, false),
    BORER(2, 6, () -> MachineRegistry.BORER.getBlockState().getBlock().asItem(), PCB, false),
    JETFUEL(4, -4, () -> RotaryItems.JET_FUEL_BUCKET.get(), MAKEYEAST, false),
    RECYCLE(4, -8, () -> RotaryItems.HSLA_STEEL_SCRAP.get(), JETFUEL, false),
    JETENGINE(6, -4, () -> RotaryBlocks.JET_ENGINE.get().asItem(), JETFUEL, true),
    MAKERAILGUN(0, 8, () -> MachineRegistry.RAILGUN.getBlockState().getBlock().asItem(), PCB, true),
    SUCKEDINTOJET(6, -8, () -> Items.ROTTEN_FLESH, JETENGINE, false),
    BEDROCKBREAKER(-4, 2, () -> RotaryItems.BEDROCK_DUST.get(), MAKESTEEL, false),
    STEAMENGINE(-8, 0, () -> RotaryBlocks.STEAM_ENGINE.get().asItem(), PUMP, false),
    STEELSHAFT(-2, -2, () -> RotaryBlocks.HSLA_SHAFT.get().asItem(), MAKESTEEL, false),
    CVT(-2, -4, () -> RotaryBlocks.CVT.get().asItem(), STEELSHAFT, false),
    BEDROCKSHAFT(-4, 6, () -> RotaryBlocks.BEDROCK_SHAFT.get().asItem(), BEDROCKBREAKER, false),
    BEDROCKTOOLS(-6, 2, () -> RotaryItems.BEDROCK_ALLOY_PICK.get(), BEDROCKBREAKER, false),
    JETCHICKEN(8, -4, () -> Items.FEATHER, JETENGINE, false),
    JETFAIL(8, -2, () -> Items.FLINT_AND_STEEL, JETENGINE, false),
    LIGHTFALL(8, -6, () -> MachineRegistry.LIGHTBRIDGE.getBlockState().getBlock().asItem(), JETENGINE, false),
    SPRINKLER(-6, -2, () -> MachineRegistry.SPRINKLER.getBlockState().getBlock().asItem(), PUMP, false),
    FLOODLIGHT(-1, -1, () -> MachineRegistry.FLOODLIGHT.getBlockState().getBlock().asItem(), MAKESTEEL, false),
    DAMAGEGEARS(-4, -2, () -> RotaryItems.HSLA_STEEL_GEAR_2x.get(), STEELSHAFT, false),
    DIAMONDGEARS(-4, -4, () -> RotaryBlocks.DIAMOND_GEARBOX_8x.get().asItem(), DAMAGEGEARS, false),
    MRADS32(2, -6, () -> RotaryItems.ANGULAR_TRANSDUCER.get(), JETFUEL, true),
    GIGAWATT(6, 0, () -> Blocks.REDSTONE_BLOCK.asItem(), JETENGINE, true),
    RAILDRAGON(2, 8, () -> Blocks.DRAGON_EGG.asItem(), MAKERAILGUN, true),
    RAILKILLED(0, 10, () -> Items.SKELETON_SKULL, MAKERAILGUN, false),
    GRAVELGUN(0, -4, () -> RotaryItems.GRAVELGUN.get(), MAKESTEEL, false),
    LANDMINE(2, 3, () -> MachineRegistry.LANDMINE.getBlockState().getBlock().asItem(), MAKESTEEL, false),
    NETHERHEATRAY(4, -2, () -> MachineRegistry.HEATRAY.getBlockState().getBlock().asItem(), JETFUEL, true),
    GPRSPAWNER(-2, 6, () -> Blocks.SPAWNER.asItem(), GPR, true),
    GPRENDPORTAL(-2, 8, () -> Blocks.END_PORTAL_FRAME.asItem(), GPRSPAWNER, true),
    CUTKNOT(4, 6, () -> RotaryItems.HSLA_DRILL.get(), BORER, true),
    RAREEXTRACT(4, 0, () -> ModExtractOres.PLATINUM.stage(3), EXTRACTOR, true),
    MASSIVEHIT(0, -8, () -> Items.FLINT, GRAVELGUN, true),
    OVERPRESSURE(-8, 2, () -> MachineRegistry.COOLINGFIN.getBlockState().getBlock().asItem(), STEAMENGINE, false),
    DOUBLEKILL(-2, -6, () -> Items.ARROW, GRAVELGUN, true),
    INSANITY(2, 2, () -> MachineRegistry.EXTRACTOR.getBlockState().getBlock().asItem(), EXTRACTOR, true),
    INSTANTBED(-6, 4, () -> MachineRegistry.BEDROCKBREAKER.getBlockState().getBlock().asItem(), BEDROCKBREAKER, true),
    PULSEFIRE(5, -5, () -> MachineRegistry.PULSEJET.getBlockState().getBlock().asItem(), JETFUEL, false);

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
     * 26.3: achievements are data-driven advancements (StatList / AchievementPage are gone). Code
     * "do X" advancements are emitted with an impossible criterion, so this grants them by awarding
     * every remaining criterion of the matching advancement to the player on the server.
     */
    public void triggerAchievement(Player ep) {
        if (!ConfigRegistry.ACHIEVEMENTS.getState())
            return;
        reika.dragonapi.libraries.AdvancementHelper.grant(ep, this.getId());
    }

    public boolean hasDependency() {
        return dependency != null;
    }

}


