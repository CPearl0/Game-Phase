package com.cpearl.gamephase.functions.item;

import com.cpearl.gamephase.GamePhase;
import com.cpearl.gamephase.compat.jei.GamePhaseJEIPlugin;
import com.cpearl.gamephase.events.GamePhaseInitializeEvent;
import com.cpearl.gamephase.events.GamePhaseSyncEvent;
import mezz.jei.api.constants.VanillaTypes;
import net.minecraft.ChatFormatting;
import net.minecraft.client.Minecraft;
import net.minecraft.network.chat.Component;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.event.entity.player.ItemTooltipEvent;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.fml.common.Mod;

import java.util.Collection;

@Mod.EventBusSubscriber(modid = GamePhase.MODID, bus = Mod.EventBusSubscriber.Bus.FORGE, value = Dist.CLIENT)
public class ClientEventHandler {
    private static Collection<String> oldPhases;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGamePhaseInitialize(GamePhaseInitializeEvent event) {
        oldPhases = event.getPhases();
        var items = PhaseItems.items;
        for (var entry: items.entrySet()) {
            if (!oldPhases.contains(entry.getKey())) {
                var removeItems = entry.getValue();
                GamePhaseJEIPlugin.removeItems(removeItems);
            }
        }
    }

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGamePhaseSync(GamePhaseSyncEvent event) {
        var phases = event.getPhases();
        var items = PhaseItems.items;
        oldPhases.forEach(phaseOld -> {
            if (phases.contains(phaseOld) || !items.containsKey(phaseOld))
                return;
            GamePhaseJEIPlugin.removeItems(items.get(phaseOld));
        });
        phases.forEach(phaseNew -> {
            if (oldPhases.contains(phaseNew) || !items.containsKey(phaseNew))
                return;
            GamePhaseJEIPlugin.addItems(items.get(phaseNew));
        });
        oldPhases = phases;
    }

    public static final Component HIDDEN_ITEM_TOOLTIP = Component.translatable("tooltip.gamephase.hiddenname").withStyle(ChatFormatting.RED);
    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onItemTooltip(ItemTooltipEvent event) {
        if (oldPhases == null)
            return;
        var items = PhaseItems.items;
        for (var entry: items.entrySet()) {
            if (!oldPhases.contains(entry.getKey())) {
                var removeItems = entry.getValue();
                if (removeItems.stream().anyMatch(item -> item.getItem().equals(event.getItemStack().getItem()))) {
                    event.getToolTip().clear();
                    event.getToolTip().add(HIDDEN_ITEM_TOOLTIP);
                }
            }
        }
    }
}
