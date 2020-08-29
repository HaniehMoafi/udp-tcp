package com.company;

import java.io.*;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Dorsa Jelvani
 * @Date: 8/29/2020
 **/
public class ClientB {

    ServerSocket serverSocket;
    ExecutorService pool;

    public ClientB() throws IOException {
        serverSocket = new ServerSocket(1316);
        pool = Executors.newFixedThreadPool(5);

    }

    public void run() throws IOException {
        while (true) {
            Socket connectionSocket = serverSocket.accept();
            ServerThread serverThread = new ServerThread(connectionSocket);
            pool.execute(serverThread);
        }
    }

    public static void main(String[] args) {
        try {

            ClientB mainServer = new ClientB();
            mainServer.run();

        } catch (IOException e) {
            e.printStackTrace();
        }

    }

}
