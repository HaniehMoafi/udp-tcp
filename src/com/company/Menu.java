package com.company;

import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

/**
 * @Author: hanieh Moafi
 * @Date: 8/29/2020
 **/
public class Menu {
    Scanner input;
    List<String> clientName;

    public Menu() {
        input = new Scanner(System.in);
        clientName= Arrays.asList("A","B","C");
    }

    public int showMenu() {
        System.out.println("select one:");
        System.out.println("1)connect to server ");
        System.out.println("2)connect to client ");
        int number = input.nextInt();


        switch (number) {
            case 1:
                return 1;
            case 2:
                return 2;
            default:
                System.out.println("wrong selection!");
                return 0;

        }
    }

    public String showChatMenu(String str) {
        if (clientName.contains(str)){
            int index=clientName.indexOf(str);
            for (int i = 0; i <clientName.size() ; i++) {
                if (i!=index) {
                    System.out.println(i + 1 + ") chat with " + clientName.get(i));
                }
            }
            System.out.println("select Number:");
            int number = input.nextInt();
            return clientName.get(number-1);
        }
        return "0";

    }
}
