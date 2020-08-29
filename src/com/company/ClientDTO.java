package com.company;

/**
 * @Author: Dorsa Jelvani
 * @Date: 8/29/2020
 **/

public class ClientDTO {

    private String clientAlias;
    private int clientPort;
    private String Ip = "localhost";

    public ClientDTO(String clientAlias, int clientPort, String ip) {
        this.clientAlias = clientAlias;
        this.clientPort = clientPort;
        Ip = ip;
    }

    public String getClientAlias() {
        return clientAlias;
    }

    public void setClientAlias(String clientAlias) {
        this.clientAlias = clientAlias;
    }

    public int getClientPort() {
        return clientPort;
    }

    public void setClientPort(int clientPort) {
        this.clientPort = clientPort;
    }

    public String getIp() {
        return Ip;
    }

    public void setIp(String ip) {
        Ip = ip;
    }
}
