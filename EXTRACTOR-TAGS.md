# Extractor ore tags and custom families — Minecraft 26.3

The extractor reads reloadable `rotarycraft:extractor` recipes. All nine vanilla ore families,
the original 90 modded families (copper now shares the vanilla chain), and raw iron are supported.
The restored modded catalog contributes 89 complete four-stage chains, 89 furnace conversions,
and three special Nether crushing variants. Missing mods leave empty input tags; they do not
prevent recipes from loading. All original modded intermediates/products have their original
V33a sprite cells and readable names.

## Connect another mod's ore to a supported family

Add its block items to the appropriate common **item** tag. For tin, put this in
`data/c/tags/item/ores/tin.json` in your datapack:

```json
{"values": ["yourmod:tin_ore", "yourmod:deepslate_tin_ore"]}
```

Publish its material under `data/c/tags/item/ingots/tin.json`:

```json
{"values": ["yourmod:tin_ingot"]}
```

The input tag selects the family automatically. The complete chain produces tin flakes.
The furnace prefers a product from outside RotaryCraft in the product tag, ordered by full
registry ID for stable selection. It keeps the original drop count and XP. When none is
available, RotaryCraft's original product is used and is itself published in that tag.
Datapacks can replace the generated smelting recipe to choose a specific mod's product.
ElectriCraft and ReactorCraft publish their ores/products in common item and block tags.
No runtime ore-dictionary cache or third-party mod class is required.

Each modded family has an aggregate input tag `rotarycraft:extractor/ores/<family path>`
containing optional references to the common aliases below. Add further aliases to that
tag if a mod uses a different naming convention. Copper's tetrahedrite alias feeds
`c:ores/copper`, retaining its existing vanilla chain.

## Multiplication and secondary products

Ordinary modded ores duplicate at 50% per stage, rare ores at 90%, and catalog Nether ores
at 80% (rare takes precedence), throughout all four stages. The bedrock drill guarantees
double output at stage zero. Explicit `duplication_chance`, used for raw iron's 23% balance,
takes precedence over that guarantee; `ore_duplication_chance` applies after it.
Nether Force, Mimichite and Essence use `c:ores/nether_force`, `c:ores/nether_mimichite`, and
`c:ores/nether_essence`: only crushing gets 80%, matching V33a's special metadata variants.

Recipe `priority` defaults to zero. Larger values win; equal values are ordered by recipe ID.
Catalog recipes use 10, Nether-specific recipes 20. A pack can use 100 for a specific override.

Final-stage recipes carry ordered `bonuses`, for example:

```json
"bonuses": [
  {"output": {"id": "rotarycraft:mod_pitchblende_flakes"}, "chance": 0.0625,
   "required_ore": "rotarycraft:extractor/ores/pitchblende"},
  {"output": {"id": "minecraft:gunpowder"}, "chance": 0.0625}
]
```

The first available entry is selected, then its chance is rolled. `required_ore` is an
optional item-tag ID; an empty tag disables that alternative. Ore availability replaces
the original mod-presence gates. This restores coal's pitchblende/uranium alternatives,
lead/nickel/platinum/iridium byproducts, aluminum from several mineral families, certus/quartz,
and the remaining V33a table. The original integer-reciprocal bonus roll is preserved:
pyrite's declared 40% actually rolled at 50%, and monazit's 15% at one in six.
Recipes without `bonuses` retain the existing vanilla bonus lookup. The default aluminum
bonus is alloy powder; a pack can select aluminum flakes in the relevant recipe instead.
JEI shows the configured multiplication and available secondary product.

## Define an entirely new ore family

Use the four reusable items `rotarycraft:custom_ore_dust`, `custom_ore_slurry`,
`custom_ore_solution`, and `custom_ore_flakes`. Store a unique namespaced
`rotarycraft:extract_family` component on every output and match it in the following stage.
This component is persisted and synchronized. Different families cannot merge or substitute
for one another. `minecraft:custom_name` provides their names, and
`minecraft:custom_model_data.colors[0]` tints the original generic extract sprite.
A resource pack or `minecraft:item_model` can supply more elaborate artwork.

