package ru.liga.service;

import java.util.Scanner;

public class InputData {//todo не используется

    public static String inputFromConsole(){//todo не используется
        Scanner inCur = new Scanner(System.in);
        String inputData = inCur.nextLine();
        inCur.close();

        return inputData;
    }
}
