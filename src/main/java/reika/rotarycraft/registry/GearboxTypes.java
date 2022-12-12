package reika.rotarycraft.registry;

import net.minecraft.client.resources.language.I18n;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.Tag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.block.Blocks;
import net.minecraftforge.registries.ForgeRegistries;
import reika.dragonapi.ModList;
import reika.dragonapi.auxiliary.EnumDifficulty;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;

import java.util.Locale;

public enum GearboxTypes {

    WOOD(MaterialRegistry.WOOD),
    STONE(MaterialRegistry.STONE),
    STEEL(MaterialRegistry.STEEL),
    TUNGSTEN(MaterialRegistry.TUNGSTEN),
    DIAMOND(MaterialRegistry.DIAMOND),
    BEDROCK(MaterialRegistry.BEDROCK),
    LIVINGWOOD(MaterialRegistry.WOOD, ModList.BOTANIA),
    LIVINGROCK(MaterialRegistry.STONE, ModList.BOTANIA);

    public static final GearboxTypes[] typeList = values();
    public final MaterialRegistry material;
    private final ModList dependency;

    GearboxTypes(MaterialRegistry mat) {
        this(mat, null);
    }

    GearboxTypes(MaterialRegistry mat, ModList mod) {
        material = mat;
        dependency = mod;
    }

    public static GearboxTypes getMaterialFromGearboxItem(ItemStack is) {
//        if (is.getTag() != null && is.getTag().contains("type"))
        return GearboxTypes.valueOf(is.getTag().getString("type"));
    }

    public static GearboxTypes getMaterialFromCraftingItem(ItemStack is) {
        Tag idx = is.getTag().get("material");
//        for (GearboxTypes g : typeList) {
//            if (g == idx)
//                return g;
//        }
        return null;
    }

    public static GearboxTypes getFromMaterial(MaterialRegistry mat) {
        return switch (mat) {
            case BEDROCK -> GearboxTypes.BEDROCK;
            case DIAMOND -> GearboxTypes.DIAMOND;
            case STEEL -> GearboxTypes.STEEL;
            case STONE -> GearboxTypes.STONE;
            case WOOD -> GearboxTypes.WOOD;
            case TUNGSTEN -> GearboxTypes.TUNGSTEN;
        };
    }

    public boolean isLoadable() {
        return dependency == null || dependency.isLoaded();
    }

    public boolean needsLubricant() {
        return this != WOOD && this.isDamageableGear();
    }

    public boolean consumesLubricant() {
        return this.needsLubricant() && this != DIAMOND;
    }

    public boolean acceptsDiamondUpgrade() {
        return this.consumesLubricant() && this != STONE;
    }

    public float getLubricantConsumeRate(int omegain) {
        return switch (this) {
            case STONE -> Math.max(1, 1 + omegain / 8192F);
            case TUNGSTEN -> Math.min(1, 0.5F + Math.max(0, 0.03125F * (ReikaMathLibrary.logbase2(omegain) - 2)));
            default -> 1;
        };
    }

    public boolean generatesHeat(int omega, int Tamb) {
        return switch (this) {
            case WOOD -> true;
            case LIVINGWOOD -> omega >= 2048 || Tamb >= 100;
            case STONE -> omega >= 8192;
            default -> false;
        };
    }

    public boolean takesTemperatureDamage() {
        return this == WOOD || this == STONE;
    }

    public boolean isDamageableGear() {
        return !material.isInfiniteStrength();
    }

    public String getBaseGearboxTexture() {
        String tex = "geartex";
        switch (this) {
            case BEDROCK -> tex = tex + "b";
            case DIAMOND -> tex = tex + "d";
            case STONE -> tex = tex + "s";
            case TUNGSTEN -> tex = tex + "t";
            case WOOD -> tex = tex + "w";
            case LIVINGROCK -> tex = tex + "s_living";
            case LIVINGWOOD -> tex = tex + "w_living";
            default -> {
            }
        }
        return tex + ".png";
    }

    public ItemStack getPart(GearPart part) {
        return RotaryItems.HSLA_STEEL_GEAR.get().getDefaultInstance();//todo (metaOffset * 16 + part.ordinal());
    }

    public ItemStack getShaftUnitItem() {
        if (this == WOOD)
            return new ItemStack(Items.STICK);
        if (this == TUNGSTEN && DifficultyEffects.getDifficulty() == EnumDifficulty.EASY)
            return STEEL.getShaftUnitItem();
        return RotaryItems.HSLA_STEEL_GEAR.get().getDefaultInstance();//todo .getStackOfMetadata(metaOffset * 16);
    }

