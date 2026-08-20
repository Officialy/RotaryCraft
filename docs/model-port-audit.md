# RotaryCraft model port audit — 2026-08-20

Mechanical diff of every ported `*Model.java` against its 1.7.10 original (`upstream/master`
`Models/**`, `Base/ModelGearboxBase.java`). 110 originals, 111 port files, 105 pairs compared
(`Solar` has no port file yet).

The comparator parses `new LODModelPart(this,tx,tz)` / `addBox` / `setRotationPoint` / `setRotation`
on the 1.7.10 side and `addOrReplaceChild` + `texOffs`/`addBox`/`PartPose` on the port side, then
diffs per part. Original names map to the port's convention by lowercasing, with an underscore
inserted before interior capitals (Techne distinguishes `Shape3a` from `Shape3A`; the port writes
those as `shape3a` and `shape3_a`).

## 1. Geometry — clean except two models

`texOffs`, box dimensions, part offsets and part rotations match 1.7.10 **exactly** across all
105 models except:

| Model | Defect |
|---|---|
| `LampModel` | all 16 parts declared `PartPose.offset(0,0,0)` — every offset dropped |
| `VLampModel` | all 21 parts declared `PartPose.rotation(...)`/`ZERO` — rotations kept, every offset dropped |

Same defect class as the gearbox housing (fixed in `a3ee3e3`): the model draws, but every piece is
piled onto the model origin.

Earlier passes of this audit flagged `Coil`, `Grinder`, `SonicWeapon`, `DistribClutch` and
`MultiClutch` — those were comparator artefacts from the `Shape3a`/`Shape3A` name collision, not
port defects. Their geometry is exact.

## 2. `mirror(true)` — a no-op in 1.7.10, a UV bug in the port

Techne emits `part.addBox(...); …; part.mirror = true;`, and 1.7.10 `ModelRenderer.addBox` reads
`this.mirror` **at addBox time** — so the flag never affected the cube. Verified mechanically:
across all 110 originals there are **zero** cases where `mirror = true` precedes that part's
`addBox`. Every one is a no-op.

`CubeListBuilder.create().mirror(true)` *does* flip the cube's U coordinates, so reproducing the
flag introduces a texture error the original never had. Present in **103 of 105** ported models.

## 3. `renderAll` stubs

`RotaryModelBase.renderAll` is the per-part draw. A port that reduces it to
`root.render(stack, tex, light, …)` draws every declared part at its rest pose in one call.

- **38 models where the 1.7.10 `renderAll` contains `GL11.glRotate`/`glTranslate`** — i.e. the
  model animates, and the port has thrown the animation away:
  `AAGun, AC, Aggregator, AirGun, BeamMirror, BedrockBreakerV, Belt, CCTV, Centrifuge,
  ChunkLoader, Clutch, Compactor, Crystallizer, Defoliator, DistribClutch, Fertilizer, FlameTurret,
  FreezeGun, Friction, HydraulicPump, HydraulicTurbine, Hydro, LaserGun, LavaMaker, LawnSprinkler,
  Mirror, MultiCannon, MultiClutch, PileDriver, PipePump, Radar, RailGun, Ram, SonicBorer,
  Splitter2, VClutch, Wetter, Woodcutter`
- **40 models where the 1.7.10 `renderAll` had no transforms** — `root.render()` is equivalent
  *provided* the original drew every part unconditionally. Ones with conditionals or parts the
  original never drew, which need a closer look: `Breeder` (5 conditionals, `Shape5c` never drawn),
  `Obsidian` (`Shape4` never drawn), `Aerosolizer`, `Extractor`, `Fin`, `Spawner`, `Lamp`, `VLamp`.
  `Shaft`/`ShaftV` draw through helper methods, so the "undrawn" reading there is a false positive.

## 4. Handbook preview `phi`

`GuiHandbook.doRenderMachine` computes a `variable` (`-1000F * (timeStep+1)` for shaft/gearbox,
`-1000/-2000/-3000` for worm/CVT/coil) and the port routes it into `GuiMachineRenderState.phi`,
which `GuiMachineRenderer` passes to `renderAll` as the **animation angle**.

In 1.7.10 that value was the `partialTicks` argument of
`TileEntityRendererDispatcher.renderTileEntityAt(te, -0.5, 0, -0.5, variable)`. Only
`RenderAdvGear` ever read it (`par8 <= -999F` → `itemMetadata = (int)-par8/1000`), to pick which of
worm/CVT/coil/high-gear to draw when there was no in-world TE. Every other renderer ignored it, and
none treated it as an angle — 1.7.10 handbook previews were static models under a rotating camera.

In the port `MachineModels` already selects the model per `MachineRegistry`, so the sentinel has no
remaining job. Feeding it to `phi` is a pure mis-port.

## Reproducing

Scripts live in the session scratchpad (`audit.py`, `deep.py`, `setcmp.py`); `git archive
upstream/master Models Base` supplies the 1.7.10 side.
