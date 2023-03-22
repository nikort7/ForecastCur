package ru.liga.service;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.Date;
import java.util.Locale;

public class DateUtils {

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

        //Day of week and month in Rus
        Locale rusLocale = new Locale("ru", "RU");
        return localDateTime.format(DateTimeFormatter.ofPattern("EE dd.MM.yyyy", rusLocale));
    }
}
