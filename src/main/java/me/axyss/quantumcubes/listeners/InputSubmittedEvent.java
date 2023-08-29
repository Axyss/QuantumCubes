package me.axyss.quantumcubes.listeners;

import org.bukkit.entity.Player;
import org.bukkit.event.Cancellable;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

import javax.annotation.Nonnull;

public class InputSubmittedEvent extends Event implements Cancellable {
    private static final HandlerList HANDLERS = new HandlerList();
    private final String inputText;
    private final Player player;
    private boolean isCancelled = false;

    public static HandlerList getHandlerList() {
        return HANDLERS;
    }

    public InputSubmittedEvent(String inputText, Player player) {
        this.inputText = inputText;
        this.player = player;
    }

    @Override
    @Nonnull
    public HandlerList getHandlers() {
        return HANDLERS;
    }

    public String getHeadId() {
        return this.inputText;
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
