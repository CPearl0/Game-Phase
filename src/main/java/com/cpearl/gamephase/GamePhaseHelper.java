package com.cpearl.gamephase;

import com.cpearl.gamephase.capability.GamePhaseCapabilityProvider;
import com.cpearl.gamephase.events.GamePhaseUpdateEvent;
import com.cpearl.gamephase.network.GamePhaseMessage;
import com.cpearl.gamephase.network.GamePhasePacketHandler;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.common.MinecraftForge;
import net.minecraftforge.network.PacketDistributor;

import java.util.ArrayList;
import java.util.List;

public class GamePhaseHelper {
    public static List<String> getPhases(ServerPlayer player) {
        final List<String>[] ret = new List[]{null};
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            ret[0] = new ArrayList<>(phaseCapability.getPhases());
        });
        return ret[0];
    }

    public static void addPhase(ServerPlayer player, String phase) {
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            if (phaseCapability.hasPhase(phase))
                return;
            var phasesOld = new ArrayList<>(phaseCapability.getPhases());
            phaseCapability.addPhase(phase);
            var phasesNew = new ArrayList<>(phaseCapability.getPhases());

            if (MinecraftForge.EVENT_BUS.post(new GamePhaseUpdateEvent(phasesOld, phasesNew, player)))
                phaseCapability.removePhase(phase);
            else
                syncPlayer(player);
        });
    }

    public static void removePhase(ServerPlayer player, String phase) {
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            if (!phaseCapability.hasPhase(phase))
                return;
            var phasesOld = new ArrayList<>(phaseCapability.getPhases());
            phaseCapability.removePhase(phase);
            var phasesNew = new ArrayList<>(phaseCapability.getPhases());
            if (MinecraftForge.EVENT_BUS.post(new GamePhaseUpdateEvent(phasesOld, phasesNew, player)))
                phaseCapability.addPhase(phase);
            else
                syncPlayer(player);
        });
    }

    public static void clearPhase(ServerPlayer player) {
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            var phasesOld = new ArrayList<>(phaseCapability.getPhases());
            if (phasesOld.isEmpty())
                return;
            phaseCapability.clearPhase();
            var phasesNew = new ArrayList<>(phaseCapability.getPhases());
            if (MinecraftForge.EVENT_BUS.post(new GamePhaseUpdateEvent(phasesOld, phasesNew, player)))
                phasesOld.forEach(phaseCapability::addPhase);
            else
                syncPlayer(player);
        });
    }

    public static boolean hasPhase(ServerPlayer player, String phase) {
        final boolean[] res = {false};
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            if (phaseCapability.hasPhase(phase))
                res[0] = true;
        });
        return res[0];
    }

    public static void syncPlayer(ServerPlayer player) {
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            GamePhaseMessage msg = new GamePhaseMessage(phaseCapability.getPhases(), GamePhaseMessage.SYNC);
            GamePhase.LOGGER.info("Begin syncing player " + player.getDisplayName());
            GamePhasePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
        });
    }
}
