package com.company;

import java.io.*;
import java.net.*;

/**
 * @Author: hanieh Moafi
 * @Date: 8/29/2020
 **/
public class ClientA {
    private DatagramSocket clientSocket;
    private static Menu menu;
    private static int menuResult;

    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;

    public ClientA(int portNumber) throws SocketException {
        clientSocket = new DatagramSocket(portNumber);
    }

    public static void main(String[] args) {
        menu = new Menu();
        menuResult = menu.showMenu();
        doTasks();
    }

    private static void doTasks() {
        ClientA client = null;
        if (menuResult == 1) {
            try {
                client = new ClientA(17000);
                client.run();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        if (menuResult == 2) {
            try {
                String clinetChat = menu.showChatMenu("A");
                client = new ClientA(17000);
                client.run2(clinetChat);
            } catch (SocketException e) {
                e.printStackTrace();
            }
        }
        if (menuResult == 9) {
            menu = new Menu();
            menuResult = menu.showMenu();
            doTasks();
        }
    }

    private void run2(String clientChat) {
        int port = 0;
        if (clientChat.equals("B")) {
            port = 40000;
        } else if (clientChat.equals("C")) {
            port = 60000;
        }
        try {
            socket = new Socket("localhost", port);

            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));

        } catch (IOException e) {
            System.out.println("client " + clientChat + " is down");
            System.exit(0);
        }

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));
        while (true) {
            try {
                System.out.println("Please enter something: ");
                String line = consoleReader.readLine();
                writer.write(line + "\n");
                writer.flush();
                if (line.startsWith("exit")) {
                    break;
                }
                line = reader.readLine();
                System.out.println("Server Response: " + line);
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
        try {
            consoleReader.close();
            reader.close();
            writer.close();
            socket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


    //connect to server(UDP)
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
