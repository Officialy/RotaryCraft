# Bevel renderer: captured native state

Target: Minecraft 26.3 / NeoForge 26.3.0.76. Original reference:
RotaryCraft 556105c29^, Renders/RenderBevel.java, Models/Animated/ModelBevel.java,
Base/RotaryTERenderer.java and Base/RotaryModelBase.java. The legacy LOD model
reference is DragonAPI de1bcf1f, Instantiable/Rendering/LODModelPart.java.

Extraction captures the mounting direction, animation angle, packed light,
model visibility, face-marker alpha/mode and all IO connections. Submission and
its deferred callbacks retain no block entity or world reads. Each callback uses
the native captured pose and copies the primitive state before the reusable
render state can change. Model visibility does not suppress independent overlays.

All 24 original mounting transforms and animation signs remain. Inventory uses
its original fixed Y=90 orientation, independent of direction. Animated gear
groups use balanced pose pushes/pops around the original Z and inverse-X pivots;
there is no inverse-rotation accumulation between groups.

Original colored faces and colorblind numbers are restored. Numbers appear only
when iotick>64 and colorblind mode is enabled; they are centered, white, full-bright
and have no shadow. Other frames use the six original cyan/blue/yellow/black/
orange/magenta face colors, bounds expanded by 1/16, face loops and three-block
rays. Filled alpha is original integer alpha/3 except SOUTH, which uses
(int)(alpha/2.4). Division precedes clamping, preserving the goggles' 512 fade.
The source compass call was commented out. Its full geometry is implemented but
remains inactive. Goggles refresh IO after capturing face alpha, matching source
ordering; connection colors and pointing offsets are captured explicitly.

IO fill and line render types now bind Minecraft's matching improved-transparency
pipeline sets. Native untextured shaders own lighting and blend state. Global
lighting/texturing toggles are unnecessary. Normal Play remains isolated from
audit-only renderer observers and the audit world/camera/options are restored.

## Model mirror correction

The earlier reservoir checkpoint incorrectly inferred mirrored boxes from legacy
mirror=true fields. ModelBox construction occurs inside addBox before these
fields are assigned. For compiled groups, RotaryModelBase.setRotation explicitly
sets mirror=false before copying geometry. Accordingly, the native bevel and
reservoir boxes are unmirrored; copying final legacy field values into a modern
CubeListBuilder.mirror() would change the baked geometry. This corrects the
previous reservoir documentation's mirror-parity claim.

## Acceptance

BevelRendererClientChecks uses the registered renderer, real native ENTITY mesh
buffers for all 24 deferred mounts, render-state mutation before replay, fixed
inventory orientation, all six source face colors/alpha values and centered
numbers. It validates actual line vertex layouts (native line vertices are
duplicated for expansion) and compiles both IO forward/OIT pipeline families.
BevelSceneClientChecks observes actual production extraction, submission and
native callback execution for an angled animated bevel, face markers and both IO
connections, followed by completed frames in classic and improved transparency.
It delegates production rendering unchanged and runs only in Automated Checks.

Validation results are recorded in the project ledger after the final run.


An independent original-source comparison matched all 24 mounting translations,
rotations and animation signs, plus all 18 model box dimensions/origins, texture
coordinate origins, pivots and local rotations. Machine-readable source parity:
build/d06-bevel-source-parity.json. This complements native mesh acceptance;
it does not claim an automated screenshot comparison against Minecraft 1.7.10.


Final client acceptance passed in 6m26s: build/d06-bevel-final-client.log.
Both world-rendering modes, native mesh/overlay/profile checks, reservoir
regressions, all twelve water screenshot samples, complete client acceptance
and shutdown/cleanup passed. DragonAPI 260 and RotaryCraft 38 unit cases have
zero failures/errors/skips. Compiled bytecode has zero references to the retired
global lighting APIs. Evidence is pinned via build/d06-bevel-proof.json; normal
Play argument hashes are unchanged. The earlier attempt timed out during an
existing resource-pack input acceptance, before bevel checks; the sequential
rerun passed it and the whole suite. Excluded legacy callers keep D06 open.
