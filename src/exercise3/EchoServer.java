/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package exercise3;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.io.PrintWriter;
import java.net.InetSocketAddress;
import java.net.ServerSocket;
import java.net.Socket;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 *
 * @author jonassimonsen
 */
public class EchoServer implements Runnable {

    Socket s;
    PrintWriter out;
    BufferedReader in;
    String echo;
    String reply;

    public static void main(String[] args) throws IOException {
        String ip = "localhost";
        int port = 4321;
        if (args.length == 2) {
            ip = args[0];
            port = Integer.parseInt(args[1]);
        }

        ServerSocket ss = new ServerSocket();
        ss.bind(new InetSocketAddress(ip, port));

        while (true) {
            EchoServer e = new EchoServer(ss.accept());
            String echo;
            Thread t1 = new Thread(e);
            t1.start();
        }
    }

    @Override
    public void run() {
        while (true) {
            try {
                in = new BufferedReader(new InputStreamReader(s.getInputStream()));
                echo = in.readLine();

                out = new PrintWriter(s.getOutputStream(), true);

                Command(echo);

                out.println(reply);
            } catch (IOException ex) {
                Logger.getLogger(EchoServer.class.getName()).log(Level.SEVERE, null, ex);
            }
        }
    }

    private String Command(String echo) {
        reply = echo;
        String[] splitter = echo.split("#");

        switch (splitter[0]) {
            case "UPPER":
                reply = splitter[1].toUpperCase();
                break;
            case "LOWER":
                reply = splitter[1].toLowerCase();
                break;
            case "REVERSE":
                reply = new StringBuilder(splitter[1]).reverse().toString();
                break;
            case "TRANSLATE":
                if (splitter[1].equals("hund")) {
                    reply = "dog";
                    return reply;
                }
                if (splitter[1].equals("kat")) {
                    reply = "cat";
                    return reply;
                }
                reply = "#NOT FOUND";
                break;
            default:
                break;
        }
        return reply;
    }

    private EchoServer(Socket socket) {
        s = socket;
    }
}