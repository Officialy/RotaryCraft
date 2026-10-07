# RotaryCraft survival beta ledger — Minecraft 26.3

Updated 2026-10-01. Target: NeoForge 26.3.0.26-beta, Java 25.

## This slice

- Fully restore the Steel Purifier: block/entity/menu/screen, handbook, thermal behavior,
  summative shaft power, datapack steel conversion, crafting recipe, generated loot/models/lang,
  JEI and recipe network synchronization. Original conversion consumes one ingot from each
  occupied valid input slot, with gunpowder and sand consumed probabilistically per batch.
  Common `c:ingots/steel` accepts foreign steel; HSLA is deliberately not added to that tag.
- Preserve existing MachineRegistry indices by appending PURIFIER.
- Fix Winder constructing an Aerosolizer instead of its own block entity.
- Restore shared lifecycle calls in Sprinkler, ECU, Power Bus, Drying Bed and Ground Hydrator.
- Restore Compactor power-input direction and write back grown output stacks, preventing
  ingredient loss when an output stack already exists. Restore V33a environmental heat/cooling,
  world ambient pressure, slower Nether pressure decay, failure behavior, thermal damage,
  comparator/status gates and friction-heater settings. Correct the original snow-melt direction.
- Fix Beam Mirror clearing the machine position rather than its old light positions, skipping
  the first beam block and treating dynamic shape as opacity. Solid stone now stops the beam.
- Fix Music Box score filenames and server-side deletion; score files belong to the current
  world's RotaryCraft directory, matching the original world-save ownership.
- Restore the original four-disc and four-pattern crafting yields while retaining max-stack-one
  storage. Bulk shaped recipes load from datagen, synchronize to clients and expose valid recipe-book
  display templates. A targeted crafting-table mixin transfers complete batches across inventory
  slots and refuses partial capacity before consuming ingredients; ordinary recipes use vanilla code.
- Restore Drying Bed output writeback, original bottom-fluid-input rejection, inventory persistence,
  live capability identity across reload, progress dirty tracking and survival break drops.
  Inventory serialization retains the ValueInput registry context before a newly loaded entity has a level.
- Restore handbook MODINTERFACE arguments for RF conversion and the original Extractor rates.

## GameTest coverage

The suite registers one isolated placement/type/lifecycle test for each of the 139 active
MachineRegistry entries, plus a registry-wide invariant. Variants account for some entries;
this count is not a count of distinct V33a machines. Tests tick real placed blocks rather than
calling processing methods with fabricated power. Creative coils supply deterministic shaft
power for powered functional tests.

Additional functional coverage in this slice:

- Purifier: full/partial batches, output capacity and incompatible output, missing power/heat/
  catalysts, sided item transactions and rollback, save/reload and capability rebinding,
  thermal surroundings, menu/shift-click and 64-bit power sync, survival break drops, network
  recipe serialization.
- Compactor: repeated production into an existing stack, whole-result backpressure, power/
  heat/pressure/matching-input gates, environmental heat/cooling and pressure decay.
- Grinder: repeated production into an existing stack.
- Centrifuge: repeated deterministic item separation and fluid extraction rollback/commit.
- Beam Mirror: contiguous beam, solid obstruction, obstruction-triggered cleanup preserving mirror.
- Music Box: server score save/read and removal when broken.
- Crafting: two bulk batches, ordinary pickup, full/partial inventory backpressure, unchanged vanilla
  shift-crafting, recipe network serialization, result-slot stack serialization and valid display icons.
- Drying Bed: eight batches into existing output, output backpressure/fluid conservation, detached
  entity loading, saved progress, sided fluid/item transactions and survival inventory drops.
- Ground Hydrator: actual farmland irrigation with complete 25 mB doses and insufficient-water rejection.

Run `gradlew :RotaryCraft:runGameTest --offline`. The separate gameTestFixtures resource set
adds iron to the common steel tag only for this run. Release jars must contain the generated
empty steel tag, never the iron fixture.

Placement/lifecycle coverage is a safety baseline. Most machines still need stronger tests of
production, automation, survival interactions, reload, networking and failure limits. Passing
this suite does not establish end-to-end survival readiness.

## Remaining machines

The owner's starting report listed 25 missing V33a machines. Purifier, Distiller, Terraformer
and the separate AutoCrafting Unit and Item Filter ports, plus Chunk Loader and Fuel Enhancer,
are restored, leaving 18 from that report:
Bait Box, Bundled Bus, CCTV, Compressor,
Defoliator, Display, Electric Motor, Flame Turret, Fuel Engine,
Generator, Mob Radar, Pile Driver, Pneumatic Engine, Portal Shaft, Projector,
Screen, Sonic Weapon and Spy Cam. Audit their pristine source and dependencies
before selecting the next complete port. Do not stub integrations to enable a compile.

## Validation on 2026-09-30

- All 215 required RotaryCraft GameTests pass against the updated World Rift/fluid-source work.
  All 11 RotaryCraft JUnit tests pass. All six mod release builds pass offline.
- The release RotaryCraft jar contains Purifier recipes/resources, the empty common steel tag,
  bulk recipes and their crafting mixin. The iron-as-steel GameTest fixture is excluded.
- The six release jars plus local TerraBlender boot in an installed production NeoForge dedicated
  server outside Gradle (20.115 seconds on the existing verification world), and save/stop cleanly.
  The isolated server binds 127.0.0.1:25575, uses offline authentication for this check, and contains
  neither c2me nor DH. Windows OSHI counter errors during process diagnostics are unrelated to mod loading.
- A real six-mod development client logged in over the network and loaded the world and JEI.
  The initial whitelist rejection was corrected using the offline Dev UUID. This verifies actual
  networking, not just fake-player GameTests. The same client reconnected after a production
  server restart; saved Purifier and Drying Bed block entities loaded and ticked correctly.
  External release-client installation remains unverified.

