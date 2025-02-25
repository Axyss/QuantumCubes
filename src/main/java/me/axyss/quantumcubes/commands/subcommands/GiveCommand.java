package me.axyss.quantumcubes.commands.subcommands;

import me.axyss.quantumcubes.commands.SubCommand;
import me.axyss.quantumcubes.data.QuantumCube;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveCommand implements SubCommand {

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player giveToPlayer;
        int defaultAmount = 1;

        // Manages [amount]
        if (args.length == 3) {
            try {
                defaultAmount = Integer.parseInt(args[2]);
            } catch (NumberFormatException err) {
            }
        }

        // Manages /gc give <player>
        if ((giveToPlayer = Bukkit.getPlayer(args[1])) != null) {
            giveToPlayer.getInventory().addItem(QuantumCube.getItem(defaultAmount));
            giveToPlayer.playSound(giveToPlayer, Sound.ENTITY_ITEM_PICKUP, 0.6f, 1.0f);
        }
    }

    @Override
    public List<String> tabComplete(Player player, String[] args) {
        if (args.length == 2) {
            return null;
        } else {
            return List.of();
        }
    }
}
