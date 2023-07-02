package me.axyss.quantumcubes;

import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.ProtocolManager;
import com.comphenix.protocol.events.ListenerPriority;
import com.comphenix.protocol.events.PacketAdapter;
import com.comphenix.protocol.events.PacketContainer;
import com.comphenix.protocol.events.PacketEvent;
import com.comphenix.protocol.wrappers.BlockPosition;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.data.BlockData;
import org.bukkit.entity.Player;

public class FakeSign {
    private static final ProtocolManager manager = ProtocolLibrary.getProtocolManager();
    private final Player fooledPlayer;
    private BlockData previousBlock;
    private Location currentLocation;
    private String[] text;
    private static PacketAdapter textReader;

    public FakeSign(Player fooledPlayer) {
        textReader = new PacketAdapter(Main.getInstance(), ListenerPriority.NORMAL, PacketType.Play.Client.UPDATE_SIGN) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                PacketContainer packet = event.getPacket();
                event.setCancelled(true);
                event.getPlayer().sendMessage(packet.getStringArrays().read(0)[0]);
            }
        };
        this.fooledPlayer = fooledPlayer;
        manager.addPacketListener(textReader);
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
        fooledPlayer.sendBlockChange(getLocation(), Material.BIRCH_SIGN.createBlockData());
        fooledPlayer.sendSignChange(getLocation(), new String[] {"", "Introduce a", "MC-Heads ID", ""});
    }

    public void dematerialize(Location location) {
        if (getLocation() == null) {
            throw new IllegalStateException("FakeSign has already been dematerialized.");
        }
        this.currentLocation = null;
        fooledPlayer.sendBlockChange(location, previousBlock);
        previousBlock = null;
        // todo esto puede dar problemillas
    }

    public void forcePlayerToOpen() {
        PacketContainer openSignPacket = new PacketContainer(PacketType.Play.Server.OPEN_SIGN_EDITOR);
        openSignPacket.getBlockPositionModifier().write(0, new BlockPosition(fooledPlayer.getLocation().toVector()));
        openSignPacket.getBooleans().write(0, true);
        manager.sendServerPacket(fooledPlayer, openSignPacket);
    }
}
