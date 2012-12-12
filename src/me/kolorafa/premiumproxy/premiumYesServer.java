/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.io.*;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kolorafa
 */
public class premiumYesServer extends Thread {

    int port;
    premiumproxyPlugin plugin;
    public boolean pracuj = true;
    public ServerSocket s;
    
    premiumYesServer(premiumproxyPlugin aThis, int aInt) {
        this.port = aInt;
        plugin = aThis;
    }

    public void zamknij(){
        pracuj=false;
        try {
            join(2000);
        } catch (InterruptedException ex) {
            Logger.getLogger(premiumYesServer.class.getName()).log(Level.SEVERE, null, ex);
        }
        try {
            s.close();
        } catch (IOException ex) {
            Logger.getLogger(premiumYesServer.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
    
    @Override
    public void run() {
        String yesString =
                "HTTP/1.1 200 OK\r\n"
                + "Content-Type: text/plain; charset=UTF8\r\n"
                + "Server: premiumProxy\r\n"
                + "Connection: close\r\n\r\n"
                + "YES";
        try {
            if (plugin.getConfig().getString("listenHost").equalsIgnoreCase("off")
                    || plugin.getConfig().getString("listenHost").equalsIgnoreCase("none")
                    || plugin.getConfig().getString("listenHost").equalsIgnoreCase("false")) {
                return;
            }
            s = new ServerSocket(port, 0, InetAddress.getByName(plugin.getConfig().getString("listenHost")));
            while (pracuj) {
                Socket c = s.accept();
                try {
                    BufferedReader input = new BufferedReader(new InputStreamReader(c.getInputStream()));
                    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                    output.write(yesString);
                    input.readLine();
                    while (input.ready()) {
                        input.readLine();
                    }
                    output.close();
                    c.close();
                } catch (IOException ex) {
                    System.out.println(ex);
                }
            }
            s.close();
        } catch (UnknownHostException ex) {
            System.out.println(ex);
        } catch (IOException ex) {
            System.out.println(ex);
        }
    }
}
