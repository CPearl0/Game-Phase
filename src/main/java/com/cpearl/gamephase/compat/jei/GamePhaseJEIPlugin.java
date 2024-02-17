package com.cpearl.gamephase.compat.jei;

import com.cpearl.gamephase.GamePhase;
import mezz.jei.api.IModPlugin;
import mezz.jei.api.JeiPlugin;
import mezz.jei.api.constants.VanillaTypes;
import mezz.jei.api.runtime.IJeiRuntime;
import net.minecraft.client.Minecraft;
import net.minecraft.resources.ResourceLocation;
import net.minecraft.world.item.ItemStack;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.Collection;

@JeiPlugin
public class GamePhaseJEIPlugin implements IModPlugin {
    public static IJeiRuntime runtime;

    private static class UpdateMessage {
        boolean isRemove;
        Collection<ItemStack> items;
        UpdateMessage(boolean isRemove, Collection<ItemStack> items) {
            this.isRemove = isRemove;
            this.items = items;
        }
    }
    private static final ArrayList<UpdateMessage> messages = new ArrayList<>();
    @Override
    public @NotNull ResourceLocation getPluginUid() {
        return new ResourceLocation(GamePhase.MODID, "main");
    }

    @Override
    public void onRuntimeAvailable(@NotNull IJeiRuntime jeiRuntime) {
        runtime = jeiRuntime;
        Minecraft.getInstance().execute(() -> {
            var ingredientManager = runtime.getIngredientManager();
            for (UpdateMessage message : messages) {
                if (message.isRemove)
                    ingredientManager.removeIngredientsAtRuntime(VanillaTypes.ITEM_STACK, message.items);
                else
                    ingredientManager.addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, message.items);
            }
            messages.clear();
        });
    }

    public static void removeItems(Collection<ItemStack> items) {
        if (runtime == null) {
            messages.add(new UpdateMessage(true, items));
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
            messages.add(new UpdateMessage(false, items));
        }
        else {
            Minecraft.getInstance().execute(() -> {
                runtime.getIngredientManager()
                        .addIngredientsAtRuntime(VanillaTypes.ITEM_STACK, items);
            });
        }
    }
}
