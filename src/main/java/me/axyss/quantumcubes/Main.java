package me.axyss.quantumcubes;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.commands.QuantumCubesCommand;
import me.axyss.quantumcubes.data.HeadDatabase;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.listeners.QuantumCubeListeners;
import me.axyss.quantumcubes.utils.Language;
import me.axyss.quantumcubes.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.nio.file.Paths;

public class Main extends JavaPlugin {
    private ProtocolManager protocolManager;
    private HeadDatabase headDB;
    private long dbRefreshInterval;

    @Override
    public void onLoad() {
        saveDefaultConfig();
        Language.loadLangFile(this);
        QuantumCube.setPlugin(this);
        QuantumCube.setDefaultItemValues(
                getConfig().getString("qc-default-name"),
                getConfig().getStringList("qc-default-lore"),
                getConfig().getString("qc-default-texture")
        );
        protocolManager = ProtocolLibrary.getProtocolManager();
        headDB = new HeadDatabase(Paths.get(getDataFolder().getPath(), "heads.db"));
        dbRefreshInterval = getConfig().getLong("db-refresh-interval");
    }

    @Override
    public void onEnable() {
        // Commands
        QuantumCubesCommand rootCommand = new QuantumCubesCommand(this, headDB);
        getCommand("quantumcubes").setExecutor(rootCommand);
        getCommand("quantumcubes").setTabCompleter(rootCommand);

        // Listeners
        protocolManager.addPacketListener(SignGui.createPacketAdapter(this));
        getServer().getPluginManager().registerEvents(new QuantumCubeListeners(this, protocolManager, headDB), this);

        // Scheduled task for database refreshing
        Bukkit.getScheduler().runTaskTimerAsynchronously(
                this, () -> headDB.refreshHeadData(),
                Math.max(0L, dbRefreshInterval - headDB.getSecondsSinceLastRefresh()) * 20L, dbRefreshInterval * 20L);

        // Telemetry
        Metrics metrics = new Metrics(this, 19747);
    }

    @Override
    public void onDisable() {
        headDB.close();
    }
}