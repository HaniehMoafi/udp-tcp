package com.company;

public interface Client {

    void connect();

    void disconnect();

    boolean isConnected();

    void send(final String message);
}