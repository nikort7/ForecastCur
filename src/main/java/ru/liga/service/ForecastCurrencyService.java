package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import java.text.ParseException;
import java.util.*;


public class ForecastCurrencyService {
    private static final int CONST_ALG_VALUE = 7;

    /**
     * Дозаплнение списка
     * @param currencyRateDtoList
     * @return
     */
    public static List<CurrencyRateDto> completingList(List<CurrencyRateDto> currencyRateDtoList) {
        String date = currencyRateDtoList.get(0).getDate();

        int differenceDays = DateUtils.getDifferenceDays(date);
        if (differenceDays < 0) {
            for (int i = 0; i < Math.abs(differenceDays); i++) {
                currencyRateDtoList.remove(i);
            }
        }
        else {
            ForecastCurrencyService.getForecastResult(currencyRateDtoList, differenceDays);
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
