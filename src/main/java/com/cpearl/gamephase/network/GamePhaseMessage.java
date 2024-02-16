package com.cpearl.gamephase.network;

import net.minecraft.nbt.CompoundTag;
import net.minecraft.nbt.ListTag;
import net.minecraft.nbt.StringTag;
import net.minecraft.nbt.Tag;
import net.minecraft.network.FriendlyByteBuf;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.fml.DistExecutor;
import net.minecraftforge.network.NetworkEvent;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.function.Supplier;

public class GamePhaseMessage {
    public static final int ID = 0;
    public static final int INIT = 0;
    public static final int SYNC = 1;
    private final List<String> phases;
    private final int type;
    public GamePhaseMessage(Collection<String> phases, int type) {
        this.phases = new ArrayList<>(phases);
        this.type = type;
    }

    public List<String> getPhases() {
        return phases;
    }

    public int getType() {
        return type;
    }

    public void encode(FriendlyByteBuf buf) {
        CompoundTag tag = new CompoundTag();
        tag.putInt("Type", type);
        ListTag listPhases = new ListTag();
        for (var phase: phases) {
            listPhases.add(StringTag.valueOf(phase));
        }
        tag.put("Phases", listPhases);
        buf.writeNbt(tag);
    }

    public static GamePhaseMessage decode(FriendlyByteBuf buf) {
        CompoundTag nbt = buf.readNbt();
        assert nbt != null;
        var type = nbt.getInt("Type");
        var listPhases = nbt.getList("Phases", Tag.TAG_STRING);
        var phases = new ArrayList<String>();
        for (int i = 0; i < listPhases.size(); i++) {
            phases.add(listPhases.getString(i));
        }
        return new GamePhaseMessage(phases, type);
    }

    public void process(Supplier<NetworkEvent.Context> context) {
        context.get().enqueueWork(() -> {
            DistExecutor.unsafeRunWhenOn(Dist.CLIENT, () -> () -> {
                ClientPacketHandler.handlePacket(this, context);
            });
        });
        context.get().setPacketHandled(true);
    }
}
