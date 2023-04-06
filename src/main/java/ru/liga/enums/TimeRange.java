package ru.liga.enums;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum TimeRange {
    TOMORROW(1),
    WEEK(7),
    ANYDATE(0),//todo snake_case
    MONTH(30);

    private int days;

    public static TimeRange getAnyDate(int daysValue) {//todo метод с неочевидным поведением
        ANYDATE.days = daysValue;//todo неочевидное поведение
        return  ANYDATE;
    }

}
