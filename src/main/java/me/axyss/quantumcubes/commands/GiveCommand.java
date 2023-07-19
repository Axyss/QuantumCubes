package me.axyss.quantumcubes.commands;

import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;

import java.util.List;

public class GiveCommand implements CommandExecutor, TabCompleter {
    private static final List<String> autocompleteOptions = List.of("give");

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player giveToPlayer;
        int giveAmount = 1;

        if ((args.length == 2 || args.length == 3) && "give".equals(args[0]) && (giveToPlayer = Bukkit.getPlayer(args[1])) != null) {

            giveToPlayer.getInventory().addItem(new ItemStack(Material.PLAYER_HEAD, Integer.parseInt(args[2])));
            giveToPlayer.playSound(giveToPlayer, Sound.ENTITY_ITEM_PICKUP, 0.5f, 1.0f);
            return true;
        }
        return false;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command command, String label, String[] args) {
        if (args.length == 1 && sender instanceof Player) {
            return autocompleteOptions;
        }
        return null;
    }
}
