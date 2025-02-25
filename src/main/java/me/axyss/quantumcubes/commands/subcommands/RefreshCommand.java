package me.axyss.quantumcubes.commands.subcommands;

import me.axyss.quantumcubes.commands.SubCommand;
import me.axyss.quantumcubes.data.HeadDatabase;
import org.bukkit.Bukkit;
import org.bukkit.command.CommandSender;
import org.bukkit.plugin.java.JavaPlugin;

public class RefreshCommand implements SubCommand {
    private JavaPlugin plugin;
    private HeadDatabase headDB;

    public RefreshCommand(JavaPlugin plugin, HeadDatabase headDB) {
        this.plugin = plugin;
        this.headDB = headDB;
    }

    @Override
    public void execute(CommandSender sender, String[] args) {
        Bukkit.getScheduler().runTaskAsynchronously(plugin, () -> {
            sender.sendMessage("§aRefreshing head database...");
            headDB.refreshHeadData();
            sender.sendMessage("§aHead database refreshed!");
        });
    }
}