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

The suite registers one isolated placement/type/lifecycle test for each of the 138 active
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
