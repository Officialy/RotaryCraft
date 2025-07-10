/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities;

import net.minecraft.core.BlockPos;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.network.Connection;
import net.minecraft.network.protocol.game.ClientboundBlockEntityDataPacket;
import net.minecraft.world.inventory.CraftingContainer;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.BlockGetter;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraftforge.fluids.FluidType;
import net.minecraftforge.fluids.capability.FluidHandlerBlockEntity;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.registry.RotaryBlockEntities;

public class BlockEntityDecoTank extends FluidHandlerBlockEntity {

    public static final int CAPACITY = 10 * FluidType.BUCKET_VOLUME;

    public BlockEntityDecoTank(final BlockPos pos, final BlockState state) {
        super(RotaryBlockEntities.DECO_TANK.get(), pos, state);
    }

//    
//    @Override
//    public ClientboundBlockEntityDataPacket getUpdatePacket() {
//        return ClientboundBlockEntityDataPacket.create(getBlockPos(), this::getUpdateTag);
//    }

    @Override
    public CompoundTag getUpdateTag() {
        return new CompoundTag();
    }

    @Override
    public void onDataPacket(Connection net, ClientboundBlockEntityDataPacket pkt) {
        load(pkt.getTag());
    }

    public enum TankFlags {
        CLEAR("Clear Glass", new ItemStack(Blocks.GLASS_PANE)),
        NOCOLOR("Ignore Fluid Color", new ItemStack(Items.INK_SAC, 1)),
        LIGHTED("Glowing", new ItemStack(Items.GLOWSTONE_DUST)),
        RESISTANT("Resistant", new ItemStack(Blocks.OBSIDIAN)),
        ;

        public static final TankFlags[] list = values();
        public final String displayName;
        private final ItemStack toggleFlag;

        TankFlags(String s, ItemStack is) {
            displayName = s;
            toggleFlag = is;
        }

        public boolean toggle(CraftingContainer ics) {
            for (int i = 0; i < ics.getContainerSize(); i++) {
                ItemStack in = ics.getItem(i);
                if (this.isItem(in))
                    return true;
            }
            return false;
        }

        public boolean isItem(ItemStack is) {
            return ReikaItemHelper.matchStacks(is, toggleFlag);
        }

        public boolean apply(BlockGetter world, BlockPos pos) {
            BlockEntityDecoTank te = (BlockEntityDecoTank) world.getBlockEntity(pos);
            return te != null;// && te.getFlag(this);//this.apply(world.getBlockMetadata(x, y, z));
        }
    }
}
