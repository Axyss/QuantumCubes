package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import me.axyss.quantumcubes.Main;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

class FakeSign {
    private final ProtocolManager manager;
    private final Player fooledPlayer;
    private BlockData previousBlock;
    private Location currentLocation;
    private String[] text;

    public FakeSign(Player fooledPlayer, ProtocolManager manager) {
        this.fooledPlayer = fooledPlayer;
        this.manager = manager;
    }

    public String[] getText() {
        return text;
    }

    public void setText(String[] lines) {
        this.text = lines;
    }

    public Location getLocation() {
        return currentLocation;
    }

    public void materialize(Location location) {
        if (getLocation() != null) {
            throw new IllegalStateException("FakeSign has already been materialized.");
        }
        this.currentLocation = location;
        previousBlock = fooledPlayer.getWorld().getBlockAt(getLocation()).getBlockData();
        fooledPlayer.sendBlockChange(getLocation(), Material.BIRCH_WALL_SIGN.createBlockData());
        fooledPlayer.sendSignChange(getLocation(), getText());
    }

    public void dematerialize() {
        if (getLocation() == null) {
            throw new IllegalStateException("FakeSign has already been dematerialized.");
        }
        fooledPlayer.sendBlockChange(currentLocation, previousBlock);
        previousBlock = null;
        currentLocation = null;
        // todo this part might be problematic in a scenario where a second player is present
    }

    public void forcePlayerToOpen() {
        PacketContainer openSignPacket = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        openSignPacket.getBlockPositionModifier().write(0, new BlockPosition(getLocation().toVector()));
        openSignPacket.getBooleans().write(0, true);
        manager.sendServerPacket(fooledPlayer, openSignPacket);
    }
}
