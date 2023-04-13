package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutputErrors {
    INPUT_ERROR("Input error"),
    COMMAND_ERROR("Введена неверная команда"),
    MISSING_VALUES("В команде не хватает значений");


    private String errorMessege;//todo сделай final + ошибка в имени переменной
}