## Beta acceptance still required

- Verify an external client installation using identical release jars and authenticated multiplayer.
- Exercise the full survival crafting/progression chain with two real players.
- Expand functional GameTests machine by machine and play the early-to-late progression.
- Check models, GUIs, particles and audio in a fresh-world client.

Use identical six-mod jars and local TerraBlender 26.3.0.0.7-local.1 on server and clients.
Keep c2me off the server. DH is optional and client-only. ChromatiCraft implementation remains
for the separate later agent; its existing port is built for stack compatibility only.


## ReactorCraft reactor-type verification (2026-09-30)

ReactorCraft now passes 34 required GameTests covering all six generating types, including its
sodium solar tower integration. RotaryCraft solar plant discovery, primary-column selection,
nearest-tower mirror aiming, legacy light weighting and safe invalidation were restored from the
original source. Tower fluid pooling retains capacity and the receiver interface now identifies
the integration's cold sodium, allowing ReactorCraft's return loop to feed the tower.

The solar tests require actual mirror-field generation; creative coils power the auxiliary
exchangers. Shared DragonAPI air-exposure checks now treat modelled iron piping according to
its motion tags and collision shape. All 215 RotaryCraft GameTests and 11 JUnit checks still
pass, as do the six release builds. See ReactorCraft/PORTING.md for the full coverage boundary.


## Distiller and ReactorCraft survival content (2026-09-30)

- Fully ported the Distiller with bottom shaft power, 6000 mB input/output tanks, one complete
  conversion per six ticks, persisted cadence, sided transactional fluid capabilities, pipe/hose/fuel-line
  integration, the original two-basin model and fluid volumes, special item renderer, handbook and JEI.
- Original V33a conversions are reloadable datapack recipes: oil -> lubricant (1:6, 2048 Nm/8192 W),
  bioethanol -> ethanol (1:1, 512 Nm/131072 W), biofuel -> ethanol (2:1, 512 Nm/131072 W).
  Input common fluid tags preserve optional mod integration; those foreign fluids are not supplied
  by the six-mod stack itself. Test-only tag additions stand in for the foreign fluids and never enter
  release jars. Recipe synchronization and live handbook/JEI views follow the loaded world data.
- Twelve new Distiller GameTests verify ratios, bottom-only power, torque/power gates, full or
  incompatible output, capacity limits, insertion/extraction rollback and commit, save/reload,
  actual hose transfer, survival harvest and recipe network round trips. Real creative coils power them.
- ReactorCraft now includes all eight decorative fluorite colours with original art, nine-gem
  compaction/uncrafting, wooden-pickaxe harvest, colour-preserving drops, neutron excitation/light
  and random-tick decay. The CPU remote is registered, craftable, coil-chargeable and persistent;
  its menu works independently of client chunk/dimension tracking and validates rod controls against
  the bound CPU. Temperature links persist dimensions and accept older coordinate-only saves.
- Verification on the current shared tree: 228/228 RotaryCraft and 49/49 ReactorCraft GameTests.
  ReactorCraft includes every previously tested reactor type and its sodium solar tower integration.
  Distiller visuals and the new remote screen still require a real-client visual check.

Release checks for this slice: RotaryCraft and ReactorCraft jars build, with 11 and 5 passing JUnit
checks. Jar inspection confirms new content and excludes the test-only fluid tag substitutions.
The shared six-mod build currently stops at the other agent's in-progress ChromatiCraft
ChromaBlocks reference to PushReaction.BLOCK; a fresh six-mod release set is therefore not yet verified.


## Terraformer (2026-09-30)

- Fully ported the Terraformer, original SsS/ici/PiP crafting recipe, steel cube with screen on
  top, 54 inventory slots, 24000 mB water tank, summative power, redstone gate, cadence, manual
  menu transfers, original automation restrictions, Tile Selector and handbook entry.
- All 36 directed V33a biome conversions are generated recipes. Eighteen source-family tags
  include modern counterparts of child biomes; cold/old-growth taiga and deep/frozen ocean remain
  separate families. GUI and JEI use synchronized loaded recipes, including power, water and
  original quantized ItemReq consumption rolls. Duplicate oak requirements are preserved.
- Modern biome storage uses 4x4 cells. Selection deduplicates cells independently of Y, and each
  completed cell pays all sixteen original column costs atomically. The entire vertical biome
  column is changed and vanilla resends affected chunk biomes. Unloaded work waits without loading
  chunks; queue, terrain footprint, target, inventory, water and cadence persist across saves.
- With a diamond and biome-block configuration enabled, original topsoil/filler replacement starts
  at Y=30, followed by registered target-biome feature generation. Feature writes are bounded to
  selected loaded terrain, honor break permissions and preserve block entities. Climate freezing
  and melting also apply without a diamond, as in the original environmental path.
- Neighboring area providers can implement TerraformerAreaProvider for inclusive marker rectangles;
  a BUILDCRAFT-PORT marker preserves the original IAreaProvider integration contract until a
  compatible BuildCraft API exists. The bridge is tested independently of BuildCraft installation.
- Fifty-one functional GameTests cover every conversion using real creative-coil power, resource
  gates and full-cell atomicity, selector behavior, child biomes, area import, persistence, fluid/item
  transactions, validated menu actions, manual transfers, soil, freezing, actual tree generation,
  loaded-area boundaries, survival inventory drops and recipe disk/network codecs.
- This slice still needs a real-client visual check of the Terraformer menu, biome colours and JEI.
  Headless tests do not verify real multiplayer biome packet delivery or third-party claim mods.

Validation: 51/51 Terraformer tests and 284/284 full RotaryCraft GameTests pass on the current
shared tree. Fresh compiler outputs are used because concurrent Gradle runs share incremental
compiler metadata. RotaryCraft release build and its 11 JUnit checks pass; final jar contents are
checked separately. Logs: build/terraformer-tests.log, build/terraformer-full-tests.log and
build/terraformer-release.log. Pending AE2 work is not included in the Terraformer commit.


