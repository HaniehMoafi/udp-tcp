package com.company;


import java.io.*;
import java.net.Socket;

public class ServerThread implements Runnable {
    Socket connectionSocket;
    BufferedReader reader;
    BufferedWriter writer;

    public ServerThread(Socket connectionSocket) throws IOException {
        this.connectionSocket = connectionSocket;
        reader = new BufferedReader(new InputStreamReader(connectionSocket.getInputStream()));
        writer = new BufferedWriter(new OutputStreamWriter(connectionSocket.getOutputStream()));
    }

    @Override
    public void run() {

        while (true) {
            try {
                System.out.println("**chat start**");
                String line = reader.readLine();
                System.out.println("response:  " + line);
                if (line.startsWith("hi ")) {
                    String[] tokens = line.split(" ");
                    String name = tokens[1];
                    writer.write("Hello " + name + "\n");
                    writer.flush();
                } else if (line.startsWith("bye")) {
                    String[] tokens = line.split(" ");
                    String word = tokens[1];
                    writer.write("see you later " + word + "\n");

                    writer.flush();
                } else if (line.startsWith("exit")) {
                    break;
                } else {
                    writer.write("Your keyword is not recognized\n");
                    writer.flush();
                }

            } catch (IOException e) {
                e.printStackTrace();
                break;
            }
        }
        try {
            reader.close();
            writer.close();
            connectionSocket.close();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
