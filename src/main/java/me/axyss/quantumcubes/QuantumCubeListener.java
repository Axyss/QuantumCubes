package me.axyss.quantumcubes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.*;
import org.bukkit.block.*;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.BlockPlaceEvent;

import java.util.UUID;

import static org.bukkit.Bukkit.getServer;

public class QuantumCubeListener implements Listener {
    @EventHandler
    public void onQuantumCubePlaced(BlockPlaceEvent event) throws InterruptedException {
        if (event.getBlockPlaced().getType() == Material.PLAYER_HEAD) {
            FakeSign testSign = new FakeSign(event.getPlayer());
            testSign.setText(new String[] {"", ""});
            testSign.materialize(event.getPlayer().getLocation());
            testSign.forcePlayerToOpen();

            Skull skull = (Skull) event.getBlockPlaced().getState();
            skull.setOwningPlayer(getServer().getOfflinePlayer(UUID.fromString("d2731bbf-0b6c-4816-ba99-add394b829d2")));
            skull.update();
        }
    }

}
