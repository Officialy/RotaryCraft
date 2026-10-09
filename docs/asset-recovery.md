# Original item sprite recovery — 2026-10-08

Four invalid modern item PNGs were recovered from the original upstream RotaryCraft atlas,
without drawing, recolouring, resizing or substituting artwork. The original atlases are retained in Git at
commit `b2288638f7f4703bb4555f72a2b86f72407cc2ae`, path `Textures/Items/items2.png`.
The `items2.png` SHA-256 is `b7f88ffe7366bd70a6bb312ec27c024ea9c6e632a3d09a8239cb1e328fde3b7f`.
The 256×256 image contains a 16×16 grid of 16×16 sprites, indexed row first.

| Modern texture | Original sprite | Source mapping |
| --- | --- | --- |
| `target.png` | 98 | `Registry/ItemRegistry.java`: TARGET(98, 1, ...) |
| `railgun_ammo.png` | 129 | RAILGUN(129, 1, ...); `Items/ItemRailGunAmmo.java` uses this for damage 0–6 |
| `railgun_ammo_1.png` | 130 | Original railgun damage 7–9 |
| `railgun_ammo_2.png` | 131 | Original railgun damage 10–12 |
| `railgun_ammo_3.png` | 132 | Original railgun damage 13–15 |
| `tungsten_alloy_spring.png` | `items.png`, 108 | `ItemStacks.springtungsten` = COMPACTS metadata 12; COMPACTS type 6, sheet 0; `ItemMulti.getItemSpriteIndex` = 16×6+12 |
| `molten_hsla_bucket.png` | 108 | BUCKET starts at 104; `ItemFuelLubeBucket.getItemSpriteIndex` adds damage; `RotaryNames.liquidNames[4]` is molten HSLA |

`Base/ItemBasic.getTexture` maps sheet 1 to `items2.png`. Existing ethanol bucket and HSLA spring
textures match the corresponding original atlas pixels and corroborate this interpretation.
`tools/recover_legacy_item_sprites.py` pins the source and atlas hash, restores these seven sprites,
fully decodes the resulting PNGs and checks exact RGBA pixel equality. Its `--verify` mode writes
nothing. Pre-recovery bytes are preserved under `build/legacy-asset-recovery/`; prior Git commits
also retain them. Generated item models continue to come from `RoCModelProvider`.

RC19 also recovers the original railgun's three additional sprite groups at indices 130–132.
Generated native range-select models use the persistent railgun-power component, with legacy
DAMAGE compatibility, to select all four groups at thresholds 7, 10 and 13. All sixteen tiers
resolve the original sprites through the actual native item-model resolver. See `railgun-port.md`.

`items.png` (sheet 0) has SHA-256
`fe702f9cf36ed86a9ea7d09c8af055c7c96eabd166a96056c316aad7de4a6ff6`.
The script separately verifies both source hashes. `RotaryNames.compactNames[12]` is
`misc.tungalloy`, and `Registry/Flywheels.java` uses `springtungsten` for the tungsten
flywheel's raw material. This establishes the tungsten-alloy sprite independently of the
bedrock coil's artwork.

`advancedgear.png` contained zero bytes and had no item-model consumer. It is archived intact
in `reference/legacy-assets/`, outside Minecraft's atlas scan. The original placer used 3D
machine rendering; its four subtypes (worm gear, CVT, coil, high gear) already have separate
registrations and generated `minecraft:special` / `rotarycraft:machine` item models using the
original machine geometry. The creative coil retains its own model too. See the archive README
for the source mapping. No flat icon was invented and no machine was unregistered.

The same original source mapping exposed a behavior mismatch: the material was registered as
`ItemCoil`, making it windable, damageable and limited to one per stack. Its stable registry ID
is retained, while its item factory now matches the original stackable COMPACTS material. A
registry regression verifies the tungsten flywheel ingredient is stackable and cannot serve as
charged tension storage. The actual HSLA and bedrock coils retain their original classes.

Validation: all 2179 PNGs under assets/rotarycraft fully decode. The native disposable client
passes original sprite decoding, actual special-model geometry for all five advanced-gear items,
stack size 64/non-damageable material components and absence of the obsolete empty resource.
The registry regression passes with the real FML-loaded registrations. The current release jar
contains no archived empty PNG. Log: ../build/backlog-second-client.log (BUILD SUCCESSFUL,
GEAR_ITEM_AUDIT_CLIENT_PASS); unit results: ../build/backlog-second-units.log.

RC19 final validation: all 2182 PNGs decode, and verify-only mode confirms all seven recovered
sprites against the pinned original pixels. All sixteen native and legacy ammunition tiers select
the intended original group in the live client. Evidence from the workspace root:
`build/railgun-final-validation.log` and `build/railgun-artifact-verification.json`.
