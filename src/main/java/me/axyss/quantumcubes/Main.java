package me.axyss.quantumcubes;

import org.bukkit.plugin.java.JavaPlugin;

public class Main extends JavaPlugin {
    public static JavaPlugin instance;

    @Override
    public void onLoad() {
        instance = this;
    }

    public static JavaPlugin getInstance() {
        return instance;
    }

    @Override
    public void onEnable() {
        getServer().getPluginManager().registerEvents(new QuantumCubeListener(), this);

    }
}