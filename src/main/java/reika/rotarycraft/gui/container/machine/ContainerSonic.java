package reika.rotarycraft.gui.container.machine;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.player.Inventory;
import net.minecraft.world.entity.player.Player;
import reika.rotarycraft.blockentities.weaponry.BlockEntitySonicWeapon;
import reika.rotarycraft.registry.RotaryMenus;
public final class ContainerSonic extends BlankContainer<BlockEntitySonicWeapon> {
    public ContainerSonic(int id, Inventory inventory, FriendlyByteBuf data) { this(id, inventory, (BlockEntitySonicWeapon)inventory.player.level().getBlockEntity(data.readBlockPos())); }
    public ContainerSonic(int id, Inventory inventory, BlockEntitySonicWeapon tile) { super(RotaryMenus.SONIC_WEAPON.get(), id, inventory, tile); }
    @Override public boolean stillValid(Player player) { return !tile.isRemoved() && tile.isPlayerAccessible(player); }
    public boolean setValue(Player player, boolean pitch, long value) {
        if (player.level().isClientSide() || player.containerMenu != this || !stillValid(player) || value < 0) return false;
        tile.setRequestedValue(pitch, value); return true;
    }
}
