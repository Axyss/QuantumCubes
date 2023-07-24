package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.gui.IGui;
import org.bukkit.Location;
import org.bukkit.entity.Player;
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
