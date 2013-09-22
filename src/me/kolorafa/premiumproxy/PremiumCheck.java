/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.Proxy;
import java.net.URI;
import java.net.URL;
import java.net.URLConnection;
import java.net.URLEncoder;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kolorafa
 */
public class PremiumCheck implements Runnable {

    premiumproxyPlugin plugin;
    String uri;
    String login;

    public PremiumCheck(premiumproxyPlugin plugin, URI uri) {
        this.plugin = plugin;
        this.uri = uri.toString();
        String login = uri.toString();
        int start = login.indexOf("?user=") + 6;
        int stop = login.indexOf("&serverId=");
        this.login = login.substring(start, stop);
    }

    public PremiumCheck(premiumproxyPlugin plugin, String uri, String login) {
        this.plugin = plugin;
        this.uri = uri;
        this.login = login;
    }

    @Override
    public void run() {
        BufferedReader bufferedreader = null;

        try {
            URL url = new URL(uri);
            URLConnection uc = url.openConnection(Proxy.NO_PROXY);
            bufferedreader = new BufferedReader(new InputStreamReader(uc.getInputStream()));
            String s1 = bufferedreader.readLine();


            if (s1.equals("YES")) {
                PremiumStatusEvent ev = new PremiumStatusEvent(login, true, s1);
                plugin.getServer().getPluginManager().callEvent(ev);
                System.out.println(login + " is PREMIUM !");
            } else {
                PremiumStatusEvent ev = new PremiumStatusEvent(login, false, s1);
                plugin.getServer().getPluginManager().callEvent(ev);
                System.out.println(login + " is NON-PREMIUM ! (" + s1 + ")");
            }
        } catch (IOException ex) {
            if (plugin.getConfig().getBoolean("nonPremiumOnIOError")) {
                PremiumStatusEvent ev = new PremiumStatusEvent(login, false, "IOError");
                plugin.getServer().getPluginManager().callEvent(ev);
                System.out.println(login + " is NON-PREMIUM ! (IOError)");
                System.out.println("IOError checking "+ login + " for premium: "+ex.getMessage());
            }else{
                System.out.println("IOError checking "+ login + " for premium: "+ex.getMessage());
            }
        } finally {
            try {
                bufferedreader.close();
            } catch (IOException ex) {
            }
        }
    }
}
