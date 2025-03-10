package me.axyss.quantumcubes.commands.subcommands;

import me.axyss.quantumcubes.commands.SubCommand;
import me.axyss.quantumcubes.utils.Language;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class ReloadCommand implements SubCommand {
    private final JavaPlugin plugin;

    public ReloadCommand(JavaPlugin plugin) {
        this.plugin = plugin;
    }
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(Language.getMessage("invalid-syntax"));
            return;
        }
        plugin.reloadConfig();
        sender.sendMessage(Language.getPrefixedMessage("reload-success"));
    }
}
