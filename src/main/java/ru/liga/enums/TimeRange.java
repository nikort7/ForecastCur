package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeRange {
    TOMORROW(1),
    WEEK(7),
    ANY_DATE(0),
    MONTH(30);

    private int days;

    public static TimeRange instalAnyDate(int daysValue) {
        ANY_DATE.days = daysValue;
        return ANY_DATE;
    }

}
