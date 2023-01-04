/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.items.tools;

import net.minecraft.core.Direction;
import net.minecraft.world.InteractionHand;
import net.minecraft.world.InteractionResult;
import net.minecraft.world.InteractionResultHolder;
import net.minecraft.world.entity.LivingEntity;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.context.UseOnContext;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.Block;
import net.minecraft.world.level.block.Blocks;
import reika.dragonapi.libraries.level.ReikaWorldHelper;
import reika.rotarycraft.RotaryCraft;
import reika.rotarycraft.api.interfaces.Screwdriverable;
import reika.rotarycraft.api.power.ShaftMachine;
import reika.rotarycraft.base.ItemRotaryTool;
import reika.rotarycraft.base.blockentity.BlockEntityEngine;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blockentity.RotaryCraftBlockEntity;
import reika.rotarycraft.base.blocks.BlockRotaryCraftMachine;
import reika.rotarycraft.blockentities.BlockEntityBucketFiller;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityCoolingFin;
import reika.rotarycraft.blockentities.level.BlockEntityFloodlight;
import reika.rotarycraft.blockentities.transmission.*;
import reika.rotarycraft.registry.MachineRegistry;
import reika.rotarycraft.registry.MaterialRegistry;

import java.util.HashMap;

/*@Strippable(value = {"buildcraft.api.tools.IToolWrench", "mrtjp.projectred.api.IScrewdriver", "binnie.extratrees.api.IToolHammer",
        "powercrystals.minefactoryreloaded.api.IMFRHammer", "santa.api.interfaces.wrench.IWrench", "com.carpentersblocks.api.ICarpentersHammer",
        "com.bluepowermod.api.misc.IScrewdriver"})*/
public class ItemScrewdriver extends ItemRotaryTool //implements IToolWrench, IScrewdriver, IToolHammer, IMFRHammer, IWrench, ICarpentersHammer, com.bluepowermod.api.misc.IScrewdriver
{
    private static final HashMap<Block, Integer> maxdamage = new HashMap<>(); //Max damage values (or BlockEntity datas) for the block ids associated

    static {
        maxdamage.put(Blocks.PISTON, 5);
        maxdamage.put(Blocks.STICKY_PISTON, 5);
        maxdamage.put(Blocks.DISPENSER, 5);
        maxdamage.put(Blocks.FURNACE, 3);
        maxdamage.put(Blocks.BLAST_FURNACE, 3);
        maxdamage.put(Blocks.OAK_STAIRS, 7);
        maxdamage.put(Blocks.STONE_STAIRS, 7);
        maxdamage.put(Blocks.BRICK_STAIRS, 7);
        maxdamage.put(Blocks.STONE_BRICK_STAIRS, 7);
        maxdamage.put(Blocks.SANDSTONE_STAIRS, 7);
        maxdamage.put(Blocks.SPRUCE_STAIRS, 7);
        maxdamage.put(Blocks.BIRCH_STAIRS, 7);
        maxdamage.put(Blocks.JUNGLE_STAIRS, 7);
        maxdamage.put(Blocks.NETHER_BRICK_STAIRS, 7);
        maxdamage.put(Blocks.QUARTZ_STAIRS, 7);
        maxdamage.put(Blocks.DROPPER, 5);
        maxdamage.put(Blocks.CARVED_PUMPKIN, 3);
        maxdamage.put(Blocks.HOPPER, 5);
    }

   /* {
        if (!world.isClientSide) {
            Block id = world.getBlockState(pos).getBlock();
            damage = world.getBlockMetadata(pos);
            if (id == Blocks.END_PORTAL_FRAME) {
                if (damage >= 4) {
                    world.setBlockMetadataWithNotify(pos, damage - 4, 3);
                    ReikaItemHelper.dropItem(world, x + 0.5, y + 1, z + 0.5, new ItemStack(Items.ENDER_EYE));
                } else {
                    int newmeta = damage == 3 ? 0 : damage + 1;
                    world.setBlockMetadataWithNotify(pos, newmeta, 3);
                }
                return InteractionResult.SUCCESS;
            }
            if (id instanceof BlockRedstoneDiode) {
                int newmeta = damage % 4 == 3 ? damage - 3 : damage + 1;
                world.setBlockMetadataWithNotify(pos, newmeta, 3);
                return InteractionResult.SUCCESS;
            }
            if ((id == Blocks.STICKY_PISTON || id == Blocks.PISTON) && world.isBlockIndirectlyGettingPowered(pos))
                return false;
            if (maxdamage.containsKey(id)) {
                if (damage < maxdamage.get(id)) {
                    world.setBlockMetadataWithNotify(pos, damage + 1, 3);
                } else {
                    world.setBlockMetadataWithNotify(pos, 0, 3);
                }
            }
            if (InterfaceCache.RECONFIGURABLEFACE.instanceOf(te))
                return ((IReconfigurableFacing) te).rotateBlock();
        }
    }*/

