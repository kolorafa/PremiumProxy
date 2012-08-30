/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.io.IOException;
import java.net.*;
import java.util.ArrayList;

/**
 *
 * @author kolorafa
 */
public class MinecraftProxySelector extends ProxySelector {

    ProxySelector defsel = null;
    int port;
    premiumproxyPlugin plugin;
    
    MinecraftProxySelector(premiumproxyPlugin plug, ProxySelector def, int pint) {
        defsel = def;
        port = pint;
        plugin = plug;
    }

    @Override
    public java.util.List<Proxy> select(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI can't be null.");
        }
        String protocol = uri.getScheme();
        //System.out.println("Download: " + uri);
        if ("http".equalsIgnoreCase(protocol) && "session.minecraft.net".equalsIgnoreCase(uri.getHost())) {
            //System.out.println("Url download: " + uri);
            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new PremiumCheck(plugin, uri));

            ArrayList<Proxy> l = new ArrayList<Proxy>();
            SocketAddress addr = new InetSocketAddress("localhost", port);
            Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
            l.add(proxy);
            return l;
        }
        if (defsel != null) {
            return defsel.select(uri);
        } else {
            ArrayList<Proxy> l = new ArrayList<Proxy>();
            l.add(Proxy.NO_PROXY);
            return l;
        }
    }

    @Override
    public void connectFailed(URI uri, SocketAddress sa, IOException ioe) {
        if (uri == null || sa == null || ioe == null) {
            throw new IllegalArgumentException("Arguments can't be null.");
        }
        defsel.connectFailed(uri, sa, ioe);
    }
}