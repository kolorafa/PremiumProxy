/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.io.*;
import java.net.Inet4Address;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.UnknownHostException;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kolorafa
 */
public class PremiumProxyMain {

    
    /**
     * @param args the command line arguments
     */
    public static void main(String[] args) {
        try {
            ServerSocket s = new ServerSocket(1234, 10, Inet4Address.getLocalHost());
            
            while(true){
                Socket c = s.accept();
                BufferedReader input = new BufferedReader(new InputStreamReader(c.getInputStream()));
                BufferedWriter output = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                
            }
        } catch (UnknownHostException ex) {
            Logger.getLogger(PremiumProxyMain.class.getName()).log(Level.SEVERE, null, ex);
        } catch (IOException ex) {
            Logger.getLogger(PremiumProxyMain.class.getName()).log(Level.SEVERE, null, ex);
        }
    }
}
