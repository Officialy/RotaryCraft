package reika.rotarycraft.data;

import net.minecraft.client.data.models.BlockModelGenerators;
import net.minecraft.client.data.models.ItemModelGenerators;
import net.minecraft.client.data.models.ItemModelOutput;
import net.minecraft.client.data.models.ModelProvider;
import net.minecraft.client.data.models.MultiVariant;
import net.minecraft.client.data.models.blockstates.BlockModelDefinitionGenerator;
import net.minecraft.client.data.models.blockstates.MultiVariantGenerator;
import net.minecraft.client.data.models.blockstates.PropertyDispatch;
import net.minecraft.client.data.models.model.ItemModelUtils;
import net.minecraft.client.data.models.model.ModelInstance;
import net.minecraft.client.data.models.model.ModelLocationUtils;
import net.minecraft.client.data.models.model.ModelTemplates;
import net.minecraft.client.data.models.model.TextureMapping;
import net.minecraft.client.data.models.model.TextureSlot;
import net.minecraft.client.renderer.block.dispatch.Variant;
import net.minecraft.client.renderer.block.dispatch.VariantMutator;
import net.minecraft.client.resources.model.sprite.Material;
import net.minecraft.core.Direction;
import net.minecraft.core.Holder;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.data.PackOutput;
import net.minecraft.resources.Identifier;
import net.minecraft.util.random.Weighted;
import net.minecraft.util.random.WeightedList;
import net.minecraft.world.item.Item;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.properties.BlockStateProperties;
import net.minecraft.world.level.block.state.properties.EnumProperty;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.Tests;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.base.blocks.entity.BlockBlastFurnace;
import reika.rotarycraft.base.blocks.entity.BlockFermenter;
import reika.rotarycraft.base.blocks.entity.BlockMiningPipe;
import reika.rotarycraft.base.blocks.entity.pipe.BlockPipeShell;
import reika.rotarycraft.base.blocks.entity.BlockWorktable;
import reika.rotarycraft.base.blocks.CanolaBlock;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;
import reika.rotarycraft.renders.item.MachineItemRenderer;

import java.lang.reflect.Field;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.stream.Stream;

/**
 * 26.1 model + blockstate provider for RotaryCraft.
 * <p>
 * Vanilla's {@link BlockModelGenerators} keeps every useful single-block helper
 * ({@code createTrivialCube}, {@code registerSimpleItemModel}, etc.) {@code private}, so a
 * subclass can't call them. Rather than copy-paste each helper or use a custom DataProvider
 * that bypasses the model-codec serialisation, we reflectively grab the three sink fields
 * ({@code blockStateOutput}, {@code itemModelOutput}, {@code modelOutput}) that vanilla feeds
 * and reuse the public {@link ModelTemplates} / {@link MultiVariantGenerator} /
 * {@link ItemModelUtils} helpers to emit:
 * <ul>
 *   <li>Per block: a {@code cube_all} block model + a single-variant blockstate + a block-item
 *       model that inherits the block model.</li>
 *   <li>Per non-block item: a flat ({@code item/generated}) model with a single layer0 texture.</li>
 * </ul>
 * Machine items that should render through the {@code rotarycraft:machine} SpecialModelRenderer
 * codec (see {@code RotaryClientExtensions}) can have their generated {@code items/&lt;name&gt;.json}
 * hand-overridden after datagen runs; the default the provider emits is a sensible cube
 * fallback that at least doesn't show as missing.
 */
public class RoCModelProvider extends ModelProvider {

    public RoCModelProvider(PackOutput output) {
        super(output, RotaryCraft.MODID);
    }

