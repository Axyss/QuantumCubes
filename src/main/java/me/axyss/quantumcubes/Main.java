package me.axyss.quantumcubes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import me.axyss.quantumcubes.data.QuantumCubeArchive;
import me.axyss.quantumcubes.listeners.QuantumCubeListener;
import me.axyss.quantumcubes.listeners.SignGuiPacketListener;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static JavaPlugin instance;
    private ProtocolManager protocolManager;

    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        protocolManager = ProtocolLibrary.getProtocolManager();
        instance = this;
    }

    @Override
    public void onEnable() {
        QuantumCubeArchive archive = new QuantumCubeArchive();
        protocolManager.addPacketListener(new SignGuiPacketListener(this, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN));
        getServer().getPluginManager().registerEvents(new QuantumCubeListener(protocolManager), this);
    }
}