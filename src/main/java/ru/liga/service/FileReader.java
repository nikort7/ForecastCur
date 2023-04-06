package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.CurrencyType;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;


public class FileReader {
    /**
     * Заполнение преременных данными из файлов
     *
     * @throws IOException Если есть проблема при чтении файлов
     */
    public static List<CurrencyRateDto> initInfo(CurrencyType currencyType) {
        FileParseService fileParseService = new FileParseService();
        List<CurrencyRateDto> currencyList = new ArrayList<>();

        try {
            currencyList = fileParseService.readFileCsv(currencyType.getLink());
            currencyList = ForecastCurrencyService.completingList(currencyList);//todo используй стримы лучше

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return currencyList;
    }

}
