package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketEvent;
import me.axyss.quantumcubes.Main;
import me.axyss.quantumcubes.gui.IGui;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.block.sign.Side;
import org.bukkit.entity.Player;
import org.bukkit.event.block.SignChangeEvent;
import org.bukkit.util.Vector;

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
                plugin.getServer().getScheduler().scheduleSyncDelayedTask(plugin, () -> {
                    Bukkit.getServer().getPluginManager().callEvent(
                            new SignChangeEvent(
                                    event.getPacket().getBlockPositionModifier().read(0)
                                            .toLocation(event.getPlayer().getWorld()).getBlock(),
                                    event.getPlayer(),
                                    event.getPacket().getStringArrays().read(0),
                                    Side.FRONT));
                });
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
