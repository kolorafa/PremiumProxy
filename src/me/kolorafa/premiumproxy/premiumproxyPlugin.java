/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.ProxySelector;
import java.net.URL;
import java.util.ArrayList;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginDescriptionFile;
import org.bukkit.plugin.java.JavaPlugin;

/**
 *
 * @author kolorafa
 */
public class premiumproxyPlugin extends JavaPlugin {

    public final Logger logger = Logger.getLogger("Minecraft");
    PluginDescriptionFile pdffile;

    public void log(String text) {
        if (getConfig().getBoolean("debug")) {
            logger.log(Level.INFO, "[" + pdffile.getName() + "] DEBUG: " + text);
        }
    }
    premiumYesServer yesserver;
    MinecraftProxySelector ps;

    @Override
    public void onDisable() {
        if (ProxySelector.getDefault() == ps) {
            ProxySelector.setDefault(ps.defsel);
        } else {
            getLogger().info("Unable to disable PS");
        }

        yesserver.zamknij();
        if (yesserver.isAlive()) {
            logger.log(Level.INFO, "[" + pdffile.getName() + "] server did not die in time.");
            yesserver.interrupt();
        }
        logger.log(Level.INFO, "[" + pdffile.getName() + "] is disabled.");
    }

    @Override
    public void onEnable() {
        loadConfiguration();
        pdffile = this.getDescription();
        ps = new MinecraftProxySelector(this, ProxySelector.getDefault(), getConfig().getInt("port"));
        ProxySelector.setDefault(ps);
        yesserver = new premiumYesServer(this, getConfig().getInt("port"));
        yesserver.start();
        //getServer().getScheduler().scheduleAsyncDelayedTask(this, yesserver);
        if (getServer().getOnlineMode() == false) {
            log("Server offline, plugin probably will not work at all !");
        }
    }

    private void loadConfiguration() {
        getConfig().options().copyDefaults(true);
        saveConfig();
    }
}
