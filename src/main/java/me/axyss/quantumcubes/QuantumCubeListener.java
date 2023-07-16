package me.axyss.quantumcubes;

import me.axyss.quantumcubes.gui.MCHeadsDatabase;
import org.bukkit.Material;
import org.bukkit.block.Skull;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.profile.PlayerProfile;

import java.io.IOException;
import java.net.*;
import java.util.UUID;


public class QuantumCubeListener implements Listener {
    @EventHandler
    public void onQuantumCubePlaced(BlockPlaceEvent event) throws URISyntaxException, IOException {
        if (event.getBlockPlaced().getType() == Material.PLAYER_HEAD) {

            //SignGui gui = new SignGui();
            //gui.readTextFrom(event.getPlayer());

            Skull skull = (Skull) event.getBlockPlaced().getState();
            PlayerProfile dummyPlayer = Main.getInstance().getServer().createPlayerProfile(UUID.randomUUID(), "QuantumPlayer");
            dummyPlayer.getTextures().setSkin(new URI("https://textures.minecraft.net/texture/a7e41e58d6704333dd2bb580beb10d571382264e5f8ea2c9990fb5024a847ee6").toURL());
            skull.setOwnerProfile(dummyPlayer);
            skull.update();

            event.getPlayer().sendMessage(MCHeadsDatabase.getMinecraftTexturesLink(2).toString());

        }
    }
}
