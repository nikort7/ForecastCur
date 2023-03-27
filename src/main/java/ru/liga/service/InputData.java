package ru.liga.service;

import java.util.Scanner;

public class InputData {

    public static String inputFromConsole(){
        Scanner inCur = new Scanner(System.in);
        String inputData = inCur.nextLine();
        inCur.close();

        return inputData;
    }
}
