/*******************************************************************************
 * @author Reika Kalseki
 *
 * Copyright 2017
 *
 * All rights reserved.
 * Distribution of the software in any form is only allowed with
 * explicit, prior permission from the owner.
 ******************************************************************************/
package reika.rotarycraft.blockentities.storage;

import java.util.ArrayList;
import java.util.List;

import com.mojang.serialization.Codec;
import com.mojang.serialization.codecs.RecordCodecBuilder;

import net.minecraft.network.RegistryFriendlyByteBuf;
import net.minecraft.network.codec.ByteBufCodecs;
import net.minecraft.network.codec.StreamCodec;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.ItemStackTemplate;

import reika.dragonapi.instantiable.storage.ManagedItemHandler;

/**
 * The inventory a harvested Scaleable Chest carries on its item — the 26.2 form of V33a's
 * {@code writeInventoryToItem}/{@code readInventoryFromItem}, which stored an {@code "Items"}
 * slot list in the stack's NBT. Vanilla's {@code DataComponents.CONTAINER} cannot hold it: it is
 * capped at 256 slots (indices 0..255), and the chest has {@value BlockEntityScaleableChest#MAXSIZE}.
 * Only non-empty slots are stored, each with its original index.
 */
public record ScaleChestContents(List<Slot> slots) {

    public static final ScaleChestContents EMPTY = new ScaleChestContents(List.of());

    public static final Codec<ScaleChestContents> CODEC = Slot.CODEC
            .sizeLimitedListOf(BlockEntityScaleableChest.MAXSIZE)
            .xmap(ScaleChestContents::new, ScaleChestContents::slots);

    public static final StreamCodec<RegistryFriendlyByteBuf, ScaleChestContents> STREAM_CODEC = Slot.STREAM_CODEC
            .apply(ByteBufCodecs.list(BlockEntityScaleableChest.MAXSIZE))
            .map(ScaleChestContents::new, ScaleChestContents::slots);

    public ScaleChestContents {
        slots = List.copyOf(slots);
    }

    public static ScaleChestContents fromHandler(ManagedItemHandler inv) {
        List<Slot> li = new ArrayList<>();
        for (int i = 0; i < inv.getSlots(); i++) {
            ItemStack is = inv.getStackInSlot(i);
            if (!is.isEmpty())
                li.add(new Slot(i, ItemStackTemplate.fromNonEmptyStack(is)));
        }
        return li.isEmpty() ? EMPTY : new ScaleChestContents(li);
    }

    /** Replaces the whole inventory, as V33a's {@code readInventoryFromItem} did ({@code inv = new ItemStack[...]}). */
    public void copyInto(ManagedItemHandler inv) {
        for (int i = 0; i < inv.getSlots(); i++)
            inv.setStackInSlot(i, ItemStack.EMPTY);
        for (Slot s : slots) {
            if (s.index() < inv.getSlots())
                inv.setStackInSlot(s.index(), s.item().create());
        }
    }

    public boolean isEmpty() {
        return slots.isEmpty();
    }

    public record Slot(int index, ItemStackTemplate item) {

        public static final Codec<Slot> CODEC = RecordCodecBuilder.create(i -> i.group(
                Codec.intRange(0, BlockEntityScaleableChest.MAXSIZE - 1).fieldOf("slot").forGetter(Slot::index),
                ItemStackTemplate.CODEC.fieldOf("item").forGetter(Slot::item)
        ).apply(i, Slot::new));

        public static final StreamCodec<RegistryFriendlyByteBuf, Slot> STREAM_CODEC = StreamCodec.composite(
                ByteBufCodecs.VAR_INT, Slot::index,
                ItemStackTemplate.STREAM_CODEC, Slot::item,
                Slot::new);
    }
}
