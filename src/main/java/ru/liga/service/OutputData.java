package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;

import java.text.ParseException;
import java.util.List;
import java.util.stream.Collectors;

public class OutputData {
    public static void printResultRow(String date, Double avgRate) {
        System.out.println(date + " " + String.format("%.2f",avgRate));
    }

    public static void printResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) throws ParseException {
        if (nextDayParam == 1) {
            String dateInRusWithName = DateUtils.getDateInRusWithName(currencyList.get(0).getDate());
            Double avgRate = Double.valueOf(currencyList.get(0).getCurrency());
            printResultRow(dateInRusWithName, avgRate);
        }
        else {
            List<CurrencyRateDto> resultList = currencyList.stream()
                    .limit(7)
                    .collect(Collectors.toList());
            for (int i = resultList.size() - 1; i >= 0; i--) {
                String dateInRusWithName = DateUtils.getDateInRusWithName(resultList.get(i).getDate());
                Double avgRate = Double.valueOf(resultList.get(i).getCurrency());
                printResultRow(dateInRusWithName, avgRate);
            }
        }
    }
}