## Chunk Loader and survival tags (2026-10-01)

- Fully ported the Chunk Loader: original bottom input, 2097152 rad/s speed threshold,
  zero base radius, one additional chunk of radius per 524288 W above the baseline, and
  configured enable/radius gates. The radius calculation retains long power and clamps
  inactive ranges to zero. Machine indices remain stable by appending CHUNKLOADER.
- Persistent NeoForge tickets belong to the machine position. Power changes reconcile the
  square, contraction releases outer chunks, overlapping loaders keep separate ownership,
  and any server-side removal releases tickets. DragonAPI restore validation now rebuilds
  its ownership cache so first-tick power loss also releases restored tickets. Runtime
  ownership caches clear at server shutdown while saved tickets remain on disk.
- Restored original bedrock-shaft/nether-star/16:1-bedrock-gear crafting, generated loot,
  block/item model definitions, rotating model, rising star/ender-pearl particles, handbook
  and notes. Render and particle code compile against the 26.3 client APIs; visuals still
  need an in-game client check.
- Audited block and item tags: machines and pipes receive pickaxe mining speed, and HSLA/
  bedrock tools, shears, armor, jetpacks, spring boots and goggles receive their equipment
  categories. Vanilla enchantment tag inheritance now recognizes those categories.
- Restored material tags for source-defined aluminum alloy/aluminum powder, silver,
  netherrack, soul-sand tar, coal, salt, sawdust, silicon, compost, coke and canola seeds;
  added pure tungsten compatibility and hardened-glass block/item tags. HSLA remains
  separate from ordinary steel and tungsten alloy remains separate from pure tungsten.
  No new harvest-tier restrictions are imposed on pipes or machine-specific drop rules.
- Ten Chunk Loader functional tests use real creative coils and inspect NeoForge ticket
  ownership independently of DragonAPI's cache. Five tag tests check live tag membership,
  actual pickaxe mining speed, equipment/enchantment inheritance, material distinctions
  and hardened-glass block/item parity. Registry-wide placement covers the new loader too.
- Validation: 301/301 RotaryCraft GameTests, 11/11 JUnit checks and the RotaryCraft release
  build passed. A final focused tag run covers the aluminum source-parity additions. Jar
  inspection confirms machine classes, generated recipe/model/mining/tool tags and excludes
  the foreign-steel/fluid GameTest substitutions.

## Fuel Enhancer (2026-10-01)

- Fully ported against the actual V33a `upstream/master` source rather than the older commented
  reference. The machine requires blaze powder, netherrack dust, tar, magma cream and pink dye.
  Bottom power must supply at least 16384 W and 16384 rad/s. Both tanks hold 5000 mB.
- Original petroleum-fuel and kerosene conversions are reloadable recipes. At minimum speed,
  each tick converts 4 mB into 1 mB of jet fuel. The output multiplier is
  `1 + floor(log2(omega / 16384)) / 2`, with integer division of the logarithm by two.
  Each catalyst is independently consumed once per conversion, irrespective of speed boost.
  Original difficulty multipliers remain live: normal difficulty gives 0.375% consumption
  per catalyst for petroleum fuel and 0.25% for kerosene.
- Common `c:fuel` and `c:kerosene` fluid tags provide the foreign-fluid integration boundary.
  The fuel tag includes optional BuildCraft IDs; kerosene is empty until an integration or
  datapack supplies a registered fluid. RotaryCraft does not invent or register those fuels.
  JEI shows only conversions whose input tag has a loaded fluid. Optional programmatic
  usability conditions and descriptions are retained on loaded recipe objects.
- Fluid input is top-only; output is horizontal and output pipe routing is fuel-line-only.
  All nine inventory slots accept recipe catalysts from every side. Automated extraction is
  denied, while manual retrieval and shift-click work. Fixed-size inventory storage keeps
  its capability identity and the loading caller's registry context. Vanilla Container reads
  expose the live stack, preventing hopper merge loss; transactional reads remain snapshots.
- Restored original gold/panel/glass-pane/mixer crafting, generated loot and block/item models,
  machine mining/motion tags, handbook notes, compact nine-slot GUI, full-width power sync,
  rotating paddles and the original stacked fluid basins. The block exposes the original
  comparator signal for empty input or full output. Machine indices stay stable by appending
  FUELENHANCER. Tank/power synchronization uses the shared periodic delta channel.
- Thirty-three functional GameTests cover both fuels, all five missing-catalyst gates, speed
  thresholds through Integer.MAX_VALUE, consecutive-tick cadence, power directions, whole-batch
  backpressure, incompatible output, fluid/item rollback and commit, real hopper insertion and
  extraction denial, detached loading, menu transfers and high power bits, actual comparator
  output, real fuel-line extraction, any-removal inventory drops, original crafting, recipe
  disk/network codecs, overlapping catalyst matching and conditional/zero/guaranteed consumption.
  Foreign-fluid substitutions and deterministic custom recipes exist only in GameTest resources.

Validation: 335/335 full RotaryCraft GameTests and 11/11 JUnit checks pass; release build passes.
Jar inspection verifies the machine, its generated recipes/models/loot/mining tags and exclusion
of every Fuel Enhancer fixture. Logs are `build/fuel-enhancer-final-validation.log` and
`build/fuel-enhancer-datagen.log`. This slice still needs client visual and multiplayer checks
for the new machine, and a real foreign-fuel integration test.


## Fuel Engine port (2026-10-01)

The Fuel Engine is ported against V33a's source: 256 rad/s and 2048 Nm (524288 W),
three independent 24000 mB tanks, eight-tick cold startup, 1 mB startup consumption,
4 mB per 36 firing ticks and 1 mB lubricant per 32 powered world ticks. Loss of fuel,
lubricant, atmosphere or ECU permission coasts down rather than instantly stopping.
All five ECU settings control speed and piston efficiency. Turbofuel retains V33a's
integer `5/2` expression, doubling the firing interval; it does not become a 2.5x bonus.

