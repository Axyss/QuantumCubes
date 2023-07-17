package me.axyss.quantumcubes.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.data.QuantumCubeArchive;
import me.axyss.quantumcubes.utils.MCHeadsDatabase;
import org.bukkit.plugin.Plugin;

import java.io.IOException;
import java.net.URISyntaxException;

public class SignGuiPacketListener extends PacketAdapter {
    public SignGuiPacketListener(Plugin plugin, ListenerPriority priority, PacketType packet) {
        super(plugin, priority, packet);
    }

    @Override
    public void onPacketReceiving(PacketEvent event) {
        PacketContainer packet = event.getPacket();
        String signPlayerInput = packet.getStringArrays().read(0)[0];

        if (signPlayerInput == null || signPlayerInput.isBlank()) {
            event.setCancelled(true);
        } else {
            QuantumCube quantumCube = QuantumCubeArchive.extractLastPlacedBy(event.getPlayer().getUniqueId());
            plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                try {
                    quantumCube.applyTexture(MCHeadsDatabase.getMinecraftTexturesLink(Integer.parseInt(signPlayerInput)));
                } catch (IOException | URISyntaxException e) {
                    throw new RuntimeException(e);
                }
            });

        }
    }
}
