package com.matejdro.bukkit.monsterhunt;

import java.util.regex.Pattern;

import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class Util {

    public static void Message(String message, CommandSender sender) {
        if (sender instanceof Player) {
            Message(message, (Player) sender);
        } else {
            sender.sendMessage(message);
        }
    }

    private static final Pattern newLinePattern = Pattern.compile("\\s?\\[NEWLINE\\]\\s?");
    public static void Message(String message, Player player) {
    	message = ChatColor.translateAlternateColorCodes('&', message);
        String[] lines = newLinePattern.split(message);
        for(String line : lines) {
            player.sendMessage(line);
        }
    }
    
    public static void Broadcast(String message) {
        for (Player i : MonsterHunt.instance.getServer().getOnlinePlayers()) {
            Message(message, i);
        }
    }

    public static void Debug(String message) {
        if (Settings.globals.getBoolean(Setting.Debug.getString(), false)) {
            Log.info("[Debug] " + message);
        }
    }

    public void StartFailed(MonsterHuntWorld world) {
        if (world.settings.getInt(Setting.SkipToIfFailsToStart) >= 0) {
            world.getWorld().setTime(world.settings.getInt(Setting.SkipToIfFailsToStart));
        }
    }

}