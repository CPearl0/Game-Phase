package com.cpearl.gamephase.network;

import com.cpearl.gamephase.GamePhase;
import net.minecraft.resources.ResourceLocation;
import net.minecraftforge.network.NetworkRegistry;
import net.minecraftforge.network.simple.SimpleChannel;

public class GamePhasePacketHandler {
    private static final String PROTOCOL_VERSION = "1";
    public static final SimpleChannel INSTANCE = NetworkRegistry.newSimpleChannel(
            new ResourceLocation(GamePhase.MODID, "main"),
            () -> PROTOCOL_VERSION,
            PROTOCOL_VERSION::equals,
            PROTOCOL_VERSION::equals
    );

    public static void init() {
        INSTANCE.registerMessage(GamePhaseMessage.ID, GamePhaseMessage.class,
                GamePhaseMessage::encode, GamePhaseMessage::decode, GamePhaseMessage::process);
    }
}
