package me.axyss.quantumcubes.commands.subcommands;

import me.axyss.quantumcubes.commands.SubCommand;
import me.axyss.quantumcubes.data.HeadDatabase;
import me.axyss.quantumcubes.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RefreshCommand implements SubCommand {
    private final JavaPlugin plugin;
    private final HeadDatabase headDB;

    public RefreshCommand(JavaPlugin plugin, HeadDatabase headDB) {
        this.plugin = plugin;
        this.headDB = headDB;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        String prefixedRefreshStartMessage = Language.getPrefixedMessage("refresh-start");
        String prefixedRefreshEndMessage = Language.getPrefixedMessage("refresh-end");

        if (args.length > 1) {
            sender.sendMessage(Language.getMessage("invalid-syntax"));
            return;
        }
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            sender.sendMessage(prefixedRefreshStartMessage);
            headDB.refreshHeadData();
            sender.sendMessage(prefixedRefreshEndMessage);
        });
    }
}