package me.axyss.quantumcubes;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.commands.give.GiveCommand;
import me.axyss.quantumcubes.commands.give.GiveTabCompleter;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.listeners.QuantumCubeListeners;
import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    private static JavaPlugin instance;
    private ProtocolManager protocolManager;

    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        QuantumCube.setItemValues(
                this.getConfig().getString("qc-default-name"),
                this.getConfig().getStringList("qc-default-lore"),
                this.getConfig().getString("qc-default-texture")
        );
        protocolManager.addPacketListener(SignGui.getPacketAdapter());
        getServer().getPluginManager().registerEvents(new QuantumCubeListeners(protocolManager), this);
        this.getCommand("quantumcubes").setExecutor(new GiveCommand());
        this.getCommand("quantumcubes").setTabCompleter(new GiveTabCompleter());
    }
}