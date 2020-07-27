package net.teamfruit.relativeplugin;

import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.HumanEntity;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class CommandListener implements CommandExecutor, TabCompleter {
    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length < 1)
            return false;

        if ("king".equals(args[0])) {
            RelativePlugin.INSTANCE.offsets.clear();
            if (args.length >= 2) {
                RelativePlugin.INSTANCE.king = args[1];
                sender.sendMessage("中心プレイヤーが " + RelativePlugin.INSTANCE.king + " にセットされました。");
            } else {
                RelativePlugin.INSTANCE.king = null;
                sender.sendMessage("中心プレイヤーがリセットされました。");
            }
            return true;
        }

        if ("on".equals(args[0])) {
            if (!RelativePlugin.INSTANCE.enabled) {
                if (RelativePlugin.INSTANCE.king == null) {
                    RelativePlugin.INSTANCE.king = sender.getName();
                    sender.sendMessage("中心プレイヤーが " + RelativePlugin.INSTANCE.king + " にセットされました。");
                }
                RelativePlugin.INSTANCE.enabled = true;
                RelativePlugin.INSTANCE.offsets.clear();
                Bukkit.broadcastMessage("相対座標が有効化されました。");
            } else {
                Bukkit.broadcastMessage("相対座標が有効化されています。");
            }
            return true;
        }

        if ("off".equals(args[0])) {
            if (RelativePlugin.INSTANCE.enabled) {
                RelativePlugin.INSTANCE.offsets.clear();
                RelativePlugin.INSTANCE.enabled = false;
                Bukkit.broadcastMessage("相対座標が無効化されました。");
            } else {
                Bukkit.broadcastMessage("相対座標が無効化されています。");
            }
            return true;
        }

        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        final List<String> completions = new ArrayList<>();
        if (args.length == 1) {
            completions.add("king");
            completions.add("on");
            completions.add("off");
        }
        if (args.length == 2 && "king".equals(args[0])) {
            completions.addAll(Bukkit.getOnlinePlayers().stream().map(HumanEntity::getName).collect(Collectors.toList()));
        }
        return completions;
    }
}
