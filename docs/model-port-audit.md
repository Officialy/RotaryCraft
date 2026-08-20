# RotaryCraft model port audit — 2026-08-20

Mechanical diff of every ported `*Model.java` against its 1.7.10 original (`upstream/master`
`Models/**`, `Base/ModelGearboxBase.java`). 110 originals, 111 port files, 105 pairs compared
(`Solar` has no port file yet).

The comparator parses `new LODModelPart(this,tx,tz)` / `addBox` / `setRotationPoint` / `setRotation`
on the 1.7.10 side, and `addOrReplaceChild` + `texOffs`/`addBox`/`PartPose` plus any constructor
`setPos`/`xRot` patching on the port side, then diffs per part. Original names map to the port's
convention by lowercasing, with an underscore inserted before interior capitals — Techne
distinguishes `Shape3a` from `Shape3A`, and the port writes those as `shape3a` and `shape3_a`.
Getting that mapping wrong makes the audit report ~100 phantom defects, so it is worth re-deriving
before trusting any future run.

## 1. Geometry — clean

`texOffs`, box dimensions, part offsets and part rotations match 1.7.10 **exactly** across all 105
models. The only remaining flag, `Grinder`'s `Shape2` Z-rotation, is a comparator artefact: the
port faithfully reproduces the original's in-`renderAll` `Shape2.rotateAngleZ = 1.0908F` as
`shape2.zRot = 1.0908F`, and the audit folds the port's assignment into the pose but not the
original's.

`LampModel` and `VLampModel` declared every part at `PartPose.offset(0,0,0)` and re-applied the real
offsets with `setPos()` on the baked `ModelPart` in the constructor. Not a visible defect — the
constructor runs before any render — but two sources of truth, with a layer that is collapsed onto
the origin on its own. Consolidated into `createLayer`.

The gearbox housing (shapes 1–11 in all four ratio models) *was* genuinely collapsed and is fixed
separately; see `a3ee3e3`.

## 2. `mirror(true)` — was a no-op in 1.7.10, a UV bug in the port

Techne emits `part.addBox(...); …; part.mirror = true;`, and 1.7.10 `ModelRenderer.addBox` reads
`this.mirror` **at addBox time** — so the flag never affected the cube. Verified mechanically:
across all 110 originals there is not one case where `mirror = true` precedes that part's `addBox`.

`CubeListBuilder` captures mirror the same way, so a `.mirror(true)` written *before* `.addBox()`
does flip the cube's U coordinates. 2878 such calls across 115 ported models — all removed. The 38
trailing `.mirror()` calls in `LampModel`/`VLampModel` sit after `addBox` and are already inert;
left alone.

## 3. `renderAll` stubs

`RotaryModelBase.renderAll` is the per-part draw. A port that reduces it to `root.render(...)` draws
every declared part at its rest pose in one call.

- **38 models where the 1.7.10 `renderAll` contains `GL11.glRotate`/`glTranslate`** — the machine
  animates and the port had thrown the animation away. All ported.
- **6 models where the 1.7.10 `renderAll` draws parts conditionally** (`Breeder`, `Extractor`,
  `Fin`, `Spawner`, `Aerosolizer`, `Obsidian`) — `root.render()` showed geometry the original never
  showed in that state. All ported.
- **`WinderModel.renderAll` was an empty method**, its whole body commented out, so the winder drew
  nothing at all. Ported. Its coil is drawn mirrored (`glScaled(-1, 1, 1)`), which 1.7.10 paired
  with `glFrontFace(GL_CW)`; 26.2 has no per-draw front-face switch, so `RenderWinder` routes the
  model through `RenderTypes.entityCutout` — which in 26.2 is the **no-cull** pipeline
  (`entityCutoutCull` is the culling one).
- **`CoilModel.renderAll` never drew `shape3_a`..`shape3_n`** and drew `shape3a`..`shape3n` twice as
  often instead: 14 coil segments missing, 14 double-drawn. The `Shape3a`/`Shape3A` collision again.
  Re-derived from the original.
- **33 models keep a `root.render()` `renderAll`, and that is correct**: for each, the 1.7.10
  `renderAll` has no GL transforms, no conditionals, and draws every declared part, so a blanket
  root render is exactly equivalent.

Verified for every ported body: same parts, same order, identical declared-but-never-drawn set,
balanced push/pop.

`BedrockBreaker`, `Magnetizer` and `Performance` were hand-ported earlier using per-part
`xRot`/`zRot` offsets rather than a `PoseStack` rotation. That is equivalent here — every part in
each rotating group shares the pivot, and the parts carry no rotation on the axes that would
reorder against `ModelPart`'s Z→Y→X composition — so they were left as they are.

## 4. Handbook preview `phi`

`GuiHandbook.doRenderMachine` computed a `variable` (`-1000F * (timeStep+1)` for shaft/gearbox,
`-1000/-2000/-3000` for worm/CVT/coil) and the port routed it into `GuiMachineRenderState.phi`,
which `GuiMachineRenderer` passed to `renderAll` as the **animation angle** — so handbook gears
snapped to an arbitrary rotation once a second.

In 1.7.10 that value was the `partialTicks` argument of
`TileEntityRendererDispatcher.renderTileEntityAt(te, -0.5, 0, -0.5, variable)`. Only `RenderAdvGear`
ever read it (`par8 <= -999F` → `itemMetadata = (int)-par8/1000`), to pick which of
worm/CVT/coil/high-gear to draw when there was no in-world TE. Every other renderer ignored it, and
none treated it as an angle — 1.7.10 handbook previews were static models under a rotating camera.

`MachineModels` resolves the model per `MachineRegistry` in the port, so the sentinel has no job
left. Field dropped; previews are static again. The `COIL` branch's `setBedrock` alternation is real
behaviour and was kept.

## 5. Also fixed in passing

`RenderLamp` bound `lamptex.png` for both floodlight orientations, so the shipped
`lampvertical.png` was never used; and it called `renderToBuffer` (a blanket root render) instead of
`renderAll`, so `VLampModel`'s beam segments drew even with beam mode off.

## Reproducing

Scripts live in the session scratchpad (`audit.py`, `deep.py`, `mirror.py`, `parity.py`,
`port_renderall.py`); `git archive upstream/master Models Base` supplies the 1.7.10 side.

Two traps worth remembering if this is re-run:

- `ModelBeamMirror` keeps an older `renderAll` inside a block comment above the live one, so
  locating it by plain text search picks the dead body.
- The `Shape3a`/`Shape3A` name collision affects `Coil`, `Grinder` and `SonicWeapon`.
