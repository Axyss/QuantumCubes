package me.axyss.quantumcubes.gui.sign;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.block.BlockState;
import org.bukkit.block.TileState;
import org.bukkit.entity.Player;

class FakeSign {
    private final ProtocolManager manager;
    private final Player fooledPlayer;
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

    public void materialize(Location location, Material signType) {
        if (getLocation() != null) {
            throw new IllegalStateException("FakeSign has already been materialized.");
        } else {
            this.currentLocation = location;
            fooledPlayer.sendBlockChange(getLocation(), signType.createBlockData());
            fooledPlayer.sendSignChange(getLocation(), getText());
        }
    }

    public void dematerialize() {
        if (getLocation() == null) {
            throw new IllegalStateException("FakeSign has already been dematerialized.");
        } else {
            Block previousBlock = currentLocation.getWorld().getBlockAt(getLocation());
            fooledPlayer.sendBlockChange(getLocation(), previousBlock.getBlockData());
            previousBlock.getState().update();
            currentLocation = null;
        }
    }

    public void forcePlayerToOpen() {
        PacketContainer openSignPacket = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        openSignPacket.getBlockPositionModifier().write(0, new BlockPosition(getLocation().toVector()));
        openSignPacket.getBooleans().write(0, true);
        manager.sendServerPacket(fooledPlayer, openSignPacket);
    }
}
