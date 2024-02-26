package com.cpearl.gamephase.events;

import com.cpearl.gamephase.GamePhase;
import com.cpearl.gamephase.capability.GamePhaseCapability;
import com.cpearl.gamephase.capability.GamePhaseCapabilityProvider;
import com.cpearl.gamephase.commands.GamePhaseCommand;
import com.cpearl.gamephase.network.GamePhaseMessage;
import com.cpearl.gamephase.network.GamePhasePacketHandler;
import net.minecraft.nbt.CompoundTag;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.entity.Entity;
import net.minecraftforge.event.AttachCapabilitiesEvent;
import net.minecraftforge.event.RegisterCommandsEvent;
import net.minecraftforge.event.entity.EntityTravelToDimensionEvent;
import net.minecraftforge.event.entity.living.LivingDeathEvent;
import net.minecraftforge.event.entity.player.PlayerEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;
import net.minecraftforge.network.PacketDistributor;

@Mod.EventBusSubscriber(modid = GamePhase.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class EventHandler {
    @SubscribeEvent
    public static void addCapability(AttachCapabilitiesEvent<Entity> event) {
        if (event.getObject() instanceof ServerPlayer player) {
            event.addCapability(new ResourceLocation(GamePhase.MODID, "phase"), new GamePhaseCapabilityProvider());
        }
    }

    @SubscribeEvent
    public static void onPlayerLoggedIn(PlayerEvent.PlayerLoggedInEvent event) {
        if (event.getEntity() instanceof ServerPlayer player) {
            player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
                GamePhaseMessage msg = new GamePhaseMessage(phaseCapability.getPhases(), GamePhaseMessage.INIT);
                GamePhasePacketHandler.INSTANCE.send(PacketDistributor.PLAYER.with(() -> player), msg);
            });
        }
    }

    @SubscribeEvent
    public static void onEntityTravelToDimension(EntityTravelToDimensionEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;
        if (event.getDimension().location().toString().equals("minecraft:overworld") &&
            player.getLevel().dimension().location().toString().equals("minecraft:the_end"))
            player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
                if (phaseCapability instanceof GamePhaseCapability capability) {
                    player.getPersistentData().put("clone_phases", capability.serializeNBT());
                }
            });
    }

    @SubscribeEvent
    public static void onLivingDeath(LivingDeathEvent event) {
        if (!(event.getEntity() instanceof ServerPlayer player))
            return;
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            if (phaseCapability instanceof GamePhaseCapability capability) {
                player.getPersistentData().put("clone_phases", capability.serializeNBT());
            }
        });
    }

    @SubscribeEvent
    public static void onPlayerClone(PlayerEvent.Clone event) {
        if (event.getOriginal() instanceof ServerPlayer oldPlayer &&
                event.getEntity() instanceof ServerPlayer newPlayer) {
            newPlayer.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
                if (phaseCapability instanceof GamePhaseCapability capability) {
                    var tag = oldPlayer.getPersistentData().get("clone_phases");
                    if (tag instanceof CompoundTag compoundTag)
                        capability.deserializeNBT(compoundTag);
                }
            });
        }
    }

    @SubscribeEvent
    public static void onCommandRegister(RegisterCommandsEvent event) {
        GamePhaseCommand.registerCommands(event);
    }
}
