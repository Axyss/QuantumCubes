package me.axyss.quantumcubes.commands.give;

import me.axyss.quantumcubes.data.QuantumCube;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

public class GiveCommand implements CommandExecutor {

    @Override
    public boolean onCommand(CommandSender sender, Command command, String label, String[] args) {
        Player giveToPlayer;
        int defaultAmount = 1;

        // Manages [amount]
        if (args.length == 3) {
            try {
                defaultAmount = Integer.parseInt(args[2]);
            } catch (NumberFormatException err) {
                return false;
            }
        }

        // Manages /gc give <player>
        if ((args.length == 2 || args.length == 3) && "give".equals(args[0]) && (giveToPlayer = Bukkit.getPlayer(args[1])) != null) {
            giveToPlayer.getInventory().addItem(QuantumCube.getItem(defaultAmount));
            giveToPlayer.playSound(giveToPlayer, Sound.ENTITY_ITEM_PICKUP, 0.6f, 1.0f);
            return true;
        }
        return false;
    }


}
