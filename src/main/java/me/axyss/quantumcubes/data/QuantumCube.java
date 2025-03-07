package me.axyss.quantumcubes.data;

import org.bukkit.ChatColor;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.NamespacedKey;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.persistence.PersistentDataType;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

public class QuantumCube {
    private final Skull head;
    private static String itemName;
    private static List<String> itemLore;
    private static String itemTextureLink;
    private static JavaPlugin plugin;

    private QuantumCube(Location skullLocation) {
        head = (Skull) skullLocation.getBlock().getState();
    }

    public static void setPlugin(JavaPlugin plugin) {
        QuantumCube.plugin = plugin;
    }

    public static QuantumCube fromLocation(Location skullLocation) {
        return new QuantumCube(skullLocation);
    }

    public static ItemStack getItem(int amount) {
        ItemStack newQuantumCubeItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta newQuantumCubeItemMeta = newQuantumCubeItem.getItemMeta();
        PlayerProfile dummyPlayer = getDummyProfile("FakeProfile");

        newQuantumCubeItem.setAmount(amount);
        newQuantumCubeItemMeta.setDisplayName(itemName);
        newQuantumCubeItemMeta.setLore(itemLore);

        try {
            dummyPlayer.getTextures().setSkin(new URI(itemTextureLink).toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ((SkullMeta) newQuantumCubeItemMeta).setOwnerProfile(dummyPlayer);
        newQuantumCubeItem.setItemMeta(newQuantumCubeItemMeta);
        return newQuantumCubeItem;
    }

    public static void setDefaultItemValues(String quantumCubeItemName, List<String> quantumCubeItemLore, String quantumCubeItemTexture) {
        QuantumCube.itemName = ChatColor.translateAlternateColorCodes('&', quantumCubeItemName);
        QuantumCube.itemLore = quantumCubeItemLore
                .stream()
                .map(loreLine -> ChatColor.translateAlternateColorCodes('&', loreLine))
                .collect(Collectors.toList());
        QuantumCube.itemTextureLink = quantumCubeItemTexture;
    }

    private static PlayerProfile getDummyProfile(String username) {
        return plugin.getServer().createPlayerProfile(UUID.randomUUID(), username);
    }

    public void applyTexture(String textureName, URL textureLink) {
        PlayerProfile dummyPlayer = getDummyProfile(textureName);
        dummyPlayer.getTextures().setSkin(textureLink);
        head.setOwnerProfile(dummyPlayer);
        head.update();
    }

    public Location getLocation() {
        return head.getLocation();
    }

    public boolean isUsed() {
        return head.getOwnerProfile() == null || !"FakeProfile".equals(head.getOwnerProfile().getName());
    }
}
