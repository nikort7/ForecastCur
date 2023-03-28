package ru.liga.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.Period;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtils {
    //Day of week and month in Rus
    private static final Locale rusLocale = new Locale("ru", "RU");

    /**
     * Получение даты, следующей за передаваемой, в русской локали
     *
     * @param dateParam Дата из файла
     * @return Результат пробразования даты
     * @throws ParseException Если есть проблема при преобразовании даты
     */
    public static String getDateInRus(String dateParam) throws ParseException {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateParam);
        // converting date to LocalDateTime
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        localDateTime = localDateTime.plusDays(1);

        String result = localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", rusLocale));
        return result;
    }

    public static String getDateInRusWithName(String dateParam) throws ParseException {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateParam);
        // converting date to LocalDateTime
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String result = localDateTime.format(DateTimeFormatter.ofPattern("EE dd.MM.yyyy", rusLocale));
        return result.substring(0, 1).toUpperCase() + result.substring(1);
    }

    public static Integer getDifferenceDays(String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate startDate = LocalDate.parse(date, formatter);
        LocalDate currentDate = LocalDate.now();
        Period period = Period.between(startDate, currentDate);
        return period.getDays();
    }
}
