package me.axyss.quantumcubes;

import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.profile.PlayerProfile;

import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;

public class QuantumCube {
    private final Skull quantumCube;

    private QuantumCube(Location skullLocation) {
        quantumCube = (Skull) skullLocation.getBlock().getState();
    }

    public static QuantumCube fromLocation(Location skullLocation) {
        return new QuantumCube(skullLocation);
    }

    public static ItemStack giveItem(Player player, int amount) {
        return new ItemStack(Material.ACACIA_BOAT);
    }

    public void applyTexture(URL textureLink) {
        PlayerProfile dummyPlayer = Main.getInstance().getServer().createPlayerProfile(UUID.randomUUID(), "QuantumPlayer");
        dummyPlayer.getTextures().setSkin(textureLink);
        quantumCube.setOwnerProfile(dummyPlayer);
        quantumCube.update();
    }
}
