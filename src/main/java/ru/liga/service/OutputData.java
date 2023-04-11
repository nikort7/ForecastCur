package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.utils.DateUtils;

import java.io.ByteArrayOutputStream;
import java.text.MessageFormat;
import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class OutputData {

    public static void printResultRow(String date, Double avgRate) {
        MessageFormat messageFormat = new MessageFormat("{0} {1}");
        Object[] messageArgs = new String[]{date, String.format("%.2f", avgRate)};
        System.out.println(messageFormat.format(messageArgs));
    }

    public static void printResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException {//todo не используется
        currencyList = currencyList.stream()
                .limit(nextDayParam)
                .collect(Collectors.toList());
        for (int i = currencyList.size() - 1; i >= 0; i--) {
            String dateInRusWithName = DateUtils.getDateInRusWithName(currencyList.get(i).getDate());
            Double avgRate = Double.valueOf(currencyList.get(i).getCurrency());
            printResultRow(dateInRusWithName, avgRate);
        }
    }

    public static String printResultToTelegram(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException {
        List<CurrencyRateDto> printedList = currencyList.stream()
                .limit(nextDayParam)
                .collect(Collectors.toList());

        String result = "";
        MessageFormat messageFormat = new MessageFormat("{0} {1} {2}");
        Object[] messageArgs;
        for (int i = printedList.size() - 1; i >= 0; i--) {
            String dateInRusWithName = DateUtils.getDateInRusWithName(printedList.get(i).getDate());
            Double avgRate = Double.valueOf(printedList.get(i).getCurrency());
            messageArgs = new String[]{dateInRusWithName, String.format("%.2f", avgRate), (i == 0 ? "" : "\n")};
            result = new StringBuilder(result).append(messageFormat.format(messageArgs)).toString();
        }
        return result;
    }

    public static void printResultToChart(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException{
        List<CurrencyRateDto> printedList = currencyList.stream()
                .limit(nextDayParam)
                .collect(Collectors.toList());

        TimeSeriesChartService.drawChart(printedList);
    }

    public static ByteArrayOutputStream getResultAsByteArray(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException{
        List<CurrencyRateDto> printedList = currencyList.stream()
                .limit(nextDayParam)
                .collect(Collectors.toList());

        TimeSeriesChartService.drawChart(printedList);
        return TimeSeriesChartService.getByteArrayOutputStream();
    }
}