Custom ore blocks can use any item tag; membership in `c:ores` is not required when a matching
recipe exists. GUI input and automation both validate the synchronized recipes, including
component ingredients. Recipe changes take effect on datapack reload.

The following illustrative family uses a tagged block, zero duplication, green intermediates,
and two emeralds per flake. It is an opt-in example, not a shipped gameplay recipe. Replace
the example identifiers, yield and product with your ore's actual values. Put the ore in
`data/yourpack/tags/item/ores/example.json`, then add these five recipe files.
The NeoForge 26.3 ingredient discriminator is `neoforge:ingredient_type`.

`data/yourpack/recipe/extractor/example_stage_0.json`:

```json
{
  "type": "rotarycraft:extractor",
  "stage": 0,
  "input": "#yourpack:ores/example",
  "duplication_chance": 0,
  "priority": 100,
  "output": {
    "id": "rotarycraft:custom_ore_dust",
    "components": {
      "rotarycraft:extract_family": "yourpack:example",
      "minecraft:custom_name": {
        "text": "Example Dust"
      },
      "minecraft:custom_model_data": {
        "colors": [
          7523394
        ]
      }
    }
  }
}
```

`data/yourpack/recipe/extractor/example_stage_1.json`:

```json
{
  "type": "rotarycraft:extractor",
  "stage": 1,
  "input": {
    "neoforge:ingredient_type": "neoforge:components",
    "items": "rotarycraft:custom_ore_dust",
    "components": {
      "rotarycraft:extract_family": "yourpack:example"
    }
  },
  "duplication_chance": 0,
  "priority": 100,
  "output": {
    "id": "rotarycraft:custom_ore_slurry",
    "components": {
      "rotarycraft:extract_family": "yourpack:example",
      "minecraft:custom_name": {
        "text": "Example Slurry"
      },
      "minecraft:custom_model_data": {
        "colors": [
          7523394
        ]
      }
    }
  }
}
```

`data/yourpack/recipe/extractor/example_stage_2.json`:

```json
{
  "type": "rotarycraft:extractor",
  "stage": 2,
  "input": {
    "neoforge:ingredient_type": "neoforge:components",
    "items": "rotarycraft:custom_ore_slurry",
    "components": {
      "rotarycraft:extract_family": "yourpack:example"
    }
  },
  "duplication_chance": 0,
  "priority": 100,
  "output": {
    "id": "rotarycraft:custom_ore_solution",
    "components": {
      "rotarycraft:extract_family": "yourpack:example",
      "minecraft:custom_name": {
        "text": "Example Solution"
      },
      "minecraft:custom_model_data": {
        "colors": [
          7523394
        ]
      }
    }
  }
}
```

`data/yourpack/recipe/extractor/example_stage_3.json`:

```json
{
  "type": "rotarycraft:extractor",
  "stage": 3,
  "input": {
    "neoforge:ingredient_type": "neoforge:components",
    "items": "rotarycraft:custom_ore_solution",
    "components": {
      "rotarycraft:extract_family": "yourpack:example"
    }
  },
  "duplication_chance": 0,
  "priority": 100,
  "output": {
    "id": "rotarycraft:custom_ore_flakes",
    "components": {
      "rotarycraft:extract_family": "yourpack:example",
      "minecraft:custom_name": {
        "text": "Example Flakes"
      },
      "minecraft:custom_model_data": {
        "colors": [
          7523394
        ]
      }
    }
  }
}
```

`data/yourpack/recipe/extractor/example_smelting.json`:

```json
{
  "type": "minecraft:smelting",
  "category": "misc",
  "ingredient": {
    "neoforge:ingredient_type": "neoforge:components",
    "items": "rotarycraft:custom_ore_flakes",
    "components": {
      "rotarycraft:extract_family": "yourpack:example"
    }
  },
  "result": {
    "id": "minecraft:emerald",
    "count": 2
  },
  "experience": 0.8,
  "cookingtime": 200
}
```

