package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.time.temporal.ChronoUnit;
import java.util.Date;
import java.util.List;
import java.util.Locale;
import java.util.Optional;

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

        long resultDays = 0;
        if (currentDate.isAfter(startDate)) {
            resultDays = ChronoUnit.DAYS.between(startDate, currentDate);
        }
        else {
            resultDays = ChronoUnit.DAYS.between(currentDate, startDate);
        }

        return Math.toIntExact(resultDays);
    }

    public static String getNowDateLastYear() {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        localDateTime =  localDateTime.minusYears(1);
        String result = localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", rusLocale));

        return result;
    }

    public static String getDateLastMonth(List<CurrencyRateDto> currencyList, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("dd.MM.yyyy");
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        localDateTime =  localDateTime.minusMonths(1);
        String resultDate = "";
        int findDateInList = 0;
        while (findDateInList == 0) {
            resultDate = localDateTime.format(DateTimeFormatter.ofPattern("dd.MM.yyyy", rusLocale));

            String finalResultDate = resultDate;
            Optional<CurrencyRateDto> c = currencyList.stream()
                    .filter(currency -> currency.getDate().equals(finalResultDate))
                    .findAny();

            if (c.isEmpty()) {
                localDateTime =  localDateTime.minusDays(1);
            }
            else {
                findDateInList = 1;
            }
        }
        return resultDate;
    }

    public static Double getDayOfMonth(String dateParam) {
        Date date = null;
        try {
            date = new SimpleDateFormat("dd.MM.yyyy").parse(dateParam);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        // converting date to LocalDateTime
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        return Double.valueOf(localDateTime.getDayOfMonth());
    }

    public static String getDateWithoutYear(String dateParam) throws ParseException {
        Date date = new SimpleDateFormat("dd.MM.yyyy").parse(dateParam);
        // converting date to LocalDateTime
        LocalDateTime localDateTime = date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
        String result = localDateTime.format(DateTimeFormatter.ofPattern("dd.MM", rusLocale));
        return result;
    }
}
