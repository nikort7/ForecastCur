package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum InputType {
    TOMORROW(1),
    WEEK(7);

    private int days;

}
