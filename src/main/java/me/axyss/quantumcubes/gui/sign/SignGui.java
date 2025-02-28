package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.axyss.quantumcubes.gui.IGui;
import me.axyss.quantumcubes.listeners.InputSubmittedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

import java.util.List;

public class SignGui implements IGui {
    private final ProtocolManager manager;
    private final FileConfiguration config;
    private final Player interactingPlayer;
    private final FakeSign sign;

    public SignGui(ProtocolManager manager, FileConfiguration config, Player interactingPlayer) {
        this.manager = manager;
        this.config = config;
        this.interactingPlayer = interactingPlayer;
        sign = new FakeSign(interactingPlayer, this.manager);
    }

    @Override
    public void open() {
        List<String> signtext = config.getStringList("sign-gui-text");
        signtext.add(0, "");
        sign.setText(signtext.toArray(new String[0]));
        sign.materialize(
                getBlindSpotOf(interactingPlayer),
                Material.valueOf(config.getString("sign-type").toUpperCase() + "_WALL_SIGN")
        );
        sign.forcePlayerToOpen();
    }

    @Override
    public void close() {
        sign.dematerialize();
    }

    public static PacketAdapter createPacketAdapter(JavaPlugin plugin) {
        return new PacketAdapter(plugin, ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () ->
                    Bukkit.getServer().getPluginManager().callEvent(
                    new InputSubmittedEvent(
                            event.getPacket().getStringArrays().read(0)[0],
                            event.getPlayer()
                    )));
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
