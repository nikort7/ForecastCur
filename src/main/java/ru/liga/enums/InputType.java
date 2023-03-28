package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
@AllArgsConstructor
public enum InputType {
    TOMORROW(1),
    WEEK(7);

    private int days;

    public static int getInputTypeFromConsole(String inputData) {

        List<String> inputList = Arrays.stream(inputData.split(" ")).toList();
        InputType inputType = InputType.valueOf(inputList.get(2).toUpperCase());

        return inputType.getDays();
    }
}
