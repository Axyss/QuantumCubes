package me.axyss.quantumcubes.utils;

import org.bukkit.ChatColor;
import org.bukkit.configuration.file.YamlConfiguration;
import org.bukkit.plugin.java.JavaPlugin;

import java.io.File;

public class Language {
    private static YamlConfiguration langFile;

    public static void saveDefaultLang(JavaPlugin plugin ) {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        if (!langFile.exists()) {
            plugin.saveResource("lang.yml", false);
        }
    }

    public static void loadFile(JavaPlugin plugin) {
        File langFile = new File(plugin.getDataFolder(), "lang.yml");
        Language.langFile = YamlConfiguration.loadConfiguration(langFile);
    }

    public static String getMessage(String messageKey) {
        String message = langFile.getString("messages." + messageKey, "&cMessage not found!");
        return ChatColor.translateAlternateColorCodes('&', message);
    }

    public static String getPrefixedMessage(String messageKey) {
        return Language.getMessage("plugin-prefix") + " " + ChatColor.RESET + Language.getMessage(messageKey);
    }
}
