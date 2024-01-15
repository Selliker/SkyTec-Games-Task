package org.example;

import org.example.webSocket.WsServer;

public class App
{
    public static void main( String[] args ) throws Exception {
        new Thread(new WsServer()).start();
    }
}
