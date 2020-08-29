package com.company;

import java.io.IOException;
import java.net.*;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;


/**
 * @Author: dorsa jelvani
 * @Date: 8/29/2020
 **/

public class Server {

    DatagramSocket serverSocket;

    public Server() throws IOException {
        serverSocket = new DatagramSocket(20000);
    }

    public void run() throws IOException {
        while (true) {
            byte[] buffer = new byte[1024];
            DatagramPacket packet = new DatagramPacket(buffer, buffer.length);
            System.out.println("Server starts listening");
            serverSocket.receive(packet);

            System.out.println("Packet Data: " + new String(packet.getData()));
            System.out.println("Packet Length: " + packet.getLength());
            System.out.println("Packet Source IP: " + packet.getAddress().getHostAddress());
            System.out.println("Packet Source Port: " + packet.getPort());

            String line = new String(packet.getData());
            String response = null;
            if (line.startsWith("name")) {
                String[] tokens = line.split(" ");
                response = "Welcome " + tokens[1];

            } else if (line.startsWith("upper")) {
                String[] tokens = line.split(" ");
                response = tokens[1].toUpperCase();
            } else {
                response = "server can not process";
            }
            DatagramPacket sendPacket = new DatagramPacket(response.getBytes(), response.getBytes().length,
                    packet.getAddress(), packet.getPort());
            serverSocket.send(sendPacket);
        }
    }

    public static void main(String[] args) {
        try {
            Server server = new Server();
            server.run();
        } catch (SocketException e) {
            e.printStackTrace();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

