package reika.rotarycraft;

import net.minecraft.core.BlockPos;
import net.minecraft.core.Direction;
import net.minecraft.core.particles.ParticleTypes;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.sounds.SoundEvents;
import net.minecraft.sounds.SoundSource;
import net.minecraft.world.entity.player.Player;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.level.Level;
import net.minecraft.world.level.block.entity.BlockEntity;
import net.minecraft.world.level.block.state.properties.NoteBlockInstrument;
import reika.dragonapi.DragonOptions;
import reika.dragonapi.auxiliary.PacketTypes;
import reika.dragonapi.instantiable.MusicScore;
import reika.dragonapi.interfaces.PacketHandler;
import reika.dragonapi.libraries.io.ReikaChatHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper;
import reika.dragonapi.libraries.io.ReikaPacketHelper.DataPacket;
import reika.dragonapi.libraries.io.ReikaPacketHelper.PacketObj;
import reika.dragonapi.libraries.java.ReikaJavaLibrary;
import reika.dragonapi.libraries.mathsci.ReikaMathLibrary;
import reika.dragonapi.libraries.registry.ReikaParticleHelper;
import reika.rotarycraft.base.blockentity.BlockEntityAimedCannon;
import reika.rotarycraft.base.blockentity.BlockEntityIOMachine;
import reika.rotarycraft.base.blockentity.BlockEntityLaunchCannon;
import reika.rotarycraft.base.blockentity.EnergyToPowerBase;
import reika.rotarycraft.blockentities.BlockEntityBlower;
import reika.rotarycraft.blockentities.BlockEntityItemCannon;
import reika.rotarycraft.blockentities.BlockEntityPlayerDetector;
import reika.rotarycraft.blockentities.BlockEntityWinder;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityHeater;
import reika.rotarycraft.blockentities.auxiliary.BlockEntityMirror;
import reika.rotarycraft.blockentities.decorative.BlockEntityMusicBox;
import reika.rotarycraft.blockentities.decorative.BlockEntityParticleEmitter;
import reika.rotarycraft.blockentities.production.BlockEntityRefrigerator;
import reika.rotarycraft.blockentities.transmission.*;
import reika.rotarycraft.blockentities.weaponry.BlockEntityContainment;
import reika.rotarycraft.blockentities.weaponry.BlockEntityTNTCannon;
import reika.rotarycraft.registry.PacketRegistry;

import java.io.DataInputStream;
import java.io.IOException;
import java.util.Random;
import java.util.UUID;

public class PacketHandlerCore implements PacketHandler {

    protected PacketRegistry pack;

    private static final Random rand = new Random();

