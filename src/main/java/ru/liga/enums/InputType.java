package ru.liga.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum InputType {
    TOMORROW("1"),
    WEEK("7");

    private String type;
    InputType(String type) {
        this.type = type;
    }

    public static String getInputTypeFromConsole(String inputData) {

        List<String> inputList = Arrays.stream(inputData.split(" ")).toList();
        InputType currencyType = InputType.valueOf(inputList.get(2).toUpperCase());

        return currencyType.getType();
    }
}
