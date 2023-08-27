package me.axyss.quantumcubes.listeners;

import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.Main;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.data.QuantumCubeArchive;
import me.axyss.quantumcubes.gui.IGui;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.utils.MCHeadsDatabase;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.block.SignChangeEvent;

import java.io.IOException;
import java.net.URISyntaxException;


public class QuantumCubeListener implements Listener {
    private final ProtocolManager manager;

    public QuantumCubeListener(ProtocolManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onQuantumCubePlaced(BlockPlaceEvent event) {
        if (event.isCancelled() || event.getBlockPlaced().getType() != Material.PLAYER_HEAD) {
            return;
        }
        if (!QuantumCube.fromLocation(event.getBlockPlaced().getLocation()).isUsed()) {
            IGui gui = new SignGui(manager, event.getPlayer());
            gui.open();
            QuantumCubeArchive.insert(event.getPlayer().getUniqueId(), QuantumCube.fromLocation(event.getBlockPlaced().getLocation()));
        }
    }

    @EventHandler
    public void onSignUpdate(SignChangeEvent event) {

        String playerInput = event.getLine(0);

        if (playerInput == null || playerInput.isBlank()) {
            event.setCancelled(true);
        } else {
            QuantumCube quantumCube = QuantumCubeArchive.extractLastPlacedBy(event.getPlayer().getUniqueId());
            Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
                try {
                    quantumCube.applyTexture(playerInput, MCHeadsDatabase.getMinecraftTexturesLink(Integer.parseInt(playerInput)));
                    event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });
        }
    }
}
