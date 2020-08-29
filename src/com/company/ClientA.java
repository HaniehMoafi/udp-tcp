package com.company;

import java.io.*;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.InetAddress;
import java.net.Socket;
import java.util.Objects;

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

    public ClientA(int portNumber) throws IOException {
        clientSocket = new DatagramSocket(portNumber);

    }

    public static void main(String[] args) {
        menu = new Menu();
        menuResult = menu.showMenu();
        menu.showChatMenu("A");
        doTasks();
    }

    private static void doTasks() {
        ClientA client = null;
        try {
            client = new ClientA(17000);
            client.run();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    //connect to server(UDP)
    public void run() throws IOException {

        BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));


        System.out.println("Please enter  destination client name(client a)");
        String line = consoleReader.readLine() + " ";
        DatagramPacket packet = new DatagramPacket(line.getBytes(), line.getBytes().length,
                InetAddress.getByName("localhost"), 20000);
        clientSocket.send(packet);

        byte[] buffer = new byte[1024];
        DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
        clientSocket.receive(receivedPacket);
        System.out.println("___________________________");
        String clientPort = new String(receivedPacket.getData());
        clientPort = clientPort.replaceAll("\\D+", "");
        System.out.println("destination port is:" + clientPort);
        if (Objects.nonNull(clientPort)) {
            socket = new Socket("localhost", Integer.parseInt(clientPort));
            reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
            writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
            System.out.println("Write your message");

            while (true) {
                System.out.println("testtt:");
                String msg = consoleReader.readLine();
                writer.write(msg + "\n");
                writer.flush();
                if (msg.startsWith("exit")) {
                    break;
                }
                msg = reader.readLine();
                System.out.println("Server Response: " + msg);
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

        clientSocket.close();
    }
}
