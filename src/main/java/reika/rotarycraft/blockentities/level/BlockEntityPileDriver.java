///*******************************************************************************
// * @author Reika Kalseki
// *
// * Copyright 2017
// *
// * All rights reserved.
// * Distribution of the software in any form is only allowed with
// * explicit, prior permission from the owner.
// ******************************************************************************/
//package reika.rotarycraft.blockentities.level;
//
//import net.minecraft.core.BlockPos;
//import net.minecraft.nbt.CompoundTag;
//import net.minecraft.world.damagesource.DamageSource;
//import net.minecraft.world.entity.LivingEntity;
//import net.minecraft.world.entity.decoration.HangingEntity;
//import net.minecraft.world.entity.item.ItemEntity;
//import net.minecraft.world.entity.player.Player;
//import net.minecraft.world.item.ItemStack;
//import net.minecraft.world.item.Items;
//import net.minecraft.world.item.alchemy.Potion;
//import net.minecraft.world.level.Level;
//import net.minecraft.world.level.block.Block;
//import net.minecraft.world.level.block.Blocks;
//import net.minecraft.world.phys.AABB;
//import net.neoforged.api.distmarker.Dist;
//import net.neoforged.common.NeoForge;
//import net.neoforged.fml.loading.FMLLoader;
//import reika.dragonapi.DragonAPI;
//import reika.dragonapi.ModList;
//import reika.dragonapi.instantiable.data.immutable.BlockKey;
//import reika.dragonapi.instantiable.data.maps.BlockMap;
//import reika.dragonapi.libraries.ReikaPlayerAPI;
//import reika.dragonapi.libraries.level.ReikaBlockHelper;
//import reika.dragonapi.libraries.level.ReikaWorldHelper;
//import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
//import reika.dragonapi.libraries.registry.ReikaItemHelper;
//import reika.rotarycraft.base.blockentity.BlockEntityPowerReceiver;
//import reika.rotarycraft.registry.MachineRegistry;
//import reika.rotarycraft.registry.RotaryBlocks;
//import reika.rotarycraft.registry.RotaryItems;
//import reika.rotarycraft.registry.SoundRegistry;
//
//import java.util.ArrayList;
//import java.util.HashMap;
//import java.util.List;
//
//
//public class BlockEntityPileDriver extends BlockEntityPowerReceiver {
//
//
//    public static final int BASEPOWER = 16384; //16 kW per meter to lift the hammer (P = mg x dh/dt)
//    public static final int MINTIME = 1;
//    public static final int BASESPEED = 300;
//    private static final BlockMap<Integer> HITS_PER_BLOCK = new BlockMap();
//    private static final BlockMap<BlockKey> BLOCK_CONVERSION = new BlockMap();
//    private static final int BITMETA = 4;
//
//    static {
//        HITS_PER_BLOCK.put(Blocks.obsidian, 5);
//        HITS_PER_BLOCK.put(Blocks.netherrack, -2);
//        HITS_PER_BLOCK.put(Blocks.GLASS, -4);
//        HITS_PER_BLOCK.put(Blocks.glowstone, -3);
//        HITS_PER_BLOCK.put(Blocks.wool, -1);
//
//        BlockKey to = new BlockKey(Blocks.AIR);
//        BLOCK_CONVERSION.put(Blocks.bedrock, new BlockKey(Blocks.bedrock));
//        BLOCK_CONVERSION.put(Blocks.STONE, new BlockKey(Blocks.cobblestone));
//        BLOCK_CONVERSION.put(RotaryBlocks.DECO.get(), RotaryItems.shieldblock.getItemDamage(), new BlockKey(RotaryBlocks.DECO.get(), RotaryItems.shieldblock.getItemDamage()));
//        BLOCK_CONVERSION.put(RotaryBlocks.MININGPIPE.get(), 3, new BlockKey(RotaryBlocks.MININGPIPE.get(), 3));
//        BLOCK_CONVERSION.put(Blocks.STONE_BRICKS, 0, new BlockKey(Blocks.STONE_BRICKS, 2));
//
//        if (ModList.GEOSTRATA.isLoaded()) {
//            for (int i = 0; i < RockTypes.rockList.length; i++) {
//                RockTypes r = RockTypes.rockList[i];
//                for (int k = 0; k < RockShapes.shapeList.length; k++) {
//                    RockShapes s = RockShapes.shapeList[k];
//                    ItemStack is = r.getItem(s);
//                    Block b = Block.getBlockFromItem(is.getItem());
//                    if (s != RockShapes.COBBLE) {
//                        BLOCK_CONVERSION.put(b, is.getItemDamage(), new BlockKey(r.getItem(RockShapes.COBBLE)));
//                    }
//                    if (r == RockTypes.GRANITE || r == RockTypes.HORNFEL) {
//                        HITS_PER_BLOCK.put(b, is.getItemDamage(), 3);
//                    } else if (r == RockTypes.PERIDOTITE || r == RockTypes.GNEISS || r == RockTypes.SCHIST) {
//                        HITS_PER_BLOCK.put(b, is.getItemDamage(), 2);
//                    } else if (r == RockTypes.SHALE || r == RockTypes.LIMESTONE) {
//                        HITS_PER_BLOCK.put(b, is.getItemDamage(), -1);
//                    }
//                }
//            }
//        }
//    }
//
//    private final HashMap<BlockPos, HitCount> numHits = new HashMap();
//    public int step = 0;
//    private int step2 = 0;
//    private boolean climbing = false;
//    private boolean active = false;
//    private boolean smashed = false;
//
//    private static int getHitCount(Block b) {
//        Integer get = HITS_PER_BLOCK.get(b);
//        return get != null ? get.intValue() : 0;
//    }
//
//    private void addNausea(Level world, BlockPos pos) {
//        AABB box = AABB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(15, 15, 15); // 5m radius
//        List<Player> sick = world.getEntitiesOfClass(Player.class, box);
//        for (Player ep : sick) {
//            if (!ep.isCreative())
//                ep.addMobEffect(new MobEffect(Potion.CONFUSION, 150, 10));
//        }
//    }
//
//    @Override
//    public void updateEntity(Level world, BlockPos pos) {
//        super.updateBlockEntity();
//        this.getIOSides(world, pos);
//        this.getPower(true);
//        int speed = BASESPEED;
//        int minpower = BASEPOWER * (step + 1);
//        if (power < minpower || torque < MINTORQUE) {
//            return;
//        }
//        if (power > minpower)
//            speed = Math.max(BASESPEED / ((int) (power / minpower)), MINTIME);
//        tickcount++;
//
//        if (!this.drawPile3(world, pos, speed) && step != 0)
//            return;
//        climbing = true;
//        tickcount = 0;
//        if (this.smash(world, x, y - step - 1, z)) {
//            step += 1;
//        }
//        this.bounce(world, x, y - step - 1, z);
//        this.dealDamage(world, x, y - step - 1, z);
//        this.addNausea(world, x, y - step - 1, z);
//        SoundRegistry.PILEDRIVER.playSoundAtBlock(world, pos, 1, 1);
//    }
//
//    private void bounce(Level world, BlockPos pos) { //bounce entities
//        AABB zone = AABB.getBoundingBox(x - 2, y, z - 2, x + 3, y + 1, z + 3).expand(24, 24, 24);
//        List<Entity> inzone = world.getEntities(Entity.class, zone);
//        for (Entity ent : inzone) {
//            if (ent != null) {
//                if (ent.onGround && !world.isClientSide)
//                    ent.motionY += 0.5 / ReikaMathLibrary.doubpow(ReikaMathLibrary.py3d(ent.getY - x, ent.getY() - y, ent.posZ - z), 0.5);
//                ent.velocityChanged = true;
//            }
//        }
//    }
//
//    public void getIOSides(Level world, BlockPos pos, int metadata) {
//        switch (metadata) {
//            case 1:
//                read = Direction.EAST;
//                read2 = Direction.WEST;
//                break;
//            case 0:
//                read = Direction.NORTH;
//                read2 = Direction.SOUTH;
//                break;
//        }
//    }
//
//    private void dealDamage(Level world, BlockPos pos) {
//        AABB box = AABB.getBoundingBox(pos.getX(), pos.getY(), pos.getZ(), pos.getX() + 1, pos.getY() + 1, pos.getZ() + 1).expand(0.5, 2, 0.5);
//        List killed = world.getEntities(LivingEntity.class, box);
//        for (int i = 0; i < killed.size(); i++) {
//            LivingEntity el = (LivingEntity) killed.get(i);
//            if (el != null) {
//                float dmg = el.getMaxHealth() * el.getTotalArmorValue();
//                if (dmg <= 0)
//                    dmg = Float.MAX_VALUE;
//                el.hurt(DamageSource.inWall, dmg); //will kill anything
//            }
//        }
//    }
//
//    private void breakGlass(Level world, BlockPos pos) {
//        int range = 5;
//        for (int i = -range; i <= range; i++) {
//            for (int j = -range; j <= range; j++) {
//                for (int k = -range; k <= range; k++) {
//                    Block b = world.getBlock(x + i, y + j, z + k);
//                    if (b != Blocks.AIR) {
//                        //ReikaSoundHelper.playBreakSound(world, x+i, y+j, z+k, b);
//                        this.breakGlass_do(world, x + i, y + j, z + k, b);
//                    }
//                }
//            }
//        }
//        AABB nearby = AABB.getBoundingBox(x - range, y - range, z - range, x + 1 + range, y + 1 + range, z + 1 + range);
//        List<HangingEntity> inzone = world.getEntitiesOfClass(HangingEntity.class, nearby);
//        for (HangingEntity ep : inzone) {
//            ep.hurt(DamageSource.OUT_OF_WORLD, 100);
//        }
//    }
//
//    private void breakGlass_do(Level world, BlockPos pos, Block id) {
//        ItemStack drop = null;
//        if (id == Blocks.GLASS || id == Blocks.GLASS_PANE || id == Blocks.GLOWSTONE) {
//            id.dropBlockAsItem(world, pos, meta, 0);
//            world.setBlockToAir(pos);
//            //world.playLocalSound(x+0.5, y+0.5, z+0.5, "DragonAPI.rand.glass", 0.5F, 1F);
//        }
//        if (id == Blocks.CACTUS || id == Blocks.SUGAR_CANE || id == Blocks.VINE ||
//                id == Blocks.LILY_PAD || id == Blocks.TALL_GRASS || id == Blocks.SAPLING ||
//                id == Blocks.FLOWER_POT || id == Blocks.SKULL) {
//            id.dropBlockAsItem(world, pos, 0);
//            world.setBlockToAir(pos);
//        }
//        if (id == Blocks.ICE) {
//            world.setBlock(pos, Blocks.WATER.defaultBlockState(), 0);
//            //world.playLocalSound(x+0.5, y+0.5, z+0.5, "DragonAPI.rand.glass", 0.5F, 1F);
//            drop = new ItemStack(Blocks.ICE);
//        }
//        if (id == Blocks.WEB) {
//            world.setBlockToAir(pos);
//            drop = new ItemStack(Items.STRING);
//        }/*
//    	if (id == Blocks.tnt) {
//    		world.setBlockToAir(pos);
//            PrimedTnt var6 = new PrimedTnt(world, x+0.5D, y+0.5D, z+0.5D);
//            world.addFreshEntity(var6);
//            world.playSoundAtEntity(var6, "DragonAPI.rand.fuse", 1.0F, 1.0F);
//    	}*/
//        if (id instanceof BlockFalling)
//            this.makeFall(world, pos, (BlockFalling) id);
//		/*if (id == RotaryCraft.miningpipe.blockID && dropmeta != 4)
//			world.setBlockToAir(pos);*/
//        if (drop == null)
//            return;
//        if (FMLLoader.getDist() == Dist.CLIENT)
//            return;
//        ItemEntity ent = new ItemEntity(world, pos, drop);
//        world.addFreshEntity(ent);
//    }
//
//    private void makeFall(Level world, BlockPos pos, BlockFalling id) {
//        BlockFalling tofall = id;
//        if (tofall.func_149831_e(world, x, y - 1, z)) {
//            byte var8 = 32;
//            if (!tofall.fallInstantly && world.hasChunksAt(x - var8, y - var8, z - var8, x + var8, y + var8, z + var8)) {
//                if (!world.isClientSide) {
//                    EntityFallingBlock var9 = new EntityFallingBlock(world, x + 0.5F, y + 0.5F, z + 0.5F, tofall, world.getBlockMetadata(pos));
//                    //tofall.onStartFalling(var9);
//                    world.addFreshEntity(var9);
//                }
//            } else {
//                world.setBlockToAir(pos);
//                while (tofall.func_149831_e(world, x, y - 1, z) && y > 0)
//                    --y;
//                if (y > 0)
//                    world.setBlock(pos, tofall);
//            }
//        }
//    }
//
//    private ArrayList<ItemStack> getDrops(Level world, BlockPos pos) {
//        Block b = world.getBlock(pos);
//		/*
//		if (BlockEntityBorer.isMineableBedrock(world, pos))
//			return ReikaJavaLibrary.makeListFrom(ReikaItemHelper.getSizedItemStack(RotaryItems.bedrockdust.copy(), DifficultyEffects.BEDROCKDUST.getInt()));
//		else*/
//        return b != null ? b.getDrops(world, pos, world.getBlockMetadata(pos), 0) : new ArrayList<>();
//    }
//
//    private BlockKey getBlockProduct(Level world, BlockPos pos, Block id) {
//        BlockKey to = BLOCK_CONVERSION.get(id);
//        if (to == null)
//            to = new BlockKey(Blocks.AIR);
//        if (ReikaBlockHelper.isLiquid(id)) {
//            to = new BlockKey(id);
//        }
//        return to;
//    }
//
//    private boolean drawPile3(Level world, BlockPos pos, int speed) {
//        if (climbing && tickcount > speed) {
//            if (world.getBlockState(x, y - step2 - 2, z).getBlock() == RotaryBlocks.MININGPIPE.get())
//                world.setBlockToAir(x, y - step2 - 2, z);
//            step2--;
//            if (world.getBlock(x, y - step2, z) == this.getBlockType()) {
//                climbing = false;
//                //step2++;
//                smashed = false;
//            } else
//                world.setBlockToAir(x, y - step2 - 1, z);
//            tickcount = 0;
//        }
//        if (climbing && tickcount <= speed) {
//            //if (world.getBlock(x, y-step2-2, z) == RotaryCraft.miningpipe.blockID)
//            //world.setBlock(x, y-step2-2, z, 0);
//            if (step2 >= step)
//                step2--;
//            if (world.getBlock(x, y - step2, z) == this.getBlockType()) {
//                climbing = false;
//                //step2++;
//                smashed = false;
//            } else
//                world.setBlockToAir(x, y - step2 - 1, z);
//            //this.tickcount = 0;
//        }
//        world.markBlockForUpdate(x, y - step2 - 1, z);
//        if (!climbing) {
//            if (ReikaWorldHelper.defaultBlockState().getMaterial(world, x, y - step2 - 1, z) == Material.water) {
//                world.addParticle("splash", x, y - step2 + 1, z, -0.2, 0.4, -0.2);
//                world.addParticle("splash", x + 0.5, y - step2 + 1, z, 0, 0.4, -0.2);
//                world.addParticle("splash", x + 1, y - step2 + 1, z, 0.2, 0.4, -0.2);
//                world.addParticle("splash", x, y - step2 + 1, z + 0.5, -0.2, 0.4, 0);
//                world.addParticle("splash", x, y - step2 + 1, z + 1, -0.2, 0.4, 0.2);
//                world.addParticle("splash", x + 0.5, y - step2 + 1, z + 1, 0, 0.4, 0.2);
//                world.addParticle("splash", x + 1, y - step2 + 1, z + 0.5, 0.2, 0.4, 0);
//                world.addParticle("splash", x + 1, y - step2 + 1, z + 1, 0.2, 0.4, 0.2);
//
//                world.addParticle("splash", x, y - step2 + 1, z, -0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0.4, -0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + 0.5, y - step2 + 1, z, 0, 0.4, -0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + 1, y - step2 + 1, z, 0.2 - 0.4 * DragonAPI.rand.nextFloat(), 0.4, -0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x, y - step2 + 1, z + 0.5, -0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0);
//                world.addParticle("splash", x, y - step2 + 1, z + 1, -0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0.2 - 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + 0.5, y - step2 + 1, z + 1, 0, 0.4, 0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + 1, y - step2 + 1, z + 0.5, 0.2 - 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0);
//                world.addParticle("splash", x + 1, y - step2 + 1, z + 1, 0.2 - 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0.2 - 0.4 * DragonAPI.rand.nextFloat());
//
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("splash", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//
//                world.playLocalSound(x + 0.5, y - step2, z + 0.5, "DragonAPI.rand.splash", 1F, 1F);
//            }
//            if (ReikaWorldHelper.defaultBlockState().getMaterial(world, x, y - step2 - 1, z) == Material.lava) {
//                world.addParticle("lava", x, y - step2 + 1, z, -0.2, 0.4, -0.2);
//                world.addParticle("lava", x + 0.5, y - step2 + 1, z, 0, 0.4, -0.2);
//                world.addParticle("lava", x + 1, y - step2 + 1, z, 0.2, 0.4, -0.2);
//                world.addParticle("lava", x, y - step2 + 1, z + 0.5, -0.2, 0.4, 0);
//                world.addParticle("lava", x, y - step2 + 1, z + 1, -0.2, 0.4, 0.2);
//                world.addParticle("lava", x + 0.5, y - step2 + 1, z + 1, 0, 0.4, 0.2);
//                world.addParticle("lava", x + 1, y - step2 + 1, z + 0.5, 0.2, 0.4, 0);
//                world.addParticle("lava", x + 1, y - step2 + 1, z + 1, 0.2, 0.4, 0.2);
//
//                world.addParticle("lava", x, y - step2 + 1, z, -0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0.4, -0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + 0.5, y - step2 + 1, z, 0, 0.4, -0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + 1, y - step2 + 1, z, 0.2 - 0.4 * DragonAPI.rand.nextFloat(), 0.4, -0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x, y - step2 + 1, z + 0.5, -0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0);
//                world.addParticle("lava", x, y - step2 + 1, z + 1, -0.2 + 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0.2 - 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + 0.5, y - step2 + 1, z + 1, 0, 0.4, 0.2 + 0.4 * DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + 1, y - step2 + 1, z + 0.5, 0.2 - 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0);
//                world.addParticle("lava", x + 1, y - step2 + 1, z + 1, 0.2 - 0.4 * DragonAPI.rand.nextFloat(), 0.4, 0.2 - 0.4 * DragonAPI.rand.nextFloat());
//
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//                world.addParticle("lava", x + DragonAPI.rand.nextFloat(), y - step2 + 1, z + DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat(), DragonAPI.rand.nextFloat());
//
//                world.playLocalSound(x + 0.5, y - step2, z + 0.5, "DragonAPI.rand.fizz", 1F, 1F);
//            }
//            world.setBlock(x, y - step2 - 1, z, RotaryBlocks.MININGPIPE.get(), BITMETA, 3);
//            step2++;
//        }/*
//		if (step2 == step) {
//			if (world.getBlock(x, y-step2-2, z) == 0)
//				world.setBlock(x, y-step2-2, z, RotaryCraft.miningpipe.blockID, BITMETA);
//		}*/
//        if (world.getBlock(x, y - step2 - 1, z) == Blocks.AIR) {
//            while (world.getBlock(x, y - step2 - 2, z) == Blocks.AIR && y - step2 - 2 > 0 && step == step2) {
//                step++;
//                step2 = step;
//            }
//        }
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d    %d", step, step2, y-step2-2));
//        return (step2 == step);
//    }
//
//    private boolean smash(Level world, BlockPos pos) {
//        NeoForge.EVENT_BUS.post(new PileDriverImpactEvent(this, pos));
//        boolean cleared = true;
//        smashed = true;
//        for (int i = -2; i < 3; i++) {
//            for (int j = -2; j < 3; j++) {
//                Block id = world.getBlock(x + i, y, z + j);
//                if (!world.isClientSide && ReikaPlayerAPI.playerCanBreakAt((WorldServer) world, x + i, y, z + j, id, meta, this.getServerPlacer())) {
//                    if (id != Blocks.AIR && i * j != 4 && i * j != -4) {
//                        if (id == Blocks.mob_spawner) {
//                            BlockEntityMobSpawner spw = (BlockEntityMobSpawner) world.getBlockEntity(x + i, y, z + j);
//                            if (spw != null) {
//                                this.spawnSpawner(world, x + i, y, z + j, spw);
//                            }
//                        }
//                        for (int h = 1; h <= 4; h++) {
//                            Block id2 = world.getBlock(x + i, y - h, z + j);
//                            int meta2 = world.getBlockMetadata(x + i, y - h, z + j);
//                            int hits = getHitCount(id2, meta2);
//                            if (hits < 0 && Math.abs(hits) >= h) {
//                                this.checkIncrementAndBreak(world, x + i, y - h, z + j, id2, meta2);
//                            }
//                        }
//                        this.checkIncrementAndBreak(world, x + i, y, z + j, id, meta);
//                    }
//                }
//            }
//        }
//        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage("FDD");
//        SoundRegistry.PILEDRIVER.playSoundAtBlock(world, pos, 1, 1);
//        for (int i = -2; i < 3; i++) {
//            for (int j = -2; j < 3; j++) {
//                if (i * j != 4 && i * j != -4 && world.getBlock(x + i, y, z + j) != Blocks.AIR && ReikaWorldHelper.defaultBlockState().getMaterial(world, x + i, y, z + j) != Material.water && ReikaWorldHelper.defaultBlockState().getMaterial(world, x + i, y, z + j) != Material.lava) {
//                    cleared = false;
//                    //world.setBlock(pos, RotaryCraft.miningpipe.blockID, BITMETA);
//                }
//            }
//        }
//        this.breakGlass(world, pos);
//        return cleared;
//    }
//
//    private void checkIncrementAndBreak(Level world, BlockPos pos, Block id) {
//        BlockPos loc = new BlockPos(pos);
//        HitCount c = numHits.get(loc);
//        boolean flag = false;
//        if (c != null) {
//            if (c.hit()) {
//                numHits.remove(loc);
//                flag = true;
//            }
//        } else {
//            int ct = getHitCount(id, meta);
//            if (ct <= 0) {
//                flag = true;
//            } else {
//                c = new HitCount(ct);
//                numHits.put(loc, c);
//            }
//        }
//        if (flag) {
//            BlockKey blockTo = this.getBlockProduct(world, pos, id, meta);
//            ArrayList<ItemStack> li = this.getDrops(world, pos);
//            if (!world.isClientSide)
//                blockTo.place(world, pos);
//            if (blockTo.blockID == Blocks.AIR) {
//                //Blocks.blocksList[id].dropBlockAsItem(world, x+i, y, z+j, meta, 0);
//                ReikaItemHelper.dropItems(world, pos, li);
//            }
//            world.markBlockForUpdate(pos);
//        }
//    }
//
//    private void spawnSpawner(Level world, BlockPos pos, BlockEntityMobSpawner spw) {
//        if (world.isClientSide)
//            return;
//        ItemStack is = RotaryItems.SPAWNER.get();
//        ReikaSpawnerHelper.addMobNBTToItem(is, spw);
//        ItemEntity ent = new ItemEntity(world, pos, is);
//        world.addFreshEntity(ent);
//    }
//
//    @Override
//    protected void writeSyncTag(CompoundTag NBT) {
//        super.writeSyncTag(NBT);
//        NBT.putInt("step", step);
//        NBT.putInt("step2", step2);
//        NBT.putBoolean("active", active);
//        NBT.putBoolean("climbing", climbing);
//        NBT.putBoolean("smashed", smashed);
//    }
//
//    @Override
//    protected void readSyncTag(CompoundTag NBT) {
//        super.readSyncTag(NBT);
//        step = NBT.getInt("step");
//        step2 = NBT.getInt("step2");
//        climbing = NBT.getBoolean("climbing");
//        active = NBT.getBoolean("active");
//        smashed = NBT.getBoolean("smashed");
//    }
//
//    @Override
//    public boolean hasModelTransparency() {
//        return false;
//    }
//
//    @Override
//    protected void animateWithTick(Level world, BlockPos pos) {
//        if (!this.isInWorld()) {
//            phi = 0;
//            return;
//        }
//        if (power < BASEPOWER * (step + 1) || torque < MINTORQUE)
//            return;
//        phi += ReikaMathLibrary.doubpow(ReikaMathLibrary.logbase(omega + 1, 2), 1.05);
//    }
//
//    @Override
//    public MachineRegistry getMachine() {
//        return MachineRegistry.PILEDRIVER;
//    }
//
//    @Override
//    public int getRedstoneOverride() {
//        return 0;
//    }
//
//    private static class HitCount {
//
//        private final int maxHits;
//        private int hits;
//
//        private HitCount(int max) {
//            maxHits = max;
//        }
//
//        private boolean hit() {
//            hits++;
//            return hits >= maxHits;
//        }
//
//    }
//
//}
