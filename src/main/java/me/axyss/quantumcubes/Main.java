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
import org.bukkit.plugin.java.JavaPlugin;

import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Paths;

public class Main extends JavaPlugin {
    private static JavaPlugin instance;
    private ProtocolManager protocolManager;
    private HeadDatabase headDB;

    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onLoad() {
        instance = this;
        protocolManager = ProtocolLibrary.getProtocolManager();
        headDB = new HeadDatabase(Paths.get(getDataFolder().getPath(), "heads.db"));
    }

    @Override
    public void onEnable() {
        this.saveDefaultConfig();
        QuantumCube.setDefaultItemValues(
                this.getConfig().getString("qc-default-name"),
                this.getConfig().getStringList("qc-default-lore"),
                this.getConfig().getString("qc-default-texture")
        );
        protocolManager.addPacketListener(SignGui.getPacketAdapter());
        getServer().getPluginManager().registerEvents(new QuantumCubeListeners(protocolManager, headDB), this);
        this.getCommand("quantumcubes").setExecutor(new GiveCommand());
        this.getCommand("quantumcubes").setTabCompleter(new GiveTabCompleter());
        Metrics metrics = new Metrics(this, 19747);
        System.out.println(getDataFolder());

        new Thread(() -> {
            try {
                headDB.refreshHeadData();
            } catch (URISyntaxException e) {
                throw new RuntimeException(e);
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }).start();
    }

    @Override
    public void onDisable() {
        // todo close db connection
    }
}