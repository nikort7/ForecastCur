package ru.liga.enums;

import lombok.Getter;

import java.util.Arrays;
import java.util.List;

@Getter
public enum InputType {
    TOMORROW(1),
    WEEK(7);

    private Integer idInputType;
    InputType(Integer idInputType) {
        this.idInputType = idInputType;
    }

    public static Integer getInputTypeFromConsole(String inputData) {

        List<String> inputList = Arrays.stream(inputData.split(" ")).toList();
        InputType inputType = InputType.valueOf(inputList.get(2).toUpperCase());

        return inputType.getIdInputType();
    }
}
