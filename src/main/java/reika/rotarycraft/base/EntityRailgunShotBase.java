//package reika.rotarycraft.base;
//
//import reika.rotarycraft.blockentities.weaponry.Turret.BlockEntityRailGun;
//import reika.rotarycraft.items.ItemRailGunAmmo;
//import reika.rotarycraft.api.Interfaces.RailGunAmmo.RailGunAmmoType;
//import net.minecraft.core.BlockPos;
//import net.minecraft.network.FriendlyByteBuf;
//import net.minecraft.world.entity.EntityType;
//import net.minecraft.world.item.Item;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.level.Level;
//
//public abstract class EntityRailgunShotBase extends EntityTurretShot {
//
//    private RailGunAmmoType type;
//
//    public EntityRailgunShotBase(final EntityType<? extends EntityTurretShot> entityType, Level world) {
//        super(entityType, world);
//    }
//
//    public EntityRailgunShotBase(Level world, BlockPos pos, BlockPos vpos, BlockEntityRailGun te, RailGunAmmoType type) {
//        super(world, pos, vpos, te);
//        this.type = type;
//    }
//
//    public final RailGunAmmoType getAmmoType() {
//        return type;
//    }
//
//    @Override
//    public void writeSpawnData(FriendlyByteBuf buf) {
//        if (type != null) {
//            buf.writeBoolean(true);
//            buf.writeInt(Item.getId(type.getItem().getItem()));
//        } else {
//            buf.writeBoolean(false);
//        }
//    }
//
//    @Override
//    public void readSpawnData(FriendlyByteBuf additionalData) {
//        if (additionalData.readBoolean()) {
//            Item i = Item.byId(additionalData.readInt());
//            if (i instanceof ItemRailGunAmmo) {
//                ItemStack is = new ItemStack(i, 1, additionalData.readNbt());
//                type = ((ItemRailGunAmmo) i).getAmmo(is, 5); //todo fix power / pwr
//            }
//        }
//    }
//}
