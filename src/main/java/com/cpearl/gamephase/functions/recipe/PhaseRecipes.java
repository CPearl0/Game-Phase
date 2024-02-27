package com.cpearl.gamephase.functions.recipe;

import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.crafting.Recipe;

import java.util.*;

public class PhaseRecipes {
    public static Map<String, Collection<ResourceLocation>> recipeIDs = new HashMap<>();
    public static Map<String, Collection<String>> recipeMods = new HashMap<>();
    public static Map<String, Collection<Recipe<?>>> recipes;
    public static void addRecipeRestriction(String phase, ResourceLocation ...recipe) {
        if (recipeIDs.containsKey(phase))
            Collections.addAll(recipeIDs.get(phase), recipe);
        else
            recipeIDs.put(phase, new HashSet<>(List.of(recipe)));
    }

    public static void addRecipeModRestriction(String phase, String ...mod) {
        if (recipeMods.containsKey(phase))
            Collections.addAll(recipeMods.get(phase), mod);
        else
            recipeMods.put(phase, new HashSet<>(List.of(mod)));
    }

    public static void removeRecipeRestriction(String phase, ResourceLocation ...recipe) {
        if (recipeIDs.containsKey(phase)) {
            for (var removeRecipe : recipe) {
                recipeIDs.get(phase).remove(removeRecipe);
            }
        }
    }

    public static void removeItemModRestriction(String phase, String ...mod) {
        if (recipeMods.containsKey(phase)) {
            for (var removeMod : mod) {
                recipeMods.get(phase).remove(removeMod);
            }
        }
    }
}
