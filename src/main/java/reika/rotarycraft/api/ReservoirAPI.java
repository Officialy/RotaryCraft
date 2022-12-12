/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.api;

import net.minecraft.world.entity.player.Player;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraftforge.fluids.FluidStack;

import java.lang.reflect.Field;
import java.util.Collection;


/**
 * Use this to register custom reservoir handlers, so that a reservoir can perform specialized actions such as special recipe types.
 */
public class ReservoirAPI {

    private static Collection<TankHandler> list;

    static {
        try {
            Class c = Class.forName("reika.rotarycraft.blockentities.Storage.BlockEntityReservoir");
            Field f = c.getDeclaredField("tankHandlers");
            f.setAccessible(true);
            list = (Collection<TankHandler>) f.get(null);
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void registerHandler(TankHandler th) {
        list.add(th);
    }

    public interface TankHandler {

        /**
         * Returns the amount of liquid to drain. Do not drain the liquidstack directly, but you can modify it in other ways.
         */
        int onTick(BlockEntity te, FluidStack stored, Player owner);

    }

}