- Fuel enters beneath the engine (above when flipped); water and lubricant enter horizontally.
  Tank extraction is denied. Transaction rollback, mixed-fluid rejection, partial-capacity
  ECU transfer and full-bucket backpressure preserve fluids. Fuel lines accept the same common
  fuel tags as the engine. Real fuel lines, water pipes and hoses feed a powered engine in tests.
- Restore the original ECU transfer path, including ceiling-mounted engines and redstone
  sample hysteresis. Placement beneath a solid ceiling with open space below flips the engine.
- Thermal checks run every 20 ticks: water assists cooling, speed drops above 450 C with a
  16 rad/s floor, and exceeding 750 C removes the engine and dispatches strength-4 and strength-8
  fire/block explosions. The explosion test intercepts the two actual start events so adjacent
  GameTest structures remain isolated; blast damage and fire spread are not measured by it.
- Register the original aluminum-cylinder/tungsten-gear survival recipe, loot, motion and
  pickaxe tags, four-gauge menu, player inventory transfers, handbook notes, shift tooltip,
  original fuel-engine texture, animated crank/pistons, item renderer, smoke and diesel sound.
  Save/load preserves all three tanks, mechanical state, flipping and timer phase; a running
  engine resumes its next fuel burn at the saved phase without repaying cold-start fuel.
- DragonAPI exposes position-specific atmosphere/combustion/density/damage queries on the
  NeoForge event bus. Vacuum and restored atmosphere are tested. Advanced Rocketry and
  Galacticraft adapters remain explicitly marked for their absent modern APIs. Common
  `c:fuel` and `c:turbofuel` tags preserve external fuel integration without inventing fuel.
  Satisforestry's absent modern API adapter remains marked; its fluid must opt into turbofuel.
  The water/ethanol substitutions are test-only resources, excluded from release jars.

Validation: 43 Fuel Engine functional tests plus its registry placement test bring the full
suite to 379/379 RotaryCraft GameTests. All 11 JUnit checks and the release build pass.
Datagen and release-jar inspection verify the recipe, models, loot and tags. Logs:
`build/fuel-engine-datagen.log` and `build/fuel-engine-final-validation.log`.

Seventeen machines remain from the owner's original missing list: Bait Box, Bundled Bus,
CCTV, Compressor, Defoliator, Display, Electric Motor, Flame Turret, Generator, Mob Radar,
Pile Driver, Pneumatic Engine, Portal Shaft, Projector, Screen, Sonic Weapon and Spy Cam.
Client visual/sound checks, real client/server joining, release-jar play and third-party
fuel/space-mod adapters are not established by the headless checks.

## Filling Station survival fix (2026-10-01)

The reported inability to insert a jetpack came from the single-tank inventory base lacking
HasItemHandler. CoreContainer consequently omitted all four machine slots. The base now
provides a complete live Container bridge, transactional item capabilities, separate manual
and automated extraction permissions, and stable inventory identity across detached loads.
Legacy ItemsRaw saves remain readable with the loading caller's registry context.

- Mouse insertion/retrieval and shift-click from either player inventory or the worn chest
  slot work. DragonAPI's ArmorSlot accepts nonempty armor in its correct equipment slot.
  Active packs remain manually retrievable; automation extracts only finished output.
- Restore horizontal shaft input, the 1024 W gate, and V33a's 4 * floor(log2(omega)) mB/tick
  fill rate. Transfers are bounded by available tank fluid and remaining item capacity.
  Full items output even when the tank is empty; occupied outputs retain the active item.
- Restore ethanol crystals at 1000 mB each and general filled-container draining, including
  transactional empty-container remainders and whole-container tank backpressure. Fluid enters
  through every side except the bottom, which is extraction-only.
- Restore explicit fluid/amount arguments to Fillable. Jetpacks and integrated gear upgrades
  compare fluid identity and persist actual fluid type along with quantity. Packs retain ethanol,
  reject mixing, consume their final fuel safely and preserve upgrades. Amount-only legacy
  jetpacks retain their previous implied jet fuel. Integrated gears preserve their ratio and
  lubricant/nitrogen mode. The generated c:rocket_fuel tag preserves optional fuel integration.
- Thirty-six functional tests cover GUI clicks, inventory/armor transfers, all four shaft
  directions, all three jetpacks, speed/power gates, the last tank millibuckets, crystals,
  general buckets, backpressure, mixed fuels, both gear fluids, stack limits, comparators,
  fluid/item rollback and commit, real hoppers, detached and legacy saves, legacy packs,
  upgrade-preserving consumption, invalid API requests, rocket-fuel tagging, and exactly-once
  survival/command-removal drops. The foreign rocket-fuel substitution is test-only.

Validation: 415/415 full RotaryCraft GameTests and 11/11 JUnit checks pass; datagen and release
build pass. Jar inspection verifies the station recipe/models/loot, machine mining/motion tags,
all three jetpack chest-armor tags and exclusion of foreign-fluid fixtures. Logs are
build/filling-station-datagen.log, build/filling-station-focused.log and
build/filling-station-final-validation.log. Client visuals and a real networked join remain
unverified by this headless suite. Deploy the updated RotaryCraft and DragonAPI jars together.

## Mob Radar and ElectriCraft cable sprites (2026-10-01)

Ported V33a's Mob Radar with real bottom-only shaft input, the 8192 W / eight-block base
range, 1024 W per additional block and 256-block cap. Retain the original integer range
behavior: without power it still scans its own column. The square scan now covers the
modern world's entire height, including negative Y and above 255. Player, animal and
hostile filters remain independent; selected RadarJammer entities activate static and
jamming clears on the next scan after removal, filtering or the API returning false.

