package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum CommandOutput {
    OUTPUT("-output");

    CommandOutput(String outputStartId) {
    }
}
