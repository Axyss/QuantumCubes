package me.axyss.quantumcubes;

import me.axyss.quantumcubes.gui.sign.SignGui;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class QuantumCubeListener implements Listener {
    @EventHandler
    public void onQuantumCubePlaced(BlockPlaceEvent event) throws InterruptedException {
        if (event.getBlockPlaced().getType() == Material.PLAYER_HEAD) {

            SignGui gui = new SignGui();
            gui.readTextFrom(event.getPlayer());

            HeadLibrary.main(event.getPlayer());

            Skull skull = (Skull) event.getBlockPlaced().getState();
            skull.setOwningPlayer(getServer().getOfflinePlayer(UUID.fromString("d2731bbf-0b6c-4816-ba99-add394b829d2")));
            skull.update();
        }
    }
}
