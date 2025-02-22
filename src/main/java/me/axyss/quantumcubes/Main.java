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

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Main extends JavaPlugin {
    private static JavaPlugin instance;
    private ProtocolManager protocolManager;
    private HeadDatabase headDB;
    private Metrics metrics;

    private static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        QuantumCube.setPlugin(this);

        try {
            headDB = new HeadDatabase(Paths.get(getDataFolder().getPath(), "heads.db"));
        } catch (URISyntaxException e) {
            throw new RuntimeException(e);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        QuantumCube.setDefaultItemValues(
                this.getConfig().getString("qc-default-name"),
                this.getConfig().getStringList("qc-default-lore"),
                this.getConfig().getString("qc-default-texture")
        );
        protocolManager.addPacketListener(SignGui.getPacketAdapter(this));
        getServer().getPluginManager().registerEvents(new QuantumCubeListeners(protocolManager, headDB), this);
        this.getCommand("quantumcubes").setExecutor(new GiveCommand());
        this.getCommand("quantumcubes").setTabCompleter(new GiveTabCompleter());
        metrics = new Metrics(this, 19747);

        try {
            Bukkit.getScheduler().runTaskTimerAsynchronously(this, () -> {
                try {
                    headDB.refreshHeadData();
                } catch (URISyntaxException e) {
                    throw new RuntimeException(e);
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }, Math.max(0L, 604800L - headDB.getSecondsSinceLastRefresh()) * 20L, 604800L * 20L);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public void onDisable() {
        // todo close db connection
    }
}