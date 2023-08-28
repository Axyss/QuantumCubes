package me.axyss.quantumcubes.listeners;

import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.Main;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.gui.IGui;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.utils.MCHeadsDatabase;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class QuantumCubeListeners implements Listener {
    private final ProtocolManager manager;
    private final DestructiveReadMap eventSharedStorage = new DestructiveReadMap();

    public QuantumCubeListeners(ProtocolManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlacedQuantumCube(BlockPlaceEvent event) {
        if (event.isCancelled() || event.getBlockPlaced().getType() != Material.PLAYER_HEAD) {  // If it's not a QC
            return;
        }
        QuantumCube placedQuantumCube = QuantumCube.fromLocation(event.getBlockPlaced().getLocation());
        if (!placedQuantumCube.isUsed()) {
            IGui playerGui = new SignGui(manager, event.getPlayer());
            playerGui.open();
            eventSharedStorage.insert(event.getPlayer().getUniqueId(), List.of(placedQuantumCube, playerGui));
        }
    }

    @EventHandler
    public void onHeadIdSubmitted(HeadIdSubmittedEvent event) {
        Player interactingPlayer = event.getPlayer();
        String headId = String.valueOf(event.getHeadId());
        List<Object> quantumCubeGuiPair = eventSharedStorage.extractLastPlacedBy(interactingPlayer.getUniqueId());

        if (headId.isBlank()) {
            event.setCancelled(true);
            return;
        }
        QuantumCube quantumCube = (QuantumCube) quantumCubeGuiPair.get(0);
        Main.getInstance().getServer().getScheduler().scheduleSyncDelayedTask(Main.getInstance(), () -> {
            try {
                quantumCube.applyTexture(headId, MCHeadsDatabase.getMinecraftTexturesLink(Integer.parseInt(headId)));
                interactingPlayer.playSound(interactingPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
            } catch (IOException | URISyntaxException e) {
                throw new RuntimeException(e);
            }
        });
        ((IGui) quantumCubeGuiPair.get(1)).close();
    }
}

// todo rename variables
class DestructiveReadMap {
    private final HashMap<UUID, List<Object>> quantumCubeGuiPairs = new HashMap<>();

    public void insert(UUID playerWhoInteracted, List<Object> guiQuantumCubePair) {
        quantumCubeGuiPairs.put(playerWhoInteracted, guiQuantumCubePair);
    }

    public List<Object> extractLastPlacedBy(UUID playerWhoInteracted) {
        List<Object> quantumCubeGuiPair = quantumCubeGuiPairs.get(playerWhoInteracted);
        quantumCubeGuiPairs.remove(playerWhoInteracted);
        return quantumCubeGuiPair;
    }
}
