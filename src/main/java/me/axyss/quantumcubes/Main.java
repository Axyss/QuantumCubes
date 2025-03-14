package me.axyss.quantumcubes;

import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.tchristofferson.configupdater.ConfigUpdater;
import me.axyss.quantumcubes.commands.QuantumCubesCommand;
import me.axyss.quantumcubes.data.HeadDatabase;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.listeners.QuantumCubeListeners;
import me.axyss.quantumcubes.utils.Language;
import me.axyss.quantumcubes.utils.Metrics;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.List;

public class Main extends JavaPlugin {
    private ProtocolManager protocolManager;
    private HeadDatabase headDB;
    private long dbRefreshInterval;

    @Override
    public void onLoad() {
        // Resource creation
        saveDefaultConfig();
        Language.saveDefaultLang(this);
        // Resource update
        updateYAMLResources("config.yml", "lang.yml");
        // Resource load
        reloadConfig();
        Language.loadFile(this);

        QuantumCube.setPlugin(this);
        setDefaultItemValues(this);
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
                this, task -> {
                    getServer().getConsoleSender().sendMessage(Language.getPrefixedMessage("refresh-start"));
                    headDB.refreshHeadData();
                    getServer().getConsoleSender().sendMessage(Language.getPrefixedMessage("refresh-end"));
                },
                Math.max(0L, dbRefreshInterval - headDB.getSecondsSinceLastRefresh()) * 20L, dbRefreshInterval * 20L);

        // Telemetry
        Metrics metrics = new Metrics(this, 19747);
    }

    @Override
    public void onDisable() {
        // todo Handle potential problems with async tasks
        headDB.close();
    }

    public static void setDefaultItemValues(JavaPlugin plugin) {
        QuantumCube.setDefaultItemValues(
                plugin.getConfig().getString("qc-default-name"),
                plugin.getConfig().getStringList("qc-default-lore"),
                plugin.getConfig().getString("qc-default-texture")
        );
    }

    private void updateYAMLResources(String... resourceNames) {
        for (String resourceName: resourceNames) {
            File resourceFile = new File(getDataFolder(), resourceName);
            try {
                ConfigUpdater.update(this, resourceName, resourceFile, List.of());
            } catch (IOException e) {
                getLogger().severe("Failed to update YAML resource: " + resourceName);
                e.printStackTrace();
                getServer().getPluginManager().disablePlugin(this);
                return;
            }
        }
    }
}