    public void handleData(PacketObj packet, Level world, Player ep) {
        DataInputStream inputStream = packet.getDataIn();
        int control = Integer.MIN_VALUE;
        int len;
        int[] data = new int[0];
        long longdata = 0;
        float floatdata = 0;
        int x = 0;
        int y = 0;
        int z = 0;
        double dx = 0;
        double dy = 0;
        double dz = 0;
        boolean readinglong = false;
        CompoundTag NBT = null;
        String stringdata = null;
        UUID id = null;
        //System.out.print(packet.length);
        try {
            ReikaJavaLibrary.pConsole(inputStream.readInt()+":"+inputStream.readInt()+":"+inputStream.readInt()+":"+inputStream.readInt()+":"+inputStream.readInt()+":"+inputStream.readInt()+":"+inputStream.readInt());
            PacketTypes packetType = packet.getType();
            switch (packetType) {
                case FULLSOUND:
                    break;
                case SOUND:
                    return;
                case STRING:
                    stringdata = packet.readString();
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    break;
                case DATA:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    len = pack.numInts;
                    data = new int[len];
                    readinglong = pack.isLongPacket();
                    if (!readinglong) {
                        for (int i = 0; i < len; i++)
                            data[i] = inputStream.readInt();
                    } else
                        longdata = inputStream.readLong();
                    break;
                case POS:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    dx = inputStream.readDouble();
                    dy = inputStream.readDouble();
                    dz = inputStream.readDouble();
                    len = pack.numInts;
                    if (len > 0) {
                        data = new int[len];
                        for (int i = 0; i < len; i++)
                            data[i] = inputStream.readInt();
                    }
                    break;
                case UPDATE:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    break;
                case FLOAT:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    floatdata = inputStream.readFloat();
                    break;
                case SYNC:
                    String name = packet.readString();
                    x = inputStream.readInt();
                    y = inputStream.readInt();
                    z = inputStream.readInt();
                    ReikaPacketHelper.updateBlockEntityData(world, x, y, z, name, inputStream);
                    return;
                case TANK:
                    String tank = packet.readString();
                    x = inputStream.readInt();
                    y = inputStream.readInt();
                    z = inputStream.readInt();
                    int level = inputStream.readInt();
                    ReikaPacketHelper.updateBlockEntityTankData(world, x, y, z, tank, level);
                    return;
                case RAW:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    len = pack.numInts;
                    data = new int[len];
                    readinglong = pack.isLongPacket();
                    if (!readinglong) {
                        for (int i = 0; i < len; i++)
                            data[i] = inputStream.readInt();
                    } else
                        longdata = inputStream.readLong();
                    break;
                case PREFIXED:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    len = inputStream.readInt();
                    data = new int[len];
                    for (int i = 0; i < len; i++)
                        data[i] = inputStream.readInt();
                    break;
                case NBT:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    NBT = ((DataPacket) packet).asNBT();
                    BlockPos c = pack.getCoordinate(NBT);
                    if (c != null) {
                        x = c.getX();
                        y = c.getY();
                        z = c.getZ();
                    }
                    break;
                case STRINGINT:
                case STRINGINTLOC:
                    stringdata = packet.readString();
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    data = new int[pack.numInts];
                    for (int i = 0; i < data.length; i++)
                        data[i] = inputStream.readInt();
                    break;
                case UUID:
                    control = inputStream.readInt();
                    pack = PacketRegistry.getEnum(control);
                    long l1 = inputStream.readLong(); //most
                    long l2 = inputStream.readLong(); //least
                    id = new UUID(l1, l2);
                    break;
            }
            if (packetType.hasCoordinates()) {
                x = inputStream.readInt();
                y = inputStream.readInt();
                z = inputStream.readInt();
            }
        } catch (IOException e) {
            e.printStackTrace();
            return;
        }
        BlockEntity te = world.getBlockEntity(new BlockPos(x, y, z));
		//todo This apparently breaks all non-block packets
		if (te == null) {
			return;
		}
        try {
            switch (pack) {
               /* case BORERTOGGLEALL: {
                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d   %d", control, data));
                    BlockEntityBorer borer = (BlockEntityBorer) te;
                    for (int i = 0; i < 5; i++) {
                        for (int j = 0; j < 7; j++) {
                            borer.cutShape[j][i] = !borer.cutShape[j][i];
                            borer.syncAllData(true);
                        }
                    }
                    break;
                }
                case BORERRESET: {
                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d   %d", control, data));
                    BlockEntityBorer borer = (BlockEntityBorer) te;
                    borer.reset();
                    break;
                }
                case BORERDROPS: {
                    BlockEntityBorer borer = (BlockEntityBorer) te;
                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d", data));
                    if (borer.drops) {
                        borer.drops = false;
                    } else {
                        borer.drops = true;
                    }
                    break;
                }
                case BORER: {
                    BlockEntityBorer borer = (BlockEntityBorer) te;
                    if (data[0] > 0 && data[0] < 100) {
                        int roworld = data[0] / 7;
                        int cols = data[0] - roworld * 7;
                        borer.cutShape[cols][roworld] = false;
                        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d -> row %d col %d", data, roworld, cols));
                    }
                    if (data[0] < 0 && data[0] > -100) {
                        int roworld = -data[0] / 7;
                        int cols = -data[0] - roworld * 7;
                        borer.cutShape[cols][roworld] = true;
                        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d -> row %d col %d", data, roworld, cols));
                    }
                    if (data[0] == 100) {
                        borer.cutShape[0][0] = false;
                        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d -> row %d col %d", data, 0, 0));
                    }
                    if (data[0] == -100) {
                        borer.cutShape[0][0] = true;
                        //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d -> row %d col %d", data, 0, 0));
                    }
                    break;
                }*/
                case BEVEL:
                    ((BlockEntityBevelGear) te).direction = data[0];
                    break;
                case SPLITTERMODE:
                    ((BlockEntitySplitter) te).setMode(data[0]);
                    break;
//                case SPAWNERTIMER: {
//                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", control, data));
//                    BlockEntitySpawnerController spawner = (BlockEntitySpawnerController) te;
//                    if (data[0] == -1) {
//                        spawner.disable = true;
//                    } else {
//                        spawner.disable = false;
//                        spawner.setDelay(data[0]);
//                    }
//                    break;
//                }
                case DETECTOR:
                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", control, data));
                    ((BlockEntityPlayerDetector) te).selectedrange = data[0];
                    break;
                case HEATER:
                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", control, data));
                    ((BlockEntityHeater) te).setTemperature = data[0];
                    break;
                case CVTMODE: {
                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", control, data));
                    BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
                    adv.stepMode();
                    break;
                }
                case CVTRATIO: {
                    BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
                    adv.setRatio(data[0]);
                    break;
                }
                case CVTTARGET: {
                    BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
                    adv.setTargetTorque(data[0]);
                    break;
                }
                case CVTREDSTONESTATE: {
                    BlockEntityAdvancedGear adv = (BlockEntityAdvancedGear) te;
                    adv.incrementCVTState(data[0] > 0);
                    break;
                }
                case CANNONFIRINGVALS: {
                    //ModLoader.getMinecraftInstance().thePlayer.addChatMessage(String.format("%d  %d", control, data));
                    BlockEntityLaunchCannon cannon = (BlockEntityLaunchCannon) te;
                    if (data[0] == 0) {
                        cannon.phi = data[1];
                        if (data[2] > cannon.getMaxTheta())
                            cannon.theta = cannon.getMaxTheta();
                        else
                            cannon.theta = data[2];
                        if (data[3] > cannon.getMaxLaunchVelocity())
                            cannon.velocity = cannon.getMaxLaunchVelocity();
                        else
                            cannon.velocity = data[3];
                    } else {
                        cannon.target[0] = data[1];
                        cannon.target[1] = data[2];
                        cannon.target[2] = data[3];
                        double ddx = cannon.target[0] - cannon.getBlockPos().getX();
                        double ddz = cannon.target[2] - cannon.getBlockPos().getZ();
                        double dd = ReikaMathLibrary.py3d(ddx, 0, ddz);
                        if (dd > cannon.getMaxLaunchDistance()) {
                            cannon.target[0] = cannon.getBlockPos().getX();
                            cannon.target[1] = 512;
                            cannon.target[2] = cannon.getBlockPos().getZ();
                        }
                    }
                    if (cannon instanceof BlockEntityTNTCannon) {
                        ((BlockEntityTNTCannon) cannon).selectedFuse = data[4];
                    }
                    break;
                }/*
                case SONICPITCH:
                    ((BlockEntitySonicWeapon) te).setpitch = longdata;
                    break;
                case SONICVOLUME:
                    ((BlockEntitySonicWeapon) te).setvolume = longdata;
                    break;
                case FORCE:
                    ((BlockEntityForceField) te).setRange = data[0];
                    break;
                case CHEST: {
                    BlockEntityScaleableChest chest = (BlockEntityScaleableChest) te;
                    chest.page = data[0];
                    ep.openGui(RotaryCraft.getInstance(), 24000 + data[0], chest.level, chest.xCoord, chest.yCoord, chest.zCoord);
                    break;
                }*/
                case COILSPEED:
                    ((BlockEntityAdvancedGear) te).setReleaseOmega(data[0]);
                    break;
                case COILTORQUE:
                    ((BlockEntityAdvancedGear) te).setReleaseTorque(data[0]);
                    break;
                case MUSICNOTE: {
                    BlockEntityMusicBox music = (BlockEntityMusicBox) te;
                    BlockEntityMusicBox.Note n = new BlockEntityMusicBox.Note(BlockEntityMusicBox.NoteLength.values()[data[2]], data[0], BlockEntityMusicBox.Instrument.values()[data[3]]);
                    for (int i = 0; i < 3; i++)
                        ;//todo figure out why reika commented this out? n.play(world, new BlockPos(x, y, z));
                    music.addNote(data[1], n);
                    break;
                }
                case MUSICSAVE:
                    ((BlockEntityMusicBox) te).save();
                    break;
                case MUSICREAD:
                    ((BlockEntityMusicBox) te).read();
                    break;
                case MUSICDEMO:
                    ((BlockEntityMusicBox) te).loadDemo();
                    break;
                case MUSICREST:
                    BlockEntityMusicBox.Note n = new BlockEntityMusicBox.Note(BlockEntityMusicBox.NoteLength.values()[data[1]], 0, BlockEntityMusicBox.Instrument.GUITAR);
                    ((BlockEntityMusicBox) te).addRest(data[0], n);
                    break;
                case MUSICBKSP:
                    ((BlockEntityMusicBox) te).backspace(data[0]);
                    break;
                case MUSICCLEARCH:
                    ((BlockEntityMusicBox) te).clearChannel(data[0]);
                    break;
                case MUSICCLEAR:
                    ((BlockEntityMusicBox) te).clearMusic();
                    break;
//                case VACUUMXP:
//                    ((BlockEntityVacuum) te).spawnXP();
//                    break;
                case WINDERTOGGLE:
                    BlockEntityWinder winder = (BlockEntityWinder) te;
                    winder.winding = !winder.winding;
                    winder.iotick = 512;
                    break;
//                case PROJECTOR:
//                    ((BlockEntityProjector) te).cycleInv();
//                    break;
                case CONTAINMENT:
                    ((BlockEntityContainment) te).setRange = data[0];
                    break;
                case ITEMCANNON:
                    ((BlockEntityItemCannon) te).selectNewTarget(null /*todo DIMENSION FROM INT DATA data[0]*/, new BlockPos(data[1], data[2], data[3]));
                    break;
                case MIRROR:
                    ((BlockEntityMirror) te).breakMirror(world, new BlockPos(x, y, z));
                    break;
                case SAFEPLAYER:
                    ((BlockEntityAimedCannon) te).removePlayerFromWhiteList(stringdata);
                    break;
//                case ENGINEBACKFIRE:
//                    ((BlockEntityJetEngine) te).backFire(world, x, y, z);
//                    break;
                case MUSICPARTICLE:
                    world.addParticle(ParticleTypes.NOTE, x + 0.2 + rand.nextDouble() * 0.6, y + 1.2, z + 0.2 + rand.nextDouble() * 0.6, rand.nextDouble(), 0.0D, 0.0D); //activeNote/24D
                    break;
                case MULTISIDE:
                    ((BlockEntityMultiClutch) te).setSideOfState(data[0], data[1]);
                    break;
//                case TERRAFORMER:
//                    ((BlockEntityTerraformer) te).setTarget(Biome.biomeList[data[0]]);
//                    break;
                case CONVERTERPOWER:
                    EnergyToPowerBase eng = (EnergyToPowerBase) te;
                    if (data[0] <= 0)
                        eng.decrementOmega();
                    else
                        eng.incrementOmega();
                    break;
                case CONVERTERREDSTONE:
                    ((EnergyToPowerBase) te).incrementRedstoneState();
                    break;
                case FERTILIZER:
                    if (world.isClientSide) {
                        ReikaParticleHelper.BONEMEAL.spawnAroundBlock(world, new BlockPos(x, y, z), 4);
                    }
                    break;
                case GRAVELGUN:
                    //ReikaJavaLibrary.pConsole(x+", "+y+", "+z);
                    ReikaParticleHelper.EXPLODE.spawnAroundBlock(world, new BlockPos(x, y, z), 1);
                    world.playLocalSound(x, y, z, SoundEvents.GENERIC_EXPLODE, SoundSource.BLOCKS, 1, 1F, false);
                    break;
                case SLIDE: {
                    ItemStack is = ep.getMainHandItem();
                    is.getOrCreateTag().putString("file", stringdata);
                    break;
                }
                /*case POWERBUS:
                    BlockEntityPowerBus bus = (BlockEntityPowerBus) te;
                    Direction dir = Direction.values()[data[0] + 2];
                    bus.setMode(dir, !bus.isSideSpeedMode(dir));
                    break;*/
                case PARTICLES:
                    ((BlockEntityParticleEmitter) te).particleType = ReikaParticleHelper.particleList[data[0]];
                    break;
                case BLOWERWHITELIST:
                    ((BlockEntityBlower) te).isWhitelist = !((BlockEntityBlower) te).isWhitelist;
                    break;
                case BLOWERNBT:
                    ((BlockEntityBlower) te).checkNBT = !((BlockEntityBlower) te).checkNBT;
                    break;
                case BLOWEROREDICT:
                    ((BlockEntityBlower) te).useOreDict = !((BlockEntityBlower) te).useOreDict;
                    break;
//                case DEFOLIATOR:
//                    ((BlockEntityDefoliator) te).onBlockBreak(world, data[0], data[1], data[2]);
//                    break;
               /* case GPR:
                    BlockEntityGPR gpr = (BlockEntityGPR) te;
                    int direction = data[0];
                    gpr.shift(gpr.getGuiDirection(), direction);
                    break;
                case CRAFTERCRAFT:
                    ((BlockEntityAutoCrafter) te).triggerCraftingCycle(data[0]);
                    break;
                case CRAFTERTHRESH:
                    ((BlockEntityAutoCrafter) te).setThreshold(data[0], data[1]);
                    break;
                case CRAFTERMODE:
                    ((BlockEntityAutoCrafter) te).incrementMode();
                    break;*/
                case POWERSYNC:
                    BlockEntityIOMachine io = (BlockEntityIOMachine) te;
                    io.torque = data[0];
                    io.omega = data[1];
                    io.power = ReikaJavaLibrary.buildLong(data[2], data[3]);
                    break;
                /*case AFTERBURN:
                    ((BlockEntityJetEngine) te).setBurnerActive(data[0] > 0);
                    break;
                case CRAFTPATTERNMODE:
                    ItemCraftPattern.setMode(ep.getCurrentEquippedItem(), RecipeMode.list[data[0]]);
                    break;
                case CRAFTPATTERNLIMIT:
                    ItemCraftPattern.changeStackLimit(ep.getCurrentEquippedItem(), data[0]);
                    break;
                case FILTERSETTING:
                    MatchData dat = MatchData.createFromNBT(NBT);
                    ((BlockEntityItemFilter) te).setData(dat);
                    break;
                case SPRINKLER:
                    ((BlockEntitySprinkler) te).doParticle(world, data[0], data[1], data[2], data[3] > 0);
                    break;
                case BLASTLEAVEONE:
                    ((BlockEntityBlastFurnace) te).leaveLastItem = data[0] > 0;
                    break;
                case EMPEFFECT:
                    ((BlockEntityEMP) te).initEffect();
                    break;*/
                case SPARKLOC:
                    if (data[4] > 0) {
//                todo        EMPSparkRenderer.instance.addSparkingLocation(new WorldLocation(data[0], data[1], data[2], data[3]));
                    } else {
//                  todo      EMPSparkRenderer.instance.removeSparkingLocation(new WorldLocation(data[0], data[1], data[2], data[3]));
                    }
                    break;
                case DISTRIBCLUTCH:
                    ((BlockEntityDistributionClutch) te).setSideEnabled(Direction.values()[data[0] + 2], data[1] > 0);
                    break;
                case DISTRIBCLUTCHPOWER:
                    ((BlockEntityDistributionClutch) te).setTorqueRequests(data);
                    break;
                case FRIDGEBREAK:
                    BlockEntityRefrigerator.doBreakFX(world, new BlockPos(x, y, z));
                    break;
            }
        } catch (NullPointerException e) {
            RotaryCraft.LOGGER.error("Machine/item was deleted before its packet " + pack + " could be received, or was not present at all!");
            if (DragonOptions.CHATERRORS.getState())
                ReikaChatHelper.writeString("Machine/item was deleted before its packet " + pack + " could be received, or was not present at all!");
            e.printStackTrace();
        } catch (Exception e) {
            e.printStackTrace();
        }
    }

    public static void sendPowerSyncPacket(BlockEntityIOMachine iotile, ServerPlayer ep) {
        int[] data = ReikaJavaLibrary.splitLong(iotile.power);
        if (ep != null) {
            ReikaPacketHelper.sendDataPacket(RotaryCraft.packetChannel, PacketRegistry.POWERSYNC.ordinal(), iotile, ep, iotile.torque, iotile.omega, data[0], data[1]);
        } else {
            ReikaPacketHelper.sendDataPacketWithRadius(RotaryCraft.packetChannel, PacketRegistry.POWERSYNC.ordinal(), iotile, 64, iotile.torque, iotile.omega, data[0], data[1]);
        }
    }
}
