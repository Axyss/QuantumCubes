package me.axyss.quantumcubes.utils;

import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Language {
    private static YamlConfiguration langFile;

    public static void loadLangFile(JavaPlugin plugin) {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
        Language.langFile = YamlConfiguration.loadConfiguration(langFile);
    }

    public static String getMessage(String message) {
        return langFile.getString(message);
    }
}