- Register the original radar-unit/screen/steel-gear/plate/circuit shaped recipe, loot,
  pickaxe and motion-blocking tags, handbook notes, menu, original radar model/texture,
  antenna animation and original GUI icons. Restore the three-quarter block shape.
- Sync immutable contact snapshots containing entity IDs, positions, icons and colors.
  A client can display server contacts beyond its normal entity tracking distance.
  Save/load retains filters and ownership; live entity references refresh after loading.
- Restore the owner's through-wall HUD geometry and off-screen rendering. The actual
  Motion Tracker item remains an unported dependency, preserved behind MOTION-PORT and
  the optional rotarycraft:motion_trackers tag. A compass exercises this integration
  only in GameTests and is excluded from release jars. The ordinary radar GUI is usable
  without that tool. DragonAPI now classifies Monster rather than all Mob entities as
  hostile and restores V33a's exact mob colors and cached subclass shades.
- ElectriCraft's RF cable renderer requests electricraft:blocks/rf and rf_end. Add a
  datagen block-atlas source for the original plural textures/blocks directory, also
  making legacy wire and battery sprites available. Preserve the existing 20-frame
  animations; their metadata was already valid.

Validation: 30 new radar functional tests plus registry placement bring RotaryCraft to
446/446 GameTests; 11/11 JUnit checks pass. ElectriCraft passes 13/13 GameTests. Both
release builds and datagen pass. Jar inspection verifies radar data, mining/motion tags,
original assets, optional tracker tag, fixture exclusion and ElectriCraft's atlas source
with intact RF animations. Logs: build/mob-radar-datagen.log, build/mob-radar-focused.log
and build/mob-radar-final-validation.log. Client rendering and real network joins remain
unverified by the headless suite. Deploy matching DragonAPI, RotaryCraft and ElectriCraft jars.

Sixteen machines remain from the owner's original missing list: Bait Box, Bundled Bus,
CCTV, Compressor, Defoliator, Display, Electric Motor, Flame Turret, Generator, Pile Driver,
Pneumatic Engine, Portal Shaft, Projector, Screen, Sonic Weapon and Spy Cam.

## Raw iron extraction and restored flake smelting (2026-10-01)

Owner-requested raw-iron support uses a lower duplication chance throughout the four-stage
chain. The generated 26.3 iron loot table uses the ore_drops Fortune formula: Fortune III
has equiprobable multipliers 1, 1, 2, 3, 4, averaging 2.2 raw iron per ore. Original normal
ore extraction averages 1.5^4 = 5.0625 flakes/ingots. Equal yield would require each raw
stage's chance to be (5.0625/2.2)^(1/4)-1 = 0.2316451155; round down to 23%.
Raw processing averages 1.23^4 = 2.28886641 ingots per raw iron, or 5.035506102 per original
ore with Fortune III. Ore remains slightly better on average, and increasingly better with
lower Fortune. This balance reference is vanilla Fortune III, not higher modded levels.

- The input recipe uses c:raw_materials/iron. Separate raw_iron_dust, raw_iron_slurry and
  raw_iron_solution preserve the lower rate across all stages and save/load; their generated
  models reuse the original iron textures. The final output is ordinary iron flakes.
  Raw solutions retain the original tungsten bonus. Every raw recipe explicitly stores
  duplication_chance: 0.23; JSON and network sync preserve it, and JEI displays the rate.
- Explicit recipe rates override bedrock guaranteed crushing. Normal iron ore retains
  50% duplication at each stage and its guaranteed first-stage bedrock bonus, averaging
  6.75 with that upgrade. Existing rare/Nether ore recipes retain their original behavior.
- Restore V33a addFurnace(): one redstone flake -> four redstone dust at 0.5 XP; one lapis
  flake -> six lapis lazuli at 0.6 XP. Both use 200-tick furnace recipes and advancements.
- Functional tests exposed missing inventory write-backs in the extractor: ManagedItemHandler
  returns copies, so shrinking an input or growing an existing stack did not persist.
  Consumption, interstage merging, stacked outputs and stacked bonuses now write back to
  the handler, preventing repeated extraction from unconsumed inputs and item loss.

Twenty new tests cover balance through Fortune 0-III, legacy defaults, input/stage gates,
recipe JSON/network sync, actual power and all raw stages, the complete raw chain, bedrock
behavior, save/load, inventory conservation, restored recipe counts/XP, real furnaces and
multi-item output backpressure. Logs: build/raw-iron-datagen.log,
build/raw-iron-client-datagen.log, build/raw-iron-focused.log and
build/raw-iron-final-validation.log.

Validation: 466/466 required RotaryCraft GameTests and 11/11 JUnit tests pass; server/client
datagen and the release build succeed. The packaged jar contains all 40 extractor recipes,
the four explicit raw rates, three raw-item model/texture mappings, both counted smelting
recipes and their advancements. The GameTest-only tracker fixture is excluded from the jar.

## Tagged modded and custom extractor ores (2026-10-01)

Restore the complete V33a 90-family ModOreList catalog through common item tags. Modern
copper reuses its vanilla chain; the other 89 families receive all four intermediate items,
356 extractor recipes, 89 counted furnace conversions and the original unmodified sprite
cells. Three additional crushing recipes preserve the Nether Force/Mimichite/Essence
metadata variants. Ordinary/rare/catalog-Nether rates are 50/90/80% at each original stage,
with rare taking precedence and the bedrock guarantee retained. Raw iron stays at 23%.

Generated final-stage bonus tables restore the V33a secondary products, including coal's
pitchblende/uranium alternatives and mineral/metal bonuses. Common ore-tag availability
replaces mod-ID gates. Preserve the original reciprocal-integer bonus rolls (including
pyrite's effective 50% and monazit's one-in-six), rather than silently changing the declared
probabilities into different behavior. JEI displays the rates and selected secondary product.

