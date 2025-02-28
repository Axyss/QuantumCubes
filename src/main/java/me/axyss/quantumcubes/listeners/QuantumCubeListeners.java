package me.axyss.quantumcubes.listeners;

import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.data.HeadDatabase;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.gui.IGui;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.utils.DestructiveReadMap;
import org.bukkit.Material;
import org.bukkit.Particle;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.player.PlayerQuitEvent;
import org.bukkit.plugin.java.JavaPlugin;

import java.net.URL;
import java.util.List;
import java.util.UUID;

public class QuantumCubeListeners implements Listener {
    private final JavaPlugin plugin;
    private final ProtocolManager manager;
    private final HeadDatabase headDB;
    private final DestructiveReadMap<UUID, List<Object>> eventSharedStorage = new DestructiveReadMap<>();
    private final List<Material> allowedQuantumCubeMaterials = List.of(Material.PLAYER_HEAD, Material.PLAYER_WALL_HEAD);

    public QuantumCubeListeners(JavaPlugin plugin, ProtocolManager manager, HeadDatabase headDB) {
        this.plugin = plugin;
        this.manager = manager;
        this.headDB = headDB;
    }

    @EventHandler
    public void onPlacedQuantumCube(BlockPlaceEvent event) {
        if (event.isCancelled() || !allowedQuantumCubeMaterials.contains(event.getBlockPlaced().getType())) {
            return;
        }
        QuantumCube placedQuantumCube = QuantumCube.fromLocation(event.getBlockPlaced().getLocation());
        if (!placedQuantumCube.isUsed() && event.getPlayer().hasPermission("quantumcubes.use")) {
            IGui playerGui = new SignGui(manager, plugin.getConfig(), event.getPlayer());
            playerGui.open();
            eventSharedStorage.insert(event.getPlayer().getUniqueId(), List.of(placedQuantumCube, playerGui));
        }
    }

    @EventHandler
    public void onInputSubmitted(InputSubmittedEvent event) {
        Player interactingPlayer = event.getPlayer();
        String headId = event.getInputText();
        List<Object> quantumCubeGuiPair = eventSharedStorage.extract(interactingPlayer.getUniqueId());
        URL headTexture;

        if (quantumCubeGuiPair == null) {
            event.setCancelled(true);
            return;
        }

        if ((headTexture = headDB.getHeadTextureURL(headId)) == null) {
            event.setCancelled(true);
        } else {
            QuantumCube quantumCube = (QuantumCube) quantumCubeGuiPair.get(0);
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                quantumCube.applyTexture(headId, headTexture);
                interactingPlayer.playSound(interactingPlayer, Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
                interactingPlayer.spawnParticle(
                        Particle.DRAGON_BREATH,
                        quantumCube.getLocation().add(0.5, 0.5, 0.5), 60, 0.0, 0.0, 0.0, 0.2
                );
            });
        }
        ((IGui) quantumCubeGuiPair.get(1)).close();
    }

    @EventHandler
    // Prevents memory leaks in case the player quits without submitting a Head ID
    private void onPlayerQuit(PlayerQuitEvent event) {
        eventSharedStorage.extract(event.getPlayer().getUniqueId());
    }
}
