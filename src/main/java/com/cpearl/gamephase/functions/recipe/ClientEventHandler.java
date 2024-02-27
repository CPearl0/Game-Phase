package com.cpearl.gamephase.functions.recipe;

import com.cpearl.gamephase.compat.jei.GamePhaseJEIPlugin;
import com.cpearl.gamephase.events.GamePhaseInitializeEvent;
import com.cpearl.gamephase.events.GamePhaseSyncEvent;
import com.cpearl.gamephase.functions.item.PhaseItems;
import net.minecraft.client.Minecraft;
import net.minecraft.world.ContainerHelper;
import net.minecraft.world.item.crafting.Recipe;
import net.minecraftforge.api.distmarker.Dist;
import net.minecraftforge.api.distmarker.OnlyIn;
import net.minecraftforge.eventbus.api.SubscribeEvent;
import net.minecraftforge.items.wrapper.RecipeWrapper;

import java.util.*;

public class ClientEventHandler {
    private static Collection<String> oldPhases;

    @SubscribeEvent
    @OnlyIn(Dist.CLIENT)
    public static void onGamePhaseInitialize(GamePhaseInitializeEvent event) {
        // Initialize the phase-recipes map
        if (PhaseRecipes.recipes == null) {
            assert Minecraft.getInstance().level != null;
            var recipeManager = Minecraft.getInstance().level.getRecipeManager();
            PhaseRecipes.recipes = new HashMap<>();
            recipeManager.
            recipeManager.getRecipes().forEach(recipe -> {
                for (var entry : PhaseRecipes.recipeMods.entrySet()) {
                    if (entry.getValue().contains(recipe.getId().getNamespace())) {
                        if (PhaseRecipes.recipes.containsKey(entry.getKey()))
                            PhaseRecipes.recipes.get(entry.getKey()).add(recipe);
                        else
                            PhaseRecipes.recipes.put(entry.getKey(), new HashSet<>(List.of(recipe)));
                    }
                }
            });
            PhaseRecipes.recipeIDs.forEach((phase, recipes) -> {
                var toAdd = new ArrayList<Recipe<?>>();
                recipes.forEach(recipe -> {
                    recipeManager.byKey(recipe).ifPresent(toAdd::add);
                });

                if (PhaseRecipes.recipes.containsKey(phase))
                    PhaseRecipes.recipes.get(phase).addAll(toAdd);
                else
                    PhaseRecipes.recipes.put(phase, new HashSet<>(toAdd));
            });
        }

        oldPhases = event.getPhases();
        var recipes = PhaseRecipes.recipes;
        for (var entry: recipes.entrySet()) {
            if (!oldPhases.contains(entry.getKey())) {
                GamePhaseJEIPlugin.removeRecipes(entry.getValue());
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
}
