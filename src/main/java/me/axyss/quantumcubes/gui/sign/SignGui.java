package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import me.axyss.quantumcubes.Main;
import me.axyss.quantumcubes.data.QuantumCube;
import me.axyss.quantumcubes.data.QuantumCubeArchive;
import me.axyss.quantumcubes.gui.IGui;
import me.axyss.quantumcubes.utils.MCHeadsDatabase;
import org.bukkit.Location;
import org.bukkit.Sound;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

import java.io.IOException;
import java.net.URISyntaxException;

public class SignGui implements IGui {
    FakeSign sign;
    Player player;
    ProtocolManager manager;

    public SignGui(ProtocolManager manager, Player player) {
        this.manager = manager;
        this.player = player;
        sign = new FakeSign(player, this.manager);
    }

    @Override
    public void open() {
        sign.setText(new String[] {"", "Introduce a", "Minecraft-Heads", "Identifier"});
        sign.materialize(getBlindSpotOf(player));
        sign.forcePlayerToOpen();
    }

    @Override
    public void close() {
        sign.dematerialize();
    }

    public static PacketAdapter getPacketAdapter() {
        return new PacketAdapter(Main.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                String signPlayerInput = packet.getStringArrays().read(0)[0];

                if (signPlayerInput == null || signPlayerInput.isBlank()) {
                    event.setCancelled(true);
                } else {
                    QuantumCube quantumCube = QuantumCubeArchive.extractLastPlacedBy(event.getPlayer().getUniqueId());
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        try {
                            quantumCube.applyTexture(MCHeadsDatabase.getMinecraftTexturesLink(Integer.parseInt(signPlayerInput)));
                            event.getPlayer().playSound(event.getPlayer(), Sound.ENTITY_ENDERMAN_TELEPORT, 0.5f, 1.0f);
                        } catch (IOException | URISyntaxException e) {
                            throw new RuntimeException(e);
                        }
                    });

                }
            }
        };
    }

    private static Location getBlindSpotOf(Player player) {
        Vector playerPosition = player.getLocation().toVector();
        Vector playerLookingAt = player.getTargetBlock(null, 5).getLocation().toVector();
        double lengthScalingFactor;

        playerLookingAt.subtract(playerPosition);
        lengthScalingFactor = -2 * (1 / playerLookingAt.length());
        playerLookingAt.multiply(lengthScalingFactor);
        playerLookingAt.add(player.getLocation().toVector());
        return playerLookingAt.toLocation(player.getWorld());
    }
}
