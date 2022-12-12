package reika.rotarycraft.auxiliary;

import net.minecraft.network.chat.Component;
import net.minecraft.world.item.CreativeModeTab;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryItems;

public class RotaryCraftTabs {

    public static final CreativeModeTab ROTARYCRAFT = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.rotarycraft")).icon(() -> {
        return RotaryItems.HSLA_STEEL_GEAR.get().getDefaultInstance();
    }).build();
    public static final CreativeModeTab ROTARYCRAFT_TRANSMISSION = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.rotarycraft_transmission")).icon(() -> {
        return RotaryItems.BEDROCK_ALLOY_SHAFT.get().getDefaultInstance();
    }).build();
    public static final CreativeModeTab ROTARYCRAFT_TOOLS = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.rotarycraft_tools")).icon(() -> {
        return RotaryItems.BEDROCK_ALLOY_PICK.get().getDefaultInstance();
    }).build();
    public static final CreativeModeTab ROTARY_ORES = CreativeModeTab.builder(CreativeModeTab.Row.TOP, 0).title(Component.translatable("itemGroup.rotary_ores")).icon(() -> {
        return RotaryItems.CUSTOMEXTRACT.get().getDefaultInstance();
    }).build();
}
