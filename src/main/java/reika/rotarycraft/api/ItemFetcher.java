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
import net.minecraft.world.item.Item;
import net.minecraft.world.item.ItemStack;
import net.neoforged.registries.ForgeRegistries;

/**
 * For fetching RotaryItems items from the enum.
 * See source code to know which are which
 */
public class ItemFetcher {

    private static Class core;
    private static Item[] itemList;

    static {
        try {
            core = Class.forName("reika.rotarycraft.RotaryCraft", false, ItemFetcher.class.getClassLoader());
            itemList = (Item[]) core.getField("items").get(null);
        } catch (ClassNotFoundException e) {
            System.out.println("RotaryCraft class not found!");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("RotaryCraft class not read!");
            e.printStackTrace();
        } catch (IllegalAccessException e) {
            System.out.println("RotaryCraft class not read!");
            e.printStackTrace();
        } catch (NoSuchFieldException e) {
            System.out.println("RotaryCraft class not read!");
            e.printStackTrace();
        } catch (SecurityException e) {
            System.out.println("RotaryCraft class not read!");
            e.printStackTrace();
        }
    }


    /**
     * For fetching items by enum ordinal
     */
    public static Item getItemByOrdinal(int ordinal) {
        return itemList != null ? itemList[ordinal] : null;
    }

    public static Item getItemByUnlocalizedName(String name) {
        for (int i = 0; i < itemList.length; i++) {
            Item it = itemList[i];
            String sg = ForgeRegistries.ITEMS.getKey(it).getNamespace(); //todo check if this is right
            if (name.equals(sg))
                return it;
        }
        return null;
    }

    public static boolean isPlayerHoldingAngularTransducer(Player ep) {
        ItemStack is = ep.getMainHandItem();
        if (is != null) {
            return is.getItem() == getItemByOrdinal(1);
        }
        return false;
    }


    public static boolean isPlayerHoldingBedrockPick(Player ep) {
        ItemStack is = ep.getMainHandItem();
        if (is != null) {
            return is.getItem() == getItemByOrdinal(15);
        }
        return false;
    }

}
