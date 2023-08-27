package me.axyss.quantumcubes.data;

import me.axyss.quantumcubes.Main;
import org.bukkit.ChatColor;
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
import java.util.List;
import java.util.UUID;

public class QuantumCube {
    private final Skull quantumCube;

    private QuantumCube(Location skullLocation) {
        quantumCube = (Skull) skullLocation.getBlock().getState();
    }

    public static QuantumCube fromLocation(Location skullLocation) {
        return new QuantumCube(skullLocation);
    }

    public static ItemStack getNew(int amount) {
        ItemStack newQuantumCubeItem = new ItemStack(Material.PLAYER_HEAD);
        ItemMeta newQuantumCubeItemMeta = newQuantumCubeItem.getItemMeta();
        PlayerProfile dummyPlayer = getDummyProfile();

        newQuantumCubeItem.setAmount(amount);
        newQuantumCubeItemMeta.setDisplayName(ChatColor.AQUA + "Quantum Cube");
        newQuantumCubeItemMeta.setLore(List.of("A wierd cube. Some say it", "responds to one's deepest", "heart's desires."));

        try {
            dummyPlayer.getTextures().setSkin(new URI("https://textures.minecraft.net/texture/7999050775dd5a524735284cbbac45aa392c0ac8fa980bd24c331552b654b824").toURL());
        } catch (MalformedURLException | URISyntaxException e) {
            throw new RuntimeException(e);
        }

        ((SkullMeta) newQuantumCubeItemMeta).setOwnerProfile(dummyPlayer);
        newQuantumCubeItem.setItemMeta(newQuantumCubeItemMeta);
        return newQuantumCubeItem;
    }

    private static PlayerProfile getDummyProfile() {
        return Main.getInstance().getServer().createPlayerProfile(UUID.randomUUID(), "");
    }

    public void applyTexture(URL textureLink) {
        PlayerProfile dummyPlayer = getDummyProfile();
        dummyPlayer.getTextures().setSkin(textureLink);
        quantumCube.setOwnerProfile(dummyPlayer);
        quantumCube.update();
    }
}
