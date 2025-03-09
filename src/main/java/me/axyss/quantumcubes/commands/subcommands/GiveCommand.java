package me.axyss.quantumcubes.commands.subcommands;

import me.axyss.quantumcubes.commands.SubCommand;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.utils.Language;
import org.bukkit.Bukkit;
import org.bukkit.Sound;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

public class GiveCommand implements SubCommand {
    private static final int defaultAmount = 1;

    @Override
    public void execute(CommandSender sender, String[] args) {
        Player giveToPlayer;
        int amount = defaultAmount;

        if (args.length > 3 || args.length < 2 || (giveToPlayer = Bukkit.getPlayer(args[1])) == null) {
            sender.sendMessage(Language.getMessage("invalid-syntax"));
            return;
        }
        if (args.length == 3) {
            try {
                amount = Integer.parseInt(args[2]);
            } catch (NumberFormatException ignored) {
                sender.sendMessage(Language.getMessage("invalid-syntax"));
                return;
            }
        }
        giveToPlayer.getInventory().addItem(QuantumCube.getItem(amount));
        giveToPlayer.playSound(giveToPlayer, Sound.ENTITY_ITEM_PICKUP, 0.6f, 1.0f);
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
