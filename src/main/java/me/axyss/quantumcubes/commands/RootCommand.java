package me.axyss.quantumcubes.commands;

import me.axyss.quantumcubes.commands.give.GiveCommand;
import me.axyss.quantumcubes.commands.help.HelpCommand;
import me.axyss.quantumcubes.commands.refresh.RefreshCommand;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;

import java.util.HashMap;
import java.util.Map;

public class RootCommand implements CommandExecutor {
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public RootCommand() {
        subCommands.put("help", new HelpCommand());
        subCommands.put("give", new GiveCommand());
        //subCommands.put("refresh", new RefreshCommand());
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 0 || !subCommands.containsKey(args[0].toLowerCase())) {
            subCommands.get("help").execute(sender, args);
        } else {
            subCommands.get(args[0].toLowerCase()).execute(sender, args);
        }
        return true;
    }
}

