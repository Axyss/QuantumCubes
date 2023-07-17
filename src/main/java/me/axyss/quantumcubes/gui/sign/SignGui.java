package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.ProtocolManager;
import me.axyss.quantumcubes.gui.IGui;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SignGui implements IGui {
    ProtocolManager manager;

    public SignGui(ProtocolManager manager) {
        this.manager = manager;
    }

    @Override
    public void openFor(Player player) {
        FakeSign testSign = new FakeSign(player, manager);
        testSign.setText(new String[] {"", "Introduce a", "Minecraft-Heads", "Identifier"});
        testSign.materialize(getBlindSpotOf(player));
        testSign.forcePlayerToOpen();
        // todo Implement dematerialization feature

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
