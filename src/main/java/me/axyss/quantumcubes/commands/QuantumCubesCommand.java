package me.axyss.quantumcubes.commands;

import me.axyss.quantumcubes.commands.subcommands.HelpCommand;
import me.axyss.quantumcubes.commands.subcommands.GiveCommand;
import me.axyss.quantumcubes.commands.subcommands.RefreshCommand;
import me.axyss.quantumcubes.data.HeadDatabase;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class QuantumCubesCommand implements CommandExecutor, TabCompleter {
    private final Map<String, SubCommand> subCommands = new HashMap<>();

    public QuantumCubesCommand(JavaPlugin plugin, HeadDatabase headDB) {
        subCommands.put("help", new HelpCommand());
        subCommands.put("give", new GiveCommand());
        subCommands.put("refresh", new RefreshCommand(plugin, headDB));
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

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {
            return List.of();
        }
        if (args.length == 1) {
            String typed = args[0].toLowerCase();
            return subCommands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(typed))
                    .filter(cmd -> player.hasPermission("quantumcubes." + cmd))
                    .toList();
        }

        String subCommandKey = args[0].toLowerCase();
        SubCommand subCommand = subCommands.get(subCommandKey);
        if (subCommand != null && player.hasPermission("quantumcubes." + subCommandKey)) {
            return subCommand.tabComplete(player, args);
        }
        return List.of();
    }
}

