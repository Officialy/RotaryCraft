# Obsolete port assets

`advancedgear.png` preserves the zero-byte file previously stored in
`src/main/resources/assets/rotarycraft/textures/item/`. It contains no artwork and is not
referenced by an item model or registry entry. Minecraft's item atlas directory source still
attempted to decode it, producing a resource-reload error.

The original `Items/Placers/ItemAdvGearPlacer.java` placed metadata 0 (worm gear), 1 (CVT),
2 (coil), and 3 (high gear), using machine rendering. The current separate `WORMGEAR`,
`CVT`, `COIL`, and `HIGHGEAR` registrations retain these machines. `RoCModelProvider`
generates their `rotarycraft:machine` special item models using the corresponding original
`WormModel`, `CVTModel`, `CoilModel`, and `HighGearModel` geometry in `MachineModels`.
The creative coil also has its own registration and coil model. No registered content,
texture artwork, or rendering behavior is removed by archiving the empty file.
