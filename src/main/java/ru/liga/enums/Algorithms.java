package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Algorithms {
    OLD(0),
    LASTYEAR(1), //todo константы и enum-ы должны быть в snake_case
    MIST(2),
    LINREG(3);


    private int algorithmType;// todo сделай final
}
