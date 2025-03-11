package me.axyss.quantumcubes.commands.subcommands;

import me.axyss.quantumcubes.commands.SubCommand;
import me.axyss.quantumcubes.utils.Language;
import org.bukkit.ChatColor;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(Language.getMessage("invalid-syntax"));
            return;
        }
        sender.sendMessage(ChatColor.translateAlternateColorCodes('&', """
            &b==== Quantum Cubes Help ====
            &9/qc help &7- Shows this help message.
            &9/qc give <player> [amount] &7- Gives a certain amount of cubes to a player.
            &9/qc refresh &7- Forces a manual refresh of the internal head database.
            &9/qc reload &7- Reloads the plugin's configuration.""")
        );
    }
}
