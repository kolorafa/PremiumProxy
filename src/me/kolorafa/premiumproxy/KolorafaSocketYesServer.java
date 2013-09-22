/*
 * To change this template, choose Tools | Templates
 * and open the template in the editor.
 */
package me.kolorafa.premiumproxy;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.io.UnsupportedEncodingException;
import java.net.InetAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.net.SocketTimeoutException;
import java.net.URLDecoder;
import java.net.UnknownHostException;
import java.nio.charset.Charset;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author kolorafa
 */
public class KolorafaSocketYesServer extends Thread {

    int port;
    premiumproxyPlugin plugin;
    String host;
    public boolean pracuj = true;
    public ServerSocket s;

    KolorafaSocketYesServer(premiumproxyPlugin aThis, int aInt) {
        this.port = aInt;
        plugin = aThis;
        this.host = plugin.getConfig().getString("listenHost");
    }

    KolorafaSocketYesServer(String host, int port) {
        this.port = port;
        this.host = host;
    }

    public void zamknij() {
        pracuj = false;
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

        String socketYesString = "\\x05\\x00\\x05\\x08\\x00\\x01" + yesString;

        try {
            if (host.equalsIgnoreCase("off")
                    || host.equalsIgnoreCase("none")
                    || host.equalsIgnoreCase("false")) {
                return;
            }
            s = new ServerSocket(port, 0, InetAddress.getByName(host));
            while (pracuj) {
                Socket c = s.accept();
                try {
                    BufferedReader input;
                    InputStream is = c.getInputStream();
                    BufferedWriter output = new BufferedWriter(new OutputStreamWriter(c.getOutputStream()));
                    OutputStream os = c.getOutputStream();
                    try {
                        plugin.log("new connection");
                        c.setSoTimeout(10000);
                        if (is.read() != 5) {
                            byte[] x = {0, (byte) 0xFF};
                            os.write(x);
                            throw new SocketTimeoutException("Wrong version");
                        }
                        is.read();
                        int ile;
                        boolean notfound = false;
                        while (is.available() > 0) {
                            ile = is.read();
                            if (ile == 1) {
                                notfound = true;
                            }
                        }
                        if (notfound) {
                            byte[] x = {0, (byte) 0xFF};
                            os.write(x);
                            throw new SocketTimeoutException("without login not found");
                        }
                        os.write(5);
                        os.write(0);
                        os.flush();

                        plugin.log("read command");
                        ile = is.read();
                        if (ile != 5) {
                            byte[] x = {0, 1};
                            os.write(x);
                            plugin.log("Dostalismy: " + String.valueOf(ile));
                            throw new SocketTimeoutException("wtf1");
                        }
                        if (is.read() != 1) {
                            byte[] x = {5, 7};
                            os.write(x);
                            throw new SocketTimeoutException("command not supported");
                        }
                        is.read();
                        if (is.read() != 1) {
                            byte[] x = {5, 8};
                            os.write(x);
                            throw new SocketTimeoutException("address type not supported");
                        }


                        byte ip[] = new byte[6];
                        is.read(ip, 0, 6);

                        os.write(5);
                        os.write(0);
                        os.write(0);
                        os.write(1);
                        os.write(ip, 0, 6);
                        os.flush();
                        plugin.log("Read first line");
                        input = new BufferedReader(new InputStreamReader(is));

                        String pierwszalinia = input.readLine();
                        int from = pierwszalinia.indexOf(" ");
                        int to = pierwszalinia.indexOf(" ", from + 1);
                        if (from == 0 || to == 0) {
                            plugin.log("something wrong with url");
                        }
                        plugin.log(pierwszalinia.substring(from+1, to));
                        Map<String, List<String>> parms = getUrlParameters("http://x" + pierwszalinia.substring(from+1, to));
                        plugin.log("select for " + parms.get("user").get(0) + " " + parms.get("serverId").get(0));
                        while (input.ready()) {
                            input.readLine();
                        }
                        String login = parms.get("user").get(0);
                        PremiumProxyAsk ev = new PremiumProxyAsk(login, parms.get("serverId").get(0), plugin.getConfig().getBoolean("defaultRedirect", true));
                        plugin.getServer().getPluginManager().callEvent(ev);
                        if (ev.getDefaultRedirectToYes()) {
                            plugin.log("sending YES for " + login);
                        }
                        if (!ev.disablePremiumStatusEvent) {
                            InetAddress address = InetAddress.getByName("session.minecraft.net"); 
                            plugin.getServer().getScheduler().scheduleAsyncDelayedTask(plugin, new PremiumCheck(plugin,"http://"+address.getHostAddress()+pierwszalinia.substring(from+1, to)+"&direct=kolorafa", login));
                            plugin.log("external check for " + login);
                        }

                        plugin.log("Write yes string start");
                        output.write(yesString);
                        //output.write(String.valueOf(new Random().nextInt()));
                        output.flush();
                        plugin.log("out close");
                        output.close();
                        c.close();
                    } catch (SocketTimeoutException ex) {
                        plugin.log("Timeout: " + ex.getMessage());
                        output.flush();
                        output.close();
                        c.close();
                    }
                } catch (IOException ex) {
                    plugin.log(ex.getMessage());
                }
            }
            s.close();
        } catch (UnknownHostException ex) {
            plugin.log(ex.getMessage());
        } catch (IOException ex) {
            plugin.log(ex.getMessage());
        }
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
}