    @Override
    @SuppressWarnings("unchecked")
    protected void registerModels(BlockModelGenerators blockModels, ItemModelGenerators itemModels) {
        Consumer<BlockModelDefinitionGenerator> blockStateOut;
        ItemModelOutput itemModelOut;
        BiConsumer<Identifier, ModelInstance> modelOut;
        try {
            Field bsf = BlockModelGenerators.class.getDeclaredField("blockStateOutput");
            bsf.setAccessible(true);
            blockStateOut = (Consumer<BlockModelDefinitionGenerator>) bsf.get(blockModels);

            Field imf = BlockModelGenerators.class.getDeclaredField("itemModelOutput");
            imf.setAccessible(true);
            itemModelOut = (ItemModelOutput) imf.get(blockModels);

            Field mof = BlockModelGenerators.class.getDeclaredField("modelOutput");
            mof.setAccessible(true);
            modelOut = (BiConsumer<Identifier, ModelInstance>) mof.get(blockModels);
        } catch (ReflectiveOperationException e) {
            throw new IllegalStateException(
                    "Failed to reflectively access BlockModelGenerators sinks — vanilla shape changed?", e);
        }

        // Track Items handled via the block iteration so the standalone-item pass doesn't
        // double-register an item model with the same id (e.g. `canola_seeds` is both a Block
        // with an auto-generated BlockItem AND an explicit standalone Item entry).
        Set<Item> blockItemsHandled = new HashSet<>();

        // ---- BLOCKS ----
        for (var holder : RotaryBlocks.BLOCKS.getEntries()) {
            Block block = holder.get();

            // Hand-written assets (multi-stage crops, multi-part models, etc.) live under
            // src/main/resources and would clash on processResources if we also emit a generated
            // file. Skip the block here — the BlockItem is also hand-modelled in those cases.
            if (block instanceof CanolaBlock) continue;

            // Pipe-shell blocks (fluid pipe, hose, fuel line, separation, suction, bedrock pipe) have a
            // hand-authored per-type multipart BLOCKSTATE (assets/rotarycraft/blockstates/<type>.json)
            // restoring the 1.7.10 per-material shell look (steel / planks / obsidian / lapis /
            // nether-brick / bedrock — see BlockPipeShell) using hand models under
            // models/block/pipe/<type>/{core,arm_*}.json. The CONN_* state they expose is generated by
            // BlockPipeShell itself, not here, so skip the blockStateOut emission entirely (the hand
            // file is authoritative — emitting our own would conflict). Point blockModelId at the
            // type's core-frame model and fall through to the normal item-icon code below, so the
            // inventory icon (and ModelProvider's own missing-model validation) both resolve correctly
            // instead of vanilla's auto-fallback guessing a nonexistent block/<name> model.
            boolean isPipeShell = block instanceof BlockPipeShell;

            // Pick a model template per block class. Most machines use the cube_all fallback —
            // the visible visuals come from the rotarycraft:machine SpecialModelRenderer codec
            // (see RotaryClientExtensions) rather than a vanilla model. A handful of blocks
            // legitimately need per-face textures because their renderer is the cube model.
            //   - BlockBlastFurnace: legacy "blastfurn_front" / "blastfurn_side" textures →
            //     CUBE_ORIENTABLE (top = side).
            //   - BlockWorktable: legacy "worktable_top" / "worktable_bottom" / "worktable" →
            //     CUBE_BOTTOM_TOP.
            //   - Everything else: cube_all pointing at block/<name>.png, generated fresh from
            //     the block's registry id.
            Identifier blockModelId;
            if (isPipeShell) {
                blockModelId = Identifier.fromNamespaceAndPath(RotaryCraft.MODID,
                        "block/pipe/" + BuiltInRegistries.BLOCK.getKey(block).getPath() + "/core");
            } else if (block instanceof BlockBlastFurnace) {
                blockModelId = ModelTemplates.CUBE_ORIENTABLE.create(
                        block, orientableMapping("blastfurn_front", "blastfurn_side", "blastfurn_side"), modelOut);
            } else if (block instanceof BlockFermenter) {
                // legacy static-face fermenter: steel sides, ferm_front / ferm_back, ferm_side top
                blockModelId = ModelTemplates.CUBE_ORIENTABLE.create(
                        block, orientableMapping("ferm_front", "ferm_side", "ferm_side"), modelOut);
            } else if (block instanceof BlockWorktable) {
                blockModelId = ModelTemplates.CUBE_BOTTOM_TOP.create(
                        block, bottomTopMapping("worktable_top", "worktable_bottom", "worktable"), modelOut);
            } else if (block instanceof reika.rotarycraft.base.blocks.entity.BlockCreativeCoil) {
                // Creative coil: no bespoke art -- a distinctive vanilla gold_block cube signals its
                // creative/infinite nature at a glance.
                var gold = new Material(Identifier.fromNamespaceAndPath("minecraft", "block/gold_block"));
                blockModelId = ModelTemplates.CUBE_ALL.create(
                        block,
                        new TextureMapping()
                                .put(TextureSlot.ALL, gold)
                                .put(TextureSlot.PARTICLE, gold),
                        modelOut);
            } else if (isPipe(block)) {
                // RotaryCraft's per-type pipe textures (block/fluid_pipe.png etc.) don't exist —
                // the legacy assets used a single piping.png spritesheet that the BER sliced into
                // connection-aware quads. Until the pipe BER is rewritten against the new
                // SubmitNodeCollector pipeline, fall back to a plain cube model using vanilla
                // {@code iron_block} as the texture so pipes render as visible grey cubes instead
                // of the missing-texture purple/black. Each pipe variant still picks its own
                // colour-ish placeholder so they're at least distinguishable at a glance.
                var placeholder = new Material(pipePlaceholderTexture(block));
                blockModelId = ModelTemplates.CUBE_ALL.create(
                        block,
                        new TextureMapping()
                                .put(TextureSlot.ALL, placeholder)
                                .put(TextureSlot.PARTICLE, placeholder),
                        modelOut);
            } else {
                // assets/<modid>/models/block/<name>.json (parent=cube_all, texture=block/<name>).
                blockModelId = ModelTemplates.CUBE_ALL.create(
                        block, TextureMapping.cube(block), modelOut);
            }

            MultiVariant single = singleVariant(blockModelId);
            // Pick the blockstate dispatch based on the actual Block subclass:
            //  - BlockMiningPipe and any HorizontalDirectionalBlock-shaped block: 4-way Y rotation.
            //  - BlockRotaryCraftMachine (full 6-direction FACING, applies to every machine /
            //    shaft / floodlight / etc. in the mod): 6-way rotation (Y rotation for the four
            //    horizontals, X rotation for UP/DOWN). Vertical machines and horizontals both go
            //    through this branch — the state still carries FACING even when getStateForPlacement
            //    refuses to assign UP/DOWN, so the blockstate JSON must enumerate them anyway.
            //  - Everything else: single fallback variant (cube_all looks identical from every angle
            //    so rotation is moot).
            if (!isPipeShell) {
                MultiVariantGenerator gen;
                if (block instanceof BlockMiningPipe) {
                    gen = MultiVariantGenerator.dispatch(block, single)
                            .with(horizontalFacingDispatch(BlockStateProperties.HORIZONTAL_FACING));
                } else if (block instanceof BlockRotaryCraftMachine) {
                    gen = MultiVariantGenerator.dispatch(block, single)
                            .with(sixWayFacingDispatch(BlockStateProperties.FACING));
                } else {
                    gen = MultiVariantGenerator.dispatch(block, single);
                }
                blockStateOut.accept(gen);
            }

            // Block-as-item model. If the block maps to a MachineRegistry entry that has a
            // registered RotaryModelBase factory, we route the item through the
            // {@code rotarycraft:machine} SpecialModelRenderer codec (see RotaryClientExtensions).
            // That makes the in-inventory / in-hand icon use the BE's actual TESR model rather
            // than the cube_all fallback that references a non-existent {@code block/<name>.png}.
            //
            // For everything else (decoratives, BlockBlastFurnace which uses real per-face
            // textures, etc.) we keep the plain block-model passthrough.
            Item asItem = block.asItem();
            if (asItem != Items.AIR) {
                MachineRegistry mr = MachineRegistry.getMachineMapping(block);
                if (mr != null && mr.hasModel() && mr.getModel() != null) {
                    itemModelOut.accept(asItem, ItemModelUtils.specialModel(
                            blockModelId,
                            new MachineItemRenderer.Unbaked(mr.name().toLowerCase(Locale.ROOT))
                    ));
                } else {
                    itemModelOut.accept(asItem, ItemModelUtils.plainModel(blockModelId));
                }
                blockItemsHandled.add(asItem);
            }
        }

        // ---- STANDALONE ITEMS ----
        // Iterates BOTH RotaryItems.ITEMS (real items) AND Tests.ITEMS (the test_item debug entry)
        // so validation against BuiltInRegistries.ITEM doesn't trip on something unhandled.
        Stream.concat(
                RotaryItems.ITEMS.getEntries().stream(),
                Tests.ITEMS.getEntries().stream()
        ).forEach(holder -> {
            Item item = holder.get();
            // Skip standalone-item registration if a BlockItem with the same Item instance was
            // already emitted in the block pass — ModelProvider's ItemInfoCollector rejects
            // duplicate keys (e.g. `canola_seeds` exists both as a Block and as a separate Item).
            if (blockItemsHandled.contains(item)) return;
            // assets/<modid>/models/item/<name>.json (parent=item/generated, layer0=item/<name>).
            Identifier itemModelId = ModelTemplates.FLAT_ITEM.create(
                    ModelLocationUtils.getModelLocation(item),
                    TextureMapping.layer0(item),
                    modelOut);
            itemModelOut.accept(item, ItemModelUtils.plainModel(itemModelId));
        });
    }

