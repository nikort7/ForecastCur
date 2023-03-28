package ru.liga.service;

import ru.liga.dto.CurrencyRateDto;
import ru.liga.enums.CurrencyType;
import ru.liga.enums.InputType;

import java.io.*;
import java.text.ParseException;
import java.util.*;


public class ForecastCurrencyService {

    /**
     * Заполнение преременных данными из файлов
     *
     * @throws IOException Если есть проблема при чтении файлов
     */
    private static List<CurrencyRateDto> initInfo(String inputData) {
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
        String date = currencyRateDtoList.get(1).getDate();

        int differenceDays = DateUtils.getDifferenceDays(date);
        getForecastResult(currencyRateDtoList, differenceDays);

        return currencyRateDtoList;
    }

    /**
     * Получение прогноза курса валют
     *
     * @param currencyList Список значений, по которому делам прогноз
     * @param nextDayParam количство дней для заполнения листа
     */
    public static void getForecastResult(List<CurrencyRateDto> currencyList, Integer nextDayParam) {
        int outLoop = 0;
        while (outLoop < nextDayParam) {
            double avgRate = currencyList.stream()
                    .limit(7)
                    .mapToDouble(w -> Double.parseDouble(w.getCurrency()))
                    .sum()
                    / 7;

            try {
                String dateInRus = DateUtils.getDateInRus(currencyList.get(0).getDate());

                currencyList.add(0, new CurrencyRateDto(currencyList.get(0).getNominal(),
                        dateInRus,
                        String.valueOf(avgRate),
                        currencyList.get(0).getCdx()));
                outLoop++;

            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
        }
    }

    /**
     * Получение прогноза
     */
    public static void getForecastResult() throws ParseException {

        String inputDataFromConsole = InputData.inputFromConsole();

        List<CurrencyRateDto> currencyRateDtoList = initInfo(inputDataFromConsole);
        Integer inputType = Integer.valueOf(InputType.getInputTypeFromConsole(inputDataFromConsole));
        if (!currencyRateDtoList.isEmpty()) {
            getForecastResult(currencyRateDtoList, inputType);
            OutputData.printResult(currencyRateDtoList, inputType);
        }
        else {
            System.out.println("Input error");
        }

    }

}
