package com.company;

import java.io.*;
import java.net.*;
import java.util.Objects;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

/**
 * @Author: Dorsa Jelvani
 * @Date: 8/29/2020
 **/

public class ClientC implements Client{

    private DatagramSocket clientSocket;
    private static Menu menu;
    private static int menuResult;

    Socket socket;
    BufferedReader reader;
    BufferedWriter writer;

    ServerSocket serverSocket;
    ExecutorService pool;

    public ClientC(int portNumber) throws IOException {
        clientSocket = new DatagramSocket(portNumber);
    }

    public static void main(String[] args) {
        menu = new Menu();
        menu.showChatMenu("C");
        doTasks();
    }

    private static void doTasks() {
        ClientC client = null;
        try {
            client = new ClientC(3333);
            client.connect();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void connect() {

        final Thread outThread = new Thread() {
            @Override
            public void run() {
                BufferedReader consoleReader = new BufferedReader(new InputStreamReader(System.in));


                System.out.println("Please enter  destination client name(client a)");
                String line = null;
                try {
                    line = consoleReader.readLine() + " ";
                } catch (IOException e) {
                    e.printStackTrace();
                }
                DatagramPacket packet = null;
                try {
                    packet = new DatagramPacket(line.getBytes(), line.getBytes().length,
                            InetAddress.getByName("localhost"), 20000);
                } catch (UnknownHostException e) {
                    e.printStackTrace();
                }
                try {
                    clientSocket.send(packet);
                } catch (IOException e) {
                    e.printStackTrace();
                }

                byte[] buffer = new byte[1024];
                DatagramPacket receivedPacket = new DatagramPacket(buffer, buffer.length);
                try {
                    clientSocket.receive(receivedPacket);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                System.out.println("___________________________");
                String clientPort = new String(receivedPacket.getData());
                clientPort = clientPort.replaceAll("\\D+", "");
                System.out.println("destination port is:" + clientPort);
                if (Objects.nonNull(clientPort)) {
                    try {
                        socket = new Socket("localhost", Integer.parseInt(clientPort));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        reader = new BufferedReader(new InputStreamReader(socket.getInputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    try {
                        writer = new BufferedWriter(new OutputStreamWriter(socket.getOutputStream()));
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    System.out.println("Write your message");

                    while (true) {
                        System.out.println("say:");
                        String msg = null;
                        try {
                            msg = consoleReader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            writer.write(msg + "\n");
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        try {
                            writer.flush();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
                        if (msg.startsWith("exit")) {
                            break;
                        }
                        try {
                            msg = reader.readLine();
                        } catch (IOException e) {
                            e.printStackTrace();
                        }
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
        };
        outThread.start();

        final Thread inThread = new Thread() {
            @Override
            public void run() {
                try {
                    serverSocket = new ServerSocket(3333);
                } catch (IOException e) {
                    e.printStackTrace();
                }
                pool = Executors.newFixedThreadPool(5);

                while (true) {
                    Socket connectionSocket = null;
                    try {
                        connectionSocket = serverSocket.accept();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    ServerThread serverThread = null;
                    try {
                        serverThread = new ServerThread(connectionSocket);
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    pool.execute(serverThread);
                }
            }
        };
        inThread.start();
    }

    @Override
    public void disconnect() {
        try {
            socket.close();
        } catch (IOException e) {
            // TODO Auto-generated catch block
//			e.printStackTrace();
        }
    }

    @Override
    public boolean isConnected() {
        return socket.isConnected() && !socket.isClosed();
    }

    @Override
    public void send(String message) {

    }
}
