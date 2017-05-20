package com.matejdro.bukkit.monsterhunt.commands;

import java.util.LinkedHashMap;

import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import com.matejdro.bukkit.monsterhunt.InputOutput;
import com.matejdro.bukkit.monsterhunt.Util;

public class HuntScoreCommand implements CommandExecutor {

    public boolean onCommand(CommandSender sender, Command command, String label, String[] args){
        if (!(sender instanceof Player)) {
            sender.sendMessage("Sorry, but you need to execute this command as player.");
            return true;
        }
        if (args.length > 0 && args[0].equals("rank")) {
            Integer rank = InputOutput.getHighScoreRank(((Player) sender).getName());
            if (rank != null)
                Util.Message("&8[&7!&8]&7Your current high score rank is &3" + String.valueOf(rank), sender);
            else
                Util.Message("&8[&7!&8]&7You do not have your high score yet.", sender);
        } else if (args.length > 0 && args[0].equals("top")) {
            Integer number = 5;
            if (args.length > 1)
                number = Integer.parseInt(args[1]);

            LinkedHashMap<String, Integer> tops = InputOutput.getTopScores(number);
            Util.Message("&9# Top high scores: #", sender);
            int counter = 0;
            for (String player : tops.keySet()) {
                counter++;
                String rank = String.valueOf(counter);
                String score = String.valueOf(tops.get(player));

                Util.Message("&7" + rank + ". &6" + player + "&7 - &a" + score + "&7 points", sender);
            }
        } else if (args.length > 0) {
            Integer score = InputOutput.getHighScore(args[0]);
            if (score != null)
                Util.Message("&8[&7!&8]&7High score of player &6" + args[0] + "&7 is &6" + String.valueOf(score) + "&7 points.", sender);
            else
                Util.Message("&7Player &6" + args[0] + "&7 do not have high score yet.", sender);
        } else {
            Integer score = InputOutput.getHighScore(((Player) sender).getName());
            if (score != null)
                Util.Message("&8[&7!&8]&7Your high score is &3" + String.valueOf(score) + "&7 points.[NEWLINE]&7For more information, try: &3/huntscore [player|top|rank]", sender);
            else
                Util.Message("&8[&7!&8]&7You do not have your high score yet.[NEWLINE]&7For more information, try: &3/huntscore [player|top|rank]", sender);
        }

        return true;
    }

}
