# Railgun ammunition and projectile port — RC19

Target: Minecraft 26.3 / NeoForge 26.3.0.51-beta. Original reference: RotaryCraft commit
`b2288638f7f4703bb4555f72a2b86f72407cc2ae`, especially `RotaryRecipes.java`,
`Items/ItemRailGunAmmo.java`, `Items/ItemExplosiveShell.java`, `Base/EntityRailgunShotBase.java`,
`Entities/EntityRailGunShot.java`, `Entities/EntityExplosiveShell.java`,
`Entities/RenderRailGunShot.java` and `TileEntities/Weaponry/Turret/TileEntityRailGun.java`.

## Ammunition, firing and recipes

Sixteen tiers preserve mass `1 << tier`, source integer torque `sqrt(512 * mass)` and the source
impact-damage curve. Tier fifteen requires 4096 Nm and retains maximal source damage. Torque
selects affordable ammunition; it does not rewrite a stack's tier. The strongest affordable stack
wins regardless of inventory order. A successful spawn consumes one round; failed factories,
invalid aim or refused spawns conserve the inventory. The muzzle and four-block-per-tick launch
velocity retain the original contract. The cannon accepts both native items and `RailGunAmmo`
extensions; `TargetEntity` targeting, impact veto and knockback hooks remain available.

`rotarycraft:railgun_power` is a persistent, networked integer component. Existing port stacks
with vanilla DAMAGE metadata still resolve the same mass, recipe ingredient and icon; typed
components take precedence. Native entity NBT and complex spawn data preserve the full ammo
stack, gun position and flight age. Registered/summoned default projectiles have valid ammunition.

Recipes are generated from the actual original patterns: three HSLA base slugs, fifteen upgrades
using planks, stone, iron, gold and bedrock alloy in successive groups of three, and sixteen
explosive shells from the steel cross around nitrate. Upgrade ingredients require the exact
previous tier, including legacy stacks, and supply component-bearing native recipe displays.
Forty-five conditional upgrade definitions select one per tier at server datapack load/reload:
easy yields sixteen, medium eight and hard two. NOOB/UNPLAYABLE retain DifficultyEffects' original
medium fallback. The base recipe is unconditional, giving sixteen active slug recipes per setting.
The handbook reads the synced recipe catalogue. Its shared DragonAPI display helper now preserves
empty shaped cells, all ingredient choices and components; cooking slots show their actual input.

## Flight, impact and terrain

Both projectiles use manual constant velocity with native swept block/entity collision and
loaded-chunk guards. They impact after eighty ticks, exclude the shooter during flight and retain
railgun-block immunity. Slugs retain flight explosion particles/sounds and water explosion/bubbles.
The kinetic impact notification precedes terrain mutation and is posted once per projectile.
Living impacts strip effects, drop non-player equipment with the original pickup delay, retain
player equipment, apply the source damage and transfer momentum. Extension vetoes stop further
processing, extension knockback is respected and velocity is synchronized. Dragon head damage and
railgun achievements remain. Generated native turret damage tags restore projectile and
armor/shield-bypass behavior; ownership survives a removed or unloaded cannon.

Both original block-damage controls gate kinetic impact terrain. The original footprint retains
`abs(x*y*z) < 9` within offsets -3..3. Datagen tags describe plants, fragile blocks, wood and
sediments at tiers one, two, three and four. Eligible families recursively break the same block
within radius five, before the special highest-tier rules. Grass conversion, dirt/stone/cobble
fatigue and stone-to-cobble conversion retain their source thresholds. Arbitrary breakable blocks
use the tier-fourteen radius three and tier-fifteen radius six. Recursive breaking is bounded,
checks loaded chunks and break permissions, and uses native loot/removal. Liquids, unbreakable
blocks and railguns are preserved; waterlogged blocks follow native fluid restoration.

Fatigue lives in dimension SavedData, not modern block-state variant metadata. Its registry-aware
codec persists the expected state and hit counter. Native place/neighbor notifications and lazy
state checks invalidate replaced blocks. The same-state replacement regression covers removal
followed by replacing stone, rather than carrying an old block's damage into a new one.

Shells are actual registered moving projectiles with the original incendiary radius eight and
extension callback. They use native explosion cancellation and TNT interaction semantics.
Repeated entity processing from the old nested terrain loop is deliberately removed: each
impacted entity receives one callback, effect removal, equipment transfer and damage application.
Collision uses the native hit point and block-position flooring instead of old truncating casts.

## Assets and client rendering

The original atlas cells 129–132 supply the four ammunition sprite groups at thresholds 7, 10
and 13. See `asset-recovery.md` and `tools/recover_legacy_item_sprites.py` for provenance and exact
RGBA verification. All sixteen tiers appear in the creative tab with mass-dependent names.
Both projectile registrations use the original railgun texture and eight cumulatively rotated
camera-facing planes. Deferred callbacks capture lighting rather than retaining mutable state.

The optional Thaumcraft void-metal ammunition subsystem is outside this supported slice; its
original upstream reference and the open ammunition/target extension contracts are preserved.
Actual remote-client and Iris/Distant Horizons/shader acceptance remain project-wide backlog tasks.

## Validation

Validation evidence is recorded in the current ReactorCraft ledger and PROJECT-BACKLOG.md.
Log paths below are relative to the workspace root:

- `build/railgun-gametest.log`: all sixty required native regressions pass, including every tier's
  firing and actual generated recipe, all five difficulty settings, impact/config gates, terrain
  and replacement fatigue, target extensions, native NBT/spawn identity and handbook input slots.
- `build/railgun-final-validation.log`: all sixty-nine family unit tests pass (DragonAPI 25,
  RotaryCraft 33, ReactorCraft 8, ChromatiCraft 3), both release jars assemble, and the disposable
  native client passes model, deferred geometry, synced handbook and server-to-client tracking
  checks. Existing turbine, gear, dropper and DragonAPI client regressions also pass. Build: 1m 50s.
- `build/railgun-final-datagen.log`: final client/server datagen passes, including conditional
  original-yield recipes. Earlier runs: `build/railgun-datagen.log` and
  `build/railgun-damage-datagen.log`.
- `build/railgun-artifact-verification.json`: all 2182 RotaryCraft PNGs decode; forty-six slug
  recipe definitions and the required native models/textures/tags/classes are packaged. Archived
  assets, TestInstance code and JUnit classes are absent from the RotaryCraft release jar.

The recovery script checks all seven recovered sprites against the pinned original atlas pixels
and hashes, without modifying files in `--verify` mode. Native integrated-client success does not
close remote-client or actual Iris/Distant Horizons/shader acceptance. Other family missing-texture
warnings remain. RC19 and preceding batches remain uncommitted and unpushed.

## Post-D20 local checkpoint — 2026-10-09

RC19, recovered assets, native tests and the later DragonAPI integration tests are included in
the owner's requested post-D20 local checkpoint. Required new generated recipes, advancements,
models and tags are explicitly included despite the existing src/generated ignore rule. The
latest checkpoint passes 85 family units, all 18 native DragonAPI tests and the isolated client
audit; prior RC19's 60-test source/gameplay suite remains recorded above. D20's full contract is
in DragonAPI/docs/block-array-port.md. Remote-client and actual shader/LOD acceptance stay open.
