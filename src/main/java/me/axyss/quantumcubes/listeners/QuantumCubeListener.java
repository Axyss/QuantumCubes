package me.axyss.quantumcubes.listeners;

import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.data.QuantumCubeArchive;
import me.axyss.quantumcubes.gui.IGui;
import me.axyss.quantumcubes.gui.sign.SignGui;
import org.bukkit.Material;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;


public class QuantumCubeListener implements Listener {
    private final ProtocolManager manager;

    public QuantumCubeListener(ProtocolManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onQuantumCubePlaced(BlockPlaceEvent event) {
        if (!event.isCancelled() && event.getBlockPlaced().getType() == Material.PLAYER_HEAD || event.getBlockPlaced().getType() == Material.PLAYER_WALL_HEAD) { // Placeholder
            IGui gui = new SignGui(manager, event.getPlayer());
            gui.open();
            QuantumCubeArchive.insert(event.getPlayer().getUniqueId(), QuantumCube.fromLocation(event.getBlockPlaced().getLocation()));
        }
    }
}
