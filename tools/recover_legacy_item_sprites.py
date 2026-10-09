"""Recover exact original atlas pixels; do not synthesize replacement artwork.

Requires Pillow. Run from any directory. --verify checks current decoded pixels without writing.
The pinned source and sprite mappings are documented in docs/asset-recovery.md.
"""
import argparse
import hashlib
import io
from pathlib import Path
import subprocess
from PIL import Image

SOURCE = "b2288638f7f4703bb4555f72a2b86f72407cc2ae"
ATLASES = {
    "items": "fe702f9cf36ed86a9ea7d09c8af055c7c96eabd166a96056c316aad7de4a6ff6",
    "items2": "b7f88ffe7366bd70a6bb312ec27c024ea9c6e632a3d09a8239cb1e328fde3b7f",
}
SPRITES = {
    "target": ("items2", 98),
    "railgun_ammo": ("items2", 129),
    "railgun_ammo_1": ("items2", 130),
    "railgun_ammo_2": ("items2", 131),
    "railgun_ammo_3": ("items2", 132),
    "molten_hsla_bucket": ("items2", 108),
    "tungsten_alloy_spring": ("items", 108),
}

def main():
    parser = argparse.ArgumentParser(description=__doc__)
    parser.add_argument("--verify", action="store_true")
    args = parser.parse_args()
    repo = Path(__file__).resolve().parents[1]
    atlases = {}
    for name, expected_hash in ATLASES.items():
        source_path = f"Textures/Items/{name}.png"
        data = subprocess.check_output(["git", "-C", str(repo), "show", f"{SOURCE}:{source_path}"])
        if hashlib.sha256(data).hexdigest() != expected_hash:
            raise ValueError(f"Original atlas does not match its recorded source hash: {source_path}")
        with Image.open(io.BytesIO(data)) as image:
            image.load()
            if image.size != (256, 256):
                raise ValueError(f"Original atlas dimensions changed: {source_path}")
            atlases[name] = image.convert("RGBA")
    textures = repo / "src/main/resources/assets/rotarycraft/textures/item"
    backups = repo / "build/legacy-asset-recovery"
    for name, (atlas_name, index) in SPRITES.items():
        x, y = (index % 16) * 16, (index // 16) * 16
        sprite = atlases[atlas_name].crop((x, y, x + 16, y + 16))
        path = textures / f"{name}.png"
        if not args.verify:
            backups.mkdir(parents=True, exist_ok=True)
            backup = backups / f"{name}.before-recovery.png"
            if path.exists() and not backup.exists():
                backup.write_bytes(path.read_bytes())
            sprite.save(path)
        with Image.open(path) as recovered:
            recovered.load()
            if recovered.size != (16, 16) or recovered.convert("RGBA").tobytes() != sprite.tobytes():
                raise ValueError(f"Recovered sprite differs from original pixels: {name}")
        print(f"{name}: {atlas_name}, cell {index}, PNG sha256={hashlib.sha256(path.read_bytes()).hexdigest()}")

if __name__ == "__main__":
    main()
