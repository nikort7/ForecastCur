package ru.liga.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum InputStart {
    RATE(1);

    InputStart(Integer idInputStart) {
    }

    public static boolean checkInputStart(String inputData) {
        List<String> inputList = Arrays.stream(inputData.split(" ")).toList();
        InputStart inputStart = InputStart.valueOf(inputList.get(0).toUpperCase());

        return true;
    }
}
