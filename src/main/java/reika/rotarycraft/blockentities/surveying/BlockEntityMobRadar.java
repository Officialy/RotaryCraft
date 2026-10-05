/*******************************************************************************
 * @author Reika Kalseki
 * Copyright 2017
 * All rights reserved. Distribution requires the owner's explicit prior permission.
 ******************************************************************************/
package reika.rotarycraft.blockentities.surveying;
import java.util.List;
import net.minecraft.core.BlockPos;
import net.minecraft.core.registries.BuiltInRegistries;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.network.chat.Component;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.animal.Animal;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.inventory.AbstractContainerMenu;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.AABB;
import reika.dragonapi.interfaces.blockentity.GuiController;
import reika.dragonapi.libraries.ReikaEntityHelper;
import reika.rotarycraft.api.interfaces.RadarJammer;
import reika.rotarycraft.auxiliary.interfaces.RangedEffect;
import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
import reika.rotarycraft.data.RoCItemTagsProvider;
import reika.rotarycraft.gui.container.machine.ContainerMobRadar;
import reika.rotarycraft.registry.*;

/** V33a full-height square scanner; synced contacts work beyond client entity tracking. */
public class BlockEntityMobRadar extends BlockEntityPowerReceiver implements GuiController, RangedEffect {
    public static final int FALLOFF = 1024;
    public String owner = "";
    public boolean hostile = true, animal = true, player = true;
    private List<LivingEntity> inzone = List.of();
    private List<Contact> contacts = List.of();
    private boolean jammed;
    public BlockEntityMobRadar(BlockPos pos, BlockState state) { super(RotaryBlockEntities.MOB_RADAR.get(), pos, state); }
    public List<LivingEntity> getEntities() { return inzone; }
    public List<Contact> getContacts() { return contacts; }
    public boolean isJammed() { return jammed; }
    @Override public int getRange() { return (int)Math.clamp(8 + (power - MINPOWER) / FALLOFF, 0L, getMaxRange()); }
    @Override public int getMaxRange() { return 256; }
    public void updateEntity(Level world, BlockPos pos) {
        super.updateBlockEntity();
        if (world.isClientSide()) return;
        getPowerBelow();
        int range = getRange();
        var zone = new AABB(pos.getX() - range, world.getMinY(), pos.getZ() - range,
                pos.getX() + 1 + range, world.getMaxY() + 1, pos.getZ() + 1 + range);
        inzone = List.copyOf(world.getEntitiesOfClass(LivingEntity.class, zone, this::acceptsEntity));
        jammed = inzone.stream().anyMatch(entity -> entity instanceof RadarJammer jammer && jammer.jamRadar(world, pos));
        contacts = inzone.stream().map(Contact::of).toList();
    }
    public boolean acceptsEntity(LivingEntity entity) {
        if (entity instanceof Player) return player;
        if (entity instanceof Animal) return animal;
        return hostile && ReikaEntityHelper.isHostile(entity);
    }
    public boolean canShowHud(Player viewer) {
        if (viewer == null || getPlacerID() == null || !getPlacerID().equals(viewer.getUUID())) return false;
        for (int slot = 0; slot < 36; slot++) if (viewer.getInventory().getItem(slot).is(RoCItemTagsProvider.MOTION_TRACKERS)) return true;
        return false;
    }
    @Override protected void animateWithTick(Level world, BlockPos pos) {
        if (world == null) phi = 0;
        else if (power >= MINPOWER) phi = (phi + 4) % 360;
    }
    @Override protected void writeSyncTag(CompoundTag tag) {
        super.writeSyncTag(tag); tag.putString("own", owner); tag.putBoolean("jam", jammed);
        tag.putBoolean("hostile", hostile); tag.putBoolean("animal", animal); tag.putBoolean("player", player);
        var list = new ListTag(); for (var contact : contacts) list.add(contact.save()); tag.put("radarContacts", list);
    }
    @Override protected void readSyncTag(CompoundTag tag) {
        super.readSyncTag(tag); owner = tag.getStringOr("own", ""); jammed = tag.getBooleanOr("jam", false);
        hostile = tag.getBooleanOr("hostile", true); animal = tag.getBooleanOr("animal", true); player = tag.getBooleanOr("player", true);
        var list = tag.getListOrEmpty("radarContacts"); var loaded = new java.util.ArrayList<Contact>();
        for (int index = 0; index < list.size(); index++) loaded.add(Contact.load(list.getCompoundOrEmpty(index)));
        contacts = List.copyOf(loaded);
        // Live entity references are refreshed by the next server scan rather than loaded from disk.
        inzone = List.of();
    }
    @Override public MachineRegistry getMachine() { return MachineRegistry.MOBRADAR; }
    @Override protected String getTEName() { return "mobradar"; }
    @Override public Block getBlockEntityBlockID() { return RotaryBlocks.MOB_RADAR.get(); }
    @Override public boolean hasModelTransparency() { return false; }
    @Override public boolean hasAnInventory() { return false; }
    @Override public boolean hasATank() { return false; }
    @Override public int getRedstoneOverride() { return 0; }
    @Override public Component getDisplayName() { return Component.translatable("block.rotarycraft.mob_radar"); }
    @Override public AbstractContainerMenu createMenu(int id, Inventory inv, Player player) { return new ContainerMobRadar(id, inv, this); }

