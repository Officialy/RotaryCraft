/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.bedrock;

import net.minecraft.core.BlockPos;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.*;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.libraries.ReikaInventoryHelper;
import reika.dragonapi.libraries.ReikaPlayerAPI;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.registry.ConfigRegistry;

public class ItemBedrockHoe extends HoeItem {

    public ItemBedrockHoe() {
        super(Tiers.NETHERITE, 1, 3.0F, new Item.Properties().setNoRepair());

    }

    @Override
    public void onCraftedBy(ItemStack p_41447_, Level p_41448_, Player p_41449_) {
        //return RotaryAchievements.BEDROCKTOOLS.triggerAchievement(ep);
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player ep, InteractionHand hand) {
        int r = 2;
        for (int i = -r; i <= r; i++) {
            for (int j = -r; j <= r; j++) {
                int dx = ep.blockPosition().getX() + i;
                int dz = ep.blockPosition().getZ() + j;
                if (!ep.isShiftKeyDown()) {
                    if (world.getBlockState(new BlockPos(dx, ep.blockPosition().getY(), dz)) == Blocks.FARMLAND.defaultBlockState()) {
                        //world.setBlockMetadataWithNotify(dx, ep.getOnPos().getY(), dz, 2, 3);
                        //ReikaItemHelper.dropItem(world, dx+0.5, y+1.2, dz+0.5, new ItemStack(Items.wheat_seeds));
                    }
                    //ReikaJavaLibrary.pConsole((dx)+", "+y+", "+(dz);
                } else {
                    if (ConfigRegistry.FAKEBEDROCK.getState() || !ReikaPlayerAPI.isFake(ep)) {
                        int slot = ReikaInventoryHelper.locateIDInInventory(Items.WHEAT_SEEDS, ep.getInventory());
                        if (slot != -1 || ep.isCreative()) {
                            Block id = world.getBlockState(new BlockPos(dx, ep.blockPosition().getY(), dz)).getBlock();
                            Block id2 = world.getBlockState(new BlockPos(dx, ep.blockPosition().getY() + 1, dz)).getBlock();
                            boolean top = id2 == Blocks.AIR;// || id2.isOpaqueCube() == false; todo opaque cube
                            if (top) {
                                if (id != Blocks.AIR) {
                                    if (id == Blocks.DIRT || id == Blocks.FARMLAND || id.isFertile(world.getBlockState(ep.blockPosition()), world, new BlockPos(dx, ep.blockPosition().getY(), dz))) {

                                        world.setBlock(new BlockPos(dx, ep.blockPosition().getY(), dz), Blocks.GRASS.defaultBlockState(), 0);
                                        if (slot != -1 && !ep.isCreative()) {
                                            ItemStack seed = ep.getInventory().getItem(slot);
                                            int count = seed.getCount();
                                            seed.setCount(count - 1);
                                            if (seed.getCount() <= 0) {
                                                ep.getInventory().setItem(slot, null);
                                                return InteractionResultHolder.success(this.getDefaultInstance());
                                            }
                                        }
                                        //ReikaSoundHelper.playStepSound(world, dx, ep.blockPosition().getY(), dz, Blocks.GRASS, 0.4F, 1);
                                    }
                                }
                            }
                        }
                    }
                }
            }
        }
        return InteractionResultHolder.fail(this.getDefaultInstance());
    }

}