    /**
     * Pipe detection. The block path doesn't tell us much directly, but every pipe block in
     * RotaryCraft maps to a {@link MachineRegistry} entry whose {@code isPipe()} returns true
     * (it's a class-level check against {@code BlockEntityPiping}). One lookup, no per-class
     * casts.
     */
    private static boolean isPipe(Block block) {
        MachineRegistry m = MachineRegistry.getMachineMapping(block);
        return m != null && m.isPipe();
    }

    /**
     * Picks a vanilla block texture per pipe variant so each kind of pipe is at least visually
     * distinct from the others even though we don't have proper RotaryCraft pipe textures.
     * The map below tracks the {@link MachineRegistry} entry rather than the block class so a
     * single switch covers all eight pipe types.
     */
    private static Identifier pipePlaceholderTexture(Block block) {
        MachineRegistry m = MachineRegistry.getMachineMapping(block);
        String vanilla = switch (m) {
            case HOSE       -> "block/black_wool";       // lubricant — dark coil look
            case FUELLINE   -> "block/red_wool";         // jet fuel — bright red, "hot"
            case VALVE      -> "block/redstone_block";   // valve toggles flow — redstone-ish
            case BYPASS     -> "block/gold_block";       // bypass — premium routing
            case SEPARATION -> "block/diamond_block";    // fluid-separating pipe — distinctive
            case SUCTION    -> "block/copper_block";     // pulls fluid in
            case BEDPIPE    -> "block/bedrock";          // bedrock pipe — literal bedrock
            default         -> "block/iron_block";       // PIPE (fluid_pipe) and fallback
        };
        return Identifier.withDefaultNamespace(vanilla);
    }

