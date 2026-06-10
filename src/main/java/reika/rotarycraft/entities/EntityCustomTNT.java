package reika.rotarycraft.entities;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.neoforged.neoforge.entity.IEntityWithComplexSpawn;

// 1.21.5: IEntityAdditionalSpawnData → IEntityWithComplexSpawn, and the buffer is now a
// RegistryFriendlyByteBuf so the spawn payload can carry registry-tracked data.
public class EntityCustomTNT extends PrimedTnt implements IEntityWithComplexSpawn {

    private int extraTime;

    public EntityCustomTNT(Level world, double x, double y, double z, LivingEntity e, int fuse) {
        super(world, x, y, z, e);
        setFuse(fuse);
    }

    @Override
    public void writeSpawnData(RegistryFriendlyByteBuf data) {
        data.writeInt(getFuse());
    }

    @Override
    public void readSpawnData(RegistryFriendlyByteBuf data) {
        setFuse(data.readInt());
    }
}
