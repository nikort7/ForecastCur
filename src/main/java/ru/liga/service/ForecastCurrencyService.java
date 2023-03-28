package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.CurrencyType;

import java.io.*;
import java.text.ParseException;
import java.util.*;


public class ForecastCurrencyService {
    private static final int CONST_ALG_VALUE = 7;

    /**
     * Заполнение преременных данными из файлов
     *
     * @throws IOException Если есть проблема при чтении файлов
     */
    public static List<CurrencyRateDto> initInfo(String inputData) {
        FileParseService fileParseService = new FileParseService();
        List<CurrencyRateDto> currencyList = new ArrayList<>();
        CurrencyType currencyType = CurrencyType.checkCurrency(inputData);
        try {
            currencyList = fileParseService.readFileCsv(currencyType.getLink());
            currencyList = completingList(currencyList);

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }

    /**
     * Дозаплнение списка
     * @param currencyRateDtoList
     * @return
     */
    private static List<CurrencyRateDto> completingList(List<CurrencyRateDto> currencyRateDtoList) {
        String date = currencyRateDtoList.get(0).getDate();

        int differenceDays = DateUtils.getDifferenceDays(date);
        if (differenceDays < 0) {
            for (int i = 0; i < Math.abs(differenceDays); i++) {
                currencyRateDtoList.remove(i);
            }
        }
        else {
            getForecastResult(currencyRateDtoList, differenceDays);
        }

        return currencyRateDtoList;
    }

    /**
     * Получение прогноза курса валют
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param nextDayParam количство дней для заполнения листа
     */
    public static void getForecastResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) {
        for (int outLoop = 0; outLoop < nextDayParam; outLoop++) {
            double avgRate = currencyList.stream()
                    .limit(CONST_ALG_VALUE)
                    .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                    .sum()
                    / CONST_ALG_VALUE;

            try {
                String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());

                currencyList.add(0, new CurrencyRateDto(currencyList.get(0).getNominal(),
                        dateInRus,
                        String.valueOf(avgRate),
                        currencyList.get(0).getCdx()));

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }
}
