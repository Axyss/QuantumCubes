package me.axyss.quantumcubes.commands;

import org.bukkit.command.CommandSender;

public interface SubCommand {
    void execute(CommandSender sender, String[] args);
}
