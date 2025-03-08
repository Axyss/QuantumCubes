package me.axyss.quantumcubes.commands;

import me.axyss.quantumcubes.commands.subcommands.HelpCommand;
import me.axyss.quantumcubes.commands.subcommands.GiveCommand;
import me.axyss.quantumcubes.commands.subcommands.RefreshCommand;
import me.axyss.quantumcubes.data.HeadDatabase;
import me.axyss.quantumcubes.utils.Language;
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
    private final Map<String, SubCommand> allowedSubcommands = new HashMap<>();

    public QuantumCubesCommand(JavaPlugin plugin, HeadDatabase headDB) {
        allowedSubcommands.put("help", new HelpCommand());
        allowedSubcommands.put("give", new GiveCommand());
        allowedSubcommands.put("refresh", new RefreshCommand(plugin, headDB));
    }

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        String subcommand = args.length == 0 ? "help" : args[0];

        if (!allowedSubcommands.containsKey(subcommand)) {
            sender.sendMessage(Language.getMessage("invalid_syntax"));
            return false;
        }
        if (!sender.hasPermission("quantumcubes." + subcommand)) {
            sender.sendMessage(Language.getMessage("no_permission"));
            return false;
        }
        allowedSubcommands.get(subcommand).execute(sender, args);
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player player)) {  // Ignores console and command blocks
            return List.of();
        }
        if (args.length == 1) {  // Autocomplete subcommand names
            String typed = args[0].toLowerCase();
            return allowedSubcommands.keySet().stream()
                    .filter(cmd -> cmd.startsWith(typed))
                    .filter(cmd -> player.hasPermission("quantumcubes." + cmd))
                    .toList();
        }
        // Pass completion responsibility onto the corresponding subcommand
        String subcommandKey = args[0].toLowerCase();
        SubCommand subCommand = allowedSubcommands.get(subcommandKey);
        if (subCommand != null && player.hasPermission("quantumcubes." + subcommandKey)) {
            return subCommand.tabComplete(player, args);
        }
        return List.of();
    }
}

