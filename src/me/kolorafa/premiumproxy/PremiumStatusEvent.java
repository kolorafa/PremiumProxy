/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author kolorafa
 */
public class PremiumStatusEvent extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    private String playerName;
    private boolean hasPremium;
    private String returned;

    public PremiumStatusEvent(String login, boolean premium, String ret) {
        playerName = login;
        hasPremium = premium;
        returned = ret;
    }

    public String getPlayerName() {
        return playerName;
    }

    public String getReturnedString() {
        return returned;
    }

    public boolean hasPremium() {
        return hasPremium;
    }
}
