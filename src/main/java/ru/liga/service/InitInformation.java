package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.CurrencyType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class InitInformation {
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
            ForecastCurrencyService.getForecastResult(currencyRateDtoList, differenceDays);
        }

        return currencyRateDtoList;
    }

}
