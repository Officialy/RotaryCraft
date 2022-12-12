package reika.rotarycraft.entities;

import io.netty.buffer.ByteBuf;
import net.minecraft.core.BlockPos;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.item.PrimedTnt;
import net.minecraft.world.level.Level;
import net.minecraftforge.entity.IEntityAdditionalSpawnData;

public class EntityCustomTNT extends PrimedTnt implements IEntityAdditionalSpawnData {

    private int extraTime;

    public EntityCustomTNT(Level world, BlockPos pos, LivingEntity e, int fuse)
    {
        super(world, pos.getX(), pos.getY(), pos.getZ(), e);
        setFuse(fuse);
    }

    @Override
    public void writeSpawnData(FriendlyByteBuf data) {
        data.writeInt(getFuse());
    }

    @Override
    public void readSpawnData(FriendlyByteBuf data) {
         setFuse(data.readInt());
    }
}
