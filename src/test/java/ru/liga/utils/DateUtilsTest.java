package ru.liga.utils;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.liga.dto.CurrencyRateDto;

import java.text.ParseException;
import java.time.LocalDate;
import java.util.Currency;
import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

public class DateUtilsTest {

    DateUtils dateUtils;

    @BeforeEach
    public void setup() {
        dateUtils = new DateUtils();
    }

    @Test
    public void getDateInRus() {
        try {
            assertThat(DateUtils.getDateInRus("12.04.2023")).isEqualTo("13.04.2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getDateInRusWithName() {
        try {
            assertThat(DateUtils.getDateInRusWithName("13.04.2023")).isEqualTo("Чт 13.04.2023");
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    public void getDifferenceDays() {
        String currentDate = DateUtils.getCurrentDate();
        assertThat(DateUtils.getDifferenceDays(currentDate)).isEqualTo(0);
    }

    @Test
    public void getNowDateLastYear() {
        assertThat(DateUtils.getNowDateLastYear()).isEqualTo("12.04.2022");
    }

    @Test
    public void getDateLastMonth() {
        List<CurrencyRateDto> list = List.of(new CurrencyRateDto("TRY", "14.03.2023", "15,05", "Турецкая лира"));
        assertThat(DateUtils.getDateLastMonth(list, "14.04.2023")).isEqualTo("14.03.2023");
    }

    @Test
    public void getDayOfMonth() {
        assertThat(DateUtils.getDayOfMonth("12.04.2023")).isEqualTo(12);

    }

    @Test
    public void getDateWithoutYear() throws ParseException {
        assertThat(DateUtils.getDateWithoutYear("12.04.2023")).isEqualTo("12.04");
    }

}