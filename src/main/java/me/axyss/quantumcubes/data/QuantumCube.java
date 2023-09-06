package me.axyss.quantumcubes.data;

import me.axyss.quantumcubes.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;
import org.bukkit.inventory.meta.SkullMeta;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.Arrays;
import java.util.UUID;

public class QuantumCube {
    private final Skull quantumCube;
    private static String quantumCubeItemName;
    private static String quantumCubeItemLore;
    private static String quantumCubeItemTexture;

    private QuantumCube(Location skullLocation) {
        quantumCube = (Skull) skullLocation.getBlock().getState();
    }

    public static QuantumCube fromLocation(Location skullLocation) {
        return new QuantumCube(skullLocation);
    }

    public void applyTexture(String textureName, URL textureLink) {
        PlayerProfile dummyPlayer = getDummyProfile(textureName);
        dummyPlayer.getTextures().setSkin(textureLink);
        quantumCube.setOwnerProfile(dummyPlayer);
        quantumCube.update();
    }

    public Location getLocation() {
        return quantumCube.getLocation();
    }

    public boolean isUsed() {
        return !"Unused Quantum Cube".equals(quantumCube.getOwnerProfile().getName());
    }

    public static void setItemValues(String quantumCubeItemName, String quantumCubeItemLore, String quantumCubeItemTexture) {
        QuantumCube.quantumCubeItemName = quantumCubeItemName;
        QuantumCube.quantumCubeItemLore = quantumCubeItemLore;
        QuantumCube.quantumCubeItemTexture = quantumCubeItemTexture;
    }

    public static ItemStack getItem(int amount) {
        ItemStack newQuantumCubeItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta newQuantumCubeItemMeta = newQuantumCubeItem.getItemMeta();
        PlayerProfile dummyPlayer = getDummyProfile("Unused Quantum Cube");

        newQuantumCubeItem.setAmount(amount);
        Main.getInstance().getServer().getConsoleSender().sendMessage(quantumCubeItemTexture);
        newQuantumCubeItemMeta.setDisplayName(quantumCubeItemName);
        newQuantumCubeItemMeta.setLore(Arrays.stream(quantumCubeItemLore.split("\n")).toList());

        try {
            dummyPlayer.getTextures().setSkin(new URI(quantumCubeItemTexture).toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ((SkullMeta) newQuantumCubeItemMeta).setOwnerProfile(dummyPlayer);
        newQuantumCubeItem.setItemMeta(newQuantumCubeItemMeta);
        return newQuantumCubeItem;
    }

    private static PlayerProfile getDummyProfile(String username) {
        return Main.getInstance().getServer().createPlayerProfile(UUID.randomUUID(), username);
    }
}