Modded flake smelts prefer a non-RotaryCraft item in the matching common product tag,
ordered by registry ID, with the original RotaryCraft product as fallback. Counts and XP
come from V33a. ElectriCraft and ReactorCraft now publish their actual ores in common
block/item tags and their corresponding material tags, so they join these chains directly.

Custom families use four reusable extract items carrying a persistent/networked
extract_family component. Datapacks define tagged input, component-sensitive following
stages, names, colors, rates, counts, bonuses and smelting products. Loaded/synchronized
recipes drive both GUI and automation validation. Priority resolves overlapping tags
deterministically. Component-aware merging prevents cross-family conversion; primary and
secondary output counts reserve full space, and washing/leaching require the full 125 mB.
See EXTRACTOR-TAGS.md for every tag mapping and five complete custom recipe examples.

Validation: 104 new tests cover every original family through creative-coil-powered complete
extraction and a real furnace, two isolated custom families, persistence, codecs, overlapping
tags, original Nether variants, exact-fit outputs, bonus gating and water backpressure.
Full suites pass: RotaryCraft 570/570, ElectriCraft 13/13 and ReactorCraft 49/49 GameTests;
RotaryCraft JUnit 11/11. Server/client datagen and all three release builds succeed. Jar
inspection confirms 399 extractor recipes, the original model/sprite references, 89 counted
tagged smelts, the unchanged raw rates and exclusion of GameTest-only fixture recipes/tags.
Logs: build/mod-ore-datagen-final.log and build/mod-ore-final-validation.log.

## Sonic Weapon, Motion Tracker and spring charging (2026-10-05)

Restore the full V33a Sonic Weapon as an active 26.3 machine: real six-face summative
power, original 262144 W gate, torque-limited long volume and speed-limited long pitch,
fixed 16-block scan, inverse-square blindness/brain/lung/lethal effects, helmet/creative
protection, hostile confusion, animal navigation, hidden silverfish host restoration/XP,
and original sound cadence. The upstream frequency switch is disabled and breakBrick()
contains no behavior; preserve its constants and that source behavior without inventing
terrain destruction. Exact torque 262144 produces 99999999 volume after source integer
rounding; lethal at the center requires torque 262145. Source handbook range text was
stale: replace the old power formula with the actual fixed range in all six existing
localized Sonic entries while retaining their other text.

Register the block/entity/menu/handbook/model layer, native submit renderer and original
SonicWeaponModel, item rendering, volume GUI and long-value packets. Server controls require
the matching active menu, valid machine and nearby player. Datagen writes the exact
psp/sts/psp HSLA-plate/sonar/turbine recipe, loot, unlock, models and mining/motion tags.

Restore the Motion Tracker's half-block samples along a 128 m ray through walls, 32 m
ordinary-mob limit, longer Wither/Dragon detection, original category colors and attack
warnings, configurable chat clearing and one charge per scan (including creative).
Keep scan deduplication local to the viewer and entity UUID instead of retaining the
legacy item-singleton last-name/distance cache. Preserve stack components while consuming
charge. Restore sprite144 byte-for-pixel from upstream Textures/Items/items.png, the exact
sonar/radar/screen/HSLA recipe and charged creative entry. Publish the actual registered
tracker in the motion_trackers tag; Mob Radar HUD requires its owner and a tracker in the
original 36 main inventory slots, including an uncharged tracker. Replace the obsolete
GameTest-only compass fixture with the real item in radar tests; no such fixture is shipped.

Repair the real charging path instead of supplying fabricated tool charge:
- Springs carry per-stack DAMAGE charge (0..32000); preserve/migrate the previous energy tag,
  names and other data. Restore standard stiffness1/power-scale1 and bedrock stiffness16/
  power-scale4 with unbreakability, source creative charge variants and honest tooltips.
- Winder reads its one configured face across all six orientations, charges upward to
  torque/stiffness capacity at the original speed cadence, retains standard spring break
  probability, and unwinds at original 8 Nm/1024 rad/s or bedrock 32 Nm/4096 rad/s for
  20/320 ticks per charge. Write every changed stack back, stop depleted output, provide
  the real menu slot/shift-click, spring-only one-item transactional ports, bottom extraction,
  survival drops, mode save/sync and guarded mode packets.
- Worktable swaps the spring's actual charge with the tool's previous charge, outputs both
  original component-bearing stacks in slots9/10, retains ChargeableTool.setCharged support,
  and requires exactly two valid inputs with clear outputs.
- Both shared inventory bases serialize through the caller's ValueInput/ValueOutput registry
  context and retain fixed-size handler identity on load. This restores detached loading
  without invalidating live inventory views.
- Shared spring-powered discharge now actually decrements the copied spring. Expected lifetime
  handles empty/creative inputs and clamps overflow. Smoke Detector reads the real spring
  charge for low-battery/range, discharges only on the server, retains original alarms and
  sound cadence, and saves/syncs its alarm and discharge state. Remove the invented extra
  amethyst sound; use the source smoke alarm.

Validation: 658/658 required RotaryCraft GameTests and 11/11 JUnit checks pass; the release
build succeeds on Minecraft26.3/NeoForge26.3.0.26-beta. The two new suites contribute37
Sonic and37 Motion/charging tests, including the complete powered Winder -> Worktable ->
live scan path. Initial fixture failures were corrected (south-facing redstone placement,
resource-copy ordering and obsolete compass tag). Distant boss tests explicitly load full
chunk neighborhoods, wait for entity visibility, and release their tickets/entities afterward;
a 6000-tick fixture timeout accommodates the headless server's fast ticks and asynchronous IO.
Server/client datagen succeeded on retry; an initial extra-iron feature-holder serialization
failure did not recur. The shared one-slot screen's two texture identifiers now point to the
actual original screen resources, fixing the Winder power tab. Logs: build/sonic-focused-2.log,
build/motion-focused.log, build/motion-range-2.log, build/sonic-motion-final.log and
build/sonic-motion-release.log. Final jar inspection verifies the new classes, recipe/model/
loot resources, original Sonic assets and exact V33a tracker pixels, actual tracker/mining/
motion tags, and Winder screen assets. Twenty test-only resources are absent;102 shared
fixture tag paths contain only the production files. Headless checks do not establish live
client visual acceptance or a real multiplayer join.

