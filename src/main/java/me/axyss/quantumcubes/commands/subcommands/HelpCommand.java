package me.axyss.quantumcubes.commands.subcommands;

import me.axyss.quantumcubes.commands.SubCommand;
import me.axyss.quantumcubes.utils.Language;
import org.bukkit.command.CommandSender;

public class HelpCommand implements SubCommand {
    @Override
    public void execute(CommandSender sender, String[] args) {
        if (args.length > 1) {
            sender.sendMessage(Language.getMessage("invalid-syntax"));
            return;
        }
        sender.sendMessage(Language.getMessage("help"));
    }
}
