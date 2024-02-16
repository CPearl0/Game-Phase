package com.cpearl.gamephase.commands;

import com.cpearl.gamephase.GamePhaseHelper;
import com.cpearl.gamephase.capability.GamePhaseCapabilityProvider;
import com.mojang.brigadier.arguments.StringArgumentType;
import com.mojang.brigadier.context.CommandContext;
import com.mojang.brigadier.exceptions.CommandSyntaxException;
import net.minecraft.commands.CommandSourceStack;
import net.minecraft.commands.Commands;
import net.minecraft.commands.arguments.EntityArgument;
import net.minecraft.network.chat.Component;
import net.minecraftforge.event.RegisterCommandsEvent;

public class GamePhaseCommand {
    public static void registerCommands(RegisterCommandsEvent event) {
        var root = Commands.literal("gamephase");
        root.then(Commands.literal("info").executes(context -> {
            showSelfInfo(context);
            return 0;
        }).then(Commands.argument("targets", EntityArgument.players()).executes(context -> {
            showInfo(context);
            return 0;
        })));
        root.then(Commands.literal("add").requires(p -> p.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("phase", StringArgumentType.string()).executes(context -> {
                            addPhase(context);
                            return 0;
                        }))));
        root.then(Commands.literal("remove").requires(p -> p.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players())
                        .then(Commands.argument("phase", StringArgumentType.string()).executes(context -> {
                            removePhase(context);
                            return 0;
                        }))));
        root.then(Commands.literal("clear").requires(p -> p.hasPermission(Commands.LEVEL_GAMEMASTERS))
                .then(Commands.argument("targets", EntityArgument.players()).executes(context -> {
                            clearPhase(context);
                            return 0;
                        })));
        event.getDispatcher().register(root);
    }

    private static void showInfo(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            players.forEach(player -> {
                StringBuilder phases = new StringBuilder();
                player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
                    phaseCapability.getPhases().forEach(phase -> {
                        phases.append(" ").append(phase);
                    });
                });
                context.getSource().sendSuccess(() -> Component.translatable("commands.gamephase.info", player.getDisplayName(), phases.toString()), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void showSelfInfo(CommandContext<CommandSourceStack> context) {
        var player = context.getSource().getPlayer();
        if (player == null)
            return;
        StringBuilder phases = new StringBuilder();
        player.getCapability(GamePhaseCapabilityProvider.GAME_PHASE).ifPresent(phaseCapability -> {
            phaseCapability.getPhases().forEach(phase -> {
                phases.append(" ").append(phase);
            });
        });
        context.getSource().sendSuccess(() -> Component.translatable("commands.gamephase.info", player.getDisplayName(), phases.toString()), true);
    }

    private static void addPhase(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            var phase = StringArgumentType.getString(context, "phase");
            players.forEach(player -> {
                GamePhaseHelper.addPhase(player, phase);
                context.getSource().sendSuccess(() -> Component.translatable("commands.gamephase.add", player.getDisplayName(), phase), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void removePhase(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            var phase = StringArgumentType.getString(context, "phase");
            players.forEach(player -> {
                GamePhaseHelper.removePhase(player, phase);
                context.getSource().sendSuccess(() -> Component.translatable("commands.gamephase.remove", player.getDisplayName(), phase), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }

    private static void clearPhase(CommandContext<CommandSourceStack> context) {
        try {
            var players = EntityArgument.getPlayers(context, "targets");
            players.forEach(player -> {
                GamePhaseHelper.clearPhase(player);
                context.getSource().sendSuccess(() -> Component.translatable("commands.gamephase.clear", player.getDisplayName()), true);
            });
        } catch (CommandSyntaxException e) {
            throw new RuntimeException(e);
        }
    }
}