    /** A one-variant {@link MultiVariant} (weight = 1) pointing at the given model id. */
    private static MultiVariant singleVariant(Identifier modelId) {
        // WeightedList.of(E) wraps the value as a weight-1 Weighted internally — avoids the
        // varargs/single-arg overload ambiguity that bites when E itself is Weighted<...>.
        return new MultiVariant(WeightedList.of(new Variant(modelId)));
    }

    /**
     * Builds a {@link Material} pointing at
     * {@code rotarycraft:block/<base>}. The atlas is implicit (block atlas) — 26.1's
     * datagen-side Material is just {@code (sprite, forceTranslucent=false)}.
     */
    private static Material texture(String base) {
        return new Material(
                Identifier.fromNamespaceAndPath(RotaryCraft.MODID, "block/" + base));
    }

    /**
     * Builds a TextureMapping for {@link ModelTemplates#CUBE_ORIENTABLE}. Slots in 26.1's
     * orientable template: TOP / FRONT / SIDE — used for legacy machines like the blast furnace
     * that have explicit front and side textures and a side-as-top variant.
     */
    private static TextureMapping orientableMapping(String front, String side, String top) {
        return new TextureMapping()
                .put(TextureSlot.FRONT, texture(front))
                .put(TextureSlot.SIDE, texture(side))
                .put(TextureSlot.TOP, texture(top));
    }

