package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum OutputType {
    LIST(0),
    GRAPH(1);


    private final int outputTypeId;
}
