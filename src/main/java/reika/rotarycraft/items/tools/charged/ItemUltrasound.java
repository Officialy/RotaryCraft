/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools.charged;

import net.minecraft.core.BlockPos;
import net.minecraft.util.Mth;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import net.minecraft.world.level.material.Fluid;
import net.neoforged.fluids.FluidStack;
import reika.dragonapi.instantiable.data.immutable.DecimalPosition;
import reika.dragonapi.libraries.ReikaFluidHelper;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.dragonapi.libraries.mathsci.ReikaVectorHelper;
import reika.rotarycraft.RotaryConfig;
import reika.rotarycraft.base.ItemChargedTool;
import reika.rotarycraft.registry.ConfigRegistry;


public class ItemUltrasound extends ItemChargedTool {

    public ItemUltrasound() {
        super();
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level world, Player ep, InteractionHand hand) {
//        if (is.getItemDamage() <= 0) {
//            this.noCharge();
//            return InteractionResultHolder.fail(this.getDefaultInstance());
//        } todo add nocharge feature
        this.warnCharge(this.getDefaultInstance());
//        ReikaChatHelper.writeString(String.format("%.3f", look.xCoord)+" "+String.format("%.3f", look.yCoord)+" "+String.format("%.3f", look.zCoord));
        boolean ores = false;
        boolean cave = false;
        boolean silver = false;
        boolean liq = false;
        boolean caveready = false;
        for (float i = 0; i <= 5; i += 0.2) {
            DecimalPosition xyz = ReikaVectorHelper.getPlayerLookCoords(ep, i);
            Block id = xyz.getBlock(world);
            Fluid f = ReikaFluidHelper.lookupFluidForBlock(id.defaultBlockState());
//            if (ReikaBlockHelper.isOre(id, meta) && !ores) {
//                ores = true;
//                ReikaChatHelper.write("Ore Detected!");
//            }
            if (id == Blocks.INFESTED_STONE && !silver) {
                silver = true;
                ReikaChatHelper.write("Silverfish Detected!");
            }
            if (id != Blocks.AIR && !ReikaWorldHelper.softBlocks(world, new BlockPos(Mth.floor(xyz.xCoord), Mth.floor(xyz.yCoord), Mth.floor(xyz.zCoord))))
                caveready = true;
            if (f != null && !liq) {
                liq = true;
                ReikaChatHelper.write(new FluidStack(f, 1000) + " Detected!");
            }
            if (caveready && ReikaWorldHelper.caveBlock(id) && !cave) {
                cave = true;
                ReikaChatHelper.write("Cave Detected!");
            }
            if (!ores && !silver && !cave && !liq) {
                if (ConfigRegistry.CLEARCHAT.getState())
                    ReikaChatHelper.clearChat(); //clr
            }
        }
        return InteractionResultHolder.success(this.getDefaultInstance());
    }
}
