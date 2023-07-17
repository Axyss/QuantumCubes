package me.axyss.quantumcubes.listeners;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import org.bukkit.plugin.Plugin;

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
            ; // Here goes some sign logic
        }
    }
}
