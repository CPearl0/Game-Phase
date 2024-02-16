package com.cpearl.gamephase.functions.dimension;

import com.cpearl.gamephase.GamePhase;
import com.cpearl.gamephase.capability.GamePhaseCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

@Mod.EventBusSubscriber(modid = GamePhase.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler {
    public static final Component DENY_DIMENSION_MESSAGE = Component.translatable("message.gamephase.denydimension").withStyle(ChatFormatting.RED);
    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;
        if (player.gameMode.getGameModeForPlayer().isCreative())
            return;
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            var dimensions = PhaseDimensions.dimensions;
            for (var entry: dimensions.entrySet()) {
                if (!phaseCapability.hasPhase(entry.getKey())) {
                    var removeDimensions = entry.getValue();
                    if (removeDimensions.stream().anyMatch(dimension -> dimension.equals(event.getDimension().location()))) {
                        event.setCanceled(true);
                        player.displayClientMessage(DENY_DIMENSION_MESSAGE, true);
                        return;
                    }
                }
            }
        });
    }
}
