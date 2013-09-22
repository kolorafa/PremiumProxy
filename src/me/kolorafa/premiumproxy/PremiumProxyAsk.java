/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.net.Proxy;
import java.util.ArrayList;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

/**
 *
 * @author kolorafa
 */
public class PremiumProxyAsk extends Event {

    private static final HandlerList handlers = new HandlerList();

    @Override
    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
    
    private String playerName;
    private String sessId;
    ArrayList<Proxy> proxyList;
    boolean allowToJoin;
    boolean disablePremiumStatusEvent;
    
    public PremiumProxyAsk(String login, ArrayList<Proxy> proxyList, boolean defaultAllow) {
        playerName = login;
        this.proxyList = proxyList;
        this.allowToJoin = defaultAllow;
        disablePremiumStatusEvent=false;
    }

    public PremiumProxyAsk(String login, String sessId, boolean defaultAllow) {
        this.playerName = login;
        this.sessId = sessId;
        this.proxyList = null;
        this.allowToJoin = defaultAllow;
        disablePremiumStatusEvent=false;
    }
    
    public void addProxyToList(Proxy p){
        proxyList.add(p);
    }

    public ArrayList<Proxy> getProxyList(){
        return proxyList;
    }

    public void setDefaultRedirectToYes(boolean allowToJoin){
        this.allowToJoin = allowToJoin;
    }

    public boolean getDefaultRedirectToYes(){
        return this.allowToJoin;
    }

    public void disablePremiumStatusEvent(boolean yes){
        this.disablePremiumStatusEvent = yes;
    }

    public String getPlayerName() {
        return playerName;
    }

}
