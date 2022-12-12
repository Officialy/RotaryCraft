/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.base.blocks;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.entity.EquipmentSlot;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.Items;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.BlockState;
import net.minecraft.world.phys.BlockHitResult;
import reika.dragonapi.libraries.registry.ReikaItemHelper;
import reika.rotarycraft.auxiliary.RotaryAux;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.blockentities.surveying.BlockEntityCaveFinder;
import reika.rotarycraft.blockentities.transmission.BlockEntityAdvancedGear;
import reika.rotarycraft.registry.RotaryItems;

import java.util.Random;

public abstract class BlockBasicMachine extends BlockRotaryCraftMachine {

    public BlockBasicMachine(Properties properties) {
        super(properties);
//        this.setHardness(4F);
//        this.setResistance(15F);
//        this.setLightLevel(0F);
//        if (par3Material == Material.METAL)
//            this.setStepSound(soundTypeMetal);
    }

    @Override
    public InteractionResult use(BlockState state, Level world, BlockPos pos, Player ep, InteractionHand pHand, BlockHitResult pHit) {
        super.use(state, world, pos, ep, pHand, pHit);
        BlockEntity te = world.getBlockEntity(pos);

        ItemStack is = ep.getMainHandItem();

        if (ep.isCrouching() && !(te instanceof BlockEntityCaveFinder))
            return InteractionResult.PASS;
//        RotaryItems ir = RotaryItems.getEntry(is);
//        if (ir != null && ir.overridesRightClick(is)) {
//            return InteractionResult.FAIL;
//        }
       if (te instanceof BlockEntityAdvancedGear) {
            BlockEntityAdvancedGear tile = (BlockEntityAdvancedGear) te;
            if (tile.getGearType().isLubricated() && tile.canAcceptAnotherLubricantBucket()) {
                if (is != null && ReikaItemHelper.matchStacks(is, RotaryItems.LUBE_BUCKET)) {
                    tile.addLubricant(1000);
                    if (!ep.isCreative())
                        ep.setItemSlot(EquipmentSlot.MAINHAND, new ItemStack(Items.BUCKET));
                    return InteractionResult.SUCCESS;
                }
            }
        }
        if (te != null && RotaryAux.hasGui(world, pos, ep) && ((RotaryCraftBlockEntity) te).isPlayerAccessible(ep)) {
//            ep.openMenu(GuiRegistry.MACHINE.ordinal());
            return InteractionResult.SUCCESS;
        }
        return InteractionResult.FAIL;
    }

}
