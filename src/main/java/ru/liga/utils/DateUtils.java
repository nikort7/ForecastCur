package ru.liga.utils;

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
    private static final Locale RUS_LOCALE = new Locale("ru", "RU");
    private static final String DATE_FORMAT = "dd.MM.yyyy";
    private static final String DATE_FORMAT_WITH_NAME = "EE dd.MM.yyyy";

    private static final String DATE_FORMAT_DAY_MONTH = "dd.MM";

    private static final Integer ONE = 1;


    private static LocalDateTime convertDateToLocalDateTime(Date date) {
        return date.toInstant().atZone(ZoneId.systemDefault()).toLocalDateTime();
    }
    /**
     * Получение даты, следующей за передаваемой, в русской локали
     *
     * @param dateParam Дата из файла
     * @return Результат пробразования даты
     * @throws ParseException Если есть проблема при преобразовании даты
     */
    public static String getDateInRus(String dateParam) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(dateParam);
        LocalDateTime localDateTime = convertDateToLocalDateTime(date);
        localDateTime = localDateTime.plusDays(ONE);

        String result = localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT, RUS_LOCALE));
        return result;
    }


    public static String getDateInRusWithName(String dateParam) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(dateParam);
        LocalDateTime localDateTime = convertDateToLocalDateTime(date);
        String result = localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_WITH_NAME, RUS_LOCALE));
        return result.substring(0, 1).toUpperCase() + result.substring(ONE);
    }

    public static Integer getDifferenceDays(String date) {//todo название метода не отражает сути. По имени ожидается, что метод ожидает 2 аргумента, а получается, что внутри есть какая-та логика поиска точки отсчета. Назови метод, чтобы было понятно, что вычисляется разница даты, которая передается в аргументах и текущей даты
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate startDate = LocalDate.parse(date, formatter);
        LocalDate currentDate = LocalDate.now();

        long resultDays;
        if (currentDate.isAfter(startDate)) {
            resultDays = ChronoUnit.DAYS.between(startDate, currentDate);
        }
        else {
            resultDays = ChronoUnit.DAYS.between(currentDate, startDate);
        }//todo if/else вынести в отдельный метод

        return Math.toIntExact(resultDays);
    }

    public static String getNowDateLastYear() {
        LocalDate currentDate = LocalDate.now();
        LocalDateTime localDateTime = currentDate.atStartOfDay();
        localDateTime =  localDateTime.minusYears(1);
        String result = localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT, RUS_LOCALE));
        return result;
    }

    public static String getDateLastMonth(List<CurrencyRateDto> currencyList, String date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern(DATE_FORMAT);
        LocalDate localDate = LocalDate.parse(date, formatter);
        LocalDateTime localDateTime = localDate.atStartOfDay();
        localDateTime =  localDateTime.minusMonths(1);
        String resultDate = "";
        int findDateInList = 0;
        while (findDateInList == 0) {
            resultDate = localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT, RUS_LOCALE));

            String finalResultDate = resultDate;
            Optional<CurrencyRateDto> c = currencyList.stream()
                    .filter(currency -> currency.getDate().equals(finalResultDate))
                    .findAny();

            if (c.isEmpty()) {
                localDateTime =  localDateTime.minusDays(ONE);
            }
            else {
                findDateInList = ONE;
            }
        }
        return resultDate;
    }

    public static Double getDayOfMonth(String dateParam) {
        Date date;
        try {
            date = new SimpleDateFormat(DATE_FORMAT).parse(dateParam);
        } catch (ParseException e) {
            throw new RuntimeException(e);
        }
        LocalDateTime localDateTime = convertDateToLocalDateTime(date);
        return Double.valueOf(localDateTime.getDayOfMonth());
    }

    public static String getDateWithoutYear(String dateParam) throws ParseException {
        Date date = new SimpleDateFormat(DATE_FORMAT).parse(dateParam);
        LocalDateTime localDateTime = convertDateToLocalDateTime(date);
        String result = localDateTime.format(DateTimeFormatter.ofPattern(DATE_FORMAT_DAY_MONTH, RUS_LOCALE));
        return result;
    }

    public static String getCurrentDate() {
        LocalDate currentDate = LocalDate.now();
        String result = currentDate.format(DateTimeFormatter.ofPattern(DATE_FORMAT, RUS_LOCALE));
        return result;
    }
}
