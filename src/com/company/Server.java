package com.company;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.DatagramSocket;
import java.net.Socket;
import java.net.SocketException;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;


/**
 * @Author: dorsa jelvani
 * @Date: 8/29/2020
 **/

public class Server {
    DatagramSocket serverSocket;
    List<ClientDTO> clients;

    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;

    public Server() throws IOException {
        serverSocket = new DatagramSocket(20000);
        clients = new ArrayList<>();
        ClientDTO clientA = new ClientDTO("A", 17000, "localhost");
        ClientDTO clientB = new ClientDTO("B", 1316, "localhost");
        ClientDTO clientC = new ClientDTO("C", 60000, "localhost");
        ClientDTO clientD = new ClientDTO("D", 1313, "localhost");
        clients.add(0, clientA);
        clients.add(1, clientB);
        clients.add(2, clientC);
        clients.add(3, clientD);
    }

    private Boolean searchClient(String alias) {
        String result = clients.stream().filter(x -> x.getClientAlias().equals(alias)).map(ClientDTO::getClientPort).toString();
        return Objects.nonNull(result) && !result.isEmpty();
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
            if (line.startsWith("get")) {
                String[] tokens = line.split("\\s+");
                String clientAlias = tokens[1];
                if (searchClient(clientAlias)) {
                    ClientDTO c = clients.stream().filter(x -> x.getClientAlias().equals(clientAlias)).findAny().orElse(null);
                    response = String.valueOf(c.getClientPort());

                    DatagramPacket sendPacket = new DatagramPacket(response.getBytes(), response.getBytes().length,
                            packet.getAddress(), packet.getPort());
                    serverSocket.send(sendPacket);
                } else
                    response = "client not found";
                System.out.println(response);
            } else {
                response = "server can not process";
                System.out.println(response);

            }

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