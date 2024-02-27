package com.cpearl.gamephase.compat.jei;

import com.cpearl.gamephase.GamePhase;
import com.cpearl.gamephase.functions.recipe.PhaseRecipes;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import net.minecraft.world.item.crafting.CraftingRecipe;
import net.minecraft.world.item.crafting.Recipe;
import org.jetbrains.annotations.NotNull;

import java.util.*;

@JeiPlugin
public class GamePhaseJEIPlugin implements IModPlugin {
    public static IJeiRuntime runtime;

    private static class ItemUpdateMessage {
        boolean isRemove;
        Collection<ItemStack> items;
        ItemUpdateMessage(boolean isRemove, Collection<ItemStack> items) {
            this.isRemove = isRemove;
            this.items = items;
        }
    }
    private static final ArrayList<ItemUpdateMessage> itemMessages = new ArrayList<>();

    private static class RecipeUpdateMessage {
        boolean isRemove;
        Collection<Recipe<?>> recipes;
        RecipeUpdateMessage(boolean isRemove, Collection<Recipe<?>> recipes) {
            this.isRemove = isRemove;
            this.recipes = recipes;
        }
    }
    private static final ArrayList<RecipeUpdateMessage> recipeMessages = new ArrayList<>();

    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(GamePhase.MODID, "main");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;

        Minecraft.getInstance().execute(() -> {
            var ingredientManager = runtime.getIngredientManager();
            for (ItemUpdateMessage message : itemMessages) {
                if (message.isRemove)
                    ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, message.items);
                else
                    ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, message.items);
            }
            itemMessages.clear();
        });
    }

    public static void removeItems(Collection<ItemStack> items) {
        if (runtime == null) {
            itemMessages.add(new ItemUpdateMessage(true, items));
        }
        else {
            Minecraft.getInstance().execute(() -> {
                runtime.getIngredientManager()
                        .removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, items);
            });
        }
    }

    public static void addItems(Collection<ItemStack> items) {
        if (runtime == null) {
            itemMessages.add(new ItemUpdateMessage(false, items));
        }
        else {
            Minecraft.getInstance().execute(() -> {
                runtime.getIngredientManager()
                        .addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, items);
            });
        }
    }

    public static void removeRecipes(Collection<Recipe<?>> recipes) {
        if (runtime == null) {
            recipeMessages.add(new RecipeUpdateMessage(true, recipes));
        }
        else {
            Minecraft.getInstance().execute(() -> {
                recipes.forEach(recipe -> {
                    runtime.getRecipeManager()
                            .hideRecipes(recipe.getType());
                });
            });
        }
    }

    public static void addItems(Collection<ItemStack> items) {
        if (runtime == null) {
            itemMessages.add(new ItemUpdateMessage(false, items));
        }
        else {
            Minecraft.getInstance().execute(() -> {
                runtime.getIngredientManager()
                        .addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, items);
            });
        }
    }
}
