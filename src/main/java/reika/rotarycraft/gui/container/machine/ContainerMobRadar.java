package reika.rotarycraft.gui.container.machine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import reika.rotarycraft.blockentities.surveying.BlockEntityMobRadar;
import reika.rotarycraft.registry.RotaryMenus;
/** V33a radar is a display without item slots; block sync carries the server contacts. */
public final class ContainerMobRadar extends BlankContainer<BlockEntityMobRadar> {
    public ContainerMobRadar(int id, Inventory inventory, FriendlyByteBuf data) { this(id, inventory, (BlockEntityMobRadar)inventory.player.level().getBlockEntity(data.readBlockPos())); }
    public ContainerMobRadar(int id, Inventory inventory, BlockEntityMobRadar tile) { super(RotaryMenus.MOB_RADAR.get(), id, inventory, tile); }
    @Override public boolean stillValid(Player player) { return !tile.isRemoved() && tile.isPlayerAccessible(player); }
}
