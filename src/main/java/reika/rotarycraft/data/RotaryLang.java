package reika.rotarycraft.data;

import net.minecraft.data.DataGenerator;
import net.neoforged.common.data.LanguageProvider;
import reika.rotarycraft.RotaryCraft;

public class RotaryLang extends LanguageProvider {
    public RotaryLang(DataGenerator gen, String locale) {
        super(gen.getPackOutput(), RotaryCraft.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        add("tab.rotarycraft", "RotaryCraft");
        add("tab.rotarycraft_transmission", "RotaryCraft Transmission");
        add("tab.rotarycraft_tools", "RotaryCraft Tools");
        add("tab.rotarycraft_ores", "RotaryCraft Ore Flakes");
    }
}
