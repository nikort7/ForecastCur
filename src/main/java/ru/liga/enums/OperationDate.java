package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
public enum OperationDate {
    DATE("date"),
    PERIOD("period");

    private String operationDate;

    OperationDate(String operationDate) {
        this.operationDate = "-" + operationDate;
    }

}