    public record Contact(int entityId, double x, double y, double z, int icon, int color) {
        static Contact of(LivingEntity entity) {
            int icon = legacyIcon(BuiltInRegistries.ENTITY_TYPE.getKey(entity.getType()).toString());
            return new Contact(entity.getId(), entity.getX(), entity.getY(), entity.getZ(), icon, ReikaEntityHelper.mobToColor(entity));
        }
        CompoundTag save() {
            var tag = new CompoundTag(); tag.putInt("id", entityId); tag.putDouble("x", x); tag.putDouble("y", y); tag.putDouble("z", z); tag.putInt("icon", icon); tag.putInt("color", color); return tag;
        }
        static Contact load(CompoundTag tag) { return new Contact(tag.getIntOr("id", -1), tag.getDoubleOr("x", 0), tag.getDoubleOr("y", 0), tag.getDoubleOr("z", 0), tag.getIntOr("icon", 1), tag.getIntOr("color", 0xffffff)); }
        // Numeric entity IDs disappeared; retain original mobicons.png cells by registry name.
        private static int legacyIcon(String type) {
            return switch (type) {
                case "minecraft:creeper" -> 50; case "minecraft:skeleton" -> 51; case "minecraft:spider" -> 52;
                case "minecraft:giant" -> 53; case "minecraft:zombie" -> 54; case "minecraft:slime" -> 55;
                case "minecraft:ghast" -> 56; case "minecraft:zombified_piglin" -> 57; case "minecraft:enderman" -> 58;
                case "minecraft:cave_spider" -> 59; case "minecraft:silverfish" -> 60; case "minecraft:blaze" -> 61;
                case "minecraft:magma_cube" -> 62; case "minecraft:ender_dragon" -> 63; case "minecraft:wither" -> 64;
                case "minecraft:bat" -> 65; case "minecraft:witch" -> 66; case "minecraft:pig" -> 90;
                case "minecraft:sheep" -> 91; case "minecraft:cow" -> 92; case "minecraft:chicken" -> 93;
                case "minecraft:squid" -> 94; case "minecraft:wolf" -> 95; case "minecraft:mooshroom" -> 96;
                case "minecraft:snow_golem" -> 97; case "minecraft:ocelot" -> 98; case "minecraft:iron_golem" -> 99;
                case "minecraft:horse" -> 100; case "minecraft:villager" -> 120; default -> 1;
            };
        }

    }
}
