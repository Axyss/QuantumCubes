package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.axyss.quantumcubes.Main;
import me.axyss.quantumcubes.gui.IGui;
import me.axyss.quantumcubes.listeners.InputSubmittedEvent;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.util.Vector;

public class SignGui implements IGui {
    FakeSign sign;
    Player interactingPlayer;
    ProtocolManager manager;

    public SignGui(ProtocolManager manager, Player interactingPlayer) {
        this.manager = manager;
        this.interactingPlayer = interactingPlayer;
        sign = new FakeSign(interactingPlayer, this.manager);
    }

    @Override
    public void open() {
        sign.setText(new String[] {"", "Introduce a", "Minecraft-Heads", "Identifier"});
        sign.materialize(getBlindSpotOf(interactingPlayer));
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
                try {
                    plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                        Bukkit.getServer().getPluginManager().callEvent(
                                new InputSubmittedEvent(
                                        event.getPacket().getStringArrays().read(0)[0],
                                        event.getPlayer()
                                ));
                    });
                } catch (NumberFormatException ignored) {}
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
