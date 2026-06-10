package reika.rotarycraft.data;

import net.minecraft.data.PackOutput;
import net.neoforged.neoforge.common.data.LanguageProvider;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.RotaryBlocks;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Locale;

/**
 * en_us translations for RotaryCraft.
 * <p>
 * Iterates over the mod's DeferredRegisters and emits a humanised name for each block / item
 * (e.g. {@code wood_flywheel} → {@code "Wood Flywheel"}). Tab labels are added on top.
 * <p>
 * 1.21.5 / NeoForge 26.x: {@code LanguageProvider} now takes a {@link PackOutput} directly
 * (no DataGenerator wrapper) and lives at {@code net.neoforged.neoforge.common.data.LanguageProvider}.
 */
public class RotaryLang extends LanguageProvider {

    public RotaryLang(PackOutput output, String locale) {
        super(output, RotaryCraft.MODID, locale);
    }

    @Override
    protected void addTranslations() {
        // Creative tabs / categories.
        add("tab.rotarycraft", "RotaryCraft");
        add("tab.rotarycraft_transmission", "RotaryCraft Transmission");
        add("tab.rotarycraft_tools", "RotaryCraft Tools");
        add("tab.rotarycraft_ores", "RotaryCraft Ore Flakes");
        add("tab.rotarycraft.all", "RotaryCraft (All)");

        // Block translations — derived from each block's registry path.
        RotaryBlocks.BLOCKS.getEntries().forEach(holder ->
                addBlock(holder, prettify(holder.getId().getPath())));

        // Item translations for the dedicated items register (RotaryBlocks.ITEMS holds the
        // auto-generated BlockItems, which inherit their block's translation key — registering
        // them again would emit a duplicate-key warning, so they're skipped here).
        RotaryItems.ITEMS.getEntries().forEach(holder ->
                addItem(holder, prettify(holder.getId().getPath())));
    }

    /**
     * Convert {@code wood_flywheel} → {@code "Wood Flywheel"}. Acronym whitelist preserves
     * all-caps for tokens like {@code hsla} → "HSLA", {@code dc} → "DC", {@code ac} → "AC",
     * {@code emp} → "EMP", {@code tnt} → "TNT" — vanilla title-casing would render these as
     * "Hsla Steel Plate" / "Dc Engine" which reads poorly for the established acronyms in
     * RotaryCraft's UI.
     */
    private static final java.util.Set<String> ACRONYMS = java.util.Set.of(
            "hsla", "dc", "ac", "emp", "tnt", "cvt", "io", "cctv", "gpr", "rc", "ic"
    );

    private static String prettify(String path) {
        String[] parts = path.split("_");
        StringBuilder sb = new StringBuilder();
        for (int i = 0; i < parts.length; i++) {
            if (i > 0) sb.append(' ');
            String p = parts[i];
            if (p.isEmpty()) continue;
            if (ACRONYMS.contains(p)) {
                sb.append(p.toUpperCase(Locale.ROOT));
            } else {
                sb.append(Character.toUpperCase(p.charAt(0)));
                if (p.length() > 1) sb.append(p.substring(1).toLowerCase(Locale.ROOT));
            }
        }
        return sb.toString();
    }
}
