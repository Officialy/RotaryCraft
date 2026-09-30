# RotaryCraft survival beta ledger — Minecraft 26.3

Updated 2026-09-30. Target: NeoForge 26.3.0.26-beta, Java 25.

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

The suite registers one isolated placement/type/lifecycle test for each of the 134 active
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

The owner's starting report listed 25 missing V33a machines. Purifier is restored in this slice,
leaving 24 from that report: Bait Box, Bundled Bus, CCTV, Chunk Loader, Compressor, Crafter,
Defoliator, Display, Distiller, Electric Motor, Flame Turret, Fuel Engine, Fuel Enhancer,
Generator, Item Filter, Mob Radar, Pile Driver, Pneumatic Engine, Portal Shaft, Projector,
Screen, Sonic Weapon, Spy Cam and Terraformer. Audit their pristine source and dependencies
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
