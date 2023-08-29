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
import org.bukkit.event.player.PlayerQuitEvent;

import java.io.IOException;
import java.net.URISyntaxException;
import java.util.HashMap;
import java.util.List;
import java.util.UUID;


public class QuantumCubeListeners implements Listener {
    private final ProtocolManager manager;
    private final DestructiveReadMap<UUID, List<Object>> eventSharedStorage = new DestructiveReadMap<>();
    private final List<Material> allowedQuantumCubeMaterials = List.of(Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD);

    public QuantumCubeListeners(ProtocolManager manager) {
        this.manager = manager;
    }

    @EventHandler
    public void onPlacedQuantumCube(BlockPlaceEvent event) {
        if (event.isCancelled() || !allowedQuantumCubeMaterials.contains(event.getBlockPlaced().getType())) {
            return;
        }
        QuantumCube placedQuantumCube = QuantumCube.fromLocation(event.getBlockPlaced().getLocation());
        if (!placedQuantumCube.isUsed() && event.getPlayer().hasPermission("quantumcubes.use")) {
            IGui playerGui = new SignGui(manager, event.getPlayer());
            playerGui.open();
            eventSharedStorage.insert(event.getPlayer().getUniqueId(), List.of(placedQuantumCube, playerGui));
        }
    }

    @EventHandler
    public void onInputSubmitted(InputSubmittedEvent event) {
        Player interactingPlayer = event.getPlayer();
        String headId = event.getInputText();
        List<Object> quantumCubeGuiPair = eventSharedStorage.extract(interactingPlayer.getUniqueId());

        if (quantumCubeGuiPair == null) {
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
            } catch (NumberFormatException ignored) {}
        });
        ((IGui) quantumCubeGuiPair.get(1)).close();
    }

    @EventHandler
    // Prevents memory leaks in case the player quits without submitting a Head ID
    private void onPlayerQuit(PlayerQuitEvent event) {
        eventSharedStorage.extract(event.getPlayer().getUniqueId());
    }
}

class DestructiveReadMap<K, V> {
    private final HashMap<K, V> map = new HashMap<>();

    public void insert(K key, V value) {
        map.put(key, value);
    }

    public V extract(K key) {
        V value = map.get(key);
        map.remove(key);
        return value;
    }
}