## Original modded family catalog

The family path names its aggregate input tag, `mod_<path>_<stage>` items and
`extractor/mod_<path>_stage_<0..3>` recipes. Copper uses the existing vanilla item/recipe names.
The count column is the output per smelted flake. Standard/rare XP is 0.7/1; `EVERYWHERE`
families use 0.5. Existing vanilla copper retains its ported furnace XP.

| Family | Family path | Common input tags | Product tag | Count | Duplication per stage |
|---|---|---|---|---:|---:|
| Tin | `tin` | `c:ores/tin`, `c:ores/cassiterite` | `c:ingots/tin` | 1 | 50% |
| Copper | `copper` | `c:ores/copper`, `c:ores/tetrahedrite` | `c:ingots/copper` | 1 | 50% |
| Lead | `lead` | `c:ores/lead` | `c:ingots/lead` | 1 | 50% |
| Nickel | `nickel` | `c:ores/nickel`, `c:ores/pentlandite` | `c:ingots/nickel` | 1 | 50% |
| Silver | `silver` | `c:ores/silver` | `c:ingots/silver` | 1 | 50% |
| Galena | `galena` | `c:ores/galena` | `c:dusts/galena` | 1 | 50% |
| Aluminum | `aluminum` | `c:ores/aluminum`, `c:ores/aluminium`, `c:ores/natural_aluminum` | `c:ingots/aluminum` | 1 | 50% |
| Iridium | `iridium` | `c:ores/iridium` | `c:ingots/iridium` | 1 | 90% |
| Firestone | `firestone` | `c:ores/firestone` | `c:shards/firestone` | 1 | 90% |
| Certus Quartz | `certus_quartz` | `c:ores/certus_quartz`, `c:ores/certus_quartz_charged`, `c:ores/charged_certus_quartz` | `c:gems/certus_quartz` | 2 | 50% |
| Uranium | `uranium` | `c:ores/uranium`, `c:ores/yellorite`, `c:ores/uraninite` | `c:ingots/uranium` | 1 | 50% |
| Mercury | `cinnabar` | `c:ores/cinnabar` | `c:items/quicksilver` | 1 | 50% |
| Amber | `amber` | `c:ores/amber` | `c:gems/amber` | 1 | 50% |
| Air Infused | `infused_air` | `c:ores/infused_air` | `c:shards/air` | 2 | 50% |
| Fire Infused | `infused_fire` | `c:ores/infused_fire` | `c:shards/fire` | 2 | 50% |
| Water Infused | `infused_water` | `c:ores/infused_water` | `c:shards/water` | 2 | 50% |
| Earth Infused | `infused_earth` | `c:ores/infused_earth` | `c:shards/earth` | 2 | 50% |
| Entropy Infused | `infused_entropy` | `c:ores/infused_entropy` | `c:shards/entropy` | 2 | 50% |
| Order Infused | `infused_order` | `c:ores/infused_order` | `c:shards/order` | 2 | 50% |
| Apatite | `apatite` | `c:ores/apatite` | `c:gems/apatite` | 3 | 50% |
| Saltpeter | `saltpeter` | `c:ores/saltpeter` | `c:dusts/saltpeter` | 2 | 50% |
| Tungsten | `tungsten` | `c:ores/tungsten`, `c:ores/tungstate` | `c:dusts/tungsten` | 1 | 50% |
| Nikolite | `nikolite` | `c:ores/nikolite`, `c:ores/electrotine` | `c:dusts/electrotine` | 5 | 50% |
| Peridot | `peridot` | `c:ores/peridot` | `c:gems/peridot` | 1 | 50% |
| Ruby | `ruby` | `c:ores/ruby` | `c:gems/ruby` | 1 | 50% |
| Sapphire | `sapphire` | `c:ores/sapphire` | `c:gems/sapphire` | 1 | 50% |
| Monazit | `monazit_ore` | `c:ores/monazit`, `c:ores/monazite`, `c:ores/monazit_ore` | `c:items/forcicium_item` | 4 | 50% |
| Force | `force` | `c:ores/force` | `c:gems/force` | 3 | 50% |
| Nether Coal | `nether_coal` | `c:ores/nether_coal` | `c:items/coal` | 1 | 80% |
| Nether Iron | `nether_iron` | `c:ores/nether_iron` | `c:ingots/iron` | 1 | 80% |
| Nether Gold | `nether_gold` | `c:ores/nether_gold` | `c:ingots/gold` | 1 | 80% |
| Nether Redstone | `nether_redstone` | `c:ores/nether_redstone` | `c:dusts/redstone` | 4 | 80% |
| Nether Lapis | `nether_lapis` | `c:ores/nether_lapis` | `c:dyes/blue` | 6 | 80% |
| Nether Diamond | `nether_diamond` | `c:ores/nether_diamond` | `c:gems/diamond` | 1 | 80% |
| Nether Emerald | `nether_emerald` | `c:ores/nether_emerald` | `c:gems/emerald` | 1 | 80% |
| Nether Tin | `nether_tin` | `c:ores/nether_tin` | `c:ingots/tin` | 1 | 80% |
| Nether Copper | `nether_copper` | `c:ores/nether_copper` | `c:ingots/copper` | 1 | 80% |
| Nether Lead | `nether_lead` | `c:ores/nether_lead` | `c:ingots/lead` | 1 | 80% |
| Nether Nickel | `nether_nickel` | `c:ores/nether_nickel` | `c:ingots/nickel` | 1 | 80% |
| Nether Silver | `nether_silver` | `c:ores/nether_silver` | `c:ingots/silver` | 1 | 80% |
| Nether Nikolite | `nether_nikolite` | `c:ores/nether_nikolite` | `c:dusts/nikolite` | 5 | 80% |
| Cobalt | `cobalt` | `c:ores/cobalt` | `c:ingots/cobalt` | 1 | 50% |
| Ardite | `ardite` | `c:ores/ardite` | `c:ingots/ardite` | 1 | 50% |
| Platinum | `platinum` | `c:ores/platinum`, `c:ores/cooperite` | `c:ingots/platinum` | 1 | 90% |
| Nether Platinum | `nether_platinum` | `c:ores/nether_platinum` | `c:ingots/platinum` | 1 | 90% |
| Zinc | `zinc` | `c:ores/zinc`, `c:ores/sphalerite` | `c:ingots/zinc` | 1 | 50% |
| Osmium | `osmium` | `c:ores/osmium` | `c:ingots/osmium` | 1 | 50% |
| Pig Iron | `nether_pig_iron` | `c:ores/nether_pig_iron`, `c:ores/nether_steel` | `c:ingots/steel` | 1 | 80% |
| Sulfur | `sulfur` | `c:ores/sulfur` | `c:dusts/sulfur` | 3 | 50% |
| Pitchblende | `pitchblende` | `c:ores/pitchblende` | `c:ingots/uranium` | 1 | 50% |
| Cadmium | `cadmium` | `c:ores/cadmium` | `c:ingots/cadmium` | 1 | 50% |
| Indium | `indium` | `c:ores/indium` | `c:ingots/indium` | 1 | 50% |
| Fluorite | `fluorite` | `c:ores/fluorite` | `c:gems/fluorite` | 6 | 50% |
| Bauxite | `bauxite` | `c:ores/bauxite` | `c:dusts/bauxite` | 1 | 50% |
| Sodalite | `sodalite` | `c:ores/sodalite` | `c:dyes/blue` | 1 | 50% |
| Pyrite | `pyrite` | `c:ores/pyrite` | `c:dusts/pyrite` | 1 | 50% |
| Ammonium Chloride | `ammonium` | `c:ores/ammonium` | `c:dusts/ammonium` | 1 | 50% |
| Calcite | `calcite` | `c:ores/calcite` | `c:gems/calcite` | 1 | 50% |
| Chimerite | `chimerite` | `c:ores/chimerite` | `c:gems/chimerite` | 2 | 50% |
| Vinteum | `vinteum` | `c:ores/vinteum` | `c:dusts/vinteum` | 1 | 50% |
| Blue Topaz | `blue_topaz` | `c:ores/blue_topaz` | `c:gems/blue_topaz` | 1 | 50% |
| Moonstone | `moonstone` | `c:ores/moonstone` | `c:gems/moonstone` | 1 | 90% |
| Sunstone | `sunstone` | `c:ores/sunstone` | `c:gems/sunstone` | 1 | 90% |
| Titanium | `titanium` | `c:ores/titanium` | `c:ingots/titanium` | 1 | 50% |
| Magmanite | `magmanite` | `c:ores/magmanite` | `c:drops/magma` | 1 | 50% |
| Magnetite | `magnetite` | `c:ores/magnetite` | `c:gems/magnetite` | 1 | 50% |
| Essence | `essence` | `c:ores/essence`, `c:ores/nether_essence`, `c:ores/end_essence` | `c:items/essence` | 4 | 50% |
| Mimichite | `mimichite` | `c:ores/mimichite` | `c:gems/mimichite` | 1 | 50% |
| Nether Uranium | `nether_uranium` | `c:ores/nether_uranium` | `c:ingots/uranium` | 1 | 80% |
| Quantum | `quantum` | `c:ores/quantum` | `c:dusts/quantum` | 2 | 50% |
| Nether Iridium | `nether_iridium` | `c:ores/nether_iridium` | `c:ingots/iridium` | 1 | 90% |
| Dark Iron | `fz_dark_iron` | `c:ores/fz_dark_iron`, `c:ores/dark_iron` | `c:ingots/fz_dark_iron` | 1 | 50% |
| Chromite | `chromite` | `c:ores/chromite` | `c:ingots/chrome` | 1 | 50% |
| Nether Sapphire | `nether_sapphire` | `c:ores/nether_sapphire` | `c:gems/sapphire` | 1 | 80% |
| Nether Peridot | `nether_green_sapphire` | `c:ores/nether_green_sapphire`, `c:ores/nether_peridot` | `c:gems/peridot` | 1 | 80% |
| Nether Titanium | `nether_titanium` | `c:ores/nether_titanium` | `c:ingots/titanium` | 1 | 80% |
| Nether Sulfur | `nether_sulfur` | `c:ores/nether_sulfur` | `c:dusts/sulfur` | 3 | 80% |
| Nether Osmium | `nether_osmium` | `c:ores/nether_osmium` | `c:ingots/osmium` | 1 | 80% |
| Silicon | `silicon` | `c:ores/silicon` | `c:items/silicon` | 1 | 50% |
| Rutile | `rutile` | `c:ores/rutile` | `c:ingots/rutile` | 1 | 50% |
| Amethyst | `amethyst` | `c:ores/amethyst` | `c:gems/amethyst` | 1 | 90% |
| Teslatite | `teslatite` | `c:ores/teslatite` | `c:dusts/teslatite` | 3 | 50% |
| Mana | `mana` | `c:ores/mana` | `c:ingots/mana` | 1 | 50% |
| Nether Saltpeter | `nether_saltpeter` | `c:ores/nether_saltpeter` | `c:dusts/saltpeter` | 4 | 80% |
| Thorium | `thorium` | `c:ores/thorium`, `c:ores/thorite`, `c:ores/thorianite` | `c:dusts/thorium` | 1 | 50% |
| Draconium | `draconium` | `c:ores/draconium` | `c:dusts/draconium` | 2 | 50% |
| Endium | `endium` | `c:ores/endium`, `c:ores/hee_endium` | `c:ingots/endium` | 1 | 50% |
| Dilithium | `dilithium` | `c:ores/dilithium` | `c:dusts/dilithium` | 1 | 50% |
| Eximite | `eximite` | `c:ores/eximite` | `c:ingots/eximite` | 1 | 50% |
| Meutoite | `meutoite` | `c:ores/meutoite` | `c:ingots/meutoite` | 1 | 50% |