    public ItemScrewdriver() {
        super(new Properties());
    }

    @Override
    public InteractionResultHolder<ItemStack> use(Level level, Player player, InteractionHand hand) {
        return super.use(level, player, hand);
    }

    @Override
    public void onUseTick(Level p_41428_, LivingEntity p_41429_, ItemStack p_41430_, int p_41431_) {
        super.onUseTick(p_41428_, p_41429_, p_41430_, p_41431_);
    }

    @Override
    public InteractionResult onItemUseFirst(ItemStack stack, UseOnContext context) {
        var level = context.getLevel();
        var pos = context.getClickedPos();
        var ep = context.getPlayer();
        var te = level.getBlockEntity(pos);
        var s = context.getClickedFace(); //I'm assuming S was the direction of the face that was clicked
        int direction = 0;
        RotaryCraft.LOGGER.info("Screwdriver used on " + pos + " on the" + s + " side");
        // if (ReikaPlayerAPI.isFakeOrNotInteractable(ep, new BlockPos(ep.position()), 8))
        //    return return InteractionResultHolder.fail(this.getDefaultInstance());;

        if (te instanceof RotaryCraftBlockEntity) {
            RotaryCraftBlockEntity t = (RotaryCraftBlockEntity) te;
            direction = t.getBlockState().getValue(BlockRotaryCraftMachine.FACING).ordinal();
        }
        if (te instanceof BlockEntityIOMachine) {
            ((BlockEntityIOMachine) te).iotick = 512;
            level.setBlockAndUpdate(pos, te.getBlockState());
        }
        if (te instanceof ShaftMachine) {
            ShaftMachine sm = (ShaftMachine) te;
            sm.setIORenderAlpha(512);
            level.setBlockAndUpdate(pos, te.getBlockState());
        }
        if (te instanceof Screwdriverable) {
            Screwdriverable sc = (Screwdriverable) te;
            boolean flag = false;
            if (ep.isShiftKeyDown())
                flag = sc.onShiftRightClick(level, pos, s);
            else
                flag = sc.onRightClick(level, pos, s);
            if (flag)
                return InteractionResult.SUCCESS;
        }
        MachineRegistry m = MachineRegistry.getMachine(level, pos);
        RotaryCraft.LOGGER.info("The machine is: " + m);
        if (m != null) {
            if (m == MachineRegistry.ENGINE) {
                BlockEntityEngine clicked = (BlockEntityEngine) te;
                int dmg = direction;
                while (direction > 3)
                    direction -= 4;
                if (direction == 3)
//                    clicked.setBlockMetadata(dmg - 3);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(dmg - 3));
                else
//                    clicked.setBlockMetadata(dmg + 1);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(dmg + 1));

                clicked.onRotate();
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.FLYWHEEL) {
                BlockEntityFlywheel clicked = (BlockEntityFlywheel) te;
                if (direction != 3 && direction != 7 && direction != 11 && direction != 15)
//                    clicked.setBlockMetadata(direction + 1);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction + 1));
                else
//                    clicked.setBlockMetadata(direction - 3);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction - 3));

                context.getPlayer().swing(InteractionHand.MAIN_HAND);
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.COOLINGFIN) {
                BlockEntityCoolingFin clicked = (BlockEntityCoolingFin) te;
                clicked.ticks = 512;
                if (ep.isShiftKeyDown()) {
                    clicked.setting = clicked.setting.next();
                    return InteractionResult.SUCCESS;
                }
            }
            /*if (m == MachineRegistry.ECU) {
                BlockEntityEngineController clicked = (BlockEntityEngineController) te;
                if (ep.isShiftKeyDown()) {
                    clicked.redstoneMode = !clicked.redstoneMode;
                    ReikaChatHelper.writeString(clicked.redstoneMode ? "ECU is now redstone-operated." : "ECU is now manually controlled.");
                } else {
                    clicked.increment();
                    ReikaChatHelper.writeString(String.format("ECU set to %.2f%s speed.", 100D * clicked.getSpeedMultiplier(), "%%"));
                }
            }
            if (m == MachineRegistry.ADVANCEDGEARS) {
                BlockEntityAdvancedGear clicked = (BlockEntityAdvancedGear) te;
                if (ep.isShiftKeyDown()) {
                    clicked.torquemode = !clicked.torquemode;
                } else {
                    if (damage != 3 && damage != 7 && damage != 11 && damage != 15)
                        clicked.setBlockMetadata(damage + 1);
                    else
                        clicked.setBlockMetadata(damage - 3);
                }
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.HYDRAULIC) {
                BlockEntityHydraulicPump clicked = (BlockEntityHydraulicPump) te;
                if (damage != 5 && damage != 11)
                    clicked.setBlockMetadata(damage + 1);
                else
                    clicked.setBlockMetadata(damage - 5);
                return InteractionResult.SUCCESS;
            }*/
            if (m == MachineRegistry.SHAFT) {
                BlockEntityShaft ts = (BlockEntityShaft) te;
                MaterialRegistry type = ts.getShaftType();
                if (direction < 5)
//                    ts.setBlockMetadata(direction + 1);
                    ts.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction + 1));
                if (direction == 5)
//                    ts.setBlockMetadata(0);
                    ts.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(0));
                if (direction > 5 && direction < 9)
//                    ts.setBlockMetadata(direction + 1);
                    ts.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction + 1));
                if (direction == 9)