Fifteen machines remain from the owner's original missing list: Bait Box, Bundled Bus,
CCTV, Compressor, Defoliator, Display, Electric Motor, Flame Turret, Generator, Pile Driver,
Pneumatic Engine, Portal Shaft, Projector, Screen and Spy Cam. ChromatiCraft was excluded
from this work as requested.


## Defoliator full 26.3 port (2026-10-05)

Restore the complete V33a Defoliator: bottom-only shaft input, 16384 W gate, original
2*floor(sqrt(omega)) random probes, 8*log2(torque) range capped at 128, and a 4000 mB tank.
Both poison and chlorine work through horizontal standard/bedrock pipes. Each successful
vegetation removal costs exactly 1 mB, retains native fortune-zero loot and break feedback,
and gives nearby living entities (including survival players) Poison IV for 50 ticks plus
0.5 generic damage in the original block cube expanded by 3.

The defoliator_targets block tag replaces removed material tests and the old mod-wood
lookup: native log/leaf/sapling tags, optional common wood tags, and datagen classification
of modern vegetation/vine/cactus/cane/bamboo classes, including RotaryCraft canola. Packs
can extend this tag. Keep permission checks under the real/offline placer's identity; an
unowned machine uses a stable RotaryCraft fake identity. Recheck block state after the
protection event so a canceled/replaced target cannot consume chemical or yield stale loot.

Potion conversion retains the source base-poison identity (ordinary bottles, including
custom-effect/component-bearing stacks; long/strong/splash/lingering variants are invalid),
1000 mB per potion and one returned glass bottle. Preserve V33a insertion into either slot
and bottle extraction from either slot, while conversion only reads slot 0 and returns to
slot 1. Add loss prevention: reserve compatible chemical capacity and returned-bottle space
before consuming the input. Full tanks, chlorine, full bottles or an obstructed return slot
leave every item and fluid intact. Unpowered conversion and real hopper input/output work.

Use PoweredLiquidReceiver with a native two-slot ResourceHandler inventory composition;
this preserves the source tank semantics without the legacy inventory base's conflicting
Container.isEmpty(). Keep inventory/fluid capability identities through detached registry-
aware loads, transactional rollback/commit, unrestricted manual removal and bottle-only
automated extraction. Spill and clear both slots on any server removal, exactly once.

Register the block/entity/menu/handbook, original DefoliatorModel, 26.3 submit renderer,
item model and GUI. Restore the source 0.625 block height and original rendering transform.
Menu data carries actual fluid level and all 64 power bits; shift-click and survival
right-click are exercised. Clamp the shared power-only GUI bar before scaling to avoid
long overflow. Datagen supplies the exact V33a P P/SPS/BIB recipe (standard pipes, HSLA
ingots, base panels, impeller), unlock, loot, mining/motion tags, models and tooltip text.
The existing GUI and model textures match upstream V33a byte-for-byte.

Repair the original malformed effect packet: supply all 3 target-coordinate ints, preserve
the 64-block radius around the destroyed target, and render client-only without requiring
a block entity at that now-air position or a loaded distant machine. Keep the 343-particle
7x7x7 spread and original outset 2 (positions +/-2.5 around each block center); the legacy
redstone green component 20 saturates the modern RGB24 green channel.

Validation: the final target-centered packet version passes 719/719 required RotaryCraft
GameTests and 11/11 JUnit checks, and the release build succeeds. This slice adds 60
Defoliator checks, including ownership and integrated hopper conversion. Server/client
datagen passed. Release inspection verifies 15 required classes/resources, the exact recipe,
native special item model, tooltip, mining/motion tags and byte-identical V33a GUI/model
textures. All 20 test-only resource paths are absent; all 102 shared fixture paths contain
only production resources. Logs: build/defoliator-datagen.log, build/defoliator-focused-5.log,
build/defoliator-full.log and build/defoliator-final.log. Artifact details:
build/defoliator-artifact-verification.json.
The deprecated native player helper is always creative; the new survival fixtures use
native survival players, NeoForge configureMockConnection and markClientLoaded, remove
players afterward, and route interaction through ServerPlayerGameMode.useItemOn.
Live client visual acceptance and real multiplayer joins remain unverified.

Fourteen machines remain from the owner's original missing list: Bait Box, Bundled Bus,
CCTV, Compressor, Display, Electric Motor, Flame Turret, Generator, Pile Driver, Pneumatic
Engine, Portal Shaft, Projector, Screen and Spy Cam. No ChromatiCraft files or tasks were
part of this slice.


## 2026-10-05 - Full Pile Driver and recovered spawner port

Port BlockEntityPileDriver fully from V33a, activate its block/entity/handbook and existing
PileDriverModel, and supply the native 26.3 submit renderer and machine item model. The two
opposing shaft inputs select the stronger source (they do not sum torque); require 80000 Nm
and 16384 W per lifted meter. Preserve the strict lift timer, descent/retraction phases,
air-column skipping, minimum one-tick cadence, original 21-cell footprint (four corners
excluded), conversions and repeated impacts. The original hit table starts at zero, so
obsidian takes six strikes. Use long arithmetic for depth/power and high-power timing.
Keep all original phase fields in native save/sync; partial sync preserves transient hit
progress, and replacement states cannot inherit a stale counter. Hit counters remain
transient across actual world reloads, matching V33a.

