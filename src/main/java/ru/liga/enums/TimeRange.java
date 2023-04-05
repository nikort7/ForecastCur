package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeRange {
    TOMORROW(1),
    WEEK(7),
    ANYDATE(0),
    MONTH(30);

    private int days;

    public static TimeRange getAnyDate(int daysValue) {
        ANYDATE.days = daysValue;
        return  ANYDATE;
    }

}
