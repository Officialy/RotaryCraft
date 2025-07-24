/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.auxiliary;

import net.minecraft.client.Minecraft;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceKey;
import net.minecraft.world.entity.player.Player;
import net.neoforged.common.ForgeConfigSpec;
import reika.dragonapi.auxiliary.trackers.PlayerHandler;
import reika.dragonapi.instantiable.Alert;
import reika.dragonapi.interfaces.configuration.ConfigList;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.ConfigRegistry;

import java.util.*;
import java.util.logging.Level;

public class HandbookNotifications {

    public static final HandbookNotifications instance = new HandbookNotifications();

    private final HashMap<UUID, ArrayList<Alert>> data = new HashMap<>();

    private final HashMap<UUID, Boolean> alert = new HashMap<>();

    private HandbookNotifications() {

    }


    public boolean newAlerts() {
        UUID uid = Minecraft.getInstance().player.getUUID();
        return alert.containsKey(uid);
    }


    public void clearAlert() {
        alert.remove(Minecraft.getInstance().player.getUUID());
    }


    public List<Alert> getNewAlerts() {
        Player ep = Minecraft.getInstance().player;
        ArrayList<Alert> li = data.get(ep.getUUID());
        return li != null ? Collections.unmodifiableList(li) : new ArrayList<>();
    }

    private void addAlert(Player ep, ConfigList c, Level lvl, String msg) {
        Alert a = new Alert(c.toString().toLowerCase(Locale.ENGLISH), c, lvl, msg);
        ArrayList<Alert> li = data.computeIfAbsent(ep.getUUID(), k -> new ArrayList<>());
        if (!li.contains(a))
            li.add(a);
    }

    public static class HandbookConfigVerifier implements PlayerHandler.PlayerTracker {

        public static final HandbookConfigVerifier instance = new HandbookConfigVerifier();

        private static final String NBT_TAG = "rc_config_alerts";

        private final HashMap<ConfigRegistry, String> data = new HashMap<>();
        private final HashMap<ConfigRegistry, Level> levels = new HashMap<>();

        private HandbookConfigVerifier() {
            this.addEntry(ConfigRegistry.ALLOWTNTCANNON, Level.INFO, "The TNT Cannon has been disabled.");
            this.addEntry(ConfigRegistry.ALLOWLIGHTBRIDGE, Level.INFO, "The Light Bridge has been disabled.");
            this.addEntry(ConfigRegistry.ALLOWEMP, Level.INFO, "The EMP has been disabled.");
            this.addEntry(ConfigRegistry.ATTACKBLOCKS, Level.WARNING, "Machines like the heat ray and EMP will not break blocks.");
            this.addEntry(ConfigRegistry.RAILGUNDAMAGE, Level.WARNING, "The Railgun will not cause block damage.");
            this.addEntry(ConfigRegistry.BANRAIN, Level.WARNING, "The Silver Iodide Cannon's ability to make rain has been disabled.");
            this.addEntry(ConfigRegistry.BEDPICKSPAWNERS, Level.WARNING, "The bedrock pickaxe's ability to harvest spawners has been disabled.");
            this.addEntry(ConfigRegistry.DIFFICULTY, Level.SEVERE, "The mod difficulty has been changed from the default.");
            this.addEntry(ConfigRegistry.EXTRACTORMAINTAIN, Level.SEVERE, "The Extractor has been set to require drill maintenance.");
            this.addEntry(ConfigRegistry.GRAVELPLAYER, Level.WARNING, "Gravel Gun PvP has been disabled.");
            this.addEntry(ConfigRegistry.HARDGRAVELGUN, Level.SEVERE, "Gravel Gun damage has been reduced.");
            this.addEntry(ConfigRegistry.HSLADICT, Level.INFO, "HSLA has been made usable in other mods' recipes.");
            this.addEntry(ConfigRegistry.INSTACUT, Level.WARNING, "The Woodcutter has been changed to not cut trees as cleanly or effectively.");
            this.addEntry(ConfigRegistry.MODORES, Level.SEVERE, "Forced mod ore compatibility has been disabled.");
            this.addEntry(ConfigRegistry.SPAWNERLEAK, Level.WARNING, "Mob spawn when breaking monster spawners has been disabled.");
            this.addEntry(ConfigRegistry.TABLEMACHINES, Level.INFO, "Machines can be crafted in tables other than the Worktable.");
            this.addEntry(ConfigRegistry.TURRETPLAYERS, Level.WARNING, "Turrets' ability to target players has been disabled.");
            this.addEntry(ConfigRegistry.VOIDHOLE, Level.WARNING, "The bedrock breaker has been made able to open holes to the Void.");
            this.addEntry(ConfigRegistry.NOMINERS, Level.SEVERE, "All automining machines have been disabled.");
            this.addEntry(ConfigRegistry.BORERMAINTAIN, Level.SEVERE, "The Borer has been set to require maintenance.");
            this.addEntry(ConfigRegistry.JETFUELPACK, Level.WARNING, "The jetpack requires jet fuel to operate.");
//todo            this.addEntry(RotaryConfig.COMMON.CONVERTERLOSS.get(), Level.SEVERE, RotaryConfig.COMMON.enableConverters() ? "RC to Mod Power Converter Losses Added." : "RC to Mod Power Converters Disabled.");
//            if (!RotaryConfig.COMMON.enableFermenterYeast())
//                this.addEntry(RotaryConfig.COMMON.BEEYEAST.get(), Level.WARNING, "Yeast is a bee product, not a Fermenter output.");
        }

        private void addEntry(ConfigRegistry cfg, Level lvl, String sg) {
            data.put(cfg, sg);
            levels.put(cfg, lvl);

            if (this.isChanged(cfg)) {
                RotaryCraft.LOGGER.info("Config Change: \"" + cfg.getLabel() + "\" from default!");
            }
        }

        @Override
        public void onPlayerLogin(Player ep) {
            CompoundTag eptag = ReikaPlayerAPI.getDeathPersistentNBT(ep);
            CompoundTag nbt = eptag.contains(NBT_TAG) ? eptag.getCompound(NBT_TAG) : new CompoundTag();
            boolean empty = true;
            for (ConfigRegistry cfg : data.keySet()) {
                String tag = cfg.toString().toLowerCase(Locale.ENGLISH);
                boolean mark = nbt.getBoolean(tag);
                boolean chg = this.isChanged(cfg);
                if (chg != mark)
                    empty = false;
                if (chg) {
//    todo                HandbookNotifications.instance.addAlert(ep, cfg, levels.get(cfg), data.get(cfg));
                }
                nbt.putBoolean(tag, chg);
            }
            if (!empty)
                HandbookNotifications.instance.alert.put(ep.getUUID(), true);
            eptag.put(NBT_TAG, nbt);
        }

        public boolean isChanged(ConfigRegistry option) {
            if (option.isBoolean()) {
                return option.getDefaultState() != option.getState();
            }
            else if (option.isNumeric()) {
                return option.getDefaultValue() != option.getValue();
            }
            else if (option.isDecimal()) {
                return option.getDefaultFloat() != option.getFloat();
            }
            else {
                return true;
            }
        }

        @Override
        public void onPlayerLogout(Player player) {

        }

        @Override
        public void onPlayerChangedDimension(Player player, ResourceKey<net.minecraft.world.level.Level> resourceKey, ResourceKey<net.minecraft.world.level.Level> resourceKey1) {

        }

        @Override
        public void onPlayerRespawn(Player player) {

        }

    }

}
