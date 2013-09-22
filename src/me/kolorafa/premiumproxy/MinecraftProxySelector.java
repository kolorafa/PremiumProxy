/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.io.IOException;
import java.io.UnsupportedEncodingException;
import java.net.*;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;

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

    public static Map<String, List<String>> getUrlParameters(String url)
            throws UnsupportedEncodingException {
        Map<String, List<String>> params = new HashMap<String, List<String>>();
        String[] urlParts = url.split("\\?");
        if (urlParts.length > 1) {
            String query = urlParts[1];
            for (String param : query.split("&")) {
                String pair[] = param.split("=");
                String key = URLDecoder.decode(pair[0], "UTF-8");
                String value = "";
                if (pair.length > 1) {
                    value = URLDecoder.decode(pair[1], "UTF-8");
                }
                List<String> values = params.get(key);
                if (values == null) {
                    values = new ArrayList<String>();
                    params.put(key, values);
                }
                values.add(value);
            }
        }
        return params;
    }

    @Override
    public java.util.List<Proxy> select(URI uri) {
        if (uri == null) {
            throw new IllegalArgumentException("URI can't be null.");
        }
        plugin.log("PremiumProxy URI check: " + uri.toASCIIString());

        if (uri.getScheme().equalsIgnoreCase("socket") && uri.getHost().equalsIgnoreCase("session.minecraft.net")) {
            try {
                Map<String, List<String>> parms = getUrlParameters(uri.toString());
                ArrayList<Proxy> l = new ArrayList<Proxy>();
                SocketAddress addr = new InetSocketAddress(plugin.getConfig().getString("connectHost"), port);
                Proxy proxy = new Proxy(Proxy.Type.SOCKS, addr);
                l.add(proxy);
                plugin.log("redirecting .....");
                return l;
            } catch (UnsupportedEncodingException ex) {
                plugin.log("url error, what the f***");
            }
        }
//        if (uri.getScheme().equalsIgnoreCase("http") && uri.getHost().equalsIgnoreCase("session.minecraft.net")) {
//            try {
//                Map<String, List<String>> parms = getUrlParameters(uri.toString());
//                ArrayList<Proxy> l = new ArrayList<Proxy>();
//
//                plugin.log("select for " + parms.get("user").get(0) + " " + parms.get("serverId").get(0));
//
//                String login = uri.toString();
//                int start = login.indexOf("?user=") + 6;
//                int stop = login.indexOf("&serverId=");
//                login = login.substring(start, stop);
//
//                PremiumProxyAsk ev = new PremiumProxyAsk(login, l, plugin.getConfig().getBoolean("defaultRedirect", true));
//                plugin.getServer().getPluginManager().callEvent(ev);
//                if (ev.getDefaultRedirectToYes()) {
//                    SocketAddress addr = new InetSocketAddress(plugin.getConfig().getString("connectHost"), port);
//                    Proxy proxy = new Proxy(Proxy.Type.HTTP, addr);
//                    l.add(proxy);
//                    plugin.log("redirecting to YES for " + login);
//                }
//                if (!ev.disablePremiumStatusEvent) {
//                    plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new PremiumCheck(plugin, uri));
//                    plugin.log("external check for " + login);
//                }
//                if (!l.isEmpty()) {
//                    return l;
//                }
//            } catch (UnsupportedEncodingException ex) {
//                plugin.log("url error, what the f***");
//            }
//        }
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