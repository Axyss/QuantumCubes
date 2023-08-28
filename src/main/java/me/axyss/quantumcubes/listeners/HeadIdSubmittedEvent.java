package me.axyss.quantumcubes.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class HeadIdSubmittedEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final int headId;
    private final Player player;
    private boolean isCancelled = false;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public HeadIdSubmittedEvent(int headId, Player player) {
        this.headId = headId;
        this.player = player;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public int getHeadId() {
        return this.headId;
    }

    public Player getPlayer() {
        return this.player;
    }

    @Override
    public boolean isCancelled() {
        return isCancelled;
    }

    @Override
    public void setCancelled(boolean cancel) {
        isCancelled = cancel;
    }
}
