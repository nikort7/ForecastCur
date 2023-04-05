package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Algorithms {
    OLD(0),
    LASTYEAR(1),
    MIST(2),
    LINREG(3);


    private int algorithmType;
}
