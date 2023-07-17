package me.axyss.quantumcubes.listeners;

import me.axyss.quantumcubes.Main;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.data.QuantumCubeArchive;
import me.axyss.quantumcubes.gui.sign.SignGui;
import me.axyss.quantumcubes.utils.MCHeadsDatabase;
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
    public void onQuantumCubePlaced(BlockPlaceEvent event) {
        if (event.getBlockPlaced().getType() == Material.PLAYER_HEAD) {
            SignGui gui = new SignGui();
            gui.openFor(event.getPlayer());
            QuantumCubeArchive.insert(event.getPlayer().getUniqueId(), QuantumCube.fromLocation(event.getBlockPlaced().getLocation()));
        }
    }
}
