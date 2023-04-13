package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum Algorithms {
    OLD(0),
    LAST_YEAR(1),
    MIST(2),
    LIN_REG(3);//todo не скупись на полные названия переменных, так как в enum важно отразить суть переменной. LINEAR_REGRESSION


    private final int algorithmType;
}