//                    ts.setBlockMetadata(6);
                    ts.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(6));

                BlockEntityShaft ts1 = (BlockEntityShaft) te;
                ts1.setShaftType(type);
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.CLUTCH) {
                if (ep.isShiftKeyDown()) {
                    BlockEntityClutch tc = (BlockEntityClutch) te;
                    tc.needsRedstone = !tc.needsRedstone;
                    return InteractionResult.SUCCESS;
                }
            }
            if (m == MachineRegistry.DISTRIBCLUTCH) {
                RotaryCraft.LOGGER.info("clutch yeah");
                if (ep.isShiftKeyDown()) {
                    BlockEntityDistributionClutch tc = (BlockEntityDistributionClutch) te;
                    tc.stepMode();
                    return InteractionResult.SUCCESS;
                }
            }
            /*if (m == MachineRegistry.FAN) {
                if (ep.isShiftKeyDown()) {
                    BlockEntityFan tf = (BlockEntityFan) te;
                    tf.wideAreaHarvest = !tf.wideAreaHarvest;
                    return InteractionResult.SUCCESS;
                }
            }*/
            if (m == MachineRegistry.FLOODLIGHT) {
                if (ep.isShiftKeyDown()) {
                    BlockEntityFloodlight clicked = (BlockEntityFloodlight) te;
                    if (clicked != null && clicked.getBlockState().getValue(BlockRotaryCraftMachine.FACING).toYRot() >= 4) { //clicked.getBlockMetadata()
                        clicked.beammode = !clicked.beammode;
                        clicked.lightsOut(level, ep.blockPosition());
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            /*if (m.isCannon()) {
                if (ep.isShiftKeyDown()) {
                    BlockEntityAimedCannon clicked = (BlockEntityAimedCannon) te;
                    if (clicked != null) {
                        clicked.targetPlayers = !clicked.targetPlayers;
                        return InteractionResult.SUCCESS;
                    }
                }
            }
            if (m == MachineRegistry.TNTCANNON) {
                BlockEntityTNTCannon clicked = (BlockEntityTNTCannon) te;
                if (clicked != null) {
                    clicked.targetMode = !clicked.targetMode;
                    return InteractionResult.SUCCESS;
                }
            }*/
            if (m == MachineRegistry.BUCKETFILLER) {
                BlockEntityBucketFiller clicked = (BlockEntityBucketFiller) te;
                if (clicked != null) {
                    clicked.filling = !clicked.filling;
                    return InteractionResult.SUCCESS;
                }
            }
            /*if (m == MachineRegistry.BELT || m == MachineRegistry.CHAIN) {
                BlockEntityBeltHub clicked = (BlockEntityBeltHub) te;
                if (ep.isShiftKeyDown()) {
                    if (clicked != null) {
                        clicked.isEmitting = !clicked.isEmitting;
                    }
                } else {
                    int newdmg = damage < 11 ? damage + 1 : 0;
                    // clicked.setBlockMetadata(newdmg);
                    clicked.reset();
                    clicked.resetOther();
                }
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.GPR) {
                BlockEntityGPR clicked = (BlockEntityGPR) te;
                if (clicked != null) {
                    clicked.flipDirection();
                    return InteractionResult.SUCCESS;
                }
            }
            if (m == MachineRegistry.CCTV) {
                BlockEntityCCTV clicked = (BlockEntityCCTV) te;
                if (ep.isShiftKeyDown()) {
                    clicked.theta -= 5;

                } else {
                    clicked.phi += 5;
                }
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.VACUUM) {
                BlockEntityVacuum clicked = (BlockEntityVacuum) te;
                if (ep.isShiftKeyDown())
                    clicked.equidistant = !clicked.equidistant;
                else
                    clicked.suckIfFull = !clicked.suckIfFull;
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.CRAFTER) {
                BlockEntityAutoCrafter clicked = (BlockEntityAutoCrafter) te;
                if (ep.isShiftKeyDown()) {
                    clicked.incrementMode();
                    ReikaChatHelper.sendChatToPlayer(ep, "Mode is now " + clicked.getMode().label);
                }
                return InteractionResult.SUCCESS;
            }*/
            if (m == MachineRegistry.GEARBOX) {
                if (ep.isShiftKeyDown()) {
                    BlockEntityGearbox clicked = (BlockEntityGearbox) level.getBlockEntity(pos);
                    clicked.reduction = !clicked.reduction;
                    return InteractionResult.SUCCESS;
                } else {
                    BlockEntityGearbox clicked = (BlockEntityGearbox) level.getBlockEntity(pos);
                    RotaryCraft.LOGGER.info("gearbox direction: " + direction);
                    if (direction != 3 && direction != 7 && direction != 11 && direction != 15) {
                        RotaryCraft.LOGGER.info("gearbox is turning 1");
//                        clicked.setBlockMetadata(direction + 1);
                        clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction + 1));
                    } else {
                        RotaryCraft.LOGGER.info("gearbox is turning");
//                        clicked.setBlockMetadata(direction - 3);
                        clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction - 3));
                    }
                    //ModLoader.getMinecraftInstance().ingameGUI.addChatMessage(String.format("%d", level.getBlockMetadata(pos)));
                }
                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.SPLITTER && (!ep.isShiftKeyDown())) {
                BlockEntitySplitter clicked = (BlockEntitySplitter) te;
                if (direction < 7 || (direction < 15 && direction > 7))
//                    clicked.setBlockMetadata(direction + 1);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction + 1));
                if (direction == 7)
//                    clicked.setBlockMetadata(0);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(0));
                if (direction == 15)
//                    clicked.setBlockMetadata(8);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(8));

                return InteractionResult.SUCCESS;
            }
            if (m == MachineRegistry.SPLITTER && (ep.isShiftKeyDown())) {    // Toggle in/out
                BlockEntitySplitter clicked = (BlockEntitySplitter) te;
                if (direction < 8)
//                    clicked.setBlockMetadata(direction + 8);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction + 8));
                else
//                    clicked.setBlockMetadata(direction - 8);
                    clicked.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(direction - 8));
                return InteractionResult.SUCCESS;
            }
            int max = m.getNumberDirections() - 1;
            RotaryCraftBlockEntity t = (RotaryCraftBlockEntity) te;

            if (t.getBlockState().getValue(BlockRotaryCraftMachine.FACING).ordinal() < max) {
                var i = t.getBlockState().getValue(BlockRotaryCraftMachine.FACING).ordinal();
                t.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.fromYRot(i + 1));
            } else
                t.getBlockState().setValue(BlockRotaryCraftMachine.FACING, Direction.DOWN);
            t.onRedirect();
            level.blockUpdated(pos, te.getBlockState().getBlock());
            ReikaWorldHelper.causeAdjacentUpdates(level, pos);

            return InteractionResult.SUCCESS;
        }
        return InteractionResult.PASS;
    }

}