    /**
     * Builds a TextureMapping for {@link ModelTemplates#CUBE_BOTTOM_TOP}. Slots: TOP / BOTTOM /
     * SIDE — used by the worktable (distinct top / bottom textures + the legacy "worktable.png"
     * side texture).
     */
    private static TextureMapping bottomTopMapping(String top, String bottom, String side) {
        return new TextureMapping()
                .put(TextureSlot.TOP, texture(top))
                .put(TextureSlot.BOTTOM, texture(bottom))
                .put(TextureSlot.SIDE, texture(side));
    }

    /**
     * Y-rotation dispatch for the four horizontal {@link Direction} values (used by blocks that
     * only place along the cardinal axes — e.g. {@link BlockMiningPipe}).
     *
     * <p>Direction → rotation mapping mirrors what vanilla furnaces / dispensers emit:
     * {@code NORTH=0°, EAST=90°, SOUTH=180°, WEST=270°}.
     */
    private static PropertyDispatch<VariantMutator> horizontalFacingDispatch(
            EnumProperty<Direction> property) {
        return PropertyDispatch.modify(property)
                .select(Direction.NORTH, BlockModelGenerators.NOP)
                .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
                .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
                .select(Direction.WEST, BlockModelGenerators.Y_ROT_270);
    }

    /**
     * Y + X rotation dispatch for full 6-direction {@link Direction} values (every
     * {@link BlockRotaryCraftMachine} subclass). UP/DOWN use an X rotation so the model's "front"
     * face points up / down respectively — matches the Y-rotation convention vanilla pistons use.
     */
    private static PropertyDispatch<VariantMutator> sixWayFacingDispatch(
            EnumProperty<Direction> property) {
        return PropertyDispatch.modify(property)
                .select(Direction.NORTH, BlockModelGenerators.NOP)
                .select(Direction.EAST, BlockModelGenerators.Y_ROT_90)
                .select(Direction.SOUTH, BlockModelGenerators.Y_ROT_180)
                .select(Direction.WEST, BlockModelGenerators.Y_ROT_270)
                .select(Direction.UP, BlockModelGenerators.X_ROT_270)
                .select(Direction.DOWN, BlockModelGenerators.X_ROT_90);
    }

    /**
     * Validation set — every block from this mod. Base provider then asserts each received a
     * blockstate definition during {@link #registerModels}.
     */
    @Override
    protected Stream<? extends Holder<Block>> getKnownBlocks() {
        // CanolaBlock has hand-written age-stage blockstate / model JSONs in src/main/resources;
        // datagen would otherwise overwrite them with a single cube_all variant. Exclude it from
        // both emit (see registerModels) and validation here.
        return BuiltInRegistries.BLOCK.listElements()
                .filter(h -> h.getKey().identifier().getNamespace().equals(RotaryCraft.MODID))
                .filter(h -> !(h.value() instanceof CanolaBlock))
                .filter(h -> !(h.value() instanceof BlockPipeShell));
    }

    /**
     * Validation set — every item from this mod (standalone items plus the block-items
     * auto-generated by {@code DeferredRegister.Blocks.registerSimpleBlockItem}).
     */
    @Override
    protected Stream<? extends Holder<Item>> getKnownItems() {
        return BuiltInRegistries.ITEM.listElements()
                .filter(h -> h.getKey().identifier().getNamespace().equals(RotaryCraft.MODID));
    }
}
