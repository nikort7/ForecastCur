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
        currencyList = currencyList.stream()
                .limit(nextDayParam)
                .collect(Collectors.toList());
        for (int i = currencyList.size() - 1; i >= 0; i--) {
            String dateInRusWithName = DateUtils.getDateInRusWithName(currencyList.get(i).getDate());
            Double avgRate = Double.valueOf(currencyList.get(i).getCurrency());
            printResultRow(dateInRusWithName, avgRate);
        }
    }
}