    public Object getBaseItem() {
        return switch (this) {
            case WOOD -> "plankWood";
            case STONE -> new ItemStack(Blocks.STONE);
            case LIVINGWOOD ->
                    new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "livingwood")));
            case LIVINGROCK ->
                    new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "livingrock")));
            case STEEL -> RotaryItems.HSLA_STEEL_INGOT;
            case TUNGSTEN -> RotaryItems.TUNGSTEN_ALLOY_INGOT;
            case DIAMOND -> Items.DIAMOND;
            case BEDROCK -> RotaryItems.BEDROCK_ALLOY_INGOT;
            default -> Blocks.AIR;
        };
    }

    public Object getMountItem() {
        return switch (this) {
            case WOOD -> "plankWood";
            case STONE -> Blocks.STONE_SLAB.asItem().getDefaultInstance();
            case LIVINGWOOD ->
                    new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "livingwood0Slab")));
            case LIVINGROCK ->
                    new ItemStack(ForgeRegistries.BLOCKS.getValue(new ResourceLocation(ModList.BOTANIA.modid, "livingrock0Slab")));
            default -> RotaryItems.MOUNT;
        };
    }

    public int getMaxLubricant() {
        return switch (this) {
            case BEDROCK -> 0;
            case DIAMOND -> 1000;
            case STEEL, TUNGSTEN -> 24000;
            case STONE, LIVINGROCK -> 8000;
            case WOOD, LIVINGWOOD -> 0;//3000;
        };
    }

    public boolean acceptsBearingUpgrade(GearboxTypes mat) {
        return switch (this) {
            case BEDROCK, LIVINGWOOD, LIVINGROCK -> false;
            case WOOD -> false;//mat == GearboxTypes.STONE;
            default -> mat != WOOD && mat.material.ordinal() <= material.ordinal() + 2 && !mat.isSpecialType();
        };
    }

    private boolean isSpecialType() {
        return switch (this) {
            case LIVINGWOOD, LIVINGROCK -> true;
            default -> false;
        };
    }

    public ItemStack getGearboxItem(int ratio) {
        //return this.getGearboxItemByIndex(ReikaMathLibrary.logbase2(ratio) - 1);

        ItemStack is = MachineRegistry.GEARBOX.getBlockState().getBlock().asItem().getDefaultInstance();
        is.getOrCreateTag().putString("type", this.name());
        return is;
    }

    public String getLocalizedGearboxName(int ratio) {
        return I18n.get("material." + this.name().toLowerCase(Locale.ENGLISH)) + " " + ratio + ":1 " + MachineRegistry.GEARBOX.getName();
    }

    public double getOmegaForRotFailure(int omega, int omegain) {
        return ReikaMathLibrary.doubpow(Math.max(omega, omegain), material.getSpeedForceExponent());
    }

    public enum GearPart {
        SHAFT(),
        GEAR(),
        UNIT2(),
        UNIT4(),
        UNIT8(),
        UNIT16(),
        BEARING(),
        SHAFTCORE();

        public static final GearPart[] list = values();

        public static GearPart getGearUnitPartItemFromRatio(int r) {
            switch (r) {
                case 2:
                    return GearPart.UNIT2;
                case 4:
                    return GearPart.UNIT4;
                case 8:
                    return GearPart.UNIT8;
                case 16:
                    return GearPart.UNIT16;
            }
            throw new IllegalArgumentException("Invalid gear ratio!");
        }

        public String getRegistryName(GearboxTypes material) {
            String s = switch (this) {
                case SHAFT -> "crafting.shaft";
                case GEAR -> "crafting.gear";
                case UNIT2 -> "crafting.gear2x";
                case UNIT4 -> "crafting.gear4x";
                case UNIT8 -> "crafting.gear8x";
                case UNIT16 -> "crafting.gear16x";
                case BEARING -> "crafting.bearing";
                case SHAFTCORE -> "crafting.shaftcore";
            };
            return I18n.get("material." + material.name().toLowerCase(Locale.ENGLISH)) + " " + I18n.get(s);
        }

        public boolean isItemOfType(ItemStack is) {
            for (GearboxTypes type : GearboxTypes.typeList) {
                if (type.getPart(this) == is)
                    return true;
            }
            return false;
        }
    }
}
