package com.company;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.SocketException;

/**
 * @Author: hanieh Moafi
 * @Date: 8/29/2020
 **/
public class ClientA {
    private DatagramSocket clientSocket;
    private static Menu menu;
    private static int menuResult;

    public ClientA(int portNumber) throws SocketException {
        clientSocket = new DatagramSocket(portNumber);
    }

    public static void main(String[] args) {
        menu = new Menu();
        menuResult = menu.showMenu();
        if (menuResult == 1) {
            ClientA client = null;
            try {
                client = new ClientA(17000);
                client.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }

    }

    public void run() throws IOException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));

        while (true) {
            System.out.println("Please enter something");
            String line = consoleReader.readLine();
            DatagramPacket packet = new DatagramPacket(line.getBytes(), line.getBytes().length,
                    InetAddress.getByName("localhost"), 20000);
            clientSocket.send(packet);

            if (line.startsWith("exit"))
                break;

            byte[] buffer = new byte[1024];
            DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
            clientSocket.receive(receivedPacket);

            System.out.println(new String(receivedPacket.getData()));
        }

        clientSocket.close();
    }
}
