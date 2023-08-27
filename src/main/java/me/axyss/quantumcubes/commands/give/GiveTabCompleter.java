package me.axyss.quantumcubes.commands.give;

import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveTabCompleter implements TabCompleter {
    private static final List<String> autocompleteOptions = List.of("give");

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (!(sender instanceof Player)) {
            return null;
        }

        return switch (args.length) {
            case 1 -> autocompleteOptions;
            case 2 -> null; // Autocompletes with usernames
            default -> List.of(); // Doesn't autocomplete
        };
    }
}
