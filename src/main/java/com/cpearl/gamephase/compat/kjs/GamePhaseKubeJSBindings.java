package com.cpearl.gamephase.compat.kjs;

import com.cpearl.gamephase.GamePhaseHelper;
import com.cpearl.gamephase.functions.dimension.PhaseDimensions;
import com.cpearl.gamephase.functions.item.PhaseItems;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.server.level.ServerPlayer;
import net.minecraft.world.item.ItemStack;

public class GamePhaseKubeJSBindings {
    public void addPhase(ServerPlayer player, String phase) {
        GamePhaseHelper.addPhase(player, phase);
    }
    public void removePhase(ServerPlayer player, String phase) {
        GamePhaseHelper.removePhase(player, phase);
    }
    public void clearPhase(ServerPlayer player) {
        GamePhaseHelper.clearPhase(player);
    }

    public void addItemRestriction(String phase, ItemStack ...item) {
        PhaseItems.addItemRestriction(phase, item);
    }

    public void addItemModRestriction(String phase, String ...mod) {
        PhaseItems.addItemModRestriction(phase, mod);
    }

    public void removeItemRestriction(String phase, ItemStack ...item) {
        PhaseItems.removeItemRestriction(phase, item);
    }

    public void removeItemModRestriction(String phase, String ...mod) {
        PhaseItems.removeItemModRestriction(phase, mod);
    }

    public void addDimensionRestriction(String phase, ResourceLocation ...dimension) {
        PhaseDimensions.addDimensionRestriction(phase, dimension);
    }

    public void removeDimensionRestriction(String phase, ResourceLocation ...dimension) {
        PhaseDimensions.removeDimensionRestriction(phase, dimension);
    }
}
