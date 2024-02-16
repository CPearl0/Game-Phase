package com.cpearl.gamephase.network;

import com.cpearl.gamephase.events.GamePhaseInitializeEvent;
import com.cpearl.gamephase.events.GamePhaseSyncEvent;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.NetworkEvent;

import java.util.function.Supplier;

public class ClientPacketHandler {
    public static void handlePacket(GamePhaseMessage msg, Supplier<NetworkEvent.Context> context) {
        switch (msg.getType()) {
            case GamePhaseMessage.INIT -> MinecraftForge.EVENT_BUS.post(new GamePhaseInitializeEvent(msg.getPhases()));
            case GamePhaseMessage.SYNC -> MinecraftForge.EVENT_BUS.post(new GamePhaseSyncEvent(msg.getPhases()));
        }
    }
}
