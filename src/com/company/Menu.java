package com.company;

import java.util.Scanner;

/**
 * @Author: hanieh Moafi
 * @Date: 8/29/2020
 **/
public class Menu {
    Scanner input;

    public Menu() {
        input = new Scanner(System.in);
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
}
