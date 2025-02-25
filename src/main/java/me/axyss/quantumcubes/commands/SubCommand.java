package me.axyss.quantumcubes.commands;

import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public interface SubCommand {
    void execute(CommandSender sender, String[] args);

    default List<String> tabComplete(Player player, String[] args) {
        return null;
    }
}
