# Reservoir native rendering and D06 lighting

Original source: RotaryCraft 663f63c60, Renders/M/RenderReservoir.java and
TileEntities/Storage/TileEntityReservoir.java. Current API reference: generated
Minecraft/NeoForge 26.3.0.76 sources. This replaces the partial active renderer,
including its inert legacy renderLiquid/renderCover path, with native extracted
state and complete model/cover/fluid submissions.

## Preserved behavior

| Original behavior | Native implementation |
| --- | --- |
| Bottom plus unconnected side walls | Extract a direction mask; draw only those model parts with the original model transform |
| Glass cover | Resolve the current glass particle material; submit the original full-width top quad at Y=0.99 |
| Fluid still icon and color | Resolve the registered FluidModel, its still material and tint at the reservoir's biome position; no guessed renderer palettes or water/lava fallback |
| Water BlockIconEvent | Fire DragonAPI FluidModelEvent at the container position with water's fluid/block state |
| Four smoothed corner heights | Snapshot loaded same-fluid neighbors and average center/diagonal/cardinal heights independently, retaining original point order and full 0..1 bounds |
| Luminosity disables both lightmap and directional lighting in-world | Select EntityLighting.FULL_BRIGHT; supply full-bright packed light; ordinary fluid uses EntityLighting.LIT |
| Legacy renderColor tag | Read FluidStack CUSTOM_DATA renderColor; in-world placeable-fluid tint retains its precedence over the override |
| Inventory/detached model | Capture all five mirrored model parts with the original unrotated inventory pose; no null-world lookups or forced luminous-world lighting |

ReservoirSurface preserves 1/16 + (14/16 * amount/capacity), with no invented flat
surface or boundary inset. It uses a north-to-south, west-to-east 3x3 level grid;
-1 means absent/different-fluid neighbor. Loaded-level queries replace the old
asymmetric adjacency cache for visual corner capture. Empty neighbors do not
lower a corner's average. Existing live wall-connectivity semantics are retained.

The render state owns primitive walls, cover UV and fluid height/UV/color/light
choice. Each submission copies the values it needs before state reuse; native
collector pose snapshots provide the draw transform. Callbacks never inspect a
block entity, tank, neighboring block or Minecraft.level. The model callback only
uses its captured direction mask and baked model parts. Model/cover visibility
policy is captured independently of the translucent liquid, matching the original
separate model and liquid passes.

## Independent lighting choices

DragonAPI EntityLighting exposes LIT, LIGHTMAP_ONLY, DIRECTIONAL_ONLY and
FULL_BRIGHT, with culled and double-sided translucent variants. Shader defines
select directional lighting and lightmap sampling independently; the sampler is
bound only when the profile uses it. Forward drawing and all three native OIT
stages are declared and registered. All variants use alpha blending, retain depth
testing and disable translucent depth writes. There are no fixed-function global
lighting toggles or persistent current-profile variables.

This distinction matters: the old disableEntityLighting toggled the lightmap
texture unit only. It did not establish whether GL_LIGHTING was on. Each remaining
legacy caller must choose a profile from its actual surrounding draw state.
D06 remains open for those callers and retirement of the no-op helper APIs.

## Verification milestones

- DragonAPI 260 unit cases and RotaryCraft 38 cases passed, with zero failures,
  errors or skips. Four new surface regressions cover original golden corner
  values, absent neighbors, input-array reuse and bottom/full-height bounds.
  Log: build/d06-reservoir-unit-compile.log.
- The first full isolated native client passed in 4m25s:
  build/d06-reservoir-client.log. RESERVOIR_RENDERER_CLIENT_AUDIT_PASS compiled all
  32 profile/cull/forward/OIT pipeline variants, checked shader flags and sampler
  bindings, and decoded real native ENTITY meshes for walls, cover, fluid and lava.
  It verified biome/model tint, water model replacement, component precedence,
  same-fluid corner averaging and different-fluid boundaries. Client cells were
  restored and render state reused before replaying old callbacks successfully.
- The full configured external-mod rotarycraft:* composite dedicated profile
  passed all 1,004 required tests in 2m01s:
  build/d06-reservoir-dedicated.log. Two new tests exercise component colors and
  in-world native-tint precedence without loading client rendering classes.
- Final real-scene acceptance passed in 4m21s:
  build/d06-reservoir-scene-client.log. An audit-only subclass delegates the
  original renderer's extraction/drawing unchanged. Lit water, full-bright lava
  and glass cover completed native world rendering in both classic and improved
  transparency, as checked after native feature execution/OIT composition.
  RESERVOIR_SCENE_CLIENT_AUDIT_PASS, all other client regressions and clean shutdown
  passed. This tests actual draw-time bindings as well as shader compilation.
  Normal Play's renderer provider is untouched.

The scene and mesh fixtures are confined to the automated client and client-only
cells; they do not change the saved server world. Normal Play does not replace
its renderer provider with the observer. Owner shader-pack/Iris/LOD acceptance
remains N08 work; GPU compilation and native world drawing are not proof of every
shader pack's lighting or shadow behavior.


D06 remains unchecked. The only remaining external compiled references to old
lighting helpers are in RenderBevel's dormant face-number method; 95 qualified
source calls in 62 files remain in the wider legacy review. Current inventories
and exact validation hashes are in build/d06-reservoir-proof.json. Per-run copied
water PNG evidence is pinned there so subsequent audits cannot overwrite it.


## Final orientation parity acceptance

The saved original renderer applies the default 90-degree world rotation only
when in a world. Original MachineRegistry declares reservoir metadata zero and
one placement direction. Detached/inventory rendering uses the scale/translation
without that world rotation. The modern model now also restores all five original
mirror flags. World parts retain north/south/west/east order followed by the
bottom; inventory parts use the original model order.

Final combined unit/client run passed in 10m38s:
`build/d06-reservoir-orientation-client.log`. Golden transformed-point checks map
local (1,2,3) to (3.5,-0.5,1.5) in world rendering and (1.5,-0.5,-2.5) in inventory
rendering. The native inventory mesh retains all 120 vertices. The 32 GPU pipeline
compilations, all reservoir and water regressions, both actual world transparency
modes and clean shutdown passed again. DragonAPI's 260 and RotaryCraft's 38 unit
cases have zero failures/errors/skips. The earlier 1,004-case dedicated result
covers the unchanged common component/color implementation; the final parity
changes are client model/renderer changes.

The evidence verifier independently reproduced all twelve current water PNG
measurements. Maximum screen-channel errors were 0.4008 byte values in classic
and 0.3202 in OIT (rounded upward); opaque occlusion differences were zero and
0.4286 summed RGB byte values, within the unchanged strict bound of three.
Logs, PNGs and lighting inventories are copied into the unique directory recorded
by `build/d06-reservoir-proof.json`, with file hashes. Subsequent runs cannot
overwrite those pinned copies. Play VM/program arguments still match their prior
SHA-256 hashes. Approval-service quota interruption was resolved after its stated
reset; no verification or checkpoint action is left pending for that reason.


Correction during the bevel source audit: the five legacy mirror=true field
assignments occur after ModelBox construction; compiled groups explicitly clear
mirror before copying. The modern CubeListBuilder.mirror() additions in the
previous checkpoint were incorrect and have been removed. All other reservoir
geometry, lighting and tint contracts remain as documented. The final bevel
client run rechecks the reservoir meshes and actual world rendering.