Keep weak-block damage up to four layers below the impact, the five-block shockwave cube,
ice-to-water plus ice loot, web-to-string, native falling-block state, hanging-entity damage,
24-particle liquid bursts, impact notification, bounce, crushing and 150-tick/amplifier-10
nausea for noncreative players. Native frame damage ejects the displayed item first and
breaks the empty frame on another hit. Apply owner permission and state revalidation to
main, secondary, shockwave, hanging and hammer mutations. Retracting tips cannot erase a
replacement or advance their phase through protection. Use native world minimum height and
loaded-chunk checks; protected waterlogged solids do not count as a cleared liquid plane.

Generate impact rules as a reloadable NeoForge block data map: 350 entries, including all
340 original GeoStrata rock/shape combinations behind mod-loaded conditions. Preserve
rock-to-own-cobble conversions, positive hit counts and shale/limestone weak-layer rules.
The optional -PpileDriverGeoTests run loads the actual GeoStrata source set and checks every
combination, with no production dependency on GeoStrata. No invented rock or recipe data.

Fully port DragonAPI's original ItemSpawner behavior into RotaryCraft's recovered-spawner
item: type-only recovery, optional custom spawn parameters, original randomized delay,
28 typed creative variants, survival/creative placement and consumption, source soft-block
placement, native collision/protection rollback and localized shift tooltips. Generate the
end-only dragon and three Twilight Forest boss dimension restrictions as a synced entity
data map. Twilight Forest runtime integration is not exercised in this slice.

Restore mining-pipe metadata 4 as the full-cube pile_driver shape with minepipe2 texture,
distinct from the preserved borer junction. Generate the three inset bar models and empty
loot; restore zero hardness/resistance, nonocclusion and original X/Z hand-break cleanup
(+/-64), with protection/revalidation and loaded-chunk guards. Retain the excavation-star
range/spread methods behind a CHROMA-PORT marker; no ChromatiCraft files were edited.

Datagen supplies exact PGP/gFg/PDP crafting (four base panels, 8x HSLA gearing, two HSLA
shaft rods, tungsten alloy flywheel core and iron drill), unlock, machine loot, impact maps,
fragile/mining/motion tags, models and lang. The piletex model artwork matches upstream
V33a byte-for-byte; retain the existing mono 44100 Hz spatial piledriver sound asset.

Validation: 797/797 full RotaryCraft GameTests and 11/11 JUnit checks pass on the final
source, as does the release build. This slice adds 77 Pile Driver/spawner/pipe checks;
with the 17 actual GeoStrata rock tests, 94/94 targeted acceptance checks pass. Native
server/client datagen succeeds. Release inspection verifies 27 required classes/resources,
source recipe, models, 350 impact rules, all 340 optional rock profiles and fixture isolation:
20 test-only paths absent; 102 shared fixture paths contain production resources only.
Logs: build/piledriver-datagen-final.log, build/piledriver-final-acceptance.log and
build/piledriver-final.log. Artifact evidence: build/piledriver-artifact-verification.json.
Live client visual acceptance and real multiplayer joins remain unverified.

Thirteen machines remain from the owner's original missing list: Bait Box, Bundled Bus,
CCTV, Compressor, Display, Electric Motor, Flame Turret, Generator, Pneumatic Engine,
Portal Shaft, Projector, Screen and Spy Cam. No ChromatiCraft tasks were run in this slice.


### 2026-10-07 — inventory and held-item renderer parity pass

Compared the active item path against local upstream/master ItemMachineRenderer,
PipeBodyRenderer and each model's original out-of-world TESR path. All 118 registered
special-rendered machine item variants now carry their registered-block identity through
native item-model baking. Restore the original Techne axis conversion and machine-specific
item poses: ordinary machines versus transmission/engine placement, engine and gearbox yaw,
wind/hydro scale and offset, sprinkler/landmine/smoke detector/chunk loader sizing, and the
spawner controller's offset. No arbitrary global quarter-turn or hotbar-only translation.

Bake immutable inventory geometry through the actual model.renderAll path rather than the
raw root. This preserves constant internal poses, repeated parts and inventory visibility
rules (coil angle, gearbox offsets, defoliator copies, cooling-fin legs, empty machine
contents, compactor rest frame, performance-engine inventory frame). Submit and GUI bounds
use the same baked vertices. Preserve native normals, live lighting/overlay, foil rendering,
outline rendering and immutable extracted bedrock-upgrade state for hydro engines, coils
and splitters. No live block entity or world is required by item rendering.

Gearboxes use each ratio's original gear train and material texture; all seven flywheel
materials, the six shaft materials, cross/merge shafts and creative coils select their own
artwork. Item-only models and transforms are generated by RoCItemDisplayModels: six pipe
families use an isometric GUI pose (30/225 degrees, 0.78125 scale, including the original
1.25 inventory enlargement) and 0.25 held scale for both hands. Held pipes are 37.5% smaller
than the former inherited first-person 0.4 scale. Ten vanilla-style steel/bedrock tools
restore item/handheld parents. World pipe geometry and world blockstates are unchanged by
this item slice. Other concurrent agents' world-renderer and chassis-sprite fixes are
preserved; this pass does not claim to complete the remaining machine ports.

Validation: client datagen passes, full JUnit 22/22 passes (six new item-renderer checks),
and release build passes against Minecraft 26.3 / NeoForge 26.3.0.51-beta. Tests bake every
registered special item, validate concrete textures and finite inventory geometry, check
all gearbox ratios, compare actual submitted vertices to their reported bounds under a
translated caller pose, check original engine axes, native custom-data snapshots, six pipe
families and ten handheld tools. Release inspection verifies all 118 special item wrappers
and their variant fields plus all six pipe models. Logs: build/item-renderer-final.log
(datagen), build/item-renderer-final-validation.log (JUnit/release), and
build/item-renderer-artifact-verification.json. Live client visual acceptance, including
first person and shader-pack rendering, remains outstanding.
