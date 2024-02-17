package com.cpearl.gamephase.functions.item;

import com.cpearl.gamephase.GamePhase;
import com.cpearl.gamephase.capability.GamePhaseCapabilityProvider;
import net.minecraft.ChatFormatting;
import net.minecraft.network.chat.Component;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;
import net.minecraftforge.event.TickEvent;
import net.minecraftforge.event.entity.player.EntityItemPickupEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.ArrayList;

@Mod.EventBusSubscriber(modid = GamePhase.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE)
public class ServerEventHandler {
    public static final Component DENY_ITEMPICKUP_MESSAGE = Component.translatable("message.gamephase.denypickup").withStyle(ChatFormatting.RED);
    @SubscribeEvent
    public static void onEntityItemPickup(EntityItemPickupEvent event) {
        var player = event.getEntity();
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            var items = PhaseItems.items;
            for (var entry: items.entrySet()) {
                if (!phaseCapability.hasPhase(entry.getKey())) {
                    var removeItems = entry.getValue();
                    if (removeItems.stream().anyMatch(item -> ItemStack.isSameItem(item, event.getItem().getItem()))) {
                        event.setCanceled(true);
                        event.getItem().setPickUpDelay(60);
                        player.displayClientMessage(DENY_ITEMPICKUP_MESSAGE, true);
                        return;
                    }
                }
            }
        });
    }

    public static final Component DROPITEM_MESSAGE = Component.translatable("message.gamephase.dropitem").withStyle(ChatFormatting.RED);
    @SubscribeEvent
    public static void onPlayerTick(TickEvent.PlayerTickEvent event) {
        if (!(event.player instanceof ServerPlayer player))
            return;
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            var inventory = player.getInventory();
            var items = PhaseItems.items;
            for (var entry: items.entrySet()) {
                if (!phaseCapability.hasPhase(entry.getKey())) {
                    var removeItems = entry.getValue();
                    var dropItems = new ArrayList<ItemStack>();
                    for (var itemInventory : inventory.items)
                        if (removeItems.stream().anyMatch(item -> ItemStack.isSameItem(item, itemInventory)))
                            dropItems.add(itemInventory);
                    for (var itemInventory : inventory.armor)
                        if (removeItems.stream().anyMatch(item -> ItemStack.isSameItem(item, itemInventory)))
                            dropItems.add(itemInventory);
                    for (var itemInventory : inventory.offhand)
                        if (removeItems.stream().anyMatch(item -> ItemStack.isSameItem(item, itemInventory)))
                            dropItems.add(itemInventory);
                    for (var item : dropItems) {
                        player.drop(item, false);
                        inventory.removeItem(item);
                        player.displayClientMessage(DROPITEM_MESSAGE, true);
                    };
                }
            }
        });
    }
}
