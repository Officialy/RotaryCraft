///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.decorative;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.nbt.ListTag;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.enchantment.Enchantment;
//import net.minecraft.world.item.enchantment.Enchantments;
//import net.minecraft.world.level.Level;
//import net.minecraftforge.common.MinecraftForge;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.libraries.ReikaInventoryHelper;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.auxiliary.MachineEnchantmentHandler;
//import reika.rotarycraft.auxiliary.interfaces.ConditionalOperation;
//import reika.rotarycraft.auxiliary.interfaces.DiscreteFunction;
//import reika.rotarycraft.auxiliary.interfaces.EnchantableMachine;
//import reika.rotarycraft.base.blockentity.InventoriedPowerReceiver;
//import reika.rotarycraft.registry.DurationRegistry;
//import reika.rotarycraft.registry.MachineRegistry;
//
//import java.util.ArrayList;
//import java.util.Collection;
//
//public class BlockEntityFireworkMachine extends InventoriedPowerReceiver implements EnchantableMachine, DiscreteFunction, ConditionalOperation {
//
//    private final MachineEnchantmentHandler enchantments = new MachineEnchantmentHandler().addFilter(Enchantments.INFINITY_ARROWS);
//
//    public boolean idle = false;
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        tickcount++;
//        this.getSummativeSidedPower();
//        if (world.isClientSide)
//            return;
//
//        if (power < MINPOWER)
//            return;
//        if (omega < MINSPEED)
//            return;
//        if (tickcount < this.getOperationTime())
//            return;
//        tickcount = 0;
//        if (!this.canCraftARocket()) {
//            idle = true;
//            return;
//        }
//        idle = false;
//        ItemStack rocket = null;
//        boolean hasStar = ReikaInventoryHelper.checkForItem(Items.FIREWORK_STAR, inv);
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(hasStar));
//        if (!hasStar) {
//            ItemStack star = this.randomRecipe();
//            if (star == null)
//                return;
//            rocket = this.starToRocket(star);
//        } else {
//            if (this.canMakeRocketFromStar()) {
//                ItemStack star = null;/*
//				if (this.consumeChance())
//					star = ReikaInventoryHelper.findAndDecrStack2(Items.firework_charge, -1, this.inv);
//				else {
//					int slot = ReikaInventoryHelper.locateInInventory(Items.firework_charge, this.inv);
//					if (slot != -1)
//						star = this.getStackInSlot(slot);
//				}*/
//                int slot = DragonAPI.rand.nextInt(inv.length);
//                while (inv[slot] == null || inv[slot].getItem() != Items.FIREWORK_STAR) {
//                    slot = DragonAPI.rand.nextInt(inv.length);
//                }
//                star = this.getStackInSlot(slot);
//                if (this.consumeChance())
//                    this.decrStackSize(slot, 1);
//
//                //ReikaChatHelper.writeItemStack(world, star);
//                rocket = this.starToRocket(star);
//            }
//        }
//        //ReikaChatHelper.writeItemStack(world, rocket);
//        if (rocket == null)
//            return;
//        EntityFireworkRocket firework = new EntityFireworkRocket(world, x + 0.5, y + 1.25, z + 0.5, rocket);
//        world.addFreshEntity(firework);
//        MinecraftForge.EVENT_BUS.post(new FireworkLaunchEvent(this, firework));
//        //-------TEST CODE----------
//        //ItemEntity ent = new ItemEntity(world, x, y+1, z, star);
//        //world.addFreshEntity(ent);
//
//        if (enchantments.hasEnchantments()) {
//            for (int i = 0; i < 8; i++) {
//                world.addParticle("portal", -0.5 + x + 2 * DragonAPI.rand.nextDouble(), y + DragonAPI.rand.nextDouble(), -0.5 + z + 2 * DragonAPI.rand.nextDouble(), 0, 0, 0);
//            }
//        }
//    }
//
//    @Override
//    public boolean canExtractItem(int i, ItemStack itemstack, int j) {
//        return false;
//    }
//
//    private boolean consumeChance() {
//        if (enchantments.hasEnchantment(Enchantments.INFINITY_ARROWS))
//            return false;
//        int excess = (int) (power / MINPOWER);
//        int chance = DragonAPI.rand.nextInt(1 + excess / 8);
//        return chance == 0;
//    }
//
//    private boolean canCraftARocket() {
//        boolean haveDye = false;
//        boolean have2Gunpowder = false;
//        boolean havePaper = false;
//        boolean haveStar = false;
//        boolean have1Gunpowder = false;
//        have1Gunpowder = ReikaInventoryHelper.checkForItem(Items.GUNPOWDER, inv);
//        haveStar = ReikaInventoryHelper.checkForItem(Items.FIREWORK_STAR, inv);
//        haveDye = ReikaInventoryHelper.checkForItem(Items.DYE, inv);
//        havePaper = ReikaInventoryHelper.checkForItem(Items.PAPER, inv);
//        int numgunpowder = 0;
//        for (int i = 0; i < inv.length; i++) {
//            if (inv[i] != null) {
//                if (inv[i].getItem() == Items.GUNPOWDER) {
//                    numgunpowder += inv[i].getCount();
//                    if (numgunpowder >= 2) {
//                        have2Gunpowder = true;
//                        i = inv.length;
//                    }
//                }
//            }
//        }
//        return (havePaper && (haveDye && have2Gunpowder) || (have1Gunpowder && haveStar));
//    }
//
//    private boolean canMakeRocketFromStar() {
//        boolean hasPaper = ReikaInventoryHelper.checkForItem(Items.paper, inv);
//        boolean hasGunpowder = ReikaInventoryHelper.checkForItem(Items.gunpowder, inv);
//        boolean hasStar = ReikaInventoryHelper.checkForItem(Items.firework_charge, inv);
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(hasPaper)+" "+String.valueOf(hasGunpowder)+" "+String.valueOf(hasStar));
//        return (hasPaper && hasGunpowder && hasStar);
//    }
//
//    private ItemStack starToRocket(ItemStack star) {
//        ItemStack product = null;
//        ItemStack gunpowder = new ItemStack(Items.gunpowder, 1, 0);
//        int numgunpowder = DragonAPI.rand.nextInt(3) + 1; // 1-3
//        ItemStack paper = new ItemStack(Items.paper, 1, 0);
//        ItemStack[] ingredients = new ItemStack[5];
//        if (this.getIngredient(Items.gunpowder, this.canMakeRocketFromStar() && this.consumeChance()))
//            ingredients[1] = gunpowder;
//        if (numgunpowder >= 2 && this.getIngredient(Items.gunpowder, this.consumeChance()))
//            ingredients[2] = gunpowder;
//        if (numgunpowder >= 3 && this.getIngredient(Items.gunpowder, this.consumeChance()))
//            ingredients[3] = gunpowder;
//        if (this.getIngredient(Items.paper, this.consumeChance()))
//            ingredients[4] = paper;
//        ingredients[0] = star;
//        product = this.setNBT(ingredients);
//        return product;
//    }
//
//    private ReikaDyeHelper pickRandomColor(boolean decr) { //Returns a DragonAPI.rand color dye from inv
//        int color = -1;
//        boolean[] hasColors = new boolean[16]; // To save CPU time, see below
//        boolean hasDye = false;
//        for (int i = 0; i < inv.length; i++) {
//            Collection<ReikaDyeHelper> dyes = ReikaDyeHelper.getColorsFromItem(inv[i]);
//            if (dyes != null) {
//                hasDye = true;
//                for (ReikaDyeHelper dye : dyes)
//                    hasColors[dye.ordinal()] = true;
//            }
//        }
//        if (!hasDye)
//            return null;
//        while (color == -1) {
//            ReikaDyeHelper randcolor = ReikaDyeHelper.getRandomColor();
//            while (!hasColors[randcolor.ordinal()])
//                randcolor = ReikaDyeHelper.getRandomColor();
//            for (int j = 0; j < inv.length; j++) {
//                if (inv[j] != null) {
//                    ReikaDyeHelper dye2 = ReikaDyeHelper.getColorFromItem(inv[j]);
//                    if (dye2 == randcolor) {
//                        if (decr) {
//                            ReikaInventoryHelper.decrStack(j, inv);
//                        }
//                        return randcolor;
//                    }
//                }
//            }
//        }
//        return null;
//    }
//
//    private boolean getIngredient(Item id, boolean decr) {
//        for (int i = 0; i < inv.length; i++) {
//            if (inv[i] != null) {
//                if (inv[i].getItem() == id) {
//                    if (decr) {
//                        ReikaInventoryHelper.decrStack(i, inv);
//                    }
//                    return true;
//                }
//            }
//        }
//        return false;
//    }
//
//    private int getShape() {
//        boolean[] hasShape = new boolean[4];
//        int shape = DragonAPI.rand.nextInt(4);
//        if (ReikaInventoryHelper.checkForItem(Items.FIRE_CHARGE, inv)) {
//            hasShape[0] = true;
//        }
//        if (ReikaInventoryHelper.checkForItem(Items.GOLD_NUGGET, inv)) {
//            hasShape[1] = true;
//        }
//        if (ReikaInventoryHelper.checkForItem(Items.FEATHER, inv)) {
//            hasShape[2] = true;
//        }
//        if (ReikaInventoryHelper.checkForItem(Items.SKULL, inv)) {
//            hasShape[3] = true;
//        }
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(hasShape[0]));
//        boolean noShapes = true;
//        for (int i = 0; i < hasShape.length; i++) {
//            if (hasShape[i]) {
//                noShapes = false;
//                i = hasShape.length;
//            }
//        }
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(noShapes));
//        if (noShapes)
//            return -1;
//        while (!hasShape[shape]) {
//            shape = DragonAPI.rand.nextInt(4);
//        }
//        int slot = -1;
//        Item id = null;
//        switch (shape) {
//            case 0:
//                id = Items.FIRE_CHARGE;
//                break;
//            case 1:
//                id = Items.GOLD_NUGGET;
//                break;
//            case 2:
//                id = Items.FEATHER;
//                break;
//            case 3:
//                id = Items.SKULL;
//                break;
//        }
//        if (id != null && this.consumeChance())
//            ReikaInventoryHelper.findAndDecrStack(id, -1, inv);
//        //else
//        //return 0;
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(shape));
//        return shape + 1;
//    }
//
//    private ItemStack randomRecipe() {
//        ReikaDyeHelper dyeColor = this.pickRandomColor(this.consumeChance() && ReikaInventoryHelper.checkForItem(Items.gunpowder, inv)); //Dye metadata to craft with - 0-15
//        boolean hasDiamond = false;
//        boolean hasGlowstone = false;
//        int shape = this.getShape(); //Shape modifiers - Fire charge, gold nugget, feather, head, or nothing
//        ItemStack gunpowder = new ItemStack(Items.GUNPOWDER, 1, 64);
//        ItemStack diamond = new ItemStack(Items.DIAMOND, 1, 0);
//        ItemStack glowstone = new ItemStack(Items.GLOWSTONE_DUST, 1, 0);
//
//        ItemStack[] inputitems = new ItemStack[5];
//        if (dyeColor != null)
//            inputitems[1] = new ItemStack(Items.DYE, 1, dyeColor.ordinal());
//        else
//            inputitems[1] = null;
//        if (inputitems[1] == null) // If missing dye
//            return null;
//        if (this.getIngredient(Items.GUNPOWDER, this.consumeChance()))
//            inputitems[0] = gunpowder;
//        if (inputitems[0] == null) // If missing gunpowder
//            return null;
//        if (DragonAPI.rand.nextInt(2) == 0) {
//            if (this.getIngredient(Items.DIAMOND, this.consumeChance()))
//                hasDiamond = true;
//        }
//        if (DragonAPI.rand.nextInt(2) == 0) {
//            if (this.getIngredient(Items.GLOWSTONE_DUST, this.consumeChance()))
//                hasGlowstone = true;
//        }
//        if (hasDiamond)
//            inputitems[2] = diamond;
//        if (hasGlowstone)
//            inputitems[3] = glowstone;
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.valueOf(shape));
//        switch (shape) {
//            case 1:
//                inputitems[4] = new ItemStack(Items.FIRE_CHARGE, 1, 0);
//                break;
//            case 2:
//                inputitems[4] = new ItemStack(Items.GOLD_NUGGET, 1, 0);
//                break;
//            case 3:
//                inputitems[4] = new ItemStack(Items.FEATHER, 1, 0);
//                break;
//            case 4:
//                inputitems[4] = new ItemStack(Items.SKULL, 1, 0);
//                break;
//            default:
//                inputitems[4] = null;
//        }
//		/*
//		for (int k = 0; k < inputitems.length; k++) {
//			ReikaChatHelper.writeItemStack(this.level, inputitems[k]);
//		}*/
//
//        ItemStack output = this.setNBT(inputitems);
//        if (DragonAPI.rand.nextInt(2) == 0) {
//            ReikaDyeHelper dyeColor2 = this.pickRandomColor(this.consumeChance());
//            if (dyeColor2 == null)
//                return output; //Bypass
//            ItemStack newcolor = new ItemStack(Items.dye, 1, dyeColor2.ordinal());
//            output = this.colorBlend(output, newcolor);
//        }
//        return output;
//    }
//
//    private ItemStack colorBlend(ItemStack base, ItemStack blendcolor) {
//        ItemStack[] inputitems = new ItemStack[2];
//        inputitems[0] = base;
//        inputitems[1] = blendcolor;
//        return this.setNBT(inputitems);
//    }
//
//    private ItemStack setNBT(ItemStack[] inputitems) {
//        ItemStack field_92102_a = null;
//        int var3 = 0;
//        int var4 = 0;
//        int var5 = 0;
//        int var6 = 0;
//        int var7 = 0;
//        int var8 = 0;
//
//        for (int var9 = 0; var9 < inputitems.length; ++var9) {
//            ItemStack var10 = inputitems[var9];
//
//            if (var10 != null) {
//                if (var10.getItem() == Items.GUNPOWDER) {
//                    ++var4;
//                } else if (var10.getItem() == Items.FIREWORK_CHARGE) {
//                    ++var6;
//                } else if (var10.getItem() == Items.DYE) {
//                    ++var5;
//                } else if (var10.getItem() == Items.PAPER) {
//                    ++var3;
//                } else if (var10.getItem() == Items.GLOWSTONE_DUST) {
//                    ++var7;
//                } else if (var10.getItem() == Items.DIAMOND) {
//                    ++var7;
//                } else if (var10.getItem() == Items.FIRE_CHARGE) {
//                    ++var8;
//                } else if (var10.getItem() == Items.FEATHER) {
//                    ++var8;
//                } else if (var10.getItem() == Items.GOLD_NUGGET) {
//                    ++var8;
//                } else {
//                    if (var10.getItem() != Items.SKULL) {
//                        return field_92102_a;
//                    }
//
//                    ++var8;
//                }
//            }
//        }
//
//        var7 += var5 + var8;
//
//        if (var4 <= 3 && var3 <= 1) {
//            CompoundTag var15;
//            CompoundTag var18;
//
//            if (var4 >= 1 && var3 == 1 && var7 == 0) {
//                field_92102_a = new ItemStack(Items.FIREWORKS);
//
//                var15 = new CompoundTag();
//                if (var6 > 0) {
//                    var18 = new CompoundTag();
//                    ListTag var25 = new ListTag();
//
//                    for (int var22 = 0; var22 < inputitems.length; ++var22) {
//                        ItemStack var26 = inputitems[var22];
//
//                        if (var26 != null && var26.getItem() == Items.FIREWORK_CHARGE && var26.hasTagCompound() && var26.getTagCompound().contains("Explosion")) {
//                            var25.add(var26.getTagCompound().getCompound("Explosion"));
//                        }
//                    }
//
//                    var18.put("Explosions", var25);
//                    var18.putByte("Flight", (byte) var4);
//                    var15.put("Fireworks", var18);
//                }
//
//                field_92102_a.setTagCompound(var15);
//                return field_92102_a;
//            } else if (var4 == 1 && var3 == 0 && var6 == 0 && var5 > 0 && var8 <= 1) {
//                field_92102_a = new ItemStack(Items.FIREWORK_CHARGE);
//                var15 = new CompoundTag();
//                var18 = new CompoundTag();
//                byte var21 = 0;
//                ArrayList var12 = new ArrayList<>();
//
//                for (int var13 = 0; var13 < inputitems.length; ++var13) {
//                    ItemStack var14 = inputitems[var13];
//
//                    if (var14 != null) {
//                        if (var14.getItem() == Items.dye) {
//                            var12.add(Integer.valueOf(ItemDye.field_150922_c[var14.getItemDamage()]));
//                        } else if (var14.getItem() == Items.GLOWSTONE_DUST) {
//                            var18.putBoolean("Flicker", true);
//                        } else if (var14.getItem() == Items.DIAMOND) {
//                            var18.putBoolean("Trail", true);
//                        } else if (var14.getItem() == Items.FIRE_CHARGE) {
//                            var21 = 1;
//                        } else if (var14.getItem() == Items.FEATHER) {
//                            var21 = 4;
//                        } else if (var14.getItem() == Items.GOLD_NUGGET) {
//                            var21 = 2;
//                        } else if (var14.getItem() == Items.SKULL) {
//                            var21 = 3;
//                        }
//                    }
//                }
//
//                int[] var24 = new int[var12.size()];
//
//                for (int var27 = 0; var27 < var24.length; ++var27) {
//                    var24[var27] = ((Integer) var12.get(var27)).intValue();
//                }
//
//                var18.putIntArray("Colors", var24);
//                var18.putByte("Type", var21);
//                var15.put("Explosion", var18);
//                field_92102_a.setTagCompound(var15);
//                return field_92102_a;
//            } else if (var4 == 0 && var3 == 0 && var6 == 1 && var5 > 0 && var5 == var7) {
//                ArrayList var16 = new ArrayList<>();
//
//                for (int var20 = 0; var20 < inputitems.length; ++var20) {
//                    ItemStack var11 = inputitems[var20];
//
//                    if (var11 != null) {
//                        if (var11.getItem() == Items.dye) {
//                            var16.add(Integer.valueOf(ItemDye.field_150922_c[var11.getItemDamage()]));
//                        } else if (var11.getItem() == Items.firework_charge) {
//                            field_92102_a = var11.copy();
//                            field_92102_a.getCount() = 1;
//                        }
//                    }
//                }
//
//                int[] var17 = new int[var16.size()];
//
//                for (int var19 = 0; var19 < var17.length; ++var19) {
//                    var17[var19] = ((Integer) var16.get(var19)).intValue();
//                }
//
//                if (field_92102_a != null && field_92102_a.hasTagCompound()) {
//                    CompoundTag var23 = field_92102_a.getTagCompound().getCompound("Explosion");
//
//                    if (var23 == null) {
//                        return field_92102_a;
//                    } else {
//                        var23.putIntArray("FadeColors", var17);
//                        return field_92102_a;
//                    }
//                } else {
//                    return field_92102_a;
//                }
//            } else {
//                return field_92102_a;
//            }
//        } else {
//            return field_92102_a;
//        }
//    }
//
//    @Override
//    public int getContainerSize() {
//        return 27;
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//    }
//
//    @Override
//    public void saveAdditional(CompoundTag NBT) {
//        super.saveAdditional(NBT);
//
//        NBT.put("enchants", enchantments.saveAdditional());
//    }
//
//    @Override
//    public void load(CompoundTag NBT) {
//        super.load(NBT);
//
//        enchantments.load(NBT.getTagList("enchants", NBTTypes.COMPOUND.ID));
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.FIREWORK;
//    }
//
//    @Override
//    public boolean isItemValidForSlot(int slot, ItemStack is) {
//        return ReikaItemHelper.isFireworkIngredient(is.getItem());
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        if (!this.canCraftARocket())
//            return 15;
//        return 0;
//    }
//
//    public ArrayList<Enchantment> getValidEnchantments() {
//        ArrayList<Enchantment> li = new ArrayList<Enchantment>();
//        li.add(Enchantment.infinity);
//        return li;
//    }
//
//    @Override
//    public int getOperationTime() {
//        return DurationRegistry.FIREWORK.getOperationTime(omega);
//    }
//
//    @Override
//    public boolean areConditionsMet() {
//        return !ReikaInventoryHelper.isEmpty(inv);
//    }
//
//    @Override
//    public String getOperationalStatus() {
//        return this.areConditionsMet() ? "Operational" : "No Items";
//    }
//
//    @Override
//    public MachineEnchantmentHandler getEnchantmentHandler() {
//        return enchantments;
//    }
//}
