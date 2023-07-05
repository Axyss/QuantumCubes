package me.axyss.quantumcubes.gui.sign;

import me.axyss.quantumcubes.gui.IGui;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.util.Vector;

public class SignGui implements IGui {

    @Override
    public void readTextFrom(Player player) {
        FakeSign testSign = new FakeSign(player);
        FakeSign.onSignUpdateDo();
        testSign.setText(new String[] {"", "Introduce a", "Minecraft-Heads", "Identifier"});
        testSign.materialize(getPlayerBlindSpot(player));
        testSign.forcePlayerToOpen();
        try {
            Thread.sleep(2000);
        } catch (java.lang.InterruptedException e) {
            System.out.println("a");
        }
        testSign.dematerialize();

    }

    private static Location getPlayerBlindSpot(Player player) {
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
