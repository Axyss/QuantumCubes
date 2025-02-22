package me.axyss.quantumcubes;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.commands.give.GiveCommand;
import me.axyss.quantumcubes.commands.give.GiveTabCompleter;
import me.axyss.quantumcubes.data.HeadDatabase;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.listeners.QuantumCubeListeners;
import me.axyss.quantumcubes.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Paths;

public class Main extends JavaPlugin {
    private ProtocolManager protocolManager;
    private HeadDatabase headDB;
    private Metrics metrics;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        QuantumCube.setPlugin(this);
        QuantumCube.setDefaultItemValues(
                getConfig().getString("qc-default-name"),
                getConfig().getStringList("qc-default-lore"),
                getConfig().getString("qc-default-texture")
        );
        protocolManager = ProtocolLibrary.getProtocolManager();
        headDB = new HeadDatabase(Paths.get(getDataFolder().getPath(), "heads.db"));
    }

    @Override
    public void onEnable() {
        // Commands
        getCommand("quantumcubes").setExecutor(new GiveCommand());
        getCommand("quantumcubes").setTabCompleter(new GiveTabCompleter());

        // Listeners
        protocolManager.addPacketListener(SignGui.createPacketAdapter(this));
        getServer().getPluginManager().registerEvents(new QuantumCubeListeners(this, protocolManager, headDB), this);

        // Scheduled task for database refreshing
        Bukkit.getScheduler().runTaskTimerAsynchronously(
                this, () -> headDB.refreshHeadData(),
                Math.max(0L, 604800L - headDB.getSecondsSinceLastRefresh()) * 20L, 604800L * 20L);

        // Telemetry
        metrics = new Metrics(this, 19747);
    }

    @Override
    public void onDisable() {
        // todo close db connection
    }